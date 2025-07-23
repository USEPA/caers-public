package gov.epa.cef.web.service.dto.json.shared;

import gov.epa.cef.web.service.dto.json.shared.reference.CountryJsonDto;
import gov.epa.cef.web.service.dto.json.shared.reference.CountyJsonDto;
import gov.epa.cef.web.service.dto.json.shared.reference.StateJsonDto;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

@Schema(name = "Address")
public class AddressJsonDto implements Serializable {

    @NotBlank(message = "Street Address is required.")
    @Size(max = 100)
    private String addressText;

    private String supplementalAddressText;

    @NotBlank
    @Size(max = 60)
    private String localityName;

    private CountyJsonDto county;

    @NotNull
    private StateJsonDto state;

    private CountryJsonDto country;

    @NotBlank
    @Size(max = 10)
    private String postalCode;


    protected String addressComment;
    protected String locationAddressIsReadOnly;
    protected String localityIsReadOnly;
    protected String postalCodeIsReadOnly;

    public String getAddressText() {
        return addressText;
    }

    public void setAddressText(String addressText) {
        this.addressText = addressText;
    }

    public String getSupplementalAddressText() {
        return supplementalAddressText;
    }

    public void setSupplementalAddressText(String supplementalAddressText) {
        this.supplementalAddressText = supplementalAddressText;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public CountyJsonDto getCounty() {
        return county;
    }

    public void setCounty(CountyJsonDto county) {
        this.county = county;
    }

    public StateJsonDto getState() {
        return state;
    }

    public void setState(StateJsonDto state) {
        this.state = state;
    }

    public CountryJsonDto getCountry() {
        return country;
    }

    public void setCountry(CountryJsonDto country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddressComment() {
        return addressComment;
    }

    public void setAddressComment(String addressComment) {
        this.addressComment = addressComment;
    }

    public String getLocationAddressIsReadOnly() {
        return locationAddressIsReadOnly;
    }

    public void setLocationAddressIsReadOnly(String locationAddressIsReadOnly) {
        this.locationAddressIsReadOnly = locationAddressIsReadOnly;
    }

    public String getLocalityIsReadOnly() {
        return localityIsReadOnly;
    }

    public void setLocalityIsReadOnly(String localityIsReadOnly) {
        this.localityIsReadOnly = localityIsReadOnly;
    }

    public String getPostalCodeIsReadOnly() {
        return postalCodeIsReadOnly;
    }

    public void setPostalCodeIsReadOnly(String postalCodeIsReadOnly) {
        this.postalCodeIsReadOnly = postalCodeIsReadOnly;
    }
}
