/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
delete from control;
delete from facility_site;
delete from emissions_report;
delete from master_facility_record;
delete from emission_factor;
delete from control_measure_code;

INSERT INTO MASTER_FACILITY_RECORD (id, eis_program_id, agency_facility_id, category_code, source_type_code, name, description, status_code, status_year,
 program_system_code, street_address, city, county_code, state_code, country_code, postal_code, latitude, longitude, 
 mailing_street_address, mailing_city, mailing_state_code, mailing_postal_code,  created_by, created_date, last_modified_by, last_modified_date) 
    VALUES ('9999991', '9758611', '1301700008', 'CAP', '133', 'Gilman Building Products LLC', 'Pulp and Paper Processing Plant',
     'OP', '1985', '63JJJJ', '173 Peachtree Rd', 'Fitzgerald', '13313', '13' , '', '31750', '33.7490', '-84.3880', '173 Peachtree Rd', 'Fitzgerald', '13', '31750', 
    'user-id', current_timestamp, 'user-id', current_timestamp);

INSERT INTO emissions_report(id, eis_program_id, program_system_code, year, status, validation_status, master_facility_id, created_by, created_date, last_modified_by, last_modified_date)
 VALUES ('9999997', '9758611', 'GADNR', '2019', 'SUBMITTED', 'PASSED_WARNINGS', '9999991', 'user-id', current_timestamp, 'user-id', current_timestamp);

--FACILITY
INSERT INTO FACILITY_SITE (id, report_id, agency_facility_identifier, category_code, source_type_code, name, description, status_code, status_year,
 program_system_code, street_address, city, county_code, state_code, country_code, postal_code, latitude, longitude,
 mailing_street_address, mailing_city, mailing_state_code, mailing_postal_code,  created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999991', '9999997', '1301700008', 'CAP', '133', 'Gilman Building Products LLC', 'Pulp and Paper Processing Plant',
     'OP', '1985', '63JJJJ', '173 Peachtree Rd', 'Fitzgerald', '13313', 'GA' , '', '31750', '33.7490', '-84.3880', '173 Peachtree Rd', 'Fitzgerald', 'GA', '31750',
    'user-id', current_timestamp, 'user-id', current_timestamp);

INSERT INTO control_measure_code (code, description) VALUES ('1','Wet Scrubber - High Efficiency');
INSERT INTO control_measure_code (code, description) VALUES ('2','Wet Scrubber - Medium Efficiency');
    
INSERT INTO CONTROL (id, facility_site_id, status_code, identifier, description, percent_capture, percent_control, created_by, created_date, last_modified_by, last_modified_date, comments, control_measure_code)
    VALUES ('9999991', '9999991', 'OP', 'Control 001', 'Acetaldehyde and Benzene Control', 50, 50, 'user-id', current_timestamp, 'user-id', current_timestamp,
    'Sample comments for Control 001', '1');

INSERT INTO CONTROL (id, facility_site_id, status_code, identifier, description, percent_capture, percent_control, created_by, created_date, last_modified_by, last_modified_date, comments, control_measure_code)
    VALUES ('9999992', '9999991', 'OP', 'Control 002', 'Acetaldehyde Control', 25, 75, 'user-id', current_timestamp, 'user-id', current_timestamp,
    'Sample comments for Control 002', '2');
