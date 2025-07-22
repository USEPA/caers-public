/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
