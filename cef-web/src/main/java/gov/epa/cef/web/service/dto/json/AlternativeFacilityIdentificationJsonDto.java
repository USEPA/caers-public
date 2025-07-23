package gov.epa.cef.web.service.dto.json;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AlternativeFacilityIdentification")
public class AlternativeFacilityIdentificationJsonDto extends IdentificationJsonDto {

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
