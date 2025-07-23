
INSERT INTO slt_properties (name, label, description, datatype) values (
'feature.industry-facility-naics.enabled','SLT Facility NAICS Editing','Enabling allows SLTs to edit facility NAICS codes (prevents Certifiers and Preparers from editing).','boolean');

CREATE OR REPLACE FUNCTION create_slt_facility_naics() RETURNS void AS $$
DECLARE
	psc RECORD;
BEGIN
	FOR psc IN (SELECT program_system_code FROM slt_config GROUP BY program_system_code) LOOP
		INSERT INTO slt_config (name, value, program_system_code) VALUES 
			('feature.industry-facility-naics.enabled', 'false', psc.program_system_code);
	END LOOP;
 END;
 $$ LANGUAGE plpgsql;

 DO $$ 
 BEGIN
 PERFORM "create_slt_facility_naics"();
 DROP FUNCTION IF EXISTS create_slt_facility_naics();
 END;$$
 