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

import com.baidu.unbiz.fluentvalidator.ValidationError;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.federal.MonthlyOperatingDetailValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MonthlyOperatingDetailValidatorTest extends BaseValidatorTest {

    @InjectMocks
    private MonthlyOperatingDetailValidator validator;

    @Mock
    private PointSourceSccCodeRepository sccRepo;

    @Before
    public void init(){

        PointSourceSccCode scc = new PointSourceSccCode();
        scc.setCode("10200303");
        scc.setFuelUseRequired(true);
        scc.setMonthlyReporting(true);

        when(sccRepo.findById("10200303")).thenReturn(Optional.of(scc));
    }

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
    public void nonZeroHoursPerPeriodTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();

        testData.setAvgHoursPerDay(BigDecimal.ZERO);
        testData.setAvgDaysPerWeek(BigDecimal.ZERO);
        testData.setActualHoursPerPeriod(BigDecimal.valueOf(20));
        testData.setAvgWeeksPerPeriod(BigDecimal.ZERO);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_ACT_HR_PER_PERIOD.value()) && errorMap.get(ValidationField.DETAIL_ACT_HR_PER_PERIOD.value()).size() == 1);
    }

    @Test
    public void opDetailNonZeroValuesTest() {

        CefValidatorContext cefContext = createContext();
        OperatingDetail testData = createBaseOperatingDetail();

        testData.setAvgHoursPerDay(BigDecimal.ZERO);
        testData.setAvgDaysPerWeek(BigDecimal.ZERO);
        testData.setActualHoursPerPeriod(BigDecimal.ZERO);
        testData.setAvgWeeksPerPeriod(BigDecimal.ZERO);

        testData.getReportingPeriod().setFuelUseValue(BigDecimal.TEN);
        testData.getReportingPeriod().setCalculationParameterValue(BigDecimal.TEN);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.DETAIL_ACT_HR_PER_PERIOD.value()) && errorMap.get(ValidationField.DETAIL_ACT_HR_PER_PERIOD.value()).size() == 1);
    }

    private OperatingDetail createBaseOperatingDetail() {

        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("OP");

        ReportingPeriod period = new ReportingPeriod();

        ReportingPeriodCode rpc = new ReportingPeriodCode();
        rpc.setCode("A");
        rpc.setShortName("Annual");
        period.setReportingPeriodTypeCode(rpc);

        period.setEmissionsProcess(new EmissionsProcess());
        period.getEmissionsProcess().setOperatingStatusCode(opStatCode);
        period.getEmissionsProcess().setSccCode("10200303");

        OperatingDetail result = new OperatingDetail();
        result.setAvgHoursPerDay(BigDecimal.valueOf(5));
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
