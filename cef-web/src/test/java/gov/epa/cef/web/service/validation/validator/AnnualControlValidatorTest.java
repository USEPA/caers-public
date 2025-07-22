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
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import gov.epa.cef.web.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.baidu.unbiz.fluentvalidator.ValidationError;

import gov.epa.cef.web.repository.ControlRepository;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.federal.AnnualControlValidator;
import gov.epa.cef.web.service.validation.validator.federal.AnnualControlPollutantValidator;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AnnualControlValidatorTest extends BaseValidatorTest {

	@InjectMocks
	private AnnualControlValidator validator;

	@InjectMocks
	private AnnualControlPollutantValidator pollutantValidator;

    @Mock
    private EmissionsReportRepository reportRepo;

    @Mock
    private ControlRepository controlRepo;

	@Before
    public void init(){

	    List<EmissionsReport> erList = new ArrayList<EmissionsReport>();
        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);
        EmissionsReport er1 = new EmissionsReport();
        EmissionsReport er2 = new EmissionsReport();
        er1.setId(1L);
        er2.setId(2L);
        er1.setYear((short) 2018);
        er2.setYear((short) 2020);
        er1.setEisProgramId("1");
        er2.setEisProgramId("1");
        er1.setMasterFacilityRecord(mfr);
        er2.setMasterFacilityRecord(mfr);
        erList.add(er1);
        erList.add(er2);

        OperatingStatusCode os = new OperatingStatusCode();
        os.setCode("OP");

        Control control = new Control();
        control.setId(1L);
        control.setOperatingStatusCode(os);
        control.setStatusYear((short) 2018);
        control.setIdentifier("test");

        when(reportRepo.findByMasterFacilityRecordId(1L)).thenReturn(erList);
        when(reportRepo.findByMasterFacilityRecordId(2L)).thenReturn(Collections.emptyList());
        when(controlRepo.retrieveByIdentifierFacilityYear(
                "test",1L,(short) 2018)).thenReturn(Collections.singletonList(control));
        when(controlRepo.retrieveByIdentifierFacilityYear(
                "test new",1L,(short) 2018)).thenReturn(Collections.emptyList());
	}

	@Test
	public void totalDirectEntryFalse_PassTest() {

		CefValidatorContext cefContext = createContext();
		Control testData = createBaseControl();

		assertTrue(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

	}

	@Test
	public void duplicateIdentifiersPassTest() {

		CefValidatorContext cefContext = createContext();
		Control testData = createBaseControl();
		Control c1 = new Control();
		c1.setIdentifier("test 1");
		testData.getFacilitySite().getControls().add(c1);

		assertTrue(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

	}

	@Test
	public void duplicateIdentifiersFailTest() {

		CefValidatorContext cefContext = createContext();
		Control testData = createBaseControl();
		Control c1 = new Control();
		c1.setIdentifier("test");
		testData.getFacilitySite().getControls().add(c1);

		assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_IDENTIFIER.value()) && errorMap.get(ValidationField.CONTROL_IDENTIFIER.value()).size() == 1);

	}

	@Test
    public void statusYearRangePassTest() {

        CefValidatorContext cefContext = createContext();
        Control testData = createBaseControl();

        testData.setStatusYear((short) 1900);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        testData.setStatusYear((short) 2020);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

	@Test
    public void statusYearRangeFailTest() {

		CefValidatorContext cefContext = createContext();
        Control testData = createBaseControl();

        testData.setStatusYear((short) 1800);

        assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_STATUS_YEAR.value()) && errorMap.get(ValidationField.CONTROL_STATUS_YEAR.value()).size() == 1);
	}

	@Test
    public void statusYearNullFailTest() {

		CefValidatorContext cefContext = createContext();
        Control testData = createBaseControl();

        OperatingStatusCode opStatCode = new OperatingStatusCode();
    	opStatCode.setCode("TS");
    	testData.setOperatingStatusCode(opStatCode);
        testData.setStatusYear(null);

        assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_STATUS_YEAR.value()) && errorMap.get(ValidationField.CONTROL_STATUS_YEAR.value()).size() == 1);
	}

	/**
	 * There should be errors for control pollutant percent reduction when the percent is < 5 or >= 100.
	 */
	@Test
	public void controlPollutantPercentReductionTest() {

		CefValidatorContext cefContext = createContext();
		Control testData = createBaseControl();
		ControlPollutant cp = new ControlPollutant();
		Pollutant p = new Pollutant();
		p.setPollutantName("Nitrogen Oxides");
		p.setPollutantCode("NOX");
		cp.setPollutant(p);
		cp.setPercentReduction(BigDecimal.valueOf(99.9));
		testData.getPollutants().add(cp);

		assertTrue(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

		cefContext = createContext();
		cp.setPercentReduction(BigDecimal.ZERO);

		assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_POLLUTANT.value()) && errorMap.get(ValidationField.CONTROL_POLLUTANT.value()).size() == 1);

		cefContext = createContext();
		cp.setPercentReduction(BigDecimal.valueOf(100.0));

		assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_POLLUTANT.value()) && errorMap.get(ValidationField.CONTROL_POLLUTANT.value()).size() == 1);

	}

	@Test
	public void duplicateControlPollutantFailTest() {

		CefValidatorContext cefContext = createContext();
		Control testData = createBaseControl();
		ControlPollutant cp1 = new ControlPollutant();
		Pollutant p1 = new Pollutant();
		p1.setPollutantCode("NOX");
		Pollutant p2 = new Pollutant();
		p2.setPollutantCode("CO2");
		cp1.setPollutant(p1);
		cp1.setPercentReduction(BigDecimal.valueOf(50.0));
		testData.getPollutants().add(cp1);
		ControlPollutant cp2 = new ControlPollutant();
		cp2.setPercentReduction(BigDecimal.valueOf(50.0));
		testData.getPollutants().add(cp2);
		cp2.setPollutant(p1);

		assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_POLLUTANT.value()) && errorMap.get(ValidationField.CONTROL_POLLUTANT.value()).size() == 1);

	}

	@Test
	public void duplicateControlPollutantPassTest() {

		CefValidatorContext cefContext = createContext();
		Control testData = createBaseControl();
		Pollutant p2 = new Pollutant();
		p2.setPollutantCode("CH4");
		ControlPollutant cp2 = new ControlPollutant();
		cp2.setPollutant(p2);
		cp2.setPercentReduction(BigDecimal.valueOf(50.0));
		testData.getPollutants().add(cp2);

		assertTrue(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

	}

	@Test
	public void controlPathAssignedFailTest() {

		CefValidatorContext cefContext = createContext();
		Control testData = createBaseControl();

		testData.setAssignments(null);

		assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_WARNING.value()) && errorMap.get(ValidationField.CONTROL_PATH_WARNING.value()).size() == 1);

	}

	@Test
	public void controlPathAssignedPassTest() {

		CefValidatorContext cefContext = createContext();
		Control testData = createBaseControl();

		assertTrue(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

	}

	@Test
	public void controlPollutantRequiredFailTest() {

		CefValidatorContext cefContext = createContext();
		Control testData = createBaseControl();

		testData.setPollutants(null);

		assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_POLLUTANT.value()) && errorMap.get(ValidationField.CONTROL_POLLUTANT.value()).size() == 1);

	}

	@Test
	public void controlPercentControlRangeFailTest() {

		CefValidatorContext cefContext = createContext();
		Control testData = createBaseControl();

		testData.setPercentControl(BigDecimal.valueOf(0.5));

		assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_PERCENT_CONTROL.value()) && errorMap.get(ValidationField.CONTROL_PERCENT_CONTROL.value()).size() == 1);

		cefContext = createContext();
		testData.setPercentControl(BigDecimal.valueOf(500.0));

		assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_PERCENT_CONTROL.value()) && errorMap.get(ValidationField.CONTROL_PERCENT_CONTROL.value()).size() == 1);
	}

	@Test
	public void controlPercentControlPrecisionFailTest() {

		CefValidatorContext cefContext = createContext();
		Control testData = createBaseControl();

		testData.setPercentControl(BigDecimal.valueOf(10.568));

		assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_PERCENT_CONTROL.value()) && errorMap.get(ValidationField.CONTROL_PERCENT_CONTROL.value()).size() == 1);
	}

	@Test
	public void controlDatesPassTest() {
		CefValidatorContext cefContext = createContext();
		Control testData = createBaseControl();

		LocalDate start = LocalDate.parse("2021-01-01");
		LocalDate end = LocalDate.parse("2021-04-04");
		LocalDate upgrade = LocalDate.parse("2021-02-02");
		testData.setStartDate(start);
		testData.setEndDate(end);
		testData.setUpgradeDate(upgrade);

		assertTrue(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
	}

	@Test
	public void controlDatesFailTest() {
		CefValidatorContext cefContext = createContext();
		Control testData = createBaseControl();

		LocalDate start = LocalDate.parse("2021-04-04");
		LocalDate end = LocalDate.parse("2021-01-01");
		testData.setStartDate(start);
		testData.setEndDate(end);

		assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_DATE.value()) && errorMap.get(ValidationField.CONTROL_DATE.value()).size() == 1);

		cefContext = createContext();
		end = LocalDate.parse("2021-05-05");
		LocalDate upgrade = LocalDate.parse("2021-06-06");
		testData.setUpgradeDate(upgrade);
		testData.setEndDate(end);

		assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_DATE.value()) && errorMap.get(ValidationField.CONTROL_DATE.value()).size() == 1);
	}

	@Test
	public void controlDateRangeFailTest() {
		CefValidatorContext cefContext = createContext();
		Control testData = createBaseControl();

		LocalDate start = LocalDate.parse("1800-04-04");
		testData.setStartDate(start);

		assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_DATE.value()) && errorMap.get(ValidationField.CONTROL_DATE.value()).size() == 1);

		cefContext = createContext();
		start = LocalDate.parse("1900-04-04");
		LocalDate end = LocalDate.parse("2055-05-05");
		testData.setStartDate(start);
		testData.setEndDate(end);

		assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_DATE.value()) && errorMap.get(ValidationField.CONTROL_DATE.value()).size() == 1);

		cefContext = createContext();
		LocalDate upgrade = LocalDate.parse("2055-05-05");
		testData.setUpgradeDate(upgrade);
		testData.setEndDate(null);

		assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_DATE.value()) && errorMap.get(ValidationField.CONTROL_DATE.value()).size() == 1);
	}

	@Test
	public void controlPollutantPassTest() {
		CefValidatorContext cefContext = createContext();
		Control testData = createBaseControl();

		ControlPollutant cp1 = new ControlPollutant();
		ControlPollutant cp2 = new ControlPollutant();
		Pollutant p1 = new Pollutant();
		Pollutant p2 = new Pollutant();
		p1.setPollutantCode("PM10-FIL");
		p2.setPollutantCode("PM25-FIL");
		cp1.setPollutant(p1);
		cp2.setPollutant(p2);
		cp1.setPercentReduction(BigDecimal.valueOf(50.0));
		cp2.setPercentReduction(BigDecimal.valueOf(50.0));
		testData.getPollutants().add(cp1);
		testData.getPollutants().add(cp2);

		assertTrue(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

		cefContext = createContext();
		ControlPollutant cp3 = new ControlPollutant();
		ControlPollutant cp4 = new ControlPollutant();
		Pollutant p3 = new Pollutant();
		Pollutant p4 = new Pollutant();
		p3.setPollutantCode("PM10-PRI");
		p4.setPollutantCode("PM25-PRI");
		cp3.setPollutant(p3);
		cp4.setPollutant(p4);
		cp3.setPercentReduction(BigDecimal.valueOf(80.0));
		cp4.setPercentReduction(BigDecimal.valueOf(50.0));
		testData.getPollutants().add(cp3);
		testData.getPollutants().add(cp4);

		assertTrue(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
	}

	@Test
	public void controlPollutant_PM25FIL_PM10FIL_FailTest() {
		CefValidatorContext cefContext = createContext();
		Control testData = createBaseControl();

		ControlPollutant cp1 = new ControlPollutant();
		ControlPollutant cp2 = new ControlPollutant();
		Pollutant p1 = new Pollutant();
		Pollutant p2 = new Pollutant();
		p1.setPollutantCode("PM10-FIL");
		p2.setPollutantCode("PM25-FIL");
		cp1.setPollutant(p1);
		cp2.setPollutant(p2);
		cp1.setPercentReduction(BigDecimal.valueOf(50.0));
		cp2.setPercentReduction(BigDecimal.valueOf(80.0));
		testData.getPollutants().add(cp1);
		testData.getPollutants().add(cp2);

		assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_POLLUTANT.value()) && errorMap.get(ValidationField.CONTROL_POLLUTANT.value()).size() == 1);
	}

	@Test
	public void controlPollutant_PM25PRI_PM10PRI_FailTest() {
		CefValidatorContext cefContext = createContext();
		Control testData = createBaseControl();

		ControlPollutant cp1 = new ControlPollutant();
		ControlPollutant cp2 = new ControlPollutant();
		Pollutant p1 = new Pollutant();
		Pollutant p2 = new Pollutant();
		p1.setPollutantCode("PM10-PRI");
		p2.setPollutantCode("PM25-PRI");
		cp1.setPollutant(p1);
		cp2.setPollutant(p2);
		cp1.setPercentReduction(BigDecimal.valueOf(50.0));
		cp2.setPercentReduction(BigDecimal.valueOf(80.0));
		testData.getPollutants().add(cp1);
		testData.getPollutants().add(cp2);

		assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_POLLUTANT.value()) && errorMap.get(ValidationField.CONTROL_POLLUTANT.value()).size() == 1);

	}

	@Test
	public void controlPathPollutantLegacyTest() {
		CefValidatorContext cefContext = createContext();
		Control testData = createBaseControl();

		ControlPollutant cp1 = new ControlPollutant();
		Pollutant p1 = new Pollutant();
		p1.setPollutantCode("10025737");
		p1.setPollutantName("test pollutant");
		p1.setLastInventoryYear(2005);
		cp1.setPollutant(p1);
		cp1.setPercentReduction(BigDecimal.valueOf(50.0));

		EmissionsReport er = new EmissionsReport();
        er.setId(1L);
        er.setYear(new Short("2020"));
		testData.getFacilitySite().setEmissionsReport(er);

		cp1.setControl(testData);

		assertFalse(this.pollutantValidator.validate(cefContext, cp1));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_POLLUTANT.value()) && errorMap.get(ValidationField.CONTROL_PATH_POLLUTANT.value()).size() == 1);
    }

	@Test
    public void operationStatusPSFailTest() {
		CefValidatorContext cefContext = createContext();
		Control testData = createBaseControl();

		OperatingStatusCode opStatusCode = new OperatingStatusCode();
        opStatusCode.setCode("PS");
        testData.setOperatingStatusCode(opStatusCode);
        testData.setStatusYear((short) 2020);

        assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_STATUS_YEAR.value()) && errorMap.get(ValidationField.CONTROL_STATUS_YEAR.value()).size() == 1);
	}

	@Test
    public void newControlShutdownPassTest() {
        // pass when previous exists and current op status is OP
        CefValidatorContext cefContext = createContext();
        Control testData = createBaseControl();
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("OP");
        testData.setOperatingStatusCode(opStatCode);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        // pass when previous exists and current op status is PS/TS
        cefContext = createContext();
        testData.getOperatingStatusCode().setCode("TS");

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        // pass when previous report exists, but control doesn't and current op status is OP
        cefContext = createContext();
        testData.getOperatingStatusCode().setCode("OP");
        testData.setIdentifier("test new");

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        // pass when previous report doesn't exist and current op status is OP
        cefContext = createContext();
        testData.getFacilitySite().getEmissionsReport().getMasterFacilityRecord().setId(2L);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void newControlShutdownPassFail() {
        // fail when previous report exists, but control doesn't and current op status is TS
        CefValidatorContext cefContext = createContext();
        Control testData = createBaseControl();
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);
        testData.setIdentifier("test new");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_STATUS_CODE.value()) && errorMap.get(ValidationField.CONTROL_STATUS_CODE.value()).size() == 1);

        // fail when previous report doesn't exist and current op status is TS
        cefContext = createContext();
        testData.getFacilitySite().getEmissionsReport().getMasterFacilityRecord().setId(2L);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_STATUS_CODE.value()) && errorMap.get(ValidationField.CONTROL_STATUS_CODE.value()).size() == 1);
    }

    @Test
    public void operationStatusPSPreviousYearOpCurrentYearFailTest() {

        CefValidatorContext cefContext = createContext();
        Control testData = createBaseControl();

        OperatingStatusCode opStatusCode = new OperatingStatusCode();
        OperatingStatusCode psStatusCode = new OperatingStatusCode();
        opStatusCode.setCode("OP");
        psStatusCode.setCode("PS");
        testData.setOperatingStatusCode(opStatusCode);
        testData.setPreviousYearOperatingStatusCode(psStatusCode);
        testData.setStatusYear((short) 2019);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_STATUS_CODE.value()) && errorMap.get(ValidationField.CONTROL_STATUS_CODE.value()).size() == 1);
    }

    @Test
    public void percentControlNullOperatingStatusOperatingFailTest() {
        CefValidatorContext cefContext = createContext();
        Control testData = createBaseControl();

        OperatingStatusCode opStatusCode = new OperatingStatusCode();
        opStatusCode.setCode("OP");
        testData.setOperatingStatusCode(opStatusCode);
        testData.setPercentControl(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_PERCENT_CONTROL.value()) && errorMap.get(ValidationField.CONTROL_PERCENT_CONTROL.value()).size() == 1);
    }

    @Test
    public void percentControlNotNullOperatingStatusOperatingPassTest() {
        CefValidatorContext cefContext = createContext();
        Control testData = createBaseControl();

        OperatingStatusCode opStatusCode = new OperatingStatusCode();
        opStatusCode.setCode("OP");
        testData.setOperatingStatusCode(opStatusCode);
        testData.setPercentControl(BigDecimal.valueOf(95));

        assertTrue(this.validator.validate(cefContext, testData));
    }

    @Test
    public void percentControlNullOperatingStatusPSPassTest() {
        CefValidatorContext cefContext = createContext();
        Control testData = createBaseControl();

        OperatingStatusCode psStatusCode = new OperatingStatusCode();
        psStatusCode.setCode("PS");
        testData.setOperatingStatusCode(psStatusCode);
        testData.setPercentControl(null);

        assertFalse(this.validator.validate(cefContext, testData));

        // The PS status causes one validation to fail. If the percentControl null validation
        // failed then the size of the getErrors array would be 2.
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
    }

    @Test
    public void operatingControlShutdownEmissionsUnitFailTest() {
        CefValidatorContext cefContext = createContext();
        Control testData = createBaseControl();

        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");

        testData.getAssignments().get(0).getControlPath().getReleasePointAppts().get(0).getEmissionsProcess()
            .getEmissionsUnit().setOperatingStatusCode(opStatCode);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_STATUS_CODE.value())
            && errorMap.get(ValidationField.CONTROL_STATUS_CODE.value()).size() == 1);
    }

    @Test
    public void operatingControlShutdownEmissionsProcessFailTest() {
        CefValidatorContext cefContext = createContext();
        Control testData = createBaseControl();

        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("PS");

        testData.getAssignments().get(0).getControlPath().getReleasePointAppts().get(0).getEmissionsProcess()
            .setOperatingStatusCode(opStatCode);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_STATUS_CODE.value())
            && errorMap.get(ValidationField.CONTROL_STATUS_CODE.value()).size() == 1);
    }

	private Control createBaseControl() {

	    OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("OP");

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);

	    EmissionsReport er = new EmissionsReport();
        er.setId(1L);
        er.setYear(new Short("2020"));
        er.setMasterFacilityRecord(mfr);

        FacilitySite facility = new FacilitySite();
        facility.setId(1L);
        facility.setOperatingStatusCode(opStatCode);
        facility.setEmissionsReport(er);

        EmissionsUnit eu = new EmissionsUnit();
        eu.setId(1L);
        eu.setOperatingStatusCode(opStatCode);

        EmissionsProcess ep = new EmissionsProcess();
        ep.setId(2L);
        ep.setOperatingStatusCode(opStatCode);
        ep.setEmissionsUnit(eu);

		Control result = new Control();
    	result.setOperatingStatusCode(opStatCode);
		result.setId(1L);
		result.setIdentifier("test");
		result.setStatusYear((short) 2020);
		result.setPercentControl(BigDecimal.valueOf(50.0));
		facility.getControls().add(result);
		result.setFacilitySite(facility);

		ControlAssignment ca = new ControlAssignment();
        ca.setId(1234L);

        ControlPath cp = new ControlPath();
        cp.setId(1L);
        cp.getAssignments().add(ca);

        ReleasePoint rp = new ReleasePoint();
        rp.setId(1L);

        List<ReleasePointAppt> rpaList = new ArrayList<>();
        ReleasePointAppt rpa = new ReleasePointAppt();
        rpa.setReleasePoint(rp);
        rpa.setEmissionsProcess(ep);
        rpaList.add(rpa);

        cp.setReleasePointAppts(rpaList);
        ca.setControlPath(cp);
        ca.setControl(result);
		result.getAssignments().add(ca);

		ControlPollutant cp1 = new ControlPollutant();
		Pollutant p1 = new Pollutant();
		p1.setPollutantCode("CO2");
		cp1.setPollutant(p1);
		cp1.setPercentReduction(BigDecimal.valueOf(50.0));
		result.getPollutants().add(cp1);

		return result;
	}

}
