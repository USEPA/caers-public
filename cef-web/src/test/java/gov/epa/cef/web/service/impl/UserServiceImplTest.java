/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.impl;

import gov.epa.cdx.shared.security.ApplicationUser;
import gov.epa.cef.web.client.soap.SecurityToken;
import gov.epa.cef.web.config.CdxConfig;
import gov.epa.cef.web.config.CefConfig;
import gov.epa.cef.web.exception.ApplicationException;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.dto.TokenDto;
import gov.epa.cef.web.service.dto.UserDto;
import gov.epa.cef.web.service.mapper.ApplicationUserMapper;
import gov.epa.cef.web.client.soap.SecurityTokenClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceImplTest extends BaseServiceTest {

    @Mock
    SecurityTokenClient tokenClient;

    @Mock
    private CefConfig cefConfig;

    @Mock
    private SecurityService securityService;

    @Mock
    private CdxConfig cdxConfig;

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    private ApplicationUserMapper applicationUserMapper;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Before
    public void init() {
        when(securityService.getCurrentApplicationUser()).thenReturn(applicationUser);
        when(securityService.getCurrentApplicationUser().getUserId()).thenReturn("mock-user");
        when(securityService.getCurrentApplicationUser().getUserRoleId()).thenReturn(123L);
        when(cefConfig.getCdxConfig()).thenReturn(cdxConfig);
        when(cefConfig.getCdxConfig().getNaasUser()).thenReturn("naas-user");
        when(cefConfig.getCdxConfig().getNaasPassword()).thenReturn("naas-password");
        when(cefConfig.getCdxConfig().getNaasIp()).thenReturn("127.0.0.1");
        when(cefConfig.getCdxConfig().getCdxBaseUrl()).thenReturn("http://cdx-url");
    }


    @Test
    public void createToken_Should_ReturnValidToken_When_ValidUserIdPassed() throws MalformedURLException {
        when(tokenClient.createSecurityToken("mock-user", "127.0.0.1")).thenReturn(new SecurityToken().withToken("token"));
        when(cefConfig.getCdxConfig().getNaasTokenUrl()).thenReturn(new URL("http://mockurl"));
        TokenDto token=userServiceImpl.createToken();
        assertEquals("token", token.getToken());
        assertEquals(new Long(123), token.getUserRoleId());
        assertEquals("token", token.getToken());
        assertEquals("http://cdx-url", token.getBaseServiceUrl());
    }

    @Test(expected=ApplicationException.class)
    public void createToken_Should_ThrowException_When_ConfigurationMissing() throws MalformedURLException {
        when(cefConfig.getCdxConfig().getNaasTokenUrl()).thenReturn(null);
        userServiceImpl.createToken();
    }

    @Test
    public void getCurrentUser_Should_ReturnUserDtoObject_WhenAuthenticatedUserAlreadyExist(){
        UserDto userDto=userServiceImpl.getCurrentUser();
        when(applicationUserMapper.toUserDto(applicationUser)).thenReturn(userDto);
        assertEquals(userDto, userServiceImpl.getCurrentUser());
    }
}
