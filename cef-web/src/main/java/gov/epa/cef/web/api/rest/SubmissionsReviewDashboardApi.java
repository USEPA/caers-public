/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.api.rest;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.epa.cef.web.domain.ReportStatus;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.service.SubmissionsReviewDasboardService;
import gov.epa.cef.web.service.dto.SubmissionsReviewDashboardDto;

@RestController
@RequestMapping("/api/submissionsReview")
public class SubmissionsReviewDashboardApi {

    @Autowired
    private SubmissionsReviewDasboardService submissionsReviewDasboardService;

    /**
     * Retrieve submissions for the current user's SLT based on year and status
     * @param reportYear
     * @param reportStatus
     * @return
     */
    @GetMapping(value = "/dashboard")
    @ResponseBody
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    @Operation(summary = "Get reviewer submission",
        description = "Get reviewer submission",
        tags = "Reviewer Submission")
    public ResponseEntity<List<SubmissionsReviewDashboardDto>> retrieveReviewerSubmissions(
            @RequestParam(required = false) Short reportYear,
            @RequestParam(required = false) String reportStatus) {

        List<SubmissionsReviewDashboardDto> result = submissionsReviewDasboardService.retrieveReviewerFacilityReports(reportYear, reportStatus);
        return new ResponseEntity<List<SubmissionsReviewDashboardDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve submissions based on year, status, and agency
     * @param agency
     * @param reportYear
     * @param reportStatus
     * @return
     */
    @GetMapping(value = "/dashboard/{agency}")
    @ResponseBody
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN})
    @Operation(summary = "Get reviewer submission agency submission",
        description = "Get reviewer submission agency submission",
        tags = "Reviewer Submission")
    public ResponseEntity<List<SubmissionsReviewDashboardDto>> retrieveAgencySubmissions(
            @NotNull @PathVariable String agency,
            @RequestParam(required = false) Short reportYear,
            @RequestParam(required = false) String reportStatus) {

        List<SubmissionsReviewDashboardDto> result = submissionsReviewDasboardService.retrieveFacilityReports(reportYear, reportStatus, agency);
        return new ResponseEntity<List<SubmissionsReviewDashboardDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve submissions for the current user's SLT based on year
     * @param reportYear
     * @return
     */
    @GetMapping(value = "/dashboard/notStarted")
    @ResponseBody
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    @Operation(summary = "Get reviewer submission not started",
        description = "Get reviewer submission not started",
        tags = "Reviewer Submission")
    public ResponseEntity<List<SubmissionsReviewDashboardDto>> retrieveReviewerNotStartedSubmissions(
            @RequestParam(required = false) Short reportYear) {

        List<SubmissionsReviewDashboardDto> result = submissionsReviewDasboardService.retrieveReviewerFacilityNotStartedReports(reportYear);
        return new ResponseEntity<List<SubmissionsReviewDashboardDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve submissions based on year and agency
     * @param agency
     * @param reportYear
     * @return
     */
    @GetMapping(value = "/dashboard/notStarted/{agency}")
    @ResponseBody
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN})
    @Operation(summary = "Get reviewer submission agency not started",
        description = "Get reviewer submission agency not started",
        tags = "Reviewer Submission")
    public ResponseEntity<List<SubmissionsReviewDashboardDto>> retrieveAgencyNotStartedSubmissions(
    		@NotNull @PathVariable String agency,
            @RequestParam(required = false) Short reportYear) {

        List<SubmissionsReviewDashboardDto> result = submissionsReviewDasboardService.retrieveFacilityNotStartedReports(reportYear, agency);
        return new ResponseEntity<List<SubmissionsReviewDashboardDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve semi-annual submissions for the current user's SLT based on year and status
     * @param reportYear
     * @param midYearSubmissionStatus
     * @return
     */
    @GetMapping(value = "/dashboard/semiAnnual")
    @ResponseBody
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    @Operation(summary = "Get reviewer submission semiannual",
        description = "Get reviewer submission semiannual",
        tags = "Reviewer Submission")
    public ResponseEntity<List<SubmissionsReviewDashboardDto>> retrieveReviewerSemiAnnualSubmissions(
            @RequestParam(required = false) Short reportYear,
            @RequestParam(required = false) ReportStatus monthlyReportStatus) {

        List<SubmissionsReviewDashboardDto> result = submissionsReviewDasboardService.retrieveReviewerSemiAnnualFacilityReports(reportYear, monthlyReportStatus);
        return new ResponseEntity<List<SubmissionsReviewDashboardDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve semi-annual submissions based on year, status, and selected agency
     * @param agency
     * @param reportYear
     * @param midYearSubmissionStatus
     * @return
     */
    @GetMapping(value = "/dashboard/semiAnnual/{agency}")
    @ResponseBody
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN})
    @Operation(summary = "Get reviewer submission agency semiannual",
        description = "Get reviewer submission agency semiannual",
        tags = "Reviewer Submission")
    public ResponseEntity<List<SubmissionsReviewDashboardDto>> retrieveAgencySemiAnnualSubmissions(
            @NotNull @PathVariable String agency,
            @RequestParam(required = false) Short reportYear,
            @RequestParam(required = false) ReportStatus monthlyReportStatus) {

        List<SubmissionsReviewDashboardDto> result = submissionsReviewDasboardService.retrieveSemiAnnualFacilityReports(reportYear, monthlyReportStatus, agency);
        return new ResponseEntity<List<SubmissionsReviewDashboardDto>>(result, HttpStatus.OK);
    }
}
