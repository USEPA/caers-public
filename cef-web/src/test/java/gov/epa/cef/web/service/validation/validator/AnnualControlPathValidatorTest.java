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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.baidu.unbiz.fluentvalidator.ValidationError;

import gov.epa.cef.web.domain.Control;
import gov.epa.cef.web.domain.ControlAssignment;
import gov.epa.cef.web.domain.ControlMeasureCode;
import gov.epa.cef.web.domain.ControlPath;
import gov.epa.cef.web.domain.ControlPathPollutant;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.FacilitySite;
import gov.epa.cef.web.domain.OperatingStatusCode;
import gov.epa.cef.web.domain.Pollutant;
import gov.epa.cef.web.domain.ReleasePointAppt;
import gov.epa.cef.web.repository.ControlAssignmentRepository;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.federal.AnnualControlPathValidator;
import gov.epa.cef.web.service.validation.validator.federal.AnnualControlPathPollutantValidator;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AnnualControlPathValidatorTest extends BaseValidatorTest {

	@InjectMocks
	private AnnualControlPathValidator validator;

	@InjectMocks
	private AnnualControlPathPollutantValidator pollutantValidator;

	@Mock
	private ControlAssignmentRepository assignmentRepo;

	@Test
	public void simpleValidatePassTest() {

		CefValidatorContext cefContext = createContext();
		ControlPath testData = createBaseControlPath();

		assertTrue(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

	}

	@Test
	public void duplicateIdentifiersPassTest() {

		CefValidatorContext cefContext = createContext();
		ControlPath testData = createBaseControlPath();

		assertTrue(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

	}

	@Test
    public void duplicateControlPathIDFailTest() {
        CefValidatorContext cefContext = createContext();
        ControlPath testData = createBaseControlPath();
        ControlPath cp1 = new ControlPath();
        cp1.setPathId("Path 1");
        cp1.setDescription("test description 6");
        testData.getFacilitySite().getControlPaths().add(cp1);

        assertFalse(this.validator.validate(cefContext, testData));
    }

	@Test
	public void duplicateControlDeviceOnPathFailTest() {

		CefValidatorContext cefContext = createContext();
		ControlPath testData = createBaseControlPath();
		testData.getAssignments().get(0).setPercentApportionment(BigDecimal.valueOf(40.0));
		ControlAssignment ca1 = new ControlAssignment();
		ControlAssignment ca2 = new ControlAssignment();
		Control c1 = new Control();
		ControlMeasureCode cmc = new ControlMeasureCode();
		OperatingStatusCode opStatusCode = new OperatingStatusCode();
        opStatusCode.setCode("OP");
		cmc.setDescription("test");
		c1.setId(1L);
		c1.setIdentifier("control1");
		c1.setControlMeasureCode(cmc);
		c1.getAssignments().add(ca1);
		c1.setOperatingStatusCode(opStatusCode);
		ca1.setSequenceNumber(1);
		ca2.setSequenceNumber(1);
		ca1.setPercentApportionment(BigDecimal.valueOf(30.0));
		ca2.setPercentApportionment(BigDecimal.valueOf(30.0));
		ca1.setControl(c1);
		ca2.setControl(c1);
		testData.getAssignments().add(ca1);
		testData.getAssignments().add(ca2);


		assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_ASSIGNMENT.value()) && errorMap.get(ValidationField.CONTROL_PATH_ASSIGNMENT.value()).size() == 1);

	}

	@Test
	public void duplicateControlPathOnPathFailTest() {

		CefValidatorContext cefContext = createContext();
		ControlPath testData = createBaseControlPath();
		testData.getAssignments().get(0).setPercentApportionment(BigDecimal.valueOf(40.0));
		ControlPath cp1 = new ControlPath();
		cp1.setId(1L);
		ControlAssignment ca1 = new ControlAssignment();
		ControlAssignment ca2 = new ControlAssignment();
		ca1.setControlPath(testData);
		ca2.setControlPath(testData);
		ca1.setControlPathChild(cp1);
		ca2.setControlPathChild(cp1);
		testData.getAssignments().add(ca1);
		testData.getAssignments().add(ca2);
		ca1.setSequenceNumber(1);
		ca2.setSequenceNumber(1);
		ca1.setPercentApportionment(BigDecimal.valueOf(30.0));
		ca2.setPercentApportionment(BigDecimal.valueOf(30.0));

		assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_ASSIGNMENT.value()) && errorMap.get(ValidationField.CONTROL_PATH_ASSIGNMENT.value()).size() == 1);

	}

    @Test
    public void releasePointApportionmentAssignmentFailTest() {

        CefValidatorContext cefContext = createContext();
		ControlPath testData = new ControlPath();
        ControlPathPollutant cpp = new ControlPathPollutant();
        Pollutant p1 = new Pollutant();
        p1.setPollutantCode("NOX");
		FacilitySite fs = new FacilitySite();
		fs.getControlPaths().add(testData);
		testData.setFacilitySite(fs);
		testData.getPollutants().add(cpp);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_RPA_WARNING.value()) && errorMap.get(ValidationField.CONTROL_PATH_RPA_WARNING.value()).size() == 1);
    }

    @Test
    public void assignedPathPollutantFailTest() {

        CefValidatorContext cefContext = createContext();
        ControlPath testData = createBaseControlPath();
        testData.getPollutants().clear();

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_POLLUTANT.value()) && errorMap.get(ValidationField.CONTROL_PATH_POLLUTANT.value()).size() == 1);
    }

    @Test
    public void controlDeviceAssignmentFailTest() {

        CefValidatorContext cefContext = createContext();
        ControlAssignment ca = new ControlAssignment();
		ControlPath testData = new ControlPath();
		FacilitySite fs = new FacilitySite();
		ReleasePointAppt rpa = new ReleasePointAppt();
        ControlPathPollutant cpp = new ControlPathPollutant();
        Pollutant p1 = new Pollutant();
        p1.setPollutantCode("NOX");
        cpp.setPollutant(p1);
        cpp.setPercentReduction(BigDecimal.valueOf(50.0));
		ca.setSequenceNumber(2);
	    ca.setPercentApportionment(BigDecimal.valueOf(100.0));
		fs.getControlPaths().add(testData);
		testData.setFacilitySite(fs);
		testData.getAssignments().add(ca);
		testData.getPollutants().add(cpp);
		testData.getReleasePointAppts().add(rpa);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_NO_CONTROL_DEVICE_ASSIGNMENT.value()) && errorMap.get(ValidationField.CONTROL_PATH_NO_CONTROL_DEVICE_ASSIGNMENT.value()).size() == 1);
    }

    @Test
    public void controlPSStatusFailTest() {
        CefValidatorContext cefContext = createContext();
		ControlPath testData = createBaseControlPath();
        testData.getAssignments().get(0).getControl().getOperatingStatusCode().setCode("PS");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_NO_CONTROL_DEVICE_ASSIGNMENT.value()) && errorMap.get(ValidationField.CONTROL_PATH_NO_CONTROL_DEVICE_ASSIGNMENT.value()).size() == 2);
    }

    @Test
    public void controlTSStatusWarnFailTest() {
        CefValidatorContext cefContext = createContext();
		ControlPath testData = createBaseControlPath();
		testData.getAssignments().get(0).getControl().getOperatingStatusCode().setCode("TS");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_NO_CONTROL_DEVICE_ASSIGNMENT.value()) && errorMap.get(ValidationField.CONTROL_PATH_NO_CONTROL_DEVICE_ASSIGNMENT.value()).size() == 1);
    }

    @Test
    public void releasePointApportionmentAssignmentPassTest() {

        CefValidatorContext cefContext = createContext();
		ControlPath testData = createBaseControlPath();

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void apportionmentTotalPassTest() {

        CefValidatorContext cefContext = createContext();
		ControlPath testData = createBaseControlPath();

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void apportionmentTotalFailTest() {

        CefValidatorContext cefContext = createContext();
		ControlPath testData = createBaseControlPath();

		testData.getAssignments().get(0).setPercentApportionment(BigDecimal.valueOf(99.0));
        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_ASSIGNMENT.value()) && errorMap.get(ValidationField.CONTROL_PATH_ASSIGNMENT.value()).size() == 1);
    }

    @Test
    public void emptyControlPathPassTest() {
        CefValidatorContext cefContext = createContext();
        ControlPath testData = createBaseControlPath();
        boolean hasControlOrChildPath = false;
        for (ControlAssignment ca : testData.getAssignments()) {
          if (ca.getControl() != null || ca.getControlPathChild() != null) {
            hasControlOrChildPath = true;
            break;
          }
        }

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(!testData.getPollutants().isEmpty() || hasControlOrChildPath);
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void emptyControlPathFailTest() {
        CefValidatorContext cefContext = createContext();
        ControlPath testData = createBaseControlPath();
        testData.getPollutants().clear();
        boolean hasControlOrChildPath = false;
        for (ControlAssignment ca : testData.getAssignments()) {
            ca.setControl(null);
            ca.setControlPathChild(null);
        }
        for (ControlAssignment ca : testData.getAssignments()) {
          if (ca.getControl() != null || ca.getControlPathChild() != null) {
            hasControlOrChildPath = true;
            break;
          }
        }
        testData.getAssignments().clear();

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(!hasControlOrChildPath && testData.getPollutants().isEmpty());
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 3);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        // If control path is empty, then the control path pollutant and control device assignment QA checks would also fire
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_POLLUTANT.value()) && errorMap.get(ValidationField.CONTROL_PATH_POLLUTANT.value()).size() == 1);
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_ASSIGNMENT_EMPTY.value()) && errorMap.get(ValidationField.CONTROL_PATH_ASSIGNMENT_EMPTY.value()).size() == 1);
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_NO_CONTROL_DEVICE_ASSIGNMENT.value()) && errorMap.get(ValidationField.CONTROL_PATH_NO_CONTROL_DEVICE_ASSIGNMENT.value()).size() == 1);
    }

    @Test
    public void sequenceNumberNullFailTest() {
        CefValidatorContext cefContext = createContext();
		ControlPath testData = createBaseControlPath();
		testData.getAssignments().get(0).setSequenceNumber(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_ASSIGNMENT.value()) && errorMap.get(ValidationField.CONTROL_PATH_ASSIGNMENT.value()).size() == 1);
    }

    @Test
    public void controlAndControlPathNullFailTest() {
        CefValidatorContext cefContext = createContext();
		ControlPath testData = createBaseControlPath();
		testData.getAssignments().get(0).setControl(null);
		testData.getAssignments().get(0).setControlPathChild(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_ASSIGNMENT.value()) && errorMap.get(ValidationField.CONTROL_PATH_ASSIGNMENT.value()).size() == 1);
    }

    @Test
    public void percentApportionmentRangeFailTest() {
        CefValidatorContext cefContext = createContext();
		ControlPath testData = createBaseControlPath();
		testData.getAssignments().get(0).setPercentApportionment(BigDecimal.valueOf(-0.1));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_ASSIGNMENT.value()) && errorMap.get(ValidationField.CONTROL_PATH_ASSIGNMENT.value()).size() == 2);

        cefContext = createContext();
		testData.getAssignments().get(0).setPercentApportionment(BigDecimal.valueOf(101.0));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_ASSIGNMENT.value()) && errorMap.get(ValidationField.CONTROL_PATH_ASSIGNMENT.value()).size() == 2);
    }

    @Test
    public void percentControlRangeFailTest() {
        CefValidatorContext cefContext = createContext();
		ControlPath testData = createBaseControlPath();
		testData.setPercentControl(BigDecimal.valueOf(0.5));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_PERCENT_CONTROL.value()) && errorMap.get(ValidationField.CONTROL_PATH_PERCENT_CONTROL.value()).size() == 1);

        cefContext = createContext();
		testData.setPercentControl(BigDecimal.valueOf(101.0));

		assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_PERCENT_CONTROL.value()) && errorMap.get(ValidationField.CONTROL_PATH_PERCENT_CONTROL.value()).size() == 1);

    }

    @Test
	public void percentControlPrecisionFailTest() {

		CefValidatorContext cefContext = createContext();
		ControlPath testData = createBaseControlPath();

		testData.setPercentControl(BigDecimal.valueOf(10.568));

		assertFalse(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_PERCENT_CONTROL.value()) && errorMap.get(ValidationField.CONTROL_PATH_PERCENT_CONTROL.value()).size() == 1);
	}

    @Test
	public void controlPollutantPassTest() {
		CefValidatorContext cefContext = createContext();
		ControlPath testData = createBaseControlPath();

		ControlPathPollutant cp1 = new ControlPathPollutant();
		ControlPathPollutant cp2 = new ControlPathPollutant();
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
		ControlPathPollutant cp3 = new ControlPathPollutant();
		ControlPathPollutant cp4 = new ControlPathPollutant();
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
	public void controlPathPollutant_PM25FIL_PM10FIL_FailTest() {
		CefValidatorContext cefContext = createContext();
		ControlPath testData = createBaseControlPath();

		ControlPathPollutant cp1 = new ControlPathPollutant();
		ControlPathPollutant cp2 = new ControlPathPollutant();
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
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_POLLUTANT.value()) && errorMap.get(ValidationField.CONTROL_PATH_POLLUTANT.value()).size() == 1);
    }

    @Test
	public void controlPathPollutant_PM25PRI_PM10PRI_FailTest() {
		CefValidatorContext cefContext = createContext();
		ControlPath testData = createBaseControlPath();

		ControlPathPollutant cp1 = new ControlPathPollutant();
		ControlPathPollutant cp2 = new ControlPathPollutant();
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
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_POLLUTANT.value()) && errorMap.get(ValidationField.CONTROL_PATH_POLLUTANT.value()).size() == 1);
    }

    @Test
	public void controlPathPollutantLegacyTest() {
		CefValidatorContext cefContext = createContext();
		ControlPath testData = createBaseControlPath();

		ControlPathPollutant cp1 = new ControlPathPollutant();
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

		cp1.setControlPath(testData);

		assertFalse(this.pollutantValidator.validate(cefContext, cp1));
		assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

		Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
		assertTrue(errorMap.containsKey(ValidationField.CONTROL_PATH_POLLUTANT.value()) && errorMap.get(ValidationField.CONTROL_PATH_POLLUTANT.value()).size() == 1);
    }


	private ControlPath createBaseControlPath() {

		ControlPath result = new ControlPath();
		result.setId(1L);
		result.setPercentControl(BigDecimal.valueOf(50.0));
		ReleasePointAppt rpa = new ReleasePointAppt();
		ControlMeasureCode cmc = new ControlMeasureCode();
		OperatingStatusCode opStatusCode = new OperatingStatusCode();
        opStatusCode.setCode("OP");
		cmc.setCode("test code");
		cmc.setDescription("test code description");
		ControlPath childPath = new ControlPath();
		childPath.setId(1L);
		childPath.setDescription("test description");
		childPath.setPathId("test path id");
		Control c = new Control();
		c.setDescription("test control");
		c.setId(1234L);
		c.setControlMeasureCode(cmc);
		c.setIdentifier("test control");
		c.setOperatingStatusCode(opStatusCode);
		ControlAssignment ca = new ControlAssignment();
		ca.setControl(c);
		ca.setSequenceNumber(1);
		ca.setPercentApportionment(BigDecimal.valueOf(100.0));
		ca.setId(1234L);
		ca.setControlPath(result);
		ca.setControlPathChild(childPath);
		ControlPathPollutant cpp = new ControlPathPollutant();
		Pollutant p1 = new Pollutant();
        p1.setPollutantCode("NOX");
        cpp.setPollutant(p1);
        cpp.setPercentReduction(BigDecimal.valueOf(50.0));
        result.getPollutants().add(cpp);
		result.setPathId("path 1");
		result.setDescription("test description 1");
		result.getReleasePointAppts().add(rpa);
		FacilitySite fs = new FacilitySite();
		fs.getControlPaths().add(result);
		result.setFacilitySite(fs);
		result.getAssignments().add(ca);

		return result;
	}

}
