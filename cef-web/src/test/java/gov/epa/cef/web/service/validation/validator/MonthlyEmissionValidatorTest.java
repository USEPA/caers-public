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
import gov.epa.cef.web.service.validation.validator.federal.MonthlyEmissionValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MonthlyEmissionValidatorTest extends BaseValidatorTest {

    @InjectMocks
    private MonthlyEmissionValidator validator;

    @Mock
    private PointSourceSccCodeRepository sccRepo;

    private UnitMeasureCode tonUom;

    @Before
    public void init() {

        tonUom = new UnitMeasureCode();
        tonUom.setCode("TON");
        tonUom.setDescription("TONS");
        tonUom.setUnitType("MASS");
        tonUom.setCalculationVariable("sTon");
        tonUom.setFuelUseUom(true);
        tonUom.setHeatContentUom(false);

        PointSourceSccCode scc1 = new PointSourceSccCode();
        scc1.setCode("10100101");
        scc1.setFuelUseRequired(true);
        scc1.setMonthlyReporting(true);

        when(sccRepo.findById("10100101")).thenReturn(Optional.of(scc1));
    }

    @Test
    public void totalEmissionsNullFailTest() {
        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission();
        testData.setTotalEmissions(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSION_TOTAL_EMISSIONS.value()) && errorMap.get(ValidationField.EMISSION_TOTAL_EMISSIONS.value()).size() == 1);
    }

    @Test
    public void monthlyRateNullFailTest() {
        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission();

        CalculationMethodCode calcMethod = new CalculationMethodCode();
        calcMethod.setCode("1");

        testData.setEmissionsCalcMethodCode(calcMethod);
        testData.setMonthlyRate(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSION_MONTHLY_RATE.value()) && errorMap.get(ValidationField.EMISSION_MONTHLY_RATE.value()).size() == 1);
    }

    /**
     * Monthly Rate is allowed to be null when calc method is anything other than CEMS (1)
     */
    @Test
    public void monthlyRateNullPassTest() {
        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission();

        CalculationMethodCode calcMethod = new CalculationMethodCode();
        calcMethod.setCode("2");

        testData.setEmissionsCalcMethodCode(calcMethod);
        testData.setMonthlyRate(null);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    /**
     * Total emissions cannot be 0 when the throughput value is greater than 0
     * This applies only to the semiannual reporting period
     */
    @Test
    public void zeroEmissionsNonZeroThroughput() {
        CefValidatorContext cefContext = createContext();
        Emission testData = createBaseEmission();

        testData.setTotalEmissions(BigDecimal.ZERO);
        testData.getReportingPeriod().setCalculationParameterValue(BigDecimal.TEN);

        ReportingPeriodCode rpType = new ReportingPeriodCode();
        rpType.setCode("SA");
        rpType.setShortName("Semiannual");
        testData.getReportingPeriod().setReportingPeriodTypeCode(rpType);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSION_TOTAL_EMISSIONS.value()) && errorMap.get(ValidationField.EMISSION_TOTAL_EMISSIONS.value()).size() == 1);
    }

    private Emission createBaseEmission() {

        Emission result = new Emission();

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);

        // when no ef is used totalDirectEntry is true
        CalculationMethodCode calcMethod = new CalculationMethodCode();
        calcMethod.setCode("2");
        calcMethod.setDescription("Engineering Judgment");
        calcMethod.setControlIndicator(false);
        calcMethod.setEpaEmissionFactor(false);
        calcMethod.setTotalDirectEntry(true);
        result.setEmissionsCalcMethodCode(calcMethod);

        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("OP");

        ReportingPeriodCode rpType = new ReportingPeriodCode();
        rpType.setCode("JAN");
        rpType.setShortName("January");

        ReportingPeriod period = new ReportingPeriod();
        period.setCalculationParameterValue(new BigDecimal("1"));
        period.setEmissionsProcess(new EmissionsProcess());
        period.getEmissionsProcess().setEmissionsProcessIdentifier("Boiler 001");
        period.getEmissionsProcess().setSccCode("10100101");
        period.getEmissionsProcess().setOperatingStatusCode(opStatCode);
        period.getEmissionsProcess().setEmissionsUnit(new EmissionsUnit());
        period.getEmissionsProcess().getEmissionsUnit().setUnitIdentifier("Boiler 001");
        period.getEmissionsProcess().getEmissionsUnit().setFacilitySite(new FacilitySite());
        period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().setStatusYear((short) 2020);
        period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().setOperatingStatusCode(opStatCode);
        period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().setEmissionsReport(new EmissionsReport());
        period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().setYear(new Short("2019"));
        period.setReportingPeriodTypeCode(rpType);
        result.setReportingPeriod(period);

        CalculationMaterialCode cmc = new CalculationMaterialCode();
        cmc.setCode("15");

        result.setEmissionsUomCode(tonUom);

        result.setTotalEmissions(new BigDecimal("10"));
        result.setCalculatedEmissionsTons(new BigDecimal("10"));

        result.setComments("Comment");
        result.setTotalManualEntry(true);

        Pollutant pollutant = new Pollutant();
        pollutant.setPollutantCode("NOX");
        result.setPollutant(pollutant);

        return result;
    }
}
