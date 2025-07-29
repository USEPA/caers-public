/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import gov.epa.cef.web.domain.Communication;
import gov.epa.cef.web.service.dto.UserFeedbackDto;
import gov.epa.cef.web.domain.CdxInboxMessageType;

public interface NotificationService {

    Boolean sendReportReadyForCertificationNotification(String to, String cc, String from, String facilityName, String reportingYear);
    void sendReportSubmittedNotification(String to, String cc, String from, String facilityName, String agencyFacilityId, String reportingYear, String slt, String sltEmail, String cdxSubmissionUrl);
    void sendReportRejectedNotification(String to, String cc, String from, String facilityName, String reportingYear, String comments, Long attachmentId, String slt, String sltEmail);
    void sendReportAcceptedNotification(String to, String cc, String from, String facilityName, String reportingYear, String comments, String slt, String sltEmail, Boolean isOptOut);
    void sendReportAdvancedQANotification(String to, String cc, String from, String facilityName, String reportingYear, String slt, String sltEmail);
    void sendSccUpdateFailedNotification(Exception exception);
    void sendRemoveNullUsersFailedNotification(Exception exception);
    void sendUserAccessRequestNotification(String to, String from, String facilityName, String agencyFacilityIdentifier, String userName, String userEmail, String role);
    void sendUserAssociationAcceptedNotification(String to, String cc, String from, String facilityName, String role);
    void sendUserAssociationRejectedNotification(String to, String cc, String from, String facilityName, String role, String comments);
    void sendUserAccessRemovedNotification(String to, String cc, String from, String facilityName, String role, String slt, String sltEmail);
    void sendUserFeedbackNotification(UserFeedbackDto userFeedback);
    void sendSLTNotification(String bcc, String from, String sltEmail, Communication communication, MultipartFile file);
    void sendMidYearReportSubmittedNotification(String to, String cc, String from, String facilityName, String agencyFacilityId, String reportingYear, String slt, String sltEmail, String cdxSubmissionUrl);
    void sendMidYearReportRejectedNotification(String to, String cc, String from, String facilityName, String reportingYear, String comments, String slt, String sltEmail);
    void sendMidYearReportAcceptedNotification(String to, String cc, String from, String facilityName, String reportingYear, String comments, String slt, String sltEmail);
    Boolean sendMidYearReportReadyForCertificationNotification(String to, String cc, String from, String facilityName, String reportingYear);

    void sendAdminNotification(AdminEmailType type, Map<String, Object> context);

    void sendCdxInboxMessage(CdxInboxMessageType type, String cdxUserId, String from, Map<String, Object> variables);

    enum AdminEmailType {

        AdminTest("CAER CEF Admin Email Test", "adminTest"),
        VirusScanFailure("Virus Scanner Web Service Failed", "virusScanFailed");

        private final String subject;
        private final String template;

        AdminEmailType(String subject, String template) {

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

}
