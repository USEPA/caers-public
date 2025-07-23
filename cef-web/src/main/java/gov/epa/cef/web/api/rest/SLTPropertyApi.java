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

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.epa.cef.web.domain.SLTConfigProperty;
import gov.epa.cef.web.exception.ApplicationErrorCode;
import gov.epa.cef.web.exception.ApplicationException;
import gov.epa.cef.web.provider.system.SLTPropertyProvider;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.PropertyService;
import gov.epa.cef.web.service.dto.PropertyDto;
import gov.epa.cef.web.service.mapper.AppPropertyMapper;

import gov.epa.cef.web.config.SLTPropertyName;

@RestController
@RequestMapping("/api/slt/property")
@RolesAllowed(value = {AppRole.ROLE_REVIEWER, AppRole.ROLE_CAERS_ADMIN})
public class SLTPropertyApi {

    @Autowired
    private SLTPropertyProvider propertyProvider;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private PropertyService propService;

    @Autowired
    private AppPropertyMapper mapper;

    /**
     * Retrieve a properties
     * @return
     */
    @GetMapping(value = "/{name}")
    @Operation(summary = "Get SLT property",
        description = "Get SLT property",
        tags = "SLT Property")
    public ResponseEntity<PropertyDto> retrieveProperty(@NotNull @PathVariable String name) {

        String userProgramSystem = this.securityService.getCurrentProgramSystemCode();

        PropertyDto result = mapper.sltToDto(propertyProvider.retrieve(new PropertyDto().withName(name), userProgramSystem));

        if (result.getName() != null && result.getName().equals(SLTPropertyName.SLTFeatureAnnouncementText)) {
			propService.sanitizeHtml(result);
		}

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve all properties for selected SLT
     * @return
     */
    @GetMapping(value = "/property/{slt}")
    @Operation(summary = "Get all SLT properties",
        description = "Get all SLT properties",
        tags = "SLT Property")
    public ResponseEntity<List<PropertyDto>> retrieveAllProperties(@NotNull @PathVariable String slt) {

    	if (securityService.hasRole(AppRole.RoleType.REVIEWER) && !this.securityService.getCurrentProgramSystemCode().equals(slt)) {
            throw new ApplicationException(ApplicationErrorCode.E_INVALID_ARGUMENT, "Program System Code "+slt+" is not valid for current user.");
        }

        List<PropertyDto> result = mapper.sltToDtoList(propertyProvider.retrieveAllForProgramSystem(slt));

        for (PropertyDto prop : result) {
    		if (prop.getName() != null && prop.getName().equals(SLTPropertyName.SLTFeatureAnnouncementText.configKey())) {
    			propService.sanitizeHtml(prop);
    		}
    	}

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update a property value
     * @return
     */
    @PutMapping(value = "/{propName}")
    @Operation(summary = "Update SLT property by name",
        description = "Update SLT property by name",
        tags = "SLT Property")
    public ResponseEntity<PropertyDto> updateProperty(@NotNull @PathVariable String propName,
            @NotNull @RequestBody PropertyDto dto) {

        String userProgramSystem = this.securityService.getCurrentProgramSystemCode();

        if (dto.getName() != null && dto.getName().equals(SLTPropertyName.SLTFeatureAnnouncementText.configKey())) {
			propService.sanitizeHtml(dto);
		}

        // set canEdit to false once initial reporting year is saved for the first time
        if (dto.getName() != null && dto.getName().equals(SLTPropertyName.MonthlyFuelReportingInitialYear.configKey())) {
            dto.setCanEdit(false);
        }

        SLTConfigProperty result = this.propertyProvider.update(dto.withName(propName), userProgramSystem, dto.getValue());
        return new ResponseEntity<>(mapper.sltToDto(result), HttpStatus.OK);
    }

    /**
     * Update multiple properties
     * @return
     */
    @PostMapping(value = "/{slt}")
    @Operation(summary = "Update SLT property by slt",
        description = "Get SLT property byh slt",
        tags = "SLT Property")
    public ResponseEntity<List<PropertyDto>> updateProperties(@NotNull @RequestBody List<PropertyDto> dtos, @NotNull @PathVariable String slt) {

    	if (securityService.hasRole(AppRole.RoleType.REVIEWER) && !this.securityService.getCurrentProgramSystemCode().equals(slt)) {
            throw new ApplicationException(ApplicationErrorCode.E_INVALID_ARGUMENT, "Program System Code "+slt+" is not valid for current user.");
        }

    	for (PropertyDto prop : dtos) {
    		if (prop.getName() != null && prop.getName().equals(SLTPropertyName.SLTFeatureAnnouncementText.configKey())) {
    			propService.sanitizeHtml(prop);
    		}
    	}

        List<PropertyDto> result = dtos.stream().map(dto -> {
            SLTConfigProperty prop = this.propertyProvider.update(dto, slt, dto.getValue());
            return mapper.sltToDto(prop);
        }).collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
