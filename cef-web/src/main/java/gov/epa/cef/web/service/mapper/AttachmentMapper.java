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
package gov.epa.cef.web.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import gov.epa.cef.web.domain.Attachment;
import gov.epa.cef.web.service.dto.AttachmentDto;

@Mapper(componentModel = "spring", uses = {})
public interface AttachmentMapper {
	
	List<AttachmentDto> toDtoList(List<Attachment> attachmentsList);
	
	@Mapping(source="reportId", target="emissionsReport.id")
	@Mapping(source="communicationId", target="communication.id")
	@Mapping(target="attachment", ignore = true)
	Attachment fromDto(AttachmentDto source);
	
	@Mapping(source="emissionsReport.id", target="reportId")
	@Mapping(source="communication.id", target="communicationId")
	@Mapping(target="attachment", ignore = true)
	AttachmentDto toDto(Attachment attachment);
	
}
