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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

/**
 * NaicsCode entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "naics_code")
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class NaicsCode implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    // Fields

    @Id
    @Column(name = "code", unique = true, nullable = false, precision = 6, scale = 0)
    private Integer code;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "first_inventory_year")
    private Integer firstInventoryYear;

    @Column(name = "last_inventory_year")
    private Integer lastInventoryYear;

    @Column(name = "map_to", length = 200)
    private String mapTo;

    @Column(name = "non_point_bucket", length = 20)
    private String nonPointBucket;

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFirstInventoryYear() {
        return firstInventoryYear;
    }

    public void setFirstInventoryYear(Integer firstInventoryYear) {
        this.firstInventoryYear = firstInventoryYear;
    }

    public Integer getLastInventoryYear() {
        return lastInventoryYear;
    }

    public void setLastInventoryYear(Integer lastInventoryYear) {
        this.lastInventoryYear = lastInventoryYear;
    }

    public String getMapTo() {
        return mapTo;
    }

    public void setMapTo(String mapTo) {
        this.mapTo = mapTo;
    }

	  public String getNonPointBucket() {
		return nonPointBucket;
	}

	  public void setNonPointBucket(String nonPointBucket) {
		this.nonPointBucket = nonPointBucket;
	}

}
