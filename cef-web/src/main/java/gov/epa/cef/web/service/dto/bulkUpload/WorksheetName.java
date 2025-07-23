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
package gov.epa.cef.web.service.dto.bulkUpload;

public enum WorksheetName {
    FacilitySite("Facility"),
    EmissionsUnit("Emission Units"),
    ReleasePoint("Release Points"),
    EmissionsProcess("Emission Processes"),
    ReportingPeriod("Reporting Period"),
    Emission("Emissions"),
    ReleasePointAppt("Apportionment"),
    OperatingDetail("Operating Details"),
    ControlPath("Control Paths"),
    Control("Control Devices"),
    ControlAssignment("Control Assignments"),
    ControlPollutant("Control Device Pollutants"),
    ControlPathPollutant("Control Path Pollutants"),
    FacilitySiteContact("Facility Contacts"),
    FacilityNaics("NAICS"),
    EmissionFormulaVariable("Emission Formula Variables"),
    LookupEmissionFactors("EmissionFactor"),
    LookupNaics("NaicsCode"),
    LookupSCCs("SourceClassificationCode"),
    LookupPollutant("Pollutant"),
    Version("Version");

    private final String sheetName;

    WorksheetName(String sheetName) {

        this.sheetName = sheetName;
    }

    public String sheetName() {

        return this.sheetName;
    }
}
