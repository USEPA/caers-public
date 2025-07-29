/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
