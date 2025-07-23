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

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import gov.epa.cef.web.domain.ControlPathPollutant;
import gov.epa.cef.web.service.dto.ControlPathPollutantDto;

@Mapper(componentModel = "spring", uses = {LookupEntityMapper.class})
public interface ControlPathPollutantMapper {
	
	@Mapping(source="controlPath.id", target="controlPathId")
	ControlPathPollutantDto toDto(ControlPathPollutant source);

    @Mapping(source="controlPathId", target="controlPath.id")
    ControlPathPollutant fromDto(ControlPathPollutantDto source);

    @Mapping(source = "controlPathId", target = "controlPath.id")
    void updateFromDto(ControlPathPollutantDto source, @MappingTarget ControlPathPollutant target);

}
