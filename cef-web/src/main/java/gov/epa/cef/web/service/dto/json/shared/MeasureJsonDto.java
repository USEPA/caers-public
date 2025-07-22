package gov.epa.cef.web.service.dto.json.shared;

import gov.epa.cef.web.service.dto.json.shared.reference.MeasureUnitJsonDto;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;

@Schema(name = "Measure")
public class MeasureJsonDto implements Serializable {

    @NotBlank
    @PositiveOrZero
    private BigDecimal value;

    @NotNull
    private MeasureUnitJsonDto unit;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public MeasureUnitJsonDto getUnit() {
        return unit;
    }

    public void setUnit(MeasureUnitJsonDto unit) {
        this.unit = unit;
    }
}
