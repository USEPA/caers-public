package gov.epa.cef.web.service.dto.json.shared.reference;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Schema(name = "MeasureUnit")
public class MeasureUnitJsonDto implements Serializable {

    @NotBlank
    @Size(max = 20)
    private String code;

    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
