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