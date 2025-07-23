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
package gov.epa.cef.web.security;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author tfesperm
 *
 */
public class AppRole {
    private static final int ROLE_NAME_START_INDEX = 5;

    public enum RoleType {
        PREPARER(142710L, ROLE_PREPARER),
        NEI_CERTIFIER(142720L, ROLE_NEI_CERTIFIER),
        REVIEWER(142730L, ROLE_REVIEWER),
        CAERS_ADMIN(142740L, ROLE_CAERS_ADMIN),
        ADMIN(-1, ROLE_ADMIN),
        UNKNOWN(-9999, "UNKNOWN");

        private final long id;

        private final String roleName;

        RoleType(long id, String roleName) {

            this.id = id;
            this.roleName = roleName;
        }

        /**
         * Get the role type for the given id
         * @param id
         * @return
         */
        public static RoleType fromId(long id) {

            RoleType result = null;

            for (RoleType roleType : RoleType.values()) {
                if (roleType.id == id) {
                    result = roleType;
                }
            }

            if (result == null) {

                String msg = String.format("Role ID %d is not valid.", id);
                throw new IllegalArgumentException(msg);
            }

            return result;
        }

        /**
         * Get the role type from the given name
         * @param roleName
         * @return
         */
        public static RoleType fromRoleName(String roleName) {

            RoleType result = RoleType.UNKNOWN;

            if (StringUtils.isNotEmpty(roleName)) {

                for (RoleType roleType : RoleType.values()) {
                    if (roleType.isSameRoleName(roleName)) {
                        result = roleType;
                    }
                }
            }

            return result;
        }

        /**
         * Get this role as a RegistrationRoleType
         * @return
         */
        public net.exchangenetwork.wsdl.register.program_facility._1.RegistrationRoleType facilityRole() {

            net.exchangenetwork.wsdl.register.program_facility._1.RegistrationRoleType result = new net.exchangenetwork.wsdl.register.program_facility._1.RegistrationRoleType();
            result.setCode(this.id);

            return result;
        }

        public long getId() {

            return this.id;
        }

        /**
         * Get this role's name with ROLE_ at the beginning
         * @return
         */
        public String grantedRoleName() {

            return this.roleName;
        }

        /**
         * Check if this role is one of the provided roles
         * @param primary
         * @param secondaries
         * @return
         */
        public boolean isOneOf(RoleType primary, RoleType... secondaries) {

            boolean result = equals(primary);
            if (!result && secondaries != null) {

                for (RoleType test : secondaries) {
                    if (equals(test)) {
                        result = true;
                        break;
                    }
                }
            }

            return result;
        }

        public boolean isSameRoleName(String role) {
            return this.roleName.substring(ROLE_NAME_START_INDEX).equals(role);
        }

        /**
         * Return all roles
         * @return
         */
        public static Collection<RoleType> cdxRoles() {

            return Stream.of(values())
                    .filter(r -> r.getId() > 0)
                    .collect(Collectors.toList());
        }

        /**
         * Get this role's name without ROLE_ at the beginning
         * @return
         */
        public String roleName() {

            return this.roleName.substring(ROLE_NAME_START_INDEX);
        }
    }

    public static final String ROLE_PREPARER = "ROLE_Preparer";
    public static final String ROLE_NEI_CERTIFIER = "ROLE_NEI Certifier";
    public static final String ROLE_REVIEWER = "ROLE_Reviewer";
    public static final String ROLE_CAERS_ADMIN = "ROLE_CAERS Administrator";
    public static final String ROLE_ADMIN = "ROLE_Admin";

    public static final String PREPARER = "PREPARER";
    public static final String NEI_CERTIFIER = "NEI_CERTIFIER";
    public static final String REVIEWER = "REVIEWER";
    public static final String CAERS_ADMIN = "CAERS_ADMIN";
    public static final String ADMIN = "ADMIN";

}
