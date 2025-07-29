/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package gov.epa.cef.web.service.validation.validator.federal;

import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.google.common.base.Strings;
import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.*;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationFeature;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.ValidationRegistry;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import gov.epa.cef.web.util.ConstantUtils;
import gov.epa.cef.web.util.SLTConfigHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SharedEmissionsProcessValidator extends BaseValidator<EmissionsProcess> {

    @Autowired
    private SLTConfigHelper sltConfigHelper;

    @Autowired
    private PointSourceSccCodeRepository sccRepo;

    @Autowired
    private EmissionsReportRepository reportRepo;

    @Autowired
    private EmissionsProcessRepository processRepo;

    @Autowired
    private EmissionRepository emissionRepo;

    @Override
    public void compose(FluentValidator validator,
                        ValidatorContext validatorContext,
                        EmissionsProcess emissionsProcess) {

        ValidationRegistry registry = getCefValidatorContext(validatorContext).getValidationRegistry();

        // shared validator only adds other shared validators for the annual reporting period
        validator.onEach(emissionsProcess.getReportingPeriods().stream().filter(period -> period.getReportingPeriodTypeCode().getShortName().equals(ConstantUtils.ANNUAL)).collect(Collectors.toList()),
            registry.findOneByType(SharedReportingPeriodValidator.class));
    }

    @Override
    public boolean validate(ValidatorContext validatorContext, EmissionsProcess emissionsProcess) {

        boolean result = true;

        CefValidatorContext context = getCefValidatorContext(validatorContext);
        SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(emissionsProcess.getEmissionsUnit().getFacilitySite().getProgramSystemCode().getCode());

        boolean isProcessOperating = ConstantUtils.STATUS_OPERATING.contentEquals(emissionsProcess.getOperatingStatusCode().getCode());

        if (isProcessOperating) {
            // Check for valid SCC Code
            if (Strings.emptyToNull(emissionsProcess.getSccCode()) != null) {

                PointSourceSccCode isPointSourceSccCode = sccRepo.findById(emissionsProcess.getSccCode()).orElse(null);
                short reportYear = emissionsProcess.getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear();

                if (isPointSourceSccCode == null) {

                    result = false;
                    context.addFederalError(
                        ValidationField.PROCESS_INFO_SCC.value(),
                        "emissionsProcess.information.scc.invalid",
                        createValidationDetails(emissionsProcess),
                        emissionsProcess.getSccCode());

                } else if (SccCategory.Point != isPointSourceSccCode.getCategory() && !sltConfig.getNonPointSccEnabled()) {
                    // has a non-point SCC, but property to allow them isn't enabled

                    result = false;
                    context.addFederalError(
                        ValidationField.PROCESS_INFO_SCC.value(),
                        "emissionsProcess.information.scc.notAllowed",
                        createValidationDetails(emissionsProcess),
                        emissionsProcess.getSccCode());

                } else if (isPointSourceSccCode.getLastInventoryYear() != null
                    && isPointSourceSccCode.getLastInventoryYear() < reportYear) {

                    result = false;
                    context.addFederalError(
                        ValidationField.PROCESS_INFO_SCC.value(),
                        "emissionsProcess.information.scc.expired",
                        createValidationDetails(emissionsProcess),
                        emissionsProcess.getSccCode(),
                        isPointSourceSccCode.getLastInventoryYear().toString());

                } else if (isPointSourceSccCode.getLastInventoryYear() != null
                    && isPointSourceSccCode.getLastInventoryYear() >= reportYear) {

                    result = false;
                    context.addFederalWarning(
                        ValidationField.PROCESS_INFO_SCC.value(),
                        "emissionsProcess.information.scc.retired",
                        createValidationDetails(emissionsProcess),
                        emissionsProcess.getSccCode(),
                        isPointSourceSccCode.getLastInventoryYear().toString());
                }
            }

            // Operating Processes must have a Reporting Period
            if (emissionsProcess.getReportingPeriods() == null || emissionsProcess.getReportingPeriods().size() == 0) {
                result = false;
                context.addFederalError(
                    ValidationField.REPORTING_PERIOD.value(),
                    "emissionsProcess.reportingPeriod.required",
                    createValidationDetails(emissionsProcess));
            }

            else {
                // At least one emission is recorded for the Reporting Period when Process Status is "Operating".
                for (ReportingPeriod rp : emissionsProcess.getReportingPeriods().stream().filter(period -> period.getReportingPeriodTypeCode().getShortName().equals(ConstantUtils.ANNUAL)).collect(Collectors.toList())) {
                    if (rp.getEmissions() == null || rp.getEmissions().size() == 0) {

                        result = false;
                        context.addFederalError(
                            ValidationField.PROCESS_PERIOD_EMISSION.value(),
                            "emissionsProcess.emission.required",
                            createValidationDetails(emissionsProcess));
                    }
                }
            }
        }

        // check current year's total emissions against previous year's total emissions warning
        EmissionsReport currentReport = emissionsProcess.getEmissionsUnit().getFacilitySite().getEmissionsReport();

        boolean sourceTypeLandfill = false;

        if (currentReport.getMasterFacilityRecord().getFacilitySourceTypeCode() != null
            && ConstantUtils.FACILITY_SOURCE_LANDFILL_CODE.contentEquals(currentReport.getMasterFacilityRecord().getFacilitySourceTypeCode().getCode())) {
            sourceTypeLandfill = true;
        }

        // find previous report
        List<EmissionsReport> erList = reportRepo.findByMasterFacilityRecordId(currentReport.getMasterFacilityRecord().getId()).stream()
            .filter(var -> (var.getYear() != null && var.getYear() < currentReport.getYear()))
            .sorted(Comparator.comparing(EmissionsReport::getYear))
            .collect(Collectors.toList());

        boolean pyProcessExists = false;

        if (!erList.isEmpty()) {

            Short previousReportYr = erList.get(erList.size()-1).getYear();
            Long previousReportId = erList.get(erList.size()-1).getId();

            for(EmissionsReport er: erList){
                if(!ConstantUtils.STATUS_ONRE.contentEquals(er.getFacilitySites().get(0).getOperatingStatusCode().getCode())){
                    previousReportYr = er.getYear();
                    previousReportId = er.getId();
                }
            }

            List<EmissionsProcess> previousProcesses = processRepo.retrieveByIdentifierParentFacilityYear(emissionsProcess.getEmissionsProcessIdentifier(),
                emissionsProcess.getEmissionsUnit().getUnitIdentifier(),
                currentReport.getMasterFacilityRecord().getId(),
                previousReportYr);

            // check if previous report exists then check if this process exists in that report
            if (!previousProcesses.isEmpty()) {

                pyProcessExists = true;

                // loop through all processes, if the same report was uploaded twice for a previous year this should only work once
                // if the previous report had the same process multiple times this may return duplicate messages
                for (EmissionsProcess previousProcess : previousProcesses) {

                    // check PS/TS status year of current report to OP status year of previous report
                    if (!isProcessOperating && previousProcess.getStatusYear() != null
                        && ConstantUtils.STATUS_OPERATING.contentEquals(previousProcess.getOperatingStatusCode().getCode())
                        && (emissionsProcess.getStatusYear() != null && emissionsProcess.getStatusYear() < previousReportYr)
                        && !sourceTypeLandfill) {
                        result = false;
                        context.addFederalError(
                            ValidationField.PROCESS_STATUS_YEAR.value(),
                            "emissionsProcess.statusYear.invalid",
                            createValidationDetails(emissionsProcess),
                            emissionsProcess.getOperatingStatusCode().getDescription(),
                            emissionsProcess.getStatusYear() != null ? emissionsProcess.getStatusYear().toString() : emissionsProcess.getStatusYear());
                    }

                    if (isProcessOperating && !context.isEnabled(ValidationFeature.SEMIANNUAL)) {
                        List<Emission> currentEmissionsList = emissionRepo.findAllAnnualByProcessIdReportId(emissionsProcess.getId(), currentReport.getId());
                        List<Emission> previousEmissionsList = emissionRepo.findAllAnnualByProcessIdReportId(previousProcess.getId(), previousReportId);

                        if (!currentEmissionsList.isEmpty() && !previousEmissionsList.isEmpty()) {
                            for (Emission ce: currentEmissionsList) {
                                for (Emission pe: previousEmissionsList) {
                                    // check if pollutant code the same and total emissions are equal in value and scale
                                    if ((ce.getPollutant().getPollutantCode().contentEquals(pe.getPollutant().getPollutantCode()))
                                        && ce.getTotalEmissions() != null
                                        && pe.getTotalEmissions() != null
                                        && ce.getTotalEmissions().compareTo(pe.getTotalEmissions()) == 0) {

                                        result = false;
                                        context.addFederalWarning(
                                            ValidationField.EMISSION_TOTAL_EMISSIONS.value(),
                                            "emission.totalEmissions.copied",
                                            createEmissionValidationDetails(ce),
                                            previousReportYr.toString());
                                    }
                                }
                            }
                        }
                    }

                    if ( // QA Warning for changing an SCC for an existing emissions process
                        (Strings.emptyToNull(emissionsProcess.getSccCode()) != null) && // Check for valid SCC Code
                            (previousProcess.getEmissionsProcessIdentifier().equals(
                                emissionsProcess.getEmissionsProcessIdentifier())) &&
                            (Strings.emptyToNull(previousProcess.getSccCode()) != null) && // Check for valid SCC Code
                            (!(previousProcess.getSccCode().equals(emissionsProcess.getSccCode())))
                    )  {
                        result = false;
                        context.addFederalWarning(
                            ValidationField.PROCESS_INFO_SCC.value(),
                            "emissionsProcess.information.scc.changed",
                            createValidationDetails(emissionsProcess));
                    }
                }
            }
        }

        // if emissions process was PS in previous year report and is not PS in this report
        if (emissionsProcess.getPreviousYearOperatingStatusCode() != null) {
            if (ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(emissionsProcess.getPreviousYearOperatingStatusCode().getCode()) &&
                !ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(emissionsProcess.getOperatingStatusCode().getCode())) {

                result = false;
                context.addFederalError(
                    ValidationField.PROCESS_STATUS_CODE.value(),
                    "emissionsProcess.statusTypeCode.psPreviousYear",
                    createValidationDetails(emissionsProcess));
            }
        }

        // If process operation status is not operating, status year is required
        if (!ConstantUtils.STATUS_OPERATING.contentEquals(emissionsProcess.getOperatingStatusCode().getCode()) && emissionsProcess.getStatusYear() == null) {

            result = false;
            context.addFederalError(
                ValidationField.PROCESS_STATUS_YEAR.value(), "emissionsProcess.statusYear.required",
                createValidationDetails(emissionsProcess));
        }

        if (emissionsProcess.getStatusYear() != null) {
            boolean isProcessPS = ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(emissionsProcess.getOperatingStatusCode().getCode());
            boolean isProcessTS = ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN.contentEquals(emissionsProcess.getOperatingStatusCode().getCode());
            boolean isUnitPS = ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(emissionsProcess.getEmissionsUnit().getOperatingStatusCode().getCode());
            boolean isUnitTS = ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN.contentEquals(emissionsProcess.getEmissionsUnit().getOperatingStatusCode().getCode());
            Short processStatusYear = emissionsProcess.getStatusYear();
            Short unitStatusYear = emissionsProcess.getEmissionsUnit().getStatusYear();

            // Status year must be between 1900 and the report year
            if (processStatusYear < 1900 || processStatusYear > emissionsProcess.getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear()) {

                result = false;
                context.addFederalError(
                    ValidationField.PROCESS_STATUS_YEAR.value(), "emissionsProcess.statusYear.range",
                    createValidationDetails(emissionsProcess),
                    emissionsProcess.getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear().toString());
            }

            // process year op status changed cannot be before unit year op status changed when both are OP.
            // check does not apply to landfills, process can be operating while unit is TS/PS.
            if (isProcessOperating && !sourceTypeLandfill && unitStatusYear != null
                && ConstantUtils.STATUS_OPERATING.contentEquals(emissionsProcess.getEmissionsUnit().getOperatingStatusCode().getCode())
                && processStatusYear < unitStatusYear) {

                result = false;
                context.addFederalError(
                    ValidationField.PROCESS_STATUS_YEAR.value(),
                    "emissionsProcess.statusYear.beforeUnitYear",
                    createValidationDetails(emissionsProcess),
                    emissionsProcess.getEmissionsUnit().getUnitIdentifier());
            }

            // process year op status changed cannot be after unit year op status changed when both are either PS or TS.
            if ((unitStatusYear != null) && (!sourceTypeLandfill)
                && ((isUnitPS || isUnitTS) && (isProcessPS || isProcessTS))
                && (unitStatusYear < processStatusYear)) {

                result = false;
                context.addFederalError(
                    ValidationField.PROCESS_STATUS_YEAR.value(),
                    "emissionsProcess.statusYear.afterUnitYear",
                    createValidationDetails(emissionsProcess),
                    emissionsProcess.getEmissionsUnit().getUnitIdentifier());
            }
        }

        if (!pyProcessExists && !isProcessOperating) {

            // process is new, but is PS/TS
            result = false;
            context.addFederalError(
                ValidationField.PROCESS_STATUS_CODE.value(),
                "emissionsProcess.statusTypeCode.newShutdown",
                createValidationDetails(emissionsProcess));
        }

        return result;
    }

    private String getEmissionsUnitIdentifier(EmissionsProcess process) {
        if (process.getEmissionsUnit() != null) {
            return process.getEmissionsUnit().getUnitIdentifier();
        }
        return null;
    }

    private ValidationDetailDto createValidationDetails(EmissionsProcess source) {

        String description = MessageFormat.format("Emissions Unit: {0}, Emissions Process: {1}",
            getEmissionsUnitIdentifier(source),
            source.getEmissionsProcessIdentifier());

        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), source.getEmissionsProcessIdentifier(), EntityType.EMISSIONS_PROCESS, description);
        return dto;
    }

    private String getEmissionsUnitIdentifier(Emission emission) {
        if (emission.getReportingPeriod() != null && emission.getReportingPeriod().getEmissionsProcess() != null
            && emission.getReportingPeriod().getEmissionsProcess().getEmissionsUnit() != null) {
            return emission.getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getUnitIdentifier();
        }
        return null;
    }

    private String getEmissionsProcessIdentifier(Emission emission) {
        if (emission.getReportingPeriod() != null && emission.getReportingPeriod().getEmissionsProcess() != null) {
            return emission.getReportingPeriod().getEmissionsProcess().getEmissionsProcessIdentifier();
        }
        return null;
    }

    private String getPollutantName(Emission emission) {
        if (emission.getPollutant() != null) {
            return emission.getPollutant().getPollutantName();
        }
        return null;
    }

    private ValidationDetailDto createEmissionValidationDetails(Emission source) {

        String description = MessageFormat.format("Emissions Unit: {0}, Emissions Process: {1}, Pollutant: {2}",
            getEmissionsUnitIdentifier(source),
            getEmissionsProcessIdentifier(source),
            getPollutantName(source));

        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), getPollutantName(source), EntityType.EMISSION, description);
        if (source.getReportingPeriod() != null) {
            dto.getParents().add(new ValidationDetailDto(source.getReportingPeriod().getId(), null, EntityType.REPORTING_PERIOD));
        }
        return dto;
    }

}
