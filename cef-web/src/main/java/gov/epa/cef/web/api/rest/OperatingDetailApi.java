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

import gov.epa.cef.web.repository.OperatingDetailRepository;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.OperatingDetailService;
import gov.epa.cef.web.service.dto.OperatingDetailDto;
import gov.epa.cef.web.service.dto.bulkUpload.OperatingDetailBulkUploadDto;
import gov.epa.cef.web.util.CsvBuilder;
import gov.epa.cef.web.util.WebUtils;

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

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/operatingDetail")
public class OperatingDetailApi {

    private final OperatingDetailService operatingDetailService;

    private final SecurityService securityService;


    @Autowired
    OperatingDetailApi(SecurityService securityService,
                       OperatingDetailService operatingDetailService) {

        this.operatingDetailService = operatingDetailService;
        this.securityService = securityService;
    }

    /**
     * Update an Operating Detail
     * @param id
     * @param dto
     * @return
     */
    @PutMapping(value = "/{id}")
    @Operation(summary = "Update operating detail",
        description = "Update operating detail",
        tags = "Operating Detail")
    public ResponseEntity<OperatingDetailDto> updateOperatingDetail(
        @NotNull @PathVariable Long id, @NotNull @RequestBody OperatingDetailDto dto) {

        this.securityService.facilityEnforcer().enforceEntity(id, OperatingDetailRepository.class);

        OperatingDetailDto result = operatingDetailService.update(dto.withId(id));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /***
     * Retrieve a CSV of all of the operating details based on the given program system code and inventory year
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/list/csv/{programSystemCode}/{year}")
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN, AppRole.ROLE_ADMIN})
    @Operation(summary = "Get SLT operating detail",
        description = "Get SLT operating detail",
        tags = "Operating Detail")
    public void getSltOperatingDetails(@PathVariable String programSystemCode, @PathVariable Short year, HttpServletResponse response) {

    	List<OperatingDetailBulkUploadDto> csvRows = operatingDetailService.retrieveOperatingDetails(programSystemCode, year);
    	CsvBuilder<OperatingDetailBulkUploadDto> csvBuilder = new CsvBuilder<OperatingDetailBulkUploadDto>(OperatingDetailBulkUploadDto.class, csvRows);

    	WebUtils.WriteCsv(response, csvBuilder);
    }
}
