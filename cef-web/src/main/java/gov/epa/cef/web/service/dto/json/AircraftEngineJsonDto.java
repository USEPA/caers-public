package gov.epa.cef.web.service.dto.json;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Schema(name = "AircraftEngineTypeCode")
public class AircraftEngineJsonDto implements Serializable {

    @NotBlank
    @Size(max = 10)
    private String code;
    private String faaAircraftType;
    private String edmsAccode;
    private String engineManufacturer;
    private String engine;
    private String edmsUid;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFaaAircraftType() {
        return faaAircraftType;
    }

    public void setFaaAircraftType(String faaAircraftType) {
        this.faaAircraftType = faaAircraftType;
    }

    public String getEdmsAccode() {
        return edmsAccode;
    }

    public void setEdmsAccode(String edmsAccode) {
        this.edmsAccode = edmsAccode;
    }

    public String getEngineManufacturer() {
        return engineManufacturer;
    }

    public void setEngineManufacturer(String engineManufacturer) {
        this.engineManufacturer = engineManufacturer;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getEdmsUid() {
        return edmsUid;
    }

    public void setEdmsUid(String edmsUid) {
        this.edmsUid = edmsUid;
    }
}
