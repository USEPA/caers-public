
ALTER TABLE control
    ADD COLUMN number_operating_months numeric(2),
    ADD COLUMN start_date date,
    ADD COLUMN end_date date,
    ADD COLUMN upgrade_date date, 
    ADD COLUMN upgrade_description character varying(200);
    