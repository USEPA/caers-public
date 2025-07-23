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
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { FacilitySiteContactService } from 'src/app/core/services/facility-site-contact.service';
import { Component, OnInit, ViewChild, Input, HostListener } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SharedService } from 'src/app/core/services/shared.service';
import { FacilityNaicsCode } from 'src/app/shared/models/facility-naics-code';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { ToastrService } from 'ngx-toastr';
import { BaseReportUrl } from 'src/app/shared/enums/base-report-url';
import { EditFacilitySiteInfoPanelComponent } from 'src/app/modules/emissions-reporting/components/edit-facility-site-info-panel/edit-facility-site-info-panel.component';
import { FacilitySiteService } from 'src/app/core/services/facility-site.service';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { UtilityService } from 'src/app/core/services/utility.service';
import { EntityType } from 'src/app/shared/enums/entity-type';
import { FacilityPermit } from 'src/app/shared/models/facility-permit';

@Component({
  selector: 'app-facility-information',
  templateUrl: './facility-information.component.html',
  styleUrls: ['./facility-information.component.scss']
})
export class FacilityInformationComponent implements OnInit {
  @Input() facilitySite: FacilitySite;
  naicsCodes: FacilityNaicsCode[];
  facilityPermits: FacilityPermit[];
  readOnlyMode = true;
  editInfo = false;

  createUrl: string;

  entityType = EntityType;

  @ViewChild(EditFacilitySiteInfoPanelComponent)
  private facilitySiteComponent: EditFacilitySiteInfoPanelComponent;

  constructor(
    private modalService: NgbModal,
    private contactService: FacilitySiteContactService,
    private facilityService: FacilitySiteService,
    private userContextService: UserContextService,
    private sharedService: SharedService,
    private utilityService: UtilityService,
    private toastr: ToastrService,
    private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.data
    .subscribe((data: { facilitySite: FacilitySite }) => {

      this.facilitySite = data.facilitySite;
      this.sharedService.emitChange(data.facilitySite);

      this.contactService.retrieveForFacility(this.facilitySite.id)
      .subscribe(contacts => {
        this.facilitySite.contacts = contacts;
      });

      this.facilityService.retrievePermits(this.facilitySite.id)
          .subscribe(permits => {
            this.facilityPermits = permits;
          });

      this.facilityService.retrieveByIdAndReportYear(this.facilitySite.id, data.facilitySite.emissionsReport.year)
      .subscribe(fs => {
        this.facilitySite.facilityNAICS = fs.facilityNAICS;
      });

      this.userContextService.getUser().subscribe( user => {
        if (UtilityService.isNotReadOnlyMode(user, data.facilitySite.emissionsReport.status)) {
          this.readOnlyMode = false;
        }
      });
    });

    this.route.paramMap
    .subscribe(params => {
      this.createUrl = `/facility/${params.get('facilityId')}/report/${params.get('reportId')}/${BaseReportUrl.FACILITY_CONTACT}`;
    });
  }

  setEditInfo(value: boolean) {
    this.editInfo = value;
  }

  updateFacilitySite() {
    if (!this.facilitySiteComponent.facilitySiteForm.valid) {
      this.facilitySiteComponent.facilitySiteForm.markAllAsTouched();
    } else {
      const updatedFacilitySite = new FacilitySite();
      Object.assign(updatedFacilitySite, this.facilitySiteComponent.facilitySiteForm.value);
      updatedFacilitySite.id = this.facilitySite.id;
      updatedFacilitySite.emissionsReport = this.facilitySite.emissionsReport;
      updatedFacilitySite.programSystemCode = this.facilitySite.programSystemCode;
      updatedFacilitySite.facilitySourceTypeCode = this.facilitySite.facilitySourceTypeCode;

      this.facilityService.update(updatedFacilitySite)
      .subscribe(result => {

        Object.assign(this.facilitySite, result);
        this.sharedService.updateReportStatusAndEmit(this.route);

        this.setEditInfo(false);
      });
    }
  }


  openDeleteModal(contactFirstName: string, contactLastName: string, contactId: number, facilitySiteId: number) {
    if (this.facilitySite.contacts.length > 1) {
      const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'sm' });
      const modalMessage = `Are you sure you want to delete ${contactFirstName} ${contactLastName} from this facility's contact information?`;
      modalRef.componentInstance.message = modalMessage;
      modalRef.componentInstance.continue.subscribe(() => {
        this.deleteContact(contactId, facilitySiteId);
      });
    } else {
      this.toastr.error('You must have at least one contact for Facility Contact Information.', 'Cannot delete facility contact.');
    }
  }

  // delete an emission unit from the database
  deleteContact(contactId: number, facilitySiteId: number) {
    this.contactService.delete(contactId).subscribe(() => {

      this.sharedService.updateReportStatusAndEmit(this.route);

      // update the UI table with the current list of emission units
      this.contactService.retrieveForFacility(facilitySiteId)
        .subscribe(contactServiceResponse => {
          this.facilitySite.contacts = contactServiceResponse;
        });
    });
  }

  canDeactivate(): Promise<boolean> | boolean {
    if (!this.editInfo || (this.facilitySiteComponent !== undefined && !this.facilitySiteComponent.facilitySiteForm.dirty)) {
        return true;
    }
    return this.utilityService.canDeactivateModal();
  }

  @HostListener('window:beforeunload', ['$event'])
  beforeunloadHandler(event) {
    if (this.editInfo && this.facilitySiteComponent.facilitySiteForm.dirty) {
      event.preventDefault();
      event.returnValue = '';
    }
    return true;
  }

}
