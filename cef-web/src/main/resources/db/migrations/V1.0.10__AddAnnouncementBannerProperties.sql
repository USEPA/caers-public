
ALTER TABLE admin_properties 
    ALTER COLUMN value TYPE character varying(2000);
insert into admin_properties (name, value) values ('feature.announcement.enabled', 'false');
insert into admin_properties (name, value) values ('feature.announcement.text', null);
