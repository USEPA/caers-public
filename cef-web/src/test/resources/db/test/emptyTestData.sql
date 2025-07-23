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
			