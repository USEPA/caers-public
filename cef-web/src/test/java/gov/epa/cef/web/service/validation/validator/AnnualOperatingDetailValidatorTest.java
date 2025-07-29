/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.validation.validator;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.baidu.unbiz.fluentvalidator.ValidationError;

import gov.epa.cef.web.domain.EmissionsProcess;
import gov.epa.cef.web.domain.OperatingDetail;
import gov.epa.cef.web.domain.OperatingStatusCode;
import gov.epa.cef.web.domain.ReportingPeriod;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.federal.AnnualOperatingDetailValidator;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AnnualOperatingDetailValidatorTest extends BaseValidatorTest {

    @InjectMocks
    private AnnualOperatingDetailValidator validator;

    @Test
    public void simpleValidatePassTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void averageHoursPerDayNullFailTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();
        testData.setAvgHoursPerDay(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_AVG_HR_PER_DAY.value()) && errorMap.get(ValidationField.DETAIL_AVG_HR_PER_DAY.value()).size() == 1);
    }

    @Test
    public void averageHoursPerDayRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();

        cefContext = createContext();
        testData.setAvgHoursPerDay(BigDecimal.valueOf(25));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_AVG_HR_PER_DAY.value()) && errorMap.get(ValidationField.DETAIL_AVG_HR_PER_DAY.value()).size() == 1);

        cefContext = createContext();
        testData.setAvgHoursPerDay(BigDecimal.ZERO);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_AVG_HR_PER_DAY.value()) && errorMap.get(ValidationField.DETAIL_AVG_HR_PER_DAY.value()).size() == 1);
    }

    @Test
    public void averageHoursPerDayBoundaryTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();
        testData.setAvgHoursPerDay(BigDecimal.valueOf(0.011));

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        testData.setAvgHoursPerDay(BigDecimal.valueOf(24));

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void averageHoursPerDayBoundaryTestFailure() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();
        testData.setAvgHoursPerDay(BigDecimal.valueOf(0.0011));

        assertFalse(this.validator.validate(cefContext, testData));
        assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        testData.setAvgHoursPerDay(BigDecimal.valueOf(24));

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void averageDaysPerWeekNullFailTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();
        testData.setAvgDaysPerWeek(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_AVG_DAY_PER_WEEK.value()) && errorMap.get(ValidationField.DETAIL_AVG_DAY_PER_WEEK.value()).size() == 1);
    }

    @Test
    public void averageDaysPerWeekRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();

        cefContext = createContext();
        testData.setAvgDaysPerWeek(BigDecimal.valueOf(8));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_AVG_DAY_PER_WEEK.value()) && errorMap.get(ValidationField.DETAIL_AVG_DAY_PER_WEEK.value()).size() == 1);

        cefContext = createContext();
        testData.setAvgDaysPerWeek(BigDecimal.ZERO);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_AVG_DAY_PER_WEEK.value()) && errorMap.get(ValidationField.DETAIL_AVG_DAY_PER_WEEK.value()).size() == 1);
    }

    @Test
    public void averageDaysPerWeekBoundaryTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();
        testData.setAvgDaysPerWeek(BigDecimal.valueOf(0.1));

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        testData.setAvgDaysPerWeek(BigDecimal.valueOf(7));

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void actualHoursPerPeriodNullFailTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();
        testData.setActualHoursPerPeriod(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_ACT_HR_PER_PERIOD.value()) && errorMap.get(ValidationField.DETAIL_ACT_HR_PER_PERIOD.value()).size() == 1);
    }

    @Test
    public void actualHoursPerPeriodRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();

        cefContext = createContext();
        testData.setActualHoursPerPeriod(BigDecimal.valueOf(8785));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_ACT_HR_PER_PERIOD.value()) && errorMap.get(ValidationField.DETAIL_ACT_HR_PER_PERIOD.value()).size() == 1);

        cefContext = createContext();
        testData.setActualHoursPerPeriod(BigDecimal.valueOf(0));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_ACT_HR_PER_PERIOD.value()) && errorMap.get(ValidationField.DETAIL_ACT_HR_PER_PERIOD.value()).size() == 1);
    }

    @Test
    public void actualHoursPerPeriodBoundaryTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();
        testData.setActualHoursPerPeriod(BigDecimal.valueOf(1));

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        testData.setActualHoursPerPeriod(BigDecimal.valueOf(8784));

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void percentsNullFailTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();
        testData.setPercentSpring(null);
        testData.setPercentSummer(null);
        testData.setPercentFall(null);
        testData.setPercentWinter(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 5);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_PCT_SPRING.value()) && errorMap.get(ValidationField.DETAIL_PCT_SPRING.value()).size() == 1);
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_PCT_SUMMER.value()) && errorMap.get(ValidationField.DETAIL_PCT_SUMMER.value()).size() == 1);
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_PCT_FALL.value()) && errorMap.get(ValidationField.DETAIL_PCT_FALL.value()).size() == 1);
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_PCT_WINTER.value()) && errorMap.get(ValidationField.DETAIL_PCT_WINTER.value()).size() == 1);
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_PCT.value()) && errorMap.get(ValidationField.DETAIL_PCT.value()).size() == 1);
    }

    @Test
    public void seasonalPercentsRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();

        cefContext = createContext();
        testData.setPercentSpring(BigDecimal.valueOf(110));
        testData.setPercentSummer(BigDecimal.valueOf(110));
        testData.setPercentFall(BigDecimal.valueOf(110));
        testData.setPercentWinter(BigDecimal.valueOf(110));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 5);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_PCT_SPRING.value()) && errorMap.get(ValidationField.DETAIL_PCT_SPRING.value()).size() == 1);
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_PCT_SUMMER.value()) && errorMap.get(ValidationField.DETAIL_PCT_SUMMER.value()).size() == 1);
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_PCT_FALL.value()) && errorMap.get(ValidationField.DETAIL_PCT_FALL.value()).size() == 1);
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_PCT_WINTER.value()) && errorMap.get(ValidationField.DETAIL_PCT_WINTER.value()).size() == 1);
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_PCT.value()) && errorMap.get(ValidationField.DETAIL_PCT.value()).size() == 1);

        cefContext = createContext();
        testData = createBaseOperatingDetail();

        testData.setPercentSpring(BigDecimal.valueOf(-10));
        testData.setPercentSummer(BigDecimal.valueOf(-10));
        testData.setPercentFall(BigDecimal.valueOf(-10));
        testData.setPercentWinter(BigDecimal.valueOf(-10));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 5);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_PCT_SPRING.value()) && errorMap.get(ValidationField.DETAIL_PCT_SPRING.value()).size() == 1);
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_PCT_SUMMER.value()) && errorMap.get(ValidationField.DETAIL_PCT_SUMMER.value()).size() == 1);
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_PCT_FALL.value()) && errorMap.get(ValidationField.DETAIL_PCT_FALL.value()).size() == 1);
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_PCT_WINTER.value()) && errorMap.get(ValidationField.DETAIL_PCT_WINTER.value()).size() == 1);
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_PCT.value()) && errorMap.get(ValidationField.DETAIL_PCT.value()).size() == 1);
    }

    @Test
    public void seasonalPercentsBoundaryTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();

        cefContext = createContext();
        testData.setPercentSpring(BigDecimal.valueOf(100));
        testData.setPercentSummer(BigDecimal.valueOf(100));
        testData.setPercentFall(BigDecimal.valueOf(100));
        testData.setPercentWinter(BigDecimal.valueOf(100));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_PCT.value()) && errorMap.get(ValidationField.DETAIL_PCT.value()).size() == 1);

        cefContext = createContext();
        testData.setPercentSpring(BigDecimal.ZERO);
        testData.setPercentSummer(BigDecimal.ZERO);
        testData.setPercentFall(BigDecimal.ZERO);
        testData.setPercentWinter(BigDecimal.ZERO);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_PCT.value()) && errorMap.get(ValidationField.DETAIL_PCT.value()).size() == 1);
    }

    @Test
    public void sumPercentsRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();

        cefContext = createContext();
        testData.setPercentWinter(BigDecimal.valueOf(26));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_PCT.value()) && errorMap.get(ValidationField.DETAIL_PCT.value()).size() == 1);

        cefContext = createContext();
        testData.setPercentWinter(BigDecimal.valueOf(24));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_PCT.value()) && errorMap.get(ValidationField.DETAIL_PCT.value()).size() == 1);
    }

    @Test
    public void sumPercentsBoundaryTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();
        testData.setPercentWinter(BigDecimal.valueOf(25.5));

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        testData.setPercentWinter(BigDecimal.valueOf(24.5));

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void avgWeeksPerPeriodNullFailTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();
        testData.setAvgWeeksPerPeriod(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_AVG_WEEK_PER_PERIOD.value()) && errorMap.get(ValidationField.DETAIL_AVG_WEEK_PER_PERIOD.value()).size() == 1);
    }

    @Test
    public void avgWeeksPerPeriodRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();

        cefContext = createContext();
        testData.setAvgWeeksPerPeriod(BigDecimal.valueOf(53));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_AVG_WEEK_PER_PERIOD.value()) && errorMap.get(ValidationField.DETAIL_AVG_WEEK_PER_PERIOD.value()).size() == 1);

        cefContext = createContext();
        testData.setAvgWeeksPerPeriod(BigDecimal.valueOf(0));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_AVG_WEEK_PER_PERIOD.value()) && errorMap.get(ValidationField.DETAIL_AVG_WEEK_PER_PERIOD.value()).size() == 1);
    }

    @Test
    public void avgWeeksPerPeriodBoundaryTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();
        testData.setAvgWeeksPerPeriod(BigDecimal.valueOf(52));

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        testData.setAvgWeeksPerPeriod(BigDecimal.ONE);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void seasonsForAvgWeeksTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();
        testData.setPercentSpring(BigDecimal.valueOf(100));
        testData.setPercentSummer(BigDecimal.ZERO);
        testData.setPercentFall(BigDecimal.ZERO);
        testData.setPercentWinter(BigDecimal.ZERO);
        testData.setAvgWeeksPerPeriod(BigDecimal.valueOf(45));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_AVG_WEEK_PER_PERIOD.value()) && errorMap.get(ValidationField.DETAIL_AVG_WEEK_PER_PERIOD.value()).size() == 1);

        cefContext = createContext();
        testData.setPercentSpring(BigDecimal.valueOf(75));
        testData.setPercentSummer(BigDecimal.valueOf(25));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_AVG_WEEK_PER_PERIOD.value()) && errorMap.get(ValidationField.DETAIL_AVG_WEEK_PER_PERIOD.value()).size() == 1);

        cefContext = createContext();
        testData.setPercentSpring(BigDecimal.valueOf(50));
        testData.setPercentFall(BigDecimal.valueOf(25));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_AVG_WEEK_PER_PERIOD.value()) && errorMap.get(ValidationField.DETAIL_AVG_WEEK_PER_PERIOD.value()).size() == 1);

        cefContext = createContext();
        testData.setPercentSpring(BigDecimal.valueOf(25));
        testData.setPercentWinter(BigDecimal.valueOf(25));

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }


    private OperatingDetail createBaseOperatingDetail() {

        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("OP");

        ReportingPeriod period = new ReportingPeriod();
        period.setEmissionsProcess(new EmissionsProcess());
        period.getEmissionsProcess().setOperatingStatusCode(opStatCode);

        OperatingDetail result = new OperatingDetail();
        result.setAvgHoursPerDay(BigDecimal.valueOf(5.0125));
        result.setAvgDaysPerWeek(BigDecimal.valueOf(5));
        result.setActualHoursPerPeriod(BigDecimal.valueOf(5));
        result.setPercentSpring(BigDecimal.valueOf(25));
        result.setPercentSummer(BigDecimal.valueOf(25));
        result.setPercentFall(BigDecimal.valueOf(25));
        result.setPercentWinter(BigDecimal.valueOf(25));
        result.setAvgWeeksPerPeriod(BigDecimal.valueOf(5));
        result.setReportingPeriod(period);
        period.getOperatingDetails().add(result);

        return result;
    }
}
