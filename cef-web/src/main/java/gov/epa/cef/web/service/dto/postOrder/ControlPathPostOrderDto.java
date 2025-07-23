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
import java.util.List;


/***
 * ControlPathPostOrderDto is used to traverse the object hierarchy from the bottom up.  The ControlAssignmentPostOrderDto will contain a reference to this ControlPathPostOrderDto
 * but this ControlPathPostOrderDto will not contain a list of ControlAssignmentPostOrderDto objects.  This helps avoid circular references when traversing the hierarchy post order.
 */
public class ControlPathPostOrderDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String description;
    private List<ReleasePointApptPostOrderDto> releasePointAppts;

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

    public List<ReleasePointApptPostOrderDto> getReleasePointAppts() {
        return releasePointAppts;
    }

    public void setReleasePointAppts(List<ReleasePointApptPostOrderDto> releasePoints) {
        this.releasePointAppts = releasePoints;
    }

}
