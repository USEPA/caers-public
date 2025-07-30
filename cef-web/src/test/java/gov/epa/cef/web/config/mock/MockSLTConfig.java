/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.config.mock;

import gov.epa.cef.web.config.SLTBaseConfig;

public class MockSLTConfig implements SLTBaseConfig {

    private String sltEmail;
    private Boolean sltEmailReadyToCertifyEnabled;
    private String sltEisUser;
    private String sltEisProgramCode;
    private Boolean facilityNaicsEnabled;
    private Boolean editSccEnabled;
    private Boolean nonPointSccEnabled;
    private String sltFeatureAnnouncementText;
    private Boolean sltFeatureAnnouncementEnabled;
    private Boolean sltMonthlyFuelReportingEnabled;
    private Short sltMonthlyFuelReportingInitialYear;
    private Boolean sltFeatureEmissionFactorCompendiumEnabled;
    private Boolean sltFeatureNewReportCreationEnabled;
    private Boolean sltFeatureBillingExemptEnabled;

    public String getSltEmail() {
        return sltEmail;
    }

    public void setSltEmail(String sltEmail) {
        this.sltEmail = sltEmail;
    }

    public Boolean getSLTEmailReadyToCertifyEnabled() { return this.sltEmailReadyToCertifyEnabled; }

    public String getSltEisUser() {
        return sltEisUser;
    }

    public void setSltEisUser(String sltEisUser) {
        this.sltEisUser = sltEisUser;
    }

    public String getSltEisProgramCode() {
        return sltEisProgramCode;
    }

    public void setSltEisProgramCode(String sltEisProgramCode) {
        this.sltEisProgramCode = sltEisProgramCode;
    }

	public Boolean getFacilityNaicsEnabled() {
		return facilityNaicsEnabled;
	}

	public void setFacilityNaicsEnabled(Boolean facilityNaicsEnabled) {
		this.facilityNaicsEnabled = facilityNaicsEnabled;
	}

    public Boolean getEditSccEnabled() {
        return editSccEnabled;
    }

    public void setEditSccEnabled(Boolean editSccEnabled) {
        this.editSccEnabled = editSccEnabled;
    }

    @Override
    public Boolean getNonPointSccEnabled() {
        return nonPointSccEnabled;
    }

    public void setNonPointSccEnabled(Boolean nonPointSccEnabled) {
        this.nonPointSccEnabled = nonPointSccEnabled;
    }

    public Boolean getSltFeatureAnnouncementEnabled() {
		return sltFeatureAnnouncementEnabled;
	}

	public void setSltFeatureAnnouncementEnabled(Boolean sltFeatureAnnouncementEnabled) {
		this.sltFeatureAnnouncementEnabled = sltFeatureAnnouncementEnabled;
	}

	public String getSltFeatureAnnouncementText() {
		return sltFeatureAnnouncementText;
	}

	public void setSltFeatureAnnouncementText(String sltFeatureAnnouncementText) {
		this.sltFeatureAnnouncementText = sltFeatureAnnouncementText;
	}

	public Boolean getSltMonthlyFuelReportingEnabled() {
		return sltMonthlyFuelReportingEnabled;
	}

	public void setSltMonthlyFuelReportingEnabled(Boolean sltMonthlyFuelReportingEnabled) {
		this.sltMonthlyFuelReportingEnabled = sltMonthlyFuelReportingEnabled;
	}

    public Short getSltMonthlyFuelReportingInitialYear() {
        return sltMonthlyFuelReportingInitialYear;
    }

    public void setSltMonthlyFuelReportingInitialYear(Short sltMonthlyFuelReportingInitialYear) {
        this.sltMonthlyFuelReportingInitialYear = sltMonthlyFuelReportingInitialYear;
    }

	public Boolean getSltFeatureEmissionFactorCompendiumEnabled() {
		return sltFeatureEmissionFactorCompendiumEnabled;
	}

	public void setSltFeatureEmissionFactorCompendiumEnabled(Boolean sltFeatureEmissionFactorCompendiumEnabled) {
		this.sltFeatureEmissionFactorCompendiumEnabled = sltFeatureEmissionFactorCompendiumEnabled;
	}

    public Boolean getSltFeatureNewReportCreationEnabled() {
        return sltFeatureNewReportCreationEnabled;
    }

    public void setSltFeatureNewReportCreationEnabled(Boolean sltFeatureNewReportCreationEnabled) {
        this.sltFeatureNewReportCreationEnabled = sltFeatureNewReportCreationEnabled;
    }

    public Boolean getSltFeatureBillingExemptEnabled() {
        return sltFeatureBillingExemptEnabled;
    }

    public void setSltFeatureBillingExemptEnabled(Boolean sltFeatureBillingExemptEnabled) {
        this.sltFeatureBillingExemptEnabled = sltFeatureBillingExemptEnabled;
    }
}
