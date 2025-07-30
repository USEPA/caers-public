/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.validation.validator;


import com.baidu.unbiz.fluentvalidator.ValidationError;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.EmissionsUnitRepository;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.state.IDDEQValidator;
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
public class IDDEQValidatorTest extends BaseValidatorTest {

    @InjectMocks
    private IDDEQValidator validator;

    @Mock
    private EmissionsReportRepository reportRepo;

    @Mock
    private EmissionsUnitRepository unitRepo;

	private OperatingStatusCode opStatCode;
	private OperatingStatusCode tsStatCode;
	private OperatingStatusCode psStatCode;
    private UnitMeasureCode tonUom;
    private CalculationMaterialCode natGasCmc;

    @Before
    public void init(){

		opStatCode = new OperatingStatusCode();
        opStatCode.setCode("OP");
        tsStatCode = new OperatingStatusCode();
        tsStatCode.setCode("TS");
        psStatCode = new OperatingStatusCode();
        psStatCode.setCode("PS");

        tonUom = new UnitMeasureCode();
        tonUom.setCode("TON");
        tonUom.setDescription("TONS");
        tonUom.setUnitType("MASS");
        tonUom.setCalculationVariable("sTon");
        tonUom.setFuelUseUom(true);
        tonUom.setHeatContentUom(false);

        natGasCmc = new CalculationMaterialCode();
        natGasCmc.setCode("209");

    	List<EmissionsReport> erList = new ArrayList<>();
        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);
        EmissionsReport er1 = new EmissionsReport();
        er1.setId(1L);
        er1.setYear((short) 2018);
        er1.setEisProgramId("1");
        er1.setMasterFacilityRecord(mfr);

        ReleasePoint rp = new ReleasePoint();
        rp.setId(1L);
        rp.setOperatingStatusCode(opStatCode);
        rp.setStatusYear((short) 2018);
        rp.setReleasePointIdentifier("identifier");

        FacilitySite f1 = new FacilitySite();
        f1.setId(1L);
        f1.setOperatingStatusCode(opStatCode);
        f1.setEmissionsReport(er1);
        f1.getReleasePoints().add(rp);

        EmissionsUnit eu = new EmissionsUnit();
        eu.setId(1L);
        eu.setOperatingStatusCode(opStatCode);
        eu.setStatusYear((short)2010);
        eu.setUnitIdentifier("Boiler 001");

        EmissionsProcess process = new EmissionsProcess();

        process.setStatusYear((short) 2018);
        process.setId(1L);
        process.setEmissionsUnit(eu);
        process.setOperatingStatusCode(opStatCode);
        process.setEmissionsProcessIdentifier("Boiler 001");
        process.setSccCode("30503506");

        UnitTypeCode utc = new UnitTypeCode();
        utc.setCode("100");
        eu.setUnitTypeCode(utc);
        eu.getEmissionsProcesses().add(process);

        ArrayList<EmissionsUnit> euList = new ArrayList<>();
        euList.add(eu);
        f1.setEmissionsUnits(euList);
        er1.getFacilitySites().add(f1);

        erList.add(er1);

    	when(reportRepo.findByMasterFacilityRecordId(1L)).thenReturn(erList);
    	when(reportRepo.findByMasterFacilityRecordId(2L)).thenReturn(Collections.emptyList());
    	when(unitRepo.retrieveByFacilityYear(
          		1L, (short) 2018)).thenReturn(Collections.singletonList(eu));

    }

    @Test
    public void simpleValidatePassTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsReport testData = createBaseReport();

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void missingEmissionFactorTest() {
    	// validation should fail when any emission does not have an emission factor
        CefValidatorContext cefContext = createContext();
        EmissionsReport testData = createBaseReport();

        testData.getFacilitySites().get(0).getEmissionsUnits().get(0).getEmissionsProcesses().get(0).getReportingPeriods().get(0).getEmissions().get(0).setEmissionsFactor(null);
        testData.getFacilitySites().get(0).getEmissionsUnits().get(0).getEmissionsProcesses().get(0).getReportingPeriods().get(0).getEmissions().get(0).setEmissionsNumeratorUom(null);
        testData.getFacilitySites().get(0).getEmissionsUnits().get(0).getEmissionsProcesses().get(0).getReportingPeriods().get(0).getEmissions().get(0).setEmissionsDenominatorUom(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSION_EF.value()) && errorMap.get(ValidationField.EMISSION_EF.value()).size() == 1);
    }

    private EmissionsReport createBaseReport() {

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);
        FacilitySourceTypeCode fstc = new FacilitySourceTypeCode();
        fstc.setCode("137");
        mfr.setFacilitySourceTypeCode(fstc);
        OperatingStatusCode os = new OperatingStatusCode();
        os.setCode("OP");

        ProgramSystemCode psc = new ProgramSystemCode();
        psc.setCode("IDDEQ");

        EmissionsReport report = new EmissionsReport();
        report.setYear(new Short("2020"));
        report.setId(3L);
        report.setEisProgramId("1");
        report.setMasterFacilityRecord(mfr);
        report.setProgramSystemCode(psc);

        ReleasePoint rp = new ReleasePoint();
        rp.setId(1L);
        rp.setOperatingStatusCode(opStatCode);
        rp.setStatusYear((short) 2020);
        rp.setReleasePointIdentifier("identifier");

        FacilitySite facility = new FacilitySite();
        facility.setId(1L);
        facility.setEmissionsReport(report);
        facility.setOperatingStatusCode(os);
        facility.getReleasePoints().add(rp);

        EmissionsUnit unit = new EmissionsUnit();
        unit.setId(1L);
        unit.setFacilitySite(facility);
        unit.setUnitIdentifier("Boiler 001");
        unit.setOperatingStatusCode(os);
        unit.setStatusYear(new Short("2010"));

        EmissionsProcess process = new EmissionsProcess();

        ReportingPeriod rperiod1 = new ReportingPeriod();
        rperiod1.setId(1L);

        Emission e1 = new Emission();
        e1.setId(1L);
        e1.setReportingPeriod(rperiod1);
        e1.setEmissionsUomCode(tonUom);

        e1.setTotalEmissions(new BigDecimal("10"));
        e1.setFormulaIndicator(false);
        e1.setEmissionsDenominatorUom(tonUom);
        e1.setEmissionsNumeratorUom(tonUom);
        e1.setEmissionsFactor(new BigDecimal("1"));
        e1.setTotalManualEntry(false);

        CalculationMethodCode calcMethod = new CalculationMethodCode();
        calcMethod.setCode("2");
        calcMethod.setDescription("Engineering Judgment");
        calcMethod.setControlIndicator(false);
        calcMethod.setEpaEmissionFactor(false);
        calcMethod.setTotalDirectEntry(true);
        e1.setEmissionsCalcMethodCode(calcMethod);

        rperiod1.getEmissions().add(e1);

        process.setStatusYear((short) 2018);
        process.setId(1L);
        process.setEmissionsUnit(unit);
        process.setAircraftEngineTypeCode(null);
        process.setOperatingStatusCode(os);
        process.getReportingPeriods().add(rperiod1);
        process.setEmissionsProcessIdentifier("Boiler 001");
        process.setSccCode("30503506");

        unit.getEmissionsProcesses().add(process);

        facility.getEmissionsUnits().add(unit);

        report.getFacilitySites().clear();
        report.getFacilitySites().add(facility);

        return report;
    }
}
