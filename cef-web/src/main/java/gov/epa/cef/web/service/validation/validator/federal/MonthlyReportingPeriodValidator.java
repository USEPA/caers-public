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
import gov.epa.cef.web.domain.PointSourceSccCode;
import gov.epa.cef.web.domain.ReportingPeriod;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.ValidationRegistry;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import gov.epa.cef.web.util.ConstantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;

@Component
public class MonthlyReportingPeriodValidator extends BaseValidator<ReportingPeriod> {

    @Autowired
    private PointSourceSccCodeRepository sccRepo;

    @Override
    public void compose(FluentValidator validator,
                        ValidatorContext validatorContext,
                        ReportingPeriod reportingPeriod) {

        ValidationRegistry registry = getCefValidatorContext(validatorContext).getValidationRegistry();

        validator.onEach(new ArrayList<>(reportingPeriod.getOperatingDetails()),
            registry.findOneByType(MonthlyOperatingDetailValidator.class));

        validator.onEach(new ArrayList<>(reportingPeriod.getEmissions()),
            registry.findOneByType(MonthlyEmissionValidator.class));

    }

    @Override
    public boolean validate(ValidatorContext validatorContext, ReportingPeriod period) {

        boolean valid = true;

        CefValidatorContext context = getCefValidatorContext(validatorContext);

        PointSourceSccCode sccCode = sccRepo.findById(period.getEmissionsProcess().getSccCode()).orElse(null);

        if (!ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(period.getEmissionsProcess().getOperatingStatusCode().getCode())
        	&& !ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN.contentEquals(period.getEmissionsProcess().getOperatingStatusCode().getCode())
            && ConstantUtils.ALL_REPORTING_PERIODS.indexOf(period.getEmissionsProcess().getInitialMonthlyReportingPeriod()) <= ConstantUtils.ALL_REPORTING_PERIODS.indexOf(period.getReportingPeriodTypeCode().getShortName())
            && sccCode != null && sccCode.getMonthlyReporting()) {

            if (period.getCalculationParameterValue() == null) {
                valid = false;
                context.addFederalError(
                    ValidationField.PERIOD_CALC_VALUE.value(),
                    "reportingPeriod.calculationParameterValue.monthly.required",
                    createValidationDetails(period));
            } else if (period.getCalculationParameterValue().compareTo(BigDecimal.ZERO) < 0) {
                valid = false;
                context.addFederalError(
                    ValidationField.PERIOD_CALC_VALUE.value(),
                    "reportingPeriod.calculationParameterValue.monthly.min",
                    createValidationDetails(period));
            }

            if (period.getFuelUseValue() != null && period.getFuelUseValue().compareTo(BigDecimal.ZERO) < 0) {
                valid = false;
                context.addFederalError(
                    ValidationField.PERIOD_FUEL_VALUE.value(),
                    "reportingPeriod.fuelUseValue.monthly.min",
                    createValidationDetails(period));
            }
        }

        return valid;
    }

    private String getEmissionsUnitIdentifier(ReportingPeriod period) {
        if (period.getEmissionsProcess() != null && period.getEmissionsProcess().getEmissionsUnit() != null) {
            return period.getEmissionsProcess().getEmissionsUnit().getUnitIdentifier();
        }
        return null;
    }

    private String getEmissionsProcessIdentifier(ReportingPeriod period) {
        if (period.getEmissionsProcess() != null) {
            return period.getEmissionsProcess().getEmissionsProcessIdentifier();
        }
        return null;
    }

    private ValidationDetailDto createValidationDetails(ReportingPeriod source) {

        String description = MessageFormat.format("Emissions Unit: {0}, Emissions Process: {1}, Month: {2}",
                getEmissionsUnitIdentifier(source),
                getEmissionsProcessIdentifier(source),
                source.getReportingPeriodTypeCode().getShortName());

        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), getEmissionsProcessIdentifier(source), EntityType.REPORTING_PERIOD, description);
        if (source.getEmissionsProcess() != null) {
            dto.getParents().add(new ValidationDetailDto(
                    source.getEmissionsProcess().getId(),
                    source.getEmissionsProcess().getEmissionsProcessIdentifier(),
                    EntityType.SEMIANNUAL_REPORTING_PERIOD));
        }
        return dto;
    }
}
