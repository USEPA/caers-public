/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.domain;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import gov.epa.cef.web.domain.common.BaseAuditEntity;
import gov.epa.cef.web.domain.common.IReportComponent;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.util.ReportCreationContext;

/**
 * ReleasePoint entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "release_point")

public class ReleasePoint extends BaseAuditEntity implements IReportComponent {

    private static final long serialVersionUID = 1L;

    // Fields

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_code", nullable = false)
    private OperatingStatusCode operatingStatusCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_site_id", nullable = false)
    private FacilitySite facilitySite;

    @Column(name = "release_point_identifier", nullable = false, length = 20)
    private String releasePointIdentifier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_code", nullable = false)
    private ReleasePointTypeCode typeCode;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "fence_line_distance", precision = 6, scale = 0)
    private Long fenceLineDistance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fence_line_distance_uom_code", nullable = false)
    private UnitMeasureCode fenceLineUomCode;

    @Column(name = "stack_height", precision = 8, scale = 3)
    private BigDecimal stackHeight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stack_height_uom_code", nullable = false)
    private UnitMeasureCode stackHeightUomCode;

    @Column(name = "stack_diameter", precision = 6, scale = 3)
    private BigDecimal stackDiameter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stack_diameter_uom_code", nullable = false)
    private UnitMeasureCode stackDiameterUomCode;

    @Column(name = "stack_width", precision = 6, scale = 3)
    private BigDecimal stackWidth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stack_width_uom_code", nullable = false)
    private UnitMeasureCode stackWidthUomCode;

    @Column(name = "stack_length", precision = 6, scale = 3)
    private BigDecimal stackLength;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stack_length_uom_code", nullable = false)
    private UnitMeasureCode stackLengthUomCode;

    @Column(name = "exit_gas_velocity", precision = 8, scale = 3)
    private BigDecimal exitGasVelocity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exit_gas_velocity_uom_code", nullable = false)
    private UnitMeasureCode exitGasVelocityUomCode;

    @Column(name = "exit_gas_temperature", precision = 4, scale = 0)
    private Short exitGasTemperature;

    @Column(name = "exit_gas_flow_rate", precision = 16, scale = 8)
    private BigDecimal exitGasFlowRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exit_gas_flow_uom_code", nullable = false)
    private UnitMeasureCode exitGasFlowUomCode;

    @Column(name = "status_year")
    private Short statusYear;

    @Column(name = "fugitive_mid_pt2_latitude", precision = 10, scale = 6)
    private BigDecimal fugitiveMidPt2Latitude;

    @Column(name = "fugitive_mid_pt2_longitude", precision = 10, scale = 6)
    private BigDecimal fugitiveMidPt2Longitude;

    @Column(name = "latitude", precision = 10, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 6)
    private BigDecimal longitude;

    @Column(name = "fugitive_height", precision = 3, scale = 0)
    private Long fugitiveHeight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fugitive_height_uom_code", nullable = false)
    private UnitMeasureCode fugitiveHeightUomCode;

    @Column(name = "fugitive_width", precision = 8, scale = 3)
    private BigDecimal fugitiveWidth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fugitive_width_uom_code", nullable = false)
    private UnitMeasureCode fugitiveWidthUomCode;

    @Column(name = "fugitive_length", precision = 8, scale = 3)
    private BigDecimal fugitiveLength;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fugitive_length_uom_code", nullable = false)
    private UnitMeasureCode fugitiveLengthUomCode;

    @Column(name = "fugitive_angle", precision = 3, scale = 0)
    private Long fugitiveAngle;

    @Column(name = "comments", length = 400)
    private String comments;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_year_status_code", nullable = true)
    private OperatingStatusCode previousYearOperatingStatusCode;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "releasePoint")
    private List<ReleasePointAppt> releasePointAppts = new ArrayList<ReleasePointAppt>(0);


    /***
     * Default constructor
     */
    public ReleasePoint() {
    }


    /***
     * Copy constructor (release point apportionment is not copied here, they are copied within the EmissionsProcess entity)
     * @param facilitySite The facility site object that this release point should be associated with
     * @param originalReleasePoint The release point object being copied
     */
    public ReleasePoint(FacilitySite facilitySite, ReleasePoint originalReleasePoint) {
        this(facilitySite, originalReleasePoint, null);
    }

    /***
     * Copy constructor (release point apportionment is not copied here, they are copied within the EmissionsProcess entity)
     * @param facilitySite The facility site object that this release point should be associated with
     * @param originalReleasePoint The release point object being copied
     * @param context context for tracking report creation changes
     */
    public ReleasePoint(FacilitySite facilitySite, ReleasePoint originalReleasePoint, ReportCreationContext context) {
        this.id = originalReleasePoint.getId();
        this.operatingStatusCode = originalReleasePoint.getOperatingStatusCode();
        this.facilitySite = facilitySite;
        this.releasePointIdentifier = originalReleasePoint.getReleasePointIdentifier();
        this.typeCode = originalReleasePoint.getTypeCode();
        this.description = originalReleasePoint.getDescription();
        this.stackHeight = originalReleasePoint.getStackHeight();
        this.stackHeightUomCode = originalReleasePoint.getStackHeightUomCode();
        this.stackDiameter = originalReleasePoint.getStackDiameter();
        this.stackDiameterUomCode = originalReleasePoint.getStackDiameterUomCode();
        this.stackWidth = originalReleasePoint.getStackWidth();
        this.stackWidthUomCode = originalReleasePoint.getStackWidthUomCode();
        this.stackLength = originalReleasePoint.getStackLength();
        this.stackLengthUomCode = originalReleasePoint.getStackLengthUomCode();
        this.exitGasVelocity = originalReleasePoint.getExitGasVelocity();
        this.exitGasVelocityUomCode = originalReleasePoint.getExitGasVelocityUomCode();
        this.exitGasTemperature = originalReleasePoint.getExitGasTemperature();
        this.exitGasFlowRate = originalReleasePoint.getExitGasFlowRate();
        this.exitGasFlowUomCode = originalReleasePoint.getExitGasFlowUomCode();
        this.statusYear = originalReleasePoint.getStatusYear();
        this.fugitiveMidPt2Latitude = originalReleasePoint.getFugitiveMidPt2Latitude();
        this.fugitiveMidPt2Longitude = originalReleasePoint.getFugitiveMidPt2Longitude();
        this.latitude = originalReleasePoint.getLatitude();
        this.longitude = originalReleasePoint.getLongitude();
        this.comments = originalReleasePoint.getComments();
        this.fugitiveHeight = originalReleasePoint.getFugitiveHeight();
        this.fugitiveHeightUomCode = originalReleasePoint.getFugitiveHeightUomCode();
        this.fugitiveLength = originalReleasePoint.getFugitiveLength();
        this.fugitiveLengthUomCode = originalReleasePoint.getFugitiveLengthUomCode();
        this.fugitiveWidth = originalReleasePoint.getFugitiveWidth();
        this.fugitiveWidthUomCode = originalReleasePoint.getFugitiveWidthUomCode();
        this.fugitiveAngle = originalReleasePoint.getFugitiveAngle();
        this.fenceLineDistance = originalReleasePoint.getFenceLineDistance();
        this.fenceLineUomCode = originalReleasePoint.getFenceLineUomCode();
    }

    // Property accessors
    public OperatingStatusCode getOperatingStatusCode() {
        return this.operatingStatusCode;
    }

    public void setOperatingStatusCode(OperatingStatusCode operatingStatusCode) {
        this.operatingStatusCode = operatingStatusCode;
    }

    public FacilitySite getFacilitySite() {
        return this.facilitySite;
    }

    public void setFacilitySite(FacilitySite facilitySite) {
        this.facilitySite = facilitySite;
    }

    public String getReleasePointIdentifier() {
        return this.releasePointIdentifier;
    }

    public void setReleasePointIdentifier(String releasePointIdentifier) {
        this.releasePointIdentifier = releasePointIdentifier;
    }

    public ReleasePointTypeCode getTypeCode() {
        return this.typeCode;
    }

    public void setTypeCode(ReleasePointTypeCode typeCode) {
        this.typeCode = typeCode;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getStackHeight() {
        return this.stackHeight;
    }

    public void setStackHeight(BigDecimal stackHeight) {
        this.stackHeight = stackHeight;
    }

    public UnitMeasureCode getStackHeightUomCode() {
        return this.stackHeightUomCode;
    }

    public void setStackHeightUomCode(UnitMeasureCode stackHeightUomCode) {
        this.stackHeightUomCode = stackHeightUomCode;
    }

    public BigDecimal getStackDiameter() {
        return this.stackDiameter;
    }

    public void setStackDiameter(BigDecimal stackDiameter) {
        this.stackDiameter = stackDiameter;
    }

    public UnitMeasureCode getStackDiameterUomCode() {
        return this.stackDiameterUomCode;
    }

    public void setStackDiameterUomCode(UnitMeasureCode stackDiameterUomCode) {
        this.stackDiameterUomCode = stackDiameterUomCode;
    }

    public BigDecimal getStackWidth() {
        return stackWidth;
    }

    public void setStackWidth(BigDecimal stackWidth) {
        this.stackWidth = stackWidth;
    }

    public UnitMeasureCode getStackWidthUomCode() {
        return stackWidthUomCode;
    }

    public void setStackWidthUomCode(UnitMeasureCode stackWidthUomCode) {
        this.stackWidthUomCode = stackWidthUomCode;
    }

    public BigDecimal getStackLength() {
        return stackLength;
    }

    public void setStackLength(BigDecimal stackLength) {
        this.stackLength = stackLength;
    }

    public UnitMeasureCode getStackLengthUomCode() {
        return stackLengthUomCode;
    }

    public void setStackLengthUomCode(UnitMeasureCode stackLengthUomCode) {
        this.stackLengthUomCode = stackLengthUomCode;
    }

    public BigDecimal getExitGasVelocity() {
        return this.exitGasVelocity;
    }

    public void setExitGasVelocity(BigDecimal exitGasVelocity) {
        this.exitGasVelocity = exitGasVelocity;
    }

    public UnitMeasureCode getExitGasVelocityUomCode() {
        return this.exitGasVelocityUomCode;
    }

    public void setExitGasVelocityUomCode(UnitMeasureCode exitGasVelicityUomCode) {
        this.exitGasVelocityUomCode = exitGasVelicityUomCode;
    }

    public Short getExitGasTemperature() {
        return this.exitGasTemperature;
    }

    public void setExitGasTemperature(Short exitGasTemperature) {
        this.exitGasTemperature = exitGasTemperature;
    }

    public BigDecimal getExitGasFlowRate() {
        return this.exitGasFlowRate;
    }

    public void setExitGasFlowRate(BigDecimal exitGasFlowRate) {
        this.exitGasFlowRate = exitGasFlowRate;
    }

    public UnitMeasureCode getExitGasFlowUomCode() {
        return this.exitGasFlowUomCode;
    }

    public void setExitGasFlowUomCode(UnitMeasureCode exitGasFlowUomCode) {
        this.exitGasFlowUomCode = exitGasFlowUomCode;
    }

    public Short getStatusYear() {
        return this.statusYear;
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
        return this.latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return this.longitude;
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

    public UnitMeasureCode getFenceLineUomCode() {
        return fenceLineUomCode;
    }

    public void setFenceLineUomCode(UnitMeasureCode fenceLineUomCode) {
        this.fenceLineUomCode = fenceLineUomCode;
    }

    public Long getFugitiveHeight() {
        return fugitiveHeight;
    }

    public void setFugitiveHeight(Long fugitiveHeight) {
        this.fugitiveHeight = fugitiveHeight;
    }

    public UnitMeasureCode getFugitiveHeightUomCode() {
        return fugitiveHeightUomCode;
    }

    public void setFugitiveHeightUomCode(UnitMeasureCode fugitiveHeightUomCode) {
        this.fugitiveHeightUomCode = fugitiveHeightUomCode;
    }

    public BigDecimal getFugitiveWidth() {
        return fugitiveWidth;
    }

    public void setFugitiveWidth(BigDecimal fugitiveWidth) {
        this.fugitiveWidth = fugitiveWidth;
    }

    public UnitMeasureCode getFugitiveWidthUomCode() {
        return fugitiveWidthUomCode;
    }

    public void setFugitiveWidthUomCode(UnitMeasureCode fugitiveWidthUomCode) {
        this.fugitiveWidthUomCode = fugitiveWidthUomCode;
    }

    public BigDecimal getFugitiveLength() {
        return fugitiveLength;
    }

    public void setFugitiveLength(BigDecimal fugitiveLength) {
        this.fugitiveLength = fugitiveLength;
    }

    public UnitMeasureCode getFugitiveLengthUomCode() {
        return fugitiveLengthUomCode;
    }

    public void setFugitiveLengthUomCode(UnitMeasureCode fugitiveLengthUomCode) {
        this.fugitiveLengthUomCode = fugitiveLengthUomCode;
    }

    public Long getFugitiveAngle() {
        return fugitiveAngle;
    }

    public void setFugitiveAngle(Long fugitiveAngle) {
        this.fugitiveAngle = fugitiveAngle;
    }

    public OperatingStatusCode getPreviousYearOperatingStatusCode() {
		return previousYearOperatingStatusCode;
	}

	public void setPreviousYearOperatingStatusCode(OperatingStatusCode previousYearOperatingStatusCode) {
		this.previousYearOperatingStatusCode = previousYearOperatingStatusCode;
	}

    public List<ReleasePointAppt> getReleasePointAppts() {
        return this.releasePointAppts;
    }

    public void setReleasePointAppts(List<ReleasePointAppt> releasePointAppts) {
        this.releasePointAppts = releasePointAppts;
    }


    /***
     * Set the id property to null for this object and the id for it's direct children.  This method is useful to INSERT the updated object instead of UPDATE.
     */
    public void clearId() {
        this.id = null;
    }


    @Override
    public ValidationDetailDto getComponentDetails() {

        String description = MessageFormat.format("Release Point: {0}", getReleasePointIdentifier());

        ValidationDetailDto dto = new ValidationDetailDto(getId(), getReleasePointIdentifier(), EntityType.RELEASE_POINT, description);
        return dto;
    }

}
