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
package gov.epa.cef.web.api.rest;

import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ReviewerCommentDto;
import gov.epa.cef.web.service.impl.ReviewerCommentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/reviewer/comment")
public class ReviewerCommentApi {

    private final ReviewerCommentServiceImpl reviewerCommentService;

    private final SecurityService securityService;

    public ReviewerCommentApi(SecurityService securityService,
                              ReviewerCommentServiceImpl reviewerCommentService) {

        this.securityService = securityService;
        this.reviewerCommentService = reviewerCommentService;
    }

    @PostMapping
    @Operation(summary = "Create reviewer comment",
        description = "Create reviewer comment",
        tags = "Reviewer Comment")
    public ResponseEntity<ReviewerCommentDto> createComment(
        @RequestBody ReviewerCommentDto dto) {

        this.securityService.facilityEnforcer().enforceEntity(dto.getEmissionsReportId(), EmissionsReportRepository.class);

        ReviewerCommentDto result = reviewerCommentService.create(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update reviewer comment",
        description = "Update reviewer comment",
        tags = "Reviewer Comment")
    public ResponseEntity<ReviewerCommentDto> updateComment(
        @NotNull @PathVariable Long id, @RequestBody ReviewerCommentDto dto) {

        this.securityService.facilityEnforcer().enforceEntity(dto.getEmissionsReportId(), EmissionsReportRepository.class);

        ReviewerCommentDto result = reviewerCommentService.update(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{reportId}/active")
    @Operation(summary = "Get reviewer comment active",
        description = "Get reviewer comment active",
        tags = "Reviewer Comment")
    public ResponseEntity<List<ReviewerCommentDto>> retrieveActive(@NotNull @PathVariable Long reportId) {

        this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        List<ReviewerCommentDto> result = reviewerCommentService.retrieveActiveForReport(reportId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{entityType}/{entityId}/active")
    @Operation(summary = "Get reviewer comment active",
        description = "Get reviewer comment active",
        tags = "Reviewer Comment")
    public ResponseEntity<ReviewerCommentDto> retrieveActive(@NotNull @PathVariable EntityType entityType, @NotNull @PathVariable Long entityId) {

        ReviewerCommentDto result = reviewerCommentService.retrieveActiveByEntity(entityId, entityType);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{entityType}/{entityId}/draft")
    @Operation(summary = "Get reviewer comment draft",
        description = "Get reviewer comment draft",
        tags = "Reviewer Comment")
    public ResponseEntity<ReviewerCommentDto> retrieveDraft(@NotNull @PathVariable EntityType entityType, @NotNull @PathVariable Long entityId) {

        ReviewerCommentDto result = reviewerCommentService.retrieveDraftByEntity(entityId, entityType);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
