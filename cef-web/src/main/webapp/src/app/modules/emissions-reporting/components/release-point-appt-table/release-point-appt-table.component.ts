/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, Input } from '@angular/core';
import { ReleasePointApportionment } from 'src/app/shared/models/release-point-apportionment';
import { ActivatedRoute } from '@angular/router';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { EmissionsProcessService } from 'src/app/core/services/emissions-process.service';
import { ReleasePointService } from 'src/app/core/services/release-point.service';
import { ReleasePointApportionmentModalComponent } from 'src/app/modules/emissions-reporting/components/release-point-apportionment-modal/release-point-apportionment-modal.component';
import { Process } from 'src/app/shared/models/process';
import { SharedService } from 'src/app/core/services/shared.service';
import { ControlPathService } from 'src/app/core/services/control-path.service';
import { ControlPath } from 'src/app/shared/models/control-path';
import { faPlus, faEdit } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-release-point-appt-table',
  templateUrl: './release-point-appt-table.component.html',
  styleUrls: ['./release-point-appt-table.component.scss']
})
export class ReleasePointApptTableComponent extends BaseSortableTable implements OnInit {
  @Input() tableData: ReleasePointApportionment[];
  @Input() process: Process;
  @Input() readOnlyMode: boolean;
  @Input() facilitySiteId: number;
  controlPaths: ControlPath[];
  baseUrl: string;
  totalApptPct = 0;
  faPlus = faPlus;
  faEdit = faEdit;

  constructor(
    private modalService: NgbModal,
    private processService: EmissionsProcessService,
    private releasePointService: ReleasePointService,
    private route: ActivatedRoute,
    private sharedService: SharedService,
    private controlPathService: ControlPathService) {
    super();
  }

  ngOnInit() {
    this.route.paramMap
      .subscribe(map => {
        this.baseUrl = `/facility/${map.get('facilityId')}/report/${map.get('reportId')}`;
      });
    this.tableData.sort((a, b) => (a.releasePointIdentifier > b.releasePointIdentifier ? 1 : -1));

    this.calcTotalPercent();
    this.controlPathService.retrieveForFacilitySite(this.facilitySiteId)
    .subscribe(controlPaths => {
      this.controlPaths = controlPaths;
    });
  }

  calcTotalPercent() {
    // calculate total percent apportionment of emissions
    let sum = 0;
    for (const rpa of this.tableData) {
      sum += rpa.percent;
    }
    this.totalApptPct = sum;

    // sort table by release point identifier
    this.tableData.sort((a, b) => (a.releasePointIdentifier > b.releasePointIdentifier ? 1 : -1));
  }

  // delete release point apportionment from the database
  deleteReleasePointApportionment(releasePtApptId: number, emissionProcessId: number) {
    this.releasePointService.deleteAppt(releasePtApptId).subscribe(() => {

      this.sharedService.updateReportStatusAndEmit(this.route);

      // update the UI table with the current list of release point apportionments
      this.processService.retrieve(emissionProcessId)
        .subscribe(processResponse => {
          this.tableData = processResponse.releasePointAppts;
          this.calcTotalPercent();
        });
      this.controlPathService.retrieveForEmissionsProcess(this.process.id)
      .subscribe(controlPaths => {
          controlPaths = controlPaths.sort((a, b) => (a.pathId > b.pathId) ? 1 : -1);
          this.sharedService.emitControlsChange(controlPaths);
      });
    });
  }

  openDeleteModal(releasePtApptIdentifier: string, releasePtApptId: number) {
    const modalMessage = `Are you sure you want to remove the association of Release Point ${releasePtApptIdentifier}
      with Emission Process ${this.process.emissionsProcessIdentifier}? The apportionment of emissions will need to be
      updated to total 100% for the remaining release points afterwards.`;
    const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'sm' });
    modalRef.componentInstance.message = modalMessage;
    modalRef.componentInstance.continue.subscribe(() => {
      this.deleteReleasePointApportionment(releasePtApptId, this.process.id);
    });
  }

  openReleasePointAptModal() {
    const modalRef = this.modalService.open(ReleasePointApportionmentModalComponent, { backdrop: 'static', scrollable: true });
    modalRef.componentInstance.process = this.process;
    modalRef.componentInstance.releasePointApportionments = this.tableData;
    modalRef.componentInstance.facilitySiteId = this.facilitySiteId;
    modalRef.componentInstance.controlPaths = this.controlPaths;

    modalRef.result.then((result) => {
      this.processService.retrieve(this.process.id)
        .subscribe(processResponse => {
          if (result !== 'dontUpdate') {
            this.sharedService.updateReportStatusAndEmit(this.route);
            this.controlPathService.retrieveForEmissionsProcess(this.process.id)
            .subscribe(controlPaths => {
              controlPaths = controlPaths.sort((a, b) => (a.pathId > b.pathId) ? 1 : -1);
              this.sharedService.emitControlsChange(controlPaths);
            });
          }
          this.tableData = processResponse.releasePointAppts;
          this.calcTotalPercent();
        });
    });
  }

  openEditModal(selectedApportionment) {
    const modalRef = this.modalService.open(ReleasePointApportionmentModalComponent, { backdrop: 'static', scrollable: true });
    modalRef.componentInstance.process = this.process;
    modalRef.componentInstance.facilitySiteId = this.facilitySiteId;
    modalRef.componentInstance.releasePointApportionments = this.tableData;
    modalRef.componentInstance.edit = true;
    modalRef.componentInstance.selectedReleasePoint = selectedApportionment;
    modalRef.componentInstance.controlPaths = this.controlPaths;

    modalRef.result.then((result) => {
      this.processService.retrieve(this.process.id)
        .subscribe(processResponse => {
          if (result !== 'dontUpdate') {
            this.sharedService.updateReportStatusAndEmit(this.route);
            this.tableData = processResponse.releasePointAppts;
            this.calcTotalPercent();
            this.controlPathService.retrieveForEmissionsProcess(this.process.id)
            .subscribe(controlPaths => {
              controlPaths = controlPaths.sort((a, b) => (a.pathId > b.pathId) ? 1 : -1);
              this.sharedService.emitControlsChange(controlPaths);
            });
          }
        });
    });
  }
}
