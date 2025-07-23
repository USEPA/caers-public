
UPDATE report_history
SET action = 'OPTED_OUT'
WHERE report_id IN (select id from emissions_report where threshold_status = 'OPERATING_BELOW_THRESHOLD')
AND action = 'COPIED_FWD';