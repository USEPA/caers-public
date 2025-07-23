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
import gov.epa.cef.web.service.EmissionsUnitService;
import gov.epa.cef.web.service.dto.EmissionsUnitDto;
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
public class EmissionsUnitApiTest extends BaseApiTest {

    @Mock
    private SecurityService securityService;

    @Mock
    private EmissionsUnitService emissionsUnitService;

    @InjectMocks
    private EmissionsUnitApi emissionsUnitApi;

    private EmissionsUnitDto emissionsUnit;

    private List<EmissionsUnitDto> emissionsUnitDtos;

    @Before
    public void init() {
        emissionsUnit=new EmissionsUnitDto();
        when(emissionsUnitService.retrieveUnitById(123L)).thenReturn(emissionsUnit);

        emissionsUnitDtos=new ArrayList<>();
        emissionsUnitDtos.add(emissionsUnit);
        when(emissionsUnitService.retrieveEmissionUnitsForFacility(1L)).thenReturn(emissionsUnitDtos);

        when(securityService.facilityEnforcer()).thenReturn(new ReviewerFacilityAccessEnforcerImpl());
    }

    @Test
    public void retrieveEmissionsUnit_Should_ReturnEmissionsUnitObectWithOkStatusCode_When_ValidIdPassed() {
        ResponseEntity<EmissionsUnitDto> result=emissionsUnitApi.retrieveEmissionsUnit(123L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(emissionsUnit, result.getBody());
    }

    @Test
    public void retrieveEmissionsUnitsOfFacility_Should_ReturnEmissionsUnitListWithOkStatusCode_WhenValidFacilitIdPassed() {
        ResponseEntity<List<EmissionsUnitDto>> result=emissionsUnitApi.retrieveEmissionsUnitsOfFacility(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(emissionsUnitDtos, result.getBody());
    }
}
