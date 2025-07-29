/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import gov.epa.cef.web.domain.common.BaseAuditEntity;

@Entity
@Table(name = "control_assignment")
public class ControlAssignment extends BaseAuditEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "control_id")
    private Control control;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "control_path_id", nullable = false)
    private ControlPath controlPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "control_path_child_id")
    private ControlPath controlPathChild;
    
    @Column(name = "sequence_number", nullable = false)
    private Integer sequenceNumber;
    
    @Column(name = "percent_apportionment", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentApportionment;

    
    /**
     * Default constructor
     */
    public ControlAssignment() {}
    
    
    /**
     * Copy constructor
     * @param control
     * @param controlAssignment
     */
    public ControlAssignment(ControlPath controlPath, Control newControl, ControlPath newControlPathChild, ControlAssignment originalControlAssignment) {
    	this.id = originalControlAssignment.getId();
    	this.controlPath = controlPath;
    	this.controlPathChild = newControlPathChild;
    	this.control = newControl;
        this.sequenceNumber = originalControlAssignment.getSequenceNumber();
        this.percentApportionment = originalControlAssignment.getPercentApportionment();
    }
    
    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    public ControlPath getControlPath() {
        return controlPath;
    }

    public void setControlPath(ControlPath controlPath) {
        this.controlPath = controlPath;
    }

    public ControlPath getControlPathChild() {
        return controlPathChild;
    }

    public void setControlPathChild(ControlPath controlPathChild) {
        this.controlPathChild = controlPathChild;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public BigDecimal getPercentApportionment() {
        return this.percentApportionment;
    }

    public void setPercentApportionment(BigDecimal percentApportionment) {
        this.percentApportionment = percentApportionment;
    }
    
    
    /***
     * Set the id property to null for this object and the id for it's direct children.  This method is useful to INSERT the updated object instead of UPDATE.
     */
    public void clearId() {
    	this.id = null;
    }

}
