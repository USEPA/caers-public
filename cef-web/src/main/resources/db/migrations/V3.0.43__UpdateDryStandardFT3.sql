
UPDATE unit_measure_code SET ef_denominator = TRUE, legacy = FALSE, fuel_use_uom = TRUE, fuel_use_type = 'gas' WHERE code = 'E6FT3SD';
INSERT INTO unit_measure_code (code,description,ef_numerator,ef_denominator,unit_type,calculation_variable,legacy,unit_design_capacity,fuel_use_uom,heat_content_uom,fuel_use_type) VALUES (
	'E3FT3SD', 'DRY STANDARD CUBIC FEET PER MINUTE', FALSE, TRUE, 'DSCF', '[k] * 1', FALSE, FALSE, TRUE, FALSE, 'gas');
	
UPDATE energy_conversion_factor SET numerator_uom_code = 'E6FT3SD' WHERE calculation_material_code = '209';
	
UPDATE calculation_material_code SET heat_content_ratio_denominator = 'E3FT3SD' WHERE code = '819';
UPDATE calculation_material_code SET heat_content_ratio_denominator = 'E3FT3SD' WHERE code = '518';
