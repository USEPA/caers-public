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
import gov.epa.cef.web.service.ReleasePointService;
import gov.epa.cef.web.service.dto.ReleasePointDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReleasePointApiTest extends BaseApiTest {

    @Mock
    private SecurityService securityService;

    @Mock
    private ReleasePointService releasePointService;

    @InjectMocks
    private ReleasePointApi releasePointApi;

    private ReleasePointDto releasePointDto;
    private List<ReleasePointDto> releasePointDtoList;

    @Before
    public void init() {
        releasePointDto=new ReleasePointDto();
        releasePointDtoList=new ArrayList<>();
        when(releasePointService.retrieveById(123L)).thenReturn(releasePointDto);
        when(releasePointService.retrieveByFacilitySiteId(1L)).thenReturn(releasePointDtoList);

        when(securityService.facilityEnforcer()).thenReturn(new ReviewerFacilityAccessEnforcerImpl());
    }

    @Test
    public void retrieveReleasePoint_Should_ReturnReleasePointObjectWithOkStatus_When_ValidIdPassed() {
        ResponseEntity<ReleasePointDto> result=releasePointApi.retrieveReleasePoint(123L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(releasePointDto, result.getBody());
    }

    @Test
    public void retrieveFacilityReleasePoints_Should_ReturnReleasePointListWithOkStatus_When_ValidFacilityIdPassed() {
        ResponseEntity<List<ReleasePointDto>> result=releasePointApi.retrieveFacilityReleasePoints(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(releasePointDtoList, result.getBody());
    }
}
