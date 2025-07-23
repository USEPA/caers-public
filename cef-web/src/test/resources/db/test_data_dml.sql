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

TRUNCATE TABLE EMISSIONS_REPORT CASCADE;
TRUNCATE TABLE CONTROL_PATH CASCADE;

--EMISSION REPORTS
INSERT INTO emissions_report(id, eis_program_id, program_system_code, year, status, validation_status, created_by, created_date, last_modified_by, last_modified_date, master_facility_id)
 VALUES ('9999997', '7415011', 'GADNR', '2018', 'IN_PROGRESS', 'PASSED_WARNINGS', 'user-id', current_timestamp, 'user-id', current_timestamp,'16');
INSERT INTO emissions_report(id, eis_program_id, program_system_code, year, status, validation_status, created_by, created_date, last_modified_by, last_modified_date, master_facility_id)
 VALUES ('9999996', '7415011', 'GADNR', '2014', 'APPROVED', 'PASSED', 'user-id', current_timestamp, 'user-id', current_timestamp,'16');
INSERT INTO emissions_report(id, eis_program_id, program_system_code, year, status, validation_status, created_by, created_date, last_modified_by, last_modified_date, master_facility_id)
 VALUES ('9999995', '7415011', 'GADNR', '2017', 'APPROVED', 'PASSED', 'user-id', current_timestamp, 'user-id', current_timestamp,'16');
INSERT INTO emissions_report(id, eis_program_id, program_system_code, year, status, validation_status, created_by, created_date, last_modified_by, last_modified_date, master_facility_id)
 VALUES ('9999994', '7415011', 'GADNR', '2016', 'APPROVED', 'PASSED', 'user-id', current_timestamp, 'user-id', current_timestamp,'16');
INSERT INTO emissions_report(id, eis_program_id, program_system_code, year, status, validation_status, created_by, created_date, last_modified_by, last_modified_date, master_facility_id)
 VALUES ('9999993', '7415011', 'GADNR', '2015', 'APPROVED', 'PASSED', 'user-id', current_timestamp, 'user-id', current_timestamp,'16');
INSERT INTO emissions_report(id, eis_program_id, program_system_code, year, status, validation_status, created_by, created_date, last_modified_by, last_modified_date, master_facility_id)
 VALUES ('9999998', '3711211', 'GADNR', '2013', 'APPROVED', 'PASSED', 'user-id', current_timestamp, 'user-id', current_timestamp,'67');
 INSERT INTO emissions_report(id, eis_program_id, program_system_code, year, status, validation_status, created_by, created_date, last_modified_by, last_modified_date, master_facility_id)
 VALUES ('9999999', '3711211', 'GADNR', '2018', 'SUBMITTED', 'PASSED', 'user-id', current_timestamp, 'user-id', current_timestamp,'67');
INSERT INTO emissions_report(id, eis_program_id, program_system_code, year, status, validation_status, created_by, created_date, last_modified_by, last_modified_date, master_facility_id)
 VALUES ('9999910', '3711211', 'GADNR', '2017', 'APPROVED', 'PASSED', 'user-id', current_timestamp, 'user-id', current_timestamp,'67');
INSERT INTO emissions_report(id, eis_program_id, program_system_code, year, status, validation_status, created_by, created_date, last_modified_by, last_modified_date, master_facility_id)
 VALUES ('9999911', '3711211', 'GADNR', '2016', 'APPROVED', 'PASSED', 'user-id', current_timestamp, 'user-id', current_timestamp,'67');
INSERT INTO emissions_report(id, eis_program_id, program_system_code, year, status, validation_status, created_by, created_date, last_modified_by, last_modified_date, master_facility_id)
 VALUES ('9999912', '2548311', 'GADNR', '2017', 'APPROVED', 'PASSED', 'user-id', current_timestamp, 'user-id', current_timestamp,'61');
INSERT INTO emissions_report(id, eis_program_id, program_system_code, year, status, validation_status, created_by, created_date, last_modified_by, last_modified_date, master_facility_id)
 VALUES ('9999913', '2548311', 'GADNR', '2016', 'APPROVED', 'PASSED', 'user-id', current_timestamp, 'user-id', current_timestamp,'61');

--FACILITY
INSERT INTO FACILITY_SITE (id, report_id, agency_facility_identifier, category_code, source_type_code, name, description, 
status_code, status_year,program_system_code, street_address, city, county_code, state_code, country_code, postal_code, latitude, longitude, 
 mailing_street_address, mailing_city, mailing_state_code, mailing_postal_code,  created_by, created_date, last_modified_by, last_modified_date) 
    VALUES ('9999991', '9999997', '14900001', 'UNK', '125', 'Ga Power Company - Plant Wansley', 'Electric Generation',
     'OP', '2018', 'GADNR', '1371 Liberty Church Road', 'Carrollton', '13149', '13' , '', '30116', '33.413100', '-85.033600', '173 Peachtree Rd', 'Fitzgerald', '13', '31750', 
    'user-id', current_timestamp, 'user-id', current_timestamp);

--FACILITY
INSERT INTO FACILITY_SITE (id, report_id, agency_facility_identifier, category_code, source_type_code, name, description, status_code, status_year,
 program_system_code, street_address, city, county_code, state_code, country_code, postal_code, latitude, longitude, 
 mailing_street_address, mailing_city, mailing_state_code, mailing_postal_code,  created_by, created_date, last_modified_by, last_modified_date) 
    VALUES ('9999992', '9999999', '10300003', 'HAP', '125', 'Ga Power Co Plt McIntosh', 'Steam Electric Generating Plant', 'OP', '2018', 
    'GADNR', '981 Old Augusta Road', 'Rincon', '13103', '13', '', '31326', '32.356700', '-81.169600', '1350 Tiarco Dr.', 'Dalton', '13', '30720', 
    'user-id', current_timestamp, 'user-id', current_timestamp);
    
--FACILITY
INSERT INTO FACILITY_SITE (id, report_id, agency_facility_identifier, category_code, source_type_code, name, description, status_code, status_year,
 program_system_code, street_address, city, county_code, state_code, country_code, postal_code, latitude, longitude, 
 mailing_street_address, mailing_city, mailing_state_code, mailing_postal_code,  created_by, created_date, last_modified_by, last_modified_date) 
    VALUES ('9999993', '9999912', '24700037', 'UNK', '133', 'Visy Paper Inc', 'Paperboard Manufacturing', 'OP', '2017', 'GADNR',
    '1800A Sarasota Parkway', 'Conyers', '13247', '13', '', '30013', '33.660270', '-83.988890', '1400 9th St', 'Brunswick', '13', '31520', 
    'user-id', current_timestamp, 'user-id', current_timestamp);
    
--FACILITY
INSERT INTO FACILITY_SITE (id, report_id, agency_facility_identifier, category_code, source_type_code, name, description, status_code, status_year,
 program_system_code, street_address, city, county_code, state_code, country_code, postal_code, latitude, longitude, 
 mailing_street_address, mailing_city, mailing_state_code, mailing_postal_code,  created_by, created_date, last_modified_by, last_modified_date) 
    VALUES ('9999994', '9999913', '24700037', 'UNK', '133', 'Visy Paper Inc', 'Paperboard Manufacturing', 'OP', '2017', 'GADNR',
    '1800A Sarasota Parkway', 'Conyers', '13247', '13', '', '30013', '33.660270', '-83.988890', '1400 9th St', 'Brunswick', '13', '31520', 
    'user-id', current_timestamp, 'user-id', current_timestamp);
    

--FACILITY SITE CONTACT
INSERT INTO FACILITY_SITE_CONTACT (id, facility_site_id, type, prefix, first_name, last_name, email, phone, phone_ext,
    street_address, city, state_code, country_code, postal_code, county_code, mailing_street_address, mailing_city, mailing_state_code, mailing_postal_code, 
    created_by, created_date, last_modified_by, last_modified_date) 
    VALUES ('9999991', '9999991', 'RO', '', 'John', 'Smith', 'johnsmith@example.com', '3193193119', '001',
    '173 Peachtree Rd', 'Fitzgerald', '13' , '', '31750', '13313', '173 Peachtree Rd', 'Fitzgerald', '13', '31750', 
    'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO FACILITY_SITE_CONTACT (id, facility_site_id, type, prefix, first_name, last_name, email, phone, phone_ext,
    street_address, city, state_code, country_code, postal_code, county_code, mailing_street_address, mailing_city, mailing_state_code, mailing_postal_code, 
    created_by, created_date, last_modified_by, last_modified_date) 
    VALUES ('9999992', '9999991', 'FAC', '', 'Jane', 'Doe', 'janedoe@example.com', '5555555555', '',
    '173 Peachtree Rd', 'Fitzgerald', '13' , '', '31750', '13313', '174 Peachtree Rd', 'Fitzgerald', '13', '31750', 
    'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO FACILITY_SITE_CONTACT (id, facility_site_id, type, prefix, first_name, last_name, email, phone, phone_ext,
    street_address, city, state_code, country_code, postal_code, county_code, mailing_street_address, mailing_city, mailing_state_code, mailing_postal_code, 
    created_by, created_date, last_modified_by, last_modified_date) 
    VALUES ('9999993', '9999991', 'TECH', '', 'Jane', 'Doe', 'janedoe@example.com', '5555555555', '',
    '173 Peachtree Rd', 'Fitzgerald', '13' , '', '31750', '13313', '174 Peachtree Rd', 'Fitzgerald', '13', '31750', 
    'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO FACILITY_SITE_CONTACT (id, facility_site_id, type, prefix, first_name, last_name, email, phone, phone_ext,
    street_address, city, state_code, country_code, postal_code, county_code, mailing_street_address, mailing_city, mailing_state_code, mailing_postal_code, 
    created_by, created_date, last_modified_by, last_modified_date) 
    VALUES ('9999994', '9999992', 'RO', '', 'Johnny', 'Bravo', 'testEmail@example.com', '1118765309', '',
    '1350 Tiarco Dr.', 'Dalton', '13' , '', '30720', '13313', '1350 Tiarco Dr.', 'Dalton', '13', '30720', 
    'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO FACILITY_SITE_CONTACT (id, facility_site_id, type, prefix, first_name, last_name, email, phone, phone_ext,
    street_address, city, state_code, country_code, postal_code, county_code, mailing_street_address, mailing_city, mailing_state_code, mailing_postal_code, 
    created_by, created_date, last_modified_by, last_modified_date) 
    VALUES ('9999995', '9999992', 'FAC', '', 'Danny', 'Welch', 'testEmail@example.com', '7062771300', '',
    '1350 Tiarco Dr.', 'Dalton', '13' , '', '30720', '13313', '1350 Tiarco Dr.', 'Dalton', '13', '30720', 
    'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO FACILITY_SITE_CONTACT (id, facility_site_id, type, prefix, first_name, last_name, email, phone, phone_ext,
    street_address, city, state_code, country_code, postal_code, county_code, mailing_street_address, mailing_city, mailing_state_code, mailing_postal_code, 
    created_by, created_date, last_modified_by, last_modified_date) 
    VALUES ('9999997', '9999993', 'RO', '', 'David', 'last namez', 'testEmail@example.com', '9122655780', '',
    '', '', '13' , '', '', null, '', '', null, '', 
    'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO FACILITY_SITE_CONTACT (id, facility_site_id, type, prefix, first_name, last_name, email, phone, phone_ext,
    street_address, city, state_code, country_code, postal_code, county_code, mailing_street_address, mailing_city, mailing_state_code, mailing_postal_code, 
    created_by, created_date, last_modified_by, last_modified_date) 
    VALUES ('9999998', '9999993', 'COMP', '', 'Jay', 'Wright', 'testEmail@example.com', '9122655780', '',
    '', '', '13' , '', '', null, '', '', null, '', 
    'user-id', current_timestamp, 'user-id', current_timestamp);   
INSERT INTO FACILITY_SITE_CONTACT (id, facility_site_id, type, prefix, first_name, last_name, email, phone, phone_ext,
    street_address, city, state_code, country_code, postal_code, county_code, mailing_street_address, mailing_city, mailing_state_code, mailing_postal_code, 
    created_by, created_date, last_modified_by, last_modified_date) 
    VALUES ('9999999', '9999993', 'TECH', '', 'Jill', 'Holmes', 'testEmail@example.com', '9127171768', '',
    '', '', '13' , '', '', null, '', '', null, '', 
    'user-id', current_timestamp, 'user-id', current_timestamp);
    
INSERT INTO FACILITY_SITE_CONTACT (id, facility_site_id, type, prefix, first_name, last_name, email, phone, phone_ext,
    street_address, city, state_code, country_code, postal_code, county_code, mailing_street_address, mailing_city, mailing_state_code, mailing_postal_code, 
    created_by, created_date, last_modified_by, last_modified_date) 
    VALUES ('9999980', '9999994', 'RO', '', 'David', 'last namez', 'testEmail@example.com', '9122655780', '',
    '', '', '13' , '', '', null, '', '', null, '', 
    'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO FACILITY_SITE_CONTACT (id, facility_site_id, type, prefix, first_name, last_name, email, phone, phone_ext,
    street_address, city, state_code, country_code, postal_code, county_code, mailing_street_address, mailing_city, mailing_state_code, mailing_postal_code, 
    created_by, created_date, last_modified_by, last_modified_date) 
    VALUES ('9999981', '9999994', 'COMP', '', 'Jay', 'Wright', 'testEmail@example.com', '9122655780', '',
    '', '', '13' , '', '', null, '', '', null, '', 
    'user-id', current_timestamp, 'user-id', current_timestamp);   
INSERT INTO FACILITY_SITE_CONTACT (id, facility_site_id, type, prefix, first_name, last_name, email, phone, phone_ext,
    street_address, city, state_code, country_code, postal_code, county_code, mailing_street_address, mailing_city, mailing_state_code, mailing_postal_code, 
    created_by, created_date, last_modified_by, last_modified_date) 
    VALUES ('9999982', '9999994', 'TECH', '', 'Jill', 'Holmes', 'testEmail@example.com', '9127171768', '',
    '', '', '13' , '', '', null, '', '', null, '', 
    'user-id', current_timestamp, 'user-id', current_timestamp);


--FACILITY NAICS
INSERT INTo facility_naics_xref(id, facility_site_id, naics_code, naics_code_type,  created_by, created_date, last_modified_by, last_modified_date)
    VALUES('9999991', '9999991', '4241', 'PRIMARY',  'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTo facility_naics_xref(id, facility_site_id, naics_code, naics_code_type,  created_by, created_date, last_modified_by, last_modified_date)
    VALUES('9999992', '9999991', '331420', 'SECONDARY',  'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTo facility_naics_xref(id, facility_site_id, naics_code, naics_code_type,  created_by, created_date, last_modified_by, last_modified_date)
    VALUES('9999993', '9999991', '311222', 'SECONDARY',  'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTo facility_naics_xref(id, facility_site_id, naics_code, naics_code_type,  created_by, created_date, last_modified_by, last_modified_date)
    VALUES('9999994', '9999992', '325180', 'PRIMARY',  'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTo facility_naics_xref(id, facility_site_id, naics_code, naics_code_type,  created_by, created_date, last_modified_by, last_modified_date)
    VALUES('9999995', '9999993', '322130', 'PRIMARY',  'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTo facility_naics_xref(id, facility_site_id, naics_code, naics_code_type,  created_by, created_date, last_modified_by, last_modified_date)
    VALUES('9999996', '9999994', '322130', 'PRIMARY',  'user-id', current_timestamp, 'user-id', current_timestamp);

--EMISSION UNITS
INSERT INTO EMISSIONS_UNIT (id, facility_site_id, unit_identifier, description, type_code, status_code, status_year, unit_measure_cd,
    created_by, created_date, last_modified_by, last_modified_date, comments, design_capacity)
    VALUES('9999991', '9999991', 'Boiler 001', 'Gas Boiler - Industrial Size', '206', 'OP', '1985', 'LB/DAY', 'user-id', 
        current_timestamp, 'user-id', current_timestamp, 'Sample Comments for Boiler 001', 647); 
INSERT INTO EMISSIONS_UNIT (id, facility_site_id, unit_identifier, description, type_code, status_code, status_year,  unit_measure_cd,
    created_by, created_date, last_modified_by, last_modified_date, comments, design_capacity)
    VALUES('9999992', '9999991', 'Boiler 002', 'Coal Boiler - Industrial Size', '100', 'OP', '1985', 'TON/HR', 'user-id', 
        current_timestamp, 'user-id', current_timestamp, 'Sample Comments for Boiler 002', 34); 
INSERT INTO EMISSIONS_UNIT (id, facility_site_id, unit_identifier, description, type_code, status_code, status_year,  unit_measure_cd,
    created_by, created_date, last_modified_by, last_modified_date, comments, design_capacity)
    VALUES('9999993', '9999991', 'Dryer 001', 'Big Dryer', '1252', 'OP', '1985', 'TON', 'user-id', 
        current_timestamp, 'user-id', current_timestamp, 'Sample Comments for Dryer 001', 72);
INSERT INTO EMISSIONS_UNIT (id, facility_site_id, unit_identifier, description, type_code, status_code, status_year,  unit_measure_cd,
    created_by, created_date, last_modified_by, last_modified_date, comments, design_capacity)
    VALUES('9999994', '9999992', 'PGM-530263', 'Heater in Boiler Room', '180', 'OP', '1977', 'MMBTU/HR', 'user-id', 
        current_timestamp, 'user-id', current_timestamp,  'Sample Comments for PGM-530263', 1);
INSERT INTO EMISSIONS_UNIT (id, facility_site_id, unit_identifier, description, type_code, status_code, status_year,  unit_measure_cd,
    created_by, created_date, last_modified_by, last_modified_date, comments, design_capacity)
    VALUES('9999995', '9999993', '36834613', '6PB (U706)', '100', 'OP', NULL, 'MMBTU/HR', 'user-id', 
        current_timestamp, 'user-id', current_timestamp,  'Sample Comments for PGM-530263', 353); 
INSERT INTO EMISSIONS_UNIT (id, facility_site_id, unit_identifier, description, type_code, status_code, status_year,  unit_measure_cd,
    created_by, created_date, last_modified_by, last_modified_date, comments, design_capacity)
    VALUES('9999996', '9999993', '36835713', 'PRIMARY INCINERATOR (R488)', '610', 'OP', NULL, NULL, 'user-id', 
        current_timestamp, 'user-id', current_timestamp,  '', null); 
INSERT INTO EMISSIONS_UNIT (id, facility_site_id, unit_identifier, description, type_code, status_code, status_year,  unit_measure_cd,
    created_by, created_date, last_modified_by, last_modified_date, comments, design_capacity)
    VALUES('9999997', '9999993', '36837113', '5LK GROUP (LG07)', '210', 'OP', NULL, 'TON/DAY', 'user-id', 
        current_timestamp, 'user-id', current_timestamp,  '', null); 
INSERT INTO EMISSIONS_UNIT (id, facility_site_id, unit_identifier, description, type_code, status_code, status_year,  unit_measure_cd,
    created_by, created_date, last_modified_by, last_modified_date, comments, design_capacity)
    VALUES('9999998', '9999993', '36837013', 'HARDWOOD WASHERS (P115-P117)', '690', 'OP', NULL, 'TON/DAY', 'user-id', 
        current_timestamp, 'user-id', current_timestamp,  '', 985); 
INSERT INTO EMISSIONS_UNIT (id, facility_site_id, unit_identifier, description, type_code, status_code, status_year,  unit_measure_cd,
    created_by, created_date, last_modified_by, last_modified_date, comments, design_capacity)
    VALUES('9999999', '9999993', '36834413', 'DIGESTER FILL EXHAUSTS--FUGATIVE', '690', 'OP', NULL, 'TON/DAY', 'user-id', 
        current_timestamp, 'user-id', current_timestamp,  '', 9000); 
INSERT INTO EMISSIONS_UNIT (id, facility_site_id, unit_identifier, description, type_code, status_code, status_year,  unit_measure_cd,
    created_by, created_date, last_modified_by, last_modified_date, comments, design_capacity)
    VALUES('9999980', '9999994', '36834613', '6PB (U706)', '100', 'OP', NULL, 'MMBTU/HR', 'user-id', 
        current_timestamp, 'user-id', current_timestamp,  'Sample Comments for PGM-530263', 353); 
INSERT INTO EMISSIONS_UNIT (id, facility_site_id, unit_identifier, description, type_code, status_code, status_year,  unit_measure_cd,
    created_by, created_date, last_modified_by, last_modified_date, comments, design_capacity)
    VALUES('9999981', '9999994', '36835713', 'PRIMARY INCINERATOR (R488)', '610', 'OP', NULL, NULL, 'user-id', 
        current_timestamp, 'user-id', current_timestamp,  '', null); 
INSERT INTO EMISSIONS_UNIT (id, facility_site_id, unit_identifier, description, type_code, status_code, status_year,  unit_measure_cd,
    created_by, created_date, last_modified_by, last_modified_date, comments, design_capacity)
    VALUES('9999982', '9999994', '36837113', '5LK GROUP (LG07)', '210', 'OP', NULL, 'TON/DAY', 'user-id', 
        current_timestamp, 'user-id', current_timestamp,  '', null); 
INSERT INTO EMISSIONS_UNIT (id, facility_site_id, unit_identifier, description, type_code, status_code, status_year,  unit_measure_cd,
    created_by, created_date, last_modified_by, last_modified_date, comments, design_capacity)
    VALUES('9999983', '9999994', '36837013', 'HARDWOOD WASHERS (P115-P117)', '690', 'OP', NULL, 'TON/DAY', 'user-id', 
        current_timestamp, 'user-id', current_timestamp,  '', 985); 
INSERT INTO EMISSIONS_UNIT (id, facility_site_id, unit_identifier, description, type_code, status_code, status_year,  unit_measure_cd,
    created_by, created_date, last_modified_by, last_modified_date, comments, design_capacity)
    VALUES('9999984', '9999994', '36834413', 'DIGESTER FILL EXHAUSTS--FUGATIVE', '690', 'OP', NULL, 'TON/DAY', 'user-id', 
        current_timestamp, 'user-id', current_timestamp,  '', 9000); 
        

--RELEASE POINTS
INSERT INTO RELEASE_POINT (id, facility_site_id, release_point_identifier, type_code, description, stack_height, stack_height_uom_code, stack_diameter, 
    stack_diameter_uom_code, exit_gas_velocity, exit_gas_velocity_uom_code, exit_gas_temperature, exit_gas_flow_rate, exit_gas_flow_uom_code, status_code, status_year, 
    fugitive_line_1_latitude, fugitive_line_1_longitude, fugitive_line_2_latitude, fugitive_line_2_longitude, latitude, longitude, 
    created_by, created_date, last_modified_by, last_modified_date, comments) 
    VALUES ('9999991', '9999991', 'Smokestack 1', '1', 'A big smokestack', 40, 'FT', 7, 'FT', 20, 'FPS', 75, 25, 'ACFS', 'OP', '1985', 
        '32.7490', '83.3880', '34.7490', '85.3880', '33.7490', '84.3880', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'Sample Comments for Smokestack 1');
INSERT INTO RELEASE_POINT (id, facility_site_id, release_point_identifier, type_code, description, stack_height, stack_height_uom_code, stack_diameter, 
    stack_diameter_uom_code, exit_gas_velocity, exit_gas_velocity_uom_code, exit_gas_temperature, exit_gas_flow_rate, exit_gas_flow_uom_code, status_code, status_year, latitude, longitude, 
    created_by, created_date, last_modified_by, last_modified_date, comments) 
    VALUES ('9999992', '9999991', 'Smokestack 2', '2', 'Another big smokestack', 7, 'FT', 14, 'FT', 154, 'FPS', 100, 35, 'ACFS', 'OP', '1985', '33.7490', '84.3880', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'Sample Comments for Smokestack 2');
INSERT INTO RELEASE_POINT (id, facility_site_id, release_point_identifier, type_code, description, stack_height, stack_height_uom_code, stack_diameter, 
    stack_diameter_uom_code, exit_gas_velocity, exit_gas_velocity_uom_code, exit_gas_temperature, exit_gas_flow_rate, exit_gas_flow_uom_code, status_code, status_year, latitude, longitude, 
    created_by, created_date, last_modified_by, last_modified_date, comments) 
    VALUES ('9999993', '9999991', 'Vent 1', '5', 'A big vent', 25, 'FT', 25, 'FT', 627, 'FPS', 123, 76, 'ACFS', 'OP', '1985', '33.7490', '84.3880', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'Sample Comments for Vent 1');
INSERT INTO RELEASE_POINT (id, facility_site_id, release_point_identifier, type_code, description, stack_height, stack_height_uom_code, stack_diameter, 
    stack_diameter_uom_code, exit_gas_velocity, exit_gas_velocity_uom_code, exit_gas_temperature, exit_gas_flow_rate, exit_gas_flow_uom_code, status_code, status_year, latitude, longitude, 
    created_by, created_date, last_modified_by, last_modified_date, comments) 
    VALUES ('9999994', '9999992', 'PGM-530264', '99', 'Fugitive Vent', 9, 'FT', 0.003, 'FT', 0.0003, 'FPS', 88, 400, 'ACFS', 'OP', '1985', '34.686640', '-84.992865', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'Sample Comments for PGM-530264');
INSERT INTO RELEASE_POINT (id, facility_site_id, release_point_identifier, type_code, description, stack_height, stack_height_uom_code, stack_diameter, 
    stack_diameter_uom_code, exit_gas_velocity, exit_gas_velocity_uom_code, exit_gas_temperature, exit_gas_flow_rate, exit_gas_flow_uom_code, status_code, status_year, latitude, longitude, 
    created_by, created_date, last_modified_by, last_modified_date, comments) 
    VALUES ('9999995', '9999992', 'PGM-530265', '1', 'Fugitive Two-Dimensional', 34, 'FT', 34, 'FT', 0, 'FPS', 212, 125, 'ACFS', 'OP', '1985', '34.68676', '-84.99323', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'Sample Comments for PGM-530265');
INSERT INTO RELEASE_POINT (id, facility_site_id, release_point_identifier, type_code, description, stack_height, stack_height_uom_code, stack_diameter, 
    stack_diameter_uom_code, exit_gas_velocity, exit_gas_velocity_uom_code, exit_gas_temperature, exit_gas_flow_rate, exit_gas_flow_uom_code, status_code, status_year, latitude, longitude, 
    created_by, created_date, last_modified_by, last_modified_date, comments) 
    VALUES ('9999996', '9999992', 'PGM-530266', '1', 'Fugitive Three-Dimensional', 8, 'FT', 65, 'FT', 0, 'FPS', 40, 867, 'ACFS', 'OP', '1985', '34.686515', '34.686397', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'Sample Comments for PGM-530266');
INSERT INTO RELEASE_POINT (id, facility_site_id, release_point_identifier, type_code, description, stack_height, stack_height_uom_code, stack_diameter, 
    stack_diameter_uom_code, exit_gas_velocity, exit_gas_velocity_uom_code, exit_gas_temperature, exit_gas_flow_rate, exit_gas_flow_uom_code, status_code, status_year, latitude, longitude, 
    created_by, created_date, last_modified_by, last_modified_date, comments) 
    VALUES ('9999997', '9999992', 'PGM-530267', '2', 'Vertical Stack', 8, 'FT', 7, 'FT', 6, 'FPS', 77, 32, 'ACFS', 'OP', '1985', '34.686397', '-84.992638', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'Sample Comments for PGM-530267');        
INSERT INTO RELEASE_POINT (id, facility_site_id, release_point_identifier, type_code, description, stack_height, stack_height_uom_code, stack_diameter, 
    stack_diameter_uom_code, exit_gas_velocity, exit_gas_velocity_uom_code, exit_gas_temperature, exit_gas_flow_rate, exit_gas_flow_uom_code, status_code, status_year, latitude, longitude, 
    created_by, created_date, last_modified_by, last_modified_date, comments) 
    VALUES ('99999980', '9999993', '35097812', '2', 'S2', 275, 'FT', 11, 'FT', 23, 'FPS', 315, 2184, 'ACFS', 'OP', NULL, '31.173460', '-81.521110', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'Vertical Release Point for Misc Processes');
INSERT INTO RELEASE_POINT (id, facility_site_id, release_point_identifier, type_code, description, stack_height, stack_height_uom_code, stack_diameter, 
    stack_diameter_uom_code, exit_gas_velocity, exit_gas_velocity_uom_code, exit_gas_temperature, exit_gas_flow_rate, exit_gas_flow_uom_code, status_code, status_year, latitude, longitude, 
    created_by, created_date, last_modified_by, last_modified_date, comments) 
    VALUES ('99999981', '9999993', '35099012', '2', 'S36', 270, 'FT', 2.3, 'FT', 39, 'FPS', 167, 161, 'ACFS', 'OP', NULL, '31.174020', '-81.519900', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'Vertical Release Point for Misc Processes');
INSERT INTO RELEASE_POINT (id, facility_site_id, release_point_identifier, type_code, description, stack_height, stack_height_uom_code, stack_diameter, 
    stack_diameter_uom_code, exit_gas_velocity, exit_gas_velocity_uom_code, exit_gas_temperature, exit_gas_flow_rate, exit_gas_flow_uom_code, status_code, status_year, latitude, longitude, 
    created_by, created_date, last_modified_by, last_modified_date, comments) 
    VALUES ('99999982', '9999993', '35099812', '2', 'S10', 228, 'FT', 5.5, 'FT', 58, 'FPS', 169, 1377, 'ACFS', 'OP', NULL, '31.173890', '-81.517120', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'S10');
INSERT INTO RELEASE_POINT (id, facility_site_id, release_point_identifier, type_code, description, stack_height, stack_height_uom_code, stack_diameter, 
    stack_diameter_uom_code, exit_gas_velocity, exit_gas_velocity_uom_code, exit_gas_temperature, exit_gas_flow_rate, exit_gas_flow_uom_code, status_code, status_year, latitude, longitude, 
    created_by, created_date, last_modified_by, last_modified_date, comments) 
    VALUES ('99999983', '9999993', '35099612', '2', 'S21', 100, 'FT', 4, 'FT', 60, 'FPS', 95, 753.6, 'ACFS', 'OP', NULL, '31.173260', '-81.519800', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'S21');
INSERT INTO RELEASE_POINT (id, facility_site_id, release_point_identifier, type_code, description, stack_height, stack_height_uom_code, stack_diameter, 
    stack_diameter_uom_code, exit_gas_velocity, exit_gas_velocity_uom_code, exit_gas_temperature, exit_gas_flow_rate, exit_gas_flow_uom_code, status_code, status_year, latitude, longitude, 
    created_by, created_date, last_modified_by, last_modified_date, comments) 
    VALUES ('99999984', '9999993', '35099612', '2', 'S16', 100, 'FT', 2, 'FT', 5, 'FPS', 125, 15.7, 'ACFS', 'OP', NULL, '31.173530', '-81.519880', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'S16');
INSERT INTO RELEASE_POINT (id, facility_site_id, release_point_identifier, type_code, description, stack_height, stack_height_uom_code, stack_diameter, 
    stack_diameter_uom_code, exit_gas_velocity, exit_gas_velocity_uom_code, exit_gas_temperature, exit_gas_flow_rate, exit_gas_flow_uom_code, status_code, status_year, latitude, longitude, 
    created_by, created_date, last_modified_by, last_modified_date, comments) 
    VALUES ('99999970', '9999994', '35097812', '2', 'S2', 275, 'FT', 11, 'FT', 23, 'FPS', 315, 2184, 'ACFS', 'OP', NULL, '31.173460', '-81.521110', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'Vertical Release Point for Misc Processes');
INSERT INTO RELEASE_POINT (id, facility_site_id, release_point_identifier, type_code, description, stack_height, stack_height_uom_code, stack_diameter, 
    stack_diameter_uom_code, exit_gas_velocity, exit_gas_velocity_uom_code, exit_gas_temperature, exit_gas_flow_rate, exit_gas_flow_uom_code, status_code, status_year, latitude, longitude, 
    created_by, created_date, last_modified_by, last_modified_date, comments) 
    VALUES ('99999971', '9999994', '35099012', '2', 'S36', 270, 'FT', 2.3, 'FT', 39, 'FPS', 167, 161, 'ACFS', 'OP', NULL, '31.174020', '-81.519900', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'Vertical Release Point for Misc Processes');
INSERT INTO RELEASE_POINT (id, facility_site_id, release_point_identifier, type_code, description, stack_height, stack_height_uom_code, stack_diameter, 
    stack_diameter_uom_code, exit_gas_velocity, exit_gas_velocity_uom_code, exit_gas_temperature, exit_gas_flow_rate, exit_gas_flow_uom_code, status_code, status_year, latitude, longitude, 
    created_by, created_date, last_modified_by, last_modified_date, comments) 
    VALUES ('99999972', '9999994', '35099812', '2', 'S10', 228, 'FT', 5.5, 'FT', 58, 'FPS', 169, 1377, 'ACFS', 'OP', NULL, '31.173890', '-81.517120', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'S10');
INSERT INTO RELEASE_POINT (id, facility_site_id, release_point_identifier, type_code, description, stack_height, stack_height_uom_code, stack_diameter, 
    stack_diameter_uom_code, exit_gas_velocity, exit_gas_velocity_uom_code, exit_gas_temperature, exit_gas_flow_rate, exit_gas_flow_uom_code, status_code, status_year, latitude, longitude, 
    created_by, created_date, last_modified_by, last_modified_date, comments) 
    VALUES ('99999973', '9999994', '35099612', '2', 'S21', 100, 'FT', 4, 'FT', 60, 'FPS', 95, 753.6, 'ACFS', 'OP', NULL, '31.173260', '-81.519800', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'S21');
INSERT INTO RELEASE_POINT (id, facility_site_id, release_point_identifier, type_code, description, stack_height, stack_height_uom_code, stack_diameter, 
    stack_diameter_uom_code, exit_gas_velocity, exit_gas_velocity_uom_code, exit_gas_temperature, exit_gas_flow_rate, exit_gas_flow_uom_code, status_code, status_year, latitude, longitude, 
    created_by, created_date, last_modified_by, last_modified_date, comments) 
    VALUES ('99999974', '9999994', '35099612', '2', 'S16', 100, 'FT', 2, 'FT', 5, 'FPS', 125, 15.7, 'ACFS', 'OP', NULL, '31.173530', '-81.519880', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'S16');

--EMISSION PROCESS
INSERT INTO EMISSIONS_PROCESS (id, emissions_unit_id, emissions_process_identifier, status_code, status_year, scc_code, scc_short_name, description, aircraft_engine_type_code,
    created_by, created_date, last_modified_by, last_modified_date, comments)
    VALUES ('9999991', '9999991', 'Drying Process', 'OP', '1985', '30700752', '', 'Softwood Veneer Dryer: Direct Natural Gas-fired: Heated Zones', '2258', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'Sample Comments for Drying Process');
INSERT INTO EMISSIONS_PROCESS (id, emissions_unit_id, emissions_process_identifier, status_code, status_year, scc_code, scc_short_name, description, 
    created_by, created_date, last_modified_by, last_modified_date, comments)
    VALUES ('9999992', '9999991', 'Disposal Process', 'OP', '1985', '50300106', '', 'Air Curtain Combustor: Wood', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'Sample Comments for Disposal Process');
INSERT INTO EMISSIONS_PROCESS (id, emissions_unit_id, emissions_process_identifier, status_code, status_year, scc_code, scc_short_name, description, 
    created_by, created_date, last_modified_by, last_modified_date, comments)
    VALUES ('9999993', '9999991', 'Storage Process', 'OP', '1985', '30700828', '', 'Chip Storage Piles: Hardwood', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'Sample Comments for Storage Process');
INSERT INTO EMISSIONS_PROCESS (id, emissions_unit_id, emissions_process_identifier, status_code, status_year, scc_code, scc_short_name, description, 
    created_by, created_date, last_modified_by, last_modified_date, comments)
    VALUES ('9999994', '9999994', 'NG Process Heater', 'OP', '1977', '30190003', '', 'Process Heater: Natural Gas', 
        'user-id', current_timestamp, 'user-id', current_timestamp, 'Example Comments for Natural Gas Process Heater');
INSERT INTO EMISSIONS_PROCESS (id, emissions_unit_id, emissions_process_identifier, status_code, status_year, scc_code, scc_short_name, description, 
    created_by, created_date, last_modified_by, last_modified_date, comments)
    VALUES ('9999995', '9999995', '46148814', 'OP', NULL, '39999999', '', '6PB ON NATURAL GAS', 
        'user-id', current_timestamp, 'user-id', current_timestamp, '');
INSERT INTO EMISSIONS_PROCESS (id, emissions_unit_id, emissions_process_identifier, status_code, status_year, scc_code, scc_short_name, description, 
    created_by, created_date, last_modified_by, last_modified_date, comments)
    VALUES ('9999996', '9999995', '46148914', 'OP', NULL, '39999999', '', '6PB ON NO. 2 FUEL OIL', 
        'user-id', current_timestamp, 'user-id', current_timestamp, '');
INSERT INTO EMISSIONS_PROCESS (id, emissions_unit_id, emissions_process_identifier, status_code, status_year, scc_code, scc_short_name, description, 
    created_by, created_date, last_modified_by, last_modified_date, comments)
    VALUES ('9999997', '9999996', '46147314', 'OP', NULL, '30700199', '', 'PRIMARY INCINERATOR (R488)', 
        'user-id', current_timestamp, 'user-id', current_timestamp, '');
INSERT INTO EMISSIONS_PROCESS (id, emissions_unit_id, emissions_process_identifier, status_code, status_year, scc_code, scc_short_name, description, 
    created_by, created_date, last_modified_by, last_modified_date, comments)
    VALUES ('9999998', '9999997', '46145914', 'OP', NULL, '30700106', '', '5LK GROUP (LG07)', 
        'user-id', current_timestamp, 'user-id', current_timestamp, '');
INSERT INTO EMISSIONS_PROCESS (id, emissions_unit_id, emissions_process_identifier, status_code, status_year, scc_code, scc_short_name, description, 
    created_by, created_date, last_modified_by, last_modified_date, comments)
    VALUES ('9999999', '9999998', '46146014', 'OP', NULL, '30700120', '', 'HARDWOOD WASHERS (P115-P117)', 
        'user-id', current_timestamp, 'user-id', current_timestamp, '');
INSERT INTO EMISSIONS_PROCESS (id, emissions_unit_id, emissions_process_identifier, status_code, status_year, scc_code, scc_short_name, description, 
    created_by, created_date, last_modified_by, last_modified_date, comments)
    VALUES ('9999910', '9999999', '46149114', 'OP', NULL, '30788801', '', 'DIGESTER FILL EXHAUSTS--FUGATIVE', 
        'user-id', current_timestamp, 'user-id', current_timestamp, '');
INSERT INTO EMISSIONS_PROCESS (id, emissions_unit_id, emissions_process_identifier, status_code, status_year, scc_code, scc_short_name, description, 
    created_by, created_date, last_modified_by, last_modified_date, comments)
    VALUES ('9999985', '9999980', '46148814', 'OP', NULL, '39999999', '', '6PB ON NATURAL GAS', 
        'user-id', current_timestamp, 'user-id', current_timestamp, '');
INSERT INTO EMISSIONS_PROCESS (id, emissions_unit_id, emissions_process_identifier, status_code, status_year, scc_code, scc_short_name, description, 
    created_by, created_date, last_modified_by, last_modified_date, comments)
    VALUES ('9999986', '9999980', '46148914', 'OP', NULL, '39999999', '', '6PB ON NO. 2 FUEL OIL', 
        'user-id', current_timestamp, 'user-id', current_timestamp, '');
INSERT INTO EMISSIONS_PROCESS (id, emissions_unit_id, emissions_process_identifier, status_code, status_year, scc_code, scc_short_name, description, 
    created_by, created_date, last_modified_by, last_modified_date, comments)
    VALUES ('9999987', '9999981', '46147314', 'OP', NULL, '30700199', '', 'PRIMARY INCINERATOR (R488)', 
        'user-id', current_timestamp, 'user-id', current_timestamp, '');
INSERT INTO EMISSIONS_PROCESS (id, emissions_unit_id, emissions_process_identifier, status_code, status_year, scc_code, scc_short_name, description, 
    created_by, created_date, last_modified_by, last_modified_date, comments)
    VALUES ('9999988', '9999982', '46145914', 'OP', NULL, '30700106', '', '5LK GROUP (LG07)', 
        'user-id', current_timestamp, 'user-id', current_timestamp, '');
INSERT INTO EMISSIONS_PROCESS (id, emissions_unit_id, emissions_process_identifier, status_code, status_year, scc_code, scc_short_name, description, 
    created_by, created_date, last_modified_by, last_modified_date, comments)
    VALUES ('9999989', '9999983', '46146014', 'OP', NULL, '30700120', '', 'HARDWOOD WASHERS (P115-P117)', 
        'user-id', current_timestamp, 'user-id', current_timestamp, '');
INSERT INTO EMISSIONS_PROCESS (id, emissions_unit_id, emissions_process_identifier, status_code, status_year, scc_code, scc_short_name, description, 
    created_by, created_date, last_modified_by, last_modified_date, comments)
    VALUES ('9999810', '9999984', '46149114', 'OP', NULL, '30788801', '', 'DIGESTER FILL EXHAUSTS--FUGATIVE', 
        'user-id', current_timestamp, 'user-id', current_timestamp, '');


--CONTROLS
INSERT INTO CONTROL (id, facility_site_id, status_code, identifier, description, percent_capture, percent_control, created_by, created_date, last_modified_by, last_modified_date, comments, control_measure_code)
    VALUES ('9999991', '9999991', 'OP', 'Control 001', 'Acetaldehyde and Benzene Control', 50, 50, 'user-id', current_timestamp, 'user-id', current_timestamp, 
    'Sample comments for Control 001', '1');
INSERT INTO CONTROL (id, facility_site_id, status_code, identifier, description, percent_capture, percent_control, created_by, created_date, last_modified_by, last_modified_date, comments, control_measure_code)
    VALUES ('9999992', '9999991', 'OP', 'Control 002', 'Acetaldehyde Control', 25, 75, 'user-id', current_timestamp, 'user-id', current_timestamp, 
    'Sample comments for Control 002', '2');

INSERT INTO control_pollutant(id, control_id, pollutant_code, created_by, created_date, last_modified_by, last_modified_date, percent_reduction)
    VALUES ('9999991', '9999991', '75070', 'user-id', current_timestamp, 'user-id', current_timestamp, 100.0);
INSERT INTO control_pollutant(id, control_id, pollutant_code, created_by, created_date, last_modified_by, last_modified_date, percent_reduction)
    VALUES ('9999992', '9999991', '71432', 'user-id', current_timestamp, 'user-id', current_timestamp, 99.9);
INSERT INTO control_pollutant(id, control_id, pollutant_code, created_by, created_date, last_modified_by, last_modified_date, percent_reduction)
    VALUES ('9999993', '9999992', '75070', 'user-id', current_timestamp, 'user-id', current_timestamp, 1.3);

INSERT INTO control_path (id, description, created_by, created_date, last_modified_by, last_modified_date, facility_site_id, path_id)
	VALUES ('9999991', 'Path Description', 'user-id', current_timestamp, 'user-id', current_timestamp, '9999991', 'Path A');
INSERT INTO control_path (id, description, created_by, created_date, last_modified_by, last_modified_date, facility_site_id, path_id)
	VALUES ('9999992', 'Path Description', 'user-id', current_timestamp, 'user-id', current_timestamp, '9999991', 'Path B');
INSERT INTO control_path (id, description, created_by, created_date, last_modified_by, last_modified_date, facility_site_id, path_id)
	VALUES ('9999993', 'Path Description', 'user-id', current_timestamp, 'user-id', current_timestamp, '9999991', 'Path C');
INSERT INTO control_path (id, description, created_by, created_date, last_modified_by, last_modified_date, facility_site_id, path_id)
	VALUES ('9999994', 'Path Description', 'user-id', current_timestamp, 'user-id', current_timestamp, '9999991', 'Path D');

INSERT INTO control_assignment (id, control_id, control_path_id, created_by, created_date, last_modified_by, last_modified_date, control_path_child_id, sequence_number, percent_apportionment)
	VALUES ('9999991', '9999991', '9999991', 'user-id', current_timestamp, 'user-id', current_timestamp, null, 1, 100);
INSERT INTO control_assignment (id, control_id, control_path_id, created_by, created_date, last_modified_by, last_modified_date, control_path_child_id, sequence_number, percent_apportionment)
	VALUES ('9999992', '9999991', '9999992', 'user-id', current_timestamp, 'user-id', current_timestamp, null, 1, 100);
INSERT INTO control_assignment (id, control_id, control_path_id, created_by, created_date, last_modified_by, last_modified_date, control_path_child_id, sequence_number, percent_apportionment)
	VALUES ('9999993', '9999991', '9999993', 'user-id', current_timestamp, 'user-id', current_timestamp, null, 1, 100);
INSERT INTO control_assignment (id, control_id, control_path_id, created_by, created_date, last_modified_by, last_modified_date, control_path_child_id, sequence_number, percent_apportionment)
	VALUES ('9999994', '9999992', '9999994', 'user-id', current_timestamp, 'user-id', current_timestamp, null, 1, 100);

--RELEASE POINT APPORTIONMENT
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date, control_path_id)
    VALUES ('9999991', '9999991', '9999991', '33', 'user-id', current_timestamp, 'user-id', current_timestamp, '9999991');
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date, control_path_id)
    VALUES ('9999992', '9999992', '9999991', '33', 'user-id', current_timestamp, 'user-id', current_timestamp, '9999992');
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date, control_path_id)
    VALUES ('9999993', '9999993', '9999991', '34', 'user-id', current_timestamp, 'user-id', current_timestamp, '9999993');
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date, control_path_id)
    VALUES ('9999994', '9999991', '9999992', '60', 'user-id', current_timestamp, 'user-id', current_timestamp, '9999994');
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date, control_path_id)
    VALUES ('9999995', '9999992', '9999992', '40', 'user-id', current_timestamp, 'user-id', current_timestamp, '9999991');
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date, control_path_id)
    VALUES ('9999996', '9999991', '9999993', '10', 'user-id', current_timestamp, 'user-id', current_timestamp, '9999992');
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date, control_path_id)
    VALUES ('9999997', '9999992', '9999993', '20', 'user-id', current_timestamp, 'user-id', current_timestamp, '9999993');
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date, control_path_id)
    VALUES ('9999998', '9999993', '9999993', '70', 'user-id', current_timestamp, 'user-id', current_timestamp, '9999994');
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999980', '9999994', '9999994', '25', 'user-id', current_timestamp, 'user-id', current_timestamp);
 INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999981', '9999995', '9999994', '25', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999982', '9999996', '9999994', '25', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999983', '9999997', '9999994', '25', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999984', '99999980', '9999996', '100', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999985', '99999980', '9999995', '100', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999986', '99999981', '9999997', '100', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999987', '99999982', '9999998', '100', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999988', '99999983', '9999999', '100', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999989', '99999984', '9999910', '100', 'user-id', current_timestamp, 'user-id', current_timestamp);
    
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999971', '99999970', '9999985', '100', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999972', '99999970', '9999986', '100', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999973', '99999971', '9999987', '100', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999974', '99999972', '9999988', '100', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999975', '99999973', '9999989', '100', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO RELEASE_POINT_APPT (id, release_point_id, emissions_process_id, percent, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999976', '99999974', '9999810', '100', 'user-id', current_timestamp, 'user-id', current_timestamp);

--REPORTING_PERIOD
INSERT INTO REPORTING_PERIOD (id, emissions_process_id, reporting_period_type_code, emissions_operating_type_code, calculation_parameter_type_code,
	calculation_parameter_value, calculation_parameter_uom, calculation_material_code, comments, created_by, created_date, last_modified_by, last_modified_date)
	VALUES ('9999991', '9999991', 'A', 'R', 'I', '351', 'LB', '698', 'Reporting Period Comment', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO REPORTING_PERIOD (id, emissions_process_id, reporting_period_type_code, emissions_operating_type_code, calculation_parameter_type_code,
	calculation_parameter_value, calculation_parameter_uom, calculation_material_code, comments, created_by, created_date, last_modified_by, last_modified_date)
	VALUES ('9999992', '9999992', 'A', 'R', 'O', '15876', 'TON', '70', 'Reporting Period Comment', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO REPORTING_PERIOD (id, emissions_process_id, reporting_period_type_code, emissions_operating_type_code, calculation_parameter_type_code,
	calculation_parameter_value, calculation_parameter_uom, calculation_material_code, comments, created_by, created_date, last_modified_by, last_modified_date)
	VALUES ('9999993', '9999993', 'A', 'R', 'E', '466', 'TON', '956', 'Reporting Period Comment', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO REPORTING_PERIOD (id, emissions_process_id, reporting_period_type_code, emissions_operating_type_code, calculation_parameter_type_code,
	calculation_parameter_value, calculation_parameter_uom, calculation_material_code, comments, created_by, created_date, last_modified_by, last_modified_date)
	VALUES ('9999994', '9999994', 'A', 'R', 'I', '35', 'E6FT3', '209', 'No comments for this reporting period', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO REPORTING_PERIOD (id, emissions_process_id, reporting_period_type_code, emissions_operating_type_code, calculation_parameter_type_code,
    calculation_parameter_value, calculation_parameter_uom, calculation_material_code, comments, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999995', '9999995', 'A', 'R', 'I', '79', 'E6FT3', '209', '', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO REPORTING_PERIOD (id, emissions_process_id, reporting_period_type_code, emissions_operating_type_code, calculation_parameter_type_code,
    calculation_parameter_value, calculation_parameter_uom, calculation_material_code, comments, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999996', '9999996', 'A', 'R', 'I', '515', 'E3GAL', '823', '', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO REPORTING_PERIOD (id, emissions_process_id, reporting_period_type_code, emissions_operating_type_code, calculation_parameter_type_code,
    calculation_parameter_value, calculation_parameter_uom, calculation_material_code, comments, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999997', '9999997', 'A', 'R', 'O', '210', 'TON', '971', '', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO REPORTING_PERIOD (id, emissions_process_id, reporting_period_type_code, emissions_operating_type_code, calculation_parameter_type_code,
    calculation_parameter_value, calculation_parameter_uom, calculation_material_code, comments, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999998', '9999998', 'A', 'R', 'O', '817', 'TON', '627', '', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO REPORTING_PERIOD (id, emissions_process_id, reporting_period_type_code, emissions_operating_type_code, calculation_parameter_type_code,
    calculation_parameter_value, calculation_parameter_uom, calculation_material_code, comments, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999999', '9999999', 'A', 'R', 'O', '817', 'TON', '627', '', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO REPORTING_PERIOD (id, emissions_process_id, reporting_period_type_code, emissions_operating_type_code, calculation_parameter_type_code,
    calculation_parameter_value, calculation_parameter_uom, calculation_material_code, comments, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999910', '9999910', 'A', 'R', 'O', '1876', 'TON', '248', '', 'user-id', current_timestamp, 'user-id', current_timestamp);

INSERT INTO REPORTING_PERIOD (id, emissions_process_id, reporting_period_type_code, emissions_operating_type_code, calculation_parameter_type_code,
    calculation_parameter_value, calculation_parameter_uom, calculation_material_code, comments, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999985', '9999985', 'A', 'R', 'I', '79', 'E6FT3', '209', '', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO REPORTING_PERIOD (id, emissions_process_id, reporting_period_type_code, emissions_operating_type_code, calculation_parameter_type_code,
    calculation_parameter_value, calculation_parameter_uom, calculation_material_code, comments, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999986', '9999986', 'A', 'R', 'I', '515', 'E3GAL', '823', '', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO REPORTING_PERIOD (id, emissions_process_id, reporting_period_type_code, emissions_operating_type_code, calculation_parameter_type_code,
    calculation_parameter_value, calculation_parameter_uom, calculation_material_code, comments, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999987', '9999987', 'A', 'R', 'O', '210', 'TON', '971', '', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO REPORTING_PERIOD (id, emissions_process_id, reporting_period_type_code, emissions_operating_type_code, calculation_parameter_type_code,
    calculation_parameter_value, calculation_parameter_uom, calculation_material_code, comments, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999988', '9999988', 'A', 'R', 'O', '817', 'TON', '627', '', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO REPORTING_PERIOD (id, emissions_process_id, reporting_period_type_code, emissions_operating_type_code, calculation_parameter_type_code,
    calculation_parameter_value, calculation_parameter_uom, calculation_material_code, comments, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999989', '9999989', 'A', 'R', 'O', '817', 'TON', '627', '', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO REPORTING_PERIOD (id, emissions_process_id, reporting_period_type_code, emissions_operating_type_code, calculation_parameter_type_code,
    calculation_parameter_value, calculation_parameter_uom, calculation_material_code, comments, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999810', '9999810', 'A', 'R', 'O', '1876', 'TON', '248', '', 'user-id', current_timestamp, 'user-id', current_timestamp);

--OPERATING DETAILS
INSERT INTO OPERATING_DETAIL (id, reporting_period_id, actual_hours_per_period, avg_hours_per_day, avg_days_per_week, avg_weeks_per_period, percent_winter, percent_spring, percent_summer, 
    percent_fall, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999991', '9999991', '8700', '24','7', '52', '25', '25', '25', '25', 'user-id', current_timestamp, 'user-id', current_timestamp); 
INSERT INTO OPERATING_DETAIL (id, reporting_period_id, actual_hours_per_period, avg_hours_per_day, avg_days_per_week, avg_weeks_per_period, percent_winter, percent_spring, percent_summer, 
    percent_fall, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999992', '9999992', '8700', '20','6','52', '10', '10', '50', '30', 'user-id', current_timestamp, 'user-id', current_timestamp); 
INSERT INTO OPERATING_DETAIL (id, reporting_period_id, actual_hours_per_period, avg_hours_per_day, avg_days_per_week, avg_weeks_per_period, percent_winter, percent_spring, percent_summer, 
    percent_fall, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999993', '9999993', '8700', '18','5', '52','45', '5', '25', '25', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO OPERATING_DETAIL (id, reporting_period_id, actual_hours_per_period, avg_hours_per_day, avg_days_per_week, avg_weeks_per_period, percent_winter, percent_spring, percent_summer, 
    percent_fall, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999994', '9999994', '8700', '23','6','52', '25', '25', '25', '25', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO OPERATING_DETAIL (id, reporting_period_id, actual_hours_per_period, avg_hours_per_day, avg_days_per_week, avg_weeks_per_period, percent_winter, percent_spring, percent_summer, 
    percent_fall, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999995', '9999995', '8621', '14','5','51', '25', '25', '25', '25', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO OPERATING_DETAIL (id, reporting_period_id, actual_hours_per_period, avg_hours_per_day, avg_days_per_week, avg_weeks_per_period, percent_winter, percent_spring, percent_summer, 
    percent_fall, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999996', '9999996', '7892', '12','5','48', '25', '25', '25', '25', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO OPERATING_DETAIL (id, reporting_period_id, actual_hours_per_period, avg_hours_per_day, avg_days_per_week, avg_weeks_per_period, percent_winter, percent_spring, percent_summer, 
    percent_fall, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999997', '9999997', '5876', '20','6','52', '24', '26', '26', '24', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO OPERATING_DETAIL (id, reporting_period_id, actual_hours_per_period, avg_hours_per_day, avg_days_per_week, avg_weeks_per_period, percent_winter, percent_spring, percent_summer, 
    percent_fall, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999998', '9999998', '8401', '22','6','52', '20', '40', '40', '20', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO OPERATING_DETAIL (id, reporting_period_id, actual_hours_per_period, avg_hours_per_day, avg_days_per_week, avg_weeks_per_period, percent_winter, percent_spring, percent_summer, 
    percent_fall, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999999', '9999999', '8401', '22','6','52', '22', '18', '50', '10', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO OPERATING_DETAIL (id, reporting_period_id, actual_hours_per_period, avg_hours_per_day, avg_days_per_week, avg_weeks_per_period, percent_winter, percent_spring, percent_summer, 
    percent_fall, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999910', '9999910', '8000', '24','7','51', '25', '25', '25', '25', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO OPERATING_DETAIL (id, reporting_period_id, actual_hours_per_period, avg_hours_per_day, avg_days_per_week, avg_weeks_per_period, percent_winter, percent_spring, percent_summer, 
    percent_fall, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999985', '9999985', '8621', '14','5','51', '25', '25', '25', '25', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO OPERATING_DETAIL (id, reporting_period_id, actual_hours_per_period, avg_hours_per_day, avg_days_per_week, avg_weeks_per_period, percent_winter, percent_spring, percent_summer, 
    percent_fall, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999986', '9999986', '7892', '12','5','48', '25', '25', '25', '25', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO OPERATING_DETAIL (id, reporting_period_id, actual_hours_per_period, avg_hours_per_day, avg_days_per_week, avg_weeks_per_period, percent_winter, percent_spring, percent_summer, 
    percent_fall, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999987', '9999987', '5876', '20','6','52', '24', '26', '26', '24', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO OPERATING_DETAIL (id, reporting_period_id, actual_hours_per_period, avg_hours_per_day, avg_days_per_week, avg_weeks_per_period, percent_winter, percent_spring, percent_summer, 
    percent_fall, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999988', '9999988', '8401', '22','6','52', '20', '40', '40', '20', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO OPERATING_DETAIL (id, reporting_period_id, actual_hours_per_period, avg_hours_per_day, avg_days_per_week, avg_weeks_per_period, percent_winter, percent_spring, percent_summer, 
    percent_fall, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999989', '9999989', '8401', '22','6','52', '22', '18', '50', '10', 'user-id', current_timestamp, 'user-id', current_timestamp);
INSERT INTO OPERATING_DETAIL (id, reporting_period_id, actual_hours_per_period, avg_hours_per_day, avg_days_per_week, avg_weeks_per_period, percent_winter, percent_spring, percent_summer, 
    percent_fall, created_by, created_date, last_modified_by, last_modified_date)
    VALUES ('9999810', '9999810', '8000', '24','7','51', '25', '25', '25', '25', 'user-id', current_timestamp, 'user-id', current_timestamp);


--EMISSIONS
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999991', '9999991', '75070', '1000', 'TON', '0.6200', '6.200E-2 Lb per 1000 Square Feet 3/8-inch Thick Veneer Produced', 
        'TON', 'E3FT2/HR', '1', 'user-id', current_timestamp, 'user-id', current_timestamp, 'Sample comments for Acetaldehyde', '1000');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999992', '9999991', '208968', '1007.75', 'TON', '0.5920', '5.900E-2 Lb per 1000 Square Feet 3/8-inch Thick Veneer Produced', 
        'TON', 'E3FT2/HR', '1', 'user-id', current_timestamp, 'user-id', current_timestamp, 'Sample comments for Acenaphthylene', '1007.75');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999993', '9999991', '71432', '2015.6', 'TON', '0.5700', '5.700E-3 Lb per 1000 Square Feet 3/8-inch Thick Veneer Produced', 
        'TON', 'E3FT2/HR', '1', 'user-id', current_timestamp, 'user-id', current_timestamp, 'Sample comments for Benzene', '2015.6');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999994', '9999994', 'CO', '217.56', 'TON', null, '', 
        null, null, '1', 'user-id', current_timestamp, 'user-id', current_timestamp, 'Sample comments for Carbon monoxide', '217.56');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999995', '9999994', 'VOC', '98', 'LB', '2.8', 'Million Cubic Feet Natural Gas Burned', 
        'LB', 'E6FT3', '8', 'user-id', current_timestamp, 'user-id', current_timestamp, 'Sample comments for Volatile Organic Compounds', '0.049');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999996', '9999994', 'SO2', '21', 'LB', '0.6', 'Million Cubic Feet Natural Gas Burned', 
        'LB', 'E6FT3', '8', 'user-id', current_timestamp, 'user-id', current_timestamp, 'Sample comments for Sulfur Oxides', '0.0105');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999997', '9999994', 'NOX', '4900', 'LB', '140', 'Million Cubic Feet Natural Gas Burned', 
        'LB', 'E6FT3', '8', 'user-id', current_timestamp, 'user-id', current_timestamp, 'Sample comments for Nitrogen Oxides', '2.222');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999980', '9999995', '129000', '0.395', 'LB', '0.005', 'Million Cubic Feet Natural Gas Burned', 
        'LB', 'E6FT3', '10', 'user-id', current_timestamp, 'user-id', current_timestamp, 'Comments for Pyrene', '0.00019');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999981', '9999995', 'CO', '2923', 'LB', '37', 'Million Cubic Feet Natural Gas Burned', 
        'LB', 'E6FT3', '11', 'user-id', current_timestamp, 'user-id', current_timestamp, 'Comments for Carbon Monoxide', '1.4615');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999982', '9999995', '7782492', '0.001896', 'LB', '0.000024', 'Million Cubic Feet Natural Gas Burned', 
        'LB', 'E6FT3', '12', 'user-id', current_timestamp, 'user-id', current_timestamp, 'Comments for Selenium', '0.000000948');      
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999983', '9999996', '7782492', '1.0815', 'LB', '0.0021', '1000 Gallons Distillate Oil (No. 1 & 2) Burned', 
        'LB', 'E3GAL', '10', 'user-id', current_timestamp, 'user-id', current_timestamp, 'Comments for Cadmium', '0.00054');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999984', '9999996', '7440439', '2.163', 'LB', '0.0042', '1000 Gallons Distillate Oil (No. 1 & 2) Burned', 
        'LB', 'E3GAL', '11', 'user-id', current_timestamp, 'user-id', current_timestamp, 'Comments for Selenium', '0.0010');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999985', '9999996', 'NH3', '721', 'LB', '1.4', '1000 Gallons Distillate Oil (No. 1 & 2) Burned', 
        'LB', 'E3GAL', '12', 'user-id', current_timestamp, 'user-id', current_timestamp, 'Comments for Ammonia', '0.3605');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999986', '9999997', '67663', '4.7187', 'LB', '0.02247', '', 
        'LB', 'TON', '8', 'user-id', current_timestamp, 'user-id', current_timestamp, '', '0.0023');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999987', '9999997', '67561', '990.927', 'LB', '9.501', '', 
        'LB', 'TON', '8', 'user-id', current_timestamp, 'user-id', current_timestamp, '', '0.4954');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999988', '9999997', '50000', '1.648', 'LB', '0.007849', '', 
        'LB', 'TON', '8', 'user-id', current_timestamp, 'user-id', current_timestamp, '', '0.0008');

INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999910', '9999998', 'PM25-FIL', '4820.3', 'LB', '5.9', '', 
        'LB', 'TON', '8', 'user-id', current_timestamp, 'user-id', current_timestamp, '', '2.410');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999911', '9999998', '7440473', '0.380', 'LB', '0.000466', '', 
        'LB', 'TON', '8', 'user-id', current_timestamp, 'user-id', current_timestamp, '', '0.00019');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999912', '9999998', '7440020', '0.105', 'LB', '0.000129', '', 
        'LB', 'TON', '8', 'user-id', current_timestamp, 'user-id', current_timestamp, '', '0.000052');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999913', '9999999', 'CO', '42.5', 'LB', NULL, '', 
        NULL, NULL, '1', 'user-id', current_timestamp, 'user-id', current_timestamp, '', '0.02125');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999914', '9999999', 'NH3', '2156', 'LB', NULL, '', 
        NULL, NULL, '3', 'user-id', current_timestamp, 'user-id', current_timestamp, '', '1.078');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999915', '9999910', '7440382', '366.12', 'LB', NULL, '', 
        NULL, NULL, '3', 'user-id', current_timestamp, 'user-id', current_timestamp, '', '0.183');
        
        
        
        
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999880', '9999985', '129000', '0.395', 'LB', '0.005', 'Million Cubic Feet Natural Gas Burned', 
        'LB', 'E6FT3', '10', 'user-id', current_timestamp, 'user-id', current_timestamp, 'Comments for Pyrene', '0.00019');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999881', '9999985', 'CO', '2923', 'LB', '37', 'Million Cubic Feet Natural Gas Burned', 
        'LB', 'E6FT3', '11', 'user-id', current_timestamp, 'user-id', current_timestamp, 'Comments for Carbon Monoxide', '1.4615');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999882', '9999985', '7782492', '0.001896', 'LB', '0.000024', 'Million Cubic Feet Natural Gas Burned', 
        'LB', 'E6FT3', '12', 'user-id', current_timestamp, 'user-id', current_timestamp, 'Comments for Selenium', '0.000000948');      
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999883', '9999986', '7782492', '1.0815', 'LB', '0.0021', '1000 Gallons Distillate Oil (No. 1 & 2) Burned', 
        'LB', 'E3GAL', '10', 'user-id', current_timestamp, 'user-id', current_timestamp, 'Comments for Cadmium', '0.00054');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999884', '9999986', '7440439', '2.163', 'LB', '0.0042', '1000 Gallons Distillate Oil (No. 1 & 2) Burned', 
        'LB', 'E3GAL', '11', 'user-id', current_timestamp, 'user-id', current_timestamp, 'Comments for Selenium', '0.0010');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999885', '9999986', 'NH3', '721', 'LB', '1.4', '1000 Gallons Distillate Oil (No. 1 & 2) Burned', 
        'LB', 'E3GAL', '12', 'user-id', current_timestamp, 'user-id', current_timestamp, 'Comments for Ammonia', '0.3605');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999886', '9999997', '67663', '4.7187', 'LB', '0.02247', '', 
        'LB', 'TON', '8', 'user-id', current_timestamp, 'user-id', current_timestamp, '', '0.0023');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999887', '9999987', '67561', '990.927', 'LB', '9.501', '', 
        'LB', 'TON', '8', 'user-id', current_timestamp, 'user-id', current_timestamp, '', '0.4954');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999888', '9999987', '50000', '1.648', 'LB', '0.007849', '', 
        'LB', 'TON', '8', 'user-id', current_timestamp, 'user-id', current_timestamp, '', '0.0008');

INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999810', '9999988', 'PM25-FIL', '4820.3', 'LB', '5.9', '', 
        'LB', 'TON', '8', 'user-id', current_timestamp, 'user-id', current_timestamp, '', '2.410');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999811', '9999988', '7440473', '0.380', 'LB', '0.000466', '', 
        'LB', 'TON', '8', 'user-id', current_timestamp, 'user-id', current_timestamp, '', '0.00019');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999812', '9999988', '7440020', '0.105', 'LB', '0.000129', '', 
        'LB', 'TON', '8', 'user-id', current_timestamp, 'user-id', current_timestamp, '', '0.000052');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999813', '9999989', 'CO', '42.5', 'LB', NULL, '', 
        NULL, NULL, '1', 'user-id', current_timestamp, 'user-id', current_timestamp, '', '0.02125');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999814', '9999989', 'NH3', '2156', 'LB', NULL, '', 
        NULL, NULL, '3', 'user-id', current_timestamp, 'user-id', current_timestamp, '', '1.078');
INSERT INTO EMISSION (id, reporting_period_id, pollutant_code, total_emissions, emissions_uom_code, emissions_factor, emissions_factor_text, 
    emissions_numerator_uom, emissions_denominator_uom, emissions_calc_method_code, created_by, created_date, last_modified_by, last_modified_date, comments,
    calculated_emissions_tons)
    VALUES ('9999815', '9999810', '7440382', '366.12', 'LB', NULL, '', 
        NULL, NULL, '3', 'user-id', current_timestamp, 'user-id', current_timestamp, '', '0.183');