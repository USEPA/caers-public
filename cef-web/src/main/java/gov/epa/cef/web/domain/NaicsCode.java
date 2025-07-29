/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
