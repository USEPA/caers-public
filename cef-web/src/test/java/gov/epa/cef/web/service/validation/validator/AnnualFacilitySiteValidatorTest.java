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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import gov.epa.cef.web.service.LookupService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.baidu.unbiz.fluentvalidator.ValidationError;

import gov.epa.cef.web.domain.ContactTypeCode;
import gov.epa.cef.web.domain.EmissionsUnit;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.FacilityNAICSXref;
import gov.epa.cef.web.domain.FacilitySite;
import gov.epa.cef.web.domain.FacilitySiteContact;
import gov.epa.cef.web.domain.FacilitySourceTypeCode;
import gov.epa.cef.web.domain.FipsCounty;
import gov.epa.cef.web.domain.FipsStateCode;
import gov.epa.cef.web.domain.MasterFacilityRecord;
import gov.epa.cef.web.domain.NaicsCode;
import gov.epa.cef.web.domain.NaicsCodeType;
import gov.epa.cef.web.domain.OperatingStatusCode;
import gov.epa.cef.web.domain.ProgramSystemCode;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.FacilitySiteRepository;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.federal.AnnualFacilitySiteValidator;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AnnualFacilitySiteValidatorTest extends BaseValidatorTest {

    @InjectMocks
    private AnnualFacilitySiteValidator validator;

    @Mock
	private FacilitySiteRepository facilitySiteRepo;

    @Mock
	private EmissionsReportRepository reportRepo;

    @Mock
    private LookupService lookupService;

    @Before
    public void init(){
    	OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("OP");

        FacilitySite fs = new FacilitySite();
        fs.setId(1L);
        fs.setOperatingStatusCode(opStatCode);
        fs.setStatusYear((short)2019);

        List<EmissionsReport> erList = new ArrayList<EmissionsReport>();
        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);
        EmissionsReport er1 = new EmissionsReport();
        EmissionsReport er2 = new EmissionsReport();
        er1.setId(1L);
        er2.setId(2L);
        er1.setYear((short) 2018);
        er2.setYear((short) 2020);
        er1.setEisProgramId("1");
        er2.setEisProgramId("1");
        er1.setMasterFacilityRecord(mfr);
        er2.setMasterFacilityRecord(mfr);
        erList.add(er1);
        erList.add(er2);

        EmissionsUnit eu = new EmissionsUnit();
        eu.setId(1L);
        eu.setOperatingStatusCode(opStatCode);
        eu.setStatusYear((short)2018);
        eu.setUnitIdentifier("Boiler 001");

    	when(reportRepo.findByMasterFacilityRecordId(1L)).thenReturn(erList);
    	when(reportRepo.findByMasterFacilityRecordId(2L)).thenReturn(Collections.emptyList());
    	when(facilitySiteRepo.findByAgencyFacilityIdentifierAndEmissionsReportYear(
          		"test", "GADNR", (short) 2018)).thenReturn(fs);
    }

    @Test
    public void simpleValidatePassTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void facilityContactPhonePassTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void facilityContactPhoneFailTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();
        testData.getContacts().get(0).setPhone("1234");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_CONTACT_PHONE.value()) && errorMap.get(ValidationField.FACILITY_CONTACT_PHONE.value()).size() == 1);
    }

    @Test
    public void simpleValidateOperationStatusTypeFailTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();

        OperatingStatusCode opStatusCode = new OperatingStatusCode();
        opStatusCode.setCode("TS");

        EmissionsUnit eu = new EmissionsUnit();
        eu.setOperatingStatusCode(opStatusCode);
        eu.setFacilitySite(testData);
        testData.getEmissionsUnits().add(eu);
        testData.setOperatingStatusCode(opStatusCode);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_STATUS.value()) && errorMap.get(ValidationField.FACILITY_STATUS.value()).size() == 1);
    }

    @Test
    public void simpleValidateStatusYearRangeTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();

        testData.setStatusYear((short) 1900);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        testData.setStatusYear((short) 2019);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void simpleValidateStatusYearRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();

        testData.setStatusYear((short) 1800);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_STATUS.value()) && errorMap.get(ValidationField.FACILITY_STATUS.value()).size() == 1);

        cefContext = createContext();
        testData.setStatusYear((short) 2020);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_STATUS.value()) && errorMap.get(ValidationField.FACILITY_STATUS.value()).size() == 1);
    }

    @Test
    public void simpleValidateFacilityNAICSPrimaryFailTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();
        for (FacilityNAICSXref naicsFlag: testData.getFacilityNAICS()) {
        	naicsFlag.setNaicsCodeType(NaicsCodeType.SECONDARY);
        }

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_NAICS.value()) && errorMap.get(ValidationField.FACILITY_NAICS.value()).size() == 1);
    }

    @Test
    public void simpleValidateFacilityNAICSDuplicateFailTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();

        FacilityNAICSXref facilityNaics = new FacilityNAICSXref();
        NaicsCode naics = new NaicsCode();
        naics.setCode(332116);
        naics.setDescription("Metal Stamping");
        facilityNaics.setNaicsCode(naics);
        facilityNaics.setNaicsCodeType(NaicsCodeType.SECONDARY);
        testData.getFacilityNAICS().add(facilityNaics);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_NAICS.value()) && errorMap.get(ValidationField.FACILITY_NAICS.value()).size() == 1);
    }

    @Test
    public void simpleValidateFacilityNAICSCodeFailTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();
        testData.setFacilityNAICS(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_NAICS.value()) && errorMap.get(ValidationField.FACILITY_NAICS.value()).size() == 2);
    }

    @Test
    public void naicsCodeIsRetiredForIYWithAlternativeFailTest() {
        // Retired NAICS code triggers QA error with suggestion for non-retired NAICS code

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();

        testData.getFacilityNAICS().clear();
        FacilityNAICSXref facilityNaics = new FacilityNAICSXref();

        String alternateNaicsCode = "332119";

        NaicsCode naics = new NaicsCode();
        naics.setCode(332116);
        naics.setMapTo(alternateNaicsCode);
        naics.setDescription("Metal Stamping");
        naics.setLastInventoryYear(2011);

        NaicsCode alternateNaics = new NaicsCode();
        alternateNaics.setCode(Integer.valueOf(alternateNaicsCode));
        alternateNaics.setMapTo(null);
        alternateNaics.setDescription("Metal Crown, Closure, and Other Metal Stamping (except Automotive)");
        alternateNaics.setLastInventoryYear(null);

        facilityNaics.setNaicsCode(naics);
        facilityNaics.setNaicsCodeType(NaicsCodeType.PRIMARY);
        testData.getFacilityNAICS().add(facilityNaics);

        when(this.lookupService.retrieveNAICSCodeEntityByCode(Integer.valueOf(naics.getMapTo()))).thenReturn(alternateNaics);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_NAICS.value()) && errorMap.get(ValidationField.FACILITY_NAICS.value()).size() == 1);

        assertTrue(cefContext.result.getErrors().get(0).getErrorMsg()
            .contains("NAICS Code " + naics.getCode() + " is no longer supported. Try using " + alternateNaicsCode + " instead."));
    }

    @Test
    public void naicsCodeIsRetiredForIYWithoutAlternativeFailTest() {
        // Retired NAICS code triggers QA error without suggestion for non-retired NAICS code

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();

        testData.getFacilityNAICS().clear();
        FacilityNAICSXref facilityNaics = new FacilityNAICSXref();

        NaicsCode naics = new NaicsCode();
        naics.setCode(332116);
        naics.setMapTo(null);
        naics.setDescription("Metal Stamping");
        naics.setLastInventoryYear(2011);

        facilityNaics.setNaicsCode(naics);
        facilityNaics.setNaicsCodeType(NaicsCodeType.PRIMARY);
        testData.getFacilityNAICS().add(facilityNaics);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_NAICS.value()) && errorMap.get(ValidationField.FACILITY_NAICS.value()).size() == 1);
        assertTrue(cefContext.result.getErrors().get(0).getErrorMsg()
            .contentEquals("NAICS Code " + naics.getCode() +
                " was retired in " + naics.getLastInventoryYear() + ". Select an active NAICS or a NAICS with last inventory year greater than or equal to the current submission inventory year."));
    }

    @Test
    public void simpleValidateContactTypeFailTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();

        for (FacilitySiteContact contact: testData.getContacts()) {
        	ContactTypeCode contactTypeCode = new ContactTypeCode();
        	contactTypeCode.setCode("FAC");
        	contact.setType(contactTypeCode);
        }

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
    }

    @Test
    public void simpleValidateContactEmailAddressFailTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();
        testData.getContacts().get(0).setEmail("notAValidEmail");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);
    }

    @Test
    public void simpleValidateContactEmailAddressPassTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();
        testData.getContacts().get(0).setEmail("validemail@gmail.com");

        assertTrue(this.validator.validate(cefContext, testData));
    }

    @Test
    public void simpleValidateContactPostalCodeFailTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();
        testData.getContacts().get(0).setPostalCode("1234567890");
        testData.getContacts().get(0).setMailingPostalCode("1234567890");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);
    }

    @Test
    public void simpleValidateFacilityPostalCodeFailTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();
        testData.setPostalCode("1234567890");
        testData.setMailingPostalCode("1234567890");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);
    }

    @Test
    public void nullFacilityCountyTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();
        testData.setCountyCode(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_COUNTY.value()) && errorMap.get(ValidationField.FACILITY_COUNTY.value()).size() == 1);
    }

    @Test
    public void facilityCountyInvalidStateTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();
        testData.getStateCode().setUspsCode("NC");;

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_COUNTY.value()) && errorMap.get(ValidationField.FACILITY_COUNTY.value()).size() == 1);
    }

    @Test
    public void facilityCountyLegacyFailTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();
        testData.getStateCode().setCode("99");
        testData.getStateCode().setUspsCode("AK");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_COUNTY.value()) && errorMap.get(ValidationField.FACILITY_COUNTY.value()).size() == 1);
    }

    @Test
    public void facilityContactCountyInvalidStateTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();
        testData.getContacts().get(0).getStateCode().setCode("37");
        testData.getContacts().get(0).getStateCode().setUspsCode("NC");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_CONTACT_COUNTY.value()) && errorMap.get(ValidationField.FACILITY_CONTACT_COUNTY.value()).size() == 1);
    }

    @Test
    public void facilityContactCountyLegacyFailTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();
        testData.getContacts().get(0).getStateCode().setCode("99");
        testData.getContacts().get(0).getStateCode().setUspsCode("AK");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_CONTACT_COUNTY.value()) && errorMap.get(ValidationField.FACILITY_CONTACT_COUNTY.value()).size() == 1);
    }

    @Test
    public void nullFacilityContactEmissionsInventoryTypeTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();
        testData.setContacts(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_CONTACT.value()) && errorMap.get(ValidationField.FACILITY_CONTACT.value()).size() == 1);
    }

    @Test
    public void nullFacilityContactEmailTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();

        for (FacilitySiteContact contact: testData.getContacts()) {
        	contact.setEmail(null);
        };

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_CONTACT.value()) && errorMap.get(ValidationField.FACILITY_CONTACT.value()).size() == 1);
    }

    /**
     * There should be no errors when facility with status of not OP is a landfill or if the status year is > current cycle year
     */
    @Test
    public void facilityNotOperatingReportEmissionsPassTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();
        testData.setStatusYear((short) 2020);
        testData.getOperatingStatusCode().setCode("TS");

        FacilitySourceTypeCode sourceType = new FacilitySourceTypeCode();
        sourceType.setCode("104");
        testData.setFacilitySourceTypeCode(sourceType);
        testData.setStatusYear((short) 2000);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    /**
     * There should be errors when facility with status of not OP is not a landfill and the status year is <= current cycle year
     */
    @Test
    public void facilityNotOperatingReportEmissionsFailTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();
        FacilitySourceTypeCode sourceType = new FacilitySourceTypeCode();
        sourceType.setCode("100");
        testData.setFacilitySourceTypeCode(sourceType);
        testData.getOperatingStatusCode().setCode("TS");
        testData.setStatusYear((short) 2000);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_EMISSION_REPORTED.value()) && errorMap.get(ValidationField.FACILITY_EMISSION_REPORTED.value()).size() == 1);
    }

    /**
     * There should be an error when facility with status of ONRE or ONP
     */
    @Test
    public void facilityOperatingNotReportinOrReportingNonpointFailTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();
        FacilitySourceTypeCode sourceType = new FacilitySourceTypeCode();
        sourceType.setCode("100");
        testData.setFacilitySourceTypeCode(sourceType);
        testData.getOperatingStatusCode().setCode("ONRE");
        testData.setStatusYear((short) 2019);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);
        // error count is 2 due to the warning for not TS or PS and the error for when ONP or ONRE

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_STATUS.value()) && errorMap.get(ValidationField.FACILITY_STATUS.value()).size() == 1);
    }

    /**
     * There should be errors when facility source type code has last inventory year < current report year
     */
    @Test
    public void facilitySourceTypeCodeLegacyFailTest() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();
        FacilitySourceTypeCode sourceType = new FacilitySourceTypeCode();
        sourceType.setCode("128");
        sourceType.setLastInventoryYear(1990);
        testData.setFacilitySourceTypeCode(sourceType);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_SOURCE_TYPE_CODE.value()) && errorMap.get(ValidationField.FACILITY_SOURCE_TYPE_CODE.value()).size() == 1);
    }

    /**
     * There should be one error when operating status changes from previous year
     * and status year is earlier than year in previous report
     */
    @Test
    public void facilityOpStatusYearChangeBeforePreviousReportOpStatusYear() {

        CefValidatorContext cefContext = createContext();
        FacilitySite testData = createBaseFacilitySite();

        OperatingStatusCode opStatusCode = new OperatingStatusCode();
        opStatusCode.setCode("TS");
        testData.setOperatingStatusCode(opStatusCode);
        testData.setStatusYear((short) 2017);
        testData.getEmissionsReport().getMasterFacilityRecord().setId(1L);
        testData.setAgencyFacilityIdentifier("test");
        ProgramSystemCode psc = new ProgramSystemCode();
        psc.setCode("GADNR");
        testData.setProgramSystemCode(psc);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.FACILITY_STATUS.value()) && errorMap.get(ValidationField.FACILITY_STATUS.value()).size() == 1);
    }

    private FacilitySite createBaseFacilitySite() {

        FipsStateCode countyStateCode = new FipsStateCode();
        countyStateCode.setCode("13");
        countyStateCode.setUspsCode("GA");

        FipsCounty countyCode = new FipsCounty();
        countyCode.setCode("13313");
        countyCode.setCountyCode("13");
        countyCode.setName("Whitfield");
        countyCode.setFipsStateCode(countyStateCode);

        FipsStateCode stateCode = new FipsStateCode();
        stateCode.setCode("13");
        stateCode.setUspsCode("GA");

        FipsStateCode mailingStateCode = new FipsStateCode();
        mailingStateCode.setCode("13");
        mailingStateCode.setUspsCode("GA");

        FacilitySite result = new FacilitySite();
        result.setStatusYear(null);
        result.setPostalCode("31750");
        result.setMailingPostalCode("31750");
        result.setCountyCode(countyCode);
        result.setStateCode(stateCode);

        EmissionsReport er = new EmissionsReport();
        er.setId(2L);
        er.setYear(new Short("2019"));
        er.setEisProgramId("1");
        result.setEmissionsReport(er);

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setEisProgramId("1");
        er.setMasterFacilityRecord(mfr);

        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("OP");
        result.setOperatingStatusCode(opStatCode);

        ContactTypeCode contactTypeCode = new ContactTypeCode();
        contactTypeCode.setCode("EI");

        FipsStateCode contactStateCode = new FipsStateCode();
        contactStateCode.setCode("13");
        contactStateCode.setUspsCode("GA");

        FipsStateCode contactMailingStateCode = new FipsStateCode();
        contactMailingStateCode.setCode("13");
        contactMailingStateCode.setUspsCode("GA");

        List<FacilitySiteContact> contactList = new ArrayList<FacilitySiteContact>();
        FacilitySiteContact contact = new FacilitySiteContact();

        contact.setType(contactTypeCode);
        contact.setFirstName("Jane");
        contact.setLastName("Doe");
        contact.setEmail("jane.doe@test.com");
        contact.setPhone("1234567890");
        contact.setPhoneExt("");
        contact.setStreetAddress("123 Test Street");
        contact.setCity("Fitzgerald");
        contact.setStateCode(contactStateCode);
        contact.setPostalCode("31750");
        contact.setMailingPostalCode("31750");
        contact.setCountyCode(countyCode);
        contactList.add(contact);

        result.setContacts(contactList);

        List<FacilityNAICSXref> naicsList = new ArrayList<FacilityNAICSXref>();
        FacilityNAICSXref facilityNaics = new FacilityNAICSXref();

        NaicsCode naics = new NaicsCode();
        naics.setCode(332116);
        naics.setDescription("Metal Stamping");

        facilityNaics.setNaicsCode(naics);
        facilityNaics.setNaicsCodeType(NaicsCodeType.PRIMARY);
        naicsList.add(facilityNaics);

        result.setFacilityNAICS(naicsList);

        return result;
    }

}
