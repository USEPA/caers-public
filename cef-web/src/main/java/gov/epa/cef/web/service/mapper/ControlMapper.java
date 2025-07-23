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

import gov.epa.cef.web.domain.Control;
import gov.epa.cef.web.service.dto.ControlDto;
import gov.epa.cef.web.service.dto.EmissionsReportItemDto;
import gov.epa.cef.web.service.dto.postOrder.ControlPostOrderDto;

@Mapper(componentModel = "spring", uses = {LookupEntityMapper.class})
public interface ControlMapper {

    @Mapping(source="facilitySite.id", target="facilitySiteId")
    ControlPostOrderDto toDto(Control source);

    List<ControlPostOrderDto> toDtoList(List<Control> source);

    EmissionsReportItemDto toReportItemDto(Control source);
    
    @Mapping(source="facilitySiteId", target="facilitySite.id")
    Control fromDto(ControlDto source);
    
    @Mapping(target="operatingStatusCode", qualifiedByName="OperatingStatusCode")
    @Mapping(target="controlMeasureCode", qualifiedByName="ControlMeasureCode")
    void updateFromDto(ControlDto source, @MappingTarget Control target);
}
