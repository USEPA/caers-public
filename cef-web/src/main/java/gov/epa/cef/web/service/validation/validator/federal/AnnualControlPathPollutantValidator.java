/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.validation.validator.federal;

import java.text.MessageFormat;
import org.springframework.stereotype.Component;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import gov.epa.cef.web.domain.ControlPathPollutant;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AnnualControlPathPollutantValidator extends BaseValidator<ControlPathPollutant> {

    @Override
    public boolean validate(ValidatorContext validatorContext, ControlPathPollutant controlPathPollutant) {

        boolean result = true;
        CefValidatorContext context = getCefValidatorContext(validatorContext);

        String regex = "^\\d{0,3}(\\.\\d{1})?$";
        Pattern pattern = Pattern.compile(regex);
        if(controlPathPollutant.getPercentReduction() != null) {
            Matcher matcher = pattern.matcher(controlPathPollutant.getPercentReduction().stripTrailingZeros().toPlainString());
            if(!matcher.matches()){
                result = false;
                context.addFederalError(
                    ValidationField.CONTROL_PATH_POLLUTANT_PERCENT_REDUCTION.value(),
                    "controlPath.pollutant.percentReductionEfficiency.invalidFormat",
                    createValidationDetails(controlPathPollutant),
                    controlPathPollutant.getControlPath().getPathId());
            }
        }

        if (controlPathPollutant.getPollutant() != null && controlPathPollutant.getPollutant().getLastInventoryYear() != null
                && controlPathPollutant.getPollutant().getLastInventoryYear() < controlPathPollutant.getControlPath().getFacilitySite().getEmissionsReport().getYear()) {

            result = false;
            context.addFederalError(
                    ValidationField.CONTROL_PATH_POLLUTANT.value(),
                    "pollutant.legacy",
                    createValidationDetails(controlPathPollutant));
        }

        return result;
    }

    private ValidationDetailDto createValidationDetails(ControlPathPollutant source) {

        String description = MessageFormat.format("Control Path: {0}, Control Path Pollutant: {1}", source.getControlPath().getPathId(), source.getPollutant().getPollutantName());

        ValidationDetailDto dto = new ValidationDetailDto(source.getControlPath().getId(), source.getControlPath().getPathId(), EntityType.CONTROL_PATH, description);
        return dto;
    }

}
