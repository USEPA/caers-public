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

import gov.epa.cef.web.service.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import gov.epa.cef.web.domain.Emission;
import gov.epa.cef.web.domain.EmissionFormulaVariable;
import gov.epa.cef.web.domain.ReportingPeriod;

@Mapper(componentModel = "spring", uses = {LookupEntityMapper.class})
public interface EmissionMapper {

    @Mapping(source="reportingPeriod.id", target="reportingPeriodId")
    EmissionDto toDto(Emission source);

    List<EmissionDto> toDtoList(List<Emission> source);

    @Mapping(source="reportingPeriodId", target="reportingPeriod.id")
    Emission fromDto(EmissionDto source);

    @Mapping(target = "emissionsCalcMethodCode", qualifiedByName  = "CalculationMethodCode")
    @Mapping(target = "emissionsUomCode", qualifiedByName  = "UnitMeasureCode")
    @Mapping(target = "emissionsNumeratorUom", qualifiedByName  = "UnitMeasureCode")
    @Mapping(target = "emissionsDenominatorUom", qualifiedByName  = "UnitMeasureCode")
    @Mapping(target = "webfireEf", qualifiedByName = "WebfireEmissionFactor")
    @Mapping(target = "variables", ignore = true)
    void updateFromDto(EmissionDto source, @MappingTarget Emission target);

    @Mapping(source="emission.id", target="emissionId")
    EmissionFormulaVariableDto formulaVariableToDto(EmissionFormulaVariable source);

    EmissionFormulaVariable formulaVariableFromDto(EmissionFormulaVariableDto source);

    List<EmissionFormulaVariable> formulaVariableFromDtoList(List<EmissionFormulaVariableDto> source);

    @Mapping(target = "emission", ignore = true)
    @Mapping(target = "variableCode", ignore = true)
    EmissionFormulaVariable updateFormulaVariableFromDto(EmissionFormulaVariableDto source, @MappingTarget EmissionFormulaVariable target);

    @Mapping(source="emissionsProcess.emissionsUnit.id", target="emissionsUnitId")
    @Mapping(source="emissionsProcess.emissionsUnit.unitIdentifier", target="unitIdentifier")
    @Mapping(source="emissionsProcess.emissionsUnit.description", target="unitDescription")
    @Mapping(source="emissionsProcess.emissionsUnit.operatingStatusCode", target="unitStatus")
    @Mapping(source="emissionsProcess.id", target="emissionsProcessId")
    @Mapping(source="emissionsProcess.emissionsProcessIdentifier", target="emissionsProcessIdentifier")
    @Mapping(source="emissionsProcess.description", target="emissionsProcessDescription")
    @Mapping(source="emissionsProcess.operatingStatusCode", target="operatingStatusCode")
    @Mapping(source="id", target="reportingPeriodId")
    EmissionBulkEntryHolderDto periodToEmissionBulkEntryDto(ReportingPeriod source);

    List<EmissionBulkEntryHolderDto> periodToEmissionBulkEntryDtoList(List<ReportingPeriod> source);

    @Mapping(source="emissionsProcess.emissionsUnit.id", target="emissionsUnitId")
    @Mapping(source="emissionsProcess.emissionsUnit.unitIdentifier", target="unitIdentifier")
    @Mapping(source="emissionsProcess.emissionsUnit.description", target="unitDescription")
    @Mapping(source="emissionsProcess.emissionsUnit.operatingStatusCode", target="unitStatus")
    @Mapping(source="emissionsProcess.emissionsUnit.initialMonthlyReportingPeriod",
            target="unitInitialMonthlyReportingPeriod")
    @Mapping(source="emissionsProcess.id", target="emissionsProcessId")
    @Mapping(source="emissionsProcess.emissionsProcessIdentifier", target="emissionsProcessIdentifier")
    @Mapping(source="emissionsProcess.description", target="emissionsProcessDescription")
    @Mapping(source="emissionsProcess.operatingStatusCode", target="operatingStatusCode")
    @Mapping(source="emissionsProcess.initialMonthlyReportingPeriod", target="processInitialMonthlyReportingPeriod")
    @Mapping(source="id", target="reportingPeriodId")
    @Mapping(source="annualReportingPeriod.id", target="annualReportingPeriodId")
    @Mapping(source="reportingPeriodTypeCode.shortName", target="period")
    MonthlyReportingEmissionHolderDto periodToMonthlyReportingEmissionDto(ReportingPeriod source);

    @Mapping(source="reportingPeriod.emissionsProcess.emissionsUnit.facilitySite.agencyFacilityIdentifier",
            target="agencyFacilityIdentifier")
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsUnit.facilitySite.name", target="facilityName")
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsUnit.id", target="emissionsUnitId")
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsUnit.unitIdentifier", target="unitIdentifier")
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsUnit.description", target="unitDescription")
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsUnit.operatingStatusCode", target="unitStatus")
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsUnit.initialMonthlyReportingPeriod",
            target="unitInitialMonthlyReportingPeriod")
    @Mapping(source="reportingPeriod.emissionsProcess.id", target="emissionsProcessId")
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsProcessIdentifier", target="emissionsProcessIdentifier")
    @Mapping(source="reportingPeriod.emissionsProcess.description", target="emissionsProcessDescription")
    @Mapping(source="reportingPeriod.emissionsProcess.sccCode", target="emissionsProcessSccCode")
    @Mapping(source="reportingPeriod.emissionsProcess.operatingStatusCode", target="operatingStatusCode")
    @Mapping(source="reportingPeriod.emissionsProcess.initialMonthlyReportingPeriod",
            target="processInitialMonthlyReportingPeriod")
    @Mapping(source="reportingPeriod.id", target="reportingPeriodId")
    @Mapping(source="reportingPeriod.reportingPeriodTypeCode.shortName", target="reportingPeriodName")
    @Mapping(source="reportingPeriod.calculationMaterialCode.description", target="calculationMaterialShortName")
    @Mapping(source="reportingPeriod.calculationParameterValue", target="calculationParameterValue")
    @Mapping(source="reportingPeriod.calculationParameterUom.code", target="calculationParameterUomCode")
    @Mapping(source="reportingPeriod.fuelUseMaterialCode.description", target="fuelUseMaterialShortName")
    @Mapping(source="reportingPeriod.fuelUseValue", target="fuelUseValue")
    @Mapping(source="reportingPeriod.fuelUseUom.code", target="fuelUseUomCode")
    @Mapping(expression="java(source.getReportingPeriod().getOperatingDetails().get(0).getId())",
            target="operatingDetailId")
    @Mapping(expression="java(source.getReportingPeriod().getOperatingDetails().get(0).getActualHoursPerPeriod())",
            target="hoursPerPeriod")
    @Mapping(expression="java(source.getReportingPeriod().getOperatingDetails().get(0).getAvgHoursPerDay())",
            target="avgHoursPerDay")
    @Mapping(expression="java(source.getReportingPeriod().getOperatingDetails().get(0).getAvgDaysPerWeek())",
            target="avgDaysPerWeek")
    @Mapping(expression="java(source.getReportingPeriod().getOperatingDetails().get(0).getAvgWeeksPerPeriod())",
            target="avgWeeksPerPeriod")
    @Mapping(source="id", target="emissionId")
    @Mapping(source="pollutant.pollutantName", target="pollutantName")
    @Mapping(source="emissionsCalcMethodCode.description", target="emissionsCalcMethodDescription")
    @Mapping(expression="java(gov.epa.cef.web.util.CalculationUtils.formatEmissionFactorOrRate(" +
            "source.getMonthlyRate()," +
            "source.getEmissionsNumeratorUom() != null ? source.getEmissionsNumeratorUom().getCode() : null," +
            "source.getEmissionsDenominatorUom() != null ? source.getEmissionsDenominatorUom().getCode() : null))",
        target="monthlyRate")
    @Mapping(expression="java(gov.epa.cef.web.util.CalculationUtils.formatEmissionFactorOrRate(" +
            "source.getEmissionsFactor()," +
            "source.getEmissionsNumeratorUom() != null ? source.getEmissionsNumeratorUom().getCode() : null," +
            "source.getEmissionsDenominatorUom() != null ? source.getEmissionsDenominatorUom().getCode() : null))",
        target="emissionsFactor")
    @Mapping(source="totalEmissions", target="totalEmissions")
    @Mapping(source="emissionsUomCode.code", target="emissionsUomCode")
    MonthlyReportingDownloadDto emissionToMonthlyReportingDownloadDto(Emission source);

    List<MonthlyReportingDownloadDto> emissionToMonthlyReportingDownloadDtoList(List<Emission> source);

    List<MonthlyReportingEmissionHolderDto> periodToMonthlyReportingEmissionDtoList(List<ReportingPeriod> source);

    EmissionBulkEntryDto toBulkDto(Emission source);

    @Mapping(source="annualEmission.id", target="annualEmissionId")
    MonthlyReportingEmissionDto toMonthlyReportingDto(Emission source);
}
