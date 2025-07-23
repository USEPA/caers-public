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
