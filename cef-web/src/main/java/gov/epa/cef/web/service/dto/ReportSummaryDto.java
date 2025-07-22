/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReportSummaryDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String casId;
    private String pollutantCode;
    private String pollutantName;
    private String pollutantType;
    private BigDecimal fugitiveTotal;
    private BigDecimal stackTotal;
    private BigDecimal fugitiveTonsTotal;
    private BigDecimal stackTonsTotal;
    private BigDecimal emissionsTonsTotal;
    private BigDecimal emissionsTotal;
    private BigDecimal previousYearTotal;
    private BigDecimal previousYearTonsTotal;
    private Short reportYear;
    private Long facilitySiteId;
    private Short previousYear;
    private Boolean notReporting;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

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
    public Boolean getNotReporting() {
        return notReporting;
    }
    public void setNotReporting(Boolean notReporting) {
        this.notReporting = notReporting;
    }


}
