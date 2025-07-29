/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {BaseSortableTable} from "src/app/shared/components/sortable-table/base-sortable-table";
import {FacilitySite} from "src/app/shared/models/facility-site";
import {OperatingStatus} from "src/app/shared/enums/operating-status";
import {EmissionService} from "src/app/core/services/emission.service";
import {ActivatedRoute} from "@angular/router";
import {FormBuilder, FormControl, Validators, FormGroup, ValidatorFn} from "@angular/forms";
import {ToastrService} from "ngx-toastr";
import {FormUtilsService} from "src/app/core/services/form-utils.service";
import {Emission} from "src/app/shared/models/emission";
import {MonthlyReportingEmissionHolder} from "src/app/shared/models/monthly-reporting-emission-holder";
import {MonthlyReportingPeriod} from "src/app/shared/enums/monthly-reporting-periods";
import {MonthlyReportingEmission} from "src/app/shared/models/monthly-reporting-emission";
import {MonthlyReportingSyncService} from "src/app/core/services/monthly-reporting-sync.service";
import {SharedService} from "src/app/core/services/shared.service";

@Component({
  selector: 'app-monthly-reporting-emission-table',
  templateUrl: './monthly-reporting-emission-table.component.html',
  styleUrls: ['./monthly-reporting-emission-table.component.scss']
})
export class MonthlyReportingEmissionTableComponent extends BaseSortableTable implements OnInit, OnChanges {
    @Input() facilitySite:FacilitySite
    @Input() period: MonthlyReportingPeriod;
    @Input() readOnlyMode: boolean;
    tableData: MonthlyReportingEmissionHolder[]
    baseUrl: string;
    savingMonthlyInfo = false;
    periodEnum = MonthlyReportingPeriod;
    readonly cemsCode = '1';

    emissionForm = this.fb.group({});

    operatingStatus = OperatingStatus;

    constructor(
        private emissionService: EmissionService,
        private route: ActivatedRoute,
        private fb: FormBuilder,
        private toastr: ToastrService,
        private sharedService: SharedService,
        private syncService: MonthlyReportingSyncService,
        public formUtils: FormUtilsService) {
        super();
    }

    ngOnInit() {
        this.syncService.getEmissionsChangeObservable(this.period)
            ?.subscribe(emissions => {
                this.tableData = emissions;
                this.initForm();
            });

        this.route.paramMap
            .subscribe(map => {
                this.baseUrl = `/facility/${map.get('facilityId')}/report/${map.get('reportId')}`;
            });

        this.controller.paginate = true;
    }

    ngOnChanges() {
        this.emissionService.retrieveForMonthlyReporting(this.facilitySite.id, this.period)
            .subscribe(result => {
                this.tableData = result;

                this.initForm();
            });
    }

    initForm() {
        this.emissionForm = this.fb.group({});
        this.tableData.forEach(rp => {
            rp.emissions.sort((a, b) => (a.pollutant.pollutantName > b.pollutant.pollutantName) ? 1 : -1);
            rp.emissions.forEach(e => {
                const fgEmission = new FormGroup({})

                const fcTotalEmissions = new FormControl({
                    value: e.totalEmissions,
                    disabled: false
                }, {
                    updateOn: 'change'
                });
                fcTotalEmissions.setValidators([
                    Validators.pattern('^[0-9]*([\\.][0-9]*)?([eE]{1}[-+]?[0-9]+)?$'),
                    this.checkSignificantFigures(fcTotalEmissions)
                ]);
                fgEmission.addControl('total', fcTotalEmissions);

                const fcMonthlyRate = new FormControl({
                    value: e.monthlyRate,
                    disabled: false
                }, {
                    updateOn: 'change'
                });
                fcMonthlyRate.setValidators([
                    Validators.pattern('^[0-9]*([\\.][0-9]*)?([eE]{1}[-+]?[0-9]+)?$')
                ]);
                fgEmission.addControl('monthly', fcMonthlyRate);

                const fcRpId = new FormControl({
                    value: rp.reportingPeriodId,
                    disabled: true
                });
                fgEmission.addControl('rpId', fcRpId);

                this.emissionForm.addControl(String(e.id), fgEmission);
            });
        });
    }

    getEmissionFormGroup(id: number) {
        return this.emissionForm.get(String(id)) as FormGroup;
    }

    readOnlyCheckTotalEmissions(e: MonthlyReportingEmission) {
        return !e.totalManualEntry && !(e.emissionsCalcMethodCode.totalDirectEntry && e.emissionsFactor == null && e.emissionsDenominatorUom == null && e.emissionsNumeratorUom == null);
    }

    readOnlyCheckMonthlyRate(e: MonthlyReportingEmission) {
        return e.totalManualEntry || !(e.emissionsCalcMethodCode.totalDirectEntry && e.emissionsFactor == null && e.emissionsDenominatorUom != null && e.emissionsNumeratorUom != null);
    }

    onSubmit() {
        if (!this.emissionForm.valid) {
            this.emissionForm.markAllAsTouched();
        } else {

            this.savingMonthlyInfo = true;
            const emissionDtos: Emission[] = [];

            for (const key of Object.keys(this.emissionForm.controls)) {
                let formGroup = this.emissionForm.get(key);

                if (formGroup.dirty) {
                    const e = new Emission();
                    e.id = Number(key);
                    e.monthlyRate = formGroup.get("monthly").value;
                    e.totalEmissions = formGroup.get("total").value;
                    e.reportingPeriodId = formGroup.get("rpId").value;
                    emissionDtos.push(e);
                }
            }

            this.emissionService.monthlyUpdate(this.facilitySite.id, this.period, emissionDtos)
                .subscribe(result => {

                    this.savingMonthlyInfo = false;
                    this.tableData = result;
                    this.initForm();
                    this.toastr.success('', 'Changes successfully saved and Total Emissions recalculated.');
                    this.sharedService.updateReportStatusAndEmit(this.route);
                    // reset dirty flags
                    this.emissionForm.markAsPristine();
                });

        }
    }

    checkSignificantFigures(totalEmissions: FormControl): ValidatorFn {
        return (control: FormGroup): {[key: string]: any} | null => {
            if (totalEmissions.value && this.formUtils.getSignificantFigures(totalEmissions.value) > this.formUtils.emissionsMaxSignificantFigures) {
                return {significantFiguresInvalid: true};
            }
            return null;
        };
    }

    determineRateLabel(): string {
      if (this.period === this.periodEnum.SEMIANNUAL) {
        return 'Semi-Annual';
      } else if (this.period === this.periodEnum.ANNUAL) {
        return 'Annual';
      } else {
        return 'Monthly';
      }
    }

}
