ALTER TABLE master_facility_record
    ALTER COLUMN agency_facility_id SET NOT NULL;
    
ALTER TABLE facility_site
    ALTER COLUMN alt_site_identifier SET NOT NULL;