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
package gov.epa.cef.web.service.dto.bulkUpload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gov.epa.cef.web.annotation.CsvColumn;
import gov.epa.cef.web.annotation.CsvFileName;

import java.io.Serializable;

@CsvFileName(name = "emissions_units.csv")
public class EmissionsUnitBulkUploadDto extends BaseWorksheetDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String agencyFacilityIdentifier;

    @Size(max = 400, message = "Comments can not exceed {max} chars; found '${validatedValue}'.")
    private String comments;

    @Size(max = 100, message = "Description can not exceed {max} chars; found '${validatedValue}'.")
    private String description;

    @Pattern(regexp = PositiveDecimalPattern,
        message = "Design Capacity is not in expected numeric format; found '${validatedValue}'.")
    private String designCapacity;

    @NotNull(message = "Facility Site ID is required.")
    private Long facilitySiteId;

    @NotNull(message = "Emissions Unit ID is required.")
    private Long id;

    @NotBlank(message = "Operating Status Code is required.")
    @Size(max = 20, message = "Operating Status Code can not exceed {max} chars; found '${validatedValue}'.")
    private String operatingStatusCodeDescription;

    private String operatingStatusCode;

    @Pattern(regexp = YearPattern,
        message = "Year Op Status Changed is not in expected format: {4} digits; found '${validatedValue}'.")
    private String statusYear;

    @NotBlank(message = "Type Code is required.")
    @Size(max = 20, message = "Type Code can not exceed {max} chars; found '${validatedValue}'.")
    private String typeCode;

    private String typeDescription;

    @NotBlank(message = "Unit Identifier is required.")
    @Size(max = 20, message = "Unit Identifier can not exceed {max} chars; found '${validatedValue}'.")
    private String unitIdentifier;

    @Size(max = 20, message = "Unit of Measure Code can not exceed {max} chars; found '${validatedValue}'.")
    private String unitOfMeasureCode;

    public EmissionsUnitBulkUploadDto() {

        super(WorksheetName.EmissionsUnit);
    }

    @JsonIgnore
    @CsvColumn(name = "Agency Facility ID", order = 1)
	public String getAgencyFacilityIdentifier() {
        return this.agencyFacilityIdentifier;
    }

    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }

    @CsvColumn(name = "Unit Comments", order = 11)
    public String getComments() {

        return comments;
    }

    public void setComments(String comments) {

        this.comments = comments;
    }

    @CsvColumn(name = "Unit Description", order = 5)
    public String getDescription() {

        return this.description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    @CsvColumn(name = "Unit Design Capacity", order = 9)
    public String getDesignCapacity() {

        return designCapacity;
    }

    public void setDesignCapacity(String designCapacity) {

        this.designCapacity = designCapacity;
    }

    @CsvColumn(name = "Internal Facility Site ID", order = 2)
    public Long getFacilitySiteId() {

        return this.facilitySiteId;
    }

    public void setFacilitySiteId(Long facilitySiteId) {

        this.facilitySiteId = facilitySiteId;
    }

    @CsvColumn(name = "Internal Unit ID", order = 4)
    public Long getId() {

        return this.id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    @CsvColumn(name = "Unit Op Status Code", order = 7)
    public String getOperatingStatusCodeDescription() {

        return this.operatingStatusCodeDescription;
    }

    public void setOperatingStatusCodeDescription(String operatingStatusCodeDescription) {

        this.operatingStatusCodeDescription = operatingStatusCodeDescription;
    }

    @CsvColumn(name = "Unit Op Status", order = 7)
    public String getOperatingStatusCode() {
        return this.operatingStatusCode;
    }
    public void setOperatingStatusCode(String operatingStatusCode) {
        this.operatingStatusCode = operatingStatusCode;
    }

    @CsvColumn(name = "Unit Op Status Year", order = 8)
    public String getStatusYear() {

        return this.statusYear;
    }

    public void setStatusYear(String statusYear) {

        this.statusYear = statusYear;
    }

    @CsvColumn(name = "Unit Type Code", order = 6)
    public String getTypeCode() {

        return this.typeCode;
    }

    public void setTypeCode(String typeCode) {

        this.typeCode = typeCode;
    }

    @CsvColumn(name = "Unit Type", order = 6)
    public String getTypeDescription() {
        return this.typeDescription;
    }
    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    @CsvColumn(name = "Agency Unit ID", order = 3)
    public String getUnitIdentifier() {

        return this.unitIdentifier;
    }

    public void setUnitIdentifier(String unitIdentifier) {

        this.unitIdentifier = unitIdentifier;
    }

    @CsvColumn(name = "Unit Design Capacity UOM", order = 10)
    public String getUnitOfMeasureCode() {

        return this.unitOfMeasureCode;
    }

    public void setUnitOfMeasureCode(String unitOfMeasureCode) {

        this.unitOfMeasureCode = unitOfMeasureCode;
    }

    public String getErrorIdentifier() {
        return "emissionsUnits-id: " + id;
    }
}
