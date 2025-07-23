ALTER TABLE emission
    ADD CONSTRAINT webfire_id_fkey FOREIGN KEY (ef_webfire_id)
        REFERENCES emission_factor (webfire_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;
