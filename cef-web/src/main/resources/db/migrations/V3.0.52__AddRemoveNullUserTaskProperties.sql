
insert into admin_properties (name, value, label, description, datatype, required) values (
    'task.remove-null-users.last-ran', '2022-07-15', 'Remove Null Users Task Last Updated', 'Date when Remove Null Users task was last run', 'string', true);
insert into admin_properties (name, value, label, description, datatype, required) values (
    'task.remove-null-users.enabled', 'true', 'Remove Null Users Task Enabled', 'Enables Remove Null Users Task', 'boolean', true);
insert into admin_properties (name, value, label, description, datatype, required) values (
    'task.remove-null-users.cron', '0 0 4 ? * *', 'Remove Null Users CRON', 'The cron expression used for the Remove Null Users task', 'string', true);
