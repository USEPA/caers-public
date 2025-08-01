/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, Input, OnChanges, OnInit } from "@angular/core";
import { AbstractControl, FormBuilder, FormControl, ValidatorFn, Validators } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { MonthlyFuelReportingService } from "src/app/core/services/monthly-fuel-reporting.services";
import { BaseSortableTable } from "src/app/shared/components/sortable-table/base-sortable-table";
import { MonthlyReportingPeriod } from "src/app/shared/enums/monthly-reporting-periods";
import { OperatingStatus } from "src/app/shared/enums/operating-status";
import { FacilitySite } from "src/app/shared/models/facility-site";
import { MonthlyFuelReporting } from "src/app/shared/models/monthly-fuel-reporting";

@Component({
  selector: 'app-monthly-fuel-table',
  templateUrl: './monthly-fuel-table.component.html',
  styleUrls: ['./monthly-fuel-table.component.scss']
})

export class MonthlyFuelTableComponent extends BaseSortableTable implements OnInit, OnChanges {
	
	@Input() period: string;
	@Input() facilitySite:FacilitySite
	@Input() readOnlyMode: boolean;
	tableData: MonthlyFuelReporting[];
	operatingStatus = OperatingStatus;
	baseUrl: string;
	month = MonthlyReportingPeriod;
	
	monthlyReportingForm = this.fb.group({});
	active: boolean;
	
	constructor(
		private monthlyFuelReportingService: MonthlyFuelReportingService,
		private route: ActivatedRoute,
		private fb: FormBuilder) {
			super();
	}
	
	ngOnInit() {
		this.monthlyFuelReportingService.retrieveForMonthData(this.facilitySite.id, this.period)
		.subscribe(result => {
			this.tableData = result;
			this.tableData?.forEach(mr => {
		      const disabled = mr.operatingStatusCode.code === OperatingStatus.TEMP_SHUTDOWN
		                    || mr.operatingStatusCode.code === OperatingStatus.PERM_SHUTDOWN
							|| this.readOnlyMode;
		      this.monthlyReportingForm.addControl(this.period + mr.reportingPeriodId, new FormControl(
		        {value: mr.fuelUseValue, disabled},
		        { validators: [
		          Validators.min(0),
		          Validators.pattern('^[0-9]*\\.?[0-9]+$'),
		        ], updateOn: 'blur'})
			  );
			  this.monthlyReportingForm.addControl(this.period + 'totalOpDays' + mr.reportingPeriodId, new FormControl(
		        {value: mr.totalOperatingDays, disabled},
		        { validators: [
		          Validators.min(0),
		          Validators.pattern('^[0-9]*\\.?[0-9]+$'),
				  this.checkTotalDays(this.period)
		        ], updateOn: 'blur'}),
		       );
		    });
		});

		this.route.paramMap
	    .subscribe(map => {
	      this.baseUrl = `/facility/${map.get('facilityId')}/report/${map.get('reportId')}`;
	    });

		this.route.data
	    .subscribe((data: { facilitySite: FacilitySite }) => {
	
	      this.facilitySite = data.facilitySite;
	    });
	}
	
	ngOnChanges() {
	}
	
	checkTotalDays(period): ValidatorFn {
        return (control: AbstractControl): {[key: string]: any} | null => {
			let leapYear = this.facilitySite.emissionsReport.year % 4 == 0;
			
			switch (period) {
	            case this.month.JANUARY:
					control.addValidators(Validators.max(31));
					break;
	            case this.month.FEBRUARY:
					if (leapYear) {
						control.addValidators(Validators.max(29));
					} else {
						control.addValidators(Validators.max(28));
					}
					break;
				case this.month.MARCH:
					control.addValidators(Validators.max(31));
					break;
				case this.month.APRIL:
					control.addValidators(Validators.max(30));
					break;
				case this.month.MAY:
					control.addValidators(Validators.max(31));
					break;
				case this.month.JUNE:
					control.addValidators(Validators.max(30));
					break;
				case this.month.JULY:
					control.addValidators(Validators.max(31));
					break;
				case this.month.AUGUST:
					control.addValidators(Validators.max(31));
					break;
				case this.month.SEPTEMBER:
					control.addValidators(Validators.max(30));
					break;
				case this.month.OCTOBER:
					control.addValidators(Validators.max(31));
					break;
				case this.month.NOVEMBER:
					control.addValidators(Validators.max(30));
					break;
				case this.month.DECEMBER:
					control.addValidators(Validators.max(31));
					break;
				default:
            }
            return null;
        };
	}
	
}