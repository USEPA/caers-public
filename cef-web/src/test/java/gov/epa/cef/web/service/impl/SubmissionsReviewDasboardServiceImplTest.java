/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
