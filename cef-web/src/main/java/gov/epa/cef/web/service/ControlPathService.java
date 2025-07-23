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