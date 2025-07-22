/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.api.rest;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;

import gov.epa.cef.web.repository.FacilityPermitRepository;
import gov.epa.cef.web.service.dto.FacilityPermitDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.epa.cef.web.domain.ProgramSystemCode;
import gov.epa.cef.web.repository.MasterFacilityNAICSXrefRepository;
import gov.epa.cef.web.repository.MasterFacilityRecordRepository;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.dto.CodeLookupDto;
import gov.epa.cef.web.service.dto.MasterFacilityRecordDto;
import gov.epa.cef.web.service.dto.MasterFacilityNAICSDto;
import gov.epa.cef.web.service.MasterFacilityRecordService;
import gov.epa.cef.web.service.LookupService;

@RestController
@RequestMapping("/api/masterFacility")
public class MasterFacilityRecordApi {

    private final MasterFacilityRecordService mfrService;

    private final SecurityService securityService;

    private final LookupService lookupService;

    @Autowired
    MasterFacilityRecordApi(SecurityService securityService,
            MasterFacilityRecordService mfrService,
            LookupService lookupService) {

        this.securityService = securityService;
        this.mfrService = mfrService;
        this.lookupService = lookupService;
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get master facility record",
        description = "Get master facility record",
        tags = "Master Facility Record")
    public ResponseEntity<MasterFacilityRecordDto> retrieveRecord(@NotNull @PathVariable Long id) {

        MasterFacilityRecordDto result = this.mfrService.findById(id);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/program/{programSystemCode}")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER, AppRole.ROLE_CAERS_ADMIN})
    @Operation(summary = "Get master facility record for program",
        description = "Get master facility record for program",
        tags = "Master Facility Record")
    public ResponseEntity<List<MasterFacilityRecordDto>> retrieveRecordsForProgram(
        @NotNull @PathVariable String programSystemCode) {

        List<MasterFacilityRecordDto> result =
            this.mfrService.findByProgramSystemCode(programSystemCode);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/search")
    @Operation(summary = "Get master facility record by criteria",
        description = "Get master facility record by criteria",
        tags = "Master Facility Record")
    public ResponseEntity<List<MasterFacilityRecordDto>> search(@RequestBody MasterFacilityRecordDto criteria) {

        List<MasterFacilityRecordDto> result = this.mfrService.findByExample(criteria);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/program/my")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    @Operation(summary = "Get master facility record for current program",
        description = "Get master facility record for current program",
        tags = "Master Facility Record")
    public ResponseEntity<List<MasterFacilityRecordDto>> retrieveRecordsForCurrentProgram() {

        List<MasterFacilityRecordDto> result =
            this.mfrService.findByProgramSystemCode(this.securityService.getCurrentProgramSystemCode());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update an existing master facility record
     * @param masterFacilityRecordId
     * @param dto
     * @return
     */
    @PutMapping(value = "/{masterFacilityRecordId}")
    @Operation(summary = "Update master facility record",
        description = "Update master facility record",
        tags = "Master Facility Record")
    public ResponseEntity<MasterFacilityRecordDto> updateMasterFacilityRecord(
    		@NotNull @PathVariable Long masterFacilityRecordId, @NotNull @RequestBody MasterFacilityRecordDto dto) {

    	this.securityService.facilityEnforcer().enforceEntity(masterFacilityRecordId, MasterFacilityRecordRepository.class);

    	MasterFacilityRecordDto result = mfrService.update(dto.withId(masterFacilityRecordId));

    	return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Create a new master facility record
     * @param dto
     * @return
     */
    @PostMapping(value = "/create")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER, AppRole.ROLE_CAERS_ADMIN})
    @Operation(summary = "Create master facility record",
        description = "Create master facility record",
        tags = "Master Facility Record")
    public ResponseEntity<MasterFacilityRecordDto> createMasterFacilityRecord(@NotNull @RequestBody MasterFacilityRecordDto dto) {

    	MasterFacilityRecordDto result = mfrService.create(dto);
    	return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping(value = "/programSystemCodes")
    @Operation(summary = "Get master facility record program system codes",
        description = "Get master facility record program system codes",
        tags = "Master Facility Record")
    public ResponseEntity<List<CodeLookupDto>> retrieveProgramSystemCodes() {

        List<CodeLookupDto> result = this.mfrService.findDistinctProgramSystems();
        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    @GetMapping(value = "/userProgramSystemCode")
    @Operation(summary = "Get master facility record program system codes for current user",
        description = "Get master facility record program system codes for current user",
        tags = "Master Facility Record")
    public ResponseEntity<ProgramSystemCode> retrieveProgramSystemCodeForCurrentUser() {
        ProgramSystemCode result = this.lookupService.retrieveProgramSystemTypeCodeEntityByCode(this.securityService.getCurrentProgramSystemCode());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping(value = "/isDuplicateAgencyId/{agencyFacilityIdentifier}/{programSystemCode}")
    @Operation(summary = "Get master facility record duplicate agency id",
        description = "Get master facility record duplicate agency id",
        tags = "Master Facility Record")
    public ResponseEntity<Boolean> isDuplicateAgencyId(@NotNull @PathVariable String agencyFacilityIdentifier, @NotNull @PathVariable String programSystemCode) {

        Boolean result = this.mfrService.isDuplicateAgencyId(agencyFacilityIdentifier, programSystemCode);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Create Master Facility Record NAICS
     * @param dto
     * @return
     */
    @PostMapping(value = "/naics/")
    @Operation(summary = "Create master facility record NAICS",
        description = "Create master facility record NAICS",
        tags = "Master Facility Record")
    public ResponseEntity<MasterFacilityNAICSDto> createMasterFacilityNaics(@NotNull @RequestBody MasterFacilityNAICSDto dto) {
    	this.securityService.facilityEnforcer().enforceEntity(dto.getMasterFacilityRecordId(), MasterFacilityRecordRepository.class);

    	MasterFacilityNAICSDto result = mfrService.createMasterFacilityNaics(dto);
    	return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update Master Facility Record NAICS
     * @param mfrNaicsId
     * @param dto
     * @return
     */
    @PutMapping(value = "/naics/{mfrNaicsId}")
    @Operation(summary = "Update master facility record NAICS",
        description = "Update master facility record NAICS",
        tags = "Master Facility Record")
    public ResponseEntity<MasterFacilityNAICSDto> updateMasterFacilityNaics(@NotNull @PathVariable Long mfrNaicsId, @NotNull @RequestBody MasterFacilityNAICSDto dto) {
    	this.securityService.facilityEnforcer().enforceEntity(dto.getMasterFacilityRecordId(), MasterFacilityRecordRepository.class);

    	MasterFacilityNAICSDto result = mfrService.updateMasterFacilityNaics(dto.withId(mfrNaicsId));
    	return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete a Master Facility Record NAICS
     * @param mfrNaicsId
     * @return
     */
    @DeleteMapping(value = "/naics/{mfrNaicsId}")
    @Operation(summary = "Delete master facility record NAICS",
        description = "Delete master facility record NAICS",
        tags = "Master Facility Record")
    public void deleteMasterFacilityNAICS(@PathVariable Long mfrNaicsId) {
    	this.securityService.facilityEnforcer().enforceEntity(mfrNaicsId, MasterFacilityNAICSXrefRepository.class);

    	mfrService.deleteMasterFacilityNaics(mfrNaicsId);
    }

    /**
     * Create Master Facility Record Permit
     * @param dto
     * @return
     */
    @PostMapping(value = "/permit/")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER, AppRole.ROLE_CAERS_ADMIN})
    @Operation(summary = "Create master facility record permit",
        description = "Create master facility record permit",
        tags = "Master Facility Record")
    public ResponseEntity<FacilityPermitDto> createMasterFacilityPermit(@NotNull @RequestBody FacilityPermitDto dto) {
        this.securityService.facilityEnforcer().enforceEntity(dto.getMasterFacilityId(), MasterFacilityRecordRepository.class);

        FacilityPermitDto result = mfrService.createMasterFacilityPermit(dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update Master Facility Record Permit
     * @param permitId
     * @param dto
     * @return
     */
    @PutMapping(value = "/permit/{permitId}")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER, AppRole.ROLE_CAERS_ADMIN})
    @Operation(summary = "Update master facility record permit",
        description = "Update master facility record permit",
        tags = "Master Facility Record")
    public ResponseEntity<FacilityPermitDto> updateMasterFacilityPermit(@NotNull @PathVariable Long permitId, @NotNull @RequestBody FacilityPermitDto dto) {
        this.securityService.facilityEnforcer().enforceEntity(dto.getMasterFacilityId(), MasterFacilityRecordRepository.class);

        FacilityPermitDto result = mfrService.updateMasterFacilityPermit(dto.withId(permitId));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete a Master Facility Record Permit
     * @param permitId
     * @return
     */
    @DeleteMapping(value = "/permit/{permitId}")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER, AppRole.ROLE_CAERS_ADMIN})
    @Operation(summary = "Delete master facility record permit",
        description = "Delete master facility record permit",
        tags = "Master Facility Record")
    public void deleteMasterFacilityPermit(@PathVariable Long permitId) {
        this.securityService.facilityEnforcer().enforceEntity(permitId, FacilityPermitRepository.class);

        mfrService.deleteMasterFacilityPermit(permitId);
    }

    /**
     * Get lat/long tolerance for EIS ID
     * @param eisId
     * @return
     */
    @GetMapping(value = "/getLatLongTolerance/{eisId}")
    @Operation(summary = "Get master facility record lat long tolerance",
        description = "Update master facility record let long tolerance",
        tags = "Master Facility Record")
    public ResponseEntity<BigDecimal> getLatLongTolerance(@NotNull @PathVariable String eisId) {

        BigDecimal result = this.mfrService.getLatLongTolerance(eisId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
