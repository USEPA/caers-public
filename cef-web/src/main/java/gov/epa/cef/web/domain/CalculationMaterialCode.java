/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.domain;

import gov.epa.cef.web.domain.common.BaseLookupEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "calculation_material_code")
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class CalculationMaterialCode extends BaseLookupEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "fuel_use_material", nullable = false)
    private Boolean fuelUseMaterial;

    @Column(name = "default_heat_content_ratio", nullable = false, precision = 12, scale = 6)
    private BigDecimal defaultHeatContentRatio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "heat_content_ratio_numerator")
    private UnitMeasureCode heatContentRatioNumeratorUom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "heat_content_ratio_denominator")
    private UnitMeasureCode heatContentRatioDenominatorUom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "non_point_standardized_uom", nullable = true)
    private UnitMeasureCode nonPointStandardizedUom;

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

	public UnitMeasureCode getHeatContentRatioNumeratorUom() {
		return heatContentRatioNumeratorUom;
	}

	public void setHeatContentRatioNumeratorUom(
			UnitMeasureCode heatContentRatioNumeratorUom) {
		this.heatContentRatioNumeratorUom = heatContentRatioNumeratorUom;
	}

	public UnitMeasureCode getHeatContentRatioDenominatorUom() {
		return heatContentRatioDenominatorUom;
	}

	public void setHeatContentRatioDenominatorUom(
			UnitMeasureCode heatContentRatioDenominatorUom) {
		this.heatContentRatioDenominatorUom = heatContentRatioDenominatorUom;
	}

	public UnitMeasureCode getNonPointStandardizedUom() {
		return nonPointStandardizedUom;
	}

	public void setNonPointStandardizedUom(UnitMeasureCode nonPointStandardizedUom) {
		this.nonPointStandardizedUom = nonPointStandardizedUom;
	}

}
