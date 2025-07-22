/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service;

import java.util.List;

import gov.epa.cef.web.service.dto.ControlAssignmentDto;
import gov.epa.cef.web.service.dto.ControlPathDto;
import gov.epa.cef.web.service.dto.ControlPathPollutantDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlPathBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlPathPollutantBulkUploadDto;

public interface ControlPathService {

	/**
     * Create a new Control Path
     * @param dto
     * @return
     */
	ControlPathDto create(ControlPathDto dto);

    /**
     * Retrieve Control Path by its id
     * @param id
     * @return
     */
    ControlPathDto retrieveById(Long id);

    /**
     * Retrieve Control Paths for a facility site
     * @param facilitySiteId
     * @return
     */
    List<ControlPathDto> retrieveForFacilitySite(Long facilitySiteId);
    
    /**
     * Retrieve Control Paths for an emissions process
     * @param processId
     * @return
     */
    List<ControlPathDto> retrieveForEmissionsProcess(Long processId);

    /**
     * Retrieve Control Paths for an emissions unit
     * @param unitId
     * @return
     */
    List<ControlPathDto> retrieveForEmissionsUnit(Long unitId);

    /**
     * Retrieve Control Paths for a release point
     * @param pointId
     * @return
     */
    List<ControlPathDto> retrieveForReleasePoint(Long pointId);
    
    /**
     * Retrieve Control Paths by control device id
     * @param controlDeviceId
     * @return
     */
	List<ControlPathDto> retrieveForControlDevice(Long controlDeviceId);

    /**
     * Update an existing Control Path by id
     * @param dto
     * @return
     */
    ControlPathDto update(ControlPathDto dto);
    
    /**
     * Retrieve parent Control Paths for a child Control Path
     * @param id
     * @return
     */
	List<ControlAssignmentDto> retrieveParentPathById(Long id);

    /**
     * Check if a control path was previously reported
     * @param controlPathId
     * @return
     */
    Boolean isPathPreviouslyReported(Long controlPathId);

    /**
     * Delete a Control Path for a given id
     * @param controlPathId
     */
    void delete(Long controlPathId);
    
    /**
     * Create a new Control Path Pollutant
     * @param dto
	 * @return 
     */
    ControlPathPollutantDto createPollutant(ControlPathPollutantDto dto);
    
    /**
     * Update existing Control Path Pollutant
     * @param dto
	 * @return 
     */
    ControlPathPollutantDto updateControlPathPollutant(ControlPathPollutantDto dto);
    
    /**
     * Delete a Control Path Pollutant for a given id
     * @param controlPathPollutantId
     */
    void deleteControlPathPollutant(Long controlPathPollutantId);
    	
	/**
     * Create a new Control Path Assignment
     * @param dto
	 * @return 
     */
    ControlAssignmentDto createAssignment(ControlAssignmentDto dto);
    
    /**
     * Retrieve Control Path Assignments by control path id
     * @param controlPathId
     * @return
     */
	List<ControlAssignmentDto> retrieveForControlPath(Long controlPathId);
	
    /**
     * Delete a Control Path Assignment for a given id
     * @param controlPathAssignmentId
     */
    void deleteAssignment(Long controlPathAssignmentId);
    
    /**
     * Update an existing Control Path Assignment by id
     * @param dto
     * @return
     */
    ControlAssignmentDto updateAssignment(ControlAssignmentDto dto);

    /**
     * Retrieve a list of control paths for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    List<ControlPathBulkUploadDto> retrieveControlPaths(String programSystemCode, Short emissionsReportYear);

    /**
     * Retrieve a list of control path pollutants for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    List<ControlPathPollutantBulkUploadDto> retrieveControlPathPollutants(String programSystemCode, Short emissionsReportYear);

}