
CREATE OR REPLACE FUNCTION fn_generate_non_point_fuel_subtraction_report(report_year numeric, slt varchar(5))
 RETURNS TABLE (id bigint, non_point_bucket varchar, sector text, fuel varchar, scc_code varchar, description text, fuel_use numeric, non_point_standardized_uom varchar, unit_description varchar) AS $$
BEGIN
  RETURN QUERY(SELECT row_number() OVER (ORDER BY scc.code, scc.non_point_bucket) AS id,
			   			scc.non_point_bucket,
						CASE WHEN scc.non_point_bucket IN ('11', '21', '23', '31', '32', '33') THEN 'Industrial' ELSE 'Commercial' END AS Sector,
						scc.fuel, scc.code,
						scc.description,
						CASE WHEN fuel.fuel_use IS NULL THEN 0 ELSE fuel.fuel_use END,
						scc.non_point_standardized_uom, scc.uom
				FROM (
					SELECT nc.non_point_bucket, ep.scc_code, SUM(rp.standardized_non_point_fuel_use) AS fuel_use
						FROM emissions_process ep
							JOIN emissions_unit eu ON ep.emissions_unit_id = eu.id
							JOIN facility_site fs ON eu.facility_site_id = fs.id
							JOIN emissions_report er ON fs.report_id = er.id
							JOIN reporting_period rp ON rp.emissions_process_id = ep.id
							JOIN point_source_scc_code pscc ON pscc.code = ep.scc_code
							JOIN facility_naics_xref fnx ON fnx.facility_site_id = fs.id
							JOIN naics_code nc ON nc.code = fnx.naics_code
						WHERE nc.non_point_bucket IS NOT NULL
							AND fnx.naics_code_type = 'PRIMARY'
							AND pscc.fuel_use_types IS NOT NULL
							AND er.status = 'APPROVED'
							AND er.year = report_year --param
						    AND er.is_deleted = false
							AND er.program_system_code = slt --param
						    AND ep.status_code::text = 'OP'::text
						GROUP BY nc.non_point_bucket, ep.scc_code
					) fuel
				RIGHT JOIN (
					SELECT nc.non_point_bucket,
						pscc.code,
						CONCAT(pscc.scc_level_one, ';', pscc.scc_level_two, ';', pscc.scc_level_three, ';', pscc.scc_level_four) AS description,
						CASE
							WHEN cmc.code IN ('56', '823', '58', '825', '57') THEN 'Distillate Oil'
							WHEN cmc.code IN ('279', '922', '923', '924', '2') THEN 'Residual Oil'
							WHEN cmc.code IN ('717', '640', '639', '663', '323' ,'664', '173') THEN 'Coal'
							WHEN cmc.code IN ('162', '864') THEN 'Kerosene'
							WHEN cmc.code IN ('178', '255', '256', '675') THEN 'LPG'
							ELSE cmc.description
						END AS fuel,
						cmc.non_point_standardized_uom,
						umc.description AS uom
					FROM (SELECT DISTINCT naics.non_point_bucket FROM naics_code naics WHERE naics.non_point_bucket IS NOT NULL) nc,
					point_source_scc_code pscc
						JOIN calculation_material_code cmc ON cmc.code = pscc.calculation_material_code
						JOIN unit_measure_code umc ON umc.code = cmc.non_point_standardized_uom
					WHERE pscc.fuel_use_types IS NOT NULL
					) scc ON fuel.scc_code = scc.code AND fuel.non_point_bucket = scc.non_point_bucket
				ORDER BY scc.code, scc.non_point_bucket
			  );
END;
$$ LANGUAGE 'plpgsql';
