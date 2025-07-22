/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.Immutable;

@Entity
@Immutable
@Table(name = "vw_submissions_review_dashboard")
public class SubmissionsReviewDashboardView implements Serializable{

    /**
     * default version
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "emissions_report_id", unique = true, nullable = false)
    private Long emissionsReportId;
    
    @Column(name = "master_facility_id", nullable = false)
    private Long masterFacilityId;
    
    @Column(name = "eis_program_id", nullable = false, length = 22)
    private String eisProgramId;
    
    @Column(name = "facility_name", nullable = false, length = 80)
    private String facilityName;

    @Column(name = "facility_site_id")
    private Long facilitySiteId;
    
    @Column(name = "agency_facility_identifier", length = 30)
    private String agencyFacilityIdentifier;
    
    @Column(name = "operating_status", length = 200)
    private String operatingStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "report_status")
    private ReportStatus reportStatus;
    
    @Column(name = "industry", length = 200)
    private String industry;
    
    @Column(name = "last_submittal_year")
    private Short lastSubmittalYear;
    
    @Column(name = "year", nullable = false)
    private Short year;
    
    @Column(name = "program_system_code", nullable = false, length = 20)
    private String programSystemCode;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "mid_year_sub_status")
    private ReportStatus midYearSubmissionStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "threshold_status")
    private ThresholdStatus thresholdStatus;
    
    public Long getEmissionsReportId() {
        return emissionsReportId;
    }
    public void setEmissionsReportId(Long emissionsReportId) {
        this.emissionsReportId = emissionsReportId;
    }
    public Long getMasterFacilityId() {
        return masterFacilityId;
    }
    public void setMasterFacilityId(Long masterFacilityId) {
        this.masterFacilityId = masterFacilityId;
    }
    public String getEisProgramId() {
        return eisProgramId;
    }
    public void setEisProgramId(String eisProgramId) {
        this.eisProgramId = eisProgramId;
    }
    public String getFacilityName() {
        return facilityName;
    }
    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }
    public Long getFacilitySiteId() {
        return facilitySiteId;
    }
    public void setFacilitySiteId(Long facilitySiteId) {
        this.facilitySiteId = facilitySiteId;
    }
    public String getAgencyFacilityIdentifier() {
        return agencyFacilityIdentifier;
    }
    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }
    public String getOperatingStatus() {
        return operatingStatus;
    }
    public void setOperatingStatus(String operatingStatus) {
        this.operatingStatus = operatingStatus;
    }
    public ReportStatus getReportStatus() {
        return reportStatus;
    }
    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }
    public String getIndustry() {
        return industry;
    }
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    public Short getLastSubmittalYear() {
        return lastSubmittalYear;
    }
    public void setLastSubmittalYear(Short lastSubmittalYear) {
        this.lastSubmittalYear = lastSubmittalYear;
    }
    public Short getYear() {
        return year;
    }
    public void setYear(Short year) {
        this.year = year;
    }
    public String getProgramSystemCode() {
        return programSystemCode;
    }
    public void setProgramSystemCode(String programSystemCode) {
        this.programSystemCode = programSystemCode;
    }
	public ReportStatus getMidYearSubmissionStatus() {
		return midYearSubmissionStatus;
	}
	public void setMidYearSubmissionStatus(ReportStatus midYearSubmissionStatus) {
		this.midYearSubmissionStatus = midYearSubmissionStatus;
	}
	public ThresholdStatus getThresholdStatus() {
		return thresholdStatus;
	}
	public void setThresholdStatus(ThresholdStatus thresholdStatus) {
		this.thresholdStatus = thresholdStatus;
	}
    
}
