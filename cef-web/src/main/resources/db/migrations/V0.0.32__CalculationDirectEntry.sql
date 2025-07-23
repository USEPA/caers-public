   
ALTER TABLE calculation_method_code
    ADD COLUMN total_direct_entry boolean NOT NULL DEFAULT false;

ALTER TABLE calculation_method_code ALTER COLUMN total_direct_entry DROP DEFAULT;

update calculation_method_code set total_direct_entry = true where code = '1';
update calculation_method_code set total_direct_entry = true where code = '2';
update calculation_method_code set total_direct_entry = true where code = '3';
update calculation_method_code set total_direct_entry = true where code = '5';
update calculation_method_code set total_direct_entry = true where code = '6';
update calculation_method_code set total_direct_entry = true where code = '7';