/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
