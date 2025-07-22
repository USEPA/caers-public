package gov.epa.cef.web.service.dto.json;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Schema(name = "OperatingDetails")
public class OperatingDetailJsonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Max(8784)
    @Digits(integer = 4, fraction= 5)
    @PositiveOrZero
    private BigDecimal actualHoursPerPeriod;

    @NotBlank
    @Digits(integer = 2, fraction = 5)
    @DecimalMax("24.0")
    @PositiveOrZero
    private BigDecimal averageHoursPerDay;

    @NotBlank
    @Digits(integer = 1, fraction = 5)
    @DecimalMax("7.0")
    @PositiveOrZero
    private BigDecimal averageDaysPerWeek;

    @NotBlank
    @Digits(integer = 2, fraction = 5)
    @Max(52)
    @PositiveOrZero
    private BigDecimal averageWeeksPerPeriod;

    @NotBlank
    @Digits(integer = 3, fraction = 1)
    @DecimalMax("100.0")
    @PositiveOrZero
    private BigDecimal percentWinterActivity;

    @NotBlank
    @Digits(integer = 3, fraction = 1)
    @DecimalMax("100.0")
    @PositiveOrZero
    private BigDecimal percentSpringActivity;

    @NotBlank
    @Digits(integer = 3, fraction = 1)
    @DecimalMax("100.0")
    @PositiveOrZero
    private BigDecimal percentSummerActivity;

    @NotBlank
    @Digits(integer = 3, fraction = 1)
    @DecimalMax("100.0")
    @PositiveOrZero
    private BigDecimal percentFallActivity;

    private BigDecimal maxHoursPerDay;
    private BigDecimal maxHoursPerWeek;
    private BigDecimal maxHoursPerPeriod;

    public BigDecimal getActualHoursPerPeriod() {
        return actualHoursPerPeriod;
    }

    public void setActualHoursPerPeriod(BigDecimal actualHoursPerPeriod) {
        this.actualHoursPerPeriod = actualHoursPerPeriod;
    }

    public BigDecimal getAverageHoursPerDay() {
        return averageHoursPerDay;
    }

    public void setAverageHoursPerDay(BigDecimal averageHoursPerDay) {
        this.averageHoursPerDay = averageHoursPerDay;
    }

    public BigDecimal getAverageDaysPerWeek() {
        return averageDaysPerWeek;
    }

    public void setAverageDaysPerWeek(BigDecimal averageDaysPerWeek) {
        this.averageDaysPerWeek = averageDaysPerWeek;
    }

    public BigDecimal getAverageWeeksPerPeriod() {
        return averageWeeksPerPeriod;
    }

    public void setAverageWeeksPerPeriod(BigDecimal averageWeeksPerPeriod) {
        this.averageWeeksPerPeriod = averageWeeksPerPeriod;
    }

    public BigDecimal getPercentWinterActivity() {
        return percentWinterActivity;
    }

    public void setPercentWinterActivity(BigDecimal percentWinterActivity) {
        this.percentWinterActivity = percentWinterActivity;
    }

    public BigDecimal getPercentSpringActivity() {
        return percentSpringActivity;
    }

    public void setPercentSpringActivity(BigDecimal percentSpringActivity) {
        this.percentSpringActivity = percentSpringActivity;
    }

    public BigDecimal getPercentSummerActivity() {
        return percentSummerActivity;
    }

    public void setPercentSummerActivity(BigDecimal percentSummerActivity) {
        this.percentSummerActivity = percentSummerActivity;
    }

    public BigDecimal getPercentFallActivity() {
        return percentFallActivity;
    }

    public void setPercentFallActivity(BigDecimal percentFallActivity) {
        this.percentFallActivity = percentFallActivity;
    }

    public BigDecimal getMaxHoursPerDay() {
        return maxHoursPerDay;
    }

    public void setMaxHoursPerDay(BigDecimal maxHoursPerDay) {
        this.maxHoursPerDay = maxHoursPerDay;
    }

    public BigDecimal getMaxHoursPerWeek() {
        return maxHoursPerWeek;
    }

    public void setMaxHoursPerWeek(BigDecimal maxHoursPerWeek) {
        this.maxHoursPerWeek = maxHoursPerWeek;
    }

    public BigDecimal getMaxHoursPerPeriod() {
        return maxHoursPerPeriod;
    }

    public void setMaxHoursPerPeriod(BigDecimal maxHoursPerPeriod) {
        this.maxHoursPerPeriod = maxHoursPerPeriod;
    }
}
