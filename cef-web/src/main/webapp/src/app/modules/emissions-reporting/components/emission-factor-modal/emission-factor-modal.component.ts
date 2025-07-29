/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EmissionFactor } from 'src/app/shared/models/emission-factor';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-emission-factor-modal',
  templateUrl: './emission-factor-modal.component.html',
  styleUrls: ['./emission-factor-modal.component.scss']
})
export class EmissionFactorModalComponent extends BaseSortableTable implements OnInit {
  @Input() isSltCalcMethod: boolean;
  @Input() tableData: EmissionFactor[];
  @Input() reportYear: number;
  errorMessage: string;
  efControl = new FormControl(null, Validators.required);

  constructor(public activeModal: NgbActiveModal) {
    super();
  }

  ngOnInit() {
	if (this.isSltCalcMethod) {
		this.errorMessage = 'There are no SLT emission factors for this combination of SCC, Pollutant, and Calculation Method. Please review and correct these fields or choose a different calculation method.';
	} else {
		this.errorMessage = 'There are no EPA emission factors for this combination of SCC, Pollutant, and Calculation Method. Please review and correct these fields or choose a different calculation method.';
	}
  }

  onClose() {
    this.activeModal.dismiss();
  }

  onSubmit() {
    if (!this.efControl.valid) {
      this.efControl.markAsTouched();
    } else {
      this.activeModal.close(this.efControl.value);
    }
  }

  createControlMeasureCodeList(factor: EmissionFactor) {
      let controlMeasureStringList = factor.controlMeasureCode.description;
      if (factor.controlMeasureCode.code !== '0') {
          if (factor.controlMeasureCode2.code !== '0') {
              controlMeasureStringList = controlMeasureStringList.concat('; ' + factor.controlMeasureCode2.description);
          }
          if (factor.controlMeasureCode3.code !== '0') {
              controlMeasureStringList = controlMeasureStringList.concat('; ' + factor.controlMeasureCode3.description);
          }
          if (factor.controlMeasureCode4.code !== '0') {
              controlMeasureStringList = controlMeasureStringList.concat('; ' + factor.controlMeasureCode4.description);
          }
          if (factor.controlMeasureCode5.code !== '0') {
              controlMeasureStringList = controlMeasureStringList.concat('; ' + factor.controlMeasureCode5.description);
          }
      }
      return controlMeasureStringList;
  }

  factorRevoked(factor: EmissionFactor) {
      var revokedDate = new Date(factor.revokedDate);
      return (factor.revoked && revokedDate?.getFullYear() < this.reportYear);
  }

}
