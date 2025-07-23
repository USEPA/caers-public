/*
 * © Copyright 2019 EPA CAERS Project Team
 *
 * This file is part of the Common Air Emissions Reporting System (CAERS).
 *
 * CAERS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * CAERS is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with CAERS.  If
 * not, see <https://www.gnu.org/licenses/>.
*/
package gov.epa.cef.web.service.impl;

import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.exception.AppValidationException;
import gov.epa.cef.web.repository.*;
import gov.epa.cef.web.service.*;
import gov.epa.cef.web.service.dto.*;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionsProcessBulkUploadDto;
import gov.epa.cef.web.service.mapper.BulkUploadMapper;
import gov.epa.cef.web.service.mapper.EmissionsProcessMapper;
import gov.epa.cef.web.util.ConstantUtils;
import gov.epa.cef.web.util.SLTConfigHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class EmissionsProcessServiceImpl implements EmissionsProcessService {

    @Autowired
    private EmissionsReportRepository reportRepo;

    @Autowired
    private EmissionsUnitRepository unitRepo;

    @Autowired
    private EmissionsProcessRepository processRepo;

    @Autowired
    private EmissionRepository emissionRepo;

    @Autowired
    private LookupService lookupService;

    @Autowired
    private EmissionsProcessMapper emissionsProcessMapper;

    @Autowired
    private EmissionsReportStatusServiceImpl reportStatusService;

    @Autowired
    private BulkUploadMapper bulkUploadMapper;

    @Autowired
    private ReportingPeriodService rpService;

    @Autowired
    private EmissionService emissionService;

    @Autowired
    private SLTConfigHelper sltConfigHelper;

    public EmissionsProcessDto create(EmissionsProcessSaveDto dto) {

        EmissionsProcess process = emissionsProcessMapper.fromSaveDto(dto);

        process.getReportingPeriods().forEach(period -> {
            if (period.getCalculationMaterialCode() != null) {
                period.setCalculationMaterialCode(lookupService.retrieveCalcMaterialCodeEntityByCode(period.getCalculationMaterialCode().getCode()));
            }

            if (period.getCalculationParameterTypeCode() != null) {
                period.setCalculationParameterTypeCode(lookupService.retrieveCalcParamTypeCodeEntityByCode(period.getCalculationParameterTypeCode().getCode()));
            }

            if (period.getCalculationParameterUom() != null) {
                period.setCalculationParameterUom(lookupService.retrieveUnitMeasureCodeEntityByCode(period.getCalculationParameterUom().getCode()));
            }

            if (period.getEmissionsOperatingTypeCode() != null) {
                period.setEmissionsOperatingTypeCode(lookupService.retrieveEmissionsOperatingTypeCodeEntityByCode(period.getEmissionsOperatingTypeCode().getCode()));
            }

            if (period.getReportingPeriodTypeCode() != null) {
                period.setReportingPeriodTypeCode(lookupService.retrieveReportingPeriodCodeEntityByCode(period.getReportingPeriodTypeCode().getCode()));
            }

            if (period.getFuelUseMaterialCode() != null) {
                period.setFuelUseMaterialCode(lookupService.retrieveCalcMaterialCodeEntityByCode(period.getFuelUseMaterialCode().getCode()));
            }

            if (period.getFuelUseUom() != null) {
                period.setFuelUseUom(lookupService.retrieveUnitMeasureCodeEntityByCode(period.getFuelUseUom().getCode()));
            }

            if (period.getHeatContentUom() != null) {
                period.setHeatContentUom(lookupService.retrieveUnitMeasureCodeEntityByCode(period.getHeatContentUom().getCode()));
            }

            period.setEmissionsProcess(process);

            period.getOperatingDetails().forEach(od -> {
                od.setReportingPeriod(period);
            });
        });

        // create reporting periods for each month and semiannual when monthly reporting is true
        EmissionsUnit eu = unitRepo.findById(process.getEmissionsUnit().getId()).orElse(null);
        if (eu != null) {
            if (monthlyReportingEnabledAndYearCheck(eu)) {
                List<ReportingPeriodCode> rpcs = lookupService.findReportingPeriodMonthAndSemiAnnualCodes();

                ReportingPeriod rp = process.getReportingPeriods().get(0);

                for (ReportingPeriodCode rpc : rpcs) {
                    ReportingPeriod newRp = new ReportingPeriod(process, rp);

                    newRp.setAnnualReportingPeriod(rp);
                    newRp.clearId();
                    newRp.setReportingPeriodTypeCode(rpc);
                    rpService.nullMonthlyReportingFields(newRp);

                    process.getReportingPeriods().add(newRp);
                }
            }
        }

        process.getReleasePointAppts().forEach(appt -> {
            appt.clearId();
            appt.setEmissionsProcess(process);
        });

        EmissionsProcessDto result = emissionsProcessMapper.emissionsProcessToEmissionsProcessDto(processRepo.save(process));
        reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(result.getId()), EmissionsProcessRepository.class);
        return result;
    }

    public EmissionsProcessDto update(EmissionsProcessSaveDto dto) {

        EmissionsProcess process = processRepo.findById(dto.getId()).orElse(null);
        String previousInitialMonth = process.getInitialMonthlyReportingPeriod();

        emissionsProcessMapper.updateFromSaveDto(dto, process);

        // when monthly reporting is enabled
        // recalculate the total annual and semiannual emissions if the initial reporting month has changed
        if (monthlyReportingEnabledAndYearCheck(process.getEmissionsUnit())) {

            PointSourceSccCodeDto scc = lookupService.retrievePointSourceSccCode(dto.getSccCode());
            if (Boolean.TRUE.equals(scc.getMonthlyReporting())
                && dto.getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_OPERATING)
                && dto.getInitialMonthlyReportingPeriod() != null && previousInitialMonth != null
                && !dto.getInitialMonthlyReportingPeriod().equals(previousInitialMonth)) {

                ReportingPeriod annualRp = process.getReportingPeriods()
                    .stream()
                    .filter(rp -> rp.getReportingPeriodTypeCode().getShortName().equals(ConstantUtils.ANNUAL))
                    .findFirst()
                    .orElse(null);

                if (annualRp != null) {
                    List<Emission> emissionsToSave = new ArrayList<>();
                    for (Emission e : annualRp.getEmissions()) {
                        emissionsToSave.addAll(emissionService.updateAnnualAndSemiAnnualEmissions(e));
                    }
                    emissionRepo.saveAll(emissionsToSave);
                }
            }
        }

        EmissionsProcessDto result = emissionsProcessMapper.emissionsProcessToEmissionsProcessDto(processRepo.save(process));
        reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(result.getId()), EmissionsProcessRepository.class);
        return result;
    }

    public EmissionsProcessDto updateScc(EmissionsProcessSaveDto dto) {

        EmissionsProcess process = processRepo.findById(dto.getId()).orElse(null);

        process.setSccCode(dto.getSccCode());
        process.setSccCategory(dto.getSccCategory());
        process.setSccDescription(dto.getSccDescription());
        process.setSccShortName(dto.getSccShortName());

        EmissionsProcessDto result = emissionsProcessMapper.emissionsProcessToEmissionsProcessDto(processRepo.save(process));
        reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(result.getId()), EmissionsProcessRepository.class);
        return result;
    }

    private Boolean hasReportingPeriodsAndEmissions(EmissionsProcess ep){
        return
            ep != null &&
            ep.getReportingPeriods() != null &&
            !ep.getReportingPeriods().isEmpty() &&
            ep.getReportingPeriods().get(0).getEmissions() != null &&
            !ep.getReportingPeriods().get(0).getEmissions().isEmpty();
    }
    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.impl.EmissionsProcessService#retrieveById(java.lang.Long)
     */
    private void setPreviousEmissionData(EmissionDto current, Emission previous){
        current.setPreviousYearTotalEmissions(previous.getTotalEmissions());
        current.setPreviousEmissionsFactor(previous.getEmissionsFactor());
        current.setPreviousNotReporting(previous.getNotReporting());
        if(previous.getEmissionsDenominatorUom() != null){
            current.setPreviousEmissionsDenominatorUomCode(previous.getEmissionsDenominatorUom().getDescription());
        }
        if(previous.getEmissionsNumeratorUom() != null){
            current.setPreviousEmissionsNumeratorUomCode(previous.getEmissionsNumeratorUom().getDescription());
        }
        if(previous.getEmissionsUomCode() != null){
            current.setPreviousEmissionsUomCode(previous.getEmissionsUomCode().getDescription());
        }
    }

    @Override
    public EmissionsProcessDto retrieveById(Long id) {
        EmissionsProcess current = processRepo
            .findById(id)
            .orElse(null);

        EmissionsProcessDto currentDto = emissionsProcessMapper.emissionsProcessToEmissionsProcessDto(current);
        Long mfrId = processRepo.retrieveMasterFacilityRecordIdById(id).orElse(null);

        if(mfrId != null){
            EmissionsProcess previousEmissionProcess = this.findPrevious(mfrId,
                    current.getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear(),
                    current.getEmissionsProcessIdentifier(),
                    current.getEmissionsUnit().getUnitIdentifier())
                .stream()
                .findFirst()
                .orElse(null);
            currentDto.setYear(current.getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear());

            if(hasReportingPeriodsAndEmissions(current) && hasReportingPeriodsAndEmissions(previousEmissionProcess)){
                List<ReportingPeriod> reportingPeriods = current.getReportingPeriods();
                reportingPeriods.forEach(dto -> {
                    for(int i=0; i<dto.getEmissions().size(); i++){
                        Emission currentEmission = dto.getEmissions().get(i);
                        EmissionDto currentEmissionDto = currentDto.getReportingPeriods().get(0).getEmissions().get(i);
                        List<Emission> oldEmissions = emissionRepo.retrieveMatchingForYear( // fetch emissions for the previous year
                                currentEmission.getPollutant().getPollutantCode(),
                                dto.getReportingPeriodTypeCode().getCode(),
                                dto.getEmissionsProcess().getEmissionsProcessIdentifier(),
                                dto.getEmissionsProcess().getEmissionsUnit().getUnitIdentifier(),
                                previousEmissionProcess.getEmissionsUnit().getFacilitySite().getEmissionsReport().getEisProgramId(),
                                previousEmissionProcess.getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear());

                        if(!oldEmissions.isEmpty()){
                            if(currentEmission.getPollutant().getPollutantCode().equals(currentEmissionDto.getPollutant().getPollutantCode())){
                                setPreviousEmissionData(currentEmissionDto, oldEmissions.get(0));
                            }
                        }
                    }
                });
            }

        }
        return currentDto;
    }

    /**
     * Retrieve versions of this process from the last year reported
     * @param id
     * @return
     */
    public EmissionsProcessDto retrievePreviousById(Long id) {
        EmissionsProcess process = processRepo
                .findById(id)
                .orElse(null);

        Long mfrId = processRepo.retrieveMasterFacilityRecordIdById(id).orElse(null);

        EmissionsProcess result = this.findPrevious(mfrId,
                    process.getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear(),
                    process.getEmissionsProcessIdentifier(),
                    process.getEmissionsUnit().getUnitIdentifier())
                .stream()
                .findFirst()
                .orElse(null);

        return emissionsProcessMapper.emissionsProcessToEmissionsProcessDto(result);
    }

    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.impl.EmissionsProcessService#retrieveForReleasePoint(java.lang.Long)
     */
    @Override
    public List<EmissionsProcessDto> retrieveForReleasePoint(Long pointId) {
        List<EmissionsProcess> result = processRepo.findByReleasePointApptsReleasePointIdOrderByEmissionsProcessIdentifier(pointId);
        if (result == null) {
            return null; // Handle null case explicitly
        }
        List<EmissionsProcessDto> dtos = emissionsProcessMapper.emissionsProcessesToEmissionsProcessDtos(result);
        
        // Set unitIdentifier and description from EmissionsUnit
        for (EmissionsProcessDto dto : dtos) {
            EmissionsUnit emissionsUnit = unitRepo.findById(dto.getEmissionsUnitId()).orElse(null);
            if (emissionsUnit != null) {
                dto.setUnitIdentifier(emissionsUnit.getUnitIdentifier());
                dto.setEmissionsUnitDescription(emissionsUnit.getDescription());
            }
        }
        
        return dtos;
    }

    /**
     * Retrieve Emissions Processes for an Emissions Unit
     * @param emissionsUnitId
     * @return
     */
    public List<EmissionsProcessDto> retrieveForEmissionsUnit(Long emissionsUnitId) {
        List<EmissionsProcess> result = processRepo.findByEmissionsUnitIdOrderByEmissionsProcessIdentifier(emissionsUnitId);
        return emissionsProcessMapper.emissionsProcessesToEmissionsProcessDtos(result);
    }

    /**
     * Delete an Emissions Process for a given id
     * @param id
     */
    public void delete(Long id) {
        EmissionsProcess process = processRepo
                .findById(id)
                .orElse(null);

        Long mfrId = processRepo.retrieveMasterFacilityRecordIdById(id).orElse(null);

        this.findPrevious(mfrId,
                process.getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear(),
                process.getEmissionsProcessIdentifier(),
                process.getEmissionsUnit().getUnitIdentifier())
            .stream()
            .findFirst()
            .ifPresent(oldUnit -> {
                throw new AppValidationException("This Process has been submitted on previous years' facility reports, so it cannot be deleted. "
                        + "If this Process is no longer operational, please use the \"Operating Status\" field to mark this Process as \"Permanently Shutdown\".");
            });

        reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(id), EmissionsProcessRepository.class);
        processRepo.deleteById(id);
    }

    /**
     * Find versions of this Process from the last year reported
     * @param mfrId
     * @param year
     * @param processIdentifier
     * @param unitIdentifier
     * @return
     */
    private List<EmissionsProcess> findPrevious(Long mfrId, Short year, String processIdentifier, String unitIdentifier) {

        // find the last year reported
        Optional<EmissionsReport> lastReport = reportRepo.findFirstByMasterFacilityRecordIdAndIsDeletedIsFalseAndYearLessThanOrderByYearDesc(mfrId,
                year);

        // check if the emissions process was reported last year
        if (lastReport.isPresent()) {
            return processRepo.retrieveByIdentifierParentFacilityYear(processIdentifier,
                    unitIdentifier,
                    mfrId,
                    lastReport.get().getYear());
        }

        return Collections.emptyList();
    }

    /**
     * Retrieve a list of emissions processes for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    public List<EmissionsProcessBulkUploadDto> retrieveEmissionsProcesses(String programSystemCode, Short emissionsReportYear) {
    	List<EmissionsProcess> processes = processRepo.findByPscAndEmissionsReportYear(programSystemCode, emissionsReportYear);
    	return bulkUploadMapper.emissionsProcessToDtoList(processes);
    }

    private SLTBaseConfig getSltConfig(EmissionsUnit eu) {
        return sltConfigHelper.getCurrentSLTConfig(eu.getFacilitySite().getEmissionsReport().getMasterFacilityRecord().getProgramSystemCode().getCode());
    }

    private boolean monthlyReportingEnabledAndYearCheck(EmissionsUnit eu) {
        SLTBaseConfig sltConfig = getSltConfig(eu);
        Short monthlyInitialYear = sltConfig.getSltMonthlyFuelReportingInitialYear();
        return Boolean.TRUE.equals(sltConfig.getSltMonthlyFuelReportingEnabled())
            && (monthlyInitialYear == null || eu.getFacilitySite().getEmissionsReport().getYear() >= monthlyInitialYear);
    }
}
