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
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { ReleasePoint } from 'src/app/shared/models/release-point';
import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BaseReportUrl } from 'src/app/shared/enums/base-report-url';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { ReleasePointService } from 'src/app/core/services/release-point.service';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { ReportStatus } from 'src/app/shared/enums/report-status';
import { SharedService } from 'src/app/core/services/shared.service';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import { UtilityService } from 'src/app/core/services/utility.service';

@Component({
  selector: 'app-release-point-table',
  templateUrl: './release-point-table.component.html',
  styleUrls: ['./release-point-table.component.scss']
})
export class ReleasePointTableComponent extends BaseSortableTable implements OnInit {
  @Input() tableData: ReleasePoint[];
  baseUrl: string;
  facilitySiteId: number;
  faPlus = faPlus;

  readOnlyMode = true;

  constructor(private modalService: NgbModal,
              private releasePointService: ReleasePointService,
              private route: ActivatedRoute,
              private userContextService: UserContextService,
              private sharedService: SharedService) {
    super();
  }

  ngOnInit() {
    this.route.paramMap
      .subscribe(map => {
        this.baseUrl = `/facility/${map.get('facilityId')}/report/${map.get('reportId')}/${BaseReportUrl.RELEASE_POINT}`;
    });

    this.route.data
      .subscribe((data: { facilitySite: FacilitySite }) => {
        this.facilitySiteId = (data.facilitySite.id);
        this.userContextService.getUser().subscribe( user => {
          if (UtilityService.isNotReadOnlyMode(user, data.facilitySite.emissionsReport.status)) {
            this.readOnlyMode = false;
          }
        });
    });
  }

  openDeleteModal(releasePointIdentifier: string, releasePointId: number) {
    const modalMessage = `Are you sure you want to delete the Release Point ${releasePointIdentifier} from this facility?
      This will also remove any Emission Process apportionments associated with this Release Point.`;
    const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'sm' });
    modalRef.componentInstance.message = modalMessage;
    modalRef.componentInstance.continue.subscribe(() => {
      this.deleteReleasePoint(releasePointId);
    });
  }

  // delete an release point from the database
  deleteReleasePoint(releasePointId: number) {
    this.releasePointService.delete(releasePointId).subscribe(() => {

      this.sharedService.updateReportStatusAndEmit(this.route);

      // update the UI table with the current list of release points
      this.releasePointService.retrieveForFacility(this.facilitySiteId)
        .subscribe(releasePointResponse => {
          this.tableData = releasePointResponse;
      });
    }, error => {
      if (error.error && error.status === 422) {
        const modalRef = this.modalService.open(ConfirmationDialogComponent);
        modalRef.componentInstance.message = error.error.message;
        modalRef.componentInstance.singleButton = true;
      }
    });
  }

}
