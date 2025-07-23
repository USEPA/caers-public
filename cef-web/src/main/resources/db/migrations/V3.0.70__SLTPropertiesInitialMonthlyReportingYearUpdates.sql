
INSERT INTO slt_properties (name, label, description, datatype) values (
'slt-feature.monthly-fuel-reporting.initialYear','Initial Monthly Reporting Year','The first report year where monthly reporting is permitted.','number');

ALTER TABLE slt_config
    ADD COLUMN can_edit boolean DEFAULT true;
