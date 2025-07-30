/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { SubmissionUnderReview } from 'src/app/shared/models/submission-under-review';
import { MonthlyFuelReportingService } from 'src/app/core/services/monthly-fuel-reporting.services';
import { ReportDownloadService } from 'src/app/core/services/report-download.service';
import { EmissionService } from 'src/app/core/services/emission.service';
import { ReportingPeriodService } from 'src/app/core/services/reporting-period.service';
import { FacilitySiteService } from 'src/app/core/services/facility-site.service';

@Component({
  selector: 'app-monthly-submission-review-list',
  templateUrl: './monthly-submission-review-list.component.html',
  styleUrls: ['./monthly-submission-review-list.component.scss']})

export class MonthlySubmissionReviewListComponent extends BaseSortableTable implements OnInit {

	@Input() tableData: SubmissionUnderReview[];
	@Input() reviewer: boolean;
	@Input() monthlyReportStatus: string;
	@Output() refreshSubmissions = new EventEmitter();

	constructor(
    private emissionService: EmissionService,
    private facilitySiteService: FacilitySiteService,
    private reportingPeriodService: ReportingPeriodService,
    private monthlyReportingService: MonthlyFuelReportingService,
    private reportDownloadService: ReportDownloadService
  ) {
    super();
	}

	ngOnInit() {
		this.controller.paginate = true;
	}
	
	downloadMonthlyReport(facilitySiteId: number, reportId: number, reportYear: number) {
    this.emissionService.retrieveMonthlyReportingDownloadDtoList(reportId, true)
      .subscribe((result) => {
        this.reportDownloadService.downloadMonthlyReport(result, facilitySiteId + '_' +
          reportYear + '_' + 'Monthly_Report');
      })
  }

}
