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
