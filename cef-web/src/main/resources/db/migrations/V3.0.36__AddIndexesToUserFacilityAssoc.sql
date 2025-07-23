ALTER TABLE user_facility_association DROP CONSTRAINT user_facility_association_user_role_id_master_facility_id_key;

CREATE INDEX "i_user_role_id_active" ON user_facility_association (user_role_id,active);
CREATE INDEX "i_master_facility_id_active" ON user_facility_association (master_facility_id,active);
CREATE INDEX "i_program_system_code" ON master_facility_record (program_system_code);
