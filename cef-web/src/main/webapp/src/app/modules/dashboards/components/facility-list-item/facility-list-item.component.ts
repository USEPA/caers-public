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
import { Component, OnInit, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { MasterFacilityRecord } from 'src/app/shared/models/master-facility-record';
import { UserFacilityAssociationService } from "src/app/core/services/user-facility-association.service";
import { SharedService } from "src/app/core/services/shared.service";

@Component({
  selector: 'app-facility-list-item',
  templateUrl: './facility-list-item.component.html',
  styleUrls: ['./facility-list-item.component.scss']
})
export class FacilityListItemComponent implements OnInit {
  @Input() facility: MasterFacilityRecord;

  constructor(private modalService: NgbModal,
              private userFacilityAssociationService: UserFacilityAssociationService,
              private sharedService: SharedService) { }

  ngOnInit() {
  }

 openRemoveAssociationModal(mfr: MasterFacilityRecord) {
    const modalMessage = `Are you sure you want to remove your access to ${mfr.name}?`;
    const modalRef = this.modalService.open(ConfirmationDialogComponent);
    modalRef.componentInstance.message = modalMessage;
    modalRef.componentInstance.continue.subscribe(() => {
        this.removeAssociation(mfr);
    });
  }

  removeAssociation(mfr: MasterFacilityRecord) {
      this.userFacilityAssociationService.selfRemoveAssociation(mfr.id)
        .subscribe(userFacilityAssociations => {
            this.sharedService.emitUserFacilityAssociationChange(userFacilityAssociations);
        });
  }

}
