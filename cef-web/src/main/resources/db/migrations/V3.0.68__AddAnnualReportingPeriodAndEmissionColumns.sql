
ALTER TABLE reporting_period
    ADD COLUMN annual_reporting_period_id bigint;

ALTER TABLE emission
    ADD COLUMN annual_emission_id bigint;

ALTER TABLE reporting_period
    ADD CONSTRAINT annual_reporting_period_fkey FOREIGN KEY (annual_reporting_period_id)
        REFERENCES reporting_period (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;

ALTER TABLE emission
    ADD CONSTRAINT annual_emission_fkey FOREIGN KEY (annual_emission_id)
        REFERENCES emission (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;
