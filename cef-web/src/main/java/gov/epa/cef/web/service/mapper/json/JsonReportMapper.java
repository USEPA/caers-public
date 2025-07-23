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

import gov.epa.cef.web.domain.EmissionsProcess;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.EmissionsUnit;
import gov.epa.cef.web.domain.ReleasePoint;
import gov.epa.cef.web.service.dto.json.EmissionsReportJsonDto;
import gov.epa.cef.web.service.dto.json.ReportInfoJsonDto;
import net.exchangenetwork.schema.cer._2._0.CERSDataType;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {JsonFacilitySiteMapper.class})
public interface JsonReportMapper {

    @Mapping(source="masterFacilityRecord.agencyFacilityIdentifier", target="agencyFacilityIdentifier")
    @Mapping(source="year", target="emissionsYear")
    @Mapping(source="facilitySites", target="facilitySite")
    @Mapping(source="programSystemCode.code", target="programSystemCode")
    EmissionsReportJsonDto fromEmissionsReport(EmissionsReport source);

    List<EmissionsReportJsonDto> fromEmissionsReportList(List<EmissionsReport>source);

    @Mapping(source="id", target="reportId")
    @Mapping(source="masterFacilityRecord.agencyFacilityIdentifier", target="agencyFacilityIdentifier")
    @Mapping(source="lastModifiedDate", target="modifiedDate")
    @Mapping(source="certificationDate", target="certifiedDate")
    ReportInfoJsonDto infoFromEmissionsReport(EmissionsReport source);

    List<ReportInfoJsonDto> infoFromEmissionsReportList(List<EmissionsReport> source);

}
