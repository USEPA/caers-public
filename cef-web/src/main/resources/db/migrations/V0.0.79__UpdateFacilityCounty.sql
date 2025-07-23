
ALTER TABLE fips_county DROP CONSTRAINT fips_county_pkey;

ALTER TABLE fips_county 
    RENAME COLUMN code TO county_code;

ALTER TABLE fips_county ADD COLUMN code character varying(5);

update fips_county set code = state_code || county_code;

ALTER TABLE fips_county ADD PRIMARY KEY (code);


ALTER TABLE facility_site
    ADD COLUMN county_code character varying(5);

ALTER TABLE facility_site
    ADD CONSTRAINT county_code_fkey FOREIGN KEY (county_code)
    REFERENCES fips_county (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

update facility_site fac
    set county_code = fc.code
    from fips_county fc 
    join fips_state_code fsc ON fsc.code = fc.state_code
    where fc.name = fac.county and fsc.usps_code = fac.state_code;

ALTER TABLE facility_site DROP COLUMN county;

ALTER TABLE facility_site_contact
    ADD COLUMN county_code character varying(5);

ALTER TABLE facility_site_contact
    ADD CONSTRAINT county_code_fkey FOREIGN KEY (county_code)
    REFERENCES fips_county (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

update facility_site_contact contact
    set county_code = fc.code
    from fips_county fc 
    join fips_state_code fsc ON fsc.code = fc.state_code
    where fc.name = contact.county and fsc.usps_code = contact.state_code;

ALTER TABLE facility_site_contact DROP COLUMN county;