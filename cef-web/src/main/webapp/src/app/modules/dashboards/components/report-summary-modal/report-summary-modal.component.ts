/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
