UPDATE calculation_material_code
SET description = 'Petrochemical Feedstocks' -- change from Petroleum feedstocks
WHERE code = '241';

UPDATE calculation_material_code
SET description = 'Petroleum Coke' -- fix casing
WHERE code = '240';
