
CREATE TABLE user_facility_association
(
  id BIGSERIAL PRIMARY KEY,
  master_facility_id bigint NOT NULL,
  user_role_id bigint NOT NULL,
  approved boolean NOT NULL default false,
  created_by character varying(255) NOT NULL,
  created_date timestamp without time zone NOT NULL DEFAULT now(),
  last_modified_by character varying(255) NOT NULL,
  last_modified_date timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT master_facility_id_fkey FOREIGN KEY (master_facility_id) 
    REFERENCES master_facility_record (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
);