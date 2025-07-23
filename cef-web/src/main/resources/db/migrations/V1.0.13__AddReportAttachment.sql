CREATE TABLE report_attachment
(
	id bigserial NOT NULL,
    report_id bigint NOT NULL,
    file_name character varying(255) NOT NULL,
    attachment oid NOT NULL,
    file_type character varying(1000) NOT NULL,
    comments character varying(200),
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id),
    CONSTRAINT emissions_report_fkey FOREIGN KEY (report_id)
        REFERENCES emissions_report (id) MATCH SIMPLE
        ON DELETE CASCADE
        ON UPDATE NO ACTION
);


ALTER TABLE report_history ADD COLUMN attachment_id bigint;
ALTER TABLE report_history ADD COLUMN user_role character varying(255);
ALTER TABLE report_history ADD COLUMN file_name character varying(50);
ALTER TABLE report_history ADD COLUMN file_deleted boolean DEFAULT false;


insert into admin_properties (name, value) values ('attachment.max-size', 100);