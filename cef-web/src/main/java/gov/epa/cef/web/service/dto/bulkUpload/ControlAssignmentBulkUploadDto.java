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

import com.fasterxml.jackson.annotation.JsonIgnore;

import gov.epa.cef.web.annotation.CsvColumn;
import gov.epa.cef.web.annotation.CsvFileName;

import java.io.Serializable;

@CsvFileName(name = "control_assignments.csv")
public class ControlAssignmentBulkUploadDto extends BaseWorksheetDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String agencyFacilityIdentifier;

    private Long controlId;

    private Long controlPathChildId;

    @NotNull(message = "Control Path ID is required.")
    private Long controlPathId;

    private String pathName;

    private String childPathName;

    private String childPathDescription;

    private String controlName;

    private String controlDescription;

    @NotNull(message = "Control Assignment ID is required.")
    private Long id;

    @NotBlank(message = "Percent Apportionment is required.")
    @Pattern(regexp = "^\\d{0,3}(\\.\\d{1,2})?$",
        message = "Percent Apportionment is not in expected numeric format: '{3}.{2}' digits; found '${validatedValue}'.")
    private String percentApportionment;

    @NotBlank(message = "Sequence number is required.")
    @Pattern(regexp = PositiveIntPattern,
        message = "Sequence number is not in expected numeric format: '{10}' digits; found '${validatedValue}'.")
    private String sequenceNumber;

    public ControlAssignmentBulkUploadDto() {

        super(WorksheetName.ControlAssignment);
    }

    @JsonIgnore
    @CsvColumn(name = "Agency Facility ID", order = 1)
	public String getAgencyFacilityIdentifier() {
        return this.agencyFacilityIdentifier;
    }
    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }

    @CsvColumn(name = "Internal Control Assignment ID", order = 2)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @CsvColumn(name = "Internal Path ID", order = 3)
    public Long getControlPathId() {
        return controlPathId;
    }
    public void setControlPathId(Long controlPathId) {
        this.controlPathId = controlPathId;
    }

    @CsvColumn(name = "Agency Path ID", order = 4)
    public String getPathName() {
        return pathName;
    }
    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    @CsvColumn(name = "Agency Control ID", order = 5)
    public String getControlName() {
        return controlName;
    }
    public void setControlName(String controlName) {
        this.controlName = controlName;
    }

    @CsvColumn(name = "Internal Control ID", order = 6)
    public Long getControlId() {
        return controlId;
    }
    public void setControlId(Long controlId) {
        this.controlId = controlId;
    }

    @CsvColumn(name = "Control Description", order = 7)
    public String getControlDescription() { return this.controlDescription; }
    public void setControlDescription(String controlDescription) { this.controlDescription = controlDescription; }

    @CsvColumn(name = "Agency Child Path ID", order = 8)
    public String getChildPathName() {
        return childPathName;
    }
    public void setChildPathName(String childPathName) {
        this.childPathName = childPathName;
    }

    @CsvColumn(name = "Internal Child Path ID", order = 9)
    public Long getControlPathChildId() {
        return controlPathChildId;
    }
    public void setControlPathChildId(Long controlPathChildId) {
        this.controlPathChildId = controlPathChildId;
    }

    @CsvColumn(name = "Child Path Description", order = 10)
    public String getChildPathDescription() { return childPathDescription; }
    public void setChildPathDescription(String childPathDescription) {
        this.childPathDescription = childPathDescription;
    }

    @CsvColumn(name = "Sequence Number", order = 11)
    public String getSequenceNumber() {
        return sequenceNumber;
    }
    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @CsvColumn(name = "Percent Path Apportionment", order = 12)
    public String getPercentApportionment() {
        return percentApportionment;
    }
    public void setPercentApportionment(String percentApportionment) {
        this.percentApportionment = percentApportionment;
    }

    public String getErrorIdentifier() {
        return "controlAssignments-id: " + id;
    }

}
