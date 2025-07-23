-- update last_inventory_year for NAICS codes
UPDATE naics_code SET last_inventory_year = 2002 WHERE code IN (449, 455, 456, 457, 458, 513, 516);
