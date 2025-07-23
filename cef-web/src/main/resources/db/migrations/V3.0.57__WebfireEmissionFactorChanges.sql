ALTER TABLE emission_factor
    ADD COLUMN control_measure_code_2 character varying(20) NOT NULL DEFAULT '0',
    ADD COLUMN control_measure_code_3 character varying(20) NOT NULL DEFAULT '0',
    ADD COLUMN control_measure_code_4 character varying(20) NOT NULL DEFAULT '0',
    ADD COLUMN control_measure_code_5 character varying(20) NOT NULL DEFAULT '0',
    ADD COLUMN quality character varying(10),
    ADD COLUMN revoked_date date,
    ADD COLUMN min_value numeric,
    ADD COLUMN max_value numeric,
    ADD COLUMN applicability character varying(2000),
    ADD COLUMN derivation character varying(2000),
    ADD COLUMN date_updated date,
    ADD COLUMN webfire_id numeric UNIQUE,
    ADD COLUMN condition character varying(20);

ALTER TABLE emission_factor
    ADD CONSTRAINT ef_control_measure_2_fkey FOREIGN KEY (control_measure_code_2)
        REFERENCES control_measure_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;

ALTER TABLE emission_factor
    ADD CONSTRAINT ef_control_measure_3_fkey FOREIGN KEY (control_measure_code_3)
        REFERENCES control_measure_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;

ALTER TABLE emission_factor
    ADD CONSTRAINT ef_control_measure_4_fkey FOREIGN KEY (control_measure_code_4)
        REFERENCES control_measure_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;

ALTER TABLE emission_factor
    ADD CONSTRAINT ef_control_measure_5_fkey FOREIGN KEY (control_measure_code_5)
        REFERENCES control_measure_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;

ALTER TABLE emission
    ADD COLUMN ef_webfire_id numeric;
