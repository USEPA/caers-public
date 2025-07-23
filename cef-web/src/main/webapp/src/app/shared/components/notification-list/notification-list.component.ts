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