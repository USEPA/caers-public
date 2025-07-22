/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.domain;

import gov.epa.cef.web.domain.common.BaseAuditEntity;
import gov.epa.cef.web.service.dto.EisSubmissionStatus;
import gov.epa.cef.web.util.ReportCreationContext;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Date;

/**
 * EmissionsReport entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "emissions_report")
public class EmissionsReport extends BaseAuditEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_system_code")
    private ProgramSystemCode programSystemCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_facility_id")
    private MasterFacilityRecord masterFacilityRecord;

    @Column(name = "cromerr_activity_id", length = 37)
    private String cromerrActivityId;

    @Column(name = "cromerr_document_id", length = 36)
    private String cromerrDocumentId;

    @Column(name = "eis_comments")
    private String eisComments;

    @Column(name = "eis_is_passed")
    private boolean eisPassed;

    @Enumerated(EnumType.STRING)
    @Column(name = "eis_last_sub_status")
    private EisSubmissionStatus eisLastSubmissionStatus;

    @Column(name = "eis_last_trans_id")
    private String eisLastTransactionId;

    @Column(name = "eis_program_id", length = 22)
    private String eisProgramId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "emissionsReport")
    private List<FacilitySite> facilitySites = new ArrayList<>();

    @Column(name = "has_submitted")
    private Boolean hasSubmitted;

    @Column(name = "returned_report")
    private Boolean returnedReport;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "emissionsReport")
    private List<UserFeedback> userFeedback = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "emissionsReport")
    private List<Attachment> reportAttachments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "emissionsReport")
    private List<ReportHistory> reportHistory = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "emissionsReport")
    private List<ReportReview> reportReviews = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "emissionsReport")
    private List<ReportChange> reportCreationChanges = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "emissionsReport")
    private List<QualityCheckResults> qualityCheckResults = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReportStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "validation_status")
    private ValidationStatus validationStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "threshold_status")
    private ThresholdStatus thresholdStatus;

    @Column(name = "year", nullable = false)
    private Short year;

    @Enumerated(EnumType.STRING)
    @Column(name = "mid_year_sub_status")
    private ReportStatus midYearSubmissionStatus;

    @Column(name = "max_qa_errors", nullable = false)
    private Integer maxQaErrors;

    @Column(name = "max_qa_warnings", nullable = false)
    private Integer maxQaWarnings;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    /***
     * Default constructor
     */
    public EmissionsReport() {

    }

    /***
     * Copy constructor
     * @param originalEmissionsReport The emissions report object being copied
     */
    public EmissionsReport(EmissionsReport originalEmissionsReport) {
        this(originalEmissionsReport, null);
    }

    /***
     * Copy constructor
     * @param originalEmissionsReport The emissions report object being copied
     * @param context context for tracking report creation changes
     */
    public EmissionsReport(EmissionsReport originalEmissionsReport, ReportCreationContext context) {

        this.id = originalEmissionsReport.getId();
        this.masterFacilityRecord = originalEmissionsReport.getMasterFacilityRecord();
        this.eisProgramId = originalEmissionsReport.eisProgramId;
        this.programSystemCode = originalEmissionsReport.programSystemCode;
        this.year = originalEmissionsReport.year;
        this.status = originalEmissionsReport.status;
        this.thresholdStatus = originalEmissionsReport.thresholdStatus;
        this.validationStatus = originalEmissionsReport.validationStatus;
        for (FacilitySite facilitySite : originalEmissionsReport.facilitySites) {
            this.facilitySites.add(new FacilitySite(this, facilitySite, context));
        }

        this.eisComments = originalEmissionsReport.eisComments;
        this.eisLastSubmissionStatus = originalEmissionsReport.eisLastSubmissionStatus;
        this.eisLastTransactionId = originalEmissionsReport.eisLastTransactionId;
        this.eisPassed = originalEmissionsReport.eisPassed;
        this.isDeleted = false;
    }

    /***
     * Set the id property to null for this object and the id for it's direct children.  This method is useful to INSERT the updated object instead of UPDATE.
     */
    public void clearId() {

        this.id = null;
        for (FacilitySite facilitySite : this.facilitySites) {
            facilitySite.clearId();
        }
    }

    public ProgramSystemCode getProgramSystemCode() {

        return this.programSystemCode;
    }

    public void setProgramSystemCode(ProgramSystemCode programSystemCode) {

        this.programSystemCode = programSystemCode;
    }

    public MasterFacilityRecord getMasterFacilityRecord() {
        return masterFacilityRecord;
    }

    public void setMasterFacilityRecord(MasterFacilityRecord masterFacilityRecord) {
        this.masterFacilityRecord = masterFacilityRecord;
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

    public String getEisComments() {

        return eisComments;
    }

    public void setEisComments(String eisComments) {

        this.eisComments = eisComments;
    }

    public EisSubmissionStatus getEisLastSubmissionStatus() {

        return eisLastSubmissionStatus;
    }

    public void setEisLastSubmissionStatus(EisSubmissionStatus eisLastSubmissionStatus) {

        this.eisLastSubmissionStatus = eisLastSubmissionStatus;
    }

    public String getEisLastTransactionId() {

        return eisLastTransactionId;
    }

    public void setEisLastTransactionId(String eisLastTransactionId) {

        this.eisLastTransactionId = eisLastTransactionId;
    }

    /**
     * @deprecated
     * Use the getter method in MasterFacilityRecord instead
     */
    @Deprecated
    public String getEisProgramId() {

        return this.eisProgramId;
    }

    /**
     * @deprecated
     * Use the setter method in MasterFacilityRecord instead
     */
    @Deprecated
    public void setEisProgramId(String eisProgramId) {

        this.eisProgramId = eisProgramId;
    }

    public List<FacilitySite> getFacilitySites() {

        return this.facilitySites;
    }

    public void setFacilitySites(Collection<FacilitySite> facilitySites) {

        this.facilitySites.clear();
        if (facilitySites != null) {
            this.facilitySites.addAll(facilitySites);
        }
    }

    public Boolean getHasSubmitted() {

        return hasSubmitted;
    }

    public void setHasSubmitted(Boolean hasSubmitted) {

        this.hasSubmitted = hasSubmitted;
    }

	public Boolean isReturnedReport() {
		return returnedReport;
	}

	public void setReturnedReport(Boolean returnedReport) {
		this.returnedReport = returnedReport;
	}

    public List<UserFeedback> getUserFeedback() {
		return userFeedback;
	}

	public void setUserFeedback(List<UserFeedback> userFeedback) {
		this.userFeedback = userFeedback;
	}

	public List<Attachment> getReportAttachments() {

        return this.reportAttachments;
    }

    public void setReportAttachments(List<Attachment> reportAttachments) {

        this.reportAttachments.clear();
        if (reportAttachments != null) {
            this.reportAttachments.addAll(reportAttachments);
        }
    }

    public List<ReportHistory> getReportHistory() {

        return this.reportHistory;
    }

    public void setReportHistory(List<ReportHistory> reportHistory) {

        this.reportHistory.clear();
        if (reportHistory != null) {
            this.reportHistory.addAll(reportHistory);
        }
    }

    public List<ReportReview> getReportReviews() {
        return reportReviews;
    }

    public void setReportReviews(List<ReportReview> reportReviews) {
        this.reportReviews.clear();
        if (reportReviews != null) {
            this.reportReviews.addAll(reportReviews);
        }
    }

    public List<ReportChange> getReportCreationChanges() {
        return reportCreationChanges;
    }

    public void setReportCreationChanges(List<ReportChange> reportCreationLog) {

        this.reportCreationChanges.clear();
        if (reportCreationLog != null) {
            this.reportCreationChanges.addAll(reportCreationLog);
        }
    }

    public List<QualityCheckResults> getQualityCheckResults() {
        return qualityCheckResults;
    }

    public void setQualityCheckResults(List<QualityCheckResults> qualityCheckResults) {
        this.qualityCheckResults.clear();
        if (qualityCheckResults != null) {
            this.qualityCheckResults.addAll(qualityCheckResults);
        }

    }

    public ReportStatus getStatus() {

        return this.status;
    }

    public void setStatus(ReportStatus status) {

        this.status = status;
    }

    public ValidationStatus getValidationStatus() {

        return this.validationStatus;
    }

    public void setValidationStatus(ValidationStatus validationStatus) {

        this.validationStatus = validationStatus;
    }

    public ThresholdStatus getThresholdStatus() {
        return thresholdStatus;
    }

    public void setThresholdStatus(ThresholdStatus thresholdStatus) {
        this.thresholdStatus = thresholdStatus;
    }

    public ReportStatus getMidYearSubmissionStatus() {
		return midYearSubmissionStatus;
	}

	public void setMidYearSubmissionStatus(ReportStatus midYearSubmissionStatus) {
		this.midYearSubmissionStatus = midYearSubmissionStatus;
	}

	public Short getYear() {

        return this.year;
    }

    public void setYear(Short year) {

        this.year = year;
    }

    public boolean isEisPassed() {

        return eisPassed;
    }

    public void setEisPassed(boolean eisPassed) {

        this.eisPassed = eisPassed;
    }

    public Integer getMaxQaErrors() {
        return maxQaErrors;
    }

    public void setMaxQaErrors(Integer max_qa_errors) {
        this.maxQaErrors = max_qa_errors;
    }

    public Integer getMaxQaWarnings() {
        return maxQaWarnings;
    }

    public void setMaxQaWarnings(Integer max_qa_warnings) {
        this.maxQaWarnings = max_qa_warnings;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }


    /***
     * Iterate the list of report history records associated with this emissions report
     * If the report action is 'submitted' then return the action date
     * If 'submitted' is not one of the report actions then return null
     */
    public Date getCertificationDate() {

        for (ReportHistory rh : this.getReportHistory()) {
            if (rh.getReportAction() == ReportAction.SUBMITTED) {
                return rh.getActionDate();
            }
        }
        return null;
    }
}
