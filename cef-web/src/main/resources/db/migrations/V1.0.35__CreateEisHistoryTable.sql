
CREATE TABLE eis_transaction_history
(
    id bigserial NOT NULL,
    agency_code varchar(3) NOT NULL,
    eis_sub_status varchar(32) NOT NULL,
    transaction_id varchar(80) NOT NULL,
    submitter_name varchar(255) NOT NULL,
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id)
);