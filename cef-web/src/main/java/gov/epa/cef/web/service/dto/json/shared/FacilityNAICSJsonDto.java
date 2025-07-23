package gov.epa.cef.web.service.dto.json.shared;

import gov.epa.cef.web.domain.NaicsCodeType;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Schema(name = "FacilityNAICS")
public class FacilityNAICSJsonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Max(999999)
    private Integer code;

    @NotNull
    private NaicsCodeType naicsCodeType;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public NaicsCodeType getNaicsCodeType() {
        return naicsCodeType;
    }

    public void setNaicsCodeType(NaicsCodeType naicsCodeType) {
        this.naicsCodeType = naicsCodeType;
    }

}
