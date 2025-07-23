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

import gov.epa.cef.web.repository.EmissionsProcessRepository;
import gov.epa.cef.web.repository.FacilitySiteRepository;
import gov.epa.cef.web.repository.ReportingPeriodRepository;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.ReportingPeriodService;
import gov.epa.cef.web.service.dto.*;
import gov.epa.cef.web.service.dto.bulkUpload.ReportingPeriodBulkUploadDto;
import gov.epa.cef.web.util.CsvBuilder;
import gov.epa.cef.web.util.WebUtils;

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

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reportingPeriod")
public class ReportingPeriodApi {

    private final ReportingPeriodService reportingPeriodService;

    private final SecurityService securityService;


    @Autowired
    ReportingPeriodApi(SecurityService securityService,
                              ReportingPeriodService reportingPeriodService) {

        this.reportingPeriodService = reportingPeriodService;
        this.securityService = securityService;
    }

    /**
     * Create a new Reporting Period
     * @param dto
     * @return
     */
    @PostMapping
    @Operation(summary = "Create reporting period",
        description = "Create report period",
        tags = "Reporting Period")
    public ResponseEntity<ReportingPeriodDto> createReportingPeriod(
        @NotNull @RequestBody ReportingPeriodDto dto) {

        this.securityService.facilityEnforcer()
            .enforceEntity(dto.getEmissionsProcessId(), EmissionsProcessRepository.class);

        ReportingPeriodDto result = reportingPeriodService.create(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update an reporting period
     * @param id
     * @param dto
     * @return
     */
    @PutMapping(value = "/{id}")
    @Operation(summary = "Update reporting period",
        description = "Update report period",
        tags = "Reporting Period")
    public ResponseEntity<ReportingPeriodUpdateResponseDto> updateReportingPeriod(
        @NotNull @PathVariable Long id, @NotNull @RequestBody ReportingPeriodDto dto) {

        this.securityService.facilityEnforcer().enforceEntity(id, ReportingPeriodRepository.class);

        ReportingPeriodUpdateResponseDto result = reportingPeriodService.update(dto.withId(id));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve a reporting period by id
     * @param periodId
     * @return
     */
    @GetMapping(value = "/{periodId}")
    @Operation(summary = "Get reporting period",
        description = "Get report period",
        tags = "Reporting Period")
    public ResponseEntity<ReportingPeriodDto> retrieveReportingPeriod(@NotNull @PathVariable Long periodId) {

        this.securityService.facilityEnforcer().enforceEntity(periodId, ReportingPeriodRepository.class);

        ReportingPeriodDto result = reportingPeriodService.retrieveById(periodId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Reporting Periods for an emissions process
     * @param processId
     * @return
     */
    @GetMapping(value = "/process/{processId}")
    @Operation(summary = "Get reporting periods for process",
        description = "Get report periods for process",
        tags = "Reporting Period")
    public ResponseEntity<Collection<ReportingPeriodDto>> retrieveReportingPeriodsForProcess(
        @NotNull @PathVariable Long processId) {

        this.securityService.facilityEnforcer().enforceEntity(processId, EmissionsProcessRepository.class);

        Collection<ReportingPeriodDto> result = reportingPeriodService.retrieveForEmissionsProcess(processId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve annual Reporting Period for an emissions process
     * @param processId
     * @return
     */
    @GetMapping(value = "/process/{processId}/annual")
    @Operation(summary = "Get reporting period annual report for process",
        description = "Create report period annual report for process",
        tags = "Reporting Period")
    public ResponseEntity<Collection<ReportingPeriodDto>> retrieveAnnualReportingPeriodForProcess(
        @NotNull @PathVariable Long processId) {

        this.securityService.facilityEnforcer().enforceEntity(processId, EmissionsProcessRepository.class);

        Collection<ReportingPeriodDto> result = reportingPeriodService.retrieveAnnualForEmissionsProcess(processId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Reporting Periods for bulk entry by Report Id
     * @param facilitySiteId
     * @return
     */
    @GetMapping(value = "/bulkEntry/{facilitySiteId}")
    @Operation(summary = "Get reporting period bulk entry for facility site",
        description = "Create report period bulk entry for facility site",
        tags = "Reporting Period")
    public ResponseEntity<Collection<ReportingPeriodBulkEntryDto>> retrieveBulkEntryReportingPeriodsForFacilitySite(
        @NotNull @PathVariable Long facilitySiteId) {

        this.securityService.facilityEnforcer().enforceEntity(facilitySiteId, FacilitySiteRepository.class);

        Collection<ReportingPeriodBulkEntryDto> result = reportingPeriodService.retrieveBulkEntryReportingPeriodsForFacilitySite(facilitySiteId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update the throughput for multiple Reporting Periods at once
     * @param dtos
     * @return
     */
    @PutMapping(value = "/bulkEntry/{facilitySiteId}")
    @Operation(summary = "Update reporting period bulk",
        description = "Update report period bulk",
        tags = "Reporting Period")
    public ResponseEntity<Collection<EmissionBulkEntryHolderDto>> bulkUpdate(
            @NotNull @PathVariable Long facilitySiteId, @NotNull @RequestBody List<ReportingPeriodBulkEntryDto> dtos) {

        this.securityService.facilityEnforcer().enforceEntity(facilitySiteId, FacilitySiteRepository.class);

        List<Long> periodIds = dtos.stream().map(ReportingPeriodBulkEntryDto::getReportingPeriodId).collect(Collectors.toList());
        this.securityService.facilityEnforcer().enforceEntities(periodIds, ReportingPeriodRepository.class);

        Collection<EmissionBulkEntryHolderDto> result = reportingPeriodService.bulkUpdate(facilitySiteId, dtos);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Monthly Reporting Period data for Facility Site and Period
     * @param facilitySiteId
     * @param period
     * @return
     */
    @GetMapping(value = "/monthlyReportingPeriods/{facilitySiteId}/{period}")
    @Operation(summary = "Get reporting period monthly process data",
        description = "Create report period montly process data",
        tags = "Reporting Period")
    public ResponseEntity<Collection<MonthlyReportingPeriodHolderDto>> retrieveMonthlyProcessesData(
        @NotNull @PathVariable Long facilitySiteId, @NotNull @PathVariable String period) {

        this.securityService.facilityEnforcer().enforceEntity(facilitySiteId, FacilitySiteRepository.class);

        Collection<MonthlyReportingPeriodHolderDto> result =
            reportingPeriodService.retrieveMonthlyReportingPeriodsForFacilitySite(facilitySiteId, period);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update monthly reporting info for multiple Reporting Periods at once
     * @param dtos
     * @return
     */
    @PutMapping(value = "/monthlyReportingPeriods/{facilitySiteId}/{period}")
    @Operation(summary = "Update reporting period monthly",
        description = "Update report period",
        tags = "Reporting Period")
    public ResponseEntity<Collection<MonthlyReportingEmissionHolderDto>> monthlyUpdate(
        @NotNull @PathVariable Long facilitySiteId, @NotNull @RequestBody List<MonthlyReportingPeriodHolderDto> dtos, @NotNull @PathVariable String period) {

        this.securityService.facilityEnforcer().enforceEntity(facilitySiteId, FacilitySiteRepository.class);

        List<Long> periodIds = dtos.stream().map(MonthlyReportingPeriodHolderDto::getReportingPeriodId).collect(Collectors.toList());
        this.securityService.facilityEnforcer().enforceEntities(periodIds, ReportingPeriodRepository.class);

        Collection<MonthlyReportingEmissionHolderDto> result = reportingPeriodService.monthlyUpdate(facilitySiteId, period, dtos);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /***
     * Retrieve a CSV of all of the reporting periods based on the given program system code and inventory year
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/list/csv/{programSystemCode}/{year}")
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN, AppRole.ROLE_ADMIN})
    @Operation(summary = "Get SLT reporting periods",
        description = "Get SLT report periods",
        tags = "Reporting Period")
    public void getSltReportingPeriods(@PathVariable String programSystemCode, @PathVariable Short year, HttpServletResponse response) {

    	List<ReportingPeriodBulkUploadDto> csvRows = reportingPeriodService.retrieveReportingPeriods(programSystemCode, year);
    	CsvBuilder<ReportingPeriodBulkUploadDto> csvBuilder = new CsvBuilder<ReportingPeriodBulkUploadDto>(ReportingPeriodBulkUploadDto.class, csvRows);

    	WebUtils.WriteCsv(response, csvBuilder);
    }
}
