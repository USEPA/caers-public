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
package gov.epa.cef.web.api.rest.external;

import gov.epa.cef.web.domain.ReportStatus;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.EmissionsReportExportService;
import gov.epa.cef.web.service.dto.json.EmissionsReportJsonDto;
import gov.epa.cef.web.service.dto.json.ReportExportQueryDto;
import gov.epa.cef.web.service.dto.json.ReportInfoJsonDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/public/export/report")
@SecurityScheme(name = "myOauth2Security",
    type = SecuritySchemeType.OAUTH2,
    flows = @OAuthFlows(
        clientCredentials = @OAuthFlow(tokenUrl = "${spring.security.oauth2.authorizationserver.endpoint.token-uri}",
            scopes = @OAuthScope(name = "slt:export", description = "report export"))))
public class ReportExportApi {

    @Autowired
    private EmissionsReportExportService exportService;

    @Autowired
    private SecurityService securityService;

    @GetMapping(value = "/{reportYear}")
    @Operation(summary = "Get detailed reports",
        description = "Get all report information for reports that match the provided criteria.",
        tags = "Report Export")
    public ResponseEntity<List<EmissionsReportJsonDto>> exportJsonReport(
        @Parameter(description = "What reporting year to return results for.", required = true) @NotNull @PathVariable Integer reportYear,
        @Parameter(description = "Earliest last modified date that will be returned in the results.") @RequestParam(required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Date modifiedStartDate,
        @Parameter(description = "Latest last modified date that will be returned in the results.") @RequestParam(required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Date modifiedEndDate,
        @Parameter(description = "Earliest certified date that will be returned in the results.") @RequestParam(required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Date certifiedStartDate,
        @Parameter(description = "Latest certified date that will be returned in the results.") @RequestParam(required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Date certifiedEndDate,
        @Parameter(description = "List of Agency Facility IDs to return results for.") @RequestParam(required = false) List<String> agencyFacilityId,
        @Parameter(description = "List of internal CAERS report IDs to return results for.") @RequestParam(required = false) List<String> reportId,
        @Parameter(description = "List of report statuses to return results for.") @RequestParam(required = false) List<ReportStatus> reportStatus,
        @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "500") Integer pageSize) {

        String programSystemCode = this.securityService.getSltRoles().get(0);

        ReportExportQueryDto criteria = new ReportExportQueryDto(
            programSystemCode, reportYear, modifiedStartDate, modifiedEndDate, certifiedStartDate, certifiedEndDate,
            agencyFacilityId, reportId, reportStatus, page, pageSize);

        List<EmissionsReportJsonDto> result =
            exportService.generateJsonExportDto(criteria);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{reportYear}/list")
    @Operation(summary = "Get list of reports",
        description = "Get list of reports based on provided criteria.",
        tags = "Report Export")
    public ResponseEntity<List<ReportInfoJsonDto>> getReportList(
        @Parameter(description = "What reporting year to return results for.", required = true) @NotNull @PathVariable Integer reportYear,
        @Parameter(description = "Earliest last modified date that will be returned in the results.") @RequestParam(required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Date modifiedStartDate,
        @Parameter(description = "Latest last modified date that will be returned in the results.") @RequestParam(required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Date modifiedEndDate,
        @Parameter(description = "Earliest certified date that will be returned in the results.") @RequestParam(required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Date certifiedStartDate,
        @Parameter(description = "Latest certified date that will be returned in the results.") @RequestParam(required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Date certifiedEndDate,
        @Parameter(description = "List of Agency Facility IDs to return results for.") @RequestParam(required = false) List<String> agencyFacilityId,
        @Parameter(description = "List of internal CAERS report IDs to return results for.") @RequestParam(required = false) List<String> reportId,
        @Parameter(description = "List of report statuses to return results for.") @RequestParam(required = false) List<ReportStatus> reportStatus) {

        String programSystemCode = this.securityService.getSltRoles().get(0);

        ReportExportQueryDto criteria = new ReportExportQueryDto(
            programSystemCode, reportYear, modifiedStartDate, modifiedEndDate, certifiedStartDate, certifiedEndDate,
            agencyFacilityId, reportId, reportStatus, null, null);

        List<ReportInfoJsonDto> result = exportService.generateJsonExportSummaryDto(criteria);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }



}
