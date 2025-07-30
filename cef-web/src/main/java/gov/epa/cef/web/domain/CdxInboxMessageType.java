/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
