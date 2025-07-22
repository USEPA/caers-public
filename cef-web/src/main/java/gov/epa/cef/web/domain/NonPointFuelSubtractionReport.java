/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import gov.epa.cef.web.domain.common.BaseEntity;

/**
 * NonPointFuelSubtractionReport entity
 */
@Entity
public class NonPointFuelSubtractionReport extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Column(name = "id")
    private Long id;
    
    @Column(name = "non_point_bucket")
    private String nonPointBucket;
    
    @Column(name = "sector")
    private String sector;
    
    @Column(name = "fuel")
    private String fuel;
    
    @Column(name = "scc_code")
    private String sccCode;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "fuel_use")
    private BigDecimal fuelUse;
    
    @Column(name = "non_point_standardized_uom")
    private String nonPointStandardizedUom;
    
    @Column(name = "unit_description")
    private String unitDescription;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNonPointBucket() {
		return nonPointBucket;
	}

	public void setNonPointBucket(String nonPointBucket) {
		this.nonPointBucket = nonPointBucket;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getFuel() {
		return fuel;
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public String getSccCode() {
		return sccCode;
	}

	public void setSccCode(String sccCode) {
		this.sccCode = sccCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getFuelUse() {
		return fuelUse;
	}

	public void setFuelUse(BigDecimal fuelUse) {
		this.fuelUse = fuelUse;
	}

	public String getNonPointStandardizedUom() {
		return nonPointStandardizedUom;
	}

	public void setNonPointStandardizedUom(String nonPointStandardizedUom) {
		this.nonPointStandardizedUom = nonPointStandardizedUom;
	}

	public String getUnitDescription() {
		return unitDescription;
	}

	public void setUnitDescription(String unitDescription) {
		this.unitDescription = unitDescription;
	}

}