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
import gov.epa.cef.web.service.validation.validator.state.MEDEPValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MEDEPValidatorTest extends BaseValidatorTest {

    @InjectMocks
    private MEDEPValidator validator;

    @Mock
    private EmissionsReportRepository reportRepo;

    @Mock
    private EmissionsUnitRepository unitRepo;

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

    	List<EmissionsReport> erList = new ArrayList<>();
        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);
        EmissionsReport er1 = new EmissionsReport();
        er1.setId(1L);
        er1.setYear((short) 2018);
        er1.setEisProgramId("1");
        er1.setMasterFacilityRecord(mfr);

        FacilitySite f1 = new FacilitySite();
        f1.setId(1L);
        f1.setOperatingStatusCode(opStatCode);
        f1.setEmissionsReport(er1);

        EmissionsUnit eu = new EmissionsUnit();
        eu.setId(1L);
        eu.setOperatingStatusCode(opStatCode);
        eu.setStatusYear((short)2018);
        eu.setUnitIdentifier("Boiler 001");

        UnitTypeCode utc = new UnitTypeCode();
        utc.setCode("100");
        eu.setUnitTypeCode(utc);

        ArrayList<EmissionsUnit> euList = new ArrayList<>();
        euList.add(eu);
        f1.setEmissionsUnits(euList);

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
    public void simpleValidateUnitChangedOrAddedTest() {
    	// fails when unit is added or edited compared to previous report
        CefValidatorContext cefContext = createContext();
        EmissionsReport testData = createBaseReport();

        testData.getFacilitySites().get(0).getEmissionsUnits().get(0).setDescription("changed for test");

        assertTrue(this.validator.validate(cefContext, testData));
        assertFalse(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        if(cefContext.result.getErrors() != null) {
            Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
            assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_STATUS_CODE.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_STATUS_CODE.value()).size() == 1);
        }
    }

    private EmissionsReport createBaseReport() {

        ProgramSystemCode psc = new ProgramSystemCode();
        psc.setCode("MEDEP");

        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("OP");

        FacilitySourceTypeCode stc = new FacilitySourceTypeCode();
        stc.setCode("137");

        UnitTypeCode utc = new UnitTypeCode();
        utc.setCode("100");

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);
        mfr.setFacilitySourceTypeCode(stc);

        FacilitySite fs = new FacilitySite();
        fs.setOperatingStatusCode(opStatCode);

        EmissionsReport result = new EmissionsReport();
        result.setYear(Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR) - 1).shortValue());
        result.setProgramSystemCode(psc);
        result.setEisProgramId("1");
        result.setId(1L);
        result.setMasterFacilityRecord(mfr);

        Pollutant p = new Pollutant();
        p.setPollutantCode("359353");
        p.setPollutantName("1,1,1,2-Tetrafluoroethane (structure: CHF2CHF2) (HFC-134)");
        p.setPollutantCasId("359-35-3");

        ControlPollutant controlPollutant = new ControlPollutant();
        controlPollutant.setPollutant(p);
        ControlPathPollutant cpp = new ControlPathPollutant();
        cpp.setPollutant(p);
        List<ControlPathPollutant> cppList = new ArrayList<>();
        cppList.add(cpp);
        ControlPath cp = new ControlPath();
        cp.setPollutants(cppList);
        List<ControlPollutant> cpList = new ArrayList<>();
        cpList.add(controlPollutant);
        Control control = new Control();
        control.setPollutants(cpList);
        List<Control> controlList = new ArrayList<>();
        controlList.add(control);
        List<ControlPath> controlPathList = new ArrayList<>();
        controlPathList.add(cp);
        fs.setControls(controlList);
        fs.setControls(controlList);

        EmissionsUnit eu = new EmissionsUnit();

        eu.setStatusYear((short) 2018);
        eu.setOperatingStatusCode(opStatCode);
        eu.setUnitIdentifier("Boiler 001");
        eu.setId(1L);
        eu.setUnitTypeCode(utc);
        List<Emission> eList = new ArrayList<>();
        List<ReportingPeriod> rpList = new ArrayList<>();
        List<EmissionsProcess> epList = new ArrayList<>();
        ReportingPeriod rp = new ReportingPeriod();

        Emission e = new Emission();
        e.setPollutant(p);
        eList.add(e);
        rp.setEmissions(eList);
        rpList.add(rp);
        EmissionsProcess ep = new EmissionsProcess();
        ep.setReportingPeriods(rpList);
        epList.add(ep);
        eu.setEmissionsProcesses(epList);
        ArrayList<EmissionsUnit> euList = new ArrayList<>();
        euList.add(eu);

        fs.setEmissionsUnits(euList);

        result.getFacilitySites().add(fs);

        return result;
    }

}
