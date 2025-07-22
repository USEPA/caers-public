/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.config;

import gov.epa.cef.web.provider.system.IPropertyKey;

public enum SLTPropertyName implements IPropertyKey {

    EmailAddress("slt-email"),
    EmailSLTReadyToCertifyEnabled("slt-email.ready-for-certification.enabled"),
    EisUser("slt-eis-user"),
    EisProgramCode("slt-eis-program-code"),
    FacilityNaicsEnabled("slt-feature.industry-facility-naics.enabled"),
    SLTFeatureSLTEditSccEnabled("slt-feature.slt-edit-process-scc.enabled"),
    SLTFeatureNonPointSccEnabled("slt-feature.non-point-scc.enabled"),
    MonthlyFuelReportingEnabled("slt-feature.monthly-fuel-reporting.enabled"),
    MonthlyFuelReportingInitialYear("slt-feature.monthly-fuel-reporting.initialYear"),
    SLTFeatureAnnouncementEnabled("slt-feature.announcement.enabled"),
    SLTFeatureAnnouncementText("slt-feature.announcement.text"),
    SLTFeatureAttachmentRequired("slt-feature.validation.ef-attachment.required"),
    SLTFeatureBillingExemptEnabled("slt-feature.slt-billing-exempt.enabled"),
    SLTFeatureThresholdScreeningGADNREnabled("slt-feature.gadnr-threshold-screen.enabled"),
    SLTFeatureEmissionFactorCompendiumEnabled("slt-feature.emission-factor-compendium.enabled"),
    ReportAttachmentUploadCSV("report-attachment-upload.csv"),
    ReportAttachmentUploadDOCX("report-attachment-upload.docx"),
    ReportAttachmentUploadPDF("report-attachment-upload.pdf"),
    ReportAttachmentUploadTXT("report-attachment-upload.txt"),
    ReportAttachmentUploadXLSX("report-attachment-upload.xlsx"),
    NewReportCreationEnabled("slt.feature.new-report-creation.enabled");

    private final String key;

    SLTPropertyName(String key) {

        this.key = key;
    }

    @Override
    public String configKey() {

        return this.key;
    }
}
