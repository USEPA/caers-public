
CREATE INDEX "i_agency_facility_identifier" ON facility_site (agency_facility_identifier);
CREATE INDEX "i_process_identifier" ON emissions_process (emissions_process_identifier);
CREATE INDEX "i_unit_id" ON emissions_process (emissions_unit_id);
CREATE INDEX "i_unit_identifier" ON emissions_unit (unit_identifier);
CREATE INDEX "i_release_point_identifier" ON release_point (release_point_identifier);
CREATE INDEX "i_emissions_process_id" ON reporting_period (emissions_process_id);
CREATE INDEX "i_reporting_period_id" ON emission (reporting_period_id);
CREATE INDEX "i_scc_pollutant_control" ON emission_factor (scc_code,pollutant_code,control_indicator);
CREATE INDEX "i_control_identifier" ON control (identifier);
CREATE INDEX "i_report_id" ON facility_site (report_id);
CREATE INDEX "i_master_facility_id" ON emissions_report (master_facility_id);
