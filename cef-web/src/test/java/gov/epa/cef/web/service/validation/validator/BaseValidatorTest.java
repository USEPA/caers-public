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
package gov.epa.cef.web.service.validation.validator;

import gov.epa.cef.web.config.TestCategories;
import gov.epa.cef.web.service.validation.CefValidatorContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.experimental.categories.Category;

import com.baidu.unbiz.fluentvalidator.ValidationError;

@Category(TestCategories.FastTest.class)
abstract class BaseValidatorTest {

    protected CefValidatorContext createContext() {

        return new CefValidatorContext(null, "validation/emissionsreport");
    }

    protected Map<String, List<ValidationError>> mapErrors(List<ValidationError> errors) {
        return errors.stream().collect(Collectors.groupingBy(ValidationError::getField));
    }
}
