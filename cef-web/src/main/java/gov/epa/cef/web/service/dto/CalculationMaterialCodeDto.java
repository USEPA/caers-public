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

import java.math.BigDecimal;

public class CalculationMaterialCodeDto extends CodeLookupDto {
	
	private static final long serialVersionUID = 1L;
	
	private Boolean fuelUseMaterial;
	private BigDecimal defaultHeatContentRatio;
	private UnitMeasureCodeDto heatContentRatioNumeratorUom;
	private UnitMeasureCodeDto heatContentRatioDenominatorUom;
	private UnitMeasureCodeDto nonPointStandardizedUom;
	
	public Boolean getFuelUseMaterial() {
		return fuelUseMaterial;
	}
	public void setFuelUseMaterial(Boolean fuelUseMaterial) {
		this.fuelUseMaterial = fuelUseMaterial;
	}
	public BigDecimal getDefaultHeatContentRatio() {
		return defaultHeatContentRatio;
	}
	public void setDefaultHeatContentRatio(BigDecimal defaultHeatContentRatio) {
		this.defaultHeatContentRatio = defaultHeatContentRatio;
	}
	public UnitMeasureCodeDto getHeatContentRatioNumeratorUom() {
		return heatContentRatioNumeratorUom;
	}
	public void setHeatContentRatioNumeratorUom(
			UnitMeasureCodeDto heatContentRatioNumeratorUom) {
		this.heatContentRatioNumeratorUom = heatContentRatioNumeratorUom;
	}
	public UnitMeasureCodeDto getHeatContentRatioDenominatorUom() {
		return heatContentRatioDenominatorUom;
	}
	public void setHeatContentRatioDenominatorUom(
			UnitMeasureCodeDto heatContentRatioDenominatorUom) {
		this.heatContentRatioDenominatorUom = heatContentRatioDenominatorUom;
	}
	public UnitMeasureCodeDto getNonPointStandardizedUom() {
		return nonPointStandardizedUom;
	}
	public void setNonPointStandardizedUom(UnitMeasureCodeDto nonPointStandardizedUom) {
		this.nonPointStandardizedUom = nonPointStandardizedUom;
	}
	
}
