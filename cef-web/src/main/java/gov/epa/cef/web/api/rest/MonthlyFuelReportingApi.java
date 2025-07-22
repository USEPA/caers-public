/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.api.rest;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.FacilitySiteRepository;
import gov.epa.cef.web.repository.ReportingPeriodRepository;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.MonthlyFuelReportingService;
import gov.epa.cef.web.service.dto.MonthlyFuelReportingDownloadDto;
import gov.epa.cef.web.service.dto.MonthlyFuelReportingDto;

@RestController
@RequestMapping("/api/monthlyFuelReporting")
public class MonthlyFuelReportingApi {

	private final SecurityService securityService;

	private final MonthlyFuelReportingService monthlyReportingService;

	@Autowired
	MonthlyFuelReportingApi(
			SecurityService securityService,
			MonthlyFuelReportingService monthlyReportingService
    ) {
		this.securityService = securityService;
		this.monthlyReportingService = monthlyReportingService;
	}

    /**
     * Retrieve Reporting Periods for fuel use materials and facility site
     * @param facilitySiteId
     * @param period
     * @return
     */
    @GetMapping(value = "/monthlyReporting/{facilitySiteId}/{period}")
    @Operation(summary = "Get monthly fuel reporting for month data",
        description = "Get monthly fuel reporting for month data",
        tags = "Monthly Fuel Reporting")
    public ResponseEntity<Collection<MonthlyFuelReportingDto>> retrieveForMonthData(
        @NotNull @PathVariable Long facilitySiteId, @NotNull @PathVariable String period) {

        this.securityService.facilityEnforcer().enforceEntity(facilitySiteId, FacilitySiteRepository.class);

        Collection<MonthlyFuelReportingDto> result = monthlyReportingService.retrieveForMonthData(facilitySiteId, period);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update the fuel value for multiple Monthly Reporting at once
     * @param dtos
     * @return
     */
    @PutMapping(value = "/monthlyReporting/{facilitySiteId}")
    @Operation(summary = "Persist monthly fuel reporting",
        description = "Persist monthly fuel reporting",
        tags = "Monthly Fuel Reporting")
    public ResponseEntity<Collection<MonthlyFuelReportingDto>> saveMonthlyReporting(
            @NotNull @PathVariable Long facilitySiteId, @NotNull @RequestBody List<MonthlyFuelReportingDto> dtos) {

    	this.securityService.facilityEnforcer().enforceEntity(facilitySiteId, FacilitySiteRepository.class);

    	List<Long> periodIds = dtos.stream().map(MonthlyFuelReportingDto::getReportingPeriodId).collect(Collectors.toList());
    	this.securityService.facilityEnforcer().enforceEntities(periodIds, ReportingPeriodRepository.class);

    	Collection<MonthlyFuelReportingDto> result = monthlyReportingService.saveMonthlyReporting(facilitySiteId, dtos);
    	return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Get data needed for exporting CSV
     *
     * @param facilitySiteId
     * @return
     */
    @GetMapping(value = "/downloadReport/{facilitySiteId}")
    @Operation(summary = "Get monthly fuel reporting download by facility site",
        description = "Get monthly fuel reporting download by facility site",
        tags = "Monthly Fuel Reporting")
    public ResponseEntity<List<MonthlyFuelReportingDownloadDto>> retrieveDownloadDtoByFacilitySiteId(@NotNull @PathVariable Long facilitySiteId) {

    	this.securityService.facilityEnforcer().enforceFacilitySite(facilitySiteId);

        List<MonthlyFuelReportingDownloadDto> mfrDownloadDto = monthlyReportingService.retrieveDownloadDtoByFacilitySiteId(facilitySiteId);

        return new ResponseEntity<>(mfrDownloadDto, HttpStatus.OK);
    }

}
