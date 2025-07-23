-- drop tables to be updated
DROP VIEW IF EXISTS vw_emissions_by_facility_and_cas;

CREATE VIEW vw_emissions_by_facility_and_cas AS
SELECT concat(e.id, rpa.id) AS id,
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
    'TON'::text AS emissions_uom_code
FROM emission e
    JOIN reporting_period rp ON rp.id = e.reporting_period_id
    JOIN emissions_process ep ON ep.id = rp.emissions_process_id
    JOIN release_point_appt rpa ON rpa.emissions_process_id = ep.id
    JOIN emissions_unit eu ON eu.id = ep.emissions_unit_id
    JOIN facility_site fs ON fs.id = eu.facility_site_id
    JOIN emissions_report er ON er.id = fs.report_id
    JOIN release_point rpt ON rpt.id = rpa.release_point_id
    JOIN release_point_type_code rptc ON rptc.code::text = rpt.type_code::text
    JOIN pollutant p ON p.pollutant_code::text = e.pollutant_code::text
    LEFT JOIN eis_tri_xref etx ON etx.eis_id::text = er.eis_program_id::text
WHERE (er.status::text = ANY (ARRAY['SUBMITTED'::character varying::text, 'APPROVED'::character varying::text])) AND ep.status_code::text = 'OP'::text AND er.is_deleted = false AND e.not_reporting = false;