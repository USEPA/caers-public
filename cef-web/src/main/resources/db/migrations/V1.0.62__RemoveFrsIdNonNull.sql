
ALTER TABLE emissions_report
    ALTER COLUMN frs_facility_id DROP NOT NULL;

ALTER TABLE emissions_report
    ALTER COLUMN eis_program_id DROP NOT NULL;


ALTER TABLE facility_site
    ALTER COLUMN eis_program_id DROP NOT NULL;