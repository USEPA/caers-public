/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { EmissionUnit } from 'src/app/shared/models/emission-unit';
import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BaseReportUrl } from 'src/app/shared/enums/base-report-url';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { EmissionUnitService } from 'src/app/core/services/emission-unit.service';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { SharedService } from 'src/app/core/services/shared.service';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import { UtilityService } from 'src/app/core/services/utility.service';
import { MonthlyReportingPeriod } from 'src/app/shared/enums/monthly-reporting-periods';

@Component({
  selector: 'app-emissions-unit-table',
  templateUrl: './emissions-unit-table.component.html',
  styleUrls: ['./emissions-unit-table.component.scss']
})
export class EmissionsUnitTableComponent extends BaseSortableTable implements OnInit {
  @Input() tableData: EmissionUnit[];
  @Input() createUrl = '.';
  readOnlyMode = true;
  baseUrl: string;
  faPlus = faPlus;

  constructor(private modalService: NgbModal,
              private emissionUnitService: EmissionUnitService,
              private userContextService: UserContextService,
              private route: ActivatedRoute,
              private sharedService: SharedService) {
    super();
  }

  ngOnInit() {
    this.route.paramMap
      .subscribe(map => {
        this.baseUrl = `/facility/${map.get('facilityId')}/report/${map.get('reportId')}/${BaseReportUrl.EMISSIONS_UNIT}`;
    });

    this.route.data
      .subscribe((data: { facilitySite: FacilitySite }) => {
        this.userContextService.getUser().subscribe( user => {
			if (UtilityService.isNotReadOnlyMode(user, data.facilitySite.emissionsReport.status)) {
				this.readOnlyMode = false;
			}
		});
	  });
  }

  openDeleteModal(unit: EmissionUnit) {

    let modalMessage = `Are you sure you want to delete ${unit.unitIdentifier} from this facility?
                        This will also remove any Emission Process, Emissions, Control Assignments,
                        and Release Point Assignments associated with this Emissions Unit.`;

    let found = false;
    for (let i = unit.emissionsProcesses.length - 1; i >= 0; i--) {
      const process = unit.emissionsProcesses[i];

      for (let j = process.reportingPeriods.length - 1; j >= 0; j--) {
        const period = process.reportingPeriods[j];

        if (period.reportingPeriodTypeCode.shortName != MonthlyReportingPeriod.ANNUAL
             && ((period.calculationParameterValue != null && Number(period.calculationParameterValue) > 0)
             || (period.fuelUseValue != null && Number(period.fuelUseValue) > 0))) {
               modalMessage = `Are you sure you want to delete ${unit.unitIdentifier} from this facility?
                               This will also remove any Emission Process, Emissions, Control Assignments, Release
                               Point Assignments, and Monthly Reporting data associated with this Emissions Unit.`;
               found = true;
               break;
        }
      }
      if (found) {
          break;
      }
    }

    const modalRef = this.modalService.open(ConfirmationDialogComponent);
    modalRef.componentInstance.message = modalMessage;
    modalRef.componentInstance.continue.subscribe(() => {
        this.deleteEmissionUnit(unit.id, unit.facilitySiteId);
    });
  }

  // delete an emission unit from the database
  deleteEmissionUnit(emissionUnitId: number, facilitySiteId: number) {
    this.emissionUnitService.delete(emissionUnitId).subscribe(() => {

      // update the UI table with the current list of emission units
      this.emissionUnitService.retrieveForFacility(facilitySiteId)
        .subscribe(emissionUnitResponse => {
          this.tableData = emissionUnitResponse;
        });

      // emit the facility data back to the sidebar to reflect the updated
      // list of emission units
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
}
