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

import gov.epa.cef.web.domain.EmissionsByFacilityAndCAS;
import gov.epa.cef.web.service.dto.EmissionsByFacilityAndCASDto;

@Mapper(componentModel = "spring", uses = {})
public interface EmissionsByFacilityAndCASMapper {
    
    @Mapping(source="pollutantCasId", target="casNumber")
    @Mapping(source="pollutantName", target="chemical")
    @Mapping(source="emissionsUomCode", target="uom")
    @Mapping(source="status", target="reportStatus")
    EmissionsByFacilityAndCASDto toDto(EmissionsByFacilityAndCAS emissionsByFacilityAndCAS);

}
