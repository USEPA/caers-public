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
package gov.epa.cef.web.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

public class FacilitySiteDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private FacilityCategoryCodeDto facilityCategoryCode;
    private CodeLookupDto facilitySourceTypeCode;
    private CodeLookupDto programSystemCode;
    private CodeLookupDto operatingStatusCode;
    private CodeLookupDto tribalCode;
    private EmissionsReportDto emissionsReport;
    private String agencyFacilityIdentifier;
    private String name;
    private String description;
    private Short statusYear;
    private String streetAddress;
    private String city;
    private FipsCountyDto countyCode;
    private FipsStateCodeDto stateCode;
    private String countryCode;
    private String postalCode;
    private String mailingStreetAddress;
    private String mailingCity;
    private FipsStateCodeDto mailingStateCode;
    private String mailingPostalCode;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String tribalCodeDesc;
    private String comments;
    private Set<FacilityNAICSDto> facilityNAICS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FacilityCategoryCodeDto getFacilityCategoryCode() {
        return facilityCategoryCode;
    }

    public void setFacilityCategoryCode(FacilityCategoryCodeDto facilityCategoryCode) {
        this.facilityCategoryCode = facilityCategoryCode;
    }

    public CodeLookupDto getFacilitySourceTypeCode() {
        return facilitySourceTypeCode;
    }

    public void setFacilitySourceTypeCode(CodeLookupDto facilitySourceTypeCode) {
        this.facilitySourceTypeCode = facilitySourceTypeCode;
    }

    public CodeLookupDto getProgramSystemCode() {
        return programSystemCode;
    }

    public void setProgramSystemCode(CodeLookupDto programSystemCode) {
        this.programSystemCode = programSystemCode;
    }

    public CodeLookupDto getOperatingStatusCode() {
        return operatingStatusCode;
    }

    public void setOperatingStatusCode(CodeLookupDto operatingStatusCode) {
        this.operatingStatusCode = operatingStatusCode;
    }

    public CodeLookupDto getTribalCode() {
        return tribalCode;
    }

    public void setTribalCode(CodeLookupDto tribalCode) {
        this.tribalCode = tribalCode;
    }

    public EmissionsReportDto getEmissionsReport() {
        return emissionsReport;
    }

    public void setEmissionsReport(EmissionsReportDto emissionsReport) {
        this.emissionsReport = emissionsReport;
    }

    public String getAgencyFacilityIdentifier() {
        return agencyFacilityIdentifier;
    }

    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
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

    public Short getStatusYear() {
        return statusYear;
    }

    public void setStatusYear(Short statusYear) {
        this.statusYear = statusYear;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public FipsCountyDto getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(FipsCountyDto countyCode) {
        this.countyCode = countyCode;
    }

    public FipsStateCodeDto getStateCode() {
        return stateCode;
    }

    public void setStateCode(FipsStateCodeDto stateCode) {
        this.stateCode = stateCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getMailingStreetAddress() {
        return mailingStreetAddress;
    }

    public void setMailingStreetAddress(String mailingStreetAddress) {
        this.mailingStreetAddress = mailingStreetAddress;
    }

    public String getMailingCity() {
        return mailingCity;
    }

    public void setMailingCity(String mailingCity) {
        this.mailingCity = mailingCity;
    }

    public FipsStateCodeDto getMailingStateCode() {
        return mailingStateCode;
    }

    public void setMailingStateCode(FipsStateCodeDto mailingStateCode) {
        this.mailingStateCode = mailingStateCode;
    }

    public String getMailingPostalCode() {
        return mailingPostalCode;
    }

    public void setMailingPostalCode(String mailingPostalCode) {
        this.mailingPostalCode = mailingPostalCode;
    }
    
    public String getComments() {
    	return comments;
    }
    
    public void setComments(String comments) {
    	this.comments = comments;
    }

    public String getTribalCodeDesc() {
        return tribalCodeDesc;
    }

    public void setTribalCodeDesc(String tribalCodeDesc) {
        this.tribalCodeDesc = tribalCodeDesc;
    }

    public Set<FacilityNAICSDto> getFacilityNAICS() {
        return facilityNAICS;
    }

    public void setFacilityNAICS(Set<FacilityNAICSDto> facilityNAICS) {
        this.facilityNAICS = facilityNAICS;
    }
    
    public FacilitySiteDto withId(Long id) {
    	setId(id);
    	return this;
    }

}
