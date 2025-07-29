/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.api.rest;

import gov.epa.cef.web.repository.ControlPathRepository;
import gov.epa.cef.web.repository.ReleasePointApptRepository;
import gov.epa.cef.web.repository.ReleasePointRepository;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.ReleasePointService;
import gov.epa.cef.web.service.dto.ReleasePointApptDto;
import gov.epa.cef.web.service.dto.ReleasePointDto;
import gov.epa.cef.web.service.dto.bulkUpload.ReleasePointApptBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ReleasePointBulkUploadDto;
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
@RequestMapping("/api/releasePoint")
public class ReleasePointApi {

    private final ReleasePointService releasePointService;

    private final SecurityService securityService;



    @Autowired
    ReleasePointApi(SecurityService securityService,
                    ReleasePointService releasePointService) {

        this.releasePointService = releasePointService;
        this.securityService = securityService;
    }

    /**
     * Create a release point
     * @param dto
     * @return
     */
    @PostMapping
    @Operation(summary = "Create release point",
        description = "Create release point",
        tags = "Release Point")
    public ResponseEntity<ReleasePointDto> createReleasePoint(
    		@NotNull @RequestBody ReleasePointDto dto) {

    	this.securityService.facilityEnforcer().enforceFacilitySite(dto.getFacilitySiteId());

    	ReleasePointDto result = releasePointService.create(dto);

    	return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve a release point by ID
     * @param pointId
     * @return
     */
    @GetMapping(value = "/{pointId}")
    @Operation(summary = "Get release point",
        description = "Get release point",
        tags = "Release Point")
    public ResponseEntity<ReleasePointDto> retrieveReleasePoint(@NotNull @PathVariable Long pointId) {

        this.securityService.facilityEnforcer().enforceEntity(pointId, ReleasePointRepository.class);

        ReleasePointDto result = releasePointService.retrieveById(pointId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve versions of this rp from the last year reported
     * @param pointId
     * @return
     */
    @GetMapping(value = "/{pointId}/previous")
    @Operation(summary = "Get previous release point",
        description = "Get previous release point",
        tags = "Release Point")
    public ResponseEntity<ReleasePointDto> retrievePreviousReleasePoint(@NotNull @PathVariable Long pointId) {

        this.securityService.facilityEnforcer().enforceEntity(pointId, ReleasePointRepository.class);

        ReleasePointDto result = releasePointService.retrievePreviousById(pointId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Release Points for a facility
     * @param facilitySiteId
     * @return
     */
    @GetMapping(value = "/facility/{facilitySiteId}")
    @Operation(summary = "Get facility release point",
        description = "Get facility release point",
        tags = "Release Point")
    public ResponseEntity<List<ReleasePointDto>> retrieveFacilityReleasePoints(
        @NotNull @PathVariable Long facilitySiteId) {

        this.securityService.facilityEnforcer().enforceFacilitySite(facilitySiteId);

        List<ReleasePointDto> result = releasePointService.retrieveByFacilitySiteId(facilitySiteId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update an Release Point by ID
     * @param pointId
     * @param dto
     * @return
     */
    @PutMapping(value = "/{pointId}")
    @Operation(summary = "Update release point",
        description = "Update release point",
        tags = "Release Point")
    public ResponseEntity<ReleasePointDto> updateReleasePoint(
        @NotNull @PathVariable Long pointId, @NotNull @RequestBody ReleasePointDto dto) {

        this.securityService.facilityEnforcer().enforceEntity(pointId, ReleasePointRepository.class);

        ReleasePointDto result = releasePointService.update(dto.withId(pointId));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete a Release Point for a given ID
     * @param pointId
     * @return
     */
    @DeleteMapping(value = "/{pointId}")
    @Operation(summary = "Delete release point",
        description = "Delete release point",
        tags = "Release Point")
    public void deleteReleasePoint(@PathVariable Long pointId) {

        this.securityService.facilityEnforcer().enforceEntity(pointId, ReleasePointRepository.class);

        releasePointService.delete(pointId);
    }

    /**
     * Delete a Release Point Apportionment for a given ID
     * @param releasePointApptId
     * @return
     */
    @DeleteMapping(value = "/appt/{releasePointApptId}")
    @Operation(summary = "Delete release point",
        description = "Delete release point",
        tags = "Release Point")
    public void deleteReleasePointAppt(@PathVariable Long releasePointApptId) {

        this.securityService.facilityEnforcer().enforceEntity(releasePointApptId, ReleasePointApptRepository.class);

        releasePointService.deleteAppt(releasePointApptId);
    }

    /**
     * Create a Release Point Apportionment
     * @param emissionsProcessId
     * @return
     */
    @PostMapping(value = "/appt/")
    @Operation(summary = "Create release point appt",
        description = "Create release point appt",
        tags = "Release Point")
    public ResponseEntity<ReleasePointApptDto> createReleasePointAppt(
    		@NotNull @RequestBody ReleasePointApptDto dto) {

    	this.securityService.facilityEnforcer().enforceFacilitySite(dto.getFacilitySiteId());

    	ReleasePointApptDto result = releasePointService.createAppt(dto);

    	return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update an Release Point Apportionment by ID
     * @param apportionmentId
     * @param dto
     * @return
     */
    @PutMapping(value = "/appt/{apportionmentId}")
    @Operation(summary = "Update release point appt",
        description = "Update release point appt",
        tags = "Release Point")
    public ResponseEntity<ReleasePointApptDto> updateReleasePointAppt(
        @NotNull @PathVariable Long apportionmentId, @NotNull @RequestBody ReleasePointApptDto dto) {

        this.securityService.facilityEnforcer().enforceEntity(apportionmentId, ReleasePointApptRepository.class);

        ReleasePointApptDto result = releasePointService.updateAppt(dto.withId(apportionmentId));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve all Release Points associated to a control path id
     * @param controlPathId
     * @return
     */
    @GetMapping(value = "/appt/{controlPathId}")
    @Operation(summary = "Get release point appt",
        description = "Get release point appt",
        tags = "Release Point")
    public ResponseEntity<List<ReleasePointDto>> retrieveReleasePointAppt(
        @NotNull @PathVariable Long controlPathId) {

        this.securityService.facilityEnforcer().enforceEntity(controlPathId, ControlPathRepository.class);

        List<ReleasePointDto> result = releasePointService.retrieveByControlPathId(controlPathId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /***
     * Retrieve a CSV of all of the release points based on the given program system code and inventory year
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/list/csv/{programSystemCode}/{year}")
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN, AppRole.ROLE_ADMIN})
    @Operation(summary = "Get SLT release points",
        description = "Get SLT release points",
        tags = "Release Point")
    public void getSltReleasePoints(@PathVariable String programSystemCode, @PathVariable Short year, HttpServletResponse response) {

    	List<ReleasePointBulkUploadDto> csvRows = releasePointService.retrieveReleasePoints(programSystemCode, year);
    	CsvBuilder<ReleasePointBulkUploadDto> csvBuilder = new CsvBuilder<ReleasePointBulkUploadDto>(ReleasePointBulkUploadDto.class, csvRows);

    	WebUtils.WriteCsv(response, csvBuilder);
    }

    /***
     * Retrieve a CSV of all of the release point apportionments based on the given program system code and inventory year
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/appt/list/csv/{programSystemCode}/{year}")
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN, AppRole.ROLE_ADMIN})
    @Operation(summary = "Get SLT release point apportionments",
        description = "Get SLT release point apportionments",
        tags = "Release Point")
    public void getSltReleasePointApportionments(@PathVariable String programSystemCode, @PathVariable Short year, HttpServletResponse response) {

    	List<ReleasePointApptBulkUploadDto> csvRows = releasePointService.retrieveReleasePointAppts(programSystemCode, year);
    	CsvBuilder<ReleasePointApptBulkUploadDto> csvBuilder = new CsvBuilder<ReleasePointApptBulkUploadDto>(ReleasePointApptBulkUploadDto.class, csvRows);

    	WebUtils.WriteCsv(response, csvBuilder);
    }
}
