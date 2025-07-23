
CREATE TABLE master_facility_record
(
  id BIGSERIAL PRIMARY KEY,
  eis_program_id character varying(22),
  program_system_code character varying(20) NOT NULL,
  name character varying(80) NOT NULL,
  category_code character varying(20),
  source_type_code character varying(20),
  status_code character varying(20) NOT NULL,
  agency_facility_id character varying(30),
  description character varying(100),
  status_year smallint,
  street_address character varying(100) NOT NULL,
  city character varying(60) NOT NULL,
  state_code character varying(5) NOT NULL,
  county_code character varying(5),
  postal_code character varying(10),
  country_code character varying(10),
  mailing_street_address character varying(100),
  mailing_city character varying(60),
  mailing_state_code character varying(5),
  mailing_postal_code character varying(10),
  mailing_country_code character varying(10),
  latitude numeric(10,6),
  longitude numeric(10,6),
  tribal_code character varying(20),
  created_by character varying(255) NOT NULL,
  created_date timestamp without time zone NOT NULL DEFAULT now(),
  last_modified_by character varying(255) NOT NULL,
  last_modified_date timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT program_system_code_fkey FOREIGN KEY (program_system_code)
      REFERENCES program_system_code (code) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION,
  CONSTRAINT category_code_fkey FOREIGN KEY (category_code)
      REFERENCES facility_category_code (code) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION,
  CONSTRAINT source_code_fkey FOREIGN KEY (source_type_code)
      REFERENCES facility_source_type_code (code) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION,
  CONSTRAINT status_code_fkey FOREIGN KEY (status_code)
      REFERENCES operating_status_code (code) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION,
  CONSTRAINT state_code_fkey FOREIGN KEY (state_code)
      REFERENCES fips_state_code (code) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION,
  CONSTRAINT county_code_fkey FOREIGN KEY (county_code)
      REFERENCES fips_county (code) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION,
  CONSTRAINT mailing_state_code_fkey FOREIGN KEY (mailing_state_code)
      REFERENCES fips_state_code (code) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION,
  CONSTRAINT tribal_code_fkey FOREIGN KEY (tribal_code)
      REFERENCES tribal_code (code) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION
);

ALTER TABLE emissions_report
ADD COLUMN master_facility_id bigint;

ALTER TABLE emissions_report 
ADD CONSTRAINT master_facility_id_fkey FOREIGN KEY (master_facility_id) 
    REFERENCES master_facility_record (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


insert into master_facility_record 
    (eis_program_id, program_system_code, name, category_code, 
     source_type_code, status_code, agency_facility_id, description, 
     status_year, street_address, city, state_code, county_code, 
     postal_code, country_code,
     mailing_street_address, mailing_city, mailing_state_code,
     mailing_postal_code, mailing_country_code,
     latitude, longitude, tribal_code,
     created_by, last_modified_by, last_modified_date)
select distinct on (fs.eis_program_id) 
    fs.eis_program_id, fs.program_system_code, fs.name, fs.category_code, 
    fs.source_type_code, fs.status_code, fs.alt_site_identifier, fs.description, 
    fs.status_year, fs.street_address, fs.city, fs.state_code, fs.county_code, 
    fs.postal_code, fs.country_code,
    fs.mailing_street_address, fs.mailing_city, fs.mailing_state_code,
    fs.mailing_postal_code, fs.mailing_country_code,
    fs.latitude, fs.longitude, fs.tribal_code,
    'SYSTEM', fs.last_modified_by, fs.last_modified_date
    from
        facility_site fs
        order by fs.eis_program_id, fs.status_year desc, fs.created_date desc;

update emissions_report er
    set master_facility_id = mfr.id
    from master_facility_record mfr
    where mfr.eis_program_id = er.eis_program_id;

ALTER TABLE emissions_report ALTER COLUMN master_facility_id SET NOT NULL;