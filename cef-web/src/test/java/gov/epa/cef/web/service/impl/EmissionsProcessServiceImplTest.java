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

import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.config.mock.MockSLTConfig;
import gov.epa.cef.web.config.slt.SLTConfigImpl;
import gov.epa.cef.web.domain.EmissionsProcess;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.EmissionsUnit;
import gov.epa.cef.web.domain.FacilitySite;
import gov.epa.cef.web.domain.MasterFacilityRecord;
import gov.epa.cef.web.domain.ProgramSystemCode;
import gov.epa.cef.web.provider.system.SLTPropertyProvider;
import gov.epa.cef.web.repository.EmissionsProcessRepository;
import gov.epa.cef.web.service.dto.EmissionsProcessDto;
import gov.epa.cef.web.service.dto.EmissionsProcessSaveDto;
import gov.epa.cef.web.service.mapper.EmissionsProcessMapper;
import gov.epa.cef.web.service.mapper.ReleasePointApptMapper;
import gov.epa.cef.web.util.SLTConfigHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmissionsProcessServiceImplTest extends BaseServiceTest {

    private EmissionsProcess emissionProcess;
    private EmissionsProcessDto emissionsProcessDto;
    private EmissionsProcessSaveDto emissionsProcessSaveDto;

    @Mock
    private EmissionsProcessRepository processRepo;

    @Mock
    private EmissionsProcessMapper emissionsProcessMapper;

    @Mock
    private ReleasePointApptMapper releasePointApptMapper;

    @Mock
    private EmissionsReportStatusServiceImpl emissionsReportStatusService;
    
    @Mock
    private SLTConfigHelper sltConfigHelper;
    
    @Mock
    private SLTPropertyProvider sltPropertyProvider;

    @InjectMocks
    private EmissionsProcessServiceImpl emissionsProcessServiceImpl;

    @Before
    public void init(){

    	ProgramSystemCode psc = new ProgramSystemCode();
    	psc.setCode("GADNR");
    	MasterFacilityRecord mfr = new MasterFacilityRecord();
    	mfr.setId(1L);
    	mfr.setProgramSystemCode(psc);
    	FacilitySite facilitySite = new FacilitySite();
    	EmissionsReport report = new EmissionsReport();
    	report.setProgramSystemCode(psc);
    	report.getFacilitySites().add(facilitySite);
    	report.setMasterFacilityRecord(mfr);
    	EmissionsUnit emissionsUnit = new EmissionsUnit();
    	emissionsUnit.setFacilitySite(facilitySite);
    	facilitySite.setEmissionsReport(report);
    	facilitySite.getEmissionsUnits().add(emissionsUnit);
        emissionProcess = new EmissionsProcess();
        emissionProcess.setId(1L);
        emissionProcess.setEmissionsUnit(emissionsUnit);
        emissionsProcessDto = new EmissionsProcessDto();
        emissionsProcessDto.setId(1L);
        emissionsProcessSaveDto = new EmissionsProcessSaveDto();
        emissionsProcessSaveDto.setId(1L);
        when(emissionsProcessMapper.emissionsProcessToEmissionsProcessDto(null)).thenReturn(null);
        when(emissionsProcessMapper.emissionsProcessToEmissionsProcessDto(emissionProcess)).thenReturn(emissionsProcessDto);

        when(processRepo.findById(1L)).thenReturn(Optional.of(emissionProcess));
        when(processRepo.findById(2L)).thenReturn(Optional.empty());

        List<EmissionsProcess> emissionsProcessList=new ArrayList<>();
        List<EmissionsProcessDto> emissionsProcessDtosList=new ArrayList<>();
        when(emissionsProcessMapper.emissionsProcessesToEmissionsProcessDtos(emissionsProcessList))
                .thenReturn(emissionsProcessDtosList);
        when(emissionsProcessMapper.emissionsProcessesToEmissionsProcessDtos(null)).thenReturn(null);

        when(processRepo.findByReleasePointApptsReleasePointIdOrderByEmissionsProcessIdentifier(1L)).thenReturn(emissionsProcessList);
        when(processRepo.findByReleasePointApptsReleasePointIdOrderByEmissionsProcessIdentifier(2L)).thenReturn(null);

        when(processRepo.findByEmissionsUnitIdOrderByEmissionsProcessIdentifier(1L)).thenReturn(emissionsProcessList);
        when(processRepo.findByEmissionsUnitIdOrderByEmissionsProcessIdentifier(2L)).thenReturn(null);

        when(processRepo.save(emissionProcess)).thenReturn(emissionProcess);

        when(emissionsReportStatusService.resetEmissionsReportForEntity(ArgumentMatchers.anyList(), ArgumentMatchers.any())).thenReturn(null);

        MockSLTConfig gaConfig = new MockSLTConfig();
        gaConfig.setSltMonthlyFuelReportingEnabled(false);
        when(sltConfigHelper.getCurrentSLTConfig("GADNR")).thenReturn(gaConfig);
    }

    @Test
    public void update_Should_ReturnEmissionProcessDtoObject_When_EmissionProcessExists(){
        EmissionsProcessDto emissionProcess = emissionsProcessServiceImpl.update(emissionsProcessSaveDto);
        assertNotEquals(null, emissionProcess);
    }

    @Test
    public void retrieveById_Should_ReturnEmissionProcessDtoObject_When_EmissionProcessExists(){
        EmissionsProcessDto emissionProcess=emissionsProcessServiceImpl.retrieveById(1L);
        assertNotEquals(null, emissionProcess);
    }

    @Test
    public void retrieveById_Should_ReturnNull_When_EmissionProcessNotExists(){
       EmissionsProcessDto emissionProcess=emissionsProcessServiceImpl.retrieveById(2L);
       assertEquals(null, emissionProcess);
    }

    @Test
    public void retrieveById_Should_ReturnNull_When_IdIsNull() {
        EmissionsProcessDto emissionProcess = emissionsProcessServiceImpl.retrieveById(null);
        assertEquals(null, emissionProcess);
    }

    @Test
    public void retrieveForReleasePoint_Should_ReturnEmissionProcessDtosList_When_ValidReleasePointIdPassed(){
        List<EmissionsProcessDto> emissionsProcessDtosList=emissionsProcessServiceImpl.retrieveForReleasePoint(1L);
        assertNotEquals(null, emissionsProcessDtosList);
    }

    @Test
    public void retrieveForReleasePoint_Should_ReturnNull_When_InvalidReleasePointIdPassed() {
        when(processRepo.findByReleasePointApptsReleasePointIdOrderByEmissionsProcessIdentifier(2L)).thenReturn(null);
        List<EmissionsProcessDto> emissionsProcessDtosList = emissionsProcessServiceImpl.retrieveForReleasePoint(2L);
        assertEquals(null, emissionsProcessDtosList);
    }

    @Test
    public void retrieveForEmissionsUnit_Should_ReturnEmissionProcessDtosList_When_ValidEmissionsUnitIdPassed(){
        List<EmissionsProcessDto> emissionsProcessDtosList=emissionsProcessServiceImpl.retrieveForEmissionsUnit(1L);
        assertNotEquals(null, emissionsProcessDtosList);
    }

    @Test
    public void retrieveForEmissionsUnit_Should_ReturnNull_When_InvalidEmissionsUnitIdPassed(){
        List<EmissionsProcessDto> emissionsProcessDtosList=emissionsProcessServiceImpl.retrieveForEmissionsUnit(2L);
        assertEquals(null, emissionsProcessDtosList);
    }

}
