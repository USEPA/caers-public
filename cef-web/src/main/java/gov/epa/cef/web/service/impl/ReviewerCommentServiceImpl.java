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

import com.google.common.base.Strings;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.exception.ApplicationErrorCode;
import gov.epa.cef.web.exception.ApplicationException;
import gov.epa.cef.web.exception.NotExistException;
import gov.epa.cef.web.repository.*;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ReviewerCommentDto;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.mapper.ReviewerCommentMapper;
import gov.epa.cef.web.util.RepoLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewerCommentServiceImpl {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ReviewerCommentRepository rcRepo;

    @Autowired
    private ReportReviewRepository rrRepo;

    @Autowired
    private EmissionsReportRepository reportRepo;

    @Autowired
    private ReviewerCommentMapper rcMapper;

    @Autowired
    private RepoLocator repoLocator;

    public ReviewerCommentDto create(ReviewerCommentDto dto) {

        ReportReview review;

        Optional<ReportReview> draftReview = retrieveMostRecentReportReview(dto.getEmissionsReportId(), ReportReviewStatus.DRAFT);

        if (draftReview.isPresent()) {

            review = draftReview.get();

            Optional<ReviewerComment> existingComment = rcRepo.findDraftByEntity(dto.getEntityId(), dto.getEntityType());
            if (existingComment.isPresent()) {
                throw new ApplicationException(ApplicationErrorCode.E_COMMENT_ALREADY_EXISTS,
                    String.format("Comment for Entity {%s} with ID %d already exists.", dto.getEntityType().toString(), dto.getEntityId()));
            }

        } else {

            ReportReview activeReview = retrieveMostRecentReportReview(dto.getEmissionsReportId(), ReportReviewStatus.ACTIVE).orElse(null);

            if (activeReview != null) {

                review = rrRepo.save(new ReportReview(activeReview.getEmissionsReport(), activeReview.getVersion() + 1, ReportReviewStatus.DRAFT));
            } else {

                ReportReview lastReview = retrieveMostRecentReportReview(dto.getEmissionsReportId(), ReportReviewStatus.HISTORY).orElse(null);

                if (lastReview != null) {
                    review = rrRepo.save(new ReportReview(lastReview.getEmissionsReport(), lastReview.getVersion() + 1, ReportReviewStatus.DRAFT));
                } else {
                    EmissionsReport report = reportRepo.findById(dto.getEmissionsReportId())
                        .orElseThrow(() -> new NotExistException("Emissions Report", dto.getEmissionsReportId()));
                    review = rrRepo.save(new ReportReview(report, 0, ReportReviewStatus.DRAFT));
                }
            }
        }

        ValidationDetailDto details = retrieveEntityValidationDetail(dto.getEntityId(), dto.getEntityType());

        ReviewerComment comment = new ReviewerComment(review, dto.getMessage(), details);

        ReviewerCommentDto result = rcMapper.toDto(rcRepo.save(comment));

        return result;
    }

    public ReviewerCommentDto update(ReviewerCommentDto dto) {

        ReviewerComment comment = rcRepo.findById(dto.getId()).orElse(null);

        if (Strings.emptyToNull(dto.getMessage()) == null) {
            rcRepo.delete(comment);
            return rcMapper.toDto(null);
        } else {
            comment.setMessage(dto.getMessage());
            return rcMapper.toDto(rcRepo.save(comment));
        }

    }

    public List<ReviewerCommentDto> retrieveActiveForReport(Long reportId) {

        Optional<ReportReview> review = retrieveMostRecentReportReview(reportId, ReportReviewStatus.ACTIVE);

        return rcMapper.toDtoList(review.map(ReportReview::getReviewerComments).orElse(Collections.EMPTY_LIST));

    }

    public ReviewerCommentDto retrieveActiveByEntity(Long entityId, EntityType entityType) {

        Optional<ReviewerComment> comments = rcRepo.findActiveByEntity(entityId, entityType);
        return rcMapper.toDto(comments.orElse(null));
    }

    public ReviewerCommentDto retrieveDraftByEntity(Long entityId, EntityType entityType) {

        Optional<ReviewerComment> comments = rcRepo.findDraftByEntity(entityId, entityType);
        return rcMapper.toDto(comments.orElse(null));
    }

    public List<ReviewerCommentDto> retrieveMostRecentByEntity(Long entityId, EntityType entityType) {
        List<ReviewerComment> result = rcRepo.findMostRecentByEntity(entityId, entityType);
        return rcMapper.toDtoList(result);
    }

    private Optional<ReportReview> retrieveMostRecentReportReview(Long entityId, ReportReviewStatus status) {
        return rrRepo.findFirstByEmissionsReportIdAndStatusOrderByVersionDesc(entityId, status);
    }

    private ValidationDetailDto retrieveEntityValidationDetail(Long entityId, EntityType entityType) {

        ValidationDetailDto details = null;

        switch(entityType) {
            case EMISSIONS_UNIT:
                details = repoLocator.getReportComponentRepository(EmissionsUnitRepository.class).retrieveReportComponentById(entityId)
                    .map(item -> item.getComponentDetails())
                    .orElseThrow(() -> new NotExistException("Emissions Unit", entityId));
                break;
            case EMISSIONS_PROCESS:
                details = repoLocator.getReportComponentRepository(EmissionsProcessRepository.class).retrieveReportComponentById(entityId)
                    .map(item -> item.getComponentDetails())
                    .orElseThrow(() -> new NotExistException("Emissions Process", entityId));
                break;
            case CONTROL:
                details = repoLocator.getReportComponentRepository(ControlRepository.class).retrieveReportComponentById(entityId)
                    .map(item -> item.getComponentDetails())
                    .orElseThrow(() -> new NotExistException("Control", entityId));
                break;
            case CONTROL_PATH:
                details = repoLocator.getReportComponentRepository(ControlPathRepository.class).retrieveReportComponentById(entityId)
                    .map(item -> item.getComponentDetails())
                    .orElseThrow(() -> new NotExistException("Path", entityId));
                break;
            case EMISSION:
                details = repoLocator.getReportComponentRepository(EmissionRepository.class).retrieveReportComponentById(entityId)
                    .map(item -> item.getComponentDetails())
                    .orElseThrow(() -> new NotExistException("Emission", entityId));
                break;
            case EMISSIONS_REPORT:
                break;
            case FACILITY_SITE:
                details = repoLocator.getReportComponentRepository(FacilitySiteRepository.class).retrieveReportComponentById(entityId)
                    .map(item -> item.getComponentDetails())
                    .orElseThrow(() -> new NotExistException("Facility Site", entityId));
                break;
            case OPERATING_DETAIL:
                details = repoLocator.getReportComponentRepository(OperatingDetailRepository.class).retrieveReportComponentById(entityId)
                    .map(item -> item.getComponentDetails())
                    .orElseThrow(() -> new NotExistException("Operating Detail", entityId));
                break;
            case RELEASE_POINT:
                details = repoLocator.getReportComponentRepository(ReleasePointRepository.class).retrieveReportComponentById(entityId)
                    .map(item -> item.getComponentDetails())
                    .orElseThrow(() -> new NotExistException("Release Point", entityId));
                break;
            case REPORTING_PERIOD:
                details = repoLocator.getReportComponentRepository(ReportingPeriodRepository.class).retrieveReportComponentById(entityId)
                    .map(item -> item.getComponentDetails())
                    .orElseThrow(() -> new NotExistException("Reporting Period", entityId));
                break;
            case REPORT_ATTACHMENT:
                break;
            default:
                break;

        }

        return details;
    }
}
