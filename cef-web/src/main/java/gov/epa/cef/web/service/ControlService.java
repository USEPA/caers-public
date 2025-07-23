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
import gov.epa.cef.web.service.dto.ControlDto;
import gov.epa.cef.web.service.dto.ControlPollutantDto;
import gov.epa.cef.web.service.dto.EmissionsReportItemDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlAssignmentBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlPollutantBulkUploadDto;
import gov.epa.cef.web.service.dto.postOrder.ControlPostOrderDto;

public interface ControlService {
	
	/**
     * Create a new Control
     * @param dto
     * @return
     */
	ControlDto create(ControlDto dto);

    /**
     * Retrieve Control by its id
     * @param id
     * @return
     */
    ControlPostOrderDto retrieveById(Long id);

    /**
     * Retrieve versions of this control from the last year reported
     * @param id
     * @return
     */
    ControlPostOrderDto retrievePreviousById(Long id);

    /**
     * Retrieve Controls for a facility site
     * @param facilitySiteId
     * @return
     */
    List<ControlPostOrderDto> retrieveForFacilitySite(Long facilitySiteId);
    
    /***
     * Retrieve a DTO containing all of the related sub-facility components for the given control
     * @param controlId
     * @return
     */
    List<EmissionsReportItemDto> retrieveControlComponents(Long controlId);
    
    /**
     * Update an existing Control by id
     * @param dto
     * @return
     */
    ControlDto update(ControlDto dto);
    
    /**
     * Delete a Control for a given id
     * @param controlId
     */
    void delete(Long controlId);
    
	/**
     * Create a new Control Pollutant
     * @param dto
     * @return
     */
	ControlPollutantDto createPollutant(ControlPollutantDto dto);
	
    /**
     * Update an existing Control Pollutant by id
     * @param dto
     * @return
     */
    ControlPollutantDto updateControlPollutant(ControlPollutantDto dto);
    
    /**
     * Delete a Control Pollutant for a given id
     * @param controlPollutantId
     */
    void deleteControlPollutant(Long controlPollutantId);

    /**
     * Retrieve a list of controls for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    List<ControlBulkUploadDto> retrieveControls(String programSystemCode, Short emissionsReportYear);

    /**
     * Retrieve a list of control assignments for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    List<ControlAssignmentBulkUploadDto> retrieveControlAssignments(String programSystemCode, Short emissionsReportYear);

    /**
     * Retrieve a list of control pollutants for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    List<ControlPollutantBulkUploadDto> retrieveControlPollutants(String programSystemCode, Short emissionsReportYear);
    
    /**
     * Retrieve Control Assignments by control id
     * @param controlId
     * @return
     */
	List<ControlAssignmentDto> retrieveControlAssignmentsForControl(Long controlId);
}