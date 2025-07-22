/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
