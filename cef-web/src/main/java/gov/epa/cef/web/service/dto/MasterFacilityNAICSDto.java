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

import gov.epa.cef.web.domain.NaicsCodeType;

public class MasterFacilityNAICSDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
    private Long masterFacilityRecordId;
    private String code;
    private String description;
    private NaicsCodeType naicsCodeType;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMasterFacilityRecordId() {
		return masterFacilityRecordId;
	}
	public void setMasterFacilityRecordId(Long masterFacilityRecordId) {
		this.masterFacilityRecordId = masterFacilityRecordId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public NaicsCodeType getNaicsCodeType() {
		return naicsCodeType;
	}
	public void setNaicsCodeType(NaicsCodeType naicsCodeType) {
		this.naicsCodeType = naicsCodeType;
	}
	public MasterFacilityNAICSDto withId(Long id) {
    	setId(id);
    	return this;
    }
    
}
