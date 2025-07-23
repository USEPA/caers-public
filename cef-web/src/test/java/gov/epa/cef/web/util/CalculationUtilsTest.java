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
