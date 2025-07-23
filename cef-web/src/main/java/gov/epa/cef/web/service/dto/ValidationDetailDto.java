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
import java.util.ArrayList;
import java.util.List;

public class ValidationDetailDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String identifier;
    private EntityType type;
    private String description;
    private List<ValidationDetailDto> parents = new ArrayList<>();

    public ValidationDetailDto() {}

    public ValidationDetailDto(Long id, String identifier, EntityType type) {
        this.id = id;
        this.identifier = identifier;
        this.type = type;
    }
    
    public ValidationDetailDto(Long id, String identifier, EntityType type, String description) {
        this.id = id;
        this.identifier = identifier;
        this.type = type;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ValidationDetailDto> getParents() {
        return parents;
    }

    public void setParents(List<ValidationDetailDto> parents) {

        this.parents.clear();
        if (parents != null) {
            this.parents.addAll(parents);
        }
    }

    public ValidationDetailDto withParents(List<ValidationDetailDto> parents) {

        this.parents.clear();
        if (parents != null) {
            this.parents.addAll(parents);
        }
        return this;
    }

    /**
     * Add parent and that parent's parents then clean up the parent to remove redundant data
     * @param parent
     * @return
     */
    public ValidationDetailDto withParent(ValidationDetailDto parent) {

        if (parent != null) {
            this.parents.add(parent);
            this.parents.addAll(parent.getParents());
            parent.getParents().clear();
        }
        return this;
    }

    /**
     * Clear id of this item and any parents it has
     */
    public void clearId() {
        this.id = null;

    }

    /**
     * Clear id of this item and any parents it has
     */
    public ValidationDetailDto withClearId() {

        clearId();
        return this;
    }

}
