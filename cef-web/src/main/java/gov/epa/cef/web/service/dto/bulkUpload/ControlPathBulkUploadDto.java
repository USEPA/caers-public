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

@CsvFileName(name = "control_paths.csv")
public class ControlPathBulkUploadDto  extends BaseWorksheetDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String agencyFacilityIdentifier;
    
    @NotNull(message = "Control Path ID is required.")
    private Long id;

    @NotNull(message = "Facility Site ID is required.")
    private Long facilitySiteId;

    @Size(max = 200, message = "Description can not exceed {max} chars; found ${validatedValue}.")
    private String description;

    @NotBlank(message = "Path ID is required.")
    @Size(max = 20, message = "Path ID can not exceed {max} chars; found ${validatedValue}.")
    private String pathId;
    
    //percentControl maps to Percent Path Effectiveness in the UI
    @Pattern(regexp = "^\\d{0,3}(\\.\\d)?$",
        message = "Percent Path Effectiveness is not in expected numeric format: '{3}.{1}' digits; found '${validatedValue}'.")
    private String percentControl;

    public ControlPathBulkUploadDto() {

        super(WorksheetName.ControlPath);
    }

    @JsonIgnore
    @CsvColumn(name = "Agency Facility ID", order = 1)
	public String getAgencyFacilityIdentifier() {
        return this.agencyFacilityIdentifier;
    }
    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }

    @CsvColumn(name = "Internal Facility Site ID", order = 2)
    public Long getFacilitySiteId() {
        return facilitySiteId;
    }
    public void setFacilitySiteId(Long facilitySiteId) {
        this.facilitySiteId = facilitySiteId;
    }

    @CsvColumn(name = "Agency Path ID", order = 3)
    public String getPathId() {
        return pathId;
    }
    public void setPathId(String pathId) {
        this.pathId = pathId;
    }

    @CsvColumn(name = "Path Description", order = 4)
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    @CsvColumn(name = "Internal Control Path ID", order = 5)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @CsvColumn(name = "Percent Path Effectiveness", order = 6)
	public String getPercentControl() {
        return percentControl;
    }
    public void setPercentControl(String percentControl) {
        this.percentControl = percentControl;
    }
    
    public String getErrorIdentifier() {
        return "controlPaths-id: " + id;
    }

}
