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
import { Control } from 'src/app/shared/models/control';
import { ActivatedRoute } from '@angular/router';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { BaseReportUrl } from 'src/app/shared/enums/base-report-url';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { ControlService } from 'src/app/core/services/control.service';
import { SharedService } from 'src/app/core/services/shared.service';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import { UtilityService } from 'src/app/core/services/utility.service';
import { DeleteControlModalComponent } from 'src/app/modules/emissions-reporting/components/delete-control-modal/delete-control-modal.component';

@Component({
  selector: 'app-inventory-control-table',
  templateUrl: './inventory-control-table.component.html',
  styleUrls: ['./inventory-control-table.component.scss']
})
export class InventoryControlTableComponent extends BaseSortableTable implements OnInit {
  @Input() tableData: Control[];
  baseUrl: string;
  faPlus = faPlus;

  readOnlyMode = true;

  constructor(private modalService: NgbModal,
              private route: ActivatedRoute,
              private controlService: ControlService,
              private userContextService: UserContextService,
              private sharedService: SharedService) {
    super();
  }

  ngOnInit() {
    this.route.paramMap
      .subscribe(map => {
        this.baseUrl = `/facility/${map.get('facilityId')}/report/${map.get('reportId')}/${BaseReportUrl.CONTROL_DEVICE}`;
    });

    this.route.data
      .subscribe((data: { facilitySite: FacilitySite }) => {
        this.userContextService.getUser().subscribe( user => {
          if (UtilityService.isNotReadOnlyMode(user, data.facilitySite.emissionsReport.status)) {
            this.readOnlyMode = false;
          }
        });
      });
  }

  openDeleteModal(controlName: string, controlId: number, facilitySiteId: number) {
	this.controlService.retrieveAssignmentsForControl(controlId)
		.subscribe(controlPathAssignments => {
	      const modalRef = this.modalService.open(DeleteControlModalComponent, { size: 'sm' });
		  modalRef.componentInstance.controlIdentifier = controlName;
		  modalRef.componentInstance.controlPathList = controlPathAssignments;
	      modalRef.componentInstance.continue.subscribe(() => {
	      this.deleteControl(controlId, facilitySiteId);
	    });
	});
  }

  // delete a control device from the database
  deleteControl(controlId: number, facilitySiteId: number) {
    this.controlService.delete(controlId).subscribe(() => {

      // update the UI table with the current list of control devices
      this.controlService.retrieveForFacilitySite(facilitySiteId)
        .subscribe(controlResponse => {
          this.sharedService.updateReportStatusAndEmit(this.route);
          this.tableData = controlResponse;
        });

    }, error => {
      if (error.error && error.status === 422) {
        const modalRef = this.modalService.open(ConfirmationDialogComponent);
        modalRef.componentInstance.message = error.error.message;
        modalRef.componentInstance.singleButton = true;
      }
    });
  }

}
