-- Drop facility_site_contact and recreate with all columns
DROP TABLE IF EXISTS facility_site_contact;

CREATE TABLE facility_site_contact
(
    id bigserial NOT NULL,
    facility_site_id bigint NOT NULL,
    type character varying(150) NOT NULL,
    prefix character varying(15),
    first_name character varying(20),
    last_name character varying(20),
    email character varying(255),
    phone character varying(15),
    phone_ext character varying(5),
    street_address character varying(100) NOT NULL,
    city character varying(60) NOT NULL,
    state_code character varying(5) NOT NULL,
    country_code character varying(10),
    postal_code character varying(10),
    mailing_street_address character varying(100),
    mailing_city character varying(60),
    mailing_state_code character varying(5),
    mailing_country_code character varying(10),
    mailing_postal_code character varying(10),
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    
    CONSTRAINT facility_site_contact_pkey PRIMARY KEY (id),
    CONSTRAINT facility_site_id_fkey FOREIGN KEY (facility_site_id)
        REFERENCES public.facility (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

