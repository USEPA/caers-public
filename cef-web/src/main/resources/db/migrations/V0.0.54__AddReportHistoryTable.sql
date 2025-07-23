CREATE TABLE report_history
(
    id bigserial NOT NULL,
    report_id bigint NOT NULL,
    action_date timestamp without time zone NOT NULL,
    action character varying(255) NOT NULL,
    user_id character varying(255) NOT NULL,
    user_full_name character varying(255) NOT NULL,
    comments character varying(2000),
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id),
    CONSTRAINT emissions_report_fkey FOREIGN KEY (report_id)
        REFERENCES emissions_report (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);