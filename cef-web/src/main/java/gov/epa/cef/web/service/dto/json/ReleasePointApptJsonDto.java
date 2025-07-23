package gov.epa.cef.web.service.dto.json;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Schema(name = "ReleasePointAppt")
public class ReleasePointApptJsonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<IdentificationJsonDto> releasePointIdentification;
    private List<IdentificationJsonDto> pathIdentification;

    @NotBlank
    @Digits(integer = 3, fraction = 2)
    @DecimalMax("100.0")
    @PositiveOrZero
    private BigDecimal averagePercentEmissions;

    @Size(max = 400)
    protected String comment;


    protected String releasePointApportionmentIsUncontrolled;


    public List<IdentificationJsonDto> getReleasePointIdentification() {
        return releasePointIdentification;
    }

    public void setReleasePointIdentification(List<IdentificationJsonDto> releasePointIdentification) {
        this.releasePointIdentification = releasePointIdentification;
    }

    public IdentificationJsonDto getMainReleasePointIdentification() {
        return !releasePointIdentification.isEmpty() ? releasePointIdentification.get(0) : null;
    }

    public List<IdentificationJsonDto> getPathIdentification() {
        return pathIdentification;
    }

    public void setPathIdentification(List<IdentificationJsonDto> pathIdentification) {
        this.pathIdentification = pathIdentification;
    }

    public IdentificationJsonDto getMainPathIdentification() {
        return !pathIdentification.isEmpty() ? pathIdentification.get(0) : null;
    }

    public BigDecimal getAveragePercentEmissions() {
        return averagePercentEmissions;
    }

    public void setAveragePercentEmissions(BigDecimal averagePercentEmissions) {
        this.averagePercentEmissions = averagePercentEmissions;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReleasePointApportionmentIsUncontrolled() {
        return releasePointApportionmentIsUncontrolled;
    }

    public void setReleasePointApportionmentIsUncontrolled(String releasePointApportionmentIsUncontrolled) {
        this.releasePointApportionmentIsUncontrolled = releasePointApportionmentIsUncontrolled;
    }
}
