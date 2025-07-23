ALTER TABLE emissions_report ADD COLUMN mid_year_sub_status VARCHAR(255);

ALTER TABLE monthly_fuel_reporting ADD COLUMN mid_year_submitted boolean DEFAULT false;
