CREATE OR REPLACE VIEW vw_report_download
AS
 SELECT row_number() OVER (ORDER BY p.pollutant_cas_id) AS id,
    er.id AS report_id,
    fs.alt_site_identifier AS facility_site_id,
    er.year AS inventory_year,
    eu.unit_identifier AS emissions_unit_id,
    eu.description AS emission_unit_description,
    ep.emissions_process_identifier AS process_id,
    ep.description AS process_description,
    p.pollutant_name,
    repcode.short_name AS reporting_period_type,
	cmc.description AS emission_calc_method,
    e.emissions_uom_code,
    e.emissions_numerator_uom,
    e.emissions_denominator_uom,
    e.emissions_factor,
    e.emissions_factor_text,
    e.comments AS emissions_comment,
    e.total_emissions
   FROM emission e
   	 JOIN calculation_method_code cmc ON cmc.code = e.emissions_calc_method_code
     JOIN reporting_period repper ON repper.id = e.reporting_period_id
     JOIN reporting_period_code repcode ON repper.reporting_period_type_code::text = repcode.code::text
     JOIN emissions_process ep ON ep.id = repper.emissions_process_id
     JOIN emissions_unit eu ON ep.emissions_unit_id = eu.id
     JOIN facility_site fs ON fs.id = eu.facility_site_id
     JOIN emissions_report er ON er.id = fs.report_id
     JOIN pollutant p ON p.pollutant_code::text = e.pollutant_code::text
  WHERE ep.status_code::text = 'OP'::text
  ORDER BY er.id, eu.unit_identifier, ep.emissions_process_identifier, p.pollutant_code;