package gov.epa.cef.web.service.mapper.json;

import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.service.dto.bulkUpload.*;
import gov.epa.cef.web.service.dto.json.*;
import gov.epa.cef.web.service.dto.json.shared.FacilityNAICSJsonDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface JsonUploadMapper {

    @Mapping(target="facilitySites", ignore = true)
    @Mapping(target = "emissionsUnits", ignore = true)
    @Mapping(target = "emissionsProcesses", ignore = true)
    @Mapping(target = "releasePoints", ignore = true)
    @Mapping(target = "releasePointAppts", ignore = true)
    @Mapping(target = "reportingPeriods", ignore = true)
    @Mapping(target = "emissions", ignore = true)
    EmissionsReportBulkUploadDto emissionsReportToDto(EmissionsReportJsonDto source);


    @Mapping(source="mainFacilityIdentification.identifier", target="agencyFacilityIdentifier")
    @Mapping(source="mainFacilityIdentification.programSystemCode", target="programSystemCode")
    @Mapping(source="statusCode.code", target="operatingStatusCode")
    @Mapping(source="statusCodeYear", target="statusYear")
    @Mapping(source="mainFacilityIdentification.tribalCode.code", target="tribalCode")
    @Mapping(source="mainFacilitySiteAddress.addressText", target="streetAddress")
    @Mapping(source="mainFacilitySiteAddress.localityName", target="city")
    @Mapping(target="countyCode", expression = "java(source.getMainFacilitySiteAddress().getCounty().getCode().substring(2))")
    @Mapping(source="mainFacilitySiteAddress.county.name", target="county")
    @Mapping(source="mainFacilitySiteAddress.state.code", target="stateCode")
    @Mapping(source="mainFacilitySiteAddress.state.fipsCode", target="stateFipsCode")
    @Mapping(source="mainFacilitySiteAddress.country.code", target="countryCode")
    @Mapping(source="mainFacilitySiteAddress.postalCode", target="postalCode")
    @Mapping(source="facilitySiteGeographicCoordinates.latitudeMeasure", target="latitude")
    @Mapping(source="facilitySiteGeographicCoordinates.longitudeMeasure", target="longitude")
    @Mapping(source="mailingAddress.addressText", target="mailingStreetAddress")
    @Mapping(source="mailingAddress.localityName", target="mailingCity")
    @Mapping(source="mailingAddress.state.code", target="mailingStateCode")
    @Mapping(source="mailingAddress.country.code", target="mailingCountryCode")
    @Mapping(source="mailingAddress.postalCode", target="mailingPostalCode")
    @Mapping(source="facilitySourceTypeCode.code", target="facilitySourceTypeCode")
    @Mapping(source="comment", target="comments")
    FacilitySiteBulkUploadDto facilitySiteToDto(FacilitySiteJsonDto source);

    @Mapping(source="mainIdentification.identifier", target="unitIdentifier")
    @Mapping(source="statusCode.code", target="operatingStatusCodeDescription")
    @Mapping(source="statusCode.description", target="operatingStatusCode")
    @Mapping(source="statusCodeYear", target="statusYear")
    @Mapping(source="unitTypeCode.code", target="typeCode")
    @Mapping(source="designCapacity.unit.code", target="unitOfMeasureCode")
    @Mapping(source="designCapacity.value", target="designCapacity")
    @Mapping(source="comment", target="comments")
    EmissionsUnitBulkUploadDto emissionsUnitToDto(EmissionsUnitJsonDto source);

    @Mapping(source="mainIdentification.identifier", target="emissionsProcessIdentifier")
    @Mapping(source="statusCode.code", target="operatingStatusCode")
    @Mapping(source="statusCode.description", target="operatingStatusCodeDescription")
    @Mapping(source="statusCodeYear", target="statusYear")
    @Mapping(source="aircraftEngineTypeCode.code", target="aircraftEngineTypeCode")
    @Mapping(source="sourceClassificationCode", target="sccCode")
    @Mapping(source="comment", target="comments")
    EmissionsProcessBulkUploadDto emissionsProcessToDto(EmissionsProcessJsonDto source);

    @Mapping(source="reportingPeriodTypeCode.code", target="reportingPeriodTypeCode")
    @Mapping(source="reportingPeriodTypeCode.shortName", target="reportingPeriodTypeShortName")
    @Mapping(source="emissionsOperatingTypeCode.code", target="emissionsOperatingTypeCode")
    @Mapping(source="calculationParameterTypeCode.code", target="calculationParameterTypeCode")
    @Mapping(source="calculationParameterTypeCode.description", target="calculationParameterTypeDescription")
    @Mapping(source="calculationParameter.value", target="calculationParameterValue")
    @Mapping(source="calculationParameter.unit.code", target="calculationParameterUom")
    @Mapping(source="calculationMaterialCode.code", target="calculationMaterialCode")
    @Mapping(source="calculationMaterialCode.description", target="calculationMaterialDescription")
    @Mapping(source="fuelUse.value", target="fuelUseValue")
    @Mapping(source="fuelUse.unit.code", target="fuelUseUom")
    @Mapping(source="fuelUseMaterialCode.code", target="fuelUseMaterialCode")
    @Mapping(source="fuelUseMaterialCode.description", target="fuelUseMaterialDescription")
    @Mapping(source="heatContent.value", target="heatContentValue")
    @Mapping(source="heatContent.unit.code", target="heatContentUom")
    @Mapping(source="emissionsOperatingTypeCode.shortName", target="emissionsOperatingTypeShortName")
    @Mapping(source="comment", target="comments")
    ReportingPeriodBulkUploadDto reportingPeriodToDto(ReportingPeriodJsonDto source);

    @Mapping(source="percentWinterActivity", target="percentWinter")
    @Mapping(source="percentSpringActivity", target="percentSpring")
    @Mapping(source="percentSummerActivity", target="percentSummer")
    @Mapping(source="percentFallActivity", target="percentFall")
    OperatingDetailBulkUploadDto operatingDetailToDto(OperatingDetailJsonDto source);

    @Mapping(source="pollutantCode.pollutantCode", target="pollutantCode")
    @Mapping(source="pollutantCode.pollutantName", target="pollutantName")
    @Mapping(source="totalEmissions.value", target="totalEmissions", numberFormat = "#.##################")
    @Mapping(source="totalEmissions.unit.code", target="emissionsUomCode")
    @Mapping(source="emissionCalculationMethodCode.code", target="emissionsCalcMethodCode")
    @Mapping(source="emissionCalculationMethodCode.description", target="emissionsCalcMethodDescription")
    @Mapping(source="emissionFactorNumeratorUnitofMeasureCode.code", target="emissionsNumeratorUom")
    @Mapping(source="emissionFactorDenominatorUnitofMeasureCode.code", target="emissionsDenominatorUom")
    @Mapping(source="calculatedEmissionsTons", target="calculatedEmissionsTons", numberFormat = "#.#####################")
    @Mapping(source="emissionFactor", target="emissionsFactor", numberFormat = "#.##################")
    @Mapping(source="comment", target="comments")
    EmissionBulkUploadDto emissionToDto(EmissionJsonDto source);

    @Mapping(source="mainIdentification.identifier", target="releasePointIdentifier")
    @Mapping(source="releasePointTypeCode.code", target="typeCode")
    @Mapping(source="releasePointTypeCode.description", target="typeDescription")
    @Mapping(source="stackHeight.value", target="stackHeight", numberFormat = "####.#")
    @Mapping(source="stackHeight.unit.code", target="stackHeightUomCode")
    @Mapping(source="stackDiameter.value", target="stackDiameter")
    @Mapping(source="stackDiameter.unit.code", target="stackDiameterUomCode")
    @Mapping(source="stackWidth.value", target="stackWidth")
    @Mapping(source="stackWidth.unit.code", target="stackWidthUomCode")
    @Mapping(source="stackLength.value", target="stackLength")
    @Mapping(source="stackLength.unit.code", target="stackLengthUomCode")
    @Mapping(source="exitGasVelocity.value", target="exitGasVelocity")
    @Mapping(source="exitGasVelocity.unit.code", target="exitGasVelocityUomCode")
    @Mapping(source="exitGasFlowRate.value", target="exitGasFlowRate")
    @Mapping(source="exitGasFlowRate.unit.code", target="exitGasFlowUomCode")
    @Mapping(source="statusCode.code", target="operatingStatusCode")
    @Mapping(source="statusCode.description", target="operatingStatusDescription")
    @Mapping(source="fugitiveHeight.value", target="fugitiveHeight")
    @Mapping(source="fugitiveHeight.unit.code", target="fugitiveHeightUomCode")
    @Mapping(source="fenceLineDistance.value", target="fenceLineDistance")
    @Mapping(source="fenceLineDistance.unit.code", target="fenceLineUomCode")
    @Mapping(source="releasePointGeographicCoordinates.latitudeMeasure", target="latitude")
    @Mapping(source="releasePointGeographicCoordinates.longitudeMeasure", target="longitude")
    @Mapping(source="releasePointGeographicCoordinates.midPoint2LatitudeMeasure", target="fugitiveLine2Latitude")
    @Mapping(source="releasePointGeographicCoordinates.midPoint2LongitudeMeasure", target="fugitiveLine2Longitude")
    @Mapping(source="comment", target="comments")
    ReleasePointBulkUploadDto releasePointToDto(ReleasePointJsonDto source);

    @Mapping(source="mainReleasePointIdentification.identifier", target="releasePointName")
    @Mapping(source="mainPathIdentification.identifier", target="pathName")
    @Mapping(source="averagePercentEmissions", target="percent")
    ReleasePointApptBulkUploadDto releasePointApptToDto(ReleasePointApptJsonDto source);

    @Mapping(source="mainIdentification.identifier", target="pathId")
    @Mapping(source="percentPathEffectiveness", target="percentControl", numberFormat = "###.#")
    ControlPathBulkUploadDto controlPathToDto(ControlPathJsonDto source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="pollutantCode.pollutantCode", target="pollutantCode")
    @Mapping(source="pollutantCode.pollutantName", target="pollutantName")
    @Mapping(source="percentControlMeasuresReductionEfficiency", target="percentReduction", numberFormat = "###.#")
    ControlPathPollutantBulkUploadDto controlPathPollutantToDto(ControlPollutantJsonDto source);

    @Mapping(source="mainIdentification.identifier", target="identifier")
    @Mapping(source="statusCode.code", target="operatingStatusCode")
    @Mapping(source="statusCode.description", target="operatingStatusCodeDescription")
    @Mapping(source="controlMeasureCode.code", target="controlMeasureCode")
    @Mapping(source="controlMeasureCode.description", target="controlMeasureCodeDescription")
    @Mapping(source="percentControlEffectiveness", target="percentControl", numberFormat = "###.#")
    ControlBulkUploadDto controlToDto(ControlJsonDto source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="pollutantCode.pollutantCode", target="pollutantCode")
    @Mapping(source="pollutantCode.pollutantName", target="pollutantName")
    @Mapping(source="percentControlMeasuresReductionEfficiency", target="percentReduction", numberFormat = "###.#")
    ControlPollutantBulkUploadDto controlPollutantToDto(ControlPollutantJsonDto source);

    @Mapping(source="mainPathIdentification.identifier", target="childPathName")
    @Mapping(source="mainControlIdentification.identifier", target="controlName")
    @Mapping(source="averagePercentApportionment", target="percentApportionment")
    ControlAssignmentBulkUploadDto controlAssignmentToDto(ControlAssignmentJsonDto source);

    FacilityNAICSBulkUploadDto faciliytNAICSToDto(FacilityNAICSJsonDto source);

    @Mapping(source="streetAddress.addressText", target="streetAddress")
    @Mapping(source="streetAddress.localityName", target="city")
    @Mapping(target="countyCode", expression = "java(source.getStreetAddress().getCounty().getCode().substring(2))")
    @Mapping(source="streetAddress.county.name", target="county")
    @Mapping(source="streetAddress.state.code", target="stateCode")
    @Mapping(source="streetAddress.state.fipsCode", target="stateFipsCode")
    @Mapping(source="streetAddress.country.code", target="countryCode")
    @Mapping(source="streetAddress.postalCode", target="postalCode")
    @Mapping(source="mailingStreetAddress.addressText", target="mailingStreetAddress")
    @Mapping(source="mailingStreetAddress.localityName", target="mailingCity")
    @Mapping(source="mailingStreetAddress.state.code", target="mailingStateCode")
    @Mapping(source="mailingStreetAddress.country.code", target="mailingCountryCode")
    @Mapping(source="mailingStreetAddress.postalCode", target="mailingPostalCode")
    @Mapping(source="type.code", target="type")
    @Mapping(source="type.description", target="contactTypeDescription")
    FacilitySiteContactBulkUploadDto facilitySiteContactToDto(FacilitySiteContactJsonDto source);
}
