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
