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
package gov.epa.cef.web.service.impl;

import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.*;
import gov.epa.cef.web.service.ReportService;
import gov.epa.cef.web.service.UserService;
import gov.epa.cef.web.service.dto.ReportDownloadDto;
import gov.epa.cef.web.service.dto.ReportHistoryDto;
import gov.epa.cef.web.service.dto.ReportSummaryDto;
import gov.epa.cef.web.service.dto.UserDto;
import gov.epa.cef.web.service.mapper.ReportDownloadMapper;
import gov.epa.cef.web.service.mapper.ReportHistoryMapper;
import gov.epa.cef.web.service.mapper.ReportSummaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private ReportSummaryRepository reportSummaryRepo;

    @Autowired
    private ReportHistoryRepository reportHistoryRepo;

    @Autowired
    private ReportReviewRepository rrRepo;

    @Autowired
    private EmissionsReportRepository erRepo;

    @Autowired
    ReportSummaryMapper reportSummaryMapper;

    @Autowired
    ReportHistoryMapper reportHistoryMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ReportDownloadRepository downloadRepo;

    @Autowired
    private ReportDownloadMapper downloadMapper;

    /***
     * Return list of report summary records with total emissions summed per pollutant for the chosen facility and year
     * @param reportYear
     * @param facilitySiteId
     * @return
     */
    public List<ReportSummaryDto> findByReportId(Long reportId) {
        List<ReportSummary> reportSummary = reportSummaryRepo.findByReportId(reportId);
        return reportSummaryMapper.toDtoList(reportSummary);
    }

    /***
     * Return list of report history records for specified report
     * @param reportId
     * @return
     */
    public List<ReportHistoryDto> findByEmissionsReportId(Long reportId) {
        List<ReportHistory> reportHistory = reportHistoryRepo.findByEmissionsReportIdOrderByActionDate(reportId);
        return reportHistoryMapper.toDtoList(reportHistory);
    }

    /**
     * Create Report History records for specified reports
     * @param reportIds
     * @param reportAction
     * @param comments
     * @param reportAttachment
     * @param cromerrActivityId
     * @param cromerrDocumentId
     */
    public void createReportHistory(List<Long> reportIds, ReportAction reportAction, String comments, Attachment reportAttachment,
                                    String cromerrActivityId, String cromerrDocumentId) {

    	UserDto appUser = this.userService.getCurrentUser();
        String userId = appUser.getCdxUserId();
        String fullName = String.format("%s %s", appUser.getFirstName(), appUser.getLastName());

        LOGGER.debug("Current User {}", appUser);

        Set<Long> auditedIds = new HashSet<>();

        this.erRepo.findAllById(reportIds)
            .forEach(report -> {

                auditedIds.add(report.getId());

                ReportHistory rptActionLog = new ReportHistory();
                rptActionLog.setUserId(userId);
                rptActionLog.setUserRole(appUser.getRole());
                rptActionLog.setUserFullName(fullName);
                rptActionLog.setEmissionsReport(report);
                rptActionLog.setReportAction(reportAction);
                rptActionLog.setComments(comments);
                rptActionLog.setCromerrActivityId(cromerrActivityId);
                rptActionLog.setCromerrDocumentId(cromerrDocumentId);
                rptActionLog.setActionDate(new Date());

                if (reportAttachment != null) {
                	rptActionLog.setReportAttachmentId(reportAttachment.getId());
                	rptActionLog.setFileName(reportAttachment.getFileName());
                }

                if (ReportAction.REJECTED.equals(reportAction)) {
                    rrRepo.findFirstByEmissionsReportIdAndStatusOrderByVersionDesc(report.getId(), ReportReviewStatus.DRAFT)
                        .ifPresent(review -> {
                            rptActionLog.setReportReview(review);
                        });

                }

                reportHistoryRepo.save(rptActionLog);
            });

        // ensure what was passed in matches what for-looped
        // set will return false if id is already in the list, i.e. none should match
        if (reportIds.stream().anyMatch(auditedIds::add)) {

            // remove the ones we did do
            reportIds.removeAll(auditedIds);

            String msg = String.format("Emissions report(s) %s does not exist.",
                reportIds.stream().map(Object::toString).collect(Collectors.joining(", ")));

            throw new IllegalStateException(msg);
        }
    }


    /**
     * Create Report History records for specified reports
     * @param reportIds
     * @param reportAction
     * @param comments
     */
    public void createReportHistory(List<Long> reportIds, ReportAction reportAction, String comments) {

    	createReportHistory(reportIds, reportAction, comments, null, null, null);
    }

    /**
     * Create Report History records for specified reports
     * @param reportIds
     * @param reportAction
     */
    public void createReportHistory(List<Long> reportIds, ReportAction reportAction) {

        createReportHistory(reportIds, reportAction, null, null, null, null);
    }

    /**
     * Create Report History record
     * @param reportId
     * @param reportAction
     * @param reportAttachment
     */
    public void createReportHistory(Long reportId, ReportAction reportAction, String comments, Attachment reportAttachment) {

    	createReportHistory(Collections.singletonList(reportId), reportAction, comments, reportAttachment, null, null);

    }

    /**
     * Create Report History record
     * @param reportId
     * @param reportAction
     * @param comments
     */
    public void createReportHistory(Long reportId, ReportAction reportAction, String comments) {

        createReportHistory(Collections.singletonList(reportId), reportAction, comments, null, null, null);
    }

    /**
     * Create Report History record
     * @param reportId
     * @param reportAction
     */
    public void createReportHistory(Long reportId, ReportAction reportAction) {

        createReportHistory(Collections.singletonList(reportId), reportAction);
    }

    /**
     * Create Report History record with CROMERR info
     * @param reportId
     * @param reportAction
     * @param cromerrActivityId
     * @param cromerrDocumentId
     */
    public void createReportHistory(Long reportId, ReportAction reportAction, String cromerrActivityId, String cromerrDocumentId) {

        createReportHistory(Collections.singletonList(reportId), reportAction, null, null, cromerrActivityId, cromerrDocumentId);
    }

    /**
     * Update Report History record to indicate attachment was deleted
     * @param id
     * @param deleted
     */
    public void updateReportHistoryDeletedAttachment (Long id, boolean deleted) {

    	ReportHistory updateLog = reportHistoryRepo.findById(id).orElse(null);

    	updateLog.setFileDeleted(true);

    	reportHistoryRepo.save(updateLog);
    }

    /***
     * Return ReportDownloadDto for the chosen report id
     * @param reportId
     * @return
     */
    public List<ReportDownloadDto> retrieveReportDownloadDtoByReportId(Long reportId){
        List<ReportDownloadView> reportDownloadsList = downloadRepo.findByReportId(reportId);
        return downloadMapper.toDtoList(reportDownloadsList);
    }
}
