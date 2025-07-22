/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit } from '@angular/core';
import { SubmissionUnderReview } from 'src/app/shared/models/submission-under-review';
import { SubmissionsReviewDashboardService } from 'src/app/core/services/submissions-review-dashboard.service';
import { SharedService } from 'src/app/core/services/shared.service';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { User } from 'src/app/shared/models/user';
import { ConfigPropertyService } from 'src/app/core/services/config-property.service';
import { UserFacilityAssociationService } from 'src/app/core/services/user-facility-association.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Component({
  selector: 'app-notification-list',
  templateUrl: './notification-list.component.html',
  styleUrls: ['./notification-list.component.scss']
})
export class NotificationListComponent implements OnInit {

  summarySubmissions: SubmissionUnderReview[];
  submitted: SubmissionUnderReview[];
  submittedCount: number = 0;
  inProgress: SubmissionUnderReview[];
  inProgressCount: number = 0;
  returned: SubmissionUnderReview[];
  returnedCount: number = 0;
  advancedQA: SubmissionUnderReview[];
  advancedQACount: number = 0;
  approved: SubmissionUnderReview[];
  approvedCount: number = 0;
  currentUser: User;
  currentYear: any;

  programSystemCodes: string[];
  announcementMap = new Map<string, SafeHtml>();

  constructor(private submissionsReviewDashboardService: SubmissionsReviewDashboardService,
              private sharedService: SharedService,
              public userContext: UserContextService,
			  private propertyService: ConfigPropertyService,
			  private ufaService: UserFacilityAssociationService,
			  private sanitizer: DomSanitizer) {

	  this.currentYear = new Date().getFullYear() - 1;
	
      this.sharedService.submissionReviewChangeEmitted$
      .subscribe(submissions => {
        this.filterAndCountSubmissions(submissions);
      });

      this.userContext.getUser().subscribe(user => {
        this.currentUser = user;
        if (this.currentUser.isReviewer()) {
          this.submissionsReviewDashboardService.retrieveReviewerSubmissions(this.currentYear, null)
          .subscribe(submissions => {
            this.filterAndCountSubmissions(submissions);
          });
        }
      });

	  this.ufaService.getMyProgramSystemCodeAssociations()
		.subscribe(programSystemCodes => {
			this.programSystemCodes = programSystemCodes;
			
		  if (this.programSystemCodes.length > 0) {
			this.programSystemCodes.forEach(code => {
				this.propertyService.retrieveSltAnnouncementEnabled(code)
		        .subscribe(result => {
	
		            if (result) {
		                this.propertyService.retrieveSltAnnouncementText(code)
		                .subscribe(text => {
							if (text.value && text.value.trim().length > 0) {
								// bypassing HTML sanitation is done to preserve styles when drawing the announcements
								this.announcementMap.set(code, this.sanitizer.bypassSecurityTrustHtml(text.value));
							}
		                });
		            }
	
		        });
			});
	    }
 	  })
  }

  ngOnInit() {
  }

  filterAndCountSubmissions(submissions) {
      this.approvedCount = this.advancedQACount = this.submittedCount = this.inProgressCount = this.returnedCount = 0;
      submissions.forEach(submission => {
        if (submission.reportStatus === 'APPROVED') {
          this.approvedCount++; 
        }
        if (submission.reportStatus === 'SUBMITTED') {
          this.submittedCount++;
        }
        if (submission.reportStatus === 'IN_PROGRESS'){
          this.inProgressCount++;
        }
        if (submission.reportStatus === 'RETURNED'){
          this.returnedCount++;
        }
        if (submission.reportStatus === 'ADVANCED_QA'){
          this.advancedQACount++;
        }
      });
  }

}