
ALTER TABLE emissions_unit
    ADD COLUMN initial_monthly_reporting_period character varying(10);

ALTER TABLE emissions_process
    ADD COLUMN initial_monthly_reporting_period character varying(10);
