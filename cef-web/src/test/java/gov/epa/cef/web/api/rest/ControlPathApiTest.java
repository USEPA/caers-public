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
