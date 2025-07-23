ALTER TABLE control_path 
	DROP COLUMN control_type;

ALTER TABLE control_path 
	ADD COLUMN facility_site_id BIGINT NOT NULL DEFAULT 9999991;

ALTER TABLE control_path 
	ALTER COLUMN facility_site_id DROP DEFAULT;

ALTER TABLE control_path 
	ADD CONSTRAINT control_path_fs_fkey 
	FOREIGN KEY (facility_site_id) REFERENCES facility_site (id) 
	MATCH SIMPLE 
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;

ALTER TABLE control_assignment 
	DROP CONSTRAINT control_assignment_rp_fkey;

ALTER TABLE control_assignment 
	DROP COLUMN release_point_id;

ALTER TABLE control_assignment 
	DROP CONSTRAINT control_assignment_ep_fkey;

ALTER TABLE control_assignment 
	DROP COLUMN emissions_process_id;

ALTER TABLE control_assignment 
	DROP CONSTRAINT control_assignment_eu_fkey;

ALTER TABLE control_assignment 
	DROP COLUMN emissions_unit_id;

ALTER TABLE control_assignment 
	ADD COLUMN control_path_parent_id BIGINT;

ALTER TABLE control_assignment 
	ADD CONSTRAINT control_assignment_con_path_parent_fkey 
	FOREIGN KEY (control_path_parent_id) REFERENCES control_path (id) 
	MATCH SIMPLE 
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;

ALTER TABLE control_path 
	DROP COLUMN control_order;

ALTER TABLE release_point_appt 
	ADD COLUMN control_path_id BIGINT;

ALTER TABLE release_point_appt 
	ADD CONSTRAINT release_point_appt_con_path_fkey 
	FOREIGN KEY (control_path_id) REFERENCES control_path (id) 
	MATCH SIMPLE 
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;

ALTER TABLE control_assignment 
	ADD COLUMN sequence_number INTEGER NOT NULL DEFAULT 1;

ALTER TABLE control_assignment 
	ALTER COLUMN sequence_number DROP DEFAULT;
