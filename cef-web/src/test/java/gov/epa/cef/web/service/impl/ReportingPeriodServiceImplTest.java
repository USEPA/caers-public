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

import gov.epa.cef.web.config.mock.MockSLTConfig;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.EmissionsProcessRepository;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.MonthlyFuelReportingRepository;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.repository.ReportingPeriodRepository;
import gov.epa.cef.web.service.dto.CodeLookupDto;
import gov.epa.cef.web.service.dto.EmissionBulkEntryHolderDto;
import gov.epa.cef.web.service.dto.ReportingPeriodBulkEntryDto;
import gov.epa.cef.web.service.dto.ReportingPeriodDto;
import gov.epa.cef.web.service.dto.ReportingPeriodUpdateResponseDto;
import gov.epa.cef.web.service.mapper.ReportingPeriodMapper;
import gov.epa.cef.web.util.SLTConfigHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ReportingPeriodServiceImplTest extends BaseServiceTest {

    @Mock
    private ReportingPeriodRepository reportingPeriodRepo;
    
    @Mock
    private EmissionsProcessRepository epRepo;

    @Mock
    private EmissionsReportRepository reportRepo;

    @Mock
    private MonthlyFuelReportingRepository monthlyRptRepo;

    @Mock
    private PointSourceSccCodeRepository pointSourceSccCodeRepo;

    @Mock
    private ReportingPeriodMapper reportingPeriodMapper;

    @Mock
    private EmissionsReportStatusServiceImpl emissionsReportStatusService;

    @Mock
    private EmissionServiceImpl emissionService;

    @Mock
    private SLTConfigHelper sltConfigHelper;

    @InjectMocks
    private ReportingPeriodServiceImpl reportingPeriodServiceImpl;

    private ReportingPeriod reportingPeriod;
    private ReportingPeriodDto reportingPeriodDto;
    private List<ReportingPeriodDto> reportingPeriodDtoList;
    private List<ReportingPeriodBulkEntryDto> reportingPeriodBulkDtoList;
    private List<EmissionBulkEntryHolderDto> emissionBulkHolderDtoList;

    private EmissionsReport report2019;
    private EmissionsReport report2018;

    private MockSLTConfig gaConfig;

    @Before
    public void init(){

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);

        ProgramSystemCode psc = new ProgramSystemCode();
        psc.setCode("GADNR");
        mfr.setProgramSystemCode(psc);

        report2019 = new EmissionsReport();
        report2019.setYear(new Short("2019"));
        report2019.setEisProgramId("1");
        report2019.setProgramSystemCode(psc);
        report2019.setMasterFacilityRecord(mfr);

        report2018 = new EmissionsReport();
        report2018.setYear(new Short("2018"));
        report2018.setEisProgramId("1");
        report2018.setMasterFacilityRecord(mfr);

        CodeLookupDto code = new CodeLookupDto();
        code.setCode("A");

        UnitMeasureCode lbUom = new UnitMeasureCode();
        lbUom.setCode("LB");
        lbUom.setDescription("POUNDS");
        lbUom.setUnitType("MASS");
        lbUom.setCalculationVariable("[lb]");

        OperatingStatusCode statusCode = new OperatingStatusCode();
        statusCode.setCode("OP");

        reportingPeriod = new ReportingPeriod();
        reportingPeriod.setId(1L);
        reportingPeriod.setCalculationParameterValue(new BigDecimal(10));
        reportingPeriod.setCalculationParameterUom(lbUom);
        ReportingPeriodCode rpc = new ReportingPeriodCode();
        rpc.setCode("A");
        rpc.setShortName("Annual");
        reportingPeriod.setReportingPeriodTypeCode(rpc);
        reportingPeriod.setEmissionsProcess(new EmissionsProcess());
        reportingPeriod.getEmissionsProcess().setOperatingStatusCode(statusCode);
        reportingPeriod.getEmissionsProcess().setEmissionsUnit(new EmissionsUnit());
        reportingPeriod.getEmissionsProcess().getEmissionsUnit().setOperatingStatusCode(statusCode);
        reportingPeriod.getEmissionsProcess().getEmissionsUnit().setFacilitySite(new FacilitySite());
        reportingPeriod.getEmissionsProcess().getEmissionsUnit().getFacilitySite().setEmissionsReport(report2019);
        reportingPeriod.getEmissionsProcess().setSccCode("10100101");

        List<ReportingPeriod> reportingPeriodList = new ArrayList<ReportingPeriod>();
        List<ReportingPeriod> emptyReportingPeriodList = new ArrayList<ReportingPeriod>();
        reportingPeriodList.add(reportingPeriod);
        when(reportingPeriodRepo.findById(1L)).thenReturn(Optional.of(reportingPeriod));
        when(reportingPeriodRepo.findById(2L)).thenReturn(Optional.empty());
        when(reportingPeriodRepo.findByEmissionsProcessId(1L)).thenReturn(reportingPeriodList);
        when(reportingPeriodRepo.findByFacilitySiteId(1L)).thenReturn(reportingPeriodList);
        when(reportingPeriodRepo.findByEmissionsProcessId(2L)).thenReturn(emptyReportingPeriodList);
        when(reportingPeriodRepo.retrieveByTypeIdentifierParentFacilityYear("A", "1", "1", 1L, new Short("2018"))).thenReturn(reportingPeriodList);
        when(reportingPeriodRepo.save(reportingPeriod)).thenReturn(reportingPeriod);

        when(reportRepo.findFirstByMasterFacilityRecordIdAndIsDeletedIsFalseAndYearLessThanOrderByYearDesc(1L, new Short("2019"))).thenReturn(Optional.of(report2018));
        when(reportRepo.findByFacilitySiteId(1L)).thenReturn(Optional.of(report2019));

        reportingPeriodDto = new ReportingPeriodDto();
        reportingPeriodDto.setId(1L);
        reportingPeriodDtoList = new ArrayList<>();
        reportingPeriodDtoList.add(reportingPeriodDto);
        ReportingPeriodBulkEntryDto bulkDto = new ReportingPeriodBulkEntryDto();
        bulkDto.setReportingPeriodId(1L);
        bulkDto.setEmissionsProcessIdentifier("1");
        bulkDto.setEmissionsProcessId((long) 1);
        bulkDto.setUnitIdentifier("1");
        bulkDto.setReportingPeriodTypeCode(code);
        bulkDto.setCalculationParameterValue("123");
        reportingPeriodBulkDtoList = new ArrayList<>();
        reportingPeriodBulkDtoList.add(bulkDto);
        when(reportingPeriodMapper.toDto(reportingPeriod)).thenReturn(reportingPeriodDto);
        when(reportingPeriodMapper.toDtoList(reportingPeriodList)).thenReturn(reportingPeriodDtoList);
        when(reportingPeriodMapper.toBulkEntryDtoList(reportingPeriodList)).thenReturn(reportingPeriodBulkDtoList);
        when(reportingPeriodMapper.toUpdateDtoFromBulkList(reportingPeriodBulkDtoList)).thenReturn(reportingPeriodDtoList);

        when(emissionsReportStatusService.resetEmissionsReportForEntity(ArgumentMatchers.anyList(), ArgumentMatchers.any())).thenReturn(null);

        emissionBulkHolderDtoList = new ArrayList<>();
        emissionBulkHolderDtoList.add(new EmissionBulkEntryHolderDto());
        when(emissionService.bulkUpdate(ArgumentMatchers.any(), ArgumentMatchers.anyList())).thenReturn(emissionBulkHolderDtoList);

        when(monthlyRptRepo.findByReportingPeriodId(1L)).thenReturn(null);

        gaConfig = new MockSLTConfig();
        gaConfig.setSltMonthlyFuelReportingEnabled(true);
        when(sltConfigHelper.getCurrentSLTConfig("GADNR")).thenReturn(gaConfig);

        PointSourceSccCode scc = new PointSourceSccCode();
        scc.setCode("10100101");
        scc.setFuelUseRequired(true);
        scc.setMonthlyReporting(true);
        scc.setFuelUseTypes("energy,liquid");
        
        EmissionsProcess ep = new EmissionsProcess();
        ep.setSccCode("10100101");

        when(pointSourceSccCodeRepo.findByCode("10100101")).thenReturn(scc);
        when(epRepo.findById(1L)).thenReturn(Optional.of(ep));
    }

    @Test
    public void update_Should_ReportingPeriodObject_When_ReportingPeriodExists(){
        ReportingPeriodUpdateResponseDto reportingPeriod = reportingPeriodServiceImpl.update(reportingPeriodDto);
        assertNotEquals(null, reportingPeriod);
    }

    @Test
    public void retrieveById_Should_Return_ReportingPeriodObject_When_ReportingPeriodExists(){
        ReportingPeriodDto reportingPeriod = reportingPeriodServiceImpl.retrieveById(1L);
        assertNotEquals(null, reportingPeriod);
    }

    @Test
    public void retrieveById_Should_Return_Null_When_ReportingPeriodDoesNotExist(){
        ReportingPeriodDto reportingPeriod = reportingPeriodServiceImpl.retrieveById(2L);
        assertEquals(null, reportingPeriod);
    }

    @Test
    public void retrieveById_Should_Return_Null_When_IDisNull(){
        ReportingPeriodDto reportingPeriod = reportingPeriodServiceImpl.retrieveById(null);
        assertEquals(null, reportingPeriod);
    }

    @Test
    public void retrieveByFacilitySiteId_Should_Return_ReportingPeriodList_When_ReportingPeriodsExist() {
        Collection<ReportingPeriodDto> reportingPeriodList = reportingPeriodServiceImpl.retrieveForEmissionsProcess(1L);
        assertNotEquals(null, reportingPeriodList);
    }

    @Test
    public void retrieveByFacilitySiteId_Should_Return_Empty_When_ReportingPeriodDoNotExist() {
        Collection<ReportingPeriodDto> reportingPeriodList = reportingPeriodServiceImpl.retrieveForEmissionsProcess(2L);
        assertEquals(new ArrayList<ReportingPeriod>(), reportingPeriodList);
    }

    @Test
    public void retrieveBulkEntryReportingPeriodsForFacilitySite_Should_Return_BulkEntryList_When_Valid() {

        gaConfig.setSltMonthlyFuelReportingEnabled(false);

        Collection<ReportingPeriodBulkEntryDto> bulkList = reportingPeriodServiceImpl.retrieveBulkEntryReportingPeriodsForFacilitySite(1L);
        assertNotEquals(new ArrayList<ReportingPeriodBulkEntryDto>(), bulkList);
    }

    @Test
    public void bulkUpdatee_Should_Return_UpdateResponseList_When_Valid() {
        Collection<EmissionBulkEntryHolderDto> updateResponseList = reportingPeriodServiceImpl.bulkUpdate(1L, reportingPeriodBulkDtoList);
        assertNotEquals(new ArrayList<ReportingPeriodBulkEntryDto>(), updateResponseList);
    }

}
