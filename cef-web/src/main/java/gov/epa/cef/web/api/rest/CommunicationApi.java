/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.api.rest;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import gov.epa.cef.web.service.dto.CommunicationHolderDto;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import gov.epa.cef.web.service.CommunicationService;
import gov.epa.cef.web.client.soap.VirusScanClient;
import gov.epa.cef.web.domain.Communication;
import gov.epa.cef.web.exception.ReportAttachmentValidationException;
import gov.epa.cef.web.exception.VirusScanException;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.AttachmentService;
import gov.epa.cef.web.service.dto.CommunicationDto;
import gov.epa.cef.web.service.dto.bulkUpload.WorksheetError;
import gov.epa.cef.web.service.dto.AttachmentDto;
import gov.epa.cef.web.service.mapper.CommunicationMapper;
import gov.epa.cef.web.util.TempFile;

@RestController
@RequestMapping("api/communication")
public class CommunicationApi {

	private final AttachmentService attachmentService;

	private final CommunicationService commService;

	private final CommunicationMapper commMapper;

	private final VirusScanClient virusScanClient;

	private final SecurityService securityService;

	private ObjectMapper objectMapper;

	Logger logger = LoggerFactory.getLogger(CommunicationApi.class);

	@Autowired
	CommunicationApi(AttachmentService attachmentService,
			CommunicationService commService,
			SecurityService securityService,
			VirusScanClient virusScanClient,
			CommunicationMapper commMapper,
			ObjectMapper objectMapper) {
		this.attachmentService = attachmentService;
		this.commService = commService;
		this.securityService = securityService;
		this.virusScanClient = virusScanClient;
		this.commMapper = commMapper;
		this.objectMapper = objectMapper;
	}

	/**
     * Save a communication attachment
     * @param file
     * @param metadata
     * @return
     */
    @PostMapping(value = "/uploadAttachment")
    @Operation(summary = "Upload Communication Attachment",
        description = "Upload communication attachment",
        tags = "Communication")
    public ResponseEntity<AttachmentDto> uploadCommunicationAttachment(
    	@NotBlank @RequestPart("file") MultipartFile file,
	    @NotNull @RequestPart("metadata") CommunicationHolderDto metadata)  {

    	AttachmentDto result = new AttachmentDto();
    	HttpStatus status = HttpStatus.NO_CONTENT;
    	Communication comm = commService.save(metadata.getCommunication(), metadata.getReportStatus(), metadata.getUserRole(), metadata.getIndustrySector(), metadata.getReportYear());
    	try (TempFile tempFile = TempFile.from(file.getInputStream(), file.getOriginalFilename())) {

    		logger.debug("Attachment filename {}", tempFile.getFileName());
    		logger.debug("AttachmentsDto {}", result);

            this.virusScanClient.scanFile(tempFile);

            String.format("%s %s",
            		securityService.getCurrentApplicationUser().getFirstName(),
            		securityService.getCurrentApplicationUser().getLastName());

            Path path = Paths.get(file.getOriginalFilename());
            result.setFileName(path.getFileName().toString());
            result.setFileType(file.getContentType());
            result.setAttachment(tempFile);


            result.setCommunicationId(comm.getId());
            result = attachmentService.saveAttachment(tempFile, result);
            this.commService.sendNotification(comm, file);
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

    	commService.deleteAllEmailStatusNotSent();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("failed", true);
        ArrayNode arrayNode = objectNode.putArray("errors");
        exception.getErrors().forEach(error -> arrayNode.add(objectMapper.convertValue(error, JsonNode.class)));

        return ResponseEntity.badRequest().body(objectNode);
    }

    /**
     * Send an email notification to the users that match the given parameters
     * @param communication
     * @return
     */
    @PutMapping(value = "/sendEmailNotification")
    @Operation(summary = "Send email notification",
        description = "Send email notification",
        tags = "Communication")
    public ResponseEntity<CommunicationDto> sendEmailNotification(@NotNull @RequestBody CommunicationHolderDto communication) {

        Communication comm = commService.save(communication.getCommunication(), communication.getReportStatus(), communication.getUserRole(), communication.getIndustrySector(), communication.getReportYear());
        CommunicationDto result = commMapper.toDto(this.commService.sendNotification(comm));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
