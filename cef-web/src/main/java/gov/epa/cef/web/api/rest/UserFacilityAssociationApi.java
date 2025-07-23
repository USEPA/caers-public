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

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.dto.MasterFacilityRecordDto;
import gov.epa.cef.web.service.dto.UserFacilityAssociationDto;
import gov.epa.cef.web.service.impl.UserFacilityAssociationServiceImpl;

@RestController
@RequestMapping("/api/userFacilityAssociation")
public class UserFacilityAssociationApi {

    private final UserFacilityAssociationServiceImpl ufaService;

    private final SecurityService securityService;

    @Autowired
    UserFacilityAssociationApi(SecurityService securityService,
                               UserFacilityAssociationServiceImpl mfrService) {

        this.securityService = securityService;
        this.ufaService = mfrService;
    }


    @GetMapping(value = "/{id}")
    @Operation(summary = "Get user facility association",
        description = "Get user facility association",
        tags = "User Facility Association")
    public ResponseEntity<UserFacilityAssociationDto> retrieveAssociation(@NotNull @PathVariable Long id) {

        UserFacilityAssociationDto result = this.ufaService.findById(id);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete user facility association",
        description = "Delete user facility association",
        tags = "User Facility Association")
    public void deleteAssociation(@NotNull @PathVariable Long id) {

        this.ufaService.deleteById(id, this.securityService.getCurrentApplicationUser());
    }

    @PostMapping(value = "/selfRemove")
    @RolesAllowed(value = {AppRole.ROLE_PREPARER, AppRole.ROLE_NEI_CERTIFIER})
    @Operation(summary = "Update user facility association self remove",
        description = "Update user facility association self remove",
        tags = "User Facility Association")
    public ResponseEntity<List<UserFacilityAssociationDto>> selfRemoveAssociation(@NotNull @RequestBody Long mfrId) {
        this.ufaService.removeSelfAssociationByCdxUserId(this.securityService.getCurrentApplicationUser(), mfrId);
        List<UserFacilityAssociationDto> result =
            this.ufaService.findByUserRoleId(this.securityService.getCurrentApplicationUser().getUserRoleId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/request")
    @RolesAllowed(value = {AppRole.ROLE_PREPARER, AppRole.ROLE_NEI_CERTIFIER})
    @Operation(summary = "Create user facility association request",
        description = "Create user facility association request",
        tags = "User Facility Association")
    public ResponseEntity<UserFacilityAssociationDto> createAssociationRequest(@NotNull @RequestBody MasterFacilityRecordDto facility) {

        UserFacilityAssociationDto result = this.ufaService.requestFacilityAssociation(facility, this.securityService.getCurrentApplicationUser());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/approve")
    @Operation(summary = "Approve user facility association request",
        description = "Approve user facility association request",
        tags = "User Facility Association")
    public ResponseEntity<List<UserFacilityAssociationDto>> approveAssociationRequests(@NotNull @RequestBody List<UserFacilityAssociationDto> associations) {

        List<UserFacilityAssociationDto> result = this.ufaService.approveAssociations(associations, this.securityService.getCurrentApplicationUser());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/reject")
    @Operation(summary = "Reject user facility association request",
        description = "Reject user facility association request",
        tags = "User Facility Association")
    public ResponseEntity<List<UserFacilityAssociationDto>> rejectAssociationRequests(@NotNull @RequestBody RejectDto dto) {

        List<UserFacilityAssociationDto> result = this.ufaService.rejectAssociations(dto.getAssociations(), dto.getComments(), this.securityService.getCurrentApplicationUser());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/user/{userRoleId}")
    @Operation(summary = "Get association for user",
        description = "Get association for user",
        tags = "User Facility Association")
    public ResponseEntity<List<UserFacilityAssociationDto>> retrieveAssociationsForUser(
        @NotNull @PathVariable Long userRoleId) {

        List<UserFacilityAssociationDto> result =
            this.ufaService.findByUserRoleId(userRoleId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/facility/{masterFacilityRecordId}/approved/details")
    @Operation(summary = "Get user facility association approved details for facility",
        description = "Get user facility association approved details for facility",
        tags = "User Facility Association")
    public ResponseEntity<List<UserFacilityAssociationDto>> retrieveApprovedAssociationDetailsForFacility(
        @NotNull @PathVariable Long masterFacilityRecordId) {

        List<UserFacilityAssociationDto> result =
            this.ufaService.findDetailsByMasterFacilityRecordIdAndActive(masterFacilityRecordId, true);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/pending/details")
    @Operation(summary = "Get user facility association pending details for current program",
        description = "Get user facility association pending details for current program",
        tags = "User Facility Association")
    public ResponseEntity<List<UserFacilityAssociationDto>> retrievePendingAssociationDetailsForCurrentProgram() {

        List<UserFacilityAssociationDto> result =
            this.ufaService.findDetailsByProgramSystemCodeAndActive(this.securityService.getCurrentProgramSystemCode(), false);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/my")
    @Operation(summary = "Get user facility association for current user",
        description = "Get user facility association for current user",
        tags = "User Facility Association")
    public ResponseEntity<List<UserFacilityAssociationDto>> retrieveAssociationsForCurrentUser() {

        List<UserFacilityAssociationDto> result =
            this.ufaService.findByUserRoleId(this.securityService.getCurrentApplicationUser().getUserRoleId());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/myPrograms")
    @Operation(summary = "Get user facility association program system code for current user",
        description = "Get user facility association program system code for current user",
        tags = "User Facility Association")
    public ResponseEntity<List<String>> retrieveProgramSystemCodeAssociationsForCurrentUser() {

        List<String> result =
            this.ufaService.findProgramSystemCodesByUserRoleId(this.securityService.getCurrentApplicationUser().getUserRoleId());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/migrate")
    @Operation(summary = "Get user facility association migrations",
        description = "Get user facility association migrations",
        tags = "User Facility Association")
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN})
    public ResponseEntity<List<UserFacilityAssociationDto>> migrateUserAssociations() {

        List<UserFacilityAssociationDto> result =
            this.ufaService.migrateAssociations();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    public static class RejectDto {

        private List<UserFacilityAssociationDto> associations;

        private String comments;

        public List<UserFacilityAssociationDto> getAssociations() {

            return associations;
        }

        public void setAssociations(List<UserFacilityAssociationDto> associations) {

            this.associations = associations;
        }

        public String getComments() {

            return comments;
        }

        public void setComments(String comments) {

            this.comments = comments;
        }

    }
}
