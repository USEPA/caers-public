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
