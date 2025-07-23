ALTER TABLE control_path
	ADD COLUMN path_id VARCHAR(20);
UPDATE control_path
	SET path_id = id;
ALTER TABLE control_path ALTER COLUMN path_id SET NOT NULL;