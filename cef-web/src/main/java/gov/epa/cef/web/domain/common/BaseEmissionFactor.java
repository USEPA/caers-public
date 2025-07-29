/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
