/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.impl;

import gov.epa.cef.web.domain.FacilitySiteContact;
import gov.epa.cef.web.repository.FacilitySiteContactRepository;
import gov.epa.cef.web.service.dto.FacilitySiteContactDto;
import gov.epa.cef.web.service.mapper.FacilitySiteContactMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FacilitySiteContactServiceImplTest extends BaseServiceTest {

    @Mock
    private FacilitySiteContactRepository contactRepo;

    @Mock
    private FacilitySiteContactMapper facilitySiteContactMapper;

    @InjectMocks
    private FacilitySiteContactServiceImpl facilitySiteContactServiceImpl;

    @Before
    public void init(){
        FacilitySiteContact facilitySiteContact = new FacilitySiteContact();
        List<FacilitySiteContact> contactList = new ArrayList<FacilitySiteContact>();
        List<FacilitySiteContact> emptyContactList = new ArrayList<FacilitySiteContact>();
        contactList.add(facilitySiteContact);
        when(facilitySiteContactMapper.toDto(null)).thenReturn(null);
        when(facilitySiteContactMapper.toDto(facilitySiteContact)).thenReturn(new FacilitySiteContactDto());
        when(contactRepo.findById(1L)).thenReturn(Optional.of(facilitySiteContact));
        when(contactRepo.findById(2L)).thenReturn(Optional.empty());
        when(contactRepo.findByFacilitySiteId(1L)).thenReturn(contactList);
        when(contactRepo.findByFacilitySiteId(2L)).thenReturn(emptyContactList);
    }

    @Test
    public void retrieveById_Should_Return_FacilitySiteContactDtoObject_When_FacilityContactExist(){
        FacilitySiteContactDto facilitySiteContactDto=facilitySiteContactServiceImpl.retrieveById(1L);
        assertNotEquals(null, facilitySiteContactDto);
    }

    @Test
    public void retrieveById_Should_Return_Null_When_FacilityContactDoesNotExist(){
        FacilitySiteContactDto facilitySiteContactDto=facilitySiteContactServiceImpl.retrieveById(2L);
        assertEquals(null, facilitySiteContactDto);
    }

    @Test
    public void retrieveById_Should_Return_Null_When_IDisNull(){
        FacilitySiteContactDto facilitySiteContactDto=facilitySiteContactServiceImpl.retrieveById(null);
        assertEquals(null, facilitySiteContactDto);
    }

    @Test
    public void retrieveForFacilitySite_Should_Return_ContactList_WhenContactsExist() {
        List<FacilitySiteContactDto> facilitySiteContactList = facilitySiteContactServiceImpl.retrieveForFacilitySite(1L);
        assertNotEquals(null, facilitySiteContactList);
    }

    @Test
    public void retrieveForFacilitySite_Should_Return_Empty_WhenContactsDoNotExist() {
        List<FacilitySiteContactDto> facilitySiteContactList = facilitySiteContactServiceImpl.retrieveForFacilitySite(2L);
        assertEquals(new ArrayList<FacilitySiteContactDto>(), facilitySiteContactList);
    }
}
