-- Emission Not Deleted
CREATE INDEX idx_emissions_report_is_not_deleted
ON emissions_report(id)
WHERE is_deleted = false;

-- Emission Reporting
CREATE INDEX idx_emission_is_reporting
ON emission(id)
WHERE not_reporting = false;

-- Release Point Type Code
CREATE INDEX idx_release_point_type_code
ON release_point(type_code);

-- Release Point Type Code
CREATE INDEX idx_release_point_type_code_lookup
ON release_point_type_code(code);

-- Pollutant Code
CREATE INDEX idx_pollutant_code
ON pollutant(pollutant_code);

CREATE INDEX idx_pollutant_cas_id
ON pollutant(pollutant_cas_id);
