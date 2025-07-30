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

import gov.epa.cef.web.domain.OperatingStatusCode;

public class MonthlyFuelReportingDto implements Serializable {

	private static final long serialVersionUID = 1L;
	// unit
    private Long emissionsUnitId;
    private String unitIdentifier;
    private String unitDescription;
    private OperatingStatusCode unitStatus;

    // process
    private Long emissionsProcessId;
    private String emissionsProcessIdentifier;
    private String emissionsProcessDescription;
    private OperatingStatusCode operatingStatusCode;
    private String emissionsProcessSccCode;
    
    // Monthly Fuel Reporting
    private Long id;
    private Long reportingPeriodId;
    private UnitMeasureCodeDto fuelUseUom;
    private CodeLookupDto fuelUseMaterialCode;
    private String period;
    private BigDecimal totalOperatingDays;
    private BigDecimal fuelUseValue;
    private BigDecimal annual_totalOperatingDays;
    private BigDecimal annual_fuelUseValue;
    private Boolean midYearSubmitted;
    
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
	public OperatingStatusCode getUnitStatus() {
		return unitStatus;
	}
	public void setUnitStatus(OperatingStatusCode unitStatus) {
		this.unitStatus = unitStatus;
	}
	public Long getEmissionsProcessId() {
		return emissionsProcessId;
	}
	public void setEmissionsProcessId(Long emissionsProcessId) {
		this.emissionsProcessId = emissionsProcessId;
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
	public void setEmissionsProcessDescription(String emissionsProcessDescription) {
		this.emissionsProcessDescription = emissionsProcessDescription;
	}
	public OperatingStatusCode getOperatingStatusCode() {
		return operatingStatusCode;
	}
	public void setOperatingStatusCode(OperatingStatusCode operatingStatusCode) {
		this.operatingStatusCode = operatingStatusCode;
	}
	public String getEmissionsProcessSccCode() {
		return emissionsProcessSccCode;
	}
	public void setEmissionsProcessSccCode(String emissionsProcessSccCode) {
		this.emissionsProcessSccCode = emissionsProcessSccCode;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getReportingPeriodId() {
		return reportingPeriodId;
	}
	public void setReportingPeriodId(Long reportingPeriodId) {
		this.reportingPeriodId = reportingPeriodId;
	}
	public UnitMeasureCodeDto getFuelUseUom() {
		return fuelUseUom;
	}
	public void setFuelUseUom(UnitMeasureCodeDto fuelUseUom) {
		this.fuelUseUom = fuelUseUom;
	}
	public CodeLookupDto getFuelUseMaterialCode() {
		return fuelUseMaterialCode;
	}
	public void setFuelUseMaterialCode(CodeLookupDto fuelUseMaterialCode) {
		this.fuelUseMaterialCode = fuelUseMaterialCode;
	}
	public BigDecimal getFuelUseValue() {
		return fuelUseValue;
	}
	public void setFuelUseValue(BigDecimal fuelUseValue) {
		this.fuelUseValue = fuelUseValue;
	}
	public BigDecimal getTotalOperatingDays() {
		return totalOperatingDays;
	}
	public void setTotalOperatingDays(BigDecimal totalOperatingDays) {
		this.totalOperatingDays = totalOperatingDays;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public Boolean getMidYearSubmitted() {
		return midYearSubmitted;
	}
	public void setMidYearSubmitted(Boolean midYearSubmitted) {
		this.midYearSubmitted = midYearSubmitted;
	}
	public BigDecimal getAnnual_totalOperatingDays() {
		return annual_totalOperatingDays;
	}
	public void setAnnual_totalOperatingDays(BigDecimal annual_totalOperatingDays) {
		this.annual_totalOperatingDays = annual_totalOperatingDays;
	}
	public BigDecimal getAnnual_fuelUseValue() {
		return annual_fuelUseValue;
	}
	public void setAnnual_fuelUseValue(BigDecimal annual_fuelUseValue) {
		this.annual_fuelUseValue = annual_fuelUseValue;
	}
	
}
