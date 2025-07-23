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

import gov.epa.cef.web.security.AppRole.RoleType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(MockitoJUnitRunner.class)
public class AppRoleTest extends BaseSecurityTest {


    @Test
    public void testRoleNames_ShouldReturn_TheValidRolesNames(){
        assertEquals("ROLE_Preparer", AppRole.ROLE_PREPARER);
        assertEquals("ROLE_NEI Certifier", AppRole.ROLE_NEI_CERTIFIER);
        assertEquals("ROLE_Reviewer", AppRole.ROLE_REVIEWER);
        assertEquals("ROLE_CAERS Administrator", AppRole.ROLE_CAERS_ADMIN);
    }

    @Test
    public void RoleType_getFormId_Should_ReturnValidRoleType_WhenTheCorrespondingIdPassed() {
        assertEquals(RoleType.PREPARER, RoleType.fromId(142710L));
        assertEquals(RoleType.NEI_CERTIFIER, RoleType.fromId(142720L));
        assertEquals(RoleType.REVIEWER, RoleType.fromId(142730L));
        assertEquals(RoleType.CAERS_ADMIN, RoleType.fromId(142740L));
    }

    @Test(expected=IllegalArgumentException.class)
    public void RoleType_getFormId_Should_ThrowException_WhenTheInvalidIddPassed() {
       RoleType.fromId(142L);
    }

    @Test
    public void RoleType_fromRoleName_Should_ReturnValidRoleType_WhenTheCorrespondingNamePassed() {
        assertEquals(RoleType.PREPARER, RoleType.fromRoleName("Preparer"));
        assertEquals(RoleType.NEI_CERTIFIER, RoleType.fromRoleName("NEI Certifier"));
        assertEquals(RoleType.REVIEWER, RoleType.fromRoleName("Reviewer"));
        assertEquals(RoleType.CAERS_ADMIN, RoleType.fromRoleName("CAERS Administrator"));
        assertEquals(RoleType.UNKNOWN, RoleType.fromRoleName(""));
    }

    @Test
    public void RoleType_grantedRoleName_Should_ReturnGrantedRoleName() {
        assertEquals("ROLE_Preparer", RoleType.PREPARER.grantedRoleName());
        assertEquals("ROLE_NEI Certifier", RoleType.NEI_CERTIFIER.grantedRoleName());
        assertEquals("ROLE_Reviewer", RoleType.REVIEWER.grantedRoleName());
        assertEquals("ROLE_CAERS Administrator", RoleType.CAERS_ADMIN.grantedRoleName());
    }

    @Test
    public void RoleType_facilityRole_Should_ReturnRegistrationRoleType() {
        assertNotEquals(null, RoleType.PREPARER.facilityRole());
        assertNotEquals(null, RoleType.NEI_CERTIFIER.facilityRole());
        assertNotEquals(null, RoleType.REVIEWER.facilityRole());
        assertNotEquals(null, RoleType.CAERS_ADMIN.facilityRole());
    }

    @Test
    public void RoleType_getId_Should_ReturnTheCorrspondingIdForTheRoleType() {
        assertEquals(142710L, RoleType.PREPARER.getId());
        assertEquals(142720L, RoleType.NEI_CERTIFIER.getId());
        assertEquals(142730L, RoleType.REVIEWER.getId());
        assertEquals(142740L, RoleType.CAERS_ADMIN.getId());
    }

    @Test
    public void RoleType_isOneOf_Should_ReturnTrue_When_TheRoleTypeIsPartOfThePassedOnes() {
        assertEquals(Boolean.TRUE, RoleType.PREPARER.isOneOf(RoleType.NEI_CERTIFIER, RoleType.REVIEWER, RoleType.PREPARER));
    }

    @Test
    public void RoleType_roleName_Should_ReturnRoleName() {
        assertEquals("Preparer", RoleType.PREPARER.roleName());
        assertEquals("NEI Certifier", RoleType.NEI_CERTIFIER.roleName());
        assertEquals("Reviewer", RoleType.REVIEWER.roleName());
        assertEquals("CAERS Administrator", RoleType.CAERS_ADMIN.roleName());
    }

    @Test
    public void RoleType_cdxRoles_Should_ReturnListOfThreeCdxRoles() {
        assertEquals(4, RoleType.PREPARER.cdxRoles().size());
    }
}
