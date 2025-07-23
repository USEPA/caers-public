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
