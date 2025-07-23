
ALTER TABLE admin_properties ADD COLUMN label character varying(64);
ALTER TABLE admin_properties ADD COLUMN description character varying(255);
ALTER TABLE admin_properties ADD COLUMN datatype character varying(20);

UPDATE admin_properties SET label = 'Total Emissions Tolerance Warning Value', description = 'How far off total emissions can be from calculated without triggering warning.', datatype = 'string' WHERE name = 'emissions.tolerance.total.warning';
UPDATE admin_properties SET label = 'Total Emissions Tolerance Error Value', description = 'How far off total emissions can be from calculated without triggering error', datatype = 'string' WHERE name = 'emissions.tolerance.total.error';
UPDATE admin_properties SET label = 'Default Email', description = 'Email sender where CAERS emails come from.', datatype = 'string' WHERE name = 'email.default';
UPDATE admin_properties SET label = 'Admin Email', description = 'Email recipient who should receive admin email notifications.', datatype = 'string' WHERE name = 'email.admin';
UPDATE admin_properties SET label = 'SCC Update Task Last Updated', description = 'Date when SCC last run.', datatype = 'string' WHERE name = 'task.scc-update.last-ran';
UPDATE admin_properties SET label = 'SCC Update Task Enabled', description = 'Enables SCC Updates', datatype = 'boolean' WHERE name = 'task.scc-update.enabled';
UPDATE admin_properties SET label = 'SCC Update Task CRON', description = 'The cron expression used for scheduling the SCC Update.', datatype = 'string' WHERE name = 'task.scc-update.cron';
UPDATE admin_properties SET label = 'Virus Scanner Enabled', description = 'Enables virus scanner on file uploads.', datatype = 'boolean' WHERE name = 'virus-scanner.enabled';
UPDATE admin_properties SET label = 'Bulk Entry Enabled', description = 'Allows users to utilize the bulk entry feature for emissions and processes.', datatype = 'boolean' WHERE name = 'feature.bulk-entry.enabled';
UPDATE admin_properties SET label = 'User Feedback Enabled', description = 'Allow users to submit feedback.', datatype = 'boolean' WHERE name = 'feature.user-feedback.enabled';
UPDATE admin_properties SET label = 'Announcement Feature Enabled', description = 'Enables application wide announcements be shown in an announcement banner.', datatype = 'boolean' WHERE name = 'feature.announcement.enabled';
UPDATE admin_properties SET label = 'Announcement Feature Text', description = 'Text shown in the announcement banner.', datatype = 'string' WHERE name = 'feature.announcement.text';
UPDATE admin_properties SET label = 'Admin Email Enabled', description = 'Enabling allows notifications to be sent to admin emails.', datatype = 'boolean' WHERE name = 'email.admin.enabled';
UPDATE admin_properties SET label = 'Excel Export Enabled', description = 'Allows excel export.', datatype = 'boolean' WHERE name = 'feature.excel-export.enabled';
UPDATE admin_properties SET label = 'CDX Association Migration Enabled', description = 'Enables button to migrate user facility associations from CDX be shown.', datatype = 'boolean' WHERE name = 'feature.cdx-association-migration.enabled';
UPDATE admin_properties SET label = 'CERS-V2 Enabled', description = 'Enables submitting data to EIS from CAERS in the CERS V2.0 schema', datatype = 'boolean' WHERE name = 'feature.cers-v2.enabled';

ALTER TABLE admin_properties ALTER COLUMN label SET NOT NULL;
ALTER TABLE admin_properties ALTER COLUMN description SET NOT NULL;
ALTER TABLE admin_properties ALTER COLUMN datatype SET NOT NULL;