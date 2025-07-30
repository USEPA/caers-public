/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.security;


import gov.epa.cdx.shared.security.ApplicationUser;
import gov.epa.cef.web.config.CacheName;
import gov.epa.cef.web.domain.MasterFacilityRecord;
import gov.epa.cef.web.domain.UserFacilityAssociation;
import gov.epa.cef.web.exception.ApplicationErrorCode;
import gov.epa.cef.web.exception.ApplicationException;
import gov.epa.cef.web.repository.UserFacilityAssociationRepository;
import gov.epa.cef.web.security.enforcer.FacilityAccessEnforcer;
import gov.epa.cef.web.security.enforcer.FacilityAccessEnforcerImpl;
import gov.epa.cef.web.security.enforcer.ProgramIdRepoLocator;
import gov.epa.cef.web.security.enforcer.ReviewerFacilityAccessEnforcerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SecurityService {

    private static final String FACILITY_ROLE_PREFIX = "{EIS}";

    private final CacheManager cacheManager;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ProgramIdRepoLocator programIdRepoLocator;

    private final UserFacilityAssociationRepository ufaRepo;

    @Autowired
    SecurityService(UserFacilityAssociationRepository ufaRepo,
                    CacheManager cacheManager,
                    ProgramIdRepoLocator programIdRepoLocator) {

        this.ufaRepo = ufaRepo;

        this.cacheManager = cacheManager;

        this.programIdRepoLocator = programIdRepoLocator;

    }

    public List<GrantedAuthority> createUserRoles(AppRole.RoleType role, Long userRoleId) {

        List<GrantedAuthority> roles = new ArrayList<>();

        if (role != null) {
            roles.add(new SimpleGrantedAuthority(role.grantedRoleName()));
        } else {

            logger.warn("RoleId is null.");
        }

        if (userRoleId != null) {
            try {
                roles.addAll(this.ufaRepo.findByUserRoleId(userRoleId).stream()
                    .map(UserFacilityAssociation::getMasterFacilityRecord)
                    .map(new SecurityService.FacilityToRoleTransform())
                    .collect(Collectors.toList()));
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        } else {

            logger.warn("UserRoleId is null.");
        }

        return roles;
    }

    public void evictUserCachedItems() {

        if (hasSecurityContext()) {

            Long userRoleId = getCurrentApplicationUser().getUserRoleId();
            evictUserCachedItems(userRoleId);

        } else {

            logger.warn("No user logged in. No cache items were evicted.");
        }
    }

    public FacilityAccessEnforcer facilityEnforcer() {

        if (hasRole(AppRole.RoleType.REVIEWER)||hasRole(AppRole.RoleType.CAERS_ADMIN)) {

            return new ReviewerFacilityAccessEnforcerImpl();
        }

        return new FacilityAccessEnforcerImpl(this.programIdRepoLocator, getCurrentUserMasterFacilityIds());
    }

    public ApplicationUser getCurrentApplicationUser() {

        return (ApplicationUser) getCurrentPrincipal();
    }

    public String getCurrentUserId() {

        return getCurrentApplicationUser().getUsername();
    }

    public String getCurrentProgramSystemCode() {

        return getCurrentApplicationUser().getClientId();
    }

    /**
     * Check if the current user has any of the provided roles
     *
     * @param role
     * @return
     */
    public boolean hasRole(AppRole.RoleType role) {

        return getCurrentRoles().stream().anyMatch(r -> r.getAuthority().equals(role.grantedRoleName()));
    }

    public List<String> getSltRoles() {

        return getCurrentRoles().stream()
            .filter(r -> r.getAuthority().startsWith("SLT_"))
            .map(r -> r.getAuthority().substring(4))
            .collect(Collectors.toList());
    }

    public boolean hasSecurityContext() {

        return SecurityContextHolder.getContext() != null
            && SecurityContextHolder.getContext().getAuthentication() != null
            && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null;
    }

    List<GrantedAuthority> createUserRoles(Long roleId, Long userRoleId) {

        AppRole.RoleType role = null;
        if (roleId != null) {
            try {
                role = AppRole.RoleType.fromId(roleId);
            } catch (IllegalArgumentException e) {
                logger.warn(e.getMessage());
            }
        } else {

            logger.warn("RoleId is null.");
        }

        return createUserRoles(role, userRoleId);
    }

    public void evictUserCachedItems(long userRoleId) {

        this.cacheManager.getCache(CacheName.UserProgramFacilities).evict(userRoleId);
        this.cacheManager.getCache(CacheName.UserMasterFacilityIds).evict(userRoleId);

        logger.info("Program Facilities for UserRoleId-[{}] were evicted from cache.", userRoleId);
    }

    ApplicationUser getCurrentApplicationUser(Authentication authentication) {

        return (ApplicationUser) getCurrentPrincipal();
    }

    /**
     * Check if the security context is empty
     *
     * @throws ApplicationException
     */
    private void checkSecurityContext() {

        if (hasSecurityContext() == false) {
            throw new ApplicationException(
                ApplicationErrorCode.E_AUTHORIZATION,
                "Security Context, authentication or principal is empty.");
        }
    }

    /**
     * Return the current security principal
     *
     * @return
     */
    private Object getCurrentPrincipal() {
        // checking security context
        checkSecurityContext();
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Get the security roles of the current user
     *
     * @return
     */
    private Collection<? extends GrantedAuthority> getCurrentRoles() {

        checkSecurityContext();
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    private List<Long> getCurrentUserMasterFacilityIds() {

        return this.ufaRepo.retrieveMasterFacilityRecordIds(getCurrentApplicationUser().getUserRoleId());
    }

    static class FacilityToRoleTransform implements Function<MasterFacilityRecord, GrantedAuthority> {

        @Override
        public GrantedAuthority apply(MasterFacilityRecord mfr) {

            return new SimpleGrantedAuthority(
                String.format("%s%d", FACILITY_ROLE_PREFIX, mfr.getId()));
        }
    }
}
