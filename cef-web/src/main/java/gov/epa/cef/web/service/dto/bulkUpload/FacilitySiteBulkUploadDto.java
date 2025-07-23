/*
 * © Copyright 2019 EPA CAERS Project Team
 *
 * This file is part of the Common Air Emissions Reporting System (CAERS).
 *
 * CAERS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * CAERS is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with CAERS.  If
 * not, see <https://www.gnu.org/licenses/>.
*/
package gov.epa.cef.web.service.dto.bulkUpload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import gov.epa.cef.web.annotation.CsvColumn;
import gov.epa.cef.web.annotation.CsvFileName;

import java.io.Serializable;


@CsvFileName(name = "facility_sites.csv")
public class FacilitySiteBulkUploadDto extends BaseWorksheetDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Facility Site ID is required.")
    private Long id;

    private Long masterFacilityRecordId;

    @Size(max = 30, message = "Agency Facility Identifier can not exceed {max} chars; found '${validatedValue}'.")
    private String agencyFacilityIdentifier;

    @Size(max = 20, message = "Category Code can not exceed {max} chars; found '${validatedValue}'.")
    private String facilityCategoryCode;

    private String facilityCategoryName;

    @Size(max = 20, message = "Source Type Code can not exceed {max} chars; found '${validatedValue}'.")
    private String facilitySourceTypeCode;

    private String facilitySourceTypeCodeDescription;

    @NotBlank(message = "Facility Name is required.")
    @Size(max = 80, message = "Facility Name can not exceed {max} chars; found '${validatedValue}'.")
    private String name;

    @Size(max = 100, message = "Description can not exceed {max} chars; found '${validatedValue}'.")
    private String description;

    @NotBlank(message = "Operating Status Code is required.")
    @Size(max = 20, message = "Operating Status Code can not exceed {max} chars; found '${validatedValue}'.")
    private String operatingStatusCode;

    private String operatingStatusDescription;

    @NotBlank(message = "Year Op Status Changed is required.")
    @Pattern(regexp = YearPattern,
        message = "Year Op Status Changed is not in expected format: {4} digits; found '${validatedValue}'.")
    private String statusYear;

    @NotBlank(message = "Program System Code is required.")
    @Size(max = 20, message = "Program System Code can not exceed {max} chars; found '${validatedValue}'.")
    private String programSystemCode;

    private String programSystemCodeDescription;

    @NotBlank(message = "Street Address is required.")
    @Size(max = 100, message = "Street Address can not exceed {max} chars; found '${validatedValue}'.")
    private String streetAddress;

    @NotBlank(message = "City is required.")
    @Size(max = 60, message = "City can not exceed {max} chars; found '${validatedValue}'.")
    private String city;

    @NotBlank(message = "County is required.")
    @Size(max = 43, message = "County can not exceed {max} chars; found '${validatedValue}'.")
    private String county;

    @Size(max = 3, message = "County Code can not exceed {max} chars; found '${validatedValue}'.")
    private String countyCode;

    @NotBlank(message = "State Code is required.")
    @Size(max = 5, message = "State Code can not exceed {max} chars; found '${validatedValue}'.")
    private String stateCode;

    @Size(max = 2, message = "State FIPS Code can not exceed {max} chars; found '${validatedValue}'.")
    private String stateFipsCode;

    @Size(max = 10, message = "Country Code can not exceed {max} chars; found '${validatedValue}'.")
    private String countryCode;

    @NotBlank(message = "Postal Code is required.")
    @Size(max = 10, message = "Postal Code can not exceed {max} chars; found '${validatedValue}'.")
    private String postalCode;

    @NotBlank(message = "Latitude is required.")
    @Pattern(regexp = LatitudePattern,
        message = "Latitude is not in expected numeric format: '+/-{2}.{6}' digits; found '${validatedValue}'.")
    private String latitude;

    @NotBlank(message = "Longitude is required.")
    @Pattern(regexp = LongitudePattern,
        message = "Longitude is not in expected numeric format: '+/-{3}.{6}' digits; found '${validatedValue}.")
    private String longitude;

    @NotBlank(message = "Mailing Street Address is required.")
    @Size(max = 100, message = "Mailing Street Address can not exceed {max} chars; found '${validatedValue}'.")
    private String mailingStreetAddress;

    @NotBlank(message = "Mailing City is required.")
    @Size(max = 60, message = "Mailing City can not exceed {max} chars; found '${validatedValue}'.")
    private String mailingCity;

    @NotBlank(message = "Mailing State Code is required.")
    @Size(max = 5, message = "Mailing State Code can not exceed {max} chars; found '${validatedValue}'.")
    private String mailingStateCode;

    @NotBlank(message = "Mailing Postal Code is required.")
    @Size(max = 10, message = "Mailing Postal Code can not exceed {max} chars; found '${validatedValue}'.")
    private String mailingPostalCode;

    @Size(max = 10, message = "Mailing Country Code can not exceed {max} chars; found '${validatedValue}'.")
    private String mailingCountryCode;

    @Size(max = 22, message = "EIS Program ID can not exceed {max} chars; found '${validatedValue}'.")
    private String eisProgramId;

    @Size(max = 20, message = "Tribal Code can not exceed {max} chars; found '${validatedValue}'.")
    private String tribalCode;

    private String tribalCodeDescription;

    @Size(max = 400, message = "Comments can not exceed {max} chars; found '${validatedValue}'.")
    private String comments;

    public FacilitySiteBulkUploadDto() {

        super(WorksheetName.FacilitySite);
    }

    @CsvColumn(name = "Internal Facility ID", order = 1)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @CsvColumn(name = "Agency Facility ID", order = 2)
    public String getAgencyFacilityIdentifier() {
        return agencyFacilityIdentifier;
    }
    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }

    public Long getMasterFacilityRecordId() {
        return masterFacilityRecordId;
    }
    public void setMasterFacilityRecordId(Long masterFacilityRecordId) {
        this.masterFacilityRecordId = masterFacilityRecordId;
    }

    @CsvColumn(name = "Facility Category Code", order = 3)
    public String getFacilityCategoryCode() {
        return facilityCategoryCode;
    }
    public void setFacilityCategoryCode(String facilityCategoryCode) {
        this.facilityCategoryCode = facilityCategoryCode;
    }

    @CsvColumn(name = "Facility Category", order = 4)
    public String getFacilityCategoryName() { return facilityCategoryName; }
    public void setFacilityCategoryName(String facilityCategoryName) {
        this.facilityCategoryName = facilityCategoryName;
    }

    public String getFacilitySourceTypeCode() {
        return facilitySourceTypeCode;
    }
    public void setFacilitySourceTypeCode(String facilitySourceTypeCode) {
        this.facilitySourceTypeCode = facilitySourceTypeCode;
    }

    public String getFacilitySourceTypeCodeDescription() {
    	return facilitySourceTypeCodeDescription;
    }
    public void setFacilitySourceTypeCodeDescription(String facilitySourceTypeCodeDescription) {
    	this.facilitySourceTypeCodeDescription = facilitySourceTypeCodeDescription;
    }

    @CsvColumn(name = "Facility Name", order = 5)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @CsvColumn(name = "Facility Description", order = 6)
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @CsvColumn(name = "Facility Op Status Code", order = 7)
    public String getOperatingStatusCode() {
        return operatingStatusCode;
    }
    public void setOperatingStatusCode(String operatingStatusCode) {
        this.operatingStatusCode = operatingStatusCode;
    }

    @CsvColumn(name = "Facility Op Status", order = 8)
    public String getOperatingStatusDescription() {
        return operatingStatusDescription;
    }
    public void setOperatingStatusDescription(String operatingStatusDescription) {
        this.operatingStatusDescription = operatingStatusDescription;
    }

    @CsvColumn(name = "Facility Op Status Year", order = 9)
    public String getStatusYear() {
        return statusYear;
    }
    public void setStatusYear(String statusYear) {
        this.statusYear = statusYear;
    }

    @CsvColumn(name = "Program System Code", order = 10)
    public String getProgramSystemCode() {
        return programSystemCode;
    }
    public void setProgramSystemCode(String programSystemCode) {
        this.programSystemCode = programSystemCode;
    }

    @CsvColumn(name = "Program System Code Description", order = 11)
    public String getProgramSystemCodeDescription() {
        return programSystemCodeDescription;
    }
    public void setProgramSystemCodeDescription(String programSystemCodeDescription) {
        this.programSystemCodeDescription = programSystemCodeDescription;
    }

    @CsvColumn(name = "Street Address", order = 12)
    public String getStreetAddress() {
        return streetAddress;
    }
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    @CsvColumn(name = "City", order = 13)
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    @CsvColumn(name = "State FIPS Code", order = 14)
    public String getStateFipsCode() {
        return stateFipsCode;
    }
    public void setStateFipsCode(String stateFipsCode) {
        this.stateFipsCode = stateFipsCode;
    }

    @CsvColumn(name = "State Code", order = 15)
    public String getStateCode() {
        return stateCode;
    }
    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    @CsvColumn(name = "County Code", order = 16)
    public String getCountyCode() {
        return countyCode;
    }
    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    @CsvColumn(name = "County", order = 17)
    public String getCounty() {
        return county;
    }
    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @CsvColumn(name = "Postal Code", order = 18)
    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @CsvColumn(name = "Latitude", order = 19)
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @CsvColumn(name = "Longitude", order = 20)
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @CsvColumn(name = "Mailing Street Address", order = 21)
    public String getMailingStreetAddress() {
        return mailingStreetAddress;
    }
    public void setMailingStreetAddress(String mailingStreetAddress) {
        this.mailingStreetAddress = mailingStreetAddress;
    }

    @CsvColumn(name = "Mailing City", order = 22)
    public String getMailingCity() {
        return mailingCity;
    }
    public void setMailingCity(String mailingCity) {
        this.mailingCity = mailingCity;
    }

    @CsvColumn(name = "Mailing State Code", order = 23)
    public String getMailingStateCode() {
        return mailingStateCode;
    }
    public void setMailingStateCode(String mailingStateCode) {
        this.mailingStateCode = mailingStateCode;
    }

    @CsvColumn(name = "Mailing Postal Code", order = 24)
    public String getMailingPostalCode() {
        return mailingPostalCode;
    }
    public void setMailingPostalCode(String mailingPostalCode) {
        this.mailingPostalCode = mailingPostalCode;
    }

    public String getMailingCountryCode() {
        return mailingCountryCode;
    }
    public void setMailingCountryCode(String mailingCountryCode) {
        this.mailingCountryCode = mailingCountryCode;
    }

    @CsvColumn(name = "EIS Program ID", order = 25)
    public String getEisProgramId() {
        return eisProgramId;
    }
    public void setEisProgramId(String eisProgramId) {
        this.eisProgramId = eisProgramId;
    }

    @CsvColumn(name = "Tribal Code Description", order = 26)
    public String getTribalCodeDescription() {
        return tribalCodeDescription;
    }
    public void setTribalCodeDescription(String tribalCodeDescription) {
        this.tribalCodeDescription = tribalCodeDescription;
    }

    @CsvColumn(name = "Tribal Code", order = 27)
    public String getTribalCode() {
        return tribalCode;
    }
    public void setTribalCode(String tribalCode) {
        this.tribalCode = tribalCode;
    }

    @CsvColumn(name = "Facility Comments", order = 28)
    public String getComments() {
    	return comments;
    }
    public void setComments(String comments) {
    	this.comments = comments;
    }

    public String getErrorIdentifier() {
        return "facilitySites-id: " + id;
    }
}
