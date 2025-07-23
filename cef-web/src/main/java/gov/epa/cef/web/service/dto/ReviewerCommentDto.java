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
