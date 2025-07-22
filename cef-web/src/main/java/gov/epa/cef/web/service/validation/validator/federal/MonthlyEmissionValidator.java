/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.validation.validator.federal;

import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import gov.epa.cef.web.domain.*;
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
public class MonthlyEmissionValidator extends BaseValidator<Emission> {

    @Autowired
    private PointSourceSccCodeRepository sccRepo;

    @Override
    public boolean validate(ValidatorContext validatorContext, Emission emission) {

        boolean valid = true;

        CefValidatorContext context = getCefValidatorContext(validatorContext);

        PointSourceSccCode sccCode = sccRepo.findById(emission.getReportingPeriod().getEmissionsProcess().getSccCode()).orElse(null);

        if (!emission.getReportingPeriod().getEmissionsProcess().getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN)
                && !emission.getReportingPeriod().getEmissionsProcess().getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN)
                && sccCode != null && sccCode.getMonthlyReporting()) {

            if (emission.getTotalEmissions() == null) {
	            valid = false;
	            context.addFederalError(
	                ValidationField.EMISSION_TOTAL_EMISSIONS.value(),
                    "emission.totalEmissions.monthly.required",
	                createValidationDetails(emission));
            }

            if (ConstantUtils.CEMS_CODE.contentEquals(emission.getEmissionsCalcMethodCode().getCode()) && emission.getMonthlyRate() == null) {
                valid = false;
                context.addFederalError(
                    ValidationField.EMISSION_MONTHLY_RATE.value(),
                    "emission.monthlyRate.monthly.required",
                    createValidationDetails(emission));
            }

            if (emission.getReportingPeriod().getReportingPeriodTypeCode().getShortName().equals(ConstantUtils.SEMIANNUAL)
                && emission.getReportingPeriod().getCalculationParameterValue() != null && emission.getTotalEmissions() != null
                && emission.getReportingPeriod().getCalculationParameterValue().compareTo(BigDecimal.ZERO) > 0
                && emission.getTotalEmissions().compareTo(BigDecimal.ZERO) == 0) {

                valid = false;
                context.addFederalWarning(
                    ValidationField.EMISSION_TOTAL_EMISSIONS.value(),
                    "emission.totalEmissions.nonZero",
                    createValidationDetails(emission));
            }
        }

        return valid;
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

    private ValidationDetailDto createValidationDetails(Emission source) {

        String description = MessageFormat.format("Emissions Unit: {0}, Emissions Process: {1}, Pollutant: {2}, Month: {3}",
                getEmissionsUnitIdentifier(source),
                getEmissionsProcessIdentifier(source),
                getPollutantName(source),
                source.getReportingPeriod().getReportingPeriodTypeCode().getShortName());

        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), getPollutantName(source), EntityType.EMISSION, description);
        if (source.getReportingPeriod() != null) {
            dto.getParents().add(new ValidationDetailDto(source.getReportingPeriod().getId(), null, EntityType.SEMIANNUAL_EMISSION));
        }
        return dto;
    }
}
