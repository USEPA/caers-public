/*
 * © Copyright 2019 EPA CAERS Project Team
 *
 * This file is part of the Common Air Emissions Reporting System (CAERS).
 *
 * CAERS is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 *
 * CAERS is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with CAERS.  If 
 * not, see <https://www.gnu.org/licenses/>.
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
