package gov.epa.cef.web.service.dto.json;

import io.swagger.v3.oas.annotations.media.Schema;
import net.exchangenetwork.schema.cer._2._0.IdentificationDataType;

import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Schema(name = "ControlAssignment")
public class ControlAssignmentJsonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    protected List<IdentificationJsonDto> controlIdentification;

    protected List<IdentificationJsonDto> pathIdentification;

    @NotBlank
    @Digits(integer = 3, fraction = 2)
    @DecimalMax("100.00")
    @PositiveOrZero
    private BigDecimal averagePercentApportionment;

    @NotBlank
    @PositiveOrZero
    private Integer sequenceNumber;

    public List<IdentificationJsonDto> getControlIdentification() {
        return controlIdentification;
    }

    public void setControlIdentification(List<IdentificationJsonDto> controlIdentification) {
        this.controlIdentification = controlIdentification;
    }

    public IdentificationJsonDto getMainControlIdentification() {
        return !controlIdentification.isEmpty() ? controlIdentification.get(0) : null;
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

    public BigDecimal getAveragePercentApportionment() {
        return averagePercentApportionment;
    }

    public void setAveragePercentApportionment(BigDecimal averagePercentApportionment) {
        this.averagePercentApportionment = averagePercentApportionment;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
