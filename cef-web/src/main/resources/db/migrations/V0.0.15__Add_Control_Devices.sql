CREATE TABLE control
(
    id bigserial NOT NULL,
    facility_site_id bigint NOT NULL,
    identifier character varying(20) NOT NULL,
    description character varying(200),
    percent_capture numeric(5, 1),
    percent_control numeric(5, 1),
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT control_pkey PRIMARY KEY (id),
    CONSTRAINT control_fac_site_fkey FOREIGN KEY (facility_site_id)
        REFERENCES facility_site (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE control_pollutant
(
    id bigserial NOT NULL,
    control_id bigint NOT NULL,
    pollutant_code character varying(20) NOT NULL,
    pollutant_name character varying(200) NOT NULL,
    pollutant_cas_id character varying(100),
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT control_pollutant_pkey PRIMARY KEY (id),
    CONSTRAINT pollutant_control_fkey FOREIGN KEY (control_id)
        REFERENCES control (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE control_path
(
    id bigserial NOT NULL,
    description character varying(200) NOT NULL,
    control_order character varying(3) NOT NULL,
    control_type character varying(10) NOT NULL,
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT control_path_pkey PRIMARY KEY (id),
    CONSTRAINT control_path_type_chk CHECK (control_type in ('Parallel', 'Serial'))
);

CREATE TABLE control_assignment
(
    id bigserial NOT NULL,
    control_id bigint NOT NULL,
    control_path_id bigint NOT NULL,
    release_point_id bigint,
    emissions_unit_id bigint,
    emissions_process_id bigint,
    description character varying(200),
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT control_assignment_pkey PRIMARY KEY (id),
    CONSTRAINT control_assignment_con_fkey FOREIGN KEY (control_id)
        REFERENCES control (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT control_assignment_con_path_fkey FOREIGN KEY (control_path_id)
        REFERENCES control_path (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT control_assignment_rp_fkey FOREIGN KEY (release_point_id)
        REFERENCES release_point (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT control_assignment_ep_fkey FOREIGN KEY (emissions_process_id)
        REFERENCES emissions_process (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT control_assignment_eu_fkey FOREIGN KEY (emissions_unit_id)
        REFERENCES emissions_unit (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

