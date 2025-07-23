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
import { Component, OnInit, ViewChild, HostListener } from '@angular/core';
import { Process } from 'src/app/shared/models/process';
import { ActivatedRoute, Router } from '@angular/router';
import { EmissionUnitService } from 'src/app/core/services/emission-unit.service';
import { EmissionsProcessService } from 'src/app/core/services/emissions-process.service';
import { EmissionUnit } from 'src/app/shared/models/emission-unit';
import { ReportingPeriod } from 'src/app/shared/models/reporting-period';
import { EditProcessInfoPanelComponent } from 'src/app/modules/emissions-reporting/components/edit-process-info-panel/edit-process-info-panel.component';
import { EditProcessOperatingDetailPanelComponent } from 'src/app/modules/emissions-reporting/components/edit-process-operating-detail-panel/edit-process-operating-detail-panel.component';
import { EditProcessReportingPeriodPanelComponent } from 'src/app/modules/emissions-reporting/components/edit-process-reporting-period-panel/edit-process-reporting-period-panel.component';
import { OperatingDetail } from 'src/app/shared/models/operating-detail';
import { ToastrService } from 'ngx-toastr';
import { SharedService } from 'src/app/core/services/shared.service';
import { ReportingPeriodService } from 'src/app/core/services/reporting-period.service';
import { UtilityService } from 'src/app/core/services/utility.service';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { ConfigPropertyService } from "src/app/core/services/config-property.service";
import {forkJoin} from "rxjs";

@Component({
  selector: 'app-create-emissions-process',
  templateUrl: './create-emissions-process.component.html',
  styleUrls: ['./create-emissions-process.component.scss']
})
export class CreateEmissionsProcessComponent implements OnInit {
  emissionsUnit: EmissionUnit;
  originalProcess: Process;
  originalPeriod: ReportingPeriod;
  originalDetails: OperatingDetail;
  facilitySite: FacilitySite;
  originalId: number;
  editInfo = true;
  monthlyReportingEnabled: boolean;

  @ViewChild(EditProcessInfoPanelComponent, { static: true })
  private infoComponent: EditProcessInfoPanelComponent;

  @ViewChild(EditProcessOperatingDetailPanelComponent, { static: true })
  private operatingDetailsComponent: EditProcessOperatingDetailPanelComponent;

  @ViewChild(EditProcessReportingPeriodPanelComponent, { static: true })
  private reportingPeriodComponent: EditProcessReportingPeriodPanelComponent;

  constructor(
    private emissionUnitService: EmissionUnitService,
    private processService: EmissionsProcessService,
    private reportingPeriodService: ReportingPeriodService,
    private route: ActivatedRoute,
    private router: Router,
    private sharedService: SharedService,
    private utilityService: UtilityService,
    private propertyService: ConfigPropertyService,
    private toastr: ToastrService) { }

  ngOnInit() {

    this.route.paramMap
      .subscribe(map => {

        this.emissionUnitService.retrieve(+map.get('unitId'))
        .subscribe(unit => {
          this.emissionsUnit = unit;
        });

        if (map.get('processId')) {
          this.processService.retrieve(+map.get('processId'))
          .subscribe(result => {

            this.reportingPeriodService.retrieveForEmissionsProcess(result.id)
            .subscribe(rp => {
              const period = new ReportingPeriod();
              // these are the only values we want copied to the new RP (annual values)
                let annualRp : ReportingPeriod;
                if (rp.length > 0) {
                    annualRp = rp.filter(r => r.reportingPeriodTypeCode.code == 'A')[0];
                    Object.assign(period, {reportingPeriodTypeCode: annualRp.reportingPeriodTypeCode, emissionsOperatingTypeCode: annualRp.emissionsOperatingTypeCode});
                }
              this.originalPeriod = period;
              this.originalDetails = annualRp.operatingDetails[0];
            });

            this.originalId = result.id;

            // remove fields we don't want copied and null out id so identifier field will be unlocked
            result.id = null;
            result.emissionsProcessIdentifier = null;
            result.comments = null;
            this.originalProcess = result;
          });
        }

    });

    this.route.data
      .subscribe(data => {
		this.facilitySite = data.facilitySite;
        this.sharedService.emitChange(data.facilitySite);
      });

      forkJoin({
          monthlyReportingEnabled: this.propertyService.retrieveSltMonthlyFuelReportingEnabled(this.facilitySite?.programSystemCode?.code),
          monthlyReportingInitialYear: this.propertyService.retrieveSltMonthlyFuelReportingInitialYear(this.facilitySite?.programSystemCode?.code)
      })
      .subscribe(({monthlyReportingEnabled, monthlyReportingInitialYear}) => {
          this.monthlyReportingEnabled = monthlyReportingEnabled && (monthlyReportingInitialYear == null || this.facilitySite?.emissionsReport?.year >= monthlyReportingInitialYear);
      });
  }

  isValid() {
    return this.infoComponent.processForm.valid
        // op details will be disabled if scc is monthly reporting
        && (this.operatingDetailsComponent.operatingDetailsForm.valid || this.operatingDetailsComponent.operatingDetailsForm.disabled && this.monthlyReportingEnabled)
        && this.reportingPeriodComponent.reportingPeriodForm.valid
        && this.operatingDetailsComponent.validateOperatingPercent();
  }

  onSubmit() {

    // console.log(this.infoComponent.processForm.value);
    // console.log(this.operatingDetailsComponent.operatingDetailsForm.value);
    // console.log(this.reportingPeriodComponent.reportingPeriodForm.value);

    if (!this.isValid()) {
      this.infoComponent.processForm.markAllAsTouched();
      this.operatingDetailsComponent.operatingDetailsForm.markAllAsTouched();
      this.reportingPeriodComponent.reportingPeriodForm.markAllAsTouched();
      if (!this.operatingDetailsComponent.validateOperatingPercent()) {
        this.toastr.error('', 'Total Operating Percent must be between 99.5 and 100.5');
      }
    } else {
      const process = new Process();
      const operatingDetails = new OperatingDetail();
      const reportingPeriod = new ReportingPeriod();

      Object.assign(process, this.infoComponent.processForm.value);
      Object.assign(operatingDetails, this.operatingDetailsComponent.operatingDetailsForm.value);
      Object.assign(reportingPeriod, this.reportingPeriodComponent.reportingPeriodForm.value);

      reportingPeriod.operatingDetails = [operatingDetails];
      process.reportingPeriods = [reportingPeriod];
      process.emissionsUnitId = this.emissionsUnit.id;
      process.emissionsProcessIdentifier = this.infoComponent.processForm.controls.emissionsProcessIdentifier.value.trim();

      // console.log(process);

      // console.log(JSON.stringify(process));

      if (this.originalProcess) {

        process.releasePointAppts = this.originalProcess.releasePointAppts;

        this.processService.create(process)
        .subscribe(result => {
          this.editInfo = false;
          this.sharedService.updateReportStatusAndEmit(this.route);
          this.router.navigate(['../../..'], { relativeTo: this.route });
        });

      } else {

        this.processService.create(process)
        .subscribe(result => {
          this.editInfo = false;
          this.sharedService.updateReportStatusAndEmit(this.route);
          this.router.navigate(['../..'], { relativeTo: this.route });
        });
      }
    }

  }

  onCancel() {
    this.editInfo = false;
    if (this.originalProcess) {
      // this.router.navigate([BaseReportUrl.EMISSIONS_PROCESS, this.originalId], { relativeTo: this.route.parent });
      this.router.navigate(['../../..'], { relativeTo: this.route });
    } else {
      this.router.navigate(['../..'], { relativeTo: this.route });
    }
  }

  canDeactivate(): Promise<boolean> | boolean {
    // Allow synchronous navigation (`true`) if both forms are clean
    if (!this.editInfo
      || (!this.infoComponent.processForm.dirty
      && !this.operatingDetailsComponent.operatingDetailsForm.dirty
      && !this.reportingPeriodComponent.reportingPeriodForm.dirty)) {
        return true;
    }
    // Otherwise ask the user with the dialog service and return its promise which resolves to true or false when the user decides
    return this.utilityService.canDeactivateModal();
  }

  @HostListener('window:beforeunload', ['$event'])
  beforeunloadHandler(event) {
    if (this.editInfo
      && (this.infoComponent.processForm.dirty
      || this.operatingDetailsComponent.operatingDetailsForm.dirty
      || this.reportingPeriodComponent.reportingPeriodForm.dirty)) {
      event.preventDefault();
      event.returnValue = '';
    }
    return true;
  }

}
