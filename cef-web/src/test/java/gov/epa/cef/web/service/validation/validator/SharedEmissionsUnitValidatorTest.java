/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package gov.epa.cef.web.service.validation.validator;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.*;

import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.service.validation.ValidationFeature;
import gov.epa.cef.web.util.ConstantUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.baidu.unbiz.fluentvalidator.ValidationError;

import gov.epa.cef.web.config.mock.MockSLTConfig;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.EmissionsUnitRepository;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.federal.SharedEmissionsUnitValidator;
import gov.epa.cef.web.util.SLTConfigHelper;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SharedEmissionsUnitValidatorTest extends BaseValidatorTest {

    @InjectMocks
    private SharedEmissionsUnitValidator validator;

    @Mock
    private PointSourceSccCodeRepository sccRepo;

    @Mock
    private EmissionsReportRepository reportRepo;

    @Mock
    private EmissionsUnitRepository unitRepo;

    @Mock
    private SLTConfigHelper sltConfigHelper;

    private OperatingStatusCode opStatCode;
    private OperatingStatusCode tsStatCode;
    private OperatingStatusCode psStatCode;

    @Before
    public void init(){

        opStatCode = new OperatingStatusCode();
        opStatCode.setCode("OP");
        tsStatCode = new OperatingStatusCode();
        tsStatCode.setCode("TS");
        psStatCode = new OperatingStatusCode();
        psStatCode.setCode("PS");

        List<PointSourceSccCode> sccList = new ArrayList<PointSourceSccCode>();
        PointSourceSccCode scc1 = new PointSourceSccCode();
        scc1.setCode("10200302");
        scc1.setFuelUseRequired(true);
        scc1.setMonthlyReporting(true);
        sccList.add(scc1);

        PointSourceSccCode scc2 = new PointSourceSccCode();
        scc2.setCode("10200301");
        scc2.setFuelUseRequired(false);
        scc2.setMonthlyReporting(false);
        sccList.add(scc2);

        PointSourceSccCode scc3 = new PointSourceSccCode();
        scc3.setCode("30503506");
        scc3.setFuelUseRequired(false);
        scc3.setMonthlyReporting(false);
        sccList.add(scc3);

        PointSourceSccCode scc4 = new PointSourceSccCode();
        scc4.setCode("30500246");
        scc4.setFuelUseRequired(false);
        scc4.setMonthlyReporting(true);
        sccList.add(scc4);

        PointSourceSccCode scc5 = new PointSourceSccCode();
        scc5.setCode("30188801");
        scc5.setFuelUseRequired(false);
        scc5.setMonthlyReporting(false);
        scc5.setDualThroughput(true);

        when(sccRepo.findById("10200302")).thenReturn(Optional.of(scc1));
        when(sccRepo.findById("10200301")).thenReturn(Optional.of(scc2));
        when(sccRepo.findById("30503506")).thenReturn(Optional.of(scc3));
        when(sccRepo.findById("30500246")).thenReturn(Optional.of(scc4));
        when(sccRepo.findById("30188801")).thenReturn(Optional.of(scc5));

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

        EmissionsUnit eu = new EmissionsUnit();
        eu.setId(1L);
        eu.setOperatingStatusCode(opStatCode);
        eu.setStatusYear((short)2018);
        eu.setUnitIdentifier("Boiler 001");

        EmissionsProcess p = new EmissionsProcess();
        p.setStatusYear((short) 2020);
        p.setId(2L);
        p.setOperatingStatusCode(opStatCode);
        p.setEmissionsProcessIdentifier("Boiler 001");
        p.setEmissionsUnit(eu);
        eu.getEmissionsProcesses().add(p);

        EmissionsProcess p1 = new EmissionsProcess();
        p1.setStatusYear((short) 2020);
        p1.setId(2L);
        p1.setOperatingStatusCode(opStatCode);
        p1.setEmissionsProcessIdentifier("Boiler 002");
        p1.setEmissionsUnit(eu);
        eu.getEmissionsProcesses().add(p1);

        EmissionsUnit eu1 = new EmissionsUnit();
        eu1.setId(1L);
        eu1.setOperatingStatusCode(opStatCode);
        eu1.setStatusYear((short)2018);
        eu1.setUnitIdentifier("ABC");

        EmissionsProcess p2 = new EmissionsProcess();
        p2.setStatusYear((short) 2018);
        p2.setId(2L);
        p2.setOperatingStatusCode(opStatCode);
        p2.setEmissionsProcessIdentifier("ABC");
        p2.setEmissionsUnit(eu);
        eu1.getEmissionsProcesses().add(p2);

        EmissionsProcess p3 = new EmissionsProcess();
        p3.setStatusYear((short) 2018);
        p3.setId(2L);
        p3.setOperatingStatusCode(opStatCode);
        p3.setEmissionsProcessIdentifier("AbC  ");
        p3.setEmissionsUnit(eu);
        eu1.getEmissionsProcesses().add(p3);

        EmissionsUnit eu2 = new EmissionsUnit();
        eu2.setId(1L);
        eu2.setOperatingStatusCode(opStatCode);
        eu2.setStatusYear((short)1800);
        eu2.setUnitIdentifier("status year test");

        EmissionsUnit eu3 = new EmissionsUnit();
        eu3.setId(1L);
        eu3.setOperatingStatusCode(opStatCode);
        eu3.setStatusYear((short)1800);
        eu3.setUnitIdentifier("identifier test");

        when(reportRepo.findByMasterFacilityRecordId(1L)).thenReturn(erList);
        when(reportRepo.findByMasterFacilityRecordId(2L)).thenReturn(Collections.emptyList());
        when(unitRepo.retrieveByIdentifierFacilityYear(
            "Boiler 001",1L,(short) 2018)).thenReturn(Collections.singletonList(eu));
        when(unitRepo.retrieveByIdentifierFacilityYear(
            "test new",1L,(short) 2018)).thenReturn(Collections.emptyList());
        when(unitRepo.retrieveByIdentifierFacilityYear(
            "ABC",1L,(short) 2018)).thenReturn(Collections.singletonList(eu1));
        when(unitRepo.retrieveByIdentifierFacilityYear(
            "status year test",1L,(short) 2018)).thenReturn(Collections.singletonList(eu2));
        when(unitRepo.retrieveByIdentifierFacilityYear(
            "identifier test",1L,(short) 2018)).thenReturn(Collections.singletonList(eu2));

        MockSLTConfig gaConfig = new MockSLTConfig();
        gaConfig.setSltMonthlyFuelReportingEnabled(true);
        when(sltConfigHelper.getCurrentSLTConfig("GADNR")).thenReturn(gaConfig);
    }

    @Test
    public void simpleValidatePassTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void simpleValidateOperationStatusTypeFailTest() {
        // fails when operating status is not OP and status year is null
        // fails when current year operating status for PS/TS is null and previous year status is OP
        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();

        testData.setOperatingStatusCode(tsStatCode);
        testData.setStatusYear(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_STATUS_CODE.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_STATUS_CODE.value()).size() == 1);
    }

    @Test
    public void operationStatusPSCopyForwardWarningTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();

        testData.setOperatingStatusCode(psStatCode);
        testData.setStatusYear((short) 2020);

        // Operating status code of PS will generate a warning to inform users component will not be copied forward
        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_STATUS_CODE.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_STATUS_CODE.value()).size() == 1);

        cefContext = createContext();
        FacilitySourceTypeCode stc = new FacilitySourceTypeCode();
        stc.setCode("104");
        testData.getFacilitySite().getEmissionsReport().getMasterFacilityRecord().setFacilitySourceTypeCode(stc);

        // When Unit status code is PS, no processes, and source type code is landfill will generate a warning to inform users component will not be copied forward
        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        cefContext = createContext();
        EmissionsProcess ep1 = new EmissionsProcess();
        ep1.setStatusYear((short) 2019);
        ep1.setOperatingStatusCode(psStatCode);
        ep1.setSccCode("10200301"); // not required
        ep1.setEmissionsProcessIdentifier("Boiler 001");
        ep1.setEmissionsUnit(testData);
        testData.getEmissionsProcesses().add(ep1);

        // When Unit status code is PS and Process status code is PS, warnings are generated to inform users components will not be copied forward
        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);

        cefContext = createContext();
        ep1.setOperatingStatusCode(opStatCode);

        // No warning when Unit status code is PS, Process status code is OP, and source type code is landfill
        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        testData.setOperatingStatusCode(opStatCode);
        ep1.setOperatingStatusCode(psStatCode);

        // When Unit status code is OP, processes is PS will generate a warning to inform users process will not be copied forward
        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
    }

    @Test
    public void simpleValidateStatusYearRangeTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();

        testData.setStatusYear((short) 1900);
        testData.setUnitIdentifier("status year test");

        boolean a = this.validator.validate(cefContext, testData);
        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        testData.setStatusYear((short) 2020);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void simpleValidateStatusYearRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();

        testData.setStatusYear((short) 1850);
        testData.setUnitIdentifier("status year test");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_STATUS_YEAR.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_STATUS_YEAR.value()).size() == 1);

        cefContext = createContext();
        testData.setStatusYear((short) 2021);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_STATUS_YEAR.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_STATUS_YEAR.value()).size() == 1);
    }

    @Test
    public void designCapacityCheckCalculationFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();

        cefContext = createContext();
        testData.setDesignCapacity(null);
        testData.setUnitOfMeasureCode(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_CAPACITY.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_CAPACITY.value()).size() == 1);

        //Verify QA Checks are NOT run when Unit is NOT Operating
        testData.setOperatingStatusCode(tsStatCode);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void designCapacityFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();
        testData.setDesignCapacity(BigDecimal.valueOf(0.0001));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null || cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_CAPACITY.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_CAPACITY.value()).size() == 1);

        cefContext = createContext();
        testData.setDesignCapacity(BigDecimal.valueOf(200000000));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null || cefContext.result.getErrors().size() == 1);

        //Verify QA Checks are NOT run when Unit is NOT Operating
        testData.setOperatingStatusCode(tsStatCode);
        testData.setDesignCapacity(BigDecimal.valueOf(0.0001));

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        testData.setDesignCapacity(BigDecimal.valueOf(200000000));

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void uniqueIdentifierCheckFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();
        testData.setUnitIdentifier("identifier test");

        EmissionsUnit em1 = new EmissionsUnit();
        em1.setUnitIdentifier("identifier test");
        em1.setId(2L);

        testData.getFacilitySite().getEmissionsUnits().add(em1);
        testData.getFacilitySite().getEmissionsUnits().add(testData);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_IDENTIFIER.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_IDENTIFIER.value()).size() == 1);
    }

    @Test
    public void designCapacityAndUomRequiredFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();
        UnitTypeCode utc = new UnitTypeCode();
        utc.setCode("200");
        testData.setUnitTypeCode(utc);
        testData.setDesignCapacity(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_CAPACITY.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_CAPACITY.value()).size() == 1);

        cefContext = createContext();
        testData.setUnitOfMeasureCode(null);
        testData.setDesignCapacity(BigDecimal.valueOf(1000.0));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_CAPACITY.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_CAPACITY.value()).size() == 1);

        //Verify QA Checks are NOT run when Unit is NOT Operating
        testData = createBaseEmissionsUnit();
        utc = new UnitTypeCode();
        utc.setCode("200");
        testData.setUnitTypeCode(utc);
        testData.setDesignCapacity(null);
        testData.setOperatingStatusCode(tsStatCode);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        cefContext = createContext();
        testData.setUnitOfMeasureCode(null);
        testData.setDesignCapacity(BigDecimal.valueOf(1000.0));

        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void legacyUomFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();
        testData.getUnitOfMeasureCode().setLegacy(true);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_UOM.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_UOM.value()).size() == 1);

        //Verify QA Checks are NOT run when Unit is NOT Operating
        testData.setOperatingStatusCode(tsStatCode);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void duplicateProcessIdentifierFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();
        testData.setUnitIdentifier("ABC");
        EmissionsProcess ep1 = new EmissionsProcess();
        EmissionsProcess ep2 = new EmissionsProcess();
        ep1.setOperatingStatusCode(opStatCode);
        ep2.setOperatingStatusCode(opStatCode);
        ep1.setEmissionsProcessIdentifier("AbC  ");
        ep2.setEmissionsProcessIdentifier("ABC");
        ep1.setEmissionsUnit(testData);
        ep2.setEmissionsUnit(testData);
        ep1.setSccCode("30503506");
        ep2.setSccCode("10200301");
        testData.getEmissionsProcesses().add(ep1);
        testData.getEmissionsProcesses().add(ep2);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_PROCESS.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_PROCESS.value()).size() == 1);
    }

    /**
     * There should be no errors when emissions unit operating status is not PS.
     * There should be three errors when emissions unit operating status is PS and emission process operating status is not PS,
     * PS unit and underlying process not copied forward warnings.
     * There should be two errors when emissions unit operating status is PS and emission process operating status is PS,
     * PS unit and PS process not copied forward warnings.
     */
    @Test
    public void emissionUnitOperatingStatusPSEmissionProcessStatusTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();
        EmissionsProcess ep = new EmissionsProcess();
        ep.setOperatingStatusCode(opStatCode);
        ep.setEmissionsProcessIdentifier("Boiler 001");
        ep.setEmissionsUnit(testData);
        testData.getEmissionsProcesses().add(ep);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        testData.setOperatingStatusCode(psStatCode);
        FacilitySourceTypeCode stc = new FacilitySourceTypeCode();
        stc.setCode("100");
        testData.getFacilitySite().getEmissionsReport().getMasterFacilityRecord().setFacilitySourceTypeCode(stc);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 3);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_STATUS_CODE.value()) && errorMap.get(ValidationField.PROCESS_STATUS_CODE.value()).size() == 2);

        cefContext = createContext();
        ep.setOperatingStatusCode(psStatCode);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);
    }

    /**
     * There should be no errors when emissions unit operating status is not TS or PS.
     * There should be one error when emissions unit operating status is TS and emission process operating status is not PS and not TS.
     * There should be no errors when emissions unit operating status is TS and emission process operating status is TS.
     * There should be one errors when emissions unit operating status is TS and emission process operating status is PS.
     */
    @Test
    public void emissionUnitOperatingStatusTSEmissionUnitStatusTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();
        EmissionsProcess ep = new EmissionsProcess();
        ep.setOperatingStatusCode(opStatCode);
        ep.setEmissionsProcessIdentifier("Boiler 001");
        ep.setEmissionsUnit(testData);
        testData.getEmissionsProcesses().add(ep);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        testData.setOperatingStatusCode(tsStatCode);
        FacilitySourceTypeCode stc = new FacilitySourceTypeCode();
        stc.setCode("100");
        testData.getFacilitySite().getEmissionsReport().getMasterFacilityRecord().setFacilitySourceTypeCode(stc);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_STATUS_CODE.value()) && errorMap.get(ValidationField.PROCESS_STATUS_CODE.value()).size() == 1);

        cefContext = createContext();
        ep.setOperatingStatusCode(tsStatCode);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        ep.setOperatingStatusCode(psStatCode);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
    }

    /**
     * There should be no errors when emissions unit design capacity uom is a valid uom for eis.
     * There should be no error when emissions unit design capacity uom is not a valid uom for eis and the unit operating status is PS.
     * There should be one error when emissions unit design capacity uom is not a valid uom for eis.
     * There should be one error when emissions unit operating status is PS and source type is not landfill.
     */
    @Test
    public void designCapacityUomFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();
        testData.getUnitOfMeasureCode().setUnitDesignCapacity(true);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        testData.setOperatingStatusCode(psStatCode);
        testData.getUnitOfMeasureCode().setUnitDesignCapacity(false);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        cefContext = createContext();
        testData.setOperatingStatusCode(opStatCode);
        testData.getUnitOfMeasureCode().setUnitDesignCapacity(false);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_UOM.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_UOM.value()).size() == 1);
    }

    @Test
    public void fuelUsePassTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();

        EmissionsProcess ep1 = new EmissionsProcess();
        ep1.setOperatingStatusCode(opStatCode);
        ep1.setSccCode("10200301"); // fuel use not required
        ep1.setEmissionsProcessIdentifier("Boiler 001");
        ep1.setEmissionsUnit(testData);
        testData.getEmissionsProcesses().add(ep1);

        UnitMeasureCode uom = new UnitMeasureCode();
        uom.setCode("BTU");

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        testData = createBaseEmissionsUnit();

        CalculationMaterialCode cmc = new CalculationMaterialCode();
        cmc.setFuelUseMaterial(true);
        cmc.setCode("794");

        ReportingPeriod rperiod1 = new ReportingPeriod();
        rperiod1.setFuelUseMaterialCode(cmc);
        rperiod1.setFuelUseUom(uom);
        rperiod1.setFuelUseValue(BigDecimal.valueOf(100));
        ReportingPeriodCode rpc = new ReportingPeriodCode();
        rpc.setCode("A");
        rperiod1.setReportingPeriodTypeCode(rpc);

        ep1.setSccCode("30500246"); // is monthly reporting
        ep1.getReportingPeriods().add(rperiod1);
        testData.getEmissionsProcesses().add(ep1);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void fuelDataValuesFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();

        ReportingPeriod rperiod1 = new ReportingPeriod();
        EmissionsProcess ep1 = new EmissionsProcess();
        ep1.setOperatingStatusCode(opStatCode);
        ep1.setSccCode("10200301"); // not required
        ep1.getReportingPeriods().add(rperiod1);
        ep1.setEmissionsProcessIdentifier("Boiler 001");
        ep1.setEmissionsUnit(testData);
        testData.getEmissionsProcesses().add(ep1);

        CalculationMaterialCode cmc = new CalculationMaterialCode();
        cmc.setFuelUseMaterial(true);
        cmc.setCode("794");

        rperiod1.setFuelUseMaterialCode(cmc);
        rperiod1.setFuelUseUom(null);
        rperiod1.setFuelUseValue(BigDecimal.valueOf(100));
        ReportingPeriodCode rpc = new ReportingPeriodCode();
        rpc.setCode("A");
        rpc.setShortName("Annual");
        rperiod1.setReportingPeriodTypeCode(rpc);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_FUEL_USE_VALUES.value()) && errorMap.get(ValidationField.PERIOD_FUEL_USE_VALUES.value()).size() == 1);

        cefContext = createContext();
        testData = createBaseEmissionsUnit();

        rperiod1 = new ReportingPeriod();
        ep1 = new EmissionsProcess();
        ep1.setOperatingStatusCode(opStatCode);
        ep1.setSccCode("10200302"); // require fuel use
        ep1.getReportingPeriods().add(rperiod1);
        ep1.setEmissionsProcessIdentifier("Boiler 001");
        ep1.setEmissionsUnit(testData);
        testData.getEmissionsProcesses().add(ep1);

        UnitMeasureCode uom = new UnitMeasureCode();
        uom.setCode("BTU");

        rperiod1.setFuelUseMaterialCode(null);
        rperiod1.setFuelUseUom(null);
        rperiod1.setFuelUseValue(null);
        rperiod1.setHeatContentUom(uom);
        rperiod1.setHeatContentValue(BigDecimal.valueOf(100));
        rperiod1.setReportingPeriodTypeCode(rpc);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_FUEL_USE_VALUES.value()) && errorMap.get(ValidationField.PERIOD_FUEL_USE_VALUES.value()).size() == 1);

        // warning if reporting fuel on a dual throughput scc
        cefContext = createContext();
        testData = createBaseEmissionsUnit();

        rperiod1 = new ReportingPeriod();
        ep1 = new EmissionsProcess();
        ep1.setOperatingStatusCode(opStatCode);
        ep1.setSccCode("30188801"); // require fuel use

        rperiod1.setFuelUseMaterialCode(cmc);
        rperiod1.setFuelUseUom(uom);
        rperiod1.setFuelUseValue(BigDecimal.valueOf(100));
        rperiod1.setHeatContentUom(uom);
        rperiod1.setHeatContentValue(BigDecimal.valueOf(100));
        rperiod1.setReportingPeriodTypeCode(rpc);
        ep1.getReportingPeriods().add(rperiod1);
        ep1.setEmissionsProcessIdentifier("Boiler 001");
        ep1.setEmissionsUnit(testData);
        testData.getEmissionsProcesses().add(ep1);


        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_FUEL_USE_VALUES.value()) && errorMap.get(ValidationField.PERIOD_FUEL_USE_VALUES.value()).size() == 1);

        cefContext = createContext();
        testData = createBaseEmissionsUnit();

        rperiod1 = new ReportingPeriod();
        rperiod1.setReportingPeriodTypeCode(rpc);
        ep1 = new EmissionsProcess();
        ep1.setOperatingStatusCode(opStatCode);
        ep1.setSccCode("30500246"); // is monthly reporting
        ep1.getReportingPeriods().add(rperiod1);
        ep1.setEmissionsProcessIdentifier("Boiler 001");
        ep1.setEmissionsUnit(testData);
        testData.getEmissionsProcesses().add(ep1);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_FUEL_USE_VALUES.value()) && errorMap.get(ValidationField.PERIOD_FUEL_USE_VALUES.value()).size() == 1);

        // one error when reporting period is monthly, monthly reporting enabled, not a dupe process, and fuel use is null
        cefContext = createContext();
        cefContext.enable(ValidationFeature.SEMIANNUAL);
        testData = createBaseEmissionsUnit();

        ReportingPeriod rperiod2 = new ReportingPeriod();
        EmissionsProcess ep2 = new EmissionsProcess();
        CalculationMaterialCode cmc2 = new CalculationMaterialCode();
        cmc2.setFuelUseMaterial(true);
        cmc2.setCode("794");

        rperiod2.setFuelUseMaterialCode(cmc2);
        rperiod2.setFuelUseUom(uom);
        rperiod2.setFuelUseValue(null);
        ReportingPeriodCode rpc2 = new ReportingPeriodCode();
        rpc2.setCode("JAN");
        rpc2.setShortName("January");
        rperiod2.setReportingPeriodTypeCode(rpc2);
        ep2.setOperatingStatusCode(opStatCode);
        ep2.setSccCode("30500246"); // not required
        ep2.setEmissionsProcessIdentifier("Boiler 001");
        ep2.setEmissionsUnit(testData);
        ep2.getReportingPeriods().add(rperiod2);
        rperiod2.setEmissionsProcess(ep2);
        testData.getEmissionsProcesses().add(ep2);

        testData.getEmissionsProcesses().get(0).getReportingPeriods().get(0).setFuelUseValue(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_FUEL_VALUE.value()) && errorMap.get(ValidationField.PERIOD_FUEL_VALUE.value()).size() == 1);
    }

    @Test
    public void heatContentValuesFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();

        ReportingPeriod rperiod1 = new ReportingPeriod();
        rperiod1.setHeatContentUom(null);
        rperiod1.setHeatContentValue(BigDecimal.valueOf(100));
        ReportingPeriodCode rpc = new ReportingPeriodCode();
        rpc.setCode("A");
        rpc.setShortName("Annual");
        rperiod1.setReportingPeriodTypeCode(rpc);

        EmissionsProcess ep1 = new EmissionsProcess();
        ep1.setStatusYear((short) 2020);
        ep1.setOperatingStatusCode(opStatCode);
        ep1.setSccCode("10200301"); // not required
        ep1.getReportingPeriods().add(rperiod1);
        ep1.setEmissionsProcessIdentifier("Boiler 001");
        ep1.setEmissionsUnit(testData);
        testData.getEmissionsProcesses().add(ep1);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_HEAT_CONTENT_VALUES.value()) && errorMap.get(ValidationField.PERIOD_HEAT_CONTENT_VALUES.value()).size() == 1);

        cefContext = createContext();
        testData = createBaseEmissionsUnit();

        ReportingPeriod rperiod = new ReportingPeriod();
        CalculationMaterialCode cmc = new CalculationMaterialCode();
        cmc.setFuelUseMaterial(true);
        cmc.setCode("794");

        UnitMeasureCode uom = new UnitMeasureCode();
        uom.setCode("BTU");

        rperiod.setFuelUseMaterialCode(cmc);
        rperiod.setFuelUseUom(uom);
        rperiod.setFuelUseValue(BigDecimal.valueOf(100));
        rperiod.setHeatContentUom(null);
        rperiod.setHeatContentValue(null);
        rperiod.setReportingPeriodTypeCode(rpc);

        EmissionsProcess ep = new EmissionsProcess();
        ep.setStatusYear((short) 2020);
        ep.setOperatingStatusCode(opStatCode);
        ep.setSccCode("10200302"); // required
        ep.getReportingPeriods().add(rperiod);
        ep.setEmissionsProcessIdentifier("Boiler 001");
        ep.setEmissionsUnit(testData);
        testData.getEmissionsProcesses().add(ep);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_HEAT_CONTENT_VALUES.value()) && errorMap.get(ValidationField.PERIOD_HEAT_CONTENT_VALUES.value()).size() == 1);
    }

    @Test
    public void processesWithSameSccFuelDataFail() {
        // fails if the process is considered a duplicate and more than one process has fuel data
        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();

        ReportingPeriod rperiod = new ReportingPeriod();
        OperatingDetail od = new OperatingDetail();
        od.setActualHoursPerPeriod(BigDecimal.ONE);
        od.setAvgDaysPerWeek(BigDecimal.ONE);
        od.setAvgHoursPerDay(BigDecimal.ONE);
        od.setAvgWeeksPerPeriod(BigDecimal.ONE);
        od.setPercentFall(BigDecimal.ZERO);
        od.setPercentSpring(BigDecimal.ZERO);
        od.setPercentSummer(BigDecimal.ZERO);
        od.setPercentWinter(BigDecimal.valueOf(100.0));
        od.setReportingPeriod(rperiod);
        rperiod.getOperatingDetails().add(od);

        CalculationMaterialCode cmc = new CalculationMaterialCode();
        cmc.setFuelUseMaterial(true);
        cmc.setCode("794");

        UnitMeasureCode uom = new UnitMeasureCode();
        uom.setCode("BTU");
        ReportingPeriodCode rpc = new ReportingPeriodCode();
        rpc.setCode("A");
        EmissionsOperatingTypeCode otc = new EmissionsOperatingTypeCode();
        otc.setCode("U");

        rperiod.setFuelUseMaterialCode(cmc);
        rperiod.setFuelUseUom(uom);
        rperiod.setFuelUseValue(BigDecimal.valueOf(100));
        rperiod.setHeatContentUom(uom);
        rperiod.setHeatContentValue(BigDecimal.valueOf(100));
        rperiod.setReportingPeriodTypeCode(rpc);
        rperiod.setEmissionsOperatingTypeCode(otc);

        EmissionsProcess ep = new EmissionsProcess();
        ep.setStatusYear((short) 2000);
        ep.setOperatingStatusCode(opStatCode);
        ep.setSccCode("10200302");
        ep.getReportingPeriods().add(rperiod);
        ep.setEmissionsProcessIdentifier("Boiler 001");
        ep.setEmissionsUnit(testData);
        testData.getEmissionsProcesses().add(ep);

        ReportingPeriod rperiod2 = new ReportingPeriod();
        OperatingDetail od2 = new OperatingDetail();
        od2.setActualHoursPerPeriod(BigDecimal.ONE);
        od2.setAvgDaysPerWeek(BigDecimal.ONE);
        od2.setAvgHoursPerDay(BigDecimal.ONE);
        od2.setAvgWeeksPerPeriod(BigDecimal.ONE);
        od2.setPercentFall(BigDecimal.ZERO);
        od2.setPercentSpring(BigDecimal.ZERO);
        od2.setPercentSummer(BigDecimal.ZERO);
        od2.setPercentWinter(BigDecimal.valueOf(100.0));
        od2.setReportingPeriod(rperiod2);
        rperiod2.getOperatingDetails().add(od2);

        rperiod2.setFuelUseMaterialCode(cmc);
        rperiod2.setFuelUseUom(uom);
        rperiod2.setFuelUseValue(BigDecimal.valueOf(100));
        rperiod2.setHeatContentUom(uom);
        rperiod2.setHeatContentValue(BigDecimal.valueOf(100));
        rperiod2.setReportingPeriodTypeCode(rpc);
        rperiod2.setEmissionsOperatingTypeCode(otc);

        EmissionsProcess ep2 = new EmissionsProcess();
        ep2.setStatusYear((short) 2000);
        ep2.setOperatingStatusCode(opStatCode);
        ep2.setSccCode("10200302");
        ep2.getReportingPeriods().add(rperiod2);
        ep2.setEmissionsProcessIdentifier("Boiler 002");
        ep2.setEmissionsUnit(testData);
        testData.getEmissionsProcesses().add(ep2);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_DUP_SCC_FUEL_USE.value()) && errorMap.get(ValidationField.PERIOD_DUP_SCC_FUEL_USE.value()).size() == 1);

        // fails if the process is considered a duplicate and both have the same pollutant
        cefContext = createContext();
        Emission e1 = new Emission();
        Emission e2 = new Emission();
        Pollutant p = new Pollutant();
        p.setPollutantCode("1308389");
        p.setPollutantName("Chromic Oxide");
        e1.setPollutant(p);
        e2.setPollutant(p);
        rperiod2.getEmissions().add(e1);
        rperiod.getEmissions().add(e1);

        rperiod2.setFuelUseMaterialCode(null);
        rperiod2.setFuelUseUom(null);
        rperiod2.setFuelUseValue(null);
        rperiod2.setHeatContentUom(null);
        rperiod2.setHeatContentValue(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_PROCESS.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_PROCESS.value()).size() == 1);

        // warning message if pollutants are the same for an SCC that allows dual throughputs
        cefContext = createContext();

        for (EmissionsProcess proc : testData.getEmissionsProcesses()) {
            proc.setSccCode("30188801");
        }

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_PROCESS.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_PROCESS.value()).size() == 1);
    }

    @Test
    public void processesWithSameSccFailWarning() {
        // fail warning if the process details are the same and operating type is different
        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();

        ReportingPeriod rperiod = new ReportingPeriod();
        OperatingDetail od = new OperatingDetail();
        od.setActualHoursPerPeriod(BigDecimal.ONE);
        od.setAvgDaysPerWeek(BigDecimal.ONE);
        od.setAvgHoursPerDay(BigDecimal.ONE);
        od.setPercentFall(BigDecimal.ZERO);
        od.setPercentSpring(BigDecimal.ZERO);
        od.setPercentSummer(BigDecimal.ZERO);
        od.setPercentWinter(BigDecimal.valueOf(100.0));
        od.setAvgWeeksPerPeriod(BigDecimal.ONE);
        od.setReportingPeriod(rperiod);
        rperiod.getOperatingDetails().add(od);

        CalculationMaterialCode cmc = new CalculationMaterialCode();
        cmc.setFuelUseMaterial(true);
        cmc.setCode("794");

        UnitMeasureCode uom = new UnitMeasureCode();
        uom.setCode("BTU");
        ReportingPeriodCode rpc = new ReportingPeriodCode();
        rpc.setCode("A");
        EmissionsOperatingTypeCode otc = new EmissionsOperatingTypeCode();
        otc.setCode("U");

        rperiod.setFuelUseMaterialCode(cmc);
        rperiod.setFuelUseUom(uom);
        rperiod.setFuelUseValue(BigDecimal.valueOf(100));
        rperiod.setHeatContentUom(uom);
        rperiod.setHeatContentValue(BigDecimal.valueOf(100));
        rperiod.setReportingPeriodTypeCode(rpc);
        rperiod.setEmissionsOperatingTypeCode(otc);

        EmissionsProcess ep = new EmissionsProcess();
        ep.setStatusYear((short) 2000);
        ep.setOperatingStatusCode(opStatCode);
        ep.setSccCode("10200302");
        ep.getReportingPeriods().add(rperiod);
        ep.setEmissionsProcessIdentifier("Boiler 001");
        ep.setEmissionsUnit(testData);
        testData.getEmissionsProcesses().add(ep);

        ReportingPeriod rperiod2 = new ReportingPeriod();
        OperatingDetail od2 = new OperatingDetail();
        od2.setActualHoursPerPeriod(BigDecimal.ONE);
        od2.setAvgWeeksPerPeriod(BigDecimal.ONE);
        od2.setAvgDaysPerWeek(BigDecimal.ONE);
        od2.setAvgHoursPerDay(BigDecimal.ONE);
        od2.setPercentFall(BigDecimal.ZERO);
        od2.setPercentSpring(BigDecimal.ZERO);
        od2.setPercentSummer(BigDecimal.ZERO);
        od2.setPercentWinter(BigDecimal.valueOf(100.0));
        od2.setReportingPeriod(rperiod2);
        rperiod2.getOperatingDetails().add(od2);

        EmissionsOperatingTypeCode otc2 = new EmissionsOperatingTypeCode();
        otc.setCode("US");

        rperiod2.setFuelUseMaterialCode(cmc);
        rperiod2.setFuelUseUom(uom);
        rperiod2.setFuelUseValue(BigDecimal.valueOf(100));
        rperiod2.setHeatContentUom(uom);
        rperiod2.setHeatContentValue(BigDecimal.valueOf(100));
        rperiod2.setReportingPeriodTypeCode(rpc);
        rperiod2.setEmissionsOperatingTypeCode(otc2);

        EmissionsProcess ep2 = new EmissionsProcess();
        ep2.setStatusYear((short) 2000);
        ep2.setOperatingStatusCode(opStatCode);
        ep2.setSccCode("10200302");
        ep2.getReportingPeriods().add(rperiod2);
        ep2.setEmissionsProcessIdentifier("Boiler 002");
        ep2.setEmissionsUnit(testData);
        testData.getEmissionsProcesses().add(ep2);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
    }


    @Test
    public void processWithDupSccWarning() {
        // fails (warning) if process details and reporting period operating types are the same, but operating details are different
        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();

        ReportingPeriod rperiod = new ReportingPeriod();
        OperatingDetail od = new OperatingDetail();
        od.setActualHoursPerPeriod(BigDecimal.ONE);
        od.setAvgWeeksPerPeriod(BigDecimal.ONE);
        od.setAvgDaysPerWeek(BigDecimal.ONE);
        od.setAvgHoursPerDay(BigDecimal.valueOf(1.5));
        od.setPercentFall(BigDecimal.ZERO);
        od.setPercentSpring(BigDecimal.ZERO);
        od.setPercentSummer(BigDecimal.ZERO);
        od.setPercentWinter(BigDecimal.valueOf(100.0));
        od.setReportingPeriod(rperiod);
        rperiod.getOperatingDetails().add(od);

        CalculationMaterialCode cmc = new CalculationMaterialCode();
        cmc.setFuelUseMaterial(true);
        cmc.setCode("794");

        UnitMeasureCode uom = new UnitMeasureCode();
        uom.setCode("BTU");
        ReportingPeriodCode rpc = new ReportingPeriodCode();
        rpc.setCode("A");
        EmissionsOperatingTypeCode otc = new EmissionsOperatingTypeCode();
        otc.setCode("U");

        rperiod.setFuelUseMaterialCode(cmc);
        rperiod.setFuelUseUom(uom);
        rperiod.setFuelUseValue(BigDecimal.valueOf(100));
        rperiod.setHeatContentUom(uom);
        rperiod.setHeatContentValue(BigDecimal.valueOf(100));
        rperiod.setReportingPeriodTypeCode(rpc);
        rperiod.setEmissionsOperatingTypeCode(otc);

        EmissionsProcess ep = new EmissionsProcess();
        ep.setStatusYear((short) 2000);
        ep.setOperatingStatusCode(opStatCode);
        ep.setSccCode("10200302");
        ep.getReportingPeriods().add(rperiod);
        ep.setEmissionsProcessIdentifier("Boiler 001");
        ep.setEmissionsUnit(testData);
        testData.getEmissionsProcesses().add(ep);

        ReportingPeriod rperiod2 = new ReportingPeriod();
        OperatingDetail od2 = new OperatingDetail();
        od2.setActualHoursPerPeriod(BigDecimal.ONE);
        od2.setAvgWeeksPerPeriod(BigDecimal.ONE);
        od2.setAvgDaysPerWeek(BigDecimal.ONE);
        od2.setAvgHoursPerDay(BigDecimal.ONE);
        od2.setPercentFall(BigDecimal.ZERO);
        od2.setPercentSpring(BigDecimal.ZERO);
        od2.setPercentSummer(BigDecimal.ZERO);
        od2.setPercentWinter(BigDecimal.valueOf(100.0));
        od2.setReportingPeriod(rperiod2);
        rperiod2.getOperatingDetails().add(od2);

        rperiod2.setFuelUseMaterialCode(cmc);
        rperiod2.setFuelUseUom(uom);
        rperiod2.setFuelUseValue(BigDecimal.valueOf(100));
        rperiod2.setHeatContentUom(uom);
        rperiod2.setHeatContentValue(BigDecimal.valueOf(100));
        rperiod2.setReportingPeriodTypeCode(rpc);
        rperiod2.setEmissionsOperatingTypeCode(otc);

        EmissionsProcess ep2 = new EmissionsProcess();
        ep2.setStatusYear((short) 2000);
        ep2.setOperatingStatusCode(opStatCode);
        ep2.setSccCode("10200302");
        ep2.getReportingPeriods().add(rperiod2);
        ep2.setEmissionsProcessIdentifier("Boiler 002");
        ep2.setEmissionsUnit(testData);
        testData.getEmissionsProcesses().add(ep2);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_PROCESS.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_PROCESS.value()).size() == 1);
    }

    @Test
    public void previousOpYearCurrentShutdownYear() {
        // pass when previous and current op status is OP
        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();
        testData.setOperatingStatusCode(opStatCode);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        // pass when previous op status is OP, current op status is PS/TS, and current op status year is after previous year
        cefContext = createContext();
        testData.setOperatingStatusCode(tsStatCode);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        // fail when previous op status is OP, current op status is PS/TS, and current op status year is prior to previous year
        cefContext = createContext();
        testData.setOperatingStatusCode(tsStatCode);
        testData.setStatusYear((short) 2000);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_STATUS_YEAR.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_STATUS_YEAR.value()).size() == 1);

        // fail when previous op status is OP, current op status is PS/TS, and both have same status years
        // fail PS unit not copied forward warning.
        cefContext = createContext();
        testData.setOperatingStatusCode(psStatCode);
        testData.setStatusYear((short) 2018);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);

        // pass when facility source type code is landfill, QA is not checked if it is landfill
        cefContext = createContext();
        testData.setOperatingStatusCode(tsStatCode);
        testData.getFacilitySite().getEmissionsReport().getMasterFacilityRecord().getFacilitySourceTypeCode().setCode("104");

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

    }

    @Test
    public void newUnitShutdownPassTest() {
        // pass when previous exists and current op status is OP
        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();
        testData.setOperatingStatusCode(opStatCode);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        // pass when previous exists and current op status is PS/TS
        cefContext = createContext();
        testData.setOperatingStatusCode(tsStatCode);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        // pass when previous report doesn't exist and current op status is OP
        cefContext = createContext();
        testData.setOperatingStatusCode(opStatCode);
        testData.getFacilitySite().getEmissionsReport().getMasterFacilityRecord().setId(2L);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void newUnitShutdownPassFail() {
        // fail when previous report exists, but unit doesn't and current op status is TS
        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();
        testData.setOperatingStatusCode(tsStatCode);
        testData.setUnitIdentifier("test new");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_STATUS_CODE.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_STATUS_CODE.value()).size() == 1);

        // fail when previous report doesn't exist and current op status is TS
        cefContext = createContext();
        testData.getFacilitySite().getEmissionsReport().getMasterFacilityRecord().setId(2L);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_STATUS_CODE.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_STATUS_CODE.value()).size() == 1);
    }

    @Test
    public void operationStatusPSPreviousYearOpCurrentYearFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();

        testData.setOperatingStatusCode(opStatCode);
        testData.setPreviousYearOperatingStatusCode(psStatCode);
        testData.setStatusYear((short) 2019);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_STATUS_CODE.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_STATUS_CODE.value()).size() == 1);
    }

    @Test
    public void emissionsUnitAddedTest() {
        // fails when unit is added compared to previous report
        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();

        testData.setId(2L);
        testData.setOperatingStatusCode(opStatCode);
        testData.setStatusYear((short) 2020);
        testData.setUnitIdentifier("new unit for test");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT.value()).size() == 1);
    }

    @Test
    public void emissionsProcessAddedToExistingUnitTest() {
        // fails when process is added to an existing unit compared to previous report
        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();

        OperatingStatusCode os = new OperatingStatusCode();
        os.setCode("OP");

        EmissionsProcess newProcess = new EmissionsProcess();
        newProcess.setStatusYear((short) 2020);
        newProcess.setId(2L);
        newProcess.setEmissionsUnit(testData);
        newProcess.setOperatingStatusCode(os);
        newProcess.setEmissionsProcessIdentifier("new process for test");

        testData.getEmissionsProcesses().add(newProcess);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_PROCESS.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_PROCESS.value()).size() == 1);
    }

    @Test
    public void emissionsProcessAddedToNewUnitTest() {
        // fails when process is added to a unit that was also added compared to previous report
        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();

        testData.setId(2L);
        testData.setOperatingStatusCode(opStatCode);
        testData.setStatusYear((short) 2020);
        testData.setUnitIdentifier("new unit for test");

        EmissionsProcess newProcess = new EmissionsProcess();
        newProcess.setStatusYear((short) 2020);
        newProcess.setId(2L);
        newProcess.setOperatingStatusCode(opStatCode);
        newProcess.setEmissionsProcessIdentifier("new process for test");
        newProcess.setEmissionsUnit(testData);

        testData.getEmissionsProcesses().add(newProcess);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(
            errorMap.containsKey(ValidationField.EMISSIONS_UNIT.value()) &&
                errorMap.get(ValidationField.EMISSIONS_UNIT.value()).size() == 1 &&
                errorMap.containsKey(ValidationField.EMISSIONS_UNIT_PROCESS.value()) &&
                errorMap.get(ValidationField.EMISSIONS_UNIT_PROCESS.value()).size() == 1);
    }

    @Test
    public void validateEmissionsProcessNonOperatingFail(){
        EmissionsUnit testData = createBaseEmissionsUnit();
        CefValidatorContext cefContext = createContext();

        EmissionsProcess emissionsProcessTS = this.createEmissionProcessWithCode(2L, ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN, "Boiler 002");
        EmissionsProcess emissionsProcessPS = this.createEmissionProcessWithCode(3L, ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN, "Boiler 002");

        List<EmissionsProcess> emissionsProcessList = Arrays.asList(emissionsProcessTS, emissionsProcessPS);
        testData.setEmissionsProcesses(emissionsProcessList);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null || cefContext.result.getErrors().size() == 1);
        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_STATUS_CODE.value()));
    }

    @Test
    public void validateEmissionsProcessMixedProcessPass(){
        CefValidatorContext cefContext = createContext();
        EmissionsUnit testData = createBaseEmissionsUnit();

        EmissionsProcess emissionsProcessOP = this.createEmissionProcessWithCode(2L, ConstantUtils.STATUS_OPERATING, "Boiler 001");
        EmissionsProcess emissionsProcessTS = this.createEmissionProcessWithCode(3L, ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN, "Boiler 002");

        List<EmissionsProcess> emissionsProcessList = Arrays.asList(emissionsProcessOP, emissionsProcessTS);

        testData.setEmissionsProcesses(emissionsProcessList);
        boolean result = this.validator.validate(cefContext, testData);
        assertTrue(result);
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    private EmissionsProcess createEmissionProcessWithCode(Long id, String code, String processIdentifier){
        EmissionsProcess emissionsProcess = new EmissionsProcess();
        emissionsProcess.setId(id);
        emissionsProcess.setOperatingStatusCode(new OperatingStatusCode().withCode(code));
        emissionsProcess.setStatusYear((short)2020);
        emissionsProcess.setEmissionsProcessIdentifier(processIdentifier);
        return emissionsProcess;
    }

    private EmissionsUnit createBaseEmissionsUnit() {

        EmissionsUnit result = new EmissionsUnit();

        ProgramSystemCode psc = new ProgramSystemCode();
        psc.setCode("GADNR");

        UnitTypeCode utc = new UnitTypeCode();
        utc.setCode("100");

        UnitMeasureCode capUom = new UnitMeasureCode();
        capUom.setCode("CURIE");

        FacilitySourceTypeCode stc = new FacilitySourceTypeCode();
        stc.setCode("137");

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);
        mfr.setFacilitySourceTypeCode(stc);

        EmissionsReport er = new EmissionsReport();
        er.setId(1L);
        er.setYear(new Short("2020"));
        er.setMasterFacilityRecord(mfr);
        er.setProgramSystemCode(psc);

        FacilitySite facility = new FacilitySite();
        facility.setId(1L);
        facility.setOperatingStatusCode(opStatCode);
        facility.setEmissionsReport(er);

        result.setStatusYear((short) 2020);
        result.setOperatingStatusCode(opStatCode);
        result.setUnitIdentifier("Boiler 001");
        result.setUnitTypeCode(utc);
        result.setUnitOfMeasureCode(capUom);
        result.setFacilitySite(facility);
        result.setDesignCapacity(BigDecimal.valueOf(100));
        result.setId(1L);

        return result;
    }

}
