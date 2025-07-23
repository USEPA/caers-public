
ALTER TABLE calculation_material_code ALTER COLUMN default_heat_content_ratio TYPE numeric(12,5);	

update public.calculation_material_code set default_heat_content_ratio = 0.00102 where code = '209';
update public.calculation_material_code set default_heat_content_ratio = 0.00059 where code = '251';
update public.calculation_material_code set default_heat_content_ratio = 0.00048 where code = '502';