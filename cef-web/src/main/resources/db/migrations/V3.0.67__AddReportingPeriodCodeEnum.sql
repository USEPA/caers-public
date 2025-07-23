
ALTER TABLE reporting_period_code ADD COLUMN reporting_period_code_type varchar(20);

UPDATE reporting_period_code SET reporting_period_code_type = 'MONTHLY' WHERE code in ('APR','AUG','DEC','FEB','JAN','JUL','JUN','MAR','MAY','NOV','OCT','SEP');
UPDATE reporting_period_code SET reporting_period_code_type = 'ANNUAL' WHERE code in ('A');
UPDATE reporting_period_code SET reporting_period_code_type = 'SEMIANNUAL' WHERE code in ('SA');
UPDATE reporting_period_code SET reporting_period_code_type = 'OTHER' WHERE code in ('5MN','E','O3D','W','ASD');
