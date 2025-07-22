/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
