/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
