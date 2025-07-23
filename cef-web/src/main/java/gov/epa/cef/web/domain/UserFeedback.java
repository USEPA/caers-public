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
