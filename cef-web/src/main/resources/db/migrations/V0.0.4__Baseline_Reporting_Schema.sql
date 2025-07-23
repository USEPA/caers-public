DROP TABLE emissions_report;
DROP TABLE facility;

--REFERENCE TABLES


ALTER TABLE agency
    ALTER COLUMN id TYPE integer ;


CREATE TABLE emissions_report
(
    id bigserial NOT NULL,
    frs_facility_id character varying(22) NOT NULL,
    eis_program_id character varying(22) NOT NULL,
    agency_code character varying(3) NOT NULL,
    year smallint NOT NULL,
    status character varying(255),
    validation_status character varying(255),
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT emissions_report_pkey PRIMARY KEY (id)
);

CREATE TABLE facility
(
    id bigserial NOT NULL,
    report_id bigint NOT NULL,
    frs_facility_id character varying(22),
    alt_site_identifier character varying(30),
    category_code character varying(20),
    source_type_code character varying(20),
    name character varying(80) NOT NULL,
    description character varying(100),
    status_code character varying(20) NOT NULL,
    status_year smallint NOT NULL,
    program_system_code character varying(20) NOT NULL,
    naics_code numeric(6) NOT NULL,
    street_address character varying(100) NOT NULL,
    city character varying(60) NOT NULL,
    county character varying(43),
    state_code character varying(5) NOT NULL,
    country_code character varying(10),
    postal_code character varying(10),
    latitude numeric(10, 6),
    longitude numeric(10, 6),
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT facility_pkey PRIMARY KEY (id),
    CONSTRAINT report_id_fkey FOREIGN KEY (report_id)
        REFERENCES emissions_report (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT category_code_fkey FOREIGN KEY (category_code)
        REFERENCES facility_category_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT source_code_fkey FOREIGN KEY (source_type_code)
        REFERENCES facility_source_type_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT status_code_fkey FOREIGN KEY (status_code)
        REFERENCES operating_status_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT program_system_code_fkey FOREIGN KEY (program_system_code)
        REFERENCES program_system_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT naics_code FOREIGN KEY (naics_code)
        REFERENCES naics_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE release_point
(
    id bigserial NOT NULL,
    facility_id bigint NOT NULL,
    release_point_identifier character varying(20) NOT NULL,
    program_system_code character varying(20) NOT NULL,
    type_code character varying(20) NOT NULL,
    description character varying(200) NOT NULL,
    stack_height numeric(6, 3),
    stack_height_uom_code character varying(20),
    stack_diameter numeric(6, 3),
    stack_diameter_uom_code character varying(20),
    exit_gas_velocity numeric(8, 3),
    exit_gas_velicity_uom_code character varying(20),
    exit_gas_temperature numeric(4),
    exit_gas_flow_rate numeric(16, 8),
    exit_gas_flow_uom_code character varying(20),
    status_code character varying(20) NOT NULL,
    status_year smallint,
    latitude numeric(10, 6) NOT NULL,
    longitude numeric(10, 6) NOT NULL,
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT release_point_pkey PRIMARY KEY (id),
    CONSTRAINT release_point_facility_fkey FOREIGN KEY (facility_id)
        REFERENCES facility (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT rp_program_sys_fkey FOREIGN KEY (program_system_code)
        REFERENCES program_system_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT rp_operating_status_fkey FOREIGN KEY (status_code)
        REFERENCES operating_status_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE emissions_unit
(
    id bigserial NOT NULL,
    facility_id bigint NOT NULL,
    unit_identifier character varying(20) NOT NULL,
    program_system_code character varying(20) NOT NULL,
    description character varying(100),
    type_code character varying(20) NOT NULL,
    type_code_description character varying(100) NOT NULL,
    status_code character varying(20) NOT NULL,
    status_year smallint NOT NULL,
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT emissions_unit_pkey PRIMARY KEY (id),
    CONSTRAINT unit_type_code_fkey FOREIGN KEY (type_code)
        REFERENCES unit_type_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT unit_status_code_fkey FOREIGN KEY (status_code)
        REFERENCES operating_status_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT unit_facility_fkey FOREIGN KEY (facility_id)
        REFERENCES facility (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE emissions_process
(
    id bigserial NOT NULL,
    emissions_unit_id bigint NOT NULL,
    emissions_process_identifier character varying(20) NOT NULL,
    status_code character varying(20) NOT NULL,
    status_year smallint NOT NULL,
    scc_code character varying(20) NOT NULL,
    scc_short_name character varying(100),
    description character varying(200),
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT emissions_process_pkey PRIMARY KEY (id),
    CONSTRAINT emissions_process_unit_fkey FOREIGN KEY (emissions_unit_id)
        REFERENCES emissions_unit (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT emissions_process_status_fkey FOREIGN KEY (status_code)
        REFERENCES operating_status_code (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE TABLE release_point_appt
(
    id bigserial NOT NULL,
    release_point_id bigint NOT NULL,
    emissions_process_id bigint NOT NULL,
    percent numeric(4, 1) NOT NULL,
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT release_point_appt_pkey PRIMARY KEY (id),
    CONSTRAINT release_pt_appt_rp_fkey FOREIGN KEY (release_point_id)
        REFERENCES release_point (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT release_pt_appt_ep_fkey FOREIGN KEY (emissions_process_id)
        REFERENCES emissions_process (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION        
);

CREATE TABLE reporting_period
(
    id bigserial NOT NULL,
    emissions_process_id bigint NOT NULL,
    reporting_period_type_code character varying(20) NOT NULL,
    emissions_operating_type_code character varying(20) NOT NULL,
    calculation_parameter_type_code character varying(20) NOT NULL,
    calculation_parameter_value numeric NOT NULL,
    calculation_parameter_uom character varying(20) NOT NULL,
    calculation_material_code character varying(20) NOT NULL,
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT reporting_period_pkey PRIMARY KEY (id),
    CONSTRAINT emissions_process_id FOREIGN KEY (emissions_process_id)
        REFERENCES emissions_process (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE operating_detail
(
    id bigserial NOT NULL,
    reporting_period_id bigint NOT NULL,
    avg_hours_per_period numeric(4),
    avg_hours_per_day numeric(3, 1),
    avg_days_per_week numeric(2, 1),
    avg_weeks_per_period numeric(2),
    percent_winter numeric(4, 1),
    percent_spring numeric(4, 1),
    percent_summer numeric(4, 1),
    percent_fall numeric(4, 1),
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT operating_detail_pkey PRIMARY KEY (id),
    CONSTRAINT operating_detail_reporting_fkey FOREIGN KEY (reporting_period_id)
        REFERENCES reporting_period (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE emission
(
    id bigserial NOT NULL,
    reporting_period_id bigint NOT NULL,
    pollutant_code character varying(20) NOT NULL,
    pollutant_name character varying(100) NOT NULL,
    total_emissions numeric(6) NOT NULL,
    emissions_uom_code character varying(20) NOT NULL,
    emissions_factor numeric NOT NULL,
    emissions_factor_text character varying(100) NOT NULL,
    emissions_calc_method_code character varying(20) NOT NULL,
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT emission_pkey PRIMARY KEY (id),
    CONSTRAINT emission_reporting_period_fkey FOREIGN KEY (reporting_period_id)
        REFERENCES reporting_period (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);