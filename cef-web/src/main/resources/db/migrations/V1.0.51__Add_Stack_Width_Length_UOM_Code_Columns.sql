ALTER TABLE release_point
    ADD COLUMN stack_width numeric(4,1),
    ADD COLUMN stack_length numeric(4,1),
    ADD COLUMN stack_width_uom_code varchar(20),
    ADD COLUMN stack_length_uom_code varchar(20),
    ADD CONSTRAINT stack_width_uom_fkey FOREIGN KEY (stack_width_uom_code)
        REFERENCES unit_measure_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    ADD CONSTRAINT stack_length_uom_fkey FOREIGN KEY (stack_length_uom_code)
        REFERENCES unit_measure_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;
