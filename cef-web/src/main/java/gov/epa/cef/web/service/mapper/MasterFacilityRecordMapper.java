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
