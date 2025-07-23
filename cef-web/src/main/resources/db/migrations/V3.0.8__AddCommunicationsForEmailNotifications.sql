ALTER TABLE report_attachment ALTER COLUMN report_id DROP NOT NULL;
ALTER TABLE report_attachment RENAME TO attachment;

CREATE TABLE communication
(
    id bigserial NOT NULL,
    sender_name character varying(255) NOT NULL,
    subject character varying(255),
    recipient_email character varying,
    content character varying,
    program_system_code character varying(20),
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id)
);

ALTER TABLE attachment
	ADD COLUMN communication_id bigint,
    ADD CONSTRAINT communication_fkey FOREIGN KEY (communication_id)
        REFERENCES communication (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;
