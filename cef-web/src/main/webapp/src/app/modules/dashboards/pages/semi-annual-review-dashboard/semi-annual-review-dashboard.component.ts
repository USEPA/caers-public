/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, ViewChild } from '@angular/core';
import { SubmissionsReviewDashboardService } from 'src/app/core/services/submissions-review-dashboard.service';
import { SubmissionUnderReview } from 'src/app/shared/models/submission-under-review';
import { EmissionsReportingService } from 'src/app/core/services/emissions-reporting.service';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { EmissionsReportAgencyData } from 'src/app/shared/models/emissions-report-agency-data';
import { User } from 'src/app/shared/models/user';
import { MonthlySubmissionReviewListComponent } from 'src/app/modules/dashboards/components/monthly-submission-review-list/monthly-submission-review-list.component';
import { ReportStatus } from 'src/app/shared/enums/report-status';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SubmissionReviewModalComponent } from 'src/app/modules/dashboards/components/submission-review-modal/submission-review-modal.component';
import { FileAttachmentModalComponent } from "../../../shared/components/file-attachment-modal/file-attachment-modal.component";

@Component( {
    selector: 'app-semi-annual-review-dashboard',
    templateUrl: './semi-annual-review-dashboard.component.html',
    styleUrls: ['./semi-annual-review-dashboard.component.scss']
} )
export class SemiAnnualReviewDashboardComponent implements OnInit {

    @ViewChild(MonthlySubmissionReviewListComponent, {static: true})

	private listComponent: MonthlySubmissionReviewListComponent;

    allSubmissions: SubmissionUnderReview[] = [];
    submissions: SubmissionUnderReview[] = [];
    user: User;
    hideButtons = false;
	invalidSelection = false;
	cannotReject = false;
    currentYear: number;
    selectedYear: number;
    selectedMidYearReportStatus = ReportStatus.SUBMITTED;
    selectedAgency: EmissionsReportAgencyData;
    selectedIndustrySector: string;

    agencyDataValues: EmissionsReportAgencyData[];
    yearValues: number[] = [];
    industrySectors: string[] = [];

    monthlyReportStatus = ReportStatus;

    submittedCount: 0;
    inProgressCount: 0;
    returnedCount: 0;
    approvedCount: 0;

    constructor(
        private userContext: UserContextService,
        private emissionReportService: EmissionsReportingService,
        private submissionsReviewDashboardService: SubmissionsReviewDashboardService,
		private modalService: NgbModal) { }

    ngOnInit() {

        this.userContext.getUser()
        .subscribe(user => {

            this.user = user;

			this.emissionReportService.getAgencyMonthlyReportedYears()
            .subscribe(result => {

				this.agencyDataValues = result.sort((a, b) => (a.programSystemCode.code > b.programSystemCode.code) ? 1 : -1);

                if (this.user?.isReviewer()) {
                    const userAgency = this.agencyDataValues.find(item => item.programSystemCode.code === this.user.programSystemCode);
                    this.selectedAgency = userAgency;
                    this.selectedYear = this.selectedAgency?.years[0];
                    this.refreshFacilityReports();
                }

			});

            this.currentYear = new Date().getFullYear();

            if (this.user?.isReviewer()) {
                this.submissionsReviewDashboardService.retrieveReviewerSemiAnnualSubmissions(this.currentYear, null)
                .subscribe(submissions => {
                    this.filterAndCountSubmissions(submissions);
                });
            }

			if (this.user?.isAdmin()) {
                this.submissionsReviewDashboardService.retrieveSemiAnnualSubmissionsByAgency(this.currentYear, null, null)
                .subscribe(submissions => {
                    this.filterAndCountSubmissions(submissions);
                });
            }

        });
    }

	onApprove() {
		const selectedSubmissions = this.listComponent.tableData.filter(item => item.checked).map(item => item.emissionsReportId);

		if (!selectedSubmissions.length) {
            this.invalidSelection = true;
        } else {
			this.invalidSelection = false;
            const modalRef = this.modalService.open(SubmissionReviewModalComponent, { size: 'lg', backdrop: 'static' });
            modalRef.componentInstance.title = 'Accept Submissions';
            modalRef.componentInstance.message = 'Would you like to accept the selected Semi-Annual Report submissions?';

            modalRef.result.then((comments) => {
               this.emissionReportService.acceptSemiAnnualReports(selectedSubmissions, comments)
                .subscribe(() => {
                    this.refreshFacilityReports();
					this.submissionsReviewDashboardService.retrieveReviewerSemiAnnualSubmissions(this.currentYear, null)
	                .subscribe(submissions => {
	                    this.filterAndCountSubmissions(submissions);
	                });
                });
            }, () => {
                // needed for dismissing without errors
            });
		}
	}

	onReject() {
		const selectedSubmissions = this.listComponent.tableData.filter(item => item.checked).map(item => item.emissionsReportId);
		const annualSubmitted = this.listComponent.tableData.filter(item => item.checked).filter(item => item.reportStatus === 'SUBMITTED' || item.reportStatus === 'APPROVED' || item.reportStatus === 'ADVANCED_QA');
		this.cannotReject = annualSubmitted.length > 0 ? true: false;

		if (!selectedSubmissions.length) {
            this.invalidSelection = true;
        } else if (!this.cannotReject) {
			this.invalidSelection = false;
            const modalRef = this.modalService.open(FileAttachmentModalComponent, { size: 'lg', backdrop: 'static' });
            modalRef.componentInstance.reportId = selectedSubmissions[0];
            modalRef.componentInstance.title = 'Reject Submissions';
            modalRef.componentInstance.message = 'Would you like to reject the selected Semi-Annual Report submissions?';

            modalRef.result.then((resp) => {
                this.emissionReportService.rejectSemiAnnualReports(selectedSubmissions, resp.comments, resp.id)
                .subscribe(() => {
                    this.refreshFacilityReports();
	                this.submissionsReviewDashboardService.retrieveReviewerSubmissions(this.currentYear, null)
	                .subscribe(submissions => {
	                    this.filterAndCountSubmissions(submissions);
	                });
                });
            }, () => {
                // needed for dismissing without errors
            });
		}
	}

    refreshFacilityReports(): void {
		this.invalidSelection = false;
		this.cannotReject = false;
        if (this.user.isReviewer()) {
            this.submissionsReviewDashboardService.retrieveReviewerSemiAnnualSubmissions(this.selectedYear, this.selectedMidYearReportStatus)
            .subscribe((submissions) => {
                this.sortSubmissions(submissions);
            });
        } else if (this.user.isAdmin() && this.selectedAgency) {
            this.submissionsReviewDashboardService.retrieveSemiAnnualSubmissionsByAgency(
                this.selectedYear,
                this.selectedMidYearReportStatus,
                this.selectedAgency.programSystemCode.code)
            .subscribe((submissions) => {
                this.sortSubmissions(submissions);
            });
        } else {
            this.sortSubmissions([]);
		}
    }

    sortSubmissions(submissions: SubmissionUnderReview[]) {
        this.allSubmissions = submissions.sort((a, b) => (a.facilityName > b.facilityName) ? 1 : -1);
        // map down to industry values, convert to a Set to get distinct values, remove nulls, and then sort
        this.industrySectors = [...new Set(this.allSubmissions.map(item => item.industry))].filter(item => item != null).sort();
        this.filterSubmissions();
    }

    filterSubmissions() {
        // reset selected sector if the current selection is no longer in the dropdown
        if (this.selectedIndustrySector && !this.industrySectors.includes(this.selectedIndustrySector)) {
            this.selectedIndustrySector = null;
        }
        if (this.selectedIndustrySector) {
            this.submissions = this.allSubmissions.filter(item => item.industry === this.selectedIndustrySector);
        } else {
            this.submissions = this.allSubmissions;
        }
    }

    onStatusSelected() {
        if (this.selectedMidYearReportStatus === this.monthlyReportStatus.SUBMITTED) {
            this.hideButtons = false;
        } else {
            this.hideButtons = true;
        }
        this.refreshFacilityReports();
    }

    onIndustrySelected(industry: string) {
        this.selectedIndustrySector = industry;
        this.filterSubmissions();
    }

    onAgencySelected() {

        if (this.selectedAgency?.years && !(this.selectedYear && this.selectedAgency.years.includes(+this.selectedYear))) {
            this.selectedYear = this.selectedAgency.years[0];
        }
        this.refreshFacilityReports();
		if (this.selectedAgency.programSystemCode) {
          this.submissionsReviewDashboardService.retrieveSemiAnnualSubmissionsByAgency(this.currentYear, null, this.selectedAgency.programSystemCode.code)
          .subscribe(submissions => {
              this.filterAndCountSubmissions(submissions);
          });
	    } else {
          this.filterAndCountSubmissions([]);
		}
    }

    filterAndCountSubmissions(submissions){
      this.approvedCount = this.submittedCount = this.inProgressCount = this.returnedCount = 0;
      submissions.forEach(submission => {

        if (submission.midYearSubmissionStatus === this.monthlyReportStatus.APPROVED) {
          this.approvedCount++;
        }
        if (submission.midYearSubmissionStatus === this.monthlyReportStatus.SUBMITTED) {
          this.submittedCount++;
        }
        if (submission.midYearSubmissionStatus === this.monthlyReportStatus.IN_PROGRESS){
          this.inProgressCount++;
        }
        if (submission.midYearSubmissionStatus === this.monthlyReportStatus.RETURNED){
          this.returnedCount++;
        }
      });
    }

    onFilter(midYearSubmissionStatus: ReportStatus) {
      this.selectedMidYearReportStatus = midYearSubmissionStatus;
      this.selectedYear = this.currentYear;
	  this.onStatusSelected();
    }
}
