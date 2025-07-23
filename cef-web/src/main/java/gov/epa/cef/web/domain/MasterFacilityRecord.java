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
package gov.epa.cef.web.domain;

import gov.epa.cef.web.domain.common.BaseAuditEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "master_facility_record")
public class MasterFacilityRecord extends BaseAuditEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_code")
    private FacilityCategoryCode facilityCategoryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_type_code")
    private FacilitySourceTypeCode facilitySourceTypeCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_system_code", nullable = false)
    private ProgramSystemCode programSystemCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_code", nullable = false)
    private OperatingStatusCode operatingStatusCode;

    @Column(name = "eis_program_id", length = 22)
    private String eisProgramId;

    @Column(name = "agency_facility_id", nullable = false, length = 30)
    private String agencyFacilityIdentifier;

    @Column(name = "name", nullable = false, length = 80)
    private String name;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "status_year")
    private Short statusYear;

    @Column(name = "street_address", nullable = false, length = 100)
    private String streetAddress;

    @Column(name = "city", nullable = false, length = 60)
    private String city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_code", nullable = false)
    private FipsStateCode stateCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "county_code")
    private FipsCounty countyCode;

    @Column(name = "country_code", length = 10)
    private String countryCode;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(name = "mailing_street_address", length = 100)
    private String mailingStreetAddress;

    @Column(name = "mailing_city", length = 60)
    private String mailingCity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mailing_state_code")
    private FipsStateCode mailingStateCode;

    @Column(name = "mailing_postal_code", length = 10)
    private String mailingPostalCode;

    @Column(name = "latitude", precision = 10, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 6)
    private BigDecimal longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tribal_code")
    private TribalCode tribalCode;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "masterFacilityRecord")
    private List<MasterFacilityNAICSXref> masterFacilityNAICS = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "masterFacilityRecord")
    private List<FacilityPermit> masterFacilityPermits = new ArrayList<>();
    
    @Column(name = "coordinate_tolerance", precision = 10, scale = 6, nullable = false)
	private BigDecimal coordinateTolerance;


	/***
     * Default constructor
     */
    public MasterFacilityRecord() {}


    /***
     * Copy constructor
     * @param originalMasterFacilityRecord The master facility record object being copied
     */
    public MasterFacilityRecord(MasterFacilityRecord originalMasterFacilityRecord) {
		this.id = originalMasterFacilityRecord.getId();
        this.facilityCategoryCode = originalMasterFacilityRecord.getFacilityCategoryCode();
        this.facilitySourceTypeCode = originalMasterFacilityRecord.getFacilitySourceTypeCode();
        this.programSystemCode = originalMasterFacilityRecord.getProgramSystemCode();
        this.operatingStatusCode = originalMasterFacilityRecord.getOperatingStatusCode();
        this.eisProgramId = originalMasterFacilityRecord.getEisProgramId();
        this.agencyFacilityIdentifier = originalMasterFacilityRecord.getAgencyFacilityIdentifier();
        this.name = originalMasterFacilityRecord.getName();
        this.description = originalMasterFacilityRecord.getDescription();
        this.statusYear = originalMasterFacilityRecord.getStatusYear();
        this.streetAddress = originalMasterFacilityRecord.getStreetAddress();
        this.city = originalMasterFacilityRecord.getCity();
        this.countyCode = originalMasterFacilityRecord.getCountyCode();
        this.stateCode = originalMasterFacilityRecord.getStateCode();
        this.countryCode = originalMasterFacilityRecord.getCountryCode();
        this.postalCode = originalMasterFacilityRecord.getPostalCode();
        this.mailingStreetAddress = originalMasterFacilityRecord.getMailingStreetAddress();
        this.mailingCity = originalMasterFacilityRecord.getMailingCity();
        this.mailingStateCode = originalMasterFacilityRecord.getMailingStateCode();
        this.mailingPostalCode = originalMasterFacilityRecord.getMailingPostalCode();
        this.latitude = originalMasterFacilityRecord.getLatitude();
        this.longitude = originalMasterFacilityRecord.getLongitude();
        this.tribalCode = originalMasterFacilityRecord.getTribalCode();
        this.coordinateTolerance = originalMasterFacilityRecord.getCoordinateTolerance();
        
        for (MasterFacilityNAICSXref naicsXref : originalMasterFacilityRecord.getMasterFacilityNAICS()) {
        	this.masterFacilityNAICS.add(new MasterFacilityNAICSXref(this, naicsXref));
        }

        for (FacilityPermit permit : originalMasterFacilityRecord.getMasterFacilityPermits()) {
            this.masterFacilityPermits.add(new FacilityPermit(this, permit));
        }

    }


    public FacilityCategoryCode getFacilityCategoryCode() {
        return facilityCategoryCode;
    }


    public void setFacilityCategoryCode(FacilityCategoryCode facilityCategoryCode) {
        this.facilityCategoryCode = facilityCategoryCode;
    }


    public FacilitySourceTypeCode getFacilitySourceTypeCode() {
        return facilitySourceTypeCode;
    }


    public void setFacilitySourceTypeCode(FacilitySourceTypeCode facilitySourceTypeCode) {
        this.facilitySourceTypeCode = facilitySourceTypeCode;
    }


    public ProgramSystemCode getProgramSystemCode() {
        return programSystemCode;
    }


    public void setProgramSystemCode(ProgramSystemCode programSystemCode) {
        this.programSystemCode = programSystemCode;
    }


    public OperatingStatusCode getOperatingStatusCode() {
        return operatingStatusCode;
    }


    public void setOperatingStatusCode(OperatingStatusCode operatingStatusCode) {
        this.operatingStatusCode = operatingStatusCode;
    }


    public String getEisProgramId() {
        return eisProgramId;
    }


    public void setEisProgramId(String eisProgramId) {
        this.eisProgramId = eisProgramId;
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


    public FipsStateCode getStateCode() {
        return stateCode;
    }


    public void setStateCode(FipsStateCode stateCode) {
        this.stateCode = stateCode;
    }


    public FipsCounty getCountyCode() {
        return countyCode;
    }


    public void setCountyCode(FipsCounty countyCode) {
        this.countyCode = countyCode;
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


    public FipsStateCode getMailingStateCode() {
        return mailingStateCode;
    }


    public void setMailingStateCode(FipsStateCode mailingStateCode) {
        this.mailingStateCode = mailingStateCode;
    }


    public String getMailingPostalCode() {
        return mailingPostalCode;
    }


    public void setMailingPostalCode(String mailingPostalCode) {
        this.mailingPostalCode = mailingPostalCode;
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


    public TribalCode getTribalCode() {
        return tribalCode;
    }


    public void setTribalCode(TribalCode tribalCode) {
        this.tribalCode = tribalCode;
    }

    public List<MasterFacilityNAICSXref> getMasterFacilityNAICS() {
		return masterFacilityNAICS;
	}


	public void setMasterFacilityNAICS(List<MasterFacilityNAICSXref> masterFacilityNAICS) {
		this.masterFacilityNAICS.clear();
		if(masterFacilityNAICS != null) {
			this.masterFacilityNAICS.addAll(masterFacilityNAICS);
		}
	}

    public List<FacilityPermit> getMasterFacilityPermits() {
        return masterFacilityPermits;
    }

    public void setMasterFacilityPermits(List<FacilityPermit> masterFacilityPermits) {
        this.masterFacilityPermits.clear();
        if(masterFacilityPermits != null) {
            this.masterFacilityPermits.addAll(masterFacilityPermits);
        }
    }

    public BigDecimal getCoordinateTolerance() {
		return coordinateTolerance;
	}

	public void setCoordinateTolerance(BigDecimal coordinateTolerance) {
		this.coordinateTolerance = coordinateTolerance;
	}


	/***
     * Set the id property to null for this object and the id for it's direct children.  This method is useful to INSERT the updated object instead of UPDATE.
     */
    public void clearId() {
    	this.id = null;
    	
    	for (MasterFacilityNAICSXref naicsXref : this.masterFacilityNAICS) {
        	naicsXref.clearId();
        }

    }
}
