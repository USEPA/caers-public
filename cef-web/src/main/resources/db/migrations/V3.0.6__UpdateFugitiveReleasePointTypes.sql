ALTER TABLE release_point_type_code ADD COLUMN category character varying(20);
ALTER TABLE release_point RENAME COLUMN fugitive_line_2_latitude TO fugitive_mid_pt2_latitude;
ALTER TABLE release_point RENAME COLUMN fugitive_line_2_longitude TO fugitive_mid_pt2_longitude;
ALTER TABLE release_point DROP COLUMN fugitive_line_1_latitude;
ALTER TABLE release_point DROP COLUMN fugitive_line_1_longitude;

UPDATE release_point_type_code SET description = 'Fugitive Area', category = 'Fugitive' WHERE code = '1';
UPDATE release_point_type_code SET category = 'Stack' WHERE code = '2';
UPDATE release_point_type_code SET category = 'Stack' WHERE code = '3';
UPDATE release_point_type_code SET category = 'Stack' WHERE code = '4';
UPDATE release_point_type_code SET category = 'Stack' WHERE code = '5';
UPDATE release_point_type_code SET category = 'Stack' WHERE code = '6';
UPDATE release_point_type_code SET category = 'Stack' WHERE code = '99';

INSERT INTO release_point_type_code (code, description, category) VALUES ('7', 'Fugitive 3-D – source area', 'Fugitive');
INSERT INTO release_point_type_code (code, description, category) VALUES ('9', 'Fugitive 2-D – source area', 'Fugitive');
