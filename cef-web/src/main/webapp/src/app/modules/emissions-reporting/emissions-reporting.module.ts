/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { SharedModule } from 'src/app/modules/shared/shared.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule} from '@angular/forms';

import { ReportsRoutingModule } from './emissions-reporting-routing.module';
import { EmissionsReportingDashboardComponent } from 'src/app/modules/emissions-reporting/pages/emissions-reporting-dashboard/emissions-reporting-dashboard.component';
import { EmissionsReportingComponent } from 'src/app/modules/emissions-reporting/pages/emissions-reporting/emissions-reporting.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { EmissionUnitDashboardComponent } from 'src/app/modules/emissions-reporting/pages/emission-unit-dashboard/emission-unit-dashboard.component';
import { FaConfig, FaIconLibrary, FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import { StepProgressComponent } from 'src/app/modules/emissions-reporting/components/step-progress/step-progress.component';


import { EmissionUnitInfoComponent } from 'src/app/modules/emissions-reporting/components/emission-unit-info/emission-unit-info.component';
import { ReleasePointTableComponent } from 'src/app/modules/emissions-reporting/components/release-point-table/release-point-table.component';
import { EmissionsUnitTableComponent } from 'src/app/modules/emissions-reporting/components/emissions-unit-table/emissions-unit-table.component';
import { FacilityInformationComponent } from 'src/app/modules/emissions-reporting/pages/facility-information/facility-information.component';
import { EmissionsUnitToSideNavPipe } from 'src/app/shared/pipes/emissions-unit-to-side-nav.pipe';
import { ReleasePointDetailsComponent } from 'src/app/modules/emissions-reporting/pages/release-point-details/release-point-details.component';
import { EmissionsProcessTableComponent } from 'src/app/modules/emissions-reporting/components/emissions-process-table/emissions-process-table.component';
import { EmissionsProcessDetailsComponent } from 'src/app/modules/emissions-reporting/pages/emissions-process-details/emissions-process-details.component';
import { EmissionTableComponent } from 'src/app/modules/emissions-reporting/components/emission-table/emission-table.component';
import { ReleasePointApptTableComponent } from 'src/app/modules/emissions-reporting/components/release-point-appt-table/release-point-appt-table.component';
import { ControlDeviceDetailsComponent } from 'src/app/modules/emissions-reporting/pages/control-device-details/control-device-details.component';
import { ControlAssignmentTableComponent } from 'src/app/modules/emissions-reporting/components/control-assignment-table/control-assignment-table.component';
import { ControlPollutantTableComponent } from 'src/app/modules/emissions-reporting/components/control-pollutant-table/control-pollutant-table.component';
import { ControlPathPollutantTableComponent } from 'src/app/modules/emissions-reporting/components/control-path-pollutant-table/control-path-pollutant-table.component';
import { InventoryControlTableComponent } from 'src/app/modules/emissions-reporting/components/inventory-control-table/inventory-control-table.component';
import { ControlPathPanelComponent } from 'src/app/modules/emissions-reporting/components/control-path-panel/control-path-panel.component';
import { EmissionsUnitsSummaryComponent } from 'src/app/modules/emissions-reporting/pages/emissions-units-summary/emissions-units-summary.component';
import { ReleasePointsSummaryComponent } from 'src/app/modules/emissions-reporting/pages/release-points-summary/release-points-summary.component';
import { ControlDevicesSummaryComponent } from 'src/app/modules/emissions-reporting/pages/control-devices-summary/control-devices-summary.component';
import { ReportSummaryComponent } from 'src/app/modules/emissions-reporting/pages/report-summary/report-summary.component';
import { CreateEmissionsProcessComponent } from 'src/app/modules/emissions-reporting/pages/create-emissions-process/create-emissions-process.component';
import { EditProcessInfoPanelComponent } from 'src/app/modules/emissions-reporting/components/edit-process-info-panel/edit-process-info-panel.component';
import { EditProcessOperatingDetailPanelComponent } from 'src/app/modules/emissions-reporting/components/edit-process-operating-detail-panel/edit-process-operating-detail-panel.component';
import { EditProcessReportingPeriodPanelComponent } from 'src/app/modules/emissions-reporting/components/edit-process-reporting-period-panel/edit-process-reporting-period-panel.component';
import { EmissionDetailsComponent } from './pages/emission-details/emission-details.component';
import { EmissionFactorModalComponent } from './components/emission-factor-modal/emission-factor-modal.component';
import { SccSearchModalComponent } from './components/scc-search-modal/scc-search-modal.component';
import { ControlPathTableComponent } from './components/control-path-table/control-path-table.component';
import { EmissionsReportValidationComponent } from './pages/emissions-report-validation/emissions-report-validation.component';
import { EditFacilityContactComponent } from './pages/edit-facility-contact/edit-facility-contact.component';
import { EditEmissionUnitInfoPanelComponent } from './components/edit-emission-unit-info-panel/edit-emission-unit-info-panel.component';
import { CreateEmissionsUnitComponent } from './pages/create-emissions-unit/create-emissions-unit.component';
import { EditReleasePointPanelComponent } from './components/edit-release-point-panel/edit-release-point-panel.component';
import { CreateReleasePointComponent } from './pages/create-release-point/create-release-point.component';
import { ReleasePointApportionmentModalComponent } from './components/release-point-apportionment-modal/release-point-apportionment-modal.component';
import { EditControlDeviceInfoPanelComponent } from './components/edit-control-device-info-panel/edit-control-device-info-panel.component';
import { CreateControlDeviceComponent } from './pages/create-control-device/create-control-device.component';
import { EditFacilitySiteInfoPanelComponent } from './components/edit-facility-site-info-panel/edit-facility-site-info-panel.component';
import { FacilityNaicsTableComponent } from './components/facility-naics-table/facility-naics-table.component';
import { FacilityNaicsModalComponent } from './components/facility-naics-modal/facility-naics-modal.component';
import { ReportHistoryComponent } from './pages/report-history/report-history.component';
import { ControlPollutantModalComponent } from './components/control-pollutant-modal/control-pollutant-modal.component';
import { EmissionsReportContainerComponent } from './pages/emissions-report-container/emissions-report-container.component';
import { ValidationReminderComponent } from './components/validation-reminder/validation-reminder.component';
import { ControlPathsSummaryComponent } from './pages/control-paths-summary/control-paths-summary.component';
import { ControlPathsTableComponent } from './components/control-paths-table/control-paths-table.component';
import { ControlPathDetailsComponent } from './pages/control-path-details/control-path-details.component';
import { EditControlPathInfoPanelComponent } from './components/edit-control-path-info-panel/edit-control-path-info-panel.component';
import { CreateControlPathComponent } from './pages/create-control-path/create-control-path.component';
import { ControlPathAssignmentTableComponent } from './components/control-path-assignment-table/control-path-assignment-table.component';
import { ControlPathAssignmentModalComponent } from './components/control-path-assignment-modal/control-path-assignment-modal.component';
import { ReportBulkUploadComponent } from './pages/report-bulk-upload/report-bulk-upload.component';
import { UserFeedbackComponent } from './pages/user-feedback/user-feedback.component';
import { DataBulkEntryComponent } from './pages/data-bulk-entry/data-bulk-entry.component';
import { BulkEntryReportingPeriodTableComponent } from './components/bulk-entry-reporting-period-table/bulk-entry-reporting-period-table.component';
import { BulkEntryEmissionsTableComponent } from './components/bulk-entry-emissions-table/bulk-entry-emissions-table.component';
import {faBan, faPlus, faQuestionCircle, fas, faUserCircle} from '@fortawesome/free-solid-svg-icons';
import { ThresholdScreeningGadnrModalComponent } from './components/threshold-screening-gadnr-modal/threshold-screening-gadnr-modal.component';
import { MonthlyReportingComponent } from 'src/app/modules/emissions-reporting/pages/monthly-reporting/monthly-reporting.component';
import { MonthlyFuelTableComponent } from 'src/app/modules/emissions-reporting/components/monthly-fuel-table/monthly-fuel-table.component';
import { MonthlyFuelReviewTableComponent } from './components/monthly-fuel-review-table/monthly-fuel-review-table.component';
import { MonthlyReportingPeriodTableComponent } from './components/monthly-reporting-period-table/monthly-reporting-period-table.component';
import { ReportJsonUploadComponent } from './pages/report-json-upload/report-json-upload.component';
import { ReportChangesPanelComponent } from './components/report-changes-panel/report-changes-panel.component';
import { ReportCreationChangesComponent } from './pages/report-creation-changes/report-creation-changes.component';
import { DeleteControlModalComponent } from 'src/app/modules/emissions-reporting/components/delete-control-modal/delete-control-modal.component';
import { ThresholdResetConfirmationModalComponent } from './components/threshold-reset-confirmation-modal/threshold-reset-confirmation-modal.component';
import { ReviewerCommentComponent } from './components/reviewer-comment/reviewer-comment.component';
import { ReviewerCommentsModalComponent } from './components/reviewer-comments-modal/reviewer-comments-modal.component';
import { MonthlyReportingEmissionTableComponent } from './components/monthly-reporting-emission-table/monthly-reporting-emission-table.component';
import { EmissionsReportValidationInfoComponent } from './components/emissions-report-validation-info/emissions-report-validation-info.component';
import { SemiannualReportValidationModalComponent } from './components/semiannual-report-validation-modal/semiannual-report-validation-modal.component';
import { FacilityPermitTableComponent } from './components/facility-permit-table/facility-permit-table.component';
import { EditFacilityOperatingStatusModalComponent } from './components/edit-facility-operating-status-modal/edit-facility-operating-status-modal.component';

@NgModule({
  declarations: [
    EmissionsReportingDashboardComponent,
    EmissionsReportingComponent,
    EmissionUnitDashboardComponent,
    StepProgressComponent,
    EmissionUnitInfoComponent,
    ReleasePointTableComponent,
    EmissionsUnitTableComponent,
    FacilityInformationComponent,
    EmissionsUnitToSideNavPipe,
    ReleasePointDetailsComponent,
    EmissionsProcessTableComponent,
    EmissionsProcessDetailsComponent,
    EmissionTableComponent,
    ReleasePointApptTableComponent,
    ControlDeviceDetailsComponent,
    ControlAssignmentTableComponent,
    ControlPollutantTableComponent,
    ControlPathPollutantTableComponent,
    InventoryControlTableComponent,
    ControlPathPanelComponent,
    EmissionsUnitsSummaryComponent,
    ReleasePointsSummaryComponent,
    ControlDevicesSummaryComponent,
    ReportSummaryComponent,
    CreateEmissionsProcessComponent,
    EditProcessInfoPanelComponent,
    EditProcessOperatingDetailPanelComponent,
    EditProcessReportingPeriodPanelComponent,
    EmissionDetailsComponent,
    EmissionFactorModalComponent,
    SccSearchModalComponent,
    ControlPathTableComponent,
    EmissionsReportValidationComponent,
    EditFacilityContactComponent,
    EditEmissionUnitInfoPanelComponent,
    CreateEmissionsUnitComponent,
    EditReleasePointPanelComponent,
    CreateReleasePointComponent,
    ReleasePointApportionmentModalComponent,
    EditControlDeviceInfoPanelComponent,
    CreateControlDeviceComponent,
    EditFacilitySiteInfoPanelComponent,
    FacilityNaicsTableComponent,
    FacilityNaicsModalComponent,
    ReportHistoryComponent,
    ControlPollutantModalComponent,
    EmissionsReportContainerComponent,
    ValidationReminderComponent,
    ControlPathsSummaryComponent,
    ControlPathsTableComponent,
    ControlPathDetailsComponent,
    EditControlPathInfoPanelComponent,
    CreateControlPathComponent,
    ControlPathAssignmentTableComponent,
    ControlPathAssignmentModalComponent,
    ReportBulkUploadComponent,
    UserFeedbackComponent,
    DataBulkEntryComponent,
    BulkEntryReportingPeriodTableComponent,
    BulkEntryEmissionsTableComponent,
    ThresholdScreeningGadnrModalComponent,
	MonthlyReportingComponent,
	MonthlyFuelReviewTableComponent,
	MonthlyFuelTableComponent,
    MonthlyReportingPeriodTableComponent,
    ReportJsonUploadComponent,
	ReportChangesPanelComponent,
	ReportCreationChangesComponent,
	DeleteControlModalComponent,
    ThresholdResetConfirmationModalComponent,
    ReviewerCommentComponent,
    ReviewerCommentsModalComponent,
    MonthlyReportingEmissionTableComponent,
    EmissionsReportValidationInfoComponent,
    SemiannualReportValidationModalComponent,
    FacilityPermitTableComponent,
    EditFacilityOperatingStatusModalComponent
  ],
  imports: [
    CommonModule,
    ReportsRoutingModule,
    ReactiveFormsModule,
    SharedModule,
    NgbModule,
    FontAwesomeModule,
    FormsModule
  ],
  entryComponents: [
    EmissionFactorModalComponent,
    SccSearchModalComponent,
    ReleasePointApportionmentModalComponent,
    FacilityNaicsModalComponent,
    ControlPollutantModalComponent,
    ControlPathAssignmentModalComponent,
	DeleteControlModalComponent
  ]
})
export class EmissionsReportingModule {
    constructor(config: FaConfig, library: FaIconLibrary) {
        config.fallbackIcon = faBan;
        library.addIconPacks(fas);
      library.addIcons(faUserCircle, faQuestionCircle, faPlus);
    }
}
