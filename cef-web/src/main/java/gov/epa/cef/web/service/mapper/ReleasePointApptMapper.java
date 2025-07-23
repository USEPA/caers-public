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

import gov.epa.cef.web.domain.ReleasePointAppt;
import gov.epa.cef.web.service.dto.ReleasePointApptDto;
import gov.epa.cef.web.service.dto.postOrder.ReleasePointApptPostOrderDto;

@Mapper(componentModel = "spring", uses = {LookupEntityMapper.class})   
public interface ReleasePointApptMapper {

    List<ReleasePointApptDto> releasePointApptsToReleasePointApptDtos(List<ReleasePointAppt> releasePointAppts);
    
    ReleasePointApptPostOrderDto toUpDto(ReleasePointAppt releasePointAppt);

    @Mapping(source = "releasePointDescription", target = "releasePoint.description")
    @Mapping(source = "releasePointIdentifier", target = "releasePoint.releasePointIdentifier")
    @Mapping(source = "releasePointTypeCode", target = "releasePoint.typeCode")
    @Mapping(source = "releasePointId", target = "releasePoint.id")
    @Mapping(source = "emissionsProcessId", target = "emissionsProcess.id")
    ReleasePointAppt fromDto(ReleasePointApptDto source);
    
    @Mapping(source = "releasePoint.description", target = "releasePointDescription")
    @Mapping(source = "releasePoint.releasePointIdentifier", target = "releasePointIdentifier")
    @Mapping(source = "releasePoint.typeCode", target = "releasePointTypeCode")
    @Mapping(source = "releasePoint.id", target = "releasePointId")
    @Mapping(source = "emissionsProcess.id", target = "emissionsProcessId")
    ReleasePointApptDto toDto(ReleasePointAppt releasePointAppt);
    
    @Mapping(source = "releasePointId", target = "releasePoint.id")
    void updateFromDto(ReleasePointApptDto source, @MappingTarget ReleasePointAppt target);
}
