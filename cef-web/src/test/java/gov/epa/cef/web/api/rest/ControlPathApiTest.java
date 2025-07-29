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
import gov.epa.cef.web.service.ControlPathService;
import gov.epa.cef.web.service.dto.ControlPathDto;
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
public class ControlPathApiTest extends BaseApiTest {

    @Mock
    private ControlPathService controlService;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private ControlPathApi controlPathApi;

    @Mock
    private FacilityAccessEnforcerImpl facilityAccessEnforcer;

    private ControlPathDto controlPath = new ControlPathDto();

    private List<ControlPathDto> controlPathList;

    @Before
    public void init() {
        controlPath = new ControlPathDto();
        when(controlService.retrieveById(123L)).thenReturn(controlPath);

        when(securityService.facilityEnforcer()).thenReturn(facilityAccessEnforcer);

        controlPathList = new ArrayList<>();
        controlPathList.add(controlPath);
        when(controlService.retrieveForFacilitySite(1L)).thenReturn(controlPathList);
    }

    @Test
    public void retrieveControlPath_Should_ReturnControlObjectWithOkStatusCode_When_ValidIdPassed() {
        ResponseEntity<ControlPathDto> result = controlPathApi.retrieveControlPath(123L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(controlPath, result.getBody());
    }

    @Test
    public void retrieveControlPathsForFacilitySitet_Should_ReturnControlListWithStatusOk_When_ValidFacilitySiteIdPassed() {
        ResponseEntity<List<ControlPathDto>> result = controlPathApi.retrieveControlPathsForFacilitySite(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(controlPathList, result.getBody());
    }

}
