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