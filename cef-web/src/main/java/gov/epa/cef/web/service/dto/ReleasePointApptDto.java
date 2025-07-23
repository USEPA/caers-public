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
import java.math.BigDecimal; 

public class ReleasePointApptDto implements Serializable{

    /**
     * default version id
     */
    private static final long serialVersionUID = 1L;
    
    private Long id;

    private Long releasePointId;
    
    private Long facilitySiteId;
    
    private Long emissionsProcessId;
    
    private String releasePointIdentifier;
    
    private String releasePointDescription;

	private CodeLookupDto releasePointTypeCode;
	
	private ControlPathDto controlPath;
    
    private BigDecimal percent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReleasePointId() {
        return releasePointId;
    }

    public void setReleasePointId(Long releasePointId) {
        this.releasePointId = releasePointId;
    }

    public String getReleasePointIdentifier() {
        return releasePointIdentifier;
    }

    public void setReleasePointIdentifier(String releasePointIdentifier) {
        this.releasePointIdentifier = releasePointIdentifier;
    }

    public String getReleasePointDescription() {
        return releasePointDescription;
    }

    public void setReleasePointDescription(String releasePointDescription) {
        this.releasePointDescription = releasePointDescription;
    }

    public CodeLookupDto getReleasePointTypeCode() {
        return releasePointTypeCode;
    }

    public void setReleasePointTypeCode(CodeLookupDto releasePointTypeCode) {
        this.releasePointTypeCode = releasePointTypeCode;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

	public Long getEmissionsProcessId() {
		return emissionsProcessId;
	}

	public void setEmissionsProcessId(Long emissionsProcessId) {
		this.emissionsProcessId = emissionsProcessId;
	}

	public Long getFacilitySiteId() {
		return facilitySiteId;
	}

	public void setFacilitySiteId(Long facilitySiteId) {
		this.facilitySiteId = facilitySiteId;
	}
    
    public ReleasePointApptDto withId(Long id) {
        setId(id);
        return this;
    }

	public ControlPathDto getControlPath() {
		return controlPath;
	}

	public void setControlPath(ControlPathDto controlPath) {
		this.controlPath = controlPath;
	}
    
}
