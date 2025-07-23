
CREATE OR REPLACE FUNCTION fn_generate_average_number_qas_report(slt varchar(5))
 RETURNS TABLE (id bigint, program_system_code varchar, year smallint, avg_errors numeric, avg_warnings numeric) AS $$
BEGIN
RETURN QUERY(SELECT row_number() OVER (ORDER BY er.program_system_code, er.year)  AS id,
             er.program_system_code,
             er.year,
             CASE WHEN (round(avg(max_qa_errors))) is null THEN 0 ELSE round(avg(max_qa_errors)) END as avg_errors,
             CASE WHEN (round(avg(max_qa_warnings))) is null THEN 0 ELSE round(avg(max_qa_warnings)) END as avg_warnings
	from emissions_report er
	where (slt = 'ALL' or er.program_system_code = slt)
    and er.is_deleted = false
	group by er.program_system_code, er.year
    order by er.program_system_code, er.year);
END;
$$ LANGUAGE 'plpgsql';
