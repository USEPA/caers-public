-- Add missing pollutants and update inventory year for existing pollutant
UPDATE pollutant SET last_inventory_year = null WHERE pollutant_code = 'PM-FIL';

INSERT INTO pollutant(pollutant_code, pollutant_name, pollutant_cas_id, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code)
VALUES ('7664939', 'Sulfuric Acid Mist (Primary Aerosol)', '7664-93-9', '152405', 'OTH', 'LB');

INSERT INTO pollutant(pollutant_code, pollutant_name, pollutant_cas_id, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code)
VALUES ('10049044', 'Chlorine Dioxide', '10049-04-4', '167296', 'OTH', 'LB');

INSERT INTO pollutant(pollutant_code, pollutant_name, pollutant_cas_id, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code)
VALUES ('NMOC', 'Non-methane Organic Compounds', null, '', 'OTH', 'TON');

INSERT INTO pollutant(pollutant_code, pollutant_name, pollutant_cas_id, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code)
VALUES ('2699798', 'Sulfuryl fluoride', '2699-79-8', '106385', 'OTH', 'LB');
