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
import gov.epa.cef.web.repository.EmissionRepository;
import gov.epa.cef.web.repository.ReportHistoryRepository;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.federal.SemiAnnualReportValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SemiAnnualReportValidatorTest extends BaseValidatorTest {

    @InjectMocks
    private SemiAnnualReportValidator validator;

    @Mock
    private EmissionRepository emissionRepo;

    @Mock
	private ReportHistoryRepository historyRepo;

    @Before
    public void init(){

    	List<Emission> eList = new ArrayList<Emission>();
    	Emission e = new Emission();
        e.setId(1L);
        e.setTotalManualEntry(true);
        CalculationMethodCode cmc = new CalculationMethodCode();
        cmc.setTotalDirectEntry(false);
        e.setEmissionsCalcMethodCode(cmc);
        eList.add(e);

        List<ReportHistory> raList = new ArrayList<ReportHistory>();
        List<ReportHistory> raList2 = new ArrayList<ReportHistory>();
        ReportHistory ra = new ReportHistory();
        ra.setId(1L);
        ra.setUserRole("Preparer");
        ra.setReportAttachmentId(1L);
        ra.setFileDeleted(false);
        raList.add(ra);

    	when(emissionRepo.findAllByReportId(1L)).thenReturn(eList);
    	when(historyRepo.findByEmissionsReportIdOrderByActionDate(1L)).thenReturn(raList);

    	when(emissionRepo.findAllByReportId(2L)).thenReturn(eList);
    	when(historyRepo.findByEmissionsReportIdOrderByActionDate(2L)).thenReturn(raList2);
    }

    @Test
    public void simpleValidatePassTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsReport testData = createBaseReport();

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void nullYearTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsReport testData = createBaseReport();
        testData.setYear(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.REPORT_YEAR.value()) && errorMap.get(ValidationField.REPORT_YEAR.value()).size() == 1);
    }

    @Test
    public void oldYearTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsReport testData = createBaseReport();
        testData.setYear(Integer.valueOf(testData.getYear() - 1).shortValue());

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.REPORT_YEAR.value()) && errorMap.get(ValidationField.REPORT_YEAR.value()).size() == 1);
    }

    @Test
    public void nullProgramSystemCodeTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsReport testData = createBaseReport();
        testData.setProgramSystemCode(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.REPORT_PROGRAM_SYSTEM_CODE.value()) && errorMap.get(ValidationField.REPORT_PROGRAM_SYSTEM_CODE.value()).size() == 1);
    }

    private EmissionsReport createBaseReport() {

        ProgramSystemCode psc = new ProgramSystemCode();
        psc.setCode("GADNR");

        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("OP");

        FacilitySite fs = new FacilitySite();
        fs.setOperatingStatusCode(opStatCode);

        EmissionsReport result = new EmissionsReport();
        result.setYear(Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR) - 1).shortValue());
        result.setProgramSystemCode(psc);
        result.setEisProgramId("1");
        result.setId(1L);
        result.getFacilitySites().add(fs);

        return result;
    }
}
