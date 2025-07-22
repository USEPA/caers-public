/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
