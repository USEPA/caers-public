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

import gov.epa.cef.web.domain.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * NonPointFuelSubtractionReport entity
 */
@Entity
public class AverageNumberQAsReport extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    private Long id;

    @Column(name = "program_system_code")
    private String programSystemCode;

    @Column(name = "year")
    private short year;

    @Column(name = "avg_errors")
    private Integer avgErrors;

    @Column(name = "avg_warnings")
    private Integer avgWarnings;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getProgramSystemCode() {
        return programSystemCode;
    }

    public void setProgramSystemCode(String programSystemCode) {
        this.programSystemCode = programSystemCode;
    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public Integer getAvgErrors() {
        return avgErrors;
    }

    public void setAvgErrors(Integer avgErrors) {
        this.avgErrors = avgErrors;
    }

    public Integer getAvgWarnings() {
        return avgWarnings;
    }

    public void setAvgWarnings(Integer avgWarnings) {
        this.avgWarnings = avgWarnings;
    }
}
