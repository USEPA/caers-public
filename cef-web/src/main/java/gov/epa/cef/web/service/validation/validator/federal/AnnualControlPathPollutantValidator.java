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
