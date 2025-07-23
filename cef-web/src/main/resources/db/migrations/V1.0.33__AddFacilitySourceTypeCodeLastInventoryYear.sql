-- Facility Source Type Code last_inventory_years
ALTER TABLE facility_source_type_code ADD COLUMN last_inventory_year integer;
update facility_source_type_code set last_inventory_year = '1990' where code = '128';
update facility_source_type_code set last_inventory_year = '2010' where code = '141';
update facility_source_type_code set last_inventory_year = '2010' where code = '155';
