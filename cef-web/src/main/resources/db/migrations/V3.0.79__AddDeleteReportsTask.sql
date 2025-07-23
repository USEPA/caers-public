
alter table emissions_report add column is_deleted boolean default false;

insert into admin_properties (name, value, label, description, datatype, required) values (
    'task.delete-reports.last-ran', '2023-06-28', 'Delete Reports Task Last Updated', 'Date when Delete Reports task was last run', 'string', true);
insert into admin_properties (name, value, label, description, datatype, required) values (
    'task.delete-reports.enabled', 'true', 'Delete Reports Task Enabled', 'Enables the Delete Reports Users Task', 'boolean', true);
insert into admin_properties (name, value, label, description, datatype, required) values (
    'task.delete-reports.cron', '0 0 1 ? * *', 'Delete Reports CRON', 'The cron expression used for the Delete Reports task', 'string', true);
