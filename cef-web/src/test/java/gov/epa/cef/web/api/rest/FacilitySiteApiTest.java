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
