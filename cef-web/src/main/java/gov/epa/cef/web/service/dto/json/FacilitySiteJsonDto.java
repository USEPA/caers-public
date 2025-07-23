package gov.epa.cef.web.service.dto.json;

import gov.epa.cef.web.service.dto.json.shared.AddressJsonDto;
import gov.epa.cef.web.service.dto.json.shared.FacilityNAICSJsonDto;
import gov.epa.cef.web.service.dto.json.shared.GeographicCoordinatesJsonDto;
import gov.epa.cef.web.service.dto.json.shared.MeasureJsonDto;
import io.swagger.v3.oas.annotations.media.Schema;
import net.exchangenetwork.schema.cer._2._0.AlternativeFacilityNameDataType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "FacilitySite")
public class FacilitySiteJsonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 22)
    private String eisProgramId;

    @Size(max = 400)
    private String comment;

    @Size(max = 20)
    protected String facilityCategoryCode;

    @NotBlank
    @Size(max = 80)
    protected String name;

    @Size(max = 100)
    protected String description;

    protected LookupJsonDto facilitySourceTypeCode;

    @NotNull
    protected LookupJsonDto statusCode;

    @NotBlank
    @PositiveOrZero
    protected Integer statusCodeYear;

    @Size(min = 1)
    protected List<FacilityIdentificationJsonDto> facilityIdentification = new ArrayList<>();

    protected List<AlternativeFacilityIdentificationJsonDto> alternativeFacilityIdentification = new ArrayList<>();

    protected List<AlternativeFacilityNameDataType> alternativeFacilityName = new ArrayList<>();

    @Size(min = 1)
    protected List<AddressJsonDto> facilitySiteAddress = new ArrayList<>();

    @NotNull
    private AddressJsonDto mailingAddress;

    private GeographicCoordinatesJsonDto facilitySiteGeographicCoordinates;

    private List<EmissionsUnitJsonDto> emissionsUnits = new ArrayList<>();
    private List<ReleasePointJsonDto> releasePoints = new ArrayList<>();
    private List<ControlPathJsonDto> controlPaths = new ArrayList<>();
    private List<ControlJsonDto> controls = new ArrayList<>();
    private List<FacilityNAICSJsonDto> facilityNAICS = new ArrayList<>();
    private List<FacilitySiteContactJsonDto> facilityContacts = new ArrayList<>();


    protected String hapFacilityCategoryCode;
    protected String coordinateTolerance;
    protected String facilitySiteComment;
    protected String facilityNameIsReadOnly;
    protected String locationIsReadOnly;
    protected String primaryNAICSIsReadOnly;

    protected LocalDate permitStatusBeginYear;
    protected LocalDate permitStatusEndYear;
    private MeasureJsonDto maximumTheoreticalEmissions;
    private MeasureJsonDto potentialToEmit;
    private String isBillable;
    private String hasSensitiveData;
    private String permitNumber;
    private List<String> permitTypes = new ArrayList<>();;
    private Integer employeeCount;
    private String constructionLimitDescription;
    private String sltFacilityCategoryCode;

    public String getEisProgramId() {
        return eisProgramId;
    }

    public void setEisProgramId(String eisProgramId) {
        this.eisProgramId = eisProgramId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFacilityCategoryCode() {
        return facilityCategoryCode;
    }

    public void setFacilityCategoryCode(String facilityCategoryCode) {
        this.facilityCategoryCode = facilityCategoryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LookupJsonDto getFacilitySourceTypeCode() {
        return facilitySourceTypeCode;
    }

    public void setFacilitySourceTypeCode(LookupJsonDto facilitySourceTypeCode) {
        this.facilitySourceTypeCode = facilitySourceTypeCode;
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

    public List<FacilityIdentificationJsonDto> getFacilityIdentification() {
        return facilityIdentification;
    }

    public void setFacilityIdentification(List<FacilityIdentificationJsonDto> facilityIdentification) {
        this.facilityIdentification = facilityIdentification;
    }

    public FacilityIdentificationJsonDto getMainFacilityIdentification() {
        return facilityIdentification.get(0);
    }


    public List<AlternativeFacilityIdentificationJsonDto> getAlternativeFacilityIdentification() {
        return alternativeFacilityIdentification;
    }

    public void setAlternativeFacilityIdentification(List<AlternativeFacilityIdentificationJsonDto> alternativeFacilityIdentification) {
        this.alternativeFacilityIdentification = alternativeFacilityIdentification;
    }

    public List<AlternativeFacilityNameDataType> getAlternativeFacilityName() {
        return alternativeFacilityName;
    }

    public void setAlternativeFacilityName(List<AlternativeFacilityNameDataType> alternativeFacilityName) {
        this.alternativeFacilityName = alternativeFacilityName;
    }

    public List<AddressJsonDto> getFacilitySiteAddress() {
        return facilitySiteAddress;
    }

    public void setFacilitySiteAddress(List<AddressJsonDto> facilitySiteAddress) {
        this.facilitySiteAddress = facilitySiteAddress;
    }

    public AddressJsonDto getMainFacilitySiteAddress() {
        return facilitySiteAddress.get(0);
    }


    public AddressJsonDto getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(AddressJsonDto mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public GeographicCoordinatesJsonDto getFacilitySiteGeographicCoordinates() {
        return facilitySiteGeographicCoordinates;
    }

    public void setFacilitySiteGeographicCoordinates(GeographicCoordinatesJsonDto facilitySiteGeographicCoordinates) {
        this.facilitySiteGeographicCoordinates = facilitySiteGeographicCoordinates;
    }

    public List<EmissionsUnitJsonDto> getEmissionsUnits() {
        return emissionsUnits;
    }

    public void setEmissionsUnits(List<EmissionsUnitJsonDto> emissionsUnits) {
        this.emissionsUnits = emissionsUnits;
    }

    public List<ReleasePointJsonDto> getReleasePoints() {
        return releasePoints;
    }

    public void setReleasePoints(List<ReleasePointJsonDto> releasePoints) {
        this.releasePoints = releasePoints;
    }

    public List<ControlPathJsonDto> getControlPaths() {
        return controlPaths;
    }

    public void setControlPaths(List<ControlPathJsonDto> controlPaths) {
        this.controlPaths = controlPaths;
    }

    public List<ControlJsonDto> getControls() {
        return controls;
    }

    public void setControls(List<ControlJsonDto> controls) {
        this.controls = controls;
    }

    public List<FacilityNAICSJsonDto> getFacilityNAICS() {
        return facilityNAICS;
    }

    public void setFacilityNAICS(List<FacilityNAICSJsonDto> facilityNAICS) {
        this.facilityNAICS = facilityNAICS;
    }

    public List<FacilitySiteContactJsonDto> getFacilityContacts() {
        return facilityContacts;
    }

    public void setFacilityContacts(List<FacilitySiteContactJsonDto> facilityContacts) {
        this.facilityContacts = facilityContacts;
    }

    public String getHapFacilityCategoryCode() {
        return hapFacilityCategoryCode;
    }

    public void setHapFacilityCategoryCode(String hapFacilityCategoryCode) {
        this.hapFacilityCategoryCode = hapFacilityCategoryCode;
    }

    public String getCoordinateTolerance() {
        return coordinateTolerance;
    }

    public void setCoordinateTolerance(String coordinateTolerance) {
        this.coordinateTolerance = coordinateTolerance;
    }

    public String getFacilitySiteComment() {
        return facilitySiteComment;
    }

    public void setFacilitySiteComment(String facilitySiteComment) {
        this.facilitySiteComment = facilitySiteComment;
    }

    public String getFacilityNameIsReadOnly() {
        return facilityNameIsReadOnly;
    }

    public void setFacilityNameIsReadOnly(String facilityNameIsReadOnly) {
        this.facilityNameIsReadOnly = facilityNameIsReadOnly;
    }

    public String getLocationIsReadOnly() {
        return locationIsReadOnly;
    }

    public void setLocationIsReadOnly(String locationIsReadOnly) {
        this.locationIsReadOnly = locationIsReadOnly;
    }

    public String getPrimaryNAICSIsReadOnly() {
        return primaryNAICSIsReadOnly;
    }

    public void setPrimaryNAICSIsReadOnly(String primaryNAICSIsReadOnly) {
        this.primaryNAICSIsReadOnly = primaryNAICSIsReadOnly;
    }

    public LocalDate getPermitStatusBeginYear() {
        return permitStatusBeginYear;
    }

    public void setPermitStatusBeginYear(LocalDate permitStatusBeginYear) {
        this.permitStatusBeginYear = permitStatusBeginYear;
    }

    public LocalDate getPermitStatusEndYear() {
        return permitStatusEndYear;
    }

    public void setPermitStatusEndYear(LocalDate permitStatusEndYear) {
        this.permitStatusEndYear = permitStatusEndYear;
    }

    public MeasureJsonDto getMaximumTheoreticalEmissions() {
        return maximumTheoreticalEmissions;
    }

    public void setMaximumTheoreticalEmissions(MeasureJsonDto maximumTheoreticalEmissions) {
        this.maximumTheoreticalEmissions = maximumTheoreticalEmissions;
    }

    public MeasureJsonDto getPotentialToEmit() {
        return potentialToEmit;
    }

    public void setPotentialToEmit(MeasureJsonDto potentialToEmit) {
        this.potentialToEmit = potentialToEmit;
    }

    public String getIsBillable() {
        return isBillable;
    }

    public void setIsBillable(String isBillable) {
        this.isBillable = isBillable;
    }

    public String getHasSensitiveData() {
        return hasSensitiveData;
    }

    public void setHasSensitiveData(String hasSensitiveData) {
        this.hasSensitiveData = hasSensitiveData;
    }

    public String getPermitNumber() {
        return permitNumber;
    }

    public void setPermitNumber(String permitNumber) {
        this.permitNumber = permitNumber;
    }

    public List<String> getPermitTypes() {
        return permitTypes;
    }

    public void setPermitTypes(List<String> permitTypes) {
        this.permitTypes = permitTypes;
    }

    public Integer getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }

    public String getConstructionLimitDescription() {
        return constructionLimitDescription;
    }

    public void setConstructionLimitDescription(String constructionLimitDescription) {
        this.constructionLimitDescription = constructionLimitDescription;
    }

    public String getSltFacilityCategoryCode() {
        return sltFacilityCategoryCode;
    }

    public void setSltFacilityCategoryCode(String sltFacilityCategoryCode) {
        this.sltFacilityCategoryCode = sltFacilityCategoryCode;
    }
}
