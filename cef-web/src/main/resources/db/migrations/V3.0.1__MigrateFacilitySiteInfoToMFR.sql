TRUNCATE TABLE master_facility_naics_xref;

-- migrate facility site info to master facility info
CREATE OR REPLACE FUNCTION migrate_fs_data_to_mfr() RETURNS void AS $$
DECLARE
	fs_info RECORD;
	fs_naics RECORD;
BEGIN
	-- for each record of fs_info - facility info from latest emissions report that has status of submitted or approved
	FOR fs_info IN (
		SELECT fs.*, e_rpt.master_facility_id AS mfr_id FROM facility_site fs 
		JOIN emissions_report e_rpt ON fs.report_id = e_rpt.id WHERE e_rpt.id IN (
		SELECT id FROM emissions_report e
				JOIN (
					-- gets submitted/approved report with most recent status year
					SELECT master_facility_id AS mfr_id, MAX(year) AS report_year
					FROM emissions_report WHERE status IN ('APPROVED', 'SUBMITTED')
					GROUP BY master_facility_id) AS mf_rpt_yr
					ON e.master_facility_id = mf_rpt_yr.mfr_id AND e.year = mf_rpt_yr.report_year
				JOIN (
					-- gets submitted/approved report with most recent modified date (testing environments may have duplicate years)
					SELECT master_facility_id, MAX(last_modified_date) AS modified_date
					FROM emissions_report
					WHERE status IN ('APPROVED', 'SUBMITTED')
					GROUP BY master_facility_id) AS mfr_rpt_modified
					ON mfr_rpt_modified.master_facility_id = e.master_facility_id 
					AND mfr_rpt_modified.modified_date = e.last_modified_date)) LOOP
	
				-- update master facility record with facility site info
				UPDATE master_facility_record SET 
					category_code = fs_info.category_code,
					status_code = fs_info.status_code,
					status_year = fs_info.status_year,
					tribal_code = fs_info.tribal_code,
					description = fs_info.description,
					mailing_street_address = fs_info.mailing_street_address,
					mailing_city = fs_info.mailing_city,
					mailing_state_code = fs_info.mailing_state_code,
					mailing_postal_code = fs_info.mailing_postal_code
				WHERE id = fs_info.mfr_id;

				-- insert facility naics into master facility naics xref
				FOR fs_naics IN (
					SELECT * FROM facility_naics_xref WHERE facility_site_id = fs_info.id) LOOP

						IF fs_naics IS NOT NULL THEN
							INSERT INTO master_facility_naics_xref (master_facility_id, naics_code, naics_code_type, created_by, created_date, last_modified_by, last_modified_date)
								VALUES (fs_info.mfr_id, fs_naics.naics_code, fs_naics.naics_code_type, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP);
						END IF;
					END LOOP;
		END LOOP;
 END;
 $$ LANGUAGE plpgsql;

 DO $$ 
 BEGIN
 PERFORM "migrate_fs_data_to_mfr"();
 DROP FUNCTION IF EXISTS migrate_fs_data_to_mfr();
 END;$$
 