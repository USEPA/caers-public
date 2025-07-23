
CREATE TABLE slt_properties (
	name character varying(64) PRIMARY KEY,
	label character varying(64),
	description character varying(255),
	datatype character varying(20));
	
INSERT INTO slt_properties (name, label, description, datatype) values ('slt-eis-program-code','SLT EIS Program System Code','SLT Program System Code associated with EIS submissions.','string');
INSERT INTO slt_properties (name, label, description, datatype) values ('slt-eis-user','SLT EIS User ID','SLT User ID associated with EIS submissions.','string');
INSERT INTO slt_properties (name, label, description, datatype) values ('slt-email','SLT Email','Email recipient who should receive SLT email notifications.','string');

ALTER TABLE slt_properties ALTER COLUMN name SET NOT NULL;
ALTER TABLE slt_properties ALTER COLUMN label SET NOT NULL;
ALTER TABLE slt_properties ALTER COLUMN description SET NOT NULL;
ALTER TABLE slt_properties ALTER COLUMN datatype SET NOT NULL;

ALTER TABLE slt_config ADD CONSTRAINT name_fkey FOREIGN KEY (name) REFERENCES slt_properties (name);

UPDATE admin_properties SET label = 'Admin Email Communications' WHERE name = 'email.admin.enabled';
UPDATE admin_properties SET description = 'Origin address for emails sent to CAERS users.' WHERE name = 'email.default';
UPDATE admin_properties SET label = 'Announcement Banner', description = 'Enables the announcement banner visible to all users in the CAER application.' WHERE name = 'feature.announcement.enabled';
UPDATE admin_properties SET label = 'Announcement Banner Message' WHERE name = 'feature.announcement.text';
UPDATE admin_properties SET label = 'Facility Report Template Download', description = 'Allows certifiers and preparers to download their current year report template.' WHERE name = 'feature.bulk-entry.enabled';
UPDATE admin_properties SET label = 'Bulk-Upload-Template Download' WHERE name = 'feature.excel-export.enabled';
UPDATE admin_properties SET label = 'Update Application SCCs', description = 'Updates SCCs available for selection inside the application, during the scheduled update time, from EPA sccwebservices.' WHERE name = 'task.scc-update.enabled';
UPDATE admin_properties SET description = 'Date of the last time SCCs in the application were updated given "Update SCCs" was enabled.' WHERE name = 'task.scc-update.last-ran';
UPDATE admin_properties SET label = 'CERS-V2 XML Schema', description = 'Enables submitting data to EIS from CAERS in the CERS V2.0 schema.' WHERE name = 'feature.cers-v2.enabled';
UPDATE admin_properties SET label = 'Migrate User Facility Associations', description = 'Enables migrating user facility associations from CDX to CAERS.' WHERE name = 'feature.cdx-association-migration.enabled';
UPDATE admin_properties SET description = 'How far off total emissions can be from calculated without triggering error.' WHERE name = 'emissions.tolerance.total.error';
UPDATE admin_properties SET label = 'User Feedback Survey', description = 'Allows certifiers and preparers to submit feedback on their reporting experience once per year following submission.' WHERE name = 'feature.user-feedback.enabled';
UPDATE admin_properties SET label = 'Document Virus Scan', description = 'Enables scanning all documents for viruses during upload to the application.' WHERE name = 'virus-scanner.enabled';
