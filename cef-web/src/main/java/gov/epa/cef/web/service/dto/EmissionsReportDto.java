/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
