CREATE TABLE emission_factor
(
    id bigserial NOT NULL,
    scc_code numeric(8) NOT NULL,
    pollutant_code character varying(12) NOT NULL,
    numerator_uom_code character varying(20) NOT NULL,
    denominator_uom_code character varying(20) NOT NULL,
    calculation_material_code character varying(20) NOT NULL,
    calculation_parameter_type_code character varying(20) NOT NULL,
    formula_indicator boolean NOT NULL,
    emission_factor numeric(30, 20),
    emission_factor_formula character varying(200),
    control_indicator boolean NOT NULL,
    description character varying(2000),
    note character varying(2000),
    source character varying(100),
    last_update_date date NOT NULL,
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
        ON DELETE NO ACTION
);

