/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.util;

import gov.epa.cef.web.config.TestCategories;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@Category(TestCategories.FastTest.class)
@RunWith(MockitoJUnitRunner.class)
public class CalculationUtilsTest {


    @Test
    public void convertMassUnits_Should_Return_ConvertedUnits_When_ValidValuesPassed() {

        BigDecimal sourceValue = new BigDecimal("10000");
        assertEquals(new BigDecimal("5.0000"), CalculationUtils.convertMassUnits(sourceValue, MassUomConversion.LB, MassUomConversion.TON));

        sourceValue = new BigDecimal("1e4");
        assertEquals(new BigDecimal("5"), CalculationUtils.convertMassUnits(sourceValue, MassUomConversion.LB, MassUomConversion.TON));

    }

}
