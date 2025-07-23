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
package gov.epa.cef.web.api.rest;

import gov.epa.cef.web.repository.ControlPollutantRepository;
import gov.epa.cef.web.repository.ControlRepository;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.ControlService;
import gov.epa.cef.web.service.dto.ControlAssignmentDto;
import gov.epa.cef.web.service.dto.ControlDto;
import gov.epa.cef.web.service.dto.ControlPollutantDto;
import gov.epa.cef.web.service.dto.EmissionsReportItemDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlAssignmentBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlPollutantBulkUploadDto;
import gov.epa.cef.web.service.dto.postOrder.ControlPostOrderDto;
import gov.epa.cef.web.util.CsvBuilder;
import gov.epa.cef.web.util.WebUtils;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/control")
public class ControlApi {

    private final ControlService controlService;

    private final SecurityService securityService;

    @Autowired
    ControlApi(ControlService controlService,
               SecurityService securityService) {

        this.controlService = controlService;
        this.securityService = securityService;
    }

    /**
     * Create a control
     * @param dto
     * @return
     */
    @PostMapping
    @Operation(summary = "Create control",
        description = "Create Control",
        tags = "Control")
    public ResponseEntity<ControlDto> createControl(@NotNull @RequestBody ControlDto dto) {

    	this.securityService.facilityEnforcer().enforceFacilitySite(dto.getFacilitySiteId());

    	ControlDto result = controlService.create(dto);

    	return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve a control by id
     * @param controlId
     * @return
     */
    @GetMapping(value = "/{controlId}")
    @Operation(summary = "Retrieve control by id",
        description = "Retrieve control by id",
        tags = "Control")
    public ResponseEntity<ControlPostOrderDto> retrieveControl(@PathVariable Long controlId) {

        this.securityService.facilityEnforcer().enforceEntity(controlId, ControlRepository.class);

        ControlPostOrderDto result = controlService.retrieveById(controlId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve versions of this control from the last year reported
     * @param controlId
     * @return
     */
    @GetMapping(value = "/{controlId}/previous")
    @Operation(summary = "Retrieve previous control by id",
        description = "Retrieve previous control by id",
        tags = "Control")
    public ResponseEntity<ControlPostOrderDto> retrievePreviousControl(@NotNull @PathVariable Long controlId) {

        this.securityService.facilityEnforcer().enforceEntity(controlId, ControlRepository.class);

        ControlPostOrderDto result = controlService.retrievePreviousById(controlId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Controls for a facility site
     * @param facilitySiteId
     * @return
     */
    @GetMapping(value = "/facilitySite/{facilitySiteId}")
    @Operation(summary = "Retrieve controls for facility",
        description = "Retrieve controls for facility",
        tags = "Control")
    public ResponseEntity<List<ControlPostOrderDto>> retrieveControlsForFacilitySite(
        @NotNull @PathVariable Long facilitySiteId) {

        this.securityService.facilityEnforcer().enforceFacilitySite(facilitySiteId);

        List<ControlPostOrderDto> result = controlService.retrieveForFacilitySite(facilitySiteId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/components/{controlId}")
    @Operation(summary = "Retrieve control components",
        description = "Retrieve control components",
        tags = "Control")
    public ResponseEntity<List<EmissionsReportItemDto>> retrieveControlComponents(
        @NotNull @PathVariable Long controlId) {

        this.securityService.facilityEnforcer().enforceEntity(controlId, ControlRepository.class);

    	List<EmissionsReportItemDto> result = controlService.retrieveControlComponents(controlId);
    	return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update a control by id
     * @param controlId
     * @param dto
     * @return
     */
    @PutMapping(value = "/{controlId}")
    @Operation(summary = "Update control",
        description = "Update control",
        tags = "Control")
    public ResponseEntity<ControlDto> updateControl(
    		@NotNull @PathVariable Long controlId, @NotNull @RequestBody ControlDto dto) {

    		this.securityService.facilityEnforcer().enforceEntity(controlId, ControlRepository.class);

    		ControlDto result = controlService.update(dto.withId(controlId));

    		return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete a Control for given id
     * @param controlId
     * @return
     */
    @DeleteMapping(value = "/{controlId}")
    @Operation(summary = "Delete control",
        description = "Delete control",
        tags = "Control")
    public void deleteControl(@NotNull @PathVariable Long controlId) {

    	this.securityService.facilityEnforcer().enforceEntity(controlId, ControlRepository.class);

    	controlService.delete(controlId);
    }

    /**
     * Create a Control Pollutant
     * @param dto
     * @return
     */
    @PostMapping(value = "/pollutant/")
    @Operation(summary = "Create control pollutant",
        description = "Create control pollutant",
        tags = "Control")
    public ResponseEntity<ControlPollutantDto> createControlPollutant(@NotNull @RequestBody ControlPollutantDto dto) {

    	this.securityService.facilityEnforcer().enforceFacilitySite(dto.getFacilitySiteId());

    	ControlPollutantDto result = controlService.createPollutant(dto);

    	return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update a Control Pollutant by id
     * @param controlPollutantId
     * @param dto
     * @return
     */
    @PutMapping(value = "/pollutant/{controlPollutantId}")
    @Operation(summary = "Update control pollutant",
        description = "Update control pollutant",
        tags = "Control")
    public ResponseEntity<ControlPollutantDto> updateControlPollutant(
    		@NotNull @PathVariable Long controlPollutantId, @NotNull @RequestBody ControlPollutantDto dto) {

        	this.securityService.facilityEnforcer().enforceEntity(controlPollutantId, ControlPollutantRepository.class);

    		ControlPollutantDto result = controlService.updateControlPollutant(dto.withId(controlPollutantId));

    		return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete a Control Pollutant for given id
     * @param controlPollutantId
     * @return
     */
    @DeleteMapping(value = "/pollutant/{controlPollutantId}")
    @Operation(summary = "Delete control pollutant",
        description = "Delete control pollutant",
        tags = "Control")
    public void deleteControlPollutant(@NotNull @PathVariable Long controlPollutantId) {

    	this.securityService.facilityEnforcer().enforceEntity(controlPollutantId, ControlPollutantRepository.class);

    	controlService.deleteControlPollutant(controlPollutantId);
    }


    /***
     * Retrieve a CSV of all of the controls based on the given program system code and inventory year
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/list/csv/{programSystemCode}/{year}")
    @Operation(summary = "Get SLT controls",
        description = "Get SLT controls",
        tags = "Control")
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN, AppRole.ROLE_ADMIN})
    public void getSltControls(@PathVariable String programSystemCode, @PathVariable Short year, HttpServletResponse response) {

    	List<ControlBulkUploadDto> csvRows = controlService.retrieveControls(programSystemCode, year);
    	CsvBuilder<ControlBulkUploadDto> csvBuilder = new CsvBuilder<ControlBulkUploadDto>(ControlBulkUploadDto.class, csvRows);

    	WebUtils.WriteCsv(response, csvBuilder);
    }

    /***
     * Retrieve a CSV of all of the control assignments based on the given program system code and inventory year
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/assignments/list/csv/{programSystemCode}/{year}")
    @Operation(summary = "Get SLT control assignments",
        description = "Create SLT control assignments",
        tags = "Control")
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN, AppRole.ROLE_ADMIN})
    public void getSltControlAssignments(@PathVariable String programSystemCode, @PathVariable Short year, HttpServletResponse response) {

    	List<ControlAssignmentBulkUploadDto> csvRows = controlService.retrieveControlAssignments(programSystemCode, year);
    	CsvBuilder<ControlAssignmentBulkUploadDto> csvBuilder = new CsvBuilder<ControlAssignmentBulkUploadDto>(ControlAssignmentBulkUploadDto.class, csvRows);

    	WebUtils.WriteCsv(response, csvBuilder);
    }

    /***
     * Retrieve a CSV of all of the control pollutants based on the given program system code and inventory year
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/pollutants/list/csv/{programSystemCode}/{year}")
    @Operation(summary = "Get SLT control pollutants",
        description = "Get SLT control pollutants",
        tags = "Control")
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN, AppRole.ROLE_ADMIN})
    public void getSltControlPollutants(@PathVariable String programSystemCode, @PathVariable Short year, HttpServletResponse response) {

    	List<ControlPollutantBulkUploadDto> csvRows = controlService.retrieveControlPollutants(programSystemCode, year);
    	CsvBuilder<ControlPollutantBulkUploadDto> csvBuilder = new CsvBuilder<ControlPollutantBulkUploadDto>(ControlPollutantBulkUploadDto.class, csvRows);

    	WebUtils.WriteCsv(response, csvBuilder);
    }

    /**
     * Retrieve Control Assignments for a Control
     * @param controlId
     * @return
     */
    @GetMapping(value = "/controlAssignment/{controlId}")
    @Operation(summary = "Get control path assignments for control path",
        description = "Get control path assignments for control path",
        tags = "Control")
    public ResponseEntity<List<ControlAssignmentDto>> retrieveControlPathAssignmentsForControlPath(
        @NotNull @PathVariable Long controlId) {

        this.securityService.facilityEnforcer().enforceEntity(controlId, ControlRepository.class);

        List<ControlAssignmentDto> result = controlService.retrieveControlAssignmentsForControl(controlId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
