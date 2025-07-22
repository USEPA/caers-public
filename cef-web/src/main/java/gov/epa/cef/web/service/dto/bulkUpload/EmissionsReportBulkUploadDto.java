/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.dto.bulkUpload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmissionsReportBulkUploadDto implements IWorkbookAware, Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long masterFacilityRecordId;
    private String eisProgramId;
    private String agencyFacilityIdentifier;
    private String programSystemCode;
    private Short year;
    private String status;
    private String validationStatus;
    private String eisLastSubmissionStatus;
    private List<FacilitySiteBulkUploadDto> facilitySites = new ArrayList<>();
    private List<EmissionsUnitBulkUploadDto> emissionsUnits = new ArrayList<>();
    private List<EmissionsProcessBulkUploadDto> emissionsProcesses = new ArrayList<>();
    private List<ReleasePointBulkUploadDto> releasePoints = new ArrayList<>();
    private List<ReleasePointApptBulkUploadDto> releasePointAppts = new ArrayList<>();
    private List<ReportingPeriodBulkUploadDto> reportingPeriods = new ArrayList<>();
    private List<OperatingDetailBulkUploadDto> operatingDetails = new ArrayList<>();
    private List<EmissionBulkUploadDto> emissions = new ArrayList<>();
    private List<EmissionFormulaVariableBulkUploadDto> emissionFormulaVariables = new ArrayList<>();
    private List<ControlPathBulkUploadDto> controlPaths = new ArrayList<>();
    private List<ControlBulkUploadDto> controls = new ArrayList<>();
    private List<ControlAssignmentBulkUploadDto> controlAssignments = new ArrayList<>();
    private List<ControlPollutantBulkUploadDto> controlPollutants = new ArrayList<>();
    private List<ControlPathPollutantBulkUploadDto> controlPathPollutants = new ArrayList<>();
    private List<FacilityNAICSBulkUploadDto> facilityNAICS = new ArrayList<>();
    private List<FacilitySiteContactBulkUploadDto> facilityContacts = new ArrayList<>();
    private List<VersionBulkUploadDto> versions = new ArrayList<>();

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getMasterFacilityRecordId() {
        return masterFacilityRecordId;
    }
    public void setMasterFacilityRecordId(Long masterFacilityId) {
        this.masterFacilityRecordId = masterFacilityId;
    }

    public String getEisProgramId() {
        return eisProgramId;
    }
    public void setEisProgramId(String eisProgramId) {
        this.eisProgramId = eisProgramId;
    }

    public String getAgencyFacilityIdentifier() {
        return agencyFacilityIdentifier;
    }
    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }

    public String getProgramSystemCode() {
        return programSystemCode;
    }
    public void setProgramSystemCode(String programSystemCode) {
        this.programSystemCode = programSystemCode;
    }

    public Short getYear() {
        return year;
    }
    public void setYear(Short year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getValidationStatus() {
        return validationStatus;
    }
    public void setValidationStatus(String validationStatus) {
        this.validationStatus = validationStatus;
    }
    

    public String getEisLastSubmissionStatus() {
        return eisLastSubmissionStatus;
    }

    public void setEisLastSubmissionStatus(String eisLastSubmissionStatus) {
        this.eisLastSubmissionStatus = eisLastSubmissionStatus;
    }


    public void setFacilitySites(List<FacilitySiteBulkUploadDto> facilitySites) {
        this.facilitySites = facilitySites;
    }
    public List<FacilitySiteBulkUploadDto> getFacilitySites() {
        return facilitySites;
    }

    public void setEmissionsUnits(List<EmissionsUnitBulkUploadDto> emissionsUnits) {
        this.emissionsUnits = emissionsUnits;
    }
    public List<EmissionsUnitBulkUploadDto> getEmissionsUnits() {
        return emissionsUnits;
    }

    public void setEmissionsProcesses(List<EmissionsProcessBulkUploadDto> emissionsProcesses) {
        this.emissionsProcesses = emissionsProcesses;
    }
    public List<EmissionsProcessBulkUploadDto> getEmissionsProcesses() {
        return emissionsProcesses;
    }

    public void setReleasePoints(List<ReleasePointBulkUploadDto> releasePoints) {
        this.releasePoints = releasePoints;
    }
    public List<ReleasePointBulkUploadDto> getReleasePoints() {
        return releasePoints;
    }

    public void setReleasePointAppts(List<ReleasePointApptBulkUploadDto> releasePointAppts) {
        this.releasePointAppts = releasePointAppts;
    }
    public List<ReleasePointApptBulkUploadDto> getReleasePointAppts() {
        return releasePointAppts;
    }

    public void setReportingPeriods(List<ReportingPeriodBulkUploadDto> reportingPeriods) {
        this.reportingPeriods = reportingPeriods;
    }
    public List<ReportingPeriodBulkUploadDto> getReportingPeriods() {
        return reportingPeriods;
    }
    public List<OperatingDetailBulkUploadDto> getOperatingDetails() {
        return operatingDetails;
    }
    public void setOperatingDetails(List<OperatingDetailBulkUploadDto> operatingDetails) {
        this.operatingDetails = operatingDetails;
    }
    public void setEmissions(List<EmissionBulkUploadDto> emissions) {
        this.emissions = emissions;
    }
    public List<EmissionBulkUploadDto> getEmissions() {
        return emissions;
    }
    public List<EmissionFormulaVariableBulkUploadDto> getEmissionFormulaVariables() {
        return emissionFormulaVariables;
    }
    public void setEmissionFormulaVariables(List<EmissionFormulaVariableBulkUploadDto> emissionFormulaVariables) {
        this.emissionFormulaVariables = emissionFormulaVariables;
    }
    public List<ControlPathBulkUploadDto> getControlPaths() {
        return controlPaths;
    }
    public void setControlPaths(List<ControlPathBulkUploadDto> controlPaths) {
        this.controlPaths = controlPaths;
    }
    public List<ControlBulkUploadDto> getControls() {
        return controls;
    }
    public void setControls(List<ControlBulkUploadDto> controls) {
        this.controls = controls;
    }
    public List<ControlAssignmentBulkUploadDto> getControlAssignments() {
        return controlAssignments;
    }
    public void setControlAssignments(List<ControlAssignmentBulkUploadDto> controlAssignments) {
        this.controlAssignments = controlAssignments;
    }
    public List<ControlPollutantBulkUploadDto> getControlPollutants() {
        return controlPollutants;
    }
    public void setControlPollutants(List<ControlPollutantBulkUploadDto> controlPollutants) {
        this.controlPollutants = controlPollutants;
    }
    public List<ControlPathPollutantBulkUploadDto> getControlPathPollutants() {
        return controlPathPollutants;
    }
    public void setControlPathPollutants(List<ControlPathPollutantBulkUploadDto> controlPathPollutants) {
        this.controlPathPollutants = controlPathPollutants;
    }
    public List<FacilityNAICSBulkUploadDto> getFacilityNAICS() {
    	return facilityNAICS;
    }
    public void setFacilityNAICS(List<FacilityNAICSBulkUploadDto> facilityNAICS) {
    	this.facilityNAICS = facilityNAICS;
    }
    public List<FacilitySiteContactBulkUploadDto> getFacilityContacts() {
    	return facilityContacts;
    }
    public void setFacilityContacts(List<FacilitySiteContactBulkUploadDto> facilityContacts) {
    	this.facilityContacts = facilityContacts;
    }
    public List<VersionBulkUploadDto> getVersions() {
        return versions;
    }
    public void setVersions(List<VersionBulkUploadDto> versions) {
        this.versions = versions;
    }

}
