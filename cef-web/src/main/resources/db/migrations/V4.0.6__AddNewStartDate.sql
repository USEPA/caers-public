ALTER TABLE emissions_process
ADD COLUMN IF NOT EXISTS start_date smallint;

ALTER TABLE emissions_unit
ADD COLUMN IF NOT EXISTS start_date smallint;

ALTER TABLE release_point
ADD COLUMN IF NOT EXISTS start_date smallint;

ALTER TABLE facility_site
ADD COLUMN IF NOT EXISTS start_date smallint;
