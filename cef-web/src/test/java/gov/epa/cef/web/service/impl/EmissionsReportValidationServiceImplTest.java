/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package gov.epa.cef.web.service.impl;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.ValidatorChain;

import gov.epa.cef.web.config.TestCategories;
import gov.epa.cef.web.config.mock.MockSLTConfig;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.*;
import gov.epa.cef.web.service.validation.ValidationRegistry;
import gov.epa.cef.web.service.validation.ValidationResult;
import gov.epa.cef.web.service.validation.validator.IEmissionsReportValidator;
import gov.epa.cef.web.service.validation.validator.federal.*;
import gov.epa.cef.web.service.validation.validator.state.GeorgiaValidator;
import gov.epa.cef.web.util.SLTConfigHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
@Category(TestCategories.FastTest.class)
public class EmissionsReportValidationServiceImplTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Mock
    private ValidationRegistry validationRegistry;

    @Mock
    private FacilitySiteRepository facilitySiteRepo;

    @Mock
    private SLTConfigHelper sltConfigHelper;

    @Spy
    private EmissionRepository emissionRepo;

    @Spy
    private ReportHistoryRepository historyRepo;

    @Spy
    private ControlAssignmentRepository assignmentRepo;

    @Mock
    private EmissionsReportRepository reportRepo;

    @Spy
    private EmissionsProcessRepository processRepo;

    @Spy
    private EmissionsUnitRepository unitRepo;

    @Spy
    @InjectMocks
    private AnnualEmissionsProcessValidator processValidator;

    @Spy
    @InjectMocks
    private AnnualReportingPeriodValidator annualPeriodValidator;

    @Spy
    @InjectMocks
    private AnnualEmissionsReportValidator erValidator;

    @Spy
    @InjectMocks
    private AnnualControlPathValidator cpValidator;

    @InjectMocks
    private EmissionsReportValidationServiceImpl validationService;

    @InjectMocks
    private AnnualFacilitySiteValidator facilityValidator;

    @InjectMocks
    private SharedEmissionsProcessValidator sharedProcessValidator;

    @InjectMocks
    private SharedEmissionsUnitValidator sharedEmissionsUnitValidator;

    @Mock
    private PointSourceSccCodeRepository sccRepo;

    @Before
    public void _onJunitBeginTest() {

        List<Emission> eList = new ArrayList<Emission>();
        Emission e = new Emission();
        e.setId(1L);
        e.setTotalManualEntry(true);
        CalculationMethodCode cmc = new CalculationMethodCode();
        cmc.setTotalDirectEntry(false);
        e.setEmissionsCalcMethodCode(cmc);
        eList.add(e);

        List<ReportHistory> raList = new ArrayList<ReportHistory>();
        ReportHistory ra = new ReportHistory();
        ra.setId(1L);
        ra.setUserRole("Preparer");
        ra.setReportAttachmentId(1L);
        ra.setFileDeleted(false);
        raList.add(ra);

        List<ControlAssignment> caList = new ArrayList<ControlAssignment>();
        ControlAssignment ca = new ControlAssignment();
        ControlPath cp1 = new ControlPath();
        ControlPath cp2 = new ControlPath();
        cp1.setId(1L);
        cp2.setId(2L);
        ca.setId(1L);
        ca.setControlPath(cp2);
        ca.setControlPathChild(cp1);
        caList.add(ca);

        List<EmissionsReport> erList = new ArrayList<EmissionsReport>();
        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);
        EmissionsReport er1 = new EmissionsReport();
        er1.setId(1L);
        er1.setYear((short) 2018);
        er1.setEisProgramId("1");
        er1.setMasterFacilityRecord(mfr);

        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("OP");

        FacilitySite fs = new FacilitySite();
        fs.setId(1L);
        fs.setOperatingStatusCode(opStatCode);
        fs.setStatusYear((short)2019);

        er1.getFacilitySites().add(fs);
        erList.add(er1);

        OperatingStatusCode os = new OperatingStatusCode();
        os.setCode("PS");

        EmissionsUnit eu = new EmissionsUnit();
        eu.setId(1L);
        eu.setOperatingStatusCode(os);
        eu.setStatusYear((short)2018);
        eu.setUnitIdentifier(null);

        EmissionsProcess p = new EmissionsProcess();
        p.setStatusYear((short) 2020);
        p.setId(2L);
        p.setOperatingStatusCode(os);
        p.setEmissionsProcessIdentifier(null);
        p.setEmissionsUnit(eu);
        eu.getEmissionsProcesses().add(p);

        EmissionsProcess p1 = new EmissionsProcess();
        p1.setEmissionsProcessIdentifier("Boiler 001");
        p1.setId(2L);
        p1.setOperatingStatusCode(os);
        p1.setStatusYear((short) 2017);

        Pollutant pollutant = new Pollutant();
        pollutant.setPollutantCode("NO3");

        List<Emission> eList2 = new ArrayList<Emission>();
        Emission previousE1 = new Emission();
        previousE1.setPollutant(pollutant);
        previousE1.setTotalEmissions(BigDecimal.valueOf(130.00));
        eList2.add(previousE1);



        PointSourceSccCode scc = new PointSourceSccCode();
        scc.setCode("10100101");
        scc.setFuelUseRequired(true);
        scc.setMonthlyReporting(true);
        scc.setFuelUseTypes("energy,liquid");

        when(reportRepo.findByMasterFacilityRecordId(1L)).thenReturn(erList);
        when(unitRepo.retrieveByIdentifierFacilityYear(
            null,1L,(short) 2018)).thenReturn(Collections.singletonList(eu));
        when(processRepo.retrieveByIdentifierParentFacilityYear(
            "Boiler 001","test_unit",1L,(short) 2018)).thenReturn(Collections.singletonList(p1));
        when(emissionRepo.findAllByProcessIdReportId(2L,1L)).thenReturn(eList2);
        when(emissionRepo.findAllByReportId(1L)).thenReturn(eList);
        when(historyRepo.findByEmissionsReportIdOrderByActionDate(1L)).thenReturn(raList);
        when(assignmentRepo.findByControlPathChildId(1L)).thenReturn(caList);
        when(sccRepo.findById("10100101")).thenReturn(Optional.of(scc));

        when(validationRegistry.findOneByType(AnnualFacilitySiteValidator.class))
            .thenReturn(facilityValidator);

        when(validationRegistry.findOneByType(AnnualReleasePointValidator.class))
            .thenReturn(new AnnualReleasePointValidator());

        when(validationRegistry.findOneByType(AnnualEmissionsUnitValidator.class))
            .thenReturn(new AnnualEmissionsUnitValidator());

        when(validationRegistry.findOneByType(SharedEmissionsUnitValidator.class))
            .thenReturn(sharedEmissionsUnitValidator);

        when(validationRegistry.findOneByType(AnnualControlValidator.class))
            .thenReturn(new AnnualControlValidator());

        when(validationRegistry.findOneByType(AnnualControlPollutantValidator.class))
            .thenReturn(new AnnualControlPollutantValidator());

        when(validationRegistry.findOneByType(AnnualEmissionsProcessValidator.class))
            .thenReturn(processValidator);

        when(validationRegistry.findOneByType(SharedEmissionsProcessValidator.class))
            .thenReturn(sharedProcessValidator);

        when(validationRegistry.findOneByType(AnnualReportingPeriodValidator.class))
            .thenReturn(annualPeriodValidator);

        when(validationRegistry.findOneByType(SharedReportingPeriodValidator.class))
            .thenReturn(new SharedReportingPeriodValidator());

        when(validationRegistry.findOneByType(AnnualOperatingDetailValidator.class))
            .thenReturn(new AnnualOperatingDetailValidator());

        when(validationRegistry.findOneByType(SharedOperatingDetailValidator.class))
            .thenReturn(new SharedOperatingDetailValidator());

        when(validationRegistry.findOneByType(AnnualEmissionValidator.class))
            .thenReturn(new AnnualEmissionValidator());

        when(validationRegistry.findOneByType(SharedEmissionValidator.class))
            .thenReturn(new SharedEmissionValidator());

        when(validationRegistry.findOneByType(AnnualControlPathValidator.class))
            .thenReturn(cpValidator);

        when(validationRegistry.findOneByType(AnnualControlPathPollutantValidator.class))
            .thenReturn(new AnnualControlPathPollutantValidator());

        when(validationRegistry.findOneByType(MonthlyReportingPeriodValidator.class))
            .thenReturn(new MonthlyReportingPeriodValidator());

        when(validationRegistry.findOneByType(MonthlyOperatingDetailValidator.class))
            .thenReturn(new MonthlyOperatingDetailValidator());

        when(validationRegistry.findOneByType(MonthlyEmissionValidator.class))
            .thenReturn(new MonthlyEmissionValidator());

        ValidatorChain reportChain = new ValidatorChain();
        reportChain.setValidators(Arrays.asList(erValidator, new GeorgiaValidator()));

        when(validationRegistry.createValidatorChain(IEmissionsReportValidator.class))
            .thenReturn(reportChain);

        when(facilitySiteRepo.findByAgencyFacilityIdentifierAndEmissionsReportYear(
            "test", "GADNR", (short) 2018)).thenReturn(fs);

        MockSLTConfig gaConfig = new MockSLTConfig();
        gaConfig.setSltMonthlyFuelReportingEnabled(false);
        gaConfig.setSltMonthlyFuelReportingInitialYear((short)2020);
        gaConfig.setSltFeatureBillingExemptEnabled(false);
        when(sltConfigHelper.getCurrentSLTConfig("GADNR")).thenReturn(gaConfig);
    }

    @Test
    public void simpleValidateFailureTest() {

        EmissionsReport report = new EmissionsReport();
        OperatingStatusCode opStatCode = new OperatingStatusCode();
        opStatCode.setCode("OP");
        report.setId(1L);
        report.setYear((short) 2020);
        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);
        mfr.setEisProgramId("123");
        report.setMasterFacilityRecord(mfr);
        FacilitySite facilitySite = new FacilitySite();
        facilitySite.setAgencyFacilityIdentifier("test");
        facilitySite.setStatusYear((short) 2019);
        ProgramSystemCode psc = new ProgramSystemCode();
        psc.setCode("GADNR");
        facilitySite.setProgramSystemCode(psc);
        EmissionsUnit emissionsUnit = new EmissionsUnit();
        EmissionsProcess emissionsProcess = new EmissionsProcess();
        ReportingPeriod reportingPeriod = new ReportingPeriod();
        ReportingPeriodCode reportingPeriodCode = new ReportingPeriodCode();
        reportingPeriodCode.setCode("A");
        reportingPeriodCode.setShortName("Annual");
        OperatingDetail detail = new OperatingDetail();
        Emission emission = new Emission();
        emission.setTotalEmissions(new BigDecimal(10));
        ControlPath controlPath = new ControlPath();
        controlPath.setId(1L);
        Control control = new Control();
        control.setIdentifier("control_Identifier");
        control.setOperatingStatusCode(opStatCode);
        control.setPercentControl(new BigDecimal(50.0));
        control.setFacilitySite(facilitySite);
        controlPath.setFacilitySite(facilitySite);
        facilitySite.getControls().add(control);
        facilitySite.getControlPaths().add(controlPath);

        List<FacilityNAICSXref> naicsList = new ArrayList<FacilityNAICSXref>();
        FacilityNAICSXref facilityNaics = new FacilityNAICSXref();

        NaicsCode naics = new NaicsCode();
        naics.setCode(332116);
        naics.setDescription("Metal Stamping");

        facilityNaics.setNaicsCode(naics);
        facilityNaics.setNaicsCodeType(NaicsCodeType.PRIMARY);
        naicsList.add(facilityNaics);

        facilitySite.setFacilityNAICS(naicsList);
        facilitySite.setOperatingStatusCode(opStatCode);

        reportingPeriod.getEmissions().add(emission);
        reportingPeriod.setEmissionsProcess(emissionsProcess);
        reportingPeriod.getOperatingDetails().add(detail);
        reportingPeriod.setReportingPeriodTypeCode(reportingPeriodCode);
        detail.setReportingPeriod(reportingPeriod);
        emission.setReportingPeriod(reportingPeriod);
        emissionsProcess.getReportingPeriods().add(reportingPeriod);
        emissionsProcess.setEmissionsUnit(emissionsUnit);
        emissionsProcess.setOperatingStatusCode(opStatCode);
        emissionsProcess.setStatusYear((short)2019);
        emissionsUnit.getEmissionsProcesses().add(emissionsProcess);
        emissionsUnit.setOperatingStatusCode(opStatCode);
        emissionsUnit.setFacilitySite(facilitySite);
        emissionsUnit.setStatusYear((short)2000);
        facilitySite.getEmissionsUnits().add(emissionsUnit);
        facilitySite.setEmissionsReport(report);
        report.getFacilitySites().add(facilitySite);

        if(report.getStatus() != null) {
            ValidationResult result = this.validationService.validate(report, false);
            assertFalse(result.isValid());

            Map<String, ValidationError> federalErrors = result.getFederalErrors().stream()
                .collect(Collectors.toMap(ValidationError::getField, re -> re));

            logger.debug("Failures {}", String.join(", ", federalErrors.keySet()));
            assertTrue(federalErrors.containsKey("report.programSystemCode"));
            assertTrue(federalErrors.containsKey("report.facilitySite.countyCode"));
            assertTrue(federalErrors.containsKey("report.facilitySite.emissionsUnit.emissionsProcess.reportingPeriod.calculationParameterValue"));
            assertTrue(federalErrors.containsKey("report.facilitySite.emissionsUnit.emissionsProcess.reportingPeriod.emission.emissionsCalcMethodCode"));
        }
    }
}
