
DELETE FROM admin_properties WHERE name = 'current-year-reporting.enabled';

INSERT INTO admin_properties (name, value, label, description, datatype, required) values (
'current-year-reporting.enabled','true','Current Year Reporting','Disable to prevent current year reports from being created.','boolean','true');


CREATE OR REPLACE FUNCTION create_slt_current_year_reporting() RETURNS void AS $$
DECLARE
	psc RECORD;
BEGIN
	FOR psc IN (SELECT program_system_code FROM slt_config GROUP BY program_system_code) LOOP
		INSERT INTO slt_config (name, value, program_system_code) VALUES 
			('slt.feature.current-year-reporting.enabled', 'true', psc.program_system_code);
	END LOOP;
 END;
 $$ LANGUAGE plpgsql;

 DO $$ 
 BEGIN
 PERFORM "create_slt_current_year_reporting"();
 DROP FUNCTION IF EXISTS create_slt_current_year_reporting();
 END;$$
 