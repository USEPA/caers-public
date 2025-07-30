/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.api.rest;

import gov.epa.cef.web.client.soap.VirusScanClient;
import gov.epa.cef.web.exception.ReportAttachmentValidationException;
import gov.epa.cef.web.exception.VirusScanException;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.AttachmentRepository;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.AttachmentService;
import gov.epa.cef.web.service.dto.AttachmentDto;
import gov.epa.cef.web.service.dto.bulkUpload.WorksheetError;
import gov.epa.cef.web.util.TempFile;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("api/reports/{reportId}/attachments")
public class AttachmentApi {

    private final AttachmentService attachmentService;

    private final SecurityService securityService;

    private final VirusScanClient virusScanClient;

    private ObjectMapper objectMapper;

    Logger LOGGER = LoggerFactory.getLogger(AttachmentApi.class);

    @Autowired
    AttachmentApi( SecurityService securityService,
    		AttachmentService attachmentService,
    		VirusScanClient virusScanClient,
    		ObjectMapper objectMapper) {

        this.virusScanClient = virusScanClient;
    	this.attachmentService = attachmentService;
    	this.securityService = securityService;
        this.objectMapper = objectMapper;
    }


    @GetMapping(value = "/{id}")
    @Operation(summary = "Attachments",
        description = "Get attachments information.",
        tags = "Attachments")
    public ResponseEntity<StreamingResponseBody> downloadAttachment(
    		@NotNull @PathVariable Long reportId,
    		@NotNull @PathVariable Long id) {
    	this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

    	AttachmentDto result = attachmentService.findAttachmentById(id);

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
    @PostMapping(value = "/uploadAttachment")
    @Operation(summary = "Upload Attachments",
        description = "Upload attachment.",
        tags = "Attachments")
    public ResponseEntity<AttachmentDto> uploadAttachment(
    	@NotBlank @RequestPart("file") MultipartFile file,
	    @NotNull @RequestPart("metadata") AttachmentDto reportAttachment,
	    @NotNull @PathVariable Long reportId)  {

    	this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        AttachmentDto result = null;
        HttpStatus status = HttpStatus.NO_CONTENT;

        try (TempFile tempFile = TempFile.from(file.getInputStream(), file.getOriginalFilename())) {

            LOGGER.debug("Attachment filename {}", tempFile.getFileName());
            LOGGER.debug("ReportAttachmentsDto {}", reportAttachment);

            this.virusScanClient.scanFile(tempFile);

            List<AttachmentDto> existingAttachments = attachmentService.findAllAttachmentsByReportId(reportId);
            for (AttachmentDto attachment : existingAttachments) {
            	if (file.getOriginalFilename().equals(attachment.getFileName())) {
            		String msg = String.format("The attached document %s does not have a unique name. "
                            + "Please update the file name.",
                            file.getOriginalFilename());

            		throw new ReportAttachmentValidationException(
                            Collections.singletonList(WorksheetError.createSystemError(msg)));
            	}
            }

            String.format("%s %s",
            		securityService.getCurrentApplicationUser().getFirstName(),
            		securityService.getCurrentApplicationUser().getLastName());

            Path path = Paths.get(file.getOriginalFilename());
        	reportAttachment.setFileName(path.getFileName().toString());
            reportAttachment.setFileType(file.getContentType());
            reportAttachment.setReportId(reportAttachment.getReportId());
            reportAttachment.setAttachment(tempFile);

            result = attachmentService.saveAttachment(tempFile, reportAttachment);

            status = HttpStatus.OK;

        } catch (VirusScanException e) {

        	String msg = String.format("The uploaded file, '%s', is suspected of containing a threat " +
                    "such as a virus or malware and was deleted. The scanner responded with: '%s'.",
                file.getOriginalFilename(), e.getMessage());

            throw new ReportAttachmentValidationException(
                    Collections.singletonList(WorksheetError.createSystemError(msg)));

        } catch (IOException e) {

            String msg = String.format("There was an issue during file upload. Please try again. If you continue to experience issues, "
                    + "please ensure that your file is not infected with a virus and reach out to the Helpdesk.",
                file.getOriginalFilename(), e.getMessage());

            throw new ReportAttachmentValidationException(
                    Collections.singletonList(WorksheetError.createSystemError(msg)));
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
    @DeleteMapping(value = "/{attachmentId}")
    @Operation(summary = "Attachments",
        description = "Delete attachments information.",
        tags = "Attachments")
    public void deleteAttachment(@NotNull @PathVariable Long reportId, @NotNull @PathVariable Long attachmentId) {
        this.securityService.facilityEnforcer().enforceEntity(attachmentId, AttachmentRepository.class);

        attachmentService.deleteAttachment(attachmentId);
    }
}
