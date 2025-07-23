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
