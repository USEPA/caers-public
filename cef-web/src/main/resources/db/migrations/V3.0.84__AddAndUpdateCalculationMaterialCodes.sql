UPDATE calculation_material_code
    SET description = 'Lubricant', fuel_use_material = true, default_heat_content_ratio = 0.144, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL', non_point_standardized_uom = null
    WHERE code = '182';
UPDATE calculation_material_code
    SET description = 'Municipal Refuse or MSW', fuel_use_material = true, default_heat_content_ratio = 9.953, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'TON', non_point_standardized_uom = null
    WHERE code = '755';

INSERT INTO calculation_material_code(
    code, description, fuel_use_material, default_heat_content_ratio, heat_content_ratio_numerator, heat_content_ratio_denominator, non_point_standardized_uom)
    VALUES ('154', 'Isobutane', true, 0.099, 'E6BTU', 'GAL', null);
INSERT INTO calculation_material_code(
    code, description, fuel_use_material, default_heat_content_ratio, heat_content_ratio_numerator, heat_content_ratio_denominator, non_point_standardized_uom)
    VALUES ('231', 'Peat', true, 8.00, 'E6BTU', 'TON', null);
INSERT INTO calculation_material_code(
    code, description, fuel_use_material, default_heat_content_ratio, heat_content_ratio_numerator, heat_content_ratio_denominator, non_point_standardized_uom)
    VALUES ('240', 'Petroleum coke', true, 0.143, 'E6BTU', 'GAL', null);
INSERT INTO calculation_material_code(
    code, description, fuel_use_material, default_heat_content_ratio, heat_content_ratio_numerator, heat_content_ratio_denominator, non_point_standardized_uom)
    VALUES ('241', 'Petroleum feedstocks', true, 0.125, 'E6BTU', 'GAL', null);
INSERT INTO calculation_material_code(
    code, description, fuel_use_material, default_heat_content_ratio, heat_content_ratio_numerator, heat_content_ratio_denominator, non_point_standardized_uom)
    VALUES ('283', 'Rendered Animal Fat', true, 0.125, 'E6BTU', 'GAL', null);
INSERT INTO calculation_material_code(
    code, description, fuel_use_material, default_heat_content_ratio, heat_content_ratio_numerator, heat_content_ratio_denominator, non_point_standardized_uom)
    VALUES ('648', 'Road Oil and Asphalt', true, 0.158, 'E6BTU', 'GAL', null);
INSERT INTO calculation_material_code(
    code, description, fuel_use_material, default_heat_content_ratio, heat_content_ratio_numerator, heat_content_ratio_denominator, non_point_standardized_uom)
    VALUES ('678', 'Butylene', true, 0.105, 'E6BTU', 'GAL', null);
INSERT INTO calculation_material_code(
    code, description, fuel_use_material, default_heat_content_ratio, heat_content_ratio_numerator, heat_content_ratio_denominator, non_point_standardized_uom)
    VALUES ('993', 'Vegetable Oil', true, 0.12, 'E6BTU', 'GAL', null);
