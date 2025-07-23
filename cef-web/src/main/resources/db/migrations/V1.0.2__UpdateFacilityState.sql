
update facility_site_contact set state_code = 'GA' where state_code = '';

update facility_site_contact contact
    set state_code = fsc.code
    from fips_state_code fsc
    where fsc.usps_code = contact.state_code;

ALTER TABLE facility_site_contact
    ADD CONSTRAINT state_code_fkey FOREIGN KEY (state_code)
    REFERENCES fips_state_code (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

update facility_site_contact set mailing_state_code = null where mailing_state_code = '';

update facility_site_contact contact
    set mailing_state_code = fsc.code
    from fips_state_code fsc
    where fsc.usps_code = contact.mailing_state_code;

ALTER TABLE facility_site_contact
    ADD CONSTRAINT mailing_state_code_fkey FOREIGN KEY (mailing_state_code)
    REFERENCES fips_state_code (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


update facility_site set state_code = 'GA' where state_code = '';

update facility_site fs
    set state_code = fsc.code
    from fips_state_code fsc
    where fsc.usps_code = fs.state_code;

ALTER TABLE facility_site
    ADD CONSTRAINT state_code_fkey FOREIGN KEY (state_code)
    REFERENCES fips_state_code (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

update facility_site set mailing_state_code = 'GA' where mailing_state_code = '';

update facility_site fs
    set mailing_state_code = fsc.code
    from fips_state_code fsc
    where fsc.usps_code = fs.mailing_state_code;

ALTER TABLE facility_site
    ADD CONSTRAINT mailing_state_code_fkey FOREIGN KEY (mailing_state_code)
    REFERENCES fips_state_code (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;