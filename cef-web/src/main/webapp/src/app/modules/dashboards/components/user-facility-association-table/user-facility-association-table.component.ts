/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { UserFacilityAssociation } from 'src/app/shared/models/user-facility-association';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { UserFacilityAssociationService } from 'src/app/core/services/user-facility-association.service';
import { MasterFacilityRecord } from 'src/app/shared/models/master-facility-record';

@Component({
  selector: 'app-user-facility-association-table',
  templateUrl: './user-facility-association-table.component.html',
  styleUrls: ['./user-facility-association-table.component.scss']
})
export class UserFacilityAssociationTableComponent extends BaseSortableTable implements OnInit {
  @Input() tableData: UserFacilityAssociation[];
  @Input() facility: MasterFacilityRecord;

  constructor(private modalService: NgbModal,
              private userFacilityAssociationService: UserFacilityAssociationService) {
    super();
  }

  ngOnInit(): void {
  }

  openDeleteModal(association: UserFacilityAssociation) {
    const modalMessage = `Are you sure you want to remove ${association.fullName}'s access to ${this.facility.name}?`;
    const modalRef = this.modalService.open(ConfirmationDialogComponent);
    modalRef.componentInstance.message = modalMessage;
    modalRef.componentInstance.continue.subscribe(() => {
        this.deleteAssociation(association);
    });
  }

  deleteAssociation(association: UserFacilityAssociation) {
    this.userFacilityAssociationService.delete(association.id)
    .subscribe(() => {
      this.tableData.splice(this.tableData.indexOf(association), 1);
    });

  }

}
