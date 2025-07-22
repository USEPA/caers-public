/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, Input } from '@angular/core';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, Validators, ValidatorFn, FormGroup, ValidationErrors } from '@angular/forms';
import { LookupService } from 'src/app/core/services/lookup.service';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { debounceTime, distinctUntilChanged, map } from 'rxjs/operators';
import { legacyItemValidator } from 'src/app/modules/shared/directives/legacy-item-validator.directive';
import { NaicsCodeType } from 'src/app/shared/enums/naics-code-type';
import { MasterFacilityNaicsCode } from 'src/app/shared/models/master-facility-naics-code';
import { MasterFacilityRecordService } from 'src/app/core/services/master-facility-record.service';

@Component({
  selector: 'app-master-facility-naics-modal',
  templateUrl: './master-facility-naics-modal.component.html',
  styleUrls: ['./master-facility-naics-modal.component.scss']
})
export class MasterFacilityNaicsModalComponent extends BaseSortableTable implements OnInit {
  @Input() masterFacilityId: number;
  @Input() facilityNaics: MasterFacilityNaicsCode[];
  @Input() year: number;
  selectedNaicsCode: MasterFacilityNaicsCode;
  naicsCodeType = NaicsCodeType;
  selectedNaicsCodeType = NaicsCodeType.PRIMARY;
  existingPrimaryCode = false;
  check = true;
  edit: boolean;

  naicsForm = this.fb.group({
    selectedNaics: [null, Validators.required],
    }, { validators: [this.checkValidNaics()]
  });

  facilityNaicsCode: MasterFacilityNaicsCode[];

  constructor(private mfrService: MasterFacilityRecordService,
              private lookupService: LookupService,
              private toastr: ToastrService,
              private fb: FormBuilder,
              public activeModal: NgbActiveModal) {
    super();
  }

  ngOnInit() {
    this.naicsForm.get('selectedNaics').setValidators([Validators.required, legacyItemValidator(this.year, 'NAICS Code', 'code')]);

    this.facilityNaics.forEach(facilityNaics => {
      if(facilityNaics.id !== this.selectedNaicsCode?.id) {
        if (facilityNaics.naicsCodeType === this.naicsCodeType.PRIMARY) {
          this.existingPrimaryCode = true;
          this.selectedNaicsCodeType = this.naicsCodeType.SECONDARY;
        }
      }
    });

    if (this.selectedNaicsCode) {
      this.naicsForm.get('selectedNaics').setValue(this.selectedNaicsCode);
      this.selectedNaicsCodeType = this.selectedNaicsCode.naicsCodeType;
    }

    this.lookupService.retrieveCurrentMFNaicsCodes(this.year)
    .subscribe(result => {
      this.facilityNaicsCode = result;
    });
  }

  isValid() {
    return this.naicsForm.valid;
  }

  onClose() {
    this.activeModal.dismiss();
  }

  onSubmit() {

    this.facilityNaics.forEach(facilityNaics => {
      if(facilityNaics.id !== this.selectedNaicsCode?.id) {

        if (this.naicsForm.value.selectedNaics && facilityNaics.code === this.naicsForm.value.selectedNaics.code) {
          this.check = false;
          this.toastr.error('', 'This Facility already contains this NAICS code, duplicates are not allowed.');
        }

        if (this.naicsForm.value.selectedNaics
          && facilityNaics.naicsCodeType === this.naicsCodeType.PRIMARY && this.selectedNaicsCodeType === this.naicsCodeType.PRIMARY) {
          this.check = false;
          this.toastr.error('', 'Each facility must have only one NAICS code assigned as primary.');
        }
      }
  });

    if (this.check) {
      if (!this.isValid()) {
        this.naicsForm.markAsTouched();
      } else {
        if (!this.edit) {
          const savedFacilityNaics = new MasterFacilityNaicsCode();

          savedFacilityNaics.naicsCodeType = this.selectedNaicsCodeType;
          savedFacilityNaics.masterFacilityRecordId = this.masterFacilityId;
          savedFacilityNaics.code = this.naicsForm.value.selectedNaics.code;
          savedFacilityNaics.description = this.naicsForm.value.selectedNaics.description;

          this.mfrService.createMasterFacilityNaics(savedFacilityNaics)
            .subscribe(() => {
              this.activeModal.close();
            });
        } else {
          this.selectedNaicsCode.naicsCodeType = this.selectedNaicsCodeType;
          this.selectedNaicsCode.masterFacilityRecordId = this.masterFacilityId;
          this.selectedNaicsCode.code = this.naicsForm.value.selectedNaics.code;
          this.selectedNaicsCode.description = this.naicsForm.value.selectedNaics.description;

          this.mfrService.updateMasterFacilityNaics(this.selectedNaicsCode)
            .subscribe(() => {
              this.activeModal.close();
          });
          this.facilityNaics.push(this.selectedNaicsCode);
        }
      }
    }
    this.check = true;
  }

  checkValidNaics(): ValidatorFn {
    return (): ValidationErrors | null => {
      if (this.naicsForm && this.naicsForm.value.selectedNaics === undefined) {
        return { invalidNaics: true };
      }
      return null;
    };
  }

  searchNAICS = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      map(term => this.facilityNaicsCode && this.facilityNaicsCode.filter(v =>
                                        v.code.toLowerCase().indexOf(term.toLowerCase()) > -1
                                        || (v.description ? v.description.toLowerCase().indexOf(term.toLowerCase()) > -1 : false))
                                        .slice(0, 20))
    )

    naicsFormatter = (result: MasterFacilityNaicsCode) => `${result.code}  -  ${result.description}`;
}
