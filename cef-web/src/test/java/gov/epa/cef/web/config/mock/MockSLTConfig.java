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
