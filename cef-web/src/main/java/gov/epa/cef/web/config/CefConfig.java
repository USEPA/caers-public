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

import gov.epa.cef.web.provider.system.AdminPropertyProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CefConfig {

    @Autowired
    protected CdxConfig cdxConfig;

    @Autowired
    protected AdminPropertyProvider propertyProvider;

    @Autowired
    protected Environment environment;

    public List<String> getAdminEmails() {

        return this.propertyProvider.getStringList(AppPropertyName.AdminEmailAddresses);
    }

    public CdxConfig getCdxConfig() {
        return cdxConfig;
    }

    public String getDefaultEmailAddress() {
        return this.propertyProvider.getString(AppPropertyName.DefaultEmailAddress);
    }

    public BigDecimal getEmissionsTotalErrorTolerance() {
        return this.propertyProvider.getBigDecimal(AppPropertyName.EmissionsTotalErrorTolerance);
    }

    public BigDecimal getEmissionsTotalWarningTolerance() {
        return this.propertyProvider.getBigDecimal(AppPropertyName.EmissionsTotalWarningTolerance);
    }

    public boolean getFeatureBulkEntryEnabled() {
        return this.propertyProvider.getBoolean(AppPropertyName.FeatureBulkEntryEnabled);
    }

    public boolean getFeatureUserFeedbackEnabled() {
        return this.propertyProvider.getBoolean(AppPropertyName.FeatureUserFeedbackEnabled);
    }

    public boolean FeatureFacilityAutomatedEmailEnabled() {
        return this.propertyProvider.getBoolean(AppPropertyName.FeatureFacilityAutomatedEmailEnabled);
    }

    public String getLastSccUpdateDate() {
        return this.propertyProvider.getString(AppPropertyName.LastSccUpdateDate);
    }

    public String getSccUpdateTaskCron() {
        return this.propertyProvider.getString(AppPropertyName.SccUpdateTaskCron);
    }

    public boolean getSccUpdateTaskEnabled() {
        return this.propertyProvider.getBoolean(AppPropertyName.SccUpdateTaskEnabled);
    }

    public String getLastRemoveNullUsersDate() {
        return this.propertyProvider.getString(AppPropertyName.LastRemoveNullUsersDate);
    }

    public String getRemoveNullUsersTaskCron() {
        return this.propertyProvider.getString(AppPropertyName.RemoveNullUsersTaskCron);
    }

    public boolean getRemoveNullUsersTaskEnabled() {
        return this.propertyProvider.getBoolean(AppPropertyName.RemoveNullUsersTaskEnabled);
    }

    public String getLastDeleteReportsDate() {
        return this.propertyProvider.getString(AppPropertyName.LastDeleteReportsDate);
    }

    public String getDeleteReportsTaskCron() {
        return this.propertyProvider.getString(AppPropertyName.DeleteReportsTaskCron);
    }

    public boolean getDeleteReportsTaskEnabled() {
        return this.propertyProvider.getBoolean(AppPropertyName.DeleteReportsTaskEnabled);
    }

    public boolean getReportCertificationEnabled() {
        return this.propertyProvider.getBoolean(AppPropertyName.ReportCertificationEnabled);
    }

    public String getMaxFileSize() {
    	return environment.getProperty("spring.servlet.multipart.max-file-size");
    }

}
