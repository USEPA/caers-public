-- Agency
TRUNCATE TABLE agency CASCADE;

-- Aircraft Engine Type Code
TRUNCATE TABLE aircraft_engine_type_code CASCADE;

--  
TRUNCATE TABLE slt_config CASCADE;
ALTER SEQUENCE slt_config_id_seq RESTART WITH 1;

-- Master Facility Record
TRUNCATE TABLE master_facility_record CASCADE;
ALTER SEQUENCE master_facility_record_id_seq RESTART WITH 1;

-- Tribal Code
TRUNCATE TABLE tribal_code CASCADE;

-- Program System Code
TRUNCATE TABLE program_system_code CASCADE;

-- Program System Code
TRUNCATE TABLE pollutant_program_system_code CASCADE;
ALTER SEQUENCE pollutant_program_system_code_id_seq RESTART WITH 1;

-- SCHEMA VERSION
TRUNCATE TABLE schema_version_cef CASCADE;

