ALTER TABLE calculation_material_code
    ADD COLUMN default_heat_content_ratio numeric(12,6),
    ADD COLUMN heat_content_ratio_numerator character varying(20),
    ADD COLUMN heat_content_ratio_denominator character varying(20);
    
UPDATE calculation_material_code SET default_heat_content_ratio = 25.09, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'TON' WHERE code = '640';
UPDATE calculation_material_code SET default_heat_content_ratio = 11.513, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'TON' WHERE code = '639';
UPDATE calculation_material_code SET default_heat_content_ratio = 24.93, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'TON' WHERE code = '663';
UPDATE calculation_material_code SET default_heat_content_ratio = 17.25, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'TON' WHERE code = '664';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.103, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL' WHERE code = '675';
UPDATE calculation_material_code SET default_heat_content_ratio = 19.292, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'TON' WHERE code = '717';
UPDATE calculation_material_code SET default_heat_content_ratio = 24.8, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'TON' WHERE code = '724';
UPDATE calculation_material_code SET default_heat_content_ratio = 590, heat_content_ratio_numerator = 'BTU', heat_content_ratio_denominator = 'BTU/SCF' WHERE code = '425';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.619, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'FT3S' WHERE code = '819';
UPDATE calculation_material_code SET default_heat_content_ratio = 5.771, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'TON' WHERE code = '56';
UPDATE calculation_material_code SET default_heat_content_ratio = 137381, heat_content_ratio_numerator = 'BTU', heat_content_ratio_denominator = 'BTU/GAL' WHERE code = '57';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.139, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL' WHERE code = '824';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.138, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL' WHERE code = '58';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.146, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL' WHERE code = '825';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.000092, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'FT3S' WHERE code = '809';
UPDATE calculation_material_code SET default_heat_content_ratio = 5.222, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'BBL' WHERE code = '127';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.135, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL' WHERE code = '865';
UPDATE calculation_material_code SET default_heat_content_ratio = 5.355, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL' WHERE code = '160';
UPDATE calculation_material_code SET default_heat_content_ratio = 14.21, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'TON' WHERE code = '173';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.092, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL' WHERE code = '178';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.001026, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'FT3S' WHERE code = '209';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.091, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL' WHERE code = '257';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.000599, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'FT3S' WHERE code = '251';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.000485, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'FT3S' WHERE code = '502';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.841, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'FT3S' WHERE code = '518';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.091, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL' WHERE code = '255';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.14, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL' WHERE code = '922';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.15, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL' WHERE code = '923';
UPDATE calculation_material_code SET default_heat_content_ratio = 17.25, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'TON' WHERE code = '323';
UPDATE calculation_material_code SET default_heat_content_ratio = 17.48, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'TON' WHERE code = '15';
UPDATE calculation_material_code SET default_heat_content_ratio = 0.138, heat_content_ratio_numerator = 'E6BTU', heat_content_ratio_denominator = 'GAL' WHERE code = '374';
    
    