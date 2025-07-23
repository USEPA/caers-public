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
package gov.epa.cef.web.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;


public class ControlAssignmentDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private ControlDto control;
    private ControlDto controlPath;
    private Integer sequenceNumber;
    private ControlPathDto controlPathChild;
    private BigDecimal percentApportionment;
    private Long facilitySiteId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ControlDto getControl() {
        return control;
    }

    public void setControl(ControlDto control) {
        this.control = control;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public ControlPathDto getControlPathChild() {
        return controlPathChild;
    }

    public void setControlPathChild(ControlPathDto controlPathChild) {
        this.controlPathChild = controlPathChild;
    }

	public BigDecimal getPercentApportionment() {
		return percentApportionment;
	}

	public void setPercentApportionment(BigDecimal percentApportionment) {
		this.percentApportionment = percentApportionment;
	}

	public Long getFacilitySiteId() {
		return facilitySiteId;
	}

	public void setFacilitySiteId(Long facilitySiteId) {
		this.facilitySiteId = facilitySiteId;
	}

	public ControlDto getControlPath() {
		return controlPath;
	}

	public void setControlPath(ControlDto controlPath) {
		this.controlPath = controlPath;
	}
    public ControlAssignmentDto withId(Long id) {
        setId(id);
        return this;
    }

}
