package gov.epa.cef.web.service.dto.json;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Schema(name = "ControlPollutant")
public class ControlPollutantJsonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private PollutantJsonDto pollutantCode;

    @NotBlank
    @Digits(integer = 3, fraction = 1)
    @DecimalMax("100.0")
    @PositiveOrZero
    private BigDecimal percentControlMeasuresReductionEfficiency;

    public PollutantJsonDto getPollutantCode() {
        return pollutantCode;
    }

    public void setPollutantCode(PollutantJsonDto pollutantCode) {
        this.pollutantCode = pollutantCode;
    }

    public BigDecimal getPercentControlMeasuresReductionEfficiency() {
        return percentControlMeasuresReductionEfficiency;
    }

    public void setPercentControlMeasuresReductionEfficiency(BigDecimal percentControlMeasuresReductionEfficiency) {
        this.percentControlMeasuresReductionEfficiency = percentControlMeasuresReductionEfficiency;
    }
}
