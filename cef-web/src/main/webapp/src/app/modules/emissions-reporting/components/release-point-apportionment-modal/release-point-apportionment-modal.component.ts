/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, Input} from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Process } from 'src/app/shared/models/process';
import { ReleasePointApportionment } from 'src/app/shared/models/release-point-apportionment';
import { ReleasePointService } from 'src/app/core/services/release-point.service';
import { ReleasePoint } from 'src/app/shared/models/release-point';
import { FormBuilder, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { ControlPath } from 'src/app/shared/models/control-path';

@Component({
  selector: 'app-release-point-apportionment-modal',
  templateUrl: './release-point-apportionment-modal.component.html',
  styleUrls: ['./release-point-apportionment-modal.component.scss']
})
export class ReleasePointApportionmentModalComponent implements OnInit {
  @Input() process: Process;
  @Input() releasePointApportionments: ReleasePointApportionment[];
  @Input() facilitySiteId: number;
  controlPaths: ControlPath[];
  edit: boolean;
  duplicateCheck = true;
  selectedReleasePoint: ReleasePointApportionment;
  releasePoints: ReleasePoint[];
  rpApptSelected: string;
  controlPathSelected: ControlPath;
  controlPathId: number;

  releasePointApptForm = this.fb.group({
    percent: ['', [Validators.required, Validators.min(1), Validators.max(100), Validators.pattern('^[0-9]{1,3}([\.][0-9]{1,2})?$')]],
    selectedReleasePointAppt: ['', Validators.required],
    controlPath: ['']
  });

  constructor(public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private releasePointService: ReleasePointService,
              private toastr: ToastrService) {}

  ngOnInit() {
    if (this.edit) {
      const releasePointApptControl = this.releasePointApptForm.get('selectedReleasePointAppt');
      releasePointApptControl.setValidators(null);

      // sets default value in dropdown
      for (const releasePt of this.releasePointApportionments) {
        if (releasePt.releasePointIdentifier === this.selectedReleasePoint.releasePointIdentifier) {
        this.rpApptSelected = releasePt.releasePointIdentifier;
        break;
        }
      }
      if (this.selectedReleasePoint.controlPath) {
        for (const controlPath of this.controlPaths) {
          if (controlPath.id === this.selectedReleasePoint.controlPath.id) {
            this.controlPathSelected = controlPath;
            break;
          }
        }
      }

    }

    if (this.selectedReleasePoint) {
      this.releasePointApptForm.reset(this.selectedReleasePoint);
    } else {
      this.selectedReleasePoint = new ReleasePointApportionment();
    }

    this.releasePointService.retrieveForFacility(this.facilitySiteId)
    .subscribe(points => {
      this.releasePoints = points;
    });

  }

  isValid() {
    return this.releasePointApptForm.valid;
  }

  onClose() {
    this.activeModal.close('dontUpdate');
  }

  onSubmit(event) {
    event.target.disabled = true;
    if (!this.isValid()) {
        event.target.disabled = false;
        this.releasePointApptForm.markAllAsTouched();
      } else {
        // check for duplicate selection
        this.releasePointApportionments.forEach(apportionment => {
          if (this.releasePointApptForm.get('selectedReleasePointAppt').value) {
            if ((this.selectedReleasePoint.releasePointIdentifier !== this.releasePointApptForm.get('selectedReleasePointAppt').value.toString())
            && (apportionment.releasePointIdentifier === this.releasePointApptForm.get('selectedReleasePointAppt').value.toString())) {
              this.duplicateCheck = false;
              event.target.disabled = false;
              // tslint:disable-next-line: max-line-length
              this.toastr.error('', 'This Emissions Process already contains this Release Point Apportionment, duplicates are not allowed.');
            }
          }
        });
        if (this.duplicateCheck) {
        this.releasePoints.forEach(releasePoint => {
          if (!this.edit || this.releasePointApptForm.get('selectedReleasePointAppt').value) {
            if (releasePoint.releasePointIdentifier === this.releasePointApptForm.get('selectedReleasePointAppt').value.toString()) {
              this.selectedReleasePoint.releasePointId = releasePoint.id;
              this.selectedReleasePoint.emissionsProcessId = this.process.id;
              this.selectedReleasePoint.facilitySiteId = this.facilitySiteId;
              Object.assign(this.selectedReleasePoint, this.releasePointApptForm.value);
              if (!this.selectedReleasePoint.controlPath) {
                this.selectedReleasePoint.controlPath = null;
              }
            }
          } else {
              if (releasePoint.releasePointIdentifier === this.selectedReleasePoint.releasePointIdentifier) {
                this.selectedReleasePoint.releasePointId = releasePoint.id;
                this.selectedReleasePoint.emissionsProcessId = this.process.id;
                this.selectedReleasePoint.facilitySiteId = this.facilitySiteId;
                this.selectedReleasePoint.percent = this.releasePointApptForm.get('percent').value;
              }
          }
        });
        if (!this.edit) {
            this.releasePointService.createAppt(this.selectedReleasePoint)
            .subscribe(() => {
              this.activeModal.close();
            });
          } else {
            this.releasePointService.updateAppt(this.selectedReleasePoint)
            .subscribe(() => {
              this.activeModal.close();
            });
          }
        }
  }
    this.duplicateCheck = true;
  }

}
