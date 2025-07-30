/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
