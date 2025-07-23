ALTER TABLE control
    ADD COLUMN comments character varying(2000);
    
ALTER TABLE release_point
    ADD COLUMN comments character varying(2000);
    
ALTER TABLE emissions_unit
    ADD COLUMN comments character varying(2000);
    
ALTER TABLE emissions_process
    ADD COLUMN comments character varying(2000);
   