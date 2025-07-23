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
package gov.epa.cef.web.service.impl;

import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.config.slt.SLTConfigImpl;
import gov.epa.cef.web.domain.SubmissionsReviewDashboardView;
import gov.epa.cef.web.provider.system.SLTPropertyProvider;
import gov.epa.cef.web.repository.SubmissionsReviewDashboardRepository;
import gov.epa.cef.web.service.UserService;
import gov.epa.cef.web.service.dto.SubmissionsReviewDashboardDto;
import gov.epa.cef.web.service.dto.UserDto;
import gov.epa.cef.web.service.mapper.SubmissionsReviewDashboardMapper;
import gov.epa.cef.web.util.SLTConfigHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SubmissionsReviewDasboardServiceImplTest extends BaseServiceTest {

    @Mock
    private SubmissionsReviewDashboardRepository repo;

    @Mock
    private SubmissionsReviewDashboardMapper mapper;

    @Mock
    private UserService userService;
    
    @Mock
    SLTPropertyProvider sltPropertyProvider;
    
    @Mock
    SLTConfigHelper sltConfigHelper;

    @InjectMocks
    SubmissionsReviewDasboardServiceImpl submissionsReviewDasboardServiceImpl;


    @Test
    public void retrieveFacilitiesReports_Should_ReturnListOfSubmissionsUnderReview_When_NoArugmentsPassed() {

        UserDto user=new UserDto();
        user.setProgramSystemCode("GADNR");
        when(userService.getCurrentUser()).thenReturn(user);

        SLTBaseConfig sltBaseConfig = new SLTConfigImpl("GADNR",sltPropertyProvider);
        when(this.sltConfigHelper.getCurrentSLTConfig("GADNR")).thenReturn(sltBaseConfig);

        List<SubmissionsReviewDashboardView> submissionsReviewDashboardView=new ArrayList<>();
        when(repo.findByProgramSystemCode("GADNR")).thenReturn(submissionsReviewDashboardView);

        List<SubmissionsReviewDashboardDto> submissionsReviewDashboardDto=new ArrayList<>();
        when(mapper.toDtoList(submissionsReviewDashboardView)).thenReturn(submissionsReviewDashboardDto);

        List<SubmissionsReviewDashboardDto> result= submissionsReviewDasboardServiceImpl.retrieveReviewerFacilityReports(null, null);

        assertEquals(submissionsReviewDashboardDto, result);
    }

}
