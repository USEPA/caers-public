/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.api.rest;

import gov.epa.cef.web.repository.EmissionsProcessRepository;
import gov.epa.cef.web.repository.EmissionsUnitRepository;
import gov.epa.cef.web.repository.FacilitySiteRepository;
import gov.epa.cef.web.repository.ReleasePointRepository;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.EmissionsProcessService;
import gov.epa.cef.web.service.dto.EmissionsProcessDto;
import gov.epa.cef.web.service.dto.EmissionsProcessSaveDto;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionsProcessBulkUploadDto;
import gov.epa.cef.web.util.CsvBuilder;
import gov.epa.cef.web.util.WebUtils;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/emissionsProcess")
public class EmissionsProcessApi {

    private final EmissionsProcessService processService;

    private final SecurityService securityService;

    @Autowired
    EmissionsProcessApi(
        SecurityService securityService,
        EmissionsProcessService processService
    ) {
        this.securityService = securityService;
        this.processService = processService;
    }

    /**
     * Create a new Emissions Process
     * @param dto
     * @return
     */
    @PostMapping
    @Operation(summary = "Create emission process",
        description = "Create emission process",
        tags = "Emission Process")
    public ResponseEntity<EmissionsProcessDto> createEmissionsProcess(
        @NotNull @RequestBody EmissionsProcessSaveDto dto) {

        this.securityService.facilityEnforcer()
            .enforceEntity(dto.getEmissionsUnitId(), EmissionsUnitRepository.class);

        EmissionsProcessDto result = processService.create(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update an Emissions Process
     * @param id
     * @param dto
     * @return
     */
    @PutMapping(value = "/{id}")
    @Operation(summary = "Update emission process",
        description = "Update emission process",
        tags = "Emission Process")
    public ResponseEntity<EmissionsProcessDto> updateEmissionsProcess(
        @NotNull @PathVariable Long id, @NotNull @RequestBody EmissionsProcessSaveDto dto) {

        this.securityService.facilityEnforcer().enforceEntity(id, EmissionsProcessRepository.class);

        EmissionsProcessDto result = processService.update(dto.withId(id));

        return new ResponseEntity<EmissionsProcessDto>(result, HttpStatus.OK);
    }

    /**
     * Update only the SCC for an Emissions Process
     * @param id
     * @param dto
     * @return
     */
    @PutMapping(value = "/scc/{id}")
    @Operation(summary = "Create emission process scc",
        description = "Create emission process scc",
        tags = "Emission Process")
    public ResponseEntity<EmissionsProcessDto> updateEmissionsProcessScc(
        @NotNull @PathVariable Long id, @NotNull @RequestBody EmissionsProcessSaveDto dto) {

        this.securityService.facilityEnforcer().enforceEntity(id, EmissionsProcessRepository.class);

        EmissionsProcessDto result = processService.updateScc(dto.withId(id));

        return new ResponseEntity<EmissionsProcessDto>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Emissions Process by id
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get emission process",
        description = "Get emission process",
        tags = "Emission Process")
    public ResponseEntity<EmissionsProcessDto> retrieveEmissionsProcess(@NotNull @PathVariable Long id) {

        this.securityService.facilityEnforcer().enforceEntity(id, EmissionsProcessRepository.class);

        EmissionsProcessDto result = processService.retrieveById(id);

        return new ResponseEntity<EmissionsProcessDto>(result, HttpStatus.OK);
    }

    /**
     * Retrieve versions of this process from the last year reported
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}/previous")
    @Operation(summary = "Get previous emission process",
        description = "Create previous emission process",
        tags = "Emission Process")
    public ResponseEntity<EmissionsProcessDto> retrievePreviousEmissionsProcess(@NotNull @PathVariable Long id) {

        this.securityService.facilityEnforcer().enforceEntity(id, EmissionsProcessRepository.class);

        EmissionsProcessDto result = processService.retrievePreviousById(id);

        return new ResponseEntity<EmissionsProcessDto>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Emissions Processes for a release point
     * @param releasePointId
     * @return
     */
    @GetMapping(value = "/releasePoint/{releasePointId}")
    @Operation(summary = "Get emission process for release point",
        description = "Get emission process for release point",
        tags = "Emission Process")
    public ResponseEntity<Collection<EmissionsProcessDto>> retrieveEmissionsProcessesForReleasePoint(
        @NotNull @PathVariable Long releasePointId) {

        this.securityService.facilityEnforcer().enforceEntity(releasePointId, ReleasePointRepository.class);

        Collection<EmissionsProcessDto> result = processService.retrieveForReleasePoint(releasePointId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Emissions Processes for an emissions unit
     * @param emissionsUnitId
     * @return
     */
    @GetMapping(value = "/emissionsUnit/{emissionsUnitId}")
    @Operation(summary = "Get emission process for emission unit",
        description = "Get emission process for emission unit",
        tags = "Emission Process")
    public ResponseEntity<Collection<EmissionsProcessDto>> retrieveEmissionsProcessesForEmissionsUnit(
        @NotNull @PathVariable Long emissionsUnitId) {

        this.securityService.facilityEnforcer().enforceEntity(emissionsUnitId, EmissionsUnitRepository.class);

        Collection<EmissionsProcessDto> result = processService.retrieveForEmissionsUnit(emissionsUnitId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete an Emissions Processes for given id
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete emission process",
        description = "Delete emission process",
        tags = "Emission Process")
    public void deleteEmissionsProcess(@PathVariable Long id) {

        this.securityService.facilityEnforcer().enforceEntity(id, EmissionsProcessRepository.class);

        processService.delete(id);
    }

    /***
     * Retrieve a CSV of all of the emissions processes based on the given program system code and inventory year
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/list/csv/{programSystemCode}/{year}")
    @Operation(summary = "Get SLT emission process",
        description = "Get SLT emission process",
        tags = "Emission Process")
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN, AppRole.ROLE_ADMIN})
    public void getSltEmissionsProcesses(@PathVariable String programSystemCode, @PathVariable Short year, HttpServletResponse response) {

    	List<EmissionsProcessBulkUploadDto> csvRows = processService.retrieveEmissionsProcesses(programSystemCode, year);
    	CsvBuilder<EmissionsProcessBulkUploadDto> csvBuilder = new CsvBuilder<EmissionsProcessBulkUploadDto>(EmissionsProcessBulkUploadDto.class, csvRows);

    	WebUtils.WriteCsv(response, csvBuilder);
    }
}
