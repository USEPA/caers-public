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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;

import gov.epa.cef.web.domain.common.BaseAuditEntity;
import gov.epa.cef.web.domain.common.IReportComponent;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.util.ConstantUtils;
import gov.epa.cef.web.util.ReportCreationContext;

import static gov.epa.cef.web.util.ConstantUtils.DEFAULT_SCC_CATEGORY;

/**
 * EmissionsProcess entity
 */
@Entity
@Table(name = "emissions_process")
public class EmissionsProcess extends BaseAuditEntity implements IReportComponent {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emissions_unit_id", nullable = false)
    private EmissionsUnit emissionsUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_engine_type_code")
    private AircraftEngineTypeCode aircraftEngineTypeCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_code", nullable = false)
    private OperatingStatusCode operatingStatusCode;

    @Column(name = "emissions_process_identifier", nullable = false, length = 20)
    private String emissionsProcessIdentifier;

    @Column(name = "status_year")
    private Short statusYear;

    @Column(name = "scc_code", nullable = false, length = 20)
    private String sccCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "scc_category", nullable = false)
    private SccCategory sccCategory;

    @Column(name = "scc_description", length = 500)
    private String sccDescription;

    @Column(name = "scc_short_name", length = 100)
    private String sccShortName;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "comments", length = 400)
    private String comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_year_status_code", nullable = true)
    private OperatingStatusCode previousYearOperatingStatusCode;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "emissionsProcess")
    private List<ReleasePointAppt> releasePointAppts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "emissionsProcess")
    private List<ReportingPeriod> reportingPeriods = new ArrayList<>();

    @Column(name = "initial_monthly_reporting_period", length = 10)
    private String initialMonthlyReportingPeriod;

    @Column(name = "slt_billing_exempt")
    private Boolean sltBillingExempt;

    /***
     * Default constructor
     */
    public EmissionsProcess() {}


    /***
     * Copy constructor
     * @param unit The emissions unit object that this process should be associated with
     * @param originalProcess The process object being copied
     */
    public EmissionsProcess(EmissionsUnit unit, EmissionsProcess originalProcess) {
        this(unit, originalProcess, null);
    }

    /***
     * Copy constructor
     * @param unit The emissions unit object that this process should be associated with
     * @param originalProcess The process object being copied
     * @param context context for tracking report creation changes
     */
    public EmissionsProcess(EmissionsUnit unit, EmissionsProcess originalProcess, ReportCreationContext context) {
		this.id = originalProcess.getId();
        this.emissionsUnit = unit;
        this.aircraftEngineTypeCode = originalProcess.getAircraftEngineTypeCode();
        this.operatingStatusCode = originalProcess.getOperatingStatusCode();
        this.emissionsProcessIdentifier = originalProcess.getEmissionsProcessIdentifier();
        this.statusYear = originalProcess.getStatusYear();
        this.sccCode = originalProcess.getSccCode();
        this.sccCategory = originalProcess.getSccCategory();
        this.sccDescription = originalProcess.getSccDescription();
        this.sccShortName = originalProcess.getSccShortName();
        this.description = originalProcess.getDescription();
        this.comments = originalProcess.getComments();
        this.initialMonthlyReportingPeriod = ConstantUtils.JANUARY;

        for (ReleasePointAppt originalApportionment : originalProcess.getReleasePointAppts()) {
        	ReleasePoint rp = null;
        	for(ReleasePoint newReleasePoint : this.emissionsUnit.getFacilitySite().getReleasePoints()) {
        		if (newReleasePoint.getId() != null && newReleasePoint.getId().equals(originalApportionment.getReleasePoint().getId())) {
        			rp = newReleasePoint;
        			break;
        		}
        	}

        	ControlPath cp = null;
        	if (originalApportionment.getControlPath() != null) {
            	for(ControlPath newControlPath : this.emissionsUnit.getFacilitySite().getControlPaths()) {
            		if (newControlPath.getId() != null && newControlPath.getId().equals(originalApportionment.getControlPath().getId())) {
            			cp = newControlPath;
            			break;
            		}
            	}
        	}
        	if (!originalApportionment.getReleasePoint().getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN)) {
        		this.releasePointAppts.add(new ReleasePointAppt(rp, this, cp, originalApportionment));
        	}
        }

        for (ReportingPeriod reportingPeriod : originalProcess.getReportingPeriods()) {
            // only copy over the annual reporting period
            if ("A".equals(reportingPeriod.getReportingPeriodTypeCode().getCode())) {
                this.reportingPeriods.add(new ReportingPeriod(this, reportingPeriod, context));
            }
        }
    }

    public EmissionsUnit getEmissionsUnit() {
        return this.emissionsUnit;
    }
    public void setEmissionsUnit(EmissionsUnit emissionsUnit) {
        this.emissionsUnit = emissionsUnit;
    }

    public AircraftEngineTypeCode getAircraftEngineTypeCode() {
        return aircraftEngineTypeCode;
    }

    public void setAircraftEngineTypeCode(AircraftEngineTypeCode aircraftEngineTypeCode) {
        this.aircraftEngineTypeCode = aircraftEngineTypeCode;
    }

    public OperatingStatusCode getOperatingStatusCode() {
        return this.operatingStatusCode;
    }
    public void setOperatingStatusCode(OperatingStatusCode operatingStatusCode) {
        this.operatingStatusCode = operatingStatusCode;
    }

    public String getEmissionsProcessIdentifier() {
        return this.emissionsProcessIdentifier;
    }
    public void setEmissionsProcessIdentifier(String emissionsProcessIdentifier) {
        this.emissionsProcessIdentifier = emissionsProcessIdentifier;
    }

    public Short getStatusYear() {
        return this.statusYear;
    }
    public void setStatusYear(Short statusYear) {
        this.statusYear = statusYear;
    }

    public String getSccCode() {
        return this.sccCode;
    }
    public void setSccCode(String sccCode) {
        this.sccCode = sccCode;
    }

    public SccCategory getSccCategory() {
        return sccCategory;
    }

    public void setSccCategory(SccCategory sccCategory) {
        if (sccCategory != null) {
            this.sccCategory = sccCategory;
        } else {
            this.sccCategory = DEFAULT_SCC_CATEGORY;
        }
    }

    public String getSccDescription() {
        return sccDescription;
    }

    public void setSccDescription(String sccDescription) {
        this.sccDescription = sccDescription;
    }

    public String getSccShortName() {
        return this.sccShortName;
    }
    public void setSccShortName(String sccShortName) {
        this.sccShortName = sccShortName;
    }

    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public OperatingStatusCode getPreviousYearOperatingStatusCode() {
		return previousYearOperatingStatusCode;
	}

	public void setPreviousYearOperatingStatusCode(OperatingStatusCode previousYearOperatingStatusCode) {
		this.previousYearOperatingStatusCode = previousYearOperatingStatusCode;
	}

    public List<ReleasePointAppt> getReleasePointAppts() {
        return this.releasePointAppts;
    }

    public void setReleasePointAppts(List<ReleasePointAppt> releasePointAppts) {

        this.releasePointAppts.clear();
        if (releasePointAppts != null) {
            this.releasePointAppts.addAll(releasePointAppts);
        }
    }

    public List<ReportingPeriod> getReportingPeriods() {
        return this.reportingPeriods;
    }

    public void setReportingPeriods(List<ReportingPeriod> reportingPeriods) {

        this.reportingPeriods.clear();
        if (reportingPeriods != null) {
            this.reportingPeriods.addAll(reportingPeriods);
        }
    }

    public List<ReportingPeriod> getAnnualReportingPeriods() {

        return this.reportingPeriods.stream().filter(rp -> {
            return "A".equals(rp.getReportingPeriodTypeCode().getCode());
        }).collect(Collectors.toList());
    }

    public String getInitialMonthlyReportingPeriod() {
        return initialMonthlyReportingPeriod;
    }

    public void setInitialMonthlyReportingPeriod(String initialMonthlyReportingPeriod) {
        this.initialMonthlyReportingPeriod = initialMonthlyReportingPeriod;
    }

    public Boolean getSltBillingExempt() {
        return sltBillingExempt;
    }

    public void setSltBillingExempt(Boolean sltBillingExempt) {
        this.sltBillingExempt = sltBillingExempt;
    }

    /***
     * Set the id property to null for this object and the id for it's direct children.  This method is useful to INSERT the updated object instead of UPDATE.
     */
    public void clearId() {
    	this.id = null;

		for (ReleasePointAppt releasePointAppt : this.releasePointAppts) {
			releasePointAppt.clearId();
		}
		for (ReportingPeriod reportingPeriod : this.reportingPeriods) {
			reportingPeriod.clearId();
		}
    }

    @Override
    public ValidationDetailDto getComponentDetails() {

        ValidationDetailDto parent = getEmissionsUnit() != null ? getEmissionsUnit().getComponentDetails() : null;

        String description = MessageFormat.format("{0}, Emission Process: {1}",
                parent != null ? parent.getDescription() : "",
                getEmissionsProcessIdentifier());

        ValidationDetailDto dto = new ValidationDetailDto(getId(), getEmissionsProcessIdentifier(), EntityType.EMISSIONS_PROCESS, description).withParent(parent);

        return dto;

    }

    public Boolean getIsBillable() {
        if (this.sltBillingExempt != null) {
            return !this.sltBillingExempt;
        }
        else {
            return null;
        }
    }
}
