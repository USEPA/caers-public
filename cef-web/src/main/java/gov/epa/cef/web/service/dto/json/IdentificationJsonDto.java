package gov.epa.cef.web.service.dto.json;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Schema(name = "Identification")
public class IdentificationJsonDto {

    @NotBlank
    @Size(max = 20)
    protected String identifier;

    @Size(max = 20)
    protected String programSystemCode;

    protected LocalDate effectiveDate;
    protected LocalDate endDate;
    protected String identifierIsReadOnly;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getProgramSystemCode() {
        return programSystemCode;
    }

    public void setProgramSystemCode(String programSystemCode) {
        this.programSystemCode = programSystemCode;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getIdentifierIsReadOnly() {
        return identifierIsReadOnly;
    }

    public void setIdentifierIsReadOnly(String identifierIsReadOnly) {
        this.identifierIsReadOnly = identifierIsReadOnly;
    }
}
