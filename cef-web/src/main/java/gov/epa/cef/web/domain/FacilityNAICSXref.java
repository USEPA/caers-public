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
@Table(name = "facility_naics_xref")
public class FacilityNAICSXref extends BaseAuditEntity {
    
    private static final long serialVersionUID = 1L;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_site_id", nullable = false)
    private FacilitySite facilitySite;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "naics_code", nullable = false)
    private NaicsCode naicsCode;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "naics_code_type", nullable = false)
    private NaicsCodeType naicsCodeType;


    /**
     * Default constructor
     */
    public FacilityNAICSXref() {}


    /**
     * Copy constructor
     * @param facilitySite
     * @param originalNaicsXref
     */
    public FacilityNAICSXref(FacilitySite facilitySite, FacilityNAICSXref originalNaicsXref) {
    	this.id = originalNaicsXref.getId();
    	this.facilitySite = facilitySite;
    	this.naicsCode = originalNaicsXref.getNaicsCode();
    	this.naicsCodeType = originalNaicsXref.getNaicsCodeType();
    }
    
    public FacilitySite getFacilitySite() {
        return facilitySite;
    }

    public NaicsCode getNaicsCode() {
        return naicsCode;
    }

    public NaicsCodeType getNaicsCodeType() {
        return naicsCodeType;
    }

    public void setFacilitySite(FacilitySite facilitySite) {
        this.facilitySite = facilitySite;
    }

    public void setNaicsCode(NaicsCode naicsCode) {
        this.naicsCode = naicsCode;
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
