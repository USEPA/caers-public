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
package gov.epa.cef.web.service.impl;

import gov.epa.cef.web.config.CefConfig;
import gov.epa.cef.web.domain.AttachmentMIMEType;
import gov.epa.cef.web.domain.ReportAction;
import gov.epa.cef.web.domain.Attachment;
import gov.epa.cef.web.domain.ReportHistory;
import gov.epa.cef.web.domain.SLTConfigProperty;
import gov.epa.cef.web.exception.NotExistException;
import gov.epa.cef.web.exception.ReportAttachmentValidationException;
import gov.epa.cef.web.repository.CommunicationRepository;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.AttachmentRepository;
import gov.epa.cef.web.repository.ReportHistoryRepository;
import gov.epa.cef.web.repository.SLTConfigRepository;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.service.EmissionsReportService;
import gov.epa.cef.web.service.AttachmentService;
import gov.epa.cef.web.service.ReportService;
import gov.epa.cef.web.service.UserService;
import gov.epa.cef.web.service.dto.AttachmentDto;
import gov.epa.cef.web.service.dto.UserDto;
import gov.epa.cef.web.service.dto.bulkUpload.WorksheetError;
import gov.epa.cef.web.service.mapper.AttachmentMapper;
import gov.epa.cef.web.util.TempFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

@Service
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private AttachmentRepository attachmentsRepo;
    
    @Autowired
    private ReportHistoryRepository reportHistoryRepo;
    
    @Autowired
    private CommunicationRepository commRepo;
    
    @Autowired
    private SLTConfigRepository sltConfigRepo;
    
    @Autowired
    private EmissionsReportRepository emissionsReportRepository;

    @Autowired
    private EmissionsReportStatusServiceImpl reportStatusService;
    
    @Autowired
    private AttachmentMapper attachmentMapper;
    
    @Autowired
    private ReportService reportService;
    
    @Autowired
    private CefConfig cefConfig;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private EmissionsReportService emissionsReportService;
    
    /***
     * Return attachment for the chosen attachment id
     * @param id
     * @return
     */
    public AttachmentDto findAttachmentById(Long id) {
		Attachment attachment = attachmentsRepo.findById(id).orElse(null);
		
		return attachmentMapper.toDto(attachment);
	}
    
    /***
     * Write file to output stream
     * @param id
     * @return
     */    
    public void writeFileTo (Long fileId, OutputStream outputStream) {
    	
    	attachmentsRepo.findById(fileId).ifPresent(file -> {
    		
    		if (file.getAttachment() != null) {
    			try (InputStream inputStream = file.getAttachment().getBinaryStream()) {
    				ByteStreams.copy(inputStream, outputStream);
    				
    			} catch (SQLException | IOException e) {
    				throw new IllegalStateException(e);
    			}
    		}
    	});
		
	}
    
    /**
     * Save a report attachment to the database.
     * @param file
     * @return
     */
    public AttachmentDto saveAttachment(@NotNull TempFile file, @NotNull AttachmentDto metadata) {

        UserDto appUser = this.userService.getCurrentUser();

    	Preconditions.checkArgument(file != null, "File can not be null");
    	
    	Attachment attachment = attachmentMapper.fromDto(metadata);
    	String programSystemCode;
    	
    	if (metadata.getReportId() != null) {
    		attachment.getEmissionsReport().setId(metadata.getReportId());
    		programSystemCode = emissionsReportService.findById(metadata.getReportId()).getProgramSystemCode().getCode();
    	} else {
    		attachment.setEmissionsReport(null);
    		programSystemCode = this.userService.getCurrentUser().getProgramSystemCode();
    	}
    	if (metadata.getCommunicationId() != null){
    		attachment.setCommunication(commRepo.findById(metadata.getCommunicationId()).orElse(null));
    	} else {
    		attachment.setCommunication(null);
    	}
    	
		 try {
		
			 attachment.setAttachment(file.createBlob());
		
		 } catch (IOException e) {
		
		     throw new IllegalStateException(e);
		 }
		 
		 if(file.length() > Long.valueOf(this.cefConfig.getMaxFileSize().replaceAll("[^0-9]", ""))*1048576) {
	         	String msg = String.format("The selected file size exceeds the maximum file upload size %d MB.",
	         			Long.valueOf(this.cefConfig.getMaxFileSize().replaceAll("[^0-9]", "")));

         	throw new ReportAttachmentValidationException(
                     Collections.singletonList(WorksheetError.createSystemError(msg)));
          }
         

		 final String csv = "csv";
         boolean acceptedType = false;
         String fileName = metadata.getFileName();
		 	for (AttachmentMIMEType type : AttachmentMIMEType.values()) {
	            if (type.label().equals(metadata.getFileType()) || fileName.substring(fileName.indexOf('.') + 1).equals(csv)) {
	            	acceptedType = true;
	            	break;
	            }
	         }
	        
        if (!acceptedType) {
        	String msg = String.format("The file extension '%s' is not in an accepted file format.",
        			(fileName.substring(fileName.indexOf('.'), fileName.length())));

        	throw new ReportAttachmentValidationException(
                    Collections.singletonList(WorksheetError.createSystemError(msg)));
        }
        
        boolean acceptedTypeSlt = false;
        
        String fileType = "";
        if (AttachmentMIMEType.fromLabel(metadata.getFileType()) != null) {
    		fileType = AttachmentMIMEType.fromLabel(metadata.getFileType()).code().toLowerCase();
    	}
        
	    String fileExtensionString = fileName.substring(fileName.indexOf('.') + 1, fileName.length());
	    List<SLTConfigProperty> permittedUploadTypes = sltConfigRepo.getPermittedReportUploadTypes(programSystemCode);
	    ArrayList<String> permittedUploadTypeExtensions = new ArrayList<String>();
	    
	    // for each type in upload types, add to list, check list
	    for (SLTConfigProperty uploadType : permittedUploadTypes) {
	    	String uploadTypeName = uploadType.getSltPropertyDetails().getName();
	    	// index + 1 to remove the period when comparing extensions
	    	String uploadTypeExtension = uploadTypeName.substring(uploadTypeName.indexOf('.') + 1, uploadTypeName.length());
	    	permittedUploadTypeExtensions.add(uploadTypeExtension);

            if (appUser.getRole().equals(AppRole.RoleType.REVIEWER.roleName())) {
                acceptedTypeSlt = true;
            } else {
                // statement after OR in place for handling CSV files - txt csv and xlsx csv are interpreted as different but need to be treated the same
                if (fileType.equals(uploadTypeExtension) || (permittedUploadTypeExtensions.contains(fileExtensionString) && fileExtensionString.equals(csv))) {
                    acceptedTypeSlt = true;
                }
            }
         }
	    
	    if (!acceptedTypeSlt) {
	    	final String joinedPermittedUploadTypes = String.join(", ", permittedUploadTypeExtensions);
        	String msg = String.format("The file extension '%s' is not an accepted file format for your agency. The following file extensions are acceptable for use: %s",
        			(fileName.substring(fileName.indexOf('.'), fileName.length())),
        			 joinedPermittedUploadTypes);

        	throw new ReportAttachmentValidationException(
                    Collections.singletonList(WorksheetError.createSystemError(msg)));
        }
		 
		Attachment result = attachmentsRepo.save(attachment);
		if (!this.userService.getCurrentUser().getRole().equalsIgnoreCase("Reviewer")) {
			reportService.createReportHistory(attachment.getEmissionsReport().getId(), ReportAction.ATTACHMENT, attachment.getComments(), result);
		}
		
		// Updates last modified date when attachment is added
        if (attachment.getEmissionsReport() != null) {
            this.emissionsReportRepository.findById(attachment.getEmissionsReport().getId()).ifPresent(report -> {
                report.setLastModifiedDate(new Date());
                this.emissionsReportRepository.save(report);
            });
        }

		return attachmentMapper.toDto(result);

    }
    
    /**
     * Delete report attachment record for a given id
     * @param id
     */
    public void deleteAttachment(Long id) {
    	Attachment attachment = attachmentsRepo.findById(id)
    			.orElseThrow(() -> new NotExistException("Report Attachment", id));
    	
    	String comment = "\"" + attachment.getFileName() + "\" was deleted.";
    	
    	reportService.createReportHistory(attachment.getEmissionsReport().getId(), ReportAction.ATTACHMENT_DELETED, comment);
    	ReportHistory history = reportHistoryRepo.findByAttachmentId(attachment.getId());
        reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(attachment.getId()), AttachmentRepository.class);

    	reportService.updateReportHistoryDeletedAttachment(history.getId(), true);
        attachmentsRepo.deleteById(id);
    }
    
    public List<AttachmentDto> findAllAttachmentsByReportId(Long reportId) {
    	List<Attachment> result = attachmentsRepo.findAllByReportId(reportId);
    	return attachmentMapper.toDtoList(result);
    }
}
