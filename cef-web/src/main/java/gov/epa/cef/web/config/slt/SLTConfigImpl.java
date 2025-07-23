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
package gov.epa.cef.web.config.slt;

import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.config.SLTPropertyName;
import gov.epa.cef.web.provider.system.SLTPropertyProvider;

public class SLTConfigImpl implements SLTBaseConfig {

    private final String programSystemCode;

    private final SLTPropertyProvider propertyProvider;

    public SLTConfigImpl(String programSystemCode, SLTPropertyProvider propertyProvider) {

        super();

        this.programSystemCode = programSystemCode;
        this.propertyProvider = propertyProvider;

    }

    public String getSltEmail() {
        return this.propertyProvider.getString(SLTPropertyName.EmailAddress, programSystemCode);
    }

    public Boolean getSLTEmailReadyToCertifyEnabled() {
        return this.propertyProvider.getBoolean(SLTPropertyName.EmailSLTReadyToCertifyEnabled, programSystemCode);
    }

    public String getSltEisUser() {
        return this.propertyProvider.getString(SLTPropertyName.EisUser, programSystemCode);
    }

    public String getSltEisProgramCode() {
        return this.propertyProvider.getString(SLTPropertyName.EisProgramCode, programSystemCode);
    }

    public Boolean getFacilityNaicsEnabled() {
    	 return this.propertyProvider.getBoolean(SLTPropertyName.FacilityNaicsEnabled, programSystemCode);
    }

    public Boolean getEditSccEnabled() {
        return this.propertyProvider.getBoolean(SLTPropertyName.SLTFeatureSLTEditSccEnabled, programSystemCode);
    }

    public Boolean getNonPointSccEnabled() {
        return this.propertyProvider.getBoolean(SLTPropertyName.SLTFeatureNonPointSccEnabled, programSystemCode);
    }

    public Boolean getSltFeatureAnnouncementEnabled() {
   	 	 return this.propertyProvider.getBoolean(SLTPropertyName.SLTFeatureAnnouncementEnabled, programSystemCode);
    }

    public String getSltFeatureAnnouncementText() {
        return this.propertyProvider.getString(SLTPropertyName.SLTFeatureAnnouncementText, programSystemCode);
    }

    public Boolean getSltFeatureAttachmentRequired() {
    	return this.propertyProvider.getBoolean(SLTPropertyName.SLTFeatureAttachmentRequired, programSystemCode);
    }

    public Boolean getReportAttachmentUploadCSV() {
  	 	 return this.propertyProvider.getBoolean(SLTPropertyName.ReportAttachmentUploadCSV, programSystemCode);
    }

    public Boolean getReportAttachmentUploadDOCX() {
 	 	 return this.propertyProvider.getBoolean(SLTPropertyName.ReportAttachmentUploadDOCX, programSystemCode);
    }

    public Boolean getReportAttachmentUploadODF() {
 	 	 return this.propertyProvider.getBoolean(SLTPropertyName.ReportAttachmentUploadPDF, programSystemCode);
    }

    public Boolean getReportAttachmentUploadTXT() {
 	 	 return this.propertyProvider.getBoolean(SLTPropertyName.ReportAttachmentUploadTXT, programSystemCode);
    }

    public Boolean getReportAttachmentUploadXLSX() {
 	 	 return this.propertyProvider.getBoolean(SLTPropertyName.ReportAttachmentUploadXLSX, programSystemCode);
    }

    public Boolean getSltMonthlyFuelReportingEnabled() {
        return this.propertyProvider.getBoolean(SLTPropertyName.MonthlyFuelReportingEnabled, programSystemCode);
    }

    public Short getSltMonthlyFuelReportingInitialYear() {
        return this.propertyProvider.getShort(SLTPropertyName.MonthlyFuelReportingInitialYear, programSystemCode);
    }

    public Boolean getSltFeatureEmissionFactorCompendiumEnabled() {
        return this.propertyProvider.getBoolean(SLTPropertyName.SLTFeatureEmissionFactorCompendiumEnabled, programSystemCode);
    }

    public Boolean getSltFeatureNewReportCreationEnabled() {
        return this.propertyProvider.getBoolean(SLTPropertyName.NewReportCreationEnabled, programSystemCode);
    }

    public Boolean getSltFeatureBillingExemptEnabled() {
        return this.propertyProvider.getBoolean(SLTPropertyName.SLTFeatureBillingExemptEnabled, programSystemCode);
    }
}
