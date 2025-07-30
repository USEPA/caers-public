/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
