-- set Methane and Propylene as Fuel Materials
UPDATE calculation_material_code SET fuel_use_material = true WHERE code IN ('257','518');

-- update denominator for Coke Oven Gas and Distillate Oil (Diesel)
UPDATE calculation_material_code SET heat_content_ratio_denominator = 'FT3S' WHERE code = '425';
UPDATE calculation_material_code SET heat_content_ratio_denominator = 'GAL' WHERE code = '57';
