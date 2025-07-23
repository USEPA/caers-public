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
