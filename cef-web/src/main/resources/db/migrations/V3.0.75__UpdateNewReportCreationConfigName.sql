
UPDATE admin_properties
SET name = 'new-report-creation.enabled', label = 'New Report Creation', description = 'Disable to prevent new reports from being created.'
WHERE name = 'current-year-reporting.enabled';

ALTER TABLE slt_config
DROP CONSTRAINT name_fkey;

UPDATE slt_config
SET name = 'slt.feature.new-report-creation.enabled'
WHERE name = 'slt.feature.current-year-reporting.enabled';

UPDATE slt_properties
SET name = 'slt.feature.new-report-creation.enabled', label = 'New Report Creation', description = 'Disable to prevent new reports from being created.'
WHERE name = 'slt.feature.current-year-reporting.enabled';

ALTER TABLE slt_config ADD CONSTRAINT name_fkey FOREIGN KEY (name) REFERENCES slt_properties (name);
