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
import { ControlPollutant } from 'src/app/shared/models/control-pollutant';
import { ControlService } from 'src/app/core/services/control.service';
import { SharedService } from 'src/app/core/services/shared.service';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { ControlPollutantModalComponent } from 'src/app/modules/emissions-reporting/components/control-pollutant-modal/control-pollutant-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';
import { faPlus, faEdit } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-control-pollutant-table',
  templateUrl: './control-pollutant-table.component.html',
  styleUrls: ['./control-pollutant-table.component.scss']
})
export class ControlPollutantTableComponent extends BaseSortableTable implements OnInit {
  @Input() tableData: ControlPollutant[];
  @Input() readOnlyMode: boolean;
  @Input() controlId: number;
  @Input() facilitySiteId: number;
  @Input() year: number;
  faPlus = faPlus;
  faEdit = faEdit;
  readonly device = 'CONTROL_DEVICE';

  constructor(private modalService: NgbModal,
              private controlService: ControlService,
              private route: ActivatedRoute,
              private sharedService: SharedService) {
    super();
  }

  ngOnInit() {
  }


  openCreateModal() {
        const modalRef = this.modalService.open(ControlPollutantModalComponent, {size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.controlPollutantFor = this.device;
        modalRef.componentInstance.controlId = this.controlId;
        modalRef.componentInstance.facilitySiteId = this.facilitySiteId;
        modalRef.componentInstance.year = this.year;
        modalRef.componentInstance.controlPollutants = this.tableData;
        modalRef.result.then((result) => {
        this.controlService.retrieve(this.controlId)
        .subscribe(control => {
            if (result !== 'dontUpdate') {
              this.sharedService.updateReportStatusAndEmit(this.route);
            }
            this.tableData = control.pollutants;
          });
        });
  }

  openEditModal(selectedPollutant){
    const modalRef = this.modalService.open(ControlPollutantModalComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.controlPollutantFor = this.device;
    modalRef.componentInstance.facilitySiteId = this.facilitySiteId;
    modalRef.componentInstance.year = this.year;
    modalRef.componentInstance.controlId = this.controlId;
    modalRef.componentInstance.selectedControlPollutant = selectedPollutant;
    modalRef.componentInstance.controlPollutants = this.tableData;
    modalRef.componentInstance.edit = true;
  }

  openDeleteModal(selectedPollutant){
    const modalMessage = `Are you sure you want to remove the association of Control Device ${this.controlId}
      with Control Pollutant ${selectedPollutant.pollutant.pollutantName}?`;
    const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'sm' });
    modalRef.componentInstance.message = modalMessage;
    modalRef.componentInstance.continue.subscribe(() => {
      this.deleteControlPollutant(selectedPollutant);
    });
  }

    // delete Control Pollutant from the database
  deleteControlPollutant(selectedPollutant) {
    this.controlService.deletePollutant(selectedPollutant.id).subscribe(() => {

      this.sharedService.updateReportStatusAndEmit(this.route);

      // update the UI table with the current list of control pollutants
      this.controlService.retrieve(this.controlId)
        .subscribe(controlResponse => {
          this.tableData = controlResponse.pollutants;
        });
    });
  }

}
