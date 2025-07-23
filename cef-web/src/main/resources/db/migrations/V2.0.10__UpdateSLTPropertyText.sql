
UPDATE admin_properties SET label = 'Facility Data Bulk Entry', description = 'Enables the Processes and Emissions data bulk entry tables inside the application.' WHERE name = 'feature.bulk-entry.enabled';
UPDATE admin_properties SET label = 'Bulk-Upload-Template Download', description = 'Allows certifiers and preparers to download their current year report template.' WHERE name = 'feature.excel-export.enabled';
UPDATE admin_properties SET description = 'Date of the last time SCCs in the application were updated given "Update Application SCCs" was enabled.' WHERE name = 'task.scc-update.last-ran';
UPDATE admin_properties SET label = 'User Feedback Survey', description = 'Allows NEI Certifiers to submit feedback on their reporting experience once per year following submission.' WHERE name = 'feature.user-feedback.enabled';
