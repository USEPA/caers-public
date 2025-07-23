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
