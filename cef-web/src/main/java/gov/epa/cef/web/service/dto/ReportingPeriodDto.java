/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.dto;

import java.io.Serializable;
import java.util.List;

import com.google.common.base.Strings;

public class ReportingPeriodDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long emissionsProcessId;
    private CodeLookupDto reportingPeriodTypeCode;
    private CodeLookupDto emissionsOperatingTypeCode;
    private CodeLookupDto calculationParameterTypeCode;
    private String calculationParameterValue;
    private UnitMeasureCodeDto calculationParameterUom;
    private CodeLookupDto calculationMaterialCode;
    private String fuelUseValue;
    private UnitMeasureCodeDto fuelUseUom;
    private CodeLookupDto fuelUseMaterialCode;
    private String heatContentValue;
    private UnitMeasureCodeDto heatContentUom;
    private String comments;
    private List<EmissionDto> emissions;
    private List<OperatingDetailDto> operatingDetails;
    private MonthlyFuelReportingDto monthlyFuelReporting;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmissionsProcessId() {
        return emissionsProcessId;
    }

    public void setEmissionsProcessId(Long emissionsProcessId) {
        this.emissionsProcessId = emissionsProcessId;
    }

    public CodeLookupDto getReportingPeriodTypeCode() {
        return reportingPeriodTypeCode;
    }

    public void setReportingPeriodTypeCode(CodeLookupDto reportingPeriodTypeCode) {
        this.reportingPeriodTypeCode = reportingPeriodTypeCode;
    }

    public CodeLookupDto getEmissionsOperatingTypeCode() {
        return emissionsOperatingTypeCode;
    }

    public void setEmissionsOperatingTypeCode(CodeLookupDto emissionsOperatingTypeCode) {
        this.emissionsOperatingTypeCode = emissionsOperatingTypeCode;
    }

    public CodeLookupDto getCalculationParameterTypeCode() {
        return calculationParameterTypeCode;
    }

    public void setCalculationParameterTypeCode(CodeLookupDto calculationParameterTypeCode) {
        this.calculationParameterTypeCode = calculationParameterTypeCode;
    }

    // this was changed to a string to keep the numbers precise, 
    // however mapstruct cannot convert an empty string to a BigDecimal
    // so this will make sure null is returned instead of an empty string
    public String getCalculationParameterValue() {
        return Strings.emptyToNull(calculationParameterValue);
    }

    public void setCalculationParameterValue(String calculationParameterValue) {
        this.calculationParameterValue = calculationParameterValue;
    }

    public UnitMeasureCodeDto getCalculationParameterUom() {
        return calculationParameterUom;
    }

    public void setCalculationParameterUom(UnitMeasureCodeDto calculationParameterUom) {
        this.calculationParameterUom = calculationParameterUom;
    }

    public CodeLookupDto getCalculationMaterialCode() {
        return calculationMaterialCode;
    }

    public void setCalculationMaterialCode(CodeLookupDto calculationMaterialCode) {
        this.calculationMaterialCode = calculationMaterialCode;
    }

    // this was changed to a string to keep the numbers precise, 
    // however mapstruct cannot convert an empty string to a BigDecimal
    // so this will make sure null is returned instead of an empty string
    public String getFuelUseValue() {
		return Strings.emptyToNull(fuelUseValue);
	}

	public void setFuelUseValue(String fuelUseValue) {
		this.fuelUseValue = fuelUseValue;
	}

	public UnitMeasureCodeDto getFuelUseUom() {
		return fuelUseUom;
	}

	public void setFuelUseUom(UnitMeasureCodeDto fuelUseUom) {
		this.fuelUseUom = fuelUseUom;
	}

	public CodeLookupDto getFuelUseMaterialCode() {
		return fuelUseMaterialCode;
	}

	public void setFuelUseMaterialCode(CodeLookupDto fuelUseMaterialCode) {
		this.fuelUseMaterialCode = fuelUseMaterialCode;
	}

	// this was changed to a string to keep the numbers precise, 
    // however mapstruct cannot convert an empty string to a BigDecimal
    // so this will make sure null is returned instead of an empty string
	public String getHeatContentValue() {
		return Strings.emptyToNull(heatContentValue);
	}

	public void setHeatContentValue(String heatContentValue) {
		this.heatContentValue = heatContentValue;
	}

	public UnitMeasureCodeDto getHeatContentUom() {
		return heatContentUom;
	}

	public void setHeatContentUom(UnitMeasureCodeDto heatContentUom) {
		this.heatContentUom = heatContentUom;
	}

	public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<EmissionDto> getEmissions() {
        return emissions;
    }

    public void setEmissions(List<EmissionDto> emissions) {
        this.emissions = emissions;
    }

    public List<OperatingDetailDto> getOperatingDetails() {
        return operatingDetails;
    }

    public void setOperatingDetails(List<OperatingDetailDto> operatingDetails) {
        this.operatingDetails = operatingDetails;
    }

	public MonthlyFuelReportingDto getMonthlyFuelReporting() {
		return monthlyFuelReporting;
	}

	public void setMonthlyFuelReporting(MonthlyFuelReportingDto monthlyFuelReporting) {
		this.monthlyFuelReporting = monthlyFuelReporting;
	}

	public ReportingPeriodDto withId(Long id) {

        setId(id);
        return this;
    }
}
