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
import { Component, OnInit, ViewChild } from '@angular/core';
import { SubmissionsReviewDashboardService } from 'src/app/core/services/submissions-review-dashboard.service';
import { SubmissionUnderReview } from 'src/app/shared/models/submission-under-review';
import { SubmissionReviewListComponent } from 'src/app/modules/dashboards/components/submission-review-list/submission-review-list.component';
import { EmissionsReportingService } from 'src/app/core/services/emissions-reporting.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SubmissionReviewModalComponent } from 'src/app/modules/dashboards/components/submission-review-modal/submission-review-modal.component';
import { FileAttachmentModalComponent } from 'src/app/modules/shared/components/file-attachment-modal/file-attachment-modal.component';
import { ReportStatus } from 'src/app/shared/enums/report-status';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { EmissionsReportAgencyData } from 'src/app/shared/models/emissions-report-agency-data';
import { User } from 'src/app/shared/models/user';
import { ConfigPropertyService } from 'src/app/core/services/config-property.service';
import { ThresholdStatus } from 'src/app/shared/enums/threshold-status';

@Component( {
    selector: 'app-submission-review-dashboard',
    templateUrl: './submission-review-dashboard.component.html',
    styleUrls: ['./submission-review-dashboard.component.scss']
} )
export class SubmissionReviewDashboardComponent implements OnInit {

    @ViewChild(SubmissionReviewListComponent, {static: true})

    private listComponent: SubmissionReviewListComponent;

    allSubmissions: SubmissionUnderReview[] = [];
    submissions: SubmissionUnderReview[] = [];
    user: User;
    hideAcceptButton = false;
    invalidSelection = false;
    currentYear: number;
    submissionStatusYear: number;
    selectedYear: number;
    selectedReportStatus = ReportStatus.SUBMITTED;
    selectedAgency: EmissionsReportAgencyData;
    selectedIndustrySector: string;
    showNotStartedOption = false;

    agencyDataValues: EmissionsReportAgencyData[];
    yearValues: number[] = [];
    industrySectors: string[] = [];

    reportStatus = ReportStatus;
    sltThresholdScreenEnabled = false;
    readonly ALL: string = 'ALL';
    readonly OPT_OUT_SUBMITTED: string = 'OPT_OUT_SUBMITTED';
    readonly OPT_OUT_APPROVED: string = 'OPT_OUT_APPROVED';
    isOptOut = null;
    allCount = 0;
    submittedCount = 0;
    inProgressCount = 0;
    returnedCount = 0;
    advancedQACount = 0;
    approvedCount = 0;
    optOutSubmitted = 0;
    optOutApproved = 0;
    notStartedCount = 0;

    filterText: string;

    constructor(
        private userContext: UserContextService,
        private emissionReportService: EmissionsReportingService,
        private submissionsReviewDashboardService: SubmissionsReviewDashboardService,
        private modalService: NgbModal,
        private propertyService: ConfigPropertyService) { }

    ngOnInit() {

        this.userContext.getUser()
        .subscribe(user => {

            this.user = user;

            this.emissionReportService.getAgencyReportedYears()
            .subscribe(agencyReportedYears => {

                this.agencyDataValues = agencyReportedYears.sort((a, b) => (a.programSystemCode.code > b.programSystemCode.code) ? 1 : -1);

                if (this.user.isReviewer()) {
                    this.selectedAgency = this.agencyDataValues.find
                        (item => item.programSystemCode.code === this.user.programSystemCode);
                    this.selectedYear = this.selectedAgency?.years[0];
                    this.onYearSelected();
                }

                this.currentYear = new Date().getFullYear() - 1;

                if (this.user.isReviewer()) {
                    this.propertyService.retrieveSLTThresholdScreeningGADNREnabled(this.user.programSystemCode)
                    .subscribe(sltThresholdScreenEnabled => {
                      this.sltThresholdScreenEnabled = sltThresholdScreenEnabled;
                    });

                    this.propertyService.retrieveSltMonthlyFuelReportingEnabled(this.user.programSystemCode)
                        .subscribe(monthlyEnabled => {
                          this.getYearDropdownOptions(this.selectedAgency, monthlyEnabled);
                          this.setSubmissionStatusSummary(this.currentYear);
                        });
                }
                if (this.user.isAdmin()) {
                    this.setSubmissionStatusSummary(this.currentYear);
                }
            });
        });
    }

    onBeginAdvancedQA() {
        const selectedSubmissions = this.listComponent.tableData.filter(item => item.checked).map(item => item.emissionsReportId);

        if (!selectedSubmissions.length) {
            this.invalidSelection = true;
        } else {
            this.invalidSelection = false;
            this.emissionReportService.beginAdvancedQA(selectedSubmissions)
            .subscribe(() => {
                this.refreshFacilityReports();
                this.setSubmissionStatusSummary(this.currentYear);
            });
        }
    }

    onApprove() {
        const selectedSubmissions = this.listComponent.tableData.filter(item => item.checked).map(item => item.emissionsReportId);

        if (!selectedSubmissions.length) {
            this.invalidSelection = true;
        } else {
            this.invalidSelection = false;
            const modalRef = this.modalService.open(SubmissionReviewModalComponent, { size: 'lg', backdrop: 'static' });
            modalRef.componentInstance.title = 'Accept Submissions';
            modalRef.componentInstance.message = 'Would you like to accept the selected submissions?';

            modalRef.result.then((comments) => {
                this.emissionReportService.acceptReports(selectedSubmissions, comments)
                .subscribe(() => {
                    this.refreshFacilityReports();
                    this.setSubmissionStatusSummary(this.currentYear);
                });
            }, () => {
                // needed for dismissing without errors
            });
        }
    }

    onReject() {
        const selectedSubmissions = this.listComponent.tableData.filter(item => item.checked).map(item => item.emissionsReportId);

        if (!selectedSubmissions.length) {
            this.invalidSelection = true;
        } else {
            this.invalidSelection = false;
            const modalRef = this.modalService.open(FileAttachmentModalComponent, { size: 'lg', backdrop: 'static' });
            modalRef.componentInstance.reportId = selectedSubmissions[0];
            modalRef.componentInstance.programSystemCode = this.user.programSystemCode;
            modalRef.componentInstance.title = 'Reject Submissions';
            modalRef.componentInstance.message = 'Would you like to reject the selected submissions?';

            modalRef.result.then((resp) => {
                this.emissionReportService.rejectReports(selectedSubmissions, resp.comments, resp.id)
                .subscribe(() => {
                    this.refreshFacilityReports();
                    this.setSubmissionStatusSummary(this.currentYear);
                });
            }, () => {
                // needed for dismissing without errors
            });
        }
    }

    refreshFacilityReports(): void {
        if (this.user.isReviewer()) {
            if (this.selectedReportStatus === this.ALL) {
                this.submissionsReviewDashboardService.retrieveReviewerSubmissions(this.selectedYear, null)
                    .subscribe((submissions) => {
                        this.sortSubmissions(submissions);
                    });
            } else if (this.selectedReportStatus === ReportStatus.NOT_STARTED) {
                this.retrieveNotStartedReports(this.selectedYear);
            } else {
                this.submissionsReviewDashboardService.retrieveReviewerSubmissions(this.selectedYear, this.selectedReportStatus)
                    .subscribe((submissions) => {
                        this.sortSubmissions(submissions);
                    });
            }
        } else if (this.user.isAdmin() && this.selectedAgency) {
            if (this.selectedReportStatus === this.ALL) {
                this.submissionsReviewDashboardService.retrieveSubmissions(
                    this.selectedYear,
                    null,
                    this.selectedAgency.programSystemCode.code)
                    .subscribe((submissions) => {
                        this.sortSubmissions(submissions);
                    });
            } else if (this.selectedReportStatus === ReportStatus.NOT_STARTED) {
                this.retrieveNotStartedReports(this.selectedYear);
            } else {
                this.submissionsReviewDashboardService.retrieveSubmissions(
                    this.selectedYear,
                    this.selectedReportStatus,
                    this.selectedAgency.programSystemCode.code)
                    .subscribe((submissions) => {
                        this.sortSubmissions(submissions);
                    });
            }
        } else {
            this.sortSubmissions([]);
        }
    }

    onYearSelected() {
        if (Number(this.selectedYear) === this.currentYear || Number(this.selectedYear) === this.currentYear + 1) {
            this.showNotStartedOption = true;
        } else {
            if (this.selectedReportStatus === ReportStatus.NOT_STARTED) {
                this.selectedReportStatus = ReportStatus.SUBMITTED;
            }
            this.showNotStartedOption = false;
        }
        this.setSubmissionStatusSummary(this.selectedYear);
        this.onStatusSelected();
    }

    retrieveNotStartedReports(year) {
        if (this.user.isReviewer()) {
            this.submissionsReviewDashboardService.retrieveReviewerNotStartedSubmissions(year)
                .subscribe(noReportList => {
                    if (this.currentYear === year) {
                        this.notStartedCount = noReportList.length;
                    }
                    if (this.selectedReportStatus === ReportStatus.NOT_STARTED) {
                        this.sortSubmissions(noReportList);
                    }
                });
        }
        if (this.user.isAdmin()) {
            this.submissionsReviewDashboardService.retrieveNotStartedSubmissions(year, this.selectedAgency?.programSystemCode.code)
                .subscribe(noReportList => {
                    if (this.currentYear === year) {
                        this.notStartedCount = noReportList.length;
                    }
                    if (this.selectedReportStatus === ReportStatus.NOT_STARTED) {
                        this.sortSubmissions(noReportList);
                    }
                });
        }
    }

    getYearDropdownOptions(agency, monthlyEnabled) {
        this.yearValues = [];
        agency?.years.forEach(year => this.yearValues.push(year));
        if (!this.yearValues.includes(this.currentYear + 1) && monthlyEnabled) {
            this.yearValues.unshift(this.currentYear + 1);
        }
        if (this.selectedAgency?.years && !(this.selectedYear && this.selectedAgency.years.includes(+this.selectedYear))) {
            this.selectedYear = this.selectedAgency.years[0];
        }
        this.onYearSelected();
    }

    sortSubmissions(submissions: SubmissionUnderReview[]) {
        this.allSubmissions = submissions.sort
        ((a, b) =>
            (a.facilityName > b.facilityName) ? 1 : -1
        );
        // map down to industry values, convert to a Set to get distinct values, remove nulls, and then sort
        this.industrySectors = [...new Set(this.allSubmissions
            .map(item => item.industry))]
            .filter(item => item != null)
            .sort();
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
        this.hideAcceptButton = !(this.selectedReportStatus === ReportStatus.SUBMITTED
            || this.selectedReportStatus === ReportStatus.ADVANCED_QA
            || this.selectedReportStatus === this.OPT_OUT_SUBMITTED);
        this.isOptOut = (this.selectedReportStatus === this.OPT_OUT_SUBMITTED
                || this.selectedReportStatus === this.OPT_OUT_APPROVED);
        this.refreshFacilityReports();
    }

    onIndustrySelected(industry: string) {
        this.selectedIndustrySector = industry;
        this.filterSubmissions();
    }


    onAgencySelected() {

        if (this.selectedAgency.programSystemCode) {
            this.propertyService.retrieveSLTThresholdScreeningGADNREnabled(this.selectedAgency.programSystemCode.code)
                .subscribe(result => {
                    this.sltThresholdScreenEnabled = result;
                });

            this.propertyService.retrieveSltMonthlyFuelReportingEnabled(this.selectedAgency.programSystemCode.code)
                .subscribe(result => {
                    this.getYearDropdownOptions(this.selectedAgency, result);
                });
        }
    }

    setSubmissionStatusSummary(year: number) {
        if (this.user.isReviewer()) {
            this.submissionsReviewDashboardService.retrieveReviewerSubmissions(year, null)
                .subscribe(submissions => {
                    this.setSubmissionStatusSummaryValues(year, submissions);
                });
        } else if (this.user.isAdmin()) {
            this.submissionsReviewDashboardService.retrieveSubmissions(
                year,
                null,
                this.selectedAgency?.programSystemCode?.code ?? null)
                .subscribe(submissions => {
                    this.setSubmissionStatusSummaryValues(year, submissions);
                });
        } else {
            return;
        }
    }

    setSubmissionStatusSummaryValues(year: number, submissions: SubmissionUnderReview[]) {
        this.submissionStatusYear = year;
        this.notStartedCount = this.approvedCount =
            this.advancedQACount = this.submittedCount =
                this.inProgressCount = this.returnedCount =
                    this.optOutSubmitted = this.optOutApproved = 0;
        this.allCount = submissions.length;

        submissions.forEach(submission => {
            if (submission.reportStatus === ReportStatus.APPROVED
                && submission.thresholdStatus !== ThresholdStatus.OPERATING_BELOW_THRESHOLD) {
                this.approvedCount++;
            }
            if (submission.reportStatus === ReportStatus.SUBMITTED
                && submission.thresholdStatus !== ThresholdStatus.OPERATING_BELOW_THRESHOLD) {
                this.submittedCount++;
            }
            if (submission.reportStatus === ReportStatus.IN_PROGRESS) {
                this.inProgressCount++;
            }
            if (submission.reportStatus === ReportStatus.RETURNED) {
                this.returnedCount++;
            }
            if (submission.reportStatus === ReportStatus.ADVANCED_QA) {
                this.advancedQACount++;
            }
            if (submission.reportStatus === ReportStatus.SUBMITTED
                && submission.thresholdStatus === ThresholdStatus.OPERATING_BELOW_THRESHOLD) {
                this.optOutSubmitted++;
            }
            if (submission.reportStatus === ReportStatus.APPROVED
                && submission.thresholdStatus === ThresholdStatus.OPERATING_BELOW_THRESHOLD) {
                this.optOutApproved++;
            }
            if (submission.reportStatus === ReportStatus.NOT_STARTED) {
                this.notStartedCount++;
            }
        });
    }

    onFilter(reportStatus: ReportStatus) {
      this.selectedReportStatus = reportStatus;
      this.selectedYear = this.submissionStatusYear;
      this.onYearSelected();
    }

    onSubmissionDeleted() {
        this.refreshFacilityReports();
        this.setSubmissionStatusSummary(this.currentYear);
    }

    onClearFilterClick() {
        this.filterText = '';
    }

}
