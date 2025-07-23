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