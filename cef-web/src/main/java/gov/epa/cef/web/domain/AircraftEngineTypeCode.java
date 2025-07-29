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

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * AircraftEngineTypeCode entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "aircraft_engine_type_code")
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class AircraftEngineTypeCode implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    // Fields

    @Id
    @Column(name = "code", unique = true, nullable = false, length = 10)
    private String code;

    @Column(name = "faa_aircraft_type", length = 50)
    private String faaAircraftType;

    @Column(name = "edms_accode", length = 15)
    private String edmsAccode;

    @Column(name = "engine_manufacturer")
    private String engineManufacturer;

    @Column(name = "engine", length = 70)
    private String engine;

    @Column(name = "edms_uid", length = 10)
    private String edmsUid;

    @Column(name = "scc", length = 10)
    private String scc;

    @Column(name = "last_inventory_year")
    private Integer lastInventoryYear;

    // Property accessors
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFaaAircraftType() {
        return this.faaAircraftType;
    }

    public void setFaaAircraftType(String faaAircraftType) {
        this.faaAircraftType = faaAircraftType;
    }

    public String getEdmsAccode() {
        return this.edmsAccode;
    }

    public void setEdmsAccode(String edmsAccode) {
        this.edmsAccode = edmsAccode;
    }

    public String getEngineManufacturer() {
        return this.engineManufacturer;
    }

    public void setEngineManufacturer(String engineManufacturer) {
        this.engineManufacturer = engineManufacturer;
    }

    public String getEngine() {
        return this.engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getEdmsUid() {
        return this.edmsUid;
    }

    public void setEdmsUid(String edmsUid) {
        this.edmsUid = edmsUid;
    }

    public String getScc() {
        return this.scc;
    }

    public void setScc(String scc) {
        this.scc = scc;
    }

    public Integer getLastInventoryYear() {
        return lastInventoryYear;
    }

    public void setLastInventoryYear(Integer lastInventoryYear) {
        this.lastInventoryYear = lastInventoryYear;
    }

}
