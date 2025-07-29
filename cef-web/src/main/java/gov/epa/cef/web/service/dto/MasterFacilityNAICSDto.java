/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
