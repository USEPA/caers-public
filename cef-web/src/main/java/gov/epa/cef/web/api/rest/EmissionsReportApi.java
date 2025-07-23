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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gov.epa.cef.web.client.soap.VirusScanClient;
import gov.epa.cef.web.domain.ReportAction;
import gov.epa.cef.web.exception.ApplicationErrorCode;
import gov.epa.cef.web.exception.ApplicationException;
import gov.epa.cef.web.exception.BulkReportValidationException;
import gov.epa.cef.web.exception.VirusScanException;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.BulkUploadService;
import gov.epa.cef.web.service.EmissionsReportExportService;
import gov.epa.cef.web.service.EmissionsReportService;
import gov.epa.cef.web.service.EmissionsReportStatusService;
import gov.epa.cef.web.service.EmissionsReportValidationService;
import gov.epa.cef.web.service.FacilitySiteService;
import gov.epa.cef.web.service.ReportService;
import gov.epa.cef.web.service.dto.*;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionsReportBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.WorksheetError;
import gov.epa.cef.web.service.dto.json.EmissionsReportJsonDto;
import gov.epa.cef.web.service.validation.ValidationFeature;
import gov.epa.cef.web.service.validation.ValidationResult;
import gov.epa.cef.web.util.TempFile;
import gov.epa.cef.web.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import net.exchangenetwork.wsdl.register.pdf._1.PdfDocumentType;
import net.exchangenetwork.wsdl.register.program_facility._1.ProgramFacility;
import net.exchangenetwork.wsdl.register.sign._1.SignatureDocumentType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/emissionsReport")
public class EmissionsReportApi {

    private final EmissionsReportService emissionsReportService;

    private final EmissionsReportStatusService emissionsReportStatusService;

    private final EmissionsReportExportService exportService;

    private final FacilitySiteService facilitySiteService;

    private final ObjectMapper objectMapper;

    private final ReportService reportService;

    private final SecurityService securityService;

    private final BulkUploadService uploadService;

    private final VirusScanClient virusScanClient;

    private final EmissionsReportValidationService validationService;

    Logger LOGGER = LoggerFactory.getLogger(EmissionsReportApi.class);

    @Autowired
    EmissionsReportApi(SecurityService securityService,
                       EmissionsReportService emissionsReportService,
                       EmissionsReportStatusService emissionsReportStatusService,
                       EmissionsReportExportService exportService,
                       FacilitySiteService facilitySiteService,
                       ReportService reportService,
                       EmissionsReportValidationService validationService,
                       BulkUploadService uploadService,
                       VirusScanClient virusScanClient,
                       ObjectMapper objectMapper) {

        this.securityService = securityService;
        this.emissionsReportService = emissionsReportService;
        this.emissionsReportStatusService = emissionsReportStatusService;
        this.exportService = exportService;
        this.facilitySiteService = facilitySiteService;
        this.reportService = reportService;
        this.validationService = validationService;
        this.uploadService = uploadService;
        this.virusScanClient = virusScanClient;

        this.objectMapper = objectMapper;
    }

    /**
     * Begin Advanced QA for the specified reports, move from Submitted to Advanced QA
     *
     * @param reportIds
     * @return
     */
    @PostMapping(value = "/beginAdvancedQA")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    @Operation(summary = "Begin advanced QA emission report",
        description = "Begin advanced QA emission report",
        tags = "Emission Report")
    public ResponseEntity<List<EmissionsReportDto>> beginAdvancedQA(@NotNull @RequestBody List<Long> reportIds) {

        this.securityService.facilityEnforcer().enforceEntities(reportIds, EmissionsReportRepository.class);

        List<EmissionsReportDto> result = emissionsReportService.beginAdvancedQAEmissionsReports(reportIds);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Approve the specified reports and move to approved
     *
     * @param reviewDTO
     * @return
     */
    @PostMapping(value = "/accept")
    @Operation(summary = "Accept reports emission report",
        description = "Accept reports emission report",
        tags = "Emission Report")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    public ResponseEntity<List<EmissionsReportDto>> acceptReports(@NotNull @RequestBody ReviewDTO reviewDTO) {

        this.securityService.facilityEnforcer().enforceEntities(reviewDTO.reportIds, EmissionsReportRepository.class);

        List<EmissionsReportDto> result = emissionsReportService.acceptEmissionsReports(reviewDTO.reportIds, reviewDTO.comments);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ExceptionHandler(value = BulkReportValidationException.class)
    public ResponseEntity<JsonNode> bulkUploadValidationError(BulkReportValidationException exception) {

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("failed", true);
        ArrayNode arrayNode = objectNode.putArray("errors");
        exception.getErrors().forEach(error -> arrayNode.add(objectMapper.convertValue(error, JsonNode.class)));

        return ResponseEntity.badRequest().body(objectNode);
    }

    /**
     * Creates an Emissions Report from either previous report
     *
     * @param masterFacilityRecordId
     * @param workbook
     * @param reportDto
     * @return
     */
    @PostMapping(value = "/facility/{masterFacilityRecordId}",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create emission report",
        description = "Create emission report",
        tags = "Emission Report")
    public ResponseEntity<EmissionsReportDto> create(
        @NotNull @PathVariable Long masterFacilityRecordId,
        @NotBlank @RequestPart("workbook") MultipartFile workbook,
        @NotNull @RequestPart("metadata") EmissionsReportStarterDto reportDto) {

        this.securityService.facilityEnforcer().enforceMasterId(masterFacilityRecordId);

        reportDto.setMasterFacilityRecordId(masterFacilityRecordId);

        if (reportDto.getYear() == null) {
            throw new ApplicationException(ApplicationErrorCode.E_INVALID_ARGUMENT, "Reporting Year must be set.");
        }

        EmissionsReportDto result = null;
        HttpStatus status = HttpStatus.NO_CONTENT;

        try (TempFile tempFile = TempFile.from(workbook.getInputStream(), workbook.getOriginalFilename())) {

            LOGGER.debug("Workbook filename {}", tempFile.getFileName());
            LOGGER.debug("ReportDto {}", reportDto);

            this.virusScanClient.scanFile(tempFile);

            result = this.uploadService.saveBulkWorkbook(reportDto, tempFile);

            status = HttpStatus.OK;

        } catch (VirusScanException e) {

            String msg = String.format("The uploaded file, '%s', is suspected of containing a threat " +
                    "such as a virus or malware and was deleted. The scanner responded with: '%s'.",
                workbook.getOriginalFilename(), e.getMessage());

            throw new BulkReportValidationException(
                Collections.singletonList(WorksheetError.createSystemError(msg)));

        } catch (IOException e) {

            throw new IllegalStateException(e);
        }

        return new ResponseEntity<>(result, status);
    }

    /**
     * Creates an Emissions Report from either previous report
     *
     * @param masterFacilityRecordId
     * @param reportDto
     * @return
     */
    @PostMapping(value = "/facility/{masterFacilityRecordId}/legacy",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create emission report from legacy json",
        description = "Create emission report from legacy json",
        tags = "Emission Report")
    public ResponseEntity<EmissionsReportDto> createFromLegacyJson(
        @NotNull @PathVariable Long masterFacilityRecordId,
        @NotNull @RequestBody EmissionsReportStarterDto reportDto) {

        this.securityService.facilityEnforcer().enforceMasterId(masterFacilityRecordId);

        reportDto.setMasterFacilityRecordId(masterFacilityRecordId);

        if (reportDto.getYear() == null) {
            throw new ApplicationException(ApplicationErrorCode.E_INVALID_ARGUMENT, "Reporting Year must be set.");
        }

        EmissionsReportDto result = this.uploadService.saveBulkJson(reportDto, reportDto.getJsonFileData());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Creates an Emissions Report from either previous report
     *
     * @param masterFacilityRecordId
     * @param reportDto
     * @return
     */
    @PostMapping(value = "/facility/{masterFacilityRecordId}/json",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create emission report from json",
        description = "Create emission report from json",
        tags = "Emission Report")
    public ResponseEntity<EmissionsReportDto> createFromJson(
        @NotNull @PathVariable Long masterFacilityRecordId,
        @NotNull @RequestBody EmissionsReportStarterDto reportDto) {

        this.securityService.facilityEnforcer().enforceMasterId(masterFacilityRecordId);

        reportDto.setMasterFacilityRecordId(masterFacilityRecordId);

        if (reportDto.getYear() == null) {
            throw new ApplicationException(ApplicationErrorCode.E_INVALID_ARGUMENT, "Reporting Year must be set.");
        }

        EmissionsReportDto result = this.uploadService.saveCaersJson(reportDto, reportDto.getJsonFileData());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Creates an Emissions Report from either previous report
     *
     * @param masterFacilityRecordId
     * @param reportDto
     * @return
     */
    @PostMapping(value = "/facility/{masterFacilityRecordId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create emission report by master facility id",
        description = "Create emission report by master facility id",
        tags = "Emission Report")
    public ResponseEntity<EmissionsReportDto> create(@NotNull @PathVariable Long masterFacilityRecordId,
                                                     @NotNull @RequestBody EmissionsReportStarterDto reportDto) {

        this.securityService.facilityEnforcer().enforceMasterId(masterFacilityRecordId);

        reportDto.setMasterFacilityRecordId(masterFacilityRecordId);

        if (reportDto.getYear() == null) {
            throw new ApplicationException(ApplicationErrorCode.E_INVALID_ARGUMENT, "Reporting Year must be set.");
        }

        EmissionsReportDto result = createEmissionsReportDto(reportDto);

        HttpStatus status = HttpStatus.NO_CONTENT;
        if (result != null) {
            status = HttpStatus.OK;
        }


        return new ResponseEntity<>(result, status);
    }

    /**
     * Set Emissions Report HasSubmitted column to true
     * @param reportId
     * @return
     */
    @PutMapping(value = "/{reportId}/feedbackSubmitted")
    @Operation(summary = "Update submitted emission report",
        description = "Update submitted emission report",
        tags = "Emission Report")
    public ResponseEntity<EmissionsReportDto> updateEmissionsReportHasSubmitted(
        @NotNull @PathVariable Long reportId) {

        // TODO should update path to /{reportId}/submitted to be concise/clear

        this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        EmissionsReportDto result =
            emissionsReportService.updateSubmitted(reportId, true);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete a report for given id
     *
     * @param reportId
     * @return
     */
    @DeleteMapping(value = "/{reportId}")
    @Operation(summary = "Delete emission report",
        description = "Delete emission report",
        tags = "Emission Report")
    public void deleteReport(@PathVariable Long reportId) {

        this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        emissionsReportService.markReportForDeletion(reportId);
    }

    /**
     * Testing method for generating upload JSON for a report
     *
     * @param reportId
     * @return
     */
    @GetMapping(value = "/export/{reportId}/legacy")
    @Operation(summary = "Export emission report",
        description = "Export emission report",
        tags = "Emission Report")
    public ResponseEntity<EmissionsReportBulkUploadDto> exportReport(
        @NotNull @PathVariable Long reportId) {

    	this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        EmissionsReportBulkUploadDto result =
            exportService.generateBulkUploadDto(reportId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Testing method for generating upload JSON for a report
     *
     * @param reportId
     * @return
     */
    @GetMapping(value = "/export/{reportId}")
    @Operation(summary = "Export json emission report",
        description = "Export json emission report",
        tags = "Emission Report")
    public ResponseEntity<EmissionsReportJsonDto> exportJsonReport(
        @NotNull @PathVariable Long reportId) {

        this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        EmissionsReportJsonDto result =
            exportService.generateJsonDto(reportId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Generate an excel spreadsheet export for a report
     *
     * @param reportId
     * @return
     */
    @GetMapping(value = "/export/{reportId}/excel")
    @Operation(summary = "Export excel emission report",
        description = "Export excel emission report",
        tags = "Emission Report")
    public ResponseEntity<StreamingResponseBody> exportReportExcel(
        @NotNull @PathVariable Long reportId) {

        this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        EmissionsReportDto report = emissionsReportService.findById(reportId);
        FacilitySiteDto facility = facilitySiteService.findByReportId(reportId);
        String fileName = StringUtils.createValidFileName(facility.getAgencyFacilityIdentifier(), facility.getName(), report.getYear());

        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", fileName))
                .body(outputStream -> {
                    exportService.generateExcel(reportId, outputStream);
                });
    }

    /**
     * Reject the specified reports and move back to in progress
     *
     * @param reviewDTO
     * @return
     */
    @PostMapping(value = "/reject")
    @Operation(summary = "Reject emission report",
        description = "Reject emission report",
        tags = "Emission Report")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    public ResponseEntity<List<EmissionsReportDto>> rejectReports(@NotNull @RequestBody ReviewDTO reviewDTO) {

        this.securityService.facilityEnforcer().enforceEntities(reviewDTO.reportIds, EmissionsReportRepository.class);

        List<EmissionsReportDto> result = emissionsReportService.rejectEmissionsReports(reviewDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Reset report status. Sets report status to in progress and validation status to unvalidated.
     *
     * @param reportIds
     * @return
     */
    @PostMapping(value = "/reset")
    @RolesAllowed(value = {AppRole.ROLE_PREPARER, AppRole.ROLE_NEI_CERTIFIER})
    @Operation(summary = "Reset emission reports",
        description = "Reset emission reports",
        tags = "Emission Report")
    public ResponseEntity<List<EmissionsReportDto>> resetReports(@NotNull @RequestBody List<Long> reportIds) {

        this.securityService.facilityEnforcer().enforceEntities(reportIds, EmissionsReportRepository.class);

        List<EmissionsReportDto> result = emissionsReportStatusService.resetEmissionsReport(reportIds);

        this.reportService.createReportHistory(reportIds, ReportAction.REOPENED);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve current report for a given facility
     *
     * @param masterFacilityRecordId {@link ProgramFacility}'s programId
     * @return
     */
    @GetMapping(value = "/facility/{masterFacilityRecordId}/current")
    @Operation(summary = "Get current emission report for facility",
        description = "Get emission report for facility",
        tags = "Emission Report")
    public ResponseEntity<EmissionsReportDto> retrieveCurrentReportForFacility(
        @NotNull @PathVariable Long masterFacilityRecordId) {

        this.securityService.facilityEnforcer().enforceMasterId(masterFacilityRecordId);

        EmissionsReportDto result = emissionsReportService.findMostRecentByMasterFacilityRecordId(masterFacilityRecordId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve report by ID
     *
     * @param reportId
     * @return
     */
    @GetMapping(value = "/{reportId}")
    @Operation(summary = "Get emission report by id",
        description = "Get emission report by id",
        tags = "Emission Report")
    public ResponseEntity<EmissionsReportDto> retrieveReport(@NotNull @PathVariable Long reportId) {

        this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        EmissionsReportDto result = emissionsReportService.findById(reportId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve reports for a given facility
     *
     * @param masterFacilityRecordId {@link ProgramFacility}'s programId
     * @return
     */
    @GetMapping(value = "/facility/{masterFacilityRecordId}/monthlyReportingEnabled/{monthlyReportingEnabled}")
    @Operation(summary = "Get emission report for facility",
        description = "Get emission report for facility",
        tags = "Emission Report")
    public ResponseEntity<List<EmissionsReportDto>> retrieveReportsForFacility(
        @NotNull @PathVariable Long masterFacilityRecordId,
        @NotNull @PathVariable boolean monthlyReportingEnabled) {

        this.securityService.facilityEnforcer().enforceMasterId(masterFacilityRecordId);

        List<EmissionsReportDto> result =
            emissionsReportService.findByMasterFacilityRecordId(masterFacilityRecordId, true, monthlyReportingEnabled);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Find all agencies with reports and which years they have reports for
     * @return
     */
    @GetMapping(value = "/agencyYears")
    @Operation(summary = "Get emission report find agency reported years",
        description = "Get emission report find agency reported years",
        tags = "Emission Report")
    public ResponseEntity<List<EmissionsReportAgencyDataDto>> findAgencyReportedYears() {

        List<EmissionsReportAgencyDataDto> result =
            emissionsReportService.findAgencyReportedYears();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieves list of agencies with monthly reporting enabled with reports, with years they have reports for.
     * @return
     */
    @GetMapping(value = "/monthlyReportingEnabled/agencyYears")
    @Operation(summary = "Get emission report find agency monthly reported years",
        description = "Get emission report find agency monthly reported years",
        tags = "Emission Report")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER, AppRole.ROLE_CAERS_ADMIN})
    public ResponseEntity<List<EmissionsReportAgencyDataDto>> findAgencyMonthlyReportedYears() {

        List<EmissionsReportAgencyDataDto> result =
            emissionsReportService.findAgencyMonthlyReportedYears();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Send an email notification to NEI Certifiers report is ready to certify and submit
     *
     * @param reportId   Emissions Report Id
     * @param isSemiannual
     * @return
     */
    @PostMapping(value = "/readyToCertify/{isSemiannual}")
    @RolesAllowed(AppRole.ROLE_PREPARER)
    @Operation(summary = "Ready to certify notification emission report",
        description = "Ready to certify notification emission reports",
        tags = "Emission Report")
    public ResponseEntity<Boolean> readyToCertifyNotification(@NotNull @RequestBody Long reportId,
                                                              @NotNull @PathVariable Boolean isSemiannual) {

        this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        Boolean result = emissionsReportService.readyToCertifyNotification(reportId, isSemiannual);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Submits a report to CROMERR after the user authenticates through the widgets and generates and activityId
     *
     * @param activityId CROMERR Activity ID
     * @param reportId   Emissions Report Id
     * @return
     */
    @GetMapping(value = "/submitToCromerr")
    @Operation(summary = "Submit to cromerr emission report",
        description = "Submit to cromerr emission report",
        tags = "Emission Report")
    @RolesAllowed(AppRole.ROLE_NEI_CERTIFIER)
    public ResponseEntity<String> submitToCromerr(
        @NotBlank @RequestParam String activityId, @NotNull @RequestParam Long reportId, @NotNull @RequestParam Boolean isSemiannual) {

        this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        String documentId = emissionsReportService.submitToCromerr(reportId, activityId, isSemiannual);

        if (isSemiannual) {
            this.reportService.createReportHistory(reportId, ReportAction.SEMIANNUAL_SUBMITTED, activityId, documentId);
        }
        else {
            this.reportService.createReportHistory(reportId, ReportAction.SUBMITTED, activityId, documentId);
        }

        return new ResponseEntity<>(documentId, HttpStatus.OK);
    }


    /**
     * Inserts a new emissions report, facility, sub-facility components, and emissions based on a JSON input string
     *
     * @param jsonNode
     * @return
     */
    @PostMapping(value = "/upload/{fileName}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN})
    @Operation(summary = "Upload emission reports",
        description = "Upload emission reports",
        tags = "Emission Report")
    public ResponseEntity<EmissionsReportDto> uploadReport(@NotNull @PathVariable String fileName, @NotNull @RequestBody JsonNode jsonNode) {

        try {
            EmissionsReportDto savedReport = this.uploadService.parseJsonNode(false)
                .andThen(this.uploadService::saveBulkEmissionsReport)
                .apply(jsonNode);
            savedReport.setFileName(fileName);
            return new ResponseEntity<>(savedReport, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("uploadReport exception: ", ex);
            String msg = String.format("'%s' failed to upload.  Error: %s",
                fileName, ex.getMessage());

            throw new BulkReportValidationException(Collections.singletonList(WorksheetError.createSystemError(msg)));
        }
    }

    @PostMapping(value = "/validation/{semiannual}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Validate emission reports",
        description = "Validate emission reports",
        tags = "Emission Report")
    public ResponseEntity<ValidationResult> validateReport(@NotNull @PathVariable Boolean semiannual,
                                                           @NotNull @RequestBody EntityRefDto entityRefDto) {

        this.securityService.facilityEnforcer()
            .enforceEntity(entityRefDto.requireNonNullId(), EmissionsReportRepository.class);

        EmissionsReportDto report = this.emissionsReportService.findById(entityRefDto.getId());

        this.validationService.createInProgressQAResults(entityRefDto.getId(), semiannual);

        ValidationResult result;

        if (report.getProgramSystemCode().getCode().equals("MEDEP")) {
            result = this.validationService.validateAndUpdateStatus(entityRefDto.getId(), semiannual, ValidationFeature.MEDEP);
        }
        else if (report.getProgramSystemCode().getCode().equals("IDDEQ")) {
            result = this.validationService.validateAndUpdateStatus(entityRefDto.getId(), semiannual, ValidationFeature.IDDEQ);
        }
        else {
            result = this.validationService.validateAndUpdateStatus(entityRefDto.getId(), semiannual);
        }

        int numErrors = result.getFederalErrors().size() + result.getStateErrors().size();
        int numWarnings = result.getFederalWarnings().size() + result.getStateWarnings().size();

        emissionsReportService.updateMaxNumberOfQAs(report.getId(), numErrors, numWarnings);

        return ResponseEntity.ok().body(result);
    }

    /**
     * Gets the QA Check Results by the report id and annual/semiannual flag
     * @param reportId
     * @param semiannual
     * @return
     */
    @GetMapping(value = "/validation/getResults/{reportId}/{semiannual}")
    @Operation(summary = "Get QA results emission reports",
        description = "Get QA results emission reports",
        tags = "Emission Report")
    public ResponseEntity<QualityCheckResultsDto> getQAResults(@NotNull @PathVariable Long reportId,
                                                               @NotNull @PathVariable boolean semiannual) {

        this.securityService.facilityEnforcer()
            .enforceEntity(reportId, EmissionsReportRepository.class);

        QualityCheckResultsDto qaResults = this.validationService.getQAResultsDto(reportId, semiannual);

        return new ResponseEntity<>(qaResults, HttpStatus.OK);
    }

    /**
     * Updates the QA Check Results status by the report id, semiannual flag, and the desired status
     * @param reportId
     * @param semiannual
     * @param status
     * @return
     */
    @PutMapping(value = "/validation/updateQAResultsStatus/{reportId}/{semiannual}/{status}")
    @Operation(summary = "Update results status emission reports",
        description = "Update results status emission reports",
        tags = "Emission Report")
    public ResponseEntity<QualityCheckResultsDto> updateQAResultsStatus(@NotNull @PathVariable Long reportId,
                                                                        @NotNull @PathVariable boolean semiannual,
                                                                        @NotNull @PathVariable String status) {

        this.securityService.facilityEnforcer()
            .enforceEntity(reportId, EmissionsReportRepository.class);

        QualityCheckResultsDto qaResults = this.validationService.updateQAResultsStatus(reportId, semiannual, status);

        return new ResponseEntity<>(qaResults, HttpStatus.OK);
    }

    /**
     * When threshold is enabled, and report is either below threshold, PS or TS; those reports cannot run QA check to validate report.
     * Validation status is reset when an attachment is deleted. When the Certify and Submit to SLT button is clicked, validation status
     * will be updated to PASSED for below threshold, PS or TS reports.
     *
     * @param reportId
     */
    @PostMapping(value = "/validation/thresholdNoQA")
    @RolesAllowed(value = {AppRole.ROLE_NEI_CERTIFIER})
    @Operation(summary = "Validate threshold QA emission report",
        description = "Validate threshold QA emission report",
        tags = "Emission Report")
    public void validateThresholdNoQAReport(@NotNull @RequestBody Long reportId) {
    	this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);
        this.validationService.validateAndUpdateThresholdNoQAStatus(reportId);
    }

    @GetMapping(value = "/{reportId}/changes")
    @Operation(summary = "Get emission report changes",
        description = "Get emission report changes",
        tags = "Emission Report")
    public ResponseEntity<List<ReportChangeDto>> retrieveReportChanges(@NotNull @PathVariable Long reportId) {

        this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        List<ReportChangeDto> result = emissionsReportService.retrieveChangesForReport(reportId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private EmissionsReportDto createEmissionsReportDto(EmissionsReportStarterDto reportDto) {

        EmissionsReportDto result;

        Long masterFacilityRecordId = reportDto.getMasterFacilityRecordId();

        switch (reportDto.getSource()) {
            case previous:
                result = this.emissionsReportService.createEmissionReportCopy(reportDto);
                break;
            case fromScratch:
                result = this.emissionsReportService.createEmissionReport(reportDto);
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * Approve the specified semi-annual report
     * @param reviewDTO
     * @return
     */
    @PostMapping(value = "/semiannual/accept")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    @Operation(summary = "Accept semiannual emission reports",
        description = "Accept semiannual emission reports",
        tags = "Emission Report")
    public ResponseEntity<Collection<EmissionsReportDto>> acceptSemiAnnualReports(@NotNull @RequestBody ReviewDTO reviewDTO) {

    	this.securityService.facilityEnforcer().enforceEntities(reviewDTO.getReportIds(), EmissionsReportRepository.class);

    	Collection<EmissionsReportDto> result = emissionsReportService.acceptSemiAnnualReports(reviewDTO);

    	return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Reject the specified reports and move back to in progress
     *
     * @param reviewDTO
     * @return
     */
    @PostMapping(value = "/semiannual/reject")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    @Operation(summary = "Reject semiannual emission reports",
        description = "Reject semiannual emission reports",
        tags = "Emission Report")
    public ResponseEntity<List<EmissionsReportDto>> rejectSemiAnnualReports(@NotNull @RequestBody ReviewDTO reviewDTO) {

        this.securityService.facilityEnforcer().enforceEntities(reviewDTO.getReportIds(), EmissionsReportRepository.class);

        List<EmissionsReportDto> result = emissionsReportService.rejectSemiAnnualReports(reviewDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/downloadCopyOfRecord/{activityId}/{documentId}")
    @Operation(summary = "Download copy of emission report",
        description = "Download copy of emission report",
        tags = "Emission Report")
    public ResponseEntity<StreamingResponseBody> downloadCopyOfRecord(
    		@NotNull @PathVariable String activityId,
    		@NotNull @PathVariable String documentId) {

        final String xmlFormat = "xml";

        SignatureDocumentType document = emissionsReportService.getCopyOfRecord(activityId, documentId);

        String fileType = "application/zip";
        if (document.getName().substring(document.getName().indexOf('.') + 1).equals(xmlFormat)) {
            fileType = "application/xml";

            return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + document.getName() + "\"")
                .body(outputStream -> {
                    emissionsReportService.transformXmlCor(document, outputStream);
                });

        }

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(fileType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + document.getName() + "\"")
            .body(outputStream -> {
                emissionsReportService.writeFileTo(document, outputStream);
            });
    }

    @GetMapping(value = "/cor/{reportId}/{isSemiannual}/{signed}")
    @Operation(summary = "Get copy emission reports",
        description = "Get copy emission reports",
        tags = "Emission Report")
    public ResponseEntity<StreamingResponseBody> generateCopyOfRecord(
        @NotNull @PathVariable Long reportId, @PathVariable boolean isSemiannual, @PathVariable boolean signed) {

        this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        PdfDocumentType document = emissionsReportService.generateCopyOfRecord(reportId, isSemiannual, signed);

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + reportId + ".pdf\"")
            .body(outputStream -> {
                document.getContent().writeTo(outputStream);
            });
    }

    @GetMapping(value= "/facility/{masterFacilityRecordId}/year/{year}")
    @Operation(summary = "Get emission report by facility and year",
        description = "Get emission report by facility and year",
        tags = "Emission Report")
    public ResponseEntity<EmissionsReportDto> retrieveReportByFacilityAndYear(
                @NotNull @PathVariable Long masterFacilityRecordId, @NotNull @PathVariable int year) {

        this.securityService.facilityEnforcer().enforceMasterId(masterFacilityRecordId);

        EmissionsReportDto result = this.emissionsReportService.retrieveReportByMasterFacilityRecordIdAndYear(masterFacilityRecordId, year);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public static class ReviewDTO {

        private String comments;

        private List<Long> reportIds;

        private Long attachmentId;

        public String getComments() {

            return comments;
        }

        public void setComments(String comments) {

            this.comments = comments;
        }

        public List<Long> getReportIds() {

            return reportIds;
        }

        public void setReportIds(List<Long> reportIds) {

            this.reportIds = reportIds;
        }

        public void setAttachmentId(Long attachmentId) {

            this.attachmentId = attachmentId;
        }

        public Long getAttachmentId() {

            return attachmentId;
        }

    }
}
