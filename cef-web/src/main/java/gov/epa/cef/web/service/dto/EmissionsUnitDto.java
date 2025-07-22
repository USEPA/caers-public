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
import java.util.List;

public class EmissionsUnitDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long facilitySiteId;
    private CodeLookupDto unitTypeCode;
    private CodeLookupDto operatingStatusCode;
    private String operatingStatusCodeDescription;
    private String unitIdentifier;
    private String description;
    private Short statusYear;
    private UnitMeasureCodeDto unitOfMeasureCode;
    private BigDecimal designCapacity;
    private String unitOfMeasureDescription;
    private String comments;
    private List<EmissionsProcessDto> emissionsProcesses;
    private String initialMonthlyReportingPeriod;

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getOperatingStatusCodeDescription() {
        return this.operatingStatusCodeDescription;
    }
    public void setOperatingStatusCodeDescription(String operatingStatusCodeDescription) {
        this.operatingStatusCodeDescription = operatingStatusCodeDescription;
    }

    public Long getFacilitySiteId() {
        return this.facilitySiteId;
    }
    public void setFacilitySiteId(Long facilitySiteId) {
        this.facilitySiteId = facilitySiteId;
    }

    public String getUnitIdentifier() {
        return this.unitIdentifier;
    }
    public void setUnitIdentifier(String unitIdentifier) {
        this.unitIdentifier = unitIdentifier;
    }

    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Short getStatusYear() {
        return this.statusYear;
    }
    public void setStatusYear(Short statusYear) {
        this.statusYear = statusYear;
    }

    public UnitMeasureCodeDto getUnitOfMeasureCode() {
        return this.unitOfMeasureCode;
    }
    public void setUnitOfMeasureCode(UnitMeasureCodeDto unitOfMeasureCode) {
        this.unitOfMeasureCode = unitOfMeasureCode;
    }

    public BigDecimal getDesignCapacity() {
        return designCapacity;
    }
    public void setDesignCapacity(BigDecimal designCapacity) {
        this.designCapacity = designCapacity;
    }
    public String getUnitOfMeasureDescription() {
        return this.unitOfMeasureDescription;
    }
    public void setUnitOfMeasureDescription(String unitOfMeasureDescription) {
        this.unitOfMeasureDescription = unitOfMeasureDescription;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public List<EmissionsProcessDto> getEmissionsProcesses() {
        return emissionsProcesses;
    }
    public void setEmissionsProcesses(List<EmissionsProcessDto> emissionsProcesses) {
        this.emissionsProcesses = emissionsProcesses;
    }

    public EmissionsUnitDto withId(Long id) {
        setId(id);
        return this;
    }

	public CodeLookupDto getOperatingStatusCode() {
		return operatingStatusCode;
	}
	public void setOperatingStatusCode(CodeLookupDto operatingStatusCode) {
		this.operatingStatusCode = operatingStatusCode;
	}
	public CodeLookupDto getUnitTypeCode() {
		return unitTypeCode;
	}
	public void setUnitTypeCode(CodeLookupDto unitTypeCode) {
		this.unitTypeCode = unitTypeCode;
	}

    public String getInitialMonthlyReportingPeriod() {
        return initialMonthlyReportingPeriod;
    }

    public void setInitialMonthlyReportingPeriod(String initialMonthlyReportingPeriod) {
        this.initialMonthlyReportingPeriod = initialMonthlyReportingPeriod;
    }
}
