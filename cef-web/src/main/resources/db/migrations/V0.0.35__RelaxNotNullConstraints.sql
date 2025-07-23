ALTER TABLE emissions_unit
    ALTER COLUMN status_year DROP NOT NULL;

ALTER TABLE emissions_unit
    ALTER COLUMN program_system_code DROP NOT NULL;

ALTER TABLE emissions_unit
    ALTER COLUMN type_code_description DROP NOT NULL;
    
ALTER TABLE emissions_unit
    ALTER COLUMN design_capacity TYPE numeric (4, 0);
    
ALTER TABLE emissions_process
    ALTER COLUMN status_year DROP NOT NULL;

ALTER TABLE facility_site
    ALTER COLUMN status_year DROP NOT NULL;
    
ALTER TABLE facility_site
    ALTER COLUMN program_system_code DROP NOT NULL;
    
ALTER TABLE release_point
    ALTER COLUMN program_system_code DROP NOT NULL;