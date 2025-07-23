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
import gov.epa.cef.web.service.validation.validator.federal.MonthlyReportingPeriodValidator;
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
public class MonthlyReportingPeriodValidatorTest extends BaseValidatorTest {

	@InjectMocks
	private MonthlyReportingPeriodValidator validator;

    @Mock
    private PointSourceSccCodeRepository sccRepo;

	private CalculationMaterialCode cmcFuel;
	private UnitMeasureCode uomTon;

    @Before
    public void init(){

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

        PointSourceSccCode scc1 = new PointSourceSccCode();
        scc1.setCode("10200303");
        scc1.setFuelUseRequired(true);
        scc1.setMonthlyReporting(true);

        when(sccRepo.findById("10200303")).thenReturn(Optional.of(scc1));
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
    public void fuelValueFailTest() {

        CefValidatorContext cefContext = createContext();
        ReportingPeriod testData = createBaseReportingPeriod();
        testData.setFuelUseValue(BigDecimal.valueOf(-1));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PERIOD_FUEL_VALUE.value()) && errorMap.get(ValidationField.PERIOD_FUEL_VALUE.value()).size() == 1);
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
}
