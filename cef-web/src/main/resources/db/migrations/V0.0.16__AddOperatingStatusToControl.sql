-- Add status code to control table
ALTER TABLE control
ADD COLUMN status_code character varying(20);

ALTER TABLE control 
ADD CONSTRAINT control_status_code_fkey FOREIGN KEY (status_code) REFERENCES operating_status_code (code);
