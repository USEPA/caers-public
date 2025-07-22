/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.impl;

import gov.epa.cef.web.domain.ReportAction;
import gov.epa.cef.web.domain.ReportHistory;
import gov.epa.cef.web.domain.ReportSummary;
import gov.epa.cef.web.repository.ReportHistoryRepository;
import gov.epa.cef.web.repository.ReportSummaryRepository;
import gov.epa.cef.web.service.dto.ReportHistoryDto;
import gov.epa.cef.web.service.dto.ReportSummaryDto;
import gov.epa.cef.web.service.mapper.ReportHistoryMapper;
import gov.epa.cef.web.service.mapper.ReportSummaryMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ReportServiceImplTest extends BaseServiceTest {

    @Mock
    private ReportSummaryRepository reportSummaryRepo;
    
    @Mock
    private ReportHistoryRepository reportHistoryRepo;

    @Mock
    private ReportSummaryMapper reportSummaryMapper;
    
    @Mock
    private ReportHistoryMapper reportHistoryMapper;


    @InjectMocks
    private ReportServiceImpl reportServiceImpl;

    @Before
    public void init() {
        List<ReportSummary> emptyReportSummaryList = new ArrayList<ReportSummary>();
        List<ReportSummaryDto> fullReportSummaryDtoList = new ArrayList<ReportSummaryDto>();
        List<ReportSummary> fullReportSummaryList = new ArrayList<ReportSummary>();
        List<ReportHistory> reportHistoryList = new ArrayList<ReportHistory>();
        List<ReportHistoryDto> reportHistoryDtoList = new ArrayList<ReportHistoryDto>();
        when(reportSummaryRepo.findByReportId(1L)).thenReturn(emptyReportSummaryList);
        when(reportSummaryRepo.findByReportId(2L)).thenReturn(emptyReportSummaryList);

        for (int x = 1; x <= 5; x++) {
            ReportSummary rs = new ReportSummary();
            rs.setCasId("ABC123-" + String.valueOf(x));
            rs.setFacilitySiteId(1L);
            rs.setFugitiveTotal(BigDecimal.valueOf(x*10));
            rs.setId(1L);
            rs.setPollutantName("Test Pollutant-" + String.valueOf(x));
            rs.setPollutantType("HAP");
            rs.setPreviousYearTotal(BigDecimal.ZERO);
            rs.setReportYear(new Short("2019"));
            rs.setStackTotal(BigDecimal.valueOf(x*20));
            rs.setEmissionsTonsTotal(rs.getFugitiveTotal().add(rs.getStackTotal()));
            fullReportSummaryList.add(rs);

            ReportSummaryDto dto = new ReportSummaryDto();
            dto.setCasId(rs.getCasId());
            dto.setFacilitySiteId(rs.getFacilitySiteId());
            dto.setFugitiveTotal(rs.getFugitiveTotal());
            dto.setId(rs.getId());
            dto.setPollutantName(rs.getPollutantName());
            dto.setPollutantType(rs.getPollutantType());
            dto.setPreviousYearTotal(rs.getPreviousYearTotal());
            dto.setReportYear(rs.getReportYear());
            dto.setStackTotal(rs.getStackTotal());
            dto.setEmissionsTonsTotal(rs.getEmissionsTonsTotal());
            fullReportSummaryDtoList.add(dto);
        }
        when(reportSummaryRepo.findByReportId(3L)).thenReturn(fullReportSummaryList);
        when(reportSummaryMapper.toDtoList(fullReportSummaryList)).thenReturn(fullReportSummaryDtoList);
        
        ReportHistory rh = new ReportHistory();
        rh.setReportAction(ReportAction.CREATED);
        rh.setUserFullName("Test Name");
        rh.setUserId("testId");
        reportHistoryList.add(rh);
        
        ReportHistoryDto rhDto = new ReportHistoryDto();
        rhDto.setReportAction(rh.getReportAction().toString());
        rhDto.setUserFullName(rh.getUserFullName());
        rhDto.setUserId(rh.getUserId());
        reportHistoryDtoList.add(rhDto);
        
        when(reportHistoryRepo.findByEmissionsReportIdOrderByActionDate(1L)).thenReturn(reportHistoryList);
        when(reportHistoryMapper.toDtoList(reportHistoryList)).thenReturn(reportHistoryDtoList);
    }

    @Test
    public void findByReportYearAndFacilitySiteId_should_return_total_emissions_when_year_and_facility_exist() {
        List<ReportSummaryDto> dtoList = reportServiceImpl.findByReportId(3L);
        assertEquals(5, dtoList.size());

        ReportSummaryDto dto = dtoList.get(0);
        assertEquals("ABC123-1", dto.getCasId());
        assertEquals(new BigDecimal(10), dto.getFugitiveTotal());
        assertEquals(new BigDecimal(30), dto.getEmissionsTonsTotal());
    }

    @Test
    public void findByReportYearAndFacilitySiteId_should_return_empty_when_year_does_not_exist_for_facility() {
        List<ReportSummaryDto> dtoList = reportServiceImpl.findByReportId(2L);
        ArrayList<ReportSummaryDto> emptyList = new ArrayList<ReportSummaryDto>();
        assertEquals(emptyList, dtoList);
    }

    @Test
    public void findByReportYearAndFacilitySiteId_should_return_empty_when_facility_does_not_exist_for_year() {
        List<ReportSummaryDto> dtoList = reportServiceImpl.findByReportId(1L);
        ArrayList<ReportSummaryDto> emptyList = new ArrayList<ReportSummaryDto>();
        assertEquals(emptyList, dtoList);
    }
    
    @Test
    public void findByReportId_should_return_history_when_report_exist() {
        List<ReportHistoryDto> dtoList = reportServiceImpl.findByEmissionsReportId(1L);

        ReportHistoryDto dto = dtoList.get(0);
        assertEquals(ReportAction.CREATED.toString(), dto.getReportAction());
        assertEquals("Test Name", dto.getUserFullName());
        assertEquals("testId", dto.getUserId());
    }
    
    @Test
    public void findByReportId_should_return_empty_when_report_does_not_exist() {
        List<ReportHistoryDto> dtoList = reportServiceImpl.findByEmissionsReportId(2L);
        ArrayList<ReportHistoryDto> emptyList = new ArrayList<ReportHistoryDto>();
        assertEquals(emptyList, dtoList);
    }
}
