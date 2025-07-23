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

public enum ReportAction {
	CREATED("Created New"),
	COPIED_FWD("Copied Forward"),
	UPLOADED("Report Uploaded"),
	REOPENED("Reopened"),
	ACCEPTED("Accepted"),
	REJECTED("Rejected"),
    SEMIANNUAL_SUBMITTED("Semiannual Submitted"),
    SEMIANNUAL_REJECTED("Semiannual Rejected"),
    READY_TO_CERTIFY("Report Ready to Certify"),
	SUBMITTED("Submitted"),
	ATTACHMENT("Uploaded Attachment"),
	ATTACHMENT_DELETED("Attachment Deleted"),
	ADVANCED_QA("Advanced QA"),
	OPTED_OUT("Opted Out"),
    DELETED("Deleted"),
    MARKED_ONRE("Marked ONRE");

	private final String label;

	ReportAction(String label) {
		this.label = label;
	}

	public String code() {
		return this.name();
	}

	public String label() {
		return this.label;
	}
}
