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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import gov.epa.cef.web.domain.common.BaseAuditEntity;

@Entity
@Table(name = "user_feedback")

public class UserFeedback extends BaseAuditEntity {
    
	private static final long serialVersionUID = 1L;

    // Fields
    
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private EmissionsReport emissionsReport;
    
    @Column(name = "facility_name")
    private String facilityName;
    
    @Column(name = "easy_and_intuitive")
    private Long intuitiveRating;

    @Column(name = "data_entry_via_screens")
    private Long dataEntryScreens;

    @Column(name = "calculation_screens")
    private Long calculationScreens;
    
    @Column(name = "controls_and_control_paths")
    private Long controlsAndControlPathAssignments;

    @Column(name = "overall_reporting_time")
    private Long overallReportingTime;

    @Column(name = "beneficial_functionality_description")
    private String beneficialFunctionalityComments;

    @Column(name = "difficult_application_functionality_description")
    private String difficultFunctionalityComments;
    
    @Column(name = "additional_features_or_enhancements_description")
    private String enhancementComments;
    
    @Column(name = "data_entry_via_bulk_upload")
    private Long dataEntryBulkUpload;

    @Column(name = "quality_assurance_checks")
    private Long qualityAssuranceChecks;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_system_code")
    private ProgramSystemCode programSystemCode;
    
    @Column(name = "year")
    private Short year;
    
    @Column(name = "user_name")
    private String userName;
    
	/***
     * Default constructor
     */
    public UserFeedback() {}
    
	public EmissionsReport getEmissionsReport() {
		return emissionsReport;
	}

	public void setEmissionsReport(EmissionsReport emissionsReport) {
		this.emissionsReport = emissionsReport;
	}

	public Long getIntuitiveRating() {
		return intuitiveRating;
	}

	public void setIntuitiveRating(Long intuitiveRating) {
		this.intuitiveRating = intuitiveRating;
	}

	public Long getDataEntryScreens() {
		return dataEntryScreens;
	}

	public void setDataEntryScreens(Long dataEntryScreens) {
		this.dataEntryScreens = dataEntryScreens;
	}

	public Long getCalculationScreens() {
		return calculationScreens;
	}

	public void setCalculationScreens(Long calculationScreens) {
		this.calculationScreens = calculationScreens;
	}

	public Long getControlsAndControlPathAssignments() {
		return controlsAndControlPathAssignments;
	}

	public void setControlsAndControlPathAssignments(Long controlsAndControlPathAssignments) {
		this.controlsAndControlPathAssignments = controlsAndControlPathAssignments;
	}

	public Long getOverallReportingTime() {
		return overallReportingTime;
	}

	public void setOverallReportingTime(Long overallReportingTime) {
		this.overallReportingTime = overallReportingTime;
	}

	public String getBeneficialFunctionalityComments() {
		return beneficialFunctionalityComments;
	}

	public void setBeneficialFunctionalityComments(String beneficialFunctionalityComments) {
		this.beneficialFunctionalityComments = beneficialFunctionalityComments;
	}

	public String getDifficultFunctionalityComments() {
		return difficultFunctionalityComments;
	}

	public void setDifficultFunctionalityComments(String difficultFunctionalityComments) {
		this.difficultFunctionalityComments = difficultFunctionalityComments;
	}

	public String getEnhancementComments() {
		return enhancementComments;
	}

	public void setEnhancementComments(String enhancementComments) {
		this.enhancementComments = enhancementComments;
	}

	public Long getDataEntryBulkUpload() {
		return dataEntryBulkUpload;
	}

	public void setDataEntryBulkUpload(Long dataEntryBulkUpload) {
		this.dataEntryBulkUpload = dataEntryBulkUpload;
	}

	public Long getQualityAssuranceChecks() {
		return qualityAssuranceChecks;
	}

	public void setQualityAssuranceChecks(Long qualityAssuranceChecks) {
		this.qualityAssuranceChecks = qualityAssuranceChecks;
	}

	public ProgramSystemCode getProgramSystemCode() {
		return programSystemCode;
	}

	public void setProgramSystemCode(ProgramSystemCode programSystemCode) {
		this.programSystemCode = programSystemCode;
	}

	public Short getYear() {
		return year;
	}

	public void setYear(Short year) {
		this.year = year;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
    
}
