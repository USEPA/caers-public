ALTER TABLE emission ADD COLUMN pollutant_type VARCHAR(10) NOT NULL DEFAULT 'HAP'; 

CREATE OR REPLACE VIEW vw_report_summary AS 
SELECT ROW_NUMBER() OVER (ORDER BY e.pollutant_cas_id) AS id, e.pollutant_cas_id AS pollutant_cas_id, e.pollutant_name AS pollutant_name, e.pollutant_type AS pollutant_type, 
    COALESCE(fugitive_amount.calculated_emissions_tons, 0) AS fugitive_total, 
    COALESCE(stack_amount.calculated_emissions_tons, 0) AS stack_total, 'tons' as uom, 
    COALESCE(SUM(e.calculated_emissions_tons), 0) AS emissions_tons_total, 
    COALESCE(previous_year_total.calculated_emissions_tons, 0) AS previous_year_total, er.year AS report_year, fs.id AS facility_site_id
FROM emission e
    INNER JOIN reporting_period repPer ON repPer.id = e.reporting_period_id
    INNER JOIN emissions_process ep ON ep.id = repPer.emissions_process_id
    INNER JOIN release_point_appt rpa ON ep.id = rpa.emissions_process_id
    INNER JOIN release_point rp ON rp.id = rpa.release_point_id
    INNER JOIN release_point_type_code rptc ON rptc.code = rp.type_code
    INNER JOIN facility_site fs ON fs.id = rp.facility_site_id
    INNER JOIN emissions_report er ON er.id = fs.report_id
    LEFT OUTER JOIN (SELECT sum(e.calculated_emissions_tons) AS calculated_emissions_tons, er.year, fs.id AS facility_id, e.pollutant_cas_id
                FROM emission e
                    INNER JOIN reporting_period repPer ON repPer.id = e.reporting_period_id
                    INNER JOIN emissions_process ep ON ep.id = repPer.emissions_process_id
                    INNER JOIN release_point_appt rpa ON ep.id = rpa.emissions_process_id
                    INNER JOIN release_point rp ON rp.id = rpa.release_point_id
                    INNER JOIN release_point_type_code rptc ON rptc.code = rp.type_code
                    INNER JOIN facility_site fs ON fs.id = rp.facility_site_id
                    INNER JOIN emissions_report er ON er.id = fs.report_id
                WHERE rptc.description = 'Fugitive'
                GROUP BY er.year, e.pollutant_cas_id, fs.id) AS fugitive_amount ON fugitive_amount.year = er.year AND fugitive_amount.pollutant_cas_id = e.pollutant_cas_id AND fugitive_amount.facility_id = fs.id
    LEFT OUTER JOIN (SELECT sum(e.calculated_emissions_tons) AS calculated_emissions_tons, er.year, fs.id AS facility_id, e.pollutant_cas_id
                FROM emission e
                    INNER JOIN reporting_period repPer ON repPer.id = e.reporting_period_id
                    INNER JOIN emissions_process ep ON ep.id = repPer.emissions_process_id
                    INNER JOIN release_point_appt rpa ON ep.id = rpa.emissions_process_id
                    INNER JOIN release_point rp ON rp.id = rpa.release_point_id
                    INNER JOIN release_point_type_code rptc ON rptc.code = rp.type_code
                    INNER JOIN facility_site fs ON fs.id = rp.facility_site_id
                    INNER JOIN emissions_report er ON er.id = fs.report_id
                WHERE rptc.description != 'Fugitive'
                GROUP BY er.year, e.pollutant_cas_id, fs.id) AS stack_amount ON stack_amount.year = er.year AND stack_amount.pollutant_cas_id = e.pollutant_cas_id AND stack_amount.facility_id = fs.id
    LEFT OUTER JOIN (SELECT sum(e.calculated_emissions_tons) AS calculated_emissions_tons, er.year, e.pollutant_cas_id, fs.id AS facility_id
                FROM emission e
                    INNER JOIN reporting_period repPer ON repPer.id = e.reporting_period_id
                    INNER JOIN emissions_process ep ON ep.id = repPer.emissions_process_id
                    INNER JOIN release_point_appt rpa ON ep.id = rpa.emissions_process_id
                    INNER JOIN release_point rp ON rp.id = rpa.release_point_id
                    INNER JOIN release_point_type_code rptc ON rptc.code = rp.type_code
                    INNER JOIN facility_site fs ON fs.id = rp.facility_site_id
                    INNER JOIN emissions_report er ON er.id = fs.report_id
                GROUP BY er.year, e.pollutant_cas_id, fs.id) AS previous_year_total ON previous_year_total.year = (er.year - 1) AND previous_year_total.facility_id = fs.id
GROUP BY er.year, fs.id, e.pollutant_cas_id, e.pollutant_name, uom, pollutant_type, fugitive_total, stack_total, previous_year_total;
