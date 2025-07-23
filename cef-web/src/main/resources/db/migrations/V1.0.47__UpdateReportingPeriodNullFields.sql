ALTER TABLE reporting_period ALTER COLUMN emissions_operating_type_code DROP NOT NULL;
ALTER TABLE reporting_period ALTER COLUMN calculation_parameter_type_code DROP NOT NULL;
ALTER TABLE reporting_period ALTER COLUMN calculation_parameter_value DROP NOT NULL;
ALTER TABLE reporting_period ALTER COLUMN calculation_parameter_uom DROP NOT NULL;
ALTER TABLE reporting_period ALTER COLUMN calculation_material_code DROP NOT NULL;
