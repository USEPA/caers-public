ALTER TABLE facility
    RENAME TO facility_site;

ALTER TABLE facility_site
    RENAME CONSTRAINT facility_pkey TO facility_site_pkey;
    
ALTER TABLE emissions_unit
    RENAME facility_id TO facility_site_id;
    
ALTER TABLE release_point
    RENAME facility_id TO facility_site_id;
    
