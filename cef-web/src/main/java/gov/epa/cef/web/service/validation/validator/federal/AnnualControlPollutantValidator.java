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

import gov.epa.cef.web.util.ConstantUtils;
import org.springframework.stereotype.Component;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import gov.epa.cef.web.domain.ControlPollutant;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AnnualControlPollutantValidator extends BaseValidator<ControlPollutant> {

    @Override
    public boolean validate(ValidatorContext validatorContext, ControlPollutant controlPollutant) {

        boolean result = true;
        CefValidatorContext context = getCefValidatorContext(validatorContext);

        String regex = "^\\d{0,3}(\\.\\d{1})?$";
        Pattern pattern = Pattern.compile(regex);
        if (!ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(
                controlPollutant.getControl().getOperatingStatusCode().getCode())
            && controlPollutant.getPercentReduction() != null
        ) {

            Matcher matcher = pattern.matcher(controlPollutant.getPercentReduction().stripTrailingZeros().toPlainString());
            if (!matcher.matches()) {
                result = false;
                context.addFederalError(
                    ValidationField.CONTROL_POLLUTANT_PERCENT_REDUCTION.value(),
                    "control.pollutant.percentReductionEfficiency.invalidFormat",
                    createValidationDetails(controlPollutant),
                    controlPollutant.getControl().getIdentifier());
            }
        }

        if (controlPollutant.getPollutant() != null && controlPollutant.getPollutant().getLastInventoryYear() != null
                && controlPollutant.getPollutant().getLastInventoryYear() < controlPollutant.getControl().getFacilitySite().getEmissionsReport().getYear()) {

            result = false;
            context.addFederalError(
                    ValidationField.CONTROL_PATH_POLLUTANT.value(),
                    "pollutant.legacy",
                    createValidationDetails(controlPollutant));
        }

        return result;
    }

    private ValidationDetailDto createValidationDetails(ControlPollutant source) {

        String description = MessageFormat.format("Control: {0}, Control Pollutant: {1}", source.getControl().getIdentifier(), source.getPollutant().getPollutantName());

        ValidationDetailDto dto = new ValidationDetailDto(source.getControl().getId(), source.getControl().getIdentifier(), EntityType.CONTROL, description);
        return dto;
    }

}
