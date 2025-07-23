package gov.epa.cef.web.service.dto.json;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Schema(name = "EmissionFormulaVariable")
public class EmissionFormulaVariableJsonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private LookupJsonDto emissionFormulaVariableCode;

    @NotBlank
    @PositiveOrZero
    private BigDecimal value;

    public LookupJsonDto getEmissionFormulaVariableCode() {
        return emissionFormulaVariableCode;
    }

    public void setEmissionFormulaVariableCode(LookupJsonDto emissionFormulaVariableCode) {
        this.emissionFormulaVariableCode = emissionFormulaVariableCode;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

}
