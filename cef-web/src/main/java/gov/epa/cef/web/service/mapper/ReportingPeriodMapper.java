/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import gov.epa.cef.web.domain.ReportingPeriod;
import gov.epa.cef.web.service.dto.ReportingPeriodBulkEntryDto;
import gov.epa.cef.web.service.dto.ReportingPeriodDto;


@Mapper(componentModel = "spring", uses = {LookupEntityMapper.class})
public interface ReportingPeriodMapper {

    @Mapping(source="emissionsProcess.id", target="emissionsProcessId")
    ReportingPeriodDto toDto(ReportingPeriod source);

    List<ReportingPeriodDto> toDtoList(List<ReportingPeriod> source);

    @Mapping(source="emissionsProcessId", target="emissionsProcess.id")
    ReportingPeriod fromDto(ReportingPeriodDto source);

    @Mapping(target = "reportingPeriodTypeCode", ignore = true)
    @Mapping(target = "emissionsOperatingTypeCode", ignore = true)
    @Mapping(target = "calculationParameterTypeCode", ignore = true)
    @Mapping(target = "calculationParameterUom", ignore = true)
    @Mapping(target = "calculationMaterialCode", ignore = true)
    @Mapping(target = "fuelUseUom", ignore = true)
    @Mapping(target = "fuelUseMaterialCode", ignore = true)
    @Mapping(target = "heatContentUom", ignore = true)
    @Mapping(target = "emissions", ignore = true)
    @Mapping(target = "operatingDetails", ignore = true)
    void updateFromDto(ReportingPeriodDto source, @MappingTarget ReportingPeriod target);

    @Mapping(source="emissionsProcess.emissionsUnit.id", target="emissionsUnitId")
    @Mapping(source="emissionsProcess.emissionsUnit.unitIdentifier", target="unitIdentifier")
    @Mapping(source="emissionsProcess.emissionsUnit.description", target="unitDescription")
    @Mapping(source="emissionsProcess.emissionsUnit.operatingStatusCode", target="unitStatus")
    @Mapping(source="emissionsProcess.id", target="emissionsProcessId")
    @Mapping(source="emissionsProcess.emissionsProcessIdentifier", target="emissionsProcessIdentifier")
    @Mapping(source="emissionsProcess.description", target="emissionsProcessDescription")
    @Mapping(source="emissionsProcess.operatingStatusCode", target="operatingStatusCode")
    @Mapping(source="emissionsProcess.sccCode", target="emissionsProcessSccCode")
    @Mapping(source="id", target="reportingPeriodId")
    ReportingPeriodBulkEntryDto toBulkEntryDto(ReportingPeriod source);

    List<ReportingPeriodBulkEntryDto> toBulkEntryDtoList(List<ReportingPeriod> source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="reportingPeriodId", target="id")
    @Mapping(source="calculationParameterValue", target="calculationParameterValue")
    ReportingPeriodDto toUpdateDtoFromBulk(ReportingPeriodBulkEntryDto source);

    List<ReportingPeriodDto> toUpdateDtoFromBulkList(List<ReportingPeriodBulkEntryDto> source);
}
