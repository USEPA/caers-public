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
