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
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { ControlPathPollutant } from 'src/app/shared/models/control-path-pollutant';
import { ControlPathService } from 'src/app/core/services/control-path.service';
import { SharedService } from 'src/app/core/services/shared.service';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { ControlPollutantModalComponent } from 'src/app/modules/emissions-reporting/components/control-pollutant-modal/control-pollutant-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';
import { faPlus, faEdit } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-control-path-pollutant-table',
  templateUrl: './control-path-pollutant-table.component.html',
  styleUrls: ['./control-path-pollutant-table.component.scss']
})

export class ControlPathPollutantTableComponent extends BaseSortableTable implements OnInit {
  @Input() tableData: ControlPathPollutant[];
  @Input() readOnlyMode: boolean;
  @Input() controlPathId: number;
  @Input() facilitySiteId: number;
  @Input() year: number;
  faPlus = faPlus;
  faEdit = faEdit;
  readonly path = 'CONTROL_PATH';

  constructor(private modalService: NgbModal,
              private controlPathService: ControlPathService,
              private route: ActivatedRoute,
              private sharedService: SharedService) {
    super();
  }

  ngOnInit() {
  }


  openCreateModal() {
        const modalRef = this.modalService.open(ControlPollutantModalComponent, {size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.controlPollutantFor = this.path;
        modalRef.componentInstance.controlPathId = this.controlPathId;
        modalRef.componentInstance.facilitySiteId = this.facilitySiteId;
        modalRef.componentInstance.year = this.year;
        modalRef.componentInstance.controlPathPollutants = this.tableData;
        modalRef.result.then((result) => {
        this.controlPathService.retrieve(this.controlPathId)
        .subscribe(controlPath => {
            if (result !== 'dontUpdate') {
              this.sharedService.updateReportStatusAndEmit(this.route);
            }
            this.tableData = controlPath.pollutants;
          });
        });
  }

  openEditModal(selectedPollutant){
    const modalRef = this.modalService.open(ControlPollutantModalComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.controlPollutantFor = this.path;
    modalRef.componentInstance.facilitySiteId = this.facilitySiteId;
    modalRef.componentInstance.year = this.year;
    modalRef.componentInstance.controlPathId = this.controlPathId;
    modalRef.componentInstance.selectedControlPathPollutant = selectedPollutant;
    modalRef.componentInstance.controlPathPollutants = this.tableData;
    modalRef.componentInstance.edit = true;
  }

  openDeleteModal(selectedPollutant){
    const modalMessage = `Are you sure you want to remove the association of Control Path ${this.controlPathId}
      with Control Path Pollutant ${selectedPollutant.pollutant.pollutantName}?`;
    const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'sm' });
    modalRef.componentInstance.message = modalMessage;
    modalRef.componentInstance.continue.subscribe(() => {
      this.deleteControlPathPollutant(selectedPollutant);
    });
  }

    // delete Control Pollutant from the database
  deleteControlPathPollutant(selectedPollutant) {
    this.controlPathService.deletePollutant(selectedPollutant.id).subscribe(() => {

      this.sharedService.updateReportStatusAndEmit(this.route);

      // update the UI table with the current list of control pollutants
      this.controlPathService.retrieve(this.controlPathId)
        .subscribe(controlPathResponse => {
          this.tableData = controlPathResponse.pollutants;
        });
    });
  }

}
