package gov.epa.cef.web.service.dto.json;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Schema(name = "FacilityIdentification")
public class FacilityIdentificationJsonDto extends IdentificationJsonDto {


    @NotBlank
    @Size(max = 30)
    protected String identifier;

    protected LookupJsonDto tribalCode;

    protected String stateAndCountyFIPSCode;
    protected String stateAndCountryFIPSCode;

    public LookupJsonDto getTribalCode() {
        return tribalCode;
    }

    public void setTribalCode(LookupJsonDto tribalCode) {
        this.tribalCode = tribalCode;
    }

    public String getStateAndCountyFIPSCode() {
        return stateAndCountyFIPSCode;
    }

    public void setStateAndCountyFIPSCode(String stateAndCountyFIPSCode) {
        this.stateAndCountyFIPSCode = stateAndCountyFIPSCode;
    }

    public String getStateAndCountryFIPSCode() {
        return stateAndCountryFIPSCode;
    }

    public void setStateAndCountryFIPSCode(String stateAndCountryFIPSCode) {
        this.stateAndCountryFIPSCode = stateAndCountryFIPSCode;
    }
}
