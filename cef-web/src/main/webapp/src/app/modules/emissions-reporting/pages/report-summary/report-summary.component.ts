/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, ViewChild } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { ReportSummary } from 'src/app/shared/models/report-summary';
import { ReportService } from 'src/app/core/services/report.service';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { SharedService } from 'src/app/core/services/shared.service';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { EmissionsReportingService } from 'src/app/core/services/emissions-reporting.service';
import { ReportDownloadService } from 'src/app/core/services/report-download.service';
import { ConfigPropertyService } from 'src/app/core/services/config-property.service';
import { User } from 'src/app/shared/models/user';
import { ReportAttachmentTableComponent } from 'src/app/modules/shared/components/report-attachment-table/report-attachment-table.component';
import { SubmissionReviewModal } from 'src/app/modules/emissions-reporting/components/submission-review-modal/submission-review-modal.component';
import { ThresholdStatus } from 'src/app/shared/enums/threshold-status';
import {ToastrService} from "ngx-toastr";

declare const gtag: Function;

@Component({
  selector: 'app-report-summary',
  templateUrl: './report-summary.component.html',
  styleUrls: ['./report-summary.component.scss']
})
export class ReportSummaryComponent implements OnInit {
  facilitySite: FacilitySite;
  tableData: ReportSummary[];
  radiationData: ReportSummary[];
  emissionsReportYear: number;
  user: User;
  baseUrl: string;
  reportId: string;
  reportCertificationEnabled: boolean;

	@ViewChild(ReportAttachmentTableComponent)
	private reportAttachmentTableComponent: ReportAttachmentTableComponent;

    constructor(
        private router: Router,
        private reportService: ReportService,
        private route: ActivatedRoute,
        private sharedService: SharedService,
        private userContextService: UserContextService,
        private modalService: NgbModal,
        private emissionsReportingService: EmissionsReportingService,
        private reportDownloadService: ReportDownloadService,
        private propertyService: ConfigPropertyService,
        private toastr: ToastrService) { }

        nonDormantData = [];

    ngOnInit() {

        this.route.paramMap
        .subscribe(map => {
			this.baseUrl = `/facility/${map.get('facilityId')}/report/${map.get('reportId')}`;
            this.reportId = map.get('reportId');
        });

        this.route.data.subscribe(data => {

            this.facilitySite = data.facilitySite;
            this.emissionsReportYear = this.facilitySite.emissionsReport.year;

            if (this.facilitySite.id) {
				this.userContextService.getUser().subscribe(user => {
					this.user = user;
				});
				this.propertyService.retrieveReportCertificationEnabled()
					.subscribe(result => {
						this.reportCertificationEnabled = result;
					});

				const fromPreviousUrl = history.state.prevPage ?? null;
    			if (fromPreviousUrl) {
				if (data.fromUpload || data.fromCreateNew &&
					(!this.facilitySite.emissionsReport.thresholdStatus || this.facilitySite.emissionsReport?.thresholdStatus === 'OPERATING_ABOVE_THRESHOLD')) {
					this.emissionsReportingService.getReportChanges(Number(this.reportId)).subscribe((log) => {
						let modalMessage = null;
						let hasReportLog = log?.length > 0 ? true : false;
						let sourceText;

						const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'md' });
						modalRef.componentInstance.title = data.fromUpload ? 'Upload Successful' : 'Report Has Been Successfully Created';
						modalRef.componentInstance.singleButton = true;
						modalRef.componentInstance.confirmButtonText = 'Close';

						if (hasReportLog) {
							modalRef.componentInstance.singleButton = false;
							modalRef.componentInstance.confirmButtonText = 'View Log';
							modalRef.componentInstance.cancelButtonText = 'Close'

							sourceText = data.fromUpload ? `Your report has been successfully uploaded. ` : `Your report has been successfully created. `;
							modalMessage = sourceText + `As part of the report creation process CAERS will
							check for updated US EPA emission factors and attempt to automatically update those for you. CAERS also reconciles Emissions Unit,
							Processes, Controls and Control Paths by ensuring all operating components in the previous report are present in the current report.`
					        +`<br><br>Please view Report Creation Log to see any changes made to your report. `;

						} else {

							modalMessage = data.fromUpload ? `Your report has been successfully uploaded and is ready for you to work on. ` :
								`Your report has been successfully created and is ready for you to work on. `;

						}

						modalRef.componentInstance.htmlMessage = modalMessage;
				        modalRef.componentInstance.continue.subscribe((a) => {
							if (hasReportLog) {
								this.router.navigate([this.baseUrl+'/changes']);
							}
				        });
					});
				}
				}


                this.reportService.retrieve(this.facilitySite.emissionsReport.id)
                    .subscribe(pollutants => {
                    // filter out radiation pollutants to show separately at the end of the table
                    // (only radionucleides right now which is code 605)
                    this.tableData = pollutants.filter(pollutant => {
                        return pollutant.pollutantCode !== '605';
                    });

                    this.radiationData = pollutants.filter(pollutant => {
                        return pollutant.pollutantCode === '605';
                    });
                });
            }
            this.sharedService.emitChange(data.facilitySite);
        });

		this.sharedService.changeEmitted$
		.subscribe((result) => {
			if (result) {
				this.facilitySite = result;
			}
		});
    }

	operatingBelowThresholdNoAttachments() {
		return ((this.facilitySite.emissionsReport?.thresholdStatus === 'OPERATING_BELOW_THRESHOLD') && this.reportAttachmentTableComponent?.tableData?.length < 1);
	}


    /**
     * validate the report
     */
    validateReport() {

        this.router.navigate(['..', 'validation'], { relativeTo: this.route });
    }

    reopenReport() {
        const modalMessage = `Do you wish to reopen the ${this.facilitySite.emissionsReport.year} report for
        ${this.facilitySite.name}? This will reset the status of the report to "In progress" and you
        will need to resubmit the report to the S/L/T authority for review. `;
        const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'sm' });
        modalRef.componentInstance.message = modalMessage;
        modalRef.componentInstance.continue.subscribe(() => {
            const ids = [this.facilitySite.emissionsReport.id];
            this.resetReport(ids);
        });
    }

    resetReport(reportIds: number[]) {
        this.emissionsReportingService.resetReports(reportIds).subscribe(result => {
            location.reload();
        });
    }

    downloadReport(emissionsReportId: number, facilitySiteId: number, agencyFacilityIdentifier: string) {
        this.reportService.retrieveReportDownloadDto(emissionsReportId, facilitySiteId).subscribe(reportDownloadDto => {
            if ((this.facilitySite.emissionsReport.status === 'APPROVED') || (this.facilitySite.emissionsReport.status === 'SUBMITTED')) {
                this.reportDownloadService.downloadFile(reportDownloadDto, agencyFacilityIdentifier + '_' +
                this.facilitySite.emissionsReport.year + '_' + 'Emissions_Report' + '_Final_Submission');
            } else {
                this.reportDownloadService.downloadFile(reportDownloadDto, agencyFacilityIdentifier + '_' +
                this.facilitySite.emissionsReport.year + '_' + 'Emissions_Report' + '_Submission_In_Progress');
            }
        });
    }

    downloadSummaryReport(agencyFacilityIdentifier: string) {

        this.nonDormantData = this.tableData.filter((item) => !item.notReporting);

        if ((this.facilitySite.emissionsReport.status === 'APPROVED') || (this.facilitySite.emissionsReport.status === 'SUBMITTED')) {
            this.reportDownloadService.downloadReportSummary(this.nonDormantData, agencyFacilityIdentifier + '_' +
            this.facilitySite.emissionsReport.year + '_' + 'Report_Summary' + '_Final_Submission');
        } else {
            this.reportDownloadService.downloadReportSummary(this.nonDormantData, agencyFacilityIdentifier + '_' +
            this.facilitySite.emissionsReport.year + '_' + 'Report_Summary' + '_Submission_In_Progress');
        }
    }

    navigateToFeedbackPage(){
        this.router.navigateByUrl(this.baseUrl+'/userfeedback');
    }

  onCertifyAndSubmit(){
    if (this.facilitySite.emissionsReport.thresholdStatus &&
      this.facilitySite.emissionsReport.thresholdStatus !== ThresholdStatus.OPERATING_ABOVE_THRESHOLD
    ) {
      this.emissionsReportingService.validateThresholdNoQAReport(this.facilitySite.emissionsReport.id)
        .subscribe(() => {
          // Makes report in submission review modal have latest lastModifiedDate and validationStatus
          this.initSubmissionReviewModal();
      });
    } else {
      this.initSubmissionReviewModal();
    }
  }

  initSubmissionReviewModal() {
    const modalRef = this.modalService.open(SubmissionReviewModal, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.report = this.facilitySite.emissionsReport;
    modalRef.componentInstance.facility = this.facilitySite;
    modalRef.componentInstance.url = this.baseUrl;
    modalRef.componentInstance.semiannualReport = false;
  }

    notifyNeiCertifier(){
        this.emissionsReportingService.readyToCertifyNotification(Number(this.reportId), false)
            .subscribe(result => {
                if (result) {
                    this.toastr.success('', 'An email notification has been sent to the NEI Certifier(s) for this facility.');
                } else {
                    this.toastr.error('An error occurred and an email notification has not been sent.');
                }
            });
    }

}
