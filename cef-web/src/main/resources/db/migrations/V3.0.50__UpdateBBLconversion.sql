-- update BBL conversions based on standard 42 GAL blue BBL
UPDATE unit_measure_code SET calculation_variable = '(50/42) * bbl', unit_type = 'BBL' WHERE code = 'BBL50GAL';
UPDATE unit_measure_code SET calculation_variable = '(31000/42) * bbl', unit_type = 'BBL' WHERE code = 'E3BBL31G';
