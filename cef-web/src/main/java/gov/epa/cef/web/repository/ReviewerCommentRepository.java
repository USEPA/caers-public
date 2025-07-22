/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
