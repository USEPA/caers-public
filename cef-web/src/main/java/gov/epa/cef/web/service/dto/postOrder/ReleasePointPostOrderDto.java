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
 * ReleasePointPostOrderDto is used to traverse the object hierarchy from the bottom up.  The ReleasePointApptPostOrderDto will contain a reference to the ReleasePointPostOrderDto
 * but this ReleasePointPostOrderDto will not contain a list of ReleasePointApptPostOrderDto objects.  This helps avoid circular references within the MapStruct mappers when 
 * traversing the hierarchy post order.
 * @author kbrundag
 *
 */
public class ReleasePointPostOrderDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String releasePointIdentifier;
    private String description;
    private String comments;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getReleasePointIdentifier() {
        return releasePointIdentifier;
    }
    public void setReleasePointIdentifier(String releasePointIdentifier) {
        this.releasePointIdentifier = releasePointIdentifier;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }

}
