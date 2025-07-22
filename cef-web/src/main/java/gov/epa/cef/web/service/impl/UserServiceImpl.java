/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
