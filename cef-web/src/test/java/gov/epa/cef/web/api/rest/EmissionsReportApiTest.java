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
import gov.epa.cef.web.service.EmissionsReportService;
import gov.epa.cef.web.service.dto.EmissionsReportDto;
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

@RunWith(MockitoJUnitRunner.Silent.class)
public class EmissionsReportApiTest extends BaseApiTest {
    @Mock
    private SecurityService securityService;

    @Mock
    private EmissionsReportService emissionsReportService;

    @InjectMocks
    private EmissionsReportApi emissionsReportApi;

    private EmissionsReportDto emissionsReportDto;

    private List<EmissionsReportDto> emissionsReportDtoList;

    @Before
    public void init() {
        emissionsReportDto=new EmissionsReportDto();
        emissionsReportDtoList=new ArrayList<>();
        when(emissionsReportService.findById(1L)).thenReturn(emissionsReportDto);
        when(emissionsReportService.findByMasterFacilityRecordId(1234L)).thenReturn(emissionsReportDtoList);
        when(emissionsReportService.findMostRecentByMasterFacilityRecordId(1234L)).thenReturn(emissionsReportDto);

        when(securityService.facilityEnforcer()).thenReturn(new ReviewerFacilityAccessEnforcerImpl());
    }

    @Test
    public void retrieveReport_Should_ReturnEmissionsReportObjectWithOkStatus_When_ValidIdPassed() {
        ResponseEntity<EmissionsReportDto> result=emissionsReportApi.retrieveReport(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(emissionsReportDto, result.getBody());
    }

    @Test
    public void retrieveReportsForFacility_Should_ReturnEmissionsReportListWithStatusOk_When_ValidFacilityProgramIdPassed() {
        ResponseEntity<List<EmissionsReportDto>> result=emissionsReportApi.retrieveReportsForFacility(1234L, false);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(emissionsReportDtoList, result.getBody());
    }

    @Test
    public void retrieveCurrentReportForFacility_Should_ReturnCurrentEmissionsReportWithStatusOk_When_ValidFacilityProgramIdPassed() {
        ResponseEntity<EmissionsReportDto> result=emissionsReportApi.retrieveCurrentReportForFacility(1234L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(emissionsReportDto, result.getBody());
    }
}
