/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service;

import java.util.List;

import gov.epa.cef.web.domain.ReportStatus;
import gov.epa.cef.web.domain.SubmissionsReviewNotStartedDashboardView;
import gov.epa.cef.web.service.dto.SubmissionsReviewDashboardDto;

public interface SubmissionsReviewDasboardService {

    /**
     * Retrieve submissions for the current user's SLT based on year and status
     * @param reportYear
     * @param reportStatus
     * @return
     */
    List<SubmissionsReviewDashboardDto> retrieveReviewerFacilityReports(Short reportYear, String reportStatus);

    /**
     * Retrieve submissions based on year, status, and program
     * @param reportYear
     * @param reportStatus
     * @param programSystemCode
     * @return
     */
    List<SubmissionsReviewDashboardDto> retrieveFacilityReports(Short reportYear, String reportStatus, String programSystemCode);

    /**
     * Retrieve facilities that are not PS with no reports for the current user's SLT based on year
     * @param reportYear
     * @return
     */
    List<SubmissionsReviewDashboardDto> retrieveReviewerFacilityNotStartedReports(Short reportYear);

    /**
     * Retrieve facilities that are not PS with no reports for the current user's SLT based on year and program
     * @param reportYear
     * @param programSystemCode
     * @return
     */
    List<SubmissionsReviewDashboardDto> retrieveFacilityNotStartedReports(Short reportYear, String programSystemCode);

    /**
     * Retrieve all facilities that are not PS for the current user's SLT based on year and program
     * @param reportYear
     * @param programSystemCode
     * @return
     */
    List<SubmissionsReviewDashboardDto> retrieveFacilityAllReportsWithNotStarted(Short reportYear, String programSystemCode);

    /**
     * Retrieve Semi-Annual submissions for the current user's SLT based on year and status
     * @param reportYear
     * @param midYearSubmissionStatus
     * @return
     */
    List<SubmissionsReviewDashboardDto> retrieveReviewerSemiAnnualFacilityReports(Short reportYear, ReportStatus midYearSubmissionStatus);

    /**
     * Retrieve Semi-Annual submissions based on year, status, and program
     * @param reportYear
     * @param midYearSubmissionStatus
     * @param programSystemCode
     * @return
     */
    List<SubmissionsReviewDashboardDto> retrieveSemiAnnualFacilityReports(Short reportYear, ReportStatus midYearSubmissionStatus, String programSystemCode);

    /**
     * Retrieve reports and statuses based on year and program
     * @param reportYear
     * @param programSystemCode
     * @return
     */
    List<SubmissionsReviewNotStartedDashboardView> retrieveFacilityReportingStatusReport(Short reportYear, String programSystemCode);
}
