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
package gov.epa.cef.web.domain;

public enum CdxInboxMessageType {
    REPORT_SUBMITTED("Submitted - {0} Annual Emissions Report for {1}; Agency ID {2}", "reportSubmitted"),
    REPORT_REJECTED("Returned - {0} Annual Emissions Report for {1}", "reportRejected"),
    REPORT_ACCEPTED("Accepted - {0} Annual Emissions Report for {1}", "reportAccepted"),
    REPORT_ADVANCED_QA("In Review - {0} Annual Emissions Report for {1}", "reportAdvancedQA"),
    FACILITY_ASSOCIATION_APPROVED("Your request to access the {0} facility in the Combined Air Emissions Reporting System has been approved", "userAssociationAccepted"),
    FACILITY_ASSOCIATION_REJECTED("Your request to access the {0} facility in the Combined Air Emissions Reporting System has been rejected", "userAssociationRejected"),
    FACILITY_ASSOCIATION_REMOVED("Your access to the {0} facility in the Combined Air Emissions Reporting System has been removed", "userAssociationRemoved"),
    REPORT_READY_FOR_CERT_SUBJECT("Ready For Certification - {0} Annual Emissions Report for {1}", "reportReadyForCertification"),
    SEMIANNUAL_REPORT_SUBMITTED("Submitted - {0} Semi-Annual Emissions Report for {1}; Agency ID {2}", "midYearReportSubmitted"),
    SEMIANNUAL_REPORT_REJECTED("Returned - {0} Semi-Annual Emissions Report for {1}", "midYearReportRejected"),
    SEMIANNUAL_REPORT_ACCEPTED("Accepted - {0} Semi-Annual Emissions Report for {1}", "midYearReportAccepted"),
    SEMIANNUAL_REPORT_READY_FOR_CERT_SUBJECT("Ready For Certification - {0} Semi-Annual Emissions Report for {1}", "midYearReportReadyForCertification");

	private final String subject;
    private final String template;

    CdxInboxMessageType(String subject, String template) {

        this.subject = subject;
        this.template = template;
    }

    public String template() {
        return this.template;
    }

    public String subject() {
        return this.subject;
    }
}
