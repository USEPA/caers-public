/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package gov.epa.cef.web.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class MonthlyReportingDownloadDto implements Serializable {

    private static final long serialVersionUID = 1L;

    // Facility Site
    private String agencyFacilityIdentifier;
    private String facilityName;

    // Unit
    private Long emissionsUnitId;
    private String unitIdentifier;
    private String unitDescription;
    private CodeLookupDto unitStatus;
    private String unitInitialMonthlyReportingPeriod;

    // Process
    private Long emissionsProcessId;
    private String emissionsProcessIdentifier;
    private String emissionsProcessDescription;
    private String emissionsProcessSccCode;
    private CodeLookupDto operatingStatusCode;
    private String processInitialMonthlyReportingPeriod;

    // Reporting Period
    private Long annualReportingPeriodId;
    private Long reportingPeriodId;
    private String reportingPeriodName;
    private String calculationMaterialShortName;
    private String calculationParameterValue;
    private String calculationParameterUomCode;
    private String fuelUseMaterialShortName;
    private String fuelUseValue;
    private String fuelUseUomCode;
    private String period;

    // Operating Detail
    private Long operatingDetailId;
    private BigDecimal hoursPerPeriod;
    private BigDecimal avgHoursPerDay;
    private BigDecimal avgDaysPerWeek;
    private BigDecimal avgWeeksPerPeriod;

    // Emission
    private Long emissionId;
    private String pollutantName;
    private String emissionsCalcMethodDescription;
    private String monthlyRate;
    private String emissionsFactorText;
    private String emissionsFactor;
    private BigDecimal totalEmissions;
    private String emissionsUomCode;

    public String getAgencyFacilityIdentifier() { return agencyFacilityIdentifier; }

    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
      this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }

    public String getFacilityName() { return facilityName; }

    public void setFacilityName(String facilityName) { this.facilityName = facilityName; }

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

    public String getEmissionsProcessSccCode() { return emissionsProcessSccCode; }

    public void setEmissionsProcessSccCode(String emissionsProcessSccCode) {
      this.emissionsProcessSccCode = emissionsProcessSccCode;
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

    public String getReportingPeriodName() {
      return reportingPeriodName;
    }

    public void setReportingPeriodName(String reportingPeriodName) {
      this.reportingPeriodName = reportingPeriodName;
    }

    public String getCalculationParameterUomCode() {
      return calculationParameterUomCode;
    }

    public void setCalculationParameterUomCode(String calculationParameterUomCode) {
      this.calculationParameterUomCode = calculationParameterUomCode;
    }

    public String getCalculationMaterialShortName() {
      return calculationMaterialShortName;
    }

    public void setCalculationMaterialShortName(String calculationMaterialShortName) {
      this.calculationMaterialShortName = calculationMaterialShortName;
    }

    public String getCalculationParameterValue() {
      return calculationParameterValue;
    }

    public void setCalculationParameterValue(String calculationParameterValue) {
      this.calculationParameterValue = calculationParameterValue;
    }

    public String getFuelUseMaterialShortName() {
      return fuelUseMaterialShortName;
    }

    public void setFuelUseMaterialShortName(String fuelUseMaterialShortName) {
      this.fuelUseMaterialShortName = fuelUseMaterialShortName;
    }

    public String getFuelUseValue() {
      return fuelUseValue;
    }

    public void setFuelUseValue(String fuelUseValue) {
      this.fuelUseValue = fuelUseValue;
    }

    public String getFuelUseUomCode() {
      return fuelUseUomCode;
    }

    public void setFuelUseUomCode(String fuelUseUomCode) {
      this.fuelUseUomCode = fuelUseUomCode;
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

    public Long getOperatingDetailId() {
      return operatingDetailId;
    }

    public void setOperatingDetailId(Long operatingDetailId) {
      this.operatingDetailId = operatingDetailId;
    }

    public BigDecimal getHoursPerPeriod() {
      return hoursPerPeriod;
    }

    public void setHoursPerPeriod(BigDecimal hoursPerPeriod) {
      this.hoursPerPeriod = hoursPerPeriod;
    }

    public BigDecimal getAvgHoursPerDay() {
      return avgHoursPerDay;
    }

    public void setAvgHoursPerDay(BigDecimal avgHoursPerDay) {
      this.avgHoursPerDay = avgHoursPerDay;
    }

    public BigDecimal getAvgDaysPerWeek() {
      return avgDaysPerWeek;
    }

    public void setAvgDaysPerWeek(BigDecimal avgDaysPerWeek) {
      this.avgDaysPerWeek = avgDaysPerWeek;
    }

    public BigDecimal getAvgWeeksPerPeriod() {
      return avgWeeksPerPeriod;
    }

    public void setAvgWeeksPerPeriod(BigDecimal avgWeeksPerPeriod) {
      this.avgWeeksPerPeriod = avgWeeksPerPeriod;
    }

    public Long getEmissionId() {
      return emissionId;
    }

    public void setEmissionId(Long emissionId) {
      this.emissionId = emissionId;
    }

    public String getPollutantName() {
      return pollutantName;
    }

    public void setPollutantName(String pollutantName) {
      this.pollutantName = pollutantName;
    }

    public String getEmissionsCalcMethodDescription() {
      return emissionsCalcMethodDescription;
    }

    public void setEmissionsCalcMethodDescription(String emissionsCalcMethodDescription) {
      this.emissionsCalcMethodDescription = emissionsCalcMethodDescription;
    }

    public String getMonthlyRate() {
      return monthlyRate;
    }

    public void setMonthlyRate(String monthlyRate) {
      this.monthlyRate = monthlyRate;
    }

    public String getEmissionsFactorText() { return emissionsFactorText; }

    public void setEmissionsFactorText(String emissionsFactorText) { this.emissionsFactorText = emissionsFactorText; }

    public String getEmissionsFactor() {
      return emissionsFactor;
    }

    public void setEmissionsFactor(String emissionsFactor) {
      this.emissionsFactor = emissionsFactor;
    }

    public BigDecimal getTotalEmissions() {
      return totalEmissions;
    }

    public void setTotalEmissions(BigDecimal totalEmissions) {
      this.totalEmissions = totalEmissions;
    }

    public String getEmissionsUomCode() {
      return emissionsUomCode;
    }

    public void setEmissionsUomCode(String emissionsUomCode) {
      this.emissionsUomCode = emissionsUomCode;
    }

}
