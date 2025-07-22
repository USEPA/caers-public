/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
