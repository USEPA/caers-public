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
import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {EntityType} from "../../../../shared/enums/entity-type";
import {ActivatedRoute} from "@angular/router";
import {SharedService} from "../../../../core/services/shared.service";
import {UserContextService} from "../../../../core/services/user-context.service";
import {FacilitySite} from "../../../../shared/models/facility-site";
import {UtilityService} from "../../../../core/services/utility.service";
import {ReviewerComment} from "../../../../shared/models/reviewer-comment";
import {ReviewerCommentService} from "../../../../core/services/reviewer-comment.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {CommentModalComponent} from "../../../shared/components/comment-modal/comment-modal.component";

@Component({
  selector: 'app-reviewer-comment',
  templateUrl: './reviewer-comment.component.html',
  styleUrls: ['./reviewer-comment.component.scss']
})
export class ReviewerCommentComponent implements OnInit, OnChanges {
    @Input() entityId: number;
    @Input() entityType: EntityType;
    facilitySite: FacilitySite;

    activeComment: ReviewerComment;
    draftComment: ReviewerComment;

    readOnlyMode = true;
    editInfo = false;
    unitId: number;

  constructor(private route: ActivatedRoute,
              private rcService: ReviewerCommentService,
              private sharedService: SharedService,
              private userContextService: UserContextService,
              private modalService: NgbModal) { }

  ngOnInit(): void {

  }

  ngOnChanges() {

      this.activeComment = null;
      this.draftComment = null;

      this.rcService.retrieveActive(this.entityId, this.entityType).subscribe(comment => {
          this.activeComment = comment;
      })

      this.rcService.retrieveDraft(this.entityId, this.entityType).subscribe(comment => {
          this.draftComment = comment;
      })

      this.route.data
          .subscribe((data: { facilitySite: FacilitySite }) => {
              this.facilitySite = data.facilitySite;
              this.userContextService.getUser().subscribe( user => {
                  if (!UtilityService.isInProgressStatus(data.facilitySite.emissionsReport.status) && user.isReviewer()) {
                      this.readOnlyMode = false;
                  }
              });
          });
  }

    openCommentModal() {

        const modalRef = this.modalService.open(CommentModalComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.title = 'Reviewer Comment';
        modalRef.componentInstance.message = 'Would you like to leave a reviewer comment for the reporter? The reporter will be able to see this comment after the report is rejected.';
        modalRef.componentInstance.defaultComment = this.draftComment?.message;

        modalRef.result.then((resp) => {
            if (this.draftComment?.id) {
                this.draftComment.message = resp;
                this.rcService.update(this.draftComment).subscribe((result => {
                    // subscribe is required for rest call to fire
                    // update draftComment with actual result to ensure UI and db are in sync
                    this.draftComment = result;
                }))

            } else {
                const newComment = new ReviewerComment();
                newComment.emissionsReportId = this.facilitySite.emissionsReport.id;
                newComment.entityId = this.entityId;
                newComment.entityType = this.entityType;
                newComment.message = resp;

                this.rcService.create(newComment).subscribe(result => {
                    this.draftComment = result;
                })
            }
        }, () => {
            // needed for dismissing without errors
        });
    }

}
