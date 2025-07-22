/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
