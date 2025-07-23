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
