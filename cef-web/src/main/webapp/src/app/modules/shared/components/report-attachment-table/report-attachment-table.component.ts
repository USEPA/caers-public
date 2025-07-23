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
import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { FileAttachmentModalComponent } from '../file-attachment-modal/file-attachment-modal.component';
import { FileDownloadService } from 'src/app/core/services/file-download.service';
import { ReportService } from 'src/app/core/services/report.service';
import { ReportHistory } from 'src/app/shared/models/report-history';
import { SharedService } from 'src/app/core/services/shared.service';
import { ActivatedRoute } from '@angular/router';
import { User } from 'src/app/shared/models/user';
import { AppRole } from 'src/app/shared/enums/app-role';
import { AttachmentService } from 'src/app/core/services/attachment.service';

@Component({
  selector: 'app-report-attachment-table',
  templateUrl: './report-attachment-table.component.html',
  styleUrls: ['./report-attachment-table.component.scss']
})
export class ReportAttachmentTableComponent extends BaseSortableTable implements OnInit {

    @Input() facilitySite: FacilitySite;
    tableData: ReportHistory[];
    user: User;
    reportStatus: string;

    constructor(
                private userContextService: UserContextService,
                private reportService: ReportService,
                private reportAttachmentService: AttachmentService,
                private fileDownloadService: FileDownloadService,
                private sharedService: SharedService,
                private modalService: NgbModal,
                private route: ActivatedRoute) {
        super();
     }

    ngOnInit() {

        this.userContextService.getUser().subscribe( user => {
            this.user = user;
        });

        this.reportService.retrieveHistory(this.facilitySite.emissionsReport.id, this.facilitySite.id)
        .subscribe(report => {
            this.tableData = report.filter(data =>
                data.userRole !== AppRole.REVIEWER && data.fileName && data.fileName.length > 0 && !data.fileDeleted)
                .sort((a, b) => (a.actionDate < b.actionDate) ? 1 : -1);
        });

        this.reportStatus = this.facilitySite.emissionsReport.status;

    }

    download(data: ReportHistory) {
        this.sharedService.emitReportIdChange(this.facilitySite.emissionsReport.id);
        this.reportAttachmentService.downloadAttachment(data.reportAttachmentId)
        .subscribe(file => {
            this.fileDownloadService.downloadFile(file, data.fileName);
            error => console.error(error);
        });
    }

    openAttachmentModal() {
        const modalRef = this.modalService.open(FileAttachmentModalComponent, {size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.reportId = this.facilitySite.emissionsReport.id;
        modalRef.componentInstance.title = `Attach Report Document`;
        modalRef.componentInstance.message = `Search for document file to be attached to the ${this.facilitySite.emissionsReport.year} Emissions Report for ${this.facilitySite.name}.`;
		modalRef.componentInstance.programSystemCode = this.facilitySite.programSystemCode.code;
        modalRef.result.then(() => {
            this.reportService.retrieveHistory(this.facilitySite.emissionsReport.id, this.facilitySite.id)
            .subscribe(report => {
                this.tableData = report.filter(data => 
                    data.userRole !== AppRole.REVIEWER && data.fileName && data.fileName.length > 0 && !data.fileDeleted)
                    .sort((a, b) => (a.actionDate < b.actionDate) ? 1 : -1);
            });
        }, () => {
            // needed for dismissing without errors
        });
    }

    openDeleteModal(id: number, fileName: string) {
        const modalMessage = `Are you sure you want to delete the attachment ${fileName} from this report?`;
        const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'sm' });
        modalRef.componentInstance.message = modalMessage;
        modalRef.componentInstance.continue.subscribe(() => {
            this.deleteAttachment(id);
        });
    }

    deleteAttachment(id: number) {
        this.sharedService.emitReportIdChange(this.facilitySite.emissionsReport.id);
        this.reportAttachmentService.deleteAttachment(id).subscribe(() => {
            this.sharedService.updateReportStatusAndEmit(this.route);
            this.reportService.retrieveHistory(this.facilitySite.emissionsReport.id, this.facilitySite.id)
            .subscribe(report => {
                this.tableData = report.filter(data => 
                    data.userRole !== AppRole.REVIEWER && data.fileName && data.fileName.length > 0 && !data.fileDeleted)
                    .sort((a, b) => (a.actionDate < b.actionDate) ? 1 : -1);
            });
        });
    }

}
