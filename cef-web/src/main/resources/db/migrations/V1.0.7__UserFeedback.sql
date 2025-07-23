CREATE TABLE user_feedback
(
  id                    							SERIAL PRIMARY KEY,
  report_id 										BIGINT,
  easy_and_intuitive    							SMALLINT,
  data_entry_via_screens 							SMALLINT,
  data_entry_via_bulk_upload						SMALLINT,
  calculation_screens								SMALLINT,
  controls_and_control_paths						SMALLINT,
  quality_assurance_checks							SMALLINT,
  overall_reporting_time							SMALLINT,
  beneficial_functionality_description				VARCHAR(2000),
  difficult_application_functionality_description 	VARCHAR(2000),
  additional_features_or_enhancements_description 	VARCHAR(2000),
  created_by            							VARCHAR(255),
  created_date          							timestamp NOT NULL,
  last_modified_by									VARCHAR(255),
  last_modified_date								timestamp NOT NULL,
  FOREIGN KEY (report_id) REFERENCES emissions_report (id)
);