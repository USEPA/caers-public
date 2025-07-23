package gov.epa.cef.web.service.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gov.epa.cef.web.domain.ReportChangeType;
import gov.epa.cef.web.domain.common.IReportComponent;

public class ReportChangeDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String message;
    private String field;
    private ReportChangeType type;
    private ValidationDetailDto details;

    @JsonIgnore
    private IReportComponent component;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public IReportComponent getComponent() {
        return component;
    }

    public void setComponent(IReportComponent component) {
        this.component = component;
    }
    
    public ReportChangeDto withMessage(String message) {
        this.message = message;
        return this;
    }
    
    public ReportChangeDto withField(String field) {
        this.field = field;
        return this;
    }
    
    public ReportChangeDto withType(ReportChangeType type) {
        this.type = type;
        return this;
    }
    
    public ReportChangeDto withDetails(ValidationDetailDto details) {
        this.details = details;
        return this;
    }
    
    public ReportChangeDto withComponent(IReportComponent component) {
        this.component = component;
        return this;
    }

}
