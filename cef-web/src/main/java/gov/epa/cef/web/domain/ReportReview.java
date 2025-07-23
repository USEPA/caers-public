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
