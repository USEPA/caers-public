/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
