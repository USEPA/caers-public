/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
