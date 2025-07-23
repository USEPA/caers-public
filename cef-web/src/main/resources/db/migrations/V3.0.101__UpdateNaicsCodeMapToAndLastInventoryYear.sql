UPDATE naics_code
SET last_inventory_year = null
WHERE code = 221111;

UPDATE naics_code
SET map_to = null
WHERE map_to IS NOT null
AND last_inventory_year <= 2016;

-- These codes map to multiple codes in Census, so null map_to in short-term
UPDATE naics_code
SET map_to = null
WHERE map_to IS NOT null
AND last_inventory_year = 2021
AND code IN (
    515120,
    453998,
    517911,
    517312
)

