/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, Input } from '@angular/core';
import { ControlAssignment } from 'src/app/shared/models/control-assignment';
import { Control } from 'src/app/shared/models/control';
import { ControlPath } from 'src/app/shared/models/control-path';
import { ControlService } from 'src/app/core/services/control.service';
import { ControlPathService } from 'src/app/core/services/control-path.service';
import { FormBuilder, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { OperatingStatus } from 'src/app/shared/enums/operating-status';

@Component({
  selector: 'app-control-path-assignment-modal',
  templateUrl: './control-path-assignment-modal.component.html',
  styleUrls: ['./control-path-assignment-modal.component.scss']
})
export class ControlPathAssignmentModalComponent implements OnInit {
  @Input() controlPath: ControlPath;
  @Input() facilitySiteId: number;
  @Input() controlPathAssignments: ControlAssignment[];
  selectedControlPathAssignment: ControlAssignment;
  edit: boolean;
  controlPaths: ControlPath[];
  controls: Control[];
  assignmentList: ControlAssignment[] = [];
  controlPathChildField: ControlPath;
  tempShutdownControlWarning: string;

  controlPathAssignmentForm = this.fb.group({
    percentApportionment: ['', [Validators.required, Validators.min(0.1), Validators.max(100), Validators.pattern('^[0-9]{1,3}([\.][0-9]{1,2})?$')]],
    control: [''],
    controlPathChild: [''],
    sequenceNumber: ['', [Validators.required, Validators.pattern('[0-9]*')]]
  }, {validator: this.customValidationFunction});

  constructor(private controlService: ControlService,
              private controlPathService: ControlPathService,
              private fb: FormBuilder,
              public activeModal: NgbActiveModal) { }

  ngOnInit() {
      this.controlService.retrieveForFacilitySite(this.facilitySiteId)
      .subscribe(controls => {
        this.controls = controls;
        if (this.edit) {
          this.setControl();
        }
      });
      this.controlPathService.retrieveForFacilitySite(this.facilitySiteId)
      .subscribe(controlPaths => {
        this.controlPaths = controlPaths.filter(controlPath => controlPath.id !== this.controlPath.id);
        if (this.edit) {
          this.setControlPathChild();
        }
      });

      if (this.selectedControlPathAssignment) {
        this.controlPathAssignmentForm.reset(this.selectedControlPathAssignment);
      } else {
        this.selectedControlPathAssignment = new ControlAssignment();
      }

      this.controlPathAssignmentForm.get('control').valueChanges
      .subscribe(() => {
          this.checkSelectedPathAssignment();
      });

      this.controlPathAssignmentForm.get('controlPathChild').valueChanges
      .subscribe(() => {
          this.checkSelectedPathAssignment();
      });

      // gets all parent and child assignments associated with current control path
      this.controlPathService.retrieveParentAssignmentsForControlPathChild(this.controlPath.id)
      .subscribe(controlPaths => {
        if (controlPaths.length > 0) {
          this.getParentAssignmentList(controlPaths, this.assignmentList);
        } else {
          this.getParentAssignmentList(this.controlPath.assignments, this.assignmentList);
        }
      });
  }

  onClose() {
    this.activeModal.close('dontUpdate');
  }

  isValid() {
    return this.controlPathAssignmentForm.valid;
  }

  onSubmit() {
    this.checkSelectedPathAssignment();
    if (!this.isValid()) {
        this.controlPathAssignmentForm.markAllAsTouched();
        this.controlPathAssignmentForm.controls.control.markAsDirty();
    } else {
        if (this.controlPathAssignmentForm.controls.controlPathChild.value) {
          this.controlPathAssignmentForm.controls.control.reset();
        }
        if (this.controlPathAssignmentForm.controls.control.value) {
          this.controlPathAssignmentForm.controls.controlPathChild.reset();
        }
        Object.assign(this.selectedControlPathAssignment, this.controlPathAssignmentForm.value);
        this.selectedControlPathAssignment.controlPath = this.controlPath;
        this.selectedControlPathAssignment.facilitySiteId = this.facilitySiteId;
        if (this.edit) {
          this.controlPathService.updateAssignmentForControlPath(this.selectedControlPathAssignment)
          .subscribe(() => {
            this.activeModal.close();
          });
        } else {
          this.controlPathService.createAssignmentForControlPath(this.selectedControlPathAssignment)
          .subscribe(() => {
            this.activeModal.close();
          });
        }
      }
  }

  // sets value of controlPathChild dropdown when editting an assignment
  setControlPathChild() {
    this.controlPathChildField = this.controlPathAssignmentForm.get('controlPathChild').value;
    if (this.controlPathChildField) {
      for (const controlPath of this.controlPaths) {
        if (controlPath.id === this.controlPathChildField.id) {
          this.controlPathAssignmentForm.controls.controlPathChild.setValue(controlPath);
          break;
        }
      }
    }
  }

  // sets value of control dropdown when editting an assignment
  setControl() {
    const controlField = this.controlPathAssignmentForm.get('control').value;
    if (controlField) {
      for (const control of this.controls) {
        if (control.id === controlField.id) {
          this.controlPathAssignmentForm.controls.control.setValue(control);
          break;
        }
      }
    }
  }

  checkSelectedPathAssignment() {
    let pathList = [];
    let checkPathList;

    // if control path is chosen, check all paths with selected control path child
    if (this.controlPathAssignmentForm.get('controlPathChild').value) {
      pathList = this.assignmentList.filter(val => val.controlPathChild !== null);
      checkPathList = pathList.filter(val => val.controlPathChild.id === this.controlPathAssignmentForm.get('controlPathChild').value.id).length;
      if (this.edit && this.selectedControlPathAssignment.controlPathChild
        && this.selectedControlPathAssignment.controlPathChild.id === this.controlPathAssignmentForm.get('controlPathChild').value.id) {
        if (checkPathList > 1) {
          this.controlPathAssignmentForm.get('controlPathChild').markAsTouched();
          this.controlPathAssignmentForm.get('controlPathChild').setErrors({ duplicateChildPath: true });
        } else {
          this.checkChildPathControl();
        }
      } else {
        if (checkPathList > 0 || this.assignmentList.filter(val => val.controlPath.id === this.controlPathAssignmentForm.get('controlPathChild').value.id).length > 0) {
          this.controlPathAssignmentForm.get('controlPathChild').markAsTouched();
          this.controlPathAssignmentForm.get('controlPathChild').setErrors({ duplicateChildPath: true });
        } else {
          this.checkChildPathControl();
        }
      }
    } else {
      this.checkSelectedControl();
    }
  }

  checkSelectedControl() {
    let controlList = [];

    // if control is chosen, check all control assignments with selected control
    if (this.controlPathAssignmentForm.get('control').value) {
      controlList = this.assignmentList.filter(val => val.control && (val.control.id === this.controlPathAssignmentForm.get('control').value.id));

      if (this.controlPathAssignmentForm.get('control').value.operatingStatusCode.code === OperatingStatus.TEMP_SHUTDOWN) {
        this.tempShutdownControlWarning = 'Warning: This Control Path is associated with the Temporarily Shutdown Control Device.';
      } else {
        this.tempShutdownControlWarning = null;
      }

      if (this.edit && controlList && (this.selectedControlPathAssignment.control
      && this.selectedControlPathAssignment.control.id === this.controlPathAssignmentForm.get('control').value.id)) {
        if (this.controlPathAssignmentForm.get('control').value.assignments && this.controlPathAssignmentForm.get('control').value.assignments.controlPathChild !== null && controlList.length > 1) {
          this.controlPathAssignmentForm.get('control').markAsTouched();
          this.controlPathAssignmentForm.get('control').setErrors({ duplicateControl: true });
        }
      } else if (controlList.length > 0) {
        this.controlPathAssignmentForm.get('control').markAsTouched();
        this.controlPathAssignmentForm.get('control').setErrors({ duplicateControl: true });
      }
    }
  }

  checkChildPathControl() {
    let selectedControlAssignmentList = [];
    let existingControlAssignmentList = [];

    // get child assignments of selected control path
    this.getChildAssignmentList(this.controlPathAssignmentForm.get('controlPathChild').value.assignments, selectedControlAssignmentList);
    existingControlAssignmentList = this.assignmentList.filter(val => val.control !== null);

    if (!this.edit && selectedControlAssignmentList.length > 0) {
      for (const selectedControl of selectedControlAssignmentList) {
        if (existingControlAssignmentList.filter(val => val.control !== null && val.control.id === selectedControl.control.id).length > 0) {
          this.controlPathAssignmentForm.get('controlPathChild').markAsTouched();
          this.controlPathAssignmentForm.get('controlPathChild').setErrors({ duplicatePathControl: true });
          break;
        }
      }
    }
  }

  getChildAssignmentList(assignmentList, mainAssignmentList) {
    for (const val of assignmentList) {
      if (val.control) {
        mainAssignmentList.push(val);
      }
      if (val.controlPathChild) {
        this.getChildAssignmentList(val.controlPathChild.assignments, mainAssignmentList);
      }
    }
  }

  getParentAssignmentList(assignmentList, mainAssignmentList) {
    const tempList = [];
    const checked = [];
    for (const ca of assignmentList) {
      this.controlPathService.retrieveParentAssignmentsForControlPathChild(ca.controlPath.id)
      .subscribe(parentAssignments => {
        if (parentAssignments.length > 0) {
          this.getParentAssignmentList(parentAssignments, mainAssignmentList);
        } else if (parentAssignments.length === 0) {
          if (checked.filter(val => val === ca.controlPath.id).length === 0) {
            checked.push(ca.controlPath.id);
            this.controlPathService.retrieveAssignmentsForControlPath(ca.controlPath.id)
            .subscribe(controlPathAssignments => {
              controlPathAssignments.forEach(val =>
                mainAssignmentList.push(val));
            });
          }
          if (ca.controlPathChild !== null) {
            tempList.push(ca);
          }
          this.getChildAssignmentList(tempList, mainAssignmentList);
        }
      });
    }
  }

  customValidationFunction(formGroup): any {
    const controlField = formGroup.controls.control.value;
    const controlPathChildField = formGroup.controls.controlPathChild.value;
    const sequenceNumber = formGroup.controls.sequenceNumber.value;
    const percentApportionment = formGroup.controls.percentApportionment.value;
    if (controlField && controlPathChildField) {
      return { controlAndPathSelected: true };
    }
    if (!controlField && !controlPathChildField) {
      return { controlAndPathNotSelected: true};
    }
    if (controlField && !controlPathChildField && controlField.operatingStatusCode.code === OperatingStatus.PERM_SHUTDOWN) {
      return { permShutdownControl: true };
    }
    if ((controlField || controlPathChildField) && (sequenceNumber && percentApportionment)) {
      formGroup.status = 'VALID';
      return null;
    }
  }

}
