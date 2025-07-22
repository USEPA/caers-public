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

@CsvFileName(name = "control_pollutants.csv")
public class ControlPollutantBulkUploadDto extends BaseWorksheetDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String agencyFacilityIdentifier;

    @NotNull(message = "Control Pollutant ID is required.")
    private Long id;

    @NotNull(message = "Control ID is required.")
    private Long controlId;
    
    private String controlName;

    @NotBlank(message = "Pollutant Code is required.")
    @Size(max = 12, message = "Pollutant Code can not exceed {max} chars; found '${validatedValue}'.")
    private String pollutantCode;
    
    private String pollutantName;

    @NotBlank(message = "Percent Reduction is required.")
    @Pattern(regexp = "^\\d{0,3}(\\.\\d{1})?$",
        message = "Percent Reduction Efficiency is not in expected numeric format: '{3}.{1}' digits; found '${validatedValue}'.")
    private String percentReduction;

    public ControlPollutantBulkUploadDto() {
        super(WorksheetName.ControlPollutant);
    }
    
    @JsonIgnore
    @CsvColumn(name = "Agency Facility ID", order = 1)
	public String getAgencyFacilityIdentifier() {
        return this.agencyFacilityIdentifier;
    }
    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }

    @CsvColumn(name = "Internal Control Pollutant ID", order = 2)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @CsvColumn(name = "Internal Control ID", order = 3)
    public Long getControlId() {
        return controlId;
    }
    public void setControlId(Long controlId) {
        this.controlId = controlId;
    }

    @CsvColumn(name = "Agency Control ID", order = 4)
    public String getControlName() {
    	return controlName;
    }
    public void setControlName(String controlName) {
    	this.controlName = controlName;
    }
    
    @CsvColumn(name = "Pollutant Code", order = 5)
    public String getPollutantCode() {
        return pollutantCode;
    }
    public void setPollutantCode(String pollutant) {
        this.pollutantCode = pollutant;
    }
    
    @CsvColumn(name = "Pollutant", order = 6)
    public String getPollutantName() {
    	return pollutantName;
    }
    public void setPollutantName(String pollutantName) {
    	this.pollutantName = pollutantName;
    }
    
    @CsvColumn(name = "Percent Control Pollutant Reduction Efficiency", order = 7)
    public String getPercentReduction() {
        return percentReduction;
    }
    public void setPercentReduction(String percentReduction) {
        this.percentReduction = percentReduction;
    }
    
    public String getErrorIdentifier() {
        return "controlPollutants-id: " + id;
    }

}
