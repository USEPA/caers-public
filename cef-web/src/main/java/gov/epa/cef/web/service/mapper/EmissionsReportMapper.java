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

import gov.epa.cef.web.domain.QualityCheckResults;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.service.dto.EmissionsReportDto;
import gov.epa.cef.web.service.dto.QualityCheckResultsDto;


@Mapper(componentModel = "spring", uses = {})
public interface EmissionsReportMapper {

    @Mapping(source="masterFacilityRecord.id", target="masterFacilityRecordId")
    EmissionsReportDto toDto(EmissionsReport emissionsReport);

    List<EmissionsReportDto> toDtoList(List<EmissionsReport> emissionsReportList);

    void updateFromDto(EmissionsReportDto source, @MappingTarget EmissionsReport target);

    QualityCheckResultsDto toQualityCheckResultsDto(QualityCheckResults qaResults);

}
