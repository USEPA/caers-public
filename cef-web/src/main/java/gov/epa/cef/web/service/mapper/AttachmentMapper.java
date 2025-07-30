/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
