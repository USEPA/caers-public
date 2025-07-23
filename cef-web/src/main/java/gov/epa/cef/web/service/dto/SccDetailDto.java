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
import java.util.Date;
import java.util.Map;

public class SccDetailDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uid;
    private String code;
    private String sccLevelOne;
    private String sccLevelTwo;
    private String sccLevelThree;
    private String sccLevelFour;
    private String sector;
    private String shortName;
    private String lastUpdated;
    private Date lastUpdatedDate;
    private Map<String, SccAttributeDto> attributes;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

	public String getSccLevelOne() {
		return sccLevelOne;
	}

	public void setSccLevelOne(String sccLevelOne) {
		this.sccLevelOne = sccLevelOne;
	}

	public String getSccLevelTwo() {
		return sccLevelTwo;
	}

	public void setSccLevelTwo(String sccLevelTwo) {
		this.sccLevelTwo = sccLevelTwo;
	}

	public String getSccLevelThree() {
		return sccLevelThree;
	}

	public void setSccLevelThree(String sccLevelThree) {
		this.sccLevelThree = sccLevelThree;
	}

	public String getSccLevelFour() {
		return sccLevelFour;
	}

	public void setSccLevelFour(String sccLevelFour) {
		this.sccLevelFour = sccLevelFour;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Map<String, SccAttributeDto> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, SccAttributeDto> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SccDetailDto [uid=").append(uid).append(", code=").append(code)
        		.append(", sccLevelOne=").append(sccLevelOne).append(", sccLevelTwo=").append(sccLevelTwo)
        		.append(", sccLevelThree=").append(sccLevelThree).append(", sccLevelFour=").append(sccLevelFour)
        		.append(", sector=").append(sector).append(", shortName=").append(shortName).append(", lastUpdated=")
                .append(lastUpdated).append(", lastUpdatedDate=").append(lastUpdatedDate).append(", attributes=")
                .append(attributes).append("]");
        return builder.toString();
    }

}
