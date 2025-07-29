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
import gov.epa.cef.web.service.FacilitySiteService;
import gov.epa.cef.web.service.dto.FacilitySiteDto;
import gov.epa.cef.web.service.mapper.FacilitySiteMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class FacilitySiteApiTest extends BaseApiTest {

    @Mock
    private FacilitySiteService facilityService;

    @Mock
    private FacilitySiteMapper facilitySiteMapper;

    @Mock
    private SecurityService securityService;

    @Mock
    private FacilityAccessEnforcerImpl facilityAccessEnforcer;

    @InjectMocks
    private FacilitySiteApi facilitySiteApi;

    private FacilitySiteDto facilitSiteDto;

    @Before
    public void init() {
        facilitSiteDto=new FacilitySiteDto();
        when(facilityService.findByIdAndReportYear(123L, (short) 2022)).thenReturn(facilitSiteDto);
        when(facilityService.findByReportId(1L)).thenReturn(facilitSiteDto);

        when(securityService.facilityEnforcer()).thenReturn(facilityAccessEnforcer);
    }

    @Test
    public void retrieveFacilitySite_Should_ReturnFacilitySiteDtoObject_When_ValidIdPassed() {
        ResponseEntity<FacilitySiteDto> result=facilitySiteApi.retrieveFacilitySiteByIdAndReportYear(123L, (short) 2022);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(facilitSiteDto, result.getBody());
    }

    @Test
    public void retrieveFacilitySiteByProgramIdAndReportId_Should_ReturnFacilitySiteDto_When_ValidProgramIdAndReportIdPassed() {
        ResponseEntity<FacilitySiteDto> result=facilitySiteApi.retrieveFacilitySiteByReportId(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(facilitSiteDto, result.getBody());

    }

}
