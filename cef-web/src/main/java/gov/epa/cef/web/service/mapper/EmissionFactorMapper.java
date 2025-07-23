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
