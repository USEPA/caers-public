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
