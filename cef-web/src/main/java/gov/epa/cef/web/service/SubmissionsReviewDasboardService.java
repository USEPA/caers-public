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
