ALTER TABLE operating_status_code ADD COLUMN sub_facility_status boolean NOT NULL DEFAULT false;
ALTER TABLE operating_status_code ALTER COLUMN sub_facility_status DROP DEFAULT;

ALTER TABLE operating_status_code ADD COLUMN facility_status boolean NOT NULL DEFAULT false;
ALTER TABLE operating_status_code ALTER COLUMN facility_status DROP DEFAULT;

update operating_status_code set sub_facility_status = true where code = 'OP';
update operating_status_code set sub_facility_status = true where code = 'PS';
update operating_status_code set sub_facility_status = true where code = 'TS';

update operating_status_code set facility_status = true where code = 'OP';
update operating_status_code set facility_status = true where code = 'PS';
update operating_status_code set facility_status = true where code = 'TS';
update operating_status_code set facility_status = true where code = 'ONRE';
update operating_status_code set facility_status = true where code = 'ONP';
