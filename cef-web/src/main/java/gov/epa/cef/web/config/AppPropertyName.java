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
