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

import gov.epa.cef.web.repository.EmissionRepository;
import gov.epa.cef.web.repository.FacilitySiteRepository;
import gov.epa.cef.web.repository.ReportingPeriodRepository;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.EmissionService;
import gov.epa.cef.web.service.dto.*;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionFormulaVariableBulkUploadDto;
import gov.epa.cef.web.service.mapper.EmissionMapper;
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
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/emission")
public class EmissionApi {

    private final EmissionService emissionService;

    private final SecurityService securityService;

    private final EmissionMapper emissionMapper;

    @Autowired
    EmissionApi(SecurityService securityService,
                EmissionService emissionService,
                EmissionMapper emissionMapper) {

        this.securityService = securityService;
        this.emissionService = emissionService;
        this.emissionMapper = emissionMapper;
    }

    /**
     * Create a new Emission
     * @param dto
     * @return
     */
    @PostMapping
    @Operation(summary = "Create emission",
        description = "Create emission",
        tags = "Emission")
    public ResponseEntity<EmissionDto> createEmission(@NotNull @RequestBody EmissionDto dto) {

        this.securityService.facilityEnforcer()
            .enforceEntity(dto.getReportingPeriodId(), ReportingPeriodRepository.class);

        EmissionDto result = emissionService.create(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve an Emission
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get emission",
        description = "Get emission",
        tags = "Emission")
    public ResponseEntity<EmissionDto> retrieveEmission(@NotNull @PathVariable Long id) {

        this.securityService.facilityEnforcer().enforceEntity(id, EmissionRepository.class);

        EmissionDto result = emissionService.retrieveById(id);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve an Emission and generate missing variables
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}/variables")
    @Operation(summary = "Get variables by id",
        description = "Get variables by id",
        tags = "Emission")
    public ResponseEntity<EmissionDto> retrieveWithVariablesById(@NotNull @PathVariable Long id) {

        this.securityService.facilityEnforcer().enforceEntity(id, EmissionRepository.class);

        EmissionDto result = emissionService.retrieveWithVariablesById(id);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update an existing Emission
     * @param id
     * @param dto
     * @return
     */
    @PutMapping(value = "/{id}")
    @Operation(summary = "Update emission",
        description = "Update emission",
        tags = "Emission")
    public ResponseEntity<EmissionDto> updateEmission(
        @NotNull @PathVariable Long id, @NotNull @RequestBody EmissionDto dto) {

        this.securityService.facilityEnforcer().enforceEntity(id, EmissionRepository.class);

        if (dto.getEmissionsDenominatorUom() != null && dto.getEmissionsDenominatorUom().getCode() == null) {
            dto.setEmissionsDenominatorUom(null);
        }
        if (dto.getEmissionsNumeratorUom() != null && dto.getEmissionsNumeratorUom().getCode() == null) {
            dto.setEmissionsNumeratorUom(null);
        }
        if (!dto.getTotalManualEntry()) {
            dto.setCalculationComment(null);
        }
        EmissionDto result = emissionService.update(dto.withId(id));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete an Emission for given id
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete emission",
        description = "Delete emission",
        tags = "Emission")
    public void deleteEmission(@NotNull @PathVariable Long id) {

        this.securityService.facilityEnforcer().enforceEntity(id, EmissionRepository.class);

        emissionService.delete(id);
    }

    /**
     * Retrieve Reporting Periods for bulk entry by Report Id
     * @param facilitySiteId
     * @return
     */
    @GetMapping(value = "/bulkEntry/{facilitySiteId}")
    @Operation(summary = "Get bulk entry emission for facility",
        description = "Get bulk entry emission for facility",
        tags = "Emission")
    public ResponseEntity<Collection<EmissionBulkEntryHolderDto>> retrieveBulkEntryEmissionsForFacilitySite(
        @NotNull @PathVariable Long facilitySiteId) {

        this.securityService.facilityEnforcer().enforceEntity(facilitySiteId, FacilitySiteRepository.class);

        Collection<EmissionBulkEntryHolderDto> result = emissionService.retrieveBulkEntryEmissionsForFacilitySite(facilitySiteId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update the total emissions for multiple Emissions at once and recalculate all Emissions for this facility
     * @param dtos
     * @return
     */
    @PutMapping(value = "/bulkEntry/{facilitySiteId}")
    @Operation(summary = "Update emission bulk",
        description = "Update emission bulk",
        tags = "Emission")
    public ResponseEntity<Collection<EmissionBulkEntryHolderDto>> bulkUpdate(
        @NotNull @PathVariable Long facilitySiteId, @RequestBody List<EmissionDto> dtos) {

        this.securityService.facilityEnforcer().enforceEntity(facilitySiteId, FacilitySiteRepository.class);

        List<Long> emissionIds = dtos.stream().map(EmissionDto::getId).collect(Collectors.toList());
        this.securityService.facilityEnforcer().enforceEntities(emissionIds, EmissionRepository.class);

        Collection<EmissionBulkEntryHolderDto> result = emissionService.bulkUpdate(facilitySiteId, dtos);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Emissions for semiannual Reporting Periods Including Operating Details for an Emissions Report
     * @param reportId
     * @param isSemiAnnual
     * @return
     */
    @GetMapping(value = "/downloadMonthlyEmissions/{reportId}/{isSemiAnnual}")
    @Operation(summary = "Get monthly emission download for facility",
        description = "Get monthly emission download for facility",
        tags = "Emission")
    @RolesAllowed(value = {AppRole.REVIEWER, AppRole.ROLE_REVIEWER, AppRole.ROLE_ADMIN, AppRole.ROLE_CAERS_ADMIN})
    public ResponseEntity<Collection<MonthlyReportingDownloadDto>> retrieveMonthlyEmissionDownloadDtoForFacilitySite(
        @NotNull @PathVariable Long reportId,
        @NotNull @PathVariable boolean isSemiAnnual) {

        Collection<MonthlyReportingDownloadDto> result =
            emissionService.retrieveMonthlyDownLoadDtoListForReport(reportId, isSemiAnnual);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Monthly Emissions for the given Facility Site and Reporting Period Type
     * @param facilitySiteId
     * @param period
     * @return
     */
    @GetMapping(value = "/monthlyEmissions/{facilitySiteId}/{period}")
    @Operation(summary = "Get monthly emission for facility site period",
        description = "Get monthly emission for facility site period",
        tags = "Emission")
    public ResponseEntity<Collection<MonthlyReportingEmissionHolderDto>> retrieveMonthlyEmissionsForFacilitySitePeriod(
        @NotNull @PathVariable Long facilitySiteId, @NotNull @PathVariable String period) {

        this.securityService.facilityEnforcer().enforceEntity(facilitySiteId, FacilitySiteRepository.class);

        Collection<MonthlyReportingEmissionHolderDto> result = emissionService.retrieveMonthlyEmissionsForFacilitySitePeriod(facilitySiteId, period);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update the total emissions for monthly Emissions at same time and recalculate all Emissions
     * @param dtos
     * @return
     */
    @PutMapping(value = "/monthlyEmissions/{facilitySiteId}/{period}")
    @Operation(summary = "Update monthly emission",
        description = "Update monthly emission",
        tags = "Emission")
    public ResponseEntity<Collection<MonthlyReportingEmissionHolderDto>> monthlyUpdate(
        @NotNull @PathVariable Long facilitySiteId, @RequestBody List<EmissionDto> dtos, @NotNull @PathVariable String period) {

        this.securityService.facilityEnforcer().enforceEntity(facilitySiteId, FacilitySiteRepository.class);

        List<Long> emissionIds = dtos.stream().map(EmissionDto::getId).collect(Collectors.toList());
        this.securityService.facilityEnforcer().enforceEntities(emissionIds, EmissionRepository.class);

        Collection<MonthlyReportingEmissionHolderDto> result = emissionService.monthlyUpdate(facilitySiteId, period, dtos);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Calculate total emissions and emission factor
     * @param dto
     * @return
     */
    @PostMapping(value = "/calculate")
    @Operation(summary = "Calculate total emissions",
        description = "Calculate total emissions",
        tags = "Emission")
    public ResponseEntity<EmissionDto> calculateTotalEmissions(@NotNull @RequestBody EmissionDto dto) {


        EmissionDto result = emissionService.calculateTotalEmissions(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Calculate emission factor
     * @param dto
     * @return
     */
    @PostMapping(value = "/calculateFactor")
    @Operation(summary = "Calculate emission factor",
        description = "Calculate emission factor",
        tags = "Emission")
    public ResponseEntity<EmissionDto> calculateEmissionFactor(@NotNull @RequestBody EmissionDto dto) {

        EmissionDto result = emissionService.calculateEmissionFactorDto(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /***
     * Retrieve a CSV of all of the emissions based on the given program system code and inventory year
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/list/csv/{programSystemCode}/{year}")
    @Operation(summary = "Get SLT emissions",
        description = "Get SLT emissions",
        tags = "Emission")
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN, AppRole.ROLE_ADMIN})
    public void getSltEmissions(@PathVariable String programSystemCode, @PathVariable Short year, HttpServletResponse response) {

    	List<EmissionBulkUploadDto> csvRows = emissionService.retrieveEmissions(programSystemCode, year);
    	CsvBuilder<EmissionBulkUploadDto> csvBuilder = new CsvBuilder<EmissionBulkUploadDto>(EmissionBulkUploadDto.class, csvRows);

    	WebUtils.WriteCsv(response, csvBuilder);
    }

    /***
     * Retrieve a CSV of all of the emission formula variables based on the given program system code and inventory year
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/formulaVariables/list/csv/{programSystemCode}/{year}")
    @Operation(summary = "Get SLT emission formula variables",
        description = "Get SLT emission formula variables",
        tags = "Emission")
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN, AppRole.ROLE_ADMIN})
    public void getSltEmissionFormulaVariables(@PathVariable String programSystemCode, @PathVariable Short year, HttpServletResponse response) {

    	List<EmissionFormulaVariableBulkUploadDto> csvRows = emissionService.retrieveEmissionFormulaVariables(programSystemCode, year);
    	CsvBuilder<EmissionFormulaVariableBulkUploadDto> csvBuilder = new CsvBuilder<EmissionFormulaVariableBulkUploadDto>(EmissionFormulaVariableBulkUploadDto.class, csvRows);

    	WebUtils.WriteCsv(response, csvBuilder);
    }

    /***
     * Return list of pollutants for provided process that were added after semiannual report was submitted
     * @param processId
     * @param reportId
     * @return
     */
    @GetMapping(value = "/createdAfterSemiannualSubmission/{processId}/{reportId}")
    @Operation(summary = "Get emissions created after semiannual submission",
        description = "Get emissions created after semiannual submission",
        tags = "Emission")
    public ResponseEntity<List<String>> getEmissionsCreatedAfterSemiannualSubmission(
        @NotNull @PathVariable Long processId, @NotNull @PathVariable Long reportId) {

        List<String> pollutants = emissionService.getEmissionsCreatedAfterSemiannualSubmission(processId, reportId);

        return new ResponseEntity<>(pollutants, HttpStatus.OK);
    }
}
