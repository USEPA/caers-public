-- Add unit of measure to emissions_unit table
ALTER TABLE emissions_unit
ADD COLUMN unit_measure_cd VARCHAR(20);

ALTER TABLE emissions_unit 
ADD CONSTRAINT unit_measure_cd_fkey FOREIGN KEY (unit_measure_cd) REFERENCES unit_measure_code (code);
