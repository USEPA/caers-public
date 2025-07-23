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
import { DatePipe } from "@angular/common";
import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { NgbActiveModal, NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { ToastrService } from "ngx-toastr";
import { Subject } from "rxjs";
import { AttachmentService } from "src/app/core/services/attachment.service";
import { ConfigPropertyService } from "src/app/core/services/config-property.service";
import { EmissionsReportingService } from "src/app/core/services/emissions-reporting.service";
import { FacilitySiteService } from "src/app/core/services/facility-site.service";
import { FileDownloadService } from "src/app/core/services/file-download.service";
import { ReportService } from "src/app/core/services/report.service";
import { SharedService } from "src/app/core/services/shared.service";
import { UserContextService } from "src/app/core/services/user-context.service";
import { UserService } from "src/app/core/services/user.service";
import { UtilityService } from "src/app/core/services/utility.service";
import { ConfirmationDialogComponent } from "src/app/shared/components/confirmation-dialog/confirmation-dialog.component";
import { AppRole } from "src/app/shared/enums/app-role";
import { ReportStatus } from "src/app/shared/enums/report-status";
import { ThresholdStatus } from "src/app/shared/enums/threshold-status";
import { ValidationStatus } from "src/app/shared/enums/validation-status.enum";
import { EmissionsReport } from "src/app/shared/models/emissions-report";
import { FacilitySite } from "src/app/shared/models/facility-site";
import { ReportHistory } from "src/app/shared/models/report-history";
import { EmissionUnitService } from "../../../../core/services/emission-unit.service";

declare const initCromerrWidget: any;
declare const gtag: Function;

@Component({
  selector: 'app-submission-review-modal',
  templateUrl: './submission-review-modal.component.html',
  styleUrls: ['./submission-review-modal.component.scss']
})
export class SubmissionReviewModal implements OnInit {

	@Input() report: EmissionsReport;
	@Input() facility: FacilitySite;
	@Input() url: string;
    @Input() semiannualReport: boolean;
	@Output() facilitySite: EventEmitter<any> = new EventEmitter();
	tableData: ReportHistory[];
	acknowledgmentChecked = false;
	fileDownloading = false;
	cromerrLoaded = false;
	cromerrLoadedEmitter = new Subject<boolean>();
	feedbackSubmitted: boolean;
    feedbackEnabled: boolean;
	lastModifiedDate: Date;

	constructor(
		public datepipe: DatePipe,
		public activeModal: NgbActiveModal,
		private modalService: NgbModal,
		private toastr: ToastrService,
		private reportAttachmentService: AttachmentService,
		private facilitySiteService: FacilitySiteService,
		private reportService: EmissionsReportingService,
		private rptService: ReportService,
		private fileDownloadService: FileDownloadService,
		private userService: UserService,
		private userContextService: UserContextService,
		private propertyService: ConfigPropertyService,
		private sharedService: SharedService,
        private emissionsUnitService: EmissionUnitService) { }

	ngOnInit() {
		this.cromerrLoadedEmitter
        .subscribe(result => {
            this.cromerrLoaded = result;
        });

		this.feedbackSubmitted = this.report.hasSubmitted;

		this.propertyService.retrieveUserFeedbackEnabled()
			.subscribe(result => {
				this.feedbackEnabled = result;
				this.userService.getCurrentUserNaasToken()
		            .subscribe(userToken => {
		                this.userContextService.getUser().subscribe( user => {
		                    if (user.isNeiCertifier() && this.report.status !== ReportStatus.SUBMITTED) {
		                        initCromerrWidget(user.cdxUserId, user.userRoleId, userToken.baseServiceUrl,
		                            this.report.id, this.report.masterFacilityRecordId, this.toastr,
		                            this.cromerrLoadedEmitter, this.feedbackEnabled, this.feedbackSubmitted,
                                    this.semiannualReport);
		                    }
		            });
		        });
		});

		this.reportService.getReport(this.report.id)
		.subscribe(report => {
			this.lastModifiedDate = report.lastModifiedDate;
		});

		// retrieve certifier and preparer attachments
		this.sharedService.emitReportIdChange(this.report.id);
		this.rptService.retrieveHistory(this.report.id, this.facility.id)
		.subscribe(attachments => {
			this.tableData = attachments.filter(data =>
                data.userRole !== AppRole.REVIEWER && data.fileName && data.fileName.length > 0 && !data.fileDeleted)
                .sort((a, b) => (a.actionDate < b.actionDate) ? 1 : -1);
		})

	}

	// currently unused, but leaving for reference or future use
    downloadPdfCor(report: EmissionsReport) {

        let reportFacility: FacilitySite;
        this.facilitySiteService.retrieveForReport(report.id)
            .subscribe(result => {
                reportFacility = result;
                this.fileDownloading = true;
                this.lastModifiedDate = result.emissionsReport.lastModifiedDate;
            });

        this.reportService.generateCopyOfRecord(report.id, this.semiannualReport,false)
            .subscribe(file => {
                this.fileDownloading = false;

                // when being run on tomcat this call will return success with an empty body
                // instead of an error when the call times out
                if (file.size > 0) {
                    this.fileDownloadService.downloadFile(file,
                        UtilityService.removeSpecialCharacters(`${reportFacility.agencyFacilityIdentifier}-${reportFacility.name}-${report.year}.pdf`));
                } else {
                    this.openDownloadFailure();
                    this.fileDownloading = false;
                }

            }, error => {
                this.openDownloadFailure();
                this.fileDownloading = false;
            });
    }

	acknowledgment(acknowledgmentChecked: boolean) {
		this.acknowledgmentChecked = !acknowledgmentChecked
	}

	onContinue() {

		this.reportService.getReport(this.report.id)
		.subscribe(report => {
			const lastModTimestamp = this.datepipe.transform(report.lastModifiedDate,'long');
			let modalMessage;
			// Compares last modified date when modal was opened or when template was downloaded to date when proceed button clicked.
			// If timestamps are the same, proceed to certify and submit.
			if (report.lastModifiedDate === this.lastModifiedDate
				&& (((report.validationStatus === ValidationStatus.SEMIANNUAL_PASSED || report.validationStatus === ValidationStatus.SEMIANNUAL_PASSED_WARNINGS) && this.semiannualReport)
                    || ((report.validationStatus === ValidationStatus.PASSED || report.validationStatus === ValidationStatus.PASSED_WARNINGS) && !this.semiannualReport))) {

				// Trigger click event to open cromerr widget modal. The cromerr widget is no longer linked to the proceed button to prevent
				// the cromerr modal from showing when timestamps do not match.
				const buttonElement = document.getElementById('certifyAndSubmit') as HTMLElement;
				buttonElement.click();
				this.activeModal.close(true);

                this.emissionsUnitService.retrieveForFacility(this.facility.id)
                    .subscribe( eus => {

                        gtag('event', 'report-submitted', { 'event_category': 'report-unit-count',
                            'event_label': `${this.report.id}-${this.report.programSystemCode.code}-${this.facility.agencyFacilityIdentifier}-${this.report.year}`,
                            'value':  eus.length});
                    });

			} else {
				if (this.report.thresholdStatus && this.report.thresholdStatus !== ThresholdStatus.OPERATING_ABOVE_THRESHOLD) {
					// Message for if timestamps do not match and report passed validation for Threshold statuses that do not have ability to run QA check.
					modalMessage = `<p>Emissions Report has been modified on <br>${lastModTimestamp}.</p>
										<p>Please restart submission review process.</p>`;
				} else {
					if (report.validationStatus !== ValidationStatus.PASSED && report.validationStatus !== ValidationStatus.PASSED_WARNINGS
                        && report.validationStatus !== ValidationStatus.SEMIANNUAL_PASSED && report.validationStatus !== ValidationStatus.SEMIANNUAL_PASSED_WARNINGS) {
						// Message for if timestamps do not match and report did not pass validation (if template downloaded after report was updated).
						modalMessage = `<p>Emissions Report has been modified on <br>${lastModTimestamp}.</p>
										<p>Please run quality checks and restart submission review process.</p>`;

					} else {
						// Message for if timestamps do not match and report passed validation.
						modalMessage = `<p>Emissions Report has been modified on <br>${lastModTimestamp}.</p>
										<p>Please restart submission review process.</p>`;
					}
				}
				this.onTimestampNoMatch(modalMessage);
				this.activeModal.close(true);
			}
		});
	}

	onTimestampNoMatch(modalMessage: string) {

		const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'md' });
	    modalRef.componentInstance.htmlMessageCenter = modalMessage;
		modalRef.componentInstance.confirmButtonText = 'Close'
		modalRef.componentInstance.title = 'Emissions Report Changed'
		modalRef.componentInstance.singleButton = true;
	    modalRef.result.then(() => {
			this.facilitySiteService.retrieveForReport(this.report.id)
			.subscribe(result => {
				this.sharedService.emitChange(result);
			});
	    }, () => {
            // needed for dismissing without errors
        });
	}

	onCancel() {
		this.activeModal.close(false);
	}

	onClose() {
		this.activeModal.close(false);
	}

	openDownloadFailure() {

        const modalMessage = `The Excel Template for this report could not be generated due to a large number of users
                              attempting to download Excel Templates in the system. Please try again in a few minutes.`;

        const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'lg' });
        modalRef.componentInstance.title = 'Template Download Failed';
        modalRef.componentInstance.message = modalMessage;
        modalRef.componentInstance.singleButton = true;
    }

	download(data: ReportHistory) {
        this.reportAttachmentService.downloadAttachment(data.reportAttachmentId)
        .subscribe(file => {
            this.fileDownloadService.downloadFile(file, data.fileName);
            error => console.error(error);
        });
    }

}
