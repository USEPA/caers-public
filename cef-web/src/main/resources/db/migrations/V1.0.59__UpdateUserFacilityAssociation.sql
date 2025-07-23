
truncate table user_facility_association;

ALTER TABLE user_facility_association
    ADD COLUMN cdx_user_id character varying(255) NOT NULL,
    ADD UNIQUE (user_role_id, master_facility_id);

insert into admin_properties (name, value) values ('feature.cdx-association-migration.enabled', 'true');