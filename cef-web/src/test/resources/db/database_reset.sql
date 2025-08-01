/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

--FUNCTIONS
DROP FUNCTION IF EXISTS fn_generate_report_summary(report_id_input bigint);
DROP FUNCTION IF EXISTS fn_generate_non_point_fuel_subtraction_report(report_year numeric, slt varchar(5));

--VIEWS
DROP VIEW IF EXISTS vw_emissions_by_facility_and_cas;
DROP VIEW IF EXISTS vw_submissions_review_dashboard;
DROP VIEW IF EXISTS vw_report_download;

--REPORTING TABLES
DROP TABLE IF EXISTS emission;
DROP TABLE IF EXISTS emission_formula_variable;
DROP TABLE IF EXISTS release_point_appt;
DROP TABLE IF EXISTS control_assignment;
DROP TABLE IF EXISTS control_path_pollutant;
DROP TABLE IF EXISTS control_path;
DROP TABLE IF EXISTS control_pollutant;
DROP TABLE IF EXISTS control;
DROP TABLE IF EXISTS operating_detail;
DROP TABLE IF EXISTS monthly_fuel_reporting;
DROP TABLE IF EXISTS reporting_period;
DROP TABLE IF EXISTS release_point;
DROP TABLE IF EXISTS emissions_process;
DROP TABLE IF EXISTS emissions_unit;
DROP TABLE IF EXISTS facility_naics_xref;
DROP TABLE IF EXISTS facility_site_contact;
DROP TABLE IF EXISTS facility_site;
DROP TABLE IF EXISTS report_history;
DROP TABLE IF EXISTS report_change;
DROP TABLE IF EXISTS attachment;
DROP TABLE IF EXISTS user_feedback;
DROP TABLE IF EXISTS communication;
DROP TABLE IF EXISTS eis_tri_xref;
DROP TABLE IF EXISTS emissions_report;
DROP TABLE IF EXISTS user_facility_association;
DROP TABLE IF EXISTS master_facility_naics_xref;
DROP TABLE IF EXISTS master_facility_record;
DROP TABLE IF EXISTS admin_properties;

--REFERENCE TABLES
DROP TABLE IF EXISTS emission_formula_variable_code;
DROP TABLE IF EXISTS emission_factor;
DROP TABLE IF EXISTS slt_emission_factor;
DROP TABLE IF EXISTS agency;
DROP TABLE IF EXISTS aircraft_engine_type_code;
DROP TABLE IF EXISTS calculation_parameter_type_code;
DROP TABLE IF EXISTS calculation_method_code;
DROP TABLE IF EXISTS contact_type_code;
DROP TABLE IF EXISTS control_measure_code;
DROP TABLE IF EXISTS coordinate_datasource_code;
DROP TABLE IF EXISTS eis_latlong_tolerance_lookup;
DROP TABLE IF EXISTS eis_transaction_attachment;
DROP TABLE IF EXISTS eis_transaction_history;
DROP TABLE IF EXISTS emissions_operating_type_code;
DROP TABLE IF EXISTS facility_category_code;
DROP TABLE IF EXISTS facility_source_type_code;
DROP TABLE IF EXISTS fips_country_code;
DROP TABLE IF EXISTS fips_county;
DROP TABLE IF EXISTS fips_state_code;
DROP TABLE IF EXISTS geographic_reference_point_code;
DROP TABLE IF EXISTS geometric_type_code;
DROP TABLE IF EXISTS hap_facility_category_code;
DROP TABLE IF EXISTS horizontal_collection_method_code;
DROP TABLE IF EXISTS horizontal_reference_datum_code;
DROP TABLE IF EXISTS naics_code;
DROP TABLE IF EXISTS naics_code_industry;
DROP TABLE IF EXISTS operating_status_code;
DROP TABLE IF EXISTS point_source_scc_code;
DROP TABLE IF EXISTS calculation_material_code;
DROP TABLE IF EXISTS pollutant;
DROP TABLE IF EXISTS region_code;
DROP TABLE IF EXISTS regulatory_code;
DROP TABLE IF EXISTS release_point_type_code;
DROP TABLE IF EXISTS reporting_period_code;
DROP TABLE IF EXISTS schema_version_cef;
DROP TABLE IF EXISTS slt_config;
DROP TABLE IF EXISTS slt_properties;
DROP TABLE IF EXISTS program_system_code;
DROP TABLE IF EXISTS tribal_code;
DROP TABLE IF EXISTS unit_measure_code;
DROP TABLE IF EXISTS unit_type_code;
DROP TABLE IF EXISTS verification_code;
DROP TABLE IF EXISTS vertical_collection_method_code;
DROP TABLE IF EXISTS vertical_reference_datum_code;

--SEQUENCES
DROP SEQUENCE IF EXISTS emission_factor_id_seq;
DROP SEQUENCE IF EXISTS emission_id_seq;
DROP SEQUENCE IF EXISTS emissions_process_id_seq;
DROP SEQUENCE IF EXISTS emissions_report_id_seq;
DROP SEQUENCE IF EXISTS emissions_unit_id_seq;
DROP SEQUENCE IF EXISTS facility_id_seq;
DROP SEQUENCE IF EXISTS operating_detail_id_seq;
DROP SEQUENCE IF EXISTS release_point_appt_id_seq;
DROP SEQUENCE IF EXISTS release_point_id_seq;
DROP SEQUENCE IF EXISTS reporting_period_id_seq;
DROP SEQUENCE IF EXISTS eis_transaction_attachment_id_seq;
DROP SEQUENCE IF EXISTS eis_transaction_history_id_seq;
DROP SEQUENCE IF EXISTS slt_config_id_seq;
