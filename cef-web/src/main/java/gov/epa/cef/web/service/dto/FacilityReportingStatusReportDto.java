/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
