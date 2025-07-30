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
