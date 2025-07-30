/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.config;

import gov.epa.cef.web.provider.system.IPropertyKey;

public enum AppPropertyName implements IPropertyKey {

    AdminEmailAddresses("email.admin"),
    AdminEmailEnabled("email.admin.enabled"),
    DefaultEmailAddress("email.default"),
    EmissionsTotalErrorTolerance("emissions.tolerance.total.error"),
    EmissionsTotalWarningTolerance("emissions.tolerance.total.warning"),
    FeatureAnnouncementEnabled("feature.announcement.enabled"),
    FeatureAnnouncementText("feature.announcement.text"),
    FeatureBulkEntryEnabled("feature.bulk-entry.enabled"),
    FeatureCdxAssociationMigrationEnabled("feature.cdx-association-migration.enabled"),
    FeatureExcelExportEnabled("feature.excel-export.enabled"),
    FeatureUserFeedbackEnabled("feature.user-feedback.enabled"),
    FeatureFacilityAutomatedEmailEnabled("feature.facility-automated-email.enabled"),
    LastSccUpdateDate("task.scc-update.last-ran"),
    SccUpdateTaskCron("task.scc-update.cron"),
    SccUpdateTaskEnabled("task.scc-update.enabled"),
    ReportCertificationEnabled("report-certification.enabled"),
	NewReportCreationEnabled("new-report-creation.enabled"),
    LastRemoveNullUsersDate("task.remove-null-users.last-ran"),
    RemoveNullUsersTaskCron("task.remove-null-users.cron"),
    RemoveNullUsersTaskEnabled("task.remove-null-users.enabled"),
    LastDeleteReportsDate("task.delete-reports.last-ran"),
    DeleteReportsTaskCron("task.delete-reports.cron"),
    DeleteReportsTaskEnabled("task.delete-reports.enabled");

    private final String key;

    AppPropertyName(String key) {

        this.key = key;
    }

    @Override
    public String configKey() {

        return this.key;
    }
}
