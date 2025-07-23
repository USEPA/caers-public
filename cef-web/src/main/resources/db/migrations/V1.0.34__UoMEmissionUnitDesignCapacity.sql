   
ALTER TABLE unit_measure_code
    ADD COLUMN unit_design_capacity boolean NOT NULL DEFAULT false;

ALTER TABLE unit_measure_code ALTER COLUMN unit_design_capacity DROP DEFAULT;

update unit_measure_code set unit_design_capacity = true where code = 'MW';
update unit_measure_code set unit_design_capacity = true where code = 'GAL';
update unit_measure_code set unit_design_capacity = true where code = 'BBL';
update unit_measure_code set unit_design_capacity = true where code = 'BLRHP';
update unit_measure_code set unit_design_capacity = true where code = 'E3LB/HR';
update unit_measure_code set unit_design_capacity = true where code = 'E6BTU/HR';
update unit_measure_code set unit_design_capacity = true where code = 'MMBTU/HR';
update unit_measure_code set unit_design_capacity = true where code = 'HP';
update unit_measure_code set unit_design_capacity = true where code = 'KW';
