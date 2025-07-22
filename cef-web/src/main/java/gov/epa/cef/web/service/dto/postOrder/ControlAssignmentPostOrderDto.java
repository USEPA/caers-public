/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
