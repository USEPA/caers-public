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
