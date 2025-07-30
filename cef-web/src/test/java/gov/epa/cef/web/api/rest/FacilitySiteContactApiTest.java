/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.api.rest;

import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.security.enforcer.ReviewerFacilityAccessEnforcerImpl;
import gov.epa.cef.web.service.FacilitySiteContactService;
import gov.epa.cef.web.service.dto.FacilitySiteContactDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FacilitySiteContactApiTest extends BaseApiTest {

    @Mock
    private SecurityService securityService;

    @Mock
    private FacilitySiteContactService facilitySiteContactService;

    @InjectMocks
    private FacilitySiteContactApi facilitySiteContactApi;


    private FacilitySiteContactDto facilitySiteContact;

    private List<FacilitySiteContactDto> facilitySiteContacts;

    @Before
    public void init() {
        facilitySiteContact=new FacilitySiteContactDto();
        facilitySiteContacts=new ArrayList<>();

        when(facilitySiteContactService.retrieveById(123L)).thenReturn(facilitySiteContact);
        when(facilitySiteContactService.retrieveForFacilitySite(1L)).thenReturn(facilitySiteContacts);

        when(securityService.facilityEnforcer()).thenReturn(new ReviewerFacilityAccessEnforcerImpl());
    }

    @Test
    public void retrieveContact_Should_ReturnFacilitySiteContactObjectWithOkStatus_When_ValidIdPassed() {
        ResponseEntity<FacilitySiteContactDto> result=facilitySiteContactApi.retrieveContact(123L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(facilitySiteContact, result.getBody());
    }

    @Test
    public void retrieveContactsForFacility_Should_ReturnFacilitySiteContactsListWithOkStatus_When_ValidFacilityIdPassed() {
        ResponseEntity<Collection<FacilitySiteContactDto>> result=facilitySiteContactApi.retrieveContactsForFacility(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(facilitySiteContacts, result.getBody());
    }
}
