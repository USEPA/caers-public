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

import gov.epa.cef.web.domain.OperatingDetail;
import gov.epa.cef.web.service.dto.MonthlyReportingPeriodHolderDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface MonthlyReportingPeriodHolderMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsUnit.id", target="emissionsUnitId")
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsUnit.unitIdentifier", target="unitIdentifier")
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsUnit.initialMonthlyReportingPeriod",
        target="unitInitialMonthlyReportingPeriod")
    @Mapping(source="reportingPeriod.emissionsProcess.sccCode", target="emissionsProcessSccCode")
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsProcessIdentifier", target="emissionsProcessIdentifier")
    @Mapping(source="reportingPeriod.emissionsProcess.description", target="emissionsProcessDescription")
    @Mapping(source="reportingPeriod.emissionsProcess.initialMonthlyReportingPeriod",
        target="processInitialMonthlyReportingPeriod")
    @Mapping(source="reportingPeriod.emissionsProcess.id", target="emissionsProcessId")
    @Mapping(source="reportingPeriod.calculationMaterialCode", target="calculationMaterialCode")
    @Mapping(source="reportingPeriod.calculationParameterValue", target="calculationParameterValue")
    @Mapping(source="reportingPeriod.calculationParameterUom", target="calculationParameterUom")
    @Mapping(source="reportingPeriod.fuelUseMaterialCode", target="fuelUseMaterialCode")
    @Mapping(source="reportingPeriod.fuelUseValue", target="fuelUseValue")
    @Mapping(source="reportingPeriod.fuelUseUom", target="fuelUseUom")
    @Mapping(source="reportingPeriod.id", target="reportingPeriodId")
    @Mapping(source="actualHoursPerPeriod", target="actualHoursPerPeriod")
    @Mapping(source="avgHoursPerDay", target="avgHoursPerDay")
    @Mapping(source="avgDaysPerWeek", target="avgDaysPerWeek")
    @Mapping(source="avgWeeksPerPeriod", target="avgWeeksPerPeriod")
    @Mapping(source="reportingPeriod.reportingPeriodTypeCode.shortName", target="period")
    MonthlyReportingPeriodHolderDto toDto(OperatingDetail source);

    List<MonthlyReportingPeriodHolderDto> toDtoList(List<OperatingDetail> source);

}
