/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.dto;

import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.FacilitySite;

import java.util.function.Function;

public class EisDataReportDto {

    private String agencyFacilityIdentifier;

    private EisTransactionAttachmentDto attachment;

    private String comments;

    private String eisProgramId;

    private long emissionsReportId;

    private String facilityName;

    private EisSubmissionStatus lastSubmissionStatus;

    private String lastTransactionId;

    private boolean passed;

    private int reportingYear;

    public String getAgencyFacilityIdentifier() {

        return agencyFacilityIdentifier;
    }

    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {

        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }

    public EisTransactionAttachmentDto getAttachment() {
        return attachment;
    }

    public void setAttachment(EisTransactionAttachmentDto attachment) {
        this.attachment = attachment;
    }

    public String getComments() {

        return comments;
    }

    public void setComments(String comments) {

        this.comments = comments;
    }

    public String getEisProgramId() {

        return eisProgramId;
    }

    public void setEisProgramId(String eisProgramId) {

        this.eisProgramId = eisProgramId;
    }

    public long getEmissionsReportId() {

        return emissionsReportId;
    }

    public void setEmissionsReportId(long emissionsReportId) {

        this.emissionsReportId = emissionsReportId;
    }

    public String getFacilityName() {

        return facilityName;
    }

    public void setFacilityName(String facilityName) {

        this.facilityName = facilityName;
    }

    public EisSubmissionStatus getLastSubmissionStatus() {

        return lastSubmissionStatus;
    }

    public void setLastSubmissionStatus(EisSubmissionStatus lastSubmissionStatus) {

        this.lastSubmissionStatus = lastSubmissionStatus;
    }

    public String getLastTransactionId() {

        return lastTransactionId;
    }

    public void setLastTransactionId(String lastTransactionId) {

        this.lastTransactionId = lastTransactionId;
    }

    public int getReportingYear() {

        return reportingYear;
    }

    public void setReportingYear(int reportingYear) {

        this.reportingYear = reportingYear;
    }

    public boolean isPassed() {

        return passed;
    }

    public void setPassed(boolean passed) {

        this.passed = passed;
    }

    public EisDataReportDto withAgencyFacilityIdentifier(final String agencyFacilityIdentifier) {

        setAgencyFacilityIdentifier(agencyFacilityIdentifier);
        return this;
    }

    public EisDataReportDto withComments(final String comments) {

        setComments(comments);
        return this;
    }

    public EisDataReportDto withEisProgramId(final String eisProgramId) {

        setEisProgramId(eisProgramId);
        return this;
    }

    public EisDataReportDto withEmissionsReportId(final long emissionsReportId) {

        setEmissionsReportId(emissionsReportId);
        return this;
    }

    public EisDataReportDto withFacilityName(final String facilityName) {

        setFacilityName(facilityName);
        return this;
    }

    public EisDataReportDto withLastSubmissionStatus(final EisSubmissionStatus lastSubmissionStatus) {

        setLastSubmissionStatus(lastSubmissionStatus);
        return this;
    }

    public EisDataReportDto withLastTransactionId(final String lastTransactionId) {

        setLastTransactionId(lastTransactionId);
        return this;
    }

    public EisDataReportDto withPassed(final boolean passed) {

        setPassed(passed);
        return this;
    }

    public EisDataReportDto withReportingYear(final int reportingYear) {

        setReportingYear(reportingYear);
        return this;
    }

    public static class FromEntity implements Function<EmissionsReport, EisDataReportDto> {

        @Override
        public EisDataReportDto apply(EmissionsReport report) {

            FacilitySite facilitySite = report.getFacilitySites().get(0);

            return new EisDataReportDto()
                .withEmissionsReportId(report.getId())
                .withFacilityName(facilitySite.getName())
                .withAgencyFacilityIdentifier(facilitySite.getAgencyFacilityIdentifier())
                .withEisProgramId(report.getEisProgramId())
                .withReportingYear(report.getYear())
                .withLastSubmissionStatus(report.getEisLastSubmissionStatus())
                .withLastTransactionId(report.getEisLastTransactionId())
                .withPassed(report.isEisPassed())
                .withComments(report.getEisComments());
        }
    }
}
