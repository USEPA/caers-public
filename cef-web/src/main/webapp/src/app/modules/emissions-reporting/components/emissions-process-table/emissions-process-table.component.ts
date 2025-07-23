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
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { Process } from 'src/app/shared/models/process';
import { EmissionsProcessService } from 'src/app/core/services/emissions-process.service';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { Component, OnInit, OnChanges, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { SharedService } from 'src/app/core/services/shared.service';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import {MonthlyReportingPeriod} from "src/app/shared/enums/monthly-reporting-periods";
import { EmissionUnit } from 'src/app/shared/models/emission-unit';

@Component({
  selector: 'app-emissions-process-table',
  templateUrl: './emissions-process-table.component.html',
  styleUrls: ['./emissions-process-table.component.scss']
})
export class EmissionsProcessTableComponent extends BaseSortableTable implements OnInit {
  @Input() tableData: Process[];
  @Input() createUrl = '.';
  @Input() parentComponentType: string;
  @Input() readOnlyMode: boolean;
  @Input() semiannualSubmitted: boolean;
  @Input() emissionUnit: EmissionUnit;

    monthlyReportingPeriods = MonthlyReportingPeriod;
    processWithMonthlyData: string[] = [];
    baseUrl: string;
    faPlus = faPlus;

    constructor(private route: ActivatedRoute,
                private processService: EmissionsProcessService,
                private modalService: NgbModal,
                private sharedService: SharedService) {
        super();
    }

    ngOnInit() {
        this.route.paramMap
            .subscribe(map => {
            this.baseUrl = `/facility/${map.get('facilityId')}/report/${map.get('reportId')}`;
        });
    }

    ngOnChanges() {
        this.processWithMonthlyData = [];
        for (const p of this.tableData) {
            for (const rp of p.reportingPeriods) {
                if (rp.reportingPeriodTypeCode.shortName == this.monthlyReportingPeriods.SEMIANNUAL &&
                    rp.calculationParameterValue != null && Number(rp.calculationParameterValue) > 0 &&
                    this.semiannualSubmitted) {

                    this.processWithMonthlyData.push(p.emissionsProcessIdentifier);
                }
            }
        }
    }

    deleteProcess(processId: number, emissionsUnitId: number) {
        this.processService.delete(processId).subscribe(() => {

            // update the table with the list of processes
            this.processService.retrieveForEmissionsUnit(emissionsUnitId)
                .subscribe(processes1 => {
                    this.tableData = processes1.sort((a, b) => (a.id > b.id ? 1 : -1));
                });

            // emit the facility data back to the sidebar to reflect the updated
            // list of emission processes
            this.route.data
            .subscribe((data: { facilitySite: FacilitySite }) => {
                this.sharedService.emitChange(data.facilitySite);
            });
            this.sharedService.updateReportStatusAndEmit(this.route);
        }, error => {
            if (error.error && error.status === 422) {
                const modalRef = this.modalService.open(ConfirmationDialogComponent);
                modalRef.componentInstance.message = error.error.message;
                modalRef.componentInstance.singleButton = true;
            }
        });
    }

    openDeleteModal(process: Process) {
        let modalMessage = `Are you sure you want to delete ${process.emissionsProcessIdentifier}? This will also remove
            any Emissions, Control Assignments, and Release Point Assignments associated with this Emissions Process.`;

        for (let i = process.reportingPeriods.length - 1; i >= 0; i--) {
            const period = process.reportingPeriods[i];

            if (period.reportingPeriodTypeCode.shortName != MonthlyReportingPeriod.ANNUAL
                && ((period.calculationParameterValue != null && Number(period.calculationParameterValue) > 0)
                    || (period.fuelUseValue != null && Number(period.fuelUseValue) > 0))) {
                modalMessage = `Are you sure you want to delete ${process.emissionsProcessIdentifier}? This will also
                                remove any Emissions, Control Assignments, Release Point Assignments, and Monthly
                                Reporting data associated with this Emissions Process.`;
                break;
            }
        }

        const modalRef = this.modalService.open(ConfirmationDialogComponent);
        modalRef.componentInstance.message = modalMessage;
        modalRef.componentInstance.continue.subscribe(() => {
            this.deleteProcess(process.id, process.emissionsUnitId);
        });
    }
}
