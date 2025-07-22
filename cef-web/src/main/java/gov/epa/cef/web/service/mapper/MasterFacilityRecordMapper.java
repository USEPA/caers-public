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
import gov.epa.cef.web.domain.MasterFacilityRecord;
import gov.epa.cef.web.service.dto.FacilitySiteDto;
import gov.epa.cef.web.service.dto.MasterFacilityRecordDto;

@Mapper(componentModel = "spring", uses = {LookupEntityMapper.class, MasterFacilityNAICSMapper.class, FacilityPermitMapper.class})
public interface MasterFacilityRecordMapper {

    MasterFacilityRecordDto toDto(MasterFacilityRecord source);

    List<MasterFacilityRecordDto> toDtoList(List<MasterFacilityRecord> source);

    @Mapping(target="countyCode", qualifiedByName = "CountyCode")
    @Mapping(target="stateCode", qualifiedByName = "FipsStateCode")
    @Mapping(target="mailingStateCode", qualifiedByName = "FipsStateCode")
    @Mapping(target="tribalCode", qualifiedByName = "TribalCode")
    @Mapping(target ="operatingStatusCode", qualifiedByName  = "OperatingStatusCode")
    @Mapping(target = "facilityCategoryCode", qualifiedByName = "FacilityCategoryCode")
    @Mapping(target = "facilitySourceTypeCode", qualifiedByName = "FacilitySourceTypeCode")
    MasterFacilityRecord fromDto(MasterFacilityRecordDto source);

    @Mapping(target="countyCode", qualifiedByName = "CountyCode")
    @Mapping(target="stateCode", qualifiedByName = "FipsStateCode")
    @Mapping(target="mailingStateCode", qualifiedByName = "FipsStateCode")
    @Mapping(target="tribalCode", qualifiedByName = "TribalCode")
    @Mapping(target ="operatingStatusCode", qualifiedByName  = "OperatingStatusCode")
    @Mapping(target = "facilityCategoryCode", qualifiedByName = "FacilityCategoryCode")
    @Mapping(target = "facilitySourceTypeCode", qualifiedByName = "FacilitySourceTypeCode")
    @Mapping(target = "masterFacilityNAICS", ignore = true)
    @Mapping(target = "masterFacilityPermits", ignore = true)
    @Mapping(target = "coordinateTolerance", ignore = true)
    void updateFromDto(MasterFacilityRecordDto source, @MappingTarget MasterFacilityRecord target);

    @Mapping(target = "id", ignore = true)
    @Mapping(source="agencyFacilityIdentifier", target="agencyFacilityIdentifier")
    MasterFacilityRecordDto facilitySiteDtoToDto(FacilitySiteDto source);

    @Mapping(target = "id", ignore = true)
    @Mapping(source="agencyFacilityIdentifier", target="agencyFacilityIdentifier")
    FacilitySite toFacilitySite(MasterFacilityRecord source);

    @Mapping(target = "id", ignore = true)
    @Mapping(source="agencyFacilityIdentifier", target="agencyFacilityIdentifier")
    MasterFacilityRecord fromFacilitySite(FacilitySite source);

    @Mapping(target = "id", ignore = true)
    @Mapping(source="agencyFacilityIdentifier", target="agencyFacilityIdentifier")
    void updateFacilitySite(MasterFacilityRecord source, @MappingTarget FacilitySite target);
}
