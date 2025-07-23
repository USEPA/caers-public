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
package gov.epa.cef.web.service.dto;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;

public class PollutantDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String pollutantCode;
    private String pollutantName;
    private String pollutantCasId;
    private String pollutantSrsId;
    private String pollutantType;
    private String pollutantStandardUomCode;
    private Integer lastInventoryYear;
    private Set<PollutantGroupDto> pollutantGroups = new HashSet<>();
    private Set<CodeLookupDto> programSystemCodes = new HashSet<>();

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

    public Set<PollutantGroupDto> getPollutantGroups() {
        return pollutantGroups;
    }

    public void setPollutantGroups(Set<PollutantGroupDto> pollutantGroups) {
        this.pollutantGroups = pollutantGroups;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Set<CodeLookupDto> getProgramSystemCodes() {
		return programSystemCodes;
	}

	public void setProgramSystemCodes(Set<CodeLookupDto> programSystemCodes) {
		this.programSystemCodes = programSystemCodes;
	}

}
