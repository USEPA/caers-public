
ALTER TABLE point_source_scc_code ADD COLUMN category character varying(20) NOT NULL DEFAULT 'Point';

ALTER TABLE emissions_process ADD COLUMN scc_category character varying(20) NOT NULL DEFAULT 'Point';

--updating scc update task last run date to ensure next run of the task will add all the non-point SCCs
UPDATE admin_properties SET value = '2017-01-01' WHERE name = 'task.scc-update.last-ran';



INSERT INTO slt_properties (name, label, description, datatype) values (
    'slt-feature.non-point-scc.enabled',
    'Include Non-Point SCCs',
    'Enabling allows Certifiers and Preparers to select Oil and Gas Exploration and Production Non-Point SCCs for reporting.',
    'boolean');

CREATE OR REPLACE FUNCTION create_slt_nonpoint_scc() RETURNS void AS $$
DECLARE
psc RECORD;
BEGIN
FOR psc IN (SELECT program_system_code FROM slt_config GROUP BY program_system_code) LOOP
        INSERT INTO slt_config ("name", "value", program_system_code) VALUES
            ('slt-feature.non-point-scc.enabled', 'false', psc.program_system_code);
END LOOP;
END;
$$ LANGUAGE plpgsql;

DO $$
BEGIN
  PERFORM "create_slt_nonpoint_scc"();
DROP FUNCTION IF EXISTS create_slt_nonpoint_scc();
END $$;