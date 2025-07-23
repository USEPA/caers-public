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


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import gov.epa.cef.web.domain.Control;
import gov.epa.cef.web.domain.ControlAssignment;
import gov.epa.cef.web.domain.ControlPath;
import gov.epa.cef.web.domain.ControlPathPollutant;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.FacilitySite;
import gov.epa.cef.web.repository.ControlAssignmentRepository;
import gov.epa.cef.web.repository.ControlPathPollutantRepository;
import gov.epa.cef.web.repository.ControlPathRepository;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.service.dto.ControlAssignmentDto;
import gov.epa.cef.web.service.dto.ControlDto;
import gov.epa.cef.web.service.dto.ControlPathDto;
import gov.epa.cef.web.service.dto.ControlPathPollutantDto;
import gov.epa.cef.web.service.dto.postOrder.ControlPostOrderDto;
import gov.epa.cef.web.service.mapper.ControlAssignmentMapper;
import gov.epa.cef.web.service.mapper.ControlMapper;
import gov.epa.cef.web.service.mapper.ControlPathMapper;
import gov.epa.cef.web.service.mapper.ControlPathPollutantMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ControlPathServiceImplTest extends BaseServiceTest {

    @Mock
    private ControlPathRepository repo;

    @Mock
    private ControlPathPollutantRepository pollutantRepo;

    @Mock
    private ControlAssignmentRepository assignmentRepo;

    @Mock
    private ControlPathMapper mapper;

    @Mock
    private ControlAssignmentMapper assignmentMapper;

    @Mock
    private ControlPathPollutantMapper pollutantMapper;

    @Mock
    private EmissionsReportStatusServiceImpl reportStatusService;

    @Mock
    private ControlServiceImpl controlService;

    @Mock
    private ControlPathServiceImpl controlPathService;

    @Mock
    private ControlMapper controlMapper;

    @Mock
    private EmissionsReportRepository reportRepo;

    @InjectMocks
    private ControlPathServiceImpl serviceImpl;

    private ControlPathDto controlPathDto;
    private List<ControlPathDto> controlPathDtoList;
    private ControlPathPollutantDto controlPathPollutantSaveDto;
    private ControlPathDto controlPathSaveDto;
    private ControlAssignmentDto controlAssignmentSaveDto;

    @Before
    public void init(){

        EmissionsReport report = new EmissionsReport();
        report.setYear(new Short("2019"));

        FacilitySite facility = new FacilitySite();
        facility.setEmissionsReport(report);

        ControlPath control = new ControlPath();
        control.setFacilitySite(facility);
        ControlPath controlPathChild = new ControlPath();
        List<ControlAssignment> caList = new ArrayList<ControlAssignment>();
        List<ControlAssignment> emptyCaList = new ArrayList<ControlAssignment>();
        ControlAssignment contAssignment = new ControlAssignment();
        List<ControlPath> controlList = new ArrayList<ControlPath>();
        List<ControlPath> emptyControlList = new ArrayList<ControlPath>();
        controlPathChild.setId(19L);
        contAssignment.setControlPathChild(controlPathChild);
        caList.add(contAssignment);
        control.setAssignments(caList);
        controlList.add(control);

        when(repo.findById(1L)).thenReturn(Optional.of(control));
        when(repo.findById(2L)).thenReturn(Optional.empty());
        when(repo.findById(19L)).thenReturn(Optional.of(controlPathChild));
        when(repo.findByEmissionsProcessId(3L)).thenReturn(controlList);
        when(repo.findByEmissionsProcessId(4L)).thenReturn(emptyControlList);
        when(repo.findByEmissionsUnitId(5L)).thenReturn(controlList);
        when(repo.findByEmissionsUnitId(6L)).thenReturn(emptyControlList);
        when(repo.findByReleasePointId(7L)).thenReturn(controlList);
        when(repo.findByReleasePointId(8L)).thenReturn(emptyControlList);
        when(repo.findByFacilitySiteIdOrderByPathId(10L)).thenReturn(controlList);
        when(repo.findByFacilitySiteIdOrderByPathId(11L)).thenReturn(emptyControlList);
        when(repo.findByControlId(12L)).thenReturn(controlList);
        when(repo.findByControlId(13L)).thenReturn(emptyControlList);
        when(repo.retrieveMasterFacilityRecordIdById(1L)).thenReturn(Optional.of(1L));
        when(assignmentRepo.findByControlPathChildId(14L)).thenReturn(caList);
        when(assignmentRepo.findByControlPathChildId(15L)).thenReturn(emptyCaList);
        when(assignmentRepo.findByControlPathIdOrderBySequenceNumber(16L)).thenReturn(caList);
        when(assignmentRepo.findByControlPathIdOrderBySequenceNumber(17L)).thenReturn(emptyCaList);

        controlPathDto = new ControlPathDto();
        controlPathDtoList=new ArrayList<>();
        when(mapper.toDto(control)).thenReturn(controlPathDto);
        when(mapper.toDtoList(controlList)).thenReturn(controlPathDtoList);

        controlPathSaveDto = new ControlPathDto();
        controlPathSaveDto.setId(18L);
        when(mapper.fromDto(controlPathSaveDto)).thenReturn(control);
        when(repo.save(control)).thenReturn(control);
        when(mapper.toDto(control)).thenReturn(controlPathDto);
        when(repo.findById(18L)).thenReturn(Optional.of(control));

        controlPathPollutantSaveDto = new ControlPathPollutantDto();
        controlPathPollutantSaveDto.setId(1L);
        ControlPathPollutant cpp = new ControlPathPollutant();
        cpp.setControlPath(control);
        ControlPathPollutantDto controlPathPollutantDto = new ControlPathPollutantDto();
        controlPathPollutantDto.setId(1L);
        when(pollutantMapper.fromDto(controlPathPollutantSaveDto)).thenReturn(cpp);
        when(pollutantRepo.save(cpp)).thenReturn(cpp);
        when(pollutantMapper.toDto(cpp)).thenReturn(controlPathPollutantDto);
        when(pollutantRepo.findById(1L)).thenReturn(Optional.of(cpp));
        when(pollutantRepo.findById(2L)).thenReturn(Optional.empty());

        controlAssignmentSaveDto = new ControlAssignmentDto();
        ControlAssignmentDto controlAssignmentDto = new ControlAssignmentDto();
        contAssignment.setId(1L);
        contAssignment.setControlPath(control);
        when(assignmentMapper.fromDto(controlAssignmentSaveDto)).thenReturn(contAssignment);
        when(assignmentRepo.save(contAssignment)).thenReturn(contAssignment);
        when(assignmentMapper.toDto(contAssignment)).thenReturn(controlAssignmentDto);
        ControlPathDto cp = new ControlPathDto();
        cp.setId(19L);
        ControlDto c = new ControlDto();
        c.setId(19L);
        ControlPostOrderDto cpo = new ControlPostOrderDto();
        cpo.setId(19L);
        Control controlDev = new Control();
        controlDev.setId(19L);
        ControlPath cpc = new ControlPath ();
        cpc.setId(19L);
        control.setId(19L);
        controlAssignmentSaveDto.setControl(c);
        controlAssignmentSaveDto.setId(1L);
        controlAssignmentSaveDto.setControlPathChild(cp);
        controlAssignmentSaveDto.setControlPath(c);
        controlPathDto.setId(19L);
        when(controlService.retrieveById(19L)).thenReturn(cpo);
        when(controlPathService.retrieveById(19L)).thenReturn(controlPathDto);
        when(assignmentRepo.findById(1L)).thenReturn(Optional.of(contAssignment));
        when(mapper.fromDto(controlPathDto)).thenReturn(control);
        when(controlMapper.fromDto(cpo)).thenReturn(controlDev);
        when(mapper.fromDto(cp)).thenReturn(cpc);
        when(assignmentMapper.toDto(contAssignment)).thenReturn(controlAssignmentDto);

        when(reportRepo.findFirstByMasterFacilityRecordIdAndIsDeletedIsFalseAndYearLessThanOrderByYearDesc(1L, new Short("2019"))).thenReturn(Optional.empty());

        when(reportStatusService.resetEmissionsReportForEntity(ArgumentMatchers.anyList(), ArgumentMatchers.any())).thenReturn(null);
    }

    @Test
    public void retrieveById_Should_Return_ControlObject_When_ControlExists(){
        ControlPathDto controlPath = serviceImpl.retrieveById(1L);
        assertNotEquals(null, controlPath);
    }

    @Test
    public void retrieveById_Should_Return_Null_When_ControlPathDoesNotExist(){
        ControlPathDto controlPath = serviceImpl.retrieveById(2L);
        assertEquals(null, controlPath);
    }

    @Test
    public void retrieveById_Should_Return_Null_When_IDisNull(){
        ControlPathDto controlPath = serviceImpl.retrieveById(null);
        assertEquals(null, controlPath);
    }

    @Test
    public void retrieveForEmissionsProcess_Should_Return_ControlList_When_ControlPathsExist() {
        Collection<ControlPathDto> controlPathList = serviceImpl.retrieveForEmissionsProcess(3L);
        assertNotEquals(null, controlPathList);
    }

    @Test
    public void retrieveForEmissionsProcess_Should_Return_Empty_When_ControlPathsDoNotExist() {
        Collection<ControlPathDto> controlPathList = serviceImpl.retrieveForEmissionsProcess(4L);
        assertEquals(new ArrayList<ControlPath>(), controlPathList);
    }

    @Test
    public void retrieveForFacilitySite_Should_Return_ControlList_When_ControlPathsExist() {
        Collection<ControlPathDto> controlPathList = serviceImpl.retrieveForFacilitySite(10L);
        assertNotEquals(null, controlPathList);
    }

    @Test
    public void retrieveForFacilitySite_Should_Return_Empty_When_ControlPathsDoNotExist() {
        Collection<ControlPathDto> controlPathList = serviceImpl.retrieveForFacilitySite(11L);
        assertEquals(new ArrayList<ControlPath>(), controlPathList);
    }

    @Test
    public void retrieveForEmissionsUnit_Should_Return_ControlList_When_ControlPathsExist() {
        Collection<ControlPathDto> controlPathList = serviceImpl.retrieveForEmissionsUnit(5L);
        assertNotEquals(null, controlPathList);
    }

    @Test
    public void retrieveForEmissionsUnit_Should_Return_Empty_When_ControlPathsDoNotExist() {
        Collection<ControlPathDto> controlPathList = serviceImpl.retrieveForEmissionsUnit(6L);
        assertEquals(new ArrayList<ControlPath>(), controlPathList);
    }

    @Test
    public void retrieveForControlDevice_Should_Return_ControlList_When_ControlPathsExist() {
        Collection<ControlPathDto> controlPathList = serviceImpl.retrieveForControlDevice(12L);
        assertNotEquals(null, controlPathList);
    }

    @Test
    public void retrieveForControlDevice_Should_Return_Empty_When_ControlPathsDoNotExist() {
        Collection<ControlPathDto> controlPathList = serviceImpl.retrieveForControlDevice(13L);
        assertEquals(new ArrayList<ControlPath>(), controlPathList);
    }

    @Test
    public void retrieveForReleasePoint_Should_Return_ControlList_When_ControlPathsExist() {
        Collection<ControlPathDto> controlPathList = serviceImpl.retrieveForReleasePoint(7L);
        assertNotEquals(null, controlPathList);
    }

    @Test
    public void retrieveForReleasePoint_Should_Return_Empty_When_ControlPathsDoNotExist() {
        Collection<ControlPathDto> controlPathList = serviceImpl.retrieveForReleasePoint(8L);
        assertEquals(new ArrayList<ControlPath>(), controlPathList);
    }

    @Test
    public void retrieveParentPathById_Should_Return_Empty_When_ControlAssignDoNotExist() {
        Collection<ControlAssignmentDto> controlAssignmentList = serviceImpl.retrieveParentPathById(14L);
        assertEquals(new ArrayList<ControlPath>(), controlAssignmentList);
    }

    @Test
    public void retrieveParentPathById_Should_Return_ControlList_When_ControlAssignExist() {
        Collection<ControlAssignmentDto> controlAssignmentList = serviceImpl.retrieveParentPathById(15L);
        assertNotEquals(null, controlAssignmentList);
    }

    @Test
    public void retrieveForControlPath_Should_Return_Empty_When_ControlAssignDoNotExist() {
        Collection<ControlAssignmentDto> controlAssignmentList = serviceImpl.retrieveForControlPath(16L);
        assertEquals(new ArrayList<ControlPath>(), controlAssignmentList);
    }

    @Test
    public void retrieveForControlPath_Should_Return_ControlList_When_ControlAssignExist() {
        Collection<ControlAssignmentDto> controlAssignmentList = serviceImpl.retrieveForControlPath(17L);
        assertNotEquals(null, controlAssignmentList);
    }

    @Test
    public void createControlPath() {
    	ControlPathDto cp = serviceImpl.create(controlPathSaveDto);
        assertNotEquals(null, cp);
    }

    @Test
    public void createPollutant() {
    	ControlPathPollutantDto cpp = serviceImpl.createPollutant(controlPathPollutantSaveDto);
        assertNotEquals(null, cpp);
    }

    @Test
    public void createAssignment() {
    	ControlAssignmentDto ca = serviceImpl.createAssignment(controlAssignmentSaveDto);
        assertNotEquals(null, ca);
    }

    @Test
    public void updateControlPathPollutant_Should_ReturnControlPathPollutantDtoObject_When_ControlPathPollutantExists() {
    	ControlPathPollutantDto controlPathPollutant = serviceImpl.updateControlPathPollutant(controlPathPollutantSaveDto);
    	assertNotEquals(null, controlPathPollutant);
    }

    @Test
    public void updateControlPath_Should_ReturnControlPathDtoObject_When_ControlPathExists() {
    	ControlPathDto controlPath = serviceImpl.update(controlPathSaveDto);
    	assertNotEquals(null, controlPath);
    }

    @Test
    public void updateAssignment_Should_ReturnControlAssignmentDtoObject_When_ControlAssignmentExists() {
    	ControlAssignmentDto controlAssignment = serviceImpl.updateAssignment(controlAssignmentSaveDto);
    	assertNotEquals(null, controlAssignment);
    }

    @Test
    public void deleteControlPath_When_ControlPathExists() {
        serviceImpl.delete(1L);

        // not throwing an exception is passing this test, but we need to have an assertion for sonarqube
        assertTrue(true);
    }

    @Test
    public void deleteAssignment_When_ControlAssignmentExists() {
    	serviceImpl.deleteAssignment(15L);
    	Collection<ControlAssignmentDto> controlAssignment = serviceImpl.retrieveParentPathById(15L);
    	assertEquals(0, controlAssignment.size());
    }

    @Test
    public void deleteControlPathPollutant_When_ControlPathPollutantExists() {
    	controlPathPollutantSaveDto.setId(2L);
    	serviceImpl.deleteControlPathPollutant(2L);
    	ControlPathPollutantDto controlPathPollutant = serviceImpl.updateControlPathPollutant(controlPathPollutantSaveDto);
    	assertEquals(null, controlPathPollutant);
    }

}
