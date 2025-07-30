/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package gov.epa.cef.web.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gov.epa.cef.web.domain.ReportReviewStatus;
import gov.epa.cef.web.domain.common.IReportComponent;

import java.io.Serializable;

public class ReviewerCommentDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long emissionsReportId;
    private ReportReviewStatus status;
    private Integer version;
    private String message;
    private Long entityId;
    private EntityType entityType;
    private ValidationDetailDto details;

    @JsonIgnore
    private IReportComponent component;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmissionsReportId() {
        return emissionsReportId;
    }

    public void setEmissionsReportId(Long emissionsReportId) {
        this.emissionsReportId = emissionsReportId;
    }

    public ReportReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReportReviewStatus status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public ValidationDetailDto getDetails() {
        return details;
    }

    public void setDetails(ValidationDetailDto details) {
        this.details = details;
    }

    public IReportComponent getComponent() {
        return component;
    }

    public void setComponent(IReportComponent component) {
        this.component = component;
    }

    public ReviewerCommentDto withMessage(String message) {
        this.message = message;
        return this;
    }

    public ReviewerCommentDto withType(EntityType entityType) {
        this.entityType = entityType;
        return this;
    }

    public ReviewerCommentDto withDetails(ValidationDetailDto details) {
        this.details = details;
        return this;
    }

    public ReviewerCommentDto withComponent(IReportComponent component) {
        this.component = component;
        return this;
    }

}
