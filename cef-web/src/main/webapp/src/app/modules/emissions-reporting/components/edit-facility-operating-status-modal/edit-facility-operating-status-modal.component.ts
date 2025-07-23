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
import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, Validators } from '@angular/forms';
import { BaseCodeLookup } from 'src/app/shared/models/base-code-lookup';

@Component({
  selector: 'app-edit-facility-operating-status-modal',
  templateUrl: './edit-facility-operating-status-modal.component.html'
})
export class EditFacilityOperatingStatusModalComponent implements OnInit {

    operatingStatusYearOptions: number[];
    operatingStatusChangeOptions: BaseCodeLookup[];

    facilitySiteOperatingStatusForm = this.fb.group({
        operatingStatusControl: [null, [Validators.required]],
        statusYearControl: [null, [Validators.required]]
    });

    constructor(public activeModal: NgbActiveModal,
                private fb: FormBuilder) { }

    ngOnInit(): void {
    }

    onClose() { this.activeModal.dismiss(); }

    onSubmit() {

        if (!this.facilitySiteOperatingStatusForm.valid) {
            this.facilitySiteOperatingStatusForm.markAllAsTouched();
        } else {
            const operatingStatus = this.facilitySiteOperatingStatusForm.get('operatingStatusControl').value;
            const statusYear = this.facilitySiteOperatingStatusForm.get('statusYearControl').value;
            this.activeModal.close({operatingStatus: operatingStatus, statusYear: statusYear});
        }
    }
}
