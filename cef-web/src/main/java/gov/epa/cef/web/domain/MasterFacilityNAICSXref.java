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
package gov.epa.cef.web.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import gov.epa.cef.web.domain.common.BaseAuditEntity;

@Entity
@Table(name = "master_facility_naics_xref")
public class MasterFacilityNAICSXref extends BaseAuditEntity {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_facility_id", nullable = false)
	private MasterFacilityRecord masterFacilityRecord;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "naics_code", nullable = false)
    private NaicsCode naicsCode;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "naics_code_type", nullable = false)
    private NaicsCodeType naicsCodeType;
	
	/**
     * Default constructor
     */
    public MasterFacilityNAICSXref() {}
    
    /**
     * Copy constructor
     * @param masterFacilityRecord
     * @param naicsXref
     */
    public MasterFacilityNAICSXref(MasterFacilityRecord masterFacilityRecord, MasterFacilityNAICSXref originalNaicsXref) {
    	this.id = originalNaicsXref.getId();
    	this.masterFacilityRecord = masterFacilityRecord;
    	this.naicsCode = originalNaicsXref.getNaicsCode();
    	this.naicsCodeType = originalNaicsXref.getNaicsCodeType();
    }

	public MasterFacilityRecord getMasterFacilityRecord() {
		return masterFacilityRecord;
	}

	public void setMasterFacilityRecord(MasterFacilityRecord masterFacilityRecord) {
		this.masterFacilityRecord = masterFacilityRecord;
	}

	public NaicsCode getNaicsCode() {
		return naicsCode;
	}

	public void setNaicsCode(NaicsCode naicsCode) {
		this.naicsCode = naicsCode;
	}

	public NaicsCodeType getNaicsCodeType() {
		return naicsCodeType;
	}

	public void setNaicsCodeType(NaicsCodeType naicsCodeType) {
		this.naicsCodeType = naicsCodeType;
	}
	
	/***
     * Set the id property to null for this object and the id for it's direct children.  This method is useful to INSERT the updated object instead of UPDATE.
     */
    public void clearId() {
    	this.id = null;
    }
}
