DROP VIEW IF EXISTS vw_submissions_review_dashboard;
CREATE OR REPLACE VIEW vw_submissions_review_dashboard as
    select er.id as emissions_report_id, 
        er.eis_program_id as eis_program_id,
        fs.name as facility_name, 
        fs.id as facility_site_id, 
        fs.alt_site_identifier as alt_facility_id, 
        os.description as operating_status,
        er.status as report_status,
        nci.industry as industry,
        er.year as year,
        er.agency_code as agency_code,
        (select max(year) from emissions_report where frs_facility_id=er.frs_facility_id and id<>er.id and year<er.year) as last_submittal_year
    from facility_site fs 
        join emissions_report er on fs.report_id=er.id
        join operating_status_code os on os.code=fs.status_code
        join facility_naics_xref fxn on fs.id = fxn.facility_site_id and primary_flag = true
        join naics_code_industry nci on nci.code_prefix = substring(fxn.naics_code::text, 1,2)::numeric;