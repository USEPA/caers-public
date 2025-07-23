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
