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
import { EmissionsReport } from 'src/app/shared/models/emissions-report';
import { EmissionsReportingService } from 'src/app/core/services/emissions-reporting.service';
import { Component, OnInit, ViewChild, TemplateRef, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { concatMap, filter } from 'rxjs/operators';
import { SharedService } from 'src/app/core/services/shared.service';
import { BusyModalComponent } from 'src/app/shared/components/busy-modal/busy-modal.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { FacilitySiteService } from 'src/app/core/services/facility-site.service';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { BaseCodeLookup } from 'src/app/shared/models/base-code-lookup';
import { LookupService } from 'src/app/core/services/lookup.service';
import { FileDownloadService } from 'src/app/core/services/file-download.service';
import { ConfigPropertyService } from 'src/app/core/services/config-property.service';
import { MasterFacilityRecord } from 'src/app/shared/models/master-facility-record';
import { UtilityService } from 'src/app/core/services/utility.service';
import {
    ThresholdScreeningGadnrModalComponent
} from 'src/app/modules/emissions-reporting/components/threshold-screening-gadnr-modal/threshold-screening-gadnr-modal.component';
import { ThresholdStatus } from 'src/app/shared/enums/threshold-status';
import {
    ThresholdResetConfirmationModalComponent
} from 'src/app/modules/emissions-reporting/components/threshold-reset-confirmation-modal/threshold-reset-confirmation-modal.component';
import {
    EditFacilityOperatingStatusModalComponent
} from 'src/app/modules/emissions-reporting/components/edit-facility-operating-status-modal/edit-facility-operating-status-modal.component';

@Component({
    selector: 'app-emissions-reporting-dashboard',
    templateUrl: './emissions-reporting-dashboard.component.html',
    styleUrls: ['./emissions-reporting-dashboard.component.scss']
})
export class EmissionsReportingDashboardComponent implements OnInit {

    readonly ONRE_CODE = 'ONRE';
    readonly OP_CODE = 'OP';

    facility: MasterFacilityRecord;
    facilitySite: FacilitySite;
    reports: EmissionsReport[];
    emissionsReport: EmissionsReport;
    operatingFacilityStatusValues: BaseCodeLookup[];
    operatingStatusYearOptions: number[] = [];
    operatingStatusChangeOptions: BaseCodeLookup[] = [];
    excelExportEnabled = false;
    thresholdScreeningEnabled = false;
	monthlyReportingEnabled = false;
	newReportCreationEnabled = false;
	sltNewReportCreationEnabled = false;
    onreReportFound = false;
    onreReportYear: number;

    @ViewChild('FailedToCreateMessageBox', {static: true})
    _failedToCreateTemplate: TemplateRef<any>;

    _failedToCreateRef: NgbModalRef;

    constructor(
        private reportService: EmissionsReportingService,
        private facilityService: FacilitySiteService,
        private fileDownloadService: FileDownloadService,
        private route: ActivatedRoute,
        private sharedService: SharedService,
        private modalService: NgbModal,
        public router: Router,
        public userContext: UserContextService,
        private facilitySiteService: FacilitySiteService,
        private propertyService: ConfigPropertyService,
        private lookupService: LookupService) { }

    ngOnInit() {

        this.setupPage();
    }

    setupPage() {

        this.route.data
            .subscribe((data: { facility: MasterFacilityRecord }) => {
                this.facility = data.facility;

                if (this.facility) {
                    this.propertyService.retrieveSltMonthlyFuelReportingEnabled(this.facility.programSystemCode.code)
                        .subscribe(result => {
                            this.monthlyReportingEnabled = result;

                            this.reportService.getFacilityReports(this.facility.id, this.monthlyReportingEnabled)
                                .subscribe(reports => {
                                    for (const report of reports) {
                                        this.onreReportFound = false;
                                        this.onreReportYear = null;
                                        if (report.id != null) {
                                            this.facilitySiteService.retrieveForReport(report.id)
                                                .subscribe(fs => {
                                                    if (fs.operatingStatusCode?.code == this.ONRE_CODE) {
                                                        this.onreReportFound = true;
                                                        this.onreReportYear = report.year;
                                                    }
                                                });
                                        }
                                    }

                                    this.reports = [...reports.sort((a, b) => b.year - a.year)];
                                    this.addOperatingStatusYearOptions(this.reports);
                                });
                        });
                }

                this.propertyService.retrieveSLTThresholdScreeningGADNREnabled(this.facility.programSystemCode.code)
                    .subscribe(result => {
                        this.thresholdScreeningEnabled = result;
                    });

                this.propertyService.retrieveSLTNewReportCreationEnabled(this.facility.programSystemCode.code)
                    .subscribe(result => {
                        this.sltNewReportCreationEnabled = result;
                    });

                this.propertyService.retrieveNewReportCreationEnabled()
                    .subscribe(result => {
                        this.newReportCreationEnabled = result;
                    });
            });
        this.sharedService.emitChange(null);
        this.facilitySite = new FacilitySite();

        this.propertyService.retrieveExcelExportEnabled()
            .subscribe(result => {
                this.excelExportEnabled = result;
            });


        this.lookupService.retrieveFacilityOperatingStatus()
            .subscribe(result => {
                this.operatingFacilityStatusValues = result;
                this.addOperatingStatusChangeOptions(this.operatingFacilityStatusValues);
            });
    }

    isOneOf(baseValue: string, testValues: string[]): boolean {

        for (const value of testValues) {
            if (baseValue === value) {
                return true;
            }
        }
        return false;
    }

    allowUpload(report: EmissionsReport) {
        return !(this.thresholdScreeningEnabled && (report.status === 'NEW' || (report.thresholdStatus && report.thresholdStatus != ThresholdStatus.OPERATING_ABOVE_THRESHOLD)));
    }

    openThresholdScreeningModal(year: number) {

        if (this.thresholdScreeningEnabled) {
            const modalRef = this.modalService.open(ThresholdScreeningGadnrModalComponent, { size: 'xl' });
            modalRef.componentInstance.year = year;
            modalRef.result.then((result: string) => {
              this.createNewReport(year, result);
            }, () => {
              // needed for dismissing without errors
            });
        } else {
            this.createNewReport(year);
        }
    }

    openThresholdResetConfirmationModal(report: EmissionsReport) {
        if (this.thresholdScreeningEnabled) {
            const modalRef = this.modalService.open(ThresholdResetConfirmationModalComponent, {size: 'md'});
            modalRef.componentInstance.year = report.year;
            modalRef.result.then((result: boolean) => {
                if (result) {
                    this.openThresholdScreeningModal(report.year);
                } else {
                    this.createNewReport(report.year, report.thresholdStatus);
                }
            }, () => {
                // needed for dismissing without errors
            });
        }
        else {
            this.createNewReport(report.year);
        }
    }

    openChangeStatusModal() {
        if (!this.thresholdScreeningEnabled) {
            const modalRef =
                this.modalService.open(EditFacilityOperatingStatusModalComponent, {size: 'md'});
            modalRef.componentInstance.operatingStatusYearOptions = this.operatingStatusYearOptions;
            modalRef.componentInstance.operatingStatusChangeOptions = this.operatingStatusChangeOptions;
            modalRef.result.then(
                (result: {operatingStatus: BaseCodeLookup, statusYear: number}) => {

                    this.openConfirmationModal(result.operatingStatus, result.statusYear).subscribe(() => {
                    // User confirmed change

                        // Async, calls API
                        this.updateFacilitySitePerMatchingReportIY(result.operatingStatus, result.statusYear);
                    });
                },
                () => { /* Operating Status and Status Year unchanged */ }
            )
        }
    }

    openConfirmationModal(operatingStatus: BaseCodeLookup, statusYear: number): EventEmitter<any> {
        const modalRef = this.modalService.open(ConfirmationDialogComponent, {size: 'md'})
        const modalMessage = `Are you sure you want to change the operating status for this Facility
                                    (Agency Id ${this.facility.agencyFacilityIdentifier}) to
                                    ${operatingStatus.description} for Inventory Year ${statusYear}?`;
        modalRef.componentInstance.message = modalMessage;
        return modalRef.componentInstance.continue;
    }

    /**
     * Create a new report. Either a copy of the previous report (if one exists) or a new report
     */
    createNewReport(year: number, thresholdInfo?: string) {

        const modalWindow = this.modalService.open(BusyModalComponent, {
            backdrop: 'static',
            size: 'lg'
        });

        modalWindow.componentInstance.message = 'Please wait while we generate your new report.';

        this.reportService.createReportFromPreviousCopy(this.facility.id, year, thresholdInfo)
                .subscribe(reportResp => {
                    if (reportResp.status === 204) {
                        // 204 No Content
                        // no previous report

                        // this.copyFacilitySiteFromCdxModel();
                        this.lookupService.retrieveProgramSystemCodeByDescription(this.facility.programSystemCode?.description)
                        .subscribe(result => {
                            this.facilitySite.programSystemCode = result;
                            this.reportService.createReportFromScratch(this.facility.id, year, thresholdInfo)
                            .subscribe(reportResp => {
                                modalWindow.dismiss();
                                this.reportCompleted(reportResp.body);
                            });
                        })
                    } else if (reportResp.status === 200) {
                        // 200 OK
                        // previous report was copied

                        modalWindow.dismiss();

                        this.reportCompleted(reportResp.body);

                    }
                });
    }

    /**
     * Callback that is triggered after the new report is created
     */
    reportCompleted(newReport: EmissionsReport) {

		this.router.navigate([
                    `/facility/${newReport.masterFacilityRecordId}/report/${newReport.id}/summary/fromCreateNew`], { state: { prevPage: this.router.url } });

    }

    onFailedToCreateCloseClick() {

            this._failedToCreateRef.dismiss();
        }

    deleteReport(reportId: number) {

        this.reportService.delete(reportId).subscribe(() => {
            this.route.data
            .subscribe((data: { facility: MasterFacilityRecord }) => {
                this.facility = data.facility;
                if (this.facility) {
                    this.reportService.getFacilityReports(this.facility.id, this.monthlyReportingEnabled)
                    .subscribe(reports => {
                        this.reports = reports.sort((a, b) => b.year - a.year);
                    });
                }
            });
        });
    }

    openDeleteModal(report: EmissionsReport) {

        let reportFacility: FacilitySite;

        this.facilitySiteService.retrieveForReport(report.id)
            .subscribe(result => {
                reportFacility = result;

                const modalRef = this.modalService.open(ConfirmationDialogComponent, {size: 'sm'});
                const modalMessage = `Are you sure you want to reset the ${report.year} Emissions Report from this Facility
                (Agency Id ${reportFacility.agencyFacilityIdentifier})? This will also reset any Facility Information, Emission Units, Control Devices,
                and Release Points associated with this report.`;
                modalRef.componentInstance.message = modalMessage;
                modalRef.componentInstance.continue.subscribe(() => {
                    if (this.thresholdScreeningEnabled) {
                        this.openThresholdResetConfirmationModal(report);
                    } else {
                        this.deleteReport(report.id);
                    }
                });
            });

    }

    downloadExcelTemplate(report: EmissionsReport) {

        let reportFacility: FacilitySite;
        this.facilitySiteService.retrieveForReport(report.id)
        .subscribe(result => {
            reportFacility = result;
        });

        const modalWindow = this.modalService.open(BusyModalComponent, {
            backdrop: 'static',
            size: 'lg'
        });

        modalWindow.componentInstance.message = 'Please wait while we generate the Excel Template for this report. This may take a few minutes.';

        this.reportService.downloadExcelExport(report.id)
            .subscribe(file => {
                modalWindow.dismiss();

                // when being run on tomcat this call will return success with an empty body
                // instead of an error when the call times out
                if (file.size > 0) {
                    this.fileDownloadService.downloadFile(file, UtilityService.removeSpecialCharacters(`${reportFacility.agencyFacilityIdentifier}-${this.facility.name}-${report.year}.xlsx`));
                } else {
                    this.openDownloadFailure();
                }

            }, error => {
                modalWindow.dismiss();
                this.openDownloadFailure();
            });
    }

    downloadJsonTemplate(report: EmissionsReport) {

        let reportFacility: FacilitySite;
        this.facilitySiteService.retrieveForReport(report.id)
            .subscribe(result => {
                reportFacility = result;
            });

        this.reportService.downloadJsonExport(report.id)
            .subscribe(file => {

                // when being run on tomcat this call will return success with an empty body
                // instead of an error when the call times out
                this.fileDownloadService.downloadFile(file, UtilityService.removeSpecialCharacters(`${reportFacility.agencyFacilityIdentifier}-${this.facility.name}-${report.year}.json`));

            }, error => {
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

    reopenReport(report) {
        const modalMessage = `Do you wish to reopen the ${report.year} report for
        ${this.facility.name}? This will reset the status of the report to "In progress" and you
        will need to resubmit the report to the S/L/T authority for review.`;
        const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'sm' });
        modalRef.componentInstance.message = modalMessage;
        modalRef.componentInstance.continue.subscribe(() => {
            const ids = [report.id];
            this.resetReport(ids, report);
        });
    }

    resetReport(reportIds: number[], report) {
        this.reportService.resetReports(reportIds).subscribe(result => {
            this.router.navigate(['/facility/' + report.masterFacilityRecordId + '/report/' + report.id + '/summary']);
        });

    }

	allowReportCreation() {
    	return this.newReportCreationEnabled && this.sltNewReportCreationEnabled;
    }

    addOperatingStatusYearOptions(reports: EmissionsReport[]) {
        this.operatingStatusYearOptions = [];
        for (const report of reports) {
            if (
                !this.isOneOf(report.status, ['NEW', 'APPROVED', 'SUBMITTED', 'ADVANCED_QA'])
                && report.facilitySite?.operatingStatusCode?.code !== 'PS'
            ) {
                this.operatingStatusYearOptions.push(report.year);
            }
        }
    }
    addOperatingStatusChangeOptions(operatingFacilityStatusValues: BaseCodeLookup[]) {
        this.operatingStatusChangeOptions = [];
        for (const status of operatingFacilityStatusValues) {
            if (status?.code === this.OP_CODE || status?.code === this.ONRE_CODE) {
                this.operatingStatusChangeOptions.push(status);
            }
        }
    }

    updateFacilitySitePerMatchingReportIY(operatingStatus: BaseCodeLookup, statusYear: number) {
        let matchingReport: EmissionsReport;
        for (const report of this.reports) {
            if (report.year === statusYear) {
                matchingReport = report;
                break;
            }
        }
        if (matchingReport) {

            this.facilityService.retrieveForReport(matchingReport.id).pipe(
                filter(facilitySite => facilitySite != null),
                concatMap(facilitySite => {

                    facilitySite.operatingStatusCode = operatingStatus;
                    facilitySite.statusYear = statusYear;

                    return this.facilityService.update(facilitySite);
                })
            ).pipe(
                filter(facilitySite => facilitySite != null)
            ).subscribe((facilitySite) => {

                this.sharedService.updateReportStatusAndEmitFacilitySiteChange(facilitySite);
                this.setupPage();
            });
        }
    }
}
