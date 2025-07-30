/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.validation.validator;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import gov.epa.cef.web.config.CefConfig;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.GHGEmissionFactorRepository;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.repository.WebfireEmissionFactorRepository;
import gov.epa.cef.web.service.dto.EmissionFactorDto;
import gov.epa.cef.web.service.dto.UnitMeasureCodeDto;
import gov.epa.cef.web.service.impl.EmissionServiceImpl;
import gov.epa.cef.web.service.mapper.EmissionFactorMapper;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.federal.SharedEmissionValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SharedEmissionValidatorTest extends BaseValidatorTest {

    @InjectMocks
    private SharedEmissionValidator validator;

    @Mock
    private WebfireEmissionFactorRepository webfireEfRepo;

    @Mock
    private GHGEmissionFactorRepository ghgEfRepo;

    @Mock
    private EmissionFactorMapper emissionFactorMapper;

    @Mock
    private PointSourceSccCodeRepository pointSourceSccCodeRepo;

    @Mock
    private CefConfig cefConfig;

    @Spy
    private EmissionServiceImpl emissionService;

    private UnitMeasureCode curieUom;
    private UnitMeasureCode lbUom;
    private UnitMeasureCode tonUom;
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

        CalculationMaterialCode woodCmc = new CalculationMaterialCode();
        woodCmc.setCode("15");
        natGasCmc = new CalculationMaterialCode();
        natGasCmc.setCode("209");

        when(cefConfig.getEmissionsTotalErrorTolerance()).thenReturn(new BigDecimal(".05"));
        when(cefConfig.getEmissionsTotalWarningTolerance()).thenReturn(new BigDecimal(".01"));

        List<WebfireEmissionFactor> efList1 = new ArrayList<>();
        List<EmissionFactorDto> efDtoList1 = new ArrayList<>();

        String efSccCode10100101 = "10100101";
        String pollutantCodeNOX = "NOX";
        String pollutantCodeCO = "CO";
        boolean efControlIndicatorFalse = false;
        String efDescription1 = "test description";
        boolean efFormulaIndicatorTrue = true;
        boolean efRevokedFalse = false;
        boolean efRevokedTrue = true;
        String efPollutantCode605 = "605";

        BigDecimal ef1EmissionFactor = BigDecimal.valueOf(1);
        WebfireEmissionFactor ef1 = new WebfireEmissionFactor();
        ef1.setSccCode(efSccCode10100101);
        ef1.setPollutantCode(pollutantCodeNOX);
        ef1.setControlIndicator(efControlIndicatorFalse);
        ef1.setDescription(efDescription1);
        ef1.setEmissionFactor(ef1EmissionFactor);
        EmissionFactorDto efDto1 = new EmissionFactorDto();
        efDto1.setSccCode(efSccCode10100101);
        efDto1.setPollutantCode(pollutantCodeNOX);
        efDto1.setControlIndicator(efControlIndicatorFalse);
        efDto1.setDescription(efDescription1);
        efDto1.setEmissionFactor(ef1EmissionFactor);

        String numUomCode = "TON";
        UnitMeasureCode num = new UnitMeasureCode();
        num.setCode(numUomCode);
        UnitMeasureCodeDto numDto = new UnitMeasureCodeDto();
        numDto.setCode(numUomCode);

        String denUomCode = "E6BTU";
        UnitMeasureCode den = new UnitMeasureCode();
        den.setCode(denUomCode);
        UnitMeasureCodeDto denDto = new UnitMeasureCodeDto();
        denDto.setCode(denUomCode);

        ef1.setEmissionsNumeratorUom(num);
        ef1.setEmissionsDenominatorUom(den);
        ef1.setRevoked(efRevokedFalse);
        efList1.add(ef1);
        efDto1.setEmissionsNumeratorUom(numDto);
        efDto1.setEmissionsDenominatorUom(denDto);
        efDto1.setRevoked(efRevokedFalse);
        efDtoList1.add(efDto1);

        String ef3EmissionFactorFormula = "5.9*A";
        WebfireEmissionFactor ef3 = new WebfireEmissionFactor();
        ef3.setSccCode(efSccCode10100101);
        ef3.setPollutantCode(pollutantCodeNOX);
        ef3.setControlIndicator(efControlIndicatorFalse);
        ef3.setDescription(efDescription1);
        ef3.setFormulaIndicator(efFormulaIndicatorTrue);
        ef3.setEmissionFactorFormula(ef3EmissionFactorFormula);
        ef3.setEmissionsNumeratorUom(num);
        ef3.setEmissionsDenominatorUom(den);
        EmissionFactorDto efDto3 = new EmissionFactorDto();
        efDto3.setSccCode(efSccCode10100101);
        efDto3.setPollutantCode(pollutantCodeNOX);
        efDto3.setControlIndicator(efControlIndicatorFalse);
        efDto3.setDescription(efDescription1);
        efDto3.setFormulaIndicator(efFormulaIndicatorTrue);
        efDto3.setEmissionFactorFormula(ef3EmissionFactorFormula);
        efDto3.setEmissionsNumeratorUom(numDto);
        efDto3.setEmissionsDenominatorUom(denDto);
        efList1.add(ef3);
        efDtoList1.add(efDto3);

        String efFormula39SU = "39*SU";
        WebfireEmissionFactor ef4 = new WebfireEmissionFactor();
        ef4.setSccCode(efSccCode10100101);
        ef4.setPollutantCode(pollutantCodeNOX);
        ef4.setControlIndicator(efControlIndicatorFalse);
        ef4.setDescription(efDescription1);
        ef4.setFormulaIndicator(efFormulaIndicatorTrue);
        ef4.setEmissionFactorFormula(efFormula39SU);
        ef4.setEmissionsNumeratorUom(num);
        ef4.setEmissionsDenominatorUom(den);
        EmissionFactorDto efDto4 = new EmissionFactorDto();
        efDto4.setSccCode(efSccCode10100101);
        efDto4.setPollutantCode(pollutantCodeNOX);
        efDto4.setControlIndicator(efControlIndicatorFalse);
        efDto4.setDescription(efDescription1);
        efDto4.setFormulaIndicator(efFormulaIndicatorTrue);
        efDto4.setEmissionFactorFormula(efFormula39SU);
        efDto4.setEmissionsNumeratorUom(numDto);
        efDto4.setEmissionsDenominatorUom(denDto);
        efList1.add(ef4);
        efDtoList1.add(efDto4);

        List<WebfireEmissionFactor> efList2 = new ArrayList<>();
        List<EmissionFactorDto> efDtoList2 = new ArrayList<>();
        WebfireEmissionFactor ef2 = new WebfireEmissionFactor();
        ef2.setSccCode(efSccCode10100101);
        ef2.setPollutantCode(efPollutantCode605);
        ef2.setControlIndicator(efControlIndicatorFalse);
        ef2.setDescription(efDescription1);
        ef2.setRevoked(efRevokedFalse);
        EmissionFactorDto efDto2 = new EmissionFactorDto();
        efDto2.setSccCode(efSccCode10100101);
        efDto2.setPollutantCode(efPollutantCode605);
        efDto2.setControlIndicator(efControlIndicatorFalse);
        efDto2.setDescription(efDescription1);
        efDto2.setRevoked(efRevokedFalse);
        efList2.add(ef2);
        efDtoList2.add(efDto2);

        UnitMeasureCodeDto tonUomDto = new UnitMeasureCodeDto();
        tonUomDto.setCode("TON");
        tonUomDto.setDescription("TONS");
        tonUomDto.setUnitType("MASS");
        tonUomDto.setFuelUseUom(true);
        tonUomDto.setHeatContentUom(false);

        List<WebfireEmissionFactor> efList3 = new ArrayList<>();
        List<EmissionFactorDto> efListDto3 = new ArrayList<>();
        WebfireEmissionFactor ef5 = new WebfireEmissionFactor();
        ef5.setSccCode(efSccCode10100101);
        ef5.setPollutantCode(pollutantCodeCO);
        ef5.setControlIndicator(efControlIndicatorFalse);
        ef5.setDescription(efDescription1);
        ef5.setRevoked(efRevokedTrue);
        ef5.setEmissionFactor(BigDecimal.valueOf(1));
        ef5.setEmissionsNumeratorUom(num);
        ef5.setEmissionsDenominatorUom(tonUom);
        EmissionFactorDto efDto5 = new EmissionFactorDto();
        efDto5.setSccCode(efSccCode10100101);
        efDto5.setPollutantCode(pollutantCodeCO);
        efDto5.setControlIndicator(efControlIndicatorFalse);
        efDto5.setDescription(efDescription1);
        efDto5.setRevoked(efRevokedTrue);
        efDto5.setEmissionFactor(BigDecimal.valueOf(1));
        efDto5.setEmissionsNumeratorUom(numDto);
        efDto5.setEmissionsDenominatorUom(tonUomDto);
        efList3.add(ef5);

        List<WebfireEmissionFactor> efList4 = new ArrayList<>();List<EmissionFactorDto> efDtoList4 = new ArrayList();
        WebfireEmissionFactor ef6 = new WebfireEmissionFactor();
        ef6.setSccCode(efSccCode10100101);
        ef6.setPollutantCode("VOC");
        ef6.setControlIndicator(efControlIndicatorFalse);
        ef6.setDescription(efDescription1);
        ef6.setEmissionFactor(BigDecimal.valueOf(1));
        ef6.setRevoked(efRevokedTrue);
        ef6.setRevokedDate(new Date());
        ef6.setEmissionsNumeratorUom(num);
        ef6.setEmissionsDenominatorUom(den);
        efList4.add(ef6);

        String ef7EmissionFactorFormula = "5.9*CS";
        String ef7Condition = "CS>0.4";
        WebfireEmissionFactor ef7 = new WebfireEmissionFactor();
        ef7.setSccCode(efSccCode10100101);
        ef7.setPollutantCode(pollutantCodeNOX);
        ef7.setControlIndicator(efControlIndicatorFalse);
        ef7.setDescription(efDescription1);
        ef7.setFormulaIndicator(efFormulaIndicatorTrue);
        ef7.setEmissionFactorFormula(ef7EmissionFactorFormula);
        ef7.setCondition(ef7Condition);
        ef7.setEmissionsNumeratorUom(num);
        ef7.setEmissionsDenominatorUom(den);
        EmissionFactorDto efDto7 = new EmissionFactorDto();
        efDto7.setSccCode(efSccCode10100101);
        efDto7.setPollutantCode(pollutantCodeNOX);
        efDto7.setControlIndicator(efControlIndicatorFalse);
        efDto7.setDescription(efDescription1);
        efDto7.setFormulaIndicator(efFormulaIndicatorTrue);
        efDto7.setEmissionFactorFormula(ef7EmissionFactorFormula);
        efDto7.setCondition(ef7Condition);
        efDto7.setEmissionsNumeratorUom(numDto);
        efDto7.setEmissionsDenominatorUom(denDto);
        efList1.add(ef7);
        efDtoList1.add(efDto7);

        List<GHGEmissionFactor> ghgEfList1 = new ArrayList<>();

        PointSourceSccCode scc = new PointSourceSccCode();
        scc.setCode(efSccCode10100101);
        scc.setFuelUseRequired(true);

        EmissionsProcess p = new EmissionsProcess();
        p.setSccCode(efSccCode10100101);
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

        when(webfireEfRepo.findBySccCodePollutantControlIndicator(efSccCode10100101, pollutantCodeNOX, efControlIndicatorFalse)).thenReturn((efList1));
        when(webfireEfRepo.findBySccCodePollutantControlIndicator(efSccCode10100101, pollutantCodeNOX, true)).thenReturn((efList1));
        when(webfireEfRepo.findBySccCodePollutantControlIndicator(efSccCode10100101, efPollutantCode605, efControlIndicatorFalse)).thenReturn((efList2));
        when(webfireEfRepo.findBySccCodePollutantControlIndicator(efSccCode10100101, pollutantCodeCO, efControlIndicatorFalse)).thenReturn((efList3));
        when(webfireEfRepo.findBySccCodePollutantControlIndicator(efSccCode10100101, "VOC", efControlIndicatorFalse)).thenReturn((efList4));
        when(pointSourceSccCodeRepo.findById(efSccCode10100101)).thenReturn(Optional.of(scc));

        when(ghgEfRepo.findByPollutantThroughputMaterialThroughputTypeControlIndicator("CO2", efControlIndicatorFalse, "173", "I")).thenReturn((ghgEfList1));
        when(emissionFactorMapper.toGHGEfDtoList(ghgEfList1)).thenReturn(efDtoList1);
        when(emissionFactorMapper.toWebfireEfDtoList(efList1)).thenReturn(efDtoList1);
    }

    @Test
    public void totalDirectEntryFalse_PassTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(false);
        // totalDirectEntry is false when ef is used

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }
    }

    @Test
    public void totalDirectEntryTrue_PassTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(true);
        // totalDirectEntry is true when ef is not used

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }
    }

    @Test
    public void formula_PassTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseFormulaEmission();

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }

        testData.setEmissionsFactor(null);
        testData.setTotalManualEntry(true);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }
    }

    /**
     * There should be one error when pollutant last inventory year is less than the emission report year
     */
    @Test
    public void pollutantLastInvYear_reportYear_FailTest() {
    	CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(true);
        testData.getPollutant().setLastInventoryYear(1900);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }
    }

    /**
     * There should be one error when Calculation Method Code has true total direct entry and
     * the reporting period's calculation parameter value is zero but the total emissions is not zero
     */
//    @Test
//    public void totalDirectEntryTrue_NonZeroTotalEmissions_FailTest() {
//
//        CefValidatorContext cefContext = createContext();
//        Emission testData = createBaseEmission(true);
//        testData.getReportingPeriod().setCalculationParameterValue(BigDecimal.ZERO);
//        testData.setTotalEmissions(BigDecimal.ONE);
//
//        assertFalse(this.validator.validate(cefContext, testData));
//        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
//
//        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
//        assertTrue(errorMap.containsKey(ValidationField.EMISSION_TOTAL_EMISSIONS.value()) && errorMap.get(ValidationField.EMISSION_TOTAL_EMISSIONS.value()).size() == 1);
//    }

    /**
     * There should be one error when Calculation Method Code has false total direct entry and a null Emissions Factor
     */
    @Test
    public void totalDirectEntryFalse_NullEmissionsFactor_FailTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(false);
        testData.setEmissionsFactor(null);
        testData.setEmissionsDenominatorUom(null);
        testData.setEmissionsNumeratorUom(null);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }
    }

    /**
     * There should be one error when no Emission Factors exist for SCC and pollutant combo
     */
    @Test
    public void noMatchEmissionsFactor_FailTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(false);
        testData.getPollutant().setPollutantCode("invalid");
        testData.getEmissionsCalcMethodCode().setEpaEmissionFactor(true);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }
    }

    /**
     * There should be one error when ef is epa ef, epa ef matched one from the db, and status is revoked
     */
    @Test
    public void epaEfRevoked_FailTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(false);
        testData.getEmissionsCalcMethodCode().setEpaEmissionFactor(true);
        Pollutant pollutant = new Pollutant();
        pollutant.setPollutantCode("CO");
        testData.setPollutant(pollutant);
        testData.setEmissionsFactorText("test description");
        testData.setEmissionsFactor(BigDecimal.valueOf(1));
        testData.setTotalEmissions(BigDecimal.valueOf(10));

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }
    }

    /**
     * There should be one error when Emissions Factor doesn't match one that does exists for SCC and pollutant combo
     */
    @Test
    public void selectNewEmissionsFactor_FailTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(false);
        testData.getEmissionsCalcMethodCode().setEpaEmissionFactor(true);
        testData.setEmissionsFactor(BigDecimal.ONE);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }
    }

    /**
     * There should be two errors when Emissions Factor is null and Numerator and Denominator UoM are non-null
     */
    @Test
    public void nullEmissionsFactor_NonNullDenomNum_FailTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(true);
        testData.setFormulaIndicator(false);
        testData.setEmissionsDenominatorUom(new UnitMeasureCode());
        testData.setEmissionsNumeratorUom(new UnitMeasureCode());

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }
    }

    /**
     * There should be one error when Emissions Factor is less than or equal to zero and the Numerator and Denominator are non-null
     */
    @Test
    public void equalToOrLessThanZeroEmissionsFactor_FailTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(true);
        testData.setEmissionsFactor(BigDecimal.valueOf(0));
        testData.setEmissionsDenominatorUom(new UnitMeasureCode());
        testData.setEmissionsNumeratorUom(new UnitMeasureCode());
        testData.setEmissionsCalcMethodCode(new CalculationMethodCode());
        testData.getEmissionsCalcMethodCode().setControlIndicator(false);
        testData.getEmissionsCalcMethodCode().setTotalDirectEntry(false);
        testData.getEmissionsCalcMethodCode().setEpaEmissionFactor(false);
        testData.setCalculationComment("manually entered/calculated total emissions");
        testData.setEmissionsFactorText("❤🌈 ");

        if (cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }
    }

    /**
     * There should be two errors when Emissions Factor is non-null and Numerator and Denominator UoM are null
     */
    @Test
    public void nonNullEmissionsFactor_NullDenomNum_FailTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(false);
        testData.setEmissionsDenominatorUom(null);
        testData.setEmissionsNumeratorUom(null);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }
    }

    /**
     * There should be two errors when Emissions Factor is non-null and Numerator and Denominator UoM are legacy
     */
    @Test
    public void nonNullEmissionsFactor_LegacyDenomNum_FailTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(false);
        // need to change to avoid other validations triggering
        testData.setEmissionsUomCode(lbUom);
        testData.setTotalManualEntry(true);
        tonUom.setLegacy(true);
        testData.setCalculationComment("manually entered/calculated total emissions");

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }
    }

    @Test
    public void totalDirectEntryFalse_DenomMismatch_PassTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(false);
        testData.setEmissionsDenominatorUom(tonUom);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }

        cefContext = createContext();

		ReportingPeriod period = new ReportingPeriod();

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);

        period.setCalculationMaterialCode(natGasCmc);
        period.setCalculationParameterValue(new BigDecimal("10"));
        period.setCalculationParameterUom(e3ft3sUom);
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
        period.setReportingPeriodTypeCode(rpType);
        testData.setReportingPeriod(period);
        testData.setTotalEmissions(new BigDecimal("0.0095"));
        testData.setEmissionsDenominatorUom(btuUom);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }

    }

    /**
     * There should be one error when Calculation Method Code has false total direct entry and Emission Denominator UoM type doesn't equal Throughput UoM type
     * and there should be no errors when totalManualEntry is true and when Emissions Process Operating Status is TEMPORARILY shutdown or PERMANENTLY shutdown.
     */
    @Test
    public void totalDirectEntryFalse_DenomMismatch_FailTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(false);
        testData.setEmissionsDenominatorUom(curieUom);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }

		cefContext = createContext();

		ReportingPeriod period = new ReportingPeriod();

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);

        period.setCalculationMaterialCode(natGasCmc);
        period.setCalculationParameterValue(new BigDecimal("209"));
        period.setCalculationParameterUom(e3ft3sUom);
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
        period.setReportingPeriodTypeCode(rpType);
        testData.setReportingPeriod(period);
        testData.setTotalEmissions(new BigDecimal("0.000198"));
        testData.setEmissionsDenominatorUom(tonUom);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
        }

        cefContext = createContext();
        CalculationMaterialCode cmc = new CalculationMaterialCode();
        cmc.setCode("763");
        period.setCalculationMaterialCode(cmc);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
        }

        cefContext = createContext();
        testData.getReportingPeriod().getEmissionsProcess().getOperatingStatusCode().setCode("TS");

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }

        cefContext = createContext();
        testData.setTotalManualEntry(true);

        if (cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }
    }

    /**
     * There should be one error when Calculation Method Code has false total direct entry and Emission Numerator UoM type doesn't equal Total Emissions UoM type.
     * There should be one error when ef numerator uom does not match emissions numerator uom.
     * There should be no errors when totalManualEntry is true and when Emissions Process Operating Status is TEMPORARILY shutdown or PERMANENTLY shutdown.
     */
    @Test
    public void totalDirectEntryFalse_NumMismatch_FailTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(false);
        testData.setEmissionsNumeratorUom(curieUom);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }

        cefContext = createContext();
        testData.getReportingPeriod().getEmissionsProcess().getOperatingStatusCode().setCode("PS");

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }

        cefContext = createContext();
        testData.setTotalManualEntry(true);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }
    }

    @Test
    public void totalDirectEntryFalse_TotalEmissionsToleranceWarning_BoundaryTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(false);
        testData.setTotalEmissions(new BigDecimal("10.1"));

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }

        cefContext = createContext();
        testData.setTotalEmissions(new BigDecimal("9.9"));

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }

        cefContext = createContext();
        testData.getReportingPeriod().getEmissionsProcess().getOperatingStatusCode().setCode("PS");

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }

    }

     /**
     * There should be two errors when emission formula variable value for % ash is less than 0.01 and greater than 30,
     * and there should be no errors when value is within range.
     */
    @Test
    public void percentAsh_Range_FailTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseFormulaEmission();
        EmissionFormulaVariableCode ashCode = new EmissionFormulaVariableCode();
        ashCode.setCode("A");
        EmissionFormulaVariable var1 = new EmissionFormulaVariable();
        var1.setValue(BigDecimal.valueOf(0.005));
        var1.setVariableCode(ashCode);

        testData.getVariables().clear();
        testData.getVariables().add(var1);
        testData.setFormulaIndicator(true);
        testData.setEmissionsFactorFormula("5.9*A");
        testData.setEmissionsDenominatorUom(btuUom);
        testData.setEmissionsFactorText("test description");
        testData.setTotalEmissions(BigDecimal.valueOf(104));
        testData.setTotalManualEntry(false);

        CalculationMethodCode epaCalcMethod = new CalculationMethodCode();
        epaCalcMethod.setCode("8");
        epaCalcMethod.setEpaEmissionFactor(true);
        epaCalcMethod.setTotalDirectEntry(false);
        epaCalcMethod.setControlIndicator(false);
        epaCalcMethod.setDescription("USEPA Emission Factor (no Control Efficiency used)");
        testData.setEmissionsCalcMethodCode(epaCalcMethod);
        var1.setEmission(testData);

        if(cefContext.result.getErrors() != null) {
            // fails when ash value is less than 0.01
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }

        cefContext = createContext();
        var1.setValue(new BigDecimal(31));

        if(cefContext.result.getErrors() != null) {
            // fails when ash value is greater than 30
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
        }

        cefContext = createContext();
        var1.setValue(new BigDecimal(0.01));

        if(cefContext.result.getErrors() != null) {
            // passes when 0.01 <= ash value <= 30
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }
    }

    /**
     * There should be two errors when emission formula variable value for % sulfur is less than 0.00001 and greater than 10,
     * and there should be no errors when value is within range.
     */
    @Test
    public void percentSulfur_Range_FailTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseFormulaEmission();
        EmissionFormulaVariableCode sulfurCode = new EmissionFormulaVariableCode();
        sulfurCode.setCode("SU");
        EmissionFormulaVariable var1 = new EmissionFormulaVariable();
        var1.setValue(BigDecimal.ZERO);
        var1.setVariableCode(sulfurCode);
        testData.getVariables().clear();
        testData.getVariables().add(var1);
        testData.setFormulaIndicator(true);
        testData.setEmissionsDenominatorUom(btuUom);
        testData.setEmissionsFactorText("test description");
        testData.setEmissionsFactorFormula("39*SU");
        testData.setTotalEmissions(BigDecimal.valueOf(104));
        testData.setTotalManualEntry(false);

        CalculationMethodCode epaCalcMethod = new CalculationMethodCode();
        epaCalcMethod.setCode("8");
        epaCalcMethod.setEpaEmissionFactor(true);
        epaCalcMethod.setTotalDirectEntry(false);
        epaCalcMethod.setControlIndicator(false);
        epaCalcMethod.setDescription("USEPA Emission Factor (no Control Efficiency used)");
        testData.setEmissionsCalcMethodCode(epaCalcMethod);

        if(cefContext.result.getErrors() != null) {
            // fails when sulfur value is less than 0.00001
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }

        cefContext = createContext();
        var1.setValue(new BigDecimal(11));

        if(cefContext.result.getErrors() != null) {
            // fails when sulfur value is greater than 10
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
        }

        cefContext = createContext();
        var1.setValue(new BigDecimal(10));

        if(cefContext.result.getErrors() != null) {
            // passes when 0.00001 <= sulfur value <= 10
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
        }
    }

   /**
    * There should be no errors when facility with status of not OP is a landfill or if the status year is > current cycle year
    */
   @Test
   public void facilityNotOperatingReportEmissionsPassTest() {

       CefValidatorContext cefContext = createContext();
       Emission testData = createBaseEmission(false);
       testData.getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getFacilitySite().getOperatingStatusCode().setCode("TS");

       if(cefContext.result.getErrors() != null) {
           assertFalse(this.validator.validate(cefContext, testData));
           assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
       }

       cefContext = createContext();
       testData.getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getFacilitySite().getFacilitySourceTypeCode().setCode("104");
       testData.getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getFacilitySite().setStatusYear((short) 2000);


       if(cefContext.result.getErrors() != null) {
           assertFalse(this.validator.validate(cefContext, testData));
           assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
       }
   }

   /**
    * There should be errors for overall control percentage when the overall control percent is < 0 or >= 100.
    * There are errors for emissions outside of tolerance range due to control percentage.
    */
   @Test
   public void overallControlPercentRangeTest() {

       CefValidatorContext cefContext = createContext();
       Emission testData = createBaseEmissionHasFuelConversion();
       testData.setOverallControlPercent(new BigDecimal(-10));
       testData.getEmissionsCalcMethodCode().setEpaEmissionFactor(true);
       testData.setFormulaIndicator(false);

       addReleasePointAppts(testData);

       if(cefContext.result.getErrors() != null) {
           assertFalse(this.validator.validate(cefContext, testData));
           assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

           Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
           assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
       }

       cefContext = createContext();
       testData.setOverallControlPercent(new BigDecimal(110));

       if(cefContext.result.getErrors() != null) {
           assertFalse(this.validator.validate(cefContext, testData));
           assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);


           Map<String, List<ValidationError>> errorMapNext = mapErrors(cefContext.result.getErrors());
           assertTrue(errorMapNext.containsKey(ValidationField.NOT_REPORTING.value()) && errorMapNext.get(ValidationField.NOT_REPORTING.value()).size() == 1);
       }
   }

   /**
    * There should be one error when calculation method includes control efficiency and overall control percent is > 0
    */
   @Test
   public void calcMethodWithControlEfficiencyAndControlPercent_FailTest() {

       CefValidatorContext cefContext = createContext();
       Emission testData = createBaseEmission(false);
       testData.setOverallControlPercent(new BigDecimal(0.5));
       testData.getEmissionsCalcMethodCode().setCode("33");
       testData.getEmissionsCalcMethodCode().setDescription("Other Emission Factor (pre-control) plus Control Efficiency");
       testData.getEmissionsCalcMethodCode().setControlIndicator(true);

       addReleasePointAppts(testData);

       if(cefContext.result.getErrors() != null) {
           assertFalse(this.validator.validate(cefContext, testData));
           assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

           Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
           assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
       }
   }

   @Test
   public void emissionsCalcMethodIsEmissionFactorAndNoDescription_FailTest() {
	   CefValidatorContext cefContext = createContext();
	   Emission testData = createBaseEmission(false);
	   testData.setEmissionsFactorText(null);

       if(cefContext.result.getErrors() != null) {
           assertFalse(this.validator.validate(cefContext, testData));
           assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

           Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
           assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
       }

       cefContext = createContext();
	   testData.setEmissionsFactorText("");

       if (cefContext.result.getErrors() != null) {
           assertFalse(this.validator.validate(cefContext, testData));
           assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

           Map<String, List<ValidationError>> errorMapNext = mapErrors(cefContext.result.getErrors());
           assertTrue(errorMapNext.containsKey(ValidationField.NOT_REPORTING.value()) && errorMapNext.get(ValidationField.NOT_REPORTING.value()).size() == 1);
       }

       cefContext = createContext();
	   testData.setEmissionsFactorText("");

	   CalculationMethodCode epaCalcMethod = new CalculationMethodCode();
       epaCalcMethod.setCode("8");
       epaCalcMethod.setEpaEmissionFactor(true);
       epaCalcMethod.setTotalDirectEntry(false);
       epaCalcMethod.setControlIndicator(false);
       epaCalcMethod.setDescription("USEPA Emission Factor (no Control Efficiency used)");
	   testData.setEmissionsCalcMethodCode(epaCalcMethod);

       if (cefContext.result.getErrors() != null) {
           // fails when epa ef is not found in the db, and an error when epa ef does not have description
           assertFalse(this.validator.validate(cefContext, testData));
           assertFalse(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);
       }
   }

   @Test
   public void emissionIsTotalManualEntryAndNoEFDescription_PassTest() {
	   CefValidatorContext cefContext = createContext();
	   Emission testData = createBaseEmission(true);
	   testData.setEmissionsFactorText(null);
	   testData.setTotalManualEntry(true);

       if(cefContext.result.getErrors() != null) {
           assertFalse(this.validator.validate(cefContext, testData));
           assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
       }
   }

   @Test
   public void emissionCalcMethodIsNotEmissionFactorAndNoDescription_PassTest() {
	   CefValidatorContext cefContext = createContext();
       Emission testData = createBaseEmission(true);

       if(cefContext.result.getErrors() != null) {
           assertFalse(this.validator.validate(cefContext, testData));
           assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
       }
   }

   @Test
   public void emissionFormualVarAndNoEmissionFormula_FailTest() {
	   CefValidatorContext cefContext = createContext();
	   Emission testData = createBaseFormulaEmission();

	   testData.setFormulaIndicator(false);

       if(cefContext.result.getErrors() != null) {
           assertFalse(this.validator.validate(cefContext, testData));
           assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

           Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
           assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
       }
   }

   @Test
   public void emissionFactorRange_FailTest() {
	   CefValidatorContext cefContext = createContext();
	   Emission testData = createBaseEmission(false);

	   testData.setFormulaIndicator(false);
	   testData.getWebfireEf().setMinValue(BigDecimal.ONE);
	   testData.getWebfireEf().setMaxValue(BigDecimal.TEN);
	   testData.setEmissionsFactor(new BigDecimal(11));
	   testData.setTotalEmissions(new BigDecimal(110));

       if(cefContext.result.getErrors() != null) {
           assertFalse(this.validator.validate(cefContext, testData));
           assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

           Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
           assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
       }
   }

   @Test
   public void emissionFactorRange_PassTest() {
	   CefValidatorContext cefContext = createContext();
	   Emission testData = createBaseEmission(false);

	   testData.setFormulaIndicator(false);
	   testData.getWebfireEf().setMinValue(BigDecimal.ONE);
	   testData.getWebfireEf().setMaxValue(BigDecimal.TEN);
	   testData.setEmissionsFactor(new BigDecimal(5));
	   testData.setTotalEmissions(new BigDecimal(50));

       if(cefContext.result.getErrors() != null) {
           assertFalse(this.validator.validate(cefContext, testData));
           assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
       }
   }

   @Test
   public void emissionFormulaVariableRange_FailTest() {
	   CefValidatorContext cefContext = createContext();
	   Emission testData = createBaseFormulaEmission();

	   testData.getVariables().get(0).setValue(new BigDecimal(0.2));

       if(cefContext.result.getErrors() != null) {
           assertFalse(this.validator.validate(cefContext, testData));
           assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

           Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
           assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
       }
   }

   @Test
   public void emissionFactorQualityU_FailTest() {
	   CefValidatorContext cefContext = createContext();
	   Emission testData = createBaseEmission(false);

	   testData.getWebfireEf().setQuality("U");

       if(cefContext.result.getErrors() != null) {
           assertFalse(this.validator.validate(cefContext, testData));
           assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

           Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
           assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
       }
   }

   @Test
   public void emissionFactorRevokedAfterReportYear_PassTest() {
	   CefValidatorContext cefContext = createContext();
	   Emission testData = createBaseEmission(false);

	   CalculationMethodCode epaCalcMethod = new CalculationMethodCode();
       epaCalcMethod.setCode("8");
       epaCalcMethod.setEpaEmissionFactor(true);
       epaCalcMethod.setTotalDirectEntry(false);
       epaCalcMethod.setControlIndicator(false);
       epaCalcMethod.setDescription("USEPA Emission Factor (no Control Efficiency used)");
	   testData.setEmissionsCalcMethodCode(epaCalcMethod);

	   Pollutant pollutant = new Pollutant();
       pollutant.setPollutantCode("VOC");
       testData.setPollutant(pollutant);

	   testData.setEmissionsDenominatorUom(btuUom);
	   testData.setEmissionsFactorText("test description");

	   testData.getReportingPeriod().setCalculationParameterUom(btuUom);

	   testData.getWebfireEf().setRevoked(true);
	   testData.getWebfireEf().setRevokedDate(new Date());

       if(cefContext.result.getErrors() != null) {
           // test should pass as EFs can be used on reports that are before their revoked dates
           assertFalse(this.validator.validate(cefContext, testData));
           assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
       }
   }

    @Test
    public void emissionsCurieNonRadionuclides_FailTest() {
        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission(false);

        testData.setEmissionsNumeratorUom(curieUom);
        testData.setEmissionsUomCode(curieUom);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }
    }

    /**
     * Emissions cannot have a control percent value when there are no controls for the pollutant associated to the process
     */
    @Test
    public void overallControlPercentValueWithNoControlPollutant() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmissionHasFuelConversion();
        testData.setOverallControlPercent(new BigDecimal(50));
        testData.getEmissionsCalcMethodCode().setEpaEmissionFactor(true);
        testData.setFormulaIndicator(false);
        testData.setTotalEmissions(new BigDecimal(0.000476));

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.NOT_REPORTING.value()) && errorMap.get(ValidationField.NOT_REPORTING.value()).size() == 1);
        }
    }

    /**
     * Emissions must have associated controls for the pollutant associated to the process when control percent value is present
     */
    @Test
    public void overallControlPercentValueWithNoControlPollutant_PassTest() {

        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmissionHasFuelConversion();
        testData.setOverallControlPercent(new BigDecimal(50));
        testData.getEmissionsCalcMethodCode().setEpaEmissionFactor(true);
        testData.setFormulaIndicator(false);
        testData.setTotalEmissions(new BigDecimal(0.000476));

        addReleasePointAppts(testData);

        if(cefContext.result.getErrors() != null) {
            assertFalse(this.validator.validate(cefContext, testData));
            assertFalse(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
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

    private Emission createBaseFormulaEmission() {
        Emission result = createBaseEmission(false);

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);

        EmissionFormulaVariableCode variableCode = new EmissionFormulaVariableCode();
        variableCode.setCode("CS");

        EmissionFormulaVariable variable = new EmissionFormulaVariable();
        variable.setVariableCode(variableCode);
        variable.setValue(BigDecimal.TEN);

        CalculationMethodCode epaCalcMethod = new CalculationMethodCode();
        epaCalcMethod.setCode("8");
        epaCalcMethod.setEpaEmissionFactor(true);
        epaCalcMethod.setTotalDirectEntry(false);
        epaCalcMethod.setControlIndicator(false);
        epaCalcMethod.setDescription("USEPA Emission Factor (no Control Efficiency used)");
        result.setEmissionsCalcMethodCode(epaCalcMethod);

        result.setEmissionsFactorFormula("5.9*CS");
        result.setFormulaIndicator(true);
        result.getVariables().add(variable);
        result.setEmissionsDenominatorUom(btuUom);
        result.setEmissionsFactorText("test description");
        result.setTotalEmissions(BigDecimal.valueOf(104));

        WebfireEmissionFactor ef = new WebfireEmissionFactor();
        ef.setEmissionFactorFormula("5.9*CS");
        ef.setEmissionsDenominatorUom(btuUom);
        ef.setFormulaIndicator(true);
        ef.setCondition("CS>0.4");
        ef.setQuality("A");
        ef.setRevoked(false);
        result.setWebfireEf(ef);

        ReportingPeriod period = new ReportingPeriod();
        period.setCalculationMaterialCode(natGasCmc);
        period.setCalculationParameterValue(new BigDecimal("104"));
        period.setCalculationParameterUom(btuUom);
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

        result.setReportingPeriod(period);

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

    private Emission addReleasePointAppts(Emission emission) {

        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("OP");

        Control control = new Control();
        control.setOperatingStatusCode(opStatCode);
        control.setId(1L);
        control.setIdentifier("test");
        control.setStatusYear((short) 2020);
        control.setPercentControl(BigDecimal.valueOf(50.0));
        emission.getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getFacilitySite().getControls().add(control);
        control.setFacilitySite(emission.getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getFacilitySite());

        ControlAssignment ca = new ControlAssignment();
        ca.setControl(control);
        control.getAssignments().add(ca);

        ControlPollutant cp = new ControlPollutant();
        cp.setPollutant(emission.getPollutant());

        control.getPollutants().add(cp);

        ControlPath controlPath = new ControlPath();
        controlPath.setId(1L);
        controlPath.setPercentControl(BigDecimal.valueOf(50.0));
        controlPath.getAssignments().add(ca);
        ControlPathPollutant controlPathPollutant = new ControlPathPollutant();
        controlPathPollutant.setPollutant(emission.getPollutant());
        controlPathPollutant.setControlPath(controlPath);
        List<ControlPathPollutant> cpaList = new ArrayList<>();
        cpaList.add(controlPathPollutant);
        controlPath.setPollutants(cpaList);

        ControlMeasureCode cmc = new ControlMeasureCode();
        OperatingStatusCode opStatusCode = new OperatingStatusCode();
        opStatusCode.setCode("OP");
        cmc.setCode("test code");
        cmc.setDescription("test code description");

        ReleasePointAppt rpa = new ReleasePointAppt();
        rpa.setEmissionsProcess(emission.getReportingPeriod().getEmissionsProcess());
        rpa.setControlPath(controlPath);

        emission.getReportingPeriod().getEmissionsProcess().getReleasePointAppts().add(rpa);

        return emission;
    }
}
