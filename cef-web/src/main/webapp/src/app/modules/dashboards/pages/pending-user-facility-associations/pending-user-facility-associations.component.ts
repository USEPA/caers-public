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
import { Component, OnInit } from '@angular/core';
import { UserFacilityAssociationService } from 'src/app/core/services/user-facility-association.service';
import { UserFacilityAssociation } from 'src/app/shared/models/user-facility-association';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { CommentModalComponent } from 'src/app/modules/shared/components/comment-modal/comment-modal.component';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';

@Component({
  selector: 'app-pending-user-facility-associations',
  templateUrl: './pending-user-facility-associations.component.html',
  styleUrls: ['./pending-user-facility-associations.component.scss']
})
export class PendingUserFacilityAssociationsComponent extends BaseSortableTable implements OnInit {
  tableData: UserFacilityAssociation[] = [];

  constructor(private modalService: NgbModal,
              private userFacilityAssociationService: UserFacilityAssociationService,
              private toastr: ToastrService) {
    super();
  }

  ngOnInit(): void {
    this.userFacilityAssociationService.getPendingAssociationDetails()
    .subscribe(result => {
      this.tableData = result;
    });
  }

  selectedAssociations() {
    return this.tableData?.filter(i => i.checked);
  }

  openApproveModal() {
    const modalMessage = `<i>I am authorizing the selected individuals to prepare (Preparer) and/or certify and submit (NEI Certifier) reports for the requested facilities. 
						I certify under penalty of law that I have verified that the individuals are authorized representatives for their requested facilities by following 
						the vetting process established by the State, Local, Tribal authority I represent. I have provided relevant documentation on this vetting process 
						to EPA. The information regarding who is an authorized Preparer and/or NEI Certifier for the requested facility is, to the best of my knowledge and 
						belief, true, accurate, and complete. I have no personal knowledge that the information submitted is other than true, accurate, and complete. I am 
						aware that there are significant penalties for submitting false information, including the possibility of fine and imprisonment for knowing 
						violations. I understand that providing access to unauthorized persons is subject to criminal, civil, administrative, or other lawful action.</i>`;
    const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'lg' });
    modalRef.componentInstance.htmlMessage = modalMessage;
    modalRef.componentInstance.continue.subscribe(() => {
      this.approveAssociations();
    });
  }

  openRejectModal() {
    const modalMessage = `Are you sure you want to reject these users' access to these facilities?`;
    const modalRef = this.modalService.open(CommentModalComponent);
    modalRef.componentInstance.title = 'Reject Requests';
    modalRef.componentInstance.message = modalMessage;
    modalRef.result.then((resp) => {
      this.rejectAssociations(resp);
    }, () => {
      // needed for dismissing without errors
    });
  }

  approveAssociations() {
    const selectedAssociations = this.selectedAssociations();
    this.userFacilityAssociationService.approveAssociations(selectedAssociations)
    .subscribe(() => {
      selectedAssociations.forEach(ufa => {
        this.tableData.splice(this.tableData.indexOf(ufa), 1);
      });
      this.toastr.success('', 'Authorization Requests were successfully approved.');
    });
  }

  rejectAssociations(comments: string) {
    const selectedAssociations = this.selectedAssociations();
    this.userFacilityAssociationService.rejectAssociations(selectedAssociations, comments)
    .subscribe(() => {
      selectedAssociations.forEach(ufa => {
        this.tableData.splice(this.tableData.indexOf(ufa), 1);
      });
      this.toastr.success('', 'Authorization Requests were successfully rejected.');
    });
  }

}
