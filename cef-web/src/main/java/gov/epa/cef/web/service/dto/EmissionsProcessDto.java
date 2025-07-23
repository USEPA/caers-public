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

import gov.epa.cef.web.domain.SccCategory;

import java.io.Serializable;
import java.util.List;

public class EmissionsProcessDto implements Serializable {

    /**
     * default version id
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long emissionsUnitId;
    private AircraftEngineTypeCodeDto aircraftEngineTypeCode;
    private CodeLookupDto operatingStatusCode;
    private String emissionsProcessIdentifier;
    private Short statusYear;
    private Short year;
    private String sccCode;
    private SccCategory sccCategory;
    private String sccDescription;
    private String sccShortName;
    private String description;
    private String comments;
    private List<ReleasePointApptDto> releasePointAppts;
    private List<ReportingPeriodDto> reportingPeriods;
    private String initialMonthlyReportingPeriod;
    private Boolean sltBillingExempt;
    private String unitIdentifier;
    private String emissionsUnitDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmissionsUnitId() {
        return emissionsUnitId;
    }

    public void setEmissionsUnitId(Long emissionsUnitId) {
        this.emissionsUnitId = emissionsUnitId;
    }

    public AircraftEngineTypeCodeDto getaircraftEngineTypeCode() {
        return aircraftEngineTypeCode;
    }

    public void setAircraftEngineTypeCode(AircraftEngineTypeCodeDto aircraftEngineTypeCode) {
        this.aircraftEngineTypeCode = aircraftEngineTypeCode;
    }

    public CodeLookupDto getOperatingStatusCode() {
        return operatingStatusCode;
    }

    public void setOperatingStatusCode(CodeLookupDto operatingStatusCode) {
        this.operatingStatusCode = operatingStatusCode;
    }

    public String getEmissionsProcessIdentifier() {
        return emissionsProcessIdentifier;
    }

    public void setEmissionsProcessIdentifier(String emissionsProcessIdentifier) {
        this.emissionsProcessIdentifier = emissionsProcessIdentifier;
    }

    public Short getStatusYear() {
        return statusYear;
    }

    public void setStatusYear(Short statusYear) {
        this.statusYear = statusYear;
    }

    public String getSccCode() {
        return sccCode;
    }

    public void setSccCode(String sccCode) {
        this.sccCode = sccCode;
    }

    public SccCategory getSccCategory() {
        return sccCategory;
    }

    public void setSccCategory(SccCategory sccCategory) {
        this.sccCategory = sccCategory;
    }

    public String getSccDescription() {
        return sccDescription;
    }

    public void setSccDescription(String sccDescription) {
        this.sccDescription = sccDescription;
    }

    public String getSccShortName() {
        return sccShortName;
    }

    public void setSccShortName(String sccShortName) {
        this.sccShortName = sccShortName;
    }

    public String getDescription() {
        return description;
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

    public List<ReleasePointApptDto> getReleasePointAppts() {
        return releasePointAppts;
    }

    public void setReleasePointAppts(List<ReleasePointApptDto> releasePoints) {
        this.releasePointAppts = releasePoints;
    }

	public List<ReportingPeriodDto> getReportingPeriods() {
		return reportingPeriods;
	}

	public void setReportingPeriods(List<ReportingPeriodDto> reportingPeriods) {
		this.reportingPeriods = reportingPeriods;
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

    public Short getYear() { return year; }

    public void setYear(Short year) { this.year = year; }

    public String getUnitIdentifier() {
        return unitIdentifier;
    }

    public void setUnitIdentifier(String unitIdentifier) {
        this.unitIdentifier = unitIdentifier;
    }

    public String getEmissionsUnitDescription() {
        return emissionsUnitDescription;
    }

    public void setEmissionsUnitDescription(String emissionsUnitDescription) {
        this.emissionsUnitDescription = emissionsUnitDescription;
    }

}

