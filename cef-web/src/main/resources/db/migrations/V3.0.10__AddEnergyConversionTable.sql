CREATE TABLE energy_conversion_factor
(
    id bigserial NOT NULL,
    calculation_material_code character varying(20) NOT NULL,
    denominator_uom_code character varying(20) NOT NULL,
    numerator_uom_code character varying(20) NOT NULL,
    conversion_factor numeric(14, 4) NOT NULL,
    source character varying(100),
    note character varying(2000),
    PRIMARY KEY (id),
    CONSTRAINT ef_calc_material_fkey FOREIGN KEY (calculation_material_code)
        REFERENCES calculation_material_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT cf_denominator_uom_fkey FOREIGN KEY (denominator_uom_code)
        REFERENCES unit_measure_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT cf_numerator_uom_fkey FOREIGN KEY (numerator_uom_code)
        REFERENCES unit_measure_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

ALTER TABLE emission 
	ADD COLUMN energy_conversion_factor_id bigint,
    ADD CONSTRAINT energy_conversion_factor_fkey FOREIGN KEY (energy_conversion_factor_id)
        REFERENCES energy_conversion_factor (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;

INSERT INTO energy_conversion_factor (id, calculation_material_code, numerator_uom_code, denominator_uom_code, conversion_factor, source, note) VALUES (1, '640', 'E3TON', 'E6BTU',25300, 'AP-42, Appendix A', NULL);
INSERT INTO energy_conversion_factor (id, calculation_material_code, numerator_uom_code, denominator_uom_code, conversion_factor, source, note) VALUES (2, '639', 'TON', 'E6BTU',15, 'https://www.eia.gov/totalenergy/data/monthly/pdf/sec13.pdf', NULL);
INSERT INTO energy_conversion_factor (id, calculation_material_code, numerator_uom_code, denominator_uom_code, conversion_factor, source, note) VALUES (3, '663', 'E3TON', 'E6BTU',26000, 'AP-42, Appendix A', NULL);
INSERT INTO energy_conversion_factor (id, calculation_material_code, numerator_uom_code, denominator_uom_code, conversion_factor, source, note) VALUES (4, '675', 'GAL', 'BTU',97400, 'AP-42, Appendix A', NULL);
INSERT INTO energy_conversion_factor (id, calculation_material_code, numerator_uom_code, denominator_uom_code, conversion_factor, source, note) VALUES (5, '717', 'TON', 'E6BTU',19.292, 'https://www.eia.gov/totalenergy/data/monthly/pdf/sec12_6.pdf', NULL);
INSERT INTO energy_conversion_factor (id, calculation_material_code, numerator_uom_code, denominator_uom_code, conversion_factor, source, note) VALUES (6, '724', 'TON', 'E6BTU',24.8, 'https://www.eia.gov/totalenergy/data/monthly/pdf/sec12_6.pdf', NULL);
INSERT INTO energy_conversion_factor (id, calculation_material_code, numerator_uom_code, denominator_uom_code, conversion_factor, source, note) VALUES (7, '56', 'E3BBL', 'E6BTU',5900, 'AP-42, Appendix A', NULL);
INSERT INTO energy_conversion_factor (id, calculation_material_code, numerator_uom_code, denominator_uom_code, conversion_factor, source, note) VALUES (8, '127', 'GAL', 'BTU',120285.714, 'DOE EIA: https://www.eia.gov/energyexplained/units-and-calculators/energy-conversion-calculators.php', NULL);
INSERT INTO energy_conversion_factor (id, calculation_material_code, numerator_uom_code, denominator_uom_code, conversion_factor, source, note) VALUES (9, '173', 'TON', 'E6BTU',16, 'AP-42, Appendix A', NULL);
INSERT INTO energy_conversion_factor (id, calculation_material_code, numerator_uom_code, denominator_uom_code, conversion_factor, source, note) VALUES (10, '178', 'E3BBL', 'E6BTU',4090, 'AP-42, Appendix A', NULL);
INSERT INTO energy_conversion_factor (id, calculation_material_code, numerator_uom_code, denominator_uom_code, conversion_factor, source, note) VALUES (11, '209', 'E6FT3S', 'E6BTU',1050, 'AP-42, Appendix A', NULL);
INSERT INTO energy_conversion_factor (id, calculation_material_code, numerator_uom_code, denominator_uom_code, conversion_factor, source, note) VALUES (12, '255', 'GAL', 'BTU',90500, 'AP-42, Appendix A', NULL);
INSERT INTO energy_conversion_factor (id, calculation_material_code, numerator_uom_code, denominator_uom_code, conversion_factor, source, note) VALUES (13, '922', 'E3BBL', 'E6BTU',6300, 'AP-42, Appendix A', NULL);
INSERT INTO energy_conversion_factor (id, calculation_material_code, numerator_uom_code, denominator_uom_code, conversion_factor, source, note) VALUES (14, '374', 'GAL', 'E6BTU',239.316, 'DOE EIA: https://www.eia.gov/energyexplained/units-and-calculators/energy-conversion-calculators.php', NULL);
INSERT INTO energy_conversion_factor (id, calculation_material_code, numerator_uom_code, denominator_uom_code, conversion_factor, source, note) VALUES (15, '828', 'KW-HR', 'BTU',3412, 'DOE EIA: https://www.eia.gov/energyexplained/units-and-calculators/energy-conversion-calculators.php', NULL);
INSERT INTO energy_conversion_factor (id, calculation_material_code, numerator_uom_code, denominator_uom_code, conversion_factor, source, note) VALUES (16, '374', 'BBL', 'GAL',42, NULL, NULL);
INSERT INTO energy_conversion_factor (id, calculation_material_code, numerator_uom_code, denominator_uom_code, conversion_factor, source, note) VALUES (17, '15', 'TON', 'E6BTU',10.4, 'AP-42, Appendix A', NULL);
