package gov.epa.cef.web.service.dto.json;

import gov.epa.cef.web.service.dto.json.shared.MeasureJsonDto;
import io.swagger.v3.oas.annotations.media.Schema;
import net.exchangenetwork.schema.cer._2._0.SupplementalCalculationParameterDataType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "ReportingPeriod")
public class ReportingPeriodJsonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private MeasureJsonDto fuelUse;

    private LookupJsonDto fuelUseMaterialCode;

    private MeasureJsonDto heatContent;

    @NotNull
    private LookupJsonDto reportingPeriodTypeCode;

    @NotNull
    private LookupJsonDto emissionsOperatingTypeCode;

    @NotNull
    private LookupJsonDto calculationParameterTypeCode;

    @NotNull
    private MeasureJsonDto calculationParameter;

    @NotNull
    private LookupJsonDto calculationMaterialCode;

    @Size(max = 400)
    private String comment;

    @Size(min = 1)
    private List<OperatingDetailJsonDto> operatingDetails = new ArrayList<>();
    private List<EmissionJsonDto> emissions = new ArrayList<>();

    protected LocalDate startDate;
    protected LocalDate endDate;
    protected Integer calculationDataYear;
    protected String calculationDataSource;
    protected List<SupplementalCalculationParameterDataType> supplementalCalculationParameter;

    public MeasureJsonDto getFuelUse() {
        return fuelUse;
    }

    public void setFuelUse(MeasureJsonDto fuelUse) {
        this.fuelUse = fuelUse;
    }

    public LookupJsonDto getFuelUseMaterialCode() {
        return fuelUseMaterialCode;
    }

    public void setFuelUseMaterialCode(LookupJsonDto fuelUseMaterialCode) {
        this.fuelUseMaterialCode = fuelUseMaterialCode;
    }

    public MeasureJsonDto getHeatContent() {
        return heatContent;
    }

    public void setHeatContent(MeasureJsonDto heatContent) {
        this.heatContent = heatContent;
    }

    public LookupJsonDto getReportingPeriodTypeCode() {
        return reportingPeriodTypeCode;
    }

    public void setReportingPeriodTypeCode(LookupJsonDto reportingPeriodTypeCode) {
        this.reportingPeriodTypeCode = reportingPeriodTypeCode;
    }

    public LookupJsonDto getEmissionsOperatingTypeCode() {
        return emissionsOperatingTypeCode;
    }

    public void setEmissionsOperatingTypeCode(LookupJsonDto emissionsOperatingTypeCode) {
        this.emissionsOperatingTypeCode = emissionsOperatingTypeCode;
    }

    public LookupJsonDto getCalculationParameterTypeCode() {
        return calculationParameterTypeCode;
    }

    public void setCalculationParameterTypeCode(LookupJsonDto calculationParameterTypeCode) {
        this.calculationParameterTypeCode = calculationParameterTypeCode;
    }

    public MeasureJsonDto getCalculationParameter() {
        return calculationParameter;
    }

    public void setCalculationParameter(MeasureJsonDto calculationParameter) {
        this.calculationParameter = calculationParameter;
    }

    public LookupJsonDto getCalculationMaterialCode() {
        return calculationMaterialCode;
    }

    public void setCalculationMaterialCode(LookupJsonDto calculationMaterialCode) {
        this.calculationMaterialCode = calculationMaterialCode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<OperatingDetailJsonDto> getOperatingDetails() {
        return operatingDetails;
    }

    public void setOperatingDetails(List<OperatingDetailJsonDto> operatingDetails) {
        this.operatingDetails = operatingDetails;
    }

    public List<EmissionJsonDto> getEmissions() {
        return emissions;
    }

    public void setEmissions(List<EmissionJsonDto> emissions) {
        this.emissions = emissions;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getCalculationDataYear() {
        return calculationDataYear;
    }

    public void setCalculationDataYear(Integer calculationDataYear) {
        this.calculationDataYear = calculationDataYear;
    }

    public String getCalculationDataSource() {
        return calculationDataSource;
    }

    public void setCalculationDataSource(String calculationDataSource) {
        this.calculationDataSource = calculationDataSource;
    }

    public List<SupplementalCalculationParameterDataType> getSupplementalCalculationParameter() {
        return supplementalCalculationParameter;
    }

    public void setSupplementalCalculationParameter(List<SupplementalCalculationParameterDataType> supplementalCalculationParameter) {
        this.supplementalCalculationParameter = supplementalCalculationParameter;
    }
}
