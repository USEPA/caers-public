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
package gov.epa.cef.web.service.validation.validator.federal;

import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.EmissionsUnitRepository;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationFeature;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.ValidationRegistry;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import gov.epa.cef.web.util.ConstantUtils;
import gov.epa.cef.web.util.SLTConfigHelper;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SharedEmissionsUnitValidator extends BaseValidator<EmissionsUnit> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PointSourceSccCodeRepository sccRepo;

    @Autowired
	private EmissionsReportRepository reportRepo;

    @Autowired
	private EmissionsUnitRepository unitRepo;

    @Autowired
    private SLTConfigHelper sltConfigHelper;

    @Override
    public void compose(FluentValidator validator,
                        ValidatorContext validatorContext,
                        EmissionsUnit emissionsUnit) {

        ValidationRegistry registry = getCefValidatorContext(validatorContext).getValidationRegistry();

        // add more validators as needed
        validator.onEach(emissionsUnit.getEmissionsProcesses(),
            registry.findOneByType(SharedEmissionsProcessValidator.class));
    }

    private Optional<FacilityNAICSXref> isNaicsCodePrefixInEmissionsUnit(EmissionsUnit emissionsUnit, String naicsCode){
        return emissionsUnit.getFacilitySite().getFacilityNAICS()
            .stream()
            .filter(o -> o.getNaicsCode().getCode().toString().startsWith(naicsCode)
                && o.getNaicsCodeType() == NaicsCodeType.PRIMARY).findFirst();
    }

    private boolean checkEmptyDesignCapacityAgainstNaicsCodeAndUnitTypeCodes(EmissionsUnit emissionsUnit, CefValidatorContext context, boolean result){
        Optional<FacilityNAICSXref> optionalNaicsCementPrefix = isNaicsCodePrefixInEmissionsUnit(emissionsUnit, ConstantUtils.NAICS_CEMENT_MANUFACTURING_PREFIX);
        Optional<FacilityNAICSXref> optionalNaicsGlassPrefix = isNaicsCodePrefixInEmissionsUnit(emissionsUnit, ConstantUtils.NAICS_GLASS_PRODUCT_MANUFACTURING_PREFIX);
        Optional<FacilityNAICSXref> optionalNaicsWastePrefix = isNaicsCodePrefixInEmissionsUnit(emissionsUnit, ConstantUtils.NAICS_WASTE_TREATMENT_AND_DISPOSAL_PREFIX);

        if (ConstantUtils.DESIGN_CAPACITY_TYPE_CODE_ERROR.contains(emissionsUnit.getUnitTypeCode().getCode())) {
            result = false;
            context.addFederalError(
                ValidationField.EMISSIONS_UNIT_CAPACITY.value(), "emissionsUnit.capacity.requiredPerUnitType",
                createValidationDetails(emissionsUnit),
                ConstantUtils.DESIGN_CAPACITY_TYPE_CODE_DESCRIPTION);
        }

        if(optionalNaicsCementPrefix.isPresent()
            && ConstantUtils.DESIGN_CAPACITY_TYPE_CODE_KILNS_ERROR.contains(emissionsUnit.getUnitTypeCode().getCode())) {
            result = false;
            context.addFederalError(
                ValidationField.EMISSIONS_UNIT_CAPACITY.value(), "emissionsUnit.capacity.requiredPerNaicsCodeAndUnitTypeCodes",
                createValidationDetails(emissionsUnit),
                String.format("'%s'", optionalNaicsCementPrefix.get().getNaicsCode().getCode()),
                ConstantUtils.DESIGN_CAPACITY_TYPE_CODE_KILNS_DESCRIPTION
            );
        }

        if(optionalNaicsGlassPrefix.isPresent()
           && ConstantUtils.UNIT_TYPE_CODE_FURNACE.equals(emissionsUnit.getUnitTypeCode().getCode())) {
            result = false;
            context.addFederalError(
                ValidationField.EMISSIONS_UNIT_CAPACITY.value(), "emissionsUnit.capacity.requiredPerNaicsCodeAndUnitTypeCode",
                createValidationDetails(emissionsUnit),
                String.format("'%s'", optionalNaicsGlassPrefix.get().getNaicsCode().getCode()),
                emissionsUnit.getUnitTypeCode().getCode(),
                emissionsUnit.getUnitTypeCode().getDescription());
        }

        if(optionalNaicsWastePrefix.isPresent()
           && ConstantUtils.UNIT_TYPE_CODE_INCINERATOR.equals(emissionsUnit.getUnitTypeCode().getCode())) {
            result = false;
            context.addFederalError(
                ValidationField.EMISSIONS_UNIT_CAPACITY.value(), "emissionsUnit.capacity.requiredPerNaicsCodeAndUnitTypeCode",
                createValidationDetails(emissionsUnit),
                String.format("'%s'", optionalNaicsWastePrefix.get().getNaicsCode().getCode()),
                emissionsUnit.getUnitTypeCode().getCode(),
                emissionsUnit.getUnitTypeCode().getDescription());
        }
        return result;
    }

    private boolean isEmissionProcessNonOperating(EmissionsProcess emissionsProcess){
        return ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN.contentEquals(emissionsProcess.getOperatingStatusCode().getCode())
            || ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(emissionsProcess.getOperatingStatusCode().getCode());
    }

    private boolean checkIfOperatingUnitContainsOnlyNonOperatingEmissionProcess(EmissionsUnit unit, CefValidatorContext context, boolean result){
        List<EmissionsProcess> epList = unit.getEmissionsProcesses();

        boolean onlyContainsNonOperating = true;
        for(EmissionsProcess ep: epList){
           if(!isEmissionProcessNonOperating(ep)){
               onlyContainsNonOperating = false;
               break;
           }
        }

        if(onlyContainsNonOperating && !epList.isEmpty()){
            result = false;
            context.addFederalError(
                ValidationField.PROCESS_STATUS_CODE.value(),
                "emissionsUnit.statusTypeCode.NonOperatingStatus",
                createValidationDetails(unit));
        }
        return result;
    }

    @Override
    public boolean validate(ValidatorContext validatorContext, EmissionsUnit emissionsUnit) {

        boolean result = true;

        CefValidatorContext context = getCefValidatorContext(validatorContext);

        // If the facility source type code is landfill, then the emissions process can still be "operating" because of passive emissions that are emitted from the landfill.
        // For all other facility source types, if the facility is shutdown, then the emissions process underneath the emissions unit must also be shutdown.
        if ((emissionsUnit.getFacilitySite().getEmissionsReport().getMasterFacilityRecord().getFacilitySourceTypeCode() == null
            || !ConstantUtils.FACILITY_SOURCE_LANDFILL_CODE.contentEquals(emissionsUnit.getFacilitySite().getEmissionsReport().getMasterFacilityRecord().getFacilitySourceTypeCode().getCode()))) {

            if (ConstantUtils.STATUS_OPERATING.contentEquals(emissionsUnit.getOperatingStatusCode().getCode())) {
                result = checkIfOperatingUnitContainsOnlyNonOperatingEmissionProcess(emissionsUnit, context, result);
            }

            //if the unit is temporarily shutdown, then the underlying processes must also be temporarily or permanently shutdown
            if (ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN.contentEquals(emissionsUnit.getOperatingStatusCode().getCode())) {
                List<EmissionsProcess> epList = emissionsUnit.getEmissionsProcesses().stream()
                    .filter(emissionsProcess -> !ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(emissionsProcess.getOperatingStatusCode().getCode())
                        && !ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN.contentEquals(emissionsProcess.getOperatingStatusCode().getCode()))
                    .collect(Collectors.toList());

                for (EmissionsProcess ep : epList) {
                    result = false;
                    context.addFederalError(
                        ValidationField.PROCESS_STATUS_CODE.value(),
                        "emissionsProcess.statusTypeCode.temporarilyShutdown",
                        createEmissionsProcessValidationDetails(ep));
                }

            }

            if (ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(emissionsUnit.getOperatingStatusCode().getCode())) {

                // Warning if unit operation status is permanently shutdown unit will not be copied forward
                result = false;
                context.addFederalWarning(
                    ValidationField.EMISSIONS_UNIT_STATUS_CODE.value(),
                    "emissionsUnit.statusTypeCode.psNotCopied",
                    createValidationDetails(emissionsUnit));

                // Warning if unit operation status is permanently shutdown, process will not be copied forward
                for (EmissionsProcess ep : emissionsUnit.getEmissionsProcesses()) {

                    result = false;
                    context.addFederalWarning(
                        ValidationField.PROCESS_STATUS_CODE.value(),
                        "emissionsProcess.statusTypeCode.notCopied",
                        createEmissionsProcessValidationDetails(ep));
                }

                List<EmissionsProcess> epList = emissionsUnit.getEmissionsProcesses().stream()
                    .filter(emissionsProcess -> !ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(emissionsProcess.getOperatingStatusCode().getCode()))
                    .collect(Collectors.toList());

                // If the unit is permanently shutdown, then the underlying processes must also be permanently shutdown
                for (EmissionsProcess ep : epList) {

                    result = false;
                    context.addFederalError(
                        ValidationField.PROCESS_STATUS_CODE.value(),
                        "emissionsProcess.statusTypeCode.permanentShutdown",
                        createEmissionsProcessValidationDetails(ep));
                }

            } else {
                // Warning if unit operating status is OP or TS and process operation status is permanently shutdown, process will not be copied forward
                for (EmissionsProcess ep : emissionsUnit.getEmissionsProcesses()) {
                    if (ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(ep.getOperatingStatusCode().getCode())) {

                        result = false;
                        context.addFederalWarning(
                            ValidationField.PROCESS_STATUS_CODE.value(),
                            "emissionsProcess.statusTypeCode.psNotCopied",
                            createEmissionsProcessValidationDetails(ep));
                    }
                }
            }

            EmissionsReport currentReport = emissionsUnit.getFacilitySite().getEmissionsReport();

            List<EmissionsReport> erList = reportRepo.findByMasterFacilityRecordId(currentReport.getMasterFacilityRecord().getId()).stream()
                .filter(var -> (var.getYear() != null && var.getYear() < currentReport.getYear()))
                .sorted(Comparator.comparing(EmissionsReport::getYear))
                .collect(Collectors.toList());

            boolean pyUnitExists = false;

            // check if previous report exists then check if this unit exists in that report
            if (!erList.isEmpty()) {
                Short previousReportYr = erList.get(erList.size() - 1).getYear();

                List<EmissionsUnit> previousUnits = unitRepo.retrieveByIdentifierFacilityYear(
                    emissionsUnit.getUnitIdentifier(),
                    currentReport.getMasterFacilityRecord().getId(),
                    previousReportYr);

                if (!previousUnits.isEmpty()) {

                    pyUnitExists = true;

                    for (EmissionsUnit previousUnit : previousUnits) {

                        // check PS/TS status year of current report to OP status year of previous report
                        if (!ConstantUtils.STATUS_OPERATING.contentEquals(emissionsUnit.getOperatingStatusCode().getCode())
                            && ConstantUtils.STATUS_OPERATING.contentEquals(previousUnit.getOperatingStatusCode().getCode())
                            && previousUnit.getStatusYear() != null
                            && (emissionsUnit.getStatusYear() == null || emissionsUnit.getStatusYear() <= previousUnit.getStatusYear())) {

                            result = false;
                            context.addFederalError(
                                ValidationField.EMISSIONS_UNIT_STATUS_YEAR.value(),
                                "emissionsUnit.statusYear.invalid",
                                createValidationDetails(emissionsUnit),
                                emissionsUnit.getOperatingStatusCode().getDescription(),
                                emissionsUnit.getStatusYear() != null ? emissionsUnit.getStatusYear().toString() : emissionsUnit.getStatusYear());

                        }

                        // Show QA warning for processes that are new for an existing unit compared to the previous report
                        List<String> previousProcessIdentifiers = previousUnit.getEmissionsProcesses().stream()
                            .map(EmissionsProcess::getEmissionsProcessIdentifier)
                            .collect(Collectors.toList());

                        for (EmissionsProcess currentYearProcess : emissionsUnit.getEmissionsProcesses()) {

                            if (!previousProcessIdentifiers.contains(currentYearProcess.getEmissionsProcessIdentifier())) {
                                result = false;
                                context.addFacilityInventoryChange(
                                    ValidationField.EMISSIONS_UNIT_PROCESS.value(),
                                    "emissionsProcess.new",
                                    createEmissionsProcessValidationDetails(currentYearProcess));
                            }
                        }
                    }
                }
            }

            if (!pyUnitExists) {

                if (!ConstantUtils.STATUS_OPERATING.contentEquals(emissionsUnit.getOperatingStatusCode().getCode())) {
                    // new unit is PS/TS
                    result = false;
                    context.addFederalError(
                        ValidationField.EMISSIONS_UNIT_STATUS_CODE.value(),
                        "emissionsUnit.statusTypeCode.newShutdown",
                        createValidationDetails(emissionsUnit));
                }
                else if (!erList.isEmpty()) {
                    result = false;
                    context.addFacilityInventoryChange(
                        ValidationField.EMISSIONS_UNIT.value(),
                        "emissionsUnit.new",
                        createValidationDetails(emissionsUnit));

                    if (!emissionsUnit.getEmissionsProcesses().isEmpty()) {
                        // If unit is new, then its processes are new. Show QA warning for those processes
                        for (EmissionsProcess eup : emissionsUnit.getEmissionsProcesses()) {
                            context.addFacilityInventoryChange(
                                ValidationField.EMISSIONS_UNIT_PROCESS.value(),
                                "emissionsProcess.new",
                                createEmissionsProcessValidationDetails(eup));
                        }
                    }
                }
            }
        }

        // if emissions unit was PS in previous year report and is not PS in this report
        if (emissionsUnit.getPreviousYearOperatingStatusCode() != null) {
            if (ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(emissionsUnit.getPreviousYearOperatingStatusCode().getCode()) &&
                !ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(emissionsUnit.getOperatingStatusCode().getCode())) {

                result = false;
                context.addFederalError(
                    ValidationField.EMISSIONS_UNIT_STATUS_CODE.value(),
                    "emissionsUnit.statusTypeCode.psPreviousYear",
                    createValidationDetails(emissionsUnit));
            }
        }

        if (emissionsUnit.getFacilitySite().getEmissionsReport().getMasterFacilityRecord().getFacilitySourceTypeCode() != null
            && ConstantUtils.FACILITY_SOURCE_LANDFILL_CODE.contentEquals(emissionsUnit.getFacilitySite().getEmissionsReport().getMasterFacilityRecord().getFacilitySourceTypeCode().getCode())) {

            // Warning if the facility source type code is landfill and there are no processes or all processes are permanently shutdown,
            // then unit will not be copied forward
            List<EmissionsProcess> epList = emissionsUnit.getEmissionsProcesses().stream()
                .filter(emissionsProcess -> !ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(emissionsProcess.getOperatingStatusCode().getCode()))
                .collect(Collectors.toList());

            if (epList.isEmpty() && ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(emissionsUnit.getOperatingStatusCode().getCode())) {

                result = false;
                context.addFederalWarning(
                    ValidationField.EMISSIONS_UNIT_STATUS_CODE.value(),
                    "emissionsUnit.statusTypeCode.psNotCopied",
                    createValidationDetails(emissionsUnit));
            }

            // Warning if process operation status is permanently shutdown process will not be copied forward
            for (EmissionsProcess ep : emissionsUnit.getEmissionsProcesses()) {
                if (ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(ep.getOperatingStatusCode().getCode())) {

                    result = false;
                    context.addFederalWarning(
                        ValidationField.PROCESS_STATUS_CODE.value(),
                        "emissionsProcess.statusTypeCode.psNotCopied",
                        createEmissionsProcessValidationDetails(ep));
                }
            }
        }

        // If unit operation status is not operating, status year is required
        if (!ConstantUtils.STATUS_OPERATING.contentEquals(emissionsUnit.getOperatingStatusCode().getCode()) && emissionsUnit.getStatusYear() == null) {

            result = false;
            context.addFederalError(
                ValidationField.EMISSIONS_UNIT_STATUS_CODE.value(), "emissionsUnit.statusTypeCode.required",
                createValidationDetails(emissionsUnit));
        }

        // Status year must be between 1900 and the report year
        if (emissionsUnit.getStatusYear() != null && (emissionsUnit.getStatusYear() < 1900 || emissionsUnit.getStatusYear() > emissionsUnit.getFacilitySite().getEmissionsReport().getYear())) {

            result = false;
            context.addFederalError(
                ValidationField.EMISSIONS_UNIT_STATUS_YEAR.value(), "emissionsUnit.statusYear.range",
                createValidationDetails(emissionsUnit),
                emissionsUnit.getFacilitySite().getEmissionsReport().getYear().toString());
        }

        // Emissions Unit identifier must be unique within the facility.
        if (emissionsUnit != null && emissionsUnit.getFacilitySite() != null && emissionsUnit.getFacilitySite().getEmissionsUnits() != null) {
            Map<Object, List<EmissionsUnit>> euMap = emissionsUnit.getFacilitySite().getEmissionsUnits().stream()
                .filter(eu -> (eu.getUnitIdentifier() != null))
                .collect(Collectors.groupingBy(feu -> feu.getUnitIdentifier().trim().toLowerCase()));


            for (List<EmissionsUnit> euList : euMap.values()) {

                if (euList.size() > 1 && euList.get(0).getUnitIdentifier().trim().toLowerCase().contentEquals(emissionsUnit.getUnitIdentifier().trim().toLowerCase())) {

                    result = false;
                    context.addFederalError(
                        ValidationField.EMISSIONS_UNIT_IDENTIFIER.value(),
                        "emissionsUnit.unitIdentifier.duplicate",
                        createValidationDetails(emissionsUnit));
                }
            }
        }

        //Only run the following checks is the Unit status is operating. Otherwise, these checks are moot b/c
        //the data will not be sent to EIS and the user shouldn't have to go back and update them. Only id, status,
        //and status year are sent to EIS for units that are not operating.
        if (ConstantUtils.STATUS_OPERATING.contentEquals(emissionsUnit.getOperatingStatusCode().getCode())) {
            // Design capacity error
            if (emissionsUnit.getUnitTypeCode() != null && emissionsUnit.getDesignCapacity() == null) {
                result = checkEmptyDesignCapacityAgainstNaicsCodeAndUnitTypeCodes(emissionsUnit, context, result);
            }

            // Design capacity range
            if ((emissionsUnit.getDesignCapacity() != null)
                && (emissionsUnit.getDesignCapacity().compareTo(BigDecimal.valueOf(0.01)) == -1 || emissionsUnit.getDesignCapacity().compareTo(BigDecimal.valueOf(100000000)) == 1)) {

                result = false;
                context.addFederalError(
                    ValidationField.EMISSIONS_UNIT_CAPACITY.value(),
                    "emissionsUnit.capacity.range",
                    createValidationDetails(emissionsUnit));
            }

            // Design capacity and UoM must be reported together.
            if ((emissionsUnit.getDesignCapacity() != null && emissionsUnit.getUnitOfMeasureCode() == null)
                || (emissionsUnit.getDesignCapacity() == null && emissionsUnit.getUnitOfMeasureCode() != null)) {

                result = false;
                context.addFederalError(
                    ValidationField.EMISSIONS_UNIT_CAPACITY.value(),
                    "emissionsUnit.capacity.required",
                    createValidationDetails(emissionsUnit));
            }
            // Cannot report legacy UoM
            if (emissionsUnit.getUnitOfMeasureCode() != null &&
                (Boolean.TRUE.equals(emissionsUnit.getUnitOfMeasureCode().getLegacy())
                    || Boolean.FALSE.equals(emissionsUnit.getUnitOfMeasureCode().getUnitDesignCapacity())
                    || (emissionsUnit.getUnitOfMeasureCode().getLastInventoryYear() != null 
                    && emissionsUnit.getUnitOfMeasureCode().getLastInventoryYear() < emissionsUnit.getFacilitySite().getEmissionsReport().getYear()))) {
                result = false;
                context.addFederalError(
                    ValidationField.EMISSIONS_UNIT_UOM.value(),
                    "emissionsUnit.capacity.legacy",
                    createValidationDetails(emissionsUnit),
                    emissionsUnit.getUnitOfMeasureCode().getCode());
            }
        }

        // checking unit processes with the same SCC code - duplicate processes
        Map<Object, List<EmissionsProcess>> sccProcessMap = emissionsUnit.getEmissionsProcesses().stream()
            .filter(ep -> ConstantUtils.STATUS_OPERATING.contentEquals(ep.getOperatingStatusCode().getCode()) && ep.getSccCode() != null)
            .collect(Collectors.groupingBy(EmissionsProcess::getSccCode));

        List<EmissionsProcess> allDuplicateProcesses = new ArrayList<>();
        List<EmissionsProcess> duplicateProcessAndFuelDataList = new ArrayList<>();
        List<EmissionsProcess> duplicateProcessNoFuelDataList = new ArrayList<>();
        List<EmissionsProcess> duplicateProcessSingleFuelList = new ArrayList<>();
        List<EmissionsProcess> notDuplicateProcessList = new ArrayList<>();
        List<Pollutant> duplicateProcessDuplicateEmissionsList = new ArrayList<>();
        List<Pollutant> duplicateEmissionsListDualThroughputScc = new ArrayList<>();

        Boolean fuelUseRequired;

        if (sccProcessMap.size() > 0) {
            for (List<EmissionsProcess> pList : sccProcessMap.values()) {
                duplicateProcessAndFuelDataList.clear();
                duplicateProcessNoFuelDataList.clear();
                duplicateProcessSingleFuelList.clear();
                notDuplicateProcessList.clear();
                allDuplicateProcesses.clear();
                duplicateProcessDuplicateEmissionsList.clear();
                duplicateEmissionsListDualThroughputScc.clear();
                PointSourceSccCode scc = sccRepo.findById(pList.get(0).getSccCode()).orElse(null);
                fuelUseRequired = scc != null ? scc.getFuelUseRequired() : null;

                // checks processes with the same SCC
                if (pList.size() > 1) {
                    for (int i = 0; i < pList.size() - 1; i++) {
                        for (int j = i + 1; j < pList.size(); j++) {

                            // check if process details are the same
                            boolean diffProcessDetails = false; // process, reporting period type, rp appt details
                            boolean diffOpDetails = false;
                            boolean sameRpOpType = false;

                            // compare process info
                            diffProcessDetails = diffProcessDetails || (!Objects.equals(pList.get(i).getStatusYear(), pList.get(j).getStatusYear()));

                            SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(emissionsUnit.getFacilitySite().getEmissionsReport().getProgramSystemCode().getCode());
                            Short monthlyReportingInitialYear = sltConfig.getSltMonthlyFuelReportingInitialYear();
                            Boolean monthlyReportingEnabled = Boolean.TRUE.equals(sltConfig.getSltMonthlyFuelReportingEnabled())
                                && (monthlyReportingInitialYear == null || emissionsUnit.getFacilitySite().getEmissionsReport().getYear() >= monthlyReportingInitialYear);

                            // only look at annual rp when monthly reporting is not enabled
                            if (!monthlyReportingEnabled || (scc != null && !scc.getMonthlyReporting())) {
                                for (EmissionsProcess proc : pList) {
                                    if (proc.getReportingPeriods().size() > 1) {
                                        List<ReportingPeriod> annualRp = proc.getReportingPeriods().stream()
                                            .filter(p -> p.getReportingPeriodTypeCode().getShortName().equals(ConstantUtils.ANNUAL))
                                            .collect(Collectors.toList());
                                        proc.setReportingPeriods(annualRp);
                                    }
                                }
                            }

                            // compare reporting period and operating details if reporting period exists
                            if (pList.get(i).getReportingPeriods().size() > 0 && pList.get(j).getReportingPeriods().size() > 0
                                && pList.get(i).getReportingPeriods().size() == pList.get(j).getReportingPeriods().size()) {

                                pList.get(i).getReportingPeriods().sort(Comparator.comparing(rp -> rp.getReportingPeriodTypeCode().getCode()));
                                pList.get(j).getReportingPeriods().sort(Comparator.comparing(rp -> rp.getReportingPeriodTypeCode().getCode()));

                                //start for loop
                                for (int k = 0; k <= pList.get(i).getReportingPeriods().size() - 1; k++) {

                                    // should be 1 detail per period. If for some reason both periods don't have 1, they're different
                                    if (pList.get(i).getReportingPeriods().get(k).getOperatingDetails().size() != pList.get(j).getReportingPeriods().get(k).getOperatingDetails().size()) {

                                        diffOpDetails = true;

                                        // if they have the same number and on is empty, they're both empty and we can skip details checks
                                    } else if (!pList.get(i).getReportingPeriods().get(k).getOperatingDetails().isEmpty()) {

                                        OperatingDetail processA = pList.get(i).getReportingPeriods().get(k).getOperatingDetails().get(0);
                                        OperatingDetail processB = pList.get(j).getReportingPeriods().get(k).getOperatingDetails().get(0);

                                        // compare operating details
                                        diffOpDetails = diffOpDetails || (!Objects.equals(processA.getActualHoursPerPeriod(), processB.getActualHoursPerPeriod()));
                                        diffOpDetails = diffOpDetails || (!Objects.equals(processA.getAvgWeeksPerPeriod(), processB.getAvgWeeksPerPeriod()));
                                        diffOpDetails = diffOpDetails || (!Objects.equals(processA.getAvgDaysPerWeek(), processB.getAvgDaysPerWeek()));
                                        diffOpDetails = diffOpDetails || (!Objects.equals(processA.getAvgHoursPerDay(), processB.getAvgHoursPerDay()));
                                        diffOpDetails = diffOpDetails || (!Objects.equals(processA.getPercentFall(), processB.getPercentFall()));
                                        diffOpDetails = diffOpDetails || (!Objects.equals(processA.getPercentSpring(), processB.getPercentSpring()));
                                        diffOpDetails = diffOpDetails || (!Objects.equals(processA.getPercentSummer(), processB.getPercentSummer()));
                                        diffOpDetails = diffOpDetails || (!Objects.equals(processA.getPercentWinter(), processB.getPercentWinter()));

                                        // compare reporting period
                                        diffProcessDetails = !Objects.equals(pList.get(i).getReportingPeriods().get(k).getReportingPeriodTypeCode(),
                                            pList.get(j).getReportingPeriods().get(k).getReportingPeriodTypeCode());
                                        sameRpOpType = Objects.equals(pList.get(i).getReportingPeriods().get(k).getEmissionsOperatingTypeCode(),
                                            pList.get(j).getReportingPeriods().get(k).getEmissionsOperatingTypeCode());
                                    }
                                    //end for loop
                                }

                            } else {
                                // process details do not match if reporting period list sizes are not equal
                                diffProcessDetails = diffProcessDetails || ((pList.get(i).getReportingPeriods().size() > 0 && pList.get(j).getReportingPeriods().size() == 0)
                                    || pList.get(i).getReportingPeriods().size() == 0 && pList.get(j).getReportingPeriods().size() > 0);
                            }

                            // compare release point apportionments
                            int sameRpAppt = pList.get(j).getReleasePointAppts().size();
                            if (pList.get(i).getReleasePointAppts().size() == sameRpAppt) {
                                if (pList.get(i).getReleasePointAppts().size() > 0) {
                                    for (ReleasePointAppt rpa1 : pList.get(i).getReleasePointAppts()) {
                                        for (ReleasePointAppt rpa2 : pList.get(j).getReleasePointAppts()) {
                                            if ((Objects.equals(rpa1.getReleasePoint().getId(), rpa2.getReleasePoint().getId()))
                                                && rpa1.getPercent().compareTo(rpa2.getPercent()) == 0) {
                                                if (((rpa1.getControlPath() != null && rpa2.getControlPath() != null)
                                                    && Objects.equals(rpa1.getControlPath().getId(), rpa2.getControlPath().getId()))
                                                    || (rpa1.getControlPath() == null && rpa2.getControlPath() == null)) {
                                                    --sameRpAppt;
                                                }
                                            }
                                        }
                                    }
                                    // sameRpAppt should be 0 if all release point appt details match
                                    diffProcessDetails = diffProcessDetails || (sameRpAppt != 0);
                                }
                            } else {
                                // release point appt details do not match if total size of list are not equal
                                diffProcessDetails = diffProcessDetails || (pList.get(i).getReleasePointAppts().size() != sameRpAppt);
                            }

                            // processes considered duplicates - CHECK DUPLICATE FUEL
                            // same process details	same operating details same reporting period op type
                            // TRUE					TRUE					TRUE
                            // processes considered not duplicates - CHECK WARNING DUPLICATE
                            // FALSE				FALSE/TRUE				TRUE
                            // TRUE					FALSE					TRUE
                            // TRUE					FALSE/TRUE				FALSE
                            // FALSE				FALSE/TRUE				FALSE
                            // note: any time the reporting period operating type is different the process is not a duplicate process

                            // processes are the same if all the details are the same
                            if (!diffProcessDetails && !diffOpDetails && sameRpOpType) {

                                // add process to list if fuel data exists
                                if (checkFuelFields(pList.get(i).getReportingPeriods().get(0)) && !duplicateProcessAndFuelDataList.contains(pList.get(i))
                                    && checkFuelFields(pList.get(j).getReportingPeriods().get(0)) && !duplicateProcessAndFuelDataList.contains(pList.get(j))) {
                                    duplicateProcessAndFuelDataList.add(pList.get(i));
                                    duplicateProcessAndFuelDataList.add(pList.get(j));

                                    // add process to list if no fuel data exists
                                } else if (!checkFuelFields(pList.get(i).getReportingPeriods().get(0)) && !checkFuelFields(pList.get(j).getReportingPeriods().get(0))) {
                                    if (!duplicateProcessNoFuelDataList.contains(pList.get(i))) {
                                        duplicateProcessNoFuelDataList.add(pList.get(i));
                                    }
                                    if (!duplicateProcessNoFuelDataList.contains(pList.get(j))) {
                                        duplicateProcessNoFuelDataList.add(pList.get(j));
                                    }

                                    // add process to list if only one process has fuel data
                                } else {
                                    if (checkFuelFields(pList.get(i).getReportingPeriods().get(0)) && !duplicateProcessSingleFuelList.contains(pList.get(i))) {
                                        duplicateProcessSingleFuelList.add(pList.get(i));
                                    }
                                    if (checkFuelFields(pList.get(j).getReportingPeriods().get(0)) && !duplicateProcessSingleFuelList.contains(pList.get(j))) {
                                        duplicateProcessSingleFuelList.add(pList.get(j));
                                    }
                                }

                                if (!allDuplicateProcesses.contains(pList.get(i))) {
                                    allDuplicateProcesses.add(pList.get(i));
                                }
                                if (!allDuplicateProcesses.contains(pList.get(j))) {
                                    allDuplicateProcesses.add(pList.get(j));
                                }

                                // duplicate processes cannot have same pollutants
                                // one error message per duplicated emission
                                List<Emission> processA_emissions = pList.get(i).getReportingPeriods().get(0).getEmissions();
                                List<Emission> processB_emissions = pList.get(j).getReportingPeriods().get(0).getEmissions();
                                if (processA_emissions.size() > 0 && processB_emissions.size() > 0) {
                                    for (Emission eA : processA_emissions) {
                                        for (Emission eB : processB_emissions) {
                                            if (eA.getPollutant().getPollutantCode().equals(eB.getPollutant().getPollutantCode())) {

                                                if (!duplicateProcessDuplicateEmissionsList.contains(eA.getPollutant())) {
                                                    // if scc is dual throughput, add to specific list to show
                                                    // warning instead of error
                                                    if (scc != null && scc.getDualThroughput()) {
                                                        duplicateEmissionsListDualThroughputScc.add(eA.getPollutant());
                                                    }
                                                    // all other cases, add to dupe list
                                                    else {
                                                        duplicateProcessDuplicateEmissionsList.add(eA.getPollutant());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // generate a warning when process has same SCC and one of the following
                            // - process details are different
                            // - operating details are different
                            // - operating type is different
                            if (diffProcessDetails || diffOpDetails || !sameRpOpType) {

                                if (!notDuplicateProcessList.contains(pList.get(i))) {
                                    notDuplicateProcessList.add(pList.get(i));
                                }
                                if (!notDuplicateProcessList.contains(pList.get(j))) {
                                    notDuplicateProcessList.add(pList.get(j));
                                }

                            }
                        }
                        // check fuel use values for non duplicated processes if reporting period exists
                        if (!allDuplicateProcesses.contains(pList.get(i)) && pList.get(i).getReportingPeriods().size() > 0) {
                            result = checkFuelData(validatorContext, pList.get(i));
                        }
                    }

                    // check fuel use values for last value in pList if it is not a duplicated processes and reporting period exists
                    if (!allDuplicateProcesses.contains(pList.get(pList.size() - 1)) && pList.get(pList.size() - 1).getReportingPeriods().size() > 0) {
                        result = checkFuelData(validatorContext, pList.get(pList.size() - 1));
                    }

                    // check fuel use conditions if process scc has only one process
                } else if (pList.size() == 1 && pList.get(0).getReportingPeriods().size() > 0) {
                    result = checkFuelData(validatorContext, pList.get(0));
                }

                // check fuel use conditions for duplicate process only if one of the processes of a given SCC has fuel data
                if (duplicateProcessSingleFuelList.size() == 1) {
                    result = checkFuelData(validatorContext, duplicateProcessSingleFuelList.get(0));
                }

                // error generated if more than one process for a given SCC code has fuel use data,
                // and if duplicate processes for a given SCC code do not have fuel use data and fuel use is required
                if (duplicateProcessAndFuelDataList.size() > 1
                    || (duplicateProcessNoFuelDataList.size() > 1 && duplicateProcessNoFuelDataList.size() == allDuplicateProcesses.size()
                    && Boolean.TRUE.equals(fuelUseRequired))) {

                    result = false;
                    context.addFederalError(
                        ValidationField.PERIOD_DUP_SCC_FUEL_USE.value(),
                        "emissionsUnit.emissionsProcess.sccDuplicate.fuelUseData",
                        createValidationDetails(emissionsUnit),
                        scc.getCode());
                }

                // error generated for each duplicate pollutant of duplicated process
                if (duplicateProcessDuplicateEmissionsList.size() > 0) {
                    for (Pollutant e : duplicateProcessDuplicateEmissionsList) {

                        result = false;
                        context.addFederalError(
                            ValidationField.EMISSIONS_UNIT_PROCESS.value(),
                            "emissionsUnit.emissionsProcess.sccDuplicate.duplicatePollutant",
                            createValidationDetails(emissionsUnit),
                            scc.getCode(),
                            e.getPollutantName());
                    }
                }

                // warning generated for each duplicate pollutant of duplicated process where scc is dual throughput scc
                if (duplicateEmissionsListDualThroughputScc.size() > 0) {
                    for (Pollutant e : duplicateEmissionsListDualThroughputScc) {

                        result = false;
                        context.addFederalWarning(
                            ValidationField.EMISSIONS_UNIT_PROCESS.value(),
                            "emissionsUnit.emissionsProcess.sccDualThroughput.duplicatePollutant",
                            createValidationDetails(emissionsUnit),
                            scc.getCode(),
                            e.getPollutantName());
                    }
                }

                // warning generated if there are multiple processes for a given SCC
                if (notDuplicateProcessList.size() > 0) {

                    result = false;
                    context.addFederalWarning(
                        ValidationField.EMISSIONS_UNIT_PROCESS.value(),
                        "emissionsUnit.emissionsProcess.sccDuplicate.notDupProcessWarning",
                        createValidationDetails(emissionsUnit),
                        notDuplicateProcessList.get(0).getSccCode());
                }

            }
        }

        // Process identifier must be unique within unit
        Map<Object, List<EmissionsProcess>> epMap = emissionsUnit.getEmissionsProcesses().stream()
            .filter(ep -> ep.getEmissionsProcessIdentifier() != null)
            .collect(Collectors.groupingBy(eu -> eu.getEmissionsProcessIdentifier().toLowerCase().trim()));

        for (List<EmissionsProcess> epList : epMap.values()) {

            if (epList.size() > 1) {

                result = false;
                context.addFederalError(
                    ValidationField.EMISSIONS_UNIT_PROCESS.value(),
                    "emissionsUnit.emissionsProcess.duplicate",
                    createValidationDetails(emissionsUnit),
                    epList.get(0).getEmissionsProcessIdentifier());

            }
        }

        return result;
    }

    // checks duplicate process for any of the fuel data fields exist
    private boolean checkFuelFields(ReportingPeriod period) {
        return (period.getFuelUseValue() != null || period.getFuelUseUom() != null || period.getFuelUseMaterialCode() != null
                || period.getHeatContentValue() != null || period.getHeatContentUom() != null);
    }

    // Fuel Use Input Checks - after checking for duplicate processes
    private boolean checkFuelData(ValidatorContext validatorContext, EmissionsProcess process) {
        CefValidatorContext context = getCefValidatorContext(validatorContext);
        boolean result = true;
        PointSourceSccCode sccCode = sccRepo.findById(process.getSccCode()).orElse(null);

        SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(process.getEmissionsUnit().getFacilitySite().getEmissionsReport().getProgramSystemCode().getCode());
        Short monthlyReportingInitialYear = sltConfig.getSltMonthlyFuelReportingInitialYear();
        Boolean monthlyReportingEnabled = Boolean.TRUE.equals(sltConfig.getSltMonthlyFuelReportingEnabled())
            && (monthlyReportingInitialYear == null || process.getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear() >= monthlyReportingInitialYear);

        if (sccCode != null) {
	        Boolean fuelDataRequired = sccCode.getFuelUseRequired();

            List<ReportingPeriod> periods = process.getReportingPeriods();
	    	// asphalt SCCs that require fuel use data for monthly reporting are included in QA when monthly reporting is enabled
	        if (monthlyReportingEnabled && sccCode.getMonthlyReporting()) {
	        	fuelDataRequired = true;

                // when monthlyReporting is enabled, must submit semiannual then annual report
                // filter rps down depending on if semiannual has been submitted/approved
                if (context.isEnabled(ValidationFeature.ANNUAL)) {
                    periods = periods.stream().filter(rp -> ConstantUtils.SECOND_HALF_MONTHS.contains(rp.getReportingPeriodTypeCode().getShortName())
                                                            || ConstantUtils.ANNUAL.equals(rp.getReportingPeriodTypeCode().getShortName())).collect(Collectors.toList());
                }
                else if (context.isEnabled(ValidationFeature.SEMIANNUAL)){
                    periods = periods.stream().filter(rp -> ConstantUtils.SEMI_ANNUAL_MONTHS.contains(rp.getReportingPeriodTypeCode().getShortName())
                                                            || ConstantUtils.ANNUAL.equals(rp.getReportingPeriodTypeCode().getShortName())).collect(Collectors.toList());
                }
	        }
            // only consider annual reporting period if monthly reporting is not enabled or the process scc is not a monthly reporting scc
            else {
                periods = periods.stream().filter(rp -> ConstantUtils.ANNUAL.equals(rp.getReportingPeriodTypeCode().getShortName())).collect(Collectors.toList());
            }

            for (ReportingPeriod period : periods) {

                if (fuelDataRequired) {
                    // Fuel Material, Fuel Value, and Fuel UoM are required when the Process SCC requires fuel use or is monthly reporting SCC
                    if ((period.getFuelUseValue() == null || period.getFuelUseUom() == null || period.getFuelUseMaterialCode() == null)
                        && ConstantUtils.ANNUAL.equals(period.getReportingPeriodTypeCode().getShortName())) {

                        result = false;
                        context.addFederalError(
                            ValidationField.PERIOD_FUEL_USE_VALUES.value(),
                            "reportingPeriod.fuelUseValues.required",
                            createEmissionsProcessValidationDetails(process),
                            process.getSccCode());
                    }
                    if (period.getFuelUseValue() == null && !ConstantUtils.ANNUAL.equals(period.getReportingPeriodTypeCode().getShortName())
                        && ConstantUtils.ALL_REPORTING_PERIODS.indexOf(period.getEmissionsProcess().getInitialMonthlyReportingPeriod()) <= ConstantUtils.ALL_REPORTING_PERIODS.indexOf(period.getReportingPeriodTypeCode().getShortName())) {

                        result = false;
                        context.addFederalError(
                            ValidationField.PERIOD_FUEL_VALUE.value(),
                            "reportingPeriod.fuelUseValue.monthly.required",
                            createMonthlyReportingValidationDetails(period),
                            process.getSccCode());
                    }

                    // Heat Content Value and Heat Content UoM when the Process SCC requires fuel use
                    if (sccCode.getFuelUseRequired() && (period.getHeatContentUom() == null || period.getHeatContentValue() == null)
                        && ConstantUtils.ANNUAL.equals(period.getReportingPeriodTypeCode().getShortName())) {

                        result = false;
                        context.addFederalError(
                            ValidationField.PERIOD_HEAT_CONTENT_VALUES.value(),
                            "reportingPeriod.heatContentValues.required",
                            createEmissionsProcessValidationDetails(process),
                            process.getSccCode());
                    }

                }
                // when Process SCC is not monthly reporting SCC and monthly reporting is enabled OR Process SCC does not require fuel use
                if (!sccCode.getFuelUseRequired() && !(sccCode.getMonthlyReporting() && monthlyReportingEnabled)
                    && ConstantUtils.ANNUAL.equals(period.getReportingPeriodTypeCode().getShortName())) {
                    // warning is generated that all fuel use fields must be reported for any to be submitted with the report.
                    if ((period.getFuelUseValue() != null || period.getFuelUseUom() != null || period.getFuelUseMaterialCode() != null) &&
                        (period.getFuelUseUom() == null || period.getFuelUseMaterialCode() == null || period.getFuelUseValue() == null)) {

                        result = false;
                        context.addFederalWarning(
                            ValidationField.PERIOD_FUEL_USE_VALUES.value(),
                            "reportingPeriod.fuelUseValues.optionalFields.required",
                            createEmissionsProcessValidationDetails(process));

                    }

                    // warning is generated that all Heat Content fields must be reported for any to be submitted with the report.
                    if ((period.getHeatContentValue() != null || period.getHeatContentUom() != null) &&
                        (period.getHeatContentUom() == null || period.getHeatContentValue() == null)) {

                        result = false;
                        context.addFederalWarning(
                            ValidationField.PERIOD_HEAT_CONTENT_VALUES.value(),
                            "reportingPeriod.heatContentValues.optionalFields.required",
                            createEmissionsProcessValidationDetails(process));

                    }

                    if ((period.getFuelUseValue() != null || period.getFuelUseUom() != null || period.getFuelUseMaterialCode() != null) &&
                        sccCode.getDualThroughput()) {
                        result = false;
                        context.addFederalWarning(
                            ValidationField.PERIOD_FUEL_USE_VALUES.value(),
                            "emissionsUnit.emissionsProcess.sccDualThroughput.fuel",
                            createEmissionsProcessValidationDetails(process));
                    }
                }
            }
        }
        return result;
    }

    private ValidationDetailDto createValidationDetails(EmissionsUnit source) {

        String description = MessageFormat.format("Emissions Unit: {0}", source.getUnitIdentifier());

        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), source.getUnitIdentifier(), EntityType.EMISSIONS_UNIT, description);
        return dto;
    }

    private String getEmissionsUnitIdentifier(EmissionsProcess process) {
        if (process.getEmissionsUnit() != null) {
            return process.getEmissionsUnit().getUnitIdentifier();
        }
        return null;
    }

    private ValidationDetailDto createEmissionsProcessValidationDetails(EmissionsProcess source) {

        String description = MessageFormat.format("Emissions Unit: {0}, Emissions Process: {1}",
            getEmissionsUnitIdentifier(source),
            source.getEmissionsProcessIdentifier());

        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), source.getEmissionsProcessIdentifier(), EntityType.EMISSIONS_PROCESS, description);
        return dto;
    }

    private ValidationDetailDto createMonthlyReportingValidationDetails(ReportingPeriod source) {

        String description = MessageFormat.format("Emissions Unit: {0}, Emissions Process: {1}, Month: {2}",
            getEmissionsUnitIdentifier(source.getEmissionsProcess()),
            source.getEmissionsProcess().getEmissionsProcessIdentifier(),
            source.getReportingPeriodTypeCode().getShortName());

        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), source.getEmissionsProcess().getEmissionsProcessIdentifier(), EntityType.REPORTING_PERIOD, description);
        if (source.getEmissionsProcess() != null) {
            dto.getParents().add(new ValidationDetailDto(
                source.getEmissionsProcess().getId(),
                source.getEmissionsProcess().getEmissionsProcessIdentifier(),
                EntityType.SEMIANNUAL_REPORTING_PERIOD));
        }
        return dto;
    }
}
