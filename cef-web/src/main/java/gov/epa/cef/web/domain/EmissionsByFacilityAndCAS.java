/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.annotation.Immutable;

import gov.epa.cef.web.domain.common.BaseEntity;

/**
 * Entity of the  View v_emissions_by_facility_and_cas 
 */
@Entity
@Immutable
@Table(name = "vw_emissions_by_facility_and_cas")
public class EmissionsByFacilityAndCAS extends BaseEntity {
    
    private static final long serialVersionUID = 1L;

    @Column(name = "trifid")
    private String trifid;
    
    @Column(name = "facility_name")
    private String facilityName;
    
    @Column(name = "year")
    private Short year;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "pollutant_name")
    private String pollutantName;
    
    @Column(name = "pollutant_cas_id")
    private String pollutantCasId;
     
    @Column(name = "apportioned_emissions")
    private BigDecimal apportionedEmissions;
    
    @Column(name = "release_point_identifier")
    private String releasePointIdentifier;
    
    @Column(name = "release_point_type")
    private String releasePointType;
    
    @Column(name = "emissions_uom_code")
    private String emissionsUomCode;
    
    @Column(name = "report_id")
    private Long reportId;
    
    public String getTrifid() {
		return trifid;
	}

	public void setTrifid(String trifid) {
		this.trifid = trifid;
	}

	public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPollutantName() {
        return pollutantName;
    }

    public void setPollutantName(String pollutantName) {
        this.pollutantName = pollutantName;
    }

    public String getPollutantCasId() {
        return pollutantCasId;
    }

    public void setPollutantCasId(String pollutantCasId) {
        this.pollutantCasId = pollutantCasId;
    }

    public String getEmissionsUomCode() {
        return emissionsUomCode;
    }

    public void setEmissionsUomCode(String emissionsUomCode) {
        this.emissionsUomCode = emissionsUomCode;
    }

    public BigDecimal getApportionedEmissions() {
        return apportionedEmissions;
    }

    public void setApportionedEmissions(BigDecimal apportionedEmissions) {
        this.apportionedEmissions = apportionedEmissions;
    }

    public String getReleasePointIdentifier() {
        return releasePointIdentifier;
    }

    public void setReleasePointIdentifier(String releasePointIdentifier) {
        this.releasePointIdentifier = releasePointIdentifier;
    }

    public String getReleasePointType() {
        return releasePointType;
    }

    public void setReleasePointType(String releasePointType) {
        this.releasePointType = releasePointType;
    }

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}
    
}
