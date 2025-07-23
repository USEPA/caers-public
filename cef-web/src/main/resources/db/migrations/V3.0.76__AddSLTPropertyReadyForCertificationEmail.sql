INSERT INTO slt_properties (name, label, description, datatype) values (
    'slt-email.ready-for-certification.enabled',
    'Receive ''Ready for Certification'' Email Notifications',
    'Enabling will cause an email notification to be sent to the SLT Email address when a preparer indicates that a report is ready for certification.',
    'boolean');

CREATE OR REPLACE FUNCTION insert_receive_ready_for_certification_emails() RETURNS void AS $func$
DECLARE psc RECORD;
    BEGIN
        FOR psc IN (SELECT program_system_code FROM slt_config GROUP BY program_system_code) LOOP
            INSERT INTO slt_config ("name", "value", program_system_code, can_edit) VALUES
            ('slt-email.ready-for-certification.enabled', 'false', psc.program_system_code, true);
        END LOOP;
    END;
$func$ LANGUAGE plpgsql;

DO $func$
BEGIN
    PERFORM "insert_receive_ready_for_certification_emails"();
    DROP FUNCTION IF EXISTS insert_receive_ready_for_certification_emails();
END $func$;