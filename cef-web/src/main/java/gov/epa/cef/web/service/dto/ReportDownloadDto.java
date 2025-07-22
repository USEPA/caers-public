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

/**
 * @author ahmahfou
 *
 */
public class ReportDownloadDto implements Serializable{

    /**
     * default version
     */
    private static final long serialVersionUID = 1L;


    private Long reportId;
    private String facilitySiteId;
	private Short reportYear;
	private String emissionsUnitId;
	private String emissionUnitDescription;
	private String processId;
	private String processDescription;
	private String sccCode;
    private String pollutantName;
    private String emissionsUomCode;
    private BigDecimal totalEmissions;
    private String emissionsNumeratorUom;
    private String emissionsDenominatorUom;
    private BigDecimal emissionsFactor;
    private BigDecimal overallControlPercent;
    private String emissionsFactorText;
    private String emissionsComment;
    private String reportingPeriodType;
    private String throughputUom;
    private String throughputMaterial;
    private BigDecimal throughputValue;
	private String emissionsCalcMethod;
	private String fuelMaterial;
	private String fuelUom;
	private BigDecimal fuelValue;
	private String heatContentRatioNumerator;
	private BigDecimal heatContentRatio;
	private BigDecimal apportionedEmissions;
    private String pathId;
    private String pathDescription;
    private BigDecimal releasePointApportionment;
    private String releasePointId;
    private String lastModifiedBy;
	private String lastModifiedDate;
	private String calculationComment;
    private boolean notReporting;

    public String getThroughputUom() {
		return throughputUom;
	}
	public void setThroughputUom(String throughputUom) {
		this.throughputUom = throughputUom;
	}
	public BigDecimal getThroughputValue() {
		return throughputValue;
	}
	public void setThroughputValue(BigDecimal throughputValue) {
		this.throughputValue = throughputValue;
	}

    public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getEmissionsNumeratorUom() {
		return emissionsNumeratorUom;
	}
	public void setEmissionsNumeratorUom(String emissionsNumeratorUom) {
		this.emissionsNumeratorUom = emissionsNumeratorUom;
	}
	public String getEmissionsDenominatorUom() {
		return emissionsDenominatorUom;
	}
	public void setEmissionsDenominatorUom(String emissionsDenominatorUom) {
		this.emissionsDenominatorUom = emissionsDenominatorUom;
	}
	public BigDecimal getEmissionsFactor() {
		return emissionsFactor;
	}
	public void setEmissionsFactor(BigDecimal emissionsFactor) {
		this.emissionsFactor = emissionsFactor;
	}
	public String getEmissionsFactorText() {
		return emissionsFactorText;
	}
	public void setEmissionsFactorText(String emissionsFactorText) {
		this.emissionsFactorText = emissionsFactorText;
	}
	public String getEmissionsComment() {
		return emissionsComment;
	}
	public void setEmissionsComment(String emissionsComment) {
		this.emissionsComment = emissionsComment;
	}
	public Long getReportId() {
		return reportId;
	}
	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}
	public String getFacilitySiteId() {
		return facilitySiteId;
	}
	public void setFacilitySiteId(String facilitySiteId) {
		this.facilitySiteId = facilitySiteId;
	}
	public Short getReportYear() {
		return reportYear;
	}
	public void setReportYear(Short reportYear) {
		this.reportYear = reportYear;
	}
	public String getEmissionsUnitId() {
		return emissionsUnitId;
	}
	public void setEmissionsUnitId(String emissionsUnitId) {
		this.emissionsUnitId = emissionsUnitId;
	}
	public String getEmissionUnitDescription() {
		return emissionUnitDescription;
	}
	public void setEmissionUnitDescription(String emissionUnitDescription) {
		this.emissionUnitDescription = emissionUnitDescription;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getProcessDescription() {
		return processDescription;
	}
	public void setProcessDescription(String processDescription) {
		this.processDescription = processDescription;
	}
	public String getSccCode() {
        return sccCode;
    }
    public void setSccCode(String sccCode) {
        this.sccCode = sccCode;
    }
    public String getPollutantName() {
		return pollutantName;
	}
	public void setPollutantName(String pollutantName) {
		this.pollutantName = pollutantName;
	}
	public BigDecimal getTotalEmissions() {
		return totalEmissions;
	}
	public void setTotalEmissions(BigDecimal totalEmissions) {
		this.totalEmissions = totalEmissions;
	}
	public String getReportingPeriodType() {
		return reportingPeriodType;
	}
	public void setReportingPeriodType(String reportingPeriodType) {
		this.reportingPeriodType = reportingPeriodType;
	}
	public String getEmissionsUomCode() {
		return emissionsUomCode;
	}
	public void setEmissionsUomCode(String emissionsUomCode) {
		this.emissionsUomCode = emissionsUomCode;
	}
	public String getEmissionsCalcMethod() {
		return emissionsCalcMethod;
	}
	public void setEmissionsCalcMethod(String emissionsCalcMethod) {
		this.emissionsCalcMethod = emissionsCalcMethod;
	}
	public BigDecimal getOverallControlPercent() {
		return overallControlPercent;
	}
	public void setOverallControlPercent(BigDecimal overallControlPercent) {
		this.overallControlPercent = overallControlPercent;
	}
	public String getCalculationComment() {
		return calculationComment;
	}
	public void setCalculationComment(String calculationComment) {
		this.calculationComment = calculationComment;
	}
	public String getThroughputMaterial() {
		return throughputMaterial;
	}
	public void setThroughputMaterial(String throughputMaterial) {
		this.throughputMaterial = throughputMaterial;
	}
	public String getFuelMaterial() {
		return fuelMaterial;
	}
	public void setFuelMaterial(String fuelMaterial) {
		this.fuelMaterial = fuelMaterial;
	}
	public String getFuelUom() {
		return fuelUom;
	}
	public void setFuelUom(String fuelUom) {
		this.fuelUom = fuelUom;
	}

	public BigDecimal getFuelValue() {
		return fuelValue;
	}
	public void setFuelValue(BigDecimal fuelValue) {
		this.fuelValue = fuelValue;
	}
	public String getHeatContentRatioNumerator() {
		return heatContentRatioNumerator;
	}
	public void setHeatContentRatioNumerator(String heatContentUom) {
		this.heatContentRatioNumerator = heatContentUom;
	}
	public BigDecimal getHeatContentRatio() {
		return heatContentRatio;
	}
	public void setHeatContentRatio(BigDecimal heatContentValue) {
		this.heatContentRatio = heatContentValue;
	}
    public BigDecimal getApportionedEmissions() {
        return apportionedEmissions;
    }
    public void setApportionedEmissions(BigDecimal apportionedEmissions) {
        this.apportionedEmissions = apportionedEmissions;
    }
    public String getPathId() {
        return pathId;
    }
    public void setPathId(String pathId) {
        this.pathId = pathId;
    }
    public String getPathDescription() {
        return pathDescription;
    }
    public void setPathDescription(String pathDescription) {
        this.pathDescription = pathDescription;
    }
    public BigDecimal getReleasePointApportionment() {
        return releasePointApportionment;
    }
    public void setReleasePointApportionment(BigDecimal releasePointApportionment) {
        this.releasePointApportionment = releasePointApportionment;
    }
    public String getReleasePointId() {
        return releasePointId;
    }
    public void setReleasePointId(String releasePointId) {
        this.releasePointId = releasePointId;
    }

    public boolean getNotReporting() {
        return notReporting;
    }

    public void setNotReporting(boolean notReporting) {
        this.notReporting = notReporting;
    }
}
