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


/***
 * ReleasePointApptPostOrderDto is used to traverse the object hierarchy from the bottom up.  This ReleasePointApptPostOrderDto will contain a reference to the ReleasePointPostOrderDto
 * but the ReleasePointPostOrderDto will not contain a list of these ReleasePointApptPostOrderDto objects.  This helps avoid circular references within the MapStruct mappers when 
 * traversing the hierarchy post order.
 * @author kbrundag
 *
 */
public class ReleasePointApptPostOrderDto implements Serializable{

    /**
     * default version id
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private ReleasePointPostOrderDto releasePoint;
    private EmissionsProcessPostOrderDto emissionsProcess;

    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public ReleasePointPostOrderDto getReleasePoint() {
    	return releasePoint;
    }
    public void setReleasePoint(ReleasePointPostOrderDto releasePoint) {
    	this.releasePoint = releasePoint;
    }
    
    public EmissionsProcessPostOrderDto getEmissionsProcess() {
    	return emissionsProcess;
    }
    public void setEmissionsProcess(EmissionsProcessPostOrderDto emissionsProcess) {
    	this.emissionsProcess = emissionsProcess;
    }
}
