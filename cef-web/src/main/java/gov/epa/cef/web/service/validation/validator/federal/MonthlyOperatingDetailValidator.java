/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.validation.validator.federal;

import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import gov.epa.cef.web.domain.OperatingDetail;
import gov.epa.cef.web.domain.PointSourceSccCode;
import gov.epa.cef.web.domain.ReportingPeriod;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import gov.epa.cef.web.util.ConstantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.MessageFormat;

@Component
public class MonthlyOperatingDetailValidator extends BaseValidator<OperatingDetail> {

    @Autowired
    private PointSourceSccCodeRepository sccRepo;

    @Override
    public boolean validate(ValidatorContext validatorContext, OperatingDetail detail) {

        boolean result = true;

        CefValidatorContext context = getCefValidatorContext(validatorContext);

        PointSourceSccCode sccCode = sccRepo.findById(detail.getReportingPeriod().getEmissionsProcess().getSccCode()).orElse(null);

        ReportingPeriod rp = detail.getReportingPeriod();
        if (!ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(detail.getReportingPeriod().getEmissionsProcess().getOperatingStatusCode().getCode())
        	&& !ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN.contentEquals(detail.getReportingPeriod().getEmissionsProcess().getOperatingStatusCode().getCode())
            && ConstantUtils.ALL_REPORTING_PERIODS.indexOf(rp.getEmissionsProcess().getInitialMonthlyReportingPeriod()) <= ConstantUtils.ALL_REPORTING_PERIODS.indexOf(rp.getReportingPeriodTypeCode().getShortName())
            && sccCode != null && sccCode.getMonthlyReporting()) {

            if (detail.getAvgDaysPerWeek() == null) {
                result = false;
                context.addFederalError(
                    ValidationField.DETAIL_AVG_DAY_PER_WEEK.value(),
                    "operatingDetail.avgDaysPerWeek.required",
                    createValidationDetails(detail));
            }

            if (detail.getActualHoursPerPeriod() == null) {
                result = false;
                context.addFederalError(
                    ValidationField.DETAIL_ACT_HR_PER_PERIOD.value(),
                    "operatingDetail.hoursPerPeriod.monthly.required",
                    createValidationDetails(detail));
            }

            if (detail.getAvgHoursPerDay() == null) {
                result = false;
                context.addFederalError(
                    ValidationField.DETAIL_AVG_HR_PER_DAY.value(),
                    "operatingDetail.avgHoursPerDay.required",
                    createValidationDetails(detail));
            }

            if (detail.getAvgWeeksPerPeriod() == null) {
                result = false;
                context.addFederalError(
                    ValidationField.DETAIL_AVG_WEEK_PER_PERIOD.value(),
                    "operatingDetail.avgWeeksPerPeriod.monthly.required",
                    createValidationDetails(detail));
            }

            if (detail.getAvgHoursPerDay() != null && detail.getAvgHoursPerDay().compareTo(BigDecimal.ZERO) == 0
                && detail.getAvgDaysPerWeek() != null && detail.getAvgDaysPerWeek().compareTo(BigDecimal.ZERO) == 0
                && detail.getAvgWeeksPerPeriod() != null && detail.getAvgWeeksPerPeriod().compareTo(BigDecimal.ZERO) == 0) {

                if (detail.getActualHoursPerPeriod() != null) {
                    if (detail.getActualHoursPerPeriod().compareTo(BigDecimal.ZERO) > 0) {

                        result = false;
                        context.addFederalWarning(
                            ValidationField.DETAIL_ACT_HR_PER_PERIOD.value(),
                            "operatingDetail.hoursPerPeriod.nonZeroValues",
                            createValidationDetails(detail));
                    }
                    if (detail.getActualHoursPerPeriod().compareTo(BigDecimal.ZERO) == 0
                        && detail.getReportingPeriod().getCalculationParameterValue() != null
                        && detail.getReportingPeriod().getCalculationParameterValue().compareTo(BigDecimal.ZERO) > 0
                        && detail.getReportingPeriod().getFuelUseValue() != null
                        && detail.getReportingPeriod().getFuelUseValue().compareTo(BigDecimal.ZERO) > 0) {

                        result = false;
                        context.addFederalWarning(
                            ValidationField.DETAIL_ACT_HR_PER_PERIOD.value(),
                            "operatingDetail.zeroValues",
                            createValidationDetails(detail));
                    }
                }
            }
        }

        return result;
    }

    private String getEmissionsUnitIdentifier(OperatingDetail source) {
        if (source.getReportingPeriod() != null && source.getReportingPeriod().getEmissionsProcess() != null
                && source.getReportingPeriod().getEmissionsProcess().getEmissionsUnit() != null) {
            return source.getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getUnitIdentifier();
        }
        return null;
    }

    private String getEmissionsProcessIdentifier(OperatingDetail source) {
        if (source.getReportingPeriod() != null && source.getReportingPeriod().getEmissionsProcess() != null) {
            return source.getReportingPeriod().getEmissionsProcess().getEmissionsProcessIdentifier();
        }
        return null;
    }

    private ValidationDetailDto createValidationDetails(OperatingDetail source) {

        String description = MessageFormat.format("Emissions Unit: {0}, Emissions Process: {1}, Month: {2}",
                getEmissionsUnitIdentifier(source),
                getEmissionsProcessIdentifier(source),
                source.getReportingPeriod().getReportingPeriodTypeCode().getShortName());

        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), getEmissionsProcessIdentifier(source), EntityType.OPERATING_DETAIL, description);
        if (source.getReportingPeriod() != null && source.getReportingPeriod().getEmissionsProcess() != null) {

            dto.getParents().add(new ValidationDetailDto(
                    source.getReportingPeriod().getEmissionsProcess().getId(),
                    source.getReportingPeriod().getEmissionsProcess().getEmissionsProcessIdentifier(),
                    EntityType.SEMIANNUAL_OPERATING_DETAIL));
        }
        return dto;
    }
}
