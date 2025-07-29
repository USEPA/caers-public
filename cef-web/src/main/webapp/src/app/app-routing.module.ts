/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {FacilityDashboardComponent} from 'src/app/modules/dashboards/pages/facility-dashboard/facility-dashboard.component';
import {SubmissionReviewDashboardComponent} from 'src/app/modules/dashboards/pages/submission-review-dashboard/submission-review-dashboard.component';
import {EisSubmissionComponent} from 'src/app/modules/dashboards/pages/eis-submission/eis-submission.component';
import {FacilityResolverService} from 'src/app/core/services/facility-resolver.service';
import {RedirectComponent} from 'src/app/modules/dashboards/pages/redirect/redirect.component';
import {ErrorComponent} from 'src/app/modules/shared/pages/error/error.component';
import {BulkUploadComponent} from 'src/app/modules/shared/pages/bulk-upload/bulk-upload.component';
import {HelpPageComponent} from 'src/app/modules/dashboards/pages/help-page/help-page.component';
import {AdminPropertiesComponent} from 'src/app/modules/dashboards/pages/admin-properties/admin-properties.component';
import {AdminAnnouncementPropertiesComponent} from 'src/app/modules/dashboards/pages/admin-announcement-properties/admin-announcement-properties.component';
import {AdminAuthGuard} from 'src/app/core/guards/admin-auth.guard';
import {ReviewerAuthGuard} from 'src/app/core/guards/reviewer-auth.guard';
import {EisTransactionsComponent} from 'src/app/modules/dashboards/pages/eis-transactions/eis-transactions.component';
import {SltPropertiesComponent} from 'src/app/modules/dashboards/pages/slt-properties/slt-properties.component';
import {AdminUserFeedbackComponent} from './modules/dashboards/pages/admin-user-feedback/admin-user-feedback.component';
import {MasterFacilityInformationComponent} from 'src/app/modules/dashboards/pages/master-facility-information/master-facility-information.component';
import {MasterFacilitySearchComponent} from 'src/app/modules/dashboards/pages/master-facility-search/master-facility-search.component';
import {PendingUserFacilityAssociationsComponent} from 'src/app/modules/dashboards/pages/pending-user-facility-associations/pending-user-facility-associations.component';
import { SemiAnnualReviewDashboardComponent } from './modules/dashboards/pages/semi-annual-review-dashboard/semi-annual-review-dashboard.component';
import { GenerateReportsComponent } from './modules/dashboards/pages/generate-reports/generate-reports.component';
import { GenerateReportsAdminComponent } from './modules/dashboards/pages/generate-reports-admin/generate-reports-admin.component';
import {EmailNotificationsComponent} from 'src/app/modules/dashboards/pages/email-notifications/email-notifications.component';

const routes: Routes = [
  { path: '', component: RedirectComponent, data: { title: 'Redirect Page' } },
  {
    path: 'facility',
    children: [
      {
        path: 'search',
        component: MasterFacilitySearchComponent,
        data: { title: 'Facility Search', breadcrumb: 'Facility Search' },
      }, {
        path: ':facilityId',
        resolve: {
          facility: FacilityResolverService
        },
        children: [
          {
            path: 'report',
            loadChildren: () => import('src/app/modules/emissions-reporting/emissions-reporting.module').then(m => m.EmissionsReportingModule)
          }
        ]
      }, {
        path: '',
        component: FacilityDashboardComponent,
        data: { title: 'Facility Dashboard' },
      }
    ]
  },
  {
    path: 'admin',
    canActivate: [AdminAuthGuard],
    children: [
      {
        path: 'properties',
        component: AdminPropertiesComponent,
        data: { title: 'Admin Properties' },
      }, {
        path: 'announcement',
        component: AdminAnnouncementPropertiesComponent,
        data: { title: 'Announcement Banner' },
      }, {
        path: 'upload',
        component: BulkUploadComponent,
        data: { title: 'Bulk Upload' },
      }, {
      path: 'facility',
      component: MasterFacilityInformationComponent,
      data: { title: 'Facility Information' },
      }, {
        path: 'userFeedback',
        component: AdminUserFeedbackComponent,
        data: { title: 'Admin User Feedback' },
      }, {
        path: 'adminReports',
        component: GenerateReportsAdminComponent,
        data: { title: 'Generate Reports' },
      }, {
        path: '',
        redirectTo: 'properties',
        pathMatch: 'full'
      }
    ]
  },
  {
    path: 'reviewer',
    canActivateChild: [ReviewerAuthGuard],
    children: [
      {
		path: 'dashboard',
		children: [
		  {
			path: 'semiAnnualDashboard',
	        component: SemiAnnualReviewDashboardComponent,
	        data: { title: 'Monthly Reporting Review Dashboard' },
	      }, {
			path: '',
	        component: SubmissionReviewDashboardComponent,
	        data: { title: 'Annual Reporting Review Dashboard' },
	      }
		]
        }, {
        path: 'facility',
        children: [
          {
            path: 'pendingAssociations',
            component: PendingUserFacilityAssociationsComponent,
            data: { title: 'Pending Authorization Requests' },
          }, {
            path: '',
            component: MasterFacilityInformationComponent,
            data: { title: 'Facility Information' },
          }, {
            path: 'emailNotifications',
            component: EmailNotificationsComponent,
            data: { title: 'Email Notifications' },
          }
        ]
      }, {
        path: 'eisSubmitData',
        component: EisSubmissionComponent,
        data: { title: 'Submit Data' },
      }, {
        path: 'eisTransactions',
        component: EisTransactionsComponent,
        data: { title: 'Review Transactions' },
      }, {
        path: 'properties',
        component: SltPropertiesComponent,
        data: { title: 'Agency Administration' },
      },  {
        path: 'reports',
        component: GenerateReportsComponent,
        data: { title: 'Generate Reports' },
      }, {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }
    ]
  },
  { path: 'helpPage', component: HelpPageComponent, data: { title: 'Help Page' } },
  { path: 'upload', redirectTo: 'admin/upload', pathMatch: 'full' },
  { path: 'error', component: ErrorComponent, data: { title: 'Error Page' } },
  { path: '*', component: RedirectComponent, data: { title: 'Redirect Page' } }
];

@NgModule({
  // useHash is required for spring and inheritanceStrategy allows children to reference data from parents
  imports: [ RouterModule.forRoot(routes, { useHash: true, paramsInheritanceStrategy: 'always', relativeLinkResolution: 'legacy' }) ],
  exports: [ RouterModule ]
})

export class AppRoutingModule {}
