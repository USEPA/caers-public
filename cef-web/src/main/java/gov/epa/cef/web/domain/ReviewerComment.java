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
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "reviewer_comment")
public class ReviewerComment extends BaseAuditEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_review_id", nullable = false)
    private ReportReview reportReview;

    @Column(name = "message", length = 2000)
    private String message;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false)
    private EntityType entityType;

    @Type(type = "json")
    @Column(name = "details", columnDefinition = "jsonb")
    private ValidationDetailDto details;

    public ReviewerComment() {}

    public ReviewerComment(ReportReview reportReview, String message, ValidationDetailDto details) {
        this.reportReview = reportReview;
        this.message = message;
        this.details = details;
        if (details != null) {
            this.entityId = details.getId();
            this.entityType = details.getType();
        }
    }

    public ReportReview getReportReview() {
        return reportReview;
    }

    public void setReportReview(ReportReview reportReview) {
        this.reportReview = reportReview;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ValidationDetailDto getDetails() {
        return details;
    }

    public void setDetails(ValidationDetailDto details) {
        this.details = details;
    }
}
