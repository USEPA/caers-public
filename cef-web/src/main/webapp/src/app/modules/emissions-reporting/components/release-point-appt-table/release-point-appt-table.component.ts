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
