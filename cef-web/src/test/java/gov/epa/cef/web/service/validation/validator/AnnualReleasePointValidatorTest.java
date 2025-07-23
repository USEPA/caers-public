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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.*;

import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.EmissionsProcessRepository;
import gov.epa.cef.web.util.ConstantUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.baidu.unbiz.fluentvalidator.ValidationError;

import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.ReleasePointRepository;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.federal.AnnualReleasePointValidator;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AnnualReleasePointValidatorTest extends BaseValidatorTest {

    @InjectMocks
    private AnnualReleasePointValidator validator;

    @Mock
    private EmissionsReportRepository reportRepo;

    @Mock
    private EmissionsProcessRepository epRepo;

    @Mock
    private ReleasePointRepository rpRepo;

    @Before
    public void init(){

    	List<EmissionsReport> erList = new ArrayList<EmissionsReport>();
        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);
        mfr.setCoordinateTolerance(BigDecimal.valueOf(0.0055));
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

        ReleasePoint rp = new ReleasePoint();
        rp.setId(1L);
        rp.setOperatingStatusCode(os);
        rp.setStatusYear((short)2018);
        rp.setReleasePointIdentifier("identifier");

        ReleasePoint rp1 = new ReleasePoint();
        rp1.setId(1L);
        rp1.setOperatingStatusCode(os);
        rp1.setStatusYear((short)2018);
        rp1.setReleasePointIdentifier("test");

        when(reportRepo.findByMasterFacilityRecordId(1L)).thenReturn(erList);
        when(reportRepo.findByMasterFacilityRecordId(2L)).thenReturn(Collections.emptyList());
        when(rpRepo.retrieveByIdentifierFacilityYear(
                "identifier",1L,(short) 2018)).thenReturn(Collections.singletonList(rp));
        when(rpRepo.retrieveByIdentifierFacilityYear(
                "test new",1L,(short) 2018)).thenReturn(Collections.emptyList());
        when(rpRepo.retrieveByIdentifierFacilityYear(
            "test",1L,(short) 2018)).thenReturn(Collections.singletonList(rp1));
        when(epRepo.findByReleasePointApptsReleasePointIdOrderByEmissionsProcessIdentifier(1L)).thenReturn(new ArrayList<>());
    }

    @Test
    public void confirmOnlyNonOperatingEmissionsProcessFail() {
        EmissionsProcess emissionsProcessTS = new EmissionsProcess();
        OperatingStatusCode oscTS = new OperatingStatusCode().withCode(ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN);
        emissionsProcessTS.setOperatingStatusCode(oscTS);

        EmissionsProcess emissionsProcessPS = new EmissionsProcess();
        OperatingStatusCode oscPS = new OperatingStatusCode().withCode(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN);
        emissionsProcessPS.setOperatingStatusCode(oscPS);

        List<EmissionsProcess> eps = Arrays.asList(emissionsProcessPS, emissionsProcessTS);
        when(epRepo.findByReleasePointApptsReleasePointIdOrderByEmissionsProcessIdentifier(1L)).thenReturn(eps);

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null || cefContext.result.getErrors().size() == 1);
        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_PROCESS_STATUS_CODE.value()));
    }

    @Test
    public void confirmMixedOperatingStatusEmissionsProcessPass(){
        EmissionsProcess emissionsProcessTS = new EmissionsProcess();
        OperatingStatusCode oscTS = new OperatingStatusCode().withCode(ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN);
        emissionsProcessTS.setOperatingStatusCode(oscTS);

        EmissionsProcess emissionsProcessOP = new EmissionsProcess();
        OperatingStatusCode oscOP = new OperatingStatusCode().withCode(ConstantUtils.STATUS_OPERATING);
        emissionsProcessOP.setOperatingStatusCode(oscOP);

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        List<EmissionsProcess> eps = Arrays.asList(emissionsProcessOP, emissionsProcessTS);
        when(epRepo.findByReleasePointApptsReleasePointIdOrderByEmissionsProcessIdentifier(1L)).thenReturn(eps);
        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void simpleValidatePassTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void exitGasTemperatureRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        testData.setExitGasTemperature(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        //Verify QA checks are Run correctly when the Release Point is Operating
        cefContext = createContext();
        testData.setExitGasTemperature((short) -31);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_GAS_TEMP.value()) && errorMap.get(ValidationField.RP_GAS_TEMP.value()).size() == 1);

        cefContext = createContext();
        testData.setExitGasTemperature((short) 4001);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_GAS_TEMP.value()) && errorMap.get(ValidationField.RP_GAS_TEMP.value()).size() == 1);

        //Verify QA Checks are NOT run when RP is NOT Operating
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);

        cefContext = createContext();
        testData.setExitGasTemperature((short) -31);

        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        cefContext = createContext();
        testData.setExitGasTemperature((short) 4001);

        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

	@Test
	public void exitGasTemperatureRangeTest() {

		CefValidatorContext cefContext = createContext();
		ReleasePoint testData = createBaseReleasePoint();
		testData.setExitGasTemperature((short) -30);

		assertTrue(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

		cefContext = createContext();
		testData.setExitGasTemperature((short) 4000);

		assertTrue(this.validator.validate(cefContext, testData));
		assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void exitGasFlowRateOrVelocityRequiredFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();
        testData.setExitGasFlowRate(null);
        testData.setExitGasFlowUomCode(null);
        testData.setExitGasVelocity(null);
        testData.setExitGasVelocityUomCode(null);
        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_GAS_RELEASE.value()) && errorMap.get(ValidationField.RP_GAS_RELEASE.value()).size() == 1);

        // cefContext = createContext();
        // testData = createBaseReleasePoint();
        // testData.setExitGasVelocity(null);
        // testData.setExitGasVelocityUomCode(null);

        // assertFalse(this.validator.validate(cefContext, testData));
        // assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        // errorMap = mapErrors(cefContext.result.getErrors());
        // assertTrue(errorMap.containsKey(ValidationField.RP_GAS_VELOCITY.value()) && errorMap.get(ValidationField.RP_GAS_VELOCITY.value()).size() == 1);

        //Verify QA Checks are NOT run when RP is NOT Operating
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);

        cefContext = createContext();

        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void exitGasFlowRateRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();
        testData.setExitGasFlowRate(BigDecimal.ZERO);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_GAS_FLOW.value()) && errorMap.get(ValidationField.RP_GAS_FLOW.value()).size() == 1);

        cefContext = createContext();
        ReleasePointTypeCode releasePointTypeCode = new ReleasePointTypeCode();
        releasePointTypeCode.setCode("1");
        releasePointTypeCode.setCategory("Fugitive");
        releasePointTypeCode.setDescription("Fugitive Area");
        testData.setTypeCode(releasePointTypeCode);
        testData.setExitGasFlowRate(BigDecimal.valueOf(200000.1));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_GAS_FLOW.value()) && errorMap.get(ValidationField.RP_GAS_FLOW.value()).size() == 1);

        cefContext = createContext();
        UnitMeasureCode flowUom = new UnitMeasureCode();
        flowUom.setCode("ACFM");
        UnitMeasureCode velocityUom = new UnitMeasureCode();
        velocityUom.setCode("FPM");
        testData.setExitGasFlowUomCode(flowUom);
        testData.setExitGasVelocityUomCode(velocityUom);
        testData.setExitGasFlowRate(BigDecimal.valueOf(12000000.1));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_GAS_FLOW.value()) && errorMap.get(ValidationField.RP_GAS_FLOW.value()).size() == 1);

        //Verify QA Checks are NOT run when RP is NOT Operating
        testData = createBaseReleasePoint();
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        cefContext = createContext();
        releasePointTypeCode.setCode("1");
        releasePointTypeCode.setCategory("Fugitive");
        releasePointTypeCode.setDescription("Fugitive Area");
        testData.setTypeCode(releasePointTypeCode);
        testData.setExitGasFlowRate(BigDecimal.valueOf(200000.1));

        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        cefContext = createContext();
        flowUom = new UnitMeasureCode();
        flowUom.setCode("ACFM");
        velocityUom = new UnitMeasureCode();
        velocityUom.setCode("FPM");
        testData.setExitGasFlowUomCode(flowUom);
        testData.setExitGasVelocityUomCode(velocityUom);
        testData.setExitGasFlowRate(BigDecimal.valueOf(12000000.1));

        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

    }

    @Test
    public void exitGasFlowRateAndUomRequiredFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();
        testData.setExitGasFlowRate(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_GAS_FLOW.value()) && errorMap.get(ValidationField.RP_GAS_FLOW.value()).size() == 1);

        cefContext = createContext();
        testData.setExitGasFlowUomCode(null);
        testData.setExitGasFlowRate(BigDecimal.valueOf(0.002));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_GAS_FLOW.value()) && errorMap.get(ValidationField.RP_GAS_FLOW.value()).size() == 1);

        //Verify QA Checks are NOT run when RP is NOT Operating
        testData = createBaseReleasePoint();
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);

        testData.setExitGasFlowRate(null);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        cefContext = createContext();
        testData.setExitGasFlowUomCode(null);
        testData.setExitGasFlowRate(BigDecimal.valueOf(0.002));
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

    }

    @Test
    public void exitGasVelocityRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();
        testData.setExitGasVelocity(BigDecimal.ZERO);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_GAS_VELOCITY.value()) && errorMap.get(ValidationField.RP_GAS_VELOCITY.value()).size() == 1);

        cefContext = createContext();
        ReleasePointTypeCode releasePointTypeCode = new ReleasePointTypeCode();
        releasePointTypeCode.setCode("1");
        releasePointTypeCode.setCategory("Fugitive");
        releasePointTypeCode.setDescription("Fugitive Area");
        testData.setTypeCode(releasePointTypeCode);
        testData.setExitGasVelocity(BigDecimal.valueOf(1500.1));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_GAS_VELOCITY.value()) && errorMap.get(ValidationField.RP_GAS_VELOCITY.value()).size() == 1);

        cefContext = createContext();
        UnitMeasureCode flowUom = new UnitMeasureCode();
        flowUom.setCode("ACFM");
        UnitMeasureCode velocityUom = new UnitMeasureCode();
        velocityUom.setCode("FPM");
        testData.setExitGasFlowUomCode(flowUom);
        testData.setExitGasVelocityUomCode(velocityUom);
        testData.setExitGasVelocity(BigDecimal.valueOf(90000.1));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_GAS_VELOCITY.value()) && errorMap.get(ValidationField.RP_GAS_VELOCITY.value()).size() == 1);

        //Verify QA Checks are NOT run when RP is NOT Operating
        testData = createBaseReleasePoint();
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("ONRE");
        testData.setOperatingStatusCode(opStatCode);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        cefContext = createContext();
        releasePointTypeCode = new ReleasePointTypeCode();
        releasePointTypeCode.setCode("1");
        releasePointTypeCode.setCategory("Fugitive");
        releasePointTypeCode.setDescription("Fugitive Area");
        testData.setTypeCode(releasePointTypeCode);
        testData.setExitGasVelocity(BigDecimal.valueOf(1500.1));

        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        cefContext = createContext();
        flowUom = new UnitMeasureCode();
        flowUom.setCode("ACFM");
        velocityUom = new UnitMeasureCode();
        velocityUom.setCode("FPM");
        testData.setExitGasFlowUomCode(flowUom);
        testData.setExitGasVelocityUomCode(velocityUom);
        testData.setExitGasVelocity(BigDecimal.valueOf(90000.1));

        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        cefContext = createContext();
        testData = createBaseReleasePoint();
        flowUom = new UnitMeasureCode();
        flowUom.setCode("ACFM");
        velocityUom = new UnitMeasureCode();
        velocityUom.setCode("FPM");
        testData.setExitGasFlowUomCode(flowUom);


        releasePointTypeCode = new ReleasePointTypeCode();
        releasePointTypeCode.setCode("6");
        releasePointTypeCode.setCategory("Stack");
        releasePointTypeCode.setDescription("Downward Facing Vent");
        testData.setTypeCode(releasePointTypeCode);
        testData.setExitGasVelocity(null);
        testData.setStackDiameter(null);
        testData.setExitGasFlowRate(BigDecimal.valueOf(9.0));
        testData.setStackLength(BigDecimal.valueOf(23.0));
        testData.setStackWidth(BigDecimal.valueOf(8.5));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(errorMap.containsKey(ValidationField.RP_GAS_VELOCITY.value()) && errorMap.get(ValidationField.RP_GAS_VELOCITY.value()).size() == 1);


    }

    @Test
    public void exitGasVelocityAndUomRequiredFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();
        testData.setExitGasVelocity(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_GAS_VELOCITY.value()) && errorMap.get(ValidationField.RP_GAS_VELOCITY.value()).size() == 1);

        cefContext = createContext();
        testData.setExitGasVelocityUomCode(null);
        testData.setExitGasVelocity(BigDecimal.valueOf(1000.0));
        testData.setExitGasFlowRate(null);
        testData.setExitGasFlowUomCode(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_GAS_VELOCITY.value()) && errorMap.get(ValidationField.RP_GAS_VELOCITY.value()).size() == 1);

        //Verify QA Checks are NOT run when RP is NOT Operating
        testData = createBaseReleasePoint();
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("I");
        testData.setOperatingStatusCode(opStatCode);
        testData.setExitGasVelocity(null);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        cefContext = createContext();
        testData.setExitGasVelocityUomCode(null);
        testData.setExitGasVelocity(BigDecimal.valueOf(1000.0));
        testData.setExitGasFlowRate(null);
        testData.setExitGasFlowUomCode(null);

        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void exitGasFlowRateUomAndVelocityUomTest() {

    	CefValidatorContext cefContext = createContext();
    	ReleasePoint testData = createBaseReleasePoint();

    	assertTrue(this.validator.validate(cefContext, testData));
    	assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void exitGasVelocityUomFailTest() {

    	CefValidatorContext cefContext = createContext();
    	ReleasePoint testData = createBaseReleasePoint();

    	UnitMeasureCode uom = new UnitMeasureCode();
    	uom.setCode("BTU");

    	testData.setExitGasVelocityUomCode(uom);
    	testData.setExitGasVelocity(BigDecimal.valueOf(0.06));
    	testData.setExitGasFlowRate(null);
    	testData.setExitGasFlowUomCode(null);

    	assertFalse(this.validator.validate(cefContext, testData));
    	assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

    	Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
    	assertTrue(errorMap.containsKey(ValidationField.RP_GAS_VELOCITY.value()) && errorMap.get(ValidationField.RP_GAS_VELOCITY.value()).size() == 1);

        //Verify QA Checks are NOT run when RP is NOT Operating
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

    }

    @Test
    public void exitGasFlowRateUomFailTest() {

    	CefValidatorContext cefContext = createContext();
    	ReleasePoint testData = createBaseReleasePoint();

    	UnitMeasureCode uom = new UnitMeasureCode();
    	uom.setCode("BTU");

    	testData.setExitGasFlowUomCode(uom);
    	testData.setExitGasVelocity(null);
    	testData.setExitGasVelocityUomCode(null);
    	testData.setStackDiameter(BigDecimal.valueOf(0.1));

    	assertFalse(this.validator.validate(cefContext, testData));
    	assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

    	Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
    	assertTrue(errorMap.containsKey(ValidationField.RP_GAS_FLOW.value()) && errorMap.get(ValidationField.RP_GAS_FLOW.value()).size() == 1);

        //Verify QA Checks are NOT run when RP is NOT Operating
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void fenceLineDistanceRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        cefContext = createContext();
        testData.setFenceLineDistance((long) -1);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_FENCELINE.value()) && errorMap.get(ValidationField.RP_FENCELINE.value()).size() == 1);

        cefContext = createContext();
        testData.setFenceLineDistance((long) 100000);
        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        // Verify QA Checks are NOT run when RP is NOT Operating
        // Operating status code of PS will generate a warning to inform users component will not be copied forward
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("PS");
        testData.setOperatingStatusCode(opStatCode);
        testData.setFenceLineDistance((long) -1);

        cefContext = createContext();
        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        testData.setFenceLineDistance((long) 100000);

        cefContext = createContext();
        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
    }

    @Test
    public void fugitiveHeightRequiredPassTest() {

    	CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseFugitiveReleasePoint();

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        ReleasePointTypeCode releasePointTypeCode = new ReleasePointTypeCode();
        releasePointTypeCode.setCode("9");
        releasePointTypeCode.setCategory("Fugitive");
        releasePointTypeCode.setDescription("Fugitive 2-D – source area");
        UnitMeasureCode uomFT = new UnitMeasureCode();
        uomFT.setCode("FT");

        testData.setTypeCode(releasePointTypeCode);
        testData.setFugitiveMidPt2Latitude(BigDecimal.valueOf(33.949));
        testData.setFugitiveMidPt2Longitude(BigDecimal.valueOf(-84.388000));
        testData.setFugitiveHeight((long)10);
        testData.setFugitiveHeightUomCode(uomFT);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void fugitiveHeightRequiredFailTest() {

    	CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseFugitiveReleasePoint();

        ReleasePointTypeCode releasePointTypeCode = new ReleasePointTypeCode();
        releasePointTypeCode.setCode("9");
        releasePointTypeCode.setCategory("Fugitive");
        releasePointTypeCode.setDescription("Fugitive 2-D – source area");
        UnitMeasureCode uomFT = new UnitMeasureCode();
        uomFT.setCode("FT");

        testData.setTypeCode(releasePointTypeCode);
        testData.setFugitiveMidPt2Latitude(BigDecimal.valueOf(33.949));
        testData.setFugitiveMidPt2Longitude(BigDecimal.valueOf(-84.388000));
        testData.setFugitiveHeight(null);
        testData.setFugitiveHeightUomCode(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_FUGITIVE.value()) && errorMap.get(ValidationField.RP_FUGITIVE.value()).size() == 1);
    }

    @Test
    public void fugitiveHeightRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseFugitiveReleasePoint();

        cefContext = createContext();
        testData.setFugitiveHeight((long) -1);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_FUGITIVE.value()) && errorMap.get(ValidationField.RP_FUGITIVE.value()).size() == 1);

        cefContext = createContext();
        testData.setFugitiveHeight((long) 600);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        //Verify QA Checks are NOT run when RP is NOT Operating
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("ONP");
        testData.setOperatingStatusCode(opStatCode);
        testData.setFugitiveHeight((long) -1);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        testData.setFugitiveHeight((long) 600);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void fugitiveLengthRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseFugitiveReleasePoint();

        cefContext = createContext();
        testData.setFugitiveLength(BigDecimal.ZERO);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_FUGITIVE.value()) && errorMap.get(ValidationField.RP_FUGITIVE.value()).size() == 1);

        cefContext = createContext();
        testData.setFugitiveLength(BigDecimal.valueOf(20000));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        //Verify QA Checks are NOT run when RP is NOT Operating
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);
        testData.setFugitiveLength(BigDecimal.ZERO);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        testData.setFugitiveLength(BigDecimal.valueOf(20000));

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void fugitiveWidthRequiredPassTest() {

    	CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseFugitiveReleasePoint();

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        ReleasePointTypeCode releasePointTypeCode = new ReleasePointTypeCode();
        releasePointTypeCode.setCode("9");
        releasePointTypeCode.setCategory("Fugitive");
        releasePointTypeCode.setDescription("Fugitive 2-D – source area");
        UnitMeasureCode uomFT = new UnitMeasureCode();
        uomFT.setCode("FT");

        testData.setTypeCode(releasePointTypeCode);
        testData.setFugitiveMidPt2Latitude(BigDecimal.valueOf(33.949));
        testData.setFugitiveMidPt2Longitude(BigDecimal.valueOf(-84.388000));
        testData.setFugitiveWidth(BigDecimal.valueOf(20));
        testData.setFugitiveWidthUomCode(uomFT);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void fugitiveWidthRequiredFailTest() {

    	CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseFugitiveReleasePoint();

        ReleasePointTypeCode releasePointTypeCode = new ReleasePointTypeCode();
        releasePointTypeCode.setCode("9");
        releasePointTypeCode.setCategory("Fugitive");
        releasePointTypeCode.setDescription("Fugitive 2-D – source area");
        UnitMeasureCode uomFT = new UnitMeasureCode();
        uomFT.setCode("FT");

        testData.setTypeCode(releasePointTypeCode);
        testData.setFugitiveMidPt2Latitude(BigDecimal.valueOf(33.949));
        testData.setFugitiveMidPt2Longitude(BigDecimal.valueOf(-84.388000));
        testData.setFugitiveWidth(null);
        testData.setFugitiveWidthUomCode(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_FUGITIVE.value()) && errorMap.get(ValidationField.RP_FUGITIVE.value()).size() == 1);
    }

    @Test
    public void fugitiveWidthRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseFugitiveReleasePoint();

        cefContext = createContext();
        testData.setFugitiveWidth(BigDecimal.ZERO);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_FUGITIVE.value()) && errorMap.get(ValidationField.RP_FUGITIVE.value()).size() == 1);

        cefContext = createContext();
        testData.setFugitiveWidth(BigDecimal.valueOf(20000));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        // Verify QA Checks are NOT run when RP is NOT Operating
        // Operating status code of PS will generate a warning to inform users component will not be copied forward
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("PS");
        testData.setOperatingStatusCode(opStatCode);
        testData.setFugitiveWidth(BigDecimal.ZERO);

        cefContext = createContext();
        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        testData.setFugitiveWidth(BigDecimal.valueOf(20000));

        cefContext = createContext();
        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
    }

    @Test
    public void fugitiveAngleRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseFugitiveReleasePoint();

        cefContext = createContext();
        testData.setFugitiveAngle((long) -1);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_FUGITIVE.value()) && errorMap.get(ValidationField.RP_FUGITIVE.value()).size() == 1);

        cefContext = createContext();
        testData.setFugitiveAngle((long) 90);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        //Verify QA Checks are NOT run when RP is NOT Operating
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("I");
        testData.setOperatingStatusCode(opStatCode);
        testData.setFugitiveAngle((long) -1);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        testData.setFugitiveAngle((long) 90);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void stackHeightRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        cefContext = createContext();
        testData.setStackHeight(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_STACK.value()) && errorMap.get(ValidationField.RP_STACK.value()).size() == 1);

        cefContext = createContext();
        testData.setStackHeight(BigDecimal.valueOf(0.5));
        testData.setStackDiameter(BigDecimal.valueOf(0.001));
        testData.setExitGasFlowRate(BigDecimal.valueOf(0.00000001));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        cefContext = createContext();
        testData.setStackHeight(BigDecimal.valueOf(1400.0));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        //Verify QA Checks are NOT run when RP is NOT Operating
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);
        testData.setStackHeight(BigDecimal.valueOf(0.5));

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        testData.setStackHeight(BigDecimal.valueOf(1400.0));

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void stackDiameterRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        cefContext = createContext();
        testData.setStackDiameter(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_STACK.value()) && errorMap.get(ValidationField.RP_STACK.value()).size() == 1);

        cefContext = createContext();
        testData.setStackDiameter(BigDecimal.ZERO);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        cefContext = createContext();
        testData.setStackDiameter(BigDecimal.valueOf(400.0));
        testData.setStackHeight(BigDecimal.valueOf(1300.0));
        testData.setExitGasVelocity(BigDecimal.valueOf(0.0025));
        testData.setExitGasFlowRate(BigDecimal.valueOf(300.0));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        //Verify QA Checks are NOT run when RP is NOT Operating
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);
        testData.setStackDiameter(null);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        testData.setStackDiameter(BigDecimal.ZERO);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        testData.setStackDiameter(BigDecimal.valueOf(400.0));
        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void uomFeetFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseFugitiveReleasePoint();

        UnitMeasureCode uomFT = new UnitMeasureCode();
        uomFT.setCode("FT");
        UnitMeasureCode uomM = new UnitMeasureCode();
        uomM.setCode("M");

        cefContext = createContext();
        testData.setFenceLineUomCode(uomM);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_UOM_FT.value()) && errorMap.get(ValidationField.RP_UOM_FT.value()).size() == 1);

        cefContext = createContext();
        testData.setFenceLineUomCode(uomFT);
        testData.setFugitiveLengthUomCode(uomM);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_UOM_FT.value()) && errorMap.get(ValidationField.RP_UOM_FT.value()).size() == 1);

        cefContext = createContext();
        testData.setFugitiveLengthUomCode(uomFT);
        testData.setFugitiveWidthUomCode(uomM);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        cefContext = createContext();
        testData.setFugitiveWidthUomCode(uomFT);
        testData.setFugitiveHeightUomCode(uomM);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        cefContext = createContext();
        testData = createBaseReleasePoint();
        testData.setStackHeightUomCode(uomM);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_UOM_FT.value()) && errorMap.get(ValidationField.RP_UOM_FT.value()).size() == 1);

        cefContext = createContext();
        testData.setStackHeightUomCode(uomFT);
        testData.setStackDiameterUomCode(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_UOM_FT.value()) && errorMap.get(ValidationField.RP_UOM_FT.value()).size() == 1);

        cefContext = createContext();
        testData.setStackDiameter(null);
        testData.setExitGasFlowRate(BigDecimal.valueOf(2.0));
        testData.setStackLength(BigDecimal.valueOf(23.0));
        testData.setStackWidth(BigDecimal.valueOf(8.5));
        testData.setStackLengthUomCode(uomFT);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_UOM_FT.value()) && errorMap.get(ValidationField.RP_UOM_FT.value()).size() == 1);

        cefContext = createContext();
        testData.setStackLengthUomCode(null);
        testData.setStackWidthUomCode(uomFT);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_UOM_FT.value()) && errorMap.get(ValidationField.RP_UOM_FT.value()).size() == 1);

        // Verify QA Checks are NOT run when RP is NOT Operating
        // Operating status code of PS will generate a warning to inform users component will not be copied forward
        testData = createBaseFugitiveReleasePoint();
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("PS");
        testData.setOperatingStatusCode(opStatCode);

        testData.setFenceLineUomCode(uomM);

        cefContext = createContext();
        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        testData.setStackDiameter(BigDecimal.ZERO);

        cefContext = createContext();
        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        cefContext = createContext();
        testData.setFenceLineUomCode(uomFT);
        testData.setFugitiveLengthUomCode(uomM);
        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        cefContext = createContext();
        testData.setFugitiveLengthUomCode(uomFT);
        testData.setFugitiveWidthUomCode(uomM);
        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
    }

    @Test
    public void stackDiameterCompareHeightFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        cefContext = createContext();
        testData.setStackDiameter(BigDecimal.valueOf(10.0));
        testData.setStackHeight(BigDecimal.valueOf(5.0));
        testData.setExitGasVelocity(BigDecimal.valueOf(2.0));
        testData.setExitGasFlowRate(BigDecimal.valueOf(150.0));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_STACK_WARNING.value()) && errorMap.get(ValidationField.RP_STACK_WARNING.value()).size() == 1);

        //Verify QA Checks are NOT run when RP is NOT Operating
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void coordinateToleranceFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        cefContext = createContext();
        testData.setLatitude(BigDecimal.valueOf(32.959000));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_COORDINATE.value()) && errorMap.get(ValidationField.RP_COORDINATE.value()).size() == 1);

        cefContext = createContext();
        testData = createBaseFugitiveReleasePoint();
        testData.setLongitude(BigDecimal.valueOf(-84.888000));
        testData.setLatitude(BigDecimal.valueOf(33.951000));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        cefContext = createContext();
        testData = createBaseFugitiveReleasePoint();
        ReleasePointTypeCode releasePointTypeCode = new ReleasePointTypeCode();
    	releasePointTypeCode.setCode("9");
    	releasePointTypeCode.setCategory("Fugitive");
    	releasePointTypeCode.setDescription("Fugitive 2-D – source area");
    	UnitMeasureCode uomFT = new UnitMeasureCode();
        uomFT.setCode("FT");

    	testData.setTypeCode(releasePointTypeCode);
        testData.setLongitude(BigDecimal.valueOf(-84.888000));
        testData.setFugitiveMidPt2Latitude(BigDecimal.valueOf(33.959));
        testData.setFugitiveMidPt2Longitude(BigDecimal.valueOf(-84.388000));
        testData.setFugitiveHeight((long)5);
        testData.setFugitiveHeightUomCode(uomFT);
        testData.setFugitiveWidth(BigDecimal.valueOf(20));
        testData.setFugitiveWidthUomCode(uomFT);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_COORDINATE.value()) && errorMap.get(ValidationField.RP_COORDINATE.value()).size() == 2);

        //Verify QA Checks are NOT run when RP is NOT Operating
        testData = createBaseFugitiveReleasePoint();
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);

        cefContext = createContext();
        testData.setLatitude(BigDecimal.valueOf(32.959000));

        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        cefContext = createContext();
        testData.setLongitude(BigDecimal.valueOf(-84.888000));
        testData.setLatitude(BigDecimal.valueOf(33.951000));
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        cefContext = createContext();
        testData.setLongitude(BigDecimal.valueOf(-84.888000));

        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void uniqueIdentifierCheckFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();
        ReleasePoint rp1 = new ReleasePoint();
        rp1.setReleasePointIdentifier("test");
        rp1.setId(2L);
        testData.setReleasePointIdentifier("test");
        testData.getFacilitySite().getReleasePoints().add(rp1);
        testData.getFacilitySite().getReleasePoints().add(testData);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_IDENTIFIER.value()) && errorMap.get(ValidationField.RP_IDENTIFIER.value()).size() == 1);
    }

    @Test
    public void exitVelocityCalcCheckFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        cefContext = createContext();
        testData.setExitGasVelocity(BigDecimal.valueOf(250.0));
        testData.setExitGasFlowRate(BigDecimal.valueOf(0.00001));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_GAS_VELOCITY.value()) && errorMap.get(ValidationField.RP_GAS_VELOCITY.value()).size() == 1);

        //Verify QA Checks are NOT run when RP is NOT Operating
        testData = createBaseReleasePoint();
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void exitVelocityCalcCheckPassTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        cefContext = createContext();
        testData.setExitGasVelocity(BigDecimal.valueOf(250.0));
        testData.setExitGasFlowRate(BigDecimal.valueOf(200.0));
        testData.setStackDiameter(BigDecimal.ONE);
        testData.setStackHeight(BigDecimal.valueOf(5.0));

        assertTrue(this.validator.validate(cefContext, testData));
      	assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void exitFlowRateCalcCheckFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        cefContext = createContext();
        testData.setExitGasVelocity(BigDecimal.valueOf(100.0));
        testData.setStackDiameter(BigDecimal.ONE);
        testData.setStackHeight(BigDecimal.valueOf(5.0));
        testData.setExitGasFlowRate(BigDecimal.valueOf(38.0));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_GAS_FLOW.value()) && errorMap.get(ValidationField.RP_GAS_FLOW.value()).size() == 1);

        //Verify QA Checks are NOT run when RP is NOT Operating
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void exitFlowRateCalcCheckPassTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        cefContext = createContext();
        testData.setExitGasVelocity(BigDecimal.valueOf(0.001));
        testData.setStackDiameter(BigDecimal.valueOf(0.001));
        testData.setStackHeight(BigDecimal.valueOf(5.0));
        testData.setExitGasFlowRate(BigDecimal.valueOf(0.00000001));

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        testData.setExitGasVelocity(BigDecimal.valueOf(1500.0));
        testData.setStackDiameter(BigDecimal.valueOf(13.2));
        testData.setStackHeight(BigDecimal.valueOf(15.0));
        testData.setExitGasFlowRate(BigDecimal.valueOf(200000.0));

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void releasePointNoDiameterOrLengthWidthFailTest() {
        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        testData.setStackDiameter(null);
        testData.setStackWidth(null);
        testData.setStackLength(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_STACK.value()) && errorMap.get(ValidationField.RP_STACK.value()).size() == 1);

    }

    @Test
    public void releasePointNoDiameterButLengthWidthPassTest() {
        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        UnitMeasureCode ftUom = new UnitMeasureCode();
        ftUom.setCode("FT");

        testData.setStackDiameter(null);
        testData.setExitGasFlowRate(BigDecimal.valueOf(2.0));
        testData.setStackLength(BigDecimal.valueOf(23.0));
        testData.setStackLengthUomCode(ftUom);
        testData.setStackWidth(BigDecimal.valueOf(8.5));
        testData.setStackWidthUomCode(ftUom);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void releasePointDiameterAndLengthWidthFailTest() {
        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        UnitMeasureCode ftUom = new UnitMeasureCode();
        ftUom.setCode("FT");

        testData.setStackDiameter(BigDecimal.valueOf(0.5));
        testData.setStackLength(BigDecimal.valueOf(1.0));
        testData.setStackLengthUomCode(ftUom);
        testData.setStackWidth(BigDecimal.valueOf(1.96));
        testData.setStackWidthUomCode(ftUom);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_STACK.value()) && errorMap.get(ValidationField.RP_STACK.value()).size() == 1);
    }

    @Test
    public void simpleValidateOperationStatusYearTypeFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        OperatingStatusCode opStatusCode = new OperatingStatusCode();
        opStatusCode.setCode("TS");
        testData.setOperatingStatusCode(opStatusCode);
        testData.setStatusYear(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_STATUS_CODE.value()) && errorMap.get(ValidationField.RP_STATUS_CODE.value()).size() == 1);
    }

    @Test
    public void simpleValidateStatusYearRangeTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        testData.setStatusYear((short) 1900);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        testData.setStatusYear((short) 2019);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void simpleValidateStatusYearRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        testData.setStatusYear((short) 1800);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_STATUS_YEAR.value()) && errorMap.get(ValidationField.RP_STATUS_YEAR.value()).size() == 1);

        cefContext = createContext();
        testData.setStatusYear((short) 2020);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_STATUS_YEAR.value()) && errorMap.get(ValidationField.RP_STATUS_YEAR.value()).size() == 1);
    }

    @Test
    public void releasePointTypeCodeLegacyFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();
        testData.getTypeCode().setCode("99");
        testData.getTypeCode().setLastInventoryYear(2002);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_TYPE_CODE.value()) && errorMap.get(ValidationField.RP_TYPE_CODE.value()).size() == 1);

        //Verify QA Checks are NOT run when RP is NOT Operating
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void releasePointLatLongWarningPassTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();
        testData.setLatitude(null);
        testData.setLongitude(null);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        testData = createBaseFugitiveReleasePoint();
        ReleasePointTypeCode releasePointTypeCode = new ReleasePointTypeCode();
    	releasePointTypeCode.setCode("9");
    	releasePointTypeCode.setCategory("Fugitive");
    	releasePointTypeCode.setDescription("Fugitive 2-D – source area");
        testData.setLatitude(null);
        testData.setLongitude(null);
        testData.setFugitiveMidPt2Latitude(null);
        testData.setFugitiveMidPt2Longitude(null);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void releasePointLatLongWarningFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();
        testData.setLatitude(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_COORDINATE.value()) && errorMap.get(ValidationField.RP_COORDINATE.value()).size() == 1);

        cefContext = createContext();
        testData = createBaseFugitiveReleasePoint();
        ReleasePointTypeCode releasePointTypeCode = new ReleasePointTypeCode();
    	releasePointTypeCode.setCode("9");
    	releasePointTypeCode.setCategory("Fugitive");
    	releasePointTypeCode.setDescription("Fugitive 2-D – source area");
        testData.setLatitude(null);
        testData.setFugitiveMidPt2Latitude(BigDecimal.valueOf(30.388000));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_COORDINATE.value()) && errorMap.get(ValidationField.RP_COORDINATE.value()).size() == 2);


        //Verify QA Checks are NOT run when RP is NOT Operating
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void operationStatusPSFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        OperatingStatusCode opStatusCode = new OperatingStatusCode();
        opStatusCode.setCode("PS");
        testData.setOperatingStatusCode(opStatusCode);
        testData.setStatusYear((short) 2019);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_STATUS_CODE.value()) && errorMap.get(ValidationField.RP_STATUS_CODE.value()).size() == 1);
    }

    @Test
    public void operationStatusPSPreviousYearOpCurrentYearFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

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
        assertTrue(errorMap.containsKey(ValidationField.RP_STATUS_CODE.value()) && errorMap.get(ValidationField.RP_STATUS_CODE.value()).size() == 1);
    }

    @Test
    public void newRpShutdownPassTest() {
        // pass when previous exists and current op status is OP
        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();
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

        // pass when previous report doesn't exist and current op status is OP
        cefContext = createContext();
        testData.getOperatingStatusCode().setCode("OP");
        testData.getFacilitySite().getEmissionsReport().getMasterFacilityRecord().setId(2L);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void newRpShutdownPassFail() {
        // fail when previous report exists, but rp doesn't and current op status is TS
        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);
        testData.setReleasePointIdentifier("test new");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_STATUS_CODE.value()) && errorMap.get(ValidationField.RP_STATUS_CODE.value()).size() == 1);

        // fail when previous report doesn't exist and current op status is TS
        cefContext = createContext();
        testData.getFacilitySite().getEmissionsReport().getMasterFacilityRecord().setId(2L);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP_STATUS_CODE.value()) && errorMap.get(ValidationField.RP_STATUS_CODE.value()).size() == 1);
    }

    @Test
    public void releasePointUnassociatedFailTest() {

        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        testData.getReleasePointAppts().clear();

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP.value()) && errorMap.get(ValidationField.RP.value()).size() == 1);
    }

    @Test
    public void releasePointAddedToFacilityTest() {
        // Fails when a release point was added to the current year report compared to the previous year report
        CefValidatorContext cefContext = createContext();
        ReleasePoint testData = createBaseReleasePoint();

        testData.setReleasePointIdentifier("new identifier for testing");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.RP.value()) && errorMap.get(ValidationField.RP.value()).size() == 1);
    }

    private ReleasePoint createBaseReleasePoint() {

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setEisProgramId("111111");
        mfr.setId(1L);
        mfr.setCoordinateTolerance(BigDecimal.valueOf(0.003));

    	EmissionsReport er = new EmissionsReport();
    	er.setYear((short)2019);
    	er.setMasterFacilityRecord(mfr);

        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("OP");

        FacilitySite facility = new FacilitySite();
        facility.setId(1L);
        facility.setLatitude(BigDecimal.valueOf(33.949000));
        facility.setLongitude(BigDecimal.valueOf(-84.388000));
        facility.setOperatingStatusCode(opStatCode);
        facility.setEmissionsReport(er);
        er.getFacilitySites().add(facility);

        ReleasePointTypeCode releasePointTypeCode = new ReleasePointTypeCode();
        releasePointTypeCode.setCode("2");
        releasePointTypeCode.setCategory("Stack");
        releasePointTypeCode.setDescription("Vertical");

        UnitMeasureCode flowUom = new UnitMeasureCode();
        flowUom.setCode("ACFS");

        UnitMeasureCode velUom = new UnitMeasureCode();
        velUom.setCode("FPS");

        UnitMeasureCode distUom = new UnitMeasureCode();
        distUom.setCode("FT");

        ReleasePoint result = new ReleasePoint();
        result.setReleasePointIdentifier("identifier");
        result.setExitGasTemperature((short) 50);
        result.setExitGasFlowRate(BigDecimal.valueOf(0.002));
        result.setExitGasFlowUomCode(flowUom);
        result.setExitGasVelocity(BigDecimal.valueOf(0.01));
        result.setExitGasVelocityUomCode(velUom);
        result.setFenceLineDistance((long) 1);
        result.setFenceLineUomCode(distUom);
        result.setStackHeightUomCode(distUom);
        result.setStackDiameterUomCode(distUom);
        result.setStackHeight(BigDecimal.ONE);
        result.setStackDiameter(BigDecimal.valueOf(0.5));
        result.setTypeCode(releasePointTypeCode);
        result.setLatitude(BigDecimal.valueOf(33.949000));
        result.setLongitude(BigDecimal.valueOf(-84.388000));
        result.setFacilitySite(facility);
        result.setStatusYear((short) 2000);
        result.setOperatingStatusCode(opStatCode);
        result.setId(1L);

        List<ReleasePointAppt> rpaList = new ArrayList<>();
        ReleasePointAppt rpa = new ReleasePointAppt();
        rpa.setReleasePoint(result);
        rpaList.add(rpa);

        result.setReleasePointAppts(rpaList);

        return result;
    }

    private ReleasePoint createBaseFugitiveReleasePoint() {

    	OperatingStatusCode opStatCode = new OperatingStatusCode();
    	opStatCode.setCode("OP");

    	MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setEisProgramId("536411");
        mfr.setId(1L);
        mfr.setCoordinateTolerance(BigDecimal.valueOf(0.0055));

        EmissionsReport er = new EmissionsReport();
        er.setYear((short)2019);
        er.setMasterFacilityRecord(mfr);

    	FacilitySite facility = new FacilitySite();
    	facility.setId(1L);
    	facility.setLatitude(BigDecimal.valueOf(33.949000));
    	facility.setLongitude(BigDecimal.valueOf(-84.388000));
    	facility.setOperatingStatusCode(opStatCode);
    	facility.setEmissionsReport(er);

    	ReleasePointTypeCode releasePointTypeCode = new ReleasePointTypeCode();
    	releasePointTypeCode.setCode("1");
    	releasePointTypeCode.setCategory("Fugitive");
    	releasePointTypeCode.setDescription("Fugitive Area");

    	UnitMeasureCode distUom = new UnitMeasureCode();
    	distUom.setCode("FT");

    	ReleasePoint result = new ReleasePoint();
    	result.setReleasePointIdentifier("identifier");
    	result.setTypeCode(releasePointTypeCode);
    	result.setFenceLineUomCode(distUom);
    	result.setFugitiveLengthUomCode(distUom);
    	result.setFugitiveWidthUomCode(distUom);
    	result.setFugitiveHeightUomCode(distUom);
    	result.setFenceLineDistance((long) 1);
    	result.setFugitiveLength(BigDecimal.ONE);
    	result.setFugitiveWidth(BigDecimal.ONE);
    	result.setFugitiveHeight((long) 1);
    	result.setLatitude(BigDecimal.valueOf(33.949000));
    	result.setLongitude(BigDecimal.valueOf(-84.388000));
    	result.setFacilitySite(facility);
    	result.setStatusYear((short) 2000);
    	result.setOperatingStatusCode(opStatCode);

        List<ReleasePointAppt> rpaList = new ArrayList<>();
        ReleasePointAppt rpa = new ReleasePointAppt();
        rpa.setReleasePoint(result);
        rpaList.add(rpa);

        result.setReleasePointAppts(rpaList);

    	return result;
    }

}
