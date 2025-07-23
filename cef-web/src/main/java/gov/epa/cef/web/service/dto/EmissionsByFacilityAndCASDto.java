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
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EmissionsByFacilityAndCASDto implements Serializable {
    
    /**
     * default version id
     */
    private static final long serialVersionUID = 1L;
    
    private String message;
    private String code;
    private String trifid;
    private String facilityName;
    private Short year;
    private String reportStatus;
    private String chemical;
    private String casNumber;
    private BigDecimal stackEmissions;
    private BigDecimal fugitiveEmissions;
    private String uom;
    private Date certificationDate;
    
    @JsonIgnore
    private Long reportId;
    
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
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
	public String getReportStatus() {
		return reportStatus;
	}
	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}
	public String getChemical() {
        return chemical;
    }
    public void setChemical(String chemical) {
        this.chemical = chemical;
    }
    public String getCasNumber() {
        return casNumber;
    }
    public void setCasNumber(String casNumber) {
        this.casNumber = casNumber;
    }
    public BigDecimal getStackEmissions() {
        return stackEmissions;
    }
    public void setStackEmissions(BigDecimal stackEmissions) {
        this.stackEmissions = stackEmissions;
    }
    public BigDecimal getFugitiveEmissions() {
        return fugitiveEmissions;
    }
    public void setFugitiveEmissions(BigDecimal fugitiveEmissions) {
        this.fugitiveEmissions = fugitiveEmissions;
    }
    public String getUom() {
        return uom;
    }
    public void setUom(String uom) {
        this.uom = uom;
    }
	public Date getCertificationDate() {
		return certificationDate;
	}
	public void setCertificationDate(Date certificationDate) {
		this.certificationDate = certificationDate;
	}
	public Long getReportId() {
		return reportId;
	}
	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}
}
