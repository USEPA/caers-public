package gov.epa.cef.web.service.dto.json;

import gov.epa.cef.web.domain.ReportStatus;
import gov.epa.cef.web.domain.ValidationStatus;
import gov.epa.cef.web.service.dto.bulkUpload.FacilitySiteBulkUploadDto;
import io.swagger.v3.oas.annotations.media.Schema;
import net.exchangenetwork.schema.cer._2._0.EventDataType;
import net.exchangenetwork.schema.cer._2._0.FacilitySiteDataType;
import net.exchangenetwork.schema.cer._2._0.LocationDataType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "EmissionsReport")
public class EmissionsReportJsonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 30)
    private String agencyFacilityIdentifier;

    @NotBlank
    @Size(max = 20)
    private String programSystemCode;

    @NotBlank
    @PositiveOrZero
    protected Integer emissionsYear;

    private ReportStatus status;

    private ValidationStatus validationStatus;

    private String eisLastSubmissionStatus;

    @NotBlank
    private String version;

    @Size(min = 1)
    private List<FacilitySiteJsonDto> facilitySite = new ArrayList<>();


    protected String userIdentifier;
    protected String model;
    protected String modelVersion;
    protected LocalDate emissionsCreationDate;
    protected String submittalComment;
    protected List<LocationDataType> location = new ArrayList<>();
    protected List<EventDataType> event = new ArrayList<>();

    public String getAgencyFacilityIdentifier() {
        return agencyFacilityIdentifier;
    }

    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }

    public String getProgramSystemCode() {
        return programSystemCode;
    }

    public void setProgramSystemCode(String programSystemCode) {
        this.programSystemCode = programSystemCode;
    }

    public Integer getEmissionsYear() {
        return emissionsYear;
    }

    public void setEmissionsYear(Integer emissionsYear) {
        this.emissionsYear = emissionsYear;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public ValidationStatus getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(ValidationStatus validationStatus) {
        this.validationStatus = validationStatus;
    }

    public String getEisLastSubmissionStatus() {
        return eisLastSubmissionStatus;
    }

    public void setEisLastSubmissionStatus(String eisLastSubmissionStatus) {
        this.eisLastSubmissionStatus = eisLastSubmissionStatus;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<FacilitySiteJsonDto> getFacilitySite() {
        return facilitySite;
    }

    public void setFacilitySite(List<FacilitySiteJsonDto> facilitySite) {
        this.facilitySite = facilitySite;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public LocalDate getEmissionsCreationDate() {
        return emissionsCreationDate;
    }

    public void setEmissionsCreationDate(LocalDate emissionsCreationDate) {
        this.emissionsCreationDate = emissionsCreationDate;
    }

    public String getSubmittalComment() {
        return submittalComment;
    }

    public void setSubmittalComment(String submittalComment) {
        this.submittalComment = submittalComment;
    }

    public List<LocationDataType> getLocation() {
        return location;
    }

    public void setLocation(List<LocationDataType> location) {
        this.location = location;
    }

    public List<EventDataType> getEvent() {
        return event;
    }

    public void setEvent(List<EventDataType> event) {
        this.event = event;
    }
}
