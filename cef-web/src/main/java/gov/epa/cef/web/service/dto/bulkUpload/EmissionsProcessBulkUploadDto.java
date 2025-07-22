/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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

@CsvFileName(name = "emissions_processes.csv")
public class EmissionsProcessBulkUploadDto extends BaseWorksheetDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String agencyFacilityIdentifier;

    private String emissionsUnitIdentifier;

    @NotNull(message = "Emissions Process ID is required.")
    private Long id;

    @NotNull(message = "Emissions Unit ID is required.")
    private Long emissionsUnitId;

    private String emissionsUnitName;

    @NotBlank(message = "Emissions Process Identifier is required.")
    @Size(max = 20, message = "Emissions Process Identifier can not exceed {max} chars; found '${validatedValue}'.")
    private String emissionsProcessIdentifier;

    private String displayName;

    @NotBlank(message = "Operating Status Code is required.")
    @Size(max = 20, message = "Operating Status Code can not exceed {max} chars; found '${validatedValue}'.")
    private String operatingStatusCode;

    private String operatingStatusCodeDescription;

    @Pattern(regexp = YearPattern,
        message = "Year Op Status Changed is not in expected format: {4} digits; found '${validatedValue}'.")
    private String statusYear;

    @NotBlank(message = "SCC Code is required.")
    @Size(max = 20, message = "SCC Code can not exceed {max} chars; found '${validatedValue}'.")
    private String sccCode;

    @Size(max = 100, message = "SCC Short Name can not exceed {max} chars; found '${validatedValue}'.")
    private String sccShortName;

    @Size(max = 200, message = "Description can not exceed {max} chars; found '${validatedValue}'.")
    private String description;

    @Size(max = 10, message = "Aircraft Engine Type Code can not exceed {max} chars; found '${validatedValue}'.")
    private String aircraftEngineTypeCode;

    private String aircraftEngineTypeCodeDescription;

    @Size(max = 400, message = "Comments can not exceed {max} chars; found '${validatedValue}'.")
    private String comments;

    @Size(max = 500, message = "SCC Description can not exceed {max} chars; found '${validatedValue}'.")
    private String sccDescription;

    private Boolean sltBillingExempt;

    public EmissionsProcessBulkUploadDto() {

        super(WorksheetName.EmissionsProcess);
    }

    @JsonIgnore
    @CsvColumn(name = "Agency Facility ID", order = 1)
	public String getAgencyFacilityIdentifier() {
        return this.agencyFacilityIdentifier;
    }
    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }

    @CsvColumn(name = "Internal Process ID", order = 4)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @CsvColumn(name = "Internal Unit ID", order = 2)
    public Long getEmissionsUnitId() {
        return emissionsUnitId;
    }
    public void setEmissionsUnitId(Long emissionsUnitId) {
        this.emissionsUnitId = emissionsUnitId;
    }

    @JsonIgnore
    @CsvColumn(name = "Agency Unit ID", order = 2)
    public String getEmissionsUnitIdentifier() {
        return this.emissionsUnitIdentifier;
    }
    public void setEmissionsUnitIdentifier(String emissionsUnitIdentifier) {
        this.emissionsUnitIdentifier = emissionsUnitIdentifier;
    }

    @CsvColumn(name = "Unit Description", order = 2)
    public String getEmissionsUnitName() {
        return emissionsUnitName;
    }
    public void setEmissionsUnitName(String emissionsUnitName) {
        this.emissionsUnitName = emissionsUnitName;
    }

    @CsvColumn(name = "Aircraft Engine Type Code", order = 10)
    public String getAircraftEngineTypeCode() {
        return aircraftEngineTypeCode;
    }
    public void setAircraftEngineTypeCode(String aircraftEngineTypeCode) {
        this.aircraftEngineTypeCode = aircraftEngineTypeCode;
    }

    @CsvColumn(name = "Aircraft Engine Type", order = 10)
    public String getAircraftEngineTypeCodeDescription() {
        return aircraftEngineTypeCodeDescription;
    }
    public void setAircraftEngineTypeCodeDescription(String aircraftEngineTypeCodeDescription) {
        this.aircraftEngineTypeCodeDescription = aircraftEngineTypeCodeDescription;
    }

    @CsvColumn(name = "Process Op Status Code", order = 6)
    public String getOperatingStatusCode() {
        return operatingStatusCode;
    }
    public void setOperatingStatusCode(String operatingStatusCode) {
        this.operatingStatusCode = operatingStatusCode;
    }

    @CsvColumn(name = "Process Op Status", order = 6)
    public String getOperatingStatusCodeDescription() {
        return operatingStatusCodeDescription;
    }
    public void setOperatingStatusCodeDescription(String operatingStatusCodeDescription) {
        this.operatingStatusCodeDescription = operatingStatusCodeDescription;
    }

    @CsvColumn(name = "Agency Process ID", order = 3)
    public String getEmissionsProcessIdentifier() {
        return emissionsProcessIdentifier;
    }
    public void setEmissionsProcessIdentifier(String emissionsProcessIdentifier) {
        this.emissionsProcessIdentifier = emissionsProcessIdentifier;
    }

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @CsvColumn(name = "Process Op Status Year", order = 7)
    public String getStatusYear() {
        return statusYear;
    }
    public void setStatusYear(String statusYear) {
        this.statusYear = statusYear;
    }

    @CsvColumn(name = "SCC", order = 9)
    public String getSccCode() {
        return sccCode;
    }
    public void setSccCode(String sccCode) {
        this.sccCode = sccCode;
    }

    @CsvColumn(name = "SCC Description", order = 8)
    public String getSccDescription() {
        return sccDescription;
    }
    public void setSccDescription(String sccDescription) {
        this.sccDescription = sccDescription;
    }

    public String getSccShortName() {
        return sccShortName;
    }
    public void setSccShortName(String sccShortName) {
        this.sccShortName = sccShortName;
    }

    @CsvColumn(name = "Process Description", order = 5)
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @CsvColumn(name = "Process Comments", order = 11)
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }

    public Boolean getSltBillingExempt() {
        return sltBillingExempt;
    }
    public void setSltBillingExempt(Boolean sltBillingExempt) {
        this.sltBillingExempt = sltBillingExempt;
    }

    public String getErrorIdentifier() {
        return "emissionsProcesses-id: " + id;
    }
}

