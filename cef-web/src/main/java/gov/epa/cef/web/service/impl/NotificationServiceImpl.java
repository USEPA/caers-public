/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.impl;

import gov.epa.cef.web.config.AppPropertyName;
import gov.epa.cef.web.domain.Communication;
import gov.epa.cef.web.domain.Attachment;
import gov.epa.cef.web.domain.CdxInboxMessageType;
import gov.epa.cef.web.exception.NotExistException;
import gov.epa.cef.web.provider.system.AdminPropertyProvider;
import gov.epa.cef.web.repository.AttachmentRepository;
import gov.epa.cef.web.service.NotificationService;
import gov.epa.cef.web.service.dto.UserFeedbackDto;
import gov.epa.cef.web.client.soap.InboxClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.InternetAddress;

@Service
public class NotificationServiceImpl implements NotificationService {

    Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);


    private final String REPORT_READY_FOR_CERT_SUBJECT = "Ready For Certification - {0} Annual Emissions Report for {1}";
    private final String REPORT_READY_FOR_CERT_BODY_TEMPLATE = "reportReadyForCertification";

    private final String REPORT_SUBMITTED_BY_CERT_SUBJECT = "Submitted - {0} Annual Emissions Report for {1}; Agency ID {2}";
    private final String REPORT_SUBMITTED_BY_CERT_BODY_TEMPLATE = "reportSubmitted";

    private final String REPORT_REJECTED_BY_SLT_SUBJECT = "Returned - {0} Annual Emissions Report for {1}";
    private final String REPORT_REJECTED_BY_SLT_BODY_TEMPLATE = "reportRejected";

    private final String REPORT_ACCEPTED_BY_SLT_SUBJECT = "Accepted - {0} Annual Emissions Report for {1}";
    private final String REPORT_ACCEPTED_BY_SLT_BODY_TEMPLATE = "reportAccepted";

    private final String MID_YEAR_REPORT_READY_FOR_CERT_SUBJECT = "Ready For Certification - {0} Semi-Annual Report for {1}";
    private final String MID_YEAR_REPORT_READY_FOR_CERT_BODY_TEMPLATE = "midYearReportReadyForCertification";

    private final String MID_YEAR_REPORT_SUBMITTED_BY_CERT_SUBJECT = "Submitted - {0} Semi-Annual Report for {1}; Agency ID {2}";
    private final String MID_YEAR_REPORT_SUBMITTED_BY_CERT_BODY_TEMPLATE = "midYearReportSubmitted";

    private final String MID_YEAR_REPORT_REJECTED_BY_SLT_SUBJECT = "Returned - {0} Semi-Annual Report for {1}";
    private final String MID_YEAR_REPORT_REJECTED_BY_SLT_BODY_TEMPLATE = "midYearReportRejected";

    private final String MID_YEAR_REPORT_ACCEPTED_BY_SLT_SUBJECT = "Accepted - {0} Semi-Annual Report for {1}";
    private final String MID_YEAR_REPORT_ACCEPTED_BY_SLT_BODY_TEMPLATE = "midYearReportAccepted";

    private final String REPORT_BEGIN_ADVANCED_QA_BY_SLT_SUBJECT = "In Review - {0} Annual Emissions Report for {1}";
    private final String REPORT_BEGIN_ADVANCED_QA_BY_SLT_BODY_TEMPLATE = "reportAdvancedQA";

    private final String SCC_UPDATE_FAILED_SUBJECT = "SCC Update Task Failed";
    private final String SCC_UPDATE_FAILED_BODY_TEMPLATE = "sccUpdateFailed";

    private final String REMOVE_NULL_USERS_FAILED_SUBJECT = "Remove Null Users Task Failed";
    private final String REMOVE_NULL_USERS_FAILED_BODY_TEMPLATE = "removeNullUsersFailed";

    private final String USER_ACCESS_REQUEST_SUBJECT = "User {0} has requested access to facility {1}";
    private final String USER_ACCESS_REQUEST_BODY_TEMPLATE = "userAccessRequest";

    private final String USER_ASSOCIATION_ACCEPTED_SUBJECT = "Your request to access the {0} facility in the Combined Air Emissions Reporting System has been approved";
    private final String USER_ASSOCIATION_ACCEPTED_BODY_TEMPLATE = "userAssociationAccepted";

    private final String USER_ASSOCIATION_REJECTED_SUBJECT = "Your request to access the {0} facility in the Combined Air Emissions Reporting System has been rejected";
    private final String USER_ASSOCIATION_REJECTED_BODY_TEMPLATE = "userAssociationRejected";

    private final String USER_ASSOCIATION_REMOVED_SUBJECT = "Your access to the {0} facility in the Combined Air Emissions Reporting System has been removed";
    private final String USER_ASSOCIATION_REMOVED_BODY_TEMPLATE = "userAssociationRemoved";

    private final String USER_FEEDBACK_SUBMITTED_SUBJECT = "User feedback Submitted for {0} {1}";
    private final String USER_FEEDBACK_SUBMITTED_BODY_TEMPLATE = "userFeedback";

    private final String SLT_NOTIFICATION_GENERIC_BODY_TEMPLATE = "sltNotification";

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    public InboxClient inboxClient;

    //note: Spring and Thymeleaf are "auto-configured" based on the spring-boot-starter-thymeleaf dependency in the pom.xml file
    //Spring/Thymeleaf will automatically assume that template files are located in the resources/templates folder and end in .html
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private AdminPropertyProvider propertyProvider;

    @Autowired
    private AttachmentRepository attachmentsRepo;

    /**
     * Utility method to send a simple email message in plain text.
     *
     * @param to The recipient of the email
     * @param from The sender of the email
     * @param subject The subject of the email
     * @param body text of the email
     */
    private void sendSimpleMessage(String to, String from, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            emailSender.send(message);
        } catch (MailException e) {
            logger.error("sendSimpleMessage - unable to send email message. - {}", e.getMessage());
        }
    }

    public void sendAdminNotification(AdminEmailType type, Map<String, Object> variables) {

        Context context = new Context();
        context.setVariables(variables);
        String emailBody = this.templateEngine.process(type.template(), context);

        sendAdminEmail(type.subject(), emailBody);
    }

    public Boolean sendHtmlMessage(String to, String cc, String from, String subject, String body) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(InternetAddress.parse(to));
            messageHelper.setFrom(from);
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);
            if (cc != null) {
            	messageHelper.setCc(InternetAddress.parse(cc));
            }
        };
        try {
            logger.info("Attempting to send message: " + subject + "\n" +
                        "To: " + to + "\n" +
                        "CC: " + cc);
        	emailSender.send(messagePreparator);
            logger.info("Successfully sent message: " + subject);
            return true;
        } catch (MailException e) {
        	logger.error("sendHTMLMessage - unable to send email message. - {}", e.getMessage());
            return false;
        }
    }

    public void sendMassHtmlMessage(String bcc, String cc, String from, String subject, String body,
    		MultipartFile file) {
    	MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(from);
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);
            if (cc != null) {
            	messageHelper.setCc(cc);
            }
            if (bcc != null) {
            	messageHelper.setBcc(InternetAddress.parse(bcc));
            }

            if (file != null && !file.isEmpty()) {
            	String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            	InputStreamSource source = new InputStreamSource() {

    				@Override
    				public InputStream getInputStream() throws IOException {
    					return file.getInputStream();
    				}
            	};
            	messageHelper.addAttachment(fileName, source);
            }
        };
        try {
        	emailSender.send(messagePreparator);
        } catch (MailException e) {
        	logger.error("sendMassHTMLMessage - unable to send email message. - {}", e.getMessage());
        }
    }

    public void sendHtmlMessage(String to, String from, String subject, String body) {
    	sendHtmlMessage(to, null, from, subject, body);
    }

    private void sendAdminEmail(String from, String subject, String body) {
        if (this.propertyProvider.getBoolean(AppPropertyName.AdminEmailEnabled)) {
            this.propertyProvider.getStringList(AppPropertyName.AdminEmailAddresses).forEach(email -> {
                sendHtmlMessage(email, from, subject, body);
            });
        } else {
            logger.info("Admin email not sent because Admin emails are disabled.");
        }
    }

    private void sendAdminEmail(String subject, String body) {
        sendAdminEmail(this.propertyProvider.getString(AppPropertyName.DefaultEmailAddress), subject, body);
    }

    public Boolean sendReportReadyForCertificationNotification(String to, String cc, String from, String facilityName, String reportingYear) {
        String emailSubject = MessageFormat.format(REPORT_READY_FOR_CERT_SUBJECT, reportingYear, facilityName);
        Context context = new Context();
        context.setVariable("reportingYear", reportingYear);
        context.setVariable("facilityName", facilityName);

        String emailBody = templateEngine.process(REPORT_READY_FOR_CERT_BODY_TEMPLATE, context);
        return sendHtmlMessage(to, cc, from, emailSubject, emailBody);
    }

    public Boolean sendMidYearReportReadyForCertificationNotification(String to, String cc, String from, String facilityName, String reportingYear) {
        String emailSubject = MessageFormat.format(MID_YEAR_REPORT_READY_FOR_CERT_SUBJECT, reportingYear, facilityName);
        Context context = new Context();
        context.setVariable("reportingYear", reportingYear);
        context.setVariable("facilityName", facilityName);

        String emailBody = templateEngine.process(MID_YEAR_REPORT_READY_FOR_CERT_BODY_TEMPLATE, context);
        return sendHtmlMessage(to, cc, from, emailSubject, emailBody);
    }

    public void sendReportSubmittedNotification(
      String to, String cc, String from, String facilityName, String agencyFacilityId, String reportingYear,
      String slt, String sltEmail, String cdxSubmissionUrl
    ) {
    	String emailSubject = MessageFormat.format(REPORT_SUBMITTED_BY_CERT_SUBJECT, reportingYear, facilityName, agencyFacilityId);
    	Context context = new Context();
    	context.setVariable("reportingYear", reportingYear);
      context.setVariable("facilityName", facilityName);
      context.setVariable("agencyFacilityId", agencyFacilityId);
      context.setVariable("sltEmail", sltEmail);
      context.setVariable("slt", slt);
      context.setVariable("cdxSubmissionUrl", cdxSubmissionUrl);

      String emailBody = templateEngine.process(REPORT_SUBMITTED_BY_CERT_BODY_TEMPLATE, context);
      sendHtmlMessage(to, cc, from, emailSubject, emailBody);
    }

    public void sendMidYearReportSubmittedNotification(
      String to, String cc, String from, String facilityName, String agencyFacilityId, String reportingYear,
      String slt, String sltEmail, String cdxSubmissionUrl
    ) {
    	String emailSubject = MessageFormat.format(MID_YEAR_REPORT_SUBMITTED_BY_CERT_SUBJECT, reportingYear, facilityName, agencyFacilityId);
    	Context context = new Context();
    	context.setVariable("reportingYear", reportingYear);
        context.setVariable("facilityName", facilityName);
        context.setVariable("agencyFacilityId", agencyFacilityId);
        context.setVariable("sltEmail", sltEmail);
        context.setVariable("slt", slt);
        context.setVariable("cdxSubmissionUrl", cdxSubmissionUrl);

      String emailBody = templateEngine.process(MID_YEAR_REPORT_SUBMITTED_BY_CERT_BODY_TEMPLATE, context);
      sendHtmlMessage(to, cc, from, emailSubject, emailBody);
    }

    public void sendMidYearReportAcceptedNotification(String to, String cc, String from, String facilityName, String reportingYear, String comments, String slt, String sltEmail)
    {
        String emailSubject = MessageFormat.format(MID_YEAR_REPORT_ACCEPTED_BY_SLT_SUBJECT, reportingYear, facilityName);
        Context context = new Context();
        context.setVariable("reportingYear", reportingYear);
        context.setVariable("facilityName", facilityName);
        context.setVariable("comments", comments);
        context.setVariable("sltEmail", sltEmail);
        context.setVariable("slt", slt);

        String emailBody = templateEngine.process(MID_YEAR_REPORT_ACCEPTED_BY_SLT_BODY_TEMPLATE, context);
        sendHtmlMessage(to, cc, from, emailSubject, emailBody);
    }

    public void sendMidYearReportRejectedNotification(String to, String cc, String from, String facilityName, String reportingYear, String comments, String slt, String sltEmail)
    {
        String emailSubject = MessageFormat.format(MID_YEAR_REPORT_REJECTED_BY_SLT_SUBJECT, reportingYear, facilityName);
        Context context = new Context();
        context.setVariable("reportingYear", reportingYear);
        context.setVariable("facilityName", facilityName);
        context.setVariable("comments", comments);
        context.setVariable("sltEmail", sltEmail);
        context.setVariable("slt", slt);

        String emailBody = templateEngine.process(MID_YEAR_REPORT_REJECTED_BY_SLT_BODY_TEMPLATE, context);
        sendHtmlMessage(to, cc, from, emailSubject, emailBody);
    }

    public void sendReportRejectedNotification(String to, String cc, String from, String facilityName, String reportingYear, String comments, Long attachmentId, String slt, String sltEmail)
    {
        String emailSubject = MessageFormat.format(REPORT_REJECTED_BY_SLT_SUBJECT, reportingYear, facilityName);
        Context context = new Context();
        context.setVariable("reportingYear", reportingYear);
        context.setVariable("facilityName", facilityName);
        context.setVariable("comments", comments);
        context.setVariable("sltEmail", sltEmail);
        context.setVariable("slt", slt);

        if (attachmentId != null) {
            Attachment attachment = attachmentsRepo.findById(attachmentId)
                    .orElseThrow(() -> new NotExistException("Report Attachment", attachmentId));

            context.setVariable("attachment", attachment.getFileName());
        }

        String emailBody = templateEngine.process(REPORT_REJECTED_BY_SLT_BODY_TEMPLATE, context);
        sendHtmlMessage(to, cc, from, emailSubject, emailBody);
    }

    public void sendReportAcceptedNotification(String to, String cc, String from, String facilityName, String reportingYear, String comments, String slt, String sltEmail, Boolean isOptOut)
    {
        String emailSubject = MessageFormat.format(REPORT_ACCEPTED_BY_SLT_SUBJECT, reportingYear, facilityName);
        Context context = new Context();
        context.setVariable("reportingYear", reportingYear);
        context.setVariable("facilityName", facilityName);
        context.setVariable("comments", comments);
        context.setVariable("sltEmail", sltEmail);
        context.setVariable("slt", slt);
        context.setVariable("isOptOut", isOptOut);

        String emailBody = templateEngine.process(REPORT_ACCEPTED_BY_SLT_BODY_TEMPLATE, context);
        sendHtmlMessage(to, cc, from, emailSubject, emailBody);
    }

    public void sendReportAdvancedQANotification(String to, String cc, String from, String facilityName, String reportingYear, String slt, String sltEmail)
    {
        String emailSubject = MessageFormat.format(REPORT_BEGIN_ADVANCED_QA_BY_SLT_SUBJECT, reportingYear, facilityName);
        Context context = new Context();
        context.setVariable("reportingYear", reportingYear);
        context.setVariable("facilityName", facilityName);
        context.setVariable("sltEmail", sltEmail);
        context.setVariable("slt", slt);

        String emailBody = templateEngine.process(REPORT_BEGIN_ADVANCED_QA_BY_SLT_BODY_TEMPLATE, context);
        sendHtmlMessage(to, cc, from, emailSubject, emailBody);
    }

    public void sendSccUpdateFailedNotification(Exception exception) {
        String emailSubject = SCC_UPDATE_FAILED_SUBJECT;
        Context context = new Context();
        context.setVariable("exception", exception);
        String emailBody = templateEngine.process(SCC_UPDATE_FAILED_BODY_TEMPLATE, context);
        sendAdminEmail(emailSubject, emailBody);
    }

    public void sendRemoveNullUsersFailedNotification(Exception exception) {
        String emailSubject = REMOVE_NULL_USERS_FAILED_SUBJECT;
        Context context = new Context();
        context.setVariable("exception", exception);
        String emailBody = templateEngine.process(REMOVE_NULL_USERS_FAILED_BODY_TEMPLATE, context);
        sendAdminEmail(emailSubject, emailBody);
    }

    public void sendUserAccessRequestNotification(String to, String from, String facilityName, String agencyFacilityIdentifier, String userName, String userEmail, String role)
    {
        String emailSubject = MessageFormat.format(USER_ACCESS_REQUEST_SUBJECT, userName, facilityName);
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("userEmail", userEmail);
        context.setVariable("role", role);
        context.setVariable("facilityName", facilityName);
        context.setVariable("agencyFacilityIdentifier", agencyFacilityIdentifier);
        String emailBody = templateEngine.process(USER_ACCESS_REQUEST_BODY_TEMPLATE, context);
        sendHtmlMessage(to, from, emailSubject, emailBody);
    }

    public void sendUserAssociationAcceptedNotification(String to, String cc, String from, String facilityName, String role)
    {
        String emailSubject = MessageFormat.format(USER_ASSOCIATION_ACCEPTED_SUBJECT, facilityName);
        Context context = new Context();
        context.setVariable("role", role);
        context.setVariable("facilityName", facilityName);
        String emailBody = templateEngine.process(USER_ASSOCIATION_ACCEPTED_BODY_TEMPLATE, context);
        sendHtmlMessage(to, cc, from, emailSubject, emailBody);
    }

    public void sendUserAccessRemovedNotification(String to, String cc, String from, String facilityName, String role, String slt, String sltEmail)
    {
        String emailSubject = MessageFormat.format(USER_ASSOCIATION_REMOVED_SUBJECT, facilityName);
        Context context = new Context();
        context.setVariable("role", role);
        context.setVariable("facilityName", facilityName);
        context.setVariable("sltEmail", sltEmail);
        context.setVariable("slt", slt);
        String emailBody = templateEngine.process(USER_ASSOCIATION_REMOVED_BODY_TEMPLATE, context);
        sendHtmlMessage(to, cc, from, emailSubject, emailBody);
    }

    public void sendSLTNotification(String cc, String from, String sltEmail, Communication communication, MultipartFile file) {
        String emailSubject = communication.getSubject();
        Context context = new Context();
        context.setVariable("content", communication.getContent());
        context.setVariable("sltEmail", sltEmail);
        context.setVariable("slt", communication.getProgramSystemCode().getCode());
        context.setVariable("sltDescription", communication.getProgramSystemCode().getDescription());

        if (file != null && !file.isEmpty()) {
        	context.setVariable("attachmentName", file.getOriginalFilename());
        }

        String emailBody = templateEngine.process(SLT_NOTIFICATION_GENERIC_BODY_TEMPLATE, context);
        sendMassHtmlMessage(communication.getRecipientEmail(), cc, from, emailSubject, emailBody, file);
    }

    public void sendUserAssociationRejectedNotification(String to, String cc, String from, String facilityName, String role, String comments)
    {
        String emailSubject = MessageFormat.format(USER_ASSOCIATION_REJECTED_SUBJECT, facilityName);
        Context context = new Context();
        context.setVariable("role", role);
        context.setVariable("facilityName", facilityName);
        context.setVariable("comments", comments);
        String emailBody = templateEngine.process(USER_ASSOCIATION_REJECTED_BODY_TEMPLATE, context);
        sendHtmlMessage(to, cc, from, emailSubject, emailBody);
    }

    public void sendUserFeedbackNotification(UserFeedbackDto userFeedback){

        String emailSubject = MessageFormat.format(USER_FEEDBACK_SUBMITTED_SUBJECT, userFeedback.getYear().toString(), userFeedback.getFacilityName());
        Context context = new Context();
        context.setVariable("facilityName", userFeedback.getFacilityName());
        context.setVariable("reportingYear", userFeedback.getYear());
        context.setVariable("userName", userFeedback.getUserName());
        context.setVariable("userRole", userFeedback.getUserRole());
        context.setVariable("userId", userFeedback.getUserId());
        context.setVariable("intuitiveRating", userFeedback.getIntuitiveRating());
        context.setVariable("dataEntryScreens", userFeedback.getDataEntryScreens());
        context.setVariable("dataEntryBulkUpload", userFeedback.getDataEntryBulkUpload());
        context.setVariable("calculationScreens", userFeedback.getCalculationScreens());
        context.setVariable("controlsAndControlPathAssignments", userFeedback.getControlsAndControlPathAssignments());
        context.setVariable("qualityAssurance", userFeedback.getQualityAssuranceChecks());
        context.setVariable("overallReportingTime", userFeedback.getOverallReportingTime());
        context.setVariable("openQuestion1", userFeedback.getBeneficialFunctionalityComments());
        context.setVariable("openQuestion2", userFeedback.getDifficultFunctionalityComments());
        context.setVariable("openQuestion3", userFeedback.getEnhancementComments());

        String emailBody = templateEngine.process(USER_FEEDBACK_SUBMITTED_BODY_TEMPLATE, context);
        sendAdminEmail(emailSubject, emailBody);
    }

    public void sendCdxInboxMessage(CdxInboxMessageType type, String cdxUserId, String from, Map<String, Object> variables)
    {
    	Context context = new Context();
      context.setVariables(variables);

      String emailSubject;
      if (type.equals(CdxInboxMessageType.REPORT_ACCEPTED)
        || type.equals(CdxInboxMessageType.SEMIANNUAL_REPORT_ACCEPTED)
        || type.equals(CdxInboxMessageType.REPORT_REJECTED)
        || type.equals(CdxInboxMessageType.SEMIANNUAL_REPORT_REJECTED)
        || type.equals(CdxInboxMessageType.REPORT_ADVANCED_QA)
        || type.equals(CdxInboxMessageType.REPORT_READY_FOR_CERT_SUBJECT)
        || type.equals(CdxInboxMessageType.SEMIANNUAL_REPORT_READY_FOR_CERT_SUBJECT)
      ) {
        emailSubject = MessageFormat.format(type.subject(), variables.get("reportingYear"),
                variables.get("facilityName"));
      } else if (type.equals(CdxInboxMessageType.REPORT_SUBMITTED)
          || type.equals(CdxInboxMessageType.SEMIANNUAL_REPORT_SUBMITTED)) {
        emailSubject = MessageFormat.format(type.subject(), variables.get("reportingYear"),
                variables.get("facilityName"), variables.get("agencyFacilityId"));
      } else {
        emailSubject = MessageFormat.format(type.subject(), variables.get("facilityName"));
      }
      String emailBody = this.templateEngine.process(type.template(), context);

    	inboxClient.sendCdxInboxMessage(cdxUserId, from, emailSubject, emailBody);
    }

}
