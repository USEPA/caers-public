ALTER TABLE control_assignment ADD COLUMN percent_apportionment numeric(4, 1) NOT NULL DEFAULT '100';
ALTER TABLE control_assignment ALTER COLUMN percent_apportionment DROP DEFAULT;