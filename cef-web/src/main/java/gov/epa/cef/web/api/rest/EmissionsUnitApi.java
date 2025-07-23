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

import gov.epa.cef.web.repository.EmissionsUnitRepository;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.EmissionsUnitService;
import gov.epa.cef.web.service.dto.EmissionsUnitDto;
import gov.epa.cef.web.service.dto.SideNavItemDto;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionsUnitBulkUploadDto;
import gov.epa.cef.web.util.CsvBuilder;
import gov.epa.cef.web.util.WebUtils;

import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/emissionsUnit")
public class EmissionsUnitApi {

    private final EmissionsUnitService emissionsUnitService;

    private final SecurityService securityService;

    public EmissionsUnitApi(SecurityService securityService,
                            EmissionsUnitService emissionsUnitService) {

        this.securityService = securityService;
        this.emissionsUnitService = emissionsUnitService;
    }

    /**
     * Create a new Emissions Unit
     * @param dto
     * @return
     */
    @PostMapping
    @Operation(summary = "Create emission unit",
        description = "Create emission unit",
        tags = "Emission Unit")
    public ResponseEntity<EmissionsUnitDto> createEmissionsUnit(
        @RequestBody EmissionsUnitDto dto) {

        this.securityService.facilityEnforcer()
            .enforceFacilitySite(dto.getFacilitySiteId());

        EmissionsUnitDto result = emissionsUnitService.create(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update an Emissions Unit
     * @param id
     * @param dto
     * @return
     */
    @PutMapping(value = "/{id}")
    @Operation(summary = "Update emission unit",
        description = "Update emission unit",
        tags = "Emission Unit")
    public ResponseEntity<EmissionsUnitDto> updateEmissionsUnit(
        @NotNull @PathVariable Long id, @NotNull @RequestBody EmissionsUnitDto dto) {

        this.securityService.facilityEnforcer().enforceEntity(id, EmissionsUnitRepository.class);

        EmissionsUnitDto result = emissionsUnitService.update(dto.withId(id));

        return new ResponseEntity<EmissionsUnitDto>(result, HttpStatus.OK);
    }

    /**
     * Retrieve a unit by it's ID
     * @param unitId
     * @return
     */
    @GetMapping(value = "/{unitId}")
    @Operation(summary = "Get emission unit",
        description = "Get emission unit",
        tags = "Emission Unit")
    public ResponseEntity<EmissionsUnitDto> retrieveEmissionsUnit(@NotNull @PathVariable Long unitId) {

        this.securityService.facilityEnforcer().enforceEntity(unitId, EmissionsUnitRepository.class);

        EmissionsUnitDto emissionsUnit = emissionsUnitService.retrieveUnitById(unitId);

        return new ResponseEntity<>(emissionsUnit, HttpStatus.OK);
    }

    /**
     * Retrieve versions of this unit from the last year reported
     * @param unitId
     * @return
     */
    @GetMapping(value = "/{unitId}/previous")
    @Operation(summary = "Get previous emission unit",
        description = "Get previous emission unit",
        tags = "Emission Unit")
    public ResponseEntity<EmissionsUnitDto> retrievePreviousEmissionsUnit(@NotNull @PathVariable Long unitId) {

        this.securityService.facilityEnforcer().enforceEntity(unitId, EmissionsUnitRepository.class);

        EmissionsUnitDto emissionsUnit = emissionsUnitService.retrievePreviousById(unitId);

        return new ResponseEntity<>(emissionsUnit, HttpStatus.OK);
    }

    /**
     * Retrieve emissions unit of a facility
     * @param facilitySiteId
     * @return list of emissions unit
     */
    @GetMapping(value = "/facility/{facilitySiteId}")
    @Operation(summary = "Get emission unit of facility",
        description = "Get emission unit of facility",
        tags = "Emission Unit")
    public ResponseEntity<List<EmissionsUnitDto>> retrieveEmissionsUnitsOfFacility(
        @NotNull @PathVariable Long facilitySiteId) {

        this.securityService.facilityEnforcer().enforceFacilitySite(facilitySiteId);

        List<EmissionsUnitDto> emissionsUnits = emissionsUnitService.retrieveEmissionUnitsForFacility(facilitySiteId);

        return new ResponseEntity<>(emissionsUnits, HttpStatus.OK);
    }

    /**
     * Retrieve emissions unit and processes for nav tree for a facility
     * @param facilitySiteId
     * @return list of emissions unit
     */
    @GetMapping(value = "/nav/facility/{facilitySiteId}")
    @Operation(summary = "Get emission unit nav for facility",
        description = "Get emission unit nav for facility",
        tags = "Emission Unit")
    public ResponseEntity<List<SideNavItemDto>> retrieveEmissionsUnitNavForFacility(
        @NotNull @PathVariable Long facilitySiteId) {

		this.securityService.facilityEnforcer().enforceFacilitySite(facilitySiteId);

		List<SideNavItemDto> emissionsUnits = emissionsUnitService.retrieveEmissionUnitNavForFacility(facilitySiteId);
		return new ResponseEntity<>(emissionsUnits, HttpStatus.OK);
	}

    /**
     * Delete an emission unit for a given ID
     * @param unitId
     * @return
     */
    @DeleteMapping(value = "/{unitId}")
    @Operation(summary = "Delete emission unit",
        description = "Delete emission unit",
        tags = "Emission Unit")
    public void deleteEmissionsUnit(@PathVariable Long unitId) {

        this.securityService.facilityEnforcer().enforceEntity(unitId, EmissionsUnitRepository.class);

        emissionsUnitService.delete(unitId);
    }

    /***
     * Retrieve a CSV of all of the emission units based on the given program system code and inventory year
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/list/csv/{programSystemCode}/{year}")
    @Operation(summary = "Get SLT emission unit",
        description = "Get SLT emission unit",
        tags = "Emission Unit")
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN, AppRole.ROLE_ADMIN})
    public void getSltEmissionsUnits(@PathVariable String programSystemCode, @PathVariable Short year, HttpServletResponse response) {

    	List<EmissionsUnitBulkUploadDto> csvRows = emissionsUnitService.retrieveEmissionsUnits(programSystemCode, year);
    	CsvBuilder<EmissionsUnitBulkUploadDto> csvBuilder = new CsvBuilder<EmissionsUnitBulkUploadDto>(EmissionsUnitBulkUploadDto.class, csvRows);

    	WebUtils.WriteCsv(response, csvBuilder);
    }
}
