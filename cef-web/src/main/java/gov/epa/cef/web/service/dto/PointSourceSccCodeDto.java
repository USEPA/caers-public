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
