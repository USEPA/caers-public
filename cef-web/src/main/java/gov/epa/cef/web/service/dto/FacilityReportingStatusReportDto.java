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
package gov.epa.cef.web.service.dto;

import gov.epa.cef.web.annotation.CsvColumn;
import gov.epa.cef.web.annotation.CsvFileName;

import java.io.Serializable;

/**
 * @author ahmahfou
 *
 */
@CsvFileName(name = "facility_reporting_status.csv")
public class FacilityReportingStatusReportDto implements Serializable{

    /**
     * default version
     */
    private static final long serialVersionUID = 1L;

    private String agencyFacilityIdentifier;
    private String facilityName;
    private String operatingStatus;
    private String industrySector;
    private Short submittalYear;
    private Short lastSubmittalYear;
    private String thresholdStatus;
    private String reportStatus;
    private String eisTransmissionStatus;

    @CsvColumn(name = "Agency Facility ID", order = 1)
    public String getAgencyFacilityIdentifier() {
        return agencyFacilityIdentifier;
    }

    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }

    @CsvColumn(name = "Facility Name", order = 2)
    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    @CsvColumn(name = "Operating Status", order = 3)
    public String getOperatingStatus() {
        return operatingStatus;
    }

    public void setOperatingStatus(String operatingStatus) {
        this.operatingStatus = operatingStatus;
    }

    @CsvColumn(name = "Industry Sector", order = 4)
    public String getIndustrySector() {
        return industrySector;
    }

    public void setIndustrySector(String industrySector) {
        this.industrySector = industrySector;
    }

    @CsvColumn(name = "Submittal Year", order = 5)
    public Short getSubmittalYear() {
        return submittalYear;
    }

    public void setSubmittalYear(Short submittalYear) {
        this.submittalYear = submittalYear;
    }

    @CsvColumn(name = "Last Submittal Year", order = 6)
    public Short getLastSubmittalYear() {
        return lastSubmittalYear;
    }

    public void setLastSubmittalYear(Short lastSubmittalYear) {
        this.lastSubmittalYear = lastSubmittalYear;
    }

    @CsvColumn(name = "Opt In/Out Status", order = 7)
    public String getThresholdStatus() {
        return thresholdStatus;
    }

    public void setThresholdStatus(String thresholdStatus) {
        this.thresholdStatus = thresholdStatus;
    }

    @CsvColumn(name = "Report Submission Status", order = 8)
    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    @CsvColumn(name = "EIS Transmission Status", order = 9)
    public String getEisTransmissionStatus() {
        return eisTransmissionStatus;
    }

    public void setEisTransmissionStatus(String eisTransmissionStatus) {
        this.eisTransmissionStatus = eisTransmissionStatus;
    }
}
