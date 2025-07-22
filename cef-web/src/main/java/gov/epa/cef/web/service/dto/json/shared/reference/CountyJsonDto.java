package gov.epa.cef.web.service.dto.json.shared.reference;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Schema(name = "County")
public class CountyJsonDto implements Serializable {

    @Size(max = 3)
    private String code;

    @Size(max = 43)
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
