package gov.epa.cef.web.service.dto.json;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "Control")
public class ControlJsonDto extends BaseFacilityComponentJsonDto {

    private static final long serialVersionUID = 1L;

    @NotNull
    private LookupJsonDto controlMeasureCode;

    @Digits(integer = 3, fraction = 1)
    @DecimalMax("100.0")
    @PositiveOrZero
    private BigDecimal percentControlEffectiveness;

    @Max(12)
    @Min(1)
    private Integer numberOperatingMonths;

    private LocalDate startDate;

    private LocalDate upgradeDate;

    private LocalDate endDate;

    @Size(max = 200)
    private String upgradeDescription;

    private List<ControlPollutantJsonDto> controlPollutants = new ArrayList<>();


    protected String controlIsReadOnly;

    private String controlManufactureMake;
    private String controlManufactureModel;

    public LookupJsonDto getControlMeasureCode() {
        return controlMeasureCode;
    }

    public void setControlMeasureCode(LookupJsonDto controlMeasureCode) {
        this.controlMeasureCode = controlMeasureCode;
    }

    public BigDecimal getPercentControlEffectiveness() {
        return percentControlEffectiveness;
    }

    public void setPercentControlEffectiveness(BigDecimal percentControlEffectiveness) {
        this.percentControlEffectiveness = percentControlEffectiveness;
    }

    public Integer getNumberOperatingMonths() {
        return numberOperatingMonths;
    }

    public void setNumberOperatingMonths(Integer numberOperatingMonths) {
        this.numberOperatingMonths = numberOperatingMonths;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getUpgradeDate() {
        return upgradeDate;
    }

    public void setUpgradeDate(LocalDate upgradeDate) {
        this.upgradeDate = upgradeDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getUpgradeDescription() {
        return upgradeDescription;
    }

    public void setUpgradeDescription(String upgradeDescription) {
        this.upgradeDescription = upgradeDescription;
    }

    public List<ControlPollutantJsonDto> getControlPollutants() {
        return controlPollutants;
    }

    public void setControlPollutants(List<ControlPollutantJsonDto> controlPollutants) {
        this.controlPollutants = controlPollutants;
    }

    public String getControlIsReadOnly() {
        return controlIsReadOnly;
    }

    public void setControlIsReadOnly(String controlIsReadOnly) {
        this.controlIsReadOnly = controlIsReadOnly;
    }

    public String getControlManufactureMake() {
        return controlManufactureMake;
    }

    public void setControlManufactureMake(String controlManufactureMake) {
        this.controlManufactureMake = controlManufactureMake;
    }

    public String getControlManufactureModel() {
        return controlManufactureModel;
    }

    public void setControlManufactureModel(String controlManufactureModel) {
        this.controlManufactureModel = controlManufactureModel;
    }
}
