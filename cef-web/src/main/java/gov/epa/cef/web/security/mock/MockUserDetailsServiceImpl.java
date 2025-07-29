/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package gov.epa.cef.web.security.mock;

import gov.epa.cdx.shared.security.ApplicationUser;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.security.AppRole.RoleType;
import gov.epa.cef.web.security.SecurityService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Mock the CDX PreAuthenticationUserDetailsService to return a mocked user for local development
 *
 * @author ahmahfou
 */
@Profile("dev")
@Service
public class MockUserDetailsServiceImpl implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private static final Long ORGANIZATION_ID = 86819L;

    //private static final Long USER_ROLE_ID = 220632L; //Preparer
    //private static final Long USER_ROLE_ID = 220231L; //Certifier
    //private static final Long USER_ROLE_ID = 225686L; //Reviewer GA
    //private static final Long USER_ROLE_ID = 240879L; //Reviewer DC
    private static final Long USER_ROLE_ID = 238459L; //Admin

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final SecurityService securityService;

    MockUserDetailsServiceImpl(SecurityService securityService) {

        this.securityService = securityService;
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) {

        ApplicationUser user = null;

        String cdxWebUserFile = System.getProperty("cdxWebUser.properties");
        if (StringUtils.isNotBlank(cdxWebUserFile)) {
            File file = new File(cdxWebUserFile);
            if (file.exists()) {

                logger.info("Reading cdxWebUser file for authentication/authorization");
                user = readCdxWebUserFile(file);
            } else {

                logger.info("Unable to find cdxWebUserFile: {}", cdxWebUserFile);
            }
        }

        if (user == null) {
            try {

                logger.info("Using hard coded user for authentication/authorization");
                //RoleType role = AppRole.RoleType.PREPARER;
                //RoleType role = AppRole.RoleType.NEI_CERTIFIER;
                //RoleType role = AppRole.RoleType.REVIEWER;
                RoleType role = AppRole.RoleType.CAERS_ADMIN;

                String userId = "brundage.kevin";
                List<GrantedAuthority> roles = this.securityService.createUserRoles(role, USER_ROLE_ID);

                user = new ApplicationUser(userId, roles);

                user.setEmail("brundage.kevin@epa.gov");
                user.setFirstName("Kevin");
                user.setLastName("Brundage");
                user.setOrganization("Organization");
                user.setTitle("Mr.");
                user.setPhoneNumber("919-555-1234");
                user.setIdTypeCode((int) role.getId());
                user.setIdTypeText(role.roleName());
                user.setRoleId(role.getId());
                user.setUserRoleId(USER_ROLE_ID);
                user.setClientId("GADNR");
                //60632 or 95092
                user.setUserOrganizationId(ORGANIZATION_ID);

            } catch (Exception e) {
                logger.error("unable to load user details: {}", e.getMessage(), e);
                throw new AuthenticationServiceException("unable to load user details");
            }
        }

        // detailed logging of the roles from a handoff
        logger.info("User {} Roles granted: {}", user.getUserId(),
            user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", ")));

        return user;
    }

    private ApplicationUser readCdxWebUserFile(File file) {

        ApplicationUser result = null;
        try (InputStream is = new FileInputStream(file)) {

            Properties properties = new Properties();
            properties.load(is);

            String userId = properties.getProperty("userId");
            RoleType role = RoleType.fromId(Long.parseLong(properties.getProperty("roleId")));
            long userRoleId = Long.parseLong(properties.getProperty("userRoleId"));

            List<GrantedAuthority> roles = this.securityService.createUserRoles(role, userRoleId);

            result = new ApplicationUser(userId, roles);

            Collection<String> ignoreFields = Arrays.asList();
            BeanUtils.populate(result, properties.entrySet().stream()
                .filter(e -> ignoreFields.contains(e.getKey().toString()) == false)
                .collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString())));

        } catch (Exception e) {

            logger.error("Unable to load user details from file: {}", e.getMessage(), e);
            throw new AuthenticationServiceException("unable to load user details");
        }

        return result;
    }
}
