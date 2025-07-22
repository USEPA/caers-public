/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { SharedModule } from './modules/shared/shared.module';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule, ErrorHandler } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {HttpClientModule, HTTP_INTERCEPTORS, HttpClientJsonpModule, HttpClientXsrfModule} from '@angular/common/http';
import { CommonModule, DatePipe } from '@angular/common';
import { AppComponent } from 'src/app/app.component';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { FacilityDashboardComponent } from 'src/app/modules/dashboards/pages/facility-dashboard/facility-dashboard.component';
import { HeaderComponent } from 'src/app/core/header/header.component';
import { FooterComponent } from 'src/app/core/footer/footer.component';
import { FacilityListComponent } from 'src/app/modules/dashboards/components/facility-list/facility-list.component';
import { NotificationListComponent } from 'src/app/shared/components/notification-list/notification-list.component';
import { FacilityListItemComponent } from 'src/app/modules/dashboards/components/facility-list-item/facility-list-item.component';
import { BreadcrumbNavComponent } from 'src/app/shared/components/breadcrumb-nav/breadcrumb-nav.component';
import { SubmissionReviewDashboardComponent } from 'src/app/modules/dashboards/pages/submission-review-dashboard/submission-review-dashboard.component';
import { SubmissionReviewListComponent } from 'src/app/modules/dashboards/components/submission-review-list/submission-review-list.component';
import { FontAwesomeModule, FaConfig, FaIconLibrary  } from '@fortawesome/angular-fontawesome';
import {faUserCircle, faQuestionCircle, faBan, fas, faPlus} from '@fortawesome/free-solid-svg-icons';
import { RedirectComponent } from 'src/app/modules/dashboards/pages/redirect/redirect.component';
import { GlobalErrorHandlerService } from 'src/app/core/services/global-error-handler.service';
import { HttpErrorInterceptor } from 'src/app/core/interceptors/http-error.interceptor';
import { ReportSummaryModalComponent } from 'src/app/modules/dashboards/components/report-summary-modal/report-summary-modal.component';
import { BusyModalComponent } from './shared/components/busy-modal/busy-modal.component';
import { RejectSubmissionModalComponent } from './modules/dashboards/components/reject-submission-modal/reject-submission-modal.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { TimeoutModalComponent } from './shared/components/timeout-modal/timeout-modal.component';
import {XhrInterceptor} from "./core/interceptors/http-xhr.interceptor";
import { ConfirmationDialogComponent } from './shared/components/confirmation-dialog/confirmation-dialog.component';
import { SubmissionReviewModalComponent } from './modules/dashboards/components/submission-review-modal/submission-review-modal.component';
import { HelpPageComponent } from './modules/dashboards/pages/help-page/help-page.component';
import { AdminPropertiesComponent } from './modules/dashboards/pages/admin-properties/admin-properties.component';
import { AdminAnnouncementPropertiesComponent } from './modules/dashboards/pages/admin-announcement-properties/admin-announcement-properties.component';
import { EisSubmissionComponent } from './modules/dashboards/pages/eis-submission/eis-submission.component';
import { EisTransactionsComponent } from './modules/dashboards/pages/eis-transactions/eis-transactions.component';
import { SortByPipe } from './shared/pipes/sort-by.pipe';
import { DataFilterPipe } from './shared/pipes/data-filter.pipe';
import { RecalculateEmissionTonsModalComponent } from './modules/dashboards/components/recalculate-emission-tons-modal/recalculate-emission-tons-modal.component';
import { SltPropertiesComponent } from 'src/app/modules/dashboards/pages/slt-properties/slt-properties.component';
import { AdminUserFeedbackComponent } from './modules/dashboards/pages/admin-user-feedback/admin-user-feedback.component';
import { UserFeedbackReportModalComponent } from './modules/dashboards/components/user-feedback-report-modal/user-feedback-report-modal.component';
import { MasterFacilityInformationComponent } from './modules/dashboards/pages/master-facility-information/master-facility-information.component';
import { MasterFacilityTableComponent } from './modules/dashboards/components/master-facility-table/master-facility-table.component';
import { MasterFacilityInfoComponent } from './modules/dashboards/components/master-facility-info/master-facility-info.component';
import { UserFacilityAssociationTableComponent } from './modules/dashboards/components/user-facility-association-table/user-facility-association-table.component';
import { EditMasterFacilityInfoComponent } from './modules/dashboards/components/edit-master-facility-info/edit-master-facility-info.component';
import { MasterFacilitySearchComponent } from './modules/dashboards/pages/master-facility-search/master-facility-search.component';
import { MasterFacilitySearchTableComponent } from './modules/dashboards/components/master-facility-search-table/master-facility-search-table.component';
import { PendingUserFacilityAssociationsComponent } from './modules/dashboards/pages/pending-user-facility-associations/pending-user-facility-associations.component';
import { MasterFacilityNaicsTableComponent } from './modules/dashboards/components/master-facility-naics-table/master-facility-naics-table.component';
import { MasterFacilityNaicsModalComponent } from './modules/dashboards/components/master-facility-naics-modal/master-facility-naics-modal.component';
import { SemiAnnualReviewDashboardComponent } from './modules/dashboards/pages/semi-annual-review-dashboard/semi-annual-review-dashboard.component';
import { MonthlySubmissionReviewListComponent } from './modules/dashboards/components/monthly-submission-review-list/monthly-submission-review-list.component';
import { CbiNoticeModalComponent } from './shared/components/cbi-notice-modal/cbi-notice-modal.component';
import { SubmissionReviewModal } from 'src/app/modules/emissions-reporting/components/submission-review-modal/submission-review-modal.component';
import { GenerateReportsComponent } from './modules/dashboards/pages/generate-reports/generate-reports.component';
import { GenerateReportsAdminComponent } from './modules/dashboards/pages/generate-reports-admin/generate-reports-admin.component';
import { EditorModule, TINYMCE_SCRIPT_SRC } from '@tinymce/tinymce-angular';
import { NgxGoogleAnalyticsRouterModule } from "ngx-google-analytics";
import { EmailNotificationsComponent } from './modules/dashboards/pages/email-notifications/email-notifications.component';
import { MasterFacilityPermitTableComponent } from './modules/dashboards/components/master-facility-permit-table/master-facility-permit-table.component';
import { MasterFacilityPermitModalComponent } from './modules/dashboards/components/master-facility-permit-modal/master-facility-permit-modal.component';


@NgModule({
  declarations: [
    AppComponent,
    FacilityDashboardComponent,
    SubmissionReviewDashboardComponent,
    RedirectComponent,
    HeaderComponent,
    FooterComponent,
    FacilityListComponent,
    NotificationListComponent,
    FacilityListItemComponent,
    BreadcrumbNavComponent,
    SubmissionReviewListComponent,
	MonthlySubmissionReviewListComponent,
    ReportSummaryModalComponent,
    BusyModalComponent,
    RejectSubmissionModalComponent,
    TimeoutModalComponent,
    ConfirmationDialogComponent,
    SubmissionReviewModalComponent,
	SemiAnnualReviewDashboardComponent,
    HelpPageComponent,
    AdminPropertiesComponent,
    AdminAnnouncementPropertiesComponent,
    AdminUserFeedbackComponent,
    EisSubmissionComponent,
    EisTransactionsComponent,
    SortByPipe,
    DataFilterPipe,
    RecalculateEmissionTonsModalComponent,
    UserFeedbackReportModalComponent,
    SltPropertiesComponent,
    MasterFacilityInformationComponent,
    MasterFacilityTableComponent,
    MasterFacilityInfoComponent,
    MasterFacilityNaicsTableComponent,
    MasterFacilityNaicsModalComponent,
    UserFacilityAssociationTableComponent,
    EditMasterFacilityInfoComponent,
    MasterFacilitySearchComponent,
    MasterFacilitySearchTableComponent,
    PendingUserFacilityAssociationsComponent,
    CbiNoticeModalComponent,
	SubmissionReviewModal,
	GenerateReportsComponent,
    GenerateReportsAdminComponent,
    EmailNotificationsComponent,
    MasterFacilityPermitTableComponent,
    MasterFacilityPermitModalComponent
  ],
  imports: [
    BrowserModule,
    SharedModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    NgbModule,
    HttpClientModule,
    HttpClientXsrfModule,
    HttpClientJsonpModule,
    AppRoutingModule,
    FontAwesomeModule,
    BrowserAnimationsModule,
	EditorModule,
    ToastrModule.forRoot(),
    NgxGoogleAnalyticsRouterModule
  ],
  entryComponents: [
    ReportSummaryModalComponent,
    BusyModalComponent,
    RejectSubmissionModalComponent,
    MasterFacilityNaicsModalComponent,
    TimeoutModalComponent,
    ConfirmationDialogComponent,
    SubmissionReviewModalComponent,
    RecalculateEmissionTonsModalComponent,
    UserFeedbackReportModalComponent
  ],
  providers: [
      {provide: ErrorHandler, useClass: GlobalErrorHandlerService},
      {provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true},
      {provide: HTTP_INTERCEPTORS, useClass: XhrInterceptor, multi: true},
	  {provide: DatePipe},
      { provide: TINYMCE_SCRIPT_SRC, useValue: 'tinymce/tinymce.min.js' }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
    constructor(config: FaConfig, library: FaIconLibrary) {
        config.fallbackIcon = faBan;
        library.addIconPacks(fas);
        library.addIcons(faUserCircle, faQuestionCircle, faPlus)
      }
}
