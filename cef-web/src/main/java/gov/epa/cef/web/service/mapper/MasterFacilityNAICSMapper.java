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
import gov.epa.cef.web.service.dto.MasterFacilityNAICSDto;

@Mapper(componentModel = "spring", uses = {})
public interface MasterFacilityNAICSMapper {
	
	@Mapping(source = "id", target = "id")
    @Mapping(source = "masterFacilityRecord.id", target = "masterFacilityRecordId")
    @Mapping(source = "naicsCode.code", target = "code")
    @Mapping(source = "naicsCode.description", target = "description")
	MasterFacilityNAICSDto mfrNAICSXrefToMfrNAICSDto(MasterFacilityNAICSXref mfrNAICS);
	
	Set<MasterFacilityNAICSDto> mfrNAICSXrefsToMfrNAICSDtos(Set<MasterFacilityNAICSXref> mfrNAICS);

	@Mapping(source = "masterFacilityRecordId", target = "masterFacilityRecord.id")
	@Mapping(source = "code", target = "naicsCode.code")
    @Mapping(source = "description", target = "naicsCode.description")
	MasterFacilityNAICSXref fromDto(MasterFacilityNAICSDto source);
	
    @Mapping(source = "masterFacilityRecordId", target = "masterFacilityRecord.id")
	MasterFacilityNAICSXref updateFromDto(MasterFacilityNAICSDto source, @MappingTarget MasterFacilityNAICSXref target);
    
    @Mapping(target = "id", ignore = true)
    MasterFacilityNAICSXref toMasterFacilityNaicsXref(FacilityNAICSXref source);
}
