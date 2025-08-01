/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

import {AfterViewInit, Component, HostListener, OnInit, ViewChild, ViewChildren} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {MonthlyFuelReportingService} from 'src/app/core/services/monthly-fuel-reporting.services';
import {SharedService} from 'src/app/core/services/shared.service';
import {UserContextService} from 'src/app/core/services/user-context.service';
import {UtilityService} from 'src/app/core/services/utility.service';
import {ConfirmationDialogComponent} from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import {MonthlyReportingPeriod} from 'src/app/shared/enums/monthly-reporting-periods';
import {OperatingStatus} from 'src/app/shared/enums/operating-status';
import {FacilitySite} from 'src/app/shared/models/facility-site';
import {MonthlyFuelReporting} from 'src/app/shared/models/monthly-fuel-reporting';
import {User} from 'src/app/shared/models/user';
import {MonthlyFuelTableComponent} from '../../components/monthly-fuel-table/monthly-fuel-table.component';
import {MonthlyReportingEmissionHolder} from "src/app/shared/models/monthly-reporting-emission-holder";
import {ReportingPeriodService} from "src/app/core/services/reporting-period.service";
import {
    SemiannualReportValidationModalComponent
} from "src/app/modules/emissions-reporting/components/semiannual-report-validation-modal/semiannual-report-validation-modal.component";
import {
    SubmissionReviewModal
} from "src/app/modules/emissions-reporting/components/submission-review-modal/submission-review-modal.component";

@Component({
	selector: 'app-monthly-fuel-reporting',
	templateUrl: './monthly-reporting.component.html',
	styleUrls: ['./monthly-reporting.component.scss']
})

export class MonthlyReportingComponent implements OnInit, AfterViewInit {

	facilitySite: FacilitySite;
	readOnlyMode = true;
	savingMonthlyInfo = false;
	baseUrl: string;
	period = MonthlyReportingPeriod;
    selectedPeriod: MonthlyReportingPeriod;
    semiAnnualReportSubmittedOrApproved = false;
	semiannualTableData: MonthlyFuelReporting[];
	annualTableData: MonthlyFuelReporting[];
    emissions: MonthlyReportingEmissionHolder[];
	isDirty = false;
	user: User;
	operatingStatus = OperatingStatus;
    loadedPeriods: MonthlyReportingPeriod[] = [this.period.JANUARY];

    selectedMonthlyReportingPeriodKeyPrefix = 'SELECTED_MONTHLY_REPORTING_PERIOD_KEY_';
    selectedMonthlyReportingPeriodKey = this.selectedMonthlyReportingPeriodKeyPrefix;

	@ViewChildren(MonthlyFuelTableComponent)
	monthlyComponent: MonthlyFuelTableComponent;
    @ViewChild('nav') nav;

    periodKeysArray: string[] = Object.keys(this.period);

	constructor(
        public route: ActivatedRoute,
		private monthlyFuelReportingService: MonthlyFuelReportingService,
		private userContextService: UserContextService,
        private reportingPeriodService: ReportingPeriodService,
		private modalService: NgbModal,
		private sharedService: SharedService) { }

	ngOnInit() {

		// emits the report info to the sidebar
		this.route.data
			.subscribe((data: { facilitySite: FacilitySite }) => {
				this.facilitySite = data.facilitySite;
                this.setSelectedMonthlyReportingPeriodKey(this.facilitySite.id);

				if (this.facilitySite.emissionsReport.midYearSubmissionStatus === 'APPROVED'
				|| this.facilitySite.emissionsReport.midYearSubmissionStatus === 'SUBMITTED') {
					this.semiAnnualReportSubmittedOrApproved = true;
				}

				this.userContextService.getUser().subscribe(user => {
					this.user = user
					if (UtilityService.isNotReadOnlyMode(user, data.facilitySite.emissionsReport.status)) {
						this.readOnlyMode = false;
					}
				});

				this.sharedService.emitChange(data.facilitySite);
			});
	}

    ngAfterViewInit() {
        if (localStorage.getItem(this.selectedMonthlyReportingPeriodKey)) {
            this.nav.select(Number(localStorage.getItem(this.selectedMonthlyReportingPeriodKey)));
            this.setSelectedPeriod(
                this.period[
                    this.periodKeysArray[localStorage.getItem(this.selectedMonthlyReportingPeriodKey)]
                ]
            );
        } else {
            this.nav.select(0);
            this.setSelectedPeriod(MonthlyReportingPeriod.JANUARY);
        }

    }

    setSelectedMonthlyReportingPeriodKey(facilityId: number) {
        this.selectedMonthlyReportingPeriodKey = this.selectedMonthlyReportingPeriodKeyPrefix.concat(facilityId.toString());
    }

    setSelectedPeriod(period: MonthlyReportingPeriod) {
        this.selectedPeriod = period;
        localStorage.setItem(this.selectedMonthlyReportingPeriodKey, this.nav.activeId);
        if (!this.loadedPeriods.includes(period)) {
            this.loadedPeriods.push(period);
        }
    }

    openSemiAnnualValidationModal() {
        const modalRef = this.modalService.open(SemiannualReportValidationModalComponent, {size: 'lg'});
        modalRef.componentInstance.facilitySite = this.facilitySite;
        modalRef.componentInstance.route = this.route;
        modalRef.componentInstance.user = this.user;
    }

    initSubmissionReviewModal() {
        const modalRef = this.modalService.open(SubmissionReviewModal, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.report = this.facilitySite.emissionsReport;
        modalRef.componentInstance.facility = this.facilitySite;
        modalRef.componentInstance.url = this.baseUrl;
        modalRef.componentInstance.semiannualReport = true;
    }

	canDeactivate(): Promise<boolean> | boolean {
		let message;
		this.isDirty = false;

		this.monthlyComponent['_results'].forEach(month => {
			month.tableData.forEach(rp => {
				let fuelVal = month.monthlyReportingForm.get(month.period + rp.reportingPeriodId).value;
				let optDays = month.monthlyReportingForm.get(month.period + 'totalOpDays' + rp.reportingPeriodId).value

				if (month.monthlyReportingForm.dirty) {
					if ((rp.fuelUseValue === null && fuelVal == null) || (rp.fuelUseValue === +fuelVal) || (rp.fuelUseValue === null && fuelVal?.length === 0)) {
					} else {
						this.isDirty = true;
					}
					if ((rp.totalOperatingDays === null && optDays == null) || (rp.totalOperatingDays === +optDays) || (rp.totalOperatingDays === null && optDays?.length === 0)) {
					} else {
						this.isDirty = true;
					}
				}
			});
		});

		// Allow synchronous navigation (`true`) if both forms are clean
		if (this.readOnlyMode || !this.isDirty) {
			return true;
		} else {
			message = 'You have unsaved changes which will be lost if you navigate away. Are you sure you wish to discard these changes?';
		}

		// Otherwise ask the user with the dialog service and return its
		// promise which resolves to true or false when the user decides
		const modalMessage = message;
		const modalRef = this.modalService.open(ConfirmationDialogComponent);
		modalRef.componentInstance.message = modalMessage;
		modalRef.componentInstance.title = 'Unsaved Changes';
		modalRef.componentInstance.confirmButtonText = 'Discard';
		return modalRef.result;
	}

	@HostListener('window:beforeunload', ['$event'])
	beforeunloadHandler(event) {
		if (!this.readOnlyMode && this.isDirty) {
			event.preventDefault();
			event.returnValue = '';
		}
		return true;
	}
}
