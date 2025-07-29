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
