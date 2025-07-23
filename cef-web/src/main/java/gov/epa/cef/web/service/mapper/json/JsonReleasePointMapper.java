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
package gov.epa.cef.web.service.mapper.json;

import gov.epa.cef.web.domain.ReleasePoint;
import gov.epa.cef.web.service.dto.json.IdentificationJsonDto;
import gov.epa.cef.web.service.dto.json.ReleasePointJsonDto;
import gov.epa.cef.web.service.dto.json.shared.GeographicCoordinatesJsonDto;
import net.exchangenetwork.schema.cer._2._0.GeographicCoordinatesDataType;
import net.exchangenetwork.schema.cer._2._0.IdentificationDataType;
import net.exchangenetwork.schema.cer._2._0.ReleasePointDataType;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {})
public interface JsonReleasePointMapper {

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

    @Mapping(source="stackHeight", target="stackHeight.value")
    @Mapping(source="stackHeightUomCode", target="stackHeight.unit")
    @Mapping(source="stackDiameter", target="stackDiameter.value")
    @Mapping(source="stackDiameterUomCode", target="stackDiameter.unit")
    @Mapping(source="stackWidth", target="stackWidth.value")
    @Mapping(source="stackWidthUomCode", target="stackWidth.unit")
    @Mapping(source="stackLength", target="stackLength.value")
    @Mapping(source="stackLengthUomCode", target="stackLength.unit")
    @Mapping(source="exitGasVelocity", target="exitGasVelocity.value")
    @Mapping(source="exitGasVelocityUomCode", target="exitGasVelocity.unit")
    @Mapping(source="exitGasFlowRate", target="exitGasFlowRate.value")
    @Mapping(source="exitGasFlowUomCode", target="exitGasFlowRate.unit")
    @Mapping(source="exitGasTemperature", target="exitGasTemperature")
    @Mapping(source="fenceLineDistance", target="fenceLineDistance.value")
    @Mapping(source="fenceLineUomCode", target="fenceLineDistance.unit")
    @Mapping(source="fugitiveHeight", target="fugitiveHeight.value")
    @Mapping(source="fugitiveHeightUomCode", target="fugitiveHeight.unit")

    @Mapping(source="typeCode", target="releasePointTypeCode")
    @Mapping(source="fugitiveAngle", target="fugitiveAngle")
    @Mapping(source="comments", target="comment")
    @Mapping(source="operatingStatusCode", target="statusCode")
    @Mapping(source="statusYear", target="statusCodeYear")
    @Mapping(source=".", target="identification")
    @Mapping(source=".", target="releasePointGeographicCoordinates")
    ReleasePointJsonDto fromReleasePoint(ReleasePoint source);

    @Mapping(source="latitude", target="latitudeMeasure")
    @Mapping(source="longitude", target="longitudeMeasure")
    @Mapping(source="fugitiveMidPt2Latitude", target="midPoint2LatitudeMeasure")
    @Mapping(source="fugitiveMidPt2Longitude", target="midPoint2LongitudeMeasure")
    GeographicCoordinatesJsonDto coordinatesFromReleasePoint(ReleasePoint source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="releasePointIdentifier", target="identifier")
    @Mapping(source="facilitySite.programSystemCode.code", target="programSystemCode")
    IdentificationJsonDto identificationFromReleasePoint(ReleasePoint source);

    default List<IdentificationJsonDto> identificationListFromReleasePoint(ReleasePoint source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(identificationFromReleasePoint(source));
    }
}
