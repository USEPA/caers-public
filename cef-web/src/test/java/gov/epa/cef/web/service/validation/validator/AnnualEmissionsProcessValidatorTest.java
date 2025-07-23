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


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.*;

import gov.epa.cef.web.config.mock.MockSLTConfig;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.*;
import gov.epa.cef.web.util.SLTConfigHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.baidu.unbiz.fluentvalidator.ValidationError;

import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.federal.AnnualEmissionsProcessValidator;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AnnualEmissionsProcessValidatorTest extends BaseValidatorTest {

    @InjectMocks
    private AnnualEmissionsProcessValidator validator;

    @Mock
	private PointSourceSccCodeRepository sccRepo;

    @Mock
    private AircraftEngineTypeCodeRepository aircraftEngCodeRepo;

    @Mock
    private EmissionsReportRepository reportRepo;

    @Mock
    private EmissionsProcessRepository processRepo;

    @Mock
    private EmissionRepository emissionRepo;

    @Mock
    private ReportHistoryRepository historyRepo;

    @Mock
    private SLTConfigHelper sltConfigHelper;

    private Pollutant pollutant;
    private EmissionsProcess p1;
    private AircraftEngineTypeCode aircraft1;
    private AircraftEngineTypeCode aircraft2;

    @Before
    public void init(){

    	pollutant = new Pollutant();
    	pollutant.setPollutantCode("NO3");

    	PointSourceSccCode ps1 = new PointSourceSccCode();
    	ps1.setCode("30503506"); // point source scc code
    	PointSourceSccCode ps2 = new PointSourceSccCode();
    	ps2.setCode("40500701"); // point source scc code
    	ps2.setLastInventoryYear((short)2016);
    	PointSourceSccCode ps3 = new PointSourceSccCode();
    	ps3.setCode("30700599"); // point source scc code
    	ps3.setLastInventoryYear((short)2019);
    	PointSourceSccCode ps4 = new PointSourceSccCode();
    	ps4.setCode("2275001000"); // point source scc code
    	PointSourceSccCode ps5 = new PointSourceSccCode();
    	ps5.setCode("2275050012"); // point source scc code

        when(sccRepo.findById("30503506")).thenReturn(Optional.of(ps1));
        when(sccRepo.findById("40500701")).thenReturn(Optional.of(ps2));
        when(sccRepo.findById("2862000000")).thenReturn(Optional.empty());
        when(sccRepo.findById("30700599")).thenReturn(Optional.of(ps3));
        when(sccRepo.findById("2275001000")).thenReturn(Optional.of(ps4));
        when(sccRepo.findById("2275050012")).thenReturn(Optional.of(ps5));

        aircraft1 = new AircraftEngineTypeCode();
        aircraft1.setCode("1322");
        aircraft1.setScc("2275001000");
        aircraft2 = new AircraftEngineTypeCode();
        aircraft2.setCode("1850");
        aircraft2.setScc("2275050012");

        when(aircraftEngCodeRepo.findById("1322")).thenReturn(Optional.of(aircraft1));
        when(aircraftEngCodeRepo.findById("1850")).thenReturn(Optional.of(aircraft2));

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

        OperatingStatusCode os = new OperatingStatusCode();
        os.setCode("OP");

        p1 = new EmissionsProcess();
        p1.setEmissionsProcessIdentifier("Boiler 001");
        p1.setId(2L);
        p1.setOperatingStatusCode(os);
        p1.setStatusYear((short) 2017);

        List<Emission> eList = new ArrayList<Emission>();
        Emission previousE1 = new Emission();
        Emission previousE2 = new Emission();
        previousE1.setPollutant(pollutant);
        previousE1.setTotalEmissions(BigDecimal.valueOf(130.00));
        previousE2.setPollutant(pollutant);
        previousE2.setTotalEmissions(BigDecimal.valueOf(10.00));
        eList.add(previousE1);
        eList.add(previousE2);

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -1);
        Date date = c.getTime();

        PointSourceSccCode scc = new PointSourceSccCode();
        scc.setCode("10100101");
        scc.setFuelUseRequired(true);
        scc.setMonthlyReporting(true);
        scc.setFuelUseTypes("energy,liquid");

        MockSLTConfig gaConfig = new MockSLTConfig();
        gaConfig.setSltFeatureBillingExemptEnabled(true);
        MockSLTConfig doeeConfig = new MockSLTConfig();
        doeeConfig.setSltFeatureBillingExemptEnabled(false);

        when(reportRepo.findByMasterFacilityRecordId(1L)).thenReturn(erList);
        when(reportRepo.findByMasterFacilityRecordId(2L)).thenReturn(Collections.emptyList());
        when(processRepo.retrieveByIdentifierParentFacilityYear(
            "Boiler 001","test_unit",1L,(short) 2018)).thenReturn(Collections.singletonList(p1));
        when(processRepo.retrieveByIdentifierParentFacilityYear(
              "test new","test_unit",1L,(short) 2018)).thenReturn(Collections.emptyList());
        when(emissionRepo.findAllAnnualByProcessIdReportId(2L,1L)).thenReturn(eList);
        when(historyRepo.retrieveMaxSemiannualSubmissionDateByReportId(3L)).thenReturn(Optional.of(date));
        when(sccRepo.findById("10100101")).thenReturn(Optional.of(scc));
        when(sltConfigHelper.getCurrentSLTConfig("GADNR")).thenReturn(gaConfig);
        when(sltConfigHelper.getCurrentSLTConfig("DOEE")).thenReturn(doeeConfig);

    }

    @Test
    public void simpleValidatePassTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void processHasReleasePointFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();
        testData.setReleasePointAppts(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_RP.value()) && errorMap.get(ValidationField.PROCESS_RP.value()).size() == 1);
    }

    @Test
    public void processHasReleasePointPassTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void processHasReleasePointDuplicateFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();
        ReleasePoint rp2 = new ReleasePoint();
        rp2.setId(2L);
        ReleasePointAppt rpa3 = new ReleasePointAppt();
        rpa3.setEmissionsProcess(testData);
        rpa3.setPercent(BigDecimal.ZERO);
        rpa3.setReleasePoint(rp2);
        testData.getReleasePointAppts().add(rpa3);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_RP.value()) && errorMap.get(ValidationField.PROCESS_RP.value()).size() == 1);
    }

    @Test
    public void releasePointApptPercentFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();
        testData.getReleasePointAppts().get(0).setPercent(BigDecimal.valueOf(49));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_RP_PCT.value()) && errorMap.get(ValidationField.PROCESS_RP_PCT.value()).size() == 1);
    }

    @Test
    public void simpleValidateReleasePointApportionmentRangeFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();
        ReleasePointAppt rpa1 = new ReleasePointAppt();
        ReleasePoint rp1 = new ReleasePoint();
        OperatingStatusCode releasePointOpStatus = new OperatingStatusCode();
        releasePointOpStatus.setCode("OP");
        rp1.setOperatingStatusCode(releasePointOpStatus);
        rp1.setReleasePointIdentifier("test");
        rp1.setId(5L);
        rpa1.setPercent(BigDecimal.valueOf(101));
        rpa1.setReleasePoint(rp1);

        testData.getReleasePointAppts().add(rpa1);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 2);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_RP_PCT.value()) && errorMap.get(ValidationField.PROCESS_RP_PCT.value()).size() == 2);

        cefContext = createContext();
        rpa1.setPercent(BigDecimal.valueOf(-1));
        testData.getReleasePointAppts().add(rpa1);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 4);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_RP_PCT.value()) && errorMap.get(ValidationField.PROCESS_RP_PCT.value()).size() == 3);
    }

    @Test
    public void releasePointApportionedAndNotOperatingFailTest() {
        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();

        List<ReleasePointAppt> appts = new ArrayList<>();
        ReleasePointAppt rpa1 = new ReleasePointAppt();
        ReleasePoint rp1 = new ReleasePoint();
        OperatingStatusCode releasePointOpStatus = new OperatingStatusCode();
        releasePointOpStatus.setCode("PS");
        rp1.setOperatingStatusCode(releasePointOpStatus);
        rp1.setReleasePointIdentifier("test");
        rp1.setId(5L);
        rpa1.setPercent(BigDecimal.valueOf(100.0));
        rpa1.setReleasePoint(rp1);
        rpa1.setEmissionsProcess(testData);
        appts.add(rpa1);
        testData.setReleasePointAppts(appts);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_RP.value()) && errorMap.get(ValidationField.PROCESS_RP.value()).size() == 1);
    }

    @Test
    public void releasePointApportionedAndOperatingPassTest() {
        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();

        List<ReleasePointAppt> appts = new ArrayList<>();
        ReleasePointAppt rpa1 = new ReleasePointAppt();
        ReleasePoint rp1 = new ReleasePoint();
        OperatingStatusCode releasePointOpStatus = new OperatingStatusCode();
        releasePointOpStatus.setCode("OP");
        rp1.setOperatingStatusCode(releasePointOpStatus);
        rp1.setReleasePointIdentifier("test");
        rp1.setId(5L);
        rpa1.setPercent(BigDecimal.valueOf(100.0));
        rpa1.setReleasePoint(rp1);
        rpa1.setEmissionsProcess(testData);
        appts.add(rpa1);
        testData.setReleasePointAppts(appts);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void releasePointApportionedNotOperatingAndProcessNotOperatingPassTest() {
        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();

        List<ReleasePointAppt> appts = new ArrayList<>();
        ReleasePointAppt rpa1 = new ReleasePointAppt();
        ReleasePoint rp1 = new ReleasePoint();
        OperatingStatusCode opStatus = new OperatingStatusCode();
        opStatus.setCode("PS");
        testData.setOperatingStatusCode(opStatus);
        rp1.setOperatingStatusCode(opStatus);
        rp1.setReleasePointIdentifier("test");
        rp1.setId(5L);
        rpa1.setPercent(BigDecimal.valueOf(100.0));
        rpa1.setReleasePoint(rp1);
        rpa1.setEmissionsProcess(testData);
        appts.add(rpa1);
        testData.setReleasePointAppts(appts);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void aircraftCodeRequiredFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();

        testData.setSccCode("2275050012");
        testData.setAircraftEngineTypeCode(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_AIRCRAFT_CODE.value()) && errorMap.get(ValidationField.PROCESS_AIRCRAFT_CODE.value()).size() == 1);

        // Verify QA Checks are NOT run when Unit is NOT Operating
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("PS");
        testData.setOperatingStatusCode(opStatCode);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void aircraftCodeValidForSccFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();

        aircraft2.setCode("1850");
        testData.setAircraftEngineTypeCode(aircraft2);
        testData.setSccCode("2275001000");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_AIRCRAFT_CODE.value()) && errorMap.get(ValidationField.PROCESS_AIRCRAFT_CODE.value()).size() == 1);

        // Verify QA Checks are NOT run when Unit is NOT Operating
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("PS");
        testData.setOperatingStatusCode(opStatCode);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void aircraftCodePassTest() {

    	CefValidatorContext cefContext = createContext();
    	EmissionsProcess testData = createBaseEmissionsProcess();

    	testData.setAircraftEngineTypeCode(aircraft1);
    	testData.setSccCode("2275001000");

    	assertTrue(this.validator.validate(cefContext, testData));
    	assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void aircraftCodeAndSccDuplicateCombinationFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();
        EmissionsProcess ep1 = createBaseEmissionsProcess();

        testData.setAircraftEngineTypeCode(aircraft1);
        testData.setSccCode("2275001000");
        testData.setId(1L);
        ep1.setAircraftEngineTypeCode(aircraft1);
        ep1.setSccCode("2275001000");
        ep1.setId(2L);

        testData.getEmissionsUnit().getEmissionsProcesses().add(ep1);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_AIRCRAFT_CODE_AND_SCC_CODE.value()) && errorMap.get(ValidationField.PROCESS_AIRCRAFT_CODE_AND_SCC_CODE.value()).size() == 1);

        //Verify QA Checks are NOT run when Process is NOT Operating
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("I");
        testData.setOperatingStatusCode(opStatCode);

        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void processAddedAfterSemiannualSubmissionWarningTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();

        testData.getEmissionsUnit().getFacilitySite().getEmissionsReport().setMidYearSubmissionStatus(ReportStatus.APPROVED);
        testData.setSccCode("10100101");
        testData.setCreatedDate(new Date());

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSIONS_UNIT_PROCESS.value()) && errorMap.get(ValidationField.EMISSIONS_UNIT_PROCESS.value()).size() == 1);
    }

    @Test
    public void sltBillingExemptNoCommentErrorTest() {
        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();

        ProgramSystemCode psc = new ProgramSystemCode();
        psc.setCode("GADNR");

        testData.getEmissionsUnit().getFacilitySite().setProgramSystemCode(psc);
        testData.setSltBillingExempt(true);
        testData.setComments(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_COMMENTS.value()) && errorMap.get(ValidationField.PROCESS_COMMENTS.value()).size() == 1);

        cefContext = createContext();
        testData = createBaseEmissionsProcess();

        testData.getEmissionsUnit().getFacilitySite().setProgramSystemCode(psc);
        testData.setSltBillingExempt(true);
        testData.setComments("");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_COMMENTS.value()) && errorMap.get(ValidationField.PROCESS_COMMENTS.value()).size() == 1);
    }

    private EmissionsProcess createBaseEmissionsProcess() {

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);
        FacilitySourceTypeCode fstc = new FacilitySourceTypeCode();
        fstc.setCode("137");
        mfr.setFacilitySourceTypeCode(fstc);
        OperatingStatusCode os = new OperatingStatusCode();
        os.setCode("OP");

        EmissionsReport report = new EmissionsReport();
        report.setYear(new Short("2020"));
        report.setId(3L);
        report.setEisProgramId("1");
        report.setMasterFacilityRecord(mfr);

        ProgramSystemCode psc = new ProgramSystemCode();
        psc.setCode("DOEE");

        FacilitySite facility = new FacilitySite();
        facility.setId(1L);
        facility.setEmissionsReport(report);
        facility.setProgramSystemCode(psc);

        EmissionsUnit unit = new EmissionsUnit();
        unit.setId(1L);
        unit.setFacilitySite(facility);
        unit.setUnitIdentifier("test_unit");
        unit.setOperatingStatusCode(os);
        unit.setStatusYear(new Short("2010"));
        facility.getEmissionsUnits().add(unit);

        EmissionsProcess result = new EmissionsProcess();

        ReportingPeriod rperiod1 = new ReportingPeriod();
        ReportingPeriodCode rpCode = new ReportingPeriodCode();
        rpCode.setCode("A");
        rpCode.setShortName("Annual");
        rperiod1.setId(1L);
        rperiod1.setReportingPeriodTypeCode(rpCode);

        Emission e1 = new Emission();
        e1.setId(1L);
        e1.setReportingPeriod(rperiod1);
        Emission e2 = new Emission();
        e2.setId(2L);
        e2.setReportingPeriod(rperiod1);
        rperiod1.getEmissions().add(e1);
        rperiod1.getEmissions().add(e2);

        ReleasePoint rp1 = new ReleasePoint();
        ReleasePoint rp2 = new ReleasePoint();
        rp1.setId(1L);
        rp2.setId(2L);
        rp1.setOperatingStatusCode(os);
        rp2.setOperatingStatusCode(os);

        ReleasePointAppt rpa1 = new ReleasePointAppt();
        ReleasePointAppt rpa2 = new ReleasePointAppt();
        rpa1.setReleasePoint(rp1);
        rpa2.setReleasePoint(rp2);
        rpa1.setEmissionsProcess(result);
        rpa2.setEmissionsProcess(result);
        rpa1.setPercent(BigDecimal.valueOf(50));
        rpa2.setPercent(BigDecimal.valueOf(50));
        rpa1.setId(1L);
        rpa2.setId(2L);
        result.setStatusYear((short) 2020);
        result.getReleasePointAppts().add(rpa1);
        result.getReleasePointAppts().add(rpa2);
        result.setId(1L);
        result.setEmissionsUnit(unit);
        result.setAircraftEngineTypeCode(null);
        result.setOperatingStatusCode(os);
        result.getReportingPeriods().add(rperiod1);
        result.setEmissionsProcessIdentifier("Boiler 001");
        result.setSccCode("30503506");

        return result;
    }
}
