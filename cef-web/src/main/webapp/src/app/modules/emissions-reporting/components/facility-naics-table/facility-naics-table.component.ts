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
import { ActivatedRoute } from '@angular/router';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FacilityNaicsCode } from 'src/app/shared/models/facility-naics-code';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { FacilitySiteService } from 'src/app/core/services/facility-site.service';
import { SharedService } from 'src/app/core/services/shared.service';
import { FacilityNaicsModalComponent } from '../facility-naics-modal/facility-naics-modal.component';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { faPlus, faEdit } from '@fortawesome/free-solid-svg-icons';
import { NaicsCodeType } from 'src/app/shared/enums/naics-code-type';
import { ConfigPropertyService } from 'src/app/core/services/config-property.service';

@Component({
  selector: 'app-facility-naics-table',
  templateUrl: './facility-naics-table.component.html',
  styleUrls: ['./facility-naics-table.component.scss']
})
export class FacilityNaicsTableComponent extends BaseSortableTable implements OnInit {
  @Input() tableData: FacilityNaicsCode[];
  @Input() readOnlyMode: boolean;
  facilitySite: FacilitySite;
  baseUrl: string;
  faPlus = faPlus;
  faEdit = faEdit;
  naicsCodeType = NaicsCodeType;
  naicsEntryEnabled: boolean;

  constructor(
    private modalService: NgbModal,
    private facilityService: FacilitySiteService,
    private sharedService: SharedService,
    private propertyService: ConfigPropertyService,
    private route: ActivatedRoute) {
    super();
  }

  ngOnInit() {
    this.route.paramMap
      .subscribe(map => {
        this.baseUrl = `/facility/${map.get('facilityId')}/report/${map.get('reportId')}`;
    });

    this.route.data
      .subscribe((data: { facilitySite: FacilitySite }) => {
        this.facilitySite = data.facilitySite;
      });

    this.propertyService.retrieveFacilityNaicsEntryEnabled(this.facilitySite.programSystemCode.code)
      .subscribe(result => {
        this.naicsEntryEnabled = result;
      });

    this.sortTable();
  }

  sortTable() {
    this.tableData.sort((a, b) => (a.description > b.description ? 1 : -1));
  }

  // delete facility NAICS from the database
  deleteFacilityNaics(facilityNaicsId: number, facilitySiteId: number) {
    this.facilityService.deleteFacilityNaics(facilityNaicsId).subscribe(() => {

      this.sharedService.updateReportStatusAndEmit(this.route);

      // update the UI table with the current list of release point apportionments
      this.facilityService.retrieveByIdAndReportYear(facilitySiteId, this.facilitySite.emissionsReport.year)
        .subscribe(facilityResponse => {
          this.tableData = facilityResponse.facilityNAICS;
          this.sortTable();
        });
    });
  }

  openDeleteModal(naicsCode: string, facilityNaicsId: number, facilitySiteId: number) {
        const modalMessage = `Are you sure you want to delete NAICS Code ${naicsCode} from this facility?`;
        const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'sm' });
        modalRef.componentInstance.message = modalMessage;
        modalRef.componentInstance.continue.subscribe(() => {
            this.deleteFacilityNaics(facilityNaicsId, facilitySiteId);
        });
    }

  openEditModal(selectedCode){
    const modalRef = this.modalService.open(FacilityNaicsModalComponent, { size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.facilitySiteId = this.facilitySite.id;
    modalRef.componentInstance.facilityNaics = this.tableData;
    modalRef.componentInstance.year = this.facilitySite.emissionsReport.year;
    modalRef.componentInstance.selectedNaicsCode = selectedCode;
    modalRef.componentInstance.selectedNaicsCodeType = selectedCode.naicsCodeType;
    modalRef.componentInstance.edit = true;

	modalRef.result.then(() => {
      this.facilityService.retrieveByIdAndReportYear(this.facilitySite.id, this.facilitySite.emissionsReport.year)
        .subscribe(facilityResponse => {

          this.sharedService.updateReportStatusAndEmit(this.route);

          this.tableData = facilityResponse.facilityNAICS;
          this.sortTable();
        });

    }, () => {
    //   needed for dismissing without errors
    });
  }

  openFacilityNaicsModal() {
    const modalRef = this.modalService.open(FacilityNaicsModalComponent, { size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.facilitySiteId = this.facilitySite.id;
    modalRef.componentInstance.facilityNaics = this.tableData;
    modalRef.componentInstance.year = this.facilitySite.emissionsReport.year;

    modalRef.result.then(() => {
      this.facilityService.retrieveByIdAndReportYear(this.facilitySite.id, this.facilitySite.emissionsReport.year)
        .subscribe(facilityResponse => {

          this.sharedService.updateReportStatusAndEmit(this.route);

          this.tableData = facilityResponse.facilityNAICS;
          this.sortTable();
        });

    }, () => {
    //   needed for dismissing without errors
    });
  }

}
