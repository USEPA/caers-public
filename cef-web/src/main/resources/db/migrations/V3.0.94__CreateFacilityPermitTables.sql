
CREATE TABLE IF NOT EXISTS facility_permit_type (
    code character varying(64) PRIMARY KEY,
    description CHARACTER VARYING(200) NOT NULL,
    type CHARACTER VARYING(20)
    );


insert into facility_permit_type (code, description, type) values ('FTV', 'Federal - Title V Operating Permit', 'Federal');
insert into facility_permit_type (code, description, type) values ('FNSR', 'Federal - New Source Review', 'Federal');
insert into facility_permit_type (code, description, type) values ('State', 'State', 'State');
insert into facility_permit_type (code, description, type) values ('SSN', 'State - Synthetic Minor', 'State');
insert into facility_permit_type (code, description, type) values ('SM', 'State - Minor', 'State');
insert into facility_permit_type (code, description, type) values ('OTHER', 'Other', 'Other');


CREATE TABLE IF NOT EXISTS facility_permit (
    id BIGSERIAL PRIMARY KEY,
    master_facility_id BIGSERIAL NOT NULL REFERENCES master_facility_record (id),
    permit_number CHARACTER VARYING(50) NOT NULL,
    permit_type CHARACTER VARYING(64) NOT NULL REFERENCES facility_permit_type (code),
    other_description CHARACTER VARYING(200),
    start_date date,
    end_date date,
    comments CHARACTER VARYING(2000),
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT now(),
    last_modified_by character varying(255) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT now()
);