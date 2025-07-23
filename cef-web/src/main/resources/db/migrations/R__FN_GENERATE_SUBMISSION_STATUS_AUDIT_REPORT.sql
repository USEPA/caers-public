
CREATE OR REPLACE FUNCTION fn_generate_submission_status_audit_report(slt varchar(5))
 RETURNS TABLE (id bigint, name varchar, program_system_code varchar, year smallint, submitted bigint, reopened bigint, rejected bigint) AS $$
BEGIN
  RETURN QUERY(SELECT row_number() OVER (ORDER BY er.program_system_code, er.year) AS id,
                      fs.name,
                      er.program_system_code,
                      er.year,
                      sum(case when rh.action = 'SUBMITTED' then 1 else 0 end) as submitted,
                      sum(case when rh.action = 'REOPENED' then 1 else 0 end) as reopened,
                      sum(case when rh.action = 'REJECTED' then 1 else 0 end) as rejected
                from emissions_report er
                inner join facility_site fs on fs.report_id = er.id
                inner join report_history rh on rh.report_id = er.id
                where (slt = 'ALL' or er.program_system_code = slt)
                and er.is_deleted = false
                group by er.program_system_code, er.year, fs.name
                order by er.program_system_code, er.year, fs.name);
END;
$$ LANGUAGE 'plpgsql';
