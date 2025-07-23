-- Add fence line distance and fugitive fields to release_point table
ALTER TABLE release_point
    ADD COLUMN fence_line_distance numeric(6,0);
	
ALTER TABLE release_point
	ADD COLUMN fence_line_distance_uom_code character varying(20);
	
ALTER TABLE release_point
    ADD COLUMN fugitive_height numeric(3,0);
	
ALTER TABLE release_point
	ADD COLUMN fugitive_height_uom_code character varying(20);
	
ALTER TABLE release_point
    ADD COLUMN fugitive_width numeric(6,0);
    
ALTER TABLE release_point
	ADD COLUMN fugitive_width_uom_code character varying(20);
	
ALTER TABLE release_point
    ADD COLUMN fugitive_length numeric(6,0);
    
ALTER TABLE release_point
	ADD COLUMN fugitive_length_uom_code character varying(20);
	
ALTER TABLE release_point
    ADD COLUMN fugitive_angle numeric(3,0);
    
ALTER TABLE release_point
	ADD CONSTRAINT fence_line_distance_uom_fkey FOREIGN KEY (fence_line_distance_uom_code)
    REFERENCES unit_measure_code (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

ALTER TABLE release_point
	ADD CONSTRAINT fugitive_height_uom_fkey FOREIGN KEY (fugitive_height_uom_code)
    REFERENCES unit_measure_code (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;
    
ALTER TABLE release_point
	ADD CONSTRAINT fugitive_width_uom_fkey FOREIGN KEY (fugitive_width_uom_code)
    REFERENCES unit_measure_code (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

ALTER TABLE release_point
	ADD CONSTRAINT fugitive_length_uom_fkey FOREIGN KEY (fugitive_length_uom_code)
    REFERENCES unit_measure_code (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;