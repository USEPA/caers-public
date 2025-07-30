/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.mapper;

import net.exchangenetwork.wsdl.register.pdf._1.HandoffUserType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import gov.epa.cdx.shared.security.ApplicationUser;
import gov.epa.cef.web.service.dto.UserDto;

/**
 * Class that contains transforms for {@link ApplicationUser}
 * @author tfesperm
 *
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApplicationUserMapper {

    @Mapping(source="userId", target="cdxUserId")
    @Mapping(source="idTypeText", target="role")
    @Mapping(source="clientId", target="programSystemCode")
    UserDto toUserDto(ApplicationUser applicationUser);

    HandoffUserType toHandoffUser(ApplicationUser applicationUser);

}
