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
import { ActivatedRoute, Router } from '@angular/router';
import { EmissionsProcessService } from 'src/app/core/services/emissions-process.service';
import { Process } from 'src/app/shared/models/process';
import { SharedService } from 'src/app/core/services/shared.service';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { ReportingPeriodService } from 'src/app/core/services/reporting-period.service';
import { ControlPath } from 'src/app/shared/models/control-path';
import { ControlPathService } from 'src/app/core/services/control-path.service';
import { EditProcessInfoPanelComponent } from 'src/app/modules/emissions-reporting/components/edit-process-info-panel/edit-process-info-panel.component';
import { ReportingPeriod } from 'src/app/shared/models/reporting-period';
import { EditProcessOperatingDetailPanelComponent } from 'src/app/modules/emissions-reporting/components/edit-process-operating-detail-panel/edit-process-operating-detail-panel.component';
import { EditProcessReportingPeriodPanelComponent } from 'src/app/modules/emissions-reporting/components/edit-process-reporting-period-panel/edit-process-reporting-period-panel.component';
import { OperatingDetailService } from 'src/app/core/services/operating-detail.service';
import { OperatingDetail } from 'src/app/shared/models/operating-detail';
import { ToastrService } from 'ngx-toastr';
import { EmissionUnitService } from 'src/app/core/services/emission-unit.service';
import { EmissionUnit } from 'src/app/shared/models/emission-unit';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { OperatingStatus } from 'src/app/shared/enums/operating-status';
import { BaseReportUrl } from 'src/app/shared/enums/base-report-url';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { UtilityService } from 'src/app/core/services/utility.service';
import {EntityType} from "../../../../shared/enums/entity-type";
import {ConfigPropertyService} from "../../../../core/services/config-property.service";
import {LookupService} from "../../../../core/services/lookup.service";
import {forkJoin} from "rxjs";
import {MonthlyReportingPeriod} from "src/app/shared/enums/monthly-reporting-periods";

@Component({
  selector: 'app-emissions-process-details',
  templateUrl: './emissions-process-details.component.html',
  styleUrls: ['./emissions-process-details.component.scss']
})
export class EmissionsProcessDetailsComponent implements OnInit {
  process: Process;
  controlPaths: ControlPath[];
  facilitySite: FacilitySite;
  unitIdentifier: string;
  emissionsUnit: EmissionUnit;

  sltEditEnabled = false;
  sltEditPossible = false;
  isMonthlyReportingScc  = false;
  monthlyReportingEnabled: boolean;
  semiAnnualReportSubmittedOrApproved = false;
  sltBillingExemptEnabled: boolean;

  hasAircraftCode = false;
  readOnlyMode = true;
  sccEditMode = false;

  editInfo = false;
  editDetails = false;
  editPeriod = false;

  createPeriod = false;

  @ViewChild(EditProcessInfoPanelComponent)
  infoComponent: EditProcessInfoPanelComponent;

  @ViewChild(EditProcessOperatingDetailPanelComponent)
  operatingDetailsComponent: EditProcessOperatingDetailPanelComponent;

  @ViewChild(EditProcessReportingPeriodPanelComponent)
  reportingPeriodComponent: EditProcessReportingPeriodPanelComponent;

  operatingStatus = OperatingStatus;
  entityType = EntityType;
  monthlyReportingPeriods = MonthlyReportingPeriod;

  constructor(
    private emissionUnitService: EmissionUnitService,
    private processService: EmissionsProcessService,
    private reportingPeriodService: ReportingPeriodService,
    private operatingDetailService: OperatingDetailService,
    private controlPathService: ControlPathService,
    private userContextService: UserContextService,
    private utilityService: UtilityService,
    private route: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal,
    private toastr: ToastrService,
    private propertyService: ConfigPropertyService,
    private lookupService: LookupService,
    private sharedService: SharedService) { }

  ngOnInit() {
    this.route.paramMap
    .subscribe(map => {
      this.processService.retrieve(+map.get('processId'))
      .subscribe(process => {
        this.process = process;

        if (this.process.aircraftEngineTypeCode) {
          this.hasAircraftCode = true;
        }

        this.emissionUnitService.retrieve(process.emissionsUnitId)
        .subscribe(unit => {
          this.unitIdentifier = unit.unitIdentifier;
          this.emissionsUnit = unit;
        });

        this.controlPathService.retrieveForEmissionsProcess(+map.get('processId'))
        .subscribe(controlPaths => {
          this.controlPaths = controlPaths.sort((a, b) => (a.pathId > b.pathId) ? 1 : -1);
        });

        this.processService.retrievePrevious(this.process.id)
        .subscribe(result => {
            this.process.previousProcess = result;
        });

        forkJoin({
          sltEditEnabled: this.propertyService.retrieveProcessSccEntryEnabled(this.facilitySite?.programSystemCode?.code),
          monthlyReportingEnabled: this.propertyService.retrieveSltMonthlyFuelReportingEnabled(this.facilitySite?.programSystemCode?.code),
          monthlyReportingInitialYear: this.propertyService.retrieveSltMonthlyFuelReportingInitialYear(this.facilitySite?.programSystemCode?.code),
          sltBillingExemptEnabled: this.propertyService.retrieveSltBillingExemptEnabled(this.facilitySite?.programSystemCode?.code)
        })
        .subscribe(({sltEditEnabled, monthlyReportingEnabled, monthlyReportingInitialYear, sltBillingExemptEnabled}) => {
          this.monthlyReportingEnabled = monthlyReportingEnabled && (monthlyReportingInitialYear == null || this.facilitySite?.emissionsReport?.year >= monthlyReportingInitialYear);

          this.sltBillingExemptEnabled = sltBillingExemptEnabled;

          if (this.process.sccCode) {
            this.getPointSourceScc(this.process.sccCode);
          }

          this.sltEditEnabled = sltEditEnabled;

          this.sccEditMode = this.sltEditPossible && this.sltEditEnabled;
        });
      });
    });

    // emits the report info to the sidebar
    this.route.data
    .subscribe((data: { facilitySite: FacilitySite }) => {
      this.facilitySite = data.facilitySite;
      this.userContextService.getUser().subscribe( user => {
        if (UtilityService.isNotReadOnlyMode(user, data.facilitySite.emissionsReport.status)) {
          this.readOnlyMode = false;
        }
        if (user.canReview() && UtilityService.isInProgressStatus(data.facilitySite.emissionsReport.status)) {
            this.sltEditPossible = true;
            this.sccEditMode = this.sltEditPossible && this.sltEditEnabled;
        }

      });
      this.sharedService.emitChange(data.facilitySite);
    });

      this.sharedService.processSccChangeEmitted$.subscribe(scc => {
          if (scc) {
              this.getPointSourceScc(scc);
          }
      });
  }

  setEditInfo(value: boolean) {
    this.editInfo = value;
  }

  setEditDetails(value: boolean) {
    this.editDetails = value;
  }

  setEditPeriod(value: boolean) {
    this.editPeriod = value;
  }

  setCreatePeriod(value: boolean) {
    this.createPeriod = value;
  }

  isMonthlyReportingProcess() {
    return this.monthlyReportingEnabled && this.isMonthlyReportingScc;
  }

  openCopyModal() {
    const modalMessage = `You are about to be redirected to enter an alternative throughput for process "${this.process?.emissionsProcessIdentifier}" of
                          emissions unit "${this.emissionsUnit?.unitIdentifier}". A copy of this process will be automatically generated and
                          pre-populated with the current process information. Are you sure you wish to continue?`;
    const modalRef = this.modalService.open(ConfirmationDialogComponent);
    modalRef.componentInstance.message = modalMessage;
    modalRef.componentInstance.continue.subscribe(() => {
        this.copyProcess();
    });
  }

  copyProcess() {
    this.router.navigate(
      [BaseReportUrl.EMISSIONS_UNIT, this.emissionsUnit.id, 'process', 'create', this.process.id], { relativeTo: this.route.parent });
  }

  updateProcess() {
    if (!this.infoComponent.processForm.valid) {
      this.infoComponent.processForm.markAllAsTouched();
    } else {
        const updatedProcess = new Process();

        Object.assign(updatedProcess, this.infoComponent.processForm.value);
        updatedProcess.emissionsUnitId = this.process.emissionsUnitId;
        updatedProcess.id = this.process.id;

        if (this.sccEditMode) {

            this.processService.updateScc(updatedProcess)
                .subscribe(result => {

                    if (this.process && updatedProcess && this.process.sccCode !== updatedProcess.sccCode) {
                        this.toastr.success(`This SCC has been updated to ${result.sccCode}. Ensure the facility updates related data fields (e.g. emission factor, throughput).`);
                    }

                    Object.assign(this.process, result);
                    this.sharedService.updateReportStatusAndEmit(this.route);
                    this.setEditInfo(false);
                });
        } else {

            this.processService.update(updatedProcess)
                .subscribe(result => {

                    if (updatedProcess.aircraftEngineTypeCode) {
                        this.hasAircraftCode = true;
                    } else {
                        this.hasAircraftCode = false;
                    }

                    if (this.process && updatedProcess && this.process.sccCode !== updatedProcess.sccCode) {

                        this.toastr.success(`This SCC has been updated to ${result.sccCode}, ensure to update related data fields (e.g. emission factor, throughput) as well.`);

                        if (this.isMonthlyReportingProcess() || this.process.initialMonthlyReportingPeriod != updatedProcess.initialMonthlyReportingPeriod) {
                            this.displayMonthlyReportingWarning(false);
                        }
                    }

                    Object.assign(this.process, result);
                    this.sharedService.updateReportStatusAndEmit(this.route);
                    this.sharedService.emitSideNavItemChangeNoRouteChange(this.facilitySite.id);
                    this.setEditInfo(false);
                });
        }
    }
  }

  updateOperatingDetail(detail: OperatingDetail) {
    if (!this.operatingDetailsComponent.operatingDetailsForm.valid || !this.operatingDetailsComponent.validateOperatingPercent()) {
      this.operatingDetailsComponent.operatingDetailsForm.markAllAsTouched();
      if (!this.operatingDetailsComponent.validateOperatingPercent()) {
        this.toastr.error('', 'Total Operating Percent must be between 99.5 and 100.5');
      }
    } else {
      const updatedDetail = new OperatingDetail();

      Object.assign(updatedDetail, this.operatingDetailsComponent.operatingDetailsForm.value);

      updatedDetail.id = detail.id;

      this.operatingDetailService.update(updatedDetail)
      .subscribe(result => {

        Object.assign(detail, result);
        this.sharedService.updateReportStatusAndEmit(this.route);
        this.setEditDetails(false);
      });
    }
  }

  updateReportingPeriod(period: ReportingPeriod) {
    if (!this.reportingPeriodComponent.reportingPeriodForm.valid) {
      this.reportingPeriodComponent.reportingPeriodForm.markAllAsTouched();
    } else {
      const updatedReportingPeriod = new ReportingPeriod();

      Object.assign(updatedReportingPeriod, this.reportingPeriodComponent.reportingPeriodForm.value);

      updatedReportingPeriod.emissionsProcessId = this.process.id;
      updatedReportingPeriod.id = period.id;

      this.reportingPeriodService.update(updatedReportingPeriod)
      .subscribe(result => {

          if (this.isMonthlyReportingProcess() && period && updatedReportingPeriod) {
              if (period.calculationMaterialCode != updatedReportingPeriod.calculationMaterialCode
                  || period.calculationParameterUom != updatedReportingPeriod.calculationParameterUom
                  || period.fuelUseMaterialCode != updatedReportingPeriod.fuelUseMaterialCode
                  || period.fuelUseUom != updatedReportingPeriod.fuelUseUom) {
                this.displayMonthlyReportingWarning(true);
              }
          }

        Object.assign(period, result.reportingPeriod);
        this.sharedService.updateReportStatusAndEmit(this.route);
        this.setEditPeriod(false);

        if (result.failedEmissions.length) {
          this.toastr.error(
            `Total Emissions for ${result.failedEmissions.join(', ')} could not be calculated because the Reporting Period Throughput units of measure cannot be converted into the Emission Factor Denominator units of measure.`,
            '',
            {timeOut: 20000, extendedTimeOut: 10000, closeButton: true}
          );
        }

        if (result.notUpdatedEmissions.length) {
          this.toastr.warning(
            `Total Emissions for ${result.notUpdatedEmissions.join(', ')} were not recalculated and must be updated manually.`,
            '',
            {timeOut: 20000, extendedTimeOut: 10000, closeButton: true}
          );
        }

        if (result.updatedEmissions.length) {
          this.toastr.success(
            `Reported data has been updated and/or Total Emissions were recalculated for ${result.updatedEmissions.join(', ')}.`,
            '',
            {timeOut: 20000, extendedTimeOut: 10000, closeButton: true}
          );
        }
      });
    }
  }

  createReportingPeriod() {
    if (!this.operatingDetailsComponent.operatingDetailsForm.valid
        || !this.reportingPeriodComponent.reportingPeriodForm.valid
        || !this.operatingDetailsComponent.validateOperatingPercent()) {

      this.operatingDetailsComponent.operatingDetailsForm.markAllAsTouched();
      this.reportingPeriodComponent.reportingPeriodForm.markAllAsTouched();
      if (!this.operatingDetailsComponent.validateOperatingPercent()) {
        this.toastr.error('', 'Total Operating Percent must be between 99.5 and 100.5');
      }
    } else {
      const operatingDetails = new OperatingDetail();
      const reportingPeriod = new ReportingPeriod();

      Object.assign(operatingDetails, this.operatingDetailsComponent.operatingDetailsForm.value);
      Object.assign(reportingPeriod, this.reportingPeriodComponent.reportingPeriodForm.value);

      reportingPeriod.operatingDetails = [operatingDetails];
      reportingPeriod.emissionsProcessId = this.process.id;

      this.reportingPeriodService.create(reportingPeriod)
      .subscribe(result => {

        this.process.reportingPeriods.push(result);
        this.sharedService.updateReportStatusAndEmit(this.route);
        this.setCreatePeriod(false);
      });
    }
  }

  getPointSourceScc(processScc: string) {
    this.lookupService.retrievePointSourceSccCode(processScc)
      .subscribe(result => {
        this.isMonthlyReportingScc = result && result.monthlyReporting && this.monthlyReportingEnabled;

        if ((this.facilitySite.emissionsReport.midYearSubmissionStatus === 'SUBMITTED' || this.facilitySite.emissionsReport.midYearSubmissionStatus === 'APPROVED') && result.monthlyReporting) {
          this.semiAnnualReportSubmittedOrApproved = true;
        }
      });
  }

  private displayMonthlyReportingWarning(toMonthly: boolean) {
    let message = 'Saved changes impact monthly reporting.';
    if (toMonthly) {
      message = message.concat(' Please update impacted values within monthly reporting.');
    }
    this.toastr.warning(
      message,
      '',
      {timeOut: 20000, extendedTimeOut: 10000, closeButton: true}
    );
  }

  canDeactivate(): Promise<boolean> | boolean {
    if ((!this.editInfo && !this.editDetails && !this.editPeriod && !this.createPeriod)
      || (!this.infoComponent?.processForm.dirty
      && !this.operatingDetailsComponent?.operatingDetailsForm.dirty
      && !this.reportingPeriodComponent?.reportingPeriodForm.dirty)) {
        this.setEditInfo(false);
        this.setEditDetails(false);
        this.setEditPeriod(false);
        return true;
    }

    let result = this.utilityService.canDeactivateModal();
    result.then(data => {
      if (data) {
        this.setEditInfo(false);
        this.setEditDetails(false);
        this.setEditPeriod(false);
      }
    });
    return result;
  }

  processHasSubmittedSemiannualData(p : Process) {
      for (const rp of p.reportingPeriods) {
          if (rp.reportingPeriodTypeCode.shortName == this.monthlyReportingPeriods.SEMIANNUAL &&
              rp.calculationParameterValue != null && Number(rp.calculationParameterValue) > 0 &&
              this.semiAnnualReportSubmittedOrApproved) {

              return true;
          }
      }
      return false;
  }

  @HostListener('window:beforeunload', ['$event'])
  beforeunloadHandler(event) {
    if ((this.editInfo || this.editDetails || this.editPeriod)
      && ((this.infoComponent && this.infoComponent.processForm.dirty)
      || (this.operatingDetailsComponent && this.operatingDetailsComponent.operatingDetailsForm.dirty)
      || (this.reportingPeriodComponent && this.reportingPeriodComponent.reportingPeriodForm.dirty))) {
      event.preventDefault();
      event.returnValue = '';
    }
    return true;
  }

}
