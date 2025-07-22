package gov.epa.cef.web.service.dto.json;

import gov.epa.cef.web.service.dto.json.shared.AddressJsonDto;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Schema(name = "FacilityContact")
public class FacilitySiteContactJsonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private LookupJsonDto type;

    @Size(max = 15)
    private String prefix;

    @NotBlank
    @Size(max = 20)
    private String firstName;

    @NotBlank
    @Size(max = 20)
    private String lastName;

    @NotBlank
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(max = 10)
    private String phone;

    @Size(max = 5)
    private String phoneExt;

    @NotNull
    private AddressJsonDto streetAddress;

    private AddressJsonDto mailingStreetAddress;

    public LookupJsonDto getType() {
        return type;
    }

    public void setType(LookupJsonDto type) {
        this.type = type;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneExt() {
        return phoneExt;
    }

    public void setPhoneExt(String phoneExt) {
        this.phoneExt = phoneExt;
    }

    public AddressJsonDto getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(AddressJsonDto streetAddress) {
        this.streetAddress = streetAddress;
    }

    public AddressJsonDto getMailingStreetAddress() {
        return mailingStreetAddress;
    }

    public void setMailingStreetAddress(AddressJsonDto mailingStreetAddress) {
        this.mailingStreetAddress = mailingStreetAddress;
    }
}
