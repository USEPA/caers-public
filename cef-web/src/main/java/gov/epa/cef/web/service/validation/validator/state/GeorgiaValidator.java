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
package gov.epa.cef.web.service.validation.validator.state;

import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.service.validation.ValidationFeature;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import gov.epa.cef.web.service.validation.validator.IEmissionsReportValidator;
import org.springframework.stereotype.Component;

@Component
public class GeorgiaValidator
    extends BaseValidator<EmissionsReport>
    implements IEmissionsReportValidator {

    @Override
    public boolean accept(ValidatorContext context, EmissionsReport emissionsReport) {

        return getCefValidatorContext(context).isEnabled(ValidationFeature.GADNR);
    }
}
