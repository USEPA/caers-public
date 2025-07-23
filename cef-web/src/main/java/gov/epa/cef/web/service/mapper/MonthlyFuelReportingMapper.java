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
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import gov.epa.cef.web.domain.MonthlyFuelReporting;
import gov.epa.cef.web.domain.ReportingPeriod;
import gov.epa.cef.web.service.dto.MonthlyFuelReportingDto;

@Mapper(componentModel = "spring", uses = {LookupEntityMapper.class})
public interface MonthlyFuelReportingMapper {
	
	@Mapping(source="id", target="reportingPeriodId")
	@Mapping(source="emissionsProcess.emissionsUnit.id", target="emissionsUnitId")
    @Mapping(source="emissionsProcess.emissionsUnit.unitIdentifier", target="unitIdentifier")
    @Mapping(source="emissionsProcess.emissionsUnit.description", target="unitDescription")
    @Mapping(source="emissionsProcess.emissionsUnit.operatingStatusCode", target="unitStatus")
    @Mapping(source="emissionsProcess.id", target="emissionsProcessId")
    @Mapping(source="emissionsProcess.emissionsProcessIdentifier", target="emissionsProcessIdentifier")
    @Mapping(source="emissionsProcess.description", target="emissionsProcessDescription")
    @Mapping(source="emissionsProcess.operatingStatusCode", target="operatingStatusCode")
    @Mapping(source="emissionsProcess.sccCode", target="emissionsProcessSccCode")
	@Mapping(source="fuelUseMaterialCode", target="fuelUseMaterialCode")
	@Mapping(source="fuelUseUom", target="fuelUseUom")
	@Mapping(target="fuelUseValue", ignore = true)
	MonthlyFuelReportingDto rptPeriodToDto(ReportingPeriod source);
	
	List<MonthlyFuelReportingDto> rptPeriodToDtoList(List<ReportingPeriod> source);
	
	@Mapping(source="reportingPeriodId", target="reportingPeriod.id")
	MonthlyFuelReporting fromDto(MonthlyFuelReportingDto source);
	
	@Mapping(source="reportingPeriod.emissionsProcess.emissionsUnit.id", target="emissionsUnitId")
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsUnit.unitIdentifier", target="unitIdentifier")
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsUnit.description", target="unitDescription")
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsUnit.operatingStatusCode", target="unitStatus")
    @Mapping(source="reportingPeriod.emissionsProcess.id", target="emissionsProcessId")
    @Mapping(source="reportingPeriod.emissionsProcess.emissionsProcessIdentifier", target="emissionsProcessIdentifier")
    @Mapping(source="reportingPeriod.emissionsProcess.description", target="emissionsProcessDescription")
    @Mapping(source="reportingPeriod.emissionsProcess.operatingStatusCode", target="operatingStatusCode")
    @Mapping(source="reportingPeriod.emissionsProcess.sccCode", target="emissionsProcessSccCode")
	@Mapping(source="reportingPeriod.id", target="reportingPeriodId")
	@Mapping(source="reportingPeriod.fuelUseMaterialCode", target="fuelUseMaterialCode")
	@Mapping(source="reportingPeriod.fuelUseUom", target="fuelUseUom")
	MonthlyFuelReportingDto toDto(MonthlyFuelReporting source);
	
	List<MonthlyFuelReportingDto> toDtoList(List<MonthlyFuelReporting> source);
	
}
