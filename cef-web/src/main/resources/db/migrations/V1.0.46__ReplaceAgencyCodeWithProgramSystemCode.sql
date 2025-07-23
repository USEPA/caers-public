-- drop view dependent on columns
DROP VIEW IF EXISTS vw_submissions_review_dashboard;

-- emissions_report
ALTER TABLE emissions_report
ADD COLUMN program_system_code character varying(20);

ALTER TABLE emissions_report 
ADD CONSTRAINT program_system_code_fkey FOREIGN KEY (program_system_code) 
    REFERENCES program_system_code (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

update emissions_report set program_system_code = 'GADNR' where agency_code = 'GA';
update emissions_report set program_system_code = 'DOEE' where agency_code = 'DC';

ALTER TABLE emissions_report DROP COLUMN agency_code;


-- slt_config
ALTER TABLE slt_config
ADD COLUMN program_system_code character varying(20);

ALTER TABLE slt_config 
ADD CONSTRAINT program_system_code_fkey FOREIGN KEY (program_system_code) 
    REFERENCES program_system_code (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

update slt_config set program_system_code = 'GADNR' where agency_code = 'GA';
update slt_config set program_system_code = 'DOEE' where agency_code = 'DC';

ALTER TABLE slt_config DROP COLUMN agency_code;


-- eis_transaction_history
ALTER TABLE eis_transaction_history
ADD COLUMN program_system_code character varying(20);

ALTER TABLE eis_transaction_history 
ADD CONSTRAINT program_system_code_fkey FOREIGN KEY (program_system_code) 
    REFERENCES program_system_code (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

update eis_transaction_history set program_system_code = 'GADNR' where agency_code = 'GA';
update eis_transaction_history set program_system_code = 'DOEE' where agency_code = 'DC';

ALTER TABLE eis_transaction_history DROP COLUMN agency_code;


-- user_feedback
ALTER TABLE user_feedback
ADD COLUMN program_system_code character varying(20);

ALTER TABLE user_feedback 
ADD CONSTRAINT program_system_code_fkey FOREIGN KEY (program_system_code) 
    REFERENCES program_system_code (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

update user_feedback set program_system_code = 'GADNR' where agency_code = 'GA';
update user_feedback set program_system_code = 'DOEE' where agency_code = 'DC';

ALTER TABLE user_feedback DROP COLUMN agency_code;


-- update view
CREATE or REPLACE VIEW vw_submissions_review_dashboard AS
SELECT er.id AS emissions_report_id,
    er.eis_program_id,
    fs.name AS facility_name,
    fs.id AS facility_site_id,
    fs.alt_site_identifier AS alt_facility_id,
    os.description AS operating_status,
    er.status AS report_status,
    nci.industry,
    er.year,
    er.program_system_code,
    ( SELECT max(emissions_report.year) AS max
           FROM emissions_report
          WHERE emissions_report.frs_facility_id::text = er.frs_facility_id::text AND emissions_report.id <> er.id AND emissions_report.year < er.year) AS last_submittal_year
   FROM facility_site fs
     JOIN emissions_report er ON fs.report_id = er.id
     JOIN operating_status_code os ON os.code::text = fs.status_code::text
     LEFT JOIN facility_naics_xref fxn ON fs.id = fxn.facility_site_id AND fxn.primary_flag = true
     LEFT JOIN naics_code_industry nci ON nci.code_prefix = "substring"(fxn.naics_code::text, 1, 2)::numeric;