CREATE INDEX "i_release_point_id_release_point_appt"
    on release_point_appt (release_point_id);
CREATE INDEX "i_name_slt_config"
    on slt_config (name);
CREATE INDEX "i_program_system_code_slt_config"
    on slt_config (program_system_code);