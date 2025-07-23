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
