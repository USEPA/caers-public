drop view if exists vw_submissions_review_dashboard;
create or replace view vw_submissions_review_dashboard as
select er.id as emissions_report_id, 
fs.name as facility_name, 
fs.alt_site_identifier as airs_id, 
os.description as operating_status, 
(select industry from naics_code_industry where code_prefix= substring(fs.naics_code::text, 1,2)::integer) as industry,
er.year as year,
er.agency_code as agency_code,
(select max(year) from emissions_report where frs_facility_id=er.frs_facility_id and id<>er.id and year<er.year) as last_submittal_year
from facility_site fs 
join emissions_report er on fs.report_id=er.id
join operating_status_code os on os.code=fs.status_code;