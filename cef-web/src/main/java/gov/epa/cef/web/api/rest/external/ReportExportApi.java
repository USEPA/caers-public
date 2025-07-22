/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
