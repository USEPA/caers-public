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
