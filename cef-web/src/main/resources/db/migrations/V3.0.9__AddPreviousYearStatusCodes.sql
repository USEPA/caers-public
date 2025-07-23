
ALTER TABLE release_point
ADD COLUMN previous_year_status_code varchar(20); 

ALTER TABLE release_point
ADD CONSTRAINT release_point_previous_year_status_code_fkey FOREIGN KEY (previous_year_status_code) REFERENCES operating_status_code (code);


ALTER TABLE emissions_unit
ADD COLUMN previous_year_status_code varchar(20); 

ALTER TABLE emissions_unit
ADD CONSTRAINT emissions_unit_previous_year_status_code_fkey FOREIGN KEY (previous_year_status_code) REFERENCES operating_status_code (code);


ALTER TABLE emissions_process
ADD COLUMN previous_year_status_code varchar(20); 

ALTER TABLE emissions_process
ADD CONSTRAINT emissions_process_previous_year_status_code_fkey FOREIGN KEY (previous_year_status_code) REFERENCES operating_status_code (code);


ALTER TABLE control
ADD COLUMN previous_year_status_code varchar(20); 

ALTER TABLE control
ADD CONSTRAINT control_previous_year_status_code_fkey FOREIGN KEY (previous_year_status_code) REFERENCES operating_status_code (code);