INSERT INTO slt_properties ("name", label, "description", datatype, "required") VALUES (
  'slt-feature.validation.ef-attachment.required',
  'Calculation Method Attachment Required',
  'Enabling requires Certifiers and Preparers to attach a file when using either a non-emission factor calculation method or including an emission factor and choosing to perform the calculations themselves.',
  'boolean',
  false
);

CREATE OR REPLACE FUNCTION create_slt_attachment_required() RETURNS void AS $$
DECLARE
	psc RECORD;
BEGIN
  FOR psc IN (SELECT program_system_code FROM slt_config GROUP BY program_system_code) LOOP
    INSERT INTO slt_config ("name", "value", program_system_code) VALUES 
    ('slt-feature.validation.ef-attachment.required', 'true', psc.program_system_code);
  END LOOP;
END;
$$ LANGUAGE plpgsql;

DO $$
BEGIN
  PERFORM "create_slt_attachment_required"();
  DROP FUNCTION IF EXISTS create_slt_attachment_required();
END;$$
