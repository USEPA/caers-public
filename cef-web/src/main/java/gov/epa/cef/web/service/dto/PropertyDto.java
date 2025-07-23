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

import gov.epa.cef.web.provider.system.IPropertyKey;

public class PropertyDto implements IPropertyKey, Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String label;
    private String value;
    private String description;
    private String datatype;
    private Boolean required;
    private Boolean canEdit;

    public PropertyDto() {};

    public PropertyDto(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String configKey() {
        return name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

    public Boolean getCanEdit() { return canEdit; }

    public void setCanEdit(Boolean canEdit) { this.canEdit = canEdit; }

    public PropertyDto withName(final String name) {
        setName(name);
        return this;
    }

    public PropertyDto withValue(final String value) {

        setValue(value);
        return this;
    }

}
