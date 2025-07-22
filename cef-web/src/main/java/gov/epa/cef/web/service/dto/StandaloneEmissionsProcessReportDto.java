/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.dto;

import gov.epa.cef.web.annotation.CsvColumn;
import gov.epa.cef.web.annotation.CsvFileName;

import java.io.Serializable;
import java.math.BigDecimal;

@CsvFileName(name = "standalone_emissions_processes.csv")
public class StandaloneEmissionsProcessReportDto implements Serializable {

    /**
     * default version id
     */
    private static final long serialVersionUID = 1L;

    private String agencyFacilityId;
    private String facilityName;
    private String agencyUnitId;
    private String unitDescription;
    private String unitOperatingStatus;
    private String agencyProcessId;
    private String processDescription;
    private String processOperatingStatus;
    private String scc;
    private BigDecimal avgDaysPerWeek;
    private BigDecimal avgHoursPerDay;
    private Short avgWeeksPerYear;
    private Short hoursPerReportingPeriod;
    private BigDecimal winterOperatingPercent;
    private BigDecimal springOperatingPercent;
    private BigDecimal summerOperatingPercent;
    private BigDecimal fallOperatingPercent;
    private String reportingPeriod;
    private String operatingType;
    private String throughputParameter;
    private String throughputMaterial;
    private BigDecimal throughputValue;
    private String throughputUom;
    private String fuelMaterial;
    private BigDecimal fuelValue;
    private String fuelUom;
    private BigDecimal heatContentRatio;
    private String heatContentRatioNumerator;
    private String heatContentRatioDenominator;

    @CsvColumn(name = "Agency Facility ID", order = 1)
    public String getAgencyFacilityId() {
        return agencyFacilityId;
    }

    public void setAgencyFacilityId(String agencyFacilityId) {
        this.agencyFacilityId = agencyFacilityId;
    }

    @CsvColumn(name = "Facility Name", order = 2)
    public String getFacilityName() { return facilityName; }

    public void setFacilityName(String facilityName) { this.facilityName = facilityName; }

    @CsvColumn(name = "Agency Unit ID", order = 3)
    public String getAgencyUnitId() {
        return agencyUnitId;
    }

    public void setAgencyUnitId(String agencyUnitId) {
        this.agencyUnitId = agencyUnitId;
    }

    @CsvColumn(name = "Unit Description", order = 4)
    public String getUnitDescription() { return unitDescription; }

    public void setUnitDescription(String unitDescription) { this.unitDescription = unitDescription; }

    @CsvColumn(name = "Unit Operating Status", order = 5)
    public String getUnitOperatingStatus() {
        return unitOperatingStatus;
    }

    public void setUnitOperatingStatus(String unitOperatingStatus) {
        this.unitOperatingStatus = unitOperatingStatus;
    }

    @CsvColumn(name = "Agency Process ID", order = 6)
    public String getAgencyProcessId() {
        return agencyProcessId;
    }

    public void setAgencyProcessId(String agencyProcessId) {
        this.agencyProcessId = agencyProcessId;
    }

    @CsvColumn(name = "Process Description", order = 7)
    public String getProcessDescription() {
        return processDescription;
    }

    public void setProcessDescription(String processDescription) {
        this.processDescription = processDescription;
    }

    @CsvColumn(name = "Process Operating Status", order = 8)
    public String getProcessOperatingStatus() {
        return processOperatingStatus;
    }

    public void setProcessOperatingStatus(String processOperatingStatus) { this.processOperatingStatus = processOperatingStatus; }

    @CsvColumn(name = "SCC", order = 9)
    public String getScc() {
        return scc;
    }

    public void setScc(String scc) {
        this.scc = scc;
    }

    @CsvColumn(name = "Avg. Days per Week", order = 10)
    public BigDecimal getAvgDaysPerWeek() {
        return avgDaysPerWeek;
    }

    public void setAvgDaysPerWeek(BigDecimal avgDaysPerWeek) {
        this.avgDaysPerWeek = avgDaysPerWeek;
    }

    @CsvColumn(name = "Avg. Hours per Day", order = 11)
    public BigDecimal getAvgHoursPerDay() {
        return avgHoursPerDay;
    }

    public void setAvgHoursPerDay(BigDecimal avgHoursPerDay) {
        this.avgHoursPerDay = avgHoursPerDay;
    }

    @CsvColumn(name = "Avg. Weeks per Year", order = 12)
    public Short getAvgWeeksPerYear() {
        return avgWeeksPerYear;
    }

    public void setAvgWeeksPerYear(Short avgWeeksPerYear) {
        this.avgWeeksPerYear = avgWeeksPerYear;
    }

    @CsvColumn(name = "Hours per Reporting Period", order = 13)
    public Short getHoursPerReportingPeriod() {
        return hoursPerReportingPeriod;
    }

    public void setHoursPerReportingPeriod(Short hoursPerReportingPeriod) { this.hoursPerReportingPeriod = hoursPerReportingPeriod; }

    @CsvColumn(name = "Winter Operating Percent", order = 14)
    public BigDecimal getWinterOperatingPercent() {
        return winterOperatingPercent;
    }

    public void setWinterOperatingPercent(BigDecimal winterOperatingPercent) { this.winterOperatingPercent = winterOperatingPercent; }

    @CsvColumn(name = "Spring Operating Percent", order = 15)
    public BigDecimal getSpringOperatingPercent() {
        return springOperatingPercent;
    }

    public void setSpringOperatingPercent(BigDecimal springOperatingPercent) { this.springOperatingPercent = springOperatingPercent; }

    @CsvColumn(name = "Summer Operating Percent", order = 16)
    public BigDecimal getSummerOperatingPercent() {
        return summerOperatingPercent;
    }

    public void setSummerOperatingPercent(BigDecimal summerOperatingPercent) { this.summerOperatingPercent = summerOperatingPercent; }

    @CsvColumn(name = "Fall Operating Percent", order = 17)
    public BigDecimal getFallOperatingPercent() {
        return fallOperatingPercent;
    }

    public void setFallOperatingPercent(BigDecimal fallOperatingPercent) { this.fallOperatingPercent = fallOperatingPercent; }

    @CsvColumn(name = "Reporting Period", order = 18)
    public String getReportingPeriod() {
        return reportingPeriod;
    }

    public void setReportingPeriod(String reportingPeriod) { this.reportingPeriod = reportingPeriod; }

    @CsvColumn(name = "Operating Type", order = 19)
    public String getOperatingType() {
        return operatingType;
    }

    public void setOperatingType(String operatingType) {
        this.operatingType = operatingType;
    }

    @CsvColumn(name = "Throughput Parameter", order = 20)
    public String getThroughputParameter() {
        return throughputParameter;
    }

    public void setThroughputParameter(String throughputParameter) {
        this.throughputParameter = throughputParameter;
    }

    @CsvColumn(name = "Throughput Material", order = 21)
    public String getThroughputMaterial() {
        return throughputMaterial;
    }

    public void setThroughputMaterial(String throughputMaterial) {
        this.throughputMaterial = throughputMaterial;
    }

    @CsvColumn(name = "Throughput Value", order = 22)
    public BigDecimal getThroughputValue() {
        return throughputValue;
    }

    public void setThroughputValue(BigDecimal throughputValue) { this.throughputValue = throughputValue; }

    @CsvColumn(name = "Throughput UOM", order = 23)
    public String getThroughputUom() {
        return throughputUom;
    }

    public void setThroughputUom(String throughputUom) {
        this.throughputUom = throughputUom;
    }

    @CsvColumn(name = "Fuel Material", order = 24)
    public String getFuelMaterial() {
        return fuelMaterial;
    }

    public void setFuelMaterial(String fuelMaterial) {
        this.fuelMaterial = fuelMaterial;
    }

    @CsvColumn(name = "Fuel Value", order = 25)
    public BigDecimal getFuelValue() {
        return fuelValue;
    }

    public void setFuelValue(BigDecimal fuelValue) {
        this.fuelValue = fuelValue;
    }

    @CsvColumn(name = "Fuel UOM", order = 26)
    public String getFuelUom() {
        return fuelUom;
    }

    public void setFuelUom(String fuelUom) {
        this.fuelUom = fuelUom;
    }

    @CsvColumn(name = "Heat Content Ratio", order = 27)
    public BigDecimal getHeatContentRatio() {
        return heatContentRatio;
    }

    public void setHeatContentRatio(BigDecimal heatContentRatio) {
        this.heatContentRatio = heatContentRatio;
    }

    @CsvColumn(name = "Heat Content Ratio Numerator", order = 28)
    public String getHeatContentRatioNumerator() {
        return heatContentRatioNumerator;
    }

    public void setHeatContentRatioNumerator(String heatContentRatioNumerator) { this.heatContentRatioNumerator = heatContentRatioNumerator; }

    @CsvColumn(name = "Heat Content Ratio Denominator", order = 29)
    public String getHeatContentRatioDenominator() {
        return heatContentRatioDenominator;
    }

    public void setHeatContentRatioDenominator(String heatContentRatioDenominator) { this.heatContentRatioDenominator = heatContentRatioDenominator; }
}

