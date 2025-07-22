/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.api.rest;

import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.FacilityNAICSXrefRepository;

import gov.epa.cef.web.repository.FacilitySiteRepository;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.FacilitySiteService;
import gov.epa.cef.web.service.dto.FacilityNAICSDto;
import gov.epa.cef.web.service.dto.FacilityPermitDto;
import gov.epa.cef.web.service.dto.FacilitySiteDto;
import gov.epa.cef.web.service.dto.bulkUpload.FacilityNAICSBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.FacilitySiteBulkUploadDto;
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

import java.util.Collection;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

/**
 * API for retrieving facility site information related to reports.
 * @author tfesperm
 *
 */
@RestController
@RequestMapping("/api/facilitySite")
public class FacilitySiteApi {

    private final FacilitySiteService facilityService;

    private final SecurityService securityService;

    @Autowired
    FacilitySiteApi(SecurityService securityService,
                    FacilitySiteService facilityService) {

        this.securityService = securityService;
        this.facilityService = facilityService;
    }

    /**
     * Update an existing facility site by ID
     * @param facilitySiteId
     * @param dto
     * @return
     */
    @PutMapping(value = "/{facilitySiteId}")
    @Operation(summary = "Update facility site",
        description = "Update facility site",
        tags = "Facility Site")
    public ResponseEntity<FacilitySiteDto> updateFacilitySite(
    		@NotNull @PathVariable Long facilitySiteId, @NotNull @RequestBody FacilitySiteDto dto) {

    	this.securityService.facilityEnforcer().enforceEntity(facilitySiteId, FacilitySiteRepository.class);

    	FacilitySiteDto result = facilityService.update(dto.withId(facilitySiteId));

    	return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve a facility site by ID and reportYear used for NAICS code descriptions
     * @param facilitySiteId
     * @param reportYear
     * @return
     */
    @GetMapping(value = "/{facilitySiteId}/reportYear/{reportYear}")
    @Operation(summary = "Get facility site by id and report year",
        description = "Get facility site by id and report year",
        tags = "Facility Site")
    public ResponseEntity<FacilitySiteDto> retrieveFacilitySiteByIdAndReportYear(
            @NotNull @PathVariable Long facilitySiteId,
            @NotNull @PathVariable Short reportYear) {

        this.securityService.facilityEnforcer().enforceFacilitySite(facilitySiteId);

        FacilitySiteDto facilitySiteDto = facilityService.findByIdAndReportYear(facilitySiteId, reportYear);
        return new ResponseEntity<>(facilitySiteDto, HttpStatus.OK);
    }

    /**
     * Retrieve a facility site by report ID
     * @param reportId
     * @return
     */
    @GetMapping(value = "/report/{reportId}")
    @Operation(summary = "Get facility site by report id",
        description = "Get facility site by report id",
        tags = "Facility Site")
    public ResponseEntity<FacilitySiteDto> retrieveFacilitySiteByReportId(
        @NotNull @PathVariable Long reportId) {

        this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        FacilitySiteDto result = facilityService.findByReportId(reportId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Create a Facility NAICS
     * @param dto
     * @return
     */
    @PostMapping(value = "/naics/")
    @Operation(summary = "Create facility site NAICS",
        description = "Create facility site NAICS",
        tags = "Facility Site")
    public ResponseEntity<FacilityNAICSDto> createFacilityNAICS(
    		@NotNull @RequestBody FacilityNAICSDto dto) {

    	this.securityService.facilityEnforcer().enforceFacilitySite(dto.getFacilitySiteId());

    	FacilityNAICSDto result = facilityService.createNaics(dto);

    	return new ResponseEntity<>(result, HttpStatus.OK);
    }

   /**
    * Update a Facility NAICS
    * @param facilityNaicsId
    * @param dto
    * @return
    */
   @PutMapping(value = "/naics/{facilityNaicsId}")
   @Operation(summary = "Update facility site NAICS",
       description = "Update facility site NAICS",
       tags = "Facility Site")
   public ResponseEntity<FacilityNAICSDto> updateFacilityNAICS(
   		@NotNull @PathVariable Long facilityNaicsId, @NotNull @RequestBody FacilityNAICSDto dto) {

	   	this.securityService.facilityEnforcer().enforceEntity(facilityNaicsId, FacilityNAICSXrefRepository.class);

	   	FacilityNAICSDto result = facilityService.updateNaics(dto.withId(facilityNaicsId));

	   	return new ResponseEntity<>(result, HttpStatus.OK);
   }

    /**
     * Delete a Facility NAICS for a given ID
     * @param facilityNaicsId
     * @return
     */
    @DeleteMapping(value = "/naics/{facilityNaicsId}")
    @Operation(summary = "Delete facility site NAICS",
        description = "Delete facility site NAICS",
        tags = "Facility Site")
    public void deleteFacilityNAICS(@PathVariable Long facilityNaicsId) {

    	this.securityService.facilityEnforcer().enforceEntity(facilityNaicsId, FacilityNAICSXrefRepository.class);

    	facilityService.deleteFacilityNaics(facilityNaicsId);
    }

    @GetMapping(value = "/{facilitySiteId}/permit")
    @Operation(summary = "Get facility site permits",
        description = "Get facility site permits",
        tags = "Facility Site")
    public ResponseEntity<Collection<FacilityPermitDto>> retrieveFacilityPermits(
            @NotNull @PathVariable Long facilitySiteId) {

        this.securityService.facilityEnforcer().enforceEntity(facilitySiteId, FacilitySiteRepository.class);

        Collection<FacilityPermitDto> result = facilityService.retrieveFacilityPermits(facilitySiteId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /***
     * Retrieve a CSV of all of the facilities based on the given program system code and inventory year
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/list/csv/{programSystemCode}/{year}")
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN, AppRole.ROLE_ADMIN})
    @Operation(summary = "Get SLT facility site",
        description = "Get SLT facility site",
        tags = "Facility Site")
    public void getSltFacilities(@PathVariable String programSystemCode, @PathVariable Short year, HttpServletResponse response) {

    	List<FacilitySiteBulkUploadDto> csvRows = facilityService.retrieveFacilities(programSystemCode, year);
    	CsvBuilder<FacilitySiteBulkUploadDto> csvBuilder = new CsvBuilder<FacilitySiteBulkUploadDto>(FacilitySiteBulkUploadDto.class, csvRows);

    	WebUtils.WriteCsv(response, csvBuilder);
    }

    /***
     * Retrieve a CSV of all of the facility's NAICS codes based on the given program system code and inventory year
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/naics/list/csv/{programSystemCode}/{year}")
    @Operation(summary = "Get SLT facility site NAICS codes",
        description = "Get SLT facility site NAICS codes",
        tags = "Facility Site")
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN, AppRole.ROLE_ADMIN})
    public void getSltFacilityNaicsCodes(@PathVariable String programSystemCode, @PathVariable Short year, HttpServletResponse response) {

    	List<FacilityNAICSBulkUploadDto> csvRows = facilityService.retrieveFacilityNaics(programSystemCode, year);
    	CsvBuilder<FacilityNAICSBulkUploadDto> csvBuilder = new CsvBuilder<FacilityNAICSBulkUploadDto>(FacilityNAICSBulkUploadDto.class, csvRows);

    	WebUtils.WriteCsv(response, csvBuilder);
    }
}
