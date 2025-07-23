ALTER TABLE facility
    ADD COLUMN mailing_street_address character varying(100);

ALTER TABLE facility
    ADD COLUMN mailing_city character varying(60);

ALTER TABLE facility
    ADD COLUMN mailing_state_code character varying(5);

ALTER TABLE facility
    ADD COLUMN mailing_postal_code character varying(10);
    
ALTER TABLE facility
    ADD COLUMN mailing_country_code character varying(10);
    
    
CREATE TABLE facility_site_contact
(
    id bigserial NOT NULL,
    facility_site_id bigint NOT NULL,
    type character varying(150) NOT NULL,
    prefix character varying(15),
    first_name character varying(20),
    last_name character varying(20),
    street_address character varying(100) NOT NULL,
    city character varying(60) NOT NULL,
    state_code character varying(5) NOT NULL,
    country_code character varying(10),
    postal_code character varying(10),
    mailing_street_address character varying(100) NOT NULL,
    mailing_city character varying(60) NOT NULL,
    mailing_state_code character varying(5) NOT NULL,
    mailing_country_code character varying(10),
    mailing_postal_code character varying(10),
    
    CONSTRAINT facility_site_contact_pkey PRIMARY KEY (id),
    CONSTRAINT facility_site_id_fkey FOREIGN KEY (id)
        REFERENCES public.facility (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);