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
import gov.epa.cef.web.service.EmissionsProcessService;
import gov.epa.cef.web.service.dto.EmissionsProcessDto;
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
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmissionsProcessApiTest extends BaseApiTest {

    @Mock
    private EmissionsProcessService processService;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private EmissionsProcessApi emissionsProcessApi;

    private
    EmissionsProcessDto emissionProcess=new EmissionsProcessDto();

    private
    List<EmissionsProcessDto> emissionProcessList;

    @Before
    public void init() {
        emissionProcess=new EmissionsProcessDto();
        when(processService.retrieveById(123L)).thenReturn(emissionProcess);

        emissionProcessList=new ArrayList<>();
        emissionProcessList.add(emissionProcess);
        when(processService.retrieveForReleasePoint(1L)).thenReturn(emissionProcessList);
        when(processService.retrieveForEmissionsUnit(1L)).thenReturn(emissionProcessList);

        when(securityService.facilityEnforcer()).thenReturn(new ReviewerFacilityAccessEnforcerImpl());
    }

    @Test
    public void retrieveEmissionsProcess_Should_ReturnEmissionProcessObjectWithOkStatusCode_When_ValidIdPassed() {
        ResponseEntity<EmissionsProcessDto> result=emissionsProcessApi.retrieveEmissionsProcess(123L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(emissionProcess, result.getBody());
    }

    @Test
    public void retrieveEmissionsProcessesForReleasePoint_Should_ReturnEmissionsProcessListWithStatusOk_When_ValidReleasePointIdPassed() {
        ResponseEntity<Collection<EmissionsProcessDto>> result=emissionsProcessApi.retrieveEmissionsProcessesForReleasePoint(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(emissionProcessList, result.getBody());
    }

    @Test
    public void retrieveEmissionsProcessesForEmissionsUnit_Should_ReturnEmissionsProcessListWithStatusOk_When_ValidEmissionUnitIdPassed() {
        ResponseEntity<Collection<EmissionsProcessDto>> result=emissionsProcessApi.retrieveEmissionsProcessesForEmissionsUnit(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(emissionProcessList, result.getBody());
    }

}
