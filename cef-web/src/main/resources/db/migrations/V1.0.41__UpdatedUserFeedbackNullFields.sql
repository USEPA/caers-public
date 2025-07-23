UPDATE user_feedback set user_name = last_modified_by
	WHERE user_name IS NULL;
	
UPDATE user_feedback set facility_name = 'Deleted Report'
	WHERE facility_name IS NULL;