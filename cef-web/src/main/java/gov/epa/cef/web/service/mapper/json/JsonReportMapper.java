/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
