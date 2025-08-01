
CREATE TABLE slt_config
(
  id SERIAL PRIMARY KEY,
  name character varying(64),
  agency_code character varying(3),
  value character varying(2000),
  UNIQUE (name, agency_code)
);

insert into slt_config (name, agency_code, value) values ('slt-email', 'GA', 'placeholder@example.com');
insert into slt_config (name, agency_code, value) values ('slt-eis-user', 'GA', 'userexample');
insert into slt_config (name, agency_code, value) values ('slt-eis-program-code', 'GA', 'GADNR');
insert into slt_config (name, agency_code, value) values ('slt-email', 'DC', 'placeholder@example.com');
insert into slt_config (name, agency_code, value) values ('slt-eis-user', 'DC', 'userexample');
insert into slt_config (name, agency_code, value) values ('slt-eis-program-code', 'DC', 'DOEE');