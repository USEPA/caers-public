CREATE TABLE emission_formula_variable_code
(
    code character varying(20) NOT NULL,
    description character varying(200),
    validation_type character varying(20),
    PRIMARY KEY (code)
);

INSERT INTO emission_formula_variable_code (code, description, validation_type) values ('A', '% Ash', 'PERCENT');
INSERT INTO emission_formula_variable_code (code, description, validation_type) values ('SU', '% Sulfur', 'PERCENT');
INSERT INTO emission_formula_variable_code (code, description, validation_type) values ('CaSu', 'Calcium vs Sulfur Proportion', 'CASU');
INSERT INTO emission_formula_variable_code (code, description, validation_type) values ('SG', 'SG in gr/100 ft3', 'REQUIRED');
INSERT INTO emission_formula_variable_code (code, description, validation_type) values ('YI', 'Initial baker''s percent of yeast', 'PERCENT');
INSERT INTO emission_formula_variable_code (code, description, validation_type) values ('TI', 'Total yeast action time in hours', 'REQUIRED');
INSERT INTO emission_formula_variable_code (code, description, validation_type) values ('SPK', 'Final (spike) baker''s percent of yeast', 'REQUIRED');
INSERT INTO emission_formula_variable_code (code, description, validation_type) values ('TS', 'Spiking time in hours (to the nearest tenth)', 'REQUIRED');
INSERT INTO emission_formula_variable_code (code, description, validation_type) values ('S', 'Surface material silt content (%)', 'PERCENT');
INSERT INTO emission_formula_variable_code (code, description, validation_type) values ('WMV', 'Mean vehicle weight (tons)', 'REQUIRED');
INSERT INTO emission_formula_variable_code (code, description, validation_type) values ('P', 'Days of Precipitation in the year', 'DAY_PER_YEAR');
INSERT INTO emission_formula_variable_code (code, description, validation_type) values ('SL', 'Road surface silt loading (grams per square meter) (g/m2 )', 'REQUIRED');
INSERT INTO emission_formula_variable_code (code, description, validation_type) values ('M', 'Material moisture content (%)', 'PERCENT');
INSERT INTO emission_formula_variable_code (code, description, validation_type) values ('U', 'Mean wind speed, meters per second (m/s) ', 'REQUIRED');
INSERT INTO emission_formula_variable_code (code, description, validation_type) values ('SMV', 'Mean Vehicle Speed (mph)', 'REQUIRED');

CREATE TABLE emission_formula_variable
(
    id bigserial NOT NULL,
    emission_id bigint NOT NULL,
    emission_formula_variable_code character varying(20) NOT NULL,
    value numeric,
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT emission_formula_variable_pkey PRIMARY KEY (id),
    CONSTRAINT emission_formula_code_fkey FOREIGN KEY (emission_formula_variable_code)
        REFERENCES emission_formula_variable_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
