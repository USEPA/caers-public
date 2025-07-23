ALTER TABLE master_facility_record
ADD CONSTRAINT psc_agency_id_key UNIQUE (program_system_code, agency_facility_id);
