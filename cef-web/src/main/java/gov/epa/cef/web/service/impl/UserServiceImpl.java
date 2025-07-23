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
package gov.epa.cef.web.service.impl;

import gov.epa.cdx.shared.security.ApplicationUser;
import gov.epa.cef.web.client.soap.SecurityTokenClient;
import gov.epa.cef.web.config.CefConfig;
import gov.epa.cef.web.exception.ApplicationException;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.UserService;
import gov.epa.cef.web.service.dto.TokenDto;
import gov.epa.cef.web.service.dto.UserDto;
import gov.epa.cef.web.service.mapper.ApplicationUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private SecurityTokenClient tokenClient;

    @Autowired
    private CefConfig cefConfig;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ApplicationUserMapper applicationUserMapper;


    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.UserService#createToken(java.lang.String)
     */
    @Override
    public TokenDto createToken() {
        TokenDto tokenDto=new TokenDto();
        try {
            Long userRoleId= securityService.getCurrentApplicationUser().getUserRoleId();
            String userId= securityService.getCurrentApplicationUser().getUserId();
            String token = tokenClient.createSecurityToken(userId, cefConfig.getCdxConfig().getNaasIp()).getToken();
            tokenDto=new TokenDto();
            tokenDto.setToken(token);
            tokenDto.setUserRoleId(userRoleId);
            tokenDto.setBaseServiceUrl(cefConfig.getCdxConfig().getCdxBaseUrl());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }

        return tokenDto;
    }

    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.UserService#getCurrentUser()
     */
    @Override
    public UserDto getCurrentUser() {
        ApplicationUser applicationUser= securityService.getCurrentApplicationUser();
        return applicationUserMapper.toUserDto(applicationUser);
    }

}
