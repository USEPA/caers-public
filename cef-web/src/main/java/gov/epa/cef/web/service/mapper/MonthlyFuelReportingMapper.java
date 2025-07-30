/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
