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
import gov.epa.cef.web.domain.common.BaseEntity;

/**
 * ReportSummary entity
 */
@Entity
public class ReportSummary extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "pollutant_code")
    private String pollutantCode;

    @Column(name = "pollutant_cas_id")
    private String casId;

    @Column(name = "pollutant_name")
    private String pollutantName;

    @Column(name = "pollutant_type")
    private String pollutantType;

    @Column(name = "fugitive_total")
    private BigDecimal fugitiveTotal;

    @Column(name = "stack_total")
    private BigDecimal stackTotal;

    @Column(name = "fugitive_tons_total")
    private BigDecimal fugitiveTonsTotal;

    @Column(name = "stack_tons_total")
    private BigDecimal stackTonsTotal;

    @Column(name = "emissions_tons_total")
    private BigDecimal emissionsTonsTotal;

	@Column(name = "emissions_total")
    private BigDecimal emissionsTotal;

    @Column(name = "previous_year_total")
    private BigDecimal previousYearTotal ;

    @Column(name = "previous_year_tons_total")
    private BigDecimal previousYearTonsTotal ;

    @Column(name = "report_year")
    private Short reportYear;

    @Column(name = "facility_site_id")
    private Long facilitySiteId ;

    @Column(name = "previous_year")
    private Short previousYear;

    @Column(name = "not_reporting")
    private boolean notReporting;

    public String getPollutantCode() {
		return pollutantCode;
	}
	public void setPollutantCode(String pollutantCode) {
		this.pollutantCode = pollutantCode;
	}

    public String getCasId() {
        return this.casId;
    }
    public void setCasId(String casId) {
        this.casId = casId;
    }

    public String getPollutantName() {
        return this.pollutantName;
    }
    public void setPollutantName(String pollutantName) {
        this.pollutantName = pollutantName;
    }

    public String getPollutantType() {
        return this.pollutantType;
    }
    public void setPollutantType(String pollutantType) {
        this.pollutantType = pollutantType;
    }

    public BigDecimal getFugitiveTotal() {
        return this.fugitiveTotal;
    }
    public void setFugitiveTotal(BigDecimal fugitiveTotal) {
        this.fugitiveTotal = fugitiveTotal;
    }

    public BigDecimal getStackTotal() {
        return this.stackTotal;
    }
    public void setStackTotal(BigDecimal stackTotal) {
        this.stackTotal = stackTotal;
    }

	public BigDecimal getFugitiveTonsTotal() {
		return fugitiveTonsTotal;
	}
	public void setFugitiveTonsTotal(BigDecimal fugitiveTonsTotal) {
		this.fugitiveTonsTotal = fugitiveTonsTotal;
	}
	public BigDecimal getStackTonsTotal() {
		return stackTonsTotal;
	}
	public void setStackTonsTotal(BigDecimal stackTonsTotal) {
		this.stackTonsTotal = stackTonsTotal;
	}

    public BigDecimal getEmissionsTonsTotal() {
        return this.emissionsTonsTotal;
    }

    public void setEmissionsTonsTotal(BigDecimal emissionsTonsTotal) {
        this.emissionsTonsTotal = emissionsTonsTotal;
    }

    public BigDecimal getEmissionsTotal() {
		return emissionsTotal;
	}

	public void setEmissionsTotal(BigDecimal emissionsTotal) {
		this.emissionsTotal = emissionsTotal;
	}

    public BigDecimal getPreviousYearTotal() {
        return this.previousYearTotal;
    }
    public void setPreviousYearTotal(BigDecimal previousYearTotal) {
        this.previousYearTotal = previousYearTotal;
    }

    public BigDecimal getPreviousYearTonsTotal() {
		return previousYearTonsTotal;
	}
	public void setPreviousYearTonsTotal(BigDecimal previousYearTonsTotal) {
		this.previousYearTonsTotal = previousYearTonsTotal;
	}
	public Short getReportYear() {
        return this.reportYear;
    }
    public void setReportYear(Short reportYear) {
        this.reportYear = reportYear;
    }

    public Long getFacilitySiteId() {
        return this.facilitySiteId;
    }
    public void setFacilitySiteId(Long facilitySiteId) {
        this.facilitySiteId = facilitySiteId;
    }
    public Short getPreviousYear() {
        return previousYear;
    }
    public void setPreviousYear(Short previousYear) {
        this.previousYear = previousYear;
    }

    public boolean getNotReporting() {
        return notReporting;
    }

    public void setNotReporting(boolean notReporting) {
        this.notReporting = notReporting;
    }
}
