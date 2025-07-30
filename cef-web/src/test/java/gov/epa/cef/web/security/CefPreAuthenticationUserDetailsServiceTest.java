/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CefPreAuthenticationUserDetailsServiceTest extends BaseSecurityTest {

    @Mock
    SecurityService securityService;

    @InjectMocks
    UserDetailsServiceImpl cefPreAuthenticationUserDetailsService;

    @Before
    public void init() {

        when(securityService.createUserRoles(142710L, 1111142710L))
            .thenReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_Preparer")));
    }

    @Test
    public void getRoles_Should_ReturnListOfAuthorities_When_ValidRoleIdPassed() {
        Map<String, String> userProperties=new HashMap<>();
        userProperties.put("uid", "FRED");
        userProperties.put("UserRoleId", "1111142710");
        userProperties.put("RoleId", "142710");
        Collection<GrantedAuthority> authorities =
            cefPreAuthenticationUserDetailsService.getRoles(userProperties);
        assertEquals(1, authorities.size());
        assertEquals("ROLE_Preparer", authorities.iterator().next().getAuthority());
    }

    @Test
    public void getRoles_Should_ReturnEmptyList_When_InvalidRoleIdPassed() {
        Map<String, String> userProperties=new HashMap<>();
        userProperties.put("RoleId", "124");
        Collection<GrantedAuthority> authorities =
            cefPreAuthenticationUserDetailsService.getRoles(userProperties);
        assertEquals(0, authorities.size());
    }
}
