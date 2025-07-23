CREATE TABLE control_path_pollutant
(
    id bigserial NOT NULL,
    control_path_id bigint NOT NULL,
    pollutant_code character varying(12) NOT NULL,
    percent_reduction numeric(6, 3),
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT control_path_pollutant_pkey PRIMARY KEY (id),
    CONSTRAINT pollutant_control_path_fkey FOREIGN KEY (control_path_id)
        REFERENCES control_path (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT control_path_pollutant_pollutant_fkey FOREIGN KEY (pollutant_code)
	    REFERENCES pollutant (pollutant_code) MATCH SIMPLE
	    ON UPDATE NO ACTION
	    ON DELETE NO ACTION
);
