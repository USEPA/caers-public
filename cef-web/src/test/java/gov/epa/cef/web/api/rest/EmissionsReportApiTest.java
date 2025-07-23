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
