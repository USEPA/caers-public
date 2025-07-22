package gov.epa.cef.web.service.dto.json;

import gov.epa.cef.web.service.dto.json.shared.GeographicCoordinatesJsonDto;
import gov.epa.cef.web.service.dto.json.shared.MeasureJsonDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.media.Schema;
import net.exchangenetwork.schema.cer._2._0.GeographicCoordinatesDataType;

import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "ReleasePoint")
public class ReleasePointJsonDto extends BaseFacilityComponentJsonDto {

    private static final long serialVersionUID = 1L;

    private MeasureJsonDto exitGasFlowRate;

    @Max(4000)
    @Min(-30)
    private Integer exitGasTemperature;

    @Schema(description = "Value must be in numeric format '{5}.{3]")
    private MeasureJsonDto exitGasVelocity;

    private MeasureJsonDto fenceLineDistance;

    @Max(89)
    @PositiveOrZero
    private Long fugitiveAngle;

    private MeasureJsonDto fugitiveHeight;
    private MeasureJsonDto stackDiameter;
    private MeasureJsonDto stackHeight;
    private MeasureJsonDto stackWidth;
    private MeasureJsonDto stackLength;

    @NotNull
    private LookupJsonDto releasePointTypeCode;

    protected GeographicCoordinatesJsonDto releasePointGeographicCoordinates;


    protected String releasePointIsReadOnly;
    protected String stackHeightIsReadOnly;
    protected String stackDiameterIsReadOnly;
    protected String widthIsReadOnly;
    protected String lengthIsReadOnly;
    protected String exitGasVelocityIsReadOnly;
    protected String exitGasFlowRateIsReadOnly;
    protected String exitGasTemperatureIsReadOnly;

    protected LocalDate permitStatusBeginYear;
    protected LocalDate permitStatusEndYear;
    private String exitGasTemperatureAmbient;
    private BigDecimal exhaustMoisturePercent;
    private String bypassFlag;

    public MeasureJsonDto getExitGasFlowRate() {
        return exitGasFlowRate;
    }

    public void setExitGasFlowRate(MeasureJsonDto exitGasFlowRate) {
        this.exitGasFlowRate = exitGasFlowRate;
    }

    public Integer getExitGasTemperature() {
        return exitGasTemperature;
    }

    public void setExitGasTemperature(Integer exitGasTemperature) {
        this.exitGasTemperature = exitGasTemperature;
    }

    public MeasureJsonDto getExitGasVelocity() {
        return exitGasVelocity;
    }

    public void setExitGasVelocity(MeasureJsonDto exitGasVelocity) {
        this.exitGasVelocity = exitGasVelocity;
    }

    public MeasureJsonDto getFenceLineDistance() {
        return fenceLineDistance;
    }

    public void setFenceLineDistance(MeasureJsonDto fenceLineDistance) {
        this.fenceLineDistance = fenceLineDistance;
    }

    public Long getFugitiveAngle() {
        return fugitiveAngle;
    }

    public void setFugitiveAngle(Long fugitiveAngle) {
        this.fugitiveAngle = fugitiveAngle;
    }

    public MeasureJsonDto getFugitiveHeight() {
        return fugitiveHeight;
    }

    public void setFugitiveHeight(MeasureJsonDto fugitiveHeight) {
        this.fugitiveHeight = fugitiveHeight;
    }

    public MeasureJsonDto getStackDiameter() {
        return stackDiameter;
    }

    public void setStackDiameter(MeasureJsonDto stackDiameter) {
        this.stackDiameter = stackDiameter;
    }

    public MeasureJsonDto getStackHeight() {
        return stackHeight;
    }

    public void setStackHeight(MeasureJsonDto stackHeight) {
        this.stackHeight = stackHeight;
    }

    public MeasureJsonDto getStackWidth() {
        return stackWidth;
    }

    public void setStackWidth(MeasureJsonDto stackWidth) {
        this.stackWidth = stackWidth;
    }

    public MeasureJsonDto getStackLength() {
        return stackLength;
    }

    public void setStackLength(MeasureJsonDto stackLength) {
        this.stackLength = stackLength;
    }

    public LookupJsonDto getReleasePointTypeCode() {
        return releasePointTypeCode;
    }

    public void setReleasePointTypeCode(LookupJsonDto releasePointTypeCode) {
        this.releasePointTypeCode = releasePointTypeCode;
    }

    public GeographicCoordinatesJsonDto getReleasePointGeographicCoordinates() {
        return releasePointGeographicCoordinates;
    }

    public void setReleasePointGeographicCoordinates(GeographicCoordinatesJsonDto releasePointGeographicCoordinates) {
        this.releasePointGeographicCoordinates = releasePointGeographicCoordinates;
    }

    public String getReleasePointIsReadOnly() {
        return releasePointIsReadOnly;
    }

    public void setReleasePointIsReadOnly(String releasePointIsReadOnly) {
        this.releasePointIsReadOnly = releasePointIsReadOnly;
    }

    public String getStackHeightIsReadOnly() {
        return stackHeightIsReadOnly;
    }

    public void setStackHeightIsReadOnly(String stackHeightIsReadOnly) {
        this.stackHeightIsReadOnly = stackHeightIsReadOnly;
    }

    public String getStackDiameterIsReadOnly() {
        return stackDiameterIsReadOnly;
    }

    public void setStackDiameterIsReadOnly(String stackDiameterIsReadOnly) {
        this.stackDiameterIsReadOnly = stackDiameterIsReadOnly;
    }

    public String getWidthIsReadOnly() {
        return widthIsReadOnly;
    }

    public void setWidthIsReadOnly(String widthIsReadOnly) {
        this.widthIsReadOnly = widthIsReadOnly;
    }

    public String getLengthIsReadOnly() {
        return lengthIsReadOnly;
    }

    public void setLengthIsReadOnly(String lengthIsReadOnly) {
        this.lengthIsReadOnly = lengthIsReadOnly;
    }

    public String getExitGasVelocityIsReadOnly() {
        return exitGasVelocityIsReadOnly;
    }

    public void setExitGasVelocityIsReadOnly(String exitGasVelocityIsReadOnly) {
        this.exitGasVelocityIsReadOnly = exitGasVelocityIsReadOnly;
    }

    public String getExitGasFlowRateIsReadOnly() {
        return exitGasFlowRateIsReadOnly;
    }

    public void setExitGasFlowRateIsReadOnly(String exitGasFlowRateIsReadOnly) {
        this.exitGasFlowRateIsReadOnly = exitGasFlowRateIsReadOnly;
    }

    public String getExitGasTemperatureIsReadOnly() {
        return exitGasTemperatureIsReadOnly;
    }

    public void setExitGasTemperatureIsReadOnly(String exitGasTemperatureIsReadOnly) {
        this.exitGasTemperatureIsReadOnly = exitGasTemperatureIsReadOnly;
    }

    public LocalDate getPermitStatusBeginYear() {
        return permitStatusBeginYear;
    }

    public void setPermitStatusBeginYear(LocalDate permitStatusBeginYear) {
        this.permitStatusBeginYear = permitStatusBeginYear;
    }

    public LocalDate getPermitStatusEndYear() {
        return permitStatusEndYear;
    }

    public void setPermitStatusEndYear(LocalDate permitStatusEndYear) {
        this.permitStatusEndYear = permitStatusEndYear;
    }

    public String getExitGasTemperatureAmbient() {
        return exitGasTemperatureAmbient;
    }

    public void setExitGasTemperatureAmbient(String exitGasTemperatureAmbient) {
        this.exitGasTemperatureAmbient = exitGasTemperatureAmbient;
    }

    public BigDecimal getExhaustMoisturePercent() {
        return exhaustMoisturePercent;
    }

    public void setExhaustMoisturePercent(BigDecimal exhaustMoisturePercent) {
        this.exhaustMoisturePercent = exhaustMoisturePercent;
    }

    public String getBypassFlag() {
        return bypassFlag;
    }

    public void setBypassFlag(String bypassFlag) {
        this.bypassFlag = bypassFlag;
    }
}
