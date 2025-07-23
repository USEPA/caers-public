ALTER TABLE emission
    ADD COLUMN pollutant_cas_id character varying(100);

ALTER TABLE emission
    ADD COLUMN comments character varying(2000);