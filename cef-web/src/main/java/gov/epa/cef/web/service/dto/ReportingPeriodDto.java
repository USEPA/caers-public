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
