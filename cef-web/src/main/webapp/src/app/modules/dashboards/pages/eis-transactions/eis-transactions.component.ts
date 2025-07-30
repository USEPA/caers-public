/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit } from '@angular/core';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { EisTranactionHistory } from 'src/app/shared/models/eis-tranaction-history';
import { EisDataService } from 'src/app/core/services/eis-data.service';
import { ToastrService } from 'ngx-toastr';
import { EisSubmissionStatus } from 'src/app/shared/models/eis-data';
import { FileDownloadService } from 'src/app/core/services/file-download.service';
import { EisTransactionAttachment } from 'src/app/shared/models/eis-transaction-attachment';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { EisTransactionAttachmentModalComponent } from 'src/app/modules/shared/components/eis-transaction-attachment-modal/eis-transaction-attachment-modal.component';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { FormControl } from '@angular/forms';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-eis-transactions',
  templateUrl: './eis-transactions.component.html',
  styleUrls: ['./eis-transactions.component.scss']
})
export class EisTransactionsComponent extends BaseSortableTable implements OnInit {

  txtFilter = new FormControl('');
  statusFilter = new FormControl('');

  tableData: EisTranactionHistory[];

  matchFunction: (item: any, searchTerm: any) => boolean = this.matches;

  page = 1;
  pageSize = 25;

  availableStatuses = [EisSubmissionStatus.ProdFacility, EisSubmissionStatus.ProdEmissions, EisSubmissionStatus.QaFacility, EisSubmissionStatus.QaEmissions];

  constructor(private eisDataService: EisDataService,
              private fileDownloadService: FileDownloadService,
              private modalService: NgbModal,
              private toastr: ToastrService) {

    super();

    this.txtFilter.valueChanges.subscribe((text) => {
      this.controller.searchTerm = {text, status: this.statusFilter.value};
    });

    this.statusFilter.valueChanges.subscribe((text) => {
      this.controller.searchTerm = {text: this.txtFilter.value, status: text};
    });
  }

  ngOnInit() {

    this.eisDataService.retrieveTransactionHistory()
    .subscribe(result => {
      this.tableData = result.map(record => {

        if (record.eisSubmissionStatus) {

          record.eisSubmissionStatus = EisSubmissionStatus[record.eisSubmissionStatus];

        }

        return record;
      });

      this.onSort({column: 'createdDate', direction: 'desc'});
    });

    this.controller.paginate = true;
  }

  private refreshHistory() {

    this.eisDataService.retrieveTransactionHistory()
    .subscribe(result => {
      this.tableData = result.map(record => {

        if (record.eisSubmissionStatus) {

          record.eisSubmissionStatus = EisSubmissionStatus[record.eisSubmissionStatus];

        }

        return record;
      });

      this.resortTable();
    });
  }

  download(data: EisTransactionAttachment) {
    this.eisDataService.downloadAttachment(data.id)
    .subscribe(file => {
        this.fileDownloadService.downloadFile(file, data.fileName);
        error => {
          console.error(error);
          this.toastr.error('', 'An error occurred while trying to download this report.');
        };
    });
  }

  openAttachmentModal(id: number) {
    const modalRef = this.modalService.open(EisTransactionAttachmentModalComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.transactionHistoryId = id;
    modalRef.componentInstance.title = `Attach Feedback Report`;
    modalRef.componentInstance.message = `Search for feedback report file to be attached to this transaction.`;
    modalRef.result.then(() => {
      this.refreshHistory();
    }, () => {
        // needed for dismissing without errors
    });
  }

  openDeleteModal(id: number, fileName: string) {
    const modalMessage = `Are you sure you want to delete the attachment ${fileName} from this record? This will also delete the transaction if it is more than 7 days old.`;
    const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'sm' });
    modalRef.componentInstance.message = modalMessage;
    modalRef.componentInstance.continue.subscribe(() => {
      this.deleteAttachment(id);
    });
  }

  deleteAttachment(id: number) {
    this.eisDataService.deleteAttachment(id).subscribe(() => {

      this.refreshHistory();
    });
  }

  selectedTransactions() {
    return this.tableData?.filter(i => i.checked);
  }

  openTransactionDeleteModal() {
    const modalMessage = `Are you sure you want to delete the following transactions? All related attachments will also be deleted.`;
    let messageList = '<ul>';
    for (const transaction of this.selectedTransactions()) {
      messageList += `<li>${transaction.transactionId}</li>`;
    }
    messageList += '</ul>';
    const modalRef = this.modalService.open(ConfirmationDialogComponent);
    modalRef.componentInstance.message = modalMessage;
    modalRef.componentInstance.htmlMessage = messageList;
    modalRef.componentInstance.continue.subscribe(() => {
      this.deleteTransactions();
    });
  }

  deleteTransactions() {
    const selectedTransactionIds = this.selectedTransactions().map(item => item.id);
    this.eisDataService.deleteFromTransactionHistory(selectedTransactionIds)
    .subscribe(() => {

      this.refreshHistory();
      this.toastr.success('', 'Transactions were successfully deleted.');
    });
  }

  onClearFilterClick() {
    this.txtFilter.setValue(null);
  }

  matches(item: EisTranactionHistory, searchTerm: {text: string, status: string}): boolean {

    if (searchTerm.status && searchTerm.status !== item.eisSubmissionStatus) {

      return false;
    }

    const term = searchTerm.text ? searchTerm.text.toLowerCase() : '';
    return item.transactionId?.toLowerCase().includes(term)
        || item.submitterName?.toLowerCase().includes(term)
        || formatDate(item.createdDate?.toString(), 'short', 'en-US').toLowerCase().includes(term);
  }

}
