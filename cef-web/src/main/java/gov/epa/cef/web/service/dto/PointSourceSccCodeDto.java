/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.dto;

import gov.epa.cef.web.domain.SccCategory;

import java.io.Serializable;

public class PointSourceSccCodeDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;
	private Short lastInventoryYear;
	private Boolean fuelUseRequired;
	private String sccLevelOne;
	private String sccLevelTwo;
	private String sccLevelThree;
	private String sccLevelFour;
	private String sector;
	private String shortName;
	private String description;
	private String fuelUseTypes;
	private CodeLookupDto calculationMaterialCode;
	private Boolean monthlyReporting;
    private SccCategory category;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Short getLastInventoryYear() {
		return lastInventoryYear;
	}

	public void setLastInventoryYear(Short lastInventoryYear) {
		this.lastInventoryYear = lastInventoryYear;
	}

	public Boolean getFuelUseRequired() {
		return fuelUseRequired;
	}

	public void setFuelUseRequired(Boolean fuelUseRequired) {
		this.fuelUseRequired = fuelUseRequired;
	}

	public Boolean getMonthlyReporting() {
		return monthlyReporting;
	}

	public void setMonthlyReporting(Boolean monthlyReporting) {
		this.monthlyReporting = monthlyReporting;
	}

	public String getSccLevelOne() {
		return sccLevelOne;
	}

	public void setSccLevelOne(String sccLevelOne) {
		this.sccLevelOne = sccLevelOne;
	}

	public String getSccLevelTwo() {
		return sccLevelTwo;
	}

	public void setSccLevelTwo(String sccLevelTwo) {
		this.sccLevelTwo = sccLevelTwo;
	}

	public String getSccLevelThree() {
		return sccLevelThree;
	}

	public void setSccLevelThree(String sccLevelThree) {
		this.sccLevelThree = sccLevelThree;
	}

	public String getSccLevelFour() {
		return sccLevelFour;
	}

	public void setSccLevelFour(String sccLevelFour) {
		this.sccLevelFour = sccLevelFour;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getFuelUseTypes() {
		return fuelUseTypes;
	}

	public void setFuelUseTypes(String fuelUseTypes) {
		this.fuelUseTypes = fuelUseTypes;
	}

	public CodeLookupDto getCalculationMaterialCode() {
		return calculationMaterialCode;
	}

	public void setCalculationMaterialCode(CodeLookupDto calculationMaterialCode) {
		this.calculationMaterialCode = calculationMaterialCode;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getDescription() {
		return this.sccLevelOne+" > "+this.sccLevelTwo+" > "+this.sccLevelThree+" > "+this.sccLevelFour;
	}

    public SccCategory getCategory() {
        return category;
    }

    public void setCategory(SccCategory category) {
        this.category = category;
    }
}
