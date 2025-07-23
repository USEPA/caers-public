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

import java.io.Serializable;
import java.util.Date;

public class UserFeedbackDto implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
	private Long id;
    private String beneficialFunctionalityComments;
    private String difficultFunctionalityComments;
    private String enhancementComments;
    private String facilityName;
    private Short year;
	private Long reportId;
    private Long intuitiveRating;
    private Long dataEntryScreens;
    private Long dataEntryBulkUpload;
    private Long calculationScreens;
    private Long controlsAndControlPathAssignments;
    private Long qualityAssuranceChecks;
    private Long overallReportingTime;
    private String userName;
    private String userId;
    private String userRole;
    private CodeLookupDto programSystemCode; 
    private Date createdDate;
    private String lastModifiedBy;
    
    public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	public Short getYear() {
		return year;
	}
	public void setYear(Short year) {
		this.year = year;
	}
    
    public Long getId() {
		return id;
	}
    
	public void setId(Long id) {
		this.id = id;
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
	public Long getReportId() {
		return reportId;
	}
	public void setReportId(Long reportId) {
		this.reportId = reportId;
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
	public Long getDataEntryBulkUpload() {
		return dataEntryBulkUpload;
	}
	public void setDataEntryBulkUpload(Long dataEntryBulkUpload) {
		this.dataEntryBulkUpload = dataEntryBulkUpload;
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
	public Long getQualityAssuranceChecks() {
		return qualityAssuranceChecks;
	}
	public void setQualityAssuranceChecks(Long qualityAssuranceChecks) {
		this.qualityAssuranceChecks = qualityAssuranceChecks;
	}
	public Long getOverallReportingTime() {
		return overallReportingTime;
	}
	public void setOverallReportingTime(Long overallReportingTime) {
		this.overallReportingTime = overallReportingTime;
	}
	public UserFeedbackDto withId(Long reportId) {
        setReportId(reportId);
        return this;
    }
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public CodeLookupDto getProgramSystemCode() {
		return programSystemCode;
	}
	public void setProgramSystemCode(CodeLookupDto programSystemCode) {
		this.programSystemCode = programSystemCode;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
       
}
