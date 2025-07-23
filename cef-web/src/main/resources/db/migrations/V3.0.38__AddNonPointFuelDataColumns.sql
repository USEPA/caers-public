
INSERT INTO unit_measure_code(
	code, description, ef_numerator, ef_denominator, unit_type, calculation_variable, legacy, unit_design_capacity, fuel_use_uom, heat_content_uom, fuel_use_type)
	VALUES ('E9BTU', 'BILLION BTUS', false, false, 'ENERGY', '[M] * [k] * btu', false, false, false, false, 'energy');
	
ALTER TABLE reporting_period
	ADD COLUMN standardized_non_point_fuel_use numeric;
	

ALTER TABLE calculation_material_code
	ADD COLUMN non_point_standardized_uom character varying(10),
	ADD CONSTRAINT non_point_standardized_uom_fkey FOREIGN KEY (non_point_standardized_uom)
		REFERENCES unit_measure_code (code) MATCH SIMPLE
	    ON UPDATE NO ACTION
	    ON DELETE NO ACTION;
	
UPDATE calculation_material_code SET non_point_standardized_uom = 'E9BTU' WHERE code = '15';
UPDATE calculation_material_code SET non_point_standardized_uom = 'E6FT3SD' WHERE code = '209';
UPDATE calculation_material_code SET non_point_standardized_uom = 'E3BBL' 
	WHERE code IN ('823','675','56','58','57','864','162','256','279','924','2','825','178','255','922','923');
UPDATE calculation_material_code SET non_point_standardized_uom = 'E3TON' 
	WHERE code IN ('640','639','663','664','717','173','323');
	
	
ALTER TABLE naics_code
	ADD COLUMN non_point_bucket character varying(20);
	
UPDATE naics_code SET non_point_bucket = '11' WHERE code >= 110000 AND code < 120000;
UPDATE naics_code SET non_point_bucket = '21' WHERE code >= 210000 AND code < 220000;
UPDATE naics_code SET non_point_bucket = '2212' WHERE code >= 221200 AND code < 221300;
UPDATE naics_code SET non_point_bucket = '2213' WHERE code >= 221300 AND code < 221400;
UPDATE naics_code SET non_point_bucket = '23' WHERE code >= 230000 AND code < 240000;
UPDATE naics_code SET non_point_bucket = '31' WHERE code >= 310000 AND code < 320000;
UPDATE naics_code SET non_point_bucket = '32' WHERE code >= 320000 AND code < 330000;
UPDATE naics_code SET non_point_bucket = '33' WHERE code >= 330000 AND code < 340000;
UPDATE naics_code SET non_point_bucket = '42' WHERE code >= 420000 AND code < 430000;
UPDATE naics_code SET non_point_bucket = '44' WHERE code >= 440000 AND code < 450000;
UPDATE naics_code SET non_point_bucket = '45' WHERE code >= 450000 AND code < 460000;
UPDATE naics_code SET non_point_bucket = '48 (except 4892)' WHERE code >= 480000 AND code < 486200;
UPDATE naics_code SET non_point_bucket = '48 (except 4892)' WHERE code >= 486300 AND code < 490000;
UPDATE naics_code SET non_point_bucket = '49' WHERE code >= 490000 AND code < 500000;
UPDATE naics_code SET non_point_bucket = '51' WHERE code >= 510000 AND code < 520000;
UPDATE naics_code SET non_point_bucket = '52' WHERE code >= 520000 AND code < 530000;
UPDATE naics_code SET non_point_bucket = '53' WHERE code >= 530000 AND code < 540000;
UPDATE naics_code SET non_point_bucket = '54' WHERE code >= 540000 AND code < 550000;
UPDATE naics_code SET non_point_bucket = '55' WHERE code >= 550000 AND code < 560000;
UPDATE naics_code SET non_point_bucket = '56' WHERE code >= 560000 AND code < 570000;
UPDATE naics_code SET non_point_bucket = '61' WHERE code >= 610000 AND code < 620000;
UPDATE naics_code SET non_point_bucket = '62' WHERE code >= 620000 AND code < 630000;
UPDATE naics_code SET non_point_bucket = '71' WHERE code >= 710000 AND code < 720000;
UPDATE naics_code SET non_point_bucket = '72' WHERE code >= 720000 AND code < 730000;
UPDATE naics_code SET non_point_bucket = '81' WHERE code >= 810000 AND code < 820000;
UPDATE naics_code SET non_point_bucket = '92' WHERE code >= 920000 AND code < 930000;


UPDATE calculation_material_code SET default_heat_content_ratio = 0.1385, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL' WHERE code = '823';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.135, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL' WHERE code = '864';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.135, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL' WHERE code = '162';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.097, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL' WHERE code = '256';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.145, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL' WHERE code = '279';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.14267, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL' WHERE code = '924';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.1225, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL' WHERE code = '2';


UPDATE unit_measure_code SET calculation_variable='bbl * year' WHERE code = 'BBL-YR';
UPDATE unit_measure_code SET calculation_variable='bbl / [day]' WHERE code = 'BBL/DAY'; 
UPDATE unit_measure_code SET calculation_variable='bbl / [h]' WHERE code = 'BBL/HR';
UPDATE unit_measure_code SET calculation_variable='bbl / year' WHERE code = 'BBL/YR';
UPDATE unit_measure_code SET calculation_variable='[k] * bbl * year' WHERE code = 'E3BBL-YR';
UPDATE unit_measure_code SET calculation_variable = '100 * bbl' WHERE code = 'E2BBL';
UPDATE unit_measure_code SET calculation_variable = '1000 * bbl / [day]' WHERE code = 'E3BBL/DAY';
UPDATE unit_measure_code SET calculation_variable = '1000 * bbl / [h]' WHERE code = 'E3BBL/HR';
UPDATE unit_measure_code SET calculation_variable = '1000 * bbl / year' WHERE code = 'E3BBL/YR';
UPDATE unit_measure_code SET calculation_variable = '[M] * bbl' WHERE code = 'E6BBL';
UPDATE unit_measure_code SET calculation_variable = '[M] * bbl / [day]' WHERE code = 'E6BBL/DAY';
UPDATE unit_measure_code SET calculation_variable = 'bbl' WHERE code = 'BBL';
UPDATE unit_measure_code SET calculation_variable = '1000 * bbl' WHERE code = 'E3BBL';
