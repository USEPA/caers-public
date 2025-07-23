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

import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.ValidationRegistry;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import gov.epa.cef.web.service.validation.validator.ISemiannualReportValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class SemiAnnualReportValidator
    extends BaseValidator<EmissionsReport>
    implements ISemiannualReportValidator {

	Logger logger = LoggerFactory.getLogger(SemiAnnualReportValidator.class);

    @Override
    public void compose(FluentValidator validator,
                        ValidatorContext validatorContext,
                        EmissionsReport emissionsReport) {

        ValidationRegistry registry = getCefValidatorContext(validatorContext).getValidationRegistry();

        // add more validators as needed
        validator.onEach(emissionsReport.getFacilitySites().get(0).getEmissionsUnits(),
            registry.findOneByType(SharedEmissionsUnitValidator.class));

        validator.onEach(emissionsReport.getFacilitySites().get(0).getEmissionsUnits(),
            registry.findOneByType(SemiAnnualEmissionsUnitValidator.class));
    }

    @Override
    public boolean validate(ValidatorContext validatorContext, EmissionsReport report) {

        boolean valid = true;

        CefValidatorContext context = getCefValidatorContext(validatorContext);

        if (report.getYear() == null) {

            // prevented by db constraints
            valid = false;
            context.addFederalError(ValidationField.REPORT_YEAR.value(), "report.year.required");

        } else if (report.getYear().intValue() < Calendar.getInstance().get(Calendar.YEAR) - 1) {

            valid = false;
            context.addFederalWarning(ValidationField.REPORT_YEAR.value(), "report.year.min", "" + (Calendar.getInstance().get(Calendar.YEAR) - 1));
        }

        if (report.getProgramSystemCode() == null) {

            // prevented by db constraints
            valid = false;
            context.addFederalError(ValidationField.REPORT_PROGRAM_SYSTEM_CODE.value(), "report.programSystemCode.required");
        }

        return valid;
    }
}
