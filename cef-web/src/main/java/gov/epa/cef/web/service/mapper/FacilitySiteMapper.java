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
import org.mapstruct.MappingTarget;

import gov.epa.cef.web.domain.FacilitySite;
import gov.epa.cef.web.service.dto.FacilitySiteDto;

@Mapper(componentModel = "spring", uses = {FacilityNAICSMapper.class, LookupEntityMapper.class})
public interface FacilitySiteMapper {

    @Mapping(source="emissionsReport.masterFacilityRecord.id", target="emissionsReport.masterFacilityRecordId")
    FacilitySiteDto toDto(FacilitySite facility);

    List<FacilitySiteDto> toDtoList(List<FacilitySite> facilitySites);

    @Mapping(target="countyCode", qualifiedByName = "CountyCode")
    @Mapping(target="stateCode", qualifiedByName = "FipsStateCode")
    @Mapping(target="mailingStateCode", qualifiedByName = "FipsStateCode")
    @Mapping(target="tribalCode", qualifiedByName = "TribalCode")
    @Mapping(target ="operatingStatusCode", qualifiedByName  = "OperatingStatusCode")
    @Mapping(target = "facilityNAICS", ignore = true)
    @Mapping(target = "facilityCategoryCode", qualifiedByName = "FacilityCategoryCode")
    @Mapping(target = "facilitySourceTypeCode", qualifiedByName = "FacilitySourceTypeCode")
    void updateFromDto(FacilitySiteDto source, @MappingTarget FacilitySite target);

    @Mapping(target="stateCode", qualifiedByName = "FipsStateCode")
    @Mapping(target="mailingStateCode", qualifiedByName = "FipsStateCode")
    FacilitySite fromDto(FacilitySiteDto facilitySite);
}