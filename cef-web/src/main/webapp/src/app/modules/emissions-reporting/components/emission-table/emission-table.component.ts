/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { Emission } from 'src/app/shared/models/emission';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ReportingPeriod } from 'src/app/shared/models/reporting-period';
import { Process } from 'src/app/shared/models/process';
import { EmissionService } from 'src/app/core/services/emission.service';
import {ReportingPeriodService} from 'src/app/core/services/reporting-period.service';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { ActivatedRoute } from '@angular/router';
import { BaseReportUrl } from 'src/app/shared/enums/base-report-url';
import { SharedService } from 'src/app/core/services/shared.service';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import {MonthlyReportingPeriod} from "src/app/shared/enums/monthly-reporting-periods";

@Component({
  selector: 'app-emission-table',
  templateUrl: './emission-table.component.html',
  styleUrls: ['./emission-table.component.scss']
})
export class EmissionTableComponent extends BaseSortableTable implements OnInit, OnChanges {
  @Input() tableData: Emission[];
  @Input() reportingPeriod: ReportingPeriod;
  @Input() process: Process;
  @Input() readOnlyMode: boolean;
  @Input() emissionsReportId: number;
  @Input() monthlyReportingProcess: boolean;
  @Input() semiannualSubmitted: boolean;

    monthlyReportingPeriods = MonthlyReportingPeriod;
    emissionsCreatedAfterSemiannualSubmission: string[] = [];
    baseUrl: string;
    faPlus = faPlus;

    constructor(private modalService: NgbModal,
                private emissionService: EmissionService,
                private reportingPeriodService: ReportingPeriodService,
                private route: ActivatedRoute,
                private sharedService: SharedService) {
        super();
    }

    ngOnInit() {

        this.tableData.sort((a,b) => (a.pollutant.pollutantName > b.pollutant.pollutantName) ? 1 : -1);

        this.route.paramMap.subscribe(map => {
            this.baseUrl = `/facility/${map.get('facilityId')}/report/${map.get('reportId')}/${BaseReportUrl.REPORTING_PERIOD}/${this.reportingPeriod.id}/${BaseReportUrl.EMISSION}`;
        });
    }

    ngOnChanges() {
        this.emissionService.retrieveEmissionsCreatedAfterSemiannualSubmission(this.process.id, this.emissionsReportId)
            .subscribe(result => {
                this.emissionsCreatedAfterSemiannualSubmission = result;
            });

        this.tableData.sort((a,b) => (a.pollutant.pollutantName > b.pollutant.pollutantName) ? 1 : -1);
    }


    openDeleteModal(emissionName: string, emissionId: number) {
        const modalMessage = `Are you sure you want to delete the pollutant ${emissionName} from this process?`;
        const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: 'sm' });
        modalRef.componentInstance.message = modalMessage;
        modalRef.componentInstance.continue.subscribe(() => {
            this.deleteEmission(emissionId);
        });
    }

    // delete an emission from the database
    deleteEmission(emissionId: number) {
        this.emissionService.delete(emissionId).subscribe(() => {

        this.sharedService.updateReportStatusAndEmit(this.route);

        // update the UI table with the current list of emissions
        this.reportingPeriodService.retrieve(this.reportingPeriod.id)
            .subscribe(reportingPeriodResponse => {
                this.tableData = reportingPeriodResponse.emissions;
            });
        });
    }
}
