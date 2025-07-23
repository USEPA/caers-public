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

import gov.epa.cef.web.domain.ReviewerComment;
import gov.epa.cef.web.service.dto.ReviewerCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface ReviewerCommentMapper {

    ReviewerComment fromDto(ReviewerCommentDto dto);

    @Mapping(source="reportReview.emissionsReport.id", target="emissionsReportId")
    @Mapping(source="reportReview.status", target="status")
    @Mapping(source="reportReview.version", target="version")
    ReviewerCommentDto toDto(ReviewerComment source);

	List<ReviewerCommentDto> toDtoList(List<ReviewerComment> source);

}
