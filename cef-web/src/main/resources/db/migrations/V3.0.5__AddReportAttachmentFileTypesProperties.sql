
INSERT INTO slt_properties(
	name, label, description, datatype, required)
	VALUES ('report-attachment-upload.csv', 'Allow CSV Report Attachment Uploads', 'Enabling allows report attachments to be uploaded in CSV format', 'boolean', true),
		   ('report-attachment-upload.xls', 'Allow XLS Report Attachment Uploads', 'Enabling allows report attachments to be uploaded in XLS format', 'boolean', true),
		   ('report-attachment-upload.xlsx', 'Allow XLSX Report Attachment Uploads', 'Enabling allows report attachments to be uploaded in XLSX format', 'boolean', true),
		   ('report-attachment-upload.txt', 'Allow TXT Report Attachment Uploads', 'Enabling allows report attachments to be uploaded in TXT format', 'boolean', true),
		   ('report-attachment-upload.pdf', 'Allow PDF Report Attachment Uploads', 'Enabling allows report attachments to be uploaded in PDF format', 'boolean', true),
		   ('report-attachment-upload.docx', 'Allow DOCX Report Attachment Uploads', 'Enabling allows report attachments to be uploaded in DOCX format', 'boolean', true);

CREATE OR REPLACE FUNCTION create_slt_report_attachment_types() RETURNS void AS $$
DECLARE
	psc RECORD;
BEGIN
	FOR psc IN (SELECT program_system_code FROM slt_config GROUP BY program_system_code) LOOP
		INSERT INTO slt_config (name, value, program_system_code) VALUES 
			('report-attachment-upload.csv', 'true', psc.program_system_code),
			('report-attachment-upload.xls', 'true', psc.program_system_code),
			('report-attachment-upload.xlsx', 'true', psc.program_system_code),
			('report-attachment-upload.txt', 'true', psc.program_system_code),
			('report-attachment-upload.pdf', 'true', psc.program_system_code),
			('report-attachment-upload.docx', 'true', psc.program_system_code);
	END LOOP;
 END;
 $$ LANGUAGE plpgsql;

 DO $$ 
 BEGIN
 PERFORM "create_slt_report_attachment_types"();
 DROP FUNCTION IF EXISTS create_slt_report_attachment_types();
 END;$$
 