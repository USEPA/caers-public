/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.api.rest;

import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.ReportService;
import gov.epa.cef.web.service.dto.ReportDownloadDto;
import gov.epa.cef.web.service.dto.ReportHistoryDto;
import gov.epa.cef.web.service.dto.ReportSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportApi {

    private final ReportService reportService;

    private final SecurityService securityService;

    @Autowired
    ReportApi( SecurityService securityService, ReportService reportService) {

        this.reportService = reportService;
        this.securityService = securityService;
    }

    /***
     * Return list of report summary records with total emissions summed per pollutant for the chosen report
     * @param reportId
     * @return
     */
    @GetMapping(value = "/emissionsSummary/report/{reportId}")
    @Operation(summary = "Get emission summary report",
        description = "Get emission summary report",
        tags = "Report")
    public ResponseEntity<List<ReportSummaryDto>> retrieveEmissionsSummary(
    		@NotNull @PathVariable Long reportId) {

        this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        List<ReportSummaryDto> reportSummary = reportService.findByReportId(reportId);

        return new ResponseEntity<>(reportSummary, HttpStatus.OK);
    }

    /***
     * Return list of report history records for the chosen report
     * @param reportId
     * @return
     */
    @GetMapping(value = "/reportHistory/report/{reportId}/facilitySiteId/{facilitySiteId}")
    @Operation(summary = "Get report history",
        description = "Get report history",
        tags = "Report")
    public ResponseEntity<List<ReportHistoryDto>> retrieveReportHistory(
    		@NotNull @PathVariable Long reportId, @NotNull @PathVariable Long facilitySiteId) {

    		this.securityService.facilityEnforcer().enforceFacilitySite(facilitySiteId);

    		List<ReportHistoryDto> reportHistory = reportService.findByEmissionsReportId(reportId);

        return new ResponseEntity<>(reportHistory, HttpStatus.OK);
    }

    /***
     * Return DownloadReportDto
     * @param facilitySiteId
     * @param reportId
     * @return
     */
    @GetMapping(value = "/downloadReport/reportId/{reportId}/facilitySiteId/{facilitySiteId}")
    @Operation(summary = "Get report download",
        description = "Get report download",
        tags = "Report")
    public ResponseEntity<List<ReportDownloadDto>> retrieveDownloadReportDto(
        @NotNull @PathVariable Long facilitySiteId, @NotNull @PathVariable Long reportId) {

        this.securityService.facilityEnforcer().enforceFacilitySite(facilitySiteId);

        List<ReportDownloadDto> reportDownloadDto = reportService.retrieveReportDownloadDtoByReportId(reportId);

        return new ResponseEntity<>(reportDownloadDto, HttpStatus.OK);
    }
}
