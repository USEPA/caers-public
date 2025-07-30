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

public class MonthlyReportingEmissionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private PollutantDto pollutant;
    private Boolean totalManualEntry;
    private BigDecimal totalEmissions;
    private UnitMeasureCodeDto emissionsUomCode;
    private BigDecimal emissionsFactor;
    private CalculationMethodCodeDto emissionsCalcMethodCode;
    private String period;
    private BigDecimal overallControlPercent;
    private BigDecimal monthlyRate;
    private UnitMeasureCodeDto emissionsNumeratorUom;
    private UnitMeasureCodeDto emissionsDenominatorUom;
    private Long annualEmissionId;

    private Boolean calculationFailed;
    private String calculationFailureMessage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getEmissionsFactor() {
        return emissionsFactor;
    }

    public void setEmissionsFactor(BigDecimal emissionsFactor) {
        this.emissionsFactor = emissionsFactor;
    }

    public CalculationMethodCodeDto getEmissionsCalcMethodCode() {
        return emissionsCalcMethodCode;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public BigDecimal getOverallControlPercent() {
        return overallControlPercent;
    }

    public void setOverallControlPercent(BigDecimal overallControlPercent) {
        this.overallControlPercent = overallControlPercent;
    }

    public BigDecimal getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(BigDecimal monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public void setEmissionsCalcMethodCode(CalculationMethodCodeDto emissionsCalcMethodCode) {
        this.emissionsCalcMethodCode = emissionsCalcMethodCode;
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

    public Long getAnnualEmissionId() {
        return annualEmissionId;
    }

    public void setAnnualEmissionId(Long annualEmissionId) {
        this.annualEmissionId = annualEmissionId;
    }

    public Boolean getCalculationFailed() {
        return calculationFailed;
    }

    public void setCalculationFailed(Boolean calculationFailed) {
        this.calculationFailed = calculationFailed;
    }

    public String getCalculationFailureMessage() {
        return calculationFailureMessage;
    }

    public void setCalculationFailureMessage(String calculationFailureMessage) {
        this.calculationFailureMessage = calculationFailureMessage;
    }
}
