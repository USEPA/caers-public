CREATE TABLE emissions_operating_type_code
(
    code character varying(20) NOT NULL,
    short_name character varying(50),
    description character varying(200),
    PRIMARY KEY (code)
);

INSERT INTO emissions_operating_type_code values ('R', 'Routine', 'The normal or typical emissions for a reporting period.');
INSERT INTO emissions_operating_type_code values ('U', 'Upset', 'Emissions over and above the Routine emissions which occur as a result of unexpected occurrences.');
INSERT INTO emissions_operating_type_code values ('SU', 'Startup', 'Emissions over and above the Routine emissions which occur as a result of planned Startup operations.');
INSERT INTO emissions_operating_type_code values ('SD', 'Shutdown', 'Emissions over and above the Routine emissions which occur as a result of planned Shutdown operations.');

UPDATE reporting_period SET emissions_operating_type_code = 'R';

ALTER TABLE reporting_period
ADD CONSTRAINT reporting_period_emissions_operating_fkey FOREIGN KEY (emissions_operating_type_code)
REFERENCES emissions_operating_type_code (code) MATCH SIMPLE
ON UPDATE NO ACTION
ON DELETE NO ACTION;