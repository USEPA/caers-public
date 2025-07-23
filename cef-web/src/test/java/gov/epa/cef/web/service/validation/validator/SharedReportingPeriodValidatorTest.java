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
import gov.epa.cef.web.config.mock.MockSLTConfig;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.repository.ReportingPeriodRepository;
import gov.epa.cef.web.service.impl.EmissionServiceImpl;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.federal.SharedReportingPeriodValidator;
import gov.epa.cef.web.util.SLTConfigHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SharedReportingPeriodValidatorTest extends BaseValidatorTest {

    private static final String DEFAULT_SLT = "GADNR";
    private static final String PM_VALIDATION_SLT = "IDDEQ";
    private static final String CLEAN_SLT = "TEST";

    @InjectMocks
    private SharedReportingPeriodValidator validator;

    @Mock
    private PointSourceSccCodeRepository sccRepo;

    @Mock
    private EmissionsReportRepository reportRepo;

    @Mock
    private ReportingPeriodRepository reportingPeriodRepo;

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
        scc1.setCode("10200302");
        scc1.setFuelUseRequired(true);
        scc1.setMonthlyReporting(true);

        PointSourceSccCode scc2 = new PointSourceSccCode();
        scc2.setCode("10200303");
        scc2.setFuelUseRequired(false);
        scc2.setMonthlyReporting(false);

        PointSourceSccCode scc3 = new PointSourceSccCode();
        scc3.setCode("10300103");
        scc3.setFuelUseRequired(true);
        scc3.setMonthlyReporting(true);

        PointSourceSccCode scc4 = new PointSourceSccCode();
        scc4.setCode("30500246");
        scc4.setFuelUseRequired(false);
        scc4.setMonthlyReporting(true);

        cmcFuel = new CalculationMaterialCode();
        cmcFuel.setCode("124");
        cmcFuel.setFuelUseMaterial(true);
        cmcFuel.setDescription("Fuel");

        scc2.setCalculationMaterialCode(cmcFuel);
        scc2.setFuelUseTypes("energy,liquid");
        scc3.setCalculationMaterialCode(cmcFuel);
        scc3.setFuelUseTypes("energy,liquid");
        scc4.setCalculationMaterialCode(cmcFuel);
        scc4.setFuelUseTypes(null);

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

        when(sccRepo.findById("10200302")).thenReturn(Optional.of(scc1));
        when(sccRepo.findById("10200303")).thenReturn(Optional.of(scc2));
        when(sccRepo.findById("10300103")).thenReturn(Optional.of(scc3));
        when(sccRepo.findById("30500246")).thenReturn(Optional.of(scc4));

        when(emissionService.checkIfCanConvertUnits(uomE3Ton, uomTon)).thenReturn(true);

        MockSLTConfig gaConfig = new MockSLTConfig();
        gaConfig.setSltMonthlyFuelReportingEnabled(true);
        when(sltConfigHelper.getCurrentSLTConfig(DEFAULT_SLT)).thenReturn(gaConfig);
        when(sltConfigHelper.getCurrentSLTConfig(PM_VALIDATION_SLT)).thenReturn(gaConfig);
        when(sltConfigHelper.getCurrentSLTConfig(CLEAN_SLT)).thenReturn(gaConfig);

    }

    @Test
    public void simpleValidatePassTest() {

        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void operatingTypeCodeFailTest() {

        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();
        testData.setEmissionsOperatingTypeCode(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_OPERATING_TYPE_CODE.value()) && errorMap.get(ValidationField.PERIOD_OPERATING_TYPE_CODE.value()).size() == 1);
    }

    @Test
    public void calculationMaterialCodeFailTest() {

        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();
        testData.setCalculationMaterialCode(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_CALC_MAT_CODE.value()) && errorMap.get(ValidationField.PERIOD_CALC_MAT_CODE.value()).size() == 1);
    }

    @Test
    public void calculationParameterTypeCodeFailTest() {

        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();
        testData.setCalculationParameterTypeCode(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_CALC_TYPE_CODE.value()) && errorMap.get(ValidationField.PERIOD_CALC_TYPE_CODE.value()).size() == 1);
    }

    @Test
    public void calculationParameterUomCodeFailTest() {

        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();
        testData.setCalculationParameterUom(uomBtuHr);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_CALC_UOM.value()) && errorMap.get(ValidationField.PERIOD_CALC_UOM.value()).size() == 1);

        cefContext = createContext();
        testData.setCalculationParameterUom(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_CALC_UOM.value()) && errorMap.get(ValidationField.PERIOD_CALC_UOM.value()).size() == 1);
    }

    // There should be one error when the Fuel Use UoM is legacy
    @Test
    public void legacyFuelUom_FailTest() {
        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();

        testData.getEmissionsProcess().setSccCode("10200303");
        testData.setFuelUseUom(uomBtuHr);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_FUEL_UOM.value()) && errorMap.get(ValidationField.PERIOD_FUEL_UOM.value()).size() == 1);
    }

    // There should be no errors when fuel use is NOT required for selected SCC.
    // There should be no errors when fuel use is required for SCC and fuel material selected matches SCC fuel material
    // There should be no errors when is monthly reporting SCC and fuel material selected matches SCC fuel material
    @Test
    public void sccFuelMaterial_PassTest() {
        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();

        testData.getEmissionsProcess().setSccCode("10200303");

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        testData = createBaseReportingPeriod();
        testData.setFuelUseMaterialCode(cmcFuel);
        testData.getEmissionsProcess().setSccCode("10300103");
        testData.setFuelUseMaterialCode(null);
        testData.setFuelUseUom(null);
        testData.setFuelUseValue(null);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        testData = createBaseReportingPeriod();
        testData.getEmissionsProcess().setSccCode("30500246");

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

    }

    // There should be one error when the selected fuel material does not match the fuel material for the selected SCC that requires fuel data
    // There should be one error when the selected fuel material does not match the fuel material for the selected SCC that is for monthly reporting
    @Test
    public void sccFuelMaterial_FailTest() {
        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();

        testData.getEmissionsProcess().setSccCode("10300103");
        UnitMeasureCode uom = new UnitMeasureCode();
        uom.setCode("E3FT3SD");
        uom.setLegacy(false);
        uom.setFuelUseType("gas");
        uom.setUnitType("DSCF");
        uom.setCalculationVariable("[k] * 1");
        testData.setFuelUseUom(uom);
        testData.setFuelUseMaterialCode(cmcFuel);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_SCC_FUEL_MATERIAL.value()) && errorMap.get(ValidationField.PERIOD_SCC_FUEL_MATERIAL.value()).size() == 1);

        cefContext = createContext();
        testData = createBaseReportingPeriod();
        testData.getEmissionsProcess().setSccCode("30500246");
        uom.setFuelUseType(null);
        testData.setFuelUseUom(uom);
        testData.setFuelUseMaterialCode(cmcFuel);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
    }

    // There should be one error when the selected fuel uom is not one of the uom for the selected SCC that requires fuel data
    // There should be one error when the selected fuel uom is not a fuel use uom for the selected asphalt SCC for monthly reporting
    @Test
    public void sccFuelUom_FailTest() {
        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();

        testData.getEmissionsProcess().setSccCode("10300103");
        testData.setFuelUseUom(uomTon);
        testData.setFuelUseMaterialCode(cmcFuel);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_SCC_FUEL_MATERIAL.value()) && errorMap.get(ValidationField.PERIOD_SCC_FUEL_MATERIAL.value()).size() == 1);

        cefContext = createContext();
        testData = createBaseReportingPeriod();
        testData.getEmissionsProcess().setSccCode("30500246");
        uomTon.setFuelUseType(null);
        testData.setFuelUseUom(uomTon);
        testData.setFuelUseMaterialCode(cmcFuel);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
    }

    @Test
    public void duplicatePollutantFailTest() {

        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();
        Pollutant p = new Pollutant();
        p.setPollutantCode("PM10-FIL");
        Emission e1 = new Emission();
        e1.setPollutant(p);
        testData.getEmissions().add(e1);
        Emission e2 = new Emission();
        e2.setPollutant(p);
        testData.getEmissions().add(e2);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_EMISSION.value()) && errorMap.get(ValidationField.PERIOD_EMISSION.value()).size() == 1);
    }

    @Test
    public void pm_FilExceedPM_PriFailTest() {
        // Contradicts reportingPeriod.emission.pm10.pri.and.pm25.fil and reportingPeriod.emission.pm10.fil.and.pm25.pri
        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();
        Pollutant p1 = new Pollutant();
        p1.setPollutantCode("PM10-FIL");
        Pollutant p2 = new Pollutant();
        p2.setPollutantCode("PM10-PRI");
        Pollutant p3 = new Pollutant();
        p3.setPollutantCode("PM25-FIL");
        Pollutant p4 = new Pollutant();
        p4.setPollutantCode("PM25-PRI");
        Emission e1 = new Emission();
        e1.setPollutant(p1);
        e1.setCalculatedEmissionsTons(BigDecimal.valueOf(100));
        testData.getEmissions().add(e1);
        Emission e2 = new Emission();
        e2.setPollutant(p2);
        e2.setCalculatedEmissionsTons(BigDecimal.valueOf(10));
        testData.getEmissions().add(e2);
        Emission e3 = new Emission();
        e3.setPollutant(p3);
        e3.setCalculatedEmissionsTons(BigDecimal.valueOf(5));
        testData.getEmissions().add(e3);
        Emission e4 = new Emission();
        e4.setPollutant(p4);
        e4.setCalculatedEmissionsTons(BigDecimal.valueOf(5));
        testData.getEmissions().add(e4);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_EMISSION.value()) && errorMap.get(ValidationField.PERIOD_EMISSION.value()).size() == 1);

        cefContext = createContext();
        p1.setPollutantCode("PM25-FIL");
        p2.setPollutantCode("PM25-PRI");
        testData.getEmissions().remove(e3);
        testData.getEmissions().remove(e4);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_EMISSION.value()) && errorMap.get(ValidationField.PERIOD_EMISSION.value()).size() == 1);
    }


    @Test
    public void pmConExceedPM_PriFailTest() {

        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();
        Pollutant p1 = new Pollutant();
        p1.setPollutantCode("PM-CON");
        Pollutant p2 = new Pollutant();
        p2.setPollutantCode("PM25-PRI");
        Emission e1 = new Emission();
        e1.setPollutant(p1);
        e1.setCalculatedEmissionsTons(BigDecimal.valueOf(100));
        testData.getEmissions().add(e1);
        Emission e2 = new Emission();
        e2.setPollutant(p2);
        e2.setCalculatedEmissionsTons(BigDecimal.valueOf(10));
        testData.getEmissions().add(e2);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_EMISSION.value()) && errorMap.get(ValidationField.PERIOD_EMISSION.value()).size() == 1);

        cefContext = createContext();
        p1.setPollutantCode("PM-CON");
        p2.setPollutantCode("PM10-PRI");
        Pollutant p3 = new Pollutant();
        p3.setPollutantCode("PM25-PRI");
        Emission e3 = new Emission();
        e3.setPollutant(p3);
        e3.setCalculatedEmissionsTons(BigDecimal.valueOf(9));
        testData.getEmissions().add(e3);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);
        // PM2.5 needs to be reported and cannot exceed PM10 if PM10 exists
        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_EMISSION.value()) && errorMap.get(ValidationField.PERIOD_EMISSION.value()).size() == 2);
    }

    @Test
    public void pmConAndPM_FilEqualPM_PriFailTest() {
        // Contradicts reportingPeriod.emission.pm10.pri.and.pm25.fil and reportingPeriod.emission.pm10.fil.and.pm25.pri
        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();
        Pollutant p1 = new Pollutant();
        p1.setPollutantCode("PM-CON");
        Pollutant p2 = new Pollutant();
        p2.setPollutantCode("PM10-FIL");
        Pollutant p3 = new Pollutant();
        p3.setPollutantCode("PM10-PRI");
        Pollutant p4 = new Pollutant();
        p4.setPollutantCode("PM25-FIL");
        Pollutant p5 = new Pollutant();
        p5.setPollutantCode("PM25-PRI");
        Emission e1 = new Emission();
        e1.setPollutant(p1);
        e1.setCalculatedEmissionsTons(BigDecimal.valueOf(10));
        testData.getEmissions().add(e1);
        Emission e2 = new Emission();
        e2.setPollutant(p2);
        e2.setCalculatedEmissionsTons(BigDecimal.valueOf(20));
        testData.getEmissions().add(e2);
        Emission e3 = new Emission();
        e3.setPollutant(p3);
        e3.setCalculatedEmissionsTons(BigDecimal.valueOf(20));
        testData.getEmissions().add(e3);
        Emission e4 = new Emission();
        e4.setPollutant(p4);
        e4.setCalculatedEmissionsTons(BigDecimal.valueOf(5));
        testData.getEmissions().add(e4);
        Emission e5 = new Emission();
        e5.setPollutant(p5);
        e5.setCalculatedEmissionsTons(BigDecimal.valueOf(15));
        testData.getEmissions().add(e5);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_EMISSION.value()) && errorMap.get(ValidationField.PERIOD_EMISSION.value()).size() == 1);

        cefContext = createContext();
        p2.setPollutantCode("PM25-FIL");
        p3.setPollutantCode("PM25-PRI");
        testData.getEmissions().remove(e4);
        testData.getEmissions().remove(e5);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_EMISSION.value()) && errorMap.get(ValidationField.PERIOD_EMISSION.value()).size() == 1);
    }

    @Test
    public void pm25PriExceedPM10PriFailTest() {

        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod(PM_VALIDATION_SLT);
        Pollutant p1 = new Pollutant();
        p1.setPollutantCode("PM10-PRI");
        Pollutant p2 = new Pollutant();
        p2.setPollutantCode("PM25-PRI");
        Emission e1 = new Emission();
        e1.setPollutant(p1);
        e1.setCalculatedEmissionsTons(BigDecimal.valueOf(10));
        testData.getEmissions().add(e1);
        Emission e2 = new Emission();
        e2.setPollutant(p2);
        e2.setCalculatedEmissionsTons(BigDecimal.valueOf(100));
        testData.getEmissions().add(e2);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_EMISSION.value()) && errorMap.get(ValidationField.PERIOD_EMISSION.value()).size() == 1);
    }

    @Test
    public void pm25FilExceedPM10FilFailTest() {

        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod(PM_VALIDATION_SLT);
        Pollutant p1 = new Pollutant();
        p1.setPollutantCode("PM10-FIL");
        Pollutant p2 = new Pollutant();
        p2.setPollutantCode("PM25-FIL");
        Emission e1 = new Emission();
        e1.setPollutant(p1);
        e1.setCalculatedEmissionsTons(BigDecimal.valueOf(10));
        testData.getEmissions().add(e1);
        Emission e2 = new Emission();
        e2.setPollutant(p2);
        e2.setCalculatedEmissionsTons(BigDecimal.valueOf(100));
        testData.getEmissions().add(e2);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_EMISSION.value()) && errorMap.get(ValidationField.PERIOD_EMISSION.value()).size() == 1);
    }

    @Test
    public void pm10FilandNotPM25Fil_FailTest() {

        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod(PM_VALIDATION_SLT);
        Pollutant p1 = new Pollutant();
        p1.setPollutantCode("PM10-FIL");
        Emission e1 = new Emission();
        e1.setPollutant(p1);
        e1.setCalculatedEmissionsTons(BigDecimal.valueOf(10));
        testData.getEmissions().add(e1);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_EMISSION.value()) && errorMap.get(ValidationField.PERIOD_EMISSION.value()).size() == 1);
    }

    @Test
    public void pm10PriAndNotPM25Pri_FailTest() {

        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod(PM_VALIDATION_SLT);
        Pollutant p1 = new Pollutant();
        p1.setPollutantCode("PM10-PRI");
        Emission e1 = new Emission();
        e1.setPollutant(p1);
        e1.setCalculatedEmissionsTons(BigDecimal.valueOf(10));
        testData.getEmissions().add(e1);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_EMISSION.value()) && errorMap.get(ValidationField.PERIOD_EMISSION.value()).size() == 1);
    }

    @Test
    public void pm10PriAndPM25Fil_FailTest() {

        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod(PM_VALIDATION_SLT);
        Pollutant p1 = new Pollutant();
        p1.setPollutantCode("PM10-PRI");
        Pollutant p2 = new Pollutant();
        p2.setPollutantCode("PM25-FIL");
        Pollutant p3 = new Pollutant();
        p3.setPollutantCode("PM25-PRI");
        Emission e1 = new Emission();
        e1.setPollutant(p1);
        e1.setCalculatedEmissionsTons(BigDecimal.valueOf(10));
        testData.getEmissions().add(e1);
        Emission e2 = new Emission();
        e2.setPollutant(p2);
        e2.setCalculatedEmissionsTons(BigDecimal.valueOf(1));
        testData.getEmissions().add(e2);
        Emission e3 = new Emission();
        e3.setPollutant(p3);
        e3.setCalculatedEmissionsTons(BigDecimal.valueOf(1));
        testData.getEmissions().add(e3);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_EMISSION.value()) && errorMap.get(ValidationField.PERIOD_EMISSION.value()).size() == 1);
    }

    @Test
    public void pm10FilAndPM25Fri_FailTest() {

        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod(PM_VALIDATION_SLT);
        Pollutant p1 = new Pollutant();
        p1.setPollutantCode("PM10-FIL");
        Pollutant p2 = new Pollutant();
        p2.setPollutantCode("PM25-PRI");
        Pollutant p3 = new Pollutant();
        p3.setPollutantCode("PM25-FIL");
        Emission e1 = new Emission();
        e1.setPollutant(p1);
        e1.setCalculatedEmissionsTons(BigDecimal.valueOf(10));
        testData.getEmissions().add(e1);
        Emission e2 = new Emission();
        e2.setPollutant(p2);
        e2.setCalculatedEmissionsTons(BigDecimal.valueOf(1));
        testData.getEmissions().add(e2);
        Emission e3 = new Emission();
        e3.setPollutant(p3);
        e3.setCalculatedEmissionsTons(BigDecimal.valueOf(1));
        testData.getEmissions().add(e3);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_EMISSION.value()) && errorMap.get(ValidationField.PERIOD_EMISSION.value()).size() == 1);
    }

    @Test
    public void fluoridesFailTest() {

        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();
        Pollutant p1 = new Pollutant();
        p1.setPollutantCode("16984488");
        Pollutant p2 = new Pollutant();
        p2.setPollutantCode("7664393");
        Emission e1 = new Emission();
        e1.setPollutant(p1);
        e1.setCalculatedEmissionsTons(BigDecimal.valueOf(10));
        testData.getEmissions().add(e1);
        Emission e2 = new Emission();
        e2.setPollutant(p2);
        e2.setCalculatedEmissionsTons(BigDecimal.valueOf(100));
        testData.getEmissions().add(e2);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_EMISSION.value()) && errorMap.get(ValidationField.PERIOD_EMISSION.value()).size() == 1);
    }

    // There should be one error when the Heat Content UoM is legacy
    @Test
    public void legacyHeatContentUom_FailTest() {
        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();

        testData.getEmissionsProcess().setSccCode("10200303");
        testData.setHeatContentUom(uomBtuHr);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_HEAT_CONTENT_UOM.value()) && errorMap.get(ValidationField.PERIOD_HEAT_CONTENT_UOM.value()).size() == 1);
    }

    // There should be one error when the selected Heat Content uom is not one of the uom for the selected SCC that requires fuel data
    @Test
    public void sccFuel_HeatContentUom_FailTest() {
        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();
        UnitMeasureCode uomGal = new UnitMeasureCode();
        uomGal.setCode("E3GAL");
        uomGal.setLegacy(false);
        uomGal.setFuelUseType("liquid");
        uomGal.setUnitType("VOLUME");
        uomGal.setCalculationVariable("[k] * [gall]");

        testData.getEmissionsProcess().setSccCode("10300103");
        testData.setHeatContentUom(uomTon);
        testData.setFuelUseMaterialCode(cmcFuel);
        testData.setFuelUseUom(uomGal);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_HEAT_CONTENT_UOM.value()) && errorMap.get(ValidationField.PERIOD_HEAT_CONTENT_UOM.value()).size() == 1);
    }

    // There should be one error when the selected Heat Content uom has the same unit type/fuel type as the selected fuel uom
    @Test
    public void fuelUom_HeatContentUom_FailTest() {
        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();
        UnitMeasureCode uomLb = new UnitMeasureCode();
        uomLb.setCode("E3LB");
        uomLb.setLegacy(false);
        uomLb.setFuelUseType("solid");
        uomLb.setUnitType("MASS");
        testData.setHeatContentUom(uomLb);
        testData.setFuelUseUom(uomTon);
        testData.setFuelUseMaterialCode(cmcFuel);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_HEAT_CONTENT_UOM.value()) && errorMap.get(ValidationField.PERIOD_HEAT_CONTENT_UOM.value()).size() == 1);
    }

    @Test
    public void throughputAndFuelValuesNotEqual_PassTest() {
        // throughput value exists, but fuel value null
        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();

        testData.setFuelUseMaterialCode(testData.getCalculationMaterialCode());
        testData.setFuelUseUom(testData.getCalculationParameterUom());
        testData.setFuelUseValue(null);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void throughputAndFuelMaterialsNotEqual_PassTest() {
        // throughput material exists, but fuel material null
        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();

        testData.setFuelUseMaterialCode(null);
        testData.setFuelUseUom(testData.getCalculationParameterUom());
        testData.setFuelUseValue(testData.getCalculationParameterValue());

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void throughputAndFuelUoMsNotEqual_PassTest() {
        // throughput UoM exists, but fuel UoM null
        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();

        testData.setFuelUseMaterialCode(testData.getCalculationMaterialCode());
        testData.setFuelUseUom(null);
        testData.setFuelUseValue(testData.getCalculationParameterValue());

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void throughputAndFuelValuesNotEqual_FailTest() {
        // Materials and UoMs should be equal
        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();

        testData.setCalculationMaterialCode(cmcFuel);
        testData.setCalculationParameterUom(uomBBL);
        testData.getEmissionsProcess().setSccCode("10300103");
        testData.setFuelUseMaterialCode(testData.getCalculationMaterialCode());
        testData.setFuelUseUom(testData.getCalculationParameterUom());
        testData.setFuelUseValue(BigDecimal.valueOf(1));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_CALC_VALUE.value()) && errorMap.get(ValidationField.PERIOD_CALC_VALUE.value()).size() == 1);
    }

    @Test
    public void throughputAndFuelMaterialsNotEqual_FailTest() {
        // Values and UoMs should be equal
        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();

        testData.getCalculationMaterialCode().setCode("640");
        testData.getCalculationMaterialCode().setFuelUseMaterial(true);
        testData.setCalculationParameterUom(uomBBL);
        testData.getEmissionsProcess().setSccCode("10300103");
        testData.setFuelUseMaterialCode(cmcFuel);
        testData.setFuelUseUom(testData.getCalculationParameterUom());
        testData.setFuelUseValue(testData.getCalculationParameterValue());

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_CALC_MAT_CODE.value()) && errorMap.get(ValidationField.PERIOD_CALC_MAT_CODE.value()).size() == 1);
    }

    @Test
    public void throughputAndFuelUoMsNotEqual_FailTest() {
        // Values and Materials should be equal
        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();

        UnitMeasureCode uomE3BBL = new UnitMeasureCode();
        uomE3BBL.setCode("E3BBL");
        uomE3BBL.setLegacy(false);
        uomE3BBL.setFuelUseType("liquid");
        uomE3BBL.setUnitType("BBL");
        uomE3BBL.setCalculationVariable("1000 * bbl");

        testData.setCalculationMaterialCode(cmcFuel);
        testData.setCalculationParameterUom(uomBBL);
        testData.getEmissionsProcess().setSccCode("10300103");
        testData.setFuelUseMaterialCode(testData.getCalculationMaterialCode());
        testData.setFuelUseUom(uomE3BBL);
        testData.setFuelUseValue(testData.getCalculationParameterValue());

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_CALC_UOM.value()) && errorMap.get(ValidationField.PERIOD_CALC_UOM.value()).size() == 1);
    }

    @Test
    public void throughputUoMChangedFromPreviousSameMaterial_FailTest() {
        CefValidatorContext cefContext = createContext();
        createPreviousReportingPeriods();
        ReportingPeriod testData = createBaseReportingPeriod();

        testData.setCalculationParameterUom(uomBBL);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_CALC_UOM.value()) && errorMap.get(ValidationField.PERIOD_CALC_UOM.value()).size() == 1);
    }

    @Test
    public void fuelUoMChangedFromPreviousSameMaterial_FailTest() {
        CefValidatorContext cefContext = createContext();
        createPreviousReportingPeriods();
        ReportingPeriod testData = createBaseReportingPeriod();

        testData.setFuelUseUom(uomBBL);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_FUEL_UOM.value()) && errorMap.get(ValidationField.PERIOD_FUEL_UOM.value()).size() == 1);
    }

    private ReportingPeriod createBaseReportingPeriod() {

        return createBaseReportingPeriod(DEFAULT_SLT);

    }

    private ReportingPeriod createBaseReportingPeriod(String slt) {

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);

        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("OP");
        ProgramSystemCode psc = new ProgramSystemCode();
        psc.setCode(slt);

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
        psc.setCode(DEFAULT_SLT);
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
