/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
