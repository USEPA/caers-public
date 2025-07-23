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
package gov.epa.cef.web.service;

import java.io.OutputStream;
import java.util.List;

import gov.epa.cef.web.service.dto.AttachmentDto;
import gov.epa.cef.web.util.TempFile;

public interface AttachmentService {
	
	/***
     * Return attachment for the chosen attachment id
     * @param id
     * @return
     */
	 AttachmentDto findAttachmentById(Long id);
	
	/***
     * Write file to output stream
     * @param id
     * @return
     */
	void writeFileTo(Long id, OutputStream outputStream);
    
    /**
     * Save a report attachment
     * @param dto
     * @return
     */
    AttachmentDto saveAttachment(TempFile file, AttachmentDto metadata);
    
    /**
     * Delete an report attachment record for a given id
     * @param id
     */
    void deleteAttachment(Long id);
    
    /**
     * Return all attachments for the chosen report
     * @param id
     * @return
     */
    List<AttachmentDto> findAllAttachmentsByReportId(Long reportId);
}