package gov.epa.cef.web.service.dto.json;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Schema(name = "EmissionFactor")
public class EmissionFactorJsonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id = 0L;
    private String description;
    private String source;

    @NotBlank
    private Long webfireId;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getWebfireId() {
        return webfireId;
    }

    public void setWebfireId(Long webfireId) {
        this.webfireId = webfireId;
    }
}