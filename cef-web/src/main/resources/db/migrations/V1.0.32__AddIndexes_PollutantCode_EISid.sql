CREATE INDEX "i_emissions_report_eisProgramId" ON emissions_report USING btree (eis_program_id);

CREATE INDEX "i_emissions_pollutantCode" ON emission USING btree (pollutant_code);