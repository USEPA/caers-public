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
import gov.epa.cef.web.repository.ReportIdRetriever;
import gov.epa.cef.web.service.dto.EmissionsReportDto;

public interface EmissionsReportStatusService {

	/**
     * Begin Advanced QA for the specified reports, move from Submitted to Advanced QA
     * @param reportIds
     * @return
     */
    List<EmissionsReportDto> advancedQAEmissionsReports(List<Long> reportIds);

    /**
     * Approve the specified reports and move to approved
     * @param reportIds
     * @return
     */
    List<EmissionsReportDto> acceptEmissionsReports(List<Long> reportIds);

    /**
     * Reject the specified reports. Sets report status to in progress and validation status to unvalidated.
     * @param reportIds
     * @return
     */
    List<EmissionsReportDto> rejectEmissionsReports(List<Long> reportIds);

    /**
     * Reset report status. Sets report status to in progress and validation status to unvalidated.
     * @param reportIds
     * @return
     */
    List<EmissionsReportDto> resetEmissionsReport(List<Long> reportIds);

    /**
     * Find the report for an entity using the provided repository class and reset the report status.
     * Sets report status to in progress and validation status to unvalidated.
     * @param entityIds
     * @param repoClazz
     * @return
     */
    <T extends ReportIdRetriever> List<EmissionsReportDto> resetEmissionsReportForEntity(List<Long> entityIds,
            Class<T> repoClazz);

    /**
     * Reject the specified semi-annual reports. Sets mid year submission status to returned.
     * @param reportIds
     * @return
     */
    public List<EmissionsReportDto> rejectSemiAnnualReports(List<Long> reportIds);

    /**
     * Approve the specified semi-annual reports. Sets mid year submission status to approved.
     * @param reportIds
     * @return
     */
    public List<EmissionsReportDto> acceptSemiAnnualReports(List<Long> reportIds);
}
