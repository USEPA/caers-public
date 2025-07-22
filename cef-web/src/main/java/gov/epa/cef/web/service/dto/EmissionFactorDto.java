/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class EmissionFactorDto {

    private Long id;
    private UnitMeasureCodeDto emissionsNumeratorUom;
    private UnitMeasureCodeDto emissionsDenominatorUom;
    private CodeLookupDto calculationParameterTypeCode;
    private CodeLookupDto calculationMaterialCode;
    private CodeLookupDto controlMeasureCode;
    private CodeLookupDto controlMeasureCode2;
    private CodeLookupDto controlMeasureCode3;
    private CodeLookupDto controlMeasureCode4;
    private CodeLookupDto controlMeasureCode5;
    private String sccCode;
    private String pollutantCode;
    private Boolean formulaIndicator;
    private Boolean controlIndicator;
    private BigDecimal emissionFactor;
    private String emissionFactorFormula;
    private String description;
    private String note;
    private String source;
    private Boolean revoked;
    private Date revokedDate;
    private List<EmissionFormulaVariableCodeDto> variables;
    private String quality;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private String applicability;
    private String derivation;
    private Date dateUpdated;
    private Long webfireId;
    private Long ghgId;
    private String condition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UnitMeasureCodeDto getEmissionsNumeratorUom() {
        return emissionsNumeratorUom;
    }

    public void setEmissionsNumeratorUom(UnitMeasureCodeDto emissionsNumeratorUom) {
        this.emissionsNumeratorUom = emissionsNumeratorUom;
    }

    public UnitMeasureCodeDto getEmissionsDenominatorUom() {
        return emissionsDenominatorUom;
    }

    public void setEmissionsDenominatorUom(UnitMeasureCodeDto emissionsDenominatorUom) {
        this.emissionsDenominatorUom = emissionsDenominatorUom;
    }

    public CodeLookupDto getCalculationParameterTypeCode() {
        return calculationParameterTypeCode;
    }

    public void setCalculationParameterTypeCode(CodeLookupDto calculationParameterTypeCode) {
        this.calculationParameterTypeCode = calculationParameterTypeCode;
    }

    public CodeLookupDto getCalculationMaterialCode() {
        return calculationMaterialCode;
    }

    public void setCalculationMaterialCode(CodeLookupDto calculationMaterialCode) {
        this.calculationMaterialCode = calculationMaterialCode;
    }

    public CodeLookupDto getControlMeasureCode() {
        return controlMeasureCode;
    }

    public void setControlMeasureCode(CodeLookupDto controlMeasureCode) {
        this.controlMeasureCode = controlMeasureCode;
    }

    public CodeLookupDto getControlMeasureCode2() {
        return controlMeasureCode2;
    }

    public void setControlMeasureCode2(CodeLookupDto controlMeasureCode2) {
        this.controlMeasureCode2 = controlMeasureCode2;
    }

    public CodeLookupDto getControlMeasureCode3() {
        return controlMeasureCode3;
    }

    public void setControlMeasureCode3(CodeLookupDto controlMeasureCode3) {
        this.controlMeasureCode3 = controlMeasureCode3;
    }

    public CodeLookupDto getControlMeasureCode4() {
        return controlMeasureCode4;
    }

    public void setControlMeasureCode4(CodeLookupDto controlMeasureCode4) {
        this.controlMeasureCode4 = controlMeasureCode4;
    }

    public CodeLookupDto getControlMeasureCode5() {
        return controlMeasureCode5;
    }

    public void setControlMeasureCode5(CodeLookupDto controlMeasureCode5) {
        this.controlMeasureCode5 = controlMeasureCode5;
    }

    public String getSccCode() {
        return sccCode;
    }

    public void setSccCode(String sccCode) {
        this.sccCode = sccCode;
    }

    public String getPollutantCode() {
        return pollutantCode;
    }

    public void setPollutantCode(String pollutantCode) {
        this.pollutantCode = pollutantCode;
    }

    public Boolean getFormulaIndicator() {
        return formulaIndicator;
    }

    public void setFormulaIndicator(Boolean formulaIndicator) {
        this.formulaIndicator = formulaIndicator;
    }

    public Boolean getControlIndicator() {
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

    public List<EmissionFormulaVariableCodeDto> getVariables() {
        return variables;
    }

    public void setVariables(List<EmissionFormulaVariableCodeDto> variables) {
        this.variables = variables;
    }

	public Boolean getRevoked() {
		return revoked;
	}

	public void setRevoked(Boolean revoked) {
		this.revoked = revoked;
	}

    public Date getRevokedDate() {
        return revokedDate;
    }

    public void setRevokedDate(Date revokedDate) {
        this.revokedDate = revokedDate;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public BigDecimal getMinValue() { return minValue; }

    public void setMinValue(BigDecimal minValue) { this.minValue = minValue; }

    public BigDecimal getMaxValue() { return maxValue; }

    public void setMaxValue(BigDecimal maxValue) { this.maxValue = maxValue; }

    public String getApplicability() {
        return applicability;
    }

    public void setApplicability(String applicability) {
        this.applicability = applicability;
    }

    public String getDerivation() {
        return derivation;
    }

    public void setDerivation(String derivation) {
        this.derivation = derivation;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Long getWebfireId() {
        return webfireId;
    }

    public void setWebfireId(Long webfireId) {
        this.webfireId = webfireId;
    }

    public Long getGhgId() { return ghgId; }

    public void setGhgId(Long ghgId) { this.ghgId = ghgId; }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
