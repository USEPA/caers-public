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

public class EmissionsReportDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String eisProgramId;
    private Long masterFacilityRecordId;
    private CodeLookupDto programSystemCode;
    private Short year;
    private String status;
    private String validationStatus;
    private String thresholdStatus;
    private Boolean hasSubmitted;
    private String eisLastSubmissionStatus;
    private String fileName;
    private Boolean returnedReport;
    private String midYearSubmissionStatus;
    private Date lastModifiedDate;
    private Integer maxQaErrors;
    private Integer maxQaWarnings;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEisProgramId() {
        return eisProgramId;
    }

    public void setEisProgramId(String eisProgramId) {
        this.eisProgramId = eisProgramId;
    }

    public Long getMasterFacilityRecordId() {
        return masterFacilityRecordId;
    }

    public void setMasterFacilityRecordId(Long masterFacilityRecordId) {
        this.masterFacilityRecordId = masterFacilityRecordId;
    }

    public CodeLookupDto getProgramSystemCode() {
        return programSystemCode;
    }

    public void setProgramSystemCode(CodeLookupDto programSystemCode) {
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

	public String getThresholdStatus() {
        return thresholdStatus;
    }

    public void setThresholdStatus(String thresholdStatus) {
        this.thresholdStatus = thresholdStatus;
    }

    public Boolean getHasSubmitted() {
		return hasSubmitted;
	}

	public void setHasSubmitted(Boolean hasSubmitted) {
		this.hasSubmitted = hasSubmitted;
	}

	public String getEisLastSubmissionStatus() {
		return eisLastSubmissionStatus;
	}

	public void setEisLastSubmissionStatus(String eisLastSubmissionStatus) {
		this.eisLastSubmissionStatus = eisLastSubmissionStatus;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public EmissionsReportDto withId(Long id) {
        setId(id);
        return this;
    }

	public Boolean getReturnedReport() {
		return returnedReport;
	}

	public void setReturnedReport(Boolean returnedReport) {
		this.returnedReport = returnedReport;
	}

	public String getMidYearSubmissionStatus() {
		return midYearSubmissionStatus;
	}

	public void setMidYearSubmissionStatus(String midYearSubmissionStatus) {
		this.midYearSubmissionStatus = midYearSubmissionStatus;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

    public Integer getMaxQaErrors() {
        return maxQaErrors;
    }

    public void setMaxQaErrors(Integer maxQaErrors) {
        this.maxQaErrors = maxQaErrors;
    }

    public Integer getMaxQaWarnings() {
        return maxQaWarnings;
    }

    public void setMaxQaWarnings(Integer maxQaWarnings) {
        this.maxQaWarnings = maxQaWarnings;
    }
}
