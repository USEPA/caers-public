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

import gov.epa.cef.web.domain.EmissionsProcess;
import gov.epa.cef.web.domain.EmissionsUnit;
import gov.epa.cef.web.service.dto.EmissionsUnitDto;
import gov.epa.cef.web.service.dto.SideNavItemDto;
import gov.epa.cef.web.service.dto.postOrder.EmissionsUnitPostOrderDto;

@Mapper(componentModel = "spring", uses = {EmissionsProcessMapper.class, LookupEntityMapper.class})   
public interface EmissionsUnitMapper {

    @Mapping(source = "facilitySite.id", target = "facilitySiteId")
    @Mapping(source = "emissionsProcesses", target = "emissionsProcesses")
    EmissionsUnitDto emissionsUnitToDto(EmissionsUnit emissionsUnit);
    
    List<EmissionsUnitDto> emissionsUnitsToEmissionUnitsDtos(List<EmissionsUnit> emissionUnits);
    
    EmissionsUnitPostOrderDto toUpDto(EmissionsUnit emissionsUnit);
    
    @Mapping(source = "facilitySiteId", target = "facilitySite.id")
    @Mapping(source = "emissionsProcesses", target = "emissionsProcesses")
    EmissionsUnit emissionsUnitFromDto(EmissionsUnitDto emissionsUnit);
    
    @Mapping(target = "operatingStatusCode", qualifiedByName  = "OperatingStatusCode")
    @Mapping(target = "unitOfMeasureCode", qualifiedByName  = "UnitMeasureCode")
    @Mapping(target = "unitTypeCode", qualifiedByName  = "UnitTypeCode")
    void updateFromDto(EmissionsUnitDto source, @MappingTarget EmissionsUnit target);

    // mapping for emission unit side nav
    @Mapping(source = "emissionsProcessIdentifier", target = "identifier")
    @Mapping(source = "description", target = "description")
    SideNavItemDto emissionsProcessToNavDto(EmissionsProcess emissionsProcess);
    
    @Mapping(source = "emissionsProcesses", target = "children")
    @Mapping(source = "unitIdentifier", target = "identifier")
    @Mapping(source = "description", target = "description")
    SideNavItemDto emissionsUnitToNavDto(EmissionsUnit emissionsUnit);
    
    List<SideNavItemDto> unitToNavDtoList(List<EmissionsUnit> emissionsUnit);
}
