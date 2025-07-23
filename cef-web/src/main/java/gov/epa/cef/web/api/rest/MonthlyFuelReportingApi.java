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
