
ALTER TABLE report_history ALTER COLUMN file_name TYPE character varying(255);
ALTER TABLE report_attachment ALTER COLUMN comments TYPE character varying(2000);

DELETE FROM admin_properties WHERE name = 'attachment.max-size';
