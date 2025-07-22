/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.api.rest;

import gov.epa.cef.web.repository.ControlAssignmentRepository;
import gov.epa.cef.web.repository.ControlPathPollutantRepository;
import gov.epa.cef.web.repository.ControlPathRepository;
import gov.epa.cef.web.repository.ControlRepository;
import gov.epa.cef.web.repository.EmissionsProcessRepository;
import gov.epa.cef.web.repository.EmissionsUnitRepository;
import gov.epa.cef.web.repository.ReleasePointRepository;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.ControlPathService;
import gov.epa.cef.web.service.dto.ControlAssignmentDto;
import gov.epa.cef.web.service.dto.ControlPathDto;
import gov.epa.cef.web.service.dto.ControlPathPollutantDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlPathBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlPathPollutantBulkUploadDto;
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
@RequestMapping("/api/controlPath")
public class ControlPathApi {

    private final ControlPathService controlPathService;

    private final SecurityService securityService;

    @Autowired
    ControlPathApi(SecurityService securityService,
    			   ControlPathService controlPathService) {

        this.securityService = securityService;
        this.controlPathService = controlPathService;
    }

    /**
     * Create a control path
     * @param dto
     * @return
     */
    @PostMapping
    @Operation(summary = "Create control path",
        description = "Create control path",
        tags = "Control Path")
    public ResponseEntity<ControlPathDto> createControlPath(@NotNull @RequestBody ControlPathDto dto) {

    	this.securityService.facilityEnforcer().enforceFacilitySite(dto.getFacilitySiteId());

    	ControlPathDto result = controlPathService.create(dto);

    	return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Control Paths for a facility site
     * @param facilitySiteId
     * @return
     */
    @GetMapping(value = "/facilitySite/{facilitySiteId}")
    @Operation(summary = "Get control paths for facility site",
        description = "Get control paths for facility site",
        tags = "Control Path")
    public ResponseEntity<List<ControlPathDto>> retrieveControlPathsForFacilitySite(
        @NotNull @PathVariable Long facilitySiteId) {

        this.securityService.facilityEnforcer().enforceFacilitySite(facilitySiteId);

        List<ControlPathDto> result = controlPathService.retrieveForFacilitySite(facilitySiteId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve a control path by id
     * @param controlPathId
     * @return
     */
    @GetMapping(value = "/{controlPathId}")
    @Operation(summary = "Get control path by id",
        description = "Get control path by id",
        tags = "Control Path")
    public ResponseEntity<ControlPathDto> retrieveControlPath(@NotNull @PathVariable Long controlPathId) {

        this.securityService.facilityEnforcer().enforceEntity(controlPathId, ControlPathRepository.class);

    	ControlPathDto result = controlPathService.retrieveById(controlPathId);

        return new ResponseEntity<ControlPathDto>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Control Paths for a process
     * @param processId
     * @return
     */
    @GetMapping(value = "/process/{processId}")
    @Operation(summary = "Get control assignments for emission process",
        description = "Get control assignments for emission process",
        tags = "Control Path")
    public ResponseEntity<List<ControlPathDto>> retrieveControlAssignmentsForEmissionsProcess(
        @NotNull @PathVariable Long processId) {

        this.securityService.facilityEnforcer().enforceEntity(processId, EmissionsProcessRepository.class);

        List<ControlPathDto> result = controlPathService.retrieveForEmissionsProcess(processId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Check if a control path was previously reported
     * @param controlPathId
     * @return
     */
    @GetMapping(value = "/{controlPathId}/reported")
    @Operation(summary = "Get path previously reported",
        description = "Get path previously reported",
        tags = "Control Path")
    public ResponseEntity<Boolean> isPathPreviouslyReported(@NotNull @PathVariable Long controlPathId) {

        this.securityService.facilityEnforcer().enforceEntity(controlPathId, ControlPathRepository.class);

        Boolean result = controlPathService.isPathPreviouslyReported(controlPathId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete a Control Path for given id
     * @param controlPathId
     * @return
     */
    @DeleteMapping(value = "/{controlPathId}")
    @Operation(summary = "Delete control path",
        description = "Delete control path",
        tags = "Control Path")
    public void deleteControlPath(@NotNull @PathVariable Long controlPathId) {

    	this.securityService.facilityEnforcer().enforceEntity(controlPathId, ControlPathRepository.class);

    	controlPathService.delete(controlPathId);
    }

    /**
     * Update a control path by id
     * @param controlPathId
     * @param dto
     * @return
     */
    @PutMapping(value = "/{controlPathId}")
    @Operation(summary = "Update control path",
        description = "Update control path",
        tags = "Control Path")
    public ResponseEntity<ControlPathDto> updateControlPath(
    		@NotNull @PathVariable Long controlPathId, @NotNull @RequestBody ControlPathDto dto) {

    		this.securityService.facilityEnforcer().enforceEntity(controlPathId, ControlPathRepository.class);

    		ControlPathDto result = controlPathService.update(dto.withId(controlPathId));

    		return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Control Paths for an emissions unit
     * @param unitId
     * @return
     */
    @GetMapping(value = "/unit/{unitId}")
    @Operation(summary = "Get control assignments for emission unit",
        description = "Get control assignments for emission unit",
        tags = "Control Path")
    public ResponseEntity<List<ControlPathDto>> retrieveControlAssignmentsForEmissionsUnit(
        @NotNull @PathVariable Long unitId) {

        this.securityService.facilityEnforcer().enforceEntity(unitId, EmissionsUnitRepository.class);

        List<ControlPathDto> result = controlPathService.retrieveForEmissionsUnit(unitId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Control Paths for a control device
     * @param pointId
     * @return
     */
    @GetMapping(value = "/controlDevice/{deviceId}")
    @Operation(summary = "Get control paths for control device",
        description = "Get control paths for control device",
        tags = "Control Path")
    public ResponseEntity<List<ControlPathDto>> retrieveControlPathsForControlDevice(
        @NotNull @PathVariable Long deviceId) {

        this.securityService.facilityEnforcer().enforceEntity(deviceId, ControlRepository.class);;

        List<ControlPathDto> result = controlPathService.retrieveForControlDevice(deviceId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Control Paths for an release point
     * @param pointId
     * @return
     */
    @GetMapping(value = "/releasePoint/{pointId}")
    @Operation(summary = "Get control assignment for release point",
        description = "Get control assignment for release point",
        tags = "Control Path")
    public ResponseEntity<List<ControlPathDto>> retrieveControlAssignmentsForReleasePoint(
        @NotNull @PathVariable Long pointId) {

        this.securityService.facilityEnforcer().enforceEntity(pointId, ReleasePointRepository.class);;

        List<ControlPathDto> result = controlPathService.retrieveForReleasePoint(pointId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Create a Control Path Assignment
     * @param controlPathAssignment
     * @return
     */
    @PostMapping(value = "/controlAssignment/")
    @Operation(summary = "Create control path assignment",
        description = "Create control path assignment",
        tags = "Control Path")
    public ResponseEntity<ControlAssignmentDto> createControlPathAssignment(
    		@NotNull @RequestBody ControlAssignmentDto dto) {

    	this.securityService.facilityEnforcer().enforceFacilitySite(dto.getFacilitySiteId());

    	ControlAssignmentDto result = controlPathService.createAssignment(dto);

    	return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Control Path Assignments for a Control Path
     * @param controlPathId
     * @return
     */
    @GetMapping(value = "/controlAssignment/{controlPathId}")
    @Operation(summary = "Get control path assignment for control path",
        description = "Create control path assignment for control path",
        tags = "Control Path")
    public ResponseEntity<List<ControlAssignmentDto>> retrieveControlPathAssignmentsForControlPath(
        @NotNull @PathVariable Long controlPathId) {

        this.securityService.facilityEnforcer().enforceEntity(controlPathId, ControlPathRepository.class);

        List<ControlAssignmentDto> result = controlPathService.retrieveForControlPath(controlPathId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve parent control path for child control path
     * @param controlPathId
     * @return
     */
    @GetMapping(value = "/paretControlAssignment/{controlPathId}")
    @Operation(summary = "Get parent assignment for control path child",
        description = "Get parent assignment for control path child",
        tags = "Control Path")
    public ResponseEntity<List<ControlAssignmentDto>> retrieveParentAssignmentsForControlPathChild(@NotNull @PathVariable Long controlPathId) {

        this.securityService.facilityEnforcer().enforceEntity(controlPathId, ControlPathRepository.class);

        List<ControlAssignmentDto> result = controlPathService.retrieveParentPathById(controlPathId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete a Control Path Assignment for given id
     * @param controlPathId
     * @return
     */
    @DeleteMapping(value = "/controlAssignment/{controlPathAssignmentId}")
    @Operation(summary = "Delete control path assignment",
        description = "Delete control path assignment",
        tags = "Control Path")
    public void deleteControlPathAssignment(@NotNull @PathVariable Long controlPathAssignmentId) {

    	this.securityService.facilityEnforcer().enforceEntity(controlPathAssignmentId, ControlAssignmentRepository.class);

    	controlPathService.deleteAssignment(controlPathAssignmentId);
    }

    /**
     * Update a control path assignment by id
     * @param controlPathAssignmentId
     * @param dto
     * @return
     */
    @PutMapping(value = "/controlAssignment/{controlPathAssignmentId}")
    @Operation(summary = "Update control path assignment",
        description = "Update control path assignment",
        tags = "Control Path")
    public ResponseEntity<ControlAssignmentDto> updateControlPathAssignment(
    		@NotNull @PathVariable Long controlPathAssignmentId, @NotNull @RequestBody ControlAssignmentDto dto) {

    		this.securityService.facilityEnforcer().enforceEntity(controlPathAssignmentId, ControlAssignmentRepository.class);

    		ControlAssignmentDto result = controlPathService.updateAssignment(dto.withId(controlPathAssignmentId));

    		return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Create a Control Path Pollutant
     * @param dto
     * @return
     */
    @PostMapping(value = "/pollutant/")
    @Operation(summary = "Create control path pollutant",
        description = "Create control path pollutant",
        tags = "Control Path")
    public ResponseEntity<ControlPathPollutantDto> createControlPathPollutant(@NotNull @RequestBody ControlPathPollutantDto dto) {

    	this.securityService.facilityEnforcer().enforceFacilitySite(dto.getFacilitySiteId());

    	ControlPathPollutantDto result = controlPathService.createPollutant(dto);

    	return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update a Control Path Pollutant by id
     * @param controlPathPollutantId
     * @param dto
     * @return
     */
    @PutMapping(value = "/pollutant/{controlPathPollutantId}")
    @Operation(summary = "Update control path pollutant",
        description = "Update control path pollutant",
        tags = "Control Path")
    public ResponseEntity<ControlPathPollutantDto> updateControlPathPollutant(
    		@NotNull @PathVariable Long controlPathPollutantId, @NotNull @RequestBody ControlPathPollutantDto dto) {

        	this.securityService.facilityEnforcer().enforceEntity(controlPathPollutantId, ControlPathPollutantRepository.class);

    		ControlPathPollutantDto result = controlPathService.updateControlPathPollutant(dto.withId(controlPathPollutantId));

    		return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete a Control Path Pollutant for given id
     * @param controlPathPollutantId
     * @return
     */
    @DeleteMapping(value = "/pollutant/{controlPathPollutantId}")
    @Operation(summary = "Delete control path polutant",
        description = "Delete control path pollutant",
        tags = "Control Path")
    public void deleteControlPathPollutant(@NotNull @PathVariable Long controlPathPollutantId) {

    	this.securityService.facilityEnforcer().enforceEntity(controlPathPollutantId, ControlPathPollutantRepository.class);

    	controlPathService.deleteControlPathPollutant(controlPathPollutantId);
    }

    /***
     *
     * Retrieve a CSV of all of the control paths based on the given program system code and inventory year
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/list/csv/{programSystemCode}/{year}")
    @Operation(summary = "Get SLT control paths",
        description = "Get SLT control paths",
        tags = "Control Path")
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN, AppRole.ROLE_ADMIN})
    public void getSltControlPaths(@PathVariable String programSystemCode, @PathVariable Short year, HttpServletResponse response) {

    	List<ControlPathBulkUploadDto> csvRows = controlPathService.retrieveControlPaths(programSystemCode, year);
    	CsvBuilder<ControlPathBulkUploadDto> csvBuilder = new CsvBuilder<ControlPathBulkUploadDto>(ControlPathBulkUploadDto.class, csvRows);

    	WebUtils.WriteCsv(response, csvBuilder);
    }

    /***
     * Retrieve a CSV of all of the control path pollutants based on the given program system code and inventory year
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/pollutants/list/csv/{programSystemCode}/{year}")
    @Operation(summary = "Get SLT control path pollutants",
        description = "Get SLT control path pollutants",
        tags = "Control Path")
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN, AppRole.ROLE_ADMIN})
    public void getSltControlPathPollutants(@PathVariable String programSystemCode, @PathVariable Short year, HttpServletResponse response) {

    	List<ControlPathPollutantBulkUploadDto> csvRows = controlPathService.retrieveControlPathPollutants(programSystemCode, year);
    	CsvBuilder<ControlPathPollutantBulkUploadDto> csvBuilder = new CsvBuilder<ControlPathPollutantBulkUploadDto>(ControlPathPollutantBulkUploadDto.class, csvRows);

    	WebUtils.WriteCsv(response, csvBuilder);
    }
}
