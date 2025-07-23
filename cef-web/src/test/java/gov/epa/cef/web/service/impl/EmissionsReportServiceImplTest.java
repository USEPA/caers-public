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
package gov.epa.cef.web.service.impl;

import gov.epa.cdx.shared.security.ApplicationUser;
import gov.epa.cef.web.client.soap.SignatureServiceClient;
import gov.epa.cef.web.config.CefConfig;
import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.config.mock.MockSLTConfig;
import gov.epa.cef.web.config.slt.SLTConfigImpl;
import gov.epa.cef.web.domain.Control;
import gov.epa.cef.web.domain.ControlAssignment;
import gov.epa.cef.web.domain.ControlPath;
import gov.epa.cef.web.domain.Emission;
import gov.epa.cef.web.domain.EmissionsProcess;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.EmissionsUnit;
import gov.epa.cef.web.domain.FacilityNAICSXref;
import gov.epa.cef.web.domain.FacilitySite;
import gov.epa.cef.web.domain.FacilitySiteContact;
import gov.epa.cef.web.domain.FacilitySourceTypeCode;
import gov.epa.cef.web.domain.MasterFacilityNAICSXref;
import gov.epa.cef.web.domain.MasterFacilityRecord;
import gov.epa.cef.web.domain.NaicsCode;
import gov.epa.cef.web.domain.OperatingStatusCode;
import gov.epa.cef.web.domain.PointSourceSccCode;
import gov.epa.cef.web.domain.ProgramSystemCode;
import gov.epa.cef.web.domain.ReleasePoint;
import gov.epa.cef.web.domain.ReportStatus;
import gov.epa.cef.web.domain.ReportingPeriod;
import gov.epa.cef.web.domain.ReportingPeriodCode;
import gov.epa.cef.web.domain.ValidationStatus;
import gov.epa.cef.web.provider.system.SLTPropertyProvider;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.repository.ReportChangeRepository;
import gov.epa.cef.web.service.*;
import gov.epa.cef.web.service.dto.EmissionsReportDto;
import gov.epa.cef.web.service.dto.EmissionsReportStarterDto;
import gov.epa.cef.web.service.mapper.EmissionsReportMapper;
import gov.epa.cef.web.service.mapper.FacilityNAICSMapper;
import gov.epa.cef.web.service.mapper.MasterFacilityRecordMapper;
import gov.epa.cef.web.util.SLTConfigHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyShort;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EmissionsReportServiceImplTest extends BaseServiceTest {

    @Mock
    private EmissionsReportRepository erRepo;

    @Mock
    private ReportChangeRepository reportChangeRepo;

    @Mock
    private PointSourceSccCodeRepository sccRepo;

    @Mock
    private EmissionsReportMapper emissionsReportMapper;

    @Mock
    private MasterFacilityRecordMapper mfrMapper;

    @Mock
    private FacilityNAICSMapper facilityNaicsMapper;

    @Mock
    private ReportService reportService;

    @Mock
    private CefConfig cefConfig;

    @Mock
    private EmissionService emissionService;

    @Mock
    private LookupService lookupService;

    @Mock
    private SLTPropertyProvider sltPropertyProvider;

    @Mock
    private SLTConfigHelper sltConfigHelper;

    @InjectMocks
    private EmissionsReportServiceImpl emissionsReportServiceImpl;

    private EmissionsReportDto emissionsReportDto;

    private List<EmissionsReportDto> emissionsReportDtoList;

    @Before
    public void init(){

        EmissionsReport emissionsReport = new EmissionsReport();
        EmissionsReport previousEmissionsReport = createHydratedEmissionsReport();
        List<EmissionsReport> previousEmissionsReportList = new ArrayList<>();
        previousEmissionsReportList.add(previousEmissionsReport);
        List<EmissionsReport> emissionsReportList = new ArrayList<>();
        List<EmissionsReport> emptyReportList = new ArrayList<>();
        emissionsReportList.add(emissionsReport);
        List<EmissionsReport> emptyEmissionsReportList = new ArrayList<>();
        when(erRepo.findById(1L)).thenReturn(Optional.of(emissionsReport));
        when(erRepo.findById(2L)).thenReturn(Optional.empty());
        when(erRepo.findByMasterFacilityRecordId(1L)).thenReturn(emissionsReportList);
        when(erRepo.findByMasterFacilityRecordId(2L)).thenReturn(emptyReportList);
        when(erRepo.findByMasterFacilityRecordId(1L, Sort.by(Sort.Direction.DESC, "year")))
            .thenReturn(emissionsReportList);
        when(erRepo.findByMasterFacilityRecordId(3L, Sort.by(Sort.Direction.DESC, "year")))
            .thenReturn(previousEmissionsReportList);
        when(erRepo.findByMasterFacilityRecordId(4L, Sort.by(Sort.Direction.DESC, "year")))
            .thenReturn(emptyEmissionsReportList);

        when(erRepo.save(any())).then(AdditionalAnswers.returnsFirstArg());
        when(reportChangeRepo.saveAll(any())).then(AdditionalAnswers.returnsFirstArg());

        when(emissionsReportMapper.toDto(any()))
            .thenAnswer(new Answer<EmissionsReportDto>() {

                @Override
                public EmissionsReportDto answer(InvocationOnMock invocationOnMock) throws Throwable {

                    return Mappers.getMapper(EmissionsReportMapper.class).toDto(invocationOnMock.getArgument(0));
                }
            });

        emissionsReportDto=new EmissionsReportDto();
        emissionsReportDtoList=new ArrayList<>();
        when(emissionsReportMapper.toDto(emissionsReport)).thenReturn(emissionsReportDto);
        when(emissionsReportMapper.toDtoList(emissionsReportList)).thenReturn(emissionsReportDtoList);

        ApplicationUser appUser = new ApplicationUser("fred.flintstone", Collections.emptyList());
        appUser.setClientId("BR");

        FacilityNAICSXref fxr = new FacilityNAICSXref();
        NaicsCode naics = new NaicsCode();
    	naics.setCode(456);
    	naics.setDescription("Description");
        fxr.setId(1L);
        fxr.setNaicsCode(naics);
        when(facilityNaicsMapper.toFacilityNaicsXref(previousEmissionsReport.getMasterFacilityRecord().getMasterFacilityNAICS().get(0))).thenReturn(fxr);

        when(this.emissionService.updateEmissionFactorDetails(any(Emission.class), any(EmissionsProcess.class), anyShort(), any())).thenReturn(mock(Emission.class));

        List<PointSourceSccCode> sccList = new ArrayList<>();
        PointSourceSccCode scc = new PointSourceSccCode();
        scc.setCode("30700734");
        scc.setFuelUseRequired(true);
        when(sccRepo.findByMonthlyReporting(true)).thenReturn(sccList);
        MockSLTConfig gaConfig = new MockSLTConfig();
        gaConfig.setSltMonthlyFuelReportingEnabled(false);

        SLTBaseConfig sltConfig = new SLTConfigImpl("GADNR", sltPropertyProvider);
        when(sltConfigHelper.getCurrentSLTConfig("GADNR")).thenReturn(sltConfig);

        List<ReportingPeriodCode> rpcs = new ArrayList<>();
        ReportingPeriodCode rpc = new ReportingPeriodCode();
        rpc.setCode("JAN");
        rpc.setShortName("January");
        rpcs.add(rpc);
        rpc.setCode("FEB");
        rpc.setShortName("February");
        rpcs.add(rpc);
        rpc.setCode("MAR");
        rpc.setShortName("March");
        rpcs.add(rpc);
        rpc.setCode("APR");
        rpc.setShortName("April");
        rpcs.add(rpc);
        rpc.setCode("MAY");
        rpc.setShortName("May");
        rpcs.add(rpc);
        rpc.setCode("JUN");
        rpc.setShortName("June");
        rpcs.add(rpc);
        rpc.setCode("JUL");
        rpc.setShortName("July");
        rpcs.add(rpc);
        rpc.setCode("AUG");
        rpc.setShortName("August");
        rpcs.add(rpc);
        rpc.setCode("SEP");
        rpc.setShortName("September");
        rpcs.add(rpc);
        rpc.setCode("OCT");
        rpc.setShortName("October");
        rpcs.add(rpc);
        rpc.setCode("NOV");
        rpc.setShortName("November");
        rpcs.add(rpc);
        rpc.setCode("DEC");
        rpc.setShortName("December");
        rpcs.add(rpc);
        rpc.setCode("SA");
        rpc.setShortName("Semiannual");
        rpcs.add(rpc);

        when(lookupService.findReportingPeriodMonthAndSemiAnnualCodes()).thenReturn(rpcs);

    }

    @Test
    public void findById_Should_Return_EmissionsReportObject_When_EmissionsReportExists(){
        EmissionsReportDto emissionsReport = emissionsReportServiceImpl.findById(1L);
        assertEquals(emissionsReportDto, emissionsReport);
    }

    @Test
    public void findById_Should_Return_Null_When_EmissionsReportNotExist(){
        EmissionsReportDto emissionsReport = emissionsReportServiceImpl.findById(2L);
        assertEquals(null, emissionsReport);
    }

    @Test
    public void findById_Should_Return_Null_When_IDisNull(){
        EmissionsReportDto emissionsReport = emissionsReportServiceImpl.findById(null);
        assertEquals(null, emissionsReport);
    }

    @Test
    public void findByFacilityId_Should_Return_ReportList_WhenReportExist() {
        Collection<EmissionsReportDto> emissionsReportList = emissionsReportServiceImpl.findByMasterFacilityRecordId(1L);
        assertEquals(emissionsReportDtoList, emissionsReportList);
    }

//    @Test
//    public void findByFacilityId_Should_ReturnReportListWithCurrentYear() {
//        Collection<EmissionsReportDto> emissionsReportList = emissionsReportServiceImpl.findByFacilityEisProgramId("XXXX", true);
//        assertEquals(2, emissionsReportList.size());
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        short currentYear = (short) calendar.get(Calendar.YEAR);
//        Object[] array = emissionsReportList.toArray();
//        EmissionsReportDto dto = (EmissionsReportDto) array[0];
//        assertEquals(dto.getYear().shortValue(), currentYear);
//    }

    @Test
    public void findByFacilityId_Should_Return_Empty_WhenReportsDoNotExist() {
        Collection<EmissionsReportDto> emissionsReportList = emissionsReportServiceImpl.findByMasterFacilityRecordId(2L);
        assertEquals(0, emissionsReportList.size());
    }

    @Test
    public void findMostRecentByFacilityEisProgramId_Should_ReturnTheLatestEmissionsReportForAFacility_WhenValidFacilityEisProgramIdPassed() {
        EmissionsReportDto emissionsReport = emissionsReportServiceImpl.findMostRecentByMasterFacilityRecordId(1L);
        assertNotEquals(null, emissionsReport);
    }

    @Test
    public void createEmissionReportCopy_Should_ReturnValidDeepCopy_WhenValidFacilityAndYearPassed() {
        EmissionsReportStarterDto starter = new EmissionsReportStarterDto();
        starter.setMasterFacilityRecordId(3L);
        starter.setYear((short) 2020);
        starter.setProgramSystemCode("GADNR");
    	EmissionsReport originalEmissionsReport = createHydratedEmissionsReport();
    	EmissionsReportDto emissionsReportCopy = emissionsReportServiceImpl.createEmissionReportCopy(starter);
    	assertEquals(ReportStatus.IN_PROGRESS.toString(), emissionsReportCopy.getStatus());
    	assertEquals(ValidationStatus.UNVALIDATED.toString(), emissionsReportCopy.getValidationStatus());
    	assertEquals("2020", emissionsReportCopy.getYear().toString());
    	assertNotEquals(originalEmissionsReport.getId(), emissionsReportCopy.getId());
    }

    @Test
    public void createEmissionReportCopy_Should_ReturnNull_WhenPreviousDoesNotExist() {
        EmissionsReportStarterDto starter = new EmissionsReportStarterDto();
        starter.setMasterFacilityRecordId(4L);
        starter.setYear((short) 2020);
        EmissionsReportDto nullEmissionsReportCopy = emissionsReportServiceImpl.createEmissionReportCopy(starter);

        assertNull(nullEmissionsReportCopy);
    }


    private EmissionsReport createHydratedEmissionsReport() {
    	ProgramSystemCode  psc = new ProgramSystemCode();
    	psc.setCode("GADNR");

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setAgencyFacilityIdentifier("ALTID");
        mfr.setCity("Raleigh");
        mfr.setDescription("Facility Description");
        mfr.setEisProgramId("EISID");
        mfr.setId(1L);
        mfr.setLatitude(BigDecimal.valueOf(2.5d));
        mfr.setLongitude(BigDecimal.valueOf(2.5d));
        mfr.setProgramSystemCode(psc);

        List<MasterFacilityNAICSXref> masterFacilityNAICS = new ArrayList<>();
        MasterFacilityNAICSXref mfXref = new MasterFacilityNAICSXref();
        mfXref.setMasterFacilityRecord(mfr);
        mfXref.setId(1L);
    	NaicsCode naics = new NaicsCode();
    	naics.setCode(123);
    	naics.setDescription("ABCDE");
    	mfXref.setNaicsCode(naics);
    	masterFacilityNAICS.add(mfXref);
    	mfr.setMasterFacilityNAICS(masterFacilityNAICS);

    	EmissionsReport er = new EmissionsReport();
    	er.setEisProgramId("");
    	er.setId(1L);
    	er.setStatus(ReportStatus.APPROVED);
    	er.setValidationStatus(ValidationStatus.PASSED);
    	er.setYear((short) 2018);

    	er.setProgramSystemCode(psc);
    	er.setMasterFacilityRecord(mfr);

    	OperatingStatusCode opStatus = new OperatingStatusCode();
    	opStatus.setCode("OP");

    	List<FacilitySite> facilitySites = new ArrayList<>();
    	FacilitySite fs = new FacilitySite();
    	fs.setAgencyFacilityIdentifier("ALTID");
    	fs.setCity("Raleigh");
    	fs.setDescription("Facility Description");
    	fs.setEmissionsReport(er);
    	fs.setId(1L);
    	fs.setLatitude(BigDecimal.valueOf(2.5d));
    	fs.setLongitude(BigDecimal.valueOf(2.5d));

    	FacilitySourceTypeCode fstc = new FacilitySourceTypeCode();
    	fstc.setCode("Source Type Code");
    	fstc.setDescription("Source Type Desc");
    	fs.setFacilitySourceTypeCode(fstc);

    	List<FacilitySiteContact> contacts = new ArrayList<>();
    	FacilitySiteContact fsc = new FacilitySiteContact();
    	fsc.setCity("Raleigh");
    	fsc.setId(1L);
    	fsc.setFacilitySite(fs);
    	fsc.setFirstName("John");
    	fsc.setLastName("Doe");
    	contacts.add(fsc);
    	fs.setContacts(contacts);

    	List<FacilityNAICSXref> facilityNAICS = new ArrayList<>();
    	FacilityNAICSXref xref = new FacilityNAICSXref();
    	xref.setFacilitySite(fs);
    	xref.setId(1L);
    	xref.setNaicsCode(naics);
    	fs.setFacilityNAICS(facilityNAICS);

    	List<ReleasePoint> releasePoints = new ArrayList<>();
    	ReleasePoint rp = new ReleasePoint();
    	rp.setOperatingStatusCode(opStatus);
    	rp.setId(1L);
    	rp.setComments("Comments");
    	releasePoints.add(rp);
    	fs.setReleasePoints(releasePoints);

    	List<Control> controls = new ArrayList<>();
    	Control control = new Control();
    	control.setOperatingStatusCode(opStatus);
    	control.setId(1L);
    	control.setFacilitySite(fs);

    	List<ControlAssignment> assignments = new ArrayList<>();
    	ControlAssignment ca = new ControlAssignment();
    	ca.setControl(control);
    	ca.setId(1L);
    	ControlPath cp = new ControlPath();
    	List<ControlAssignment> caSet = new ArrayList<>();
    	caSet.add(ca);
    	cp.setAssignments(caSet);
    	cp.setId(1L);
    	ca.setControlPath(cp);
    	assignments.add(ca);

    	control.setAssignments(assignments);
    	controls.add(control);
    	fs.setControls(controls);


    	List<EmissionsUnit> units = new ArrayList<>();
    	EmissionsUnit eu = new EmissionsUnit();
    	eu.setOperatingStatusCode(opStatus);
    	eu.setId(1L);
    	eu.setComments("Test Unit");
    	eu.setFacilitySite(fs);

    	List<EmissionsProcess> processes = new ArrayList<>();
    	EmissionsProcess ep = new EmissionsProcess();
    	ep.setOperatingStatusCode(opStatus);
    	ep.setId(1L);
    	ep.setEmissionsUnit(eu);
    	ep.setComments("Test Process Comments");

    	List<ReportingPeriod> reportingPeriods = new ArrayList<>();
    	ReportingPeriod repPer = new ReportingPeriod();
    	repPer.setId(1L);
    	repPer.setComments("Reporting Period Comments");
    	repPer.setEmissionsProcess(ep);

    	ReportingPeriodCode rptc = new ReportingPeriodCode();
    	rptc.setCode("A");
    	rptc.setShortName("Annual");
    	repPer.setReportingPeriodTypeCode(rptc);

    	List<Emission> emissions = new ArrayList<>();
    	Emission e = new Emission();
    	e.setId(1L);
    	e.setComments("Test Emission Comments");
    	emissions.add(e);
    	repPer.setEmissions(emissions);

    	reportingPeriods.add(repPer);
    	ep.setReportingPeriods(reportingPeriods);
    	processes.add(ep);
    	eu.setEmissionsProcesses(processes);
    	units.add(eu);
    	fs.setEmissionsUnits(units);

    	facilitySites.add(fs);
    	er.setFacilitySites(facilitySites);

    	return er;
	}
}
