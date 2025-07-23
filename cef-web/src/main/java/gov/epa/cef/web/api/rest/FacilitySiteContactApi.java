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

import gov.epa.cef.web.repository.FacilitySiteContactRepository;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.FacilitySiteContactService;
import gov.epa.cef.web.service.dto.FacilitySiteContactDto;
import gov.epa.cef.web.service.dto.bulkUpload.FacilitySiteContactBulkUploadDto;
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
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/facilitySiteContact")
public class FacilitySiteContactApi {

    private final FacilitySiteContactService facilitySiteContactService;

    private final SecurityService securityService;

    @Autowired
    FacilitySiteContactApi(SecurityService securityService,
                           FacilitySiteContactService facilitySiteContactService) {

        this.facilitySiteContactService = facilitySiteContactService;
        this.securityService = securityService;
    }

    /**
     * Create a facility site contact
     * @param dto
     * @return
     */
    @PostMapping
    @Operation(summary = "Create facility site contact",
        description = "Create facility site contact",
        tags = "Facility Site Contact")
    public ResponseEntity<FacilitySiteContactDto> createContact(
    		@NotNull @RequestBody FacilitySiteContactDto dto) {

    	this.securityService.facilityEnforcer()
    		.enforceFacilitySite(dto.getFacilitySiteId());

    	FacilitySiteContactDto result = facilitySiteContactService.create(dto);

    	return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve a facility site contact by ID
     * @param contactId
     * @return
     */
    @GetMapping(value = "/{contactId}")
    @Operation(summary = "Get facility site contact",
        description = "Get facility site contact",
        tags = "Facility Site Contact")
    public ResponseEntity<FacilitySiteContactDto> retrieveContact(@NotNull @PathVariable Long contactId) {

        this.securityService.facilityEnforcer().enforceEntity(contactId, FacilitySiteContactRepository.class);

        FacilitySiteContactDto result = facilitySiteContactService.retrieveById(contactId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Facility Site Contacts for a facility site
     * @param facilitySiteId
     * @return
     */
    @GetMapping(value = "/facility/{facilitySiteId}")
    @Operation(summary = "Get facility site contact for facility",
        description = "Get facility site contact for facility",
        tags = "Facility Site Contact")
    public ResponseEntity<Collection<FacilitySiteContactDto>> retrieveContactsForFacility(
        @NotNull @PathVariable Long facilitySiteId) {

        this.securityService.facilityEnforcer().enforceFacilitySite(facilitySiteId);

        Collection<FacilitySiteContactDto> result =
            facilitySiteContactService.retrieveForFacilitySite(facilitySiteId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update an existing facility site contact by ID
     * @param contactId
     * @param dto
     * @return
     */
    @PutMapping(value = "/{contactId}")
    @Operation(summary = "Update facility site contact",
        description = "Update facility site contact",
        tags = "Facility Site Contact")
    public ResponseEntity<FacilitySiteContactDto> updateContact(
        @NotNull @PathVariable Long contactId, @NotNull @RequestBody FacilitySiteContactDto dto) {

        this.securityService.facilityEnforcer().enforceEntity(contactId, FacilitySiteContactRepository.class);

        FacilitySiteContactDto result = facilitySiteContactService.update(dto.withId(contactId));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete a facility site contact by ID
     * @param contactId
     * @return
     */
    @DeleteMapping(value = "/{contactId}")
    @Operation(summary = "Delete facility site contact",
        description = "Delete facility site contact",
        tags = "Facility Site Contact")
    public void deleteContact(@PathVariable Long contactId) {

        this.securityService.facilityEnforcer().enforceEntity(contactId, FacilitySiteContactRepository.class);

        facilitySiteContactService.delete(contactId);
    }


    /***
     * Retrieve a CSV of all of the facility contacts based on the given program system code and inventory year
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/list/csv/{programSystemCode}/{year}")
    @Operation(summary = "Get SLT facility site contacts",
        description = "Get SLT facility site contacts",
        tags = "Facility Site Contact")
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN, AppRole.ROLE_ADMIN})
    public void getSltFacilityContacts(@PathVariable String programSystemCode, @PathVariable Short year, HttpServletResponse response) {

    	List<FacilitySiteContactBulkUploadDto> csvRows = facilitySiteContactService.retrieveFacilitySiteContacts(programSystemCode, year);
    	CsvBuilder<FacilitySiteContactBulkUploadDto> csvBuilder = new CsvBuilder<FacilitySiteContactBulkUploadDto>(FacilitySiteContactBulkUploadDto.class, csvRows);

    	WebUtils.WriteCsv(response, csvBuilder);
    }
}
