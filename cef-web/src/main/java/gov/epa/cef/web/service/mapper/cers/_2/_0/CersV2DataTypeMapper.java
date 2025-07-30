/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.mapper.cers._2._0;

import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import gov.epa.cef.web.domain.EmissionsProcess;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.EmissionsUnit;
import gov.epa.cef.web.domain.ReleasePoint;
import net.exchangenetwork.schema.cer._2._0.CERSDataType;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {CersV2FacilitySiteMapper.class})
public interface CersV2DataTypeMapper {

    @Mapping(source="year", target="emissionsYear")
    @Mapping(source="facilitySites", target="facilitySite")
    @Mapping(source="programSystemCode.code", target="programSystemCode")
    CERSDataType fromEmissionsReport(EmissionsReport source);
    
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="operatingStatusCode", target="operatingStatusCode")
    @Mapping(source="facilitySite", target="facilitySite")
    @Mapping(source="unitIdentifier", target="unitIdentifier")
    @Mapping(source="statusYear", target="statusYear")
    EmissionsUnit emissionsUnitToNonOperatingEmissionsUnit(EmissionsUnit source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="operatingStatusCode", target="operatingStatusCode")
    @Mapping(source="emissionsUnit", target="emissionsUnit")
    @Mapping(source="emissionsProcessIdentifier", target="emissionsProcessIdentifier")
    @Mapping(source="statusYear", target="statusYear")
    EmissionsProcess processToNonOperatingEmissionsProcess(EmissionsProcess source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="facilitySite", target="facilitySite")
    @Mapping(source="operatingStatusCode", target="operatingStatusCode")
    @Mapping(source="statusYear", target="statusYear")
    @Mapping(source="releasePointIdentifier", target="releasePointIdentifier")
    ReleasePoint releasePointToNonOperatingReleasePoint(ReleasePoint source);
}
