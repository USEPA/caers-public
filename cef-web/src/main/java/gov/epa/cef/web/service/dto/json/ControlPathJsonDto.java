package gov.epa.cef.web.service.dto.json;

import gov.epa.cef.web.service.dto.bulkUpload.ControlAssignmentBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlPollutantBulkUploadDto;
import io.swagger.v3.oas.annotations.media.Schema;
import net.exchangenetwork.schema.cer._2._0.IdentificationDataType;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "ControlPath")
public class ControlPathJsonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(min = 1)
    protected List<IdentificationJsonDto> identification;

    @Size(max = 200)
    private String description;

    @Digits(integer = 3, fraction = 1)
    @DecimalMax("100.0")
    @PositiveOrZero
    private BigDecimal percentPathEffectiveness;

    private List<ControlAssignmentJsonDto> controlPathDefinition = new ArrayList<>();

    private List<ControlPollutantJsonDto> controlPathPollutants = new ArrayList<>();

    private String name;

    protected String pathIsReadOnly;

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

    public BigDecimal getPercentPathEffectiveness() {
        return percentPathEffectiveness;
    }

    public void setPercentPathEffectiveness(BigDecimal percentPathEffectiveness) {
        this.percentPathEffectiveness = percentPathEffectiveness;
    }

    public List<ControlAssignmentJsonDto> getControlPathDefinition() {
        return controlPathDefinition;
    }

    public void setControlPathDefinition(List<ControlAssignmentJsonDto> controlPathDefinition) {
        this.controlPathDefinition = controlPathDefinition;
    }

    public List<ControlPollutantJsonDto> getControlPathPollutants() {
        return controlPathPollutants;
    }

    public void setControlPathPollutants(List<ControlPollutantJsonDto> controlPathPollutants) {
        this.controlPathPollutants = controlPathPollutants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathIsReadOnly() {
        return pathIsReadOnly;
    }

    public void setPathIsReadOnly(String pathIsReadOnly) {
        this.pathIsReadOnly = pathIsReadOnly;
    }
}
