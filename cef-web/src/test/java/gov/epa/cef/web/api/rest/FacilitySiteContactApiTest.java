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
