/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service;

import java.util.List;

import gov.epa.cef.web.domain.ReportAction;
import gov.epa.cef.web.domain.Attachment;
import gov.epa.cef.web.service.dto.ReportDownloadDto;
import gov.epa.cef.web.service.dto.ReportHistoryDto;
import gov.epa.cef.web.service.dto.ReportSummaryDto;

public interface ReportService {

    /***
     * Return list of report summary records with total emissions summed per pollutant for the chosen report
     * @param reportId
     * @return
     */
    List<ReportSummaryDto> findByReportId(Long reportId);
    
    /***
     * Return list of report history records for the chosen report id
     * @param reportId
     * @return
     */
    List<ReportHistoryDto> findByEmissionsReportId(Long reportId);
    
    /**
     * Create Report History record 
     * @param reportIds
     * @param reportAction
     * @param comments
     * @param reportAttachment
     * @param cromerrActivityId
     * @param cromerrDocumentId
     */
    void createReportHistory(List<Long> reportIds, ReportAction reportAction, String comments, Attachment reportAttachment, String cromerrActivityId, String cromerrDocumentId);
    
    /**
     * Create Report History record 
     * @param userIds
     * @param reportAction
     * @param comments
     */
    void createReportHistory(List<Long> reportIds, ReportAction reportAction, String comments);
    
    /**
     * Create Report History record 
     * @param userIds
     * @param reportAction
     */
    void createReportHistory(List<Long> reportIds, ReportAction reportAction);
    
    /**
     * Create Report History record
     * @param reportId
     * @param reportAction
     * @param comments
     * @param reportAttachment
     */
    void createReportHistory(Long reportId, ReportAction reportAction, String comments, Attachment reportAttachment);

    /**
     * Create Report History record 
     * @param userId
     * @param reportAction
     * @param comments
     */
    void createReportHistory(Long reportId, ReportAction reportAction, String comments);
    
    /**
     * Create Report History record 
     * @param userId
     * @param reportAction
     */
    void createReportHistory(Long reportId, ReportAction reportAction);
    
    /**
     * Create Report History record with CROMERR info
     * @param reportId
     * @param reportAction
     * @param cromerrActivityId
     * @param cromerrDocumentId
     */
    void createReportHistory(Long reportId, ReportAction reportAction, String cromerrActivityId, String cromerrDocumentId);
    
    /**
     * Update Report History record to indicate attachment was deleted
     * @param id
     * @param deleted
     */
    void updateReportHistoryDeletedAttachment(Long id, boolean deleted);
    
    /***
     * Return ReportDownloadDto for the chosen report id
     * @param reportId
     * @return
     */
    List<ReportDownloadDto> retrieveReportDownloadDtoByReportId(Long reportId);

}