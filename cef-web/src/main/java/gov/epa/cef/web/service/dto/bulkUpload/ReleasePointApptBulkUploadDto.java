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

@CsvFileName(name = "release_point_appts.csv")
public class ReleasePointApptBulkUploadDto extends BaseWorksheetDto implements Serializable{

    private static final long serialVersionUID = 1L;

    private String agencyFacilityIdentifier;

    @NotNull(message = "Release Point Apportionment ID is required.")
    private Long id;

    @NotNull(message = "Release Point ID is required.")
    private Long releasePointId;

    private String releasePointName;

    private Long emissionsUnitId;

    private String emissionsUnitName;

    @NotNull(message = "Emission Process ID is required.")
    private Long emissionProcessId;

    private String emissionProcessName;

	@NotBlank(message = "Percent Apportionment is required.")
    @Pattern(regexp = "^\\d{0,3}(\\.\\d{1,2})?$",
        message = "Percent is not in expected numeric format: '{3}.{2}' digits; found '${validatedValue}'.")
    private String percent;

    private Long controlPathId;

    private String pathName;

    public ReleasePointApptBulkUploadDto() {

        super(WorksheetName.ReleasePointAppt);
    }

    @JsonIgnore
    @CsvColumn(name = "Agency Facility ID", order = 1)
	public String getAgencyFacilityIdentifier() {
        return this.agencyFacilityIdentifier;
    }
    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }

    @CsvColumn(name = "Internal ID", order = 2)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @CsvColumn(name = "Internal Release Point ID", order = 3)
    public Long getReleasePointId() {
        return releasePointId;
    }
    public void setReleasePointId(Long releasePointId) {
        this.releasePointId = releasePointId;
    }

    @CsvColumn(name = "Agency Rel Pt ID", order = 4)
    public String getReleasePointName() {
		return releasePointName;
	}
	public void setReleasePointName(String releasePointName) {
		this.releasePointName = releasePointName;
	}

    @CsvColumn(name = "Emissions Unit ID", order = 5)
    public Long getEmissionsUnitId() {
        return emissionsUnitId;
    }
    public void setEmissionsUnitId(Long emissionsUnitId) {
        this.emissionsUnitId = emissionsUnitId;
    }

    @CsvColumn(name = "Emissions Unit Name", order = 6)
    public String getEmissionsUnitName() {
        return emissionsUnitName;
    }
    public void setEmissionsUnitName(String emissionsUnitName) {
        this.emissionsUnitName = emissionsUnitName;
    }

    @CsvColumn(name = "Internal Process ID", order = 7)
    public Long getEmissionProcessId() {
        return emissionProcessId;
    }
    public void setEmissionProcessId(Long emissionProcessId) {
        this.emissionProcessId = emissionProcessId;
    }

    @CsvColumn(name = "Agency Process ID", order = 8)
	public String getEmissionProcessName() {
		return emissionProcessName;
	}
	public void setEmissionProcessName(String emissionProcessName) {
		this.emissionProcessName = emissionProcessName;
	}

    @CsvColumn(name = "Internal Control Path ID", order = 9)
    public Long getControlPathId() {
        return controlPathId;
    }
    public void setControlPathId(Long controlPathId) {
        this.controlPathId = controlPathId;
    }

    @CsvColumn(name = "Agency Control Path ID", order = 10)
	public String getPathName() {
		return pathName;
	}
	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

    @CsvColumn(name = "Percent Rel Pt Apportionment", order = 11)
    public String getPercent() {
        return percent;
    }
    public void setPercent(String percent) {
        this.percent = percent;
    }

	public String getErrorIdentifier() {
        return "releasePointAppts-id: " + id;
    }
}
