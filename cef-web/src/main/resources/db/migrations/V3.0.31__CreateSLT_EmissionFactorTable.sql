CREATE TABLE slt_emission_factor (
    id bigserial NOT NULL,
    scc_code character varying(20) NOT NULL,
    pollutant_code character varying(12) NOT NULL,
    numerator_uom_code character varying(20) NOT NULL,
    denominator_uom_code character varying(20) NOT NULL,
    calculation_material_code character varying(20) NOT NULL,
    calculation_parameter_type_code character varying(20) NOT NULL,
    formula_indicator boolean NOT NULL,
    emission_factor numeric(30,20),
    emission_factor_formula character varying(200),
    control_indicator boolean NOT NULL,
    description character varying(2000),
    note character varying(2000),
    source character varying(100),
    last_update_date date NOT NULL,
    control_measure_code character varying(20) NOT NULL,
    revoked boolean DEFAULT false NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT ef_calc_material_fkey FOREIGN KEY (calculation_material_code)
        REFERENCES calculation_material_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT ef_calc_parameter_fkey FOREIGN KEY (calculation_parameter_type_code)
        REFERENCES calculation_parameter_type_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT ef_denominator_uom_fkey FOREIGN KEY (denominator_uom_code)
        REFERENCES unit_measure_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT ef_numerator_uom_fkey FOREIGN KEY (numerator_uom_code)
        REFERENCES unit_measure_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT ef_control_measure_fkey FOREIGN KEY (control_measure_code)
    	REFERENCES control_measure_code (code) MATCH SIMPLE
    	ON UPDATE NO ACTION
    	ON DELETE NO ACTION
);

ALTER TABLE emission ADD COLUMN emissions_factor_source character varying(100);

INSERT INTO emission_formula_variable_code (code, description, validation_type) values ('VOC', '% VOC', 'PERCENT');

INSERT INTO slt_properties (name, label, description, datatype) values (
'slt-feature.emission-factor-compendium.enabled','SLT Compendium','Enabling allows certifiers and preparers to use emission factors from the SLT compendium.','boolean');

