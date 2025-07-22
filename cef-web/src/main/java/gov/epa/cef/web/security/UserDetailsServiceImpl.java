/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.security;

import gov.epa.cdx.shared.security.naas.CdxHandoffPreAuthenticationUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Profile("prod")
@Service
public class UserDetailsServiceImpl extends CdxHandoffPreAuthenticationUserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SecurityService securityService;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken) {

        try {
            // Load user details from NAAS token via cdx-shared handoff code
            return super.loadUserDetails(preAuthenticatedAuthenticationToken);
        } catch (Exception e) {
            logger.error("unable to load user details: {}", e.getMessage(), e);
            throw new AuthenticationServiceException("unable to load user details");
        }
    }

    @Override
    protected Collection<GrantedAuthority> getRoles(Map<String, String> userProperties) {

        String userId = userProperties.get(USER_ID);

        List<GrantedAuthority> roles = this.securityService.createUserRoles(
            nullSafeLong(userProperties.get(ROLE_ID)), nullSafeLong(userProperties.get(USER_ROLE_ID)));

        // detailed logging of the roles from a handoff
        logger.info("User {} Roles granted: {}", userId,
            roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", ")));

        return roles;
    }
}
