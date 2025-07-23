CREATE INDEX "i_emissions_process_id_release_point_appt"
    on release_point_appt (emissions_process_id);

CREATE INDEX "i_reporting_period_id_operating_detail"
    on operating_detail (reporting_period_id);

CREATE INDEX "i_facility_site_id_emissions_unit"
    on emissions_unit (facility_site_id);
