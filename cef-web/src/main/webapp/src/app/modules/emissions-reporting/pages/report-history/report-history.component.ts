/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import { ReportService } from 'src/app/core/services/report.service';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { SharedService } from 'src/app/core/services/shared.service';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { ReportAction } from 'src/app/shared/enums/report-action.enum';
import { FileDownloadService } from 'src/app/core/services/file-download.service';
import { ReportHistory } from 'src/app/shared/models/report-history';
import { AttachmentService } from 'src/app/core/services/attachment.service';
import { EmissionsReportingService } from 'src/app/core/services/emissions-reporting.service';
import { UtilityService } from 'src/app/core/services/utility.service';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {
    ThresholdResetConfirmationModalComponent
} from "../../components/threshold-reset-confirmation-modal/threshold-reset-confirmation-modal.component";
import {
    ReviewerCommentsModalComponent
} from "../../components/reviewer-comments-modal/reviewer-comments-modal.component";

@Component({
  selector: 'app-report-history',
  templateUrl: './report-history.component.html',
  styleUrls: ['./report-history.component.scss']
})
export class ReportHistoryComponent extends BaseSortableTable implements OnInit {
    facilitySite: FacilitySite;
    tableData: ReportHistory[];
    reportAction: ReportAction;
    emissionsReportId: number;

	readonly xmlType = 'application/xml';
    readonly htmlType = 'text/html';

    constructor(
        private reportService: ReportService,
        private reportAttachmentService: AttachmentService,
        private fileDownloadService: FileDownloadService,
        private route: ActivatedRoute,
        private sharedService: SharedService,
		private emissionsReportingService: EmissionsReportingService,
        private modalService: NgbModal) {
            super();
        }

    ngOnInit() {
        this.route.data.subscribe((data: { facilitySite: FacilitySite }) => {
            this.facilitySite = data.facilitySite;
            this.sharedService.emitChange(data.facilitySite);
            if (this.facilitySite.id) {
                this.emissionsReportId = this.facilitySite.emissionsReport.id;
            }
        });

        this.reportService.retrieveHistory(this.emissionsReportId, this.facilitySite.id)
        .subscribe(report => {
            this.tableData = report;
        });
    }

    enumValue(action: string) {
        return ReportAction[action];
    }

    openReviewerCommentsModal(data: ReportHistory) {
        const modalRef = this.modalService.open(ReviewerCommentsModalComponent, {size: 'lg'});
        modalRef.componentInstance.reviewerComments = data.reviewerComments;
    }

    download(data: ReportHistory) {
        this.sharedService.emitReportIdChange(this.emissionsReportId);
        this.reportAttachmentService.downloadAttachment(data.reportAttachmentId)
        .subscribe(file => {
            this.fileDownloadService.downloadFile(file, data.fileName);
            error => console.error(error);
        });
    }

	downloadCopyOfRecord(data: ReportHistory) {
		this.emissionsReportingService.downloadCopyOfRecord(data.cromerrActivityId, data.cromerrDocumentId)
        .subscribe(file => {

            const baseFileName = `${this.facilitySite.agencyFacilityIdentifier}-${this.facilitySite.name}-${this.facilitySite.emissionsReport.year}`;

			if (file.type === this.xmlType) {
		    	this.fileDownloadService.downloadFile(file, UtilityService.removeSpecialCharacters(`${baseFileName}_CoR.xml`));
			} else if (file.type === this.htmlType) {
                this.fileDownloadService.downloadFile(file, UtilityService.removeSpecialCharacters(`${baseFileName}_CoR.html`));
            } else {
				this.fileDownloadService.downloadFile(file, UtilityService.removeSpecialCharacters(`${baseFileName}_CoR.zip`));
			}
            error => console.error(error);
        });
	}

}
