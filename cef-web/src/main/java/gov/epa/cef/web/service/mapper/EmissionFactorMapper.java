/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import gov.epa.cef.web.domain.GHGEmissionFactor;
import gov.epa.cef.web.domain.WebfireEmissionFactor;
import gov.epa.cef.web.domain.SLTEmissionFactor;
import gov.epa.cef.web.service.dto.EmissionFactorDto;

@Mapper(componentModel = "spring")
public interface EmissionFactorMapper {

    EmissionFactorDto toWebfireEfDto(WebfireEmissionFactor source);
    EmissionFactorDto toSltEfDto(SLTEmissionFactor source);

    List<EmissionFactorDto> toWebfireEfDtoList(Collection<WebfireEmissionFactor> source);
    List<EmissionFactorDto> toSltEfDtoList(Collection<SLTEmissionFactor> source);

    @Mapping(source="id", target="ghgId")
    EmissionFactorDto toGHGEfDto(GHGEmissionFactor source);

    List<EmissionFactorDto> toGHGEfDtoList(Collection<GHGEmissionFactor> source);

    WebfireEmissionFactor webfireEfFromDto(EmissionFactorDto source);
    SLTEmissionFactor sltEfFromDto(EmissionFactorDto source);

    @Mapping(source="ghgId", target="id")
    GHGEmissionFactor ghgEfFromDto(EmissionFactorDto source);

}
