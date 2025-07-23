--naics_code_industry
CREATE TABLE naics_code_industry
(
  code_prefix         NUMERIC(4) PRIMARY KEY,
  industry 	      VARCHAR(200)
);

INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (11, 'Agriculture, Forestry, Fishing and Hunting');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (21, 'Mining, Quarrying, and Oil and Gas Extraction');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (22, 'Utilities');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (23, 'Construction');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (31, 'Manufacturing');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (32, 'Manufacturing');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (33, 'Manufacturing');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (42, 'Wholesale Trade');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (44, 'Retail Trade');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (45, 'Retail Trade');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (48, 'Transportation and Warehousing');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (49, 'Transportation and Warehousing');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (51, 'Information');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (52, 'Finance and Insuranc');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (53, 'Real Estate and Rental and Leasing');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (54, 'Professional, Scientific, and Technical Services');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (55, 'Management of Companies and Enterprises');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (56, 'Administrative and Support and Waste Management and Remediation Services');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (61, 'Educational Services');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (62, 'Health Care and Social Assistance');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (71, 'Arts, Entertainment, and Recreation');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (72, 'Accommodation and Food Services');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (81, 'Other Services (except Public Administration)');
INSERT INTO naics_code_industry ("code_prefix", "industry") VALUES (92, 'Public Administration');


create or replace view vw_submissions_review_dashboard as
select er.id as emissions_report_id, 
fs.name as facility_name, 
fs.alt_site_identifier as airs_id, 
os.description as operating_status, 
(select industry from naics_code_industry where code_prefix= substring(fs.naics_code::text, 1,2)::integer) as industry,
er.year as last_submittal_year
from facility_site fs 
join emissions_report er on fs.report_id=er.id
join operating_status_code os on os.code=fs.status_code
join (
select fs.frs_facility_id,  max(er.year) as year
from facility_site fs 
join emissions_report er on fs.report_id=er.id
group by fs.frs_facility_id, er.year
) latest_report on latest_report.frs_facility_id=fs.frs_facility_id and er.year=latest_report.year;