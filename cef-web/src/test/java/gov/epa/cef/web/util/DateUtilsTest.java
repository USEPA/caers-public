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

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

@Category(TestCategories.FastTest.class)
@RunWith(MockitoJUnitRunner.class)
public class DateUtilsTest {


    @Test
    public void getFiscalYearForDate_Should_ReturnCorrectCurrentFiscalYear_When_ValidDatePassed() {

        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.YEAR, 2019);
        assertEquals(new Integer(2019), DateUtils.getFiscalYearForDate(cal.getTime()));

        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
        cal.set(Calendar.YEAR, 2019);
        assertEquals(new Integer(2019), DateUtils.getFiscalYearForDate(cal.getTime()));

        cal.set(Calendar.MONTH, Calendar.MARCH);
        cal.set(Calendar.YEAR, 2019);
        assertEquals(new Integer(2019), DateUtils.getFiscalYearForDate(cal.getTime()));

        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.YEAR, 2019);
        assertEquals(new Integer(2019), DateUtils.getFiscalYearForDate(cal.getTime()));

        cal.set(Calendar.MONTH, Calendar.MAY);
        cal.set(Calendar.YEAR, 2019);
        assertEquals(new Integer(2019), DateUtils.getFiscalYearForDate(cal.getTime()));

        cal.set(Calendar.MONTH, Calendar.JUNE);
        cal.set(Calendar.YEAR, 2019);
        assertEquals(new Integer(2019), DateUtils.getFiscalYearForDate(cal.getTime()));

        cal.set(Calendar.MONTH, Calendar.JULY);
        cal.set(Calendar.YEAR, 2019);
        assertEquals(new Integer(2019), DateUtils.getFiscalYearForDate(cal.getTime()));

        cal.set(Calendar.MONTH, Calendar.AUGUST);
        cal.set(Calendar.YEAR, 2019);
        assertEquals(new Integer(2019), DateUtils.getFiscalYearForDate(cal.getTime()));

        cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
        cal.set(Calendar.YEAR, 2019);
        assertEquals(new Integer(2019), DateUtils.getFiscalYearForDate(cal.getTime()));

        cal.set(Calendar.MONTH, Calendar.OCTOBER);
        cal.set(Calendar.YEAR, 2019);
        assertEquals(new Integer(2020), DateUtils.getFiscalYearForDate(cal.getTime()));

        cal.set(Calendar.MONTH, Calendar.NOVEMBER);
        cal.set(Calendar.YEAR, 2019);
        assertEquals(new Integer(2020), DateUtils.getFiscalYearForDate(cal.getTime()));

        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.YEAR, 2019);
        assertEquals(new Integer(2020), DateUtils.getFiscalYearForDate(cal.getTime()));
    }

}
