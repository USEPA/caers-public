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
import { ControlPathAssignmentModalComponent } from 'src/app/modules/emissions-reporting/components/control-path-assignment-modal/control-path-assignment-modal.component';
import { ControlPath } from 'src/app/shared/models/control-path';
import { ControlAssignment } from 'src/app/shared/models/control-assignment';
import { ControlPathService } from 'src/app/core/services/control-path.service';
import { SharedService } from 'src/app/core/services/shared.service';
import { ActivatedRoute } from '@angular/router';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { BaseReportUrl } from 'src/app/shared/enums/base-report-url';
import { faPlus, faEdit } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-control-path-assignment-table',
  templateUrl: './control-path-assignment-table.component.html',
  styleUrls: ['./control-path-assignment-table.component.scss']
})
export class ControlPathAssignmentTableComponent extends BaseSortableTable implements OnInit {
  @Input() readOnlyMode: boolean;
  @Input() tableData: ControlAssignment[];
  @Input() controlPath: ControlPath;
  @Input() facilitySiteId: number;
  baseUrlForControlDevice: string;
  baseUrlForControlPath: string;
  faPlus = faPlus;
  faEdit = faEdit;

  constructor(private modalService: NgbModal,
              private controlPathService: ControlPathService,
              private sharedService: SharedService,
              private route: ActivatedRoute) {
              super();
  }

  ngOnInit() {
    this.route.paramMap
    .subscribe(map => {
      this.baseUrlForControlDevice = `/facility/${map.get('facilityId')}/report/${map.get('reportId')}/${BaseReportUrl.CONTROL_DEVICE}`;
      this.baseUrlForControlPath = `/facility/${map.get('facilityId')}/report/${map.get('reportId')}/${BaseReportUrl.CONTROL_PATH}`;
    });
  }

  openCreateModal() {
    const modalRef = this.modalService.open(ControlPathAssignmentModalComponent, { backdrop: 'static', scrollable: true, size: 'lg' });
    modalRef.componentInstance.controlPath = this.controlPath;
    modalRef.componentInstance.facilitySiteId = this.facilitySiteId;
    modalRef.result.then((result) => {
      this.controlPathService.retrieveAssignmentsForControlPath(this.controlPath.id)
      .subscribe(pathAssignmentsResponse => {
        this.controlPathService.retrieve(this.controlPath.id)
        .subscribe(controlPath => {
          if (result !== 'dontUpdate') {
            this.sharedService.updateReportStatusAndEmit(this.route);
          }
          this.tableData = pathAssignmentsResponse;
          this.controlPath = controlPath;
          this.sharedService.updateReportStatusAndEmit(this.route);
        });
      });
    });
  }

  openDeleteModal(controlIdentifer: string, controlPathId: number, pathIdentifer: string ) {
    let modalMessage = '';
    if (controlIdentifer) {
      modalMessage = `Are you sure you want to remove the control path: ${controlIdentifer}`;
    }
    if (pathIdentifer) {
      modalMessage = `Are you sure you want to remove the control path: ${pathIdentifer}`;
    }
    const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'sm' });
    modalRef.componentInstance.message = modalMessage;
    modalRef.componentInstance.continue.subscribe(() => {
      this.deleteControlPath(controlPathId);
    });
  }

  deleteControlPath(controlPathId: number) {
    this.controlPathService.deleteAssignmentForControlPath(controlPathId).subscribe(() => {
      this.sharedService.updateReportStatusAndEmit(this.route);
      this.controlPathService.retrieveAssignmentsForControlPath(this.controlPath.id)
        .subscribe(pathAssignmentsResponse => {
          this.controlPathService.retrieve(this.controlPath.id)
          .subscribe(controlPath => {
            this.tableData = pathAssignmentsResponse;
            this.controlPath = controlPath;
            this.sharedService.updateReportStatusAndEmit(this.route);
          });
        });
    });
  }

  openEditModal(selectedControlPathAssignment) {
      const modalRef = this.modalService.open(ControlPathAssignmentModalComponent, { backdrop: 'static', scrollable: true, size: 'lg' });
      modalRef.componentInstance.controlPath = this.controlPath;
      modalRef.componentInstance.facilitySiteId = this.facilitySiteId;
      modalRef.componentInstance.edit = true;
      modalRef.componentInstance.selectedControlPathAssignment = selectedControlPathAssignment;
      modalRef.componentInstance.controlPathAssignments = this.tableData;

      modalRef.result.then((result) => {
        this.controlPathService.retrieveAssignmentsForControlPath(this.controlPath.id)
          .subscribe(pathAssignmentsResponse => {
            this.controlPathService.retrieve(this.controlPath.id)
            .subscribe(controlPath => {
              if (result !== 'dontUpdate') {
                this.sharedService.updateReportStatusAndEmit(this.route);
              }
              this.tableData = pathAssignmentsResponse;
              this.controlPath = controlPath;
              this.sharedService.updateReportStatusAndEmit(this.route);
            });
          });
      });
  }
}
