/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
