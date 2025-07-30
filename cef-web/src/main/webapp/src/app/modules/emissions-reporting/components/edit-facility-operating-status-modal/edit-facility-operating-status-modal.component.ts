/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
