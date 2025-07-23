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
import { Observable } from 'rxjs';
import { debounceTime, distinctUntilChanged, map, switchMap } from 'rxjs/operators';
import { Pollutant } from 'src/app/shared/models/pollutant';
import { ControlPollutant } from 'src/app/shared/models/control-pollutant';
import { LookupService } from 'src/app/core/services/lookup.service';
import { ControlService } from 'src/app/core/services/control.service';
import { Validators, FormBuilder } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { legacyItemValidator } from 'src/app/modules/shared/directives/legacy-item-validator.directive';
import { ControlPathPollutant } from 'src/app/shared/models/control-path-pollutant';
import { ControlPathService } from 'src/app/core/services/control-path.service';
import { formatPollutant } from 'src/app/modules/shared/utils/format';
import { UserService } from 'src/app/core/services/user.service';
import { FacilitySiteService } from 'src/app/core/services/facility-site.service';
import { BaseCodeLookup } from 'src/app/shared/models/base-code-lookup';

@Component({
  selector: 'app-control-pollutant-modal',
  templateUrl: './control-pollutant-modal.component.html',
  styleUrls: ['./control-pollutant-modal.component.scss']
})
export class ControlPollutantModalComponent implements OnInit {
  controlPollutants: ControlPollutant[];
  controlPathPollutants: ControlPathPollutant[];
  pollutantValues: Pollutant[];
  selectedPollutant: Pollutant;
  selectedControlPollutant: ControlPollutant;
  selectedControlPathPollutant: ControlPathPollutant;
  controlId: number;
  controlPathId: number;
  readonly path = 'CONTROL_PATH';
  readonly device = 'CONTROL_DEVICE';
  facilitySiteId: number;
  programSystemCode: BaseCodeLookup;
  year: number;
  edit: boolean;
  duplicateCheck = true;
  controlPollutantFor: string;

  pollutantForm = this.fb.group({
    pollutant: [null , [Validators.required]],
    percentReduction: ['', [
      Validators.required,
      Validators.max(99.9),
      Validators.min(5),
      Validators.pattern('^[0-9]{1,4}([\.][0-9]{1})?$')
    ]]
  });

  constructor(private lookupService: LookupService,
              private fb: FormBuilder,
              public activeModal: NgbActiveModal,
              private controlService: ControlService,
              private controlPathService: ControlPathService,
              private facilitySiteService: FacilitySiteService,
              private toastr: ToastrService) { }

  ngOnInit() {
    if (this.controlPollutantFor === this.device) {
      if (this.selectedControlPollutant) {
        this.pollutantForm.reset(this.selectedControlPollutant);
      } else {
        this.selectedControlPollutant = new ControlPollutant();
      }
    } else if (this.controlPollutantFor === this.path) {
      if (this.selectedControlPathPollutant) {
        this.pollutantForm.reset(this.selectedControlPathPollutant);
      } else {
        this.selectedControlPathPollutant = new ControlPathPollutant();
      }
    }

    this.pollutantForm.get('pollutant').setValidators([
      Validators.required,
      legacyItemValidator(this.year, 'Pollutant', 'pollutantName')
    ]);
    
    this.facilitySiteService.retrieveByIdAndReportYear(this.facilitySiteId, this.year).pipe( switchMap(f => {
        this.programSystemCode = f.programSystemCode;
        return this.lookupService.retrieveCurrentPollutants(this.year);
    })).subscribe(result => {
        this.pollutantValues = result.filter(r => 
          r.programSystemCodes.length === 0 || r.programSystemCodes.some(psc => psc.code == this.programSystemCode.code)
        );
    });

    this.pollutantForm.get('pollutant').valueChanges.pipe(
      debounceTime(200),
      distinctUntilChanged()
    ).subscribe(() => {
      const pollutantFormControl = this.pollutantForm.get('pollutant');
      if (this.pollutantValues
        && pollutantFormControl.value != null
        && typeof pollutantFormControl.value === 'string'
        && pollutantFormControl.value.length > 0
      ) {
        // searchedPollutant === 'string' when no pollutant selected
        this.pollutantForm.get('pollutant').setErrors({invalidPollutant: true});
      }
    });

  }

  isValid() {
    return this.pollutantForm.valid;
  }

  onClose() {
    this.activeModal.close('dontUpdate');
  }
  onSubmit() {
    if (!this.isValid()) {
        this.pollutantForm.markAllAsTouched();
    } else {

      if (this.controlPollutantFor === this.device) {
        if (!this.edit){
          this.controlPollutants.forEach(pollutant => {
            if (pollutant.pollutant.pollutantName === this.pollutantForm.get('pollutant').value.pollutantName) {
              this.duplicateCheck = false;
              this.toastr.error('', 'This Control Path already contains this Control Pollutant, duplicates are not allowed.');
            }
          });
        }

        if (this.duplicateCheck) {
          this.selectedPollutant = this.pollutantForm.get('pollutant').value;
          this.selectedControlPollutant.pollutant = this.selectedPollutant;
          this.selectedControlPollutant.percentReduction = this.pollutantForm.get('percentReduction').value;
          this.selectedControlPollutant.controlId = this.controlId;
          this.selectedControlPollutant.facilitySiteId = this.facilitySiteId;

          if (!this.edit) {
            this.controlService.createPollutant(this.selectedControlPollutant).subscribe(() => {
              this.activeModal.close();
            });
          } else {
            this.controlService.updatePollutant(this.selectedControlPollutant).subscribe(() => {
              this.activeModal.close();
            });
          }
        }
      } else if (this.controlPollutantFor === this.path) {
        if (!this.edit) {
          this.controlPathPollutants.forEach(pollutant => {
            if (pollutant.pollutant.pollutantName === this.pollutantForm.get('pollutant').value.pollutantName) {
              this.duplicateCheck = false;
              this.toastr.error('', 'This Control Path already contains this Control Path Pollutant, duplicates are not allowed.');
            }
          });
        }

        if (this.duplicateCheck) {
          this.selectedPollutant = this.pollutantForm.get('pollutant').value;
          this.selectedControlPathPollutant.pollutant = this.selectedPollutant;
          this.selectedControlPathPollutant.percentReduction = this.pollutantForm.get('percentReduction').value;
          this.selectedControlPathPollutant.controlPathId = this.controlPathId;
          this.selectedControlPathPollutant.facilitySiteId = this.facilitySiteId;

          if (!this.edit) {
            this.controlPathService.createPollutant(this.selectedControlPathPollutant).subscribe(() => {
              this.activeModal.close();
            });
          } else {
            this.controlPathService.updatePollutant(this.selectedControlPathPollutant).subscribe(() => {
              this.activeModal.close();
            });
          }
        }
      }
    }
    this.duplicateCheck = true;
  }

  searchPollutants = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      map(term => this.pollutantValues && this.pollutantValues.filter(v => v.pollutantName.toLowerCase().indexOf(term.toLowerCase()) > -1
                                        || v.pollutantCode.toLowerCase().indexOf(term.toLowerCase()) > -1
                                        || (v.pollutantCasId ? v.pollutantCasId.toLowerCase().indexOf(term.toLowerCase()) > -1 : false))
                                        .slice(0, 20)))

  pollutantFormatter = (result: Pollutant) => formatPollutant(result);

}
