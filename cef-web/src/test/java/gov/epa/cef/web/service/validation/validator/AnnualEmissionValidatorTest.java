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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import gov.epa.cef.web.repository.WebfireEmissionFactorRepository;
import gov.epa.cef.web.repository.EmissionRepository;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.repository.ReportingPeriodRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.baidu.unbiz.fluentvalidator.ValidationError;

import gov.epa.cef.web.config.CefConfig;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.service.impl.EmissionServiceImpl;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.federal.AnnualEmissionValidator;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AnnualEmissionValidatorTest extends BaseValidatorTest {

    @InjectMocks
    private AnnualEmissionValidator validator;

    @Mock
    private WebfireEmissionFactorRepository webfireEfRepo;

    @Mock
    private EmissionRepository emissionRepo;

    @Mock
    private EmissionsReportRepository reportRepo;

    @Mock
    private PointSourceSccCodeRepository pointSourceSccCodeRepo;

    @Mock
    private CefConfig cefConfig;

    @Mock
    private ReportingPeriodRepository reportingPeriodRepo;

    @Spy
    private EmissionServiceImpl emissionService;


    private UnitMeasureCode curieUom;
    private UnitMeasureCode lbUom;
    private UnitMeasureCode tonUom;
    private UnitMeasureCode uomTon;
    private CalculationMaterialCode cmcFuel;
    private UnitMeasureCode btuUom;
    private UnitMeasureCode e3ft3sUom;
    private OperatingStatusCode opStatCode_op;
    private FacilitySourceTypeCode sourceType_100;
    private CalculationMaterialCode natGasCmc;
    private ReportingPeriodCode rpType;

    @Before
    public void init() {
    	opStatCode_op = new OperatingStatusCode();
    	opStatCode_op.setCode("OP");

    	sourceType_100 = new FacilitySourceTypeCode();
    	sourceType_100.setCode("100");

        curieUom = new UnitMeasureCode();
        curieUom.setCode("CURIE");
        curieUom.setDescription("CURIES");
        curieUom.setUnitType("RADIOACTIVITY");
        curieUom.setCalculationVariable("1");
        curieUom.setFuelUseUom(false);
        curieUom.setHeatContentUom(false);

        lbUom = new UnitMeasureCode();
        lbUom.setCode("LB");
        lbUom.setDescription("POUNDS");
        lbUom.setUnitType("MASS");
        lbUom.setCalculationVariable("[lb]");
        lbUom.setFuelUseUom(true);
        lbUom.setHeatContentUom(false);

        tonUom = new UnitMeasureCode();
        tonUom.setCode("TON");
        tonUom.setDescription("TONS");
        tonUom.setUnitType("MASS");
        tonUom.setCalculationVariable("sTon");
        tonUom.setFuelUseUom(true);
        tonUom.setHeatContentUom(false);

        uomTon = new UnitMeasureCode();
        uomTon.setCode("TON");
        uomTon.setLegacy(false);
        uomTon.setFuelUseType("solid");
        uomTon.setUnitType("MASS");
        uomTon.setCalculationVariable("sTon");

        cmcFuel = new CalculationMaterialCode();
        cmcFuel.setCode("124");
        cmcFuel.setFuelUseMaterial(true);
        cmcFuel.setDescription("Fuel");

        btuUom = new UnitMeasureCode();
        btuUom.setCode("E6BTU");
        btuUom.setDescription("MILLION BTUS");
        btuUom.setUnitType("ENERGY");
        btuUom.setCalculationVariable("[M] * btu");
        btuUom.setFuelUseUom(true);
        btuUom.setHeatContentUom(true);

        e3ft3sUom = new UnitMeasureCode();
        e3ft3sUom.setCode("E3FT3S");
        e3ft3sUom.setDescription("1000 STANDARD CUBIC FEET");
        e3ft3sUom.setUnitType("SCF");
        e3ft3sUom.setCalculationVariable("1000");
        e3ft3sUom.setFuelUseUom(true);
        e3ft3sUom.setHeatContentUom(false);

        natGasCmc = new CalculationMaterialCode();
        natGasCmc.setCode("209");

        when(cefConfig.getEmissionsTotalErrorTolerance()).thenReturn(new BigDecimal(".05"));
        when(cefConfig.getEmissionsTotalWarningTolerance()).thenReturn(new BigDecimal(".01"));

        List<WebfireEmissionFactor> efList1 = new ArrayList<>();

        WebfireEmissionFactor ef1 = new WebfireEmissionFactor();
        ef1.setSccCode("10100101");
        ef1.setPollutantCode("NOX");
        ef1.setControlIndicator(false);
        ef1.setDescription("test description");
        ef1.setEmissionFactor(BigDecimal.valueOf(1));

        UnitMeasureCode num = new UnitMeasureCode();
        num.setCode("TON");

        UnitMeasureCode den = new UnitMeasureCode();
        den.setCode("E6BTU");

        ef1.setEmissionsNumeratorUom(num);
        ef1.setEmissionsDenominatorUom(den);
        ef1.setRevoked(false);
        efList1.add(ef1);

        WebfireEmissionFactor ef3 = new WebfireEmissionFactor();
        ef3.setSccCode("10100101");
        ef3.setPollutantCode("NOX");
        ef3.setControlIndicator(false);
        ef3.setDescription("test description");
        ef3.setFormulaIndicator(true);
        ef3.setEmissionFactorFormula("5.9*A");
        ef3.setEmissionsNumeratorUom(num);
        ef3.setEmissionsDenominatorUom(den);
        efList1.add(ef3);

        WebfireEmissionFactor ef4 = new WebfireEmissionFactor();
        ef4.setSccCode("10100101");
        ef4.setPollutantCode("NOX");
        ef4.setControlIndicator(false);
        ef4.setDescription("test description");
        ef4.setFormulaIndicator(true);
        ef4.setEmissionFactorFormula("39*SU");
        ef4.setEmissionsNumeratorUom(num);
        ef4.setEmissionsDenominatorUom(den);
        efList1.add(ef4);

        List<WebfireEmissionFactor> efList2 = new ArrayList<>();
        WebfireEmissionFactor ef2 = new WebfireEmissionFactor();
        ef2.setSccCode("10100101");
        ef2.setPollutantCode("605");
        ef2.setControlIndicator(false);
        ef2.setDescription("test description");
        ef2.setRevoked(false);
        efList2.add(ef2);

        List<WebfireEmissionFactor> efList3 = new ArrayList<>();
        WebfireEmissionFactor ef5 = new WebfireEmissionFactor();
        ef5.setSccCode("10100101");
        ef5.setPollutantCode("CO");
        ef5.setControlIndicator(false);
        ef5.setDescription("test description");
        ef5.setRevoked(true);
        ef5.setEmissionFactor(BigDecimal.valueOf(1));
        ef5.setEmissionsNumeratorUom(num);
        ef5.setEmissionsDenominatorUom(tonUom);
        efList3.add(ef5);

        List<WebfireEmissionFactor> efList4 = new ArrayList<>();
        WebfireEmissionFactor ef6 = new WebfireEmissionFactor();
        ef6.setSccCode("10100101");
        ef6.setPollutantCode("VOC");
        ef6.setControlIndicator(false);
        ef6.setDescription("test description");
        ef6.setEmissionFactor(BigDecimal.valueOf(1));
        ef6.setRevoked(true);
        ef6.setRevokedDate(new Date());
        ef6.setEmissionsNumeratorUom(num);
        ef6.setEmissionsDenominatorUom(den);
        efList4.add(ef6);

        WebfireEmissionFactor ef7 = new WebfireEmissionFactor();
        ef7.setSccCode("10100101");
        ef7.setPollutantCode("NOX");
        ef7.setControlIndicator(false);
        ef7.setDescription("test description");
        ef7.setFormulaIndicator(true);
        ef7.setEmissionFactorFormula("5.9*CS");
        ef7.setCondition("CS>0.4");
        ef7.setEmissionsNumeratorUom(num);
        ef7.setEmissionsDenominatorUom(den);
        efList1.add(ef7);

        PointSourceSccCode scc = new PointSourceSccCode();
        scc.setCode("10100101");
        scc.setFuelUseRequired(true);

        EmissionsProcess p = new EmissionsProcess();
        p.setSccCode("10100101");
        p.setEmissionsProcessIdentifier("Boiler 001");

        rpType = new ReportingPeriodCode();
        rpType.setCode("A");
        rpType.setShortName("Annual");

        ReportingPeriod rp = new ReportingPeriod();
        rp.setCalculationParameterValue(new BigDecimal(10));
        rp.setCalculationParameterUom(lbUom);
        rp.setReportingPeriodTypeCode(rpType);
        rp.setEmissionsProcess(p);
        rp.getEmissionsProcess().setEmissionsUnit(new EmissionsUnit());
        rp.getEmissionsProcess().getEmissionsUnit().setUnitIdentifier("Boiler 001");
        rp.getEmissionsProcess().getEmissionsUnit().setFacilitySite(new FacilitySite());

        when(webfireEfRepo.findBySccCodePollutantControlIndicator("10100101", "NOX", false)).thenReturn((efList1));
        when(webfireEfRepo.findBySccCodePollutantControlIndicator("10100101", "NOX", true)).thenReturn((efList1));
        when(webfireEfRepo.findBySccCodePollutantControlIndicator("10100101", "605", false)).thenReturn((efList2));
        when(webfireEfRepo.findBySccCodePollutantControlIndicator("10100101", "CO", false)).thenReturn((efList3));
        when(webfireEfRepo.findBySccCodePollutantControlIndicator("10100101", "VOC", false)).thenReturn((efList4));
        when(pointSourceSccCodeRepo.findById("10100101")).thenReturn(Optional.of(scc));

    }

    /**
     * There should be two errors when pollutant is 605 and UoM is not CURIE and when UoM is null
     */
    @Test
    public void pollutantCode605_UomFailTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(false);
        Pollutant pollutant = new Pollutant();
        pollutant.setPollutantCode("605");
        testData.setPollutant(pollutant);

        if(cefContext.result.getErrors() !=  null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }

        cefContext = createContext();
        testData = createBaseEmission(false);
        testData.setEmissionsUomCode(null);
        pollutant.setPollutantCode("605");
        testData.setPollutant(pollutant);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMapNext = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMapNext.containsKey(ValidationField.NOT_REPORTING.value()) && errorMapNext.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }
    }

    /**
     * There should be one error when Calculation Method Code has false total direct entry and Emission Total Emissions is not calculated correctly
     * and there should be no errors when totalManualEntry is true and when Emissions Process Operating Status is TEMPORARILY shutdown or PERMANENTLY shutdown.
     */
    @Test
    public void totalDirectEntryFalse_TotalEmissionsToleranceError_FailTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmissionHasFuelConversion();
        testData.setTotalEmissions(new BigDecimal("10.6"));
        testData.setCalculationComment("manually entered/calculated total emissions");
        testData.getEmissionsCalcMethodCode().setEpaEmissionFactor(true);
        testData.setFormulaIndicator(false);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }

        cefContext = createContext();
        testData.setTotalManualEntry(true);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }
        cefContext = createContext();
        testData.setTotalEmissions(new BigDecimal("9.4"));

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }

        cefContext = createContext();

        testData.setTotalManualEntry(false);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }

        cefContext = createContext();
        testData.getReportingPeriod().getEmissionsProcess().getOperatingStatusCode().setCode("TS");

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }
    }

    /**
     * There should be one warning when Calculation Method Code has false total direct entry and Emission Total Emissions is not calculated correctly
     * and there should be no errors when totalManualEntry is true and when Emissions Process Operating Status is TEMPORARILY shutdown or PERMANENTLY shutdown.
     */
    @Test
    public void totalDirectEntryFalse_TotalEmissionsToleranceWarning_FailTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmissionHasFuelConversion();
        testData.setTotalEmissions(new BigDecimal(".000962"));
        testData.setCalculationComment("manually entered/calculated total emissions");
        testData.getEmissionsCalcMethodCode().setEpaEmissionFactor(true);
        testData.setFormulaIndicator(false);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }

        cefContext = createContext();
        testData.setTotalManualEntry(true);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }

        cefContext = createContext();
        testData.setTotalEmissions(new BigDecimal(".000942"));

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }

        cefContext = createContext();
        testData.setTotalManualEntry(false);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMapNext = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMapNext.containsKey(ValidationField.NOT_REPORTING.value()) && errorMapNext.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }

        cefContext = createContext();
        testData.setTotalEmissions(new BigDecimal("0.000962"));

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMapNext = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMapNext.containsKey(ValidationField.NOT_REPORTING.value()) && errorMapNext.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }
    }

    /**
     * There should be one error when Calculation Method Code is Engineering Judgement (no ef, total direct entry is true) and a null comment
     */
    @Test
    public void totalDirectEntryTrue_NullComment_FailTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(true);
        testData.setComments(null);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }
    }

    /**
     * There should be one error when total emissions values is less than 0,
     * one error for total emissions are outside of tolerance,
     * and there should be no errors when value is greater than or equal to 0
     */
    @Test
    public void totalEmissionsValue_FailTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmissionHasFuelConversion();
        testData.setTotalEmissions(BigDecimal.ONE);
        testData.getEmissionsCalcMethodCode().setEpaEmissionFactor(true);
        testData.setFormulaIndicator(false);

        if(cefContext.result.getErrors() != null) {
            // calculated total emissions out of range
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }

        cefContext = createContext();
        testData.setTotalEmissions(new BigDecimal(-10));

        if(cefContext.result.getErrors() != null) {
            // invalid total emissions value
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
        }

        cefContext = createContext();
        testData.setTotalEmissions(new BigDecimal(0.00095));

        if(cefContext.result.getErrors() != null) {
            // pass case
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }
    }

    /**
     * There should be one error when total emissions has true total manual entry, has an ef, and a null calculation description
     */
    @Test
    public void totalManualEntryTrue_NullComment_FailTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(false);
        testData.setTotalManualEntry(true);
        testData.setCalculationComment(null);
        testData.setEmissionsFactor(new BigDecimal(0.005));

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }
    }

    /**
     * There should be one error when the Emission UoM is legacy
     */
    @Test
    public void legacyEmissionsUom_FailTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(true);
        testData.getEmissionsUomCode().setLegacy(true);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }
    }

    @Test
    public void emissions20PercentHigherFromPrevious_FailTest() {
        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(false);
        Emission previousEmission = createPreviousEmission();
        testData.setTotalEmissions(new BigDecimal("12"));
        testData.setEmissionsUomCode(tonUom);
        testData.setEmissionsNumeratorUom(tonUom);
        previousEmission.setEmissionsUomCode(tonUom);
        previousEmission.setEmissionsNumeratorUom(tonUom);

        when(cefConfig.getEmissionsTotalErrorTolerance()).thenReturn(new BigDecimal(".30"));
        when(cefConfig.getEmissionsTotalWarningTolerance()).thenReturn(new BigDecimal(".25"));

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }
    }

    /**
     * Total emissions cannot be 0 when the throughput value is greater than 0
     */
    @Test
    public void zeroEmissionsNonZeroThroughput() {
        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(true);

        testData.setTotalEmissions(BigDecimal.ZERO);
        testData.getReportingPeriod().setCalculationParameterValue(BigDecimal.TEN);

        ReportingPeriodCode rpType = new ReportingPeriodCode();
        rpType.setCode("A");
        rpType.setShortName("Annual");
        testData.getReportingPeriod().setReportingPeriodTypeCode(rpType);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }
    }

    @Test
    public void emissions20PercentLowerFromPrevious_FailTest() {
        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(false);
        Emission previousEmission = createPreviousEmission();

        testData.setCalculatedEmissionsTons(new BigDecimal("8")); // Used if UoMs don't match
        testData.setEmissionsUomCode(lbUom);
        testData.setEmissionsNumeratorUom(lbUom);
        previousEmission.setEmissionsUomCode(tonUom);
        previousEmission.setEmissionsNumeratorUom(tonUom);

        when(cefConfig.getEmissionsTotalErrorTolerance()).thenReturn(new BigDecimal(".30"));
        when(cefConfig.getEmissionsTotalWarningTolerance()).thenReturn(new BigDecimal(".25"));

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }
    }

    private Emission createBaseEmission(boolean totalDirectEntry) {

        Emission result = new Emission();

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);

        // when no ef is used totalDirectEntry is true
        CalculationMethodCode calcMethod = new CalculationMethodCode();
        calcMethod.setCode("2");
        calcMethod.setDescription("Engineering Judgment");
        calcMethod.setControlIndicator(false);
        calcMethod.setEpaEmissionFactor(false);
        calcMethod.setTotalDirectEntry(totalDirectEntry);
        result.setEmissionsCalcMethodCode(calcMethod);

        FacilitySourceTypeCode sourceType = new FacilitySourceTypeCode();
        sourceType.setCode("100");
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("OP");
        CalculationMaterialCode cmc = new CalculationMaterialCode();
        cmc.setCode("15");

        ReportingPeriod period = new ReportingPeriod();
        period.setCalculationMaterialCode(cmc);
        period.setCalculationParameterValue(new BigDecimal("10"));
        period.setCalculationParameterUom(tonUom);
        period.setReportingPeriodTypeCode(rpType);
        period.setEmissionsProcess(new EmissionsProcess());
        period.getEmissionsProcess().setOperatingStatusCode(opStatCode);
        period.getEmissionsProcess().setEmissionsProcessIdentifier("Boiler 001");
        period.getEmissionsProcess().setEmissionsUnit(new EmissionsUnit());
        period.getEmissionsProcess().getEmissionsUnit().setUnitIdentifier("Boiler 001");
        period.getEmissionsProcess().getEmissionsUnit().setFacilitySite(new FacilitySite());
        period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().setStatusYear((short) 2020);
        period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().setOperatingStatusCode(opStatCode);
        period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().setFacilitySourceTypeCode(sourceType);
        period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().setEmissionsReport(new EmissionsReport());
        period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().setYear(new Short("2019"));
        period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().setMasterFacilityRecord(mfr);
        period.getEmissionsProcess().setSccCode("10100101");
        period.setHeatContentUom(tonUom);
        period.setFuelUseMaterialCode(natGasCmc);
        period.setFuelUseUom(tonUom);
        period.setHeatContentValue(BigDecimal.valueOf(10));

        result.setReportingPeriod(period);

        result.setEmissionsUomCode(tonUom);

        result.setTotalEmissions(new BigDecimal("10"));
        result.setCalculatedEmissionsTons(new BigDecimal("10"));

        // when ef is used totalDirectEntry is false
        if (!totalDirectEntry) {
        	result.setFormulaIndicator(false);
        	result.setEmissionsDenominatorUom(tonUom);
            result.setEmissionsNumeratorUom(tonUom);
            result.setEmissionsFactor(new BigDecimal("1"));
            result.setTotalManualEntry(false);
            result.setEmissionsFactorText("❤🌈");

            WebfireEmissionFactor ef = new WebfireEmissionFactor();
            ef.setEmissionFactor(new BigDecimal(1));
            ef.setEmissionsNumeratorUom(tonUom);
            ef.setEmissionsDenominatorUom(tonUom);
            ef.setFormulaIndicator(false);
            ef.setQuality("A");
            ef.setRevoked(false);
            result.setWebfireEf(ef);
        } else {
            result.setComments("Comment");
            result.setTotalManualEntry(true);
        }

        Pollutant pollutant = new Pollutant();
        pollutant.setPollutantCode("NOX");
        result.setPollutant(pollutant);

        return result;
    }

    private Emission createBaseEmissionHasFuelConversion() {

    	Emission result = new Emission();

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);

        CalculationMethodCode calcMethod = new CalculationMethodCode();
        calcMethod.setCode("11");
        calcMethod.setDescription("Vendor Emission Factor (no Control Efficiency used)");
        calcMethod.setControlIndicator(false);
        calcMethod.setEpaEmissionFactor(false);
        calcMethod.setTotalDirectEntry(false);
        result.setEmissionsCalcMethodCode(calcMethod);

        FacilitySourceTypeCode sourceType = new FacilitySourceTypeCode();
        sourceType.setCode("100");

        ReportingPeriod period = new ReportingPeriod();
        period.setCalculationMaterialCode(natGasCmc);
        period.setCalculationParameterValue(new BigDecimal("1"));
        period.setCalculationParameterUom(e3ft3sUom);
        period.setReportingPeriodTypeCode(rpType);
        period.setEmissionsProcess(new EmissionsProcess());
        period.getEmissionsProcess().setEmissionsProcessIdentifier("Boiler 001");
        period.getEmissionsProcess().setSccCode("10100101");
        period.getEmissionsProcess().setOperatingStatusCode(opStatCode_op);
        period.getEmissionsProcess().setEmissionsUnit(new EmissionsUnit());
        period.getEmissionsProcess().getEmissionsUnit().setUnitIdentifier("Boiler 001");
        period.getEmissionsProcess().getEmissionsUnit().setFacilitySite(new FacilitySite());
        period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().setStatusYear((short) 2020);
        period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().setOperatingStatusCode(opStatCode_op);
        period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().setFacilitySourceTypeCode(sourceType_100);
        period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().setEmissionsReport(new EmissionsReport());
        period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().setYear(new Short("2019"));
        period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().setMasterFacilityRecord(mfr);
        period.setHeatContentUom(e3ft3sUom);
        period.setFuelUseMaterialCode(natGasCmc);
        period.setFuelUseUom(btuUom);
        period.setHeatContentValue(BigDecimal.valueOf(1050));

        result.setReportingPeriod(period);

        result.setEmissionsUomCode(tonUom);

        result.setTotalEmissions(new BigDecimal("10"));

        result.setEmissionsDenominatorUom(btuUom);
        result.setEmissionsNumeratorUom(tonUom);
        result.setEmissionsFactor(new BigDecimal("1"));
        result.setTotalManualEntry(false);
        result.setEmissionsFactorText("test description");

        Pollutant pollutant = new Pollutant();
        pollutant.setPollutantCode("NOX");
        result.setPollutant(pollutant);

        return result;
    }

    private Emission createPreviousEmission() {

        Emission result = createBaseEmission(false);

        List<EmissionsReport> erList = new ArrayList<>();

        ProgramSystemCode psc = new ProgramSystemCode();
        psc.setCode("GADNR");
        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);
        EmissionsReport er1 = new EmissionsReport();
        er1.setId(1L);
        er1.setEisProgramId("test_id");
        er1.setYear((short) 2018);
        er1.setMasterFacilityRecord(mfr);
        er1.setProgramSystemCode(psc);

        result.getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getFacilitySite().setStatusYear((short) 2020);
        result.getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getFacilitySite().setEmissionsReport(er1);

        List<Emission> previousEmissions = new ArrayList<Emission>();
        previousEmissions.add(result);

        erList.add(er1);

        when(reportRepo.findByMasterFacilityRecordId(er1.getMasterFacilityRecord().getId())).thenReturn(erList);

        when(emissionRepo.retrieveMatchingForYear(
            result.getPollutant().getPollutantCode(),
            result.getReportingPeriod().getReportingPeriodTypeCode().getCode(),
            result.getReportingPeriod().getEmissionsProcess().getEmissionsProcessIdentifier(),
            result.getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getUnitIdentifier(),
            erList.get(erList.size()-1).getEisProgramId(),
            erList.get(erList.size()-1).getYear()
        )).thenReturn(previousEmissions);

        return result;
    }

    @Test
    public void notReportingPreviousCurrentYearTest() {

        CefValidatorContext cefContext = createContext();
        createNotReportingPreviousReportingPeriods();
        ReportingPeriod testReportingPeriod = createNotReportingBaseReportingPeriod();

        List<Emission> emissionList = testReportingPeriod.getEmissions();
        emissionList.forEach(e -> {
            if(cefContext.result.getErrors() != null) {
                assertFalse(this.validator.validate(cefContext, e));
                assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
            }
        });
    }

    private ReportingPeriod createNotReportingBaseReportingPeriod() {

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

        List<Emission> emissionList = new ArrayList<>();
        Emission emission = new Emission();
        Pollutant pollutant = new Pollutant();
        pollutant.setPollutantCasId("100-02-7");
        pollutant.setPollutantName("4-Nitrophenol");
        pollutant.setPollutantCode("10027");
        emission.setPollutant(pollutant);
        emission.setNotReporting(true);
        emission.setReportingPeriod(result);
        emissionList.add(emission);
        result.setEmissions(emissionList);


        emissionsProcess.getReportingPeriods().add(result);
        emissionsUnit.getEmissionsProcesses().add(emissionsProcess);
        facilitySite.setEmissionsReport(report);
        facilitySite.getEmissionsUnits().add(emissionsUnit);

        return result;
    }

    private void createNotReportingPreviousReportingPeriods() {
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

        List<Emission> emissionList = new ArrayList<>();
        Emission emission = new Emission();
        Pollutant pollutant = new Pollutant();
        pollutant.setPollutantCasId("100-02-7");
        pollutant.setPollutantName("4-Nitrophenol");
        pollutant.setPollutantCode("10027");
        emission.setPollutant(pollutant);
        emission.setNotReporting(true);
        emission.setReportingPeriod(previousPeriod);
        emissionList.add(emission);

        previousPeriod.setEmissions(emissionList);
        List<ReportingPeriod> previousPeriods = new ArrayList<>();
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
            erList.get(erList.size() - 1).getYear()
        )).thenReturn(previousPeriods);
    }
}
