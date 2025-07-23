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
