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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuditorAwareImplTest extends BaseSecurityTest {

    @Mock
    SecurityService securityService;

    @InjectMocks
    private AuditorAwareImpl auditorAwareImpl;

    @Before
    public void init() {

        when(securityService.getCurrentUserId()).thenReturn("mock-user");
    }

    @Test
    public void getCurrentAuditor_Should_ReturnCurrentAuthenticatedUser_When_AuthenticatedUserAlreadyExist() {

        when(securityService.hasSecurityContext()).thenReturn(true);

        Optional<String> currentAuditor = auditorAwareImpl.getCurrentAuditor();
        assertTrue(currentAuditor.isPresent());
        assertEquals("mock-user", currentAuditor.get());
    }

    @Test
    public void getCurrentAuditor_Should_ReturnNull_When_NoAuthenticatedUserAvailable() {

        when(securityService.hasSecurityContext()).thenReturn(false);

        assertFalse(auditorAwareImpl.getCurrentAuditor().isPresent());
    }
}
