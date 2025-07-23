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
