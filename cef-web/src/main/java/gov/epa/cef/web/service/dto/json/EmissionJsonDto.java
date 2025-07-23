package gov.epa.cef.web.service.dto.json;

import gov.epa.cef.web.service.dto.json.shared.MeasureJsonDto;
import gov.epa.cef.web.service.dto.json.shared.reference.MeasureUnitJsonDto;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "Emission")
public class EmissionJsonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 100)
    private String emissionFactorFormula;

    private List<EmissionFormulaVariableJsonDto> emissionFormulaVariables = new ArrayList<>();

    @Digits(integer = 2, fraction = 6)
    @DecimalMax("99.999999")
    @PositiveOrZero
    private BigDecimal overallControlPercent;

    private boolean totalManualEntry;

    @PositiveOrZero
    private BigDecimal calculatedEmissionsTons;

    @Size(max = 4000)
    private String calculationComment;


    @NotNull
    private PollutantJsonDto pollutantCode;

    @NotNull
    private MeasureJsonDto totalEmissions;

    @NotNull
    private LookupJsonDto emissionCalculationMethodCode;

    @PositiveOrZero
    private BigDecimal emissionFactor;

    @Size(max = 100)
    private String emissionFactorText;

    private MeasureUnitJsonDto emissionFactorNumeratorUnitofMeasureCode;

    private MeasureUnitJsonDto emissionFactorDenominatorUnitofMeasureCode;

    @Size(max = 400)
    private String comment;

    private boolean notReporting;

    private String emissionsFactorDescription;

    private LocalDate stackTestDate;

    public String getEmissionFactorFormula() {
        return emissionFactorFormula;
    }

    public void setEmissionFactorFormula(String emissionFactorFormula) {
        this.emissionFactorFormula = emissionFactorFormula;
    }

    public List<EmissionFormulaVariableJsonDto> getEmissionFormulaVariables() {
        return emissionFormulaVariables;
    }

    public void setEmissionFormulaVariables(List<EmissionFormulaVariableJsonDto> emissionFormulaVariables) {
        this.emissionFormulaVariables = emissionFormulaVariables;
    }

    public BigDecimal getOverallControlPercent() {
        return overallControlPercent;
    }

    public void setOverallControlPercent(BigDecimal overallControlPercent) {
        this.overallControlPercent = overallControlPercent;
    }

    public boolean isTotalManualEntry() {
        return totalManualEntry;
    }

    public void setTotalManualEntry(boolean totalManualEntry) {
        this.totalManualEntry = totalManualEntry;
    }

    public BigDecimal getCalculatedEmissionsTons() {
        return calculatedEmissionsTons;
    }

    public void setCalculatedEmissionsTons(BigDecimal calculatedEmissionsTons) {
        this.calculatedEmissionsTons = calculatedEmissionsTons;
    }

    public String getCalculationComment() {
        return calculationComment;
    }

    public void setCalculationComment(String calculationComment) {
        this.calculationComment = calculationComment;
    }

    public PollutantJsonDto getPollutantCode() {
        return pollutantCode;
    }

    public void setPollutantCode(PollutantJsonDto pollutantCode) {
        this.pollutantCode = pollutantCode;
    }

    public MeasureJsonDto getTotalEmissions() {
        return totalEmissions;
    }

    public void setTotalEmissions(MeasureJsonDto totalEmissions) {
        this.totalEmissions = totalEmissions;
    }

    public LookupJsonDto getEmissionCalculationMethodCode() {
        return emissionCalculationMethodCode;
    }

    public void setEmissionCalculationMethodCode(LookupJsonDto emissionCalculationMethodCode) {
        this.emissionCalculationMethodCode = emissionCalculationMethodCode;
    }

    public BigDecimal getEmissionFactor() {
        return emissionFactor;
    }

    public void setEmissionFactor(BigDecimal emissionFactor) {
        this.emissionFactor = emissionFactor;
    }

    public String getEmissionFactorText() {
        return emissionFactorText;
    }

    public void setEmissionFactorText(String emissionFactorText) {
        this.emissionFactorText = emissionFactorText;
    }

    public MeasureUnitJsonDto getEmissionFactorNumeratorUnitofMeasureCode() {
        return emissionFactorNumeratorUnitofMeasureCode;
    }

    public void setEmissionFactorNumeratorUnitofMeasureCode(MeasureUnitJsonDto emissionFactorNumeratorUnitofMeasureCode) {
        this.emissionFactorNumeratorUnitofMeasureCode = emissionFactorNumeratorUnitofMeasureCode;
    }

    public MeasureUnitJsonDto getEmissionFactorDenominatorUnitofMeasureCode() {
        return emissionFactorDenominatorUnitofMeasureCode;
    }

    public void setEmissionFactorDenominatorUnitofMeasureCode(MeasureUnitJsonDto emissionFactorDenominatorUnitofMeasureCode) {
        this.emissionFactorDenominatorUnitofMeasureCode = emissionFactorDenominatorUnitofMeasureCode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getStackTestDate() {
        return stackTestDate;
    }

    public void setStackTestDate(LocalDate stackTestDate) {
        this.stackTestDate = stackTestDate;
    }

    public boolean getNotReporting() {
        return notReporting;
    }

    public void setNotReporting(boolean notReporting) {
        this.notReporting = notReporting;
    }

    public String getEmissionsFactorDescription() {
        return emissionsFactorDescription;
    }

    public void setEmissionsFactorDescription(String emissionsFactorDescription) {
        this.emissionsFactorDescription = emissionsFactorDescription;
    }
}
