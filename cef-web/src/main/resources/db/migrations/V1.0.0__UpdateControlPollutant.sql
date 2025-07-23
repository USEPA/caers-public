UPDATE control_pollutant
	SET percent_reduction = 0
	WHERE percent_reduction IS NULL;
ALTER TABLE control_pollutant 
	ALTER COLUMN percent_reduction SET NOT NULL;