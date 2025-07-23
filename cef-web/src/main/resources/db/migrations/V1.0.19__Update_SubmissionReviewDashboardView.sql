DROP VIEW IF EXISTS vw_submissions_review_dashboard;

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
    er.agency_code,
    ( SELECT max(emissions_report.year) AS max
           FROM emissions_report
          WHERE emissions_report.frs_facility_id::text = er.frs_facility_id::text AND emissions_report.id <> er.id AND emissions_report.year < er.year) AS last_submittal_year
   FROM facility_site fs
     JOIN emissions_report er ON fs.report_id = er.id
     JOIN operating_status_code os ON os.code::text = fs.status_code::text
     LEFT JOIN facility_naics_xref fxn ON fs.id = fxn.facility_site_id AND fxn.primary_flag = true
     LEFT JOIN naics_code_industry nci ON nci.code_prefix = "substring"(fxn.naics_code::text, 1, 2)::numeric;