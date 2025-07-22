/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
