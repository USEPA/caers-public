/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, Input } from '@angular/core';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { faPlus, faEdit } from '@fortawesome/free-solid-svg-icons';
import { MasterFacilityNaicsCode } from 'src/app/shared/models/master-facility-naics-code';
import { MasterFacilityRecordService } from 'src/app/core/services/master-facility-record.service';
import { MasterFacilityRecord } from 'src/app/shared/models/master-facility-record';
import { MasterFacilityNaicsModalComponent } from 'src/app/modules/dashboards/components/master-facility-naics-modal/master-facility-naics-modal.component';
import { User } from 'src/app/shared/models/user';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { ConfigPropertyService } from 'src/app/core/services/config-property.service';

const CurrentYear = new Date().getFullYear() - 1;

@Component({
  selector: 'app-master-facility-naics-table',
  templateUrl: './master-facility-naics-table.component.html',
  styleUrls: ['./master-facility-naics-table.component.scss']
})
export class MasterFacilityNaicsTableComponent extends BaseSortableTable implements OnInit {
  @Input() masterFacilityId: number;
  @Input() facility: MasterFacilityRecord;
  @Input() tableData: MasterFacilityNaicsCode[];
  baseUrl: string;
  faPlus = faPlus;
  faEdit = faEdit;
  naicsEntryEnabled: boolean;
  user: User;

  constructor(
    private modalService: NgbModal,
    private userContextService: UserContextService,
    private propertyService: ConfigPropertyService,
    private mfrService: MasterFacilityRecordService) {
    super();
  }

  ngOnInit() {
      this.userContextService.getUser().subscribe( user => {
            this.user = user;
        });

      this.propertyService.retrieveFacilityNaicsEntryEnabled(this.facility.programSystemCode.code)
        .subscribe(result => {
          this.naicsEntryEnabled = result;
        });

  }

  sortTable() {
    this.tableData.sort((a, b) => (a.description > b.description ? 1 : -1));
  }

  // delete facility NAICS from the database
  deleteMfrFacilityNaics(mfrNaicsId: number, mfrId: number) {
    this.mfrService.deleteMasterFacilityNaics(mfrNaicsId).subscribe(() => {

      // update the UI table with the current list of facility NAICS
      this.mfrService.getRecord(mfrId)
        .subscribe(masterFacilityResponse => {
          this.facility.masterFacilityNAICS = masterFacilityResponse.masterFacilityNAICS;
          this.sortTable();
        });
    });
  }

  openDeleteModal(naicsCode: string, mfrNaicsId: number, mfrId: number) {
        const modalMessage = `Are you sure you want to delete NAICS Code ${naicsCode} from this master facility record?`;
        const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'sm' });
        modalRef.componentInstance.message = modalMessage;
        modalRef.componentInstance.continue.subscribe(() => {
            this.deleteMfrFacilityNaics(mfrNaicsId, mfrId);
        });
    }

  openEditModal(selectedCode){
    const modalRef = this.modalService.open(MasterFacilityNaicsModalComponent, { size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.masterFacilityId = this.facility.id;
    modalRef.componentInstance.facilityNaics = this.tableData;
    modalRef.componentInstance.year = CurrentYear;
    modalRef.componentInstance.selectedNaicsCode = selectedCode;
    modalRef.componentInstance.selectedNaicsCodeType = selectedCode.naicsCodeType;
    modalRef.componentInstance.edit = true;

    modalRef.result.then(() => {
      this.mfrService.getRecord(this.facility.id)
        .subscribe(facilityResponse => {

          this.facility.masterFacilityNAICS = facilityResponse.masterFacilityNAICS;
          this.sortTable();
        });

    }, () => {
    //   needed for dismissing without errors
    });
  }

  openFacilityNaicsModal() {
    const modalRef = this.modalService.open(MasterFacilityNaicsModalComponent, { size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.masterFacilityId = this.facility.id;
    modalRef.componentInstance.facilityNaics = this.tableData;
    modalRef.componentInstance.year = CurrentYear;

    modalRef.result.then(() => {
      this.mfrService.getRecord(this.facility.id)
        .subscribe(facilityResponse => {

          this.facility.masterFacilityNAICS = facilityResponse.masterFacilityNAICS;
          this.sortTable();
        });

    }, () => {
    //   needed for dismissing without errors
    });
  }

}
