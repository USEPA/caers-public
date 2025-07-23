ALTER TABLE control
    ALTER COLUMN comments TYPE character varying(400);
    
ALTER TABLE emission
    ALTER COLUMN comments TYPE character varying(400);
    
ALTER TABLE emissions_process
    ALTER COLUMN comments TYPE character varying(400);
    
ALTER TABLE emissions_unit
    ALTER COLUMN comments TYPE character varying(400);

ALTER TABLE release_point
    ALTER COLUMN comments TYPE character varying(400);
    
ALTER TABLE reporting_period
    ALTER COLUMN comments TYPE character varying(400);