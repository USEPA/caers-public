/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { SubmissionUnderReview } from 'src/app/shared/models/submission-under-review';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ReportSummaryModalComponent } from 'src/app/modules/dashboards/components/report-summary-modal/report-summary-modal.component';
import { ReportService } from 'src/app/core/services/report.service';
import { ReportDownloadService } from 'src/app/core/services/report-download.service';
import { EmissionService } from 'src/app/core/services/emission.service';
import { EmissionsReportingService } from 'src/app/core/services/emissions-reporting.service';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { ToastrService } from 'ngx-toastr';
import { ConfigPropertyService } from 'src/app/core/services/config-property.service';
import { User } from 'src/app/shared/models/user';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { FacilitySiteService } from 'src/app/core/services/facility-site.service';
import { FileDownloadService } from 'src/app/core/services/file-download.service';
import { BusyModalComponent } from 'src/app/shared/components/busy-modal/busy-modal.component';
import { UtilityService } from 'src/app/core/services/utility.service';


@Component({
  selector: 'app-submission-review-list',
  templateUrl: './submission-review-list.component.html',
  styleUrls: ['./submission-review-list.component.scss']})

export class SubmissionReviewListComponent extends BaseSortableTable implements OnInit, OnChanges {

  @Input() tableData: SubmissionUnderReview[];
  @Input() reportStatus: string;
  @Input() filterText: string;
  @Input() user: User;
  @Output() refreshSubmissions = new EventEmitter();

  reviewer: boolean;
  admin: boolean;
  monthlyReportingEnabled = false;

  matchFunction: (item: any, searchTerm: any) => boolean = this.matches;

  constructor(
    private modalService: NgbModal,
    private reportService: ReportService,
    private reportDownloadService: ReportDownloadService,
    private emissionService: EmissionService,
    private emissionsReportService: EmissionsReportingService,
    private facilitySiteService: FacilitySiteService,
    private fileDownloadService: FileDownloadService,
    private toastr: ToastrService,
    private propertyService: ConfigPropertyService)
  {
    super();
  }

  ngOnChanges(changes: SimpleChanges): void {
	this.controller.searchTerm = this.filterText || '';
  }

  ngOnInit() {
    this.controller.paginate = true;

	  if (this.user?.isReviewer()) {
		  this.propertyService.retrieveSltMonthlyFuelReportingEnabled(this.user.programSystemCode)
		    .subscribe(result => {
		      this.monthlyReportingEnabled = result;
		    });
	  }

	  this.reviewer = this.user?.isReviewer();
	  this.admin = this.user?.isAdmin();

  }

  openSummaryModal(submission: SubmissionUnderReview) {
    const modalWindow = this.modalService.open(ReportSummaryModalComponent, { size: 'lg' });
    modalWindow.componentInstance.submission = submission;
  }

  downloadReport(reportId: number, facilitySiteId: number, year: number, agencyFacilityIdentifier: number, reportStatus: string){
    this.reportService.retrieveReportDownloadDto(reportId, facilitySiteId)
    .subscribe(reportDownloadDto => {
      if ((reportStatus==='APPROVED') || (reportStatus==='SUBMITTED')) {
        this.reportDownloadService.downloadFile(reportDownloadDto, agencyFacilityIdentifier +'_'+
        year +'_' + 'Emissions_Report' + '_Final_Submission');
      } else {
        this.reportDownloadService.downloadFile(reportDownloadDto, agencyFacilityIdentifier +'_'+
        year +'_' + 'Emissions_Report' + '_Submission_In_Progress');
      }
    });
  }

  deleteReport(agencyFacilityIdentifier: string, reportId: number, reportYear: number) {
    this.emissionsReportService.delete(reportId).subscribe(() => {
      this.toastr.success('', `The ${reportYear} Emissions Report from this Facility (Agency Id ${agencyFacilityIdentifier}) has been deleted successfully.`);
	  this.refreshSubmissions.emit();
    });
  }

  openDeleteModal(agencyFacilityIdentifier: string, reportId: number, reportYear: number) {
    const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'sm' });
    const modalMessage = `Are you sure you want to delete the ${reportYear} Emissions Report from this Facility
      					  (Agency Id ${agencyFacilityIdentifier})? This will also remove any Facility Information, Emission Units,
						  Control Devices, and Release Point associated with this report.`;
    modalRef.componentInstance.message = modalMessage;
    modalRef.componentInstance.continue.subscribe(() => {
      this.deleteReport(agencyFacilityIdentifier, reportId, reportYear);
    });
  }

  matches(item: SubmissionUnderReview, searchTerm: string): boolean {
    const term = searchTerm.toLowerCase();
    return item.facilityName?.toLowerCase().includes(term)
      || item.agencyFacilityIdentifier?.toString().includes(term)
	  || item.operatingStatus?.toLowerCase().includes(term)
	  || item.lastSubmittalYear?.toString().includes(term);
  }

  downloadMonthlyReport(facilitySiteId: number, reportId: number, reportYear: number){
    this.emissionService.retrieveMonthlyReportingDownloadDtoList(reportId, false)
      .subscribe(result => {
        this.reportDownloadService.downloadMonthlyReport(result, facilitySiteId + '_' +
        reportYear + '_' + 'Monthly_Report');
      });
  }

  downloadExcelTemplate(report) {

		let reportFacility: FacilitySite;
		this.facilitySiteService.retrieveForReport(report.emissionsReportId)
			.subscribe(result => {
				reportFacility = result;
			});

		const modalWindow = this.modalService.open(BusyModalComponent, {
			backdrop: 'static',
			size: 'lg'
		});

		modalWindow.componentInstance.message = 'Please wait while we generate the Excel Template for this report. This may take a few minutes.';

		this.emissionsReportService.downloadExcelExport(report.emissionsReportId)
			.subscribe(file => {
				modalWindow.dismiss();

				// when being run on tomcat this call will return success with an empty body
				// instead of an error when the call times out
				if (file.size > 0) {
					this.fileDownloadService.downloadFile(file, UtilityService.removeSpecialCharacters(`${reportFacility.agencyFacilityIdentifier}-${reportFacility.name}-${report.year}.xlsx`));
				} else {
					this.openDownloadFailure();
				}

			}, error => {
				modalWindow.dismiss();
				this.openDownloadFailure();
			});
  }

  openDownloadFailure() {

        const modalMessage = `The Excel Template for this report could not be generated due to a large number of users
                              attempting to download Excel Templates in the system. Please try again in a few minutes.`;

        const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'lg' });
        modalRef.componentInstance.title = 'Template Download Failed';
        modalRef.componentInstance.message = modalMessage;
        modalRef.componentInstance.singleButton = true;
  }

  downloadJsonTemplate(report) {

        let reportFacility: FacilitySite;
        this.facilitySiteService.retrieveForReport(report.emissionsReportId)
        .subscribe(result => {
            reportFacility = result;
        });

        this.emissionsReportService.downloadJsonExport(report.emissionsReportId)
        .subscribe(file => {

            // when being run on tomcat this call will return success with an empty body
            // instead of an error when the call times out
            this.fileDownloadService.downloadFile(file, UtilityService.removeSpecialCharacters(`${reportFacility.agencyFacilityIdentifier}-${reportFacility.name}-${report.year}.json`));

        }, error => {
        });
  }

}
