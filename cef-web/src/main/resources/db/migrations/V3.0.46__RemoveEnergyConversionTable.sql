UPDATE unit_measure_code SET description = '1000 DRY STANDARD CUBIC FEET' WHERE code = 'E3FT3SD';

ALTER TABLE emission DROP COLUMN energy_conversion_factor_id;

DROP TABLE energy_conversion_factor;
