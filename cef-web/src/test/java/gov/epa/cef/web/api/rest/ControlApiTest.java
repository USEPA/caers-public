/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.api.rest;

import gov.epa.cef.web.security.enforcer.FacilityAccessEnforcerImpl;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.ControlService;
import gov.epa.cef.web.service.dto.postOrder.ControlPostOrderDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ControlApiTest extends BaseApiTest {

    @Mock
    private ControlService controlService;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private ControlApi controlApi;

    @Mock
    private FacilityAccessEnforcerImpl facilityAccessEnforcer;

    private ControlPostOrderDto control = new ControlPostOrderDto();

    private List<ControlPostOrderDto> controlList;

    @Before
    public void init() {
        control = new ControlPostOrderDto();
        when(controlService.retrieveById(123L)).thenReturn(control);

        when(securityService.facilityEnforcer()).thenReturn(facilityAccessEnforcer);

        controlList = new ArrayList<>();
        controlList.add(control);
        when(controlService.retrieveForFacilitySite(1L)).thenReturn(controlList);
    }

    @Test
    public void retrieveControl_Should_ReturnControlObjectWithOkStatusCode_When_ValidIdPassed() {
        ResponseEntity<ControlPostOrderDto> result = controlApi.retrieveControl(123L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(control, result.getBody());
    }

    @Test
    public void retrieveControlsForFacilitySitet_Should_ReturnControlListWithStatusOk_When_ValidFacilitySiteIdPassed() {
        ResponseEntity<List<ControlPostOrderDto>> result = controlApi.retrieveControlsForFacilitySite(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(controlList, result.getBody());
    }

}
