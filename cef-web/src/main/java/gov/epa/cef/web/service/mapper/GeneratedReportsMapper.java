/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.mapper;

import java.util.List;

import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.service.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GeneratedReportsMapper {

	@Mapping(source="nonPointBucket", target="naics")
	@Mapping(source="sccCode", target="scc")
	@Mapping(source="fuelUse", target="fuelConsumption")
	@Mapping(source="nonPointStandardizedUom", target="units")
    NonPointFuelSubtractionReportDto nonPointFuelSubtractionReportToDto(NonPointFuelSubtractionReport report);

    List<NonPointFuelSubtractionReportDto> nonPointFuelSubtractionReportToDtos(List<NonPointFuelSubtractionReport> reports);

    @Mapping(source="programSystemCode", target="slt")
    @Mapping(source="avgErrors", target="averageErrors")
    @Mapping(source="avgWarnings", target="averageWarnings")
    AverageNumberQAsReportDto averageNumberQAsReportToDto(AverageNumberQAsReport report);

    List<AverageNumberQAsReportDto> averageNumberQAsReportToDtos(List<AverageNumberQAsReport> reports);

    @Mapping(source="programSystemCode", target="slt")
    SubmissionsStatusAuditReportDto submissionStatusAuditReportToDto(SubmissionStatusAuditReport report);

    List<SubmissionsStatusAuditReportDto> submissionStatusAuditReportToDtos(List<SubmissionStatusAuditReport> reports);

    @Mapping(source="reportingPeriod.emissionsProcess.emissionsUnit.facilitySite.agencyFacilityIdentifier", target="agencyFacilityId")
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsUnit.facilitySite.name", target="facilityName")
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsUnit.unitIdentifier", target="agencyUnitId")
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsUnit.description", target="unitDescription")
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsUnit.operatingStatusCode.description", target="unitOperatingStatus")
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsProcessIdentifier", target="agencyProcessId")
    @Mapping(source="reportingPeriod.emissionsProcess.description", target="processDescription")
    @Mapping(source="reportingPeriod.emissionsProcess.operatingStatusCode.description", target="processOperatingStatus")
    @Mapping(source="reportingPeriod.emissionsProcess.sccCode", target="scc")
    @Mapping(source="avgWeeksPerPeriod", target="avgWeeksPerYear")
    @Mapping(source="actualHoursPerPeriod", target="hoursPerReportingPeriod")
    @Mapping(source="percentWinter", target="winterOperatingPercent")
    @Mapping(source="percentSpring", target="springOperatingPercent")
    @Mapping(source="percentSummer", target="summerOperatingPercent")
    @Mapping(source="percentFall", target="fallOperatingPercent")
    @Mapping(source="reportingPeriod.reportingPeriodTypeCode.shortName", target="reportingPeriod")
    @Mapping(source="reportingPeriod.emissionsOperatingTypeCode.shortName", target="operatingType")
    @Mapping(source="reportingPeriod.calculationParameterTypeCode.description", target="throughputParameter")
    @Mapping(source="reportingPeriod.calculationMaterialCode.description", target="throughputMaterial")
    @Mapping(source="reportingPeriod.calculationParameterValue", target="throughputValue")
    @Mapping(source="reportingPeriod.calculationParameterUom.code", target="throughputUom")
    @Mapping(source="reportingPeriod.fuelUseMaterialCode.description", target="fuelMaterial")
    @Mapping(source="reportingPeriod.fuelUseValue", target="fuelValue")
    @Mapping(source="reportingPeriod.fuelUseUom.code", target="fuelUom")
    @Mapping(source="reportingPeriod.heatContentValue", target="heatContentRatio")
    @Mapping(source="reportingPeriod.heatContentUom.code", target="heatContentRatioNumerator")
    @Mapping(source="reportingPeriod.fuelUseUom.code", target="heatContentRatioDenominator")
    StandaloneEmissionsProcessReportDto processToStandaloneProcessReportDto(OperatingDetail detail);

    List<StandaloneEmissionsProcessReportDto> processToStandaloneProcessReportDtos(List<OperatingDetail> details);

    @Mapping(source="year", target="submittalYear")
    @Mapping(source="industry", target="industrySector")
    @Mapping(source="eisLastSubStatus", target="eisTransmissionStatus")
    FacilityReportingStatusReportDto submissionReviewToFacilityReportingStatusReportDto(SubmissionsReviewNotStartedDashboardView report);

    List<FacilityReportingStatusReportDto> submissionReviewToFacilityReportingStatusReportDtos(List<SubmissionsReviewNotStartedDashboardView> reports);
}
