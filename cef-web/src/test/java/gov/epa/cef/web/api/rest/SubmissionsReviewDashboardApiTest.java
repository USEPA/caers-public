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

import gov.epa.cef.web.domain.ReportStatus;
import gov.epa.cef.web.service.dto.SubmissionsReviewDashboardDto;
import gov.epa.cef.web.service.impl.SubmissionsReviewDasboardServiceImpl;
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
public class SubmissionsReviewDashboardApiTest extends BaseApiTest {

    @Mock
    private SubmissionsReviewDasboardServiceImpl submissionsReviewDasboardServiceImpl;

    @InjectMocks
    private SubmissionsReviewDashboardApi submissionsReviewDashboardApi;


    @Test
    public void retrieveFacilitiesReportsForReview_Should_ReturnListOfFacilitiesReportsForReview_WhenNoParameterPassed() {
        List<SubmissionsReviewDashboardDto> submissionsUnderReview=new ArrayList<>();
        when(submissionsReviewDasboardServiceImpl.retrieveFacilityReports((short)2020, ReportStatus.IN_PROGRESS.name(), "GADNR")).thenReturn(new ArrayList<>());
        ResponseEntity<List<SubmissionsReviewDashboardDto>> result=submissionsReviewDashboardApi.retrieveAgencySubmissions("GADNR", (short)2020, ReportStatus.IN_PROGRESS.name());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(submissionsUnderReview, result.getBody());
    }

}
