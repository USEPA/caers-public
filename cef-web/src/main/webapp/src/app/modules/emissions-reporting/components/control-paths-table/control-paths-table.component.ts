/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
