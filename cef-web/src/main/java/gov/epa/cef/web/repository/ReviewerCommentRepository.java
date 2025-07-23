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
package gov.epa.cef.web.repository;

import gov.epa.cef.web.domain.ReportReviewStatus;
import gov.epa.cef.web.domain.ReviewerComment;
import gov.epa.cef.web.service.dto.EntityType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewerCommentRepository extends CrudRepository<ReviewerComment, Long> {

    @Query("select rc from ReviewerComment rc join rc.reportReview rr where rc.entityId = :entityId and rc.entityType = :entityType and rr.status = 'ACTIVE'")
    Optional<ReviewerComment> findActiveByEntity(Long entityId, EntityType entityType);

    @Query("select rc from ReviewerComment rc join rc.reportReview rr where rc.entityId = :entityId and rc.entityType = :entityType and rr.status = 'DRAFT'")
    Optional<ReviewerComment> findDraftByEntity(Long entityId, EntityType entityType);

    @Query("select rc from ReviewerComment rc join rc.reportReview rr where rc.entityId = :entityId and rc.entityType = :entityType order by rr.version desc")
    List<ReviewerComment> findMostRecentByEntity(Long entityId, EntityType entityType);
}
