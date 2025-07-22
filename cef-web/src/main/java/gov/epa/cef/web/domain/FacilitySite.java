/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.domain;

import gov.epa.cef.web.domain.common.BaseAuditEntity;
import gov.epa.cef.web.domain.common.IReportComponent;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.util.ConstantUtils;
import gov.epa.cef.web.util.ReportCreationContext;

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
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Facility entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "facility_site")
public class FacilitySite extends BaseAuditEntity implements IReportComponent {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_code")
    private FacilityCategoryCode facilityCategoryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_type_code")
    private FacilitySourceTypeCode facilitySourceTypeCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_system_code")
    private ProgramSystemCode programSystemCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_code", nullable = false)
    private OperatingStatusCode operatingStatusCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private EmissionsReport emissionsReport;

    @Column(name = "agency_facility_identifier", nullable = false, length = 30)
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
    @JoinColumn(name = "county_code")
    private FipsCounty countyCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_code", nullable = false)
    private FipsStateCode stateCode;

    @Column(name = "country_code", length = 10)
    private String countryCode;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(name = "mailing_street_address", length = 100)
    private String mailingStreetAddress;

    @Column(name = "mailing_city", length = 60)
    private String mailingCity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mailing_state_code", nullable = false)
    private FipsStateCode mailingStateCode;

    @Column(name = "mailing_postal_code", length = 10)
    private String mailingPostalCode;

    @Column(name = "latitude", precision = 10, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 6)
    private BigDecimal longitude;
    
    @Column(name = "comments", length = 400)
    private String comments;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tribal_code")
    private TribalCode tribalCode;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "facilitySite")
    private List<FacilityNAICSXref> facilityNAICS = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "facilitySite")
    private List<EmissionsUnit> emissionsUnits = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "facilitySite")
    private List<ReleasePoint> releasePoints = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "facilitySite")
    private List<FacilitySiteContact> contacts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "facilitySite")
    private List<Control> controls = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "facilitySite")
    private List<ControlPath> controlPaths = new ArrayList<>();


    /***
     * Default constructor
     */
    public FacilitySite() {}


    /***
     * Copy constructor
     * @param emissionsReport The emissions report object that this facility site should be associated with
     * @param originalFacilitySite The facility site object being copied
     */
    public FacilitySite(EmissionsReport emissionsReport, FacilitySite originalFacilitySite) {
        this(emissionsReport, originalFacilitySite, null);
    }

    /***
     * Copy constructor
     * @param emissionsReport The emissions report object that this facility site should be associated with
     * @param originalFacilitySite The facility site object being copied
     * @param context context for tracking report creation changes
     */
    public FacilitySite(EmissionsReport emissionsReport, FacilitySite originalFacilitySite, ReportCreationContext context) {
		this.id = originalFacilitySite.getId();
        this.facilityCategoryCode = originalFacilitySite.getFacilityCategoryCode();
        this.facilitySourceTypeCode = originalFacilitySite.getFacilitySourceTypeCode();
        this.programSystemCode = originalFacilitySite.getProgramSystemCode();
        this.operatingStatusCode = originalFacilitySite.getOperatingStatusCode();
        this.emissionsReport = emissionsReport;
        this.agencyFacilityIdentifier = originalFacilitySite.getAgencyFacilityIdentifier();
        this.name = originalFacilitySite.getName();
        this.description = originalFacilitySite.getDescription();
        this.statusYear = originalFacilitySite.getStatusYear();
        this.streetAddress = originalFacilitySite.getStreetAddress();
        this.city = originalFacilitySite.getCity();
        this.countyCode = originalFacilitySite.getCountyCode();
        this.stateCode = originalFacilitySite.getStateCode();
        this.countryCode = originalFacilitySite.getCountryCode();
        this.postalCode = originalFacilitySite.getPostalCode();
        this.mailingStreetAddress = originalFacilitySite.getMailingStreetAddress();
        this.mailingCity = originalFacilitySite.getMailingCity();
        this.mailingStateCode = originalFacilitySite.getMailingStateCode();
        this.mailingPostalCode = originalFacilitySite.getMailingPostalCode();
        this.latitude = originalFacilitySite.getLatitude();
        this.longitude = originalFacilitySite.getLongitude();
        this.tribalCode = originalFacilitySite.getTribalCode();
        this.comments = originalFacilitySite.getComments();

        for (FacilityNAICSXref naicsXref : originalFacilitySite.getFacilityNAICS()) {
        	this.facilityNAICS.add(new FacilityNAICSXref(this, naicsXref));
        }
        for (ReleasePoint releasePoint : originalFacilitySite.getReleasePoints()) {
        	if (!releasePoint.getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN)) {
        		this.releasePoints.add(new ReleasePoint(this, releasePoint));
        	} else {
        	    context.addCreationChange(ValidationField.RP, "component.shutdown", null, ReportChangeType.DELETE, "Release Point", releasePoint.getReleasePointIdentifier());
        	}
        }
        
        //controls need to be before emission unit so that emission process and release point apportionments
        //underneath the units can association themselves with the appropriate controls
        for (Control control : originalFacilitySite.getControls()) {
        	if (control.getOperatingStatusCode() == null || !control.getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN)) {
        		this.controls.add(new Control(this, control));
        	} else {
        	    context.addCreationChange(ValidationField.CONTROL, "component.shutdown", null, ReportChangeType.DELETE, "Control Device", control.getIdentifier());
        	}
        }
        for (ControlPath controlPath : originalFacilitySite.getControlPaths()) {
            this.controlPaths.add(new ControlPath(this, controlPath));
        }
        
        for (ControlPath newControlPath: this.controlPaths){
        	for (ControlPath originalControlPaths : originalFacilitySite.getControlPaths()) {
        		for (ControlAssignment originalControlAssignment : originalControlPaths.getAssignments()) {
        			Control c = null;
    	        	ControlPath cpc = null;
        		
		        	for(Control newControl : this.controls) {
		        		if (originalControlAssignment.getControl() != null && newControl.getId().equals(originalControlAssignment.getControl().getId())
		        				&& originalControlAssignment.getControlPath().getPathId().equals(newControlPath.getPathId())) {
		        			c = newControl;
		        			break;
		        		}
		        	}
		        	
		        	//if the original control assignment has a child control path, then loop through the
		        	//control paths associated with the new facility, find the appropriate one, and
		        	//associate it to this control assignment - otherwise leave child path as null
		        	if (originalControlAssignment.getControlPathChild() != null) {
		            	for(ControlPath newControlPathChild : this.controlPaths) {
		            		if (newControlPathChild.getPathId().equals(originalControlAssignment.getControlPathChild().getPathId())
		            				&& newControlPath.getPathId().equals(originalControlAssignment.getControlPath().getPathId())) {
		            			cpc = newControlPathChild;
		            			break;
		            		}
		            	}
		        	}
		        	if(c != null || cpc != null){
		        		newControlPath.getAssignments().add(new ControlAssignment(newControlPath, c, cpc, originalControlAssignment));
		        	}
        		}
        	}
        }
        
        for (EmissionsUnit emissionsUnit : originalFacilitySite.getEmissionsUnits()) {
        	if (!emissionsUnit.getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN)) {

        		this.emissionsUnits.add(new EmissionsUnit(this, emissionsUnit, context));
 
        	} else if (emissionsUnit.getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN)
        			&& originalFacilitySite.getEmissionsReport().getMasterFacilityRecord().getFacilitySourceTypeCode() != null
        			&& originalFacilitySite.getEmissionsReport().getMasterFacilityRecord().getFacilitySourceTypeCode().getCode().equals(ConstantUtils.FACILITY_SOURCE_LANDFILL_CODE)
        			&& emissionsUnit.getEmissionsProcesses().stream()
                        .filter(emissionsProcess -> !emissionsProcess.getOperatingStatusCode().getCode().contentEquals(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN))
                        .collect(Collectors.toList()).size() > 0) {

    			this.emissionsUnits.add(new EmissionsUnit(this, emissionsUnit, context));

        	} else if (context != null) {

        	    context.addCreationChange(ValidationField.EMISSIONS_UNIT, "component.shutdown", null, ReportChangeType.DELETE, "Unit", emissionsUnit.getUnitIdentifier());
        	}
        }
        
        for (FacilitySiteContact siteContact : originalFacilitySite.getContacts()) {
        	this.contacts.add(new FacilitySiteContact(this, siteContact));
        }
    }


    public FacilityCategoryCode getFacilityCategoryCode() {
        return this.facilityCategoryCode;
    }

    public void setFacilityCategoryCode(FacilityCategoryCode facilityCategoryCode) {
        this.facilityCategoryCode = facilityCategoryCode;
    }

    public FacilitySourceTypeCode getFacilitySourceTypeCode() {
        return this.facilitySourceTypeCode;
    }

    public void setFacilitySourceTypeCode(FacilitySourceTypeCode facilitySourceTypeCode) {
        this.facilitySourceTypeCode = facilitySourceTypeCode;
    }

    public ProgramSystemCode getProgramSystemCode() {
        return this.programSystemCode;
    }

    public void setProgramSystemCode(ProgramSystemCode programSystemCode) {
        this.programSystemCode = programSystemCode;
    }

    public OperatingStatusCode getOperatingStatusCode() {
        return this.operatingStatusCode;
    }

    public void setOperatingStatusCode(OperatingStatusCode operatingStatusCode) {
        this.operatingStatusCode = operatingStatusCode;
    }

    public EmissionsReport getEmissionsReport() {
        return this.emissionsReport;
    }

    public void setEmissionsReport(EmissionsReport emissionsReport) {
        this.emissionsReport = emissionsReport;
    }

    public String getAgencyFacilityIdentifier() {
        return this.agencyFacilityIdentifier;
    }

    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Short getStatusYear() {
        return this.statusYear;
    }

    public void setStatusYear(Short statusYear) {
        this.statusYear = statusYear;
    }

    public String getStreetAddress() {
        return this.streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public FipsCounty getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(FipsCounty countyCode) {
        this.countyCode = countyCode;
    }

    public FipsStateCode getStateCode() {
        return this.stateCode;
    }

    public void setStateCode(FipsStateCode stateCode) {
        this.stateCode = stateCode;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPostalCode() {
        return this.postalCode;
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
        return this.latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return this.longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
    
    public String getComments() {
    	return comments;
    }
    
    public void setComments(String comments) {
    	this.comments = comments;
    }

    public TribalCode getTribalCode() {
        return tribalCode;
    }

    public void setTribalCode(TribalCode tribalCode) {
        this.tribalCode = tribalCode;
    }

    public List<FacilityNAICSXref> getFacilityNAICS() {
        return facilityNAICS;
    }

    public void setFacilityNAICS(List<FacilityNAICSXref> facilityNAICS) {

        this.facilityNAICS.clear();
        if (facilityNAICS != null) {
            this.facilityNAICS.addAll(facilityNAICS);
        }
    }

    public List<EmissionsUnit> getEmissionsUnits() {
        return this.emissionsUnits;
    }

    public void setEmissionsUnits(List<EmissionsUnit> emissionsUnits) {

        this.emissionsUnits.clear();
        if (emissionsUnits != null) {
            this.emissionsUnits.addAll(emissionsUnits);
        }
    }

    public List<ReleasePoint> getReleasePoints() {
        return this.releasePoints;
    }

    public void setReleasePoints(List<ReleasePoint> releasePoints) {

        this.releasePoints.clear();
        if (releasePoints != null) {
            this.releasePoints.addAll(releasePoints);
        };
    }

    public List<FacilitySiteContact> getContacts() {
        return this.contacts;
    }

    public void setContacts(List<FacilitySiteContact> contacts) {

        this.contacts.clear();
        if (contacts != null) {
            this.contacts.addAll(contacts);
        }
    }

    public List<Control> getControls() {
        return controls;
    }

    public void setControls(List<Control> controls) {
        this.controls.clear();
        if (controls != null) {
            this.controls.addAll(controls);
        }
    }

    public List<ControlPath> getControlPaths() {
        return controlPaths;
    }

    public void setControlPaths(List<ControlPath> controlPaths) {
        this.controlPaths.clear();
        if (controlPaths != null) {
            this.controlPaths.addAll(controlPaths);
        }
    }


    /***
     * Set the id property to null for this object and the id for it's direct children.  This method is useful to INSERT the updated object instead of UPDATE.
     */
    public void clearId() {
    	this.id = null;

        for (FacilityNAICSXref naicsXref : this.facilityNAICS) {
        	naicsXref.clearId();
        }
		for (ReleasePoint releasePoint : this.releasePoints) {
			releasePoint.clearId();
		}
		for (EmissionsUnit emissionsUnit : this.emissionsUnits) {
			emissionsUnit.clearId();
		}
        for (FacilitySiteContact contact : this.contacts) {
        	contact.clearId();
        }
		for (Control control : this.controls) {
			control.clearId();
		}
		for (ControlPath controlPath : this.controlPaths) {
			controlPath.clearId();
		}
    }


    public boolean isSameFacilityInfo(FacilitySite other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        return Objects.equals(agencyFacilityIdentifier, other.agencyFacilityIdentifier)
                && Objects.equals(city, other.city) && Objects.equals(countryCode, other.countryCode)
                && Objects.equals(countyCode, other.countyCode)
                && Objects.equals(facilityCategoryCode, other.facilityCategoryCode)
                && Objects.equals(facilitySourceTypeCode, other.facilitySourceTypeCode)
                && Objects.equals(latitude, other.latitude) && Objects.equals(longitude, other.longitude)
                && Objects.equals(operatingStatusCode, other.operatingStatusCode)
                && Objects.equals(postalCode, other.postalCode)
                && Objects.equals(programSystemCode, other.programSystemCode)
                && Objects.equals(stateCode, other.stateCode) && Objects.equals(statusYear, other.statusYear)
                && Objects.equals(streetAddress, other.streetAddress) && Objects.equals(tribalCode, other.tribalCode);
    }

    public boolean isSameMailingInfo(FacilitySite other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        return  Objects.equals(mailingCity, other.mailingCity)
                && Objects.equals(mailingPostalCode, other.mailingPostalCode)
                && Objects.equals(mailingStateCode, other.mailingStateCode)
                && Objects.equals(mailingStreetAddress, other.mailingStreetAddress);
    }


    @Override
    public ValidationDetailDto getComponentDetails() {

        ValidationDetailDto dto = new ValidationDetailDto(getId(), getAgencyFacilityIdentifier(), EntityType.FACILITY_SITE, "Facility Site ");
        return dto;
    }
}
