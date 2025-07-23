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
import gov.epa.cef.web.domain.EisTransactionAttachment;
import gov.epa.cef.web.exception.ReportAttachmentValidationException;
import gov.epa.cef.web.repository.EisTransactionAttachmentRepository;
import gov.epa.cef.web.service.dto.EisTransactionAttachmentDto;
import gov.epa.cef.web.service.dto.bulkUpload.WorksheetError;
import gov.epa.cef.web.service.mapper.EisTransactionMapper;
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
import java.util.Collections;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

@Service
@Transactional
public class EisAttachmentServiceImpl {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EisTransactionAttachmentRepository attachmentRepo;

    @Autowired
    private EisTransactionMapper attachmentMapper;

    @Autowired
    private CefConfig cefConfig;

    /***
     * Return attachment for the chosen attachment id
     * @param id
     * @return
     */
    public EisTransactionAttachmentDto findAttachmentById(Long id) {
		EisTransactionAttachment attachment = attachmentRepo.findById(id).orElse(null);
		
		return attachmentMapper.attachmentToDto(attachment);
	}
    
    /***
     * Write file to output stream
     * @param id
     * @return
     */    
    public void writeFileTo (Long fileId, OutputStream outputStream) {
    	
        attachmentRepo.findById(fileId).ifPresent(file -> {
    		
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
     * Save an attachment to the database.
     * @param file
     * @return
     */
    public EisTransactionAttachmentDto saveAttachment(@NotNull TempFile file, @NotNull EisTransactionAttachmentDto metadata) {

    	Preconditions.checkArgument(file != null, "File can not be null");

    	EisTransactionAttachment attachment = attachmentMapper.attachmentFromDto(metadata);
    	attachment.getTransactionHistory().setId(metadata.getTransactionHistoryId());

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

         boolean acceptedType = false;
         String fileName = metadata.getFileName();
		 	for (AttachmentMIMEType type : AttachmentMIMEType.values()) {
	            if (type.label().equals(metadata.getFileType())) {
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
		 
	        EisTransactionAttachment result = attachmentRepo.save(attachment);

		return attachmentMapper.attachmentToDto(result);

    }
    
    /**
     * Delete attachment record for a given id
     * @param id
     */
    public void deleteAttachment(Long id) {
//        EisTransactionAttachment attachment = attachmentRepo.findById(id)
//    			.orElseThrow(() -> new NotExistException("Eis Transaction History Attachment", id));

        attachmentRepo.deleteById(id);
    }
}
