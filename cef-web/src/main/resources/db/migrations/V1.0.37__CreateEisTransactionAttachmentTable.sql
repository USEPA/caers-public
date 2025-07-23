
CREATE TABLE eis_transaction_attachment
(
    id bigserial NOT NULL,
    eis_transaction_history_id bigint NOT NULL,
    file_name character varying(255) NOT NULL,
    attachment oid NOT NULL,
    file_type character varying(1000) NOT NULL,
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id),
    CONSTRAINT eis_transaction_history_fkey FOREIGN KEY (eis_transaction_history_id)
        REFERENCES eis_transaction_history (id) MATCH SIMPLE
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);