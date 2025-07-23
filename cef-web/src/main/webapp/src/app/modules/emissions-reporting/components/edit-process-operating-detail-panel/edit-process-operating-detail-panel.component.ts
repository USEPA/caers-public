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
import {Component, OnInit, Input, OnChanges, SimpleChanges} from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { OperatingDetail } from 'src/app/shared/models/operating-detail';
import { wholeNumberValidator } from 'src/app/modules/shared/directives/whole-number-validator.directive';
import { SharedService } from "src/app/core/services/shared.service";
import { LookupService } from "src/app/core/services/lookup.service";
import { ConfigPropertyService } from "src/app/core/services/config-property.service";
import { FacilitySite } from "src/app/shared/models/facility-site";
import {forkJoin} from "rxjs";

@Component({
  selector: 'app-edit-process-operating-detail-panel',
  templateUrl: './edit-process-operating-detail-panel.component.html',
  styleUrls: ['./edit-process-operating-detail-panel.component.scss']
})
export class EditProcessOperatingDetailPanelComponent implements OnInit, OnChanges {

  readonly numberPattern = '^[0-9]*\\.?[0-9]+$';
  readonly numberPattern3Decimals = '^[0-9]*\\.?[0-9]{1,3}$';
  readonly numberPattern5Decimals = '^[0-9]*\\.?[0-9]{1,5}$';

  @Input() operatingDetails: OperatingDetail;
  @Input() isMonthlyReportingProcess: boolean;
  @Input() facilitySite: FacilitySite;

  monthlyReportingEnabled: boolean;

  operatingDetailsForm = this.fb.group({
    actualHoursPerPeriod: ['', [
      Validators.required,
      Validators.min(1),
      Validators.max(8784),
      wholeNumberValidator(),
    ]],
    avgHoursPerDay: ['', [
      Validators.required,
      Validators.min(0.0110),
      Validators.max(24),
      Validators.pattern(this.numberPattern5Decimals)
    ]],
    avgDaysPerWeek: ['', [
      Validators.required,
      Validators.min(0.0110),
      Validators.max(7),
      Validators.pattern(this.numberPattern5Decimals)
    ]],
    avgWeeksPerPeriod: ['', [
      Validators.required,
      Validators.min(1),
      Validators.max(52),
      wholeNumberValidator()
    ]],
    percentWinter: ['', [
      Validators.required,
      Validators.min(0),
      Validators.max(100),
      Validators.pattern(this.numberPattern)
    ]],
    percentSpring: ['', [
      Validators.required,
      Validators.min(0),
      Validators.max(100),
      Validators.pattern(this.numberPattern)
    ]],
    percentSummer: ['', [
      Validators.required,
      Validators.min(0),
      Validators.max(100),
      Validators.pattern(this.numberPattern)
    ]],
    percentFall: ['', [
      Validators.required,
      Validators.min(0),
      Validators.max(100),
      Validators.pattern(this.numberPattern)
    ]]
  });

  constructor(private fb: FormBuilder,
              private propertyService: ConfigPropertyService,
              private lookupService: LookupService,
              private sharedService: SharedService) { }

  ngOnInit() {

      this.getHours().valueChanges
          .subscribe(value => {
              this.calculateTotalHours();
          });

      this.getDays().valueChanges
          .subscribe(value => {
              this.calculateTotalHours();
          });

      this.getWeeks().valueChanges
          .subscribe(value => {
              this.calculateTotalHours();
          });

      this.sharedService.processSccChangeEmitted$.subscribe(scc => {
          if (scc) {
              this.getPointSourceScc(scc);
          }
      });

      forkJoin({
          monthlyReportingEnabled: this.propertyService.retrieveSltMonthlyFuelReportingEnabled(this.facilitySite?.programSystemCode?.code),
          monthlyReportingInitialYear: this.propertyService.retrieveSltMonthlyFuelReportingInitialYear(this.facilitySite?.programSystemCode?.code)
      })
      .subscribe(({monthlyReportingEnabled, monthlyReportingInitialYear}) => {
          this.monthlyReportingEnabled = monthlyReportingEnabled && (monthlyReportingInitialYear == null || this.facilitySite?.emissionsReport?.year >= monthlyReportingInitialYear);
      });
  }

    validateOperatingPercent() {
    if ((parseFloat(this.operatingDetailsForm.controls.percentFall.value) +
        parseFloat(this.operatingDetailsForm.controls.percentSpring.value) +
        parseFloat(this.operatingDetailsForm.controls.percentWinter.value) +
        parseFloat(this.operatingDetailsForm.controls.percentSummer.value) > 100.5) || (
        parseFloat(this.operatingDetailsForm.controls.percentFall.value) +
        parseFloat(this.operatingDetailsForm.controls.percentSpring.value) +
        parseFloat(this.operatingDetailsForm.controls.percentWinter.value) +
        parseFloat(this.operatingDetailsForm.controls.percentSummer.value) < 99.5)) {
          return false;
        } else {
          return true;
        }
  }

  ngOnChanges(changes: SimpleChanges) {
      if (changes.isMonthlyReportingProcess) {
          if (changes.isMonthlyReportingProcess.currentValue) {
              this.operatingDetailsForm.disable();
          } else {
              this.operatingDetailsForm.enable();
          }
      }

      if (changes.operatingDetails) {
          this.operatingDetailsForm.reset(this.operatingDetails);
      }
  }

  getHours() {
      return this.operatingDetailsForm.get('avgHoursPerDay');
  }

  getDays() {
      return this.operatingDetailsForm.get('avgDaysPerWeek');
  }

  getWeeks() {
      return this.operatingDetailsForm.get('avgWeeksPerPeriod');
  }

  getTotalHours() {
      return this.operatingDetailsForm.get('actualHoursPerPeriod');
  }

  private calculateTotalHours() {

    if (this.getHours().valid && this.getDays().valid && this.getWeeks().valid) {

        let totalHours = parseFloat(this.getHours().value) * parseFloat(this.getDays().value) * parseFloat(this.getWeeks().value);

        // if process operates 24 hours, 7 days per week, 52 weeks per year
        if (totalHours == 8736){
            let leapYear = this.facilitySite?.emissionsReport?.year % 4 == 0;

            if (leapYear) {
                totalHours = 8784;
            }
            else {
                totalHours = 8760;
            }
        }

        this.getTotalHours().setValue(Math.round(totalHours));
    }
  }

    getPointSourceScc(processScc: string) {
        this.lookupService.retrievePointSourceSccCode(processScc)
            .subscribe(result => {
                if (result && result.monthlyReporting && this.monthlyReportingEnabled) {
                    this.operatingDetailsForm.disable();
                } else {
                    this.operatingDetailsForm.enable();
                }
            });
    }

  onSubmit() {

    // console.log(this.operatingDetailsForm);

    // let operatingDetails = new OperatingDetail();
    // Object.assign(operatingDetails, this.operatingDetailsForm.value);
    // console.log(operatingDetails);
  }

}
