/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package gov.epa.cef.web.service.dto;

import gov.epa.cef.web.service.validation.ValidationResult;

import java.io.Serializable;
import java.util.Date;

public class QualityCheckResultsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private boolean semiannual;
    private int federalErrorsCount;
    private int federalWarningsCount;
    private int stateErrorsCount;
    private int stateWarningsCount;
    private int facilityInventoryChangesCount;
    private EmissionsReportDto emissionsReportDto;
    private String qaResultsStatus;
    private ValidationResult currentResults;
    private Date createdDate;
    private Date lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsSemiannual() {
        return semiannual;
    }

    public void setIsSemiannual(boolean semiannual) {
        this.semiannual = semiannual;
    }

    public int getFederalErrorsCount() {
        return federalErrorsCount;
    }

    public void setFederalErrorsCount(int federalErrorsCount) {
        this.federalErrorsCount = federalErrorsCount;
    }

    public int getFederalWarningsCount() {
        return federalWarningsCount;
    }

    public void setFederalWarningsCount(int federalWarningsCount) {
        this.federalWarningsCount = federalWarningsCount;
    }

    public int getStateErrorsCount() {
        return stateErrorsCount;
    }

    public void setStateErrorsCount(int stateErrorsCount) {
        this.stateErrorsCount = stateErrorsCount;
    }

    public int getStateWarningsCount() {
        return stateWarningsCount;
    }

    public void setStateWarningsCount(int stateWarningsCount) {
        this.stateWarningsCount = stateWarningsCount;
    }

    public int getFacilityInventoryChangesCount() {
        return facilityInventoryChangesCount;
    }

    public void setFacilityInventoryChangesCount(int facilityInventoryChangesCount) {
        this.facilityInventoryChangesCount = facilityInventoryChangesCount;
    }

    public EmissionsReportDto getEmissionsReportDto() {
        return emissionsReportDto;
    }

    public void setEmissionsReportDto(EmissionsReportDto emissionsReportDto) {
        this.emissionsReportDto = emissionsReportDto;
    }

    public String getQaResultsStatus() {
        return qaResultsStatus;
    }

    public void setQaResultsStatus(String qaResultsStatus) {
        this.qaResultsStatus = qaResultsStatus;
    }

    public ValidationResult getCurrentResults() {
        return currentResults;
    }

    public void setCurrentResults(ValidationResult currentResults) {
        this.currentResults = currentResults;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
