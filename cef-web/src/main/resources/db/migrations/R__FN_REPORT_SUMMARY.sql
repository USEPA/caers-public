-- Drop the existing function if it exists
DROP FUNCTION IF EXISTS fn_generate_report_summary(bigint);

CREATE OR REPLACE FUNCTION fn_generate_report_summary(report_id_input bigint)
RETURNS TABLE(
    id bigint,
    pollutant_code character varying(12),
    pollutant_cas_id character varying(100),
    pollutant_name character varying(200),
    pollutant_type character varying(12),
    fugitive_tons_total numeric,
    stack_tons_total numeric,
    fugitive_total numeric,
    stack_total numeric,
    emissions_tons_total numeric,
    emissions_total numeric,
    previous_year_tons_total numeric,
    previous_year_total numeric,
    report_year smallint,
    facility_site_id bigint,
    previous_year smallint,
    not_reporting boolean
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT row_number() OVER (ORDER BY p.pollutant_cas_id) AS id,
        p.pollutant_code,
        p.pollutant_cas_id,
        p.pollutant_name,
        p.pollutant_type,
        COALESCE(rtrim(fugitive_amount.calculated_emissions_tons::text, '00'::text)::numeric, 0::numeric) AS fugitive_tons_total,
        COALESCE(rtrim(stack_amount.calculated_emissions_tons::text, '00'::text)::numeric, 0::numeric) AS stack_tons_total,
        COALESCE(rtrim(fugitive_amount.total_emissions::text, '00'::text)::numeric, 0::numeric) AS fugitive_total,
        COALESCE(rtrim(stack_amount.total_emissions::text, '00'::text)::numeric, 0::numeric) AS stack_total,
        COALESCE(rtrim(sum(e.calculated_emissions_tons * rpa.percent / 100::numeric)::text, '00'::text)::numeric, 0::numeric) AS emissions_tons_total,
        COALESCE(rtrim(sum(e.total_emissions * rpa.percent / 100::numeric)::text, '00'::text)::numeric, 0::numeric) AS emissions_total,
        COALESCE(rtrim(previous_year_total.calculated_emissions_tons::text, '00'::text)::numeric, 0::numeric) AS previous_year_tons_total,
        rtrim(previous_year_total.total_emissions::text, '00'::text)::numeric AS previous_year_total,
        er.year AS report_year,
        fs.id AS facility_site_id,
        previous_year_total.year AS previous_year,
        e.not_reporting
    FROM emission e
        JOIN reporting_period repper ON repper.id = e.reporting_period_id
        JOIN emissions_process ep ON ep.id = repper.emissions_process_id
        JOIN release_point_appt rpa ON ep.id = rpa.emissions_process_id
        JOIN release_point rp ON rp.id = rpa.release_point_id
        JOIN release_point_type_code rptc ON rptc.code::text = rp.type_code::text
        JOIN facility_site fs ON fs.id = rp.facility_site_id
        JOIN emissions_report er ON er.id = fs.report_id
        JOIN pollutant p ON p.pollutant_code::text = e.pollutant_code::text
        LEFT JOIN (
            SELECT sum(e_0.calculated_emissions_tons * rpa_0.percent / 100::numeric) AS calculated_emissions_tons,
                sum(e_0.total_emissions * rpa_0.percent / 100::numeric) AS total_emissions,
                er_0.year,
                fs_0.id AS facility_id,
                p_0.pollutant_cas_id,
                p_0.pollutant_code,
                rptc_0.category
            FROM emission e_0
                JOIN reporting_period repper_0 ON repper_0.id = e_0.reporting_period_id
                JOIN emissions_process ep_0 ON ep_0.id = repper_0.emissions_process_id
                JOIN release_point_appt rpa_0 ON ep_0.id = rpa_0.emissions_process_id
                JOIN release_point rp_0 ON rp_0.id = rpa_0.release_point_id
                JOIN release_point_type_code rptc_0 ON rptc_0.code::text = rp_0.type_code::text
                JOIN facility_site fs_0 ON fs_0.id = rp_0.facility_site_id
                JOIN emissions_report er_0 ON er_0.id = fs_0.report_id
                JOIN pollutant p_0 ON p_0.pollutant_code::text = e_0.pollutant_code::text
            WHERE rptc_0.category::text = 'Fugitive'::text
                AND ep_0.status_code::text = 'OP'::text
                AND er_0.is_deleted = false
                AND repper_0.reporting_period_type_code = 'A'
            GROUP BY er_0.year, p_0.pollutant_cas_id, p_0.pollutant_code, fs_0.id, rptc_0.category
        ) fugitive_amount ON fugitive_amount.year = er.year
        AND fugitive_amount.pollutant_code::text = p.pollutant_code::text
        AND fugitive_amount.facility_id = fs.id
        LEFT JOIN (
            SELECT sum(e_1.calculated_emissions_tons * rpa_1.percent / 100::numeric) AS calculated_emissions_tons,
                sum(e_1.total_emissions * rpa_1.percent / 100::numeric) AS total_emissions,
                er_1.year,
                fs_1.id AS facility_id,
                p_1.pollutant_cas_id,
                p_1.pollutant_code
            FROM emission e_1
                JOIN reporting_period repper_1 ON repper_1.id = e_1.reporting_period_id
                JOIN emissions_process ep_1 ON ep_1.id = repper_1.emissions_process_id
                JOIN release_point_appt rpa_1 ON ep_1.id = rpa_1.emissions_process_id
                JOIN release_point rp_1 ON rp_1.id = rpa_1.release_point_id
                JOIN release_point_type_code rptc_1 ON rptc_1.code::text = rp_1.type_code::text
                JOIN facility_site fs_1 ON fs_1.id = rp_1.facility_site_id
                JOIN emissions_report er_1 ON er_1.id = fs_1.report_id
                JOIN pollutant p_1 ON p_1.pollutant_code::text = e_1.pollutant_code::text
            WHERE rptc_1.category::text <> 'Fugitive'::text
                AND ep_1.status_code::text = 'OP'::text
                AND repper_1.reporting_period_type_code = 'A'
            GROUP BY er_1.year, p_1.pollutant_cas_id, p_1.pollutant_code, fs_1.id
        ) stack_amount ON stack_amount.year = er.year
        AND stack_amount.pollutant_code::text = p.pollutant_code::text
        AND stack_amount.facility_id = fs.id
        LEFT JOIN (
            SELECT
                previous_emissions.master_facility_id,
                previous_emissions.year,
                previous_emissions.pollutant_code,
                previous_emissions.calculated_emissions_tons,
                previous_emissions.total_emissions
            FROM (
                SELECT
                    er_2.master_facility_id,
                    er_2.year,
                    p_2.pollutant_code,
                    SUM(e_2.calculated_emissions_tons * rpa_2.percent / 100.0) AS calculated_emissions_tons,
                    SUM(e_2.total_emissions * rpa_2.percent / 100.0) AS total_emissions
                FROM emission e_2
                JOIN reporting_period repper_2
                    ON repper_2.id = e_2.reporting_period_id
                JOIN emissions_process ep_2
                    ON ep_2.id = repper_2.emissions_process_id
                JOIN release_point_appt rpa_2
                    ON ep_2.id = rpa_2.emissions_process_id
                JOIN release_point rp_2
                    ON rp_2.id = rpa_2.release_point_id
                JOIN facility_site fs_2
                    ON fs_2.id = rp_2.facility_site_id
                JOIN emissions_report er_2
                    ON er_2.id = fs_2.report_id
                JOIN pollutant p_2
                    ON p_2.pollutant_code = e_2.pollutant_code
                WHERE ep_2.status_code = 'OP'
                GROUP BY
                    er_2.master_facility_id,
                    er_2.year,
                    p_2.pollutant_code
                ) previous_emissions
                INNER JOIN (
                    SELECT
                        er_inner.master_facility_id,
                        MAX(er_inner.year) AS max_year
                    FROM emissions_report er_inner
                    JOIN emissions_report er_outer
                        ON er_inner.master_facility_id = er_outer.master_facility_id
                        AND er_outer.id = report_id_input
                        AND er_inner.year < er_outer.year
                        AND COALESCE(er_inner.threshold_status, '') != 'OPERATING_BELOW_THRESHOLD'
                    GROUP BY er_inner.master_facility_id, er_outer.year
                ) max_years
            ON previous_emissions.master_facility_id = max_years.master_facility_id
            AND previous_emissions.year = max_years.max_year
            ) previous_year_total
            ON previous_year_total.pollutant_code = p.pollutant_code
        AND previous_year_total.master_facility_id = er.master_facility_id
    WHERE ep.status_code::text = 'OP'::text
        AND er.id = report_id_input
        AND repper.reporting_period_type_code = 'A'
    GROUP BY er.year, fs.id, p.pollutant_code, p.pollutant_cas_id, p.pollutant_name, p.pollutant_type,
        (COALESCE(rtrim(fugitive_amount.calculated_emissions_tons::text, '00'::text)::numeric, 0::numeric)),
        (COALESCE(rtrim(stack_amount.calculated_emissions_tons::text, '00'::text)::numeric, 0::numeric)),
        (COALESCE(rtrim(fugitive_amount.total_emissions::text, '00'::text)::numeric, 0::numeric)),
        (COALESCE(rtrim(stack_amount.total_emissions::text, '00'::text)::numeric, 0::numeric)),
        (COALESCE(rtrim(previous_year_total.calculated_emissions_tons::text, '00'::text)::numeric, 0::numeric)),
        previous_year_total.total_emissions, previous_year_total.year, e.not_reporting
    ORDER BY p.pollutant_name;
END;
$$
