package gov.epa.cef.web.service.dto.json.shared;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;

@Schema(name = "GeographicCoordinates")
public class GeographicCoordinatesJsonDto implements Serializable {

    @NotBlank
    @Digits(integer = 2, fraction = 6)
    protected String latitudeMeasure;

    @NotBlank
    @Digits(integer = 3, fraction = 6)
    protected String longitudeMeasure;


    @Digits(integer = 2, fraction = 6)
    protected String midPoint2LatitudeMeasure;

    @Digits(integer = 3, fraction = 6)
    protected String midPoint2LongitudeMeasure;

    protected String sourceMapScaleNumber;
    protected String horizontalAccuracyMeasure;
    protected String horizontalAccuracyUnitofMeasure;
    protected String horizontalCollectionMethodCode;
    protected String horizontalReferenceDatumCode;
    protected String geographicReferencePointCode;
    protected LocalDate dataCollectionDate;
    protected String geographicComment;
    protected String verticalMeasure;
    protected String verticalUnitofMeasureCode;
    protected String verticalCollectionMethodCode;
    protected String verticalReferenceDatumCode;
    protected String verificationMethodCode;
    protected String coordinateDataSourceCode;
    protected String geometricTypeCode;
    protected String areaWithinPerimeter;
    protected String areaWithinPerimeterUnitofMeasureCode;
    protected String geographicCoordinateIsReadOnly;

    public String getLatitudeMeasure() {
        return latitudeMeasure;
    }

    public void setLatitudeMeasure(String latitudeMeasure) {
        this.latitudeMeasure = latitudeMeasure;
    }

    public String getLongitudeMeasure() {
        return longitudeMeasure;
    }

    public void setLongitudeMeasure(String longitudeMeasure) {
        this.longitudeMeasure = longitudeMeasure;
    }

    public String getMidPoint2LatitudeMeasure() {
        return midPoint2LatitudeMeasure;
    }

    public void setMidPoint2LatitudeMeasure(String midPoint2LatitudeMeasure) {
        this.midPoint2LatitudeMeasure = midPoint2LatitudeMeasure;
    }

    public String getMidPoint2LongitudeMeasure() {
        return midPoint2LongitudeMeasure;
    }

    public void setMidPoint2LongitudeMeasure(String midPoint2LongitudeMeasure) {
        this.midPoint2LongitudeMeasure = midPoint2LongitudeMeasure;
    }

    public String getSourceMapScaleNumber() {
        return sourceMapScaleNumber;
    }

    public void setSourceMapScaleNumber(String sourceMapScaleNumber) {
        this.sourceMapScaleNumber = sourceMapScaleNumber;
    }

    public String getHorizontalAccuracyMeasure() {
        return horizontalAccuracyMeasure;
    }

    public void setHorizontalAccuracyMeasure(String horizontalAccuracyMeasure) {
        this.horizontalAccuracyMeasure = horizontalAccuracyMeasure;
    }

    public String getHorizontalAccuracyUnitofMeasure() {
        return horizontalAccuracyUnitofMeasure;
    }

    public void setHorizontalAccuracyUnitofMeasure(String horizontalAccuracyUnitofMeasure) {
        this.horizontalAccuracyUnitofMeasure = horizontalAccuracyUnitofMeasure;
    }

    public String getHorizontalCollectionMethodCode() {
        return horizontalCollectionMethodCode;
    }

    public void setHorizontalCollectionMethodCode(String horizontalCollectionMethodCode) {
        this.horizontalCollectionMethodCode = horizontalCollectionMethodCode;
    }

    public String getHorizontalReferenceDatumCode() {
        return horizontalReferenceDatumCode;
    }

    public void setHorizontalReferenceDatumCode(String horizontalReferenceDatumCode) {
        this.horizontalReferenceDatumCode = horizontalReferenceDatumCode;
    }

    public String getGeographicReferencePointCode() {
        return geographicReferencePointCode;
    }

    public void setGeographicReferencePointCode(String geographicReferencePointCode) {
        this.geographicReferencePointCode = geographicReferencePointCode;
    }

    public LocalDate getDataCollectionDate() {
        return dataCollectionDate;
    }

    public void setDataCollectionDate(LocalDate dataCollectionDate) {
        this.dataCollectionDate = dataCollectionDate;
    }

    public String getGeographicComment() {
        return geographicComment;
    }

    public void setGeographicComment(String geographicComment) {
        this.geographicComment = geographicComment;
    }

    public String getVerticalMeasure() {
        return verticalMeasure;
    }

    public void setVerticalMeasure(String verticalMeasure) {
        this.verticalMeasure = verticalMeasure;
    }

    public String getVerticalUnitofMeasureCode() {
        return verticalUnitofMeasureCode;
    }

    public void setVerticalUnitofMeasureCode(String verticalUnitofMeasureCode) {
        this.verticalUnitofMeasureCode = verticalUnitofMeasureCode;
    }

    public String getVerticalCollectionMethodCode() {
        return verticalCollectionMethodCode;
    }

    public void setVerticalCollectionMethodCode(String verticalCollectionMethodCode) {
        this.verticalCollectionMethodCode = verticalCollectionMethodCode;
    }

    public String getVerticalReferenceDatumCode() {
        return verticalReferenceDatumCode;
    }

    public void setVerticalReferenceDatumCode(String verticalReferenceDatumCode) {
        this.verticalReferenceDatumCode = verticalReferenceDatumCode;
    }

    public String getVerificationMethodCode() {
        return verificationMethodCode;
    }

    public void setVerificationMethodCode(String verificationMethodCode) {
        this.verificationMethodCode = verificationMethodCode;
    }

    public String getCoordinateDataSourceCode() {
        return coordinateDataSourceCode;
    }

    public void setCoordinateDataSourceCode(String coordinateDataSourceCode) {
        this.coordinateDataSourceCode = coordinateDataSourceCode;
    }

    public String getGeometricTypeCode() {
        return geometricTypeCode;
    }

    public void setGeometricTypeCode(String geometricTypeCode) {
        this.geometricTypeCode = geometricTypeCode;
    }

    public String getAreaWithinPerimeter() {
        return areaWithinPerimeter;
    }

    public void setAreaWithinPerimeter(String areaWithinPerimeter) {
        this.areaWithinPerimeter = areaWithinPerimeter;
    }

    public String getAreaWithinPerimeterUnitofMeasureCode() {
        return areaWithinPerimeterUnitofMeasureCode;
    }

    public void setAreaWithinPerimeterUnitofMeasureCode(String areaWithinPerimeterUnitofMeasureCode) {
        this.areaWithinPerimeterUnitofMeasureCode = areaWithinPerimeterUnitofMeasureCode;
    }

    public String getGeographicCoordinateIsReadOnly() {
        return geographicCoordinateIsReadOnly;
    }

    public void setGeographicCoordinateIsReadOnly(String geographicCoordinateIsReadOnly) {
        this.geographicCoordinateIsReadOnly = geographicCoordinateIsReadOnly;
    }
}
