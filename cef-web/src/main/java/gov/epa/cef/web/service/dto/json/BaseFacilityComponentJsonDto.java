package gov.epa.cef.web.service.dto.json;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

public abstract class BaseFacilityComponentJsonDto implements Serializable {

    @Size(min = 1)
    protected List<IdentificationJsonDto> identification;

    @Size(max = 200)
    protected String description;

    protected LookupJsonDto statusCode;

    @PositiveOrZero
    protected Integer statusCodeYear;

    @Size(max = 400)
    protected String comment;

    public List<IdentificationJsonDto> getIdentification() {
        return identification;
    }

    public void setIdentification(List<IdentificationJsonDto> identification) {
        this.identification = identification;
    }

    public IdentificationJsonDto getMainIdentification() {
        return identification.get(0);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LookupJsonDto getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(LookupJsonDto statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getStatusCodeYear() {
        return statusCodeYear;
    }

    public void setStatusCodeYear(Integer statusCodeYear) {
        this.statusCodeYear = statusCodeYear;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
