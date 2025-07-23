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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Set;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "pollutant")
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Pollutant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "pollutant_code", nullable = false, length = 12)
    private String pollutantCode;

    @Column(name = "pollutant_name", nullable = false, length = 100)
    private String pollutantName;

    @Column(name = "pollutant_cas_id", length = 100)
    private String pollutantCasId;

    @Column(name = "pollutant_srs_id", nullable = false, length = 40)
    private String pollutantSrsId;

    @Column(name = "pollutant_type", nullable = false, length = 12)
    private String pollutantType;

    @Column(name = "pollutant_standard_uom_code", length = 12)
    private String pollutantStandardUomCode;

    @Column(name = "last_inventory_year")
    private Integer lastInventoryYear;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "pollutant_pollutant_group", joinColumns = @JoinColumn(name = "pollutant_code"), inverseJoinColumns = @JoinColumn(name = "pollutant_group_id"))
    private Set<PollutantGroup> pollutantGroups;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "pollutant_program_system_code", joinColumns = @JoinColumn(name = "pollutant_code"), inverseJoinColumns = @JoinColumn(name = "program_system_code"))
    private Set<ProgramSystemCode> programSystemCodes;

    public String getPollutantCode() {
        return pollutantCode;
    }

    public void setPollutantCode(String pollutantCode) {
        this.pollutantCode = pollutantCode;
    }

    public String getPollutantName() {
        return pollutantName;
    }

    public void setPollutantName(String pollutantName) {
        this.pollutantName = pollutantName;
    }

    public String getPollutantCasId() {
        return pollutantCasId;
    }

    public void setPollutantCasId(String pollutantCasId) {
        this.pollutantCasId = pollutantCasId;
    }

    public String getPollutantSrsId() {
        return pollutantSrsId;
    }

    public void setPollutantSrsId(String pollutantSrsId) {
        this.pollutantSrsId = pollutantSrsId;
    }

    public String getPollutantType() {
        return pollutantType;
    }

    public void setPollutantType(String pollutantType) {
        this.pollutantType = pollutantType;
    }

    public String getPollutantStandardUomCode() {
        return pollutantStandardUomCode;
    }

    public void setPollutantStandardUomCode(String pollutantStandardUomCode) {
        this.pollutantStandardUomCode = pollutantStandardUomCode;
    }

    public Integer getLastInventoryYear() {
        return lastInventoryYear;
    }

    public void setLastInventoryYear(Integer lastInventoryYear) {
        this.lastInventoryYear = lastInventoryYear;
    }

    public Set<PollutantGroup> getPollutantGroups() { return pollutantGroups; }

    public void setPollutantGroups(Set<PollutantGroup> pollutantGroups) { this.pollutantGroups = pollutantGroups; }

    public Set<ProgramSystemCode> getProgramSystemCodes() {
        return programSystemCodes;
    }

    public void setProgramSystemCodes(Set<ProgramSystemCode> programSystemCodes) {
        this.programSystemCodes = programSystemCodes;
    }
}
