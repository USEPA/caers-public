
INSERT INTO slt_properties (name, label, description, datatype) values (
    'slt-feature.slt-edit-process-scc.enabled',
    'Only SLT Edits SCC',
    'Enabling allows SLTs to edit SCCs (prevents Certifiers and Preparers from editing).',
    'boolean');

CREATE OR REPLACE FUNCTION create_slt_edit_scc() RETURNS void AS $$
DECLARE
    psc RECORD;
BEGIN
    FOR psc IN (SELECT program_system_code FROM slt_config GROUP BY program_system_code) LOOP
        INSERT INTO slt_config ("name", "value", program_system_code) VALUES
            ('slt-feature.slt-edit-process-scc.enabled', 'false', psc.program_system_code);
    END LOOP;
END;
$$ LANGUAGE plpgsql;

DO $$
BEGIN
  PERFORM "create_slt_edit_scc"();
DROP FUNCTION IF EXISTS create_slt_edit_scc();
END $$;
