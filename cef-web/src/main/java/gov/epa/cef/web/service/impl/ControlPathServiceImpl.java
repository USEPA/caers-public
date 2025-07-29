/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.epa.cef.web.domain.Control;
import gov.epa.cef.web.domain.ControlAssignment;
import gov.epa.cef.web.domain.ControlPath;
import gov.epa.cef.web.domain.ControlPathPollutant;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.repository.ControlAssignmentRepository;
import gov.epa.cef.web.repository.ControlPathPollutantRepository;
import gov.epa.cef.web.repository.ControlPathRepository;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.service.ControlPathService;
import gov.epa.cef.web.service.dto.ControlAssignmentDto;
import gov.epa.cef.web.service.dto.ControlDto;
import gov.epa.cef.web.service.dto.ControlPathDto;
import gov.epa.cef.web.service.dto.ControlPathPollutantDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlPathBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlPathPollutantBulkUploadDto;
import gov.epa.cef.web.service.mapper.BulkUploadMapper;
import gov.epa.cef.web.service.mapper.ControlAssignmentMapper;
import gov.epa.cef.web.service.mapper.ControlMapper;
import gov.epa.cef.web.service.mapper.ControlPathMapper;
import gov.epa.cef.web.service.mapper.ControlPathPollutantMapper;

@Service
public class ControlPathServiceImpl implements ControlPathService {

    @Autowired
    private ControlPathRepository repo;

    @Autowired
    private ControlAssignmentRepository assignmentRepo;

    @Autowired
    private ControlPathMapper mapper;

    @Autowired
    private ControlAssignmentMapper assignmentMapper;

    @Autowired
    private ControlMapper controlMapper;

    @Autowired
    private EmissionsReportStatusServiceImpl reportStatusService;

    @Autowired
    private ControlServiceImpl controlService;

    @Autowired
    private ControlPathPollutantMapper pollutantMapper;

    @Autowired
    private ControlPathPollutantRepository pollutantRepo;

    @Autowired
    private EmissionsReportRepository reportRepo;

    @Autowired
    private BulkUploadMapper bulkUploadMapper;

    @Override
    public ControlPathDto retrieveById(Long id) {
        ControlPath result = getPathById(id);
        return mapper.toDto(result);
    }

    @Override
    public List<ControlPathDto> retrieveForEmissionsProcess(Long processId) {
        List<ControlPath> result = repo.findByEmissionsProcessId(processId);
        result.addAll(getChildren(result));
        return mapper.toDtoList(result);
    }

    @Override
    public List<ControlPathDto> retrieveForFacilitySite(Long facilitySiteId) {
    	List<ControlPath> result = repo.findByFacilitySiteIdOrderByPathId(facilitySiteId);
        return mapper.toDtoList(result);    }

	@Override
    public List<ControlPathDto> retrieveForEmissionsUnit(Long unitId) {
        List<ControlPath> result = repo.findByEmissionsUnitId(unitId);
        result.addAll(getChildren(result));
        return mapper.toDtoList(result);
    }

	@Override
    public List<ControlPathDto> retrieveForControlDevice(Long controlDeviceId) {
        List<ControlPath> result = repo.findByControlId(controlDeviceId);
        result.addAll(getChildren(result));
        return mapper.toDtoList(result);
    }

	@Override
    public List<ControlAssignmentDto> retrieveForControlPath(Long controlPathId) {
        List<ControlAssignment> result = assignmentRepo.findByControlPathIdOrderBySequenceNumber(controlPathId);
        return assignmentMapper.toDtoList(result);
    }

	@Override
    public List<ControlAssignmentDto> retrieveParentPathById(Long controlPathId) {
		List<ControlAssignment> result = assignmentRepo.findByControlPathChildId(controlPathId);
        return assignmentMapper.toDtoList(result);
    }

    @Override
    public List<ControlPathDto> retrieveForReleasePoint(Long pointId) {
        List<ControlPath> result = repo.findByReleasePointId(pointId);
        result.addAll(getChildren(result));
        return mapper.toDtoList(result);
    }

    /**
     * Create a new Control Path from a DTO object
     */
    public ControlPathDto create(ControlPathDto dto) {
    	ControlPath controlPath = mapper.fromDto(dto);

    	ControlPathDto result = mapper.toDto(repo.save(controlPath));
    	reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(result.getId()), ControlPathRepository.class);
    	return result;
    }

    /**
     * Create a new Control Path Pollutant from a DTO object
     */
    public ControlPathPollutantDto createPollutant(ControlPathPollutantDto dto) {
    	ControlPathPollutantDto result = null;
    	ControlPathPollutant controlPath = pollutantMapper.fromDto(dto);

    	if (controlPath != null) {
	    	result = pollutantMapper.toDto(pollutantRepo.save(controlPath));
	    	reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(result.getControlPathId()), ControlPathRepository.class);
    	}
    	return result;
    }

    /**
     * Update an existing Control Path Pollutant from a DTO
     */
    public ControlPathPollutantDto updateControlPathPollutant(ControlPathPollutantDto dto) {

    	ControlPathPollutantDto result = null;
    	ControlPathPollutant controlPath = pollutantRepo.findById(dto.getId()).orElse(null);
    	pollutantMapper.updateFromDto(dto, controlPath);

    	if (controlPath != null) {
	    	result = pollutantMapper.toDto(pollutantRepo.save(controlPath));
	    	reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(result.getControlPathId()), ControlPathRepository.class);
    	}
    	return result;
    }

    /**
     * Delete a Control Path Pollutant for a given id
     * @Param controlPathPollutantId
     */
    public void deleteControlPathPollutant(Long controlPathPollutantId) {
        reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(controlPathPollutantId), ControlPathPollutantRepository.class);
    	pollutantRepo.deleteById(controlPathPollutantId);
    }

	/***
	 * Get child paths associated with the ControlPaths in the paths list
     *
     * This will help flatten out the ControlPath parent-child relationships so that all ControlPaths will be included in the same list
	 * @param paths
	 */
	private List<ControlPath> getChildren(List<ControlPath> paths) {
		List<ControlPath> childPaths = new ArrayList<>();

		if (paths != null) {
	        for (ControlPath path : paths) {
	        	childPaths.addAll(getChildPaths(path));
	        }
        }

		return childPaths;
	}

    /**
     * Update an existing Control Path from a DTO
     */
    public ControlPathDto update(ControlPathDto dto) {

    	ControlPathDto result = null;
    	ControlPath controlPath = repo.findById(dto.getId()).orElse(null);
    	mapper.updateFromDto(dto, controlPath);

    	if (controlPath != null) {
	    	result = mapper.toDto(repo.save(controlPath));
	    	reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(result.getId()), ControlPathRepository.class);
    	}
    	return result;
    }


    /***
     * Iterate the path's assignments looking for child paths
     * @param path
     */
    private List<ControlPath> getChildPaths(ControlPath path) {
    	List<ControlPath> childPaths = new ArrayList<>();
    	if (!path.getAssignments().isEmpty()) {
			for (ControlAssignment assignment : path.getAssignments()) {
				if (assignment.getControlPathChild() != null) {
					ControlPath childPath = getPathById(assignment.getControlPathChild().getId());
					if (childPath != null) {
						childPaths.add(childPath);
						childPaths.addAll(getChildPaths(childPath));
					}
				}
			}
    	}

    	return childPaths;
	}


    /***
     * Retrieve a ControlPath from the repo based on it's ID
     * @param id
     * @return
     */
    private ControlPath getPathById(Long id) {
    	return repo
    			.findById(id)
    			.orElse(null);
    }

    /**
     * Check if a control path was previously reported
     * @param controlPathId
     * @return
     */
    public Boolean isPathPreviouslyReported(Long controlPathId) {
        ControlPath path= repo
                .findById(controlPathId)
                .orElse(null);

        Long mfrId = repo.retrieveMasterFacilityRecordIdById(controlPathId).orElse(null);

        // find the last year reported
        Optional<EmissionsReport> lastReport = reportRepo.findFirstByMasterFacilityRecordIdAndIsDeletedIsFalseAndYearLessThanOrderByYearDesc(mfrId,
                path.getFacilitySite().getEmissionsReport().getYear());

        // check if the control path was reported last year
        if (lastReport.isPresent()) {
            return repo.retrieveByIdentifierFacilityYear(path.getPathId(),
                    mfrId,
                    lastReport.get().getYear())
                    .stream().findFirst().isPresent();
        }

        return false;
    }

    /**
     * Delete a Control Path for a given id
     * @Param controlId
     */
    public void delete(Long controlPathId) {

        reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(controlPathId), ControlPathRepository.class);
    	repo.deleteById(controlPathId);
    }

    /**
     * Create a new Control Path Assignment from a DTO object
     */
    public ControlAssignmentDto createAssignment(ControlAssignmentDto dto){
    	ControlAssignment controlAssignment = assignmentMapper.fromDto(dto);

    	ControlAssignmentDto result = assignmentMapper.toDto(assignmentRepo.save(controlAssignment));

    	reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(result.getId()), ControlAssignmentRepository.class);
    	return result;
    }

    /**
     * Delete a Control Path Assignment for a given id
     * @Param controlId
     */
    public void deleteAssignment(Long controlPathAssignmentId) {
        reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(controlPathAssignmentId), ControlAssignmentRepository.class);
    	assignmentRepo.deleteById(controlPathAssignmentId);
    }

    /**
     * Update an existing Control Path Assignment from a DTO
     */
    public ControlAssignmentDto updateAssignment(ControlAssignmentDto dto) {

    	ControlAssignmentDto result = null;
    	ControlPathDto controlPathChildDto = null;
    	ControlDto controlDto = null;

    	if(dto.getControl() != null){
        	controlDto = controlService.retrieveById(dto.getControl().getId());
    	}
    	if(dto.getControlPathChild() != null){
    		controlPathChildDto = this.retrieveById(dto.getControlPathChild().getId());
    	}
    	ControlPathDto controlPathDto = this.retrieveById(dto.getControlPath().getId());

    	ControlAssignment controlPathAssignment = assignmentRepo.findById(dto.getId()).orElse(null);
    	dto.setControlPath(null);
    	dto.setControlPathChild(null);
    	dto.setControl(null);
    	assignmentMapper.updateFromDto(dto, controlPathAssignment);
    	ControlPath controlPath = mapper.fromDto(controlPathDto);
    	if(controlPathAssignment != null) {
    		controlPathAssignment.setControlPath(controlPath);

	    	if(controlDto != null && controlDto.getId() != null){
	        	Control control = controlMapper.fromDto(controlDto);
	        	controlPathAssignment.setControl(control);
	    	}
	    	if(controlPathChildDto != null && controlPathChildDto.getId() != null){
	        	ControlPath controlPathChild = mapper.fromDto(controlPathChildDto);
	        	controlPathAssignment.setControlPathChild(controlPathChild);
	    	}

	    	result = assignmentMapper.toDto(assignmentRepo.save(controlPathAssignment));
	    	reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(result.getId()), ControlAssignmentRepository.class);
    	}
    	return result;
    }


    /**
     * Retrieve a list of control paths for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    public List<ControlPathBulkUploadDto> retrieveControlPaths(String programSystemCode, Short emissionsReportYear) {
    	List<ControlPath> controlPaths = repo.findByPscAndEmissionsReportYear(programSystemCode, emissionsReportYear);
    	return bulkUploadMapper.controlPathToDtoList(controlPaths);
    }


    /**
     * Retrieve a list of control path pollutants for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    public List<ControlPathPollutantBulkUploadDto> retrieveControlPathPollutants(String programSystemCode, Short emissionsReportYear) {
    	List<ControlPathPollutant> controlPathPollutants = pollutantRepo.findByPscAndEmissionsReportYear(programSystemCode, emissionsReportYear);
    	return bulkUploadMapper.controlPathPollutantToDtoList(controlPathPollutants);
    }

}
