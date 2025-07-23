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

import com.fasterxml.jackson.annotation.JsonIgnore;

import gov.epa.cef.web.annotation.CsvColumn;
import gov.epa.cef.web.annotation.CsvFileName;

import java.io.Serializable;

@CsvFileName(name = "facility_naics.csv")
public class FacilityNAICSBulkUploadDto extends BaseWorksheetDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String agencyFacilityIdentifier;

    @NotNull(message = "NAICS ID is required.")
	private Long id;

    @NotNull(message = "Facility Site ID is required.")
	private Long facilitySiteId;

    @NotBlank(message = "NAICS code is required.")
    @Pattern(regexp = "^\\d{0,6}$",
        message = "NAICS is not in expected numeric format; found '${validatedValue}'.")
	private String code;
    
    private String description;

	private String naicsCodeType;
    
	private Boolean primaryFlag;

    public FacilityNAICSBulkUploadDto() {

        super(WorksheetName.FacilityNaics);
    }
    
    @JsonIgnore
    @CsvColumn(name = "Agency Facility ID", order = 1)
    public String getAgencyFacilityIdentifier() {
        return agencyFacilityIdentifier;
    }
    
    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }

    @CsvColumn(name = "Internal Facility NAICS ID", order = 2)
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    @CsvColumn(name = "Internal Facility ID", order = 3)
	public Long getFacilitySiteId() {
		return facilitySiteId;
	}

	public void setFacilitySiteId(Long facilitySiteId) {
		this.facilitySiteId = facilitySiteId;
	}

    @CsvColumn(name = "NAICS Code", order = 4)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

    @CsvColumn(name = "NAICS Description", order = 5)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

    @CsvColumn(name = "NAICS Type", order = 6)
	public String getNaicsCodeType() {
		return naicsCodeType;
	}

	public void setNaicsCodeType(String naicsCodeType) {
		this.naicsCodeType = naicsCodeType;
	}

    public Boolean isPrimaryFlag() {
		return primaryFlag;
	}

    public void setPrimaryFlag(Boolean primaryFlag) {
		this.primaryFlag = primaryFlag;
	}
    
    public String getErrorIdentifier() {
        return "facilityNAICS-id: " + id;
    }

}
