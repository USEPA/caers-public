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

public class ReleasePointDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long facilitySiteId;
    private CodeLookupDto operatingStatusCode;
    private String releasePointIdentifier;
    private CodeLookupDto typeCode;
    private String description;
    private BigDecimal stackHeight;
    private CodeLookupDto stackHeightUomCode;
    private BigDecimal stackDiameter;
    private CodeLookupDto stackDiameterUomCode;
    private BigDecimal stackWidth;
    private CodeLookupDto stackWidthUomCode;
    private BigDecimal stackLength;
    private CodeLookupDto stackLengthUomCode;
    private BigDecimal exitGasVelocity;
    private CodeLookupDto exitGasVelocityUomCode;
    private Short exitGasTemperature;
    private BigDecimal exitGasFlowRate;
    private CodeLookupDto exitGasFlowUomCode;
    private Short statusYear;
    private BigDecimal fugitiveMidPt2Latitude;
    private BigDecimal fugitiveMidPt2Longitude;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String comments;
    private Long fenceLineDistance;
    private CodeLookupDto fenceLineUomCode;
    private Long fugitiveHeight;
    private CodeLookupDto fugitiveHeightUomCode;
    private BigDecimal fugitiveWidth;
    private CodeLookupDto fugitiveWidthUomCode;
    private BigDecimal fugitiveLength;
    private CodeLookupDto fugitiveLengthUomCode;
    private Long fugitiveAngle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFacilitySiteId() {
        return facilitySiteId;
    }

    public void setFacilitySiteId(Long facilitySiteId) {
        this.facilitySiteId = facilitySiteId;
    }

    public CodeLookupDto getOperatingStatusCode() {
        return operatingStatusCode;
    }

    public void setOperatingStatusCode(CodeLookupDto operatingStatusCode) {
        this.operatingStatusCode = operatingStatusCode;
    }

    public String getReleasePointIdentifier() {
        return releasePointIdentifier;
    }

    public void setReleasePointIdentifier(String releasePointIdentifier) {
        this.releasePointIdentifier = releasePointIdentifier;
    }

    public CodeLookupDto getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(CodeLookupDto typeCode) {
        this.typeCode = typeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getStackHeight() {
        return stackHeight;
    }

    public void setStackHeight(BigDecimal stackHeight) {
        this.stackHeight = stackHeight;
    }

    public CodeLookupDto getStackHeightUomCode() {
        return stackHeightUomCode;
    }

    public void setStackHeightUomCode(CodeLookupDto stackHeightUomCode) {
        this.stackHeightUomCode = stackHeightUomCode;
    }

    public BigDecimal getStackDiameter() {
        return stackDiameter;
    }

    public void setStackDiameter(BigDecimal stackDiameter) {
        this.stackDiameter = stackDiameter;
    }

    public CodeLookupDto getStackDiameterUomCode() {
        return stackDiameterUomCode;
    }

    public void setStackDiameterUomCode(CodeLookupDto stackDiameterUomCode) {
        this.stackDiameterUomCode = stackDiameterUomCode;
    }

    public BigDecimal getStackWidth() { return stackWidth; }

    public void setStackWidth(BigDecimal stackWidth) { this.stackWidth = stackWidth; }

    public CodeLookupDto getStackWidthUomCode() { return stackWidthUomCode; }

    public void setStackWidthUomCode(CodeLookupDto stackWidthUomCode) { this.stackWidthUomCode = stackWidthUomCode; }

    public BigDecimal getStackLength() { return stackLength; }

    public void setStackLength(BigDecimal stackLength) { this.stackLength = stackLength; }

    public CodeLookupDto getStackLengthUomCode() { return stackLengthUomCode; }

    public void setStackLengthUomCode(CodeLookupDto stackLengthUomCode) { this.stackLengthUomCode = stackLengthUomCode; }

    public BigDecimal getExitGasVelocity() {
        return exitGasVelocity;
    }

    public void setExitGasVelocity(BigDecimal exitGasVelocity) {
        this.exitGasVelocity = exitGasVelocity;
    }

    public CodeLookupDto getExitGasVelocityUomCode() {
        return exitGasVelocityUomCode;
    }

    public void setExitGasVelocityUomCode(CodeLookupDto exitGasVelicityUomCode) {
        this.exitGasVelocityUomCode = exitGasVelicityUomCode;
    }

    public Short getExitGasTemperature() {
        return exitGasTemperature;
    }

    public void setExitGasTemperature(Short exitGasTemperature) {
        this.exitGasTemperature = exitGasTemperature;
    }

    public BigDecimal getExitGasFlowRate() {
        return exitGasFlowRate;
    }

    public void setExitGasFlowRate(BigDecimal exitGasFlowRate) {
        this.exitGasFlowRate = exitGasFlowRate;
    }

    public CodeLookupDto getExitGasFlowUomCode() {
        return exitGasFlowUomCode;
    }

    public void setExitGasFlowUomCode(CodeLookupDto exitGasFlowUomCode) {
        this.exitGasFlowUomCode = exitGasFlowUomCode;
    }

    public Short getStatusYear() {
        return statusYear;
    }

    public void setStatusYear(Short statusYear) {
        this.statusYear = statusYear;
    }

    public BigDecimal getFugitiveMidPt2Latitude() {
		return fugitiveMidPt2Latitude;
	}

	public void setFugitiveMidPt2Latitude(BigDecimal fugitiveMidPt2Latitude) {
		this.fugitiveMidPt2Latitude = fugitiveMidPt2Latitude;
	}

	public BigDecimal getFugitiveMidPt2Longitude() {
		return fugitiveMidPt2Longitude;
	}

	public void setFugitiveMidPt2Longitude(BigDecimal fugitiveMidPt2Longitude) {
		this.fugitiveMidPt2Longitude = fugitiveMidPt2Longitude;
	}

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getFenceLineDistance() {
        return fenceLineDistance;
    }

    public void setFenceLineDistance(Long fenceLineDistance) {
        this.fenceLineDistance = fenceLineDistance;
    }

    public CodeLookupDto getFenceLineUomCode() {
        return fenceLineUomCode;
    }

    public void setFenceLineUomCode(CodeLookupDto fenceLineUomCode) {
        this.fenceLineUomCode = fenceLineUomCode;
    }

    public Long getFugitiveHeight() {
        return fugitiveHeight;
    }

    public void setFugitiveHeight(Long fugitiveHeight) {
        this.fugitiveHeight = fugitiveHeight;
    }

    public CodeLookupDto getFugitiveHeightUomCode() {
        return fugitiveHeightUomCode;
    }

    public void setFugitiveHeightUomCode(CodeLookupDto fugitiveHeightUomCode) {
        this.fugitiveHeightUomCode = fugitiveHeightUomCode;
    }

    public BigDecimal getFugitiveWidth() {
        return fugitiveWidth;
    }

    public void setFugitiveWidth(BigDecimal fugitiveWidth) {
        this.fugitiveWidth = fugitiveWidth;
    }

    public CodeLookupDto getFugitiveWidthUomCode() {
        return fugitiveWidthUomCode;
    }

    public void setFugitiveWidthUomCode(CodeLookupDto fugitiveWidthUomCode) {
        this.fugitiveWidthUomCode = fugitiveWidthUomCode;
    }

    public BigDecimal getFugitiveLength() {
        return fugitiveLength;
    }

    public void setFugitiveLength(BigDecimal fugitiveLength) {
        this.fugitiveLength = fugitiveLength;
    }

    public CodeLookupDto getFugitiveLengthUomCode() {
        return fugitiveLengthUomCode;
    }

    public void setFugitiveLengthUomCode(CodeLookupDto fugitiveLengthUomCode) {
        this.fugitiveLengthUomCode = fugitiveLengthUomCode;
    }

    public Long getFugitiveAngle() {
        return fugitiveAngle;
    }

    public void setFugitiveAngle(Long fugitiveAngle) {
        this.fugitiveAngle = fugitiveAngle;
    }

    public ReleasePointDto withId(Long id) {
        setId(id);
        return this;
    }

}
