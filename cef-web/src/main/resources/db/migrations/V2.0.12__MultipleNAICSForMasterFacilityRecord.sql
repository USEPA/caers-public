CREATE TABLE master_facility_naics_xref
(
    id bigserial NOT NULL,
    master_facility_id bigint NOT NULL,
    naics_code numeric(6) NOT NULL,
    naics_code_type varchar(10) NOT NULL,
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT mfr_naics_xref_pkey PRIMARY KEY (id),
    CONSTRAINT mfr_naics_mfr_fkey FOREIGN KEY (master_facility_id)
        REFERENCES master_facility_record (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT mfr_naics_code_fkey FOREIGN KEY (naics_code)
        REFERENCES naics_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
