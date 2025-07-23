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
import {Component, Input, OnChanges, OnInit} from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { MonthlyReportingPeriodHolder } from 'src/app/shared/models/monthly-reporting-period-holder';
import { MonthlyReportingPeriod, MonthsWith30Days, MonthsWith31Days } from 'src/app/shared/enums/monthly-reporting-periods';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { EmissionsProcessService } from 'src/app/core/services/emissions-process.service';
import { FormUtilsService } from 'src/app/core/services/form-utils.service';
import { ReportingPeriodService } from 'src/app/core/services/reporting-period.service';
import { ToastrService } from "ngx-toastr";
import { MonthlyReportingSyncService } from "src/app/core/services/monthly-reporting-sync.service";
import {EmissionService} from "src/app/core/services/emission.service";
import {SharedService} from "src/app/core/services/shared.service";

@Component({
    selector: 'app-monthly-reporting-period-table',
    templateUrl: './monthly-reporting-period-table.component.html'
})
export class MonthlyReportingPeriodTableComponent extends BaseSortableTable implements OnInit, OnChanges {

    throughputValueFC = 'throughputValue';
    fuelUseValueFC = 'fuelUseValue';
    avgDaysPerWeekFC = 'avgDaysPerWeek';
    avgHoursPerDayFC = 'avgHoursPerDay';
    totalOperatingDaysFC = 'totalOperatingDays';
    hoursPerPeriodFC = 'hoursPerPeriod';
    avgWeeksPerPeriodFC = 'avgWeeksPerPeriod';
    allMonths = MonthlyReportingPeriod;
    readonly monthsWith30Days = MonthsWith30Days;
    readonly monthsWith31Days = MonthsWith31Days;

    @Input() period: string;
    @Input() facilitySite: FacilitySite
    @Input() readOnlyMode: boolean;
    savingMonthlyInfo = false;
    baseUrl: string;
    monthlyPeriodForm = this.fb.group({ });
    tableData: MonthlyReportingPeriodHolder[] = [];
    validMessage: string;

    constructor(
        private fb: FormBuilder,
        private route: ActivatedRoute,
        private emissionsProcessService: EmissionsProcessService,
        private formUtils: FormUtilsService,
        private toastr: ToastrService,
        private sharedService: SharedService,
        private reportingPeriodService: ReportingPeriodService,
        private syncService: MonthlyReportingSyncService,
        private emissionService: EmissionService
    ) {
        super();
    }

    ngOnChanges() {
        this.retrieveMonthlyProcessData(); // calls initForm()
    }

    ngOnInit() {
        this.syncService.getPeriodChangeObservable(this.period)
            ?.subscribe(periods => {
                this.tableData = periods;
                this.initForm();
            });

        this.createBaseUrl();

        this.controller.paginate = true;
    }

    createBaseUrl() {
        this.route.paramMap
            .subscribe(map => {
                this.baseUrl = `/facility/${ map.get('facilityId') }/report/${ map.get('reportId') }`;
            });
    }

    initForm() {
        this.monthlyPeriodForm = this.fb.group({ });
        for (const item of this.tableData) {

            /**
             * create a formGroup per row
             */
            const fgPeriod = new FormGroup({})

            fgPeriod.addControl(
                this.throughputValueFC,
                new FormControl(
                    { value: item.calculationParameterValue, disabled: false },
                    { validators: [
                        Validators.min(0),
                        Validators.pattern('^[0-9]*\\.?[0-9]+$'),
                    ], updateOn: 'change' }
                )
            );
            fgPeriod.get(this.throughputValueFC)
                .addValidators(
                    this.checkSignificantFigures(
                        fgPeriod.get(this.throughputValueFC) as FormControl)
                );

            fgPeriod.addControl(
                this.fuelUseValueFC,
                new FormControl(
                    { value: item.fuelUseValue, disabled: false },
                    { validators: [
                        Validators.min(0),
                        Validators.pattern('^[0-9]*\\.?[0-9]+$'),
                    ], updateOn: 'change' }
                )
            );
            fgPeriod.get(this.fuelUseValueFC)
                .addValidators(
                    this.checkSignificantFigures(
                        fgPeriod.get(this.fuelUseValueFC) as FormControl)
                );

            fgPeriod.addControl(
                this.hoursPerPeriodFC,
                new FormControl(
                    { value: item.actualHoursPerPeriod, disabled: false },
                    { validators: [
                        Validators.pattern('^[0-9]*\\.?[0-9]+$'),
                    ], updateOn: 'change' }
                )
            );
            fgPeriod.get(this.hoursPerPeriodFC)
                .addValidators([
                    this.checkHoursPerReportingPeriod(
                        fgPeriod.get(this.hoursPerPeriodFC) as FormControl),
                    Validators.pattern('^[0-9]*\\.?[0-9]{1,5}$')
                    ]
                );

            fgPeriod.addControl(
                this.avgHoursPerDayFC,
                new FormControl(
                    { value: item.avgHoursPerDay, disabled: false },
                    { validators: [
                        Validators.min(0),
                        Validators.max(24),
                        Validators.pattern('^[0-9]*\\.?[0-9]{1,5}$'),
                    ], updateOn: 'change' }
                )
            );

            fgPeriod.addControl(
                this.avgDaysPerWeekFC,
                new FormControl(
                    { value: item.avgDaysPerWeek, disabled: false },
                    { validators: [
                        Validators.min(0),
                        Validators.max(7),
                        Validators.pattern('^[0-9]*\\.?[0-9]{1,5}$'),
                    ], updateOn: 'change' }
                )
            );

            if (this.period === this.allMonths.SEMIANNUAL) {
                fgPeriod.addControl(
                    this.avgWeeksPerPeriodFC,
                    new FormControl(
                        {value: item.avgWeeksPerPeriod, disabled: false},
                        {
                            validators: [
                                Validators.max(24),
                                Validators.min(0),
                                Validators.pattern('^[0-9]*\\.?[0-9]{1,5}$')
                            ], updateOn: 'change'
                        }
                    )
                );
            }
            else if (this.period === this.allMonths.ANNUAL) {
                fgPeriod.addControl(
                    this.avgWeeksPerPeriodFC,
                    new FormControl(
                        {value: item.avgWeeksPerPeriod, disabled: false},
                        {
                            validators: [
                                Validators.required,
                                Validators.max(48),
                                Validators.min(0),
                                Validators.pattern('^[0-9]*$\\.?[0-9]{1,5}$')
                            ], updateOn: 'change'
                        }
                    )
                );
            }
            else {
                fgPeriod.addControl(
                    this.avgWeeksPerPeriodFC,
                    new FormControl(
                        {value: item.avgWeeksPerPeriod, disabled: false},
                        {
                            validators: [
                                Validators.max(4),
                                Validators.min(0),
                                Validators.pattern('^[0-9]*\\.?[0-9]{1,5}$')
                            ], updateOn: 'change'
                        }
                    )
                );
            }

            if (!this.monthlyPeriodForm.contains(String(item.reportingPeriodId))) {
                this.monthlyPeriodForm.addControl(String(item.reportingPeriodId), fgPeriod);
            }
            else {
                this.monthlyPeriodForm.setControl(String(item.reportingPeriodId), fgPeriod);
            }
        }
    }

    retrieveMonthlyProcessData() {
        this.reportingPeriodService.retrieveMonthlyReportingPeriodData(this.facilitySite.id, this.period)
            .subscribe(
                (result) => {
                    this.tableData = result;
                    this.initForm();
                }
            )
    }

    getPeriodFormGroup(id: number) {
        return this.monthlyPeriodForm.get(String(id)) as FormGroup;
    }

    checkHoursPerReportingPeriod(fc: FormControl): ValidatorFn {
        const leapYear = this.facilitySite.emissionsReport.year % 4 === 0;
        return (control: FormGroup): { [key: string]: any } | null => {
            if (fc.value) {
                if ((this.period === this.allMonths.FEBRUARY) &&
                    leapYear &&
                    (fc.value < 0 || 696 < fc.value)
                ) {
                    return { februaryLeapYearHoursInvalid: true };
                } else if ((this.period === this.allMonths.FEBRUARY) &&
                    !leapYear &&
                    (fc.value < 0 || 672 < fc.value)
                ) {
                    return { februaryNonLeapYearHoursInvalid: true };
                } else if ((this.monthsWith30Days.indexOf(this.period) !== -1) &&
                    (fc.value < 0 || 720 < fc.value)
                ) {
                    return { monthsWith30DaysRangeInvalid: true };
                } else if ((this.monthsWith31Days.indexOf(this.period) !== -1) &&
                    (fc.value < 0 || 744 < fc.value)
                ) {
                    return { monthsWith31DaysRangeInvalid: true };
                }
            }
            return null;
        };
    }

    checkSignificantFigures(fc: FormControl): ValidatorFn {
        return (): { [key: string]: any } | null => {
            if (fc.value && this.formUtils.getSignificantFigures(fc.value)
                > this.formUtils.emissionsMaxSignificantFigures) {
                return { significantFiguresInvalid: true };
            }
            return null;
        };
    }

    onSubmit() {
        this.validMessage = null;
        if (!this.monthlyPeriodForm.valid) {
            this.validMessage = 'The table contains invalid data. Please check all pages and correct all invalid data before attempting to save.';
            this.monthlyPeriodForm.markAllAsTouched();
        } else {

            this.savingMonthlyInfo = true;
            let rpsToSave: MonthlyReportingPeriodHolder[] = [];
            let nullValueFound = false;

            this.tableData.forEach(rp => {

                let formGroup = this.monthlyPeriodForm.get(String(rp.reportingPeriodId));

                if (!nullValueFound &&
                    (formGroup.get(this.throughputValueFC).value == null || formGroup.get(this.fuelUseValueFC).value == null
                     || formGroup.get(this.hoursPerPeriodFC).value == null || formGroup.get(this.avgHoursPerDayFC).value == null
                     || formGroup.get(this.avgDaysPerWeekFC).value == null || formGroup.get(this.avgWeeksPerPeriodFC).value == null)) {

                    nullValueFound = true;
                }

                if (formGroup.dirty) {
                    rp.calculationParameterValue = formGroup.get(this.throughputValueFC).value;
                    rp.fuelUseValue = formGroup.get(this.fuelUseValueFC).value;
                    rp.actualHoursPerPeriod = formGroup.get(this.hoursPerPeriodFC).value;
                    rp.avgHoursPerDay = formGroup.get(this.avgHoursPerDayFC).value;
                    rp.avgDaysPerWeek = formGroup.get(this.avgDaysPerWeekFC).value;
                    rp.avgWeeksPerPeriod = formGroup.get(this.avgWeeksPerPeriodFC).value;

                    rpsToSave.push(rp);
                }
            });

            this.reportingPeriodService.monthlyUpdate(this.facilitySite.id, rpsToSave, this.period)
                .subscribe(result => {
                    this.savingMonthlyInfo = false;
                    this.syncService.emitEmissionsChange(result, this.period);
                    if (nullValueFound) {
                        this.toastr.warning('', 'Some required fields may be empty. You will need to enter all required information in order to complete your report.');
                    }
                    else {
                        this.toastr.success('', 'Total Emissions have been recalculated.');
                    }
                    this.sharedService.updateReportStatusAndEmit(this.route);
                    // reset dirty flags
                    this.monthlyPeriodForm.markAsPristine();
                }, error => {
                  if( error?.error) {
                      this.savingMonthlyInfo = false;
                      this.toastr.error("The data you entered has not been saved. Please review the operating details you have entered.")
                    }
                    });
        }
    }

}
