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
import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { ValidationResult } from 'src/app/shared/models/validation-result';
import { EmissionsReportingService } from 'src/app/core/services/emissions-reporting.service';
import { ActivatedRoute, Router, UrlTree } from '@angular/router';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { SharedService } from 'src/app/core/services/shared.service';
import { ValidationStatus } from 'src/app/shared/enums/validation-status.enum';
import { ValidationDetail } from 'src/app/shared/models/validation-detail';
import { EntityType } from 'src/app/shared/enums/entity-type';
import { BaseReportUrl } from 'src/app/shared/enums/base-report-url';
import { ReviewerCommentService } from 'src/app/core/services/reviewer-comment.service';
import { ReviewerComment } from 'src/app/shared/models/reviewer-comment';
import { MonthlyReportingPeriod } from 'src/app/shared/enums/monthly-reporting-periods';
import { HttpErrorResponse } from "@angular/common/http";
import { QAResultsStatus } from 'src/app/shared/enums/qa-results-status';
import { QualityCheckResults } from 'src/app/shared/models/quality-check-results';

@Component({
  selector: 'app-emissions-report-validation-info',
  templateUrl: './emissions-report-validation-info.component.html',
  styleUrls: ['./emissions-report-validation-info.component.scss']
})
export class EmissionsReportValidationInfoComponent implements OnInit {

    private readonly defaultValidationButtonTimeLimit = 300000; // 5 mins timeout
    private validationButtonTimer: ReturnType<typeof setTimeout>;

    @Input() validationType: string;
    @Input() facilitySite: FacilitySite = null;
    @Input() route: ActivatedRoute;

    validationResult: ValidationResult;
    qaResults: QualityCheckResults = null;
    validationComplete: boolean;
    baseUrl: string;
    periods = MonthlyReportingPeriod;
    isValidationButtonEnabled = true;
    isSemiannual = false;

    reviewerComments: ReviewerComment[];

    statusMessage = '';
    readonly incompleteQAMessage = `CAERS previously encountered an unexpected error so
     quality checks could not be completed. Reach out to the help desk if a second quality check run attempt is
     unsuccessful.`
    readonly nonexistentQAMessage = `Quality checks have not been run for this report yet.`
    readonly loadingQAMessage = `CAERS is finding the most-recent quality check
        results . . .`;
    readonly runningQAMessage = `CAERS is currently conducting quality checks and the
        results will be displayed here shortly. You may navigate through your report while waiting, but please do not
        make any changes before the results are displayed.`
    readonly slowQAMessage = `The quality checks are taking more time than anticipated. Feel
        free to revisit this page at a later time, but refrain from editing your report until CAERS completes its
        validations.`

    @ViewChild('myModalClose') closeModal;

    constructor(
        private router: Router,
        private sharedService: SharedService,
        private emissionsReportingService: EmissionsReportingService,
        private rcService: ReviewerCommentService) {
    }

    ngOnInit() {

        this.statusMessage = this.loadingQAMessage;
        this.validationComplete = false;

        this.isSemiannual = this.validationType === this.periods.SEMIANNUAL;

        this.route.paramMap
            .subscribe(map => {
                this.baseUrl = `/facility/${map.get('facilityId')}/report/${map.get('reportId')}`;
            });

        this.route.data.subscribe((data: { facilitySite: FacilitySite }) => { // TODO: Use better rxJS operator(s) than nested subscriptions

            if (data.facilitySite) {

                this.facilitySite = data.facilitySite;

                this.getQAStatusAndResults(); // Async

                this.rcService.retrieveActiveForReport(data.facilitySite.emissionsReport.id)
                    .subscribe(result => {
                        this.reviewerComments = result;

                        this.reviewerComments.forEach(item => {
                            item.url = this.generateUrl(item.details);
                        });
                    });
            }
        });
    }

    updateStatusAndAssembleErrors(facilitySite: FacilitySite, validationResult: ValidationResult, isSemiannual:boolean) {

        this.validationResult = validationResult;

        if (validationResult.valid) {
            if (this.hasWarnings()) {
                if (isSemiannual) {
                    facilitySite.emissionsReport.validationStatus = ValidationStatus.SEMIANNUAL_PASSED_WARNINGS;
                }
                else {
                    facilitySite.emissionsReport.validationStatus = ValidationStatus.PASSED_WARNINGS;
                }
            } else {
                if (isSemiannual) {
                    facilitySite.emissionsReport.validationStatus = ValidationStatus.SEMIANNUAL_PASSED;
                }
                else {
                    facilitySite.emissionsReport.validationStatus = ValidationStatus.PASSED;
                }
            }
        } else {
            facilitySite.emissionsReport.validationStatus = ValidationStatus.UNVALIDATED;
        }
        this.sharedService.emitChange(facilitySite);

        this.validationResult.federalErrors.forEach(item => {
            item.url = this.generateUrl(item.invalidValue);
        });
        this.validationResult.stateErrors.forEach(item => {
            item.url = this.generateUrl(item.invalidValue);
        });
        this.validationResult.federalWarnings.forEach(item => {
            item.url = this.generateUrl(item.invalidValue);
        });
        this.validationResult.stateWarnings.forEach(item => {
            item.url = this.generateUrl(item.invalidValue);
        });
        this.validationResult.facilityInventoryChanges.forEach(item => {
            item.url = this.generateUrl(item.invalidValue);
        })

        this.sharedService.emitValidationResultChange(this.validationResult);

        this.validationComplete = true;
        this.statusMessage = this.loadingQAMessage; // Reset message
    }

    hasErrors() {
        if (this.validationComplete === true && this.validationResult) {
            return this.validationResult.federalErrors.length || this.validationResult.stateErrors.length;
        }
        return false;
    }

    hasWarnings() {
        if (this.validationComplete === true && this.validationResult) {
            return this.validationResult.federalWarnings.length || this.validationResult.stateWarnings.length || this.validationResult.facilityInventoryChanges.length;
        }
        return false;
    }

    generateUrl(detail: ValidationDetail): string {

        let tree: UrlTree;
        if (detail) {
            if (EntityType.EMISSION === detail.type) {

                const period = detail.parents.find(p => p.type === EntityType.REPORTING_PERIOD);
                if (period) {
                    tree = this.router.createUrlTree([
                        BaseReportUrl.REPORTING_PERIOD,
                        period.id,
                        BaseReportUrl.EMISSION,
                        detail.id
                    ], {relativeTo: this.route.parent});
                }
            } else if (EntityType.EMISSIONS_PROCESS === detail.type) {

                tree = this.router.createUrlTree(
                    [BaseReportUrl.EMISSIONS_PROCESS, detail.id],
                    {relativeTo: this.route.parent});

            } else if (EntityType.EMISSIONS_REPORT === detail.type) {

                tree = this.router.createUrlTree(
                    [BaseReportUrl.REPORT_SUMMARY],
                    {relativeTo: this.route.parent});

            } else if (EntityType.EMISSIONS_UNIT === detail.type) {

                tree = this.router.createUrlTree(
                    [BaseReportUrl.EMISSIONS_UNIT, detail.id],
                    {relativeTo: this.route.parent});

            } else if (EntityType.FACILITY_SITE === detail.type) {

                tree = this.router.createUrlTree(
                    [BaseReportUrl.FACILITY_INFO],
                    {relativeTo: this.route.parent});

            } else if (EntityType.CONTROL_PATH === detail.type) {

                tree = this.router.createUrlTree(
                    [BaseReportUrl.CONTROL_PATH, detail.id],
                    {relativeTo: this.route.parent});

            } else if (EntityType.CONTROL === detail.type) {

                tree = this.router.createUrlTree(
                    [BaseReportUrl.CONTROL_DEVICE, detail.id],
                    {relativeTo: this.route.parent});

            } else if (EntityType.RELEASE_POINT === detail.type) {

                tree = this.router.createUrlTree(
                    [BaseReportUrl.RELEASE_POINT, detail.id],
                    {relativeTo: this.route.parent});

            } else if (EntityType.REPORT_ATTACHMENT === detail.type) {

                tree = this.router.createUrlTree(
                    [BaseReportUrl.REPORT_SUMMARY],
                    {relativeTo: this.route.parent});

            } else if (EntityType.OPERATING_DETAIL === detail.type
                || EntityType.REPORTING_PERIOD === detail.type) {

                const process = detail.parents.find(p => p.type === EntityType.EMISSIONS_PROCESS);
                if (process) {
                    tree = this.router.createUrlTree(
                        [BaseReportUrl.EMISSIONS_PROCESS, process.id],
                        {relativeTo: this.route.parent});
                }
            }
        }

        return tree ? this.router.serializeUrl(tree) : '.';
    }

    calculateElapsedTime(){
        if (this.qaResults?.lastModifiedDate != null) {
            const lastModifiedDate = new Date(this.qaResults.lastModifiedDate);
            const currentDate = new Date();
            return currentDate.getTime() - lastModifiedDate.getTime();
        } else { // 1st time running QA Checks
            return 0;
        }
    }

    getQAStatusAndResults() {

        this.emissionsReportingService.getQAResults(this.facilitySite?.emissionsReport.id, this.isSemiannual)
            .subscribe(results => {
                this.qaResults = results;
                if (this.qaResults != null && this.qaResults.qaResultsStatus === QAResultsStatus.IN_PROGRESS) {
                    const elapsedTime = this.calculateElapsedTime();
                    this.statusMessage = this.runningQAMessage;
                    if (elapsedTime >= this.defaultValidationButtonTimeLimit) {
                        this.enableValidationButtonAndResetTimer(); // if time is longer than server timeout, the enable button
                    }
                    else {
                        this.disableValidationButtonAndContinueTimer(this.defaultValidationButtonTimeLimit - elapsedTime); // if elapsed time is shorter than timeout, disable button
                    }
                } else if (
                    this.qaResults != null
                    && this.qaResults.qaResultsStatus === QAResultsStatus.COMPLETE
                    && this.qaResults.currentResults != null
                ) {
                    this.updateStatusAndAssembleErrors(
                        this.facilitySite, this.qaResults.currentResults, this.isSemiannual);
                    this.enableValidationButtonAndResetTimer();
                } else if (this.qaResults != null && this.qaResults.qaResultsStatus === QAResultsStatus.INCOMPLETE) {
                    this.statusMessage = this.incompleteQAMessage;
                    this.enableValidationButtonAndResetTimer();
                } else {
                    this.statusMessage = this.nonexistentQAMessage;
                    this.enableValidationButtonAndResetTimer();
                }
            });
    }

    updateQAResultsStatusIncomplete() {
        this.emissionsReportingService
            .updateQAResultsStatus(this.facilitySite.emissionsReport.id, this.isSemiannual, QAResultsStatus.INCOMPLETE)
            .subscribe((result) => {
                this.qaResults = result;
            });
    }

    runValidation(){
        this.emissionsReportingService.validateReport(this.facilitySite.emissionsReport.id, this.isSemiannual)
            .subscribe(
                validationResult => {
                    if (validationResult != null && this.isSemiannual) {
                        this.getQAStatusAndResults();
                    } else if (validationResult != null) {
                        this.forceReload(); // Calls ngOnInit() > getQAStatusAndResults() again
                    } else {
                        this.enableValidationButtonAndResetTimer();
                        throw new HttpErrorResponse({status: 500})
                    }
                },
                error => {
                    if (
                        ((error instanceof HttpErrorResponse) && (error.status === 502))
                        || (typeof(error) === 'string' && error.includes('502') && error.includes('/validation'))
                    ) {
                        this.statusMessage = this.slowQAMessage;
                    } else {
                        this.enableValidationButtonAndResetTimer();
                        this.updateQAResultsStatusIncomplete(); // Async
                    }
                });
    }

    canRunValidation(){
        const elapsedTime = this.calculateElapsedTime();
        return this.qaResults?.qaResultsStatus !== QAResultsStatus.IN_PROGRESS
            || (this.qaResults?.qaResultsStatus === QAResultsStatus.IN_PROGRESS && elapsedTime >= this.defaultValidationButtonTimeLimit);
    }

    validate() {
        this.statusMessage = this.runningQAMessage;
        this.validationComplete = false;
        this.disableValidationButtonAndStartTimer();
        if (this.canRunValidation()){
            this.runValidation();
        }
    }

    disableValidationButtonAndStartTimer() {
        this.isValidationButtonEnabled = false;
        this.validationButtonTimer = setTimeout(() => {
            this.enableValidationButtonAndResetTimer();
        }, this.defaultValidationButtonTimeLimit)
    }

    enableValidationButtonAndResetTimer() {
        clearTimeout(this.validationButtonTimer);
        this.isValidationButtonEnabled = true;
    }

    disableValidationButtonAndContinueTimer(timeRemaining: number) {
        this.isValidationButtonEnabled = false;
        clearTimeout(this.validationButtonTimer);
        this.validationButtonTimer = setTimeout(() => {
            this.enableValidationButtonAndResetTimer();
        }, timeRemaining)
    }

    forceReload() { // Prevents infinite loading message despite complete QAs
        if (this.router.url.includes('/validation')) window.location.reload();
    }
}

