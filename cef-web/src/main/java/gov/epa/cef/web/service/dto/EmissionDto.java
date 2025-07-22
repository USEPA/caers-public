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
import java.util.List;

public class EmissionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long reportingPeriodId;
    private PollutantDto pollutant;
    private Boolean totalManualEntry;
    private BigDecimal overallControlPercent;
    private BigDecimal totalEmissions;
    private BigDecimal previousYearTotalEmissions;
    private UnitMeasureCodeDto emissionsUomCode;
    private String previousEmissionsUomCode;
    private String previousEmissionsNumeratorUomCode;
    private String previousEmissionsDenominatorUomCode;
    private Boolean formulaIndicator;
    private BigDecimal emissionsFactor;
    private BigDecimal previousEmissionsFactor;
    private String emissionsFactorFormula;
    private String emissionsFactorText;
    private CalculationMethodCodeDto emissionsCalcMethodCode;
    private String comments;
    private String calculationComment;
    private BigDecimal calculatedEmissionsTons;
    private UnitMeasureCodeDto emissionsNumeratorUom;
    private UnitMeasureCodeDto emissionsDenominatorUom;
    private String emissionsFactorSource;
    private List<EmissionFormulaVariableDto> variables;
    private EmissionFactorDto webfireEf;
    private Long ghgEfId;
    private BigDecimal monthlyRate;
    private Boolean notReporting;
    private Boolean previousNotReporting;
    private String emissionsFactorDescription;

    public BigDecimal getPreviousEmissionsFactor() { return previousEmissionsFactor; }

    public void setPreviousEmissionsFactor(BigDecimal previousEmissionsFactor) {
        this.previousEmissionsFactor = previousEmissionsFactor;
    }

    public BigDecimal getPreviousYearTotalEmissions() { return previousYearTotalEmissions; }

    public void setPreviousYearTotalEmissions(BigDecimal previousYearTotalEmissions) {
        this.previousYearTotalEmissions = previousYearTotalEmissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportingPeriodId() {
        return reportingPeriodId;
    }

    public void setReportingPeriodId(Long reportingPeriodId) {
        this.reportingPeriodId = reportingPeriodId;
    }

    public PollutantDto getPollutant() {
        return pollutant;
    }

    public void setPollutant(PollutantDto pollutant) {
        this.pollutant = pollutant;
    }

    public Boolean getTotalManualEntry() {
        return totalManualEntry;
    }

    public void setTotalManualEntry(Boolean totalManualEntry) {
        this.totalManualEntry = totalManualEntry;
    }

    public BigDecimal getOverallControlPercent() {
        return overallControlPercent;
    }

    public void setOverallControlPercent(BigDecimal overallControlPercent) {
        this.overallControlPercent = overallControlPercent;
    }

    public BigDecimal getTotalEmissions() {
        return totalEmissions;
    }

    public void setTotalEmissions(BigDecimal totalEmissions) {
        this.totalEmissions = totalEmissions;
    }

    public UnitMeasureCodeDto getEmissionsUomCode() {
        return emissionsUomCode;
    }

    public void setEmissionsUomCode(UnitMeasureCodeDto emissionsUomCode) {
        this.emissionsUomCode = emissionsUomCode;
    }

    public Boolean getFormulaIndicator() {
        return formulaIndicator;
    }

    public void setFormulaIndicator(Boolean formulaIndicator) {
        this.formulaIndicator = formulaIndicator;
    }

    public BigDecimal getEmissionsFactor() {
        return emissionsFactor;
    }

    public void setEmissionsFactor(BigDecimal emissionsFactor) {
        this.emissionsFactor = emissionsFactor;
    }

    public String getEmissionsFactorFormula() {
        return emissionsFactorFormula;
    }

    public void setEmissionsFactorFormula(String emissionsFactorFormula) {
        this.emissionsFactorFormula = emissionsFactorFormula;
    }

    public String getEmissionsFactorText() {
        return emissionsFactorText;
    }

    public void setEmissionsFactorText(String emissionsFactorText) {
        this.emissionsFactorText = emissionsFactorText;
    }

    public CalculationMethodCodeDto getEmissionsCalcMethodCode() {
        return emissionsCalcMethodCode;
    }

    public void setEmissionsCalcMethodCode(CalculationMethodCodeDto emissionsCalcMethodCode) {
        this.emissionsCalcMethodCode = emissionsCalcMethodCode;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCalculationComment() {
        return calculationComment;
    }

    public void setCalculationComment(String calculationComment) {
        this.calculationComment = calculationComment;
    }

    public BigDecimal getCalculatedEmissionsTons() {
        return calculatedEmissionsTons;
    }

    public UnitMeasureCodeDto getEmissionsNumeratorUom() {
        return emissionsNumeratorUom;
    }

    public UnitMeasureCodeDto getEmissionsDenominatorUom() {
        return emissionsDenominatorUom;
    }

    public void setCalculatedEmissionsTons(BigDecimal calculatedEmissionsTons) {
        this.calculatedEmissionsTons = calculatedEmissionsTons;
    }

    public void setEmissionsNumeratorUom(UnitMeasureCodeDto emissionsNumeratorUom) {
        this.emissionsNumeratorUom = emissionsNumeratorUom;
    }

    public void setEmissionsDenominatorUom(UnitMeasureCodeDto emissionsDenominatorUom) {
        this.emissionsDenominatorUom = emissionsDenominatorUom;
    }

	public List<EmissionFormulaVariableDto> getVariables() {
        return variables;
    }

    public void setVariables(List<EmissionFormulaVariableDto> variables) {
        this.variables = variables;
    }

	public String getEmissionsFactorSource() {
		return emissionsFactorSource;
	}

	public void setEmissionsFactorSource(String emissionsFactorSource) {
		this.emissionsFactorSource = emissionsFactorSource;
	}

    public EmissionFactorDto getWebfireEf() {
        return webfireEf;
    }

    public void setWebfireEf(EmissionFactorDto webfireEf) {
        this.webfireEf = webfireEf;
    }

    public Long getGhgEfId() { return ghgEfId; }

    public void setGhgEfId(Long ghgEfId) { this.ghgEfId = ghgEfId; }

    public BigDecimal getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(BigDecimal monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public EmissionDto withId(Long id) {
        setId(id);
        return this;
    }

    public String getPreviousEmissionsUomCode() { return previousEmissionsUomCode; }

    public void setPreviousEmissionsUomCode(String previousEmissionsUomCode) {
        this.previousEmissionsUomCode = previousEmissionsUomCode;
    }

    public String getPreviousEmissionsNumeratorUomCode() {
        return previousEmissionsNumeratorUomCode;
    }

    public void setPreviousEmissionsNumeratorUomCode(String previousEmissionsNumeratorUomCode) {
        this.previousEmissionsNumeratorUomCode = previousEmissionsNumeratorUomCode;
    }

    public String getPreviousEmissionsDenominatorUomCode() {
        return previousEmissionsDenominatorUomCode;
    }

    public void setPreviousEmissionsDenominatorUomCode(String previousEmissionsDenominatorUomCode) {
        this.previousEmissionsDenominatorUomCode = previousEmissionsDenominatorUomCode;
    }

    public Boolean getNotReporting() {
        return notReporting;
    }

    public void setNotReporting(Boolean notReporting) {
        this.notReporting = notReporting;
    }

    public Boolean getPreviousNotReporting() {
        return previousNotReporting;
    }

    public void setPreviousNotReporting(Boolean previousNotReporting) {
        this.previousNotReporting = previousNotReporting;
    }

    public String getEmissionsFactorDescription() {
        return emissionsFactorDescription;
    }

    public void setEmissionsFactorDescription(String emissionsFactorDescription) {
        this.emissionsFactorDescription = emissionsFactorDescription;
    }

}
