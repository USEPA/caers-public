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

import gov.epa.cef.web.domain.UserFacilityAssociation;
import gov.epa.cef.web.service.dto.UserFacilityAssociationDto;
import net.exchangenetwork.wsdl.register.streamlined._1.RegistrationNewUserProfile;

@Mapper(componentModel = "spring", uses = {})
public interface UserFacilityAssociationMapper {

    @Mapping(target = "masterFacilityRecord.masterFacilityPermits", ignore = true)
    UserFacilityAssociationDto toDto(UserFacilityAssociation source);

    @Mapping(source="regUser.user.firstName", target="firstName")
    @Mapping(source="regUser.user.lastName", target="lastName")
    @Mapping(source="regUser.organization.email", target="email")
    @Mapping(source="regUser.role.type.code", target="roleId")
    @Mapping(source="regUser.role.type.description", target="roleDescription")
    @Mapping(target = "masterFacilityRecord.masterFacilityPermits", ignore = true)
    UserFacilityAssociationDto toDto(UserFacilityAssociation source, RegistrationNewUserProfile regUser);

    List<UserFacilityAssociationDto> toDtoList(Iterable<UserFacilityAssociation> source);

}
