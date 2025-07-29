/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.mapper.cers._2._0;

import gov.epa.cef.web.domain.ReleasePoint;
import net.exchangenetwork.schema.cer._2._0.GeographicCoordinatesDataType;
import net.exchangenetwork.schema.cer._2._0.IdentificationDataType;
import net.exchangenetwork.schema.cer._2._0.ReleasePointDataType;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {})
public interface CersV2ReleasePointMapper {

	@AfterMapping
    default void afterMapping(@MappingTarget ReleasePointDataType target, ReleasePoint source) {
        if (source.getLatitude() == null || source.getLongitude() == null) {
            target.setReleasePointGeographicCoordinates(null);
        }

        if (target.getReleasePointWidthMeasure() == null && source.getFugitiveWidth() != null) {
            target.setReleasePointWidthMeasure(source.getFugitiveWidth().toString());
        }
        if (target.getReleasePointWidthUnitofMeasureCode() == null && source.getFugitiveWidthUomCode() != null) {
            target.setReleasePointWidthUnitofMeasureCode(source.getFugitiveWidthUomCode().getCode());
        }
        if (target.getReleasePointLengthMeasure() == null && source.getFugitiveLength() != null) {
            target.setReleasePointLengthMeasure(source.getFugitiveLength().toString());
        }
        if (target.getReleasePointLengthUnitofMeasureCode() == null && source.getFugitiveLengthUomCode() != null) {
            target.setReleasePointLengthUnitofMeasureCode(source.getFugitiveLengthUomCode().getCode());
        }
        // Remove unneeded UoMs
        if (target.getReleasePointWidthMeasure() == null && target.getReleasePointWidthUnitofMeasureCode() != null) {
            target.setReleasePointWidthUnitofMeasureCode(null);
        }
        if (target.getReleasePointLengthMeasure() == null && target.getReleasePointLengthUnitofMeasureCode() != null) {
            target.setReleasePointLengthUnitofMeasureCode(null);
        }
        if (target.getReleasePointStackDiameterMeasure() == null && target.getReleasePointStackDiameterUnitofMeasureCode() != null) {
            target.setReleasePointStackDiameterUnitofMeasureCode(null);
        }
        if (target.getReleasePointFenceLineDistanceMeasure() == null && target.getReleasePointFenceLineDistanceUnitofMeasureCode() != null) {
            target.setReleasePointFenceLineDistanceUnitofMeasureCode(null);
        }
        if (target.getReleasePointFugitiveHeightMeasure() == null && target.getReleasePointFugitiveHeightUnitofMeasureCode() != null) {
            target.setReleasePointFugitiveHeightUnitofMeasureCode(null);
        }
    }

    @Mapping(source="typeCode.code", target="releasePointTypeCode")
    @Mapping(source="description", target="releasePointDescription")
    @Mapping(source="stackHeight", target="releasePointStackHeightMeasure")
    @Mapping(source="stackHeightUomCode.code", target="releasePointStackHeightUnitofMeasureCode")
    @Mapping(source="stackDiameter", target="releasePointStackDiameterMeasure")
    @Mapping(source="stackDiameterUomCode.code", target="releasePointStackDiameterUnitofMeasureCode")
    @Mapping(source="stackWidth", target="releasePointWidthMeasure")
    @Mapping(source="stackWidthUomCode.code", target="releasePointWidthUnitofMeasureCode")
    @Mapping(source="stackLength", target="releasePointLengthMeasure")
    @Mapping(source="stackLengthUomCode.code", target="releasePointLengthUnitofMeasureCode")
    @Mapping(source="exitGasVelocity", target="releasePointExitGasVelocityMeasure")
    @Mapping(source="exitGasVelocityUomCode.code", target="releasePointExitGasVelocityUnitofMeasureCode")
    @Mapping(source="exitGasFlowRate", target="releasePointExitGasFlowRateMeasure")
    @Mapping(source="exitGasFlowUomCode.code", target="releasePointExitGasFlowRateUnitofMeasureCode")
    @Mapping(source="exitGasTemperature", target="releasePointExitGasTemperatureMeasure")
    @Mapping(source="fenceLineDistance", target="releasePointFenceLineDistanceMeasure")
    @Mapping(source="fenceLineUomCode.code", target="releasePointFenceLineDistanceUnitofMeasureCode")
    @Mapping(source="fugitiveHeight", target="releasePointFugitiveHeightMeasure")
    @Mapping(source="fugitiveHeightUomCode.code", target="releasePointFugitiveHeightUnitofMeasureCode")

    @Mapping(source="fugitiveAngle", target="releasePointFugitiveAngleMeasure")
    @Mapping(source="comments", target="releasePointComment")
    @Mapping(source="operatingStatusCode.code", target="releasePointStatusCode")
    @Mapping(source="statusYear", target="releasePointStatusCodeYear")
    @Mapping(source=".", target="releasePointIdentification")
    @Mapping(source=".", target="releasePointGeographicCoordinates")
    ReleasePointDataType fromReleasePoint(ReleasePoint source);

    @Mapping(source="latitude", target="latitudeMeasure")
    @Mapping(source="longitude", target="longitudeMeasure")
    @Mapping(target="horizontalReferenceDatumCode", constant="003")
    @Mapping(source="fugitiveMidPt2Latitude", target="midPoint2LatitudeMeasure")
    @Mapping(source="fugitiveMidPt2Longitude", target="midPoint2LongitudeMeasure")
    GeographicCoordinatesDataType coordinatesFromReleasePoint(ReleasePoint source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="releasePointIdentifier", target="identifier")
    @Mapping(source="facilitySite.programSystemCode.code", target="programSystemCode")
    IdentificationDataType identificationFromReleasePoint(ReleasePoint source);

    default List<IdentificationDataType> identificationListFromReleasePoint(ReleasePoint source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(identificationFromReleasePoint(source));
    }
}
