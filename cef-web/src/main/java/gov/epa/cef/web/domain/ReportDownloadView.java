/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.domain;


import gov.epa.cef.web.domain.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "vw_report_download")
public class ReportDownloadView extends BaseEntity{

    /**
     * default version
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "report_id", nullable = false)
    private Long reportId;

    @Column(name = "facility_site_id")
    private String facilitySiteId;

    @Column(name = "inventory_year")
    private Short reportYear;

    @Column(name = "emissions_unit_id", length = 20)
    private String emissionsUnitId;

    @Column(name = "emission_unit_description", length = 100)
    private String emissionUnitDescription;

    @Column(name = "process_id", length = 20)
    private String processId;

    @Column(name = "process_description", length = 200)
    private String processDescription;

    @Column(name = "scc_code", length = 20)
    private String sccCode;

	@Column(name = "pollutant_name", length = 200)
    private String pollutantName;

    @Column(name = "emissions_uom_code", length = 20)
    private String emissionsUomCode;

    @Column(name = "emission_calc_method", length = 200)
    private String emissionsCalcMethod;

	@Column(name = "emissions_numerator_uom", length = 20)
    private String emissionsNumeratorUom;

    @Column(name = "emissions_denominator_uom", length = 20)
    private String emissionsDenominatorUom;

    @Column(name = "emissions_factor")
    private BigDecimal emissionsFactor;

    @Column(name = "emissions_factor_text", length = 100)
    private String emissionsFactorText;

    @Column(name = "emissions_comment", length = 400)
    private String emissionsComment;

    @Column(name = "reporting_period_type", length = 50)
    private String reportingPeriodType;

    @Column(name = "throughput_uom", length = 20)
    private String throughputUom;

    @Column(name= "overall_control_percent")
    private BigDecimal overallControlPercent;

    @Column(name = "throughput_amount")
    private BigDecimal throughputValue;

    @Column(name = "throughput_material", length = 200)
    private String throughputMaterial;

    @Column(name = "fuel_value")
    private BigDecimal fuelValue;

    @Column(name = "fuel_uom", length = 20)
    private String fuelUom;

    @Column(name = "fuel_material", length = 200)
    private String fuelMaterial;

	@Column(name = "heat_content_ratio")
    private BigDecimal heatContentRatio;

    @Column(name = "heat_content_ratio_numerator", length = 20)
    private String heatContentRatioNumerator;

	@Column(name = "total_emissions")
    private BigDecimal totalEmissions;

    @Column(name = "apportioned_emissions")
    private BigDecimal apportionedEmissions;

    @Column(name = "path_id")
    private String pathId;

    @Column(name = "path_description")
    private String pathDescription;

    @Column(name = "release_point_apportionment")
    private BigDecimal releasePointApportionment;

    @Column(name = "release_point_id")
    private String releasePointId;

    @Column(name = "last_modified_by", length = 255)
    private String lastModifiedBy;

	@Column(name = "last_modified_date")
	private Date lastModifiedDate;

	@Column(name = "calculation_comment", length = 4000)
    private String calculationComment;

    @Column(name = "not_reporting")
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

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
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
	public String getEmissionsCalcMethod() {
		return emissionsCalcMethod;
	}

	public void setEmissionsCalcMethod(String emissionsCalcMethod) {
		this.emissionsCalcMethod = emissionsCalcMethod;
	}

	public String getEmissionsUomCode() {
		return emissionsUomCode;
	}

	public void setEmissionsUomCode(String emissionsUomCode) {
		this.emissionsUomCode = emissionsUomCode;
	}

	public String getReportingPeriodType() {
		return reportingPeriodType;
	}

	public void setReportingPeriodType(String reportingPeriodType) {
		this.reportingPeriodType = reportingPeriodType;
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

	public BigDecimal getFuelValue() {
		return fuelValue;
	}

	public void setFuelValue(BigDecimal fuelValue) {
		this.fuelValue = fuelValue;
	}

	public String getFuelUom() {
		return fuelUom;
	}

	public void setFuelUom(String fuelUom) {
		this.fuelUom = fuelUom;
	}

	public String getFuelMaterial() {
		return fuelMaterial;
	}

	public void setFuelMaterial(String fuelMaterial) {
		this.fuelMaterial = fuelMaterial;
	}

	public BigDecimal getHeatContentRatio() {
		return heatContentRatio;
	}

	public void setHeatContentRatio(BigDecimal heatContentValue) {
		this.heatContentRatio = heatContentValue;
	}

	public String getHeatContentRatioNumerator() {
		return heatContentRatioNumerator;
	}

	public void setHeatContentRatioNumerator(String heatContentUom) {
		this.heatContentRatioNumerator = heatContentUom;
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
