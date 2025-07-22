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

public class MonthlyFuelReportingDownloadDto implements Serializable {

	private static final long serialVersionUID = 1L;
	// unit
    private String emissionsUnitIdentifier;

    // process
    private String emissionsProcessIdentifier;
    private String emissionsProcessSccCode;
    private String fuelUseMaterialCode;
    private String fuelUseUom;
    
    // Monthly Fuel Reporting
    private BigDecimal january_totalOperatingDays;
    private BigDecimal january_fuelUseValue;
    private BigDecimal february_totalOperatingDays;
    private BigDecimal february_fuelUseValue;
    private BigDecimal march_totalOperatingDays;
    private BigDecimal march_fuelUseValue;
    private BigDecimal april_totalOperatingDays;
    private BigDecimal april_fuelUseValue;
    private BigDecimal may_totalOperatingDays;
    private BigDecimal may_fuelUseValue;
    private BigDecimal june_totalOperatingDays;
    private BigDecimal june_fuelUseValue;
    private BigDecimal july_totalOperatingDays;
    private BigDecimal july_fuelUseValue;
    private BigDecimal august_totalOperatingDays;
    private BigDecimal august_fuelUseValue;
    private BigDecimal september_totalOperatingDays;
    private BigDecimal september_fuelUseValue;
    private BigDecimal october_totalOperatingDays;
    private BigDecimal october_fuelUseValue;
    private BigDecimal november_totalOperatingDays;
    private BigDecimal november_fuelUseValue;
    private BigDecimal december_totalOperatingDays;
    private BigDecimal december_fuelUseValue;
    private BigDecimal semiAnnual_totalOperatingDays;
    private BigDecimal semiAnnual_fuelUseValue;
    private BigDecimal annual_totalOperatingDays;
    private BigDecimal annual_fuelUseValue;
    
	public String getEmissionsUnitIdentifier() {
		return emissionsUnitIdentifier;
	}
	public void setEmissionsUnitIdentifier(String emissionsUnitId) {
		this.emissionsUnitIdentifier = emissionsUnitId;
	}
	public String getEmissionsProcessIdentifier() {
		return emissionsProcessIdentifier;
	}
	public void setEmissionsProcessIdentifier(String emissionsProcessId) {
		this.emissionsProcessIdentifier = emissionsProcessId;
	}
	public String getEmissionsProcessSccCode() {
		return emissionsProcessSccCode;
	}
	public void setEmissionsProcessSccCode(String emissionsProcessSccCode) {
		this.emissionsProcessSccCode = emissionsProcessSccCode;
	}
	public String getFuelUseUom() {
		return fuelUseUom;
	}
	public void setFuelUseUom(String fuelUseUom) {
		this.fuelUseUom = fuelUseUom;
	}
	public String getFuelUseMaterialCode() {
		return fuelUseMaterialCode;
	}
	public void setFuelUseMaterialCode(String fuelUseMaterialCode) {
		this.fuelUseMaterialCode = fuelUseMaterialCode;
	}
	public BigDecimal getJanuary_totalOperatingDays() {
		return january_totalOperatingDays;
	}
	public void setJanuary_totalOperatingDays(BigDecimal january_totalOperatingDays) {
		this.january_totalOperatingDays = january_totalOperatingDays;
	}
	public BigDecimal getJanuary_fuelUseValue() {
		return january_fuelUseValue;
	}
	public void setJanuary_fuelUseValue(BigDecimal january_fuelUseValue) {
		this.january_fuelUseValue = january_fuelUseValue;
	}
	public BigDecimal getFebruary_totalOperatingDays() {
		return february_totalOperatingDays;
	}
	public void setFebruary_totalOperatingDays(BigDecimal february_totalOperatingDays) {
		this.february_totalOperatingDays = february_totalOperatingDays;
	}
	public BigDecimal getFebruary_fuelUseValue() {
		return february_fuelUseValue;
	}
	public void setFebruary_fuelUseValue(BigDecimal february_fuelUseValue) {
		this.february_fuelUseValue = february_fuelUseValue;
	}
	public BigDecimal getMarch_totalOperatingDays() {
		return march_totalOperatingDays;
	}
	public void setMarch_totalOperatingDays(BigDecimal march_totalOperatingDays) {
		this.march_totalOperatingDays = march_totalOperatingDays;
	}
	public BigDecimal getMarch_fuelUseValue() {
		return march_fuelUseValue;
	}
	public void setMarch_fuelUseValue(BigDecimal march_fuelUseValue) {
		this.march_fuelUseValue = march_fuelUseValue;
	}
	public BigDecimal getApril_totalOperatingDays() {
		return april_totalOperatingDays;
	}
	public void setApril_totalOperatingDays(BigDecimal april_totalOperatingDays) {
		this.april_totalOperatingDays = april_totalOperatingDays;
	}
	public BigDecimal getApril_fuelUseValue() {
		return april_fuelUseValue;
	}
	public void setApril_fuelUseValue(BigDecimal april_fuelUseValue) {
		this.april_fuelUseValue = april_fuelUseValue;
	}
	public BigDecimal getMay_totalOperatingDays() {
		return may_totalOperatingDays;
	}
	public void setMay_totalOperatingDays(BigDecimal may_totalOperatingDays) {
		this.may_totalOperatingDays = may_totalOperatingDays;
	}
	public BigDecimal getMay_fuelUseValue() {
		return may_fuelUseValue;
	}
	public void setMay_fuelUseValue(BigDecimal may_fuelUseValue) {
		this.may_fuelUseValue = may_fuelUseValue;
	}
	public BigDecimal getJune_totalOperatingDays() {
		return june_totalOperatingDays;
	}
	public void setJune_totalOperatingDays(BigDecimal june_totalOperatingDays) {
		this.june_totalOperatingDays = june_totalOperatingDays;
	}
	public BigDecimal getJune_fuelUseValue() {
		return june_fuelUseValue;
	}
	public void setJune_fuelUseValue(BigDecimal june_fuelUseValue) {
		this.june_fuelUseValue = june_fuelUseValue;
	}
	public BigDecimal getJuly_totalOperatingDays() {
		return july_totalOperatingDays;
	}
	public void setJuly_totalOperatingDays(BigDecimal july_totalOperatingDays) {
		this.july_totalOperatingDays = july_totalOperatingDays;
	}
	public BigDecimal getJuly_fuelUseValue() {
		return july_fuelUseValue;
	}
	public void setJuly_fuelUseValue(BigDecimal july_fuelUseValue) {
		this.july_fuelUseValue = july_fuelUseValue;
	}
	public BigDecimal getAugust_totalOperatingDays() {
		return august_totalOperatingDays;
	}
	public void setAugust_totalOperatingDays(BigDecimal august_totalOperatingDays) {
		this.august_totalOperatingDays = august_totalOperatingDays;
	}
	public BigDecimal getAugust_fuelUseValue() {
		return august_fuelUseValue;
	}
	public void setAugust_fuelUseValue(BigDecimal august_fuelUseValue) {
		this.august_fuelUseValue = august_fuelUseValue;
	}
	public BigDecimal getSeptember_totalOperatingDays() {
		return september_totalOperatingDays;
	}
	public void setSeptember_totalOperatingDays(BigDecimal september_totalOperatingDays) {
		this.september_totalOperatingDays = september_totalOperatingDays;
	}
	public BigDecimal getSeptember_fuelUseValue() {
		return september_fuelUseValue;
	}
	public void setSeptember_fuelUseValue(BigDecimal september_fuelUseValue) {
		this.september_fuelUseValue = september_fuelUseValue;
	}
	public BigDecimal getOctober_totalOperatingDays() {
		return october_totalOperatingDays;
	}
	public void setOctober_totalOperatingDays(BigDecimal october_totalOperatingDays) {
		this.october_totalOperatingDays = october_totalOperatingDays;
	}
	public BigDecimal getOctober_fuelUseValue() {
		return october_fuelUseValue;
	}
	public void setOctober_fuelUseValue(BigDecimal october_fuelUseValue) {
		this.october_fuelUseValue = october_fuelUseValue;
	}
	public BigDecimal getNovember_totalOperatingDays() {
		return november_totalOperatingDays;
	}
	public void setNovember_totalOperatingDays(BigDecimal november_totalOperatingDays) {
		this.november_totalOperatingDays = november_totalOperatingDays;
	}
	public BigDecimal getNovember_fuelUseValue() {
		return november_fuelUseValue;
	}
	public void setNovember_fuelUseValue(BigDecimal november_fuelUseValue) {
		this.november_fuelUseValue = november_fuelUseValue;
	}
	public BigDecimal getDecember_totalOperatingDays() {
		return december_totalOperatingDays;
	}
	public void setDecember_totalOperatingDays(BigDecimal december_totalOperatingDays) {
		this.december_totalOperatingDays = december_totalOperatingDays;
	}
	public BigDecimal getDecember_fuelUseValue() {
		return december_fuelUseValue;
	}
	public void setDecember_fuelUseValue(BigDecimal december_fuelUseValue) {
		this.december_fuelUseValue = december_fuelUseValue;
	}
	public BigDecimal getSemiAnnual_totalOperatingDays() {
		return semiAnnual_totalOperatingDays;
	}
	public void setSemiAnnual_totalOperatingDays(BigDecimal semiAnnual_totalOperatingDays) {
		this.semiAnnual_totalOperatingDays = semiAnnual_totalOperatingDays;
	}
	public BigDecimal getSemiAnnual_fuelUseValue() {
		return semiAnnual_fuelUseValue;
	}
	public void setSemiAnnual_fuelUseValue(BigDecimal semiAnnual_fuelUseValue) {
		this.semiAnnual_fuelUseValue = semiAnnual_fuelUseValue;
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
