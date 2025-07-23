
-- drop tables to be updated
DROP VIEW IF EXISTS vw_emissions_by_facility_and_cas;
DROP VIEW IF EXISTS vw_submissions_review_dashboard;

-- drop columns
ALTER TABLE facility_site DROP COLUMN frs_facility_id;
ALTER TABLE facility_site DROP COLUMN eis_program_id;

ALTER TABLE emissions_report DROP COLUMN frs_facility_id;