
CREATE TABLE report_review
(
    id bigserial NOT NULL PRIMARY KEY,
    report_id bigint NOT NULL,
    version integer NOT NULL DEFAULT 0,
    status character varying(10) NOT NULL,
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    FOREIGN KEY (report_id) REFERENCES emissions_report (id)
);

CREATE TABLE reviewer_comment
(
    id bigserial NOT NULL PRIMARY KEY,
    report_review_id bigint NOT NULL,
    entity_id bigint NOT NULL,
    entity_type character varying(20) NOT NULL,
    message character varying(2000) NOT NULL,
    details jsonb,
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT NOW(),
    FOREIGN KEY (report_review_id) REFERENCES report_review (id)
);


ALTER TABLE report_history ADD COLUMN report_review_id bigint;

ALTER TABLE report_history
    ADD CONSTRAINT report_history_review_fkey FOREIGN KEY (report_review_id) REFERENCES report_review (id);
