UPDATE emissions_report SET eis_last_sub_status = 'NotStarted'
WHERE eis_last_sub_status IS NULL;
