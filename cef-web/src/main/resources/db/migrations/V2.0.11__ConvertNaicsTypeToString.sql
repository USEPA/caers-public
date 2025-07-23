DROP VIEW IF EXISTS vw_submissions_review_dashboard;

ALTER TABLE facility_naics_xref
ADD COLUMN naics_code_type varchar(10);
   
UPDATE facility_naics_xref
SET naics_code_type = 'PRIMARY'
WHERE primary_flag is true;

UPDATE facility_naics_xref
SET naics_code_type = 'SECONDARY'
WHERE primary_flag is false
OR primary_flag is null;

ALTER TABLE facility_naics_xref
ALTER COLUMN naics_code_type SET NOT NULL;

ALTER TABLE facility_naics_xref
DROP COLUMN primary_flag;
