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
import java.util.List;


public class ControlPathDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String description;
    private String pathId;
    //percentControl maps to Percent Path Effectiveness in the UI
    private BigDecimal percentControl;
    private List<ControlAssignmentDto> assignments;
    private Long facilitySiteId;
    private List<ControlPathPollutantDto> pollutants;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ControlAssignmentDto> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<ControlAssignmentDto> assignments) {
        this.assignments = assignments;
    }

	public String getPathId() {
		return pathId;
	}

	public void setPathId(String pathId) {
		this.pathId = pathId;
	}
	
	public BigDecimal getPercentControl() {
        return percentControl;
    }

    public void setPercentControl(BigDecimal percentControl) {
        this.percentControl = percentControl;
    }
	
    public ControlPathDto withId(Long id) {
        setId(id);
        return this;
    }

	public Long getFacilitySiteId() {
		return facilitySiteId;
	}

	public void setFacilitySiteId(Long facilitySiteId) {
		this.facilitySiteId = facilitySiteId;
	}
	
	public List<ControlPathPollutantDto> getPollutants() {
		return pollutants;
	}

	public void setPollutants(List<ControlPathPollutantDto> pollutants) {
		this.pollutants = pollutants;
	}

}
