   
ALTER TABLE calculation_method_code
    ADD COLUMN epa_emission_factor boolean NOT NULL DEFAULT false;

ALTER TABLE calculation_method_code ALTER COLUMN epa_emission_factor DROP DEFAULT;

ALTER TABLE calculation_method_code
    ADD COLUMN control_indicator boolean NOT NULL DEFAULT false;

ALTER TABLE calculation_method_code ALTER COLUMN control_indicator DROP DEFAULT;

update calculation_method_code set epa_emission_factor = true where code = '8';
update calculation_method_code set epa_emission_factor = true where code = '28';
update calculation_method_code set control_indicator = true where code = '28';
