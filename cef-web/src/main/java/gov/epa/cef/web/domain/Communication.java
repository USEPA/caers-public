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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import gov.epa.cef.web.domain.common.BaseAuditEntity;

/**
 * Communication entity
 */
@Entity
@Table(name = "communication")
public class Communication extends BaseAuditEntity {
	
	private static final long serialVersionUID = 1L;
	
    @Column(name = "sender_name", nullable = false)
	private String senderName;
    
    @Column(name = "subject", length = 255)
	private String subject;
    
    @Column(name = "recipient_email", length = 2000)
	private String recipientEmail;
    
    @Column(name = "content", length = 2000)
	private String content;

    @Column(name = "email_status")
    private String emailStatus;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_system_code")
    private ProgramSystemCode programSystemCode;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "emissionsReport")
    private Attachment attachments;

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getRecipientEmail() {
		return recipientEmail;
	}

	public void setRecipientEmail(String recipientEmail) {
		this.recipientEmail = recipientEmail;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content =content;
	}

	public ProgramSystemCode getProgramSystemCode() {
		return programSystemCode;
	}

	public void setProgramSystemCode(ProgramSystemCode programSystemCode) {
		this.programSystemCode = programSystemCode;
	}

	public Attachment getAttachments() {
		return attachments;
	}

	public void setAttachments(Attachment attachments) {
		this.attachments = attachments;
	}

	public String getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}
    
}
