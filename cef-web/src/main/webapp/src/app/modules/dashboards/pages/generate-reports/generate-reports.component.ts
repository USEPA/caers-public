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
import { FormControl } from '@angular/forms';
import { EisDataService } from 'src/app/core/services/eis-data.service';
import { GeneratedReportTypes } from 'src/app/shared/enums/generated-report-types';
import { GeneratedReportsService} from 'src/app/core/services/generated-reports.service';
import { ReportDownloadService } from 'src/app/core/services/report-download.service';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { User } from 'src/app/shared/models/user';
import { AnnualEmissionsReportComponents } from 'src/app/shared/enums/annual-emissions-report-components';
import { FileDownloadService } from 'src/app/core/services/file-download.service';

const CurrentYear = new Date().getFullYear() - 1;

@Component({
  selector: 'app-generate-reports',
  templateUrl: './generate-reports.component.html',
  styleUrls: ['./generate-reports.component.scss']
})
export class GenerateReportsComponent implements OnInit {

  cboYear = new FormControl();
  availableYears: number[];
  readonly currentYear = CurrentYear;
  reportTypes = GeneratedReportTypes;
  reportTypeEnumValues=[];
  user: User;
  annualEmissionsReportComponents = AnnualEmissionsReportComponents;
  annualEmissionsCompEnumValues=[];
  invalidSelection = true;

  selectedReport: GeneratedReportTypes = null;
  selectedAnnualEmissionsComponent: AnnualEmissionsReportComponents = null;

  constructor(private eisDataService: EisDataService,
			  private generatedReportsService: GeneratedReportsService,
			  private reportDownloadService: ReportDownloadService,
			  private fileDownloadService: FileDownloadService,
			  private userContextService: UserContextService) {
      this.reportTypeEnumValues = Object.keys(this.reportTypes);
	  this.annualEmissionsCompEnumValues = Object.keys(this.annualEmissionsReportComponents);
  }

  ngOnInit(): void {
    this.userContextService.getUser()
      .subscribe( user => {
            this.user = user;
        });

    this.availableYears = [];

    this.retrieveDataStats();
  }

  retrieveDataStats(onComplete?: () => void) {

    this.eisDataService.retrieveStatsByYear(CurrentYear).subscribe({
      next: (stats) => {

        // add current year manually just in case reports are being run before any
        // reports have been submitted and approved for the current reporting year
        this.availableYears.push(CurrentYear);
        stats.availableYears.forEach(year => {
          if (this.availableYears.indexOf(year) < 0) {

            this.availableYears.push(year);
          }
        });
      },
      complete: () => {

        if (onComplete) {
          onComplete();
        }
      }
    });
  }

  onYearSelectionChange() {
	this.checkSelections();
  }

  onReportSelectionChange() {
	this.checkSelections();
  }

  onReportComponentSelectionChange() {
	this.checkSelections();
  }

  checkSelections() {
	if (this.cboYear.value !== null && this.selectedReport !== null) {
		this.invalidSelection = GeneratedReportTypes[this.selectedReport] === this.reportTypes.ANNUAL_EMISSIONS_REPORTING_DATA
            && this.selectedAnnualEmissionsComponent === null;
	} else {
        this.invalidSelection = GeneratedReportTypes[this.selectedReport] !== this.reportTypes.MAX_NUMBER_OF_QAS;
    }
  }

  generateReport(slt: string) {
	if (!this.invalidSelection) {
		if (GeneratedReportTypes[this.selectedReport] === this.reportTypes.NONPOINT_FUEL_SUBTRACTION) {
			this.generatedReportsService.generateNonPointFuelSubtractionReport(this.cboYear.value, slt)
				.subscribe(nonPointFuelSubtractionDto => {
					this.reportDownloadService.downloadNonPointFuelSubtractionReport(nonPointFuelSubtractionDto, slt + '_' + this.cboYear.value + '_' + 'Non-Point_Fuel_Subtraction_Report');
				});

		} else if (GeneratedReportTypes[this.selectedReport] === this.reportTypes.ANNUAL_EMISSIONS_REPORTING_DATA) {
			if (this.selectedAnnualEmissionsComponent) {
				let component = this.camelCase(AnnualEmissionsReportComponents[this.selectedAnnualEmissionsComponent]);

				if (AnnualEmissionsReportComponents[this.selectedAnnualEmissionsComponent] !== this.annualEmissionsReportComponents.ALL) {
					this.generatedReportsService.annualEmissionsReportData(component, this.cboYear.value, slt)
						.subscribe(file => {
							if (file.size > 0) {
								this.fileDownloadService.downloadFile(file, this.cboYear.value + '_' + slt + '_' + this.selectedAnnualEmissionsComponent.valueOf() + '.csv');
							}
						});
				} else {
					this.generatedReportsService.annualEmissionsReportData(component, this.cboYear.value, slt)
						.subscribe(file => {
							if (file.size > 0) {
								this.fileDownloadService.downloadFile(file, this.cboYear.value + '_' + slt + '_Annual_Emission_Reporting_Data.zip');
							}
						});
				}
			}
		} else if (GeneratedReportTypes[this.selectedReport] === this.reportTypes.MAX_NUMBER_OF_QAS) {
            this.generatedReportsService.generateAverageMaxNumberQasReport()
                .subscribe(averageMaxNumberQasDto => {
                    this.reportDownloadService.downloadAverageMaxNumberQasReport(averageMaxNumberQasDto, slt + '_' + 'Average_Max_Number_of_QAs_Report');
                });
        } else if (GeneratedReportTypes[this.selectedReport] === this.reportTypes.STANDALONE_EMISSIONS_PROCESSES) {
            this.generatedReportsService.generateStandaloneEmissionsProcessesReport(this.cboYear.value, slt)
                .subscribe(file => {
                    if (file.size > 0) {
                        this.fileDownloadService.downloadFile(file, this.cboYear.value + '_' + slt + '_' + 'Standalone_Emissions_Processes_Report.csv');
                    }
                });
        } else if (GeneratedReportTypes[this.selectedReport] === this.reportTypes.FACILITY_REPORTING_STATUS) {
            this.generatedReportsService.generateFacilityReportingStatusReport(this.cboYear.value, slt)
                .subscribe(file => {
                    if (file.size > 0) {
                        this.fileDownloadService.downloadFile(file, this.cboYear.value + '_' + slt + '_' + 'Facility_Reporting_Status_Report.csv');
                    }
                });
        }
	}
  }

  camelCase(value: string) {
	  let first = value.substr(0,1).toLowerCase();
	  return (first + value.substr(1)).replace(/\s/g, '');
  }
}
