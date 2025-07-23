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
package gov.epa.cef.web.service.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ReportHistoryDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long emissionsReportId;
	private String userId;
	private String userFullName;
	private Date actionDate;
	private String reportAction;
	private String comments;
	private Long reportAttachmentId;
	private String userRole;
	private String fileName;
	private boolean fileDeleted;
    private String cromerrActivityId;
    private String cromerrDocumentId;
    private List<ReviewerCommentDto> reviewerComments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public String getReportAction() {
		return reportAction;
	}

	public void setReportAction(String reportAction) {
		this.reportAction = reportAction;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getEmissionsReportId() {
		return emissionsReportId;
	}

	public void setEmissionsReportId(Long emissionsReportId) {
		this.emissionsReportId = emissionsReportId;
	}

	public Long getReportAttachmentId() {
		return reportAttachmentId;
	}

	public void setReportAttachmentId(Long reportAttachmentId) {
		this.reportAttachmentId = reportAttachmentId;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserRole() {
		return userRole;
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

    public List<ReviewerCommentDto> getReviewerComments() {
        return reviewerComments;
    }

    public void setReviewerComments(List<ReviewerCommentDto> reviewerComments) {
        this.reviewerComments = reviewerComments;
    }
}
