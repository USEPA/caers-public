DROP INDEX IF EXISTS "i_quality_check_results_emission_report_id";

CREATE INDEX "i_emissions_process_status_code"
    on emissions_process (status_code);

CREATE INDEX "i_reporting_period_type_code"
    on reporting_period (reporting_period_type_code);

CREATE INDEX "i_emission_formula_variable_emission_id"
    on emission_formula_variable (emission_id);

CREATE INDEX "i_release_point_appt_control_path_id"
    on release_point_appt (control_path_id);

CREATE INDEX "i_release_point_facility_site_id"
    on release_point (facility_site_id);
