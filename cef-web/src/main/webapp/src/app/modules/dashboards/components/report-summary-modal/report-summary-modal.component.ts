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
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { SubmissionUnderReview } from 'src/app/shared/models/submission-under-review';
import { ReportSummary } from 'src/app/shared/models/report-summary';
import { ReportService } from 'src/app/core/services/report.service';
import { ReportDownloadService } from 'src/app/core/services/report-download.service';

@Component({
  selector: 'app-report-summary-modal',
  templateUrl: './report-summary-modal.component.html',
  styleUrls: ['./report-summary-modal.component.scss']
})
export class ReportSummaryModalComponent implements OnInit {

    @Input() submission: SubmissionUnderReview;
    tableData: ReportSummary[];
    radiationData: ReportSummary[];
    emissionsReportYear: number;

    constructor(
      public activeModal: NgbActiveModal,
      private reportService: ReportService,
      private reportDownloadService: ReportDownloadService) { }

    nonDormantData = [];

    ngOnInit() {
        if (this.submission.emissionsReportId) {
            this.emissionsReportYear = this.submission.year;
            this.reportService.retrieve(this.submission.emissionsReportId)
            .subscribe(pollutants => {
              // filter out radiation pollutants to show separately at the end of the table
              // (only radionucleides right now which is code 605)
              this.tableData = pollutants.filter(pollutant => {
                  return pollutant.pollutantCode !== '605';
              });

              this.radiationData = pollutants.filter(pollutant => {
                  return pollutant.pollutantCode === '605';
              });
            });
        }
    }

    onClose() {
      this.activeModal.close();
    }

    onPrint() {
      alert('Print clicked');
    }

    downloadSummaryReport() {
        this.nonDormantData = this.tableData.filter((item) => !item.notReporting);

        if ((this.submission.reportStatus === 'APPROVED') || (this.submission.reportStatus === 'SUBMITTED')) {
            this.reportDownloadService.downloadReportSummary(this.nonDormantData, this.submission.agencyFacilityIdentifier + '_' +
            this.submission.year + '_' + 'Report_Summary' + '_Final_Submission');
        } else {
            this.reportDownloadService.downloadReportSummary(this.nonDormantData, this.submission.agencyFacilityIdentifier + '_' +
            this.submission.year + '_' + 'Report_Summary' + '_Submission_In_Progress');
        }
    }

}
