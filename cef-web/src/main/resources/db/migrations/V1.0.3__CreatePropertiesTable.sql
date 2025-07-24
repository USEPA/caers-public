
CREATE TABLE admin_properties
(
  name character varying(64) PRIMARY KEY,
  value character varying(255)
);

insert into admin_properties (name, value) values ('emissions.tolerance.total.warning', '.01');
insert into admin_properties (name, value) values ('emissions.tolerance.total.error', '.05');
insert into admin_properties (name, value) values ('email.default', 'no-reply@example.com');
insert into admin_properties (name, value) values ('env.admins', 'USER1, USER2, NSER3');