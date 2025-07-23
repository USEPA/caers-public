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

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import gov.epa.cef.web.domain.FacilityNAICSXref;
import gov.epa.cef.web.domain.MasterFacilityNAICSXref;
import gov.epa.cef.web.service.dto.FacilityNAICSDto;

@Mapper(componentModel = "spring", uses = {})
public interface FacilityNAICSMapper {
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "facilitySite.id", target = "facilitySiteId")
    @Mapping(source = "naicsCode.code", target = "code")
    @Mapping(source = "naicsCode.description", target = "description")
    FacilityNAICSDto facilityNAICSXrefToFacilityNAICSDto(FacilityNAICSXref facilityNAICS);
    
    Set<FacilityNAICSDto> facilityNAICSXrefsToFacilityNAICSDtos(Set<FacilityNAICSXref> facilityNAICSs);

    @Mapping(source = "facilitySiteId", target = "facilitySite.id")
    @Mapping(source = "code", target = "naicsCode.code")
    @Mapping(source = "description", target = "naicsCode.description")
    FacilityNAICSXref fromDto(FacilityNAICSDto source);

    @Mapping(source = "facilitySiteId", target = "facilitySite.id")
    FacilityNAICSXref updateFromDto(FacilityNAICSDto source, @MappingTarget FacilityNAICSXref target);
    
    FacilityNAICSXref toFacilityNaicsXref(MasterFacilityNAICSXref source);
    
}
