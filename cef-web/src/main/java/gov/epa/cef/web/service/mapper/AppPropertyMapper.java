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

import gov.epa.cef.web.domain.AdminProperty;
import gov.epa.cef.web.domain.SLTConfigProperty;
import gov.epa.cef.web.service.dto.PropertyDto;

@Mapper(componentModel = "spring", uses = {})
public interface AppPropertyMapper {

    PropertyDto toDto(AdminProperty source);

    List<PropertyDto> toDtoList(List<AdminProperty> source);

    AdminProperty fromDto(PropertyDto source);

    @Mapping(source ="sltPropertyDetails.name", target="name")
    @Mapping(source ="sltPropertyDetails.label", target="label")
    @Mapping(source ="sltPropertyDetails.description", target="description")
    @Mapping(source ="sltPropertyDetails.datatype", target="datatype")
    @Mapping(source ="sltPropertyDetails.required", target="required")
    PropertyDto sltToDto(SLTConfigProperty source);

    List<PropertyDto> sltToDtoList(List<SLTConfigProperty> source);

    SLTConfigProperty sltFromDto(PropertyDto source);
}
