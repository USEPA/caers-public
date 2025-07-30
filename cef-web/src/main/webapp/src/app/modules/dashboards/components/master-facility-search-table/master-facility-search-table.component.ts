/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { MasterFacilityRecord } from 'src/app/shared/models/master-facility-record';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { FormControl } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserFacilityAssociationService } from 'src/app/core/services/user-facility-association.service';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { ToastrService } from 'ngx-toastr';
import { UserFacilityAssociation } from 'src/app/shared/models/user-facility-association';

@Component({
  selector: 'app-master-facility-search-table',
  templateUrl: './master-facility-search-table.component.html',
  styleUrls: ['./master-facility-search-table.component.scss']
})
export class MasterFacilitySearchTableComponent extends BaseSortableTable implements OnInit {
  @Input() tableData: MasterFacilityRecord[];
  @Output() accessRequested = new EventEmitter<UserFacilityAssociation>();

  filteredItems: MasterFacilityRecord[] = [];
  filter = new FormControl('');
  statusFilter = new FormControl('');

  selectedFacility: MasterFacilityRecord;

  matchFunction: (item: any, searchTerm: any) => boolean = this.matches;

  constructor(private modalService: NgbModal,
              private userFacilityAssociationService: UserFacilityAssociationService,
              private toastr: ToastrService) {
    super();

    this.filter.valueChanges.subscribe((text) => {
      this.controller.searchTerm = {text, status: this.statusFilter.value};
    });

    this.statusFilter.valueChanges.subscribe((text) => {
      this.controller.searchTerm = {text: this.filter.value, status: text};
    });
  }

  ngOnInit(): void {
    this.controller.paginate = true;
  }

  selectFacility(facility: MasterFacilityRecord) {

    if (!facility.associationStatus) {
      this.selectedFacility = facility;
    }
  }

  onClearFilterClick() {
      this.filter.setValue('');
   }

  matches(item: MasterFacilityRecord, searchTerm: {text: string, status: string}): boolean {
    if ((searchTerm.status === 'UNASSOCIATED' && item.associationStatus)
        || (searchTerm.status === 'APPROVED' && item.associationStatus !== 'ACTIVE')
        || (searchTerm.status === 'PENDING' && item.associationStatus !== 'PENDING')) {

      return false;
    }

    const term = searchTerm.text ? searchTerm.text.toLowerCase() : '';
    return item.name?.toLowerCase().includes(term)
        || item.agencyFacilityIdentifier?.toLowerCase().includes(term)
        || `${item.streetAddress}, ${item.city}, ${item?.stateCode?.uspsCode} ${item.postalCode}`.toLowerCase().includes(term);
  }

  openRequestAccessModal() {
    const modalMessage = `Are you sure you want to request access to ${this.selectedFacility.name}?`;
    const modalRef = this.modalService.open(ConfirmationDialogComponent);
    modalRef.componentInstance.message = modalMessage;
    modalRef.componentInstance.continue.subscribe(() => {
        this.requestAccess(this.selectedFacility);
    });
  }

  requestAccess(facility: MasterFacilityRecord) {
    this.userFacilityAssociationService.createAssociationRequest(facility)
    .subscribe(result => {
      this.toastr.success('', `Access requested for ${facility.name}`);
      this.accessRequested.emit(result);
      facility.associationStatus = 'PENDING';
      this.selectedFacility = null;
    });

  }

}
