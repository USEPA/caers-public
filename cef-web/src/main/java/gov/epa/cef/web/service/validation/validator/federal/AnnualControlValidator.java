/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.validation.validator.federal;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import gov.epa.cef.web.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import gov.epa.cef.web.service.validation.ValidationRegistry;
import com.google.common.base.Strings;

import gov.epa.cef.web.repository.ControlRepository;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import gov.epa.cef.web.util.ConstantUtils;

@Component
public class AnnualControlValidator extends BaseValidator<Control> {

    @Autowired
    private EmissionsReportRepository reportRepo;

    @Autowired
    private ControlRepository controlRepo;

	private static final String PM10FIL = "PM10-FIL";
    private static final String PM10PRI = "PM10-PRI";
    private static final String PM25FIL = "PM25-FIL";
    private static final String PM25PRI = "PM25-PRI";
    private static final LocalDate MIN_DATE_RANGE = LocalDate.parse("1900-01-01");
    private static final LocalDate MAX_DATE_RANGE = LocalDate.parse("2050-12-31");

    @Override
    public void compose(FluentValidator validator,
                        ValidatorContext validatorContext,
                        Control control) {

        ValidationRegistry registry = getCefValidatorContext(validatorContext).getValidationRegistry();

        validator.onEach(control.getPollutants(),
            registry.findOneByType(AnnualControlPollutantValidator.class));
    }

    @Override
    public boolean validate(ValidatorContext validatorContext, Control control) {

        boolean result = true;

        CefValidatorContext context = getCefValidatorContext(validatorContext);
        EmissionsReport currentReport = control.getFacilitySite().getEmissionsReport();

		Map<Object, List<Control>> cMap = control.getFacilitySite().getControls().stream()
        .filter(controls -> (controls.getIdentifier() != null))
        .collect(Collectors.groupingBy(c -> c.getIdentifier().trim().toLowerCase()));

        if (!ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(control.getOperatingStatusCode().getCode())) {

            for (List<Control> cList: cMap.values()) {
                if (cList.size() > 1 && cList.get(0).getIdentifier().trim().toLowerCase().contentEquals(control.getIdentifier().trim().toLowerCase())) {

                    result = false;
                    context.addFederalError(
                        ValidationField.CONTROL_IDENTIFIER.value(),
                        "control.controlIdentifier.duplicate",
                        createValidationDetails(control));
                }
            }

            if (!control.getAssignments().isEmpty()) {

                // Check if associated units or processes are shutdown
                for (ControlAssignment ca : control.getAssignments()) {
                    List<ReleasePointAppt> rpas = ca.getControlPath().getReleasePointAppts();
                    if (!rpas.isEmpty()) {
                        for (ReleasePointAppt rpa : rpas) {
                            EmissionsProcess ep = rpa.getEmissionsProcess();
                            if (
                                ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(
                                    ep.getEmissionsUnit().getOperatingStatusCode().getCode())
                                    || ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN.contentEquals(
                                    ep.getEmissionsUnit().getOperatingStatusCode().getCode())
                            ) {
                                result = false;
                                context.addFederalWarning(
                                    ValidationField.CONTROL_STATUS_CODE.value(),
                                    "control.statusTypeCode.emissionsUnit.shutdown",
                                    createValidationDetails(control),
                                    ep.getEmissionsUnit().getDescription());
                            }

                            if (
                                ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(
                                    ep.getOperatingStatusCode().getCode())
                                    || ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN.contentEquals(
                                    ep.getOperatingStatusCode().getCode())
                            ) {
                                result = false;
                                context.addFederalWarning(
                                    ValidationField.CONTROL_STATUS_CODE.value(),
                                    "control.statusTypeCode.emissionsProcess.shutdown",
                                    createValidationDetails(control),
                                    ep.getDescription());
                            }
                        }
                    }
                }
            } else {

                result = false;
                context.addFederalWarning(
                    ValidationField.CONTROL_PATH_WARNING.value(),
                    "control.pathWarning.notAssigned",
                    createValidationDetails(control));
            }

            // if control was PS in previous year report and is not PS in this report
            if (
                control.getPreviousYearOperatingStatusCode() != null
                && ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(
                    control.getPreviousYearOperatingStatusCode().getCode())
            ) {

                    result = false;
                    context.addFederalError(
                        ValidationField.CONTROL_STATUS_CODE.value(),
                        "control.statusTypeCode.psPreviousYear",
                        createValidationDetails(control));
            }

            if (control.getControlMeasureCode() != null && control.getControlMeasureCode().getLastInventoryYear() != null
                && control.getControlMeasureCode().getLastInventoryYear() < currentReport.getYear()
            ) {
                result = false;
                if (Strings.emptyToNull(control.getControlMeasureCode().getMapTo()) != null) {

                    context.addFederalError(
                        ValidationField.CONTROL_MEASURE_CODE.value(),
                        "control.controlMeasureCode.legacy.map",
                        createValidationDetails(control),
                        control.getControlMeasureCode().getDescription(),
                        control.getControlMeasureCode().getMapTo());
                } else {

                    context.addFederalError(
                        ValidationField.CONTROL_MEASURE_CODE.value(),
                        "control.controlMeasureCode.legacy",
                        createValidationDetails(control),
                        control.getControlMeasureCode().getDescription());
                }
            }

            // Number Operating Months must be between 1 and 12
            if (control.getNumberOperatingMonths() != null && (control.getNumberOperatingMonths() < 1 || control.getNumberOperatingMonths() > 12)) {
                result = false;
                context.addFederalError(
                    ValidationField.CONTROL_NUMBER_OPERATING_MONTHS.value(),
                    "control.numberOperatingMonths.range",
                    createValidationDetails(control));
            }

            if (control.getPercentControl() != null) {
                // percent control must be > 1 or < 100
                if (control.getPercentControl().compareTo(BigDecimal.ONE) == -1 || control.getPercentControl().compareTo(BigDecimal.valueOf(100)) == 1) {

                    result = false;
                    context.addFederalError(
                        ValidationField.CONTROL_PERCENT_CONTROL.value(),
                        "control.percentControl.range",
                        createValidationDetails(control));
                }

                Pattern pattern = Pattern.compile(ConstantUtils.REGEX_ONE_DECIMAL_PRECISION);
                Matcher matcher = pattern.matcher(control.getPercentControl().stripTrailingZeros().toPlainString());
                if(!matcher.matches()){

                    result = false;
                    context.addFederalError(
                        ValidationField.CONTROL_PERCENT_CONTROL.value(),
                        "control.percentControl.invalidFormat",
                        createValidationDetails(control));
                }
            } else {

                if (!control.getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN)) {

                    result = false;
                    context.addFederalError(
                        ValidationField.CONTROL_PERCENT_CONTROL.value(),
                        "control.percentControl.required",
                        createValidationDetails(control));
                }
            }

            if (control.getStartDate() != null && control.getEndDate() != null && (control.getEndDate().isBefore(control.getStartDate()))) {

                result = false;
                context.addFederalError(
                    ValidationField.CONTROL_DATE.value(),
                    "control.date.startEndInvalid",
                    createValidationDetails(control));
            }

            if (control.getStartDate() != null && (control.getStartDate().isBefore(MIN_DATE_RANGE) || control.getStartDate().isAfter(MAX_DATE_RANGE))) {

                result = false;
                context.addFederalError(
                    ValidationField.CONTROL_DATE.value(),
                    "control.date.range",
                    createValidationDetails(control),
                    "Control Start");
            }

            if (control.getEndDate() != null && (control.getEndDate().isBefore(MIN_DATE_RANGE) || control.getEndDate().isAfter(MAX_DATE_RANGE))) {

                result = false;
                context.addFederalError(
                    ValidationField.CONTROL_DATE.value(),
                    "control.date.range",
                    createValidationDetails(control),
                    "Control End");
            }

            if (control.getUpgradeDate() != null) {

                if ((control.getStartDate() != null && (control.getUpgradeDate().isBefore(control.getStartDate())))
                    || (control.getEndDate() != null && (control.getEndDate().isBefore(control.getUpgradeDate())))) {

                    result = false;
                    context.addFederalError(
                        ValidationField.CONTROL_DATE.value(),
                        "control.date.upgradeDateInvalid",
                        createValidationDetails(control));
                }

                if (control.getUpgradeDate().isBefore(MIN_DATE_RANGE) || control.getUpgradeDate().isAfter(MAX_DATE_RANGE)) {

                    result = false;
                    context.addFederalError(
                        ValidationField.CONTROL_DATE.value(),
                        "control.date.range",
                        createValidationDetails(control),
                        "Control Upgrade");
                }
            }

            if (control.getPollutants().isEmpty()) {

                result = false;
                context.addFederalError(
                    ValidationField.CONTROL_POLLUTANT.value(),
                    "control.controlPollutant.required",
                    createValidationDetails(control));
            } else {

                for (ControlPollutant cp : control.getPollutants()) {

                    // percent reduction must be > 5 or < 99.9
                    if (cp.getPercentReduction().compareTo(BigDecimal.valueOf(5)) == -1 || cp.getPercentReduction().compareTo(BigDecimal.valueOf(99.9)) == 1) {

                        result = false;
                        context.addFederalError(
                            ValidationField.CONTROL_POLLUTANT.value(),
                            "control.controlPollutant.range",
                            createValidationDetails(control),
                            cp.getPollutant().getPollutantName());
                    }
                }

                Map<String, List<ControlPollutant>> cpMap = control.getPollutants().stream()
                    .filter(e -> e.getPollutant() != null)
                    .collect(Collectors.groupingBy(e -> e.getPollutant().getPollutantCode()));

                BigDecimal pm10Fil = cpMap.containsKey(PM10FIL) ? cpMap.get(PM10FIL).get(0).getPercentReduction() : null;
                BigDecimal pm10Pri = cpMap.containsKey(PM10PRI) ? cpMap.get(PM10PRI).get(0).getPercentReduction() : null;
                BigDecimal pm25Fil = cpMap.containsKey(PM25FIL) ? cpMap.get(PM25FIL).get(0).getPercentReduction() : null;
                BigDecimal pm25Pri = cpMap.containsKey(PM25PRI) ? cpMap.get(PM25PRI).get(0).getPercentReduction() : null;

                for (List<ControlPollutant> pList : cpMap.values()) {
                    if (pList.size() > 1) {

                        result = false;
                        context.addFederalError(
                            ValidationField.CONTROL_POLLUTANT.value(),
                            "control.controlPollutant.duplicate",
                            createValidationDetails(control),
                            pList.get(0).getPollutant().getPollutantName());

                    }
                }

                // PM2.5 Filterable should not exceed PM10 Filterable.
                if (pm25Fil != null && pm10Fil != null
                    && pm10Fil.compareTo(pm25Fil) == -1) {

                    result = false;
                    context.addFederalError(
                        ValidationField.CONTROL_POLLUTANT.value(),
                        "control.controlPollutant.pm25.fil.greater.pm10fil",
                        createValidationDetails(control));

                }

                // PM2.5 Primary should not exceed PM10 Primary.
                if (pm25Pri != null && pm10Pri != null
                    && pm10Pri.compareTo(pm25Pri) == -1) {

                    result = false;
                    context.addFederalError(
                        ValidationField.CONTROL_POLLUTANT.value(),
                        "control.controlPollutant.pm25.pri.greater.pm10pri",
                        createValidationDetails(control));

                }
            }
        } else { // Control is PS

            // Warning if control device operation status is permanently shutdown control device will not be copied forward
            result = false;
            context.addFederalWarning(
                ValidationField.CONTROL_STATUS_YEAR.value(), "control.statusYear.psNotCopied",
                createValidationDetails(control));
        }

        if (!ConstantUtils.STATUS_OPERATING.contentEquals(control.getOperatingStatusCode().getCode())) {

            // If control device operation status is not operating, status year is required
            if (control.getStatusYear() == null) {

                result = false;
                context.addFederalError(
                    ValidationField.CONTROL_STATUS_YEAR.value(), "control.statusYear.required",
                    createValidationDetails(control));
            }

            // Check if previous report exists then check if this control exists in that report
            List<EmissionsReport> erList = reportRepo.findByMasterFacilityRecordId(currentReport.getMasterFacilityRecord().getId()).stream()
                    .filter(var -> (var.getYear() != null && var.getYear() < currentReport.getYear()))
                    .sorted(Comparator.comparing(EmissionsReport::getYear))
                    .collect(Collectors.toList());

            boolean pyControlExists = false;

            if (!erList.isEmpty()) {
                Short previousReportYr = erList.get(erList.size()-1).getYear();

                List<Control> previousControls = controlRepo.retrieveByIdentifierFacilityYear(
                        control.getIdentifier(),
                        currentReport.getMasterFacilityRecord().getId(),
                        previousReportYr);

                if (!previousControls.isEmpty()) {
                    pyControlExists = true;
                }
            }

            if (!pyControlExists) {

                // control is new, but is PS/TS
                result = false;
                context.addFederalError(
                        ValidationField.CONTROL_STATUS_CODE.value(),
                        "control.statusTypeCode.newShutdown",
                        createValidationDetails(control));
            }
		}

		// Status year must be between 1900 and the report year
		if (control.getStatusYear() != null && (control.getStatusYear() < 1900 || control.getStatusYear() > currentReport.getYear())) {

	        result = false;
	        context.addFederalError(
	            ValidationField.CONTROL_STATUS_YEAR.value(), "control.statusYear.range",
	            createValidationDetails(control),
                currentReport.getYear().toString());
		}

        return result;
    }

    private ValidationDetailDto createValidationDetails(Control source) {

        String description = MessageFormat.format("Control: {0}", source.getIdentifier());

        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), source.getIdentifier(), EntityType.CONTROL, description);
        return dto;
    }

}
