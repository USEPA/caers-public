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