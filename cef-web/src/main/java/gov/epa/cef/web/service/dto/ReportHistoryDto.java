/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
