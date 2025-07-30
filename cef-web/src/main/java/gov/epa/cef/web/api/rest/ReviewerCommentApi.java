/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
