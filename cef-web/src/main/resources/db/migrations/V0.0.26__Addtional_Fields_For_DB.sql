   
ALTER TABLE facility_site_contact
    ADD COLUMN county character varying(43);
      
ALTER TABLE emission
    ALTER COLUMN emissions_factor DROP NOT NULL;

ALTER TABLE emission
    ALTER COLUMN emissions_factor_text DROP NOT NULL;

ALTER TABLE emission
    ADD COLUMN emissions_numerator_uom character varying(20);

ALTER TABLE emission
    ADD COLUMN emissions_denominator_uom character varying(20);
    
ALTER TABLE emission
    ADD COLUMN calculated_emissions_tons numeric; 
    
 ALTER TABLE emission
    ADD CONSTRAINT emission_numerator_uom_fkey FOREIGN KEY (emissions_numerator_uom)
    REFERENCES unit_measure_code (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;
    
 ALTER TABLE emission
    ADD CONSTRAINT emission_denominator_uom_fkey FOREIGN KEY (emissions_denominator_uom)
    REFERENCES unit_measure_code (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;
    
CREATE OR REPLACE VIEW public.vw_emissions_by_facility_and_cas
    WITH (security_barrier=false)
    AS
     SELECT concat(e.id, rpa.id) AS id,
    fs.frs_facility_id,
    fs.name AS facility_name,
    er.year,
    e.pollutant_name,
    e.pollutant_cas_id,
    e.total_emissions * rpa.percent * 0.01 AS apportioned_emissions,
    rpt.release_point_identifier,
        CASE
            WHEN rptc.description::text = 'Fugitive'::text THEN 'non-point'::text
            ELSE 'point'::text
        END AS release_point_type,
    e.emissions_uom_code
   FROM emission e,
    reporting_period rp,
    emissions_process ep,
    facility_site fs,
    emissions_unit eu,
    emissions_report er,
    release_point rpt,
    release_point_appt rpa,
    release_point_type_code rptc
  WHERE e.reporting_period_id = rp.id 
  AND rp.emissions_process_id = ep.id 
  AND ep.emissions_unit_id = eu.id 
  AND eu.facility_site_id = fs.id 
  AND fs.report_id = er.id 
  AND ep.id = rpa.emissions_process_id 
  AND rpa.release_point_id = rpt.id 
  AND rpt.type_code::text = rptc.code::text;