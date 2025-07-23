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
import java.util.ArrayList;
import java.util.List;

public class MonthlyReportingEmissionHolderDto implements Serializable {

    private static final long serialVersionUID = 1L;

    // unit
    private Long emissionsUnitId;
    private String unitIdentifier;
    private String unitDescription;
    private CodeLookupDto unitStatus;
    private String unitInitialMonthlyReportingPeriod;

    // process
    private Long emissionsProcessId;
    private String emissionsProcessIdentifier;
    private String emissionsProcessDescription;
    private CodeLookupDto operatingStatusCode;
    private String processInitialMonthlyReportingPeriod;

    // reporting period
    private Long reportingPeriodId;
    private CodeLookupDto reportingPeriodTypeCode;
    private String period;

    // annual reporting period ID
    private Long annualReportingPeriodId;

    private List<MonthlyReportingEmissionDto> emissions = new ArrayList<>();

    public Long getEmissionsUnitId() {
        return emissionsUnitId;
    }

    public void setEmissionsUnitId(Long emissionsUnitId) {
        this.emissionsUnitId = emissionsUnitId;
    }

    public String getUnitIdentifier() {
        return unitIdentifier;
    }

    public void setUnitIdentifier(String unitIdentifier) {
        this.unitIdentifier = unitIdentifier;
    }

    public String getUnitDescription() {
        return unitDescription;
    }

    public void setUnitDescription(String unitDescription) {
        this.unitDescription = unitDescription;
    }

    public CodeLookupDto getUnitStatus() {
        return unitStatus;
    }

    public void setUnitStatus(CodeLookupDto unitStatus) {
        this.unitStatus = unitStatus;
    }

    public String getUnitInitialMonthlyReportingPeriod() {
        return unitInitialMonthlyReportingPeriod;
    }

    public void setUnitInitialMonthlyReportingPeriod(String unitInitialMonthlyReportingPeriod) {
        this.unitInitialMonthlyReportingPeriod = unitInitialMonthlyReportingPeriod;
    }

    public Long getEmissionsProcessId() {
        return emissionsProcessId;
    }

    public void setEmissionsProcessId(Long emissionsProcessid) {
        this.emissionsProcessId = emissionsProcessid;
    }

    public String getEmissionsProcessIdentifier() {
        return emissionsProcessIdentifier;
    }

    public void setEmissionsProcessIdentifier(String emissionsProcessIdentifier) {
        this.emissionsProcessIdentifier = emissionsProcessIdentifier;
    }

    public String getEmissionsProcessDescription() {
        return emissionsProcessDescription;
    }

    public void setEmissionsProcessDescription(String processDescription) {
        this.emissionsProcessDescription = processDescription;
    }

    public CodeLookupDto getOperatingStatusCode() {
        return operatingStatusCode;
    }

    public void setOperatingStatusCode(CodeLookupDto operatingStatusCode) {
        this.operatingStatusCode = operatingStatusCode;
    }

    public String getProcessInitialMonthlyReportingPeriod() {
        return processInitialMonthlyReportingPeriod;
    }

    public void setProcessInitialMonthlyReportingPeriod(String processInitialMonthlyReportingPeriod) {
        this.processInitialMonthlyReportingPeriod = processInitialMonthlyReportingPeriod;
    }

    public Long getReportingPeriodId() {
        return reportingPeriodId;
    }

    public void setReportingPeriodId(Long reportingPeriodId) {
        this.reportingPeriodId = reportingPeriodId;
    }

    public CodeLookupDto getReportingPeriodTypeCode() {
        return reportingPeriodTypeCode;
    }

    public void setReportingPeriodTypeCode(CodeLookupDto reportingPeriodTypeCode) {
        this.reportingPeriodTypeCode = reportingPeriodTypeCode;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Long getAnnualReportingPeriodId() {
        return annualReportingPeriodId;
    }

    public void setAnnualReportingPeriodId(Long annualReportingPeriodId) {
        this.annualReportingPeriodId = annualReportingPeriodId;
    }

    public List<MonthlyReportingEmissionDto> getEmissions() {
        return emissions;
    }

    public void setEmissions(List<MonthlyReportingEmissionDto> emissions) {
        this.emissions = emissions;
    }
}
