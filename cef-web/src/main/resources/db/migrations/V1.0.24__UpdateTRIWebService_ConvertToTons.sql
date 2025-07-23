DROP VIEW IF EXISTS vw_emissions_by_facility_and_cas;

CREATE OR REPLACE VIEW vw_emissions_by_facility_and_cas
    WITH (security_barrier=false)
    AS
    SELECT concat(e.id, rpa.id) AS id,
    fs.frs_facility_id,
	etx.trifid,
    fs.name AS facility_name,
    er.year,
    er.status,
    er.id AS report_id,
    p.pollutant_name,
    p.pollutant_cas_id,
    e.calculated_emissions_tons * rpa.percent * 0.01 AS apportioned_emissions,
    rpt.release_point_identifier,
        CASE
            WHEN rptc.description::text = 'Fugitive'::text THEN 'fugitive'::text
            ELSE 'stack'::text
        END AS release_point_type,
    'TON' as emissions_uom_code
   FROM emission e
    join reporting_period rp on rp.id = e.reporting_period_id
    join emissions_process ep on ep.id = rp.emissions_process_id
	join release_point_appt rpa on rpa.emissions_process_id = ep.id
	join emissions_unit eu on eu.id = ep.emissions_unit_id
	join facility_site fs on fs.id = eu.facility_site_id
    join emissions_report er on er.id = fs.report_id
    join release_point rpt on rpt.id = rpa.release_point_id
    join release_point_type_code rptc on rptc.code::text = rpt.type_code::text
    join pollutant p on p.pollutant_code::text = e.pollutant_code::text
	left join eis_tri_xref etx on etx.eis_id = er.eis_program_id
  WHERE (er.status::text = ANY (ARRAY['SUBMITTED'::character varying::text, 'APPROVED'::character varying::text])) 
  AND ep.status_code::text = 'OP'::text;