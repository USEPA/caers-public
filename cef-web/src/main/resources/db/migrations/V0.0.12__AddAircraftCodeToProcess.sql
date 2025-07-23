-- Add aircraft engine type code to emissions_process table
ALTER TABLE emissions_process
ADD COLUMN aircraft_engine_type_code character varying(10);

ALTER TABLE emissions_process 
ADD CONSTRAINT process_aircraft_engine_fkey FOREIGN KEY (aircraft_engine_type_code) REFERENCES aircraft_engine_type_code (code);
