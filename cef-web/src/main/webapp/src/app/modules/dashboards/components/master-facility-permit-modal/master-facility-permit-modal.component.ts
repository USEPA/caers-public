import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, Validators} from "@angular/forms";
import {MasterFacilityRecordService} from "src/app/core/services/master-facility-record.service";
import {LookupService} from "src/app/core/services/lookup.service";
import {ToastrService} from "ngx-toastr";
import {FacilityPermit} from "src/app/shared/models/facility-permit";
import {PermitType} from "src/app/shared/models/permit-type";
import {FormUtilsService} from "src/app/core/services/form-utils.service";

@Component({
  selector: 'app-master-facility-permit-modal',
  templateUrl: './master-facility-permit-modal.component.html',
  styleUrls: ['./master-facility-permit-modal.component.scss']
})
export class MasterFacilityPermitModalComponent implements OnInit {
  @Input() masterFacilityId: number;
  @Input() masterFacilityPermits: FacilityPermit[]; //existing permits
  selectedPermit: FacilityPermit;
  edit: boolean;

  permitTypeValues: PermitType[];

  permitForm = this.fb.group({
    permitNumber: ['', [Validators.required, Validators.maxLength(50)]],
    permitType: ['', [Validators.required]],
    otherDescription: ['', [Validators.required, Validators.maxLength(100)]],
    startDate: ['', [Validators.required]],
    endDate: ['', [Validators.required]],
    comments: ['', [Validators.maxLength(2000)]]
  });

  constructor(private mfrService: MasterFacilityRecordService,
              private lookupService: LookupService,
              private toastr: ToastrService,
              private fb: FormBuilder,
              public activeModal: NgbActiveModal,
              public formUtils: FormUtilsService) { }

  ngOnInit(): void {

      this.permitForm.get('permitType').valueChanges
          .subscribe(value => {
              if (value?.type === 'Other') {
                  this.permitForm.get('otherDescription').enable();
              } else {
                  this.permitForm.get('otherDescription').disable();
              }
          });

    if (this.selectedPermit) {
      this.permitForm.reset(this.selectedPermit);
    }

    this.lookupService.retrievePermitTypes()
      .subscribe(result => {
          this.permitTypeValues = result;
          this.permitForm.get('permitType').updateValueAndValidity()
      });

  }

  selectedtype() {
      return this.permitForm.get('permitType');
  }

  isValid() {
    return this.permitForm.valid;
  }

  onClose() {
    this.activeModal.dismiss();
  }

  onSubmit() {

    if (!this.isValid()) {
      this.permitForm.markAsTouched();
    } else {

        const updatedPermit = new FacilityPermit();

        Object.assign(updatedPermit, this.permitForm.value);
        updatedPermit.masterFacilityId = this.masterFacilityId;

        if (!this.edit) {

        this.mfrService.createMasterFacilityPermit(updatedPermit)
            .subscribe(() => {
              this.activeModal.close();
            });
      } else {

          updatedPermit.id = this.selectedPermit.id;

        this.mfrService.updateMasterFacilityPermit(updatedPermit)
            .subscribe(() => {
              this.activeModal.close();
            });
        this.masterFacilityPermits.push(updatedPermit);
      }
    }

  }

}
