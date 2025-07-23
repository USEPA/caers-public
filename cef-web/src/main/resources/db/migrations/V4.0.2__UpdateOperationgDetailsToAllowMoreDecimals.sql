ALTER TABLE operating_detail
    ALTER COLUMN actual_hours_per_period TYPE numeric(9, 5),
    ALTER COLUMN avg_hours_per_day TYPE numeric(7, 5),
    ALTER COLUMN avg_days_per_week TYPE numeric(6, 5),
    ALTER COLUMN avg_weeks_per_period TYPE numeric(7, 5);