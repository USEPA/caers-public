/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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

