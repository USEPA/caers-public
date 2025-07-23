
INSERT INTO slt_properties (name, label, description, datatype) values (
'slt-feature.monthly-fuel-reporting.enabled','Monthly Fuel Reporting','Enabling allows certifiers and preparers to enter Fuel Value on a monthly basis.','boolean');

INSERT INTO slt_properties (name, label, description, datatype) values (
'slt-feature.industry-facility-naics.enabled','SLT Facility NAICS Editing','Enabling allows SLTs to edit facility NAICS codes (prevents Certifiers and Preparers from editing).','boolean');
UPDATE slt_config SET name = 'slt-feature.industry-facility-naics.enabled' WHERE name = 'feature.industry-facility-naics.enabled';
DELETE FROM slt_properties WHERE name = 'feature.industry-facility-naics.enabled';

CREATE TABLE monthly_fuel_reporting
(
	id bigserial NOT NULL,
	reporting_period_id bigint NOT NULL,
    fuel_use_material_code character varying(20) NOT NULL,
    fuel_use_value numeric,
    fuel_use_uom character varying(20),
    total_operating_days numeric(3, 1),
    period character varying(20) NOT NULL,
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT monthly_fuel_reporting_pkey PRIMARY KEY (id),
    CONSTRAINT reporting_period_id FOREIGN KEY (reporting_period_id)
        REFERENCES reporting_period (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fuel_use_material_fkey FOREIGN KEY (fuel_use_material_code)
        REFERENCES calculation_material_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fuel_use_uom_fkey FOREIGN KEY (fuel_use_uom)
        REFERENCES unit_measure_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);