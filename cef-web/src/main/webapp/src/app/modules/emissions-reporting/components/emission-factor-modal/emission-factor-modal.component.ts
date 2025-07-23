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
