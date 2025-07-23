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

@CsvFileName(name = "emission_formula_variables.csv")
public class EmissionFormulaVariableBulkUploadDto extends BaseWorksheetDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String agencyFacilityIdentifier;

    private String pollutantCode;

    private String pollutantName;

    private String emissionsProcessIdentifier;

    private String emissionsUnitIdentifier;

    @NotBlank(message = "Emission Formula Variable Code is required.")
    @Size(max = 20, message = "Emission Formula Variable Code can not exceed {max} chars; found '${validatedValue}'.")
    private String emissionFormulaVariableCode;

    @NotNull(message = "Emission is required.")
    private Long emissionId;

    @NotNull(message = "Emission Factor Formula ID is required.")
    private Long id;

    @NotBlank(message = "Value is required.")
    @Pattern(regexp = PositiveDecimalPattern,
        message = "Value is not in expected numeric format; found '${validatedValue}'.")
    private String value;

    private String emissionsFactorFormula;

    private String emissionFormulaVariableDescription;

	public EmissionFormulaVariableBulkUploadDto() {

        super(WorksheetName.EmissionFormulaVariable);
    }

	@JsonIgnore
    @CsvColumn(name = "Agency Facility ID", order = 1)
	public String getAgencyFacilityIdentifier() {
        return this.agencyFacilityIdentifier;
    }

    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }

    @CsvColumn(name = "EF Formula Variable Code", order = 7)
    public String getEmissionFormulaVariableCode() {

        return emissionFormulaVariableCode;
    }

    public void setEmissionFormulaVariableCode(String emissionFormulaVariableCode) {

        this.emissionFormulaVariableCode = emissionFormulaVariableCode;
    }

    @CsvColumn(name = "Internal Emission ID", order = 3)
    public Long getEmissionId() {

        return emissionId;
    }

    @JsonIgnore
    @CsvColumn(name = "Pollutant Code", order = 4)
    public String getPollutantCode() {
        return this.pollutantCode;
    }
    public void setPollutantCode(String pollutantCode) {
        this.pollutantCode = pollutantCode;
    }

    @JsonIgnore
    @CsvColumn(name = "Pollutant", order = 5)
    public String getPollutantName() {
        return pollutantName;
    }
    public void setPollutantName(String pollutantName) {
        this.pollutantName = pollutantName;
    }

    @JsonIgnore
    @CsvColumn(name = "Agency Process ID", order = 6)
    public String getEmissionsProcessIdentifier() {
        return this.emissionsProcessIdentifier;
    }
    public void setEmissionsProcessIdentifier(String emissionsProcessIdentifier) {
        this.emissionsProcessIdentifier = emissionsProcessIdentifier;
    }

    @JsonIgnore
    @CsvColumn(name = "Agency Unit ID", order = 7)
    public String getEmissionsUnitIdentifier() {
        return this.emissionsUnitIdentifier;
    }
    public void setEmissionsUnitIdentifier(String emissionsUnitIdentifier) {
        this.emissionsUnitIdentifier = emissionsUnitIdentifier;
    }

    public void setEmissionId(Long emissionId) {

        this.emissionId = emissionId;
    }

    @CsvColumn(name = "Internal Variable ID", order = 2)
    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    @CsvColumn(name = "EF Variable Value", order = 9)
    public String getValue() {

        return value;
    }

    public void setValue(String value) {

        this.value = value;
    }

    @CsvColumn(name = "EF Formula", order = 10)
    public String getEmissionsFactorFormula() {
		return emissionsFactorFormula;
	}

	public void setEmissionsFactorFormula(String emissionsFactorFormula) {
		this.emissionsFactorFormula = emissionsFactorFormula;
	}

    @CsvColumn(name = "EF Formula Variable", order = 8)
	public String getEmissionFormulaVariableDescription() {
		return emissionFormulaVariableDescription;
	}

	public void setEmissionFormulaVariableDescription(String emissionFormulaVariableDescription) {
		this.emissionFormulaVariableDescription = emissionFormulaVariableDescription;
	}

	public String getErrorIdentifier() {
        return "emissionFormulaVariables-id: " + id;
    }

}
