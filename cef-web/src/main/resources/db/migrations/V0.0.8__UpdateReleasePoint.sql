-- Add fugitive lat and long to release_point table
ALTER TABLE release_point
    ADD COLUMN fugitive_line_1_latitude numeric(10, 6);
ALTER TABLE release_point
    ADD COLUMN fugitive_line_1_longitude numeric(10, 6);

ALTER TABLE release_point
    ADD COLUMN fugitive_line_2_latitude numeric(10, 6);
ALTER TABLE release_point
    ADD COLUMN fugitive_line_2_longitude numeric(10, 6);

-- Fix typo in release_point table
ALTER TABLE release_point
    RENAME exit_gas_velicity_uom_code TO exit_gas_velocity_uom_code;