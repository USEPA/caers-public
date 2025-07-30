/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
TRUNCATE TABLE MASTER_FACILITY_RECORD CASCADE;
TRUNCATE TABLE CONTROL_PATH CASCADE;
TRUNCATE TABLE SLT_CONFIG CASCADE;

INSERT INTO MASTER_FACILITY_RECORD (id, eis_program_id, agency_facility_id, category_code, source_type_code, name, description, status_code, status_year,
 program_system_code, street_address, city, county_code, state_code, country_code, postal_code, latitude, longitude, 
 mailing_street_address, mailing_city, mailing_state_code, mailing_postal_code,  created_by, created_date, last_modified_by, last_modified_date) 
    VALUES ('9999991', '9758611', '1301700008', 'CAP', '133', 'Gilman Building Products LLC', 'Pulp and Paper Processing Plant',
     'OP', '1985', '63JJJJ', '173 Peachtree Rd', 'Fitzgerald', '13313', '13' , '', '31750', '33.7490', '-84.3880', '173 Peachtree Rd', 'Fitzgerald', '13', '31750', 
    'user-id', current_timestamp, 'user-id', current_timestamp);
    
INSERT INTO SLT_CONFIG (name, value, program_system_code) VALUES 
			('slt-feature.industry-facility-naics.enabled', 'false', 'GADNR');
			