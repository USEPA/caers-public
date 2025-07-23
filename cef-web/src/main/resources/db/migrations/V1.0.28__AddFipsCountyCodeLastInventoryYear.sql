-- FIPS county code last_inventory_years
ALTER TABLE fips_county ADD COLUMN last_inventory_year integer;
update fips_county set last_inventory_year = '1999' where county_code = '025' and state_code = '12';
update fips_county set last_inventory_year = '2008' where county_code = '201' and state_code = '02';
update fips_county set last_inventory_year = '1990' where county_code = '010' and state_code = '02';
update fips_county set last_inventory_year = '1990' where county_code = '131' and state_code = '46';
update fips_county set last_inventory_year = '1992' where county_code = '231' and state_code = '02';
update fips_county set last_inventory_year = '2007' where county_code = '232' and state_code = '02';
update fips_county set last_inventory_year = '2008' where county_code = '280' and state_code = '02';
update fips_county set last_inventory_year = '2013' where county_code = '515' and state_code = '51';
update fips_county set last_inventory_year = '2001' where county_code = '560' and state_code = '51';
update fips_county set last_inventory_year = '1995' where county_code = '780' and state_code = '51';
update fips_county set last_inventory_year = '1990' where county_code = '193' and state_code = '29';
update fips_county set last_inventory_year = '1986' where county_code = '140' and state_code = '02';
