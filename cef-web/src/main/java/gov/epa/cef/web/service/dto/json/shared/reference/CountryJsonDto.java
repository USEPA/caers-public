package gov.epa.cef.web.service.dto.json.shared.reference;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Schema(name = "Country")
public class CountryJsonDto implements Serializable {

    @Size(max = 10)
    private String code;

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
