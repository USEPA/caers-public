/* add columns to existing table */
alter table user_feedback
	add facility_name varchar(80),
	add agency_code varchar(3),
	add year SMALLINT,
	add user_name varchar(255);

CREATE INDEX i_user_feedback_year
    ON user_feedback USING btree
    (year ASC NULLS LAST);
    
CREATE INDEX i_user_feedback_year_and_agency_code
    ON user_feedback USING btree
    (year ASC NULLS LAST, agency_code ASC NULLS LAST);

/* create temp table with desired data */
SELECT 
	uf.id as userfeedback_id,
	er.id as report_id, 
	fs.name as facility_name, 
	er.year as year,
	er.agency_code as agency_code
INTO TEMP TABLE temp_feedback
FROM user_feedback uf
	LEFT OUTER JOIN emissions_report er on uf.report_id=er.id
	LEFT OUTER JOIN facility_site fs on er.id=fs.report_id;
	
CREATE OR REPLACE FUNCTION update_user_feedback() RETURNS SETOF record AS $$
DECLARE
	fb_record user_feedback%ROWTYPE;
BEGIN
	/* loop through each record in user_feedback */
 	FOR fb_record IN (SELECT * FROM user_feedback) LOOP
		/* set facility name where values are null  */
		IF fb_record.facility_name IS NULL THEN
			UPDATE user_feedback set facility_name = 
			(SELECT facility_name FROM temp_feedback 
			WHERE user_feedback.id = temp_feedback.userfeedback_id);
		END IF;
	END LOOP;

	/* set year, and agency code where values are null */
	UPDATE user_feedback set year = '2019'
	WHERE year IS NULL;

	UPDATE user_feedback set agency_code = 'GA'
	WHERE agency_code IS NULL;

DROP TABLE IF EXISTS temp_feedback;
END;
$$ LANGUAGE plpgsql;

 DO $$ 
 BEGIN
 PERFORM "update_user_feedback"();
 DROP FUNCTION IF EXISTS update_user_feedback();
 END;$$        