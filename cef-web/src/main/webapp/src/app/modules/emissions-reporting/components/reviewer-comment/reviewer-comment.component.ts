/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
