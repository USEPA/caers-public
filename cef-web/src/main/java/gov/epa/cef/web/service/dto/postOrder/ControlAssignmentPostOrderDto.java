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
package gov.epa.cef.web.service.dto.postOrder;

import java.io.Serializable;

import gov.epa.cef.web.service.dto.EmissionsReportItemDto;

/***
 * ControlAssignmentPostOrderDto is used to traverse the object hierarchy from the bottom up.  This ControlAssignmentPostOrderDto will contain a reference to the ControlPathPostOrderDto
 * but the ControlPathPostOrderDto will not contain a list of these ControlAssignmentPostOrderDto objects.  This helps avoid circular references when traversing the hierarchy post order.
 */
public class ControlAssignmentPostOrderDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private EmissionsReportItemDto control;
    private ControlPathPostOrderDto controlPath;
    private String description;
    private Integer sequenceNumber;
    private ControlPathPostOrderDto controlPathChild;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmissionsReportItemDto getControl() {
        return control;
    }

    public void setControl(EmissionsReportItemDto control) {
        this.control = control;
    }

    public ControlPathPostOrderDto getControlPath() {
        return controlPath;
    }

    public void setControlPath(ControlPathPostOrderDto controlPath) {
        this.controlPath = controlPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public ControlPathPostOrderDto getControlPathChild() {
        return controlPathChild;
    }

    public void setControlPathChild(ControlPathPostOrderDto controlPathChild) {
        this.controlPathChild = controlPathChild;
    }

}
