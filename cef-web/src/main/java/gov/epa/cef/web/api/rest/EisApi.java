/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.api.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import gov.epa.cdx.shared.security.ApplicationUser;
import gov.epa.cef.web.client.soap.VirusScanClient;
import gov.epa.cef.web.exception.ReportAttachmentValidationException;
import gov.epa.cef.web.exception.VirusScanException;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.dto.EisDataCriteria;
import gov.epa.cef.web.service.dto.EisDataListDto;
import gov.epa.cef.web.service.dto.EisDataReportDto;
import gov.epa.cef.web.service.dto.EisDataStatsDto;
import gov.epa.cef.web.service.dto.EisHeaderDto;
import gov.epa.cef.web.service.dto.EisTransactionAttachmentDto;
import gov.epa.cef.web.service.dto.EisSubmissionStatus;
import gov.epa.cef.web.service.dto.EisTransactionHistoryDto;
import gov.epa.cef.web.service.dto.bulkUpload.WorksheetError;
import gov.epa.cef.web.service.dto.simple.SimpleStringValue;
import gov.epa.cef.web.service.impl.EisAttachmentServiceImpl;
import gov.epa.cef.web.service.impl.EisTransmissionServiceImpl;
import gov.epa.cef.web.service.impl.EisXmlServiceImpl;
import gov.epa.cef.web.util.TempFile;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/eis")
public class EisApi {

    private final EisAttachmentServiceImpl attachmentService;

    private final EisTransmissionServiceImpl eisTransmissionService;

    private final EisXmlServiceImpl eisXmlService;

    private final SecurityService securityService;

    private final VirusScanClient virusScanClient;

    private ObjectMapper objectMapper;

    Logger logger = LoggerFactory.getLogger(EisApi.class);

    @Autowired
    EisApi(SecurityService securityService,
           EisAttachmentServiceImpl attachmentService,
           EisTransmissionServiceImpl eisTransmissionService,
           EisXmlServiceImpl eisXmlService,
           VirusScanClient virusScanClient,
           ObjectMapper objectMapper) {

        this.securityService = securityService;
        this.attachmentService = attachmentService;
        this.eisXmlService = eisXmlService;

        this.eisTransmissionService = eisTransmissionService;
        this.virusScanClient = virusScanClient;
        this.objectMapper = objectMapper;
    }

    @Validated(EisHeaderDto.EisApiGroup.class)
    @PostMapping(value = "/transaction",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create transaction",
        description = "Create transaction",
        tags = "EIS")
    public ResponseEntity<EisDataListDto> createTransaction(@NotNull @RequestBody EisHeaderDto eisHeader) {

        Preconditions.checkArgument(eisHeader.getSubmissionStatus() != null,
            "SubmissionStatus can not be null.");

        Preconditions.checkArgument(eisHeader.getEmissionsReports().size() > 0,
            "EmissionsReportIds must contain at lease one ID.");

        this.securityService.facilityEnforcer()
            .enforceEntities(eisHeader.getEmissionsReports(), EmissionsReportRepository.class);

        ApplicationUser appUser = this.securityService.getCurrentApplicationUser();

        eisHeader.withProgramSystemCode(appUser.getClientId())
            .withAuthorName(String.format("%s %s", appUser.getFirstName(), appUser.getLastName()))
            .withOrganizationName(appUser.getOrganization());

        EisDataListDto result = this.eisTransmissionService.submitReports(eisHeader);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/emissionsReport/{id}/passed",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Put EIS passed status",
        description = "Put EIS passed status",
        tags = "EIS")
    public ResponseEntity<EisDataReportDto> putEisPassedStatus(@NotNull @PathVariable("id") Long reportId,
                                                          @NotNull @RequestBody SimpleStringValue passed) {

        this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        EisDataReportDto result =
            this.eisTransmissionService.updateReportEisPassedStatus(reportId, passed.getValue());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/emissionsReport/{id}/comment",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Put EIS comment",
        description = "Put EIS comment",
        tags = "EIS")
    public ResponseEntity<EisDataReportDto> putEisComment(@NotNull @PathVariable("id") Long reportId,
                                                          @NotNull @RequestBody SimpleStringValue comment) {

        this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        EisDataReportDto result =
            this.eisTransmissionService.updateReportComment(reportId, comment.getValue());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/emissionsReport",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get EIS data list",
        description = "Get EIS data list",
        tags = "EIS")
    public ResponseEntity<EisDataListDto> retrieveEisDataList(@NotNull @RequestParam(value = "year") Integer year,
                                                              @RequestParam(value = "status", required = false) EisSubmissionStatus status) {

        ApplicationUser appUser = this.securityService.getCurrentApplicationUser();

        EisDataCriteria criteria = new EisDataCriteria()
            .withProgramSystemCode(appUser.getClientId())
            .withReportingYear(year)
            .withSubmissionStatus(status);

        EisDataListDto result = this.eisTransmissionService.retrieveSubmittableData(criteria);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/emissionsReport/stats",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get EIS stats by year",
        description = "Get EIS stats by year",
        tags = "EIS")
	public ResponseEntity<EisDataStatsDto> retrieveEisStatsByYear(@NotNull @RequestParam(value = "year") Short year) {

        ApplicationUser appUser = this.securityService.getCurrentApplicationUser();

        EisDataStatsDto result = this.eisTransmissionService.retrieveStatInfoByYear(appUser.getClientId(), year);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/history",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get EIS transaction history",
        description = "Get EIS transaction history",
        tags = "EIS")
    public ResponseEntity<List<EisTransactionHistoryDto>> retrieveEisTransactionHistory() {

        ApplicationUser appUser = this.securityService.getCurrentApplicationUser();

        List<EisTransactionHistoryDto> result = this.eisTransmissionService.retrieveTransactionHistory(appUser.getClientId());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/history/delete")
    @Operation(summary = "Delete transaction history",
        description = "Delete transaction history",
        tags = "EIS")
    public void deleteFromTransactionHistory(@NotNull @RequestBody List<Long> reportIds) {

        this.eisTransmissionService.deleteFromTransactionHistory(reportIds);
    }

    @GetMapping(value = "/history/attachment/{id}")
    @Operation(summary = "Download attachment",
        description = "Download attachment",
        tags = "EIS")
    public ResponseEntity<StreamingResponseBody> downloadAttachment(
            @NotNull @PathVariable Long id) {

        EisTransactionAttachmentDto result = attachmentService.findAttachmentById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(result.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + result.getFileName() + "\"")
                .body(outputStream -> {
                        attachmentService.writeFileTo(id, outputStream);
                });
    }


    /**
     * Save a report attachment for the chosen report
     * @param reportId
     * @param file
     * @param dto
     * @return
     */
    @PostMapping(value = "/history/attachment/uploadAttachment")
    @Operation(summary = "Upload attachment",
        description = "Upload attachment",
        tags = "EIS")
    public ResponseEntity<EisTransactionAttachmentDto> uploadAttachment(
        @NotBlank @RequestPart("file") MultipartFile file,
        @NotNull @RequestPart("metadata") EisTransactionAttachmentDto attachment)  {

        EisTransactionAttachmentDto result = null;
        HttpStatus status = HttpStatus.NO_CONTENT;

        try (TempFile tempFile = TempFile.from(file.getInputStream(), file.getOriginalFilename())) {

            logger.debug("Attachment filename {}", tempFile.getFileName());
            logger.debug("EisHistoryAttachmentDto {}", attachment);

            this.virusScanClient.scanFile(tempFile);

            String.format("%s %s",
                    securityService.getCurrentApplicationUser().getFirstName(),
                    securityService.getCurrentApplicationUser().getLastName());

            Path path = Paths.get(file.getOriginalFilename());
            attachment.setFileName(path.getFileName().toString());
            attachment.setFileType(file.getContentType());
            attachment.setTransactionHistoryId(attachment.getTransactionHistoryId());
            attachment.setAttachment(tempFile);

            result = attachmentService.saveAttachment(tempFile, attachment);

            status = HttpStatus.OK;

        } catch (VirusScanException e) {

            String msg = String.format("The uploaded file, '%s', is suspected of containing a threat " +
                    "such as a virus or malware and was deleted. The scanner responded with: '%s'.",
                file.getOriginalFilename(), e.getMessage());

            throw new ReportAttachmentValidationException(
                    Collections.singletonList(WorksheetError.createSystemError(msg)));

        } catch (IOException e) {

            throw new IllegalStateException(e);
        }

        return new ResponseEntity<>(result, status);

    }

    @ExceptionHandler(value = ReportAttachmentValidationException.class)
    public ResponseEntity<JsonNode> uploadValidationError(ReportAttachmentValidationException exception) {

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("failed", true);
        ArrayNode arrayNode = objectNode.putArray("errors");
        exception.getErrors().forEach(error -> arrayNode.add(objectMapper.convertValue(error, JsonNode.class)));

        return ResponseEntity.badRequest().body(objectNode);
    }

    /**
     * Delete a report attachment record for given id
     * @param id
     * @return
     */
    @DeleteMapping(value = "/history/attachment/{attachmentId}")
    @Operation(summary = "Delete attachment",
        description = "Delete attachment",
        tags = "EIS")
    public void deleteAttachment(@NotNull @PathVariable Long attachmentId) {

        attachmentService.deleteAttachment(attachmentId);
    }

    @GetMapping(value = "/emissionsReport/{reportId}",
        produces = MediaType.APPLICATION_XML_VALUE)
    @Operation(summary = "Get EIS XML",
        description = "Get EIS XML",
        tags = "EIS")
    public ResponseEntity<StreamingResponseBody> retrieveEisXml(
        @NotNull @PathVariable("reportId") Long reportId) {

        this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        ApplicationUser appUser = this.securityService.getCurrentApplicationUser();

        EisHeaderDto eisHeader = new EisHeaderDto()
            .withProgramSystemCode(appUser.getClientId())
            .withAuthorName(String.format("%s %s", appUser.getFirstName(), appUser.getLastName()))
            .withOrganizationName(appUser.getOrganization())
            .withSubmissionStatus(EisSubmissionStatus.ProdFacility)
            .withEmissionsReports(Collections.singletonList(reportId));

        return new ResponseEntity<>(outputStream -> {

            this.eisXmlService.writeEisXmlTo(eisHeader, outputStream);

        }, HttpStatus.OK);
    }
}
