/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.exception.NotExistException;
import gov.epa.cef.web.repository.*;
import gov.epa.cef.web.service.dto.*;
import gov.epa.cef.web.service.mapper.*;
import gov.epa.cef.web.util.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import gov.epa.cef.web.exception.ApplicationErrorCode;
import gov.epa.cef.web.exception.ApplicationException;
import gov.epa.cef.web.service.EmissionService;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionFormulaVariableBulkUploadDto;
import gov.epa.cef.web.service.validation.ValidationField;

@Service
public class EmissionServiceImpl implements EmissionService {

    Logger logger = LoggerFactory.getLogger(EmissionServiceImpl.class);

    @Autowired
    private EmissionRepository emissionRepo;

    @Autowired
    private EmissionsByFacilityAndCASRepository emissionsByFacilityAndCASRepo;

    @Autowired
    private EmissionsReportRepository emissionsReportRepo;

    @Autowired
    private ReportingPeriodRepository periodRepo;

    @Autowired
    private ReportHistoryRepository historyRepo;

    @Autowired
    private UnitMeasureCodeRepository uomRepo;

    @Autowired
    private WebfireEmissionFactorRepository webfireEfRepo;

    @Autowired
    private GHGEmissionFactorRepository ghgEfRepo;

    @Autowired
    private EmissionFactorServiceImpl efService;

    @Autowired
    private EmissionsReportStatusServiceImpl reportStatusService;

    @Autowired
    private EmissionMapper emissionMapper;

    @Autowired
    private EmissionFactorMapper emissionFactorMapper;

    @Autowired
    private EmissionsByFacilityAndCASMapper emissionsByFacilityAndCASMapper;

    @Autowired
    private LookupEntityMapper lookupEntityMapper;

    @Autowired
    private EisTriXrefRepository eisTriXrefRepo;

    @Autowired
    private EmissionFormulaVariableRepository variablesRepo;

    @Autowired
    private BulkUploadMapper bulkUploadMapper;

    @Autowired
    private PointSourceSccCodeRepository pointSourceSccCodeRepo;

    @Autowired
    private SLTConfigHelper sltConfigHelper;

    private static final String POINT_EMISSION_RELEASE_POINT = "stack";
    private static final int TWO_DECIMAL_POINTS = 2;

    private enum RETURN_CODE {NO_EMISSIONS_REPORT, NO_EMISSIONS_REPORTED_FOR_CAS, EMISSIONS_FOUND, MULTIPLE_FACILITIES, NO_EIS_FACILITIES}

    /**
     * Create a new emission from a DTO object
     */
    public EmissionDto create(EmissionDto dto) {

        Emission emission = emissionMapper.fromDto(dto);

        emission.getVariables().forEach(v -> {
            v.setEmission(emission);
        });

        emission.setCalculatedEmissionsTons(calculateEmissionTons(emission));

        EmissionDto result = emissionMapper.toDto(emissionRepo.save(emission));

        ReportingPeriod rp = periodRepo.findById(dto.getReportingPeriodId()).orElse(null);

        if (rp != null) {
            if (monthlyReportingEnabledAndYearCheck(rp)) {
                List<ReportingPeriod> periods = periodRepo.findByAnnualReportingPeriodId(rp.getId());

                for (ReportingPeriod p : periods) {
                    Emission newEmission = new Emission(emission.getReportingPeriod(), emission);

                    newEmission.clearId();
                    newEmission.setReportingPeriod(p);
                    newEmission.setAnnualEmission(emission);
                    newEmission.setMonthlyRate(null);
                    newEmission.setTotalEmissions(BigDecimal.ZERO);
                    newEmission.setLastModifiedBy(emission.getLastModifiedBy());
                    newEmission.setLastModifiedDate(emission.getLastModifiedDate());
                    newEmission.setCreatedBy(emission.getCreatedBy());
                    newEmission.setCreatedDate(emission.getCreatedDate());

                    p.getEmissions().add(newEmission);
                }
                periodRepo.saveAll(periods);
            }
        }

        reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(result.getId()), EmissionRepository.class);
        return result;
    }

    @Override
    public EmissionDto retrieveById(Long id) {

        return this.emissionRepo
            .findById(id)
            .map(e -> emissionMapper.toDto(e))
            .orElse(null);
    }

    public EmissionDto retrieveWithVariablesById(Long id) {

        EmissionDto result = this.emissionRepo
                                .findById(id)
                                .map(e -> emissionMapper.toDto(e))
                                .orElse(null);

        // add missing emission factor variables
        if (result.getFormulaIndicator()) {
            List<EmissionFormulaVariableCodeDto> variables = this.efService.parseFormulaVariables(result.getEmissionsFactorFormula());
            List<String> existingVariables = result.getVariables().stream()
                    .map(EmissionFormulaVariableDto::getVariableCode)
                    .map(EmissionFormulaVariableCodeDto::getCode)
                    .collect(Collectors.toList());

            result.getVariables().addAll(variables.stream()
                .filter(v -> !existingVariables.contains(v.getCode()))
                .map(v -> {
                    EmissionFormulaVariableDto dto = new EmissionFormulaVariableDto();
                    dto.setVariableCode(v);
                    return dto;
                }).collect(Collectors.toList()));
        }

        return result;
    }

    /**
     * Update an existing emission from a DTO
     */
    public EmissionDto update(EmissionDto dto) {

        Emission emission = emissionRepo.findById(dto.getId()).orElse(null);
        emissionMapper.updateFromDto(dto, emission);

        // Match up variables with the existing value if it exists to preserve id, created_by, etc.
        List<EmissionFormulaVariable> variables = new ArrayList<>();
        dto.getVariables().forEach(v -> {
            Optional<EmissionFormulaVariable> variable = emission.getVariables().stream().filter(ov -> ov.getId().equals(v.getId())).findFirst();
            if (variable.isPresent()) {
                variables.add(emissionMapper.updateFormulaVariableFromDto(v, variable.get()));
            } else {
                variables.add(emissionMapper.formulaVariableFromDto(v));
            }
        });
        emission.setVariables(variables);

        emission.getVariables().forEach(v -> {
            v.setEmission(emission);
        });

        // update monthly emissions if monthly reporting is enabled
        if (emission != null) {
            PointSourceSccCode procScc = pointSourceSccCodeRepo.findByCode(emission.getReportingPeriod().getEmissionsProcess().getSccCode());
            if (monthlyReportingEnabledAndYearCheck(emission.getReportingPeriod()) && procScc.getMonthlyReporting()) {
                List<Emission> emissions = emissionRepo.findByAnnualEmissionId(emission.getId());
                BigDecimal annualTotalEmissions = null;
                for (Emission em : emissions) {
                    em.setPollutant(emission.getPollutant());
                    em.setEmissionsCalcMethodCode(emission.getEmissionsCalcMethodCode());
                    em.setEmissionsFactor(emission.getEmissionsFactor());
                    em.setFormulaIndicator(emission.getFormulaIndicator());
                    em.setEmissionsFactorFormula(emission.getEmissionsFactorFormula());
                    em.setEmissionsNumeratorUom(emission.getEmissionsNumeratorUom());
                    em.setEmissionsDenominatorUom(emission.getEmissionsDenominatorUom());
                    em.setEmissionsUomCode(emission.getEmissionsUomCode());
                    em.setOverallControlPercent(emission.getOverallControlPercent());
                    //copy total manual entry (I prefer to calculate check box) to monthly emission
                    em.setTotalManualEntry(emission.getTotalManualEntry());

                    // copy over emission formula variables
                    em.getVariables().clear();
                    for (EmissionFormulaVariable variable : emission.getVariables()) {
                        EmissionFormulaVariable var = new EmissionFormulaVariable(em, variable);
                        var.clearId();
                        em.getVariables().add(var);
                    }

                    // clear monthly rates if an EF is entered or if there are no uoms present
                    if (emission.getEmissionsFactor() != null
                        || (emission.getEmissionsNumeratorUom() == null
                            && emission.getEmissionsDenominatorUom() == null)) {

                        em.setMonthlyRate(null);
                    }

                    // sum up annual total emissions because it isn't recalculated on save when monthly reporting is enabled
                    if (em.getReportingPeriod().getCalculationParameterValue() != null && em.getReportingPeriod().getCalculationParameterUom() != null
                        && em.getEmissionsNumeratorUom() != null && em.getEmissionsDenominatorUom() != null) {

                        calculateTotalEmissions(em, em.getReportingPeriod());
                        if (em.getTotalEmissions() != null && !ConstantUtils.SEMIANNUAL.equals(em.getReportingPeriod().getReportingPeriodTypeCode().getShortName())) {
                            annualTotalEmissions = (annualTotalEmissions == null) ? em.getTotalEmissions() : annualTotalEmissions.add(em.getTotalEmissions());
                        }
                    }
                }
                emission.setTotalEmissions(annualTotalEmissions);
                emissionRepo.saveAll(emissions);
            }
        }

        EmissionDto result = emissionMapper.toDto(update(emission));
        reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(result.getId()), EmissionRepository.class);
        return result;
    }

    /**
     * Update an Emission directly and calculate all calculated values
     * @param emission
     * @return
     */
    private Emission update(Emission emission) {

        emission.setCalculatedEmissionsTons(calculateEmissionTons(emission));

        Emission result = emissionRepo.save(emission);
        return result;
    }

    /**
     * Delete an Emission for a given id
     * @param id
     */
    public void delete(Long id) {
        reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(id), EmissionRepository.class);

        Emission e = emissionRepo.findById(id).orElse(null);

        if (e != null) {
            List<Emission> emissions = emissionRepo.findByAnnualEmissionId(id);
            if (!emissions.isEmpty()) {
                List<Long> emissionIdsToDelete = new ArrayList<>();
                for (Emission em : emissions) {
                  emissionIdsToDelete.add(em.getId());
                }
                // Prevents foreign key error in certain situations
                emissionRepo.deleteAllById(emissionIdsToDelete);
            }
        }

        emissionRepo.deleteById(id);
    }

    /**
     * Delete Emissions by report id
     * @param facilitySiteId
     */
    public void deleteByFacilitySite(Long facilitySiteId) {
        variablesRepo.deleteByFacilitySite(facilitySiteId);
        emissionRepo.deleteByFacilitySite(facilitySiteId);
    }

    private void setPreviousEmissionsAndEmissionsFactors(Emission emission, EmissionBulkEntryDto bulkEntryHolderDto){
        bulkEntryHolderDto.setPreviousTotalEmissions(emission.getTotalEmissions());
        bulkEntryHolderDto.setPreviousEmissionsUomCode(emission.getEmissionsUomCode().getDescription());
        bulkEntryHolderDto.setPreviousEmissionsFactor(emission.getEmissionsFactor());
        if(emission.getEmissionsNumeratorUom() != null){
            bulkEntryHolderDto.setPreviousEmissionsNumeratorUomCode(emission.getEmissionsNumeratorUom().getDescription());
        }
        if(emission.getEmissionsDenominatorUom() != null){
            bulkEntryHolderDto.setPreviousEmissionsDenominatorUomCode(emission.getEmissionsDenominatorUom().getDescription());
        }
    }

    /**
     * Retrieve Emissions grouped by Reporting Period for Bulk Entry
     * @param facilitySiteId
     * @return
     */
    public List<EmissionBulkEntryHolderDto> retrieveBulkEntryEmissionsForFacilitySite(Long facilitySiteId) {

        EmissionsReport er = this.emissionsReportRepo.findByFacilitySiteId(facilitySiteId)
            .orElseThrow(() -> new NotExistException("Facility Site", facilitySiteId));

        SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(er.getProgramSystemCode().getCode());

        List<ReportingPeriod> entities;
        Short monthlyInitialYear = sltConfig.getSltMonthlyFuelReportingInitialYear();
        if (Boolean.TRUE.equals(sltConfig.getSltMonthlyFuelReportingEnabled())
            && (monthlyInitialYear == null || er.getYear() >= monthlyInitialYear)) {
            entities = periodRepo.findByFacilitySiteIdAndPeriod(facilitySiteId, ConstantUtils.ANNUAL, false);
        } else {
            entities = periodRepo.findByFacilitySiteId(facilitySiteId);
        }

        entities = entities.stream()
                .filter(rp -> !"PS".equals(rp.getEmissionsProcess().getOperatingStatusCode().getCode())
                        && rp.getReportingPeriodTypeCode().getShortName().equals(ConstantUtils.ANNUAL))
                .filter(rp -> {
                    FacilitySourceTypeCode typeCode = rp.getEmissionsProcess().getEmissionsUnit().getFacilitySite().getFacilitySourceTypeCode();
                    return !"PS".equals(rp.getEmissionsProcess().getEmissionsUnit().getOperatingStatusCode().getCode())
                            || (typeCode != null && ConstantUtils.FACILITY_SOURCE_LANDFILL_CODE.contentEquals(typeCode.getCode()));
                }).collect(Collectors.toList());

        List<EmissionBulkEntryHolderDto> result = emissionMapper.periodToEmissionBulkEntryDtoList(entities);

        if (!entities.isEmpty()) {
            // find the last year reported
            Optional<EmissionsReport> lastReport = emissionsReportRepo.findFirstByMasterFacilityRecordIdAndIsDeletedIsFalseAndYearLessThanOrderByYearDesc(
                    entities.get(0).getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().getMasterFacilityRecord().getId(),
                    entities.get(0).getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear());

            if (lastReport.isPresent()) {
                result.forEach(dto -> {
                    dto.getEmissions().forEach(e -> {
                        List<Emission> oldEntities = emissionRepo.retrieveMatchingForYear(e.getPollutant().getPollutantCode(),
                                dto.getReportingPeriodTypeCode().getCode(),
                                dto.getEmissionsProcessIdentifier(),
                                dto.getUnitIdentifier(),
                                lastReport.get().getEisProgramId(),
                                lastReport.get().getYear());
                        // Add previous emissions values if they exist
                        if (!oldEntities.isEmpty()) {
                            setPreviousEmissionsAndEmissionsFactors(oldEntities.get(0), e);
                        }
                    });
                });
            }
        }

        return result;
    }

    /**
     * Update the total emissions for the provided emissions and recalculate all emissions for the facility
     * @param facilitySiteId
     * @param dtos
     * @return
     */
    public List<EmissionBulkEntryHolderDto> bulkUpdate(Long facilitySiteId, List<EmissionDto> dtos) {

        List<ReportingPeriod> entities = periodRepo.findByFacilitySiteId(facilitySiteId).stream()
                .filter(rp -> !"PS".equals(rp.getEmissionsProcess().getOperatingStatusCode().getCode()))
                .collect(Collectors.toList());

        Map<Long, BigDecimal> updateMap = dtos.stream().collect(Collectors.toMap(EmissionDto::getId, EmissionDto::getTotalEmissions));

        if (!entities.isEmpty()) {
            // find the last year reported
            EmissionsReport lastReport = emissionsReportRepo.findFirstByMasterFacilityRecordIdAndIsDeletedIsFalseAndYearLessThanOrderByYearDesc(
                    entities.get(0).getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().getMasterFacilityRecord().getId(),
                    entities.get(0).getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear()).orElse(null);

            List<EmissionBulkEntryHolderDto> result = entities.stream().map(rp -> {

                EmissionBulkEntryHolderDto rpDto = emissionMapper.periodToEmissionBulkEntryDto(rp);

                List<EmissionBulkEntryDto> emissions = rp.getEmissions().stream().map(emission -> {

                    String calculationFailureMessage = null;

                    // update total emissions when manual entry is enabled
                    if (Boolean.TRUE.equals(emission.getTotalManualEntry())
                            || Boolean.TRUE.equals(emission.getEmissionsCalcMethodCode().getTotalDirectEntry())) {

                        if (updateMap.containsKey(emission.getId())) {

                            emission.setTotalEmissions(updateMap.get(emission.getId()));
                            update(emission);
                        }
                    } else {

                        // recalculate calculated emissions and record if there is an issue
                        try {
                            calculateTotalEmissions(emission, rp);
                            update(emission);
                        } catch (ApplicationException e) {

                            calculationFailureMessage = e.getMessage();
                        }
                    }

                    EmissionBulkEntryDto eDto = emissionMapper.toBulkDto(emission);
                    eDto.setCalculationFailed(calculationFailureMessage != null);
                    eDto.setCalculationFailureMessage(calculationFailureMessage);

                    // find previous reporting period
                    if (lastReport != null) {
                        List<Emission> oldEntities = emissionRepo.retrieveMatchingForYear(eDto.getPollutant().getPollutantCode(),
                                rpDto.getReportingPeriodTypeCode().getCode(),
                                rpDto.getEmissionsProcessIdentifier(),
                                rpDto.getUnitIdentifier(),
                                lastReport.getEisProgramId(),
                                lastReport.getYear());
                        if (!oldEntities.isEmpty()) {
                            eDto.setPreviousTotalEmissions(oldEntities.get(0).getTotalEmissions());
                            eDto.setPreviousEmissionsUomCode(oldEntities.get(0).getEmissionsUomCode().getCode());
                        }
                    }

                    return eDto;
                }).collect(Collectors.toList());

                rpDto.setEmissions(emissions);

                return rpDto;
            }).collect(Collectors.toList());

            return result;

        } else {
            return Collections.emptyList();
        }
    }

    public List<MonthlyReportingDownloadDto> retrieveMonthlyDownLoadDtoListForReport(
            Long reportId, boolean isSemiAnnual) {
        List<Emission> emissionEntities;
        if (isSemiAnnual) {
            emissionEntities = emissionRepo.findAllByReportIdWithOperatingDetails(reportId).stream()
                // Operating Statuses / Landfills filtered in repository
                .filter(e -> isPeriodInFirstHalfOfYear(e.getReportingPeriod())
                    && isInitialMonthOnOrBeforeSelectedMonth(
                        e.getReportingPeriod().getReportingPeriodTypeCode().getShortName(), e.getReportingPeriod()))
                // Sort periods in chronological order
                .sorted(Comparator.comparing(e -> ConstantUtils.SEMIANNUAL_REPORTING_PERIODS.indexOf(
                    e.getReportingPeriod().getReportingPeriodTypeCode().getShortName())))
                .collect(Collectors.toList());

            return emissionMapper.emissionToMonthlyReportingDownloadDtoList(emissionEntities);
        } else {
            emissionEntities = emissionRepo.findAllByReportIdWithOperatingDetails(reportId).stream()
                // Operating Statuses / Landfills filtered in repository
                .filter(e ->
                    !ConstantUtils.SEMIANNUAL.equals(e.getReportingPeriod().getReportingPeriodTypeCode().getShortName())
                    && isInitialMonthOnOrBeforeSelectedMonth(
                    e.getReportingPeriod().getReportingPeriodTypeCode().getShortName(), e.getReportingPeriod()))
                // Sort periods in chronological order
                .sorted(Comparator.comparing(e -> ConstantUtils.ALL_REPORTING_PERIODS.indexOf(
                    e.getReportingPeriod().getReportingPeriodTypeCode().getShortName())))
                .collect(Collectors.toList());

            return emissionMapper.emissionToMonthlyReportingDownloadDtoList(emissionEntities);
        }
    }

    /**
     * Retrieve Emissions grouped by Reporting Period for Monthly Reporting
     * @param facilitySiteId
     * @return
     */
    public List<MonthlyReportingEmissionHolderDto> retrieveMonthlyEmissionsForFacilitySitePeriod(Long facilitySiteId, String period) {

        List<ReportingPeriod> entities = getReportingPeriodsForFacilitySiteAndPeriod(facilitySiteId, period).stream()
            // SCCs filtered in repository
            // op status filtered in repo
            // landfill checked in repo
            .filter(rp -> isInitialMonthOnOrBeforeSelectedMonth(period, rp))
            .collect(Collectors.toList());

        return emissionMapper.periodToMonthlyReportingEmissionDtoList(entities);
    }

    public List<MonthlyReportingEmissionHolderDto> retrieveAllSemiAnnualMonthlyEmissionsForFacilitySite(Long facilitySiteId) {

        List<ReportingPeriod> reportingPeriodEntities =
            periodRepo.findMonthlyOperatingByFacilitySiteId(facilitySiteId, true).stream()
                // SCCs filtered in repository
                .filter(rp -> areUnitAndProcessOperatingOrLandfill(rp) && isPeriodInFirstHalfOfYear(rp)
                              && isInitialMonthOnOrBeforeSelectedMonth(rp.getReportingPeriodTypeCode().getShortName(), rp))
                // sort periods in chronological order
                .sorted(Comparator.comparing(rp -> ConstantUtils.ALL_MONTHS.indexOf(rp.getReportingPeriodTypeCode().getShortName())))
                .collect(Collectors.toList());

        return emissionMapper.periodToMonthlyReportingEmissionDtoList(reportingPeriodEntities);
    }

    public List<MonthlyReportingEmissionHolderDto> retrieveAllAnnualMonthlyEmissionsForFacilitySite(Long facilitySiteId) {

        List<ReportingPeriod> reportingPeriodEntities =
            periodRepo.findMonthlyOperatingByFacilitySiteId(facilitySiteId, true).stream()
                // SCCs filtered in repository
                .filter(rp -> areUnitAndProcessOperatingOrLandfill(rp) && !ConstantUtils.SEMIANNUAL.equals(rp.getReportingPeriodTypeCode().getShortName())
                    && isInitialMonthOnOrBeforeSelectedMonth(rp.getReportingPeriodTypeCode().getShortName(), rp))
                // sort periods in chronological order
                .sorted(Comparator.comparing(rp -> ConstantUtils.ALL_MONTHS.indexOf(rp.getReportingPeriodTypeCode().getShortName())))
                .collect(Collectors.toList());

        return emissionMapper.periodToMonthlyReportingEmissionDtoList(reportingPeriodEntities);
    }

    public List<MonthlyReportingEmissionHolderDto> monthlyUpdateFromPeriod(Long facilitySiteId, String period, List<Emission> emissions) {
        List<EmissionDto> emissionDtos = emissionMapper.toDtoList(emissions);
        return monthlyUpdate(facilitySiteId, period, emissionDtos);
    }

    /**
     * Update the monthly rate and total emissions for the provided emissions
     * @param dtos
     * @return
     */
    public List<MonthlyReportingEmissionHolderDto> monthlyUpdate(Long facilitySiteId, String period, List<EmissionDto> dtos) {

        List<ReportingPeriod> entities = getReportingPeriodsForFacilitySiteAndPeriod(facilitySiteId, period);

        // remove periods where the initial reporting month is after the selected month
        if (!ConstantUtils.ANNUAL.equals(period) && !ConstantUtils.SEMIANNUAL.equals(period)) {
            entities.removeIf(rp -> (ConstantUtils.ALL_MONTHS.contains(rp.getEmissionsProcess().getInitialMonthlyReportingPeriod())
                && ConstantUtils.ALL_MONTHS.indexOf(rp.getEmissionsProcess().getInitialMonthlyReportingPeriod()) > ConstantUtils.ALL_MONTHS.indexOf(period)));
        }

        Map<Long, BigDecimal> updateTotalMap = new HashMap<>();
        Map<Long, BigDecimal> updateMonthlyMap = new HashMap<>();
        List<Long> rpIds = new ArrayList<>();

        for (EmissionDto e : dtos) {
            if (e.getTotalEmissions() != null) {
                updateTotalMap.put(e.getId(), e.getTotalEmissions());
            }
            if (e.getMonthlyRate() != null) {
                updateMonthlyMap.put(e.getId(), e.getMonthlyRate());
            }
            if (!rpIds.contains(e.getReportingPeriodId())) {
                rpIds.add(e.getReportingPeriodId());
            }
        }

        List<Emission> emissionsToSave = new ArrayList<>();
        List<MonthlyReportingEmissionHolderDto> result = entities.stream().map(rp -> {

            MonthlyReportingEmissionHolderDto rpDto = emissionMapper.periodToMonthlyReportingEmissionDto(rp);
            List<MonthlyReportingEmissionDto> emissions;

            if (rpIds.contains(rp.getId())) {

                emissions = rp.getEmissions().stream().map(emission -> {

                    String calculationFailureMessage = null;
                    Long emissionId = emission.getId();
                    boolean calcMethodTotalDirectEntry = emission.getEmissionsCalcMethodCode().getTotalDirectEntry();

                    // update total emissions when manual entry is enabled
                    if (Boolean.TRUE.equals(emission.getTotalManualEntry())
                        || (Boolean.TRUE.equals(calcMethodTotalDirectEntry)
                            && emission.getEmissionsFactor() == null && !updateMonthlyMap.containsKey(emissionId))) {

                        if (updateTotalMap.containsKey(emissionId)) {
                            emission.setTotalEmissions(updateTotalMap.get(emissionId));
                        }
                        emission.setCalculatedEmissionsTons(calculateEmissionTons(emission));

                    } else if (Boolean.TRUE.equals(calcMethodTotalDirectEntry)
                            && emission.getEmissionsFactor() == null && updateMonthlyMap.containsKey(emissionId)) {

                        // set the monthly rate, then recalculate calculated emissions and record if there is an issue
                        emission.setMonthlyRate(updateMonthlyMap.get(emissionId));

                        try {
                            calculateTotalEmissions(emission, rp);
                            emission.setCalculatedEmissionsTons(calculateEmissionTons(emission));
                        } catch (ApplicationException e) {
                            calculationFailureMessage = e.getMessage();
                        }

                    } else {
                        // recalculate calculated emissions and record if there is an issue
                        try {
                            calculateTotalEmissions(emission, rp);
                            emission.setCalculatedEmissionsTons(calculateEmissionTons(emission));
                        } catch (ApplicationException e) {
                            calculationFailureMessage = e.getMessage();
                        }
                    }
                    emissionsToSave.add(emission);

                    // also calculate semiannual and annual emissions by summing all necessary values
                    emissionsToSave.addAll(updateAnnualAndSemiAnnualEmissions(emission));

                    MonthlyReportingEmissionDto eDto = emissionMapper.toMonthlyReportingDto(emission);
                    eDto.setCalculationFailed(calculationFailureMessage != null);
                    eDto.setCalculationFailureMessage(calculationFailureMessage);

                    return eDto;
                }).collect(Collectors.toList());

            }
            else {
                emissions = rp.getEmissions().stream().map(emission -> emissionMapper.toMonthlyReportingDto(emission)).collect(Collectors.toList());
            }
            rpDto.setEmissions(emissions);
            return rpDto;

        }).collect(Collectors.toList());

        emissionRepo.saveAll(emissionsToSave);

        if (dtos.size() > 0 && dtos.get(0) != null && dtos.get(0).getReportingPeriodId() != null) {
            reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(dtos.get(0).getReportingPeriodId()), ReportingPeriodRepository.class);
        }

        return result;
    }

    /**
     * Recalculate the total emissions in tons for all emissions in a report without changing any other values
     * @param reportId
     * @return
     */
    public List<EmissionDto> recalculateEmissionTons(Long reportId) {

        List<Emission> emissions = this.emissionRepo.findAllByReportId(reportId);
        List<Emission> result = new ArrayList<>();

        emissions.forEach(e -> {
            BigDecimal calculatedValue = calculateEmissionTons(e);
            if (!Objects.equals(calculatedValue, e.getCalculatedEmissionsTons())) {
                e.setCalculatedEmissionsTons(calculatedValue);
                result.add(this.emissionRepo.save(e));
            }
        });

        return emissionMapper.toDtoList(result);
    }

    /**
     * Calculate total emissions for an emission. Also calculates emission factor if it uses a formula
     * This method should be used when the Reporting Period in the database should be used for calculations
     * and you have an EmissionDto, probably with values that differ from the ones in the database.
     * @param dto
     * @return
     */
    public EmissionDto calculateTotalEmissions(EmissionDto dto) {

        ReportingPeriod rp = periodRepo.findById(dto.getReportingPeriodId()).orElse(null);

        UnitMeasureCode totalEmissionUom = uomRepo.findById(dto.getEmissionsUomCode().getCode()).orElse(null);
        UnitMeasureCode efNumerator = uomRepo.findById(dto.getEmissionsNumeratorUom().getCode()).orElse(null);
        UnitMeasureCode efDenom = uomRepo.findById(dto.getEmissionsDenominatorUom().getCode()).orElse(null);

        Emission emission = emissionMapper.fromDto(dto);

        emission.setEmissionsUomCode(totalEmissionUom);
        emission.setEmissionsNumeratorUom(efNumerator);
        emission.setEmissionsDenominatorUom(efDenom);

        EmissionDto result = emissionMapper.toDto(calculateTotalEmissions(emission, rp));

        return result;
    }

    /**
     * Calculate total emissions for an emission and reporting period. Also calculates emission factor if it uses a formula
     * This method should be used when you need to specify a Reporting Period with a different throughput or UoM than the
     * one in the database.
     * @param emission
     * @param rp
     * @return
     */
    public Emission calculateTotalEmissions(Emission emission, ReportingPeriod rp) {

        return calculateTotalEmissions(emission, rp, rp.getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear());
    }

    public Boolean checkIfCanConvertUnits(UnitMeasureCode uom1, UnitMeasureCode uom2) {
    /* TODO: Move to CalculationUtils and maybe return boolean primitive;
     * update ReportingPeriodValidatorTest and possibly others */
        List<String> liquidFuelType = Arrays.asList("BBL","VOLUME");
    	Boolean canCalculate = false;
    	// BBL and GAL have different unit types but have conversion to calculate.
    	if (uom1 != null && uom2 != null
    			&& (uom1.getUnitType().equals(uom2.getUnitType())
    			|| (liquidFuelType.contains(uom1.getUnitType()) && liquidFuelType.contains(uom2.getUnitType())))) {
    		canCalculate = true;
    	}
    	return canCalculate;
    }

    /**
     * Calculate emission factor for an emission if a formula is present.
     * This method should be used when you need to calculate the emission factor without calculating the total emissions.
     * @param dto
     * @return
     */
    public EmissionDto calculateEmissionFactorDto(EmissionDto dto) {

        Emission emission = emissionMapper.fromDto(dto);
        return emissionMapper.toDto(calculateEmissionFactor(emission));
    }

    private Emission calculateEmissionFactor(Emission emission) {

        if (emission.getFormulaIndicator()) {
            List<EmissionFormulaVariable> variables = emission.getVariables();

            BigDecimal ef = CalculationUtils.calculateEmissionFormula(emission.getEmissionsFactorFormula(), variables);
            emission.setEmissionsFactor(ef);
        }
        return emission;
    }

    public Emission calculateTotalEmissions(Emission emission, ReportingPeriod rp, Short emissionsReportYear) {

    	UnitMeasureCode totalEmissionUom = emission.getEmissionsUomCode();
        UnitMeasureCode efNumerator = emission.getEmissionsNumeratorUom();
        UnitMeasureCode efDenom = emission.getEmissionsDenominatorUom();

        UnitMeasureCode rpThroughputUom = rp.getCalculationParameterUom();
        BigDecimal rpThroughputValue = rp.getCalculationParameterValue();
        UnitMeasureCode rpFuelUom = rp.getFuelUseUom();
        UnitMeasureCode rpHeatContentUom = rp.getHeatContentUom();
        Boolean canConvert = false;

        PointSourceSccCode scc = pointSourceSccCodeRepo.findById(rp.getEmissionsProcess().getSccCode()).orElse(null);
        Boolean fuelUseSCC = scc != null ? scc.getFuelUseRequired() : false;

        if (rpThroughputValue == null) {
            throw new ApplicationException(ApplicationErrorCode.E_INVALID_ARGUMENT, "Reporting Period Throughput Value must be set.");
        }
        if (rpThroughputUom == null) {
            throw new ApplicationException(ApplicationErrorCode.E_INVALID_ARGUMENT, "Reporting Period Calculation Unit of Measure must be set.");
        }
        if (totalEmissionUom == null) {
            throw new ApplicationException(ApplicationErrorCode.E_INVALID_ARGUMENT, "Total Emissions Unit of Measure must be set.");
        }
        if (efNumerator == null) {
            throw new ApplicationException(ApplicationErrorCode.E_INVALID_ARGUMENT, "Emission Factor Numerator Unit of Measure must be set.");
        }
        if (efDenom == null) {
            throw new ApplicationException(ApplicationErrorCode.E_INVALID_ARGUMENT, "Emission Factor Denominator Unit of Measure must be set.");
        }

        // if throughput uom type does not match ef denominator uom type, need to convert to calculate for fuel use SCCs.
        if (!checkIfCanConvertUnits(rpThroughputUom, efDenom) && (fuelUseSCC || emission.getGhgEfId() != null)) { // All GHG EFs involve fuel

        	// Check if heat content ratio numerator and denominator(fuel) uom can be used to convert units.
        	if (Boolean.TRUE.equals(rpThroughputUom.getFuelUseUom()) || Boolean.TRUE.equals(rpThroughputUom.getHeatContentUom())) {
        		if (rpHeatContentUom != null && rp.getHeatContentValue() != null && rpFuelUom != null) {

	        		if (((checkIfCanConvertUnits(rpHeatContentUom, efDenom) && checkIfCanConvertUnits(rpFuelUom, rpThroughputUom))
	        				|| (checkIfCanConvertUnits(rpFuelUom, efDenom) && checkIfCanConvertUnits(rpHeatContentUom, rpThroughputUom)))) {
        					canConvert = true;
	        		}
        		}
        	}

        	if (!canConvert) {
    			throw new ApplicationException(ApplicationErrorCode.E_INVALID_ARGUMENT,
                        String.format("Reporting Period Calculation Unit of Measure cannot be converted into Emission Factor Denominator Unit of Measure."));
    		}
        }

        if (!totalEmissionUom.getUnitType().equals(efNumerator.getUnitType())) {
            throw new ApplicationException(ApplicationErrorCode.E_INVALID_ARGUMENT,
                    String.format("Emission Factor Numerator Unit of Measure %s cannot be converted into Total Emissions Unit of Measure %s.",
                            efNumerator.getDescription(), totalEmissionUom.getDescription()));
        }

        calculateEmissionFactor(emission);

        // check if the year is divisible by 4 which would make it a leap year
        boolean leapYear = emissionsReportYear % 4 == 0;
        BigDecimal totalEmissions = rpThroughputValue;

        // calculate using heat content ratio conversion
        if (canConvert) {
            // if heat content ratio numerator uom type == ef denominator uom type , and heat content ratio denominator uom type == throughput uom type
            if (checkIfCanConvertUnits(rpHeatContentUom, efDenom)
                && checkIfCanConvertUnits(rpFuelUom, rpThroughputUom)) {

                totalEmissions = totalEmissions.multiply(rp.getHeatContentValue());

                // convert units for throughput to match heat content ratio denominator
                if (!rpFuelUom.getCode().equals(rpThroughputUom.getCode())) {
                    totalEmissions = CalculationUtils.convertUnits(rpThroughputUom.getCalculationVariable(), rpFuelUom.getCalculationVariable(), leapYear).multiply(totalEmissions);
                }

                // convert units for heat content ratio numerator to match ef denominator
                if (!efDenom.getCode().equals(rpHeatContentUom.getCode())) {
                    totalEmissions = CalculationUtils.convertUnits(rpHeatContentUom.getCalculationVariable(), efDenom.getCalculationVariable(), leapYear).multiply(totalEmissions);
                }
            }

            // if heat content ratio denominator uom type == ef denominator uom type, and heat content ratio numerator uom type == throughput uom type
            if (checkIfCanConvertUnits(rpFuelUom, efDenom)
                && checkIfCanConvertUnits(rpHeatContentUom, rpThroughputUom)) {

                totalEmissions = totalEmissions.divide(rp.getHeatContentValue(), MathContext.DECIMAL128);

                // convert units for throughput to match heat content ratio numerator
                if (!rpHeatContentUom.getCode().equals(rpThroughputUom.getCode())) {
                    totalEmissions = CalculationUtils.convertUnits(rpThroughputUom.getCalculationVariable(), rpHeatContentUom.getCalculationVariable(), leapYear).multiply(totalEmissions);
                }

                // convert units for heat content ratio denominator to match ef denominator
                if (!efDenom.getCode().equals(rpFuelUom.getCode())) {
                    totalEmissions = CalculationUtils.convertUnits(rpFuelUom.getCalculationVariable(), efDenom.getCalculationVariable(), leapYear).multiply(totalEmissions);
                }
            }
        }

        // apply emission factor or monthly rate
        boolean noFactorOrRate = false;
        if (emission.getEmissionsFactor() != null) {
            totalEmissions = emission.getEmissionsFactor().multiply(totalEmissions);
        }
        else if (emission.getMonthlyRate() != null) {
            totalEmissions = emission.getMonthlyRate().multiply(totalEmissions);
        }
        else {
            noFactorOrRate = true;
        }


        if (!canConvert) {
            // convert units for ef denominator and throughput
            if (checkIfCanConvertUnits(rpThroughputUom, efDenom)
                && !rpThroughputUom.getCode().equals(efDenom.getCode())) {
                totalEmissions = CalculationUtils.convertUnits(rpThroughputUom.getCalculationVariable(), efDenom.getCalculationVariable(), leapYear).multiply(totalEmissions);
            }
        }

        // convert units for numerator and total emissions
        if (!totalEmissionUom.getCode().equals(efNumerator.getCode())) {
            totalEmissions = CalculationUtils.convertUnits(efNumerator.getCalculationVariable(), totalEmissionUom.getCalculationVariable(), leapYear).multiply(totalEmissions);
        }

        if (emission.getOverallControlPercent() != null) {
            BigDecimal controlRate = new BigDecimal("100").subtract(emission.getOverallControlPercent()).divide(new BigDecimal("100"));
            totalEmissions = totalEmissions.multiply(controlRate);
        }

        // if there is no factor or monthly rate that can be used for calculation, set total emissions to null
        if (!noFactorOrRate) {
            totalEmissions = CalculationUtils.setSignificantFigures(totalEmissions, CalculationUtils.EMISSIONS_PRECISION);
        }
        else {
            totalEmissions = null;
        }

        emission.setTotalEmissions(totalEmissions);

        return emission;
    }

    /**
     * Find Emission by TRI Facility ID and CAS Number.
     * This method is the second version of the interface to TRIMEweb so that TRI users can
     * see what emissions have been reported to the Common Emissions Form for the current
     * facility and chemical that they are working on. This version takes a TRIFID and looks
     * up the EIS ID in CAERS and then finds any existing emissions.
     *
     * @param trifid
     * @param pollutantCasId
     * @return
     */
    public EmissionsByFacilityAndCASDto findEmissionsByTrifidAndCAS(String trifid, String pollutantCasId) {
        logger.debug("findEmissionsByTrifidAndCAS - Entering");

        EmissionsByFacilityAndCASDto emissionsByFacilityDto = new EmissionsByFacilityAndCASDto();
        Short latestReportYear = null;

        //Get The Corresponding EIS for the TRIFID that was provided
        List<EisTriXref> xrefs = eisTriXrefRepo.findByTrifid(trifid);


        if (xrefs.isEmpty()) {
            logger.debug("findEmissionsByTrifidAndCAS - No corresponding EIS ID found for TRIFID - returning empty");
            String noEISFacilityMessage = "No corresponding EIS ID found for TRIFID = ".concat(trifid);
            emissionsByFacilityDto.setMessage(noEISFacilityMessage);
            emissionsByFacilityDto.setCode(RETURN_CODE.NO_EIS_FACILITIES.toString());
            return emissionsByFacilityDto;
        }

        //we should only ever get one eis ID back. If we get multiple back the we don't know which one to lookup.
        //Respond to the calling service with the appropriate message.
        else if (xrefs.size() > 1) {
            logger.debug("findEmissionsByTrifidAndCAS - Found multiple EIS IDs for the given TRIFID - "
            		+ "returning empty data to calling service");
            String multipleFacilitiesMessage = "Found multiple EIS IDs for the provided TRIFID = ".concat(trifid).
            		concat(". Unable to retrieve data.");
            emissionsByFacilityDto.setMessage(multipleFacilitiesMessage);
            emissionsByFacilityDto.setCode(RETURN_CODE.MULTIPLE_FACILITIES.toString());
            return emissionsByFacilityDto;
        }

        //First find the most recent report for the the given facility so we can check THAT report for emissions
        String eisProgramId = xrefs.get(0).getEisId();
        List<EmissionsReport> emissionsReports = emissionsReportRepo.findByEisProgramId(eisProgramId, Sort.by(Sort.Direction.DESC, "year"));
        if (!emissionsReports.isEmpty()) {
            latestReportYear = emissionsReports.get(0).getYear();
        } else {
            logger.debug("findEmissionsByTrifidAndCAS - No Emissions Reports for the given facility - returning empty");
            String noReportsMessage = "No available reports found for TRIFID ID = ".concat(trifid);
            emissionsByFacilityDto.setMessage(noReportsMessage);
            emissionsByFacilityDto.setCode(RETURN_CODE.NO_EMISSIONS_REPORT.toString());
            return emissionsByFacilityDto;
        }

        List<EmissionsByFacilityAndCAS> emissionsByFacilityAndCAS =
                emissionsByFacilityAndCASRepo.findByTrifidAndPollutantCasIdAndYear(trifid, pollutantCasId, latestReportYear);

        //if there are any emissions that match the facility and CAS Id for the most recent year,
        //then loop through them and add them to the point / nonPoint totals
        if (emissionsByFacilityAndCAS.isEmpty()) {
            logger.debug("findEmissionsByTrifidAndCAS - No emissions for the given CAS number were reported on the most recent report for the facility");
            String noEmissionsMessage = "There were no emissions reported for the CAS number ".concat(pollutantCasId).
                    concat(" on the most recent emissions report for TRIFID = ").concat(trifid);
            emissionsByFacilityDto.setMessage(noEmissionsMessage);
            emissionsByFacilityDto.setCode(RETURN_CODE.NO_EMISSIONS_REPORTED_FOR_CAS.toString());
            return emissionsByFacilityDto;
        } else {
            logger.debug("findEmissionsByTrifidAndCAS - found {} emission records", emissionsByFacilityAndCAS.size());
            //populate the common parts of the DTO object by mapping the first result.
            //since we're matching on facility and CAS, all of these fields should be the same for each instance of the list
            emissionsByFacilityDto = emissionsByFacilityAndCASMapper.toDto(emissionsByFacilityAndCAS.get(0));

            //query the report history table to find the most recent SUBMITTED date for the report we're returning data for
            emissionsByFacilityDto.setCertificationDate(historyRepo.retrieveMaxSubmissionDateByReportId(emissionsByFacilityDto.getReportId()).orElse(null));

            BigDecimal stackEmissions = new BigDecimal(0).setScale(TWO_DECIMAL_POINTS, RoundingMode.HALF_UP);
            BigDecimal fugitiveEmissions = new BigDecimal(0).setScale(TWO_DECIMAL_POINTS, RoundingMode.HALF_UP);

            for (EmissionsByFacilityAndCAS currentEmissions :emissionsByFacilityAndCAS) {
                //if the release point type is fugitive - add it to the "fugitive" emissions. Otherwise add the amount
                //to the stack release emissions
                if (StringUtils.equalsIgnoreCase(POINT_EMISSION_RELEASE_POINT, currentEmissions.getReleasePointType())) {
                    stackEmissions = stackEmissions.add(currentEmissions.getApportionedEmissions());
                } else {
                    fugitiveEmissions = fugitiveEmissions.add(currentEmissions.getApportionedEmissions());
                }
            }

            //Round both of the values to the nearest hundredth
            emissionsByFacilityDto.setStackEmissions(stackEmissions);
            emissionsByFacilityDto.setFugitiveEmissions(fugitiveEmissions);
            String totalEmissionsMessage = "Found %s stack emissions and %s fugitive emissions for CAS number = %s on"
                    + " the most recent emissions report for TRIFID = %s";
            totalEmissionsMessage = String.format(totalEmissionsMessage, stackEmissions.toPlainString(), fugitiveEmissions.toPlainString(), pollutantCasId, trifid);
            emissionsByFacilityDto.setMessage(totalEmissionsMessage);
            emissionsByFacilityDto.setCode(RETURN_CODE.EMISSIONS_FOUND.toString());
        }

        logger.debug("findEmissionsByTrifidAndCAS - Exiting");
        return emissionsByFacilityDto;
    }

    public BigDecimal calculateEmissionTons(Emission emission) {
        if (emission.getTotalEmissions() != null) {
            try {
                return CalculationUtils.convertMassUnits(emission.getTotalEmissions(),
                    MassUomConversion.valueOf(emission.getEmissionsUomCode().getCode()),
                    MassUomConversion.TON);
            } catch (IllegalArgumentException ex) {
                logger.debug("Could not perform emission conversion. {}", ex.getLocalizedMessage());
                return null;
            }
        }
        return null;
    }


    /**
     * Retrieve a list of emissions for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    public List<EmissionBulkUploadDto> retrieveEmissions(String programSystemCode, Short emissionsReportYear) {
    	List<Emission> emissions = emissionRepo.findByPscAndEmissionsReportYear(programSystemCode, emissionsReportYear);
    	return bulkUploadMapper.emissionToDtoList(emissions);
    }


    /**
     * Retrieve a list of emission formula variables for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    public List<EmissionFormulaVariableBulkUploadDto> retrieveEmissionFormulaVariables(String programSystemCode, Short emissionsReportYear) {
    	List<EmissionFormulaVariable> variables = variablesRepo.findByPscAndEmissionsReportYear(programSystemCode, emissionsReportYear);
    	return bulkUploadMapper.emissionFormulaVariableToDtoList(variables);
    }

    public Emission updateEmissionFactorDetails(Emission emission, EmissionsProcess process, Short emissionsReportYear, ReportCreationContext context) {
    	// Update details if the selected calc method is an EPA emission factor
    	if (emission.getEmissionsCalcMethodCode().getEpaEmissionFactor()) {

            // Get list of all EPA EFs matching SCC/Pollutant/Control Indicator, Exclude Revoked
            List<EmissionFactorDto> efList =
                emissionFactorMapper.toWebfireEfDtoList(
                    webfireEfRepo.findBySccCodePollutantControlIndicator(process.getSccCode(),
                        emission.getPollutant().getPollutantCode(),
                        emission.getEmissionsCalcMethodCode().getControlIndicator())
                    .stream().filter(ef -> !ef.getRevoked()).collect(Collectors.toList()));

            // Update GHG EF details if calc material code exists
            if (doesEmissionContainCalculationMaterialCode(emission) && doesEmissionContainCalculationParameterTypeCode(emission)) {
                // Add all EPA EFs matching Pollutant/Control Indicator/Material/Input Parameter to list, Exclude Revoked
                efList.addAll(emissionFactorMapper.toGHGEfDtoList(
                        ghgEfRepo.findByPollutantThroughputMaterialThroughputTypeControlIndicator(
                            emission.getPollutant().getPollutantCode(),
                            emission.getEmissionsCalcMethodCode().getControlIndicator(),
                            emission.getReportingPeriod().getCalculationMaterialCode().getCode(),
                            emission.getReportingPeriod().getCalculationParameterTypeCode().getCode()))
                    .stream().filter(ef -> !ef.getRevoked()).collect(Collectors.toList()));
            }

	    	if (efList.size() == 1) {

	    		// update ef or formula, uoms, desc since 1:1
                EmissionFactorDto ef = efList.get(0);

	    		boolean sameEf = compareEf(emission, ef);

                String description = ef.getDescription();

	    		if (description != null && description.length() > 99) {
	    			emission.setEmissionsFactorText(description.substring(0,97).concat("..."));
	    		} else {
	    			emission.setEmissionsFactorText(description);
	    		}
	    		if (ef.getFormulaIndicator()) {
	    			emission.setFormulaIndicator(true);
                    String previousFormula = emission.getEmissionsFactorFormula();
	    			emission.setEmissionsFactorFormula(ef.getEmissionFactorFormula());

                    if (previousFormula != null && !previousFormula.trim().equals(ef.getEmissionFactorFormula().trim())) {
                        List<EmissionFormulaVariableCodeDto> variableCodeDtos =
                            this.efService.parseFormulaVariables(emission.getEmissionsFactorFormula());
                        List<String> variableCodesStrs = this.efService.parseFormulaVariables(emission.getEmissionsFactorFormula())
                            .stream()
                            .map(EmissionFormulaVariableCodeDto::getCode)
                            .collect(Collectors.toList());
                        List<String> existingVariables = emission.getVariables().stream()
                            .map(EmissionFormulaVariable::getVariableCode)
                            .map(EmissionFormulaVariableCode::getCode)
                            .collect(Collectors.toList());

                        if ( // Cannot transfer variables if formulas have more than one
                            variableCodesStrs.size() == 1 && existingVariables != null && existingVariables.size() == 1
                            && !variableCodesStrs.contains(existingVariables.get(0))
                            ) {
                                EmissionFormulaVariableCode newEmissionFormulaVariableCode = new EmissionFormulaVariableCode();
                                newEmissionFormulaVariableCode.setCode(variableCodeDtos.get(0).getCode());
                                newEmissionFormulaVariableCode.setDescription(variableCodeDtos.get(0).getDescription());
                                newEmissionFormulaVariableCode.setValidationType(variableCodeDtos.get(0).getValidationType());
                                emission.getVariables().get(0).setVariableCode(newEmissionFormulaVariableCode);
                            }
                    }

	    		} else {
	    			emission.setEmissionsFactor(ef.getEmissionFactor());
	    		}
	    		emission.setEmissionsDenominatorUom(lookupEntityMapper.dtoToUnitMeasureCode(ef.getEmissionsDenominatorUom()));
	    		emission.setEmissionsNumeratorUom(lookupEntityMapper.dtoToUnitMeasureCode(ef.getEmissionsNumeratorUom()));
	    		emission.setEmissionsFactorSource(ef.getSource());
                setEFByType(emission, ef);

	    		if (!emission.getTotalManualEntry()) {
	    			// attempt to recalculate emissions
	    			try {
	        			emission = recalculateTotalEmissions(emission, emissionsReportYear, context);
	        			emission.setCalculatedEmissionsTons(calculateEmissionTons(emission));
	        		// swallow exception if necessary so process can continue
	    			} catch (Exception e) {}
	    		}

	    		if (context != null && !sameEf) {
    				context.addCreationChange(ValidationField.EMISSION_EF, "emission.emissionsFactor.usepa.updated", emission, ReportChangeType.UPDATE);
	    		}

            } else if (efList.size() > 1) {
                for (int i = 0; i < efList.size()-1; i++) {
                    // if everything other than description matches
                    BigDecimal emissionEf = emission.getEmissionsFactor();
                    BigDecimal listEmissionEf = efList.get(i).getEmissionFactor();
                    String emissionFormula = emission.getEmissionsFactorFormula();
                    String listEmissionFormula = efList.get(i).getEmissionFactorFormula();
                    Boolean listEfFormulaInd = efList.get(i).getFormulaIndicator();
                    String listEmissionDesc = efList.get(i).getDescription();

                    if ((emissionEf != null && listEmissionEf != null && listEmissionEf.compareTo(emissionEf) == 0 ||
                        (emissionFormula != null && listEmissionFormula != null &&
                         listEmissionFormula.trim().equals(emissionFormula.trim()) && listEfFormulaInd == emission.getFormulaIndicator())) &&
                         efList.get(i).getEmissionsDenominatorUom().equals(emission.getEmissionsDenominatorUom()) &&
                         efList.get(i).getEmissionsNumeratorUom().equals(emission.getEmissionsNumeratorUom())) {

                        if (listEmissionDesc.length() > 99) {
                            emission.setEmissionsFactorText(listEmissionDesc.substring(0,97).concat("..."));
                        } else {
                            emission.setEmissionsFactorText(listEmissionDesc);
                        }

                        if (listEfFormulaInd) {
                            emission.setFormulaIndicator(true);
                            emission.setEmissionsFactorFormula(listEmissionFormula);

                        } else {
                            emission.setEmissionsFactor(listEmissionEf);
                        }
                        emission.setEmissionsFactorSource(efList.get(i).getSource());
                        setEFByType(emission, efList.get(i));

                        if (!emission.getTotalManualEntry()) {
                            // attempt to recalculate emissions
                            try {
                                emission = recalculateTotalEmissions(emission, emissionsReportYear, context);
                                emission.setCalculatedEmissionsTons(calculateEmissionTons(emission));

                            // swallow exception if necessary so process can continue
                            } catch (Exception e) {}
                        }

                        break;
	    			}
				}
	    	}
	    // attempt to recalculate emissions if ef is used and total emissions is not manually entered
    	} else if(!emission.getTotalManualEntry() && !emission.getEmissionsCalcMethodCode().getTotalDirectEntry()) {
    		emission = recalculateTotalEmissions(emission, emissionsReportYear, context);
    	}

    	return emission;
    }

    public Emission recalculateTotalEmissions(Emission emission, Short reportYear, ReportCreationContext context) {
    	BigDecimal enteredTotalEmission = emission.getTotalEmissions();
    	boolean emissionsRecalculated = false;
    	// attempt to recalculate emissions
		try {
			emission = calculateTotalEmissions(emission, emission.getReportingPeriod(), reportYear);
	    	emissionsRecalculated = true;
		} catch (Exception e) {
			logger.debug("Could not recalculate total emissions. {}", e.getLocalizedMessage());
		}

		// add recalculation log to report creation log when total emissions value has been updated
		if (emissionsRecalculated && enteredTotalEmission.compareTo(emission.getTotalEmissions()) != 0) {
			context.addCreationChange(ValidationField.EMISSION_EF, "emission.totalEmissions.recalculated", emission, ReportChangeType.UPDATE);
		}

    	return emission;
    }

    public List<Emission> updateAnnualAndSemiAnnualEmissions(Emission emission) {
        List<Emission> allEmissions;
        Emission annualEmission;
        Emission semiAnnualEmission = new Emission();
        List<Emission> returnList = new ArrayList<>();

        if (emission.getAnnualEmission() != null) {
            allEmissions = emissionRepo.findByAnnualEmissionId(emission.getAnnualEmission().getId());
            annualEmission = emissionRepo.findById(emission.getAnnualEmission().getId()).orElse(null);
        }
        else {
            allEmissions = emissionRepo.findByAnnualEmissionId(emission.getId());
            annualEmission = emission;
        }

        // track each individually just in case emissions aren't in chronological order
        BigDecimal annualTotal = BigDecimal.ZERO;
        BigDecimal semiAnnualTotal = BigDecimal.ZERO;

        BigDecimal annualMonthlyRate = BigDecimal.ZERO;
        BigDecimal semiAnnualMonthlyRate = BigDecimal.ZERO;

        BigDecimal semiAnnualMonthlyValueCount = BigDecimal.ZERO;
        BigDecimal annualMonthlyValueCount = BigDecimal.ZERO;

        for (Emission e : allEmissions) {

            String periodName = e.getReportingPeriod().getReportingPeriodTypeCode().getShortName();
            String initialReportingMonth = e.getReportingPeriod().getEmissionsProcess().getInitialMonthlyReportingPeriod();
            BigDecimal totalEmissions = e.getTotalEmissions();
            BigDecimal monthlyRate = e.getMonthlyRate();

            if (!ConstantUtils.SEMIANNUAL.equals(periodName) && !ConstantUtils.ANNUAL.equals(periodName)
                && initialReportingMonth != null && ConstantUtils.ALL_MONTHS.indexOf(initialReportingMonth) <= ConstantUtils.ALL_MONTHS.indexOf(periodName)
                && totalEmissions != null) {

                annualTotal = annualTotal.add(totalEmissions);
                if (monthlyRate != null) {
                    annualMonthlyValueCount = annualMonthlyValueCount.add(BigDecimal.ONE);
                    annualMonthlyRate = annualMonthlyRate.add(monthlyRate);
                }

                if (ConstantUtils.SEMI_ANNUAL_MONTHS.contains(periodName)) {
                    semiAnnualTotal = semiAnnualTotal.add(totalEmissions);
                    if (monthlyRate != null) {
                        semiAnnualMonthlyValueCount = semiAnnualMonthlyValueCount.add(BigDecimal.ONE);
                        semiAnnualMonthlyRate = semiAnnualMonthlyRate.add(monthlyRate);
                    }
                }
            }

            if (ConstantUtils.SEMIANNUAL.equals(periodName)) {
                semiAnnualEmission = e;
            }
        }

        // scale used to ensure emissions don't use more than 6 significant figures, per the validation on the monthly reporting UI
        int scale = 6 - semiAnnualTotal.precision() + semiAnnualTotal.scale();
        int rateScale = 6 - semiAnnualMonthlyRate.precision() + semiAnnualMonthlyRate.scale();

        semiAnnualEmission.setTotalEmissions(semiAnnualTotal.setScale(scale, RoundingMode.HALF_UP));
        semiAnnualEmission.setCalculatedEmissionsTons(calculateEmissionTons(semiAnnualEmission));

        if (semiAnnualEmission.getEmissionsCalcMethodCode().getCode().contentEquals(ConstantUtils.CEMS_CODE)) {
            // Sum of monthly rates not needed
            calculateRateForCems(semiAnnualEmission, rateScale);
        }
        else if (
            semiAnnualMonthlyRate.compareTo(BigDecimal.ZERO) == 0
            && !semiAnnualEmission.getEmissionsCalcMethodCode().getCode().contentEquals(ConstantUtils.CEMS_CODE)
        ) {
            semiAnnualEmission.setMonthlyRate(null);
        }
        else {
            semiAnnualEmission.setMonthlyRate(semiAnnualMonthlyRate.divide(semiAnnualMonthlyValueCount, rateScale, RoundingMode.HALF_UP));
        }

        returnList.add(semiAnnualEmission);

        annualEmission.setTotalEmissions(annualTotal.setScale(scale, RoundingMode.HALF_UP));
        annualEmission.setCalculatedEmissionsTons(calculateEmissionTons(annualEmission));

        if (annualEmission.getEmissionsCalcMethodCode().getCode().contentEquals(ConstantUtils.CEMS_CODE)) {
            // Sum of monthly rates not needed
            calculateRateForCems(annualEmission, rateScale);
        }
        else if (
            annualMonthlyRate.compareTo(BigDecimal.ZERO) == 0
            && !annualEmission.getEmissionsCalcMethodCode().getCode().contentEquals(ConstantUtils.CEMS_CODE)
        ) {
            annualEmission.setMonthlyRate(null);
        } else {
            annualEmission.setMonthlyRate(annualMonthlyRate.divide(annualMonthlyValueCount, rateScale, RoundingMode.HALF_UP));
        }

        returnList.add(annualEmission);

        return returnList;
    }

    public List<String> getEmissionsCreatedAfterSemiannualSubmission(Long processId, Long reportId) {
        List<Emission> emissions = emissionRepo.findAllAnnualByProcessIdReportId(processId, reportId);
        Date semiannualSubDate = historyRepo.retrieveMaxSemiannualSubmissionDateByReportId(reportId).orElse(null);

        List<String> returnList = new ArrayList<>();

        if (semiannualSubDate != null) {
            for (Emission e : emissions) {
                if (e.getCreatedDate().after(semiannualSubDate)) {
                    returnList.add(e.getPollutant().getPollutantCode());
                }
            }
        }

        return returnList;
    }

    private boolean compareEf(Emission e, EmissionFactorDto ef) {
        String efText = ef.getDescription();
        if (efText != null && efText.length() > 99) {
            efText = efText.substring(0,97).concat("...");
        }

        String efFormula = ef.getEmissionFactorFormula();
        String emissionFormula = e.getEmissionsFactorFormula();
        BigDecimal efFactor = ef.getEmissionFactor();
        BigDecimal emissionEf = e.getEmissionsFactor();

        return Objects.equals(efText, e.getEmissionsFactorText()) && (
                  (efFormula == null && emissionFormula == null)
                  || (efFormula != null && emissionFormula != null
                    && efFormula.trim().contentEquals(emissionFormula.trim())
                  )
                )
                && Objects.equals(ef.getFormulaIndicator(), e.getFormulaIndicator())
                && (efFactor == null || (emissionEf != null && efFactor.compareTo(emissionEf) == 0))
                && Objects.equals(ef.getEmissionsNumeratorUom(), e.getEmissionsNumeratorUom()) && Objects.equals(ef.getEmissionsDenominatorUom(), e.getEmissionsDenominatorUom());
    }

    private SLTBaseConfig getSltConfig(ReportingPeriod rp) {
        return sltConfigHelper.getCurrentSLTConfig(rp.getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().getMasterFacilityRecord().getProgramSystemCode().getCode());
    }

    private List<ReportingPeriod> getReportingPeriodsForFacilitySiteAndPeriod(Long facilitySiteId, String period) {
        return new ArrayList<>(periodRepo.findOperatingByFacilitySiteIdAndPeriod(facilitySiteId, period, true));
    }

    private boolean monthlyReportingEnabledAndYearCheck(ReportingPeriod rp) {
        SLTBaseConfig sltConfig = getSltConfig(rp);
        Short monthlyInitialYear = sltConfig.getSltMonthlyFuelReportingInitialYear();
        return Boolean.TRUE.equals(sltConfig.getSltMonthlyFuelReportingEnabled())
            && (monthlyInitialYear == null || rp.getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear() >= monthlyInitialYear);
    }

    public boolean isInitialMonthOnOrBeforeSelectedMonth(String selectedMonth, ReportingPeriod rp) {
        return ((ConstantUtils.ALL_MONTHS.indexOf(rp.getEmissionsProcess().getEmissionsUnit().getInitialMonthlyReportingPeriod())
            <= ConstantUtils.ALL_MONTHS.indexOf(selectedMonth))
            || (selectedMonth.equals(ConstantUtils.SEMIANNUAL) && ConstantUtils.ALL_MONTHS.indexOf(rp.getEmissionsProcess().getEmissionsUnit().getInitialMonthlyReportingPeriod()) <= 5)
            || (selectedMonth.equals(ConstantUtils.ANNUAL) && ConstantUtils.ALL_MONTHS.contains(rp.getEmissionsProcess().getEmissionsUnit().getInitialMonthlyReportingPeriod())))
            && ((ConstantUtils.ALL_MONTHS.indexOf(rp.getEmissionsProcess().getInitialMonthlyReportingPeriod())
            <= ConstantUtils.ALL_MONTHS.indexOf(selectedMonth))
            || (selectedMonth.equals(ConstantUtils.SEMIANNUAL) && ConstantUtils.ALL_MONTHS.indexOf(rp.getEmissionsProcess().getInitialMonthlyReportingPeriod()) <= 5)
            || (selectedMonth.equals(ConstantUtils.ANNUAL) && ConstantUtils.ALL_MONTHS.contains(rp.getEmissionsProcess().getInitialMonthlyReportingPeriod())));
    }

    public boolean isPeriodInFirstHalfOfYear(ReportingPeriod rp) {
        return (ConstantUtils.ALL_MONTHS.indexOf(rp.getReportingPeriodTypeCode().getShortName()) <= 5
                && !rp.getReportingPeriodTypeCode().getShortName().equals(ConstantUtils.ANNUAL));
    }

    public boolean areUnitAndProcessOperatingOrLandfill(ReportingPeriod rp) {
        FacilitySourceTypeCode typeCode =
            rp.getEmissionsProcess().getEmissionsUnit().getFacilitySite().getFacilitySourceTypeCode();
        return (
            typeCode != null && ConstantUtils.FACILITY_SOURCE_LANDFILL_CODE.contentEquals(typeCode.getCode())
        ) || (
            ConstantUtils.STATUS_OPERATING.equals(
                rp.getEmissionsProcess().getEmissionsUnit().getOperatingStatusCode().getCode()
            ) && ConstantUtils.STATUS_OPERATING.equals(
                rp.getEmissionsProcess().getOperatingStatusCode().getCode()
            )
        );
    }

    // Semi-Annual and Annual rate calculation for Continuous Emissions Monitoring System (CEMS)
    private void calculateRateForCems(Emission e, int rateScale) {
        if (e.getReportingPeriod().getCalculationParameterValue() == null) {
            e.setMonthlyRate(null);
        } else if (e.getReportingPeriod().getCalculationParameterValue().compareTo(BigDecimal.ZERO) == 0) {
            e.setMonthlyRate(BigDecimal.ZERO); // Prevents arithmeticException (divide-by-zero)
        } else {
            e.setMonthlyRate(e.getTotalEmissions().divide(e.getReportingPeriod().getCalculationParameterValue(), rateScale, RoundingMode.HALF_UP));
        }
    }

    /**
     * Set the Emission Factor DTO or ID depending on EF Type
     * @param emission
     * @param ef
     */
    private void setEFByType(Emission emission, EmissionFactorDto ef) {
        if (ef.getWebfireId() != null) {
            emission.setWebfireEf(emissionFactorMapper.webfireEfFromDto(ef));
        }
        if (ef.getGhgId() != null) {
            emission.setGhgEfId(ef.getGhgId());
        }
    }

    private boolean doesEmissionContainCalculationMaterialCode(Emission emission){
        return emission.getReportingPeriod() != null
            && emission.getReportingPeriod().getCalculationMaterialCode() != null
            && StringUtils.isNotBlank(emission.getReportingPeriod().getCalculationMaterialCode().getCode());
    }

    private boolean doesEmissionContainCalculationParameterTypeCode(Emission emission) {
        return emission.getReportingPeriod() != null
            && emission.getReportingPeriod().getCalculationParameterTypeCode() != null
            && StringUtils.isNotBlank(emission.getReportingPeriod().getCalculationParameterTypeCode().getCode());
    }
}
