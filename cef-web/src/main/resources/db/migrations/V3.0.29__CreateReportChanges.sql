
CREATE TABLE report_change
(
    id bigserial NOT NULL PRIMARY KEY,
    report_id bigint NOT NULL,
    message character varying(2000) NOT NULL,
    field character varying(255) NOT NULL,
    type character varying(20) NOT NULL,
    details jsonb,
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    FOREIGN KEY (report_id) REFERENCES emissions_report (id)
);