-- set Hot Mix Asphalt as Fuel Materials
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '849' AND description = 'Hot Mix Asphalt';

-- add monthly_reporting column to point_source_scc_code 
ALTER TABLE point_source_scc_code ADD COLUMN monthly_reporting boolean NOT NULL DEFAULT false;
UPDATE point_source_scc_code SET monthly_reporting = true WHERE fuel_use_required = true OR code IN ('30500245','30500246','10300701');
UPDATE point_source_scc_code SET calculation_material_code = '849' WHERE code = '30500245';
UPDATE point_source_scc_code SET calculation_material_code = '849' WHERE code = '30500246';
UPDATE point_source_scc_code SET calculation_material_code = '819' WHERE code = '10300701';

-- update monthly_fuel_reporting table
ALTER TABLE monthly_fuel_reporting DROP fuel_use_value;
ALTER TABLE monthly_fuel_reporting DROP total_operating_days;
ALTER TABLE monthly_fuel_reporting DROP period;

ALTER TABLE monthly_fuel_reporting 
	ADD COLUMN jan_fuel_use_value numeric,
	ADD COLUMN feb_fuel_use_value numeric,
	ADD COLUMN march_fuel_use_value numeric,
	ADD COLUMN april_fuel_use_value numeric,
	ADD COLUMN may_fuel_use_value numeric,
	ADD COLUMN june_fuel_use_value numeric,
	ADD COLUMN july_fuel_use_value numeric,
	ADD COLUMN aug_fuel_use_value numeric,
	ADD COLUMN sept_fuel_use_value numeric,
	ADD COLUMN oct_fuel_use_value numeric,
	ADD COLUMN nov_fuel_use_value numeric,
	ADD COLUMN dec_fuel_use_value numeric,
	ADD COLUMN semiannual_fuel_use_value numeric,
	ADD COLUMN annual_fuel_use_value numeric,

	ADD COLUMN jan_total_op_days numeric(3, 1),
	ADD COLUMN feb_total_op_days numeric(3, 1),
	ADD COLUMN march_total_op_days numeric(3, 1),
	ADD COLUMN april_total_op_days numeric(3, 1),
	ADD COLUMN may_total_op_days numeric(3, 1),
	ADD COLUMN june_total_op_days numeric(3, 1),
	ADD COLUMN july_total_op_days numeric(3, 1),
	ADD COLUMN aug_total_op_days numeric(3, 1),
	ADD COLUMN sept_total_op_days numeric(3, 1),
	ADD COLUMN oct_total_op_days numeric(3, 1),
	ADD COLUMN nov_total_op_days numeric(3, 1),
	ADD COLUMN dec_total_op_days numeric(3, 1),
	ADD COLUMN semi_annual_total_op_days numeric(4, 1),
	ADD COLUMN annual_total_op_days numeric(4, 1);
