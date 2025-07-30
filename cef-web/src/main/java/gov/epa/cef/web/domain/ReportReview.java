/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package gov.epa.cef.web.domain;

import gov.epa.cef.web.domain.common.BaseAuditEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "report_review")
public class ReportReview extends BaseAuditEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private EmissionsReport emissionsReport;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "reportReview")
    private List<ReviewerComment> reviewerComments = new ArrayList<>();

    @Column(name = "version", nullable = false)
    private Integer version;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReportReviewStatus status;

    public ReportReview() {
    }

    public ReportReview(EmissionsReport emissionsReport, Integer version, ReportReviewStatus status) {
        this.emissionsReport = emissionsReport;
        this.version = version;
        this.status = status;
    }

    public EmissionsReport getEmissionsReport() {
        return emissionsReport;
    }

    public void setEmissionsReport(EmissionsReport emissionsReport) {
        this.emissionsReport = emissionsReport;
    }

    public List<ReviewerComment> getReviewerComments() {
        return reviewerComments;
    }

    public void setReviewerComments(List<ReviewerComment> reviewerComments) {
        this.reviewerComments = reviewerComments;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ReportReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReportReviewStatus status) {
        this.status = status;
    }
}
