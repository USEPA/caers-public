ALTER TABLE reporting_period 
	ADD COLUMN fuel_use_value numeric,
	ADD COLUMN fuel_use_uom character varying(20),
	ADD COLUMN fuel_use_material_code character varying(20),
	ADD COLUMN heat_content_value numeric,
	ADD COLUMN heat_content_uom character varying(20),
	ADD CONSTRAINT fuel_use_uom_fkey FOREIGN KEY (fuel_use_uom)
        REFERENCES unit_measure_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    ADD CONSTRAINT heat_content_uom_fkey FOREIGN KEY (heat_content_uom)
	    REFERENCES unit_measure_code (code) MATCH SIMPLE
	    ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    ADD CONSTRAINT fuel_use_material_code_fkey FOREIGN KEY (fuel_use_material_code)
	    REFERENCES calculation_material_code (code) MATCH SIMPLE
	    ON UPDATE NO ACTION
        ON DELETE NO ACTION;

ALTER TABLE calculation_material_code ADD COLUMN fuel_use_material boolean NOT NULL DEFAULT false;
ALTER TABLE unit_measure_code ADD COLUMN fuel_use_uom boolean NOT NULL DEFAULT false;
ALTER TABLE unit_measure_code ADD COLUMN heat_content_uom boolean NOT NULL DEFAULT false;

UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '124';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '794';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '795';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '395';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '65';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '177';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '277';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '561';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '567';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '310';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '596';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '599';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '30';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '1';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '15';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '942';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '18';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '943';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '944';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '675';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '374';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '44';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '818';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '822';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '56';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '57';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '823';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '824';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '58';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '825';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '127';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '864';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '159';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '865';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '160';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '162';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '870';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '178';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '523';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '951';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '216';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '908';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '909';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '255';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '256';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '279';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '922';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '923';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '924';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '313';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '323';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '2';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '640';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '639';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '663';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '664';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '717';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '724';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '173';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '266';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '425';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '809';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '819';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '827';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '126';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '502';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '209';
UPDATE calculation_material_code SET fuel_use_material = true WHERE code = '251';

UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'BTU';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'E6BTU';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'THERM';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'BBL';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'BBL50GAL';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'E2BBL';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'E3BBL';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'E3BBL31G';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'E3GAL';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'E6GAL';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'GAL';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'E2LB';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'E2TON';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'E3LB';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'E3TON';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'E6LB';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'E6TON';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'LB';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'TON';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'E3FT3S';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'E6FT3S';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'FT3S';
UPDATE unit_measure_code SET fuel_use_uom = true WHERE code = 'FT3SD';

UPDATE unit_measure_code SET heat_content_uom = true WHERE code = 'BTU';
UPDATE unit_measure_code SET heat_content_uom = true WHERE code = 'E6BTU';
UPDATE unit_measure_code SET heat_content_uom = true WHERE code = 'THERM';

