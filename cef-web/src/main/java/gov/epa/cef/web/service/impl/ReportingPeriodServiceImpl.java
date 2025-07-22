/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.impl;

import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.exception.ApplicationException;
import gov.epa.cef.web.exception.NotExistException;
import gov.epa.cef.web.repository.*;
import gov.epa.cef.web.service.LookupService;
import gov.epa.cef.web.service.ReportingPeriodService;
import gov.epa.cef.web.service.dto.*;
import gov.epa.cef.web.service.dto.bulkUpload.ReportingPeriodBulkUploadDto;
import gov.epa.cef.web.service.mapper.BulkUploadMapper;
import gov.epa.cef.web.service.mapper.MonthlyReportingPeriodHolderMapper;
import gov.epa.cef.web.service.mapper.ReportingPeriodMapper;
import gov.epa.cef.web.util.CalculationUtils;
import gov.epa.cef.web.util.ConstantUtils;

import gov.epa.cef.web.util.SLTConfigHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportingPeriodServiceImpl implements ReportingPeriodService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ReportingPeriodRepository repo;

    @Autowired
    private EmissionsReportRepository reportRepo;

    @Autowired
    private EmissionsProcessRepository epRepo;

    @Autowired
    private LookupService lookupService;

    @Autowired
    private ReportingPeriodMapper mapper;

    @Autowired
    private EmissionsReportStatusServiceImpl reportStatusService;

    @Autowired
    private EmissionServiceImpl emissionService;

    @Autowired
    private PointSourceSccCodeRepository pointSourceSccCodeRepo;

    @Autowired
    private BulkUploadMapper bulkUploadMapper;

    @Autowired
    private MonthlyReportingPeriodHolderMapper monthlyRptPeriodMapper;

    @Autowired
    private SLTConfigHelper sltConfigHelper;

    public ReportingPeriodDto create(ReportingPeriodDto dto) {

        ReportingPeriod period = mapper.fromDto(dto);

        if (dto.getCalculationMaterialCode() != null) {
            period.setCalculationMaterialCode(lookupService.retrieveCalcMaterialCodeEntityByCode(dto.getCalculationMaterialCode().getCode()));
        }

        if (dto.getCalculationParameterTypeCode() != null) {
            period.setCalculationParameterTypeCode(lookupService.retrieveCalcParamTypeCodeEntityByCode(dto.getCalculationParameterTypeCode().getCode()));
        }

        if (dto.getCalculationParameterUom() != null) {
            period.setCalculationParameterUom(lookupService.retrieveUnitMeasureCodeEntityByCode(dto.getCalculationParameterUom().getCode()));
        }

        if (dto.getEmissionsOperatingTypeCode() != null) {
            period.setEmissionsOperatingTypeCode(lookupService.retrieveEmissionsOperatingTypeCodeEntityByCode(dto.getEmissionsOperatingTypeCode().getCode()));
        }

        if (dto.getReportingPeriodTypeCode() != null) {
            period.setReportingPeriodTypeCode(lookupService.retrieveReportingPeriodCodeEntityByCode(dto.getReportingPeriodTypeCode().getCode()));
        }

        if (dto.getFuelUseMaterialCode() != null) {
            period.setFuelUseMaterialCode(lookupService.retrieveCalcMaterialCodeEntityByCode(dto.getFuelUseMaterialCode().getCode()));
        }

        if (dto.getFuelUseUom() != null) {
            period.setFuelUseUom(lookupService.retrieveUnitMeasureCodeEntityByCode(dto.getFuelUseUom().getCode()));
        }

        if (dto.getHeatContentUom() != null) {
            period.setHeatContentUom(lookupService.retrieveUnitMeasureCodeEntityByCode(dto.getHeatContentUom().getCode()));
        }

        period.getOperatingDetails().forEach(od -> {
            od.setReportingPeriod(period);
        });

        period.setStandardizedNonPointFuelUse(calculateFuelUseNonPointStandardized(period));

        ReportingPeriodDto result = mapper.toDto(repo.save(period));

        EmissionsProcess ep = epRepo.findById(dto.getEmissionsProcessId()).orElse(null);
        if (ep != null) {
            if (monthlyReportingEnabledAndYearCheck(ep)) {
                List<ReportingPeriodCode> rpcs = lookupService.findReportingPeriodMonthAndSemiAnnualCodes();

                for (ReportingPeriodCode rpc : rpcs) {
                    ReportingPeriod newRp = new ReportingPeriod(ep, period);

                    newRp.setAnnualReportingPeriod(period);
                    newRp.clearId();
                    newRp.setReportingPeriodTypeCode(rpc);
                    nullMonthlyReportingFields(newRp);
                    repo.save(newRp);
                }
            }
        }

        reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(result.getId()), ReportingPeriodRepository.class);
        return result;
    }

    public ReportingPeriodUpdateResponseDto update(ReportingPeriodDto dto) {

        ReportingPeriod period = repo.findById(dto.getId())
            .orElseThrow(() -> new NotExistException("Reporting Period", dto.getId()));

        mapper.updateFromDto(dto, period);

        if (dto.getCalculationMaterialCode() != null) {
            period.setCalculationMaterialCode(lookupService.retrieveCalcMaterialCodeEntityByCode(dto.getCalculationMaterialCode().getCode()));
        }

        if (dto.getCalculationParameterTypeCode() != null) {
            period.setCalculationParameterTypeCode(lookupService.retrieveCalcParamTypeCodeEntityByCode(dto.getCalculationParameterTypeCode().getCode()));
        }

        if (dto.getCalculationParameterUom() != null) {
            period.setCalculationParameterUom(lookupService.retrieveUnitMeasureCodeEntityByCode(dto.getCalculationParameterUom().getCode()));
        }

        if (dto.getEmissionsOperatingTypeCode() != null) {
            period.setEmissionsOperatingTypeCode(lookupService.retrieveEmissionsOperatingTypeCodeEntityByCode(dto.getEmissionsOperatingTypeCode().getCode()));
        }

        if (dto.getReportingPeriodTypeCode() != null) {
            period.setReportingPeriodTypeCode(lookupService.retrieveReportingPeriodCodeEntityByCode(dto.getReportingPeriodTypeCode().getCode()));
        }

        if (dto.getFuelUseMaterialCode() != null) {
            period.setFuelUseMaterialCode(lookupService.retrieveCalcMaterialCodeEntityByCode(dto.getFuelUseMaterialCode().getCode()));
        } else {
        	period.setFuelUseMaterialCode(null);
        }

        if (dto.getFuelUseUom() != null) {
            period.setFuelUseUom(lookupService.retrieveUnitMeasureCodeEntityByCode(dto.getFuelUseUom().getCode()));
        } else {
        	period.setFuelUseUom(null);
        }

        if (dto.getHeatContentUom() != null) {
            period.setHeatContentUom(lookupService.retrieveUnitMeasureCodeEntityByCode(dto.getHeatContentUom().getCode()));
        } else {
        	period.setHeatContentUom(null);
        }

        ReportingPeriodUpdateResponseDto result = new ReportingPeriodUpdateResponseDto();

        period.getEmissions().forEach(emission -> {
            if (!(Boolean.TRUE.equals(emission.getTotalManualEntry())
                    || Boolean.TRUE.equals(emission.getEmissionsCalcMethodCode().getTotalDirectEntry()))
            		&& !(emission.getEmissionsFactor() == null && emission.getEmissionsFactorFormula() == null)) {
                try {
                    Emission updatedEmission = emissionService.calculateTotalEmissions(emission, period);
                    emission.setEmissionsFactor(updatedEmission.getEmissionsFactor());
                    emission.setTotalEmissions(updatedEmission.getTotalEmissions());
                    result.getUpdatedEmissions().add(emission.getPollutant().getPollutantName());
                } catch (ApplicationException e) {
                    result.getFailedEmissions().add(emission.getPollutant().getPollutantName());
                }
            } else {
                result.getNotUpdatedEmissions().add(emission.getPollutant().getPollutantName());
            }

            emission.setCalculatedEmissionsTons(emissionService.calculateEmissionTons(emission));
        });

        period.setStandardizedNonPointFuelUse(calculateFuelUseNonPointStandardized(period));

        PointSourceSccCode procScc = pointSourceSccCodeRepo.findByCode(period.getEmissionsProcess().getSccCode());
        if (monthlyReportingEnabledAndYearCheck(period.getEmissionsProcess()) && procScc.getMonthlyReporting()) {
            List<ReportingPeriod> periods = repo.findByAnnualReportingPeriodId(period.getId());

            for (ReportingPeriod p : periods) {
                p.setFuelUseMaterialCode(period.getFuelUseMaterialCode());
                p.setFuelUseUom(period.getFuelUseUom());
                p.setCalculationMaterialCode(period.getCalculationMaterialCode());
                p.setCalculationParameterUom(period.getCalculationParameterUom());
                p.setHeatContentValue(period.getHeatContentValue());
                p.setHeatContentUom(period.getHeatContentUom());

                for (Emission e : p.getEmissions()) {
                    if (p.getCalculationParameterValue() != null && e.getEmissionsDenominatorUom() != null && e.getEmissionsNumeratorUom() != null) {
                        emissionService.calculateTotalEmissions(e, p);
                        e.setCalculatedEmissionsTons(emissionService.calculateEmissionTons(e));
                    }
                }
            }
            repo.saveAll(periods);
        }

        ReportingPeriodDto resultDto = mapper.toDto(repo.save(period));

        reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(resultDto.getId()), ReportingPeriodRepository.class);

        result.setReportingPeriod(resultDto);
        return result;
    }

    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.impl.ReportingPeriodService#retrieveById(java.lang.Long)
     */
    @Override
    public ReportingPeriodDto retrieveById(Long id) {
        ReportingPeriod result = repo
            .findById(id)
            .orElse(null);
        return mapper.toDto(result);
    }

    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.impl.ReportingPeriodService#retrieveForReleasePoint(java.lang.Long)
     */
    @Override
    public List<ReportingPeriodDto> retrieveForEmissionsProcess(Long processId) {
        List<ReportingPeriod> result = repo.findByEmissionsProcessId(processId);
        return mapper.toDtoList(result);
    }

    @Override
    public List<ReportingPeriodDto> retrieveAnnualForEmissionsProcess(Long processId) {
        List<ReportingPeriod> result = repo.findByEmissionsProcessIdAndReportingPeriodTypeCodeCode(processId, "A");
        return mapper.toDtoList(result);
    }

    /**
     * Retrieve Reporting Periods for Bulk Entry for a specific facility site
     * @param facilitySiteId
     * @return
     */
    public List<ReportingPeriodBulkEntryDto> retrieveBulkEntryReportingPeriodsForFacilitySite(Long facilitySiteId) {

        EmissionsReport er = this.reportRepo.findByFacilitySiteId(facilitySiteId)
            .orElseThrow(() -> new NotExistException("Facility Site", facilitySiteId));

        SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(er.getProgramSystemCode().getCode());

        List<ReportingPeriod> entities;
        Short monthlyInitialYear = sltConfig.getSltMonthlyFuelReportingInitialYear();
        if (Boolean.TRUE.equals(sltConfig.getSltMonthlyFuelReportingEnabled())
            && (monthlyInitialYear == null || er.getYear() >= monthlyInitialYear)) {
            entities = repo.findByFacilitySiteIdAndPeriod(facilitySiteId, ConstantUtils.ANNUAL, false);
        } else {
            entities = repo.findByFacilitySiteId(facilitySiteId);
        }

        entities = entities.stream()
                .filter(rp -> !"PS".equals(rp.getEmissionsProcess().getOperatingStatusCode().getCode())
                              && rp.getReportingPeriodTypeCode().getShortName().equals(ConstantUtils.ANNUAL))
                .filter(rp -> {
                    FacilitySourceTypeCode typeCode = rp.getEmissionsProcess().getEmissionsUnit().getFacilitySite().getFacilitySourceTypeCode();
                    return !"PS".equals(rp.getEmissionsProcess().getEmissionsUnit().getOperatingStatusCode().getCode())
                            || (typeCode != null && ConstantUtils.FACILITY_SOURCE_LANDFILL_CODE.contentEquals(typeCode.getCode()));
                }).collect(Collectors.toList());

        List<ReportingPeriodBulkEntryDto> result = mapper.toBulkEntryDtoList(entities);

        if (!entities.isEmpty()) {
            // find the last year reported
            Optional<EmissionsReport> lastReport = reportRepo.findFirstByMasterFacilityRecordIdAndIsDeletedIsFalseAndYearLessThanOrderByYearDesc(
                    entities.get(0).getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().getMasterFacilityRecord().getId(),
                    entities.get(0).getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear());

            if (lastReport.isPresent()) {
                result.forEach(dto -> {
                    List<ReportingPeriod> oldEntities = repo.retrieveByTypeIdentifierParentFacilityYear(dto.getReportingPeriodTypeCode().getCode(),
                            dto.getEmissionsProcessIdentifier(),
                            dto.getUnitIdentifier(),
                            lastReport.get().getMasterFacilityRecord().getId(),
                            lastReport.get().getYear());
                    if (!oldEntities.isEmpty()) {
                        if (oldEntities.get(0).getCalculationParameterValue() != null) {
                            dto.setPreviousCalculationParameterValue(oldEntities.get(0).getCalculationParameterValue().toString());
                        }
                        if (oldEntities.get(0).getCalculationParameterUom() != null) {
                        	dto.setPreviousCalculationParameterUomCode(oldEntities.get(0).getCalculationParameterUom().getCode());
                        } else {
                        	dto.setPreviousCalculationParameterUomCode(null);
                        }
                    }
                });
            }

            for (ReportingPeriodBulkEntryDto rp : result) {
                EmissionsProcess ep = epRepo.findById(rp.getEmissionsProcessId()).orElse(null);
                PointSourceSccCode code = pointSourceSccCodeRepo.findByCode(ep.getSccCode());
                rp.setFuelUseRequired(code.getFuelUseRequired());
            }
        }

        return result;
    }

    /**
     * Update the throughput for multiple Reporting Periods at once
     * @param facilitySiteId
     * @param dtos
     * @return
     */
    public List<EmissionBulkEntryHolderDto> bulkUpdate(Long facilitySiteId, List<ReportingPeriodBulkEntryDto> dtos) {

        List<ReportingPeriod> periods = dtos.stream().map(dto -> {
            ReportingPeriod period = repo.findById(dto.getReportingPeriodId())
                    .orElseThrow(() -> new NotExistException("Reporting Period", dto.getReportingPeriodId()));
            period.setCalculationParameterValue(new BigDecimal(dto.getCalculationParameterValue()));
            if (dto.getFuelUseValue() != null && !dto.getFuelUseValue().isEmpty()) {
                period.setFuelUseValue(new BigDecimal(dto.getFuelUseValue()));
            } else {
                period.setFuelUseValue(null);
            }
            if (dto.getFuelUseUom() != null) {
                UnitMeasureCode fuelUom = lookupService.retrieveUnitMeasureCodeEntityByCode(dto.getFuelUseUom().getCode());
                period.setFuelUseUom(fuelUom);
            } else {
                period.setFuelUseUom(null);
            }
            return period;
        }).collect(Collectors.toList());

        repo.saveAll(periods);

        return emissionService.bulkUpdate(facilitySiteId, Collections.emptyList());
    }

    /**
     * Retrieve Monthly Reporting Periods for a specific facility site and month
     * @param facilitySiteId
     * @param period
     * @return
     */
    public List<MonthlyReportingPeriodHolderDto> retrieveMonthlyReportingPeriodsForFacilitySite(Long facilitySiteId, String period) {

        List<ReportingPeriod> reportingPeriodEntities =
            repo.findOperatingByFacilitySiteIdAndPeriod(facilitySiteId, period, true).stream()
                // SCCs filtered in repository
                // op status filtered in repo
                // landfill checked in repo
                .filter(rp -> emissionService.isInitialMonthOnOrBeforeSelectedMonth(period, rp))
                .collect(Collectors.toList());

        List<OperatingDetail> operatingDetailEntities = new ArrayList<>();
        for (ReportingPeriod rp : reportingPeriodEntities) {
            operatingDetailEntities.add(rp.getOperatingDetails().get(0));
        }

        return monthlyRptPeriodMapper.toDtoList(operatingDetailEntities);
    }

    /**
     * Update monthly reporting info for multiple Reporting Periods at once
     * @param facilitySiteId
     * @param period
     * @param dtos
     * @return
     */
    public List<MonthlyReportingEmissionHolderDto> monthlyUpdate(Long facilitySiteId, String period, List<MonthlyReportingPeriodHolderDto> dtos) {

        EmissionsReport er = this.reportRepo.findByFacilitySiteId(facilitySiteId).orElse(null);

        List<ReportingPeriod> semiannualAndAnnualRps = new ArrayList<>();
        List<Emission> emissions = new ArrayList<>();

        List<ReportingPeriod> periods = dtos.stream().map(dto -> {
            ReportingPeriod p = repo.findById(dto.getReportingPeriodId())
                .orElseThrow(() -> new NotExistException("Reporting Period", dto.getReportingPeriodId()));
            p.setCalculationParameterValue(dto.getCalculationParameterValue() == null ? null : new BigDecimal(dto.getCalculationParameterValue()));
            p.setFuelUseValue(dto.getFuelUseValue() == null ? null : new BigDecimal(dto.getFuelUseValue()));
            p.getOperatingDetails().get(0).setActualHoursPerPeriod(dto.getActualHoursPerPeriod());
            p.getOperatingDetails().get(0).setAvgHoursPerDay(dto.getAvgHoursPerDay());
            p.getOperatingDetails().get(0).setAvgDaysPerWeek(dto.getAvgDaysPerWeek());
            p.getOperatingDetails().get(0).setAvgWeeksPerPeriod(dto.getAvgWeeksPerPeriod());

            if (!period.equals(ConstantUtils.ANNUAL) && !period.equals(ConstantUtils.SEMIANNUAL)) {

                List<ReportingPeriod> allMonthlyRps = repo.findByAnnualReportingPeriodId(p.getAnnualReportingPeriod().getId());

                ReportingPeriod annualRp = repo.findById(p.getAnnualReportingPeriod().getId()).orElse(null);
                ReportingPeriod semiannualRp = null;

                BigDecimal saHoursPerPeriod = BigDecimal.ZERO;
                BigDecimal aHoursPerPeriod = BigDecimal.ZERO;

                BigDecimal saDaysPerWeek = BigDecimal.ZERO;
                BigDecimal aDaysPerWeek = BigDecimal.ZERO;

                BigDecimal saWeeksPerPeriod = BigDecimal.ZERO;
                BigDecimal aWeeksPerPeriod = BigDecimal.ZERO;

                BigDecimal springHours = BigDecimal.ZERO;
                BigDecimal summerHours = BigDecimal.ZERO;
                BigDecimal fallHours = BigDecimal.ZERO;
                BigDecimal winterHours = BigDecimal.ZERO;

                BigDecimal saThroughputValue = BigDecimal.ZERO;
                BigDecimal aThroughputValue = BigDecimal.ZERO;

                BigDecimal saFuelValue = null;
                BigDecimal aFuelValue = null;

                for (ReportingPeriod rp : allMonthlyRps) {
                    if (rp.getReportingPeriodTypeCode().getShortName().equals(ConstantUtils.SEMIANNUAL)) {
                        semiannualRp = rp;
                    }

                    // values for comparing to go here
                    BigDecimal throughputValue = rp.getCalculationParameterValue();
                    BigDecimal fuelValue = rp.getFuelUseValue();
                    BigDecimal actualHoursPerPeriod = rp.getOperatingDetails().get(0).getActualHoursPerPeriod();
                    BigDecimal avgDaysPerWeek = rp.getOperatingDetails().get(0).getAvgDaysPerWeek();
                    BigDecimal avgWeeksPerPeriod = rp.getOperatingDetails().get(0).getAvgWeeksPerPeriod();

                    if (!rp.getReportingPeriodTypeCode().getShortName().equals(ConstantUtils.SEMIANNUAL)
                        && !rp.getReportingPeriodTypeCode().getShortName().equals(ConstantUtils.ANNUAL)) {

                        if (ConstantUtils.SEMI_ANNUAL_MONTHS.contains(rp.getReportingPeriodTypeCode().getShortName())) {

                            saThroughputValue = (throughputValue != null) ? saThroughputValue.add(throughputValue) : saThroughputValue;
                            aThroughputValue = (throughputValue != null) ? aThroughputValue.add(throughputValue) : aThroughputValue;

                            saFuelValue = (fuelValue != null) ? ((saFuelValue == null) ? fuelValue : saFuelValue.add(fuelValue)) : saFuelValue;
                            aFuelValue = (fuelValue != null) ? ((aFuelValue == null) ? fuelValue : aFuelValue.add(fuelValue)) : aFuelValue;

                            if (rp.getOperatingDetails() != null && !rp.getOperatingDetails().isEmpty()) {
                                saHoursPerPeriod = (actualHoursPerPeriod != null) ? (saHoursPerPeriod.add(actualHoursPerPeriod)) : saHoursPerPeriod;
                                aHoursPerPeriod = (actualHoursPerPeriod != null) ? (aHoursPerPeriod.add(actualHoursPerPeriod)) : aHoursPerPeriod;

                                saDaysPerWeek = (avgDaysPerWeek != null) ? saDaysPerWeek.add(avgDaysPerWeek) : saDaysPerWeek;
                                aDaysPerWeek = (avgDaysPerWeek != null) ? aDaysPerWeek.add(avgDaysPerWeek) : aDaysPerWeek;

                                saWeeksPerPeriod = (avgWeeksPerPeriod != null) ? (saWeeksPerPeriod.add(avgWeeksPerPeriod)) : saWeeksPerPeriod;
                                aWeeksPerPeriod = (avgWeeksPerPeriod != null) ? (aWeeksPerPeriod.add(avgWeeksPerPeriod)) : aWeeksPerPeriod;
                            }
                        }
                        else {
                            aThroughputValue = (throughputValue != null) ? aThroughputValue.add(throughputValue) : aThroughputValue;
                            aFuelValue = (fuelValue != null) ? ((aFuelValue == null) ? fuelValue : aFuelValue.add(fuelValue)) : aFuelValue;

                            if (rp.getOperatingDetails() != null && !rp.getOperatingDetails().isEmpty()) {
                                aHoursPerPeriod = (actualHoursPerPeriod != null) ? (aHoursPerPeriod.add(actualHoursPerPeriod)) : aHoursPerPeriod;
                                aDaysPerWeek = (avgDaysPerWeek != null) ? aDaysPerWeek.add(avgDaysPerWeek) : aDaysPerWeek;
                                aWeeksPerPeriod = (avgWeeksPerPeriod != null) ? (aWeeksPerPeriod.add(avgWeeksPerPeriod)) : aWeeksPerPeriod;
                            }
                        }

                        // seasonal hours for percentages
                        if (rp.getOperatingDetails() != null && !rp.getOperatingDetails().isEmpty()) {
                            if (ConstantUtils.SPRING_MONTHS.contains(rp.getReportingPeriodTypeCode().getShortName())) {
                                springHours = (actualHoursPerPeriod != null) ? springHours.add(actualHoursPerPeriod) : springHours;
                            }
                            else if (ConstantUtils.SUMMER_MONTHS.contains(rp.getReportingPeriodTypeCode().getShortName())) {
                                summerHours = (actualHoursPerPeriod != null) ? summerHours.add(actualHoursPerPeriod) : summerHours;
                            }
                            else if (ConstantUtils.FALL_MONTHS.contains(rp.getReportingPeriodTypeCode().getShortName())) {
                                fallHours = (actualHoursPerPeriod != null) ? fallHours.add(actualHoursPerPeriod) : fallHours;
                            }
                            else if (ConstantUtils.WINTER_MONTHS.contains(rp.getReportingPeriodTypeCode().getShortName())) {
                                winterHours = (actualHoursPerPeriod != null) ? winterHours.add(actualHoursPerPeriod) : winterHours;
                            }
                        }
                    }
                }

                if (er != null) {
                    int saDays = ConstantUtils.DAYS_IN_HALF_YEAR;
                    int aDays = ConstantUtils.DAYS_IN_YEAR;
                    if (er.getYear() % 4 == 0) {
                        saDays += 1;
                        aDays += 1;
                    }

                    if (semiannualRp != null) {
                        semiannualRp.setCalculationParameterValue(saThroughputValue);
                        semiannualRp.setFuelUseValue(saFuelValue);
                        semiannualRp.getOperatingDetails().get(0).setActualHoursPerPeriod(saHoursPerPeriod);
                        semiannualRp.getOperatingDetails().get(0).setAvgHoursPerDay(saHoursPerPeriod.divide(BigDecimal.valueOf(saDays), 5, RoundingMode.HALF_UP));
                        semiannualRp.getOperatingDetails().get(0).setAvgDaysPerWeek(saDaysPerWeek.divide(BigDecimal.valueOf(6), 5, RoundingMode.HALF_UP));
                        semiannualRp.getOperatingDetails().get(0).setAvgWeeksPerPeriod(saWeeksPerPeriod);

                        semiannualAndAnnualRps.add(semiannualRp);
                    }
                    if (annualRp != null) {
                        annualRp.setCalculationParameterValue(aThroughputValue);
                        annualRp.setFuelUseValue(aFuelValue);
                        annualRp.getOperatingDetails().get(0).setActualHoursPerPeriod(aHoursPerPeriod);
                        annualRp.getOperatingDetails().get(0).setAvgHoursPerDay(aHoursPerPeriod.divide(BigDecimal.valueOf(aDays),  5, RoundingMode.HALF_UP));
                        annualRp.getOperatingDetails().get(0).setAvgDaysPerWeek(aDaysPerWeek.divide(BigDecimal.valueOf(12),  5, RoundingMode.HALF_UP));
                        annualRp.getOperatingDetails().get(0).setAvgWeeksPerPeriod(aWeeksPerPeriod);
                        annualRp.setStandardizedNonPointFuelUse(calculateFuelUseNonPointStandardized(annualRp));

                        if (aHoursPerPeriod.compareTo(BigDecimal.ZERO) > 0) {
                            annualRp.getOperatingDetails().get(0).setPercentSpring(springHours.divide(aHoursPerPeriod, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
                            annualRp.getOperatingDetails().get(0).setPercentSummer(summerHours.divide(aHoursPerPeriod, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
                            annualRp.getOperatingDetails().get(0).setPercentFall(fallHours.divide(aHoursPerPeriod, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
                            annualRp.getOperatingDetails().get(0).setPercentWinter(winterHours.divide(aHoursPerPeriod, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
                        }
                        else {
                            // op percentages will all be 0 if total hours is 0
                            annualRp.getOperatingDetails().get(0).setPercentSpring(BigDecimal.ZERO);
                            annualRp.getOperatingDetails().get(0).setPercentSummer(BigDecimal.ZERO);
                            annualRp.getOperatingDetails().get(0).setPercentFall(BigDecimal.ZERO);
                            annualRp.getOperatingDetails().get(0).setPercentWinter(BigDecimal.ZERO);
                        }
                        semiannualAndAnnualRps.add(annualRp);
                    }
                }
            }

            emissions.addAll(p.getEmissions());
            return p;
        }).collect(Collectors.toList());

        periods.addAll(semiannualAndAnnualRps);
        repo.saveAll(periods);

        if (dtos.size() > 0 && dtos.get(0) != null && dtos.get(0).getEmissionsUnitId() != null) {
            reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(dtos.get(0).getEmissionsUnitId()), EmissionsUnitRepository.class);
        }

        return emissionService.monthlyUpdateFromPeriod(facilitySiteId, period, emissions);
    }

    public List<MonthlyReportingPeriodHolderDto> retrieveAllSemiAnnualMonthlyReportingPeriodsForFacilitySite(Long facilitySiteId) {

        List<ReportingPeriod> reportingPeriodEntities =
            repo.findMonthlyOperatingByFacilitySiteId(facilitySiteId, true).stream()
                // SCCs filtered in repository
                .filter(rp -> emissionService.areUnitAndProcessOperatingOrLandfill(rp)
                              && emissionService.isPeriodInFirstHalfOfYear(rp)
                              && emissionService.isInitialMonthOnOrBeforeSelectedMonth(rp.getReportingPeriodTypeCode().getShortName(), rp))
                // sort periods in chronological order
                .sorted(Comparator.comparing(rp -> ConstantUtils.ALL_MONTHS.indexOf(rp.getReportingPeriodTypeCode().getShortName())))
                .collect(Collectors.toList());

        List<OperatingDetail> operatingDetailEntities = new ArrayList<>();
        for (ReportingPeriod rp : reportingPeriodEntities) {
            operatingDetailEntities.add(rp.getOperatingDetails().get(0));
        }

        return monthlyRptPeriodMapper.toDtoList(operatingDetailEntities);
    }

    public List<MonthlyReportingPeriodHolderDto> retrieveAllAnnualMonthlyReportingPeriodsForFacilitySite(Long facilitySiteId) {

        List<ReportingPeriod> reportingPeriodEntities =
            repo.findMonthlyOperatingByFacilitySiteId(facilitySiteId, true).stream()
                // SCCs filtered in repository
                .filter(rp -> emissionService.areUnitAndProcessOperatingOrLandfill(rp)
                    && !ConstantUtils.SEMIANNUAL.equals(rp.getReportingPeriodTypeCode().getShortName())
                    && emissionService.isInitialMonthOnOrBeforeSelectedMonth(rp.getReportingPeriodTypeCode().getShortName(), rp))
                // sort periods in chronological order
                .sorted(Comparator.comparing(rp -> ConstantUtils.ALL_MONTHS.indexOf(rp.getReportingPeriodTypeCode().getShortName())))
                .collect(Collectors.toList());

        List<OperatingDetail> operatingDetailEntities = new ArrayList<>();
        for (ReportingPeriod rp : reportingPeriodEntities) {
            operatingDetailEntities.add(rp.getOperatingDetails().get(0));
        }

        return monthlyRptPeriodMapper.toDtoList(operatingDetailEntities);
    }

    public BigDecimal calculateFuelUseNonPointStandardized(ReportingPeriod period) {

    	BigDecimal convertedFuelUse = null;

    	if (period.getFuelUseUom() != null && period.getFuelUseMaterialCode() != null && period.getFuelUseValue() != null && period.getFuelUseMaterialCode().getNonPointStandardizedUom() != null) {

            // check if fuel material is natural gas (209) and is reported in any power of ft3s
            if (period.getHeatContentValue() != null && period.getHeatContentUom() != null && period.getFuelUseMaterialCode().getCode().equals(ConstantUtils.NATURAL_GAS_CODE)
                && emissionService.checkIfCanConvertUnits(period.getFuelUseUom(), lookupService.retrieveUnitMeasureCodeEntityByCode(ConstantUtils.FT3S))) {

                UnitMeasureCode e6btuUom = lookupService.retrieveUnitMeasureCodeEntityByCode(ConstantUtils.E6BTU);
                CalculationMaterialCode natGas = lookupService.retrieveCalcMaterialCodeEntityByCode(ConstantUtils.NATURAL_GAS_CODE);

                // multiply fuel value by heat content ratio, fuel uom and heat content denominator uom is the same uom
                // this value is now in heat content ratio numerator units
                convertedFuelUse = period.getFuelUseValue().multiply(period.getHeatContentValue());

                // convert to e6btu if necessary
                if (emissionService.checkIfCanConvertUnits(period.getHeatContentUom(), e6btuUom)) {
                    convertedFuelUse = CalculationUtils.convertUnits(period.getHeatContentUom().getCalculationVariable(), e6btuUom.getCalculationVariable()).multiply(convertedFuelUse);
                }

                // convert back to ft3sd using default heat content (0.00102 e6btu/ft3sd)
                convertedFuelUse = convertedFuelUse.divide(natGas.getDefaultHeatContentRatio(), MathContext.DECIMAL128);

                // convert directly from ft3sd to e6ft3sd by dividing by 1000000
                convertedFuelUse = convertedFuelUse.divide(new BigDecimal(1000000), MathContext.DECIMAL128);
            }


    		// do direct conversion to non-point uom if non-point standardized uom and fuel uom unit types are the same.
        	else if (emissionService.checkIfCanConvertUnits(period.getFuelUseUom(), period.getFuelUseMaterialCode().getNonPointStandardizedUom())) {
    			convertedFuelUse = CalculationUtils.convertUnits(period.getFuelUseUom().getCalculationVariable(), period.getFuelUseMaterialCode().getNonPointStandardizedUom().getCalculationVariable()).multiply(period.getFuelUseValue());

    		} else {

	        	// use user heat content ratio input for conversion to non-point uom only if the fuel uom and heat content numerator uom unit types are not the same.
	        	if (period.getHeatContentValue() != null && period.getHeatContentUom() != null
	        			&& !emissionService.checkIfCanConvertUnits(period.getFuelUseUom(), period.getHeatContentUom())) {

	        			if (emissionService.checkIfCanConvertUnits(period.getFuelUseMaterialCode().getNonPointStandardizedUom(), period.getHeatContentUom())) {

		        			// multiply fuel value by heat content ratio, fuel uom and heat content denominator uom is the same uom.
		        			convertedFuelUse = period.getFuelUseValue().multiply(period.getHeatContentValue());

		        			// convert fuel use (currently in heat content conversion numerator units) to non-point standardized uom
				        	convertedFuelUse = CalculationUtils.convertUnits(period.getHeatContentUom().getCalculationVariable(), period.getFuelUseMaterialCode().getNonPointStandardizedUom().getCalculationVariable()).multiply(convertedFuelUse);

			        	}
	        	}
	    	}
    	}
    	return convertedFuelUse;
    }


    /**
     * Retrieve a list of reporting periods for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    public List<ReportingPeriodBulkUploadDto>retrieveReportingPeriods(String programSystemCode, Short emissionsReportYear) {
    	List<ReportingPeriod> reportingPeriods = repo.findByPscAndEmissionsReportYear(programSystemCode, emissionsReportYear);
    	return bulkUploadMapper.reportingPeriodToDtoList(reportingPeriods);
    }

    /**
     * Update the fuel use value for multiple Reporting Periods at once from monthly reporting fuel use values
     * @param monthlyReports
     * @return
     */
    public List<ReportingPeriodDto> monthlyReportingUpdateReportingPeriod(List<MonthlyFuelReporting> monthlyReports) {

    	List<ReportingPeriod> periods = monthlyReports.stream().map(mr -> {
    		ReportingPeriod period = repo.findById(mr.getReportingPeriod().getId())
    				.orElseThrow(() -> new NotExistException("Reporting Period", mr.getReportingPeriod().getId()));
    		period.setFuelUseValue(mr.getAnnual_fuelUseValue());
    		return period;
    	}).collect(Collectors.toList());

    	return mapper.toDtoList((List<ReportingPeriod>) repo.saveAll(periods));
    }

    /**
     * Calculate non-point fuel use for all reporting periods without changing any other values
     * @return
     */
    public List<ReportingPeriodDto> calculateNonPointStandardizedFuelUse() {

        List<ReportingPeriod> rps = this.repo.findAllForNonPointStandardizedUpdate();
        List<ReportingPeriod> result = new ArrayList<>();

        rps.forEach(rp -> {
            BigDecimal nonPointFuelUseValue = calculateFuelUseNonPointStandardized(rp);
            if (!Objects.equals(nonPointFuelUseValue, rp.getStandardizedNonPointFuelUse())) {
                rp.setStandardizedNonPointFuelUse(nonPointFuelUseValue);
                result.add(this.repo.save(rp));
            }
        });

        return mapper.toDtoList(result);
    }

    public ReportingPeriod nullMonthlyReportingFields(ReportingPeriod period) {

        period.setCalculationParameterValue(null);
        if (period.getFuelUseValue() != null) {
            period.setFuelUseValue(null);
        }

        for (OperatingDetail opDetail: period.getOperatingDetails()) {
            opDetail.setActualHoursPerPeriod(null);
            opDetail.setAvgWeeksPerPeriod(null);
            opDetail.setAvgHoursPerDay(null);
            opDetail.setAvgDaysPerWeek(null);

            opDetail.setPercentFall(null);
            opDetail.setPercentSpring(null);
            opDetail.setPercentSummer(null);
            opDetail.setPercentWinter(null);
        }

        for (Emission e : period.getEmissions()) {
            e.setMonthlyRate(null);
            e.setTotalEmissions(null);
        }

        return period;
    }

    /**
     * Delete Reporting Periods by facility site id
     * @param facilitySiteId
     */
    public void deleteByFacilitySite(Long facilitySiteId) {
        repo.deleteByFacilitySite(facilitySiteId);
    }

    private SLTBaseConfig getSltConfig(EmissionsProcess ep) {
        return sltConfigHelper.getCurrentSLTConfig(ep.getEmissionsUnit().getFacilitySite().getEmissionsReport().getMasterFacilityRecord().getProgramSystemCode().getCode());
    }

    private boolean monthlyReportingEnabledAndYearCheck(EmissionsProcess ep) {
        SLTBaseConfig sltConfig = getSltConfig(ep);
        Short monthlyInitialYear = sltConfig.getSltMonthlyFuelReportingInitialYear();
        return Boolean.TRUE.equals(sltConfig.getSltMonthlyFuelReportingEnabled())
            && (monthlyInitialYear == null || ep.getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear() >= monthlyInitialYear);
    }
}
