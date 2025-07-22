/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
