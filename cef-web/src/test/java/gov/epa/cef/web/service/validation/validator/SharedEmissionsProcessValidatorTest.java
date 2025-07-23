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
import gov.epa.cef.web.repository.*;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationFeature;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.federal.SharedEmissionsProcessValidator;
import gov.epa.cef.web.util.SLTConfigHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SharedEmissionsProcessValidatorTest extends BaseValidatorTest {

    @InjectMocks
    private SharedEmissionsProcessValidator validator;

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
    private SLTConfigHelper sltConfigHelper;

    private Pollutant pollutant;
    private EmissionsProcess p1;
    private AircraftEngineTypeCode aircraft1;
    private AircraftEngineTypeCode aircraft2;

    @Before
    public void init(){

        pollutant = new Pollutant();
        pollutant.setPollutantCode("NO3");

        List<PointSourceSccCode> sccList = new ArrayList<PointSourceSccCode>();

        PointSourceSccCode ps1 = new PointSourceSccCode();
        ps1.setCode("30503506"); // point source scc code
        ps1.setCategory(SccCategory.Point);
        PointSourceSccCode ps2 = new PointSourceSccCode();
        ps2.setCode("40500701"); // point source scc code
        ps2.setLastInventoryYear((short)2016);
        ps2.setCategory(SccCategory.Point);
        PointSourceSccCode ps3 = new PointSourceSccCode();
        ps3.setCode("30700599"); // point source scc code
        ps3.setLastInventoryYear((short)2019);
        ps3.setCategory(SccCategory.Point);
        PointSourceSccCode ps4 = new PointSourceSccCode();
        ps4.setCode("2275001000"); // point source scc code
        ps4.setCategory(SccCategory.Point);
        PointSourceSccCode ps5 = new PointSourceSccCode();
        ps5.setCode("2275050012"); // point source scc code
        ps5.setCategory(SccCategory.Point);

        sccList.add(ps1);
        sccList.add(ps2);
        sccList.add(ps3);
        sccList.add(ps4);
        sccList.add(ps5);

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



        OperatingStatusCode os = new OperatingStatusCode();
        os.setCode("OP");

        FacilitySite fs1 = new FacilitySite();
        fs1.setId(1L);
        fs1.setOperatingStatusCode(os);
        fs1.setStatusYear((short)2018);

        er1.getFacilitySites().add(fs1);

        FacilitySite fs2 = new FacilitySite();
        fs2.setId(2L);
        fs2.setOperatingStatusCode(os);
        fs2.setStatusYear((short)2020);

        er2.getFacilitySites().add(fs2);

        erList.add(er1);
        erList.add(er2);


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

        MockSLTConfig gaConfig = new MockSLTConfig();
        gaConfig.setNonPointSccEnabled(false);

        when(reportRepo.findByMasterFacilityRecordId(1L)).thenReturn(erList);
        when(reportRepo.findByMasterFacilityRecordId(2L)).thenReturn(Collections.emptyList());
        when(processRepo.retrieveByIdentifierParentFacilityYear(
            "Boiler 001","test_unit",1L,(short) 2018)).thenReturn(Collections.singletonList(p1));
        when(processRepo.retrieveByIdentifierParentFacilityYear(
            "test new","test_unit",1L,(short) 2018)).thenReturn(Collections.emptyList());
        when(emissionRepo.findAllAnnualByProcessIdReportId(2L,1L)).thenReturn(eList);
        when(sltConfigHelper.getCurrentSLTConfig("GADNR")).thenReturn(gaConfig);
    }

    @Test
    public void simpleValidatePassTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void invalidSccCodeTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();
        testData.setSccCode("40500701");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_INFO_SCC.value()) && errorMap.get(ValidationField.PROCESS_INFO_SCC.value()).size() == 1);

        cefContext = createContext();
        testData.setSccCode("2862000000");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_INFO_SCC.value()) && errorMap.get(ValidationField.PROCESS_INFO_SCC.value()).size() == 1);

        cefContext = createContext();
        testData.setSccCode("30700599");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_INFO_SCC.value()) && errorMap.get(ValidationField.PROCESS_INFO_SCC.value()).size() == 1);

        //Verify QA Checks are NOT run when Unit is NOT Operating
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);

        testData.setSccCode("40500701");
        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        testData.setSccCode("2862000000");
        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());

        testData.setSccCode("30700599");
        cefContext = createContext();
        assertTrue(this.validator.validate(cefContext, testData));
        assertNull(cefContext.result.getErrors());
    }

    @Test
    public void reportingPeriodNoEmissionsFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();
        testData.getReportingPeriods().get(0).setEmissions(null);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_PERIOD_EMISSION.value()) && errorMap.get(ValidationField.PROCESS_PERIOD_EMISSION.value()).size() == 1);
    }

    @Test
    public void reportingPeriodNoEmissionsPassTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        testData = createBaseEmissionsProcess();
        testData.getReportingPeriods().get(0).setEmissions(null);
        testData.getOperatingStatusCode().setCode("TS");
        testData.getEmissionsUnit().setStatusYear((short) 2020); // avoids different QA error

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

    }

    /**
     * There should be no errors when total emission value for pollutant code is not the same as value from previous report year.
     * There should be one error when total emission value for pollutant code for current report year is
     * the same as the emission value for previous report year
     */
    @Test
    public void copiedEmissions_Warning_Test() {

        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();
        List<Emission> eList2 = new ArrayList<Emission>();
        Emission test1 = new Emission();
        test1.setPollutant(pollutant);
        test1.setTotalEmissions(BigDecimal.valueOf(150.00));
        eList2.add(test1);

        when(emissionRepo.findAllAnnualByProcessIdReportId(testData.getId(),testData.getEmissionsUnit().getFacilitySite().getEmissionsReport().getId())).thenReturn(eList2);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        cefContext = createContext();
        test1.setTotalEmissions(BigDecimal.valueOf(130.00));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.EMISSION_TOTAL_EMISSIONS.value()) && errorMap.get(ValidationField.EMISSION_TOTAL_EMISSIONS.value()).size() == 1);
    }

    @Test
    public void copiedEmissionsSemiannualWarningTest() {

        // don't show total emissions warning when doing semiannual QAs
        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();
        List<Emission> eList2 = new ArrayList<>();
        Emission test1 = new Emission();
        test1.setPollutant(pollutant);
        test1.setTotalEmissions(BigDecimal.valueOf(150.00));
        eList2.add(test1);

        when(emissionRepo.findAllAnnualByProcessIdReportId(testData.getId(),testData.getEmissionsUnit().getFacilitySite().getEmissionsReport().getId())).thenReturn(eList2);

        cefContext.enable(ValidationFeature.SEMIANNUAL);
        test1.setTotalEmissions(BigDecimal.valueOf(130.00));

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void previousOpYearCurrentShutdownYear() {
        // pass when previous and current op status is OP
        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();
        testData.setStatusYear((short) 2017);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        // fail when previous op status is OP, current op status is PS/TS, and both have same status years
        cefContext = createContext();
        testData.getOperatingStatusCode().setCode("PS");
        testData.getEmissionsUnit().setStatusYear((short) 2020); // avoids different QA error

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        // fail when previous op status is OP, current op status is PS/TS, and current op status year is prior to previous year
        cefContext = createContext();
        testData.setStatusYear((short) 2016);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_STATUS_YEAR.value()) && errorMap.get(ValidationField.PROCESS_STATUS_YEAR.value()).size() == 1);

        // pass when facility source type code is landfill, QA is not checked if it is landfill
        cefContext = createContext();
        testData.getEmissionsUnit().getFacilitySite().getEmissionsReport().getMasterFacilityRecord().getFacilitySourceTypeCode().setCode("104");

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        // pass when previous op status is OP, current op status is PS/TS, and current op status year is after previous year
        cefContext = createContext();
        testData.setStatusYear((short) 2020);
        testData.getOperatingStatusCode().setCode("PS");
        testData.getEmissionsUnit().getFacilitySite().getEmissionsReport().getMasterFacilityRecord().getFacilitySourceTypeCode().setCode("137");

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void newProcessShutdownPassTest() {
        // pass when previous exists and current op status is OP
        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("OP");
        testData.setOperatingStatusCode(opStatCode);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        // pass when previous exists and current op status is PS/TS
        cefContext = createContext();
        testData.getOperatingStatusCode().setCode("TS");

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        // pass when previous report exists, but process doesn't and current op status is OP
        cefContext = createContext();
        testData.getOperatingStatusCode().setCode("OP");
        testData.setEmissionsProcessIdentifier("test new");

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        // pass when previous report doesn't exist and current op status is OP
        cefContext = createContext();
        testData.getEmissionsUnit().getFacilitySite().getEmissionsReport().getMasterFacilityRecord().setId(2L);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }

    @Test
    public void newRpShutdownPassFail() {
        // fail when previous report exists, but process doesn't and current op status is TS
        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);
        testData.setEmissionsProcessIdentifier("test new");

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_STATUS_CODE.value()) && errorMap.get(ValidationField.PROCESS_STATUS_CODE.value()).size() == 1);

        // fail when previous report doesn't exist and current op status is TS
        cefContext = createContext();
        testData.getEmissionsUnit().getFacilitySite().getEmissionsReport().getMasterFacilityRecord().setId(2L);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_STATUS_CODE.value()) && errorMap.get(ValidationField.PROCESS_STATUS_CODE.value()).size() == 1);
    }

    @Test
    public void opStatusPSOrTSMissingYear() {
        // fail when year is blank and current op status is TS or PS
        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setStatusYear(null);
        testData.setOperatingStatusCode(opStatCode);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_STATUS_YEAR.value()) && errorMap.get(ValidationField.PROCESS_STATUS_YEAR.value()).size() == 1);
    }

    @Test
    public void processOpYearBeforeUnitOpYearPassTest() {
        // pass when OP process year is >= OP unit year
        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();
        testData.setStatusYear(new Short("2015"));

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        // pass when TS/PS process and OP unit
        cefContext = createContext();
        testData.getEmissionsUnit().getFacilitySite().getEmissionsReport().setId(2L);
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        testData.setOperatingStatusCode(opStatCode);
        testData.setStatusYear(new Short("2018"));
        testData.getEmissionsUnit().setStatusYear(new Short("2019"));

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());

        // pass when OP process year is <= unit year
        cefContext = createContext();
        opStatCode.setCode("OP");
        testData.setOperatingStatusCode(opStatCode);
        testData.setStatusYear(new Short("2009"));
        testData.getEmissionsUnit().getFacilitySite().getEmissionsReport().getMasterFacilityRecord().getFacilitySourceTypeCode().setCode("104");

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }


    @Test
    public void processOpYearBeforeUnitOpYearFail() {
        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();
        testData.setStatusYear(new Short("2000"));

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_STATUS_YEAR.value()) && errorMap.get(ValidationField.PROCESS_STATUS_YEAR.value()).size() == 1);
    }


    @Test
    public void processTsOrPsOpYearAfterUnitTsOrPsOpYearPass() {
        // pass when process is TS, unit is TS, process Op status year == unit Op status year, and facility is not a landfill
        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();
        EmissionsProcess epPreviousYearReport = createBaseEmissionsProcess();
        epPreviousYearReport.getEmissionsUnit().getFacilitySite().getEmissionsReport().setYear((short) 2019);
        epPreviousYearReport.getEmissionsUnit().getFacilitySite().getEmissionsReport().setId(2L);
        epPreviousYearReport.setId(1L);
        epPreviousYearReport.getEmissionsUnit().setId(1L);

        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("TS");
        epPreviousYearReport.setOperatingStatusCode(opStatCode);
        testData.setOperatingStatusCode(opStatCode);
        testData.getEmissionsUnit().setOperatingStatusCode(opStatCode);
        testData.getEmissionsUnit().setStatusYear((short) 2020);
        testData.setStatusYear((short) 2020);
        testData.getEmissionsUnit().getFacilitySite().getEmissionsReport().getMasterFacilityRecord().getFacilitySourceTypeCode().setCode("106");
        testData.getEmissionsUnit().getFacilitySite().getEmissionsReport().setYear((short) 2021);

        List<EmissionsReport> erList = new ArrayList<EmissionsReport>();
        erList.add(epPreviousYearReport.getEmissionsUnit().getFacilitySite().getEmissionsReport());
        erList.add(testData.getEmissionsUnit().getFacilitySite().getEmissionsReport());
        when(reportRepo.findByMasterFacilityRecordId(1L)).thenReturn(erList);

        List<EmissionsProcess> previousProcesses = new ArrayList<EmissionsProcess>();
        previousProcesses.add(epPreviousYearReport);
        when(processRepo.retrieveByIdentifierParentFacilityYear(
            epPreviousYearReport.getEmissionsProcessIdentifier(),
            epPreviousYearReport.getEmissionsUnit().getUnitIdentifier(),
            testData.getEmissionsUnit().getFacilitySite().getEmissionsReport().getMasterFacilityRecord().getId(),
            epPreviousYearReport.getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear()
        )).thenReturn(previousProcesses);

        assertTrue(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() == null || cefContext.result.getErrors().isEmpty());
    }


    @Test
    public void processTsOrPsOpYearAfterUnitTsOrPsOpYearFail() {
        // fail when process is PS, unit is TS, process Op status year > unit Op status year, and facility is not a landfill
        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();
        EmissionsProcess epPreviousYearReport = createBaseEmissionsProcess();
        epPreviousYearReport.getEmissionsUnit().getFacilitySite().getEmissionsReport().setYear((short) 2020);
        epPreviousYearReport.getEmissionsUnit().getFacilitySite().getEmissionsReport().setId(2L);
        epPreviousYearReport.setId(1L);
        epPreviousYearReport.getEmissionsUnit().setId(1L);
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("PS");
        epPreviousYearReport.setOperatingStatusCode(opStatCode);
        testData.setOperatingStatusCode(opStatCode);
        testData.getEmissionsUnit().setOperatingStatusCode(opStatCode);
        testData.getEmissionsUnit().setStatusYear((short) 2020);
        testData.setStatusYear((short) 2021);
        testData.getEmissionsUnit().getFacilitySite().getEmissionsReport().getMasterFacilityRecord().getFacilitySourceTypeCode().setCode("106");
        testData.getEmissionsUnit().getFacilitySite().getEmissionsReport().setYear((short) 2021);

        List<EmissionsReport> erList = new ArrayList<EmissionsReport>();
        erList.add(epPreviousYearReport.getEmissionsUnit().getFacilitySite().getEmissionsReport());
        erList.add(testData.getEmissionsUnit().getFacilitySite().getEmissionsReport());
        when(reportRepo.findByMasterFacilityRecordId(1L)).thenReturn(erList);

        List<EmissionsProcess> previousProcesses = new ArrayList<EmissionsProcess>();
        previousProcesses.add(epPreviousYearReport);
        when(processRepo.retrieveByIdentifierParentFacilityYear(
            epPreviousYearReport.getEmissionsProcessIdentifier(),
            epPreviousYearReport.getEmissionsUnit().getUnitIdentifier(),
            testData.getEmissionsUnit().getFacilitySite().getEmissionsReport().getMasterFacilityRecord().getId(),
            epPreviousYearReport.getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear()
        )).thenReturn(previousProcesses);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap1 = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap1.containsKey(ValidationField.PROCESS_STATUS_YEAR.value()) && errorMap1.get(ValidationField.PROCESS_STATUS_YEAR.value()).size() == 1);
    }


    @Test
    public void operationStatusPSPreviousYearOpCurrentYearFailTest() {

        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();

        OperatingStatusCode opStatusCode = new OperatingStatusCode();
        OperatingStatusCode psStatusCode = new OperatingStatusCode();
        opStatusCode.setCode("OP");
        psStatusCode.setCode("PS");
        testData.setOperatingStatusCode(opStatusCode);
        testData.setPreviousYearOperatingStatusCode(psStatusCode);
        testData.setStatusYear((short) 2019);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_STATUS_CODE.value()) && errorMap.get(ValidationField.PROCESS_STATUS_CODE.value()).size() == 1);
    }

    @Test
    public void processMissingReportingPeriod() {
        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();
        testData.getReportingPeriods().clear();

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.REPORTING_PERIOD.value()) && errorMap.get(ValidationField.REPORTING_PERIOD.value()).size() == 1);
    }

    @Test
    public void sccChangedAndPreviousEmissionsProcessFailTest() {
        CefValidatorContext cefContext = createContext();
        EmissionsProcess testData = createBaseEmissionsProcess();

        p1.setSccCode("2275001000");
        p1.setAircraftEngineTypeCode(aircraft1);

        testData.setSccCode("2275050012");
        testData.setAircraftEngineTypeCode(aircraft2);

        assertFalse(this.validator.validate(cefContext, testData));
        assertTrue(cefContext.result.getErrors() != null && cefContext.result.getErrors().size() == 1);

        Map<String, List<ValidationError>> errorMap = mapErrors(cefContext.result.getErrors());
        assertTrue(errorMap.containsKey(ValidationField.PROCESS_INFO_SCC.value()) && errorMap.get(ValidationField.PROCESS_INFO_SCC.value()).size() == 1);
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
        psc.setCode("GADNR");

        FacilitySite facility = new FacilitySite();
        facility.setId(1L);
        facility.setEmissionsReport(report);
        facility.setProgramSystemCode(psc);
        facility.setOperatingStatusCode(os);
        facility.setStatusYear((short)2020);


        EmissionsUnit unit = new EmissionsUnit();
        unit.setId(1L);
        unit.setFacilitySite(facility);
        unit.setUnitIdentifier("test_unit");
        unit.setOperatingStatusCode(os);
        unit.setStatusYear(new Short("2010"));
        facility.getEmissionsUnits().add(unit);

        report.getFacilitySites().add(facility);

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
