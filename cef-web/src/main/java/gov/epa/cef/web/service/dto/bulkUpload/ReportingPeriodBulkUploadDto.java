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
package gov.epa.cef.web.service.dto.bulkUpload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gov.epa.cef.web.annotation.CsvColumn;
import gov.epa.cef.web.annotation.CsvFileName;

import java.io.Serializable;

@CsvFileName(name = "reporting_periods.csv")
public class ReportingPeriodBulkUploadDto extends BaseWorksheetDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String agencyFacilityIdentifier;

    private String emissionsProcessIdentifier;

    private String emissionsUnitIdentifier;

    @NotNull(message = "Reporting Period ID is required.")
    private Long id;

    @NotNull(message = "Emissions Process ID is required.")
    private Long emissionsProcessId;

    private String emissionsProcessName;

    private String displayName;

    @NotNull(message = "Reporting Period Type Code is required.")
    @Size(max = 20, message = "Reporting Period Type Code can not exceed {max} chars; found '${validatedValue}'.")
    private String reportingPeriodTypeCode;

    private String reportingPeriodTypeShortName;

    @NotNull(message = "Operating Type Code is required.")
    @Size(max = 20, message = "Operating Type Code can not exceed {max} chars; found '${validatedValue}'.")
    private String emissionsOperatingTypeCode;

    private String emissionsOperatingTypeShortName;

	@NotNull(message = "Throughput Parameter Type Code is required.")
    @Size(max = 20, message = "Throughput Parameter Type Code can not exceed {max} chars; found '${validatedValue}'.")
    private String calculationParameterTypeCode;

    private String calculationParameterTypeDescription;

    @NotBlank(message = "Throughput Value is required.")
    @Pattern(regexp = PositiveDecimalPattern,
        message = "Throughput Value is not in expected numeric format; found '${validatedValue}'.")
    private String calculationParameterValue;

    @NotNull(message = "Throughput Unit of Measure Code is required.")
    @Size(max = 20, message = "Throughput Unit of Measure Code can not exceed {max} chars; found '${validatedValue}'.")
    private String calculationParameterUom;

    @NotNull(message = "Throughput Material Code is required.")
    @Size(max = 20, message = "Throughput Material Code can not exceed {max} chars; found '${validatedValue}'.")
    private String calculationMaterialCode;

    private String calculationMaterialDescription;

    @Pattern(regexp = PositiveDecimalPattern,
        message = "Fuel Value is not in expected numeric format; found '${validatedValue}'.")
    private String fuelUseValue;

    @Size(max = 20, message = "Fuel Unit of Measure Code can not exceed {max} chars; found '${validatedValue}'.")
    private String fuelUseUom;

    @Size(max = 20, message = "Fuel Material Code can not exceed {max} chars; found '${validatedValue}'.")
    private String fuelUseMaterialCode;

    private String fuelUseMaterialDescription;

	@Pattern(regexp = PositiveDecimalPattern,
        message = "Heat Content Ratio is not in expected numeric format; found '${validatedValue}'.")
    private String heatContentValue;

    @Size(max = 20, message = "Heat Content Ratio Numerator can not exceed {max} chars; found '${validatedValue}'.")
    private String heatContentUom;

    @Size(max = 400, message = "Comments can not exceed {max} chars; found '${validatedValue}'.")
    private String comments;

    public ReportingPeriodBulkUploadDto() {

        super(WorksheetName.ReportingPeriod);
    }

    @JsonIgnore
    @CsvColumn(name = "Agency Facility ID", order = 1)
	public String getAgencyFacilityIdentifier() {
        return this.agencyFacilityIdentifier;
    }

    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }

    @JsonIgnore
    @CsvColumn(name = "Agency Process ID", order = 2)
    public String getEmissionsProcessIdentifier() {
        return this.emissionsProcessIdentifier;
    }
    public void setEmissionsProcessIdentifier(String emissionsProcessIdentifier) {
        this.emissionsProcessIdentifier = emissionsProcessIdentifier;
    }

    @CsvColumn(name = "Internal Process ID", order = 3)
    public Long getEmissionsProcessId() {
        return emissionsProcessId;
    }
    public void setEmissionsProcessId(Long emissionsProcessId) {
        this.emissionsProcessId = emissionsProcessId;
    }

    @JsonIgnore
    @CsvColumn(name = "Agency Unit ID", order = 4)
    public String getEmissionsUnitIdentifier() {
        return this.emissionsUnitIdentifier;
    }
    public void setEmissionsUnitIdentifier(String emissionsUnitIdentifier) {
        this.emissionsUnitIdentifier = emissionsUnitIdentifier;
    }

    @CsvColumn(name = "Internal Unit ID", order = 5)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @CsvColumn(name = "Process Description", order = 6)
    public String getEmissionsProcessName() {
        return emissionsProcessName;
    }
    public void setEmissionsProcessName(String emissionsProcessName) {
        this.emissionsProcessName = emissionsProcessName;
    }

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @CsvColumn(name = "Reporting Period Type Code", order = 7)
    public String getReportingPeriodTypeCode() {
        return reportingPeriodTypeCode;
    }
    public void setReportingPeriodTypeCode(String reportingPeriodTypeCode) {
        this.reportingPeriodTypeCode = reportingPeriodTypeCode;
    }

    @CsvColumn(name = "Reporting Period Type", order = 8)
    public String getReportingPeriodTypeShortName() {
        return reportingPeriodTypeShortName;
    }
    public void setReportingPeriodTypeShortName(String reportingPeriodTypeShortName) {
        this.reportingPeriodTypeShortName = reportingPeriodTypeShortName;
    }


    @CsvColumn(name = "Operating Type Code", order = 9)
    public String getEmissionsOperatingTypeCode() {
        return emissionsOperatingTypeCode;
    }
    public void setEmissionsOperatingTypeCode(String emissionsOperatingTypeCode) {
        this.emissionsOperatingTypeCode = emissionsOperatingTypeCode;
    }

    @CsvColumn(name = "Operating Type", order = 10)
    public String getEmissionsOperatingTypeShortName() {
        return emissionsOperatingTypeShortName;
    }
    public void setEmissionsOperatingTypeShortName(String emissionsOperatingTypeShortName) {
        this.emissionsOperatingTypeShortName = emissionsOperatingTypeShortName;
    }

    @CsvColumn(name = "Throughput Parameter Code", order = 11)
    public String getCalculationParameterTypeCode() {
        return calculationParameterTypeCode;
    }
    public void setCalculationParameterTypeCode(String calculationParameterTypeCode) {
        this.calculationParameterTypeCode = calculationParameterTypeCode;
    }

    @CsvColumn(name = "Throughput Parameter", order = 12)
    public String getCalculationParameterTypeDescription() {
		return calculationParameterTypeDescription;
	}
	public void setCalculationParameterTypeDescription(String calculationParameterTypeDescription) {
		this.calculationParameterTypeDescription = calculationParameterTypeDescription;
	}

    @CsvColumn(name = "Throughput Value", order = 13)
    public String getCalculationParameterValue() {
        return calculationParameterValue;
    }
    public void setCalculationParameterValue(String calculationParameterValue) {
        this.calculationParameterValue = calculationParameterValue;
    }

    @CsvColumn(name = "Throughput UOM", order = 14)
    public String getCalculationParameterUom() {
        return calculationParameterUom;
    }
    public void setCalculationParameterUom(String calculationParameterUom) {
        this.calculationParameterUom = calculationParameterUom;
    }

    @CsvColumn(name = "Throughput Material Code", order = 15)
    public String getCalculationMaterialCode() {
        return calculationMaterialCode;
    }
    public void setCalculationMaterialCode(String calculationMaterialCode) {
        this.calculationMaterialCode = calculationMaterialCode;
    }

    @CsvColumn(name = "Throughput Material", order = 16)
	public String getCalculationMaterialDescription() {
		return calculationMaterialDescription;
	}
	public void setCalculationMaterialDescription(String calculationMaterialDescription) {
		this.calculationMaterialDescription = calculationMaterialDescription;
	}

    @CsvColumn(name = "Fuel Use Value", order = 17)
    public String getFuelUseValue() {
		return fuelUseValue;
	}
	public void setFuelUseValue(String fuelUseValue) {
		this.fuelUseValue = fuelUseValue;
	}

    @CsvColumn(name = "Fuel Use UOM", order = 18)
	public String getFuelUseUom() {
		return fuelUseUom;
	}
	public void setFuelUseUom(String fuelUseUom) {
		this.fuelUseUom = fuelUseUom;
	}

    @CsvColumn(name = "Fuel Material Code", order = 19)
	public String getFuelUseMaterialCode() {
		return fuelUseMaterialCode;
	}
	public void setFuelUseMaterialCode(String fuelUseMaterialCode) {
		this.fuelUseMaterialCode = fuelUseMaterialCode;
	}

    @CsvColumn(name = "Fuel Material", order = 20)
	public String getFuelUseMaterialDescription() {
		return fuelUseMaterialDescription;
	}
	public void setFuelUseMaterialDescription(String fuelUseMaterialDescription) {
		this.fuelUseMaterialDescription = fuelUseMaterialDescription;
	}

    @CsvColumn(name = "Heat Content Value", order = 21)
	public String getHeatContentValue() {
		return heatContentValue;
	}
	public void setHeatContentValue(String heatContentValue) {
		this.heatContentValue = heatContentValue;
	}

    @CsvColumn(name = "Heat Content UOM", order = 22)
	public String getHeatContentUom() {
		return heatContentUom;
	}
	public void setHeatContentUom(String heatContentUom) {
		this.heatContentUom = heatContentUom;
	}

    @CsvColumn(name = "Reporting Period Comments", order = 23)
	public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getErrorIdentifier() {
        return "reportingPeriods-id: " + id;
    }
}
