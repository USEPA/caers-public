/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
