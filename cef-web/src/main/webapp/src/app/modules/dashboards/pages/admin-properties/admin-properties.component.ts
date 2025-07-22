/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit } from '@angular/core';
import { AdminPropertyService } from 'src/app/core/services/admin-property.service';
import { AppProperty } from 'src/app/shared/models/app-property';
import { ToastrService } from 'ngx-toastr';
import { FormBuilder, Validators, FormControl } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { RecalculateEmissionTonsModalComponent } from 'src/app/modules/dashboards/components/recalculate-emission-tons-modal/recalculate-emission-tons-modal.component';
import { UserFacilityAssociationService } from 'src/app/core/services/user-facility-association.service';
import { EmissionsReportingService } from 'src/app/core/services/emissions-reporting.service';
import { FacilitySiteService } from 'src/app/core/services/facility-site.service';
import {wholeNumberValidator} from 'src/app/modules/shared/directives/whole-number-validator.directive';
import { SharedService } from 'src/app/core/services/shared.service';

const Admin_AnnouncementText = 'feature.announcement.text';

@Component({
  selector: 'app-admin-properties',
  templateUrl: './admin-properties.component.html',
  styleUrls: ['./admin-properties.component.scss']
})
export class AdminPropertiesComponent implements OnInit {
  properties: AppProperty[];

  propertyForm = this.fb.group({});

  deleteReportForm = this.fb.group({
    deleteReportId: [null, [wholeNumberValidator()]],
  });

  deleteReportInfo: string;

  migrating = false;
  migrationFeature = false;

  constructor(
      private propertyService: AdminPropertyService,
      private ufaService: UserFacilityAssociationService,
      private reportService: EmissionsReportingService,
      private facilitySiteService: FacilitySiteService,
	    private sharedService: SharedService,
      private fb: FormBuilder,
      private modalService: NgbModal,
      private toastr: ToastrService) { }

  ngOnInit() {
    this.propertyService.retrieveAll()
    .subscribe(result => {

      result.sort((a, b) => (a.name > b.name) ? 1 : -1);
      result.forEach(prop => {
        if (prop.datatype !== 'boolean') {
	
          if (!prop.required) {
            this.propertyForm.addControl(prop.name, new FormControl(prop.value));
          } else {
            this.propertyForm.addControl(prop.name, new FormControl(prop.value, { validators: [Validators.required]}));
          }
        } else {
          const booleanValue = (prop.value.toLowerCase() === 'true');
          this.propertyForm.addControl(prop.name, new FormControl(booleanValue));
        }
      });

      this.properties = result;
      this.setMigrationFeature();
    });
  }

  private setMigrationFeature() {
    this.migrationFeature = this.properties
                            .find(p => p.name.toLowerCase() === 'feature.cdx-association-migration.enabled')
                            ?.value?.toString() === 'true';
  }

  openTestEmailModal() {

    const adminEmails = this.properties.find(p => p.name.toLowerCase() === 'email.admin').value;

    const modalMessage = `Are you sure you want to send a test email to ${adminEmails}?`;
    const modalRef = this.modalService.open(ConfirmationDialogComponent);
    modalRef.componentInstance.message = modalMessage;
    modalRef.componentInstance.continue.subscribe(() => {
        this.sendTestEmail();
    });
  }

  sendTestEmail() {
    this.propertyService.sendTestEmail()
    .subscribe(() => {
      this.toastr.success('', 'Test email sent');
    });
  }

  openRecalculateEmissionTonsModal() {

    const modalRef = this.modalService.open(RecalculateEmissionTonsModalComponent);
    modalRef.result.then((reportId: number) => {
      if (reportId) {
        this.recalculateEmissionTons(reportId);
      }
    }, () => {
      // needed for dismissing without errors
    });
  }

  recalculateEmissionTons(reportId: number) {
    this.propertyService.recalculateEmissionTotalTons(reportId)
    .subscribe(result => {
      console.log(result);
      this.toastr.success('', result.length + ' emissions had their emission total in tons updated.');
    });
  }

  calculateNonPointStandardizedFuelUse() {
    this.propertyService.calculateNonPointStandardizedFuelUse()
    .subscribe(result => {
      this.toastr.success('', 'fuel use has been standardized for non-point fuel report for ' + result.length + ' records.');
    });
  }

  openMigrateUserAssociationsModal() {

    const modalMessage = `Are you sure you want to migrate user associations? This should only ever be done once.`;
    const modalRef = this.modalService.open(ConfirmationDialogComponent);
    modalRef.componentInstance.message = modalMessage;
    modalRef.componentInstance.continue.subscribe(() => {
        this.migrateUserAssociations();
    });
  }

  migrateUserAssociations() {
    this.migrating = true;
    this.ufaService.migrateUserAssociations()
    .subscribe(() => {
      this.toastr.success('', 'User Associations Migrated');
      this.migrating = false;
    });
  }

  onSubmit() {
    if (!this.propertyForm.valid) {
      this.propertyForm.markAllAsTouched();
    } else {

      const updatedProperties: AppProperty[] = [];
      this.properties.forEach(prop => {
        if (prop.value !== this.propertyForm.get([prop.name]).value) {
          prop.value = this.propertyForm.get([prop.name]).value;
          updatedProperties.push(prop);
        }
      });

      this.propertyService.bulkUpdate(updatedProperties)
      .subscribe(result => {

        this.toastr.success('', 'Properties updated successfully.');
        this.setMigrationFeature();
		result.forEach(prop => {
			if (prop.name === Admin_AnnouncementText) {
				this.sharedService.emitAdminBannerChange(prop.value);
			}
		});
      });

    }
  }

}
