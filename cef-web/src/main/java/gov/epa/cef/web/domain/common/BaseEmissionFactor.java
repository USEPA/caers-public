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
package gov.epa.cef.web.domain.common;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import gov.epa.cef.web.domain.*;

@MappedSuperclass
public abstract class BaseEmissionFactor extends BaseEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numerator_uom_code")
    private UnitMeasureCode emissionsNumeratorUom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "denominator_uom_code")
    private UnitMeasureCode emissionsDenominatorUom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calculation_parameter_type_code", nullable = false)
    private CalculationParameterTypeCode calculationParameterTypeCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calculation_material_code", nullable = false)
    private CalculationMaterialCode calculationMaterialCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "control_measure_code")
    private ControlMeasureCode controlMeasureCode;

    @Column(name = "pollutant_code", length = 12)
    private String pollutantCode;

    @Column(name = "formula_indicator", nullable = false)
    private Boolean formulaIndicator;

    @Column(name = "control_indicator", nullable = false)
    private Boolean controlIndicator;

    @Column(name = "emission_factor", precision = 30, scale = 20)
    private BigDecimal emissionFactor;

    @Column(name = "emission_factor_formula", length = 200)
    private String emissionFactorFormula;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "note", length = 2000)
    private String note;

    @Column(name = "source", length = 200)
    private String source;

    @Column(name = "last_update_date")
    private Date lastUpdateDate;

    @Column(name = "revoked", nullable = false)
    private Boolean revoked;

    public UnitMeasureCode getEmissionsNumeratorUom() {
        return emissionsNumeratorUom;
    }

    public void setEmissionsNumeratorUom(UnitMeasureCode emissionsNumeratorUom) {
        this.emissionsNumeratorUom = emissionsNumeratorUom;
    }

    public UnitMeasureCode getEmissionsDenominatorUom() {
        return emissionsDenominatorUom;
    }

    public void setEmissionsDenominatorUom(UnitMeasureCode emissionsDenominatorUom) {
        this.emissionsDenominatorUom = emissionsDenominatorUom;
    }

    public CalculationParameterTypeCode getCalculationParameterTypeCode() {
        return calculationParameterTypeCode;
    }

    public void setCalculationParameterTypeCode(CalculationParameterTypeCode calculationParameterTypeCode) {
        this.calculationParameterTypeCode = calculationParameterTypeCode;
    }

    public CalculationMaterialCode getCalculationMaterialCode() {
        return calculationMaterialCode;
    }

    public void setCalculationMaterialCode(CalculationMaterialCode calculationMaterialCode) {
        this.calculationMaterialCode = calculationMaterialCode;
    }

    public String getPollutantCode() {
        return pollutantCode;
    }

    public void setPollutantCode(String pollutantCode) {
        this.pollutantCode = pollutantCode;
    }

    public Boolean isFormulaIndicator() {
        return formulaIndicator;
    }

    public void setFormulaIndicator(Boolean formulaIndicator) {
        this.formulaIndicator = formulaIndicator;
    }

    public Boolean isControlIndicator() {
        return controlIndicator;
    }

    public void setControlIndicator(Boolean controlIndicator) {
        this.controlIndicator = controlIndicator;
    }

    public BigDecimal getEmissionFactor() {
        return emissionFactor;
    }

    public void setEmissionFactor(BigDecimal emissionFactor) {
        this.emissionFactor = emissionFactor;
    }

    public String getEmissionFactorFormula() {
        return emissionFactorFormula;
    }

    public void setEmissionFactorFormula(String emissionFactorFormula) {
        this.emissionFactorFormula = emissionFactorFormula;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public ControlMeasureCode getControlMeasureCode() {
        return controlMeasureCode;
    }

    public void setControlMeasureCode(ControlMeasureCode controlMeasureCode) {
        this.controlMeasureCode = controlMeasureCode;
    }

	public Boolean getRevoked() {
		return revoked;
	}

	public void setRevoked(Boolean revoked) {
		this.revoked = revoked;
	}

}
