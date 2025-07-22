/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.domain;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import gov.epa.cef.web.domain.common.BaseAuditEntity;
import gov.epa.cef.web.domain.common.IReportComponent;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.util.ReportCreationContext;

@Entity
@Table(name = "control")
public class Control extends BaseAuditEntity implements IReportComponent {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_site_id", nullable = false)
    private FacilitySite facilitySite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_code")
    private OperatingStatusCode operatingStatusCode;
    
    @Column(name = "status_year")
    private Short statusYear;

    @Column(name = "identifier", nullable = false, length = 20)
    private String identifier;

    @Column(name = "description", length = 200)
    private String description;
    
    @Column(name = "upgrade_description", length = 200)
    private String upgradeDescription;

    @Column(name = "percent_capture", precision = 4, scale = 1)
    private BigDecimal percentCapture;

    @Column(name = "percent_control", precision = 6, scale = 3)
    private BigDecimal percentControl;
    
    @Column(name = "number_operating_months", precision = 2, scale = 0)
    private Short numberOperatingMonths;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "upgrade_date")
    private LocalDate upgradeDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "comments", length = 400)
    private String comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "control")
    private List<ControlAssignment> assignments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "control")
    private List<ControlPollutant> pollutants = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "control_measure_code", nullable = false)
    private ControlMeasureCode controlMeasureCode;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_year_status_code", nullable = true)
    private OperatingStatusCode previousYearOperatingStatusCode;

	/**
     * Default constructor
     */
    public Control() {}
    
    
    /**
     * Copy constructor (control assignment is not copied here, they are copied within the ControlPath entity)
     * @param originalControl
     */
    public Control(FacilitySite facilitySite, Control originalControl) {
        this(facilitySite, originalControl, null);
    }
    
    /**
     * Copy constructor (control assignment is not copied here, they are copied within the ControlPath entity)
     * @param originalControl
     */
    public Control(FacilitySite facilitySite, Control originalControl, ReportCreationContext context) {
    	this.id = originalControl.getId();
    	this.facilitySite = facilitySite;
    	this.operatingStatusCode = originalControl.getOperatingStatusCode();
    	this.statusYear = originalControl.getStatusYear();
    	this.identifier = originalControl.getIdentifier();
    	this.description = originalControl.getDescription();
    	this.percentControl = originalControl.getPercentControl();
    	this.upgradeDescription = originalControl.getUpgradeDescription();
    	this.numberOperatingMonths = originalControl.getNumberOperatingMonths();
    	this.startDate = originalControl.getStartDate();
    	this.upgradeDate = originalControl.getUpgradeDate();
    	this.endDate = originalControl.getEndDate();
    	this.comments = originalControl.getComments();
    	for (ControlPollutant pollutant : originalControl.getPollutants()) {
    		this.pollutants.add(new ControlPollutant(this, pollutant));
    	}
    	this.controlMeasureCode = originalControl.getControlMeasureCode();
    }
    
    public FacilitySite getFacilitySite() {
        return facilitySite;
    }

    public void setFacilitySite(FacilitySite facilitySite) {
        this.facilitySite = facilitySite;
    }

    public OperatingStatusCode getOperatingStatusCode() {
        return operatingStatusCode;
    }

    public void setOperatingStatusCode(OperatingStatusCode operatingStatusCode) {
        this.operatingStatusCode = operatingStatusCode;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPercentCapture() {
        return percentCapture;
    }

    public void setPercentCapture(BigDecimal percentCapture) {
        this.percentCapture = percentCapture;
    }

    public BigDecimal getPercentControl() {
        return percentControl;
    }

    public void setPercentControl(BigDecimal percentControl) {
        this.percentControl = percentControl;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<ControlAssignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<ControlAssignment> assignments) {

        this.assignments.clear();
        if (assignments != null) {
            this.assignments.addAll(assignments);
        }
    }

    public List<ControlPollutant> getPollutants() {
        return pollutants;
    }

    public void setPollutants(List<ControlPollutant> pollutants) {

        this.pollutants.clear();
        if (pollutants != null) {
            this.pollutants.addAll(pollutants);
        }
    }

    public ControlMeasureCode getControlMeasureCode() {
        return controlMeasureCode;
    }

    public void setControlMeasureCode(ControlMeasureCode controlMeasureCode) {
        this.controlMeasureCode = controlMeasureCode;
    }
    
    public String getUpgradeDescription() {
		return upgradeDescription;
	}

	public void setUpgradeDescription(String upgradeDescription) {
		this.upgradeDescription = upgradeDescription;
	}

	public Short getNumberOperatingMonths() {
		return numberOperatingMonths;
	}

	public void setNumberOperatingMonths(Short numberOperatingMonths) {
		this.numberOperatingMonths = numberOperatingMonths;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getUpgradeDate() {
		return upgradeDate;
	}

	public void setUpgradeDate(LocalDate upgradeDate) {
		this.upgradeDate = upgradeDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Short getStatusYear() {
		return statusYear;
	}

	public void setStatusYear(Short statusYear) {
		this.statusYear = statusYear;
	}
	
    public OperatingStatusCode getPreviousYearOperatingStatusCode() {
		return previousYearOperatingStatusCode;
	}

	public void setPreviousYearOperatingStatusCode(OperatingStatusCode previousYearOperatingStatusCode) {
		this.previousYearOperatingStatusCode = previousYearOperatingStatusCode;
	}

	/***
     * Set the id property to null for this object and the id for it's direct children.  This method is useful to INSERT the updated object instead of UPDATE.
     */
    public void clearId() {
    	this.id = null;

    	for (ControlAssignment assignment : this.assignments) {
    		assignment.clearId();
    	}
    	
    	for (ControlPollutant pollutant : this.pollutants) {
    		pollutant.clearId();
    	}
    }


    @Override
    public ValidationDetailDto getComponentDetails() {

        String description = MessageFormat.format("Control: {0}", getIdentifier());

        ValidationDetailDto dto = new ValidationDetailDto(getId(), getIdentifier(), EntityType.CONTROL, description);
        return dto;
    }

}
