
ALTER TABLE slt_properties ADD COLUMN required boolean NOT NULL DEFAULT true;

INSERT INTO slt_properties (name, label, description, datatype, required) values (
'slt-feature.announcement.enabled','SLT Announcement Banner','Enables the SLT announcement banner visible users associated with SLT in the CAER application.','boolean', true);

INSERT INTO slt_properties (name, label, description, datatype, required) values (
'slt-feature.announcement.text','SLT Announcement Banner Message','Text shown in the SLT announcement banner.','textarea', false);

ALTER TABLE admin_properties ADD COLUMN required boolean NOT NULL DEFAULT true;

UPDATE admin_properties SET required = false, datatype = 'textarea' WHERE name = 'feature.announcement.text';
UPDATE admin_properties SET datatype = 'textarea' WHERE name = 'email.admin';
UPDATE admin_properties SET required = false WHERE name = 'task.scc-update.last-ran';
