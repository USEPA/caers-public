/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, Input, OnChanges, Output, EventEmitter } from '@angular/core';
import { BulkEntryReportingPeriod } from 'src/app/shared/models/bulk-entry-reporting-period';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ReportingPeriodService } from 'src/app/core/services/reporting-period.service';
import { BulkEntryEmissionHolder } from 'src/app/shared/models/bulk-entry-emission-holder';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { OperatingStatus } from 'src/app/shared/enums/operating-status';
import { LookupService } from "src/app/core/services/lookup.service";
import { UnitMeasureCode } from "src/app/shared/models/unit-measure-code";
import { FormUtilsService } from "src/app/core/services/form-utils.service";
import { legacyUomValidator } from "src/app/modules/shared/directives/legacy-uom-validator.directive";

@Component({
  selector: 'app-bulk-entry-reporting-period-table',
  templateUrl: './bulk-entry-reporting-period-table.component.html',
  styleUrls: ['./bulk-entry-reporting-period-table.component.scss']
})
export class BulkEntryReportingPeriodTableComponent extends BaseSortableTable implements OnInit, OnChanges {
  @Input() tableData: BulkEntryReportingPeriod[];
  @Input() readOnlyMode: boolean;
  @Output() periodsUpdated = new EventEmitter<BulkEntryEmissionHolder[]>();
  baseUrl: string;
  facilitySite: FacilitySite;
  savingBulkInfo = false;
  fuelUseUomValues: UnitMeasureCode[];

  reportingPeriodForm = this.fb.group({});

  operatingStatus = OperatingStatus;

  constructor(
      public formUtils: FormUtilsService,
      private reportingPeriodService: ReportingPeriodService,
      private lookupService: LookupService,
      private route: ActivatedRoute,
      private fb: FormBuilder,
      private toastr: ToastrService) {
    super();
    this.fuelUseUomValues = [];
  }

  ngOnInit() {
    this.getFuelUoms();

    this.tableData.forEach(rp => {

      const disabled = rp.operatingStatusCode.code === OperatingStatus.TEMP_SHUTDOWN
                    || rp.operatingStatusCode.code === OperatingStatus.PERM_SHUTDOWN;
      this.reportingPeriodForm.addControl('throughput' + rp.reportingPeriodId, new FormControl(
        {value: rp.calculationParameterValue, disabled},
        { validators: [
          Validators.required,
          Validators.min(0),
          Validators.pattern('^[0-9]*\\.?[0-9]+$')
        ], updateOn: 'blur'}));

        this.reportingPeriodForm.addControl('fuel' + rp.reportingPeriodId, new FormControl(
          {value: rp.fuelUseValue, disabled},
          { validators: [
            Validators.min(0),
            Validators.pattern('^[0-9]*\\.?[0-9]+$')
          ], updateOn: 'blur'}));

        this.reportingPeriodForm.addControl('fuelUom' + rp.reportingPeriodId, new FormControl(
          { },
          { validators: [
            legacyUomValidator()
          ], updateOn: 'blur'}));
        this.reportingPeriodForm.get('fuelUom' + rp.reportingPeriodId).patchValue(rp.fuelUseUom);
        if (!rp.fuelUseRequired || this.readOnlyMode) {
          this.reportingPeriodForm.get('fuelUom' + rp.reportingPeriodId).disable();
        }
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
    this.tableData.sort((a, b) => {

      if (a.unitIdentifier === b.unitIdentifier) {
        return a.emissionsProcessIdentifier > b.emissionsProcessIdentifier ? 1 : -1;
      }
      return a.unitIdentifier > b.unitIdentifier ? 1 : -1;
    });
  }

  getFuelUoms() {
    this.lookupService.retrieveFuelUseUom()
      .subscribe(result => {
        this.fuelUseUomValues = result;
      });
  }

  onSubmit() {
    if (!this.reportingPeriodForm.valid) {
      this.reportingPeriodForm.markAllAsTouched();
    } else {

      let valueMismatch = false;
      this.savingBulkInfo = true;
      this.tableData.forEach(rp => {
        rp.calculationParameterValue = this.reportingPeriodForm.get('throughput' + rp.reportingPeriodId).value;
        rp.fuelUseValue = this.reportingPeriodForm.get('fuel' + rp.reportingPeriodId).value;
        rp.fuelUseUom = this.reportingPeriodForm.get('fuelUom' + rp.reportingPeriodId).value;

        if (rp.fuelUseMaterialCode?.code === rp.calculationMaterialCode?.code
            && rp.fuelUseUom?.code === rp.calculationParameterUom?.code
            && rp.fuelUseValue !== rp.calculationParameterValue && !valueMismatch) {

            valueMismatch = true;
        }
      });

      this.reportingPeriodService.bulkUpdate(this.facilitySite.id, this.tableData)
      .subscribe(result => {
        this.savingBulkInfo = false;
        this.periodsUpdated.emit(result);
        const successMessage = 'Throughput and Fuel values successfully saved and Total Emissions have been recalculated.';
        if (valueMismatch) {
          this.toastr.warning('', 'Unless the process is an alternative throughput, Fuel Value and Throughput Value should match when their Materials and UoMs are the same. '
                                   + successMessage);
        } else {
          this.toastr.success('', successMessage);
        }
        // reset dirty flags
        this.reportingPeriodForm.markAsPristine();
      });

    }
  }

}
