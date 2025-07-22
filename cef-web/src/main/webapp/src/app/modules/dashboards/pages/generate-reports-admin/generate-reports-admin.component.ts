/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import {Component, OnInit} from '@angular/core';
import {FormControl} from '@angular/forms';
import {EisDataService} from 'src/app/core/services/eis-data.service';
import {GeneratedReportsService} from 'src/app/core/services/generated-reports.service';
import {ReportDownloadService} from 'src/app/core/services/report-download.service';
import {UserContextService} from 'src/app/core/services/user-context.service';
import {User} from 'src/app/shared/models/user';
import {FileDownloadService} from 'src/app/core/services/file-download.service';
import { EmissionsReportingService } from 'src/app/core/services/emissions-reporting.service';
import {AdminGeneratedReportTypes} from "../../../../shared/enums/admin-generated-report-types";

const CurrentYear = new Date().getFullYear() - 1;

@Component({
  selector: 'app-generate-reports',
  templateUrl: './generate-reports-admin.component.html',
  styleUrls: ['./generate-reports-admin.component.scss']
})
export class GenerateReportsAdminComponent implements OnInit {

  availableYears: number[];
  readonly currentYear = CurrentYear;
  reportTypes = AdminGeneratedReportTypes;
  reportTypeEnumValues=[];
  user: User;
  availableSlts=[];
  invalidSelection = true;

  selectedReport: AdminGeneratedReportTypes = null;
  selectedSlt: string;

  constructor(private eisDataService: EisDataService,
			  private generatedReportsService: GeneratedReportsService,
			  private reportDownloadService: ReportDownloadService,
			  private fileDownloadService: FileDownloadService,
              private emissionReportService: EmissionsReportingService,
			  private userContextService: UserContextService) {
      this.reportTypeEnumValues = Object.keys(this.reportTypes);
  }

  ngOnInit(): void {
	this.userContextService.getUser()
    .subscribe( user => {
          this.user = user;
      });

      this.emissionReportService.getAgencyReportedYears()
          .subscribe(result => {
              this.availableSlts = result.sort((a, b) => (a.programSystemCode.code > b.programSystemCode.code) ? 1 : -1);
              this.selectedSlt = "ALL";
              this.checkSelections();
          });
  }

  onReportSelectionChange(report) {
	this.checkSelections();
  }

  onSltSelectionChange(slt) {
	this.checkSelections();
  }

  checkSelections() {
      if (this.selectedSlt !== null && this.selectedReport !== null) {
          this.invalidSelection = false;
      }
      else {
          this.invalidSelection = true;
      }
  }

  generateReport(slt: string) {
	if (!this.invalidSelection) {
		if (AdminGeneratedReportTypes[this.selectedReport] === this.reportTypes.MAX_NUMBER_OF_QAS) {
            this.generatedReportsService.generateAverageMaxNumberQasReportAdmin(slt)
                .subscribe(averageMaxNumberQasDto => {
                    this.reportDownloadService.downloadAverageMaxNumberQasReport(averageMaxNumberQasDto, slt + '_' + 'Average_Max_Number_of_QAs_Report');
                });
        }
        if (AdminGeneratedReportTypes[this.selectedReport] === this.reportTypes.SUBMISSION_STATUS_AUDIT) {
            this.generatedReportsService.generateSubmissionStatusAuditReport(slt)
                .subscribe(submissionStatusAuditDto => {
                    this.reportDownloadService.downloadSubmissionStatusAuditReport(submissionStatusAuditDto, slt + '_' + 'Submission_Status_Audit_Report');
                });
        }
	}
  }

  camelCase(value: string) {
	  let first = value.substr(0,1).toLowerCase();
	  return (first + value.substr(1)).replace(/\s/g, '');
  }
}
