-- release point type code last_inventory_years
ALTER TABLE release_point_type_code ADD COLUMN last_inventory_year integer;
update release_point_type_code set last_inventory_year = '2002' where code = '99';
