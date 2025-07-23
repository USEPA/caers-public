ALTER TABLE emissions_process
    ADD COLUMN slt_billing_exempt boolean;

INSERT INTO slt_properties (name, label, description, datatype) values (
    'slt-feature.slt-billing-exempt.enabled',
    'SLT Billing-Exempt',
    'Enabling allows users to mark Processes as Billing-Exempt.',
    'boolean');

CREATE OR REPLACE FUNCTION create_slt_billing_exempt() RETURNS void AS $$
DECLARE
    psc RECORD;
BEGIN
    FOR psc IN (SELECT program_system_code FROM slt_config GROUP BY program_system_code) LOOP
        INSERT INTO slt_config ("name", "value", program_system_code) VALUES
            ('slt-feature.slt-billing-exempt.enabled', 'false', psc.program_system_code);
    END LOOP;
END;
$$ LANGUAGE plpgsql;

DO $$
BEGIN
  PERFORM "create_slt_billing_exempt"();
DROP FUNCTION IF EXISTS create_slt_billing_exempt();
END $$;
