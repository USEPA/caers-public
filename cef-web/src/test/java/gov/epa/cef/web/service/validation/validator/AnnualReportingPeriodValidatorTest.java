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
import java.util.*;

import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.ReportingPeriodRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.baidu.unbiz.fluentvalidator.ValidationError;

import gov.epa.cef.web.config.mock.MockSLTConfig;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.service.impl.EmissionServiceImpl;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.federal.AnnualReportingPeriodValidator;
import gov.epa.cef.web.util.SLTConfigHelper;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AnnualReportingPeriodValidatorTest extends BaseValidatorTest {

    @InjectMocks
    private AnnualReportingPeriodValidator validator;

    @Mock
    private EmissionsReportRepository reportRepo;

    @Mock
    private ReportingPeriodRepository reportingPeriodRepo;

    @Mock
    private PointSourceSccCodeRepository sccRepo;

    @Mock
    private EmissionServiceImpl emissionService;

    @Mock
    private SLTConfigHelper sltConfigHelper;

    private CalculationMaterialCode cmcFuel;
    private UnitMeasureCode uomTon;
    private UnitMeasureCode uomE3Ton;
    private UnitMeasureCode uomBtuHr;
    private UnitMeasureCode uomBBL;

    @Before
    public void init(){

        PointSourceSccCode scc1 = new PointSourceSccCode();
        scc1.setCode("10200303");
        scc1.setFuelUseRequired(true);
        scc1.setMonthlyReporting(true);
        scc1.setCalculationMaterialCode(cmcFuel);
        scc1.setFuelUseTypes("energy,liquid");

        PointSourceSccCode scc2 = new PointSourceSccCode();
        scc2.setCode("10100101");
        scc2.setFuelUseRequired(true);
        scc2.setMonthlyReporting(true);
        scc2.setCalculationMaterialCode(cmcFuel);
        scc2.setFuelUseTypes("energy,liquid");

        cmcFuel = new CalculationMaterialCode();
        cmcFuel.setCode("124");
        cmcFuel.setFuelUseMaterial(true);
        cmcFuel.setDescription("Fuel");

        uomTon = new UnitMeasureCode();
        uomTon.setCode("TON");
        uomTon.setLegacy(false);
        uomTon.setFuelUseType("solid");
        uomTon.setUnitType("MASS");
        uomTon.setCalculationVariable("sTon");

        uomE3Ton = new UnitMeasureCode();
        uomE3Ton.setCode("E3TON");
        uomE3Ton.setLegacy(false);
        uomE3Ton.setFuelUseType("solid");
        uomE3Ton.setUnitType("MASS");
        uomE3Ton.setCalculationVariable("[k] * sTon");

        uomBtuHr = new UnitMeasureCode();
        uomBtuHr.setCode("BTU/HR");
        uomBtuHr.setLegacy(true);
        uomBtuHr.setUnitType("POWER");
        uomBtuHr.setCalculationVariable("btu / [h]");

        uomBBL = new UnitMeasureCode();
        uomBBL.setCode("BBL");
        uomBBL.setLegacy(false);
        uomBBL.setFuelUseType("liquid");
        uomBBL.setUnitType("BBL");
        uomBBL.setCalculationVariable("bbl");

        when(sccRepo.findById("10200303")).thenReturn(Optional.of(scc1));
        when(sccRepo.findById("10100101")).thenReturn(Optional.of(scc2));

        when(emissionService.checkIfCanConvertUnits(uomTon, uomTon)).thenReturn(true);

        List<String> pollList = new ArrayList<>(Collections.singletonList("CO"));
        when(emissionService.getEmissionsCreatedAfterSemiannualSubmission(1L, 3L)).thenReturn(pollList);

        MockSLTConfig gaConfig = new MockSLTConfig();
        gaConfig.setSltMonthlyFuelReportingEnabled(true);
        when(sltConfigHelper.getCurrentSLTConfig("GADNR")).thenReturn(gaConfig);

    }

    @Test
    public void simpleValidatePassTest() {

        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void calculationParameterValueFailTest() {

        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();
        testData.setCalculationParameterValue(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_CALC_VALUE.value()) && errorMap.get(ValidationField.PERIOD_CALC_VALUE.value()).size() == 1);

        cefContext = createContext();
        testData.setCalculationParameterValue(BigDecimal.valueOf(-1));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_CALC_VALUE.value()) && errorMap.get(ValidationField.PERIOD_CALC_VALUE.value()).size() == 1);
    }


    @Test
    public void throughputChanged20PercentFromPrevious_FailTest() {
        CefValidatorContext cefContext = createContext();
        createPreviousReportingPeriods();
        ReportingPeriod testData = createBaseReportingPeriod();

        testData.setCalculationParameterValue(new BigDecimal("655.024800"));
        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_CALC_VALUE.value()) && errorMap.get(ValidationField.PERIOD_CALC_VALUE.value()).size() == 1);
    }

    @Test
    public void fuelChanged20PercentFromPrevious_FailTest() {
        CefValidatorContext cefContext = createContext();
        createPreviousReportingPeriods();
        ReportingPeriod testData = createBaseReportingPeriod();

        testData.setFuelUseValue(new BigDecimal("436.683200"));
        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_FUEL_VALUE.value()) && errorMap.get(ValidationField.PERIOD_FUEL_VALUE.value()).size() == 1);
    }

    @Test
    public void emissionAddedAfterSemiannualSubmissionWarningTest() {

        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();

        testData.getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().setMidYearSubmissionStatus(ReportStatus.APPROVED);
        testData.getEmissionsProcess().setSccCode("10100101");

        Pollutant p = new Pollutant();
        p.setPollutantCode("CO");
        Emission e1 = new Emission();
        e1.setPollutant(p);
        e1.setCreatedDate(new Date());
        testData.getEmissions().add(e1);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_EMISSION.value()) && errorMap.get(ValidationField.PERIOD_EMISSION.value()).size() == 1);
    }

    private ReportingPeriod createBaseReportingPeriod() {

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);

        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("OP");
        ProgramSystemCode psc = new ProgramSystemCode();
        psc.setCode("GADNR");

        FacilitySite facilitySite = new FacilitySite();
        facilitySite.setProgramSystemCode(psc);
        EmissionsReport report = new EmissionsReport();
        report.setId(3L);
        report.setYear(new Short("2020"));
        report.setProgramSystemCode(psc);
        report.getFacilitySites().add(facilitySite);
        report.setMasterFacilityRecord(mfr);
        EmissionsUnit emissionsUnit = new EmissionsUnit();
        emissionsUnit.setFacilitySite(facilitySite);
        emissionsUnit.setId(1L);
        emissionsUnit.setUnitIdentifier("Boiler 001");

        EmissionsProcess emissionsProcess = new EmissionsProcess();
        emissionsProcess.setId(1L);
        emissionsProcess.setEmissionsUnit(emissionsUnit);
        emissionsProcess.setEmissionsProcessIdentifier("Boiler 001");

        ReportingPeriod result = new ReportingPeriod();
        result.setEmissionsProcess(emissionsProcess);
        result.getEmissionsProcess().setOperatingStatusCode(opStatCode);
        result.getEmissionsProcess().setSccCode("10200303");
        result.setId(2L);
        ReportingPeriodCode rpc = new ReportingPeriodCode();
        rpc.setCode("A");
        rpc.setShortName("Annual");
        result.setReportingPeriodTypeCode(rpc);
        EmissionsOperatingTypeCode eotc = new EmissionsOperatingTypeCode();
        eotc.setCode("R");
        result.setEmissionsOperatingTypeCode(eotc);
        result.setCalculationParameterValue(BigDecimal.valueOf(0));
        CalculationMaterialCode cmc = new CalculationMaterialCode();
        cmc.setCode("226");
        cmc.setFuelUseMaterial(false);
        result.setCalculationMaterialCode(cmc);
        CalculationParameterTypeCode cptc = new CalculationParameterTypeCode();
        cptc.setCode("O");
        result.setCalculationParameterTypeCode(cptc);
        result.setCalculationParameterUom(uomTon);
        // Above 20% lower than previous year
        result.setCalculationParameterValue(new BigDecimal("436683.200001"));
        result.setFuelUseMaterialCode(cmcFuel);
        result.setFuelUseUom(uomTon);
        // Below 20% higher than previous year
        result.setFuelUseValue(new BigDecimal("655024.799999"));

        emissionsProcess.getReportingPeriods().add(result);
        emissionsUnit.getEmissionsProcesses().add(emissionsProcess);
        facilitySite.setEmissionsReport(report);
        facilitySite.getEmissionsUnits().add(emissionsUnit);

        return result;
    }

    private void createPreviousReportingPeriods() {
        List<EmissionsReport> erList = new ArrayList<>();

        ProgramSystemCode psc = new ProgramSystemCode();
        psc.setCode("GADNR");
        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);
        EmissionsReport er1 = new EmissionsReport();
        er1.setId(1L);
        er1.setYear((short) 2019);
        er1.setMasterFacilityRecord(mfr);
        er1.setProgramSystemCode(psc);

        FacilitySite f1 = new FacilitySite();
        f1.setId(1L);

        EmissionsUnit eu = new EmissionsUnit();
        eu.setId(1L);
        eu.setUnitIdentifier("Boiler 001");

        EmissionsProcess process = new EmissionsProcess();
        process.setId(1L);
        process.setEmissionsUnit(eu);
        process.setEmissionsProcessIdentifier("Boiler 001");

        ReportingPeriodCode rpc = new ReportingPeriodCode();
        rpc.setCode("A");
        rpc.setShortName("Annual");

        CalculationMaterialCode cmc = new CalculationMaterialCode();
        cmc.setCode("226");
        cmc.setFuelUseMaterial(false);
        CalculationParameterTypeCode cptc = new CalculationParameterTypeCode();
        cptc.setCode("O");

        ReportingPeriod previousPeriod = new ReportingPeriod();
        previousPeriod.setEmissionsProcess(process);
        previousPeriod.setReportingPeriodTypeCode(rpc);
        previousPeriod.setId(2L);
        previousPeriod.setCalculationMaterialCode(cmc);
        previousPeriod.setCalculationParameterTypeCode(cptc);
        previousPeriod.setCalculationParameterUom(uomTon);
        previousPeriod.setCalculationParameterValue(new BigDecimal("545854"));
        previousPeriod.setFuelUseMaterialCode(cmcFuel);
        previousPeriod.setFuelUseUom(uomTon);
        previousPeriod.setFuelUseValue(new BigDecimal("545854"));

        List<ReportingPeriod> previousPeriods = new ArrayList<ReportingPeriod>();
        previousPeriods.add(previousPeriod);

        process.getReportingPeriods().add(previousPeriod);
        eu.getEmissionsProcesses().add(process);
        f1.setEmissionsReport(er1);
        f1.getEmissionsUnits().add(eu);

        erList.add(er1);

        when(reportRepo.findByMasterFacilityRecordId(er1.getMasterFacilityRecord().getId())).thenReturn(erList);

        when(reportingPeriodRepo.retrieveByTypeIdentifierParentFacilityYear(
            previousPeriod.getReportingPeriodTypeCode().getCode(),
            previousPeriod.getEmissionsProcess().getEmissionsProcessIdentifier(),
            previousPeriod.getEmissionsProcess().getEmissionsUnit().getUnitIdentifier(),
            er1.getMasterFacilityRecord().getId(),
            erList.get(erList.size()-1).getYear()
        )).thenReturn(previousPeriods);
    }
}
