ALTER TABLE control_pollutant
    ADD COLUMN percent_reduction numeric(4, 1);
    
ALTER TABLE control
    ALTER COLUMN percent_capture TYPE numeric (4, 1);

ALTER TABLE control
    ALTER COLUMN percent_control TYPE numeric (4, 1);