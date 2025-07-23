
insert into admin_properties (name, value) values ('email.admin.enabled', 'true');
-- delete unused property
delete from admin_properties where name = 'env.admins';
