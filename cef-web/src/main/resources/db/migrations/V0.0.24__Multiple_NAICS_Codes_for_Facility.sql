CREATE TABLE facility_naics_xref
(
    id bigserial NOT NULL,
    facility_site_id bigint NOT NULL,
    naics_code numeric(6) NOT NULL,
    primary_flag boolean NOT NULL,
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT facility_naics_xref_pkey PRIMARY KEY (id),
    CONSTRAINT facility_naics_facility_site_fkey FOREIGN KEY (facility_site_id)
        REFERENCES facility_site (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT facility_naics_code_fkey FOREIGN KEY (naics_code)
        REFERENCES naics_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

DROP VIEW IF EXISTS vw_submissions_review_dashboard;
CREATE OR REPLACE VIEW vw_submissions_review_dashboard as
select er.id as emissions_report_id, 
fs.name as facility_name, 
fs.alt_site_identifier as airs_id, 
os.description as operating_status, 
(select industry from naics_code_industry where code_prefix= substring(fxn.naics_code::text, 1,2)::integer) as industry,
er.year as year,
er.agency_code as agency_code,
(select max(year) from emissions_report where frs_facility_id=er.frs_facility_id and id<>er.id and year<er.year) as last_submittal_year
from facility_site fs 
join emissions_report er on fs.report_id=er.id
join operating_status_code os on os.code=fs.status_code
join facility_naics_xref fxn on fs.id = fxn.facility_site_id and primary_flag = true;

ALTER TABLE facility_site DROP COLUMN naics_code;