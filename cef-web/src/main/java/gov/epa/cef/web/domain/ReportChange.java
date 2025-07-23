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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import gov.epa.cef.web.domain.common.BaseAuditEntity;
import gov.epa.cef.web.service.dto.ValidationDetailDto;

@Entity
@Table(name = "report_change")
public class ReportChange extends BaseAuditEntity {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "report_id", nullable = false)
	private EmissionsReport emissionsReport;

	@Column(name = "message", length = 2000)
	private String message;
	
	@Column(name = "field", length = 255)
	private String field;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
    private ReportChangeType type;

	@Type(type = "json")
	@Column(name = "details", columnDefinition = "jsonb")
	private ValidationDetailDto details;
	
	public ReportChange() {}
	
	public ReportChange(EmissionsReport emissionsReport, String message, String field, ReportChangeType type, ValidationDetailDto details) {
	    this.emissionsReport = emissionsReport;
	    this.message = message;
	    this.field = field;
	    this.type = type;
	    this.details = details;
	}
	
	public EmissionsReport getEmissionsReport() {
		return emissionsReport;
	}

	public void setEmissionsReport(EmissionsReport emissionsReport) {
		this.emissionsReport = emissionsReport;
	}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public ReportChangeType getType() {
        return type;
    }

    public void setType(ReportChangeType type) {
        this.type = type;
    }

    public ValidationDetailDto getDetails() {
        return details;
    }

    public void setDetails(ValidationDetailDto details) {
        this.details = details;
    }

}
