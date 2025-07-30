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

@CsvFileName(name = "release_points.csv")
public class ReleasePointBulkUploadDto extends BaseWorksheetDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String agencyFacilityIdentifier;

    @Size(max = 400, message = "Comments can not exceed {max} chars; found '${validatedValue}'.")
    private String comments;

    @Size(max = 200, message = "Description can not exceed {max} chars; found '${validatedValue}'.")
    private String description;

    @Pattern(regexp = "^\\d{0,8}(\\.\\d{1,8})?$",
        message = "Exit Gas Flow Rate is not in expected numeric format: '{8}.{8}' digits; found '${validatedValue}'.")
    private String exitGasFlowRate;

    @Size(max = 20, message = "Exit Gas Flow Unit of Measure Code can not exceed {max} chars; found '${validatedValue}'.")
    private String exitGasFlowUomCode;

    @Pattern(regexp = "^[+-]?\\d{0,4}$",
        message = "Exit Gas Temperature is not in expected format: '+/-{4}' digits; found '${validatedValue}'.")
    private String exitGasTemperature;

    @Pattern(regexp = "^\\d{0,5}(\\.\\d{1,3})?$",
        message = "Exit Gas Velocity is not in expected numeric format: '{5}.{3}' digits; found '${validatedValue}'.")
    private String exitGasVelocity;

    @Size(max = 20, message = "Exit Gas Velocity Unit of Measure Code can not exceed {max} chars; found '${validatedValue}'.")
    private String exitGasVelocityUomCode;

    @NotNull(message = "Release Point ID is required.")
    private Long facilitySiteId;

    @Pattern(regexp = "^\\d{0,6}$",
        message = "Fence Line Distance is not in expected format: {10} digits; found '${validatedValue}'.")
    private String fenceLineDistance;

    @Size(max = 20, message = "Fence Line Unit of Measure Code can not exceed {max} chars; found '${validatedValue}'.")
    private String fenceLineUomCode;

    @Pattern(regexp = "^\\d{0,3}$",
        message = "Fugitive Angle is not in expected numeric format: {3} digits; found '${validatedValue}'.")
    private String fugitiveAngle;

    @Pattern(regexp = "^\\d{0,3}$",
        message = "Fugitive Height is not in expected numeric format: {3} digits; found '${validatedValue}'.")
    private String fugitiveHeight;

    @Size(max = 20, message = "Fugitive Height Unit of Measure Code can not exceed {max} chars; found '${validatedValue}'.")
    private String fugitiveHeightUomCode;

    @Pattern(regexp = "^\\d{0,5}(\\.\\d{1,3})?$",
        message = "Fugitive Length is not in expected numeric format: '{5}.{3}' digits; found '${validatedValue}'.")
    private String fugitiveLength;

    @Size(max = 20, message = "Fugitive Length Unit of Measure Code can not exceed {max} chars; found '${validatedValue}'; found '${validatedValue}'.")
    private String fugitiveLengthUomCode;

    @Pattern(regexp = "^\\d{0,5}(\\.\\d{1,3})?$",
        message = "Fugitive Width is not in expected numeric format: '{5}.{3}' digits; found '${validatedValue}'.")
    private String fugitiveWidth;

    @Size(max = 20, message = "Fugitive Width Unit of Measure Code can not exceed {max} chars; found '${validatedValue}'.")
    private String fugitiveWidthUomCode;

    @Pattern(regexp = LatitudePattern,
        message = "Fugitive Mid Point 2 Latitude is not in expected numeric format:'+/-{2}.{6}' digits; found '${validatedValue}'.")
    private String fugitiveLine2Latitude;

    @Pattern(regexp = LongitudePattern,
        message = "Fugitive Mid Point 2 Longitude is not in expected numeric format: '+/-{3}.{6}' digits; found '${validatedValue}'.")
    private String fugitiveLine2Longitude;

    @NotNull(message = "Release Point ID is required.")
    private Long id;

    @Pattern(regexp = LatitudePattern,
        message = "Latitude is not in expected numeric format: '+/-{2}.{6}' digits; found '${validatedValue}'.")
    private String latitude;

    @Pattern(regexp = LongitudePattern,
        message = "Longitude is not in expected numeric format: '+/-{3}.{6}' digits; found '${validatedValue}'.")
    private String longitude;

    @NotBlank(message = "Operating Status Code is required.")
    @Size(max = 20, message = "Operating Status Code can not exceed {max} chars; found '${validatedValue}'.")
    private String operatingStatusCode;

    private String operatingStatusDescription;

    @NotBlank(message = "Release Point Identifier is required.")
    @Size(max = 20, message = "Release Point Identifier can not exceed {max} chars; found '${validatedValue}'.")
    private String releasePointIdentifier;

    @Pattern(regexp = "^\\d{0,3}(\\.\\d{1,3})?$",
        message = "Stack Diameter is not in expected numeric format: '{3}.{3}' digits; found '${validatedValue}'.")
    private String stackDiameter;

    @Size(max = 20, message = "Stack Diameter Unit of Measure Code can not exceed {max} chars; found '${validatedValue}'.")
    private String stackDiameterUomCode;

    @Pattern(regexp = "^\\d{0,4}(\\.\\d)?$",
        message = "Stack Height is not in expected numeric format: '{4}.{1}' digits; found '${validatedValue}'.")
    private String stackHeight;

    @Size(max = 20, message = "Stack Height Unit of Measure Code can not exceed {max} chars; found '${validatedValue}'.")
    private String stackHeightUomCode;

    @Pattern(regexp = "^\\d{0,3}(\\.\\d{1,3})?$",
        message = "Stack Width is not in expected numeric format: '{3}.{3}' digits; found '${validatedValue}'.")
    private String stackWidth;

    @Size(max = 20, message = "Stack Width Unit of Measure Code can not exceed {max} chars; found '${validatedValue}'.")
    private String stackWidthUomCode;

    @Pattern(regexp = "^\\d{0,3}(\\.\\d{1,3})?$",
        message = "Stack Length is not in expected numeric format: '{3}.{3}' digits; found '${validatedValue}'.")
    private String stackLength;

    @Size(max = 20, message = "Stack Length Unit of Measure Code can not exceed {max} chars; found '${validatedValue}'.")
    private String stackLengthUomCode;

    @Pattern(regexp = YearPattern,
        message = "Year Op Status Changed is not in expected format: {4} digits; found '${validatedValue}'.")
    private String statusYear;

    @NotBlank(message = "Type Code is required.")
    @Size(max = 20, message = "Type Code can not exceed {max} chars; found '${validatedValue}'.")
    private String typeCode;

    private String typeDescription;

    public ReleasePointBulkUploadDto() {

        super(WorksheetName.ReleasePoint);
    }

    @JsonIgnore
    @CsvColumn(name = "Agency Facility ID", order = 1)
	public String getAgencyFacilityIdentifier() {
        return this.agencyFacilityIdentifier;
    }

    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }

    @CsvColumn(name = "Release Point Comments", order = 35)
    public String getComments() {

        return comments;
    }

    public void setComments(String comments) {

        this.comments = comments;
    }

    @CsvColumn(name = "Rel Pt Description", order = 6)
    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    @CsvColumn(name = "Exit Gas Flow Rate", order = 24)
    public String getExitGasFlowRate() {

        return exitGasFlowRate;
    }

    public void setExitGasFlowRate(String exitGasFlowRate) {

        this.exitGasFlowRate = exitGasFlowRate;
    }

    @CsvColumn(name = "Exit Gas Flow Rate UOM", order = 25)
    public String getExitGasFlowUomCode() {

        return exitGasFlowUomCode;
    }

    public void setExitGasFlowUomCode(String exitGasFlowUomCode) {

        this.exitGasFlowUomCode = exitGasFlowUomCode;
    }

    @CsvColumn(name = "Exit Gas Temperature", order = 23)
    public String getExitGasTemperature() {

        return exitGasTemperature;
    }

    public void setExitGasTemperature(String exitGasTemperature) {

        this.exitGasTemperature = exitGasTemperature;
    }

    @CsvColumn(name = "Exit Gas Velocity", order = 21)
    public String getExitGasVelocity() {

        return exitGasVelocity;
    }

    public void setExitGasVelocity(String exitGasVelocity) {

        this.exitGasVelocity = exitGasVelocity;
    }

    @CsvColumn(name = "Exit Gas Velocity UOM", order = 22)
    public String getExitGasVelocityUomCode() {

        return exitGasVelocityUomCode;
    }

    public void setExitGasVelocityUomCode(String exitGasVelicityUomCode) {

        this.exitGasVelocityUomCode = exitGasVelicityUomCode;
    }

    @CsvColumn(name = "Internal Facility Site ID", order = 2)
    public Long getFacilitySiteId() {

        return facilitySiteId;
    }

    public void setFacilitySiteId(Long facilitySiteId) {

        this.facilitySiteId = facilitySiteId;
    }

    @CsvColumn(name = "Fence Line Distance", order = 26)
    public String getFenceLineDistance() {

        return fenceLineDistance;
    }

    public void setFenceLineDistance(String fenceLineDistance) {

        this.fenceLineDistance = fenceLineDistance;
    }

    @CsvColumn(name = "Fence Line Distance UOM", order = 27)
    public String getFenceLineUomCode() {

        return fenceLineUomCode;
    }

    public void setFenceLineUomCode(String fenceLineUomCode) {

        this.fenceLineUomCode = fenceLineUomCode;
    }

    @CsvColumn(name = "Fugitive Angle", order = 34)
    public String getFugitiveAngle() {

        return fugitiveAngle;
    }

    public void setFugitiveAngle(String fugitiveAngle) {

        this.fugitiveAngle = fugitiveAngle;
    }

    @CsvColumn(name = "Fugitive Height", order = 28)
    public String getFugitiveHeight() {

        return fugitiveHeight;
    }

    public void setFugitiveHeight(String fugitiveHeight) {

        this.fugitiveHeight = fugitiveHeight;
    }

    @CsvColumn(name = "Fugitive Height UOM", order = 29)
    public String getFugitiveHeightUomCode() {

        return fugitiveHeightUomCode;
    }

    public void setFugitiveHeightUomCode(String fugitiveHeightUomCode) {

        this.fugitiveHeightUomCode = fugitiveHeightUomCode;
    }

    @CsvColumn(name = "Fugitive Length", order = 32)
    public String getFugitiveLength() {

        return fugitiveLength;
    }

    public void setFugitiveLength(String fugitiveLength) {

        this.fugitiveLength = fugitiveLength;
    }

    @CsvColumn(name = "Fugitive Length UOM", order = 33)
    public String getFugitiveLengthUomCode() {

        return fugitiveLengthUomCode;
    }

    public void setFugitiveLengthUomCode(String fugitiveLengthUomCode) {

        this.fugitiveLengthUomCode = fugitiveLengthUomCode;
    }

    @CsvColumn(name = "Fugitive Midpoint 2 Latitude", order = 11)
    public String getFugitiveLine2Latitude() {
		return fugitiveLine2Latitude;
	}

	public void setFugitiveLine2Latitude(String fugitiveLine2Latitude) {
		this.fugitiveLine2Latitude = fugitiveLine2Latitude;
	}

    @CsvColumn(name = "Fugitive Midpoint 2 Longitude", order = 12)
	public String getFugitiveLine2Longitude() {
		return fugitiveLine2Longitude;
	}

	public void setFugitiveLine2Longitude(String fugitiveLine2Longitude) {
		this.fugitiveLine2Longitude = fugitiveLine2Longitude;
	}

    @CsvColumn(name = "Fugitive Width", order = 30)
    public String getFugitiveWidth() {

        return fugitiveWidth;
    }

    public void setFugitiveWidth(String fugitiveWidth) {

        this.fugitiveWidth = fugitiveWidth;
    }

    @CsvColumn(name = "Fugitive Width UOM", order = 31)
    public String getFugitiveWidthUomCode() {

        return fugitiveWidthUomCode;
    }

    public void setFugitiveWidthUomCode(String fugitiveWidthUomCode) {

        this.fugitiveWidthUomCode = fugitiveWidthUomCode;
    }

    @CsvColumn(name = "Internal Release Point ID", order = 4)
    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    @CsvColumn(name = "Rel Pt Lat", order = 9)
    public String getLatitude() {

        return latitude;
    }

    public void setLatitude(String latitude) {

        this.latitude = latitude;
    }

    @CsvColumn(name = "Rel Pt Long", order = 10)
    public String getLongitude() {

        return longitude;
    }

    public void setLongitude(String longitude) {

        this.longitude = longitude;
    }

    @CsvColumn(name = "Rel Pt Op Status Code", order = 7)
    public String getOperatingStatusCode() {

        return operatingStatusCode;
    }

    public void setOperatingStatusCode(String operatingStatusCode) {

        this.operatingStatusCode = operatingStatusCode;
    }

    @CsvColumn(name = "Rel Pt Op Status", order = 7)
    public String getOperatingStatusDescription() {
        return operatingStatusDescription;
    }
    public void setOperatingStatusDescription(String operatingStatusDescription) {
        this.operatingStatusDescription = operatingStatusDescription;
    }

    @CsvColumn(name = "Agency Rel Pt ID", order = 3)
    public String getReleasePointIdentifier() {

        return releasePointIdentifier;
    }

    public void setReleasePointIdentifier(String releasePointIdentifier) {

        this.releasePointIdentifier = releasePointIdentifier;
    }

    @CsvColumn(name = "Stack Diameter", order = 15)
    public String getStackDiameter() {

        return stackDiameter;
    }

    public void setStackDiameter(String stackDiameter) {

        this.stackDiameter = stackDiameter;
    }

    @CsvColumn(name = "Stack Diameter UOM", order = 16)
    public String getStackDiameterUomCode() {

        return stackDiameterUomCode;
    }

    public void setStackDiameterUomCode(String stackDiameterUomCode) {

        this.stackDiameterUomCode = stackDiameterUomCode;
    }

    @CsvColumn(name = "Stack Height", order = 13)
    public String getStackHeight() {

        return stackHeight;
    }

    public void setStackHeight(String stackHeight) {

        this.stackHeight = stackHeight;
    }

    @CsvColumn(name = "Stack Height UOM", order = 14)
    public String getStackHeightUomCode() {

        return stackHeightUomCode;
    }

    public void setStackHeightUomCode(String stackHeightUomCode) {

        this.stackHeightUomCode = stackHeightUomCode;
    }

    @CsvColumn(name = "Stack Width", order = 17)
    public String getStackWidth() {
        return stackWidth;
    }

    public void setStackWidth(String stackWidth) {
        this.stackWidth = stackWidth;
    }

    @CsvColumn(name = "Stack Width UOM", order = 18)
    public String getStackWidthUomCode() {
        return stackWidthUomCode;
    }

    public void setStackWidthUomCode(String stackWidthUomCode) {
        this.stackWidthUomCode = stackWidthUomCode;
    }

    @CsvColumn(name = "Stack Length", order = 19)
    public String getStackLength() {
        return stackLength;
    }

    public void setStackLength(String stackLength) {
        this.stackLength = stackLength;
    }

    @CsvColumn(name = "Stack Length UOM", order = 20)
    public String getStackLengthUomCode() {
        return stackLengthUomCode;
    }

    public void setStackLengthUomCode(String stackLengthUomCode) {
        this.stackLengthUomCode = stackLengthUomCode;
    }

    @CsvColumn(name = "Rel Pt Op Status Year", order = 8)
    public String getStatusYear() {

        return statusYear;
    }

    public void setStatusYear(String statusYear) {

        this.statusYear = statusYear;
    }

    @CsvColumn(name = "Rel Pt Type Code", order = 5)
    public String getTypeCode() {

        return typeCode;
    }

    public void setTypeCode(String typeCode) {

        this.typeCode = typeCode;
    }

    @CsvColumn(name = "Rel Pt Type", order = 5)
    public String getTypeDescription() {

        return typeDescription;
    }
    public void setTypeDescription(String typeDescription) {

        this.typeDescription = typeDescription;
    }

    public String getErrorIdentifier() {
        return "releasePoints-id: " + id;
    }

}
