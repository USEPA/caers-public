DROP VIEW vw_emissions_by_facility_and_cas;

CREATE OR REPLACE VIEW vw_emissions_by_facility_and_cas
AS
 SELECT concat(e.id, rpa.id) AS id,
    fs.frs_facility_id,
    fs.name AS facility_name,
    er.year,
	er.status,
    p.pollutant_name,
    p.pollutant_cas_id,
    e.total_emissions * rpa.percent * 0.01 AS apportioned_emissions,
    rpt.release_point_identifier,
        CASE
            WHEN rptc.description::text = 'Fugitive'::text THEN 'fugitive'::text
            ELSE 'stack'::text
        END AS release_point_type,
    e.emissions_uom_code
   FROM emission e,
    reporting_period rp,
    emissions_process ep,
    facility_site fs,
    emissions_unit eu,
    emissions_report er,
    release_point rpt,
    release_point_appt rpa,
    release_point_type_code rptc,
    pollutant p
  WHERE (er.status::text = ANY (ARRAY['SUBMITTED'::character varying, 'APPROVED'::character varying]::text[])) 
  AND ep.status_code::text = 'OP'::text AND e.reporting_period_id = rp.id 
  AND rp.emissions_process_id = ep.id 
  AND ep.emissions_unit_id = eu.id 
  AND eu.facility_site_id = fs.id 
  AND fs.report_id = er.id 
  AND ep.id = rpa.emissions_process_id 
  AND rpa.release_point_id = rpt.id 
  AND rpt.type_code::text = rptc.code::text 
  AND e.pollutant_code::text = p.pollutant_code::text;