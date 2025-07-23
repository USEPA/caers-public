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
import { ControlPath } from 'src/app/shared/models/control-path';
import { ActivatedRoute } from '@angular/router';
import { BaseReportUrl } from 'src/app/shared/enums/base-report-url';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { ControlPathService } from 'src/app/core/services/control-path.service';
import { SharedService } from 'src/app/core/services/shared.service';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import { UtilityService } from 'src/app/core/services/utility.service';
import { ReleasePointService } from 'src/app/core/services/release-point.service';
import { DeleteControlModalComponent } from 'src/app/modules/emissions-reporting/components/delete-control-modal/delete-control-modal.component';

@Component({
  selector: 'app-control-paths-table',
  templateUrl: './control-paths-table.component.html',
  styleUrls: ['./control-paths-table.component.scss']
})
export class ControlPathsTableComponent extends BaseSortableTable implements OnInit {
  @Input() tableData: ControlPath[];
  baseUrl: string;
  readOnlyMode = true;
  faPlus = faPlus;

  constructor(private route: ActivatedRoute,
              private modalService: NgbModal,
              private controlPathService: ControlPathService,
              private userContextService: UserContextService,
			  private releasePointService: ReleasePointService,
              private sharedService: SharedService) {
    super();
               }

  ngOnInit() {
    this.route.paramMap
    .subscribe(map => {
      this.baseUrl = `/facility/${map.get('facilityId')}/report/${map.get('reportId')}/${BaseReportUrl.CONTROL_PATH}`;
    });

    this.route.data
    .subscribe((data: { facilitySite: FacilitySite }) => {
      this.userContextService.getUser().subscribe( user => {
        if (UtilityService.isNotReadOnlyMode(user, data.facilitySite.emissionsReport.status)) {
          this.readOnlyMode = false;
        }
      });
    });
  }

  openDeleteModal(pathId: string, id: number, facilitySiteId: number) {
	this.controlPathService.retrieveParentAssignmentsForControlPathChild(id)
		.subscribe(controlPathAssignments => {
		
		this.releasePointService.retrieveReleasePointsForControlPath(id)
			.subscribe(releasePointAppts => {
		      const modalRef = this.modalService.open(DeleteControlModalComponent, { size: 'sm' });
			  modalRef.componentInstance.controlPathIdentifier = pathId;
			  modalRef.componentInstance.controlPathList = controlPathAssignments;
			  modalRef.componentInstance.releasePointList = releasePointAppts;
		      modalRef.componentInstance.continue.subscribe(() => {
		
		      this.controlPathService.isPathPreviouslyReported(id).subscribe(result => {
		        if (result) {
		          this.openPreviouslyReportedConfirmationModal(id, facilitySiteId);
		        } else {
		          this.deleteControlPath(id, facilitySiteId);
		        }
		      });
		    });
		});
	});
  }

  openPreviouslyReportedConfirmationModal(id: number, facilitySiteId: number) {
    const modalMessage = `This Control Path has been submitted on previous years' facility reports.
                          Are you sure you want to permanently delete this Control Path?
                          This will remove any associations from the path to other sub-facility components in the report.`;
    const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'sm' });
    modalRef.componentInstance.message = modalMessage;
    modalRef.componentInstance.continue.subscribe(() => {
      this.deleteControlPath(id, facilitySiteId);
    });
  }

  deleteControlPath(id: number, facilitySiteId: number) {
    this.controlPathService.delete(id).subscribe(() => {
        // update the UI table with the current list of control devices
      this.controlPathService.retrieveForFacilitySite(facilitySiteId)
      .subscribe(controlPathResponse => {
        this.sharedService.updateReportStatusAndEmit(this.route);
        this.tableData = controlPathResponse;
      });
    });
  }

}
