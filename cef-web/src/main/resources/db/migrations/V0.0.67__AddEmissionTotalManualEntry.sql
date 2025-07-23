ALTER TABLE emission ADD COLUMN total_manual_entry boolean NOT NULL DEFAULT true;

ALTER TABLE emission
    ADD COLUMN calculation_comment character varying(4000);