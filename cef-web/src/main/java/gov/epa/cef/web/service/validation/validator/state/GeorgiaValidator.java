/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
