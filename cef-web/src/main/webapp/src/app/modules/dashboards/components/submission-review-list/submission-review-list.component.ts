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
