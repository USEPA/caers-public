
ALTER TABLE user_facility_association
	ADD authorized_by character varying(255),
	ADD authorization_date timestamp without time zone,
	ADD deauthorization_by character varying(255),
	ADD deauthoriztion_date timestamp without time zone;

ALTER TABLE user_facility_association
	RENAME approved TO active;
	
UPDATE user_facility_association 
	SET authorization_date = last_modified_date
	WHERE active = true;
	