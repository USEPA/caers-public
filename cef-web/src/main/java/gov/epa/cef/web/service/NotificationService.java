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
