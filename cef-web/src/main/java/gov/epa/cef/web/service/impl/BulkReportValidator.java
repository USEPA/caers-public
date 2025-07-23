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

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.exception.BulkReportValidationException;
import gov.epa.cef.web.service.LookupService;
import gov.epa.cef.web.service.dto.bulkUpload.*;
import gov.epa.cef.web.util.ConstantUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class BulkReportValidator {

    static final String SPREADSHEET_MAJOR_VERSION = "4";

    private final Validator validator;

    private static final String INVALID_VALUE_LOOKUP_MESSAGE = "The %1$s %2$s '%3$s' is not a valid %2$s. Please check the JSON " +
        "upload reference spreadsheet for allowed values.";

    @Autowired
    private LookupService lookupService;

    @Autowired
    BulkReportValidator(Validator validator) {

        this.validator = validator;
    }

    public void validate(EmissionsReportBulkUploadDto report) {
        validate(report, true);
    }

    public void validate(EmissionsReportBulkUploadDto report, boolean excel) {

        List<WorksheetError> violations = new ArrayList<>();

        String regex = String.format("^%s(\\.\\d+)?$", SPREADSHEET_MAJOR_VERSION);

        if (excel && (report.getVersions().isEmpty() || !report.getVersions().get(0).getVersion().matches(regex))) {

            String msg = "This spreadsheet is out of date. Please download the most recent version of the spreadsheet.";
            violations.add(new WorksheetError(WorksheetName.Version.toString(), -1, msg));
            throw new BulkReportValidationException(violations);
        }

        WorksheetDtoValidator worksheetValidator = new WorksheetDtoValidator(this.validator, violations);

        Consumer<FacilitySiteBulkUploadDto> siteIdCheck = new FacilityIdValidator(report, violations);
        Consumer<EmissionsUnitBulkUploadDto> emissionsUnitCheck = new EmissionsUnitValidator(violations);
        Consumer<EmissionsProcessBulkUploadDto> emissionsProcessCheck = new EmissionsProcessValidator(violations);
        Consumer<ReportingPeriodBulkUploadDto> reportingPeriodCheck = new ReportingPeriodValidator(report, violations);
        Consumer<ReleasePointBulkUploadDto> releasePointCheck = new ReleasePointValidator(violations);
        Consumer<ControlBulkUploadDto> controlCheck = new ControlValidator(violations);
        Consumer<ControlPollutantBulkUploadDto> controlPollutantCheck = new ControlPollutantValidator(violations);
        Consumer<ControlPathBulkUploadDto> controlPathCheck = new ControlPathValidator(violations);
        Consumer<ControlPathPollutantBulkUploadDto> controlPathPollutantCheck = new ControlPathPollutantValidator(violations);
        Consumer<List <ControlAssignmentBulkUploadDto>> loopCheck = new ControlAssignmentLoopValidator(report, violations);
        Consumer<ControlAssignmentBulkUploadDto> controlAssignmentCheck = new ControlAssignmentValidator(violations);
        Consumer<EmissionBulkUploadDto> capPollutantCheck = new CapPollutantValidator(report, violations);

        report.getFacilitySites().forEach(siteIdCheck.andThen(worksheetValidator));
        report.getEmissionsUnits().forEach(emissionsUnitCheck.andThen(worksheetValidator));
        report.getEmissionsProcesses().forEach(emissionsProcessCheck.andThen(worksheetValidator));
        report.getReleasePoints().forEach(releasePointCheck.andThen(worksheetValidator));
        report.getReleasePointAppts().forEach(worksheetValidator);
        report.getReportingPeriods().forEach(reportingPeriodCheck.andThen(worksheetValidator));
        report.getOperatingDetails().forEach(worksheetValidator);
        report.getEmissions().forEach(capPollutantCheck.andThen(worksheetValidator));
        report.getEmissionFormulaVariables().forEach(worksheetValidator);
        report.getControlPaths().forEach(controlPathCheck.andThen(worksheetValidator));
        report.getControls().forEach(controlCheck.andThen(worksheetValidator));
        loopCheck.accept(report.getControlAssignments());
        report.getControlAssignments().forEach(controlAssignmentCheck.andThen(worksheetValidator));
        report.getControlPollutants().forEach(controlPollutantCheck.andThen(worksheetValidator));
        report.getControlPathPollutants().forEach(controlPathPollutantCheck.andThen(worksheetValidator));
        report.getFacilityNAICS().forEach(worksheetValidator);
        report.getFacilityContacts().forEach(worksheetValidator);

        if (!excel) { // Additional JSON report validations. Separate for performance reasons

            Consumer<FacilitySiteBulkUploadDto> facilitySiteTypeCheck = new facilitySiteJSONValidator(violations);
            Consumer<FacilitySiteContactBulkUploadDto> contactTypeCheck = new FacilitySiteContactJSONValidator(violations);
            Consumer<FacilityNAICSBulkUploadDto> naicsTypeCheck = new FacilityNAICSJSONValidator(violations);
            Consumer<ReleasePointBulkUploadDto> releasePointTypeCheck = new ReleasePointJSONValidator(violations);
            Consumer<EmissionsUnitBulkUploadDto> emissionsUnitTypeCheck = new EmissionsUnitJSONValidator(violations);
            Consumer<EmissionsProcessBulkUploadDto> emissionsProcessTypeCheck = new EmissionsProcessJSONValidator(violations);
            Consumer<ControlBulkUploadDto> controlTypeCheck = new ControlJSONValidator(violations);
            BiConsumer<ControlAssignmentBulkUploadDto, List<ControlPathBulkUploadDto>> controlAssignmentAndPathInvalidCheck = new ControlAssignmentAndPathJSONValidator(violations);
            BiConsumer<ControlAssignmentBulkUploadDto, List<ControlBulkUploadDto>> controlAssignmentAndDeviceInvalidCheck = new ControlAssignmentAndDeviceJSONValidator(violations);
            Consumer<ControlPollutantBulkUploadDto> controlPollutantTypeCheck = new ControlPollutantJSONValidator(violations);
            Consumer<ControlPathPollutantBulkUploadDto> controlPathPollutantTypeCheck = new ControlPathPollutantJSONValidator(violations);
            BiConsumer<ReleasePointApptBulkUploadDto, List<ReleasePointBulkUploadDto>> apptAndReleasePointInvalidCheck = new ReleasePointApptAndReleasePointJSONValidator(violations);
            BiConsumer<ReleasePointApptBulkUploadDto, List<EmissionsProcessBulkUploadDto>> apptAndEmissionsProcessInvalidCheck = new ReleasePointApptAndEmissionProcessJSONValidator(violations);
            BiConsumer<ReleasePointApptBulkUploadDto, List<ControlPathBulkUploadDto>> apptAndControlPathInvalidCheck = new ReleasePointApptAndControlPathJSONValidator(violations);
            Consumer<ReportingPeriodBulkUploadDto> reportingPeriodTypeCheck = new ReportingPeriodJSONValidator(violations);
            Consumer<EmissionBulkUploadDto> emissionTypeCheck = new EmissionJSONValidator(report, violations);
            Consumer<EmissionFormulaVariableBulkUploadDto> formulaVariableTypeCheck = new FormulaVariableJSONValidator(violations);

            if (report.getFacilitySites() != null && !report.getFacilitySites().isEmpty()) {
                report.getFacilitySites().forEach(facilitySiteTypeCheck);
            } else {
                String msg = "The Facility Site is missing. Please check the JSON upload reference spreadsheet for fields/allowed values.";
                violations.add(WorksheetError.createSystemError(msg));
            }
            report.getFacilityContacts().forEach(contactTypeCheck);
            report.getFacilityNAICS().forEach(naicsTypeCheck);
            report.getReleasePoints().forEach(releasePointTypeCheck);
            report.getEmissionsUnits().forEach(emissionsUnitTypeCheck);
            report.getEmissionsProcesses().forEach(emissionsProcessTypeCheck);
            report.getControls().forEach(controlTypeCheck);
            report.getControlAssignments().forEach(assignment -> controlAssignmentAndPathInvalidCheck.accept(assignment, report.getControlPaths()));
            report.getControlAssignments().forEach(assignment -> controlAssignmentAndDeviceInvalidCheck.accept(assignment, report.getControls()));
            report.getControlPollutants().forEach(controlPollutantTypeCheck);
            report.getControlPathPollutants().forEach((controlPathPollutantTypeCheck));
            report.getReleasePointAppts().forEach(appt -> apptAndReleasePointInvalidCheck.accept(appt, report.getReleasePoints()));
            report.getReleasePointAppts().forEach(appt -> apptAndEmissionsProcessInvalidCheck.accept(appt, report.getEmissionsProcesses()));
            report.getReleasePointAppts().forEach(appt -> apptAndControlPathInvalidCheck.accept(appt, report.getControlPaths()));
            report.getReportingPeriods().forEach(reportingPeriodTypeCheck);
            report.getEmissions().forEach(emissionTypeCheck);
            report.getEmissionFormulaVariables().forEach(formulaVariableTypeCheck);
        }

        if (violations.size() > 0) {

            throw new BulkReportValidationException(violations);
        }
    }

    static class EmissionsUnitValidator implements Consumer<EmissionsUnitBulkUploadDto> {

    	private final List<WorksheetError> violations;

    	public EmissionsUnitValidator(List<WorksheetError> violations) {
            this.violations = violations;
        }

    	List<String> checkedUnitIdentifierList = new ArrayList<String>();

    	public void accept(EmissionsUnitBulkUploadDto unit) {

            if (unit.getUnitIdentifier() != null && !checkedUnitIdentifierList.contains(unit.getUnitIdentifier().trim().toLowerCase())) {
            	checkedUnitIdentifierList.add(unit.getUnitIdentifier().trim().toLowerCase());
            } else {
            	String msg = String.format("Unit ID '%s' already exists within the facility. Duplicates are not allowed.", unit.getUnitIdentifier());
                violations.add(new WorksheetError(unit.getSheetName(), unit.getRow(), unit.getErrorIdentifier(), msg));
            }
    	}
    }

    private class EmissionsUnitJSONValidator implements Consumer<EmissionsUnitBulkUploadDto> {

        private final List<WorksheetError> violations;

        public EmissionsUnitJSONValidator(List<WorksheetError> violations) {
            this.violations = violations;
        }

        public void accept(EmissionsUnitBulkUploadDto unit) {

            UnitTypeCode unitTypeCode = null;
            OperatingStatusCode opStatusCode = null;
            UnitMeasureCode uomCode = null;

            if (Strings.emptyToNull(unit.getTypeCode()) != null) {
                unitTypeCode = lookupService.retrieveUnitTypeCodeEntityByCode(unit.getTypeCode());
            }
            if (Strings.emptyToNull(unit.getOperatingStatusCodeDescription()) != null) {
                opStatusCode = lookupService.retrieveSubFacilityOperatingStatusCodeEntityByCode(unit.getOperatingStatusCodeDescription());
            }
            if (Strings.emptyToNull(unit.getUnitOfMeasureCode()) != null) {
                uomCode = lookupService.retrieveUnitMeasureCodeEntityByCode(unit.getUnitOfMeasureCode());
            }

            if (Strings.emptyToNull(unit.getTypeCode()) != null && unitTypeCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Emissions Unit", "Type Code", unit.getTypeCode());
                violations.add(new WorksheetError(unit.getSheetName(), unit.getRow(), unit.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(unit.getOperatingStatusCodeDescription()) != null && opStatusCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Emissions Unit", "Operating Status Code", unit.getOperatingStatusCodeDescription());
                violations.add(new WorksheetError(unit.getSheetName(), unit.getRow(), unit.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(unit.getUnitOfMeasureCode()) != null && uomCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Emissions Unit", "UoM Code", unit.getUnitOfMeasureCode());
                violations.add(new WorksheetError(unit.getSheetName(), unit.getRow(), unit.getErrorIdentifier(), msg));
            }
        }
    }

    static class EmissionsProcessValidator implements Consumer<EmissionsProcessBulkUploadDto> {

    	private final List<WorksheetError> violations;

    	public EmissionsProcessValidator(List<WorksheetError> violations) {
            this.violations = violations;
        }

    	HashMap<Long, List<String>> checkUnitIdentifierList = new HashMap<Long, List<String>>();

    	public void accept(EmissionsProcessBulkUploadDto process) {

    		if (process.getEmissionsUnitId() != null && process.getEmissionsProcessIdentifier() != null) {

	            if (checkUnitIdentifierList.isEmpty() || !checkUnitIdentifierList.containsKey(process.getEmissionsUnitId())) {

	            	List<String> processList = new ArrayList<>();
	            	processList.add(process.getEmissionsProcessIdentifier().trim().toLowerCase());
	            	checkUnitIdentifierList.put(process.getEmissionsUnitId(), processList);

	            } else {
	            	List<String> processList = checkUnitIdentifierList.get(process.getEmissionsUnitId());

	            	if (processList.contains(process.getEmissionsProcessIdentifier().trim().toLowerCase())) {
	            		String msg = String.format("Process ID '%s' already exists for the emissions unit. Duplicates are not allowed.", process.getEmissionsProcessIdentifier());
	                    violations.add(new WorksheetError(process.getSheetName(), process.getRow(), process.getErrorIdentifier(), msg));
	            	}
	            }
    		}
    	}
    }

    private class EmissionsProcessJSONValidator implements Consumer<EmissionsProcessBulkUploadDto> {

        private final List<WorksheetError> violations;

        public EmissionsProcessJSONValidator(List<WorksheetError> violations) {
            this.violations = violations;
        }

        public void accept(EmissionsProcessBulkUploadDto process) {

            AircraftEngineTypeCode aircraftEngineTypeCode = null;
            OperatingStatusCode opStatusCode = null;

            if (Strings.emptyToNull(process.getAircraftEngineTypeCode()) != null) {
                aircraftEngineTypeCode = lookupService.retrieveAircraftEngineCodeEntityByCode(process.getAircraftEngineTypeCode());
            }
            if (Strings.emptyToNull(process.getOperatingStatusCode()) != null) {
                opStatusCode = lookupService.retrieveSubFacilityOperatingStatusCodeEntityByCode(process.getOperatingStatusCode());
            }

            if (Strings.emptyToNull(process.getAircraftEngineTypeCode()) != null && aircraftEngineTypeCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Emissions Process", "Aircraft Engine Type Code", process.getAircraftEngineTypeCode());
                violations.add(new WorksheetError(process.getSheetName(), process.getRow(), process.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(process.getOperatingStatusCode()) != null && opStatusCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Emissions Process", "Operating Status Code", process.getOperatingStatusCode());
                violations.add(new WorksheetError(process.getSheetName(), process.getRow(), process.getErrorIdentifier(), msg));
            }
        }
    }

    static class ReportingPeriodValidator implements Consumer<ReportingPeriodBulkUploadDto> {

        private final EmissionsReportBulkUploadDto report;

        private final List<WorksheetError> violations;

        private final Map<Long, List<OperatingDetailBulkUploadDto>> detailMap;

        public ReportingPeriodValidator(EmissionsReportBulkUploadDto report, List<WorksheetError> violations) {

            this.report = report;
            this.violations = violations;

            this.detailMap = this.report.getOperatingDetails().stream()
                                                              .filter(od -> od.getReportingPeriodId() != null)
                                                              .collect(Collectors.groupingBy(OperatingDetailBulkUploadDto::getReportingPeriodId));
        }

        HashMap<Long, List<String>> processPeriodMap = new HashMap<Long, List<String>>();

        public void accept(ReportingPeriodBulkUploadDto item) {

            if (item.getEmissionsProcessId() != null && !processPeriodMap.containsKey(item.getEmissionsProcessId())) {

                List<String> typeList = new ArrayList<>();
                typeList.add(item.getReportingPeriodTypeCode());
                processPeriodMap.put(item.getEmissionsProcessId(), typeList);

            } else {
                List<String> typeList = processPeriodMap.get(item.getEmissionsProcessId());

                // the following line can be used when we begin to allow multiple reporting periods, as long as they're different types
//                if (typeList != null && typeList.contains(item.getReportingPeriodTypeCode())) {
                if (typeList != null && !typeList.isEmpty()) {
                    String msg = "There is more than one Reporting Period reported for the emissions process. Only one Reporting Period per process is allowed.";
                    violations.add(new WorksheetError(item.getSheetName(), item.getRow(), item.getErrorIdentifier(), msg));
                }
            }

            // check to make sure there is exactly 1 operating details per reporting period
            if (!detailMap.containsKey(item.getId())) {
                String msg = "Reporting Period does not have associated operating details on the \"Operating Details\" tab.";
                violations.add(new WorksheetError(item.getSheetName(), item.getRow(), item.getErrorIdentifier(), msg));
            } else if (detailMap.get(item.getId()).size() > 1) {
                List<OperatingDetailBulkUploadDto> details = detailMap.get(item.getId());
                String msg = String.format("There is more than one Operating Details reported for the reporting period on rows %s. "
                        + "Only one Operating Details per period is allowed.",
                        details.stream().map(OperatingDetailBulkUploadDto::getRow).collect(Collectors.toList()).toString());
                violations.add(new WorksheetError(details.get(0).getSheetName(), details.get(0).getRow(), details.get(0).getErrorIdentifier(), msg));
            }
        }
    }

    private class ReportingPeriodJSONValidator implements Consumer<ReportingPeriodBulkUploadDto> {

        private final List<WorksheetError> violations;

        public ReportingPeriodJSONValidator(List<WorksheetError> violations) {
            this.violations = violations;
        }

        public void accept(ReportingPeriodBulkUploadDto reportingPeriod) {

            CalculationMaterialCode calculationMaterialCodeEntity = null;
            CalculationParameterTypeCode calculationParameterTypeCodeEntity = null;
            UnitMeasureCode calculationParameterUomEntity = null;
            EmissionsOperatingTypeCode emissionsOperatingTypeCodeEntity = null;
            ReportingPeriodCode reportingPeriodCodeEntity = null;
            CalculationMaterialCode fuelUseMaterialCodeEntity = null;
            UnitMeasureCode fuelUseUomEntity = null;
            UnitMeasureCode heatContentUomEntity = null;

            if (Strings.emptyToNull(reportingPeriod.getCalculationMaterialCode()) != null) {
                calculationMaterialCodeEntity = lookupService.retrieveCalcMaterialCodeEntityByCode(reportingPeriod.getCalculationMaterialCode());
            }
            if (Strings.emptyToNull(reportingPeriod.getCalculationParameterTypeCode()) != null) {
                calculationParameterTypeCodeEntity = lookupService.retrieveCalcParamTypeCodeEntityByCode(reportingPeriod.getCalculationParameterTypeCode());
            }
            if (Strings.emptyToNull(reportingPeriod.getCalculationParameterUom()) != null) {
                calculationParameterUomEntity = lookupService.retrieveUnitMeasureCodeEntityByCode(reportingPeriod.getCalculationParameterUom());
            }
            if (Strings.emptyToNull(reportingPeriod.getEmissionsOperatingTypeCode()) != null) {
                emissionsOperatingTypeCodeEntity = lookupService.retrieveEmissionsOperatingTypeCodeEntityByCode(reportingPeriod.getEmissionsOperatingTypeCode());
            }
            if (Strings.emptyToNull(reportingPeriod.getReportingPeriodTypeCode()) != null) {
                reportingPeriodCodeEntity = lookupService.retrieveReportingPeriodCodeEntityByCode(reportingPeriod.getReportingPeriodTypeCode());
            }
            if (Strings.emptyToNull(reportingPeriod.getFuelUseMaterialCode()) != null) {
                fuelUseMaterialCodeEntity = lookupService.retrieveFuelUseMaterialCodeEntityByCode(reportingPeriod.getFuelUseMaterialCode());
            }
            if (Strings.emptyToNull(reportingPeriod.getFuelUseUom()) != null) {
                fuelUseUomEntity = lookupService.retrieveUnitMeasureCodeEntityByCode(reportingPeriod.getFuelUseUom());
            }
            if (Strings.emptyToNull(reportingPeriod.getHeatContentUom()) != null) {
                heatContentUomEntity = lookupService.retrieveUnitMeasureCodeEntityByCode(reportingPeriod.getHeatContentUom());
            }

            if (Strings.emptyToNull(reportingPeriod.getCalculationMaterialCode()) != null && calculationMaterialCodeEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Reporting Period Throughput", "Material Code", reportingPeriod.getCalculationMaterialCode());
                violations.add(new WorksheetError(reportingPeriod.getSheetName(), reportingPeriod.getRow(), reportingPeriod.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(reportingPeriod.getCalculationParameterTypeCode()) != null && calculationParameterTypeCodeEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Reporting Period Throughput", "Type Code", reportingPeriod.getCalculationParameterTypeCode());
                violations.add(new WorksheetError(reportingPeriod.getSheetName(), reportingPeriod.getRow(), reportingPeriod.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(reportingPeriod.getCalculationParameterUom()) != null && calculationParameterUomEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Reporting Period Throughput", "UoM Code", reportingPeriod.getCalculationParameterUom());
                violations.add(new WorksheetError(reportingPeriod.getSheetName(), reportingPeriod.getRow(), reportingPeriod.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(reportingPeriod.getEmissionsOperatingTypeCode()) != null && emissionsOperatingTypeCodeEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Reporting Period", "Operating Type Code", reportingPeriod.getEmissionsOperatingTypeCode());
                violations.add(new WorksheetError(reportingPeriod.getSheetName(), reportingPeriod.getRow(), reportingPeriod.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(reportingPeriod.getReportingPeriodTypeCode()) != null && reportingPeriodCodeEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Reporting Period", "Type Code", reportingPeriod.getEmissionsOperatingTypeCode());
                violations.add(new WorksheetError(reportingPeriod.getSheetName(), reportingPeriod.getRow(), reportingPeriod.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(reportingPeriod.getFuelUseMaterialCode()) != null && fuelUseMaterialCodeEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Reporting Period", "Fuel Use Material Code", reportingPeriod.getFuelUseMaterialCode());
                violations.add(new WorksheetError(reportingPeriod.getSheetName(), reportingPeriod.getRow(), reportingPeriod.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(reportingPeriod.getFuelUseUom()) != null && fuelUseUomEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Reporting Period Fuel Use", "UoM Code", reportingPeriod.getFuelUseUom());
                violations.add(new WorksheetError(reportingPeriod.getSheetName(), reportingPeriod.getRow(), reportingPeriod.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(reportingPeriod.getHeatContentUom()) != null && heatContentUomEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Reporting Period Heat Content", "UoM Code", reportingPeriod.getHeatContentUom());
                violations.add(new WorksheetError(reportingPeriod.getSheetName(), reportingPeriod.getRow(), reportingPeriod.getErrorIdentifier(), msg));
            }
        }
    }

    private class EmissionJSONValidator implements Consumer<EmissionBulkUploadDto> {

        private final EmissionsReportBulkUploadDto report;
        private final List<WorksheetError> violations;

        public EmissionJSONValidator(EmissionsReportBulkUploadDto report, List<WorksheetError> violations) {
            this.report = report;
            this.violations = violations;
        }

        @Override
        public void accept(EmissionBulkUploadDto emission) {

            CalculationMethodCode emissionsCalcMethodCodeEntity = null;
            UnitMeasureCode emissionsUomCodeEntity = null;
            UnitMeasureCode emissionsNumeratorUomEntity = null;
            UnitMeasureCode emissionsDenominatorUomEntity = null;
            Pollutant emissionsPollutantEntity = null;

            if (Strings.emptyToNull(emission.getEmissionsCalcMethodCode()) != null) {
                emissionsCalcMethodCodeEntity = lookupService.retrieveCalcMethodCodeEntityByCode(emission.getEmissionsCalcMethodCode());
            }
            if (Strings.emptyToNull(emission.getEmissionsUomCode()) != null) {
                emissionsUomCodeEntity = lookupService.retrieveUnitMeasureCodeEntityByCode(emission.getEmissionsUomCode());
            }
            if (Strings.emptyToNull(emission.getEmissionsNumeratorUom()) != null) {
                emissionsNumeratorUomEntity = lookupService.retrieveUnitMeasureCodeEntityByCode(emission.getEmissionsNumeratorUom());
            }
            if (Strings.emptyToNull(emission.getEmissionsDenominatorUom()) != null) {
                emissionsDenominatorUomEntity = lookupService.retrieveUnitMeasureCodeEntityByCode(emission.getEmissionsDenominatorUom());
            }
            if (Strings.emptyToNull(emission.getPollutantCode()) != null) {
                emissionsPollutantEntity = lookupService.retrievePollutantByPollutantCode(emission.getPollutantCode());
                if (emissionsPollutantEntity  != null && "CAP".equals(emissionsPollutantEntity.getPollutantType()) && Boolean.TRUE.equals(emission.getNotReporting())) {
                String msg = String.format("Pollutant '%s' with Type: CAP cannot be marked as 'Not reporting this year'.", emission.getPollutantCode());
                violations.add(new WorksheetError(emission.getSheetName(), emission.getRow(), emission.getErrorIdentifier(), msg));
                }
                // New validation for pollutants not reported in the previous year
                if (emissionsPollutantEntity != null && Boolean.TRUE.equals(emission.getNotReporting()) && !wasReportedLastYear(emission.getPollutantCode(), report.getMasterFacilityRecordId())) {
                    String msg = String.format("Pollutant '%s' was not reported in the previous year's report and cannot be marked as 'Not reporting this year'.", emission.getPollutantCode());
                    violations.add(new WorksheetError(emission.getSheetName(), emission.getRow(), emission.getErrorIdentifier(), msg));
                }

                if (emissionsPollutantEntity != null && emissionsPollutantEntity.getPollutantCode() != null) {
                    String pollutantCode = emissionsPollutantEntity.getPollutantCode();
                    String programCode = report.getProgramSystemCode();
                    boolean invalidMedep = !programCode.equals("MEDEP") && ConstantUtils.POLLUTANT_CODES_MEDEP.contains(pollutantCode);
                    boolean invalidRidem = !programCode.equals("RIDEM") && ConstantUtils.POLLUTANT_CODES_RIDEM.contains(pollutantCode);
                    if (invalidMedep || invalidRidem) {
                        String msg = String.format(
                            "Pollutant '%s' is not a SLT specific pollutant associated with facility, it cannot be included.",
                            emission.getPollutantName()
                        );
                        violations.add(new WorksheetError(emission.getSheetName(), emission.getRow(), emission.getErrorIdentifier(), msg));
                    }
                }
            }

            if (Strings.emptyToNull(emission.getEmissionsCalcMethodCode()) != null && emissionsCalcMethodCodeEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Emissions", "Calculation Method Code", emission.getEmissionsCalcMethodCode());
                violations.add(new WorksheetError(emission.getSheetName(), emission.getRow(), emission.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(emission.getEmissionsUomCode()) != null && emissionsUomCodeEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Emissions", "UoM Code", emission.getEmissionsUomCode());
                violations.add(new WorksheetError(emission.getSheetName(), emission.getRow(), emission.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(emission.getEmissionsNumeratorUom()) != null && emissionsNumeratorUomEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Emissions Numerator", "UoM Code", emission.getEmissionsNumeratorUom());
                violations.add(new WorksheetError(emission.getSheetName(), emission.getRow(), emission.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(emission.getEmissionsDenominatorUom()) != null && emissionsDenominatorUomEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Emissions Denominator", "UoM Code", emission.getEmissionsDenominatorUom());
                violations.add(new WorksheetError(emission.getSheetName(), emission.getRow(), emission.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(emission.getPollutantCode()) != null && emissionsPollutantEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Emissions", "Pollutant Code", emission.getPollutantCode());
                violations.add(new WorksheetError(emission.getSheetName(), emission.getRow(), emission.getErrorIdentifier(), msg));
            }
        }

        private boolean wasReportedLastYear(String pollutantCode, Long facilityId) {
            return lookupService.wasPollutantReportedLastYear(pollutantCode, facilityId);
        }
    }

    private class FormulaVariableJSONValidator implements Consumer<EmissionFormulaVariableBulkUploadDto> {

        private final List<WorksheetError> violations;

        public FormulaVariableJSONValidator(List<WorksheetError> violations) {
            this.violations = violations;
        }

        public void accept(EmissionFormulaVariableBulkUploadDto formulaVariable) {

            EmissionFormulaVariableCode formulaVariableCodeEntity = null;

            if (Strings.emptyToNull(formulaVariable.getEmissionFormulaVariableCode()) != null) {
                formulaVariableCodeEntity = lookupService.retrieveFormulaVariableCodeEntityByCode(formulaVariable.getEmissionFormulaVariableCode());
            }

            if (Strings.emptyToNull(formulaVariable.getEmissionFormulaVariableCode()) != null && formulaVariableCodeEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Emission", "Formula Variable Code", formulaVariable.getEmissionFormulaVariableCode());
                violations.add(new WorksheetError(formulaVariable.getSheetName(), formulaVariable.getRow(), formulaVariable.getErrorIdentifier(), msg));
            }
        }
    }

    static class ReleasePointValidator implements Consumer<ReleasePointBulkUploadDto> {

    	private final List<WorksheetError> violations;

    	public ReleasePointValidator(List<WorksheetError> violations) {
            this.violations = violations;
        }

    	List<String> checkedUnitIdentifierList = new ArrayList<String>();

    	public void accept(ReleasePointBulkUploadDto releasePoint) {

            if (releasePoint.getReleasePointIdentifier() != null && !checkedUnitIdentifierList.contains(releasePoint.getReleasePointIdentifier().trim().toLowerCase())) {
            	checkedUnitIdentifierList.add(releasePoint.getReleasePointIdentifier().trim().toLowerCase());
            } else {
            	String msg = String.format("Release Point ID '%s' already exists within the facility. Duplicates are not allowed.", releasePoint.getReleasePointIdentifier());
                violations.add(new WorksheetError(releasePoint.getSheetName(), releasePoint.getRow(), releasePoint.getErrorIdentifier(), msg));
            }

            if (Strings.emptyToNull(releasePoint.getTypeCode()) != null) {

	            // check to make sure Fugitives don't have Stack info and Stacks don't have Fugitive info
            	if (ConstantUtils.FUGITIVE_RELEASE_POINT_TYPES.contains(releasePoint.getTypeCode())
	                    && (Strings.emptyToNull(releasePoint.getStackDiameter()) != null
	                    || Strings.emptyToNull(releasePoint.getStackDiameterUomCode()) != null
	                    || Strings.emptyToNull(releasePoint.getStackHeight()) != null
	                    || Strings.emptyToNull(releasePoint.getStackHeightUomCode()) != null
	                    || Strings.emptyToNull(releasePoint.getStackLength()) != null
	                    || Strings.emptyToNull(releasePoint.getStackLengthUomCode()) != null
	                    || Strings.emptyToNull(releasePoint.getStackWidth()) != null
	                    || Strings.emptyToNull(releasePoint.getStackWidthUomCode()) != null)) {

	                String msg = String.format("The Release Point contains data for both fugitive and stack release point types. Only data for one release point type should be entered.");
	                violations.add(new WorksheetError(releasePoint.getSheetName(), releasePoint.getRow(), releasePoint.getErrorIdentifier(), msg));

            	} else if (!ConstantUtils.FUGITIVE_RELEASE_POINT_TYPES.contains(releasePoint.getTypeCode())
	                    && (Strings.emptyToNull(releasePoint.getFugitiveAngle()) != null
	                    || Strings.emptyToNull(releasePoint.getFugitiveHeight()) != null
	                    || Strings.emptyToNull(releasePoint.getFugitiveHeightUomCode()) != null
	                    || Strings.emptyToNull(releasePoint.getFugitiveLength()) != null
	                    || Strings.emptyToNull(releasePoint.getFugitiveLengthUomCode()) != null
	                    || Strings.emptyToNull(releasePoint.getFugitiveLine2Latitude()) != null
	                    || Strings.emptyToNull(releasePoint.getFugitiveLine2Longitude()) != null
	                    || Strings.emptyToNull(releasePoint.getFugitiveWidth()) != null
	                    || Strings.emptyToNull(releasePoint.getFugitiveWidthUomCode()) != null)) {

	                String msg = String.format("The Release Point contains data for both fugitive and stack release point types. Only data for one release point type should be entered.");
	                violations.add(new WorksheetError(releasePoint.getSheetName(), releasePoint.getRow(), releasePoint.getErrorIdentifier(), msg));
	            }

            	if (!ConstantUtils.FUGITIVE_RELEASE_PT_2D_TYPE.equals(releasePoint.getTypeCode())
                		&& (Strings.emptyToNull(releasePoint.getFugitiveLine2Latitude()) != null
                        || Strings.emptyToNull(releasePoint.getFugitiveLine2Longitude()) != null)) {

                	String msg = String.format("The Release Point contains data for Mid Point 2 Latitude and/or Mid Point 2 Longitude. Midpoint 2 coordinates are only valid for Fugitive 2-D release point types.");
                    violations.add(new WorksheetError(releasePoint.getSheetName(), releasePoint.getRow(), releasePoint.getErrorIdentifier(), msg));
                }

            	if ((ConstantUtils.FUGITIVE_RELEASE_PT_2D_TYPE.equals(releasePoint.getTypeCode())
            			|| ConstantUtils.FUGITIVE_RELEASE_PT_3D_TYPE.equals(releasePoint.getTypeCode()))
                		&& (Strings.emptyToNull(releasePoint.getFugitiveLength()) != null
        				|| Strings.emptyToNull(releasePoint.getFugitiveLengthUomCode()) != null)) {

                	String msg = String.format("The Release Point contains data for Fugitive Length. Fugitive Length is only valid for Fugitive Area release point types.");
                    violations.add(new WorksheetError(releasePoint.getSheetName(), releasePoint.getRow(), releasePoint.getErrorIdentifier(), msg));
                }
            }
    	}
    }

    private class ReleasePointJSONValidator implements Consumer<ReleasePointBulkUploadDto> {

        private final List<WorksheetError> violations;

        public ReleasePointJSONValidator(List<WorksheetError> violations) {
            this.violations = violations;
        }

        public void accept(ReleasePointBulkUploadDto releasePoint) {

            OperatingStatusCode opStatusCode = null;
            ReleasePointTypeCode rpTypeCode = null;
            UnitMeasureCode stackHeightUomCode = null;
            UnitMeasureCode stackWidthUomCode = null;
            UnitMeasureCode stackLengthUomCode = null;
            UnitMeasureCode stackDiameterUomCode = null;
            UnitMeasureCode exitGasFlowUomCode = null;
            UnitMeasureCode exitGasVelocityUomCode = null;
            UnitMeasureCode fenceLineUomCode = null;
            UnitMeasureCode fugitiveHeightUomCode = null;
            UnitMeasureCode fugitiveLengthUomCode = null;
            UnitMeasureCode fugitiveWidthUomCode = null;

            if (Strings.emptyToNull(releasePoint.getOperatingStatusCode()) != null) {
                opStatusCode = lookupService.retrieveOperatingStatusCodeEntityByCode(releasePoint.getOperatingStatusCode());
            }
            if (Strings.emptyToNull(releasePoint.getTypeCode()) != null) {
                rpTypeCode = lookupService.retrieveReleasePointTypeCodeEntityByCode(releasePoint.getTypeCode());
            }
            if (Strings.emptyToNull(releasePoint.getStackHeightUomCode()) != null) {
                stackHeightUomCode = lookupService.retrieveUnitMeasureCodeEntityByCode(releasePoint.getStackHeightUomCode());
            }
            if (Strings.emptyToNull(releasePoint.getStackWidthUomCode()) != null) {
                stackWidthUomCode = lookupService.retrieveUnitMeasureCodeEntityByCode(releasePoint.getStackWidthUomCode());
            }
            if (Strings.emptyToNull(releasePoint.getStackLengthUomCode()) != null) {
                stackLengthUomCode = lookupService.retrieveUnitMeasureCodeEntityByCode(releasePoint.getStackLengthUomCode());
            }
            if (Strings.emptyToNull(releasePoint.getStackDiameterUomCode()) != null) {
                stackDiameterUomCode = lookupService.retrieveUnitMeasureCodeEntityByCode(releasePoint.getStackDiameterUomCode());;
            }
            if (Strings.emptyToNull(releasePoint.getExitGasFlowUomCode()) != null) {
                exitGasFlowUomCode = lookupService.retrieveUnitMeasureCodeEntityByCode(releasePoint.getExitGasFlowUomCode());
            }
            if (Strings.emptyToNull(releasePoint.getExitGasVelocityUomCode()) != null) {
                exitGasVelocityUomCode = lookupService.retrieveUnitMeasureCodeEntityByCode(releasePoint.getExitGasVelocityUomCode());
            }
            if (Strings.emptyToNull(releasePoint.getFenceLineUomCode()) != null) {
                fenceLineUomCode = lookupService.retrieveUnitMeasureCodeEntityByCode(releasePoint.getFenceLineUomCode());
            }
            if (Strings.emptyToNull(releasePoint.getFugitiveHeightUomCode()) != null) {
                fugitiveHeightUomCode = lookupService.retrieveUnitMeasureCodeEntityByCode(releasePoint.getFugitiveHeightUomCode());
            }
            if (Strings.emptyToNull(releasePoint.getFugitiveLengthUomCode()) != null) {
                fugitiveLengthUomCode = lookupService.retrieveUnitMeasureCodeEntityByCode(releasePoint.getFugitiveLengthUomCode());
            }
            if (Strings.emptyToNull(releasePoint.getFugitiveWidthUomCode()) != null) {
                fugitiveWidthUomCode = lookupService.retrieveUnitMeasureCodeEntityByCode(releasePoint.getFugitiveWidthUomCode());
            }

            if (Strings.emptyToNull(releasePoint.getOperatingStatusCode()) != null && opStatusCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Release Point", "Operating Status Code", releasePoint.getOperatingStatusCode());
                violations.add(new WorksheetError(releasePoint.getSheetName(), releasePoint.getRow(), releasePoint.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(releasePoint.getTypeCode()) != null && rpTypeCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Release Point", "Type Code", releasePoint.getTypeCode());
                violations.add(new WorksheetError(releasePoint.getSheetName(), releasePoint.getRow(), releasePoint.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(releasePoint.getStackHeightUomCode()) != null && stackHeightUomCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Stack Height", "UoM Code", releasePoint.getStackHeightUomCode());
                violations.add(new WorksheetError(releasePoint.getSheetName(), releasePoint.getRow(), releasePoint.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(releasePoint.getStackWidthUomCode()) != null && stackWidthUomCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Stack Width", "UoM Code", releasePoint.getStackWidthUomCode());
                violations.add(new WorksheetError(releasePoint.getSheetName(), releasePoint.getRow(), releasePoint.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(releasePoint.getStackLengthUomCode()) != null && stackLengthUomCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Stack Length", "UoM Code", releasePoint.getStackLengthUomCode());
                violations.add(new WorksheetError(releasePoint.getSheetName(), releasePoint.getRow(), releasePoint.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(releasePoint.getStackDiameterUomCode()) != null && stackDiameterUomCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Stack Diameter", "UoM Code", releasePoint.getStackDiameterUomCode());
                violations.add(new WorksheetError(releasePoint.getSheetName(), releasePoint.getRow(), releasePoint.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(releasePoint.getExitGasFlowUomCode()) != null && exitGasFlowUomCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Exit Gas Flow", "UoM Code", releasePoint.getExitGasFlowUomCode());
                violations.add(new WorksheetError(releasePoint.getSheetName(), releasePoint.getRow(), releasePoint.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(releasePoint.getExitGasVelocityUomCode()) != null && exitGasVelocityUomCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Exit Gas Velocity", "UoM Code", releasePoint.getExitGasVelocityUomCode());
                violations.add(new WorksheetError(releasePoint.getSheetName(), releasePoint.getRow(), releasePoint.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(releasePoint.getFenceLineUomCode()) != null && fenceLineUomCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Fence Line", "UoM Code", releasePoint.getFenceLineUomCode());
                violations.add(new WorksheetError(releasePoint.getSheetName(), releasePoint.getRow(), releasePoint.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(releasePoint.getFugitiveHeightUomCode()) != null && fugitiveHeightUomCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Fugitive Height", "UoM Code", releasePoint.getFugitiveHeightUomCode());
                violations.add(new WorksheetError(releasePoint.getSheetName(), releasePoint.getRow(), releasePoint.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(releasePoint.getFugitiveLengthUomCode()) != null && fugitiveLengthUomCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Fugitive Length", "UoM Code", releasePoint.getFugitiveLengthUomCode());
                violations.add(new WorksheetError(releasePoint.getSheetName(), releasePoint.getRow(), releasePoint.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(releasePoint.getFugitiveWidthUomCode()) != null && fugitiveWidthUomCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Fugitive Width", "UoM Code", releasePoint.getFugitiveWidthUomCode());
                violations.add(new WorksheetError(releasePoint.getSheetName(), releasePoint.getRow(), releasePoint.getErrorIdentifier(), msg));
            }
        }
    }

    static class ReleasePointApptAndReleasePointJSONValidator implements BiConsumer<ReleasePointApptBulkUploadDto, List<ReleasePointBulkUploadDto>> {

        private final List<WorksheetError> violations;

        public ReleasePointApptAndReleasePointJSONValidator(List<WorksheetError> violations) {

            this.violations = violations;
        }

        public void accept(ReleasePointApptBulkUploadDto releasePointAppt, List<ReleasePointBulkUploadDto> releasePoints) {

            boolean releasePointNameMatchFound = false;

            if (Strings.emptyToNull(releasePointAppt.getReleasePointName()) != null && !releasePoints.isEmpty()) {
                for (ReleasePointBulkUploadDto releasePoint : releasePoints) {
                    if (releasePointAppt.getReleasePointName().contentEquals(releasePoint.getReleasePointIdentifier())) {
                        releasePointNameMatchFound = true;
                        break;
                    }
                }
            }

            if (Strings.emptyToNull(releasePointAppt.getReleasePointName()) != null && releasePointNameMatchFound == false) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Release Point Apportionment", "Release Point ID", releasePointAppt.getPathName());
                violations.add(new WorksheetError(releasePointAppt.getSheetName(), releasePointAppt.getRow(), releasePointAppt.getErrorIdentifier(), msg));
            }
        }
    }

    static class ReleasePointApptAndEmissionProcessJSONValidator implements BiConsumer<ReleasePointApptBulkUploadDto, List<EmissionsProcessBulkUploadDto>> {

        private final List<WorksheetError> violations;

        public ReleasePointApptAndEmissionProcessJSONValidator(List<WorksheetError> violations) {

            this.violations = violations;
        }

        public void accept(ReleasePointApptBulkUploadDto releasePointAppt, List<EmissionsProcessBulkUploadDto> emissionsProcesses) {

            boolean identifierMatchFound = false;

            if (Strings.emptyToNull(releasePointAppt.getEmissionProcessName()) != null && !emissionsProcesses.isEmpty()) {
                for (EmissionsProcessBulkUploadDto process : emissionsProcesses) {
                    if (releasePointAppt.getEmissionProcessName().contentEquals(process.getEmissionsProcessIdentifier())) {
                        identifierMatchFound = true;
                        break;
                    }
                }
            }

            if (Strings.emptyToNull(releasePointAppt.getEmissionProcessName()) != null && identifierMatchFound == false) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Release Point Apportionment", "Emissions Process ID", releasePointAppt.getEmissionProcessName());
                violations.add(new WorksheetError(releasePointAppt.getSheetName(), releasePointAppt.getRow(), releasePointAppt.getErrorIdentifier(), msg));
            }
        }
    }

    static class ReleasePointApptAndControlPathJSONValidator implements BiConsumer<ReleasePointApptBulkUploadDto, List<ControlPathBulkUploadDto>> {

        private final List<WorksheetError> violations;

        public ReleasePointApptAndControlPathJSONValidator(List<WorksheetError> violations) {

            this.violations = violations;
        }

        public void accept(ReleasePointApptBulkUploadDto releasePointAppt, List<ControlPathBulkUploadDto> controlPaths) {

            boolean identifierMatchFound = false;

            if (Strings.emptyToNull(releasePointAppt.getPathName()) != null && !controlPaths.isEmpty()) {
                for (ControlPathBulkUploadDto controlPath : controlPaths) {
                    if (releasePointAppt.getPathName().contentEquals(controlPath.getPathId())) {
                        identifierMatchFound = true;
                        break;
                    }
                }
            }

            if (Strings.emptyToNull(releasePointAppt.getPathName()) != null && identifierMatchFound == false) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Release Point Apportionment", "Control Path ID", releasePointAppt.getPathName());
                violations.add(new WorksheetError(releasePointAppt.getSheetName(), releasePointAppt.getRow(), releasePointAppt.getErrorIdentifier(), msg));
            }
        }
    }

    static class ControlValidator implements Consumer<ControlBulkUploadDto> {

    	private final List<WorksheetError> violations;

    	public ControlValidator(List<WorksheetError> violations) {
            this.violations = violations;
        }

    	List<String> checkedControlIdentifierList = new ArrayList<String>();

    	public void accept(ControlBulkUploadDto control) {

            if (control.getIdentifier() != null && !checkedControlIdentifierList.contains(control.getIdentifier().trim().toLowerCase())) {
            	checkedControlIdentifierList.add(control.getIdentifier().trim().toLowerCase());
            } else {
            	String msg = String.format("Control ID '%s' already exists within the facility. Duplicates are not allowed.", control.getIdentifier());
                violations.add(new WorksheetError(control.getSheetName(), control.getRow(), control.getErrorIdentifier(), msg));
            }
    	}
    }

    static class ControlPollutantValidator implements Consumer<ControlPollutantBulkUploadDto> {

        private final List<WorksheetError> violations;

        public ControlPollutantValidator(List<WorksheetError> violations) {
            this.violations = violations;
        }

        Set<String> checkedControlPollutantSet = new HashSet<>();

        public void accept(ControlPollutantBulkUploadDto controlPollutant) {
            Pollutant pollutantEntity = null;
            if (controlPollutant.getControlId() != null) {
                String entryKey = controlPollutant.getControlId() + ", " + controlPollutant.getPollutantCode();
                if (!checkedControlPollutantSet.contains(entryKey)) {
                    checkedControlPollutantSet.add(entryKey);
                } else {
                    String msg = String.format("Control %s already contains Control Pollutant %s, duplicates are not allowed.",  controlPollutant.getControlId(), controlPollutant.getPollutantCode());
                    violations.add(new WorksheetError(controlPollutant.getSheetName(), controlPollutant.getRow(), controlPollutant.getErrorIdentifier(), msg));
                }
            }
        }
    }

    private class ControlJSONValidator implements Consumer<ControlBulkUploadDto> {

        private final List<WorksheetError> violations;

        public ControlJSONValidator(List<WorksheetError> violations) {
            this.violations = violations;
        }

        public void accept(ControlBulkUploadDto control) {

            OperatingStatusCode opStatusCode = null;
            ControlMeasureCode controlMeasureCode = null;

            if (Strings.emptyToNull(control.getOperatingStatusCode()) != null) {
                opStatusCode = lookupService.retrieveSubFacilityOperatingStatusCodeEntityByCode(control.getOperatingStatusCode());
            }
            if (Strings.emptyToNull(control.getControlMeasureCode()) != null) {
                controlMeasureCode = lookupService.retrieveControlMeasureCodeEntityByCode(control.getControlMeasureCode());
            }

            if (Strings.emptyToNull(control.getOperatingStatusCode()) != null && opStatusCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Control Device", "Operating Status Code", control.getOperatingStatusCode());
                violations.add(new WorksheetError(control.getSheetName(), control.getRow(), control.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(control.getControlMeasureCode()) != null && controlMeasureCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Control Device", "Control Measure Code", control.getControlMeasureCode());
                violations.add(new WorksheetError(control.getSheetName(), control.getRow(), control.getErrorIdentifier(), msg));
            }
        }
    }

    private class ControlPollutantJSONValidator implements Consumer<ControlPollutantBulkUploadDto> {

        private final List<WorksheetError> violations;

        Set<String> checkedControlPollutantSet = new HashSet<>();

        public ControlPollutantJSONValidator(List<WorksheetError> violations) {
            this.violations = violations;
        }

        public void accept(ControlPollutantBulkUploadDto controlPollutant) {

            Pollutant pollutantEntity = null;

            if (Strings.emptyToNull(controlPollutant.getPollutantCode()) != null) {
                pollutantEntity = lookupService.retrievePollutantByPollutantCode(controlPollutant.getPollutantCode());
            }

            if (Strings.emptyToNull(controlPollutant.getPollutantCode()) != null && pollutantEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Control Device", "Pollutant Code", controlPollutant.getPollutantCode());
                violations.add(new WorksheetError(controlPollutant.getSheetName(), controlPollutant.getRow(), controlPollutant.getErrorIdentifier(), msg));
            }

            if (controlPollutant.getControlId() != null ) {
                String entryKey = controlPollutant.getControlId() + ", " + controlPollutant.getPollutantCode();
                if (!checkedControlPollutantSet.contains(entryKey)) {
                    checkedControlPollutantSet.add(entryKey);
                } else {
                    String msg = String.format("Control %s already contains Control Pollutant %s, duplicates are not allowed.", controlPollutant.getControlId(), controlPollutant.getPollutantCode());
                    violations.add(new WorksheetError(controlPollutant.getSheetName(), controlPollutant.getRow(), controlPollutant.getErrorIdentifier(), msg));
                }
            }
        }
    }

    static class ControlPathValidator implements Consumer<ControlPathBulkUploadDto> {

    	private final List<WorksheetError> violations;

    	public ControlPathValidator(List<WorksheetError> violations) {
            this.violations = violations;
        }

    	List<String> checkedControlPathIdentifierList = new ArrayList<String>();

    	public void accept(ControlPathBulkUploadDto controlPath) {

            if (controlPath.getPathId() != null && !checkedControlPathIdentifierList.contains(controlPath.getPathId().trim().toLowerCase())) {
            	checkedControlPathIdentifierList.add(controlPath.getPathId().trim().toLowerCase());
            } else {
            	String msg = String.format("Path ID '%s' already exists within the facility. Duplicates are not allowed.", controlPath.getPathId());
                violations.add(new WorksheetError(controlPath.getSheetName(), controlPath.getRow(), controlPath.getErrorIdentifier(), msg));
            }
    	}
    }

    static class ControlPathPollutantValidator implements Consumer<ControlPathPollutantBulkUploadDto> {
        private final List<WorksheetError> violations;
        public ControlPathPollutantValidator(List<WorksheetError> violations) {
            this.violations = violations;
        }
        Set<String> checkedControlPathPollutantSet = new HashSet<>();
        public void accept(ControlPathPollutantBulkUploadDto controlPathPollutant) {
            Pollutant pollutantEntity = null;
            if (controlPathPollutant.getControlPathId() != null) {
                String entryKey = controlPathPollutant.getControlPathId() + ", " + controlPathPollutant.getPollutantCode();
                if (!checkedControlPathPollutantSet.contains(entryKey)) {
                    checkedControlPathPollutantSet.add(entryKey);
                } else {
                    String msg = String.format("Control Path %s already contains Control Path Pollutant %s, duplicates are not allowed.", controlPathPollutant.getControlPathId(), controlPathPollutant.getPollutantCode());
                    violations.add(new WorksheetError(controlPathPollutant.getSheetName(), controlPathPollutant.getRow(), controlPathPollutant.getErrorIdentifier(), msg));
                }
            }
        }
    }

    private class ControlPathPollutantJSONValidator implements Consumer<ControlPathPollutantBulkUploadDto> {

        private final List<WorksheetError> violations;

        public ControlPathPollutantJSONValidator(List<WorksheetError> violations) {
            this.violations = violations;
        }

        Set<String> checkedControlPathPollutantSet = new HashSet<>();

        public void accept(ControlPathPollutantBulkUploadDto controlPathPollutant) {

            Pollutant pollutantEntity = null;

            if (Strings.emptyToNull(controlPathPollutant.getPollutantCode()) != null) {
                pollutantEntity = lookupService.retrievePollutantByPollutantCode(controlPathPollutant.getPollutantCode());
            }

            if (Strings.emptyToNull(controlPathPollutant.getPollutantCode()) != null && pollutantEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Control Path", "Pollutant Code", controlPathPollutant.getPollutantCode());
                violations.add(new WorksheetError(controlPathPollutant.getSheetName(), controlPathPollutant.getRow(), controlPathPollutant.getErrorIdentifier(), msg));
            }

            if (controlPathPollutant.getControlPathId() != null ) {
                String entryKey = controlPathPollutant.getControlPathId() + ", " + controlPathPollutant.getPollutantCode();
                if (!checkedControlPathPollutantSet.contains(entryKey)) {
                    checkedControlPathPollutantSet.add(entryKey);
                } else {
                    String msg = String.format("Control Path %s already contains Control Path Pollutant %s, duplicates are not allowed.", controlPathPollutant.getControlPathId(), controlPathPollutant.getPollutantCode());
                    violations.add(new WorksheetError(controlPathPollutant.getSheetName(), controlPathPollutant.getRow(), controlPathPollutant.getErrorIdentifier(), msg));
                }
            }
        }
    }

    static class ControlAssignmentLoopValidator implements Consumer<List <ControlAssignmentBulkUploadDto>> {

    	private final EmissionsReportBulkUploadDto report;

    	private final List<WorksheetError> violations;

        public ControlAssignmentLoopValidator(EmissionsReportBulkUploadDto report, List<WorksheetError> violations) {

            this.violations = violations;
            this.report = report;
        }

        @Override
        public void accept(List<ControlAssignmentBulkUploadDto> controlAssignments) {

        	List<String> parentPaths = buildParentPaths(controlAssignments);
        	List<String> caList = new ArrayList<String>();
            Set<String> assignmentTree = new HashSet<String>();
            List<String> childPathsList = new ArrayList<String>();
            List<String> checkedParentPaths = new ArrayList<String>();

        	controlAssignments.forEach(ca ->{
        		if(ca.getControlPathChildId() != null){
            		caList.add(ca.getControlPathId()+"/"+ca.getControlPathChildId());
        		}
        	});

        	if(!parentPaths.isEmpty()){
        		for(String parent: parentPaths){
        			childPathsList.clear();
        			assignmentTree.clear();
        			checkedParentPaths.clear();
	        		if(parent != null){
	        			assignmentTree.add(parent);
	        			buildChildPaths(parent, caList, childPathsList, checkedParentPaths);
	        		}
	        		checkForLoops(parent, childPathsList, assignmentTree, caList, violations, report.getControlPaths());
        		}
        	}
        }
    }


    static List <String> buildParentPaths(List<ControlAssignmentBulkUploadDto> assignments){
    	List<String> parentPaths = new ArrayList<String>();
    	assignments.forEach(ca ->{
    		if(ca.getControlPathId() != null && !parentPaths.contains(ca.getControlPathId().toString())){
    			parentPaths.add(ca.getControlPathId().toString());
    		}
    	});
    	return parentPaths;
    }


    public static void buildChildPaths(String parentPath, List<String> assignments, List<String> childPathsList, List<String> checkedParentPaths){
    	List<String> childPaths = new ArrayList<String>();
    	for(String ca: assignments){
    		if(ca.contains(parentPath+"/") && !checkedParentPaths.contains(ca)){
    			childPaths.add(ca.substring(parentPath.length()+1));
    			childPathsList.add(ca.substring(parentPath.length()+1));
    			checkedParentPaths.add(ca);
    		}
    	}

    	if(!childPaths.isEmpty()){
    		for(String cp: childPaths){
    			buildChildPaths(cp, assignments, childPathsList, checkedParentPaths);
    		}
    	}
    }

    static boolean checkForLoops(String parentPath, List<String> childPaths, Set<String> assignmentTree, List<String> assignments, List<WorksheetError> violations, List<ControlPathBulkUploadDto> controlPaths){

    	for(String cp: childPaths){
    		boolean added = assignmentTree.add(cp);
    		if(!added){
    		    ControlPathBulkUploadDto childPathDto = null;
    		    ControlPathBulkUploadDto parentPathDto = null;
    			for(ControlPathBulkUploadDto controlPath: controlPaths){
    				if(controlPath.getId().toString().contentEquals(cp)){
    					childPathDto = controlPath;
    				}
    				if(controlPath.getId().toString().contentEquals(parentPath)){
    					parentPathDto = controlPath;
    				}
    			}
    			String msg = String.format("Control Path '%s' is associated more than once with a control path in rows %s. "
    			                         + "A control path may be associated only once with another control path.",
                        parentPathDto.getPathId(), assignmentTree.toString());
    			violations.add(new WorksheetError("Control Assignments", childPathDto.getRow(), childPathDto.getErrorIdentifier(), msg));
    			return true;
    		}
    	}
    	return false;
    }

    static class ControlAssignmentValidator implements Consumer<ControlAssignmentBulkUploadDto> {

        private final List<WorksheetError> violations;

        public ControlAssignmentValidator(List<WorksheetError> violations) {

            this.violations = violations;
        }

        @Override
        public void accept(ControlAssignmentBulkUploadDto controlAssignment) {

            if (controlAssignment.getControlId() != null && controlAssignment.getControlPathChildId() != null) {

                String msg = String.format("A Control Path and a Control Device cannot both be assigned on the same Control Path Assignment row.");

                violations.add(new WorksheetError(controlAssignment.getSheetName(), controlAssignment.getRow(), controlAssignment.getErrorIdentifier(), msg));
            }

            if (controlAssignment.getControlPathId() != null && controlAssignment.getControlPathChildId() == null && controlAssignment.getControlId() == null) {
            	String msg = String.format("Control Path Assignment must contain at least one Control Path or Control Device.");

                violations.add(new WorksheetError(controlAssignment.getSheetName(), controlAssignment.getRow(), controlAssignment.getErrorIdentifier(), msg));
            }
        }
    }


    static class ControlAssignmentAndPathJSONValidator implements BiConsumer<ControlAssignmentBulkUploadDto, List<ControlPathBulkUploadDto>> {

        private final List<WorksheetError> violations;

        public ControlAssignmentAndPathJSONValidator(List<WorksheetError> violations) {

            this.violations = violations;
        }

        @Override
        public void accept(ControlAssignmentBulkUploadDto controlAssignment, List<ControlPathBulkUploadDto> controlPaths) {

            boolean pathNameMatchFound = false;
            boolean childPathNameMatchFound = false;

            boolean isAssignmentIdPresent = controlAssignment.getId() != null
                && Strings.emptyToNull(controlAssignment.getId().toString()) != null;
            boolean isPathNamePresent = Strings.emptyToNull(controlAssignment.getPathName()) != null;
            boolean isChildPathNamePresent = Strings.emptyToNull(controlAssignment.getChildPathName()) != null;

            if (isAssignmentIdPresent && isPathNamePresent && !controlPaths.isEmpty()) {
                for (ControlPathBulkUploadDto path : controlPaths) {
                    if (path.getPathId().contentEquals(controlAssignment.getPathName())) {
                        pathNameMatchFound = true;
                        break;
                    }
                }
            }
            if (isAssignmentIdPresent && isChildPathNamePresent && !controlPaths.isEmpty()) {
                for (ControlPathBulkUploadDto path : controlPaths) {
                    if (path.getPathId().contentEquals(controlAssignment.getChildPathName())) {
                        childPathNameMatchFound = true;
                        break;
                    }
                }
            }

            if (isAssignmentIdPresent && isPathNamePresent && pathNameMatchFound == false) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Control Assignment", "Path ID", controlAssignment.getPathName());
                violations.add(new WorksheetError(controlAssignment.getSheetName(), controlAssignment.getRow(), controlAssignment.getErrorIdentifier(), msg));
            }
            if (isAssignmentIdPresent && isChildPathNamePresent && childPathNameMatchFound == false) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Control Assignment", "Child Path ID", controlAssignment.getChildPathName());
                violations.add(new WorksheetError(controlAssignment.getSheetName(), controlAssignment.getRow(), controlAssignment.getErrorIdentifier(), msg));
            }
        }
    }

    static class ControlAssignmentAndDeviceJSONValidator implements BiConsumer<ControlAssignmentBulkUploadDto, List<ControlBulkUploadDto>> {

        private final List<WorksheetError> violations;

        public ControlAssignmentAndDeviceJSONValidator(List<WorksheetError> violations) {

            this.violations = violations;
        }

        @Override
        public void accept(ControlAssignmentBulkUploadDto controlAssignment, List<ControlBulkUploadDto> controls) {

            boolean controlIdentifierMatchFound = false;

            boolean isAssignmentIdPresent = controlAssignment.getId() != null
                && Strings.emptyToNull(controlAssignment.getId().toString()) != null;
            boolean isControlIdentifierPresent = Strings.emptyToNull(controlAssignment.getControlName()) != null;

            if (isAssignmentIdPresent && isControlIdentifierPresent && !controls.isEmpty()) {
                for (ControlBulkUploadDto control : controls) {
                    if (control.getIdentifier().contentEquals(controlAssignment.getControlName())) {
                        controlIdentifierMatchFound = true;
                        break;
                    }
                }
            }

            if (isAssignmentIdPresent && isControlIdentifierPresent && controlIdentifierMatchFound == false) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Control Assignment", "Device ID", controlAssignment.getControlName());
                violations.add(new WorksheetError(controlAssignment.getSheetName(), controlAssignment.getRow(), controlAssignment.getErrorIdentifier(), msg));
            }
        }
    }

    private class facilitySiteJSONValidator implements Consumer<FacilitySiteBulkUploadDto> {

        private final List<WorksheetError> violations;

        public facilitySiteJSONValidator(List<WorksheetError> violations) {

            this.violations = violations;
        }

        @Override
        public void accept(FacilitySiteBulkUploadDto facilitySite) {

            FacilityCategoryCode facilityCategoryCodeEntity = null;
            OperatingStatusCode operatingStatusCodeEntity = null;
            FipsStateCode stateCodeEntity = null;
            FipsCounty countyCodeEntity = null;
            TribalCode tribalCodeEntity = null;
            FipsStateCode mailingStateCodeEntity = null;

            if (Strings.emptyToNull(facilitySite.getFacilityCategoryCode()) != null) {
               facilityCategoryCodeEntity = lookupService.retrieveFacilityCategoryCodeEntityByCode(facilitySite.getFacilityCategoryCode());
            }
            if (Strings.emptyToNull(facilitySite.getOperatingStatusCode()) != null) {
                operatingStatusCodeEntity = lookupService.retrieveOperatingStatusCodeEntityByCode(facilitySite.getOperatingStatusCode());
            }
            if (Strings.emptyToNull(facilitySite.getStateCode()) != null) {
                stateCodeEntity = lookupService.retrieveStateCodeEntityByUspsCode(facilitySite.getStateCode());
            }
            if (Strings.emptyToNull(facilitySite.getCounty()) != null) {
                countyCodeEntity = lookupService.retrieveCountyEntityByCountyCode(facilitySite.getCountyCode());
            }
            if (Strings.emptyToNull(facilitySite.getTribalCode()) != null) {
                tribalCodeEntity = lookupService.retrieveTribalCodeEntityByCode(facilitySite.getTribalCode());
            }
            if (Strings.emptyToNull(facilitySite.getMailingStateCode()) != null) {
                mailingStateCodeEntity = lookupService.retrieveStateCodeEntityByUspsCode(facilitySite.getMailingStateCode());
            }

            if (Strings.emptyToNull(facilitySite.getFacilityCategoryCode()) != null && facilityCategoryCodeEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Facility Site", "Category Code", facilitySite.getFacilityCategoryCode());
                violations.add(new WorksheetError(facilitySite.getSheetName(), facilitySite.getRow(), facilitySite.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(facilitySite.getOperatingStatusCode()) != null && operatingStatusCodeEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Facility Site", "Operating Status Code", facilitySite.getOperatingStatusCode());
                violations.add(new WorksheetError(facilitySite.getSheetName(), facilitySite.getRow(), facilitySite.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(facilitySite.getStateCode()) != null && stateCodeEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Facility Site", "State Code", facilitySite.getStateCode());
                violations.add(new WorksheetError(facilitySite.getSheetName(), facilitySite.getRow(), facilitySite.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(facilitySite.getCounty()) != null && countyCodeEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Facility Site", "County Code", facilitySite.getCountyCode());
                violations.add(new WorksheetError(facilitySite.getSheetName(), facilitySite.getRow(), facilitySite.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(facilitySite.getStateCode()) != null
                && stateCodeEntity != null
                && Strings.emptyToNull(stateCodeEntity.getCode()) != null
                && Strings.emptyToNull(facilitySite.getCountyCode()) != null)
            {
                FipsCounty fipsCountyFromState = lookupService.retrieveCountyEntityByStateAndCountyCode(stateCodeEntity.getCode(), facilitySite.getCountyCode());
                if (countyCodeEntity != null && fipsCountyFromState == null) {
                    String msg = String.format("The Facility Site County Code '%s' is not associated with the State Code '%s'. Please check the JSON upload reference spreadsheet for allowed values.", facilitySite.getCountyCode(), stateCodeEntity.getCode());
                    violations.add(new WorksheetError(facilitySite.getSheetName(), facilitySite.getRow(), facilitySite.getErrorIdentifier(), msg));
                }
            }
            if (Strings.emptyToNull(facilitySite.getTribalCode()) != null && tribalCodeEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Facility Site", "Tribal Code", facilitySite.getTribalCode());
                violations.add(new WorksheetError(facilitySite.getSheetName(), facilitySite.getRow(), facilitySite.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(facilitySite.getMailingStateCode()) != null && mailingStateCodeEntity == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Facility Site", "Mailing State Code", facilitySite.getMailingStateCode());
                violations.add(new WorksheetError(facilitySite.getSheetName(), facilitySite.getRow(), facilitySite.getErrorIdentifier(), msg));
            }
        }
    }

    static class FacilityIdValidator implements Consumer<FacilitySiteBulkUploadDto> {

        private final EmissionsReportBulkUploadDto report;

        private final List<WorksheetError> violations;

        public FacilityIdValidator(EmissionsReportBulkUploadDto report, List<WorksheetError> violations) {

            this.violations = violations;
            this.report = report;
        }

        @Override
        public void accept(FacilitySiteBulkUploadDto facilitySite) {

            String blank = ":BLANK:";

            if (report.getProgramSystemCode().equals(facilitySite.getProgramSystemCode()) == false) {

                String val = Objects.toString(facilitySite.getProgramSystemCode(), blank);
                String msg = String.format("The Program System Code '%s' indicated on the Facility Information tab does not match the Program System Code '%s' for the facility for which you are attempting to upload a CAERS report.",
                    val, report.getProgramSystemCode());

                violations.add(new WorksheetError(facilitySite.getSheetName(), facilitySite.getRow(), facilitySite.getErrorIdentifier(), msg));
            }

            if (Strings.emptyToNull(report.getEisProgramId()) != null && report.getEisProgramId().equals(facilitySite.getEisProgramId()) == false) {

                String val = MoreObjects.firstNonNull(Strings.emptyToNull(facilitySite.getEisProgramId()), blank);
                String msg = String.format("The EIS Program ID '%s' indicated on the Facility Information tab does not match the EIS Program ID '%s' for the facility for which you are attempting to upload a CAERS report.",
                    val, report.getEisProgramId());

                violations.add(new WorksheetError(facilitySite.getSheetName(), facilitySite.getRow(), facilitySite.getErrorIdentifier(), msg));
            }

            if (report.getAgencyFacilityIdentifier().equals(facilitySite.getAgencyFacilityIdentifier()) == false) {

                String val = MoreObjects.firstNonNull(Strings.emptyToNull(facilitySite.getAgencyFacilityIdentifier()), blank);
                String msg = String.format("The State Program ID '%s' indicated on the Facility Information tab does not match the State Program ID '%s' for the facility for which you are attempting to upload a CAERS report.",
                    val, report.getAgencyFacilityIdentifier());

                violations.add(new WorksheetError(facilitySite.getSheetName(), facilitySite.getRow(), facilitySite.getErrorIdentifier(), msg));
            }
        }
    }

    private class FacilitySiteContactJSONValidator implements Consumer<FacilitySiteContactBulkUploadDto> {

        private final List<WorksheetError> violations;

        public FacilitySiteContactJSONValidator(List<WorksheetError> violations) {
            this.violations = violations;
        }

        public void accept(FacilitySiteContactBulkUploadDto contact) {

            ContactTypeCode contactTypeCode = null;
            FipsStateCode stateCode = null;
            FipsStateCode mailingStateCode = null;
            FipsCounty county = null;

            if (Strings.emptyToNull(contact.getType()) != null) {
                contactTypeCode = lookupService.retrieveContactTypeEntityByCode(contact.getType());
            }
            if (Strings.emptyToNull(contact.getStateCode()) != null) {
                stateCode = lookupService.retrieveStateCodeEntityByUspsCode(contact.getStateCode());
            }
            if (Strings.emptyToNull(contact.getMailingStateCode()) != null) {
                mailingStateCode = lookupService.retrieveStateCodeEntityByUspsCode(contact.getMailingStateCode());
            }
            if (Strings.emptyToNull(contact.getCountyCode()) != null) {
                county = lookupService.retrieveCountyEntityByCountyCode(contact.getCountyCode());
            }

            if (Strings.emptyToNull(contact.getType()) != null && contactTypeCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Facility Contact", "Type Code", contact.getType());
                violations.add(new WorksheetError(contact.getSheetName(), contact.getRow(), contact.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(contact.getStateCode()) != null
                && stateCode != null
                && Strings.emptyToNull(stateCode.getCode()) != null
                && Strings.emptyToNull(contact.getCountyCode()) != null)
            {
                FipsCounty fipsCountyFromState = lookupService.retrieveCountyEntityByStateAndCountyCode(stateCode.getCode(), contact.getCountyCode());
                if (county != null && fipsCountyFromState == null) {
                    String msg = String.format("The Facility Contact County Code '%s' is not associated with the State Code '%s'. Please check the JSON upload reference spreadsheet for allowed values.", contact.getCountyCode(), stateCode.getCode());
                    violations.add(new WorksheetError(contact.getSheetName(), contact.getRow(), contact.getErrorIdentifier(), msg));
                }
            }
            if (Strings.emptyToNull(contact.getStateCode()) != null && stateCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Facility Contact", "State Code", contact.getStateCode());
                violations.add(new WorksheetError(contact.getSheetName(), contact.getRow(), contact.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(contact.getCountyCode()) != null && county == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Facility Contact", "County Code", contact.getCountyCode());
                violations.add(new WorksheetError(contact.getSheetName(), contact.getRow(), contact.getErrorIdentifier(), msg));
            }
            if (Strings.emptyToNull(contact.getMailingStateCode()) != null && mailingStateCode == null) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Facility Contact", "Mailing State Code", contact.getMailingStateCode());
                violations.add(new WorksheetError(contact.getSheetName(), contact.getRow(), contact.getErrorIdentifier(), msg));
            }
        }
    }

    private class FacilityNAICSJSONValidator implements Consumer<FacilityNAICSBulkUploadDto> {

        private final List<WorksheetError> violations;

        public FacilityNAICSJSONValidator(List<WorksheetError> violations) {
            this.violations = violations;
        }

        public void accept(FacilityNAICSBulkUploadDto facilityNAICS) {

            NaicsCode naicsCodeEntity = null;
            boolean canConvertToInteger = true;
            String naicsCodeMsg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Facility NAICS Code", "NAICS Code", facilityNAICS.getCode());

            if (Strings.emptyToNull(facilityNAICS.getCode()) != null) {
                try {
                    naicsCodeEntity = lookupService.retrieveNAICSCodeEntityByCode(Integer.valueOf(facilityNAICS.getCode()));
                } catch (NumberFormatException nfe) {
                    canConvertToInteger = false;
                    violations.add(new WorksheetError(facilityNAICS.getSheetName(), facilityNAICS.getRow(), facilityNAICS.getErrorIdentifier(), naicsCodeMsg));
                }
            }

            if (Strings.emptyToNull(facilityNAICS.getCode()) != null && canConvertToInteger && naicsCodeEntity == null) {
                violations.add(new WorksheetError(facilityNAICS.getSheetName(), facilityNAICS.getRow(), facilityNAICS.getErrorIdentifier(), naicsCodeMsg));
            }
            if (Strings.emptyToNull(facilityNAICS.getNaicsCodeType()) != null && !ConstantUtils.NAICS_CODE_TYPES.contains(facilityNAICS.getNaicsCodeType())) {
                String msg = String.format(INVALID_VALUE_LOOKUP_MESSAGE, "Facility NAICS", "Code Type", facilityNAICS.getNaicsCodeType());
                violations.add(new WorksheetError(facilityNAICS.getSheetName(), facilityNAICS.getRow(), facilityNAICS.getErrorIdentifier(), msg));
            }
        }
    }

    static class WorksheetDtoValidator implements Consumer<BaseWorksheetDto> {

        private final Validator validator;

        private final List<WorksheetError> violations;

        public WorksheetDtoValidator(Validator validator, List<WorksheetError> violations) {

            this.validator = validator;
            this.violations = violations;
        }

        @Override
        public void accept(BaseWorksheetDto dto) {

            this.validator.validate(dto).forEach(violation -> {

                violations.add(new WorksheetError(dto.getSheetName(), dto.getRow(), dto.getErrorIdentifier(), violation.getMessage()));
            });
        }
    }

    private class CapPollutantValidator implements Consumer<EmissionBulkUploadDto> {
        private final EmissionsReportBulkUploadDto report;
        private final List<WorksheetError> violations;

        public CapPollutantValidator(EmissionsReportBulkUploadDto report, List<WorksheetError> violations) {
            this.report = report;
            this.violations = violations;
        }

        @Override
        public void accept(EmissionBulkUploadDto emission) {

            Pollutant emissionsPollutantEntity = null;
            emissionsPollutantEntity = lookupService.retrievePollutantByPollutantCode(emission.getPollutantCode());

            if (Strings.emptyToNull(emission.getPollutantCode()) != null) {
                if (emissionsPollutantEntity  != null && "CAP".equals(emissionsPollutantEntity.getPollutantType()) && Boolean.TRUE.equals(emission.getNotReporting())) {
                    String msg = String.format("Pollutant '%s' with Type: CAP cannot be marked as 'Not reporting this year'.", emission.getPollutantCode());
                    violations.add(new WorksheetError(emission.getSheetName(), emission.getRow(), emission.getErrorIdentifier(), msg));
                }
            }

            if (Strings.emptyToNull(emission.getPollutantCode()) != null) {
                // New validation for pollutants not reported in the previous year
                if (emissionsPollutantEntity != null && Boolean.TRUE.equals(emission.getNotReporting()) && !wasReportedLastYear(emission.getPollutantCode(), report.getMasterFacilityRecordId())) {
                    String msg = String.format("Pollutant '%s' was not reported in the previous year's report and cannot be marked as 'Not reporting this year'.", emission.getPollutantCode());
                    violations.add(new WorksheetError(emission.getSheetName(), emission.getRow(), emission.getErrorIdentifier(), msg));
                }
            }
        }

        private boolean wasReportedLastYear(String pollutantCode, Long masterFacilityId) {
            return lookupService.wasPollutantReportedLastYear(pollutantCode, masterFacilityId);
        }
    }
}
