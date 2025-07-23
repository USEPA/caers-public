--update the unit design capacity setting it to true
UPDATE unit_measure_code SET unit_design_capacity = true, legacy = false, last_inventory_year = null WHERE code = 'TON/DAY';
UPDATE unit_measure_code SET unit_design_capacity = true, legacy = false, last_inventory_year = null WHERE code = 'TON/HR';
UPDATE unit_measure_code SET unit_design_capacity = true, legacy = false, last_inventory_year = null WHERE code = 'TON/YR';
UPDATE unit_measure_code SET unit_design_capacity = true, legacy = false, last_inventory_year = null WHERE code = 'FT3/MIN';
UPDATE unit_measure_code SET unit_design_capacity = true, legacy = false, last_inventory_year = null WHERE code = 'E3FT2/HR';
