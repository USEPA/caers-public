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
package gov.epa.cef.web.domain;

import java.util.Date;

import javax.persistence.*;

import gov.epa.cef.web.domain.common.BaseAuditEntity;

/**
 * ReportHistory entity
 */
@Entity
@Table(name = "report_history")
public class ReportHistory extends BaseAuditEntity {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "report_id", nullable = false)
	private EmissionsReport emissionsReport;

	@Enumerated(EnumType.STRING)
	@Column(name = "action", nullable = false)
	private ReportAction reportAction;

	@Column(name = "action_date", nullable = false)
	protected Date actionDate;

	@Column(name = "user_id", nullable = false)
	private String userId;

	@Column(name = "user_full_name", nullable = false)
	private String userFullName;

	@Column(name = "user_role")
	private String userRole;

	@Column(name = "comments", length = 2000)
	private String comments;

	@Column(name = "attachment_id")
	private Long reportAttachmentId;

	@Column(name = "file_name", length = 255)
	private String fileName;

	@Column(name = "file_deleted")
	private boolean fileDeleted;

	@Column(name = "cromerr_activity_id", length = 255)
    private String cromerrActivityId;

	@Column(name = "cromerr_document_id", length = 255)
    private String cromerrDocumentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_review_id")
    private ReportReview reportReview;

    /***
     * Default constructor
     */
    public ReportHistory() { }

    /***
     * Copy constructor
     * Note: this constructor is not and should not be used by the report copy constructor
     * @param emissionsReport The emissions report object that this facility site should be associated with
     * @param originalReportHistory The facility site object being copied
     */
    public ReportHistory(EmissionsReport emissionsReport, ReportHistory originalReportHistory) {
        this.id = originalReportHistory.getId();
        this.emissionsReport = emissionsReport;
        this.reportAction = originalReportHistory.getReportAction();
        this.actionDate = originalReportHistory.getActionDate();
        this.userId = originalReportHistory.getUserId();
        this.userFullName = originalReportHistory.getUserFullName();
        this.userRole = originalReportHistory.getUserRole();
        this.comments = originalReportHistory.getComments();
        this.reportAttachmentId = originalReportHistory.getReportAttachmentId();
        this.fileName = originalReportHistory.getFileName();
        this.fileDeleted = originalReportHistory.isFileDeleted();
        this.cromerrActivityId = originalReportHistory.getCromerrActivityId();
        this.cromerrDocumentId = originalReportHistory.getCromerrDocumentId();
        this.reportReview = originalReportHistory.getReportReview();
    }

    /***
     * Set the id property to null for this object.  This method is useful to INSERT the updated object instead of UPDATE.
     */
    public void clearId() {
        this.id = null;
    }

    public EmissionsReport getEmissionsReport() {
		return emissionsReport;
	}

	public void setEmissionsReport(EmissionsReport emissionsReport) {
		this.emissionsReport = emissionsReport;
	}

	public ReportAction getReportAction() {
		return reportAction;
	}

	public void setReportAction(ReportAction reportAction) {
		this.reportAction = reportAction;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getReportAttachmentId() {
		return reportAttachmentId;
	}

	public void setReportAttachmentId(Long reportAttachmentId) {
		this.reportAttachmentId = reportAttachmentId;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isFileDeleted() {
		return fileDeleted;
	}

	public void setFileDeleted(boolean fileDeleted) {
		this.fileDeleted = fileDeleted;
	}

    public String getCromerrActivityId() {
        return cromerrActivityId;
    }

    public void setCromerrActivityId(String cromerrActivityId) {
        this.cromerrActivityId = cromerrActivityId;
    }

    public String getCromerrDocumentId() {
        return cromerrDocumentId;
    }

    public void setCromerrDocumentId(String cromerrDocumentId) {
        this.cromerrDocumentId = cromerrDocumentId;
    }

    public ReportReview getReportReview() {
        return reportReview;
    }

    public void setReportReview(ReportReview reportReview) {
        this.reportReview = reportReview;
    }
}
