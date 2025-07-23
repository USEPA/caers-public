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
