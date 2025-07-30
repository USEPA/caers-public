/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
